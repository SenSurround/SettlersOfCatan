package settlersofcatan.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import settlersofcatan.client.GameApi.Container;
import settlersofcatan.client.GameApi.Operation;
import settlersofcatan.client.GameApi.SetTurn;
import settlersofcatan.client.GameApi.UpdateUI;

public class SettlersOfCatanPresenter {
    
    public interface View {
        /**
         * Sets the presenter. The viewer will call certain methods on the presenter, e.g.,
         * when a card is selected ({@link #cardSelected}),
         * when selection is done ({@link #finishedSelectingCards}), etc.
         *
         * The process of making a claim looks as follows to the viewer:
         * 1) The viewer calls {@link #cardSelected} a couple of times to select the cards to drop to
         * the middle pile
         * 2) The viewer calls {@link #finishedSelectingCards} to finalize his selection
         * 3) The viewer calls {@link #rankSelected} to pass the selected rank, which sends the claim.
         * The process of making a claim looks as follows to the presenter:
         * 1) The presenter calls {@link #chooseNextCard} and passes the current selection.
         * 2) The presenter calls {@link #chooseRankForClaim} and passes the possible ranks.
         */
        void setPresenter(SettlersOfCatanPresenter settlersOfCatanPresenter);

        /** Sets the state for a viewer, i.e., not one of the players. */
        void setViewerState(
                List<Hex> hexList,
                List<Node> nodeList,
                List<Path> pathList);

        /**
         * Sets the state for a player
         */
        void setPlayerState(
                List<Hex> hexList,
                List<Node> nodeList,
                List<Path> pathList,
                List<String> resourceCards,
                List<String> developmentCards,
                int victoryPoints,
                boolean hasLongestRoad,
                boolean hasLargestArmy);
        
        /**
         * Asks the player to make a move. This involves either choosing a settlement,
         * city, or road from his cache and placing it on the board where the rules allow
         * for, trading resources via a harbor trade, purchasing a Development Card from
         * the available stack, or playing one of the Development Cards in their hand.
         * A player can do any combination of these things so long as they have enough
         * Resource Cards to allow for it. When they are finished, they choose EndTurn
         * to move the turn to the next player
         */
        void makeMove(String moveType);


        /**
         * Asks the player to choose the cards needed to perform the harbor trade
         */
        void chooseCards(List<String> selectedCards, List<String> remainingCards);


        /**
         * Asks the player to choose the developement card to play
         */
        void chooseDevelopmentCard(String selectedCard);
        
        /**
         * Verifies the player wants to make a specific move
         */
        
        void twoStepValidation();
        
        
        /**
         * Ends the current player's turn and moves to the next player in the list
         */
        void endTurn(int nextPlayer);
      }
    
    private final SettlersOfCatanLogic settlersOfCatanLogic = new SettlersOfCatanLogic();
    private final View view;
    private final Container container;
    /** A viewer doesn't have a color. */
    private int myPlayer;
    private List<String> myResourceCards;
    private List<String> myDevelopmentCards;
    private Board theBoard;
    private int myVictoryPoints;
    private boolean hasLongestRoad;
    private boolean hasLargestArmy;
    private List<Integer> playerIds;
    private String selectedDevelopmentCard = "";
    private List<String> selectedResourceCards = new ArrayList<String>();
    private List<String> unSelectedResourceCards = new ArrayList<String>();
    private String harborTradePending = "";
    
    private int currentDevelopmentCard = 0;

    public SettlersOfCatanPresenter(View view, Container container) {
      this.view = view;
      this.container = container;
      view.setPresenter(this);
    }
    
    public void updateUI(UpdateUI updateUI)
    {
        playerIds = updateUI.getPlayerIds();
        int yourPlayerId = updateUI.getYourPlayerId();
        myPlayer = updateUI.getPlayerIndex(yourPlayerId);
        
        if(updateUI.getState().isEmpty())
        {
            // Blue must send the initial move
            if (myPlayer == 0)
            {
                sendInitialMove(playerIds);
            }
            
            return;
        }
        
        theBoard = new Board(updateUI.getState());
        
        if(updateUI.isViewer())
        {
            view.setViewerState(
                    theBoard.getHexList(),
                    theBoard.getNodeList(),
                    theBoard.getPathList());
            
            return;
        }
        if(updateUI.isAiPlayer())
        {
            return;
        }
        
        myResourceCards = settlersOfCatanLogic.getResourceCardsFromState(updateUI.getState(), settlersOfCatanLogic.getPlayerId(playerIds,  yourPlayerId));
        unSelectedResourceCards = new ArrayList<String>(myResourceCards);
        myDevelopmentCards = settlersOfCatanLogic.getDevelopmentCardsFromState(updateUI.getState());
        myVictoryPoints = settlersOfCatanLogic.getVictoryPointCount(updateUI.getState(), settlersOfCatanLogic.getPlayerId(playerIds,  yourPlayerId));
        hasLongestRoad = settlersOfCatanLogic.hasLongestRoad(updateUI.getState(), settlersOfCatanLogic.getPlayerId(playerIds,  yourPlayerId));
        hasLargestArmy = settlersOfCatanLogic.hasLargestArmy(updateUI.getState(), settlersOfCatanLogic.getPlayerId(playerIds,  yourPlayerId));
        
        view.setPlayerState(
                theBoard.getHexList(),
                theBoard.getNodeList(),
                theBoard.getPathList(),
                myResourceCards,
                myDevelopmentCards,
                myVictoryPoints,
                hasLongestRoad,
                hasLargestArmy);
    }

    private void sendInitialMove(List<Integer> playerIds)
    {
        container.sendMakeMove(settlersOfCatanLogic.getMoveInitial(playerIds));
    }
    
    private boolean hasResourcesForUse(String option)
    {
        boolean retVal = false;
        
        switch(option)
        {
        case Constants.BUILDSETTLEMENT:
            retVal = myResourceCards.contains(Constants.BRICK)
                  && myResourceCards.contains(Constants.LUMBER)
                  && myResourceCards.contains(Constants.WOOL)
                  && myResourceCards.contains(Constants.GRAIN);
            break;
        case Constants.BUILDCITY:
            int ore = 0;
            int grain = 0;
            for(int i = 0; i < myResourceCards.size(); i++)
            {
                if(myResourceCards.get(i).equals(Constants.ORE))
                    ore++;
                else if(myResourceCards.get(i).equals(Constants.GRAIN))
                    grain++;
            }
            
            retVal = ore > 2 && grain > 1;
            break;
        case Constants.BUILDROAD:
            retVal = myResourceCards.contains(Constants.BRICK)
                  && myResourceCards.contains(Constants.LUMBER);
            break;
        case Constants.BUYDEVELOPMENTCARD:
            retVal = myResourceCards.contains(Constants.ORE)
                  && myResourceCards.contains(Constants.WOOL)
                  && myResourceCards.contains(Constants.GRAIN);
            break;
        }
        
        return retVal;
    }
    
    public void selectSettlement()
    {
        if( theBoard.hasAvailableSettlements(myPlayer)
         && hasResourcesForUse(Constants.BUILDSETTLEMENT))
        {
            view.makeMove(Constants.BUILDSETTLEMENT);
        }
    }
    
    public void selectCity()
    {
        if( theBoard.hasAvailableCities(myPlayer)
         && hasResourcesForUse(Constants.BUILDCITY))
        {
            view.makeMove(Constants.BUILDCITY);
        }
    }
    
    public void selectRoad()
    {
        if( theBoard.hasAvailableRoads(myPlayer)
         && hasResourcesForUse(Constants.BUILDROAD))
        {
            view.makeMove(Constants.BUILDROAD);
        }
    }
    
    public void selectPurchaseDevelopmentCard()
    {
        if( currentDevelopmentCard < 25
         && hasResourcesForUse(Constants.BUYDEVELOPMENTCARD))
        {
            view.makeMove(Constants.BUYDEVELOPMENTCARD);
        }
    }
    
    public void selectPlayDevelopmentCard()
    {
        if(myDevelopmentCards.size() > 0)
        {
           view.makeMove(Constants.PLAYDEVELOPMENTCARD); 
           view.chooseDevelopmentCard(selectedDevelopmentCard);
        }
    }
    
    public void selectDevelopmentCard(int index)
    {
        selectedDevelopmentCard = myDevelopmentCards.get(index);
        view.chooseDevelopmentCard(selectedDevelopmentCard);
    }
    
    public void selectNormalHarborTrade()
    {
        harborTradePending = Constants.HARBORTRADE;
        view.chooseCards(new ArrayList<String>(selectedResourceCards), new ArrayList<String>(unSelectedResourceCards));
    }
    
    public void selectThreeToOneHarborTrade()
    {
        if(theBoard.ownsThreeToOnePort(myPlayer))
        {
            harborTradePending = Constants.THREETOONEHARBORTRADE;
            view.chooseCards(new ArrayList<String>(selectedResourceCards), new ArrayList<String>(unSelectedResourceCards));
        }
    }
    
    public void selectTwoToOneHarborTrade(String resource)
    {
        if(theBoard.ownsTwoToOnePort(myPlayer, resource))
        {
            harborTradePending = "TWOTOONE" + resource + "HARBORTRADE";
            view.chooseCards(new ArrayList<String>(selectedResourceCards), new ArrayList<String>(unSelectedResourceCards));
        }
    }
    
    public void selectResourceCard(int index)
    {
        int loc = unSelectedResourceCards.indexOf(myResourceCards.get(0));
        
        if( selectedResourceCards.isEmpty()
         || unSelectedResourceCards.get(loc).equals(selectedResourceCards.get(0)))
        {
            selectedResourceCards.add(unSelectedResourceCards.get(loc));
            unSelectedResourceCards.remove(loc);
            view.chooseCards(new ArrayList<String>(selectedResourceCards), new ArrayList<String>(unSelectedResourceCards));
            
            if( harborTradePending.equals(Constants.HARBORTRADE)
             && selectedResourceCards.size() == 4)
                view.makeMove(Constants.HARBORTRADE);
            else if( harborTradePending.equals(Constants.THREETOONEHARBORTRADE)
                  && selectedResourceCards.size() == 3)
                view.makeMove(Constants.THREETOONEHARBORTRADE);
            else if( harborTradePending.equals(Constants.TWOTOONEOREHARBORTRADE)
                  && selectedResourceCards.size() == 2)
                view.makeMove(Constants.TWOTOONEOREHARBORTRADE);
            else if( harborTradePending.equals(Constants.TWOTOONEGRAHARBORTRADE)
                  && selectedResourceCards.size() == 2)
                view.makeMove(Constants.TWOTOONEGRAHARBORTRADE);
            else if( harborTradePending.equals(Constants.TWOTOONELUMHARBORTRADE)
                  && selectedResourceCards.size() == 2)
                view.makeMove(Constants.TWOTOONELUMHARBORTRADE);
            else if( harborTradePending.equals(Constants.TWOTOONEWOOHARBORTRADE)
                  && selectedResourceCards.size() == 2)
                view.makeMove(Constants.TWOTOONEWOOHARBORTRADE);
            else if( harborTradePending.equals(Constants.TWOTOONEBRIHARBORTRADE)
                  && selectedResourceCards.size() == 2)
                view.makeMove(Constants.TWOTOONEBRIHARBORTRADE);
        }
    }
    
    public void verifyTwoStep()
    {
        if( harborTradePending.equals("")
         || (harborTradePending.equals(Constants.HARBORTRADE) && harborTradeCheck(4, ""))
         || (harborTradePending.equals(Constants.THREETOONEHARBORTRADE) && harborTradeCheck(3, ""))
         || (harborTradePending.equals(Constants.TWOTOONEOREHARBORTRADE) && harborTradeCheck(2, Constants.ORE))
         || (harborTradePending.equals(Constants.TWOTOONEGRAHARBORTRADE) && harborTradeCheck(2, Constants.GRAIN))
         || (harborTradePending.equals(Constants.TWOTOONELUMHARBORTRADE) && harborTradeCheck(2, Constants.LUMBER))
         || (harborTradePending.equals(Constants.TWOTOONEWOOHARBORTRADE) && harborTradeCheck(2, Constants.WOOL))
         || (harborTradePending.equals(Constants.TWOTOONEBRIHARBORTRADE) && harborTradeCheck(2, Constants.BRICK)))
        {
            view.twoStepValidation();
        }
        
        harborTradePending = "";
    }
    
    private boolean harborTradeCheck(int count, String resource)
    {
        boolean retVal = true;
        
        String resourceToCheck = "";
        
        if(!resource.equals(""))
            resourceToCheck = resource;
        else if(!selectedResourceCards.isEmpty())
            resourceToCheck = selectedResourceCards.get(0);
        else
            retVal = false;
        
        if(retVal && (count == selectedResourceCards.size()))
        {
            for(int i = 0; i < count; i++)
            {
                retVal = retVal && selectedResourceCards.get(i).equals(resourceToCheck);
            }
        }
        
        return retVal;
    }
    
    public void endTurn()
    {
        int nextPlayer = (myPlayer + 1) % playerIds.size();
        
        view.endTurn(nextPlayer);
    }
    
    public Board getBoard()
    {
        return theBoard;
    }
    
    public void setPathToBuild(int path)
    {
        
    }
    
    public void setNodeToBuild(int path)
    {
        
    }
}
