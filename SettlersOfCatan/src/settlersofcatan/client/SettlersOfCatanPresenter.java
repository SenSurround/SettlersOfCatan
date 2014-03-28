package settlersofcatan.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import settlersofcatan.client.GameApi.Container;
import settlersofcatan.client.GameApi.Delete;
import settlersofcatan.client.GameApi.EndGame;
import settlersofcatan.client.GameApi.Set;
import settlersofcatan.client.GameApi.SetVisibility;
import settlersofcatan.client.GameApi.Operation;
import settlersofcatan.client.GameApi.SetTurn;
import settlersofcatan.client.GameApi.SetRandomInteger;
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
                boolean hasLargestArmy,
                boolean myTurn,
                String infoMessage);
        
        /**
         * Asks the player to make a move. This involves either choosing a settlement,
         * city, or road from his cache and placing it on the board where the rules allow
         * for, trading resources via a harbor trade, purchasing a Development Card from
         * the available stack, or playing one of the Development Cards in their hand.
         * A player can do any combination of these things so long as they have enough
         * Resource Cards to allow for it. When they are finished, they choose EndTurn
         * to move the turn to the next player
         */
        void makeMove(String moveType, List<Operation> move);


        /**
         * Asks the player to choose the cards needed to perform the harbor trade
         */
        void chooseCards(List<String> selectedCards, List<String> remainingCards);


        /**
         * Asks the player to choose the developement card to play
         */
        void chooseDevelopmentCard(String selectedCard);
        
        
        /**
         * Ends the current player's turn and moves to the next player in the list
         */
        void endTurn(int nextPlayer);
        
        
        
        void handleDiceRoll(int roll1, int roll2);
      }
    
    private final SettlersOfCatanLogic settlersOfCatanLogic = new SettlersOfCatanLogic();
    private final View view;
    private final Container container;
    /** A viewer doesn't have a color. */
    public int myPlayer;
    private List<String> myResourceCards;
    private List<String> myDevelopmentCards;
    private Board theBoard;
    private Map<String, Object> theState;
    private int myVictoryPoints;
    private boolean hasLongestRoad;
    private boolean hasLargestArmy;
    private List<Integer> playerIds;
    private List<String> selectedResourceCards = new ArrayList<String>();
    private List<String> unSelectedResourceCards = new ArrayList<String>();
    private String storedResourceOut = "";
    private String storedResourceIn = "";
    public boolean lookingForCity = false;
    private String pathAdding = "";
    private String savedRandomCard;
    
    public int currentPlayer = -1;
    public String infoMessage = "";
    
    private int robberDestination;
    
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
                currentPlayer = 0;
                infoMessage = Constants.MAKEFIRSTFREEMOVESETTLEMENT;
                sendInitialMove(playerIds);
            }
            
            return;
        }
        
        theBoard = new Board(updateUI.getState());
        theState = updateUI.getState();
        
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
        
        if(isDiceRoll(theState))
        {
            view.handleDiceRoll(
                    Integer.parseInt(theState.get(Constants.DIE0).toString()),
                    Integer.parseInt(theState.get(Constants.DIE1).toString()));
            
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
                hasLargestArmy,
                currentPlayer == myPlayer,
                infoMessage);
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
        case Constants.BUILDSETTLEMENTPT1:
        case Constants.BUILDSETTLEMENTPT2:
            retVal = myResourceCards.contains(Constants.BRICK)
                  && myResourceCards.contains(Constants.LUMBER)
                  && myResourceCards.contains(Constants.WOOL)
                  && myResourceCards.contains(Constants.GRAIN);
            break;
        case Constants.BUILDCITYPT1:
        case Constants.BUILDCITYPT2:
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
        case Constants.BUILDROADPT1:
        case Constants.BUILDROADPT2:
            retVal = myResourceCards.contains(Constants.BRICK)
                  && myResourceCards.contains(Constants.LUMBER);
            break;
        case Constants.BUYDEVELOPMENTCARDPT1:
        case Constants.BUYDEVELOPMENTCARDPT2:
            retVal = myResourceCards.contains(Constants.ORE)
                  && myResourceCards.contains(Constants.WOOL)
                  && myResourceCards.contains(Constants.GRAIN);
            break;
        }
        
        return retVal;
    }
    
    public boolean canBuildSettlement()
    {
        boolean status = false;
        
        if( theBoard.hasAvailableSettlements(myPlayer)
         && hasResourcesForUse(Constants.BUILDSETTLEMENTPT1))
        {
            status = true;
        }
        
        return status;
    }
    
    public boolean canBuildCity()
    {
        boolean status = false;
        
        if( theBoard.hasAvailableCities(myPlayer)
         && hasResourcesForUse(Constants.BUILDCITYPT1)
         && theBoard.hasOneSettlementOut(myPlayer))
        {
            status = true;
        }
        
        return status;
    }
    
    public boolean canBuildRoad()
    {
        boolean status = false;
        
        if( theBoard.hasAvailableRoads(myPlayer)
         && hasResourcesForUse(Constants.BUILDROADPT1))
        {
            status = true;
        }
        
        return status;
    }
    
    public boolean canBuyDevelopmentCard()
    {
        boolean status = false;
        
        if( currentDevelopmentCard < 25
         && hasResourcesForUse(Constants.BUYDEVELOPMENTCARDPT1))
        {
            status = true;
        }
        
        return status;
    }
    
    public boolean canHarborTrade(String resource)
    {
        boolean status = false;
        
        int count = 0;
        
        for(int i = 0; i < myResourceCards.size(); i++)
        {
            if(myResourceCards.get(i).equals(resource))
            {
                count++;
            }
        }
        
        if(count >= 4)
        {
            status = true;
        }
        else if(count >= 3 && theBoard.ownsThreeToOnePort(myPlayer))
        {
            status = true;
        }
        else if(count >= 2 && theBoard.ownsTwoToOnePort(myPlayer, resource))
        {
            status = true;
        }
        
        return status;
    }
    
    public boolean canPlayDevelopmentCard()
    {
        boolean status = false;
        for(int i = 0; i < myDevelopmentCards.size(); i++)
        {
            if(myDevelopmentCards.get(i).equals(Constants.DEVELOPMENTCARDTYPEDEF00)
            || myDevelopmentCards.get(i).equals(Constants.DEVELOPMENTCARDTYPEDEF01)
            || myDevelopmentCards.get(i).equals(Constants.DEVELOPMENTCARDTYPEDEF02)
            || myDevelopmentCards.get(i).equals(Constants.DEVELOPMENTCARDTYPEDEF03))
            {
                status = true;
            }
        }
        
        return status;
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
        }
    }
    
    public Board getBoard()
    {
        return theBoard;
    }
    
    public String setRobber(int hex)
    {
        robberDestination = hex;
        infoMessage = Constants.MOVEROBBERPT2;
        return Constants.MOVEROBBERPT2 + theBoard.addWhoToRob(hex, myPlayer, theState);
    }
    
    public void setPathToBuild(int path)
    {
        String pathString = "";
        String playerString = "";
        
        if(path < 10)
            pathString = Constants.PATHTOKEN + "0" + path;
        else
            pathString = Constants.PATHTOKEN + path;
        
        if(myPlayer == 0)
            playerString = Constants.PB;
        else if(myPlayer == 1)
            playerString = Constants.PR;
        else if(myPlayer == 2)
            playerString = Constants.PY;
        else if(myPlayer == 3)
            playerString = Constants.PG;
        
        if(infoMessage == Constants.MAKEFIRSTFREEMOVEROAD)
        {
            if(settlersOfCatanLogic.canAddRoadHere(theState, pathString, playerString))
            {
                List<Operation> addSettlementFirstMove = new ArrayList<Operation>();
                addSettlementFirstMove.add(new SetTurn(playerIds.get(currentPlayer)));
                addSettlementFirstMove.add(new Set(pathString, Constants.ROADTOKEN + "00" + playerString));
                addSettlementFirstMove.add(new Set(Constants.ROADTOKEN + "00" + playerString, pathString));
                if(currentPlayer != playerIds.size() - 1)
                {
                    currentPlayer = (currentPlayer + 1) % playerIds.size();
                    infoMessage = Constants.MAKEFIRSTFREEMOVESETTLEMENT;
                }
                else
                {
                    infoMessage = Constants.MAKESECONDFREEMOVESETTLEMENT;
                }
                container.sendMakeMove(addSettlementFirstMove);
            }
        }
        else if(infoMessage == Constants.MAKESECONDFREEMOVEROAD)
        {
            if(settlersOfCatanLogic.canAddRoadHere(theState, pathString, playerString))
            {
                List<Operation> addSettlementFirstMove = new ArrayList<Operation>();
                addSettlementFirstMove.add(new SetTurn(playerIds.get(currentPlayer)));
                addSettlementFirstMove.add(new Set(pathString, Constants.ROADTOKEN + "01" + playerString));
                addSettlementFirstMove.add(new Set(Constants.ROADTOKEN + "01" + playerString, pathString));
                if(currentPlayer != 0)
                {
                    currentPlayer = (currentPlayer - 1) % playerIds.size();
                    infoMessage = Constants.MAKESECONDFREEMOVESETTLEMENT;
                }
                else
                {
                    infoMessage = Constants.ROLLDICE;
                }
                container.sendMakeMove(addSettlementFirstMove);
            }
        }
        else if(infoMessage.equals(Constants.MAKEMOVE))
        {
            if(settlersOfCatanLogic.canAddRoadHere(theState, pathString, playerString))
            {
                List<Operation> addRoad = new ArrayList<Operation>();
                addRoad.add(new SetTurn(playerIds.get(currentPlayer)));
                addRoad.add(new Set(pathString, getFirstAvailableRoad()));
                addRoad.add(new Set(getFirstAvailableRoad(), pathString));
                addRoad.add(new SetVisibility(getFirstAvailableResource(Constants.BRICK, 0)));
                addRoad.add(new SetVisibility(getFirstAvailableResource(Constants.LUMBER, 0)));
                
                pathAdding = pathString;
                infoMessage = Constants.BUILDROADPT1;
                container.sendMakeMove(addRoad);
            }
        }
    }
    
    public void finishRoadBuild()
    {
        List<Operation> addRoad = new ArrayList<Operation>();
        addRoad.add(new Delete(getFirstAvailableResource(Constants.BRICK, 0)));
        addRoad.add(new Delete(getFirstAvailableResource(Constants.LUMBER, 0)));
        
        infoMessage = Constants.BUILDROADPT2;
        
        if(takesLongestRoad(pathAdding))
        {
            String playerString = "";
            
            if(myPlayer == 0)
                playerString = Constants.PB;
            else if(myPlayer == 1)
                playerString = Constants.PR;
            else if(myPlayer == 2)
                playerString = Constants.PY;
            else if(myPlayer == 3)
                playerString = Constants.PG;
            
            addRoad.add(new Set(Constants.LONGESTROAD,playerString));

            if(myVictoryPoints >= 8)
            {
                addRoad.add(new EndGame(playerIds.get(myPlayer)));
                infoMessage = Constants.ENDGAME;
            }
        }
        container.sendMakeMove(addRoad);
    }
    
    public void setNodeToBuild(int node)
    {
        String nodeString = "";
        String playerString = "";
        
        if(node < 10)
            nodeString = Constants.NODETOKEN + "0" + node;
        else
            nodeString = Constants.NODETOKEN + node;
        
        if(myPlayer == 0)
            playerString = Constants.PB;
        else if(myPlayer == 1)
            playerString = Constants.PR;
        else if(myPlayer == 2)
            playerString = Constants.PY;
        else if(myPlayer == 3)
            playerString = Constants.PG;
        
        if(infoMessage == Constants.MAKEFIRSTFREEMOVESETTLEMENT)
        {
            if(settlersOfCatanLogic.canAddSettlementHereFirstMove(theState, nodeString, playerString))
            {
                List<Operation> addSettlementFirstMove = new ArrayList<Operation>();
                addSettlementFirstMove.add(new SetTurn(playerIds.get(currentPlayer)));
                addSettlementFirstMove.add(new Set(nodeString, Constants.SETTLEMENTTOKEN + "00" + playerString));
                addSettlementFirstMove.add(new Set(Constants.SETTLEMENTTOKEN + "00" + playerString, nodeString));
                infoMessage = Constants.MAKEFIRSTFREEMOVEROAD;
                container.sendMakeMove(addSettlementFirstMove);
            }
        }
        else if(infoMessage == Constants.MAKESECONDFREEMOVESETTLEMENT)
        {
            if(settlersOfCatanLogic.canAddSettlementHereFirstMove(theState, nodeString, playerString))
            {
                List<Operation> addSettlementSecondMode = new ArrayList<Operation>();
                addSettlementSecondMode.add(new SetTurn(playerIds.get(currentPlayer)));
                addSettlementSecondMode.add(new Set(nodeString, Constants.SETTLEMENTTOKEN + "01" + playerString));
                addSettlementSecondMode.add(new Set(Constants.SETTLEMENTTOKEN + "01" + playerString, nodeString));
                
                List<String> resources = theBoard.getResoucesForNode(node);
                
                for(int i = 0; i < resources.size(); i++)
                {
                    addSettlementSecondMode.add(
                            new Set(
                                    Constants.RESOURCECARDTOKEN + "0" + i + playerString,
                                    resources.get(i)));
                    addSettlementSecondMode.add(
                            new SetVisibility(
                                    Constants.RESOURCECARDTOKEN + "0" + i + playerString, 
                                    Arrays.asList(playerIds.get(myPlayer))));
                }
                
                infoMessage = Constants.MAKESECONDFREEMOVEROAD;
                container.sendMakeMove(addSettlementSecondMode);
            }
        }
        else if(lookingForCity && infoMessage.equals(Constants.MAKEMOVE))
        {
            if(canPlaceCity(node))
            {
                List<Operation> addCity = new ArrayList<Operation>();
                addCity.add(new SetTurn(playerIds.get(currentPlayer)));
                addCity.add(new Set(nodeString, getFirstAvailableCity()));
                addCity.add(new Set(getFirstAvailableCity(), nodeString));
                addCity.add(new Delete(theState.get(nodeString).toString()));
                addCity.add(new SetVisibility(getFirstAvailableResource(Constants.ORE, 0)));
                addCity.add(new SetVisibility(getFirstAvailableResource(Constants.ORE, 1)));
                addCity.add(new SetVisibility(getFirstAvailableResource(Constants.ORE, 2)));
                addCity.add(new SetVisibility(getFirstAvailableResource(Constants.GRAIN, 0)));
                addCity.add(new SetVisibility(getFirstAvailableResource(Constants.GRAIN, 1)));
                
                infoMessage = Constants.BUILDCITYPT1;
                container.sendMakeMove(addCity);
            }
        }
        else if(infoMessage.equals(Constants.MAKEMOVE))
        {
            if(settlersOfCatanLogic.canAddSettlementHere(theState, nodeString, playerString))
            {
                List<Operation> addSettlement = new ArrayList<Operation>();
                addSettlement.add(new SetTurn(playerIds.get(currentPlayer)));
                addSettlement.add(new Set(nodeString, getFirstAvailableSettlement()));
                addSettlement.add(new Set(getFirstAvailableSettlement(), nodeString));
                addSettlement.add(new SetVisibility(getFirstAvailableResource(Constants.BRICK, 0)));
                addSettlement.add(new SetVisibility(getFirstAvailableResource(Constants.LUMBER, 0)));
                addSettlement.add(new SetVisibility(getFirstAvailableResource(Constants.WOOL, 0)));
                addSettlement.add(new SetVisibility(getFirstAvailableResource(Constants.GRAIN, 0)));
                
                infoMessage = Constants.BUILDSETTLEMENTPT1;
                container.sendMakeMove(addSettlement);
            }
        }
    }
    
    public void finishSettlementBuild()
    {
        List<Operation> addSettlement = new ArrayList<Operation>();
        addSettlement.add(new Delete(getFirstAvailableResource(Constants.BRICK, 0)));
        addSettlement.add(new Delete(getFirstAvailableResource(Constants.LUMBER, 0)));
        addSettlement.add(new Delete(getFirstAvailableResource(Constants.WOOL, 0)));
        addSettlement.add(new Delete(getFirstAvailableResource(Constants.GRAIN, 0)));
        
        infoMessage = Constants.BUILDSETTLEMENTPT2;
        
        if(myVictoryPoints == 9)
        {
            addSettlement.add(new EndGame(playerIds.get(myPlayer)));
            infoMessage = Constants.ENDGAME;
        }
        container.sendMakeMove(addSettlement);
    }
    
    public void finishCityBuild()
    {
        List<Operation> addCity = new ArrayList<Operation>();
        addCity.add(new Delete(getFirstAvailableResource(Constants.ORE, 0)));
        addCity.add(new Delete(getFirstAvailableResource(Constants.ORE, 1)));
        addCity.add(new Delete(getFirstAvailableResource(Constants.ORE, 2)));
        addCity.add(new Delete(getFirstAvailableResource(Constants.GRAIN, 0)));
        addCity.add(new Delete(getFirstAvailableResource(Constants.GRAIN, 1)));
        
        infoMessage = Constants.BUILDCITYPT2;
        
        if(myVictoryPoints == 9)
        {
            addCity.add(new EndGame(playerIds.get(myPlayer)));
            infoMessage = Constants.ENDGAME;
        }
        container.sendMakeMove(addCity);
    }
    
    public void buyDevelopmentCard()
    {   
        if(infoMessage.equals(Constants.MAKEMOVE))
        {
            String card = getFirstAvailableDevelopmentCard();
            
            if(!card.equals(""))
            {
                List<Operation> buyDevelopmentCard = new ArrayList<Operation>();
                buyDevelopmentCard.add(new SetTurn(playerIds.get(currentPlayer)));
                buyDevelopmentCard.add(new SetVisibility(card, Arrays.asList(playerIds.get(myPlayer))));
                buyDevelopmentCard.add(new SetVisibility(getFirstAvailableResource(Constants.ORE, 0)));
                buyDevelopmentCard.add(new SetVisibility(getFirstAvailableResource(Constants.WOOL, 0)));
                buyDevelopmentCard.add(new SetVisibility(getFirstAvailableResource(Constants.GRAIN, 0)));
                
                infoMessage = Constants.BUYDEVELOPMENTCARDPT1;
                container.sendMakeMove(buyDevelopmentCard);
            }
        }
    }
    
    public void finishBuyingDevelopmentCard()
    {
        List<Operation> buyDevelopmentCard = new ArrayList<Operation>();
        buyDevelopmentCard.add(new Delete(getFirstAvailableResource(Constants.ORE, 0)));
        buyDevelopmentCard.add(new Delete(getFirstAvailableResource(Constants.WOOL, 0)));
        buyDevelopmentCard.add(new Delete(getFirstAvailableResource(Constants.GRAIN, 0)));
        
        infoMessage = Constants.BUYDEVELOPMENTCARDPT2;
        
        if( myVictoryPoints == 10)
        {
            buyDevelopmentCard.add(new EndGame(playerIds.get(myPlayer)));
            infoMessage = Constants.ENDGAME;
        }
        container.sendMakeMove(buyDevelopmentCard);
    }
    
    public void doHarborTrade(String resource1, String resource2)
    {
        if(infoMessage.equals(Constants.MAKEMOVE))
        {
            if(theBoard.ownsTwoToOnePort(myPlayer, resource1))
            {
                List<Operation> harborTrade = new ArrayList<Operation>();
                harborTrade.add(new SetTurn(playerIds.get(currentPlayer)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 0)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 1)));
                
                if(resource1.equals(Constants.ORE))
                    infoMessage = Constants.TWOTOONEOREHARBORTRADEPT1;
                else if(resource1.equals(Constants.LUMBER))
                    infoMessage = Constants.TWOTOONELUMHARBORTRADEPT1;
                else if(resource1.equals(Constants.BRICK))
                    infoMessage = Constants.TWOTOONEBRIHARBORTRADEPT1;
                else if(resource1.equals(Constants.WOOL))
                    infoMessage = Constants.TWOTOONEWOOHARBORTRADEPT1;
                else if(resource1.equals(Constants.GRAIN))
                    infoMessage = Constants.TWOTOONEGRAHARBORTRADEPT1;
                
                storedResourceOut = resource1;
                storedResourceIn = resource2;
                
                container.sendMakeMove(harborTrade);
            }
            else if(theBoard.ownsThreeToOnePort(myPlayer))
            {
                List<Operation> harborTrade = new ArrayList<Operation>();
                harborTrade.add(new SetTurn(playerIds.get(currentPlayer)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 0)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 1)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 2)));
                
                infoMessage = Constants.THREETOONEHARBORTRADEPT1;

                storedResourceOut = resource1;
                storedResourceIn = resource2;
                
                container.sendMakeMove(harborTrade);
            }
            else
            {
                List<Operation> harborTrade = new ArrayList<Operation>();
                harborTrade.add(new SetTurn(playerIds.get(currentPlayer)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 0)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 1)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 2)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 3)));
                
                infoMessage = Constants.HARBORTRADEPT1;

                storedResourceOut = resource1;
                storedResourceIn = resource2;
                
                container.sendMakeMove(harborTrade);
            }
        }
    }
    
    public void finishHarborTrade()
    {
        if(infoMessage.equals(Constants.HARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 2)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 3)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = Constants.HARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(Constants.THREETOONEHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 2)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = Constants.THREETOONEHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(Constants.TWOTOONEBRIHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = Constants.TWOTOONEBRIHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(Constants.TWOTOONEOREHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = Constants.TWOTOONEOREHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(Constants.TWOTOONELUMHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = Constants.TWOTOONELUMHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(Constants.TWOTOONEGRAHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = Constants.TWOTOONEGRAHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(Constants.TWOTOONEWOOHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = Constants.TWOTOONEWOOHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
    }
    
    public void setResourceCardSelected(int resourceCard)
    {
        
    }
    
    public boolean canPlaceSettlement(int node)
    {
        boolean status = false;
        String nodeString = "";
        String playerString = "";
        
        if(node < 10)
            nodeString = Constants.NODETOKEN + "0" + node;
        else
            nodeString = Constants.NODETOKEN + node;
        
        if(myPlayer == 0)
            playerString = Constants.PB;
        else if(myPlayer == 1)
            playerString = Constants.PR;
        else if(myPlayer == 2)
            playerString = Constants.PY;
        else if(myPlayer == 3)
            playerString = Constants.PG;
        
        if(settlersOfCatanLogic.initial || settlersOfCatanLogic.firstFreeMove || settlersOfCatanLogic.secondFreeMove)
        {
            status = settlersOfCatanLogic.canAddSettlementHereFirstMove(theState, nodeString, playerString);
        }
        else
        {
            status = settlersOfCatanLogic.canAddSettlementHere(theState, nodeString, playerString);
        }
        
        return status;
    }
    
    public boolean canPlaceCity(int node)
    {
        boolean status = false;
        
        status = theBoard.nodeList.get(node).getPlayer() == myPlayer
              && theBoard.nodeList.get(node).getSettlement() == 1;
        
        return status;
    }
    
    public boolean canPlaceRoad(int path)
    {
        boolean status = false;
        String pathString = "";
        String playerString = "";
        
        if(path < 10)
            pathString = Constants.PATHTOKEN + "0" + path;
        else
            pathString = Constants.PATHTOKEN + path;
        
        if(myPlayer == 0)
            playerString = Constants.PB;
        else if(myPlayer == 1)
            playerString = Constants.PR;
        else if(myPlayer == 2)
            playerString = Constants.PY;
        else if(myPlayer == 3)
            playerString = Constants.PG;
        
        status = settlersOfCatanLogic.canAddRoadHere(theState, pathString, playerString);
        
        return status;
    }

    public void rollDice()
    {
        List<Operation> rollDiceMove = new ArrayList<Operation>();
        rollDiceMove.add(new SetTurn(playerIds.get(currentPlayer)));
        rollDiceMove.add(new SetRandomInteger(Constants.DIE0, 1, 6));
        rollDiceMove.add(new SetRandomInteger(Constants.DIE1, 1, 6));
        container.sendMakeMove(rollDiceMove);
    }
    
    private boolean isDiceRoll(Map<String, Object> state)
    {
        return state.containsKey(Constants.DIE0) && state.containsKey(Constants.DIE1);
    }

    public void dispenseResources(int rollTotal)
    {
        List<Operation> dispenseResourcesMove = new ArrayList<Operation>();
        dispenseResourcesMove.add(new SetTurn(playerIds.get(currentPlayer)));
        dispenseResourcesMove.add(new Delete(Constants.DIE0));
        dispenseResourcesMove.add(new Delete(Constants.DIE1));
        
        int blueCount = 0;
        int redCount = 0;
        int yellowCount = 0;
        int greenCount = 0;
        
        List<Integer> nodes = theBoard.getNodesPerDieRoll(rollTotal);
        List<String> resources = theBoard.getResourcesPerDieRoll(rollTotal);
        
        for(int i = 0; i < nodes.size(); i++)
        {
            if(theBoard.nodeList.get(nodes.get(i)).getPlayer() == 0)
            {
                String card = getOpenResourceCardSlot(Constants.PB, blueCount);
                if(!card.equals(""))
                {
                    dispenseResourcesMove.add(
                            new Set(
                                    card,
                                    resources.get(i)));
                    dispenseResourcesMove.add(
                            new SetVisibility(
                                    card, 
                                    Arrays.asList(playerIds.get(0))));

                    blueCount++;
                
                    if(theBoard.nodeList.get(nodes.get(i)).getSettlement() == 2)
                    {
                        card = getOpenResourceCardSlot(Constants.PB, blueCount);
                        if(!card.equals(""))
                        {
                            dispenseResourcesMove.add(
                                    new Set(
                                            card,
                                            resources.get(i)));
                            dispenseResourcesMove.add(
                                    new SetVisibility(
                                            card, 
                                            Arrays.asList(playerIds.get(0))));
                        }

                        blueCount++;
                    }
                }
            }
            else if(theBoard.nodeList.get(nodes.get(i)).getPlayer() == 1)
            {
                String card = getOpenResourceCardSlot(Constants.PR, redCount);
                if(!card.equals(""))
                {
                    dispenseResourcesMove.add(
                            new Set(
                                    card,
                                    resources.get(i)));
                    dispenseResourcesMove.add(
                            new SetVisibility(
                                    card, 
                                    Arrays.asList(playerIds.get(1))));
                    redCount++;
                    
                    if(theBoard.nodeList.get(nodes.get(i)).getSettlement() == 2)
                    {
                        card = getOpenResourceCardSlot(Constants.PR, redCount);
                        if(!card.equals(""))
                        {
                            dispenseResourcesMove.add(
                                    new Set(
                                            card,
                                            resources.get(i)));
                            dispenseResourcesMove.add(
                                    new SetVisibility(
                                            card, 
                                            Arrays.asList(playerIds.get(1))));
                            redCount++;
                        }
                    }
                }
            }
            else if(theBoard.nodeList.get(nodes.get(i)).getPlayer() == 2)
            {
                String card = getOpenResourceCardSlot(Constants.PY, yellowCount);
                if(!card.equals(""))
                {
                    dispenseResourcesMove.add(
                            new Set(
                                    card,
                                    resources.get(i)));
                    dispenseResourcesMove.add(
                            new SetVisibility(
                                    card, 
                                    Arrays.asList(playerIds.get(2))));
                    yellowCount++;
                    
                    if(theBoard.nodeList.get(nodes.get(i)).getSettlement() == 2)
                    {
                        card = getOpenResourceCardSlot(Constants.PY, yellowCount);
                        if(!card.equals(""))
                        {
                            dispenseResourcesMove.add(
                                    new Set(
                                            card,
                                            resources.get(i)));
                            dispenseResourcesMove.add(
                                    new SetVisibility(
                                            card, 
                                            Arrays.asList(playerIds.get(2))));
                            yellowCount++;
                        }
                    }
                }
            }
            else if(theBoard.nodeList.get(nodes.get(i)).getPlayer() == 3)
            {
                String card = getOpenResourceCardSlot(Constants.PG, greenCount);
                if(!card.equals(""))
                {
                    dispenseResourcesMove.add(
                            new Set(
                                    card,
                                    resources.get(i)));
                    dispenseResourcesMove.add(
                            new SetVisibility(
                                    card, 
                                    Arrays.asList(playerIds.get(3))));
                    greenCount++;
                    
                    if(theBoard.nodeList.get(nodes.get(i)).getSettlement() == 2)
                    {
                        card = getOpenResourceCardSlot(Constants.PG, greenCount);
                        if(!card.equals(""))
                        {
                            dispenseResourcesMove.add(
                                    new Set(
                                            card,
                                            resources.get(i)));
                            dispenseResourcesMove.add(
                                    new SetVisibility(
                                            card, 
                                            Arrays.asList(playerIds.get(3))));
                            greenCount++;
                        }
                    }
                }
            }
        }
        
        infoMessage = Constants.ROLLEDTOK + rollTotal;
        container.sendMakeMove(dispenseResourcesMove);
    }

    public void clearRollAndMoveRobber()
    {
        List<Operation> clearRoll = new ArrayList<Operation>();
        clearRoll.add(new SetTurn(playerIds.get(currentPlayer)));
        clearRoll.add(new Delete(Constants.DIE0));
        clearRoll.add(new Delete(Constants.DIE1));
        infoMessage = Constants.MOVEROBBERPT1;
        container.sendMakeMove(clearRoll);
    }
    
    private String getOpenResourceCardSlot(String playerString, int skip)
    {
        String cardSlot = "";
        String currentSearch = "";
        int ourSkip = skip;
        
        for(int i = 0; i < 30 && cardSlot.equals(""); i++)
        {
            if(i < 10)
                currentSearch = Constants.RESOURCECARDTOKEN + "0" + i + playerString;
            else
                currentSearch = Constants.RESOURCECARDTOKEN + i + playerString;
            
            if(!theState.containsKey(currentSearch))
            {
                if(ourSkip > 0)
                {
                    ourSkip--;
                }
                else
                {
                    cardSlot = currentSearch;
                }
            }
        }
        
        return cardSlot;
    }

    public void endTurn()
    {
        List<Operation> dispenseResourcesMove = new ArrayList<Operation>();
        currentPlayer = (currentPlayer + 1) % playerIds.size();
        dispenseResourcesMove.add(new SetTurn(playerIds.get(currentPlayer)));
        
        infoMessage = Constants.ROLLDICE;
        container.sendMakeMove(dispenseResourcesMove);
    }
    
    private String getFirstAvailableSettlement()
    {
        String settlementToFind = "";
        String settlementToSearch = "";
        String playerString = "";
        
        if(myPlayer == 0)
            playerString = Constants.PB;
        else if(myPlayer == 1)
            playerString = Constants.PR;
        else if(myPlayer == 2)
            playerString = Constants.PY;
        else if(myPlayer == 3)
            playerString = Constants.PG;
        
        for(int i = 0; i < 5 && settlementToFind.equals(""); i++)
        {
            settlementToSearch = Constants.SETTLEMENTTOKEN + "0" + i + playerString;
            if(!theState.containsKey(settlementToSearch))
            {
                settlementToFind = settlementToSearch;
            }
        }
        
        return settlementToFind;
    }
    
    private String getFirstAvailableCity()
    {
        String cityToFind = "";
        String cityToSearch = "";
        String playerString = "";
        
        if(myPlayer == 0)
            playerString = Constants.PB;
        else if(myPlayer == 1)
            playerString = Constants.PR;
        else if(myPlayer == 2)
            playerString = Constants.PY;
        else if(myPlayer == 3)
            playerString = Constants.PG;
        
        for(int i = 0; i < 4 && cityToFind.equals(""); i++)
        {
            cityToSearch = Constants.CITYTOKEN + "0" + i + playerString;
            if(!theState.containsKey(cityToSearch))
            {
                cityToFind = cityToSearch;
            }
        }
        
        return cityToFind;
    }
    
    private String getFirstAvailableRoad()
    {
        String roadToFind = "";
        String roadToSearch = "";
        String playerString = "";
        
        if(myPlayer == 0)
            playerString = Constants.PB;
        else if(myPlayer == 1)
            playerString = Constants.PR;
        else if(myPlayer == 2)
            playerString = Constants.PY;
        else if(myPlayer == 3)
            playerString = Constants.PG;
        
        for(int i = 0; i < 15 && roadToFind.equals(""); i++)
        {
            if(i < 10)
                roadToSearch = Constants.ROADTOKEN + "0" + i + playerString;
            else
                roadToSearch = Constants.ROADTOKEN + i + playerString;
            
            if(!theState.containsKey(roadToSearch))
            {
                roadToFind = roadToSearch;
            }
        }
        
        return roadToFind;
    }
    
    private String getFirstAvailableResource(String resource, int skip)
    {
        String resourceToFind = "";
        String resourceToSearch = "";
        String playerString = "";
        int ourSkip = skip;
        
        if(myPlayer == 0)
            playerString = Constants.PB;
        else if(myPlayer == 1)
            playerString = Constants.PR;
        else if(myPlayer == 2)
            playerString = Constants.PY;
        else if(myPlayer == 3)
            playerString = Constants.PG;
        
        for(int i = 0; i < 30 && resourceToFind.equals(""); i++)
        {
            if(i < 10)
                resourceToSearch = Constants.RESOURCECARDTOKEN + "0" + i + playerString;
            else
                resourceToSearch = Constants.RESOURCECARDTOKEN + i + playerString;
            
            if( theState.containsKey(resourceToSearch)
              && theState.get(resourceToSearch).toString().equals(resource))
            {
                if(ourSkip == 0)
                    resourceToFind = resourceToSearch;
                else
                    ourSkip--;
            }
        }
        
        return resourceToFind;
    }

    public void makeMove()
    {
        infoMessage = Constants.MAKEMOVE;
    }
    
    public String getFirstAvailableDevelopmentCard()
    {
        String developmentCardToFind = "";
        String developmentCardToSearch = "";
        
        for(int i = 0; i < 25 && developmentCardToFind.equals(""); i++)
        {
            if(i < 10)
                developmentCardToSearch = Constants.DEVELOPMENTCARDTOKEN + "0" + i;
            else
                developmentCardToSearch = Constants.DEVELOPMENTCARDTOKEN + i;
            
            if(i == currentDevelopmentCard)
            {
                developmentCardToFind = developmentCardToSearch;
                i++;
            }
        }
        
        return developmentCardToFind;
    }

    public boolean takesLongestRoad(String pathString)
    {
        boolean status = false;
        
        String playerString = "";
        
        if(myPlayer == 0)
            playerString = Constants.PB;
        else if(myPlayer == 1)
            playerString = Constants.PR;
        else if(myPlayer == 2)
            playerString = Constants.PY;
        else if(myPlayer == 3)
            playerString = Constants.PG;
        
        Map<String, Object> testState = new HashMap<String, Object>(theState);
        
        testState.put(pathString, getFirstAvailableRoad());
        
        status = settlersOfCatanLogic.checkLongestRoadClaim(playerString, theState);
        
        return status;
    }

    public void finishRobber(String victim)
    {
        String hex = "";
        if(robberDestination < 10)
            hex = Constants.HEXTOKEN + "0" + robberDestination;
        else
            hex = Constants.HEXTOKEN + robberDestination;
        
        String randomCard = "";
        
        if(!victim.equals(""))
            randomCard = getRandomCard(victim);
        
        if(infoMessage.equals(Constants.MOVEROBBERPT2))
        {
            List<Operation> setRobber = new ArrayList<Operation>();
            setRobber.add(new SetTurn(playerIds.get(currentPlayer)));
            setRobber.add(new Set(Constants.ROBBER, hex));
            if(!randomCard.equals(""))
            {
                setRobber.add(new SetVisibility(randomCard));
                savedRandomCard = randomCard;
            }
            infoMessage = Constants.MOVEROBBERPT3;
            container.sendMakeMove(setRobber);
        }
    }
    
    public void finishMoveRobber()
    {
        if(infoMessage.equals(Constants.MOVEROBBERPT3))
        {
            if(!savedRandomCard.equals(""))
            {
                String resource = theState.get(savedRandomCard).toString();
                List<Operation> setRobber = new ArrayList<Operation>();
                setRobber.add(new Delete(savedRandomCard));
                setRobber.add(new Set(getOpenResourceCardSlot(resource, 0), resource));
                setRobber.add(new SetVisibility(getOpenResourceCardSlot(resource, 0), Arrays.asList(playerIds.get(myPlayer))));
                infoMessage = Constants.MOVEROBBERPT4;
                container.sendMakeMove(setRobber);
            }
            
            savedRandomCard = "";
        }
    }
    
    private String getRandomCard(String victim)
    {
        String cardToSearch = "";
        List<String> knownCards = new ArrayList<String>();
        
        for(int i = 0; i < 30; i++)
        {
            if(i < 10)
                cardToSearch = Constants.RESOURCECARDTOKEN + "0" + i + victim;
            else
                cardToSearch = Constants.RESOURCECARDTOKEN + i + victim;
            
            if(theState.containsKey(cardToSearch))
            {
                knownCards.add(cardToSearch);
            }
        }
        
        return knownCards.get((int)((Math.random()*knownCards.size())));
    }
}
