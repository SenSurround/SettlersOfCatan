package org.settlersofcatan.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.media.client.Audio;

import org.game_api.GameApi.Container;
import org.game_api.GameApi.Delete;
import org.game_api.GameApi.EndGame;
import org.game_api.GameApi.Set;
import org.game_api.GameApi.SetVisibility;
import org.game_api.GameApi.Operation;
import org.game_api.GameApi.SetTurn;
import org.game_api.GameApi.SetRandomInteger;
import org.game_api.GameApi.UpdateUI;

import org.settlersofcatan.graphics.BoardImages;
import org.settlersofcatan.graphics.BoardSounds;

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
    private List<String> playerIds;
    private List<String> selectedResourceCards = new ArrayList<String>();
    private List<String> unSelectedResourceCards = new ArrayList<String>();
    private String storedResourceOut = "";
    private String storedResourceIn = "";
    public boolean lookingForCity = false;
    private String pathAdding = "";
    private String savedRandomCard = "";
    
    public int currentPlayer = -1;
    public String infoMessage = "";
    
    private int robberDestination;
    
    private int currentDevelopmentCard = 0;
    
    private Audio buildRoadSound;
    private Audio buildHouseSound;
    private Audio moveRobberSound;

    public SettlersOfCatanPresenter(View view, Container container) {
      this.view = view;
      this.container = container;
      view.setPresenter(this);
      
      BoardSounds bs = GWT.create(BoardSounds.class);
      
      buildRoadSound = Audio.createIfSupported();
      buildRoadSound.addSource(bs.jackhammerMp3().getSafeUri().asString(), AudioElement.TYPE_MP3);
      buildRoadSound.addSource(bs.jackhammerWav().getSafeUri().asString(), AudioElement.TYPE_WAV);

      buildHouseSound = Audio.createIfSupported();
      buildHouseSound.addSource(bs.hammerMp3().getSafeUri().asString(), AudioElement.TYPE_MP3);
      buildHouseSound.addSource(bs.hammerWav().getSafeUri().asString(), AudioElement.TYPE_WAV);

      moveRobberSound = Audio.createIfSupported();
      moveRobberSound.addSource(bs.laughMp3().getSafeUri().asString(), AudioElement.TYPE_MP3);
      moveRobberSound.addSource(bs.laughWav().getSafeUri().asString(), AudioElement.TYPE_WAV);
    }
    
    public void updateUI(UpdateUI updateUI)
    {
        playerIds = updateUI.getPlayerIds();
        String yourPlayerId = updateUI.getYourPlayerId();
        myPlayer = updateUI.getPlayerIndex(yourPlayerId);
        
        if(updateUI.getState().isEmpty())
        {
            // Blue must send the initial move
            if (myPlayer == 0)
            {
                currentPlayer = 0;
                infoMessage = SettlersOfCatanConstants.MAKEFIRSTFREEMOVESETTLEMENT;
                sendInitialMove(playerIds);
            }
            else
            {
                infoMessage = SettlersOfCatanConstants.MAKEFIRSTFREEMOVESETTLEMENT;
            }
            
            return;
        }
        
        if(-1 != getCurrentPlayer(updateUI.getLastMove()))
            currentPlayer = getCurrentPlayer(updateUI.getLastMove());
        
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
        
        if(isDiceRoll(theState) && myPlayer == currentPlayer)
        {
            view.handleDiceRoll(
                    Integer.parseInt(theState.get(SettlersOfCatanConstants.DIE0).toString()),
                    Integer.parseInt(theState.get(SettlersOfCatanConstants.DIE1).toString()));
            
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

    private void sendInitialMove(List<String> playerIds)
    {
        container.sendMakeMove(settlersOfCatanLogic.getMoveInitial(playerIds));
    }
    
    private boolean hasResourcesForUse(String option)
    {
        boolean retVal = false;
        
        switch(option)
        {
        case SettlersOfCatanConstants.BUILDSETTLEMENTPT1:
        case SettlersOfCatanConstants.BUILDSETTLEMENTPT2:
            retVal = myResourceCards.contains(SettlersOfCatanConstants.BRICK)
                  && myResourceCards.contains(SettlersOfCatanConstants.LUMBER)
                  && myResourceCards.contains(SettlersOfCatanConstants.WOOL)
                  && myResourceCards.contains(SettlersOfCatanConstants.GRAIN);
            break;
        case SettlersOfCatanConstants.BUILDCITYPT1:
        case SettlersOfCatanConstants.BUILDCITYPT2:
            int ore = 0;
            int grain = 0;
            for(int i = 0; i < myResourceCards.size(); i++)
            {
                if(myResourceCards.get(i).equals(SettlersOfCatanConstants.ORE))
                    ore++;
                else if(myResourceCards.get(i).equals(SettlersOfCatanConstants.GRAIN))
                    grain++;
            }
            
            retVal = ore > 2 && grain > 1;
            break;
        case SettlersOfCatanConstants.BUILDROADPT1:
        case SettlersOfCatanConstants.BUILDROADPT2:
            retVal = myResourceCards.contains(SettlersOfCatanConstants.BRICK)
                  && myResourceCards.contains(SettlersOfCatanConstants.LUMBER);
            break;
        case SettlersOfCatanConstants.BUYDEVELOPMENTCARDPT1:
        case SettlersOfCatanConstants.BUYDEVELOPMENTCARDPT2:
            retVal = myResourceCards.contains(SettlersOfCatanConstants.ORE)
                  && myResourceCards.contains(SettlersOfCatanConstants.WOOL)
                  && myResourceCards.contains(SettlersOfCatanConstants.GRAIN);
            break;
        }
        
        return retVal;
    }
    
    public boolean canBuildSettlement()
    {
        boolean status = false;
        
        if( theBoard.hasAvailableSettlements(myPlayer)
         && hasResourcesForUse(SettlersOfCatanConstants.BUILDSETTLEMENTPT1))
        {
            status = true;
        }
        
        return status;
    }
    
    public boolean canBuildCity()
    {
        boolean status = false;
        
        if( theBoard.hasAvailableCities(myPlayer)
         && hasResourcesForUse(SettlersOfCatanConstants.BUILDCITYPT1)
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
         && hasResourcesForUse(SettlersOfCatanConstants.BUILDROADPT1))
        {
            status = true;
        }
        
        return status;
    }
    
    public boolean canBuyDevelopmentCard()
    {
        boolean status = false;
        
        if( currentDevelopmentCard < 25
         && hasResourcesForUse(SettlersOfCatanConstants.BUYDEVELOPMENTCARDPT1))
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
            if(myDevelopmentCards.get(i).equals(SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00)
            || myDevelopmentCards.get(i).equals(SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF01)
            || myDevelopmentCards.get(i).equals(SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF02)
            || myDevelopmentCards.get(i).equals(SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF03))
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
        infoMessage = SettlersOfCatanConstants.MOVEROBBERPT2;
        return SettlersOfCatanConstants.MOVEROBBERPT2 + theBoard.addWhoToRob(hex, myPlayer, theState);
    }
    
    public void setPathToBuild(int path)
    {
        String pathString = "";
        String playerString = "";
        
        if(path < 10)
            pathString = SettlersOfCatanConstants.PATHTOKEN + "0" + path;
        else
            pathString = SettlersOfCatanConstants.PATHTOKEN + path;
        
        if(myPlayer == 0)
            playerString = SettlersOfCatanConstants.PB;
        else if(myPlayer == 1)
            playerString = SettlersOfCatanConstants.PR;
        else if(myPlayer == 2)
            playerString = SettlersOfCatanConstants.PY;
        else if(myPlayer == 3)
            playerString = SettlersOfCatanConstants.PG;
        
        if(infoMessage == SettlersOfCatanConstants.MAKEFIRSTFREEMOVEROAD)
        {
            if(settlersOfCatanLogic.canAddRoadHere(theState, pathString, playerString))
            {
                int player;
                if(currentPlayer != playerIds.size() - 1)
                {
                    player = (currentPlayer + 1) % playerIds.size();
                    //infoMessage = SettlersOfCatanConstants.MAKEFIRSTFREEMOVESETTLEMENT;
                }
                else
                {
                    player = currentPlayer;
                    //infoMessage = SettlersOfCatanConstants.MAKESECONDFREEMOVESETTLEMENT;
                }
                infoMessage = SettlersOfCatanConstants.MAKESECONDFREEMOVESETTLEMENT;
                
                List<Operation> addSettlementFirstMove = new ArrayList<Operation>();
                addSettlementFirstMove.add(new SetTurn(playerIds.get(player)));
                addSettlementFirstMove.add(new Set(pathString, SettlersOfCatanConstants.ROADTOKEN + "00" + playerString));
                addSettlementFirstMove.add(new Set(SettlersOfCatanConstants.ROADTOKEN + "00" + playerString, pathString));
                buildRoadSound.play();
                container.sendMakeMove(addSettlementFirstMove);
            }
        }
        else if(infoMessage == SettlersOfCatanConstants.MAKESECONDFREEMOVEROAD)
        {
            if(settlersOfCatanLogic.canAddRoadHere(theState, pathString, playerString))
            {
                int player;
                if(currentPlayer != 0)
                {
                    player = (currentPlayer - 1) % playerIds.size();
                    //infoMessage = SettlersOfCatanConstants.MAKESECONDFREEMOVESETTLEMENT;
                }
                else
                {
                    player = currentPlayer;
                    //infoMessage = SettlersOfCatanConstants.ROLLDICE;
                }
                infoMessage = SettlersOfCatanConstants.ROLLDICE;
                
                List<Operation> addSettlementFirstMove = new ArrayList<Operation>();
                addSettlementFirstMove.add(new SetTurn(playerIds.get(player)));
                addSettlementFirstMove.add(new Set(pathString, SettlersOfCatanConstants.ROADTOKEN + "01" + playerString));
                addSettlementFirstMove.add(new Set(SettlersOfCatanConstants.ROADTOKEN + "01" + playerString, pathString));
                buildRoadSound.play();
                container.sendMakeMove(addSettlementFirstMove);
            }
        }
        else if(infoMessage.equals(SettlersOfCatanConstants.MAKEMOVE))
        {
            if(settlersOfCatanLogic.canAddRoadHere(theState, pathString, playerString))
            {
                List<Operation> addRoad = new ArrayList<Operation>();
                addRoad.add(new SetTurn(playerIds.get(currentPlayer)));
                addRoad.add(new Set(pathString, getFirstAvailableRoad()));
                addRoad.add(new Set(getFirstAvailableRoad(), pathString));
                addRoad.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.BRICK, 0)));
                addRoad.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.LUMBER, 0)));
                
                pathAdding = pathString;
                infoMessage = SettlersOfCatanConstants.BUILDROADPT1;
                container.sendMakeMove(addRoad);
            }
        }
    }
    
    public void finishRoadBuild()
    {
        List<Operation> addRoad = new ArrayList<Operation>();
        addRoad.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.BRICK, 0)));
        addRoad.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.LUMBER, 0)));
        
        infoMessage = SettlersOfCatanConstants.BUILDROADPT2;
        
        if(takesLongestRoad(pathAdding))
        {
            String playerString = "";
            
            if(myPlayer == 0)
                playerString = SettlersOfCatanConstants.PB;
            else if(myPlayer == 1)
                playerString = SettlersOfCatanConstants.PR;
            else if(myPlayer == 2)
                playerString = SettlersOfCatanConstants.PY;
            else if(myPlayer == 3)
                playerString = SettlersOfCatanConstants.PG;
            
            addRoad.add(new Set(SettlersOfCatanConstants.LONGESTROAD,playerString));

            if(myVictoryPoints >= 8)
            {
                addRoad.add(new EndGame(playerIds.get(myPlayer)));
                infoMessage = SettlersOfCatanConstants.ENDGAME;
            }
        }
        
        buildRoadSound.play();
        container.sendMakeMove(addRoad);
    }
    
    public void setNodeToBuild(int node)
    {
        String nodeString = "";
        String playerString = "";
        
        if(node < 10)
            nodeString = SettlersOfCatanConstants.NODETOKEN + "0" + node;
        else
            nodeString = SettlersOfCatanConstants.NODETOKEN + node;
        
        if(myPlayer == 0)
            playerString = SettlersOfCatanConstants.PB;
        else if(myPlayer == 1)
            playerString = SettlersOfCatanConstants.PR;
        else if(myPlayer == 2)
            playerString = SettlersOfCatanConstants.PY;
        else if(myPlayer == 3)
            playerString = SettlersOfCatanConstants.PG;
        
        if(infoMessage == SettlersOfCatanConstants.MAKEFIRSTFREEMOVESETTLEMENT)
        {
            if(settlersOfCatanLogic.canAddSettlementHereFirstMove(theState, nodeString, playerString))
            {
                List<Operation> addSettlementFirstMove = new ArrayList<Operation>();
                addSettlementFirstMove.add(new SetTurn(playerIds.get(currentPlayer)));
                addSettlementFirstMove.add(new Set(nodeString, SettlersOfCatanConstants.SETTLEMENTTOKEN + "00" + playerString));
                addSettlementFirstMove.add(new Set(SettlersOfCatanConstants.SETTLEMENTTOKEN + "00" + playerString, nodeString));
                infoMessage = SettlersOfCatanConstants.MAKEFIRSTFREEMOVEROAD;
                buildHouseSound.play();
                container.sendMakeMove(addSettlementFirstMove);
            }
        }
        else if(infoMessage == SettlersOfCatanConstants.MAKESECONDFREEMOVESETTLEMENT)
        {
            if(settlersOfCatanLogic.canAddSettlementHereFirstMove(theState, nodeString, playerString))
            {
                List<Operation> addSettlementSecondMode = new ArrayList<Operation>();
                addSettlementSecondMode.add(new SetTurn(playerIds.get(currentPlayer)));
                addSettlementSecondMode.add(new Set(nodeString, SettlersOfCatanConstants.SETTLEMENTTOKEN + "01" + playerString));
                addSettlementSecondMode.add(new Set(SettlersOfCatanConstants.SETTLEMENTTOKEN + "01" + playerString, nodeString));
                
                List<String> resources = theBoard.getResoucesForNode(node);
                
                for(int i = 0; i < resources.size(); i++)
                {
                    addSettlementSecondMode.add(
                            new Set(
                                    SettlersOfCatanConstants.RESOURCECARDTOKEN + "0" + i + playerString,
                                    resources.get(i)));
                    addSettlementSecondMode.add(
                            new SetVisibility(
                                    SettlersOfCatanConstants.RESOURCECARDTOKEN + "0" + i + playerString, 
                                    Arrays.asList(playerIds.get(myPlayer))));
                }
                
                infoMessage = SettlersOfCatanConstants.MAKESECONDFREEMOVEROAD;
                buildHouseSound.play();
                container.sendMakeMove(addSettlementSecondMode);
            }
        }
        else if(lookingForCity && infoMessage.equals(SettlersOfCatanConstants.MAKEMOVE))
        {
            if(canPlaceCity(node))
            {
                List<Operation> addCity = new ArrayList<Operation>();
                addCity.add(new SetTurn(playerIds.get(currentPlayer)));
                addCity.add(new Set(nodeString, getFirstAvailableCity()));
                addCity.add(new Set(getFirstAvailableCity(), nodeString));
                addCity.add(new Delete(theState.get(nodeString).toString()));
                addCity.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.ORE, 0)));
                addCity.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.ORE, 1)));
                addCity.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.ORE, 2)));
                addCity.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.GRAIN, 0)));
                addCity.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.GRAIN, 1)));
                
                infoMessage = SettlersOfCatanConstants.BUILDCITYPT1;
                container.sendMakeMove(addCity);
            }
        }
        else if(infoMessage.equals(SettlersOfCatanConstants.MAKEMOVE))
        {
            if(settlersOfCatanLogic.canAddSettlementHere(theState, nodeString, playerString))
            {
                List<Operation> addSettlement = new ArrayList<Operation>();
                addSettlement.add(new SetTurn(playerIds.get(currentPlayer)));
                addSettlement.add(new Set(nodeString, getFirstAvailableSettlement()));
                addSettlement.add(new Set(getFirstAvailableSettlement(), nodeString));
                addSettlement.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.BRICK, 0)));
                addSettlement.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.LUMBER, 0)));
                addSettlement.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.WOOL, 0)));
                addSettlement.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.GRAIN, 0)));
                
                infoMessage = SettlersOfCatanConstants.BUILDSETTLEMENTPT1;
                container.sendMakeMove(addSettlement);
            }
        }
    }
    
    public void finishSettlementBuild()
    {
        List<Operation> addSettlement = new ArrayList<Operation>();
        addSettlement.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.BRICK, 0)));
        addSettlement.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.LUMBER, 0)));
        addSettlement.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.WOOL, 0)));
        addSettlement.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.GRAIN, 0)));
        
        infoMessage = SettlersOfCatanConstants.BUILDSETTLEMENTPT2;
        
        if(myVictoryPoints == 9)
        {
            addSettlement.add(new EndGame(playerIds.get(myPlayer)));
            infoMessage = SettlersOfCatanConstants.ENDGAME;
        }
        buildHouseSound.play();
        container.sendMakeMove(addSettlement);
    }
    
    public void finishCityBuild()
    {
        List<Operation> addCity = new ArrayList<Operation>();
        addCity.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.ORE, 0)));
        addCity.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.ORE, 1)));
        addCity.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.ORE, 2)));
        addCity.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.GRAIN, 0)));
        addCity.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.GRAIN, 1)));
        
        infoMessage = SettlersOfCatanConstants.BUILDCITYPT2;
        
        if(myVictoryPoints == 9)
        {
            addCity.add(new EndGame(playerIds.get(myPlayer)));
            infoMessage = SettlersOfCatanConstants.ENDGAME;
        }
        buildHouseSound.play();
        container.sendMakeMove(addCity);
    }
    
    public void buyDevelopmentCard()
    {   
        if(infoMessage.equals(SettlersOfCatanConstants.MAKEMOVE))
        {
            String card = getFirstAvailableDevelopmentCard();
            
            if(!card.equals(""))
            {
                List<Operation> buyDevelopmentCard = new ArrayList<Operation>();
                buyDevelopmentCard.add(new SetTurn(playerIds.get(currentPlayer)));
                buyDevelopmentCard.add(new SetVisibility(card, Arrays.asList(playerIds.get(myPlayer))));
                buyDevelopmentCard.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.ORE, 0)));
                buyDevelopmentCard.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.WOOL, 0)));
                buyDevelopmentCard.add(new SetVisibility(getFirstAvailableResource(SettlersOfCatanConstants.GRAIN, 0)));
                
                infoMessage = SettlersOfCatanConstants.BUYDEVELOPMENTCARDPT1;
                container.sendMakeMove(buyDevelopmentCard);
            }
        }
    }
    
    public void finishBuyingDevelopmentCard()
    {
        List<Operation> buyDevelopmentCard = new ArrayList<Operation>();
        buyDevelopmentCard.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.ORE, 0)));
        buyDevelopmentCard.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.WOOL, 0)));
        buyDevelopmentCard.add(new Delete(getFirstAvailableResource(SettlersOfCatanConstants.GRAIN, 0)));
        
        infoMessage = SettlersOfCatanConstants.BUYDEVELOPMENTCARDPT2;
        
        if( myVictoryPoints == 10)
        {
            buyDevelopmentCard.add(new EndGame(playerIds.get(myPlayer)));
            infoMessage = SettlersOfCatanConstants.ENDGAME;
        }
        container.sendMakeMove(buyDevelopmentCard);
    }
    
    public void doHarborTrade(String resource1, String resource2)
    {
        if(infoMessage.equals(SettlersOfCatanConstants.MAKEMOVE))
        {
            if(theBoard.ownsTwoToOnePort(myPlayer, resource1))
            {
                List<Operation> harborTrade = new ArrayList<Operation>();
                harborTrade.add(new SetTurn(playerIds.get(currentPlayer)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 0)));
                harborTrade.add(new SetVisibility(getFirstAvailableResource(resource1, 1)));
                
                if(resource1.equals(SettlersOfCatanConstants.ORE))
                    infoMessage = SettlersOfCatanConstants.TWOTOONEOREHARBORTRADEPT1;
                else if(resource1.equals(SettlersOfCatanConstants.LUMBER))
                    infoMessage = SettlersOfCatanConstants.TWOTOONELUMHARBORTRADEPT1;
                else if(resource1.equals(SettlersOfCatanConstants.BRICK))
                    infoMessage = SettlersOfCatanConstants.TWOTOONEBRIHARBORTRADEPT1;
                else if(resource1.equals(SettlersOfCatanConstants.WOOL))
                    infoMessage = SettlersOfCatanConstants.TWOTOONEWOOHARBORTRADEPT1;
                else if(resource1.equals(SettlersOfCatanConstants.GRAIN))
                    infoMessage = SettlersOfCatanConstants.TWOTOONEGRAHARBORTRADEPT1;
                
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
                
                infoMessage = SettlersOfCatanConstants.THREETOONEHARBORTRADEPT1;

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
                
                infoMessage = SettlersOfCatanConstants.HARBORTRADEPT1;

                storedResourceOut = resource1;
                storedResourceIn = resource2;
                
                container.sendMakeMove(harborTrade);
            }
        }
    }
    
    public void finishHarborTrade()
    {
        if(infoMessage.equals(SettlersOfCatanConstants.HARBORTRADEPT1))
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
            
            infoMessage = SettlersOfCatanConstants.HARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(SettlersOfCatanConstants.THREETOONEHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 2)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = SettlersOfCatanConstants.THREETOONEHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(SettlersOfCatanConstants.TWOTOONEBRIHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = SettlersOfCatanConstants.TWOTOONEBRIHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(SettlersOfCatanConstants.TWOTOONEOREHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = SettlersOfCatanConstants.TWOTOONEOREHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(SettlersOfCatanConstants.TWOTOONELUMHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = SettlersOfCatanConstants.TWOTOONELUMHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(SettlersOfCatanConstants.TWOTOONEGRAHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = SettlersOfCatanConstants.TWOTOONEGRAHARBORTRADEPT2;
            
            container.sendMakeMove(harborTrade);

            storedResourceOut = "";
            storedResourceIn = "";
        }
        else if(infoMessage.equals(SettlersOfCatanConstants.TWOTOONEWOOHARBORTRADEPT1))
        {
            List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 0)));
            harborTrade.add(new Delete(getFirstAvailableResource(storedResourceOut, 1)));
            harborTrade.add(new Set(getFirstAvailableResource(storedResourceOut, 0), storedResourceIn));
            harborTrade.add(new SetVisibility(
                    getFirstAvailableResource(storedResourceOut, 0), 
                            Arrays.asList(playerIds.get(myPlayer))));
            
            infoMessage = SettlersOfCatanConstants.TWOTOONEWOOHARBORTRADEPT2;
            
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
            nodeString = SettlersOfCatanConstants.NODETOKEN + "0" + node;
        else
            nodeString = SettlersOfCatanConstants.NODETOKEN + node;
        
        if(myPlayer == 0)
            playerString = SettlersOfCatanConstants.PB;
        else if(myPlayer == 1)
            playerString = SettlersOfCatanConstants.PR;
        else if(myPlayer == 2)
            playerString = SettlersOfCatanConstants.PY;
        else if(myPlayer == 3)
            playerString = SettlersOfCatanConstants.PG;
        
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
            pathString = SettlersOfCatanConstants.PATHTOKEN + "0" + path;
        else
            pathString = SettlersOfCatanConstants.PATHTOKEN + path;
        
        if(myPlayer == 0)
            playerString = SettlersOfCatanConstants.PB;
        else if(myPlayer == 1)
            playerString = SettlersOfCatanConstants.PR;
        else if(myPlayer == 2)
            playerString = SettlersOfCatanConstants.PY;
        else if(myPlayer == 3)
            playerString = SettlersOfCatanConstants.PG;
        
        status = settlersOfCatanLogic.canAddRoadHere(theState, pathString, playerString);
        
        return status;
    }

    public void rollDice()
    {
        List<Operation> rollDiceMove = new ArrayList<Operation>();
        rollDiceMove.add(new SetTurn(playerIds.get(currentPlayer)));
        rollDiceMove.add(new SetRandomInteger(SettlersOfCatanConstants.DIE0, 1, 6));
        rollDiceMove.add(new SetRandomInteger(SettlersOfCatanConstants.DIE1, 1, 6));
        container.sendMakeMove(rollDiceMove);
    }
    
    private boolean isDiceRoll(Map<String, Object> state)
    {
        return state.containsKey(SettlersOfCatanConstants.DIE0) && state.containsKey(SettlersOfCatanConstants.DIE1);
    }

    public void dispenseResources(int rollTotal)
    {
        List<Operation> dispenseResourcesMove = new ArrayList<Operation>();
        dispenseResourcesMove.add(new SetTurn(playerIds.get(currentPlayer)));
        dispenseResourcesMove.add(new Delete(SettlersOfCatanConstants.DIE0));
        dispenseResourcesMove.add(new Delete(SettlersOfCatanConstants.DIE1));
        
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
                String card = getOpenResourceCardSlot(SettlersOfCatanConstants.PB, blueCount);
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
                        card = getOpenResourceCardSlot(SettlersOfCatanConstants.PB, blueCount);
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
                String card = getOpenResourceCardSlot(SettlersOfCatanConstants.PR, redCount);
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
                        card = getOpenResourceCardSlot(SettlersOfCatanConstants.PR, redCount);
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
                String card = getOpenResourceCardSlot(SettlersOfCatanConstants.PY, yellowCount);
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
                        card = getOpenResourceCardSlot(SettlersOfCatanConstants.PY, yellowCount);
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
                String card = getOpenResourceCardSlot(SettlersOfCatanConstants.PG, greenCount);
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
                        card = getOpenResourceCardSlot(SettlersOfCatanConstants.PG, greenCount);
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
        
        infoMessage = SettlersOfCatanConstants.ROLLEDTOK + rollTotal;
        container.sendMakeMove(dispenseResourcesMove);
    }

    public void clearRollAndMoveRobber()
    {
        List<Operation> clearRoll = new ArrayList<Operation>();
        clearRoll.add(new SetTurn(playerIds.get(currentPlayer)));
        clearRoll.add(new Delete(SettlersOfCatanConstants.DIE0));
        clearRoll.add(new Delete(SettlersOfCatanConstants.DIE1));
        infoMessage = SettlersOfCatanConstants.MOVEROBBERPT1;
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
                currentSearch = SettlersOfCatanConstants.RESOURCECARDTOKEN + "0" + i + playerString;
            else
                currentSearch = SettlersOfCatanConstants.RESOURCECARDTOKEN + i + playerString;
            
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
        int player = (currentPlayer + 1) % playerIds.size();
        dispenseResourcesMove.add(new SetTurn(playerIds.get(player)));
        
        infoMessage = SettlersOfCatanConstants.ROLLDICE;
        container.sendMakeMove(dispenseResourcesMove);
    }
    
    private String getFirstAvailableSettlement()
    {
        String settlementToFind = "";
        String settlementToSearch = "";
        String playerString = "";
        
        if(myPlayer == 0)
            playerString = SettlersOfCatanConstants.PB;
        else if(myPlayer == 1)
            playerString = SettlersOfCatanConstants.PR;
        else if(myPlayer == 2)
            playerString = SettlersOfCatanConstants.PY;
        else if(myPlayer == 3)
            playerString = SettlersOfCatanConstants.PG;
        
        for(int i = 0; i < 5 && settlementToFind.equals(""); i++)
        {
            settlementToSearch = SettlersOfCatanConstants.SETTLEMENTTOKEN + "0" + i + playerString;
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
            playerString = SettlersOfCatanConstants.PB;
        else if(myPlayer == 1)
            playerString = SettlersOfCatanConstants.PR;
        else if(myPlayer == 2)
            playerString = SettlersOfCatanConstants.PY;
        else if(myPlayer == 3)
            playerString = SettlersOfCatanConstants.PG;
        
        for(int i = 0; i < 4 && cityToFind.equals(""); i++)
        {
            cityToSearch = SettlersOfCatanConstants.CITYTOKEN + "0" + i + playerString;
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
            playerString = SettlersOfCatanConstants.PB;
        else if(myPlayer == 1)
            playerString = SettlersOfCatanConstants.PR;
        else if(myPlayer == 2)
            playerString = SettlersOfCatanConstants.PY;
        else if(myPlayer == 3)
            playerString = SettlersOfCatanConstants.PG;
        
        for(int i = 0; i < 15 && roadToFind.equals(""); i++)
        {
            if(i < 10)
                roadToSearch = SettlersOfCatanConstants.ROADTOKEN + "0" + i + playerString;
            else
                roadToSearch = SettlersOfCatanConstants.ROADTOKEN + i + playerString;
            
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
            playerString = SettlersOfCatanConstants.PB;
        else if(myPlayer == 1)
            playerString = SettlersOfCatanConstants.PR;
        else if(myPlayer == 2)
            playerString = SettlersOfCatanConstants.PY;
        else if(myPlayer == 3)
            playerString = SettlersOfCatanConstants.PG;
        
        for(int i = 0; i < 30 && resourceToFind.equals(""); i++)
        {
            if(i < 10)
                resourceToSearch = SettlersOfCatanConstants.RESOURCECARDTOKEN + "0" + i + playerString;
            else
                resourceToSearch = SettlersOfCatanConstants.RESOURCECARDTOKEN + i + playerString;
            
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
        infoMessage = SettlersOfCatanConstants.MAKEMOVE;
    }
    
    public String getFirstAvailableDevelopmentCard()
    {
        String developmentCardToFind = "";
        String developmentCardToSearch = "";
        
        for(int i = 0; i < 25 && developmentCardToFind.equals(""); i++)
        {
            if(i < 10)
                developmentCardToSearch = SettlersOfCatanConstants.DEVELOPMENTCARDTOKEN + "0" + i;
            else
                developmentCardToSearch = SettlersOfCatanConstants.DEVELOPMENTCARDTOKEN + i;
            
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
            playerString = SettlersOfCatanConstants.PB;
        else if(myPlayer == 1)
            playerString = SettlersOfCatanConstants.PR;
        else if(myPlayer == 2)
            playerString = SettlersOfCatanConstants.PY;
        else if(myPlayer == 3)
            playerString = SettlersOfCatanConstants.PG;
        
        Map<String, Object> testState = new HashMap<String, Object>(theState);
        
        testState.put(pathString, getFirstAvailableRoad());
        
        status = settlersOfCatanLogic.checkLongestRoadClaim(playerString, theState);
        
        return status;
    }

    public void finishRobber(String victim)
    {
        String hex = "";
        if(robberDestination < 10)
            hex = SettlersOfCatanConstants.HEXTOKEN + "0" + robberDestination;
        else
            hex = SettlersOfCatanConstants.HEXTOKEN + robberDestination;
        
        String randomCard = "";
        
        if(!victim.equals(""))
            randomCard = getRandomCard(victim);
        
        if(infoMessage.equals(SettlersOfCatanConstants.MOVEROBBERPT2))
        {
            List<Operation> setRobber = new ArrayList<Operation>();
            setRobber.add(new SetTurn(playerIds.get(currentPlayer)));
            setRobber.add(new Set(SettlersOfCatanConstants.ROBBER, hex));
            if(!randomCard.equals(""))
            {
                setRobber.add(new SetVisibility(randomCard));
                savedRandomCard = randomCard;
            }
            infoMessage = SettlersOfCatanConstants.MOVEROBBERPT3;
            container.sendMakeMove(setRobber);
        }
    }
    
    public void finishMoveRobber()
    {
        if(infoMessage.equals(SettlersOfCatanConstants.MOVEROBBERPT3))
        {
            List<Operation> setRobber = new ArrayList<Operation>();
            if(!savedRandomCard.equals(""))
            {
                String resource = theState.get(savedRandomCard).toString();
                setRobber.add(new Delete(savedRandomCard));
                setRobber.add(new Set(getOpenResourceCardSlot(resource, 0), resource));
                setRobber.add(new SetVisibility(getOpenResourceCardSlot(resource, 0), Arrays.asList(playerIds.get(myPlayer))));
            }
            
            infoMessage = SettlersOfCatanConstants.MOVEROBBERPT4;
            container.sendMakeMove(setRobber);
            
            savedRandomCard = "";
            
            moveRobberSound.play();
        }
    }
    
    private String getRandomCard(String victim)
    {
        String cardToSearch = "";
        List<String> knownCards = new ArrayList<String>();
        
        for(int i = 0; i < 30; i++)
        {
            if(i < 10)
                cardToSearch = SettlersOfCatanConstants.RESOURCECARDTOKEN + "0" + i + victim;
            else
                cardToSearch = SettlersOfCatanConstants.RESOURCECARDTOKEN + i + victim;
            
            if(theState.containsKey(cardToSearch))
            {
                knownCards.add(cardToSearch);
            }
        }
        
        return knownCards.get((int)((Math.random()*knownCards.size())));
    }
    
    private int getCurrentPlayer(List<Operation> move)
    {
        int player = -1;
        
        for(int i = 0; i < move.size(); i++)
        {
            if(move.get(i).getMessageName().equals("SetTurn"))
            {
                for(int j = 0; j < playerIds.size(); j++)
                {
                    if(playerIds.get(j).equals(((SetTurn)move.get(i)).getPlayerId()))
                    {
                        player = j;
                    }
                }
            }
        }
        
        return player;
    }
    
    // A Java method using JSNI
    native void sayHelloInJava(String name) /*-{
      $wnd.sayHello(name); // $wnd is a JSNI synonym for 'window'
    }-*/;
}
