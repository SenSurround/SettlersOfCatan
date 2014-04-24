package org.settlersofcatan.client;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

import org.game_api.GameApi.Delete;
import org.game_api.GameApi.EndGame;
import org.game_api.GameApi.Operation;
import org.game_api.GameApi.Set;
import org.game_api.GameApi.SetRandomInteger;
import org.game_api.GameApi.SetTurn;
import org.game_api.GameApi.SetVisibility;
import org.game_api.GameApi.Shuffle;
import org.game_api.GameApi.VerifyMove;
import org.game_api.GameApi.VerifyMoveDone;

public class SettlersOfCatanLogic {  
    
    // Global Error String
    // For Debug Purposes
    private String err = "";
    private int firstOpenDevelopmentCard = 0;
    private int verifyCount = 0;
    public boolean firstFreeMove = false;
    public boolean secondFreeMove = false;
    private boolean settlementTurn = false;
    private boolean finishRoadBuild = false;
    private boolean finishSettlementBuild = false;
    private boolean finishCityBuild = false;
    private boolean finishBuyingDevelopmentCard = false;
    private boolean finishHarborTrade = false;
    private boolean finishRobberMove = false;
    public boolean initial = true;
    
    private List<String> playerIds;
    
    List<String> resourceList = Arrays.asList(
            Constants.ORE,    Constants.ORE,    Constants.ORE,
            Constants.GRAIN,  Constants.GRAIN,  Constants.GRAIN,  Constants.GRAIN,
            Constants.LUMBER, Constants.LUMBER, Constants.LUMBER, Constants.LUMBER,
            Constants.WOOL,   Constants.WOOL,   Constants.WOOL,   Constants.WOOL,
            Constants.BRICK,  Constants.BRICK,  Constants.BRICK,
            Constants.DESERT);
    
    List<String> developmentCardTypeList = Arrays.asList(
            Constants.DEVELOPMENTCARDTYPEDEF00, Constants.DEVELOPMENTCARDTYPEDEF00,
            Constants.DEVELOPMENTCARDTYPEDEF00, Constants.DEVELOPMENTCARDTYPEDEF00,
            Constants.DEVELOPMENTCARDTYPEDEF00, Constants.DEVELOPMENTCARDTYPEDEF00,
            Constants.DEVELOPMENTCARDTYPEDEF00, Constants.DEVELOPMENTCARDTYPEDEF00,
            Constants.DEVELOPMENTCARDTYPEDEF00, Constants.DEVELOPMENTCARDTYPEDEF00,
            Constants.DEVELOPMENTCARDTYPEDEF00, Constants.DEVELOPMENTCARDTYPEDEF00,
            Constants.DEVELOPMENTCARDTYPEDEF00, Constants.DEVELOPMENTCARDTYPEDEF00,
            Constants.DEVELOPMENTCARDTYPEDEF01, Constants.DEVELOPMENTCARDTYPEDEF01,
            Constants.DEVELOPMENTCARDTYPEDEF02, Constants.DEVELOPMENTCARDTYPEDEF02,
            Constants.DEVELOPMENTCARDTYPEDEF03, Constants.DEVELOPMENTCARDTYPEDEF03,
            Constants.DEVELOPMENTCARDTYPEDEF04, Constants.DEVELOPMENTCARDTYPEDEF05,
            Constants.DEVELOPMENTCARDTYPEDEF06, Constants.DEVELOPMENTCARDTYPEDEF07,
            Constants.DEVELOPMENTCARDTYPEDEF08);
  
    List<String> harborTradeTypeList = Arrays.asList(
            Constants.HARBORTYPE00, Constants.HARBORTYPE00, Constants.HARBORTYPE00,
            Constants.HARBORTYPE00, Constants.HARBORTYPE01, Constants.HARBORTYPE02,
            Constants.HARBORTYPE03, Constants.HARBORTYPE04, Constants.HARBORTYPE05);
    
    public VerifyMoveDone verify(VerifyMove verifyMove) {
        try {
            checkMoveIsLegal(verifyMove);
            return new VerifyMoveDone();
        } catch (Exception e) {
            return new VerifyMoveDone(verifyMove.getLastMovePlayerId(), e.getMessage());
        }
      }
	
	public void checkMoveIsLegal(VerifyMove verifyMove) {
	    List<Operation> lastMove = verifyMove.getLastMove();
	    Map<String, Object> lastState = verifyMove.getLastState();
	    String lastMovePlayerId = verifyMove.getLastMovePlayerId();
	    String playerString = getPlayerId(verifyMove.getPlayerIds(), lastMovePlayerId);
        String nextPlayerString = getNextPlayerId(verifyMove.getPlayerIds(), lastMovePlayerId);
        String previousPlayerString = getPreviousPlayerId(verifyMove.getPlayerIds(), lastMovePlayerId);
	    
	    // Attempt to determine which move is being attempted
	    String expectedMove = findExpectedMove(
	            lastState, lastMove, playerString, nextPlayerString, lastMovePlayerId);
	    
	    // See whether the set of moves matches the expected move
	    // It also verifies whether the move is legal given the last state
	    boolean status = moveIsLegal(
	            expectedMove,
	            lastMove,
	            lastState,
	            playerString,
	            lastMovePlayerId,
	            nextPlayerString,
	            previousPlayerString,
	            verifyMove.getPlayerIds());
	    
	    if(!status)
	    {
	        throw new RuntimeException("We have a hacker!\n" + err);
	    }
	}
	
	public List<Operation> getMoveInitial(List<String> playerIds)
	{
	    List<Operation> firstMove = Lists.newArrayList();
	    
	    String playerBlueId = playerIds.get(0);
	    
	    // Set turn to player
	    firstMove.add(new SetTurn(playerBlueId));
	    
	    for(int i = 0; i < resourceList.size(); i++)
	    {
	        if(i < 10)
	            firstMove.add(
	                    new Set(Constants.HEXTOKEN + "0" + i,
	                            resourceList.get(i)));
	        else
                firstMove.add(
                        new Set(Constants.HEXTOKEN + i,
                                resourceList.get(i)));
	    }
        firstMove.add(new Shuffle(
                Lists.newArrayList("HEX00", "HEX01", "HEX02", "HEX03", "HEX04", "HEX05", 
                                   "HEX06", "HEX07", "HEX08", "HEX09", "HEX10", "HEX11", 
                                   "HEX12", "HEX13", "HEX14", "HEX15", "HEX16", "HEX17", "HEX18")));
	    
        for(int i = 0; i < developmentCardTypeList.size(); i++)
        {
            if(i < 10)
                firstMove.add(
                        new Set(Constants.DEVELOPMENTCARDTOKEN + "0" + i,
                                developmentCardTypeList.get(i)));
            else
                firstMove.add(
                        new Set(Constants.DEVELOPMENTCARDTOKEN + i,
                                developmentCardTypeList.get(i)));
        }
        firstMove.add(new Shuffle(
                Lists.newArrayList("DEV00", "DEV01", "DEV02", "DEV03", "DEV04",
                                   "DEV05", "DEV06", "DEV07", "DEV08", "DEV09",
                                   "DEV10", "DEV11", "DEV12", "DEV13", "DEV14",
                                   "DEV15", "DEV16", "DEV17", "DEV18", "DEV19",
                                   "DEV20", "DEV21", "DEV22", "DEV23", "DEV24")));
        
        
        for(int i = 0; i < developmentCardTypeList.size(); i++)
        {
            if(i < 10)
                firstMove.add(
                        new SetVisibility(Constants.DEVELOPMENTCARDTOKEN + "0" + i,
                                          Constants.visibleToNone));
            else
                firstMove.add(
                        new SetVisibility(Constants.DEVELOPMENTCARDTOKEN + i,
                                          Constants.visibleToNone));
        }

        for(int i = 0; i < harborTradeTypeList.size(); i++)
        {
            firstMove.add(
                    new Set(Constants.HARBORTRADETOKEN + "0" + i,
                            harborTradeTypeList.get(i)));
        }
        firstMove.add(new Shuffle(
                Lists.newArrayList("MAR00", "MAR01", "MAR02", "MAR03", "MAR04",
                                   "MAR05", "MAR06", "MAR07", "MAR08")));
        
        firstMove.add(new Set(Constants.SOLDIERCOUNTPB, 0));
        firstMove.add(new Set(Constants.SOLDIERCOUNTPR, 0));
        
        if(playerIds.size() == 3)
        {
            firstMove.add(new Set(Constants.SOLDIERCOUNTPY, 0));
        }
        else if(playerIds.size() == 4)
        {
            firstMove.add(new Set(Constants.SOLDIERCOUNTPY, 0));
            firstMove.add(new Set(Constants.SOLDIERCOUNTPG, 0));
        }
	    
	    return firstMove;
	}
	
    public List<String> getResourceCardsFromState(
            Map<String, Object> state,
            String playerString)
    {
        List<String> resourceCards = new ArrayList<String>();
        
        for(int i = 0; i < 30; i++)
        {
            String cardToSearchFor = "";
            if(i < 10)
                cardToSearchFor = Constants.RESOURCECARDTOKEN + "0" + i + playerString;
            else
                cardToSearchFor = Constants.RESOURCECARDTOKEN + i + playerString;
            
            if(state.containsKey(cardToSearchFor))
            {
                resourceCards.add(state.get(cardToSearchFor).toString());
            }
        }
        
        return resourceCards;
    }
    
    public List<String> getDevelopmentCardsFromState(
            Map<String, Object> state)
    {
        List<String> resourceCards = new ArrayList<String>();
        
        for(int i = 0; i < 25; i++)
        {
            String cardToSearchFor = "";
            if(i < 10)
                cardToSearchFor = Constants.DEVELOPMENTCARDTOKEN + "0" + i;
            else
                cardToSearchFor = Constants.DEVELOPMENTCARDTOKEN + i;
            
            if(state.containsKey(cardToSearchFor))
            {
                Object lookAtMe = state.get(cardToSearchFor);
                
                if(lookAtMe != null)
                {
                    resourceCards.add(state.get(cardToSearchFor).toString());
                }
            }
        }
        
        return resourceCards;
    }
    
    public int getVictoryPointCount(
            Map<String, Object> state,
            String playerString)
    {
        int victoryPoints = 0;
        
        victoryPoints = victoryPoints + countPointsFromCities(state, playerString);
        victoryPoints = victoryPoints + countPointsFromSettlements(state, playerString);
        victoryPoints = victoryPoints + countPointsfromLongestRoad(state, playerString);
        victoryPoints = victoryPoints + countPointsfromLargestArmy(state, playerString);
        victoryPoints = victoryPoints + countPointsfromDevelopmentCards(state, playerString);
        
        return victoryPoints;
    }
    
    public boolean hasLongestRoad(
            Map<String, Object> state,
            String playerString)
    {
        return state.containsKey(Constants.LONGESTROAD)
            && state.get(Constants.LONGESTROAD).toString().contains(playerString);
    }
    
    public boolean hasLargestArmy(
            Map<String, Object> state,
            String playerString)
    {
        return state.containsKey(Constants.LARGESTARMY)
            && state.get(Constants.LARGESTARMY).toString().contains(playerString);
    }
	
	// Umbrella function to test all moves
	// Returns true for moves that meet their criteria
	// Returns false for badly formed moves
	// err contains the error message in this case
	private boolean moveIsLegal(
	        String expectedMove,
	        List<Operation> lastMove,
	        Map<String, Object> lastState,
	        String playerString,
	        String playerId,
	        String nextPlayerString,
	        String previousPlayerString,
	        List<String> playerIds)
	{
	    boolean status = false;
	    
	    switch(expectedMove)
	    {
    	    case Constants.FIRSTMOVE:
    	        status = isFirstMoveLegal(lastMove, playerString, playerIds);
    	        break;
            case Constants.FIRSTROUNDSETTLEMENT:
                status = isFirstRoundSettlementLegal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    settlementTurn = false;
                }
                break;
            case Constants.FIRSTROUNDROAD:
                status = isFirstRoundRoadLegal(lastMove, lastState, playerString, nextPlayerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    if(playerId == playerIds.get(playerIds.size() - 1))
                    {
                        firstFreeMove = false;
                        secondFreeMove = true;
                    }
                    settlementTurn = true;
                }
                break;
            case Constants.SECONDROUNDSETTLEMENT:
                status = isSecondRoundSettlementLegal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    settlementTurn = false;
                }
                break;
            case Constants.SECONDROUNDROAD:
                status = isSecondRoundRoadLegal(lastMove, lastState, playerString, previousPlayerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    settlementTurn = true;
                    if(playerId == playerIds.get(0))
                    {
                        firstFreeMove = false;
                        secondFreeMove = false;
                        settlementTurn = false;
                    }
                }
                break;
            case Constants.ROLLDICE:
                status = isRollDiceLegal(lastMove, playerString);
                break;
            case Constants.CLEARROLL:
                status = isDiceClearLegal(lastMove, lastState, playerString);
                break;
    	    case Constants.CHANGETURN:
                status = isChangeTurnMoveLegal(lastMove, nextPlayerString, playerIds);
    	        break;
            case Constants.MOVEROBBERPT3:
                status = isMoveRobberMovePt1Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishRobberMove = true;
                }
                break;
            case Constants.MOVEROBBERPT4:
                status = isMoveRobberMovePt2Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishRobberMove = false;
                }
                break;
    	    case Constants.BUILDCITYPT1:
                status = isBuildCityMovePt1Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishCityBuild = true;
                }
                break;
            case Constants.BUILDCITYPT2:
                status = isBuildCityMovePt2Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishCityBuild = true;
                }
                break;
            case Constants.BUILDSETTLEMENTPT1:
                status = isBuildSettlementMovePt1Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishSettlementBuild = true;
                }
                break;
            case Constants.BUILDSETTLEMENTPT2:
                status = isBuildSettlementMovePt2Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishSettlementBuild = false;
                }
                break;
            case Constants.BUILDROADPT1:
                status = isBuildRoadMovePt1Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishRoadBuild = true;
                }
                break;
            case Constants.BUILDROADPT2:
                status = isBuildRoadMovePt2Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishRoadBuild = false;
                }
                break;
            case Constants.BUYDEVELOPMENTCARDPT1:
                status = isBuyDevelopmentCardMovePt1Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishBuyingDevelopmentCard = true;
                }
                break;
            case Constants.BUYDEVELOPMENTCARDPT2:
                status = isBuyDevelopmentCardMovePt2Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishBuyingDevelopmentCard = false;
                }
                break;
            case Constants.PLAYDEVELOPMENTCARD:
                status = isPlayDevelopmentCardMoveLegal(lastMove, lastState, playerString, playerId, playerIds);
                break;
            case Constants.HARBORTRADEPT1:
                status = isHarborTradeMovePt1Legal(lastMove, lastState, playerString, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishHarborTrade = true;
                }
                break;
            case Constants.HARBORTRADEPT2:
                status = isHarborTradeMovePt2Legal(lastMove, lastState, playerString, playerId, playerIds);
                if(status)// && ++verifyCount % playerIds.size() == 0)
                {
                    finishHarborTrade = false;
                }
                break;
            default:
                err = "No Legal Move Detected";
                break;
	    }
	    
	    return status;
	}
	    

    // Parent Function for First Move
    // Determines if this entire move matches for changing turns
    private boolean isFirstMoveLegal(
            List<Operation> lastMove,
            String playerString,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, nextPlayerString)
        // SET(HEX00, resourceList.get(0))
        // ...
        // SET(HEX18, resourceList.get(18))
        // SHUFFLE(HEX00...HEX18)
        // SET(DEV00, developmentCardTypeList.get(0))
        // ...
        // SET(DEV24, developmentCardTypeList.get(24))
        // SHUFFLE(HEX00...HEX18)
        // SETVISIBILITY(DEV00, visibleToNone)
        // ...
        // SETVISIBILITY(DEV24, visibleToNone)
        // SET(MAR00, harborTradeTypeList.get(0))
        // ...
        // SET(MAR08, harborTradeTypeList.get(8))
        // SHUFFLE(MAR00...MAR08)
        // SET(SOLDIERCOUNTPB, 0)
        // SET(SOLDIERCOUNTPR, 0)
        // OPTIONAL - SET(SOLDIERCOUNTPY, 0)
        // OPTIONAL - SET(SOLDIERCOUNTPG, 0)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1-19 - SET must match HEXXX to resourceListXX
        // Line 20 - Shuffle HEX list
        // Lines 21-45 - SET must match DEVXX to developmentCardTypeListXX
        // Line 46 - Shuffle DEV list
        // Lines 47-71 - SETVISIBLE DEV list to visibleToNone
        // Lines 72-80 - SET must match MARXX to harborTradeTypeListXX
        // Line 81 - Shuffle MAR list
        // Line 82-85 - SET soldiercounts for each player
        
        // Ensure move is between 83-85 lines
        boolean status = (lastMove.size() == 84 && playerIds.size() == 2)
                      || (lastMove.size() == 85 && playerIds.size() == 3)
                      || (lastMove.size() == 86 && playerIds.size() == 4);
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1-19
            for(int i = 0; i < 19; i++)
            {
                status = status
                        && matchHexToInitialResource(
                                lastMove.get(i + 1), i);
            }
            
            // Line 20
            status = status
                    && matchHexShuffle(
                            lastMove.get(20),20);
            

            // Lines 21 - 45
            for(int i = 0; i < 25; i++)
            {
                status = status
                        && matchDevelopmentCardToInitialType(
                                lastMove.get(i + 21), i);
            }
            
            // Line 46
            status = status
                    && matchDevelopmentCardShuffle(
                            lastMove.get(46),46);

            // Lines 47 - 71
            for(int i = 0; i < 25; i++)
            {
                status = status
                        && matchDevelopmentCardSetVisible(
                                lastMove.get(i + 47), i);
            }
            

            // Lines 72-80
            for(int i = 0; i < 9; i++)
            {
                status = status
                        && matchHarborToInitialType(
                                lastMove.get(i + 72), i);
            }
            
            // Line 81
            status = status
                    && matchHarborShuffle(
                            lastMove.get(81),81);
            
            // Line 82
            status = status
                    && matchInitSoldierCount(
                            lastMove.get(82),82, Constants.SOLDIERCOUNTTOKEN + Constants.PB);
            
            // Line 83
            status = status
                    && matchInitSoldierCount(
                            lastMove.get(83),83, Constants.SOLDIERCOUNTTOKEN + Constants.PR);
            
            if(lastMove.size() > 84)
            {
                // Line 84
                status = status
                        && matchInitSoldierCount(
                                lastMove.get(84),84, Constants.SOLDIERCOUNTTOKEN + Constants.PY);
            
                if(lastMove.size() > 85)
                {
                    // Line 85
                    status = status
                            && matchInitSoldierCount(
                                    lastMove.get(85),85, Constants.SOLDIERCOUNTTOKEN + Constants.PG);
                }
            }
            
            if(status)
            {
                this.playerIds = playerIds;
            }
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "CHANGETURN expects: 1";
        }
        
        return status;
    }
    
    private boolean isFirstRoundSettlementLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SETTURN(playerId)
        // SET(NODEXX, SETTLEMENTYY + playerString)
        // SET(SETTLEMENTYY + playerString, NODEXX)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - NODE and SETTLEMENT must be the same
        //             NODE must be empty prior
        
        // Ensure move is exactly 3 lines
        boolean status = lastMove.size() == 3;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1,2
            status = status
                  && matchSettlementAndNodePairFree(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString);
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDSETTLEMENT expects: 3";
        }
        
        return status;
        
    }
    
    private boolean isFirstRoundRoadLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String nextPlayerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SETTURN(playerId)
        // SET(PATHXX, ROADYY + playerString)
        // SET(ROADYY + playerString, PATHXX)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - PATH and ROAD must be the same
        //             PATH must be empty prior
        
        // Ensure move is exactly 3 lines
        boolean status = lastMove.size() == 3;
        
        if(status)
        {
            if(playerIds.get(playerIds.size()-1).equals(playerId))
            {
                // Line 0
                status = status
                      && matchTurnMoveForSamePlayer(
                              lastMove.get(0), 0, 
                              playerString,
                              playerIds);
            }
            else
            {
                // Line 0
                status = status
                      && matchTurnMoveForSamePlayer(
                              lastMove.get(0), 0, 
                              nextPlayerString,
                              playerIds);
            }
            
            // Lines 1,2
            status = status
                  && matchRoadAndPathPair(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString);
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDROADFIRST expects: 3";
        }
        
        return status;
        
    }
    
    private boolean isSecondRoundSettlementLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SETTURN(playerId)
        // SET(NODEXX, SETTLEMENTYY + playerString)
        // SET(SETTLEMENTYY + playerString, NODEXX)
        // OPTIONAL - SET(RESOURCECARDXX, RESOURCE)
        // OPTIONAL - SETVISIBILITY(RESOURCECARDXX, playerId)
        // UP TO 3 pairs of those
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - NODE and SETTLEMENT must be the same
        //             NODE must be empty prior
        // Lines 3,4,5,6,7,8 - Optional set pairs for resources
        
        // Ensure move is exactly 3,5,7,9 lines
        boolean status = lastMove.size() == 3
                      || lastMove.size() == 5
                      || lastMove.size() == 7
                      || lastMove.size() == 9;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1,2
            status = status
                  && matchSettlementAndNodePairFree(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString);
            
            List<String> expectedResources = getExpectedResourcesFromNode(
                    lastState,
                    ((Set)lastMove.get(1)).getKey());
            
            // Line 3
            if(expectedResources.size() > 0)
            {
                status = status
                      && matchResourceCardAdd(
                              lastMove.get(3), 3,
                              lastMove.get(4), 4,
                              lastState, 
                              playerString,
                              playerId,
                              expectedResources.get(0));

                // Line 4
                if(expectedResources.size() > 1)
                {
                    status = status
                          && matchResourceCardAdd(
                                  lastMove.get(5), 5,
                                  lastMove.get(6), 6,
                                  lastState, 
                                  playerString,
                                  playerId,
                                  expectedResources.get(1));

                    // Line 5
                    if(expectedResources.size() > 2)
                    {
                        status = status && expectedResources.size() > 2
                              && matchResourceCardAdd(
                                      lastMove.get(7), 7,
                                      lastMove.get(8), 8,
                                      lastState, 
                                      playerString,
                                      playerId,
                                      expectedResources.get(2));
                    }
                }
            }
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDSETTLEMENT expects: 3";
        }
        
        return status;
        
    }
    
    private boolean isSecondRoundRoadLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String previousPlayerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SETTURN(playerId)
        // SET(PATHXX, ROADYY + playerString)
        // SET(ROADYY + playerString, PATHXX)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - PATH and ROAD must be the same
        //             PATH must be empty prior
        
        // Ensure move is exactly 3 lines
        boolean status = lastMove.size() == 3;
        
        if(status)
        {
            if(playerIds.get(0).equals(playerId))
            {
                // Line 0
                status = status
                      && matchTurnMoveForSamePlayer(
                              lastMove.get(0), 0, 
                              playerString,
                              playerIds);
            }
            else
            {
                // Line 0
                status = status
                      && matchTurnMoveForSamePlayer(
                              lastMove.get(0), 0, 
                              previousPlayerString,
                              playerIds);
            }
            
            // Lines 1,2
            status = status
                  && matchRoadAndPathPair(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString);
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDROADFIRST expects: 3";
        }
        
        return status;
        
    }
    
    // Parent Function for Roll Dice
    // Determines if this entire move matches for rolling dice
    private boolean isRollDiceLegal(
            List<Operation> lastMove,
            String playerString)
    {
        // EXPECTED MOVE FORM
        // SETTURN(TURN, playerString)
        // SETRANDOMINTEGER(DIE0, 1, 6)
        // SETRANDOMINTEGER(DIE1, 1, 6)
        
        // REQUIREMENTS
        // Line 0 - Turn must match nextPlayerString
        // Line 1 - Sets Die 0 to a random int 1-6
        // Line 2 - Sets Die 1 to a random int 1-6
        
        // Ensure move is exactly 3 lines
        boolean status = lastMove.size() == 3;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);

            // Line 1
            status = status
                  && matchDieRoll(
                          lastMove.get(1), 1, 
                          Constants.DIE0);

            // Line 2
            status = status
                  && matchDieRoll(
                          lastMove.get(2), 2, 
                          Constants.DIE1);
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "CHANGETURN expects: 1";
        }
        
        return status;
    }
    
    // Parent Function for Dice Clear
    // Determines if this entire move matches for clearing dice roll
    private boolean isDiceClearLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString)
    {
        // EXPECTED MOVE FORM
        // SETTURN(TURN, playerString)
        // DELETE(DIE0)
        // DELETE(DIE1)
        // OPTIONAL - SET(RESOURCECARDXX, RESOURCE)
        // OPTIONAL - SETVISIBILITY(RESOURCECARDXX, playerId)
        
        // REQUIREMENTS
        // Line 0 - Turn must match nextPlayerString
        // Line 1 - Deletes Die0
        // Line 2 - Deletes Die1
        // Lines 3+ - Adds resources based on die roll
        
        // Ensure move is at least 3 lines
        boolean status = lastMove.size() >= 3;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);

            // Line 1
            status = status
                  && matchDieClear(
                          lastMove.get(1), 1,
                          lastState,
                          Constants.DIE0);

            // Line 2
            status = status
                  && matchDieClear(
                          lastMove.get(2), 2,
                          lastState,
                          Constants.DIE1);
            
            for(int i = 3; (i+1) < lastMove.size(); i = i + 2 )
            {
                // Lines i, i+1
                status = status
                      && matchResourceCardAddIgnoreExpected(
                              lastMove.get(i), i,
                              lastMove.get(i+1), i+1,
                              lastState);
            }
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "CHANGETURN expects: 1";
        }
        
        return status;
    }
    
    // Parent Function for Change Turn
    // Determines if this entire move matches for changing turns
    private boolean isChangeTurnMoveLegal(
            List<Operation> lastMove,
            String nextPlayerString,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, nextPlayerId)
        
        // REQUIREMENTS
        // Line 0 - Turn must match nextPlayerString
        
        // Ensure move is exactly 14 lines
        boolean status = lastMove.size() == 1;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          nextPlayerString,
                          playerIds);
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "CHANGETURN expects: 1";
        }
        
        return status;
    }

    // Parent Function for Moving Robber
    // Determines if this entire move matches for moving a robber
    private boolean isMoveRobberMovePt1Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // SET(ROBBER, HEX)
        // OPTIONAL - SETVISIBILITY(RESOURCECARDAA + playerString)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Line 1 - Move Robber
        // Line 2 - AA, BB, CC, DD, EE must be 3 ore, 2 grain
        
        // Ensure move is exactly 2 lines
        boolean status = lastMove.size() == 2
                      || lastMove.size() == 3;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Line 1
            status = status
                  && matchRobberSolo(
                          lastMove.get(1), 1,
                          lastState, 
                          playerString);
            
            if(lastMove.size() == 3)
            {
                
                // Lines 2
                status = status
                      && matchAHiddenResource(
                              lastMove.get(2), 2,
                              lastState, 
                              playerString);
            }
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "MOVEROBBER expects: 2";
        }
        
        return status;
    }
    
 // Parent Function for Moving Robber
    // Determines if this entire move matches for moving a robber
    private boolean isMoveRobberMovePt2Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // OPTIONAL - DELETE(RESOURCECARD not yours)
        // OPTIONAL - SET(RESOURCECARDXX, RESOURCE)
        // OPTIONAL - SETVISIBILITY(RESOURCECARDXX, playerId)
        
        // REQUIREMENTS
        // Line 0 - Must not be owned by player
        // Lines 2,3 - ResourceCard pair
        
        // Ensure move is exactly 0 lines
        boolean status = lastMove.size() == 0
                      || lastMove.size() == 3;
        
        if(status && lastMove.size() == 3)
        {
            // Line 0
            status = status
                  && matchASingleDelete(
                          lastMove.get(0), 0, 
                          lastState,
                          playerString);
            
            // Line 1
            status = status
                  && matchResourceCardAddIgnoreExpected(
                          lastMove.get(1), 1,
                          lastMove.get(2), 2,
                          lastState);
        }
        else if (status)
        {
            // Do nothing and like it
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "MOVEROBBER expects: 2";
        }
        
        return status;
    }
    
    // Parent Function for Building City
    // Determines if this entire move matches for building a city
    private boolean isBuildCityMovePt1Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // DELETE(SETTLEMENTZZ + playerString)
        // SET(NODEXX, CITYYY + playerString)
        // SET(CITYYY + playerString, NODEXX)
        // SETVISIBILITY(RESOURCECARDAA + playerString)
        // SETVISIBILITY(RESOURCECARDBB + playerString)
        // SETVISIBILITY(RESOURCECARDCC + playerString)
        // SETVISIBILITY(RESOURCECARDDD + playerString)
        // SETVISIBILITY(RESOURCECARDEE + playerString)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2,3 - NODE and CITY must be the same
        //               NODE must contain SETTLEMENT
        //               CITY must not have an attached different NODE prior
        //               SETTLEMENT must have an attached this NODE prior
        // Lines 4,5,6,7,8 - AA, BB, CC, DD, EE must be 3 ore, 2 grain
        
        // Ensure move is exactly 9 lines
        boolean status = lastMove.size() == 9;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1,2,3
            status = status
                  && matchCitySettlementAndNodeTrio(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastState, 
                          playerString);
            
            // Lines 4,5,6,7,8,9,10,11,12,13
            status = status
                  && matchCityResourcesPt1(
                          lastMove.get(4), 4, 
                          lastMove.get(5), 5, 
                          lastMove.get(6), 6, 
                          lastMove.get(7), 7, 
                          lastMove.get(8), 8, 
                          lastState,
                          playerString
                          );
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDCITY expects: 14";
        }
        
        return status;
    }
    
    // Parent Function for Building City
    // Determines if this entire move matches for building a city
    private boolean isBuildCityMovePt2Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // DELETE(RESOURCECARDCC + playerString)
        // DELETE(RESOURCECARDDD + playerString)
        // DELETE(RESOURCECARDEE + playerString)
        // OPTIONAL ENDGAME(playerId)
        
        // REQUIREMENTS
        // Lines 0,1,2,3,4 - AA, BB, CC, DD, EE must be 3 ore, 2 grain
        // Line 5 - Asserts the game has ended for playerId
        
        // Ensure move is exactly 5 or 6 lines
        boolean status = lastMove.size() == 5
                      || lastMove.size() == 6;
        
        if(status)
        {
            
            // Lines 0,1,2,3,4
            status = status
                  && matchCityResourcesPt2(
                          lastMove.get(0), 0, 
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastState,
                          playerString
                          );
            
            if(lastMove.size() == 6)
            {
                // Line 14
                status = status
                      && matchEndGame(
                              lastMove.get(5), 5, 
                              lastState,
                              playerString,
                              playerId,
                              Constants.ADDCITY);
            }
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDCITY expects: 5";
        }
        
        return status;
    }

    // Parent Function for Building Settlements
    // Determines if this entire move matches for building a settlement
    private boolean isBuildSettlementMovePt1Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // SET(NODEXX, SETTLEMENTYY + playerString)
        // SET(SETTLEMENTYY + playerString, NODEXX)
        // SETVISIBILITY(RESOURCECARDAA + playerString)
        // SETVISIBILITY(RESOURCECARDBB + playerString)
        // SETVISIBILITY(RESOURCECARDCC + playerString)
        // SETVISIBILITY(RESOURCECARDDD + playerString)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - NODE and SETTLEMENT must be the same
        //             NODE must be empty prior
        //             NODE must pass canAddNodeHere
        //             SETTLEMENT must not have an attached NODE prior
        // Lines 3,4,5,6 - AA, BB, CC, DD must be 1 brick, 1 lumber, 1 wool, 1 grain
        
        // Ensure move is exactly 11 or 12 lines
        boolean status = lastMove.size() == 11
                      || lastMove.size() == 12;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1,2
            status = status
                  && matchSettlementAndNodePair(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString);
            
            // Lines 3,4,5,6
            status = status
                  && matchSettlementResourcesPt1(
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastMove.get(5), 5, 
                          lastMove.get(6), 6, 
                          lastState,
                          playerString
                          );
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDSETTLEMENT expects: 11";
        }
        
        return status;
    }
    
 // Parent Function for Building Settlements
    // Determines if this entire move matches for building a settlement
    private boolean isBuildSettlementMovePt2Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // DELETE(RESOURCECARDCC + playerString)
        // DELETE(RESOURCECARDDD + playerString)
        // OPTIONAL ENDGAME(playerId)
        
        // REQUIREMENTS
        // Lines 0,1,2,3 - AA, BB, CC, DD must be 1 brick, 1 lumber, 1 wool, 1 grain
        // Line 4 - Asserts the game has ended for playerId
        
        // Ensure move is exactly 11 or 12 lines
        boolean status = lastMove.size() == 4
                      || lastMove.size() == 5;
        
        if(status)
        {
            // Lines 0,1,2,3
            status = status
                  && matchSettlementResourcesPt2(
                          lastMove.get(0), 0, 
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastState,
                          playerString
                          );
            
            if(lastMove.size() == 5)
            {
                // Line 4
                status = status
                      && matchEndGame(
                              lastMove.get(4), 4, 
                              lastState,
                              playerString,
                              playerId,
                              Constants.ADDSETTLEMENT);
            }
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDSETTLEMENT expects: 11";
        }
        
        return status;
    }

    // Parent Function for Building Roads
    // Determines if this entire move matches for building a road
    private boolean isBuildRoadMovePt1Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // SET(PATHXX, ROADYY + playerString)
        // SET(ROADYY + playerString, PATHXX)
        // SETVISIBILITY(RESOURCECARDAA + playerString)
        // SETVISIBILITY(RESOURCECARDBB + playerString)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - PATH and ROAD must be the same
        //             PATH must be empty prior
        //             PATH must pass canAddRoadHere
        //             ROAD must not have an attached NODE prior
        // Lines 3,4 - AA, BB, must be 1 brick, 1 lumber
        
        // Ensure move is exactly 5 lines
        boolean status = lastMove.size() == 5;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1,2
            status = status
                  && matchRoadAndPathPair(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString);
            
            // Lines 3,4,5,6
            status = status
                  && matchRoadResourcesPt1(
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastState,
                          playerString
                          );
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDROAD expects: 5";
        }
        
        return status;
    }
    
    // Parent Function for Building Roads
    // Determines if this entire move matches for building a road
    private boolean isBuildRoadMovePt2Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // OPTIONAL - SET(LONGESTROAD, playerString)
        // OPTIONAL - ENDGAME(playerId)
        
        // REQUIREMENTS
        // Lines 1,2 - AA, BB, must be 1 brick, 1 lumber
        // Line 3 - Asserts the game has ended for playerId
        
        // Ensure move is exactly 2,3,4 lines
        boolean status = lastMove.size() == 2
                      || lastMove.size() == 3
                      || lastMove.size() == 4;
        
        if(status)
        {
            // Lines 0,1
            status = status
                  && matchRoadResourcesPt2(
                          lastMove.get(0), 0, 
                          lastMove.get(1), 1, 
                          lastState,
                          playerString
                          );
            
            if(lastMove.size() > 2)
            {                
                // Line 2
                status = status
                      && processLongestRoadClaim(
                              lastMove.get(2), 2, 
                              lastState,
                              playerString);
                
                if(lastMove.size() == 4)
                {
                    // Line 3
                    status = status
                          && matchEndGame(
                                  lastMove.get(3), 3, 
                                  lastState,
                                  playerString,
                                  playerId,
                                  Constants.ADDLONGESTROAD);
                }
            }
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUILDROAD expects: 2";
        }
        
        return status;
    }

    // Parent Function for Buying Development Cards
    // Determines if this entire move matches for buying development cards
    private boolean isBuyDevelopmentCardMovePt1Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + playerId)
        // SETVISIBILITY(RESOURCECARDAA + playerString)
        // SETVISIBILITY(RESOURCECARDBB + playerString)
        // SETVISIBILITY(RESOURCECARDCC + playerString)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Line 1 - DEVELOPMENTCARDXX must be the first card available
        //          Must assign the DEVELOPMENTCARDXX to playerString
        // Lines 2,3,4 - AA, BB, CC, must be 1 ore, 1 grain, 1 wool
        
        // Ensure move is exactly 8 lines
        boolean status = lastMove.size() == 5;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1
            status = status
                  && matchDevelopmentCardandDevelopmentCardTypePair(
                          lastMove.get(1), 1, 
                          lastState, 
                          playerString,
                          playerId);
            
            // Lines 2,3,4
            status = status
                  && matchBuyDevelopmentCardResourcesPt1(
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastState,
                          playerString
                          );
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUYDEVELOPMENTCARD expects: 5";
        }
        
        return status;
    }
    
   // Parent Function for Buying Development Cards
    // Determines if this entire move matches for buying development cards
    private boolean isBuyDevelopmentCardMovePt2Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // DELETE(RESOURCECARDCC + playerString)
        
        // REQUIREMENTS
        // Lines 0,1,2 - AA, BB, CC, must be 1 ore, 1 grain, 1 wool
        
        // Ensure move is exactly 3 lines
        boolean status = lastMove.size() == 3;
        
        if(status)
        {
            // Lines 0,1,2
            status = status
                  && matchBuyDevelopmentCardResourcesPt2(
                          lastMove.get(0), 0, 
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState,
                          playerString
                          );
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUYDEVELOPMENTCARD expects: 3";
        }
        
        return status;
    }

    // Parent Function for Play Development Card
    // Determines if this entire move matches for playing a development card
    private boolean isPlayDevelopmentCardMoveLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // There are 4 types of cards that can be played

        // EXPECTED PLAY SOLDIER FORM
        // SET(TURN, playerString)
        // SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + nobody)
        // SET(ROBBER, HEX)
        // SET(SOLDIERCOUNT + playerId, VAL)
        // OPTIONAL - SET(LARGESTARMY, playerString)
        // OPTIONAL - ENDGAME(playerId)
        //
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Line 1 - DEVELOPMENTCARDXX must be owned by playerString
        //          Must assign DEVELOPMENTCARDXX to visible nobody
        // Line 2 - Determine where to move the robber
        //          Must be different than the current robber HEX
        // Line 3 - Update the SOLDIERCOUNT for the current player
        //          Must increment the previous value
        // Line 4 - Set the LARGESTARMY to the current player.
        //          This line only happens when the SOLDIERCOUNT set
        //          earlier is one greater than the current holder of
        //          LARGESTARMY
        // Line 5 - Sets the ENDGAME for the current player.
        //          This line only happens if LARGESTARMY is set
        //          for the current player and it pushes the current player
        //          at or over 10 victory points
        
        boolean status = false;
        
        if( findASetMoveInMoves(lastMove, Constants.ROBBER, "")
         && ( lastMove.size() == 4
           || lastMove.size() == 5
           || lastMove.size() == 6 ) )
        {
            // Line 0
            status = matchTurnMoveForSamePlayer(
                            lastMove.get(0), 0, 
                            playerString,
                            playerIds);
            
            // Line 1
            status = status
                  && matchDevelopmentCardandDevelopmentCardTypePlayedPair(
                          lastMove.get(1), 1,
                          lastState, 
                          playerString,
                          Constants.DEVELOPMENTCARDTYPEDEF00);
              
              // Lines 2,3
              status = status
                    && matchRobber(
                            lastMove.get(2), 2,
                            lastMove.get(3), 3,
                            lastState,
                            playerString
                            );
              
              if(lastMove.size() > 4)
              {
                  // Create a new state with the path added, to verify longest road claim
                  Map<String, Object> newState = new HashMap<String, Object>(lastState);
                  newState.put(((Set)lastMove.get(3)).getKey(), ((Set)lastMove.get(3)).getValue());
                  
                  // Line 4
                  status = status
                        && processLargestArmyClaim(
                                lastMove.get(4), 4, 
                                newState,
                                playerString,
                                ((Set)lastMove.get(3)).getKey());
                  
                  if(lastMove.size() == 6)
                  {
                      // Line 5
                      status = status
                            && matchEndGame(
                                    lastMove.get(5), 5, 
                                    newState,
                                    playerString,
                                    playerId,
                                    Constants.ADDLONGESTROAD);
                  }
              }
        }

        // EXPECTED YEAR OF PLENTY FORM
        // SET(TURN, playerString)
        // SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + nobody)
        // SET(RESOURCECARDAA + playerString, resource)
        // SET(RESOURCECARDBB + playerString, resource)
        //
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Line 1 - DEVELOPMENTCARDXX must be owned by playerString
        //          Must assign DEVELOPMENTCARDXX to visible nobody
        // Lines 2,3 - Two RESOURCE cards for playerString to choose

        else if( findASetMoveInMoves(lastMove, Constants.RESOURCECARDTOKEN, "")
              && lastMove.size() == 4 )
         {
             // Line 0
             status = matchTurnMoveForSamePlayer(
                             lastMove.get(0), 0, 
                             playerString,
                             playerIds);
             
             // Line 1
             status = status
                   && matchDevelopmentCardandDevelopmentCardTypePlayedPair(
                           lastMove.get(1), 1, 
                           lastState, 
                           playerString,
                           Constants.DEVELOPMENTCARDTYPEDEF01);
               
               // Lines 2
               status = status
                     && matchAddResource(
                             lastMove.get(2), 2,
                             lastState,
                             playerString
                             );
               
               // Lines 3
               status = status
                     && matchAddResource(
                             lastMove.get(3), 3,
                             lastState,
                             playerString
                             );
         }
        
        // EXPECTED MONOPOLY FORM
        // SET(TURN, playerString)
        // SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + nobody)
        // SET(MONOPOLYRESOURCE, resource)
        // SET(MONOPOLYBENEFACTOR, playerString)
        //
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Line 1 - DEVELOPMENTCARDXX must be owned by playerString
        //          Must assign DEVELOPMENTCARDXX to visible nobody
        // Line 2 - Assign the MONOPOLYRESOURCE to a specific resource
        // Line 3 - Assign the MONOPOLYBENEFACTOR to playerString

        else if( findASetMoveInMoves(lastMove, Constants.MONOPOLYRESOURCE, "")
                && lastMove.size() == 4 )
        {
            // Line 0
            status = matchTurnMoveForSamePlayer(
                            lastMove.get(0), 0, 
                            playerString,
                            playerIds);
           
            // Line 1
            status = status
                    && matchDevelopmentCardandDevelopmentCardTypePlayedPair(
                            lastMove.get(1), 1, 
                            lastState, 
                            playerString,
                            Constants.DEVELOPMENTCARDTYPEDEF02);
             
            // Lines 2,3
            status = status
                    && matchMonopoly(
                            lastMove.get(2), 2,
                            lastMove.get(3), 3,
                            lastState,
                            playerString
                            );
        }
        
        // EXPECTED ROAD BUILDING
        // SET(TURN, playerString)
        // SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + nobody)
        // SET(PATHXX, ROADYY + playerString)
        // SET(ROADYY + playerString, PATHXX)
        // SET(PATHZZ, ROADTT + playerString)
        // SET(ROADTT + playerString, PATHZZ)
        // OPTIONAL - SET(LONGESTROAD, playerString)
        // OPTIONAL - ENDGAME(playerId)
        //
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Line 1 - DEVELOPMENTCARDXX must be owned by playerString
        //          Must assign DEVELOPMENTCARDXX to visible nobody
        // Lines 2,3 - PATH and ROAD must be the same
        //             PATH must be empty prior
        //             PATH must pass canAddRoadHere
        //             ROAD must not have an attached NODE prior
        // Lines 4,5 - PATH and ROAD must be the same
        //             PATH must be empty prior
        //             PATH must pass canAddRoadHere
        //             ROAD must not have an attached NODE prior
        // Line 6 - Set the LONGESTROAD to the current player.
        //          This line only happens when the length of the
        //          current player's longest road is at least
        //          one greater than the current holder of
        //          LONGESTROAD
        // Line 7 - Sets the ENDGAME for the current player.
        //          This line only happens if LARGESTARMY is set
        //          for the current player and it pushes the current player
        //          at or over 10 victory points
        
        else if( findASetMoveInMoves(lastMove, Constants.PATHTOKEN, playerString)
                && ( lastMove.size() == 6
                  || lastMove.size() == 7
                  || lastMove.size() == 8 ) )
         {
             // Line 0
             status = matchTurnMoveForSamePlayer(
                             lastMove.get(0), 0, 
                             playerString,
                             playerIds);
             
             // Line 1
             status = status
                   && matchDevelopmentCardandDevelopmentCardTypePlayedPair(
                           lastMove.get(1), 1, 
                           lastState, 
                           playerString,
                           Constants.DEVELOPMENTCARDTYPEDEF03);

             // Lines 2,3
             status = status
                   && matchRoadAndPathPair(
                           lastMove.get(2), 2, 
                           lastMove.get(3), 3, 
                           lastState, 
                           playerString);

             // Lines 4,5
             status = status
                   && matchRoadAndPathPair(
                           lastMove.get(4), 4, 
                           lastMove.get(5), 5, 
                           lastState, 
                           playerString);
               
               if(lastMove.size() > 6)
               {
                   // Create a new state with the paths added, to verify longest road claim
                   Map<String, Object> newState = new HashMap<String, Object>(lastState);
                   newState.put(((Set)lastMove.get(2)).getKey(), ((Set)lastMove.get(2)).getValue());
                   newState.put(((Set)lastMove.get(3)).getKey(), ((Set)lastMove.get(3)).getValue());
                   newState.put(((Set)lastMove.get(4)).getKey(), ((Set)lastMove.get(4)).getValue());
                   newState.put(((Set)lastMove.get(5)).getKey(), ((Set)lastMove.get(5)).getValue());
                   
                   // Line 6
                   status = status
                         && processLongestRoadClaim(
                                 lastMove.get(6), 6, 
                                 newState,
                                 playerString);
                   
                   if(lastMove.size() == 9)
                   {
                       // Line 7
                       status = status
                             && matchEndGame(
                                     lastMove.get(7), 7, 
                                     newState,
                                     playerString,
                                     playerId,
                                     Constants.ADDLONGESTROAD);
                   }
               }
         }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "BUYDEVELOPMENTCARD expects: 8";
        }
        
        return status;
    }
    
    // Parent Function for Harbor Trades
    // Determines if this entire move matches for a harbor trade
    private boolean isHarborTradeMovePt1Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // SETVISIBILITY(RESOURCECARDAA + playerString)
        // SETVISIBILITY(RESOURCECARDBB + playerString)
        // ***
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // 
        // DIFFERING BRANCHES
        //
        // Normal Harbor trade
        // Lines 1,2,3,4 - AA, BB, CC, DD must be the same resource
        // 
        // Three to One Harbor trade
        // Lines 1,2,3 - AA, BB, CC, must be the same resource
        //               playerString must own a city or settlement
        //               on a harbor allowing 3-1 trading
        // 
        // Two to One Specific Resource Harbor trade
        // Lines 1,2 - AA, BB, must be the same resource
        //             playerString must own a city or settlement
        //             on a harbor allowing 2-1 trading
        //             That harbor must be specifically catered
        //             to the resources being traded
        
        // Ensure move is exactly 3,4,5 lines
        boolean status = lastMove.size() == 3
                      || lastMove.size() == 4
                      || lastMove.size() == 5;
        
        if(status && lastMove.size() == 5)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1,2,3,4
            status = status
                  && matchNormalHarborTradeResourcesPt1(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastState,
                          playerString
                          );
        }
        else if(status && lastMove.size() == 4)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1,2,3
            status = status
                  && matchThreeToOneHarborTradeResourcesPt1(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastState,
                          playerString
                          );
        }
        else if(status && lastMove.size() == 3)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString,
                          playerIds);
            
            // Lines 1,2
            status = status
                  && matchTwoToOneHarborTradeResourcesPt1(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2,
                          lastState,
                          playerString
                          );
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "HARBORTRADE expects: 3,4,5";
        }
        
        return status;
    }
    
 // Parent Function for Harbor Trades
    // Determines if this entire move matches for a harbor trade
    private boolean isHarborTradeMovePt2Legal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            List<String> playerIds)
    {
        // EXPECTED MOVE FORM
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // ***
        // SET(RESOURCECARDAA+ playerString, resource)
        // SETVISIBLE(RESOURCECARDAA+ playerString, list only player)
        
        // REQUIREMENTS
        //
        // Normal Harbor trade
        // Lines 0,1,2,3 - AA, BB, CC, DD must be the same resource
        // Line 4,5 - New resource requested
        // 
        // Three to One Harbor trade
        // Lines 0,1,2 - AA, BB, CC, must be the same resource
        //               playerString must own a city or settlement
        //               on a harbor allowing 3-1 trading
        // Line 3,4 - New resource requested
        // 
        // Two to One Specific Resource Harbor trade
        // Lines 0,1 - AA, BB, must be the same resource
        //             playerString must own a city or settlement
        //             on a harbor allowing 2-1 trading
        //             That harbor must be specifically catered
        //             to the resources being traded
        // Line 2,3 - New resource requested
        
        // Ensure move is exactly 3,4,5 lines
        boolean status = lastMove.size() == 4
                      || lastMove.size() == 5
                      || lastMove.size() == 6;
        
        if(status && lastMove.size() == 6)
        {
            // Lines 0,1,2,3,4
            status = status
                  && matchNormalHarborTradeResourcesPt2(
                          lastMove.get(0), 0, 
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4,
                          lastMove.get(5), 5,
                          lastState,
                          playerString,
                          playerId
                          );
        }
        else if(status && lastMove.size() == 5)
        {
            // Lines 0,1,2,3
            status = status
                  && matchThreeToOneHarborTradeResourcesPt2(
                          lastMove.get(0), 0, 
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4,
                          lastState,
                          playerString,
                          playerId
                          );
        }
        else if(status && lastMove.size() == 4)
        {
            // Lines 0,1,2
            status = status
                  && matchTwoToOneHarborTradeResourcesPt2(
                          lastMove.get(0), 0, 
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3,
                          lastState,
                          playerString,
                          playerId
                          );
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "HARBORTRADE expects: 3,4,5";
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a set hex matches
    private boolean matchHexToInitialResource(
            Operation move, int moveNum)
    {
        boolean status = false;
        
        String moveString = "";
        if(moveNum < 10)
            moveString = Constants.HEXTOKEN + "0" + moveNum;
        else
            moveString = Constants.HEXTOKEN + moveNum;

        if(!move.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "MATCHHEX expects: SET(HEXXX, resourceList.get(XX))\n"
                + "Set move expected";
        }
        else if(!((Set)move).getKey().equals(moveString))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "MATCHHEX expects: SET(HEXXX, resourceList.get(XX))\n"
                    + "HEX name does not match";
        }
        else if(!((Set)move).getValue().toString().equals(resourceList.get(moveNum)))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "MATCHHEX expects: SET(HEXXX, resourceList.get(XX))\n"
                    + "HEX value does not match";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a set developmentCard matches
    private boolean matchDevelopmentCardToInitialType(
            Operation move, int moveNum)
    {
        boolean status = false;
        
        String moveString = "";
        if(moveNum < 10)
            moveString = Constants.DEVELOPMENTCARDTOKEN + "0" + moveNum;
        else
            moveString = Constants.DEVELOPMENTCARDTOKEN + moveNum;

        if(!move.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "MATCHDEV expects: SET(DEVXX, developmentCardTypeList.get(XX))\n"
                + "Set move expected";
        }
        else if(!((Set)move).getKey().equals(moveString))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "MATCHDEV expects: SET(DEVXX, developmentCardTypeList.get(XX))\n"
                    + "DEV name does not match";
        }
        else if(!((Set)move).getValue().toString().equals(developmentCardTypeList.get(moveNum)))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "MATCHDEV expects: SET(DEVXX, developmentCardTypeList.get(XX))\n"
                    + "DEV value does not match";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a set harbor matches
    private boolean matchHarborToInitialType(
            Operation move, int moveNum)
    {
        boolean status = false;
        
        String moveString = Constants.HARBORTRADETOKEN + "0" + moveNum;

        if(!move.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "MATCHMAR expects: SET(MARXX, harborTradeTypeList.get(XX))\n"
                + "Set move expected";
        }
        else if(!((Set)move).getKey().equals(moveString))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "MATCHMAR expects: SET(MARXX, harborTradeTypeList.get(XX))\n"
                    + "MAR name does not match";
        }
        else if(!((Set)move).getValue().toString().equals(harborTradeTypeList.get(moveNum)))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "MATCHMAR expects: SET(MARXX, harborTradeTypeList.get(XX))\n"
                    + "MAR value does not match";
        }
        else
        {
            status = true;
        }
        
        return status;
    }

    // Returns whether the list of moves for a shuffle hex matches
    private boolean matchHexShuffle(
            Operation move, int moveNum)
    {
        boolean status = false;

        if(!move.getMessageName().equals("Shuffle"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "SHUFFLEHEX expects: SHUFFLE(HEX00...HEX18)\n"
                + "Shuffle move expected";
        }
        else if(!((Shuffle)move).getKeys().equals(
                Lists.newArrayList(
                        "HEX00", "HEX01", "HEX02", "HEX03", "HEX04", "HEX05", 
                        "HEX06", "HEX07", "HEX08", "HEX09", "HEX10", "HEX11", 
                        "HEX12", "HEX13", "HEX14", "HEX15", "HEX16", "HEX17", "HEX18")))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "SHUFFLEHEX expects: SHUFFLE(HEX00...HEX18)\n"
                    + "SHUFFLEHEX name does not match";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a shuffle developmentCard matches
    private boolean matchDevelopmentCardShuffle(
            Operation move, int moveNum)
    {
        boolean status = false;

        if(!move.getMessageName().equals("Shuffle"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "SHUFFLEDEV expects: SHUFFLE(DEV00...DEV24)\n"
                + "Shuffle move expected";
        }
        else if(!((Shuffle)move).getKeys().equals(
                Lists.newArrayList(
                        "DEV00", "DEV01", "DEV02", "DEV03", "DEV04",
                        "DEV05", "DEV06", "DEV07", "DEV08", "DEV09",
                        "DEV10", "DEV11", "DEV12", "DEV13", "DEV14",
                        "DEV15", "DEV16", "DEV17", "DEV18", "DEV19",
                        "DEV20", "DEV21", "DEV22", "DEV23", "DEV24")))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "SHUFFLEDEV expects: SHUFFLE(DEV00...DEV24)\n"
                    + "SHUFFLEDEV name does not match";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a shuffle harbor matches
    private boolean matchHarborShuffle(
            Operation move, int moveNum)
    {
        boolean status = false;

        if(!move.getMessageName().equals("Shuffle"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "SHUFFLEMAR expects: SHUFFLE(MAR00...MAR08)\n"
                + "Shuffle move expected";
        }
        else if(!((Shuffle)move).getKeys().equals(
                Lists.newArrayList(
                        "MAR00", "MAR01", "MAR02", "MAR03", "MAR04",
                        "MAR05", "MAR06", "MAR07", "MAR08")))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "SHUFFLEMAR expects: SHUFFLE(MAR00...MAR08)\n"
                    + "SHUFFLEMAR name does not match";
        }
        else
        {
            status = true;
        }
        
        return status;
    }

    // Returns whether the list of moves for a shuffle harbor matches
    private boolean matchInitSoldierCount(
            Operation move, int moveNum, String playerSoldier)
    {
        boolean status = false;

        if(!move.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "SETCOLDIERCOUNT expects: SET(SOLDIERCOUNTPX, 0)\n"
                + "Set move expected";
        }
        else if(!((Set)move).getKey().equals(playerSoldier))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "SETCOLDIERCOUNT expects: SET(SOLDIERCOUNTPX, 0)\n"
                    + "SETCOLDIERCOUNT name does not match";
        }
        else if(!((Set)move).getValue().toString().equals("0"))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "SETCOLDIERCOUNT expects: SET(SOLDIERCOUNTPX, 0)\n"
                    + "SETCOLDIERCOUNT value does not match";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a set developmentCard matches
    private boolean matchDevelopmentCardSetVisible(
            Operation move, int moveNum)
    {
        boolean status = false;
        
        String moveString = "";
        if(moveNum < 10)
            moveString = Constants.DEVELOPMENTCARDTOKEN + "0" + moveNum;
        else
            moveString = Constants.DEVELOPMENTCARDTOKEN + moveNum;

        if(!move.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "SETVISDEV expects: SETVISIBILITY(DEVXX, visibleToNone))\n"
                + "SetVisibility move expected";
        }
        else if(!((SetVisibility)move).getKey().equals(moveString))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "SETVISDEV expects: SETVISIBILITY(DEVXX, visibleToNone))\n"
                    + "SETVISDEV name does not match";
        }
        else if(!((SetVisibility)move).getVisibleToPlayerIds().equals(Constants.visibleToNone))
        {
            err = "Incorrect Move Number: moveNum\n"
                    + "SETVISDEV expects: SETVISIBILITY(DEVXX, visibleToNone))\n"
                    + "SETVISDEV value does not match";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a turn move match
    private boolean matchTurnMoveForSamePlayer(
            Operation move, int moveNum,
            String playerString,
            List<String> playerIds)
    {
        boolean status = false;

        if(!move.getMessageName().equals("SetTurn"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "MATCHSETTURN expects: SET(TURN, playerString)\n"
                + "SetTurn move expected";
        }
        else if(!getPlayerId(playerIds, (((SetTurn)move).getPlayerId())).equals(playerString))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "MATCHSETTURN expects: SET(TURN, playerString)\n"
                + "playerString value expected"
                + "Expected-"+playerString+"Got-"+getPlayerId(playerIds, (((SetTurn)move).getPlayerId()));
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a city, settlement and node match
    private boolean matchCitySettlementAndNodeTrio(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Map<String, Object> lastState,
            String playerString)
    {
        String nodeXX = "";
        String settlementYY = "";
        String cityZZ = "";

        boolean status = false;

        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDCITY expects: DELETE(SETTLEMENTZZ + playerString)\n"
                + "Delete move expected";
        }
        else if(!((Delete)move1).getKey().contains(Constants.SETTLEMENTTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDCITY expects: DELETE(SETTLEMENTZZ + playerString)\n"
                + "Settlement key expected";
        }
        else if(!((Delete)move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDCITY expects: DELETE(SETTLEMENTZZ + playerString)\n"
                + "playerString key expected";
        }
        else if(!lastState.containsKey(((Delete)move1).getKey()))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDCITY expects: DELETE(SETTLEMENTZZ + playerString)\n"
                + "SETTLEMENT is not yet already assigned a NODE";
        }
        else
        {
            settlementYY = ((Delete)move1).getKey();
            
            if(!move2.getMessageName().equals("Set"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDCITY expects: SET(NODEXX, CITYYY + playerString)\n"
                    + "Set move expected";
            }
            else if(!((Set)move2).getKey().contains(Constants.NODETOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDCITY expects: SET(NODEXX, CITYYY + playerString)\n"
                    + "NODE key expected";
            }
            else if(!((Set)move2).getValue().toString().contains(Constants.CITYTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDCITY expects: SET(NODEXX, CITYYY + playerString)\n"
                    + "CITY value expected";
            }
            else if(!((Set)move2).getValue().toString().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDCITY expects: SET(NODEXX, CITYYY + playerString)\n"
                    + "playerString value expected";
            }
            else
            {
                nodeXX = ((Set)move2).getKey();
                cityZZ = ((Set)move2).getValue().toString();

                if(!move3.getMessageName().equals("Set"))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SET(CITYYY + playerString, NODEXX)\n"
                        + "Set move expected";
                }
                else if(!((Set)move3).getKey().equals(cityZZ))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SET(CITYYY + playerString, NODEXX)\n"
                        + "CITY key doesn't match last line";
                }
                else if(!((Set)move3).getValue().toString().contains(nodeXX))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SET(CITYYY + playerString, NODEXX)\n"
                        + "NODE value doesn't match last line";
                }
                else if(!lastState.get(nodeXX).toString().equals(settlementYY))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SET(CITYYY + playerString, NODEXX)\n"
                        + "NODE does not contain SETTLEMENT";
                }
                else if(lastState.containsKey(cityZZ))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SET(CITYYY + playerString, NODEXX)\n"
                        + "CITY is already assigned a NODE";
                }
                else
                {
                    status = true;
                }
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a build city move
    private boolean matchCityResourcesPt1(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Operation move5, int move5Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        String resourceCard1 = "";
        String resourceCard2 = "";
        String resourceCard3 = "";
        String resourceCard4 = "";
        
        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "SetVisibility move expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "RESOURCECARD key expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "playerString key expected";
        }
        else
        {
            resourceCard1 = ((SetVisibility)move1).getKey();
            
            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "SetVisibility move expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(Constants.RESOURCECARDTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD key expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "playerString key expected";
            }
            else if(resourceCard1.equals(((SetVisibility)move2).getKey()))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "Duplicate to resource 1";
            }
            else
            {
                resourceCard2 = ((SetVisibility)move2).getKey();
                
                if(!move3.getMessageName().equals("SetVisibility"))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "SetVisibility move expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(Constants.RESOURCECARDTOKEN))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "RESOURCECARD key expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(playerString))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "playerString key expected";
                }
                else if(resourceCard1.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 1";
                }
                else if(resourceCard2.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 2";
                }
                else
                {
                    resourceCard3 = ((SetVisibility)move3).getKey();
                    
                    if(!move4.getMessageName().equals("SetVisibility"))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "SetVisibility move expected";
                    }
                    else if(!((SetVisibility)move4).getKey().contains(Constants.RESOURCECARDTOKEN))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "RESOURCECARD key expected";
                    }
                    else if(!((SetVisibility)move4).getKey().contains(playerString))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "playerString key expected";
                    }
                    else if(resourceCard1.equals(((SetVisibility)move4).getKey()))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "Duplicate to resource 1";
                    }
                    else if(resourceCard2.equals(((SetVisibility)move4).getKey()))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "Duplicate to resource 2";
                    }
                    else if(resourceCard3.equals(((SetVisibility)move4).getKey()))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "Duplicate to resource 3";
                    }
                    else
                    {
                        resourceCard4 = ((SetVisibility)move4).getKey();
                        
                        if(!move5.getMessageName().equals("SetVisibility"))
                        {
                            err = "Incorrect Move Number: " + move5Num + "\n"
                                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDEE + playerString)\n"
                                + "SetVisibility move expected";
                        }
                        else if(!((SetVisibility)move5).getKey().contains(Constants.RESOURCECARDTOKEN))
                        {
                            err = "Incorrect Move Number: " + move5Num + "\n"
                                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDEE + playerString)\n"
                                + "RESOURCECARD key expected";
                        }
                        else if(!((SetVisibility)move5).getKey().contains(playerString))
                        {
                            err = "Incorrect Move Number: " + move5Num + "\n"
                                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDEE + playerString)\n"
                                + "playerString key expected";
                        }
                        else if(resourceCard1.equals(((SetVisibility)move5).getKey()))
                        {
                            err = "Incorrect Move Number: " + move5Num + "\n"
                                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDEE + playerString)\n"
                                + "Duplicate to resource 1";
                        }
                        else if(resourceCard2.equals(((SetVisibility)move5).getKey()))
                        {
                            err = "Incorrect Move Number: " + move5Num + "\n"
                                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDEE + playerString)\n"
                                + "Duplicate to resource 2";
                        }
                        else if(resourceCard3.equals(((SetVisibility)move5).getKey()))
                        {
                            err = "Incorrect Move Number: " + move5Num + "\n"
                                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDEE + playerString)\n"
                                + "Duplicate to resource 3";
                        }
                        else if(resourceCard4.equals(((SetVisibility)move5).getKey()))
                        {
                            err = "Incorrect Move Number: " + move5Num + "\n"
                                + "BUILDCITY expects: SETVISIBILITY(RESOURCECARDEE + playerString)\n"
                                + "Duplicate to resource 4";
                        }
                        else
                        {
                            status = true;
                        }
                    }
                }
            }
        }
        
        return status;
    }
    
 // Returns whether the list of resources matches for a build city move
    private boolean matchCityResourcesPt2(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Operation move5, int move5Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        int ore = 3;
        int grain = 2;
        
        // Starting a new tree to increase visibility
        // All 5 resource cards are different and correctly formed
            
        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDCITY expects: DELETE(RESOURCECARDAA + playerString)\n"
                + "Delete move expected";
        }
        else if(!move2.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "BUILDCITY expects: DELETE(RESOURCECARDBB + playerString)\n"
                + "Delete move expected";
        }
        else if(!move3.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move3Num + "\n"
                + "BUILDCITY expects: DELETE(RESOURCECARDCC + playerString)\n"
                + "Delete move expected";
        }
        else if(!move4.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move4Num + "\n"
                + "BUILDCITY expects: DELETE(RESOURCECARDDD + playerString)\n"
                + "Delete move expected";
        }
        else if(!move5.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move5Num + "\n"
                + "BUILDCITY expects: DELETE(RESOURCECARDEE + playerString)\n"
                + "Delete move expected";
        }
        else
        {
            String resourceCard1 = ((Delete)move1).getKey();
            String resourceCard2 = ((Delete)move2).getKey();
            String resourceCard3 = ((Delete)move3).getKey();
            String resourceCard4 = ((Delete)move4).getKey();
            String resourceCard5 = ((Delete)move5).getKey();
            switch(lastState.get(resourceCard1).toString())
            {
                case Constants.ORE:
                    ore--;
                    break;
                case Constants.GRAIN:
                    grain--;
                    break;
            }

            switch(lastState.get(resourceCard2).toString())
            {
                case Constants.ORE:
                    ore--;
                    break;
                case Constants.GRAIN:
                    grain--;
                    break;
            }

            switch(lastState.get(resourceCard3).toString())
            {
                case Constants.ORE:
                    ore--;
                    break;
                case Constants.GRAIN:
                    grain--;
                    break;
            }

            switch(lastState.get(resourceCard4).toString())
            {
                case Constants.ORE:
                    ore--;
                    break;
                case Constants.GRAIN:
                    grain--;
                    break;
            }

            switch(lastState.get(resourceCard5).toString())
            {
                case Constants.ORE:
                    ore--;
                    break;
                case Constants.GRAIN:
                    grain--;
                    break;
            }
            
            if(ore != 0)
            {
                err = "Incorrect BUILDCITY Resources\n"
                        + "Incorrect number of ore";
            }
            else if(grain != 0)
            {
                err = "Incorrect BUILDCITY Resources\n"
                        + "Incorrect number of grain";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a settlement and node match
    private boolean matchSettlementAndNodePair(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString)
    {
        String nodeXX = "";
        String settlementYY = "";

        boolean status = false;

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENT expects: SET(NODEXX, SETTLEMENTYY + playerString)\n"
                + "Set move expected";
        }
        else if(!((Set)move1).getKey().contains(Constants.NODETOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENT expects: SET(NODEXX, SETTLEMENTYY + playerString)\n"
                + "NODE key expected";
        }
        else if(!((Set)move1).getValue().toString().contains(Constants.SETTLEMENTTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENT expects: SET(NODEXX, SETTLEMENTYY + playerString)\n"
                + "SETTLEMENT value expected";
        }
        else if(!((Set)move1).getValue().toString().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENT expects: SET(NODEXX, SETTLEMENTYY + playerString)\n"
                + "playerString value expected";
        }
        else
        {
            nodeXX = ((Set)move1).getKey();
            settlementYY = ((Set)move1).getValue().toString();

            if(!move2.getMessageName().equals("Set"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENT expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                    + "Set move expected";
            }
            else if(!((Set)move2).getKey().equals(settlementYY))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENT expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                    + "SETTLEMENT key doesn't match last line";
            }
            else if(!((Set)move2).getValue().toString().contains(nodeXX))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENT expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                    + "NODE value doesn't match last line";
            }
            else if(lastState.containsKey(nodeXX))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                        + "BUILDSETTLEMENT expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                        + "NODE is not empty";
            }
            else if(lastState.containsKey(settlementYY))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                        + "BUILDSETTLEMENT expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                        + "SETTLEMENT is already assigned a NODE";
            }
            else if(!canAddSettlementHere(lastState, nodeXX, playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                        + "BUILDSETTLEMENT expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                        + "NODE is not eligible for SETTLEMENT from playerString";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a settlement and node match
    private boolean matchSettlementAndNodePairFree(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString)
    {
        String nodeXX = "";
        String settlementYY = "";

        boolean status = false;

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENTFREE expects: SET(NODEXX, SETTLEMENTYY + playerString)\n"
                + "Set move expected";
        }
        else if(!((Set)move1).getKey().contains(Constants.NODETOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENTFREE expects: SET(NODEXX, SETTLEMENTYY + playerString)\n"
                + "NODE key expected";
        }
        else if(!((Set)move1).getValue().toString().contains(Constants.SETTLEMENTTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENTFREE expects: SET(NODEXX, SETTLEMENTYY + playerString)\n"
                + "SETTLEMENT value expected";
        }
        else if(!((Set)move1).getValue().toString().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENTFREE expects: SET(NODEXX, SETTLEMENTYY + playerString)\n"
                + "playerString value expected";
        }
        else
        {
            nodeXX = ((Set)move1).getKey();
            settlementYY = ((Set)move1).getValue().toString();

            if(!move2.getMessageName().equals("Set"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENTFREE expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                    + "Set move expected";
            }
            else if(!((Set)move2).getKey().equals(settlementYY))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENTFREE expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                    + "SETTLEMENT key doesn't match last line";
            }
            else if(!((Set)move2).getValue().toString().contains(nodeXX))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENTFREE expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                    + "NODE value doesn't match last line";
            }
            else if(lastState.containsKey(nodeXX))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                        + "BUILDSETTLEMENTFREE expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                        + "NODE is not empty";
            }
            else if(lastState.containsKey(settlementYY))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                        + "BUILDSETTLEMENTFREE expects: SET(SETTLEMENTYY + playerString, NODEXX)\n"
                        + "SETTLEMENT is already assigned a NODE";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a build settlement move
    private boolean matchSettlementResourcesPt1(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        String resourceCard1 = "";
        String resourceCard2 = "";
        String resourceCard3 = "";
        
        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "SetVisibility move expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "RESOURCECARD key expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "playerString key expected";
        }
        else
        {
            resourceCard1 = ((SetVisibility)move1).getKey();
            
            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "SetVisibility move expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(Constants.RESOURCECARDTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD key expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "playerString key expected";
            }
            else if(resourceCard1.equals(((SetVisibility)move2).getKey()))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "Duplicate to resource 1";
            }
            else
            {
                resourceCard2 = ((SetVisibility)move2).getKey();
                
                if(!move3.getMessageName().equals("SetVisibility"))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "SetVisibility move expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(Constants.RESOURCECARDTOKEN))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "RESOURCECARD key expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(playerString))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "playerString key expected";
                }
                else if(resourceCard1.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 1";
                }
                else if(resourceCard2.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 2";
                }
                else
                {
                    resourceCard3 = ((SetVisibility)move3).getKey();
                    
                    if(!move4.getMessageName().equals("SetVisibility"))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "SetVisibility move expected";
                    }
                    else if(!((SetVisibility)move4).getKey().contains(Constants.RESOURCECARDTOKEN))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "RESOURCECARD key expected";
                    }
                    else if(!((SetVisibility)move4).getKey().contains(playerString))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "playerString key expected";
                    }
                    else if(resourceCard1.equals(((SetVisibility)move4).getKey()))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "Duplicate to resource 1";
                    }
                    else if(resourceCard2.equals(((SetVisibility)move4).getKey()))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "Duplicate to resource 2";
                    }
                    else if(resourceCard3.equals(((SetVisibility)move4).getKey()))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "BUILDSETTLEMENT expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "Duplicate to resource 3";
                    }
                    else
                    {
                        status = true;
                    }
                }
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a build settlement move
    private boolean matchSettlementResourcesPt2(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        int grain = 1;
        int lumber = 1;
        int wool = 1;
        int brick = 1;
        
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
            
        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDAA + playerString)\n"
                + "Delete move expected";
        }
        else if(!move2.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDBB + playerString)\n"
                + "Delete move expected";
        }
        else if(!move3.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move3Num + "\n"
                + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDCC + playerString)\n"
                + "Delete move expected";
        }
        else if(!move4.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move4Num + "\n"
                + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDDD + playerString)\n"
                + "Delete move expected";
        }
        else
        {
            String resourceCard1 = ((Delete)move1).getKey();
            String resourceCard2 = ((Delete)move2).getKey();
            String resourceCard3 = ((Delete)move3).getKey();
            String resourceCard4 = ((Delete)move4).getKey();
            
            switch(lastState.get(resourceCard1).toString())
            {
                case Constants.GRAIN:
                    grain--;
                    break;
                case Constants.LUMBER:
                    lumber--;
                    break;
                case Constants.WOOL:
                    wool--;
                    break;
                case Constants.BRICK:
                    brick--;
                    break;
            }

            switch(lastState.get(resourceCard2).toString())
            {
                case Constants.GRAIN:
                    grain--;
                    break;
                case Constants.LUMBER:
                    lumber--;
                    break;
                case Constants.WOOL:
                    wool--;
                    break;
                case Constants.BRICK:
                    brick--;
                    break;
            }

            switch(lastState.get(resourceCard3).toString())
            {
                case Constants.GRAIN:
                    grain--;
                    break;
                case Constants.LUMBER:
                    lumber--;
                    break;
                case Constants.WOOL:
                    wool--;
                    break;
                case Constants.BRICK:
                    brick--;
                    break;
            }

            switch(lastState.get(resourceCard4).toString())
            {
                case Constants.GRAIN:
                    grain--;
                    break;
                case Constants.LUMBER:
                    lumber--;
                    break;
                case Constants.WOOL:
                    wool--;
                    break;
                case Constants.BRICK:
                    brick--;
                    break;
            }
            
            if(grain != 0)
            {
                err = "Incorrect BUILDSETTLEMENT Resources\n"
                        + "Incorrect number of grain";
            }
            else if(lumber != 0)
            {
                err = "Incorrect BUILDSETTLEMENT Resources\n"
                        + "Incorrect number of lumber";
            }
            else if(wool != 0)
            {
                err = "Incorrect BUILDSETTLEMENT Resources\n"
                        + "Incorrect number of wool";
            }
            else if(brick != 0)
            {
                err = "Incorrect BUILDSETTLEMENT Resources\n"
                        + "Incorrect number of brick";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a road and path match
    private boolean matchRoadAndPathPair(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString)
    {
        String pathXX = "";
        String roadYY = "";

        boolean status = false;

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDROAD expects: SET(PATHXX, ROADYY + playerString)\n"
                + "Set move expected";
        }
        else if(!((Set)move1).getKey().contains(Constants.PATHTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDROAD expects: SET(PATHXX, ROADYY + playerString)\n"
                + "PATH key expected";
        }
        else if(!((Set)move1).getValue().toString().contains(Constants.ROADTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDROAD expects: SET(PATHXX, ROADYY + playerString)\n"
                + "ROAD value expected";
        }
        else if(!((Set)move1).getValue().toString().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDROAD expects: SET(PATHXX, ROADYY + playerString)\n"
                + "playerString value expected";
        }
        else
        {
            pathXX = ((Set)move1).getKey();
            roadYY = ((Set)move1).getValue().toString();

            if(!move2.getMessageName().equals("Set"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SET(ROADYY + playerString, PATHXX)\n"
                    + "Set move expected";
            }
            else if(!((Set)move2).getKey().equals(roadYY))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SET(ROADYY + playerString, PATHXX)\n"
                    + "ROAD key doesn't match last line";
            }
            else if(!((Set)move2).getValue().toString().contains(pathXX))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SET(ROADYY + playerString, PATHXX)\n"
                    + "PATH value doesn't match last line";
            }
            else if(lastState.containsKey(pathXX))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SET(ROADYY + playerString, PATHXX)\n"
                    + "PATH is not empty";
            }
            else if(lastState.containsKey(roadYY))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SET(ROADYY + playerString, PATHXX)\n"
                    + "ROAD is already assigned a PATH";
            }
            else if(!canAddRoadHere(lastState, pathXX, playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SET(ROADYY + playerString, PATHXX)\n"
                    + "PATH is not eligible for ROAD from playerString";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }

    // Returns whether the list of resources matches for a build road move
    private boolean matchRoadResourcesPt1(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        String resourceCard1 = "";
        
        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDROAD expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "SetVisibility move expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDROAD expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "RESOURCECARD key expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDROAD expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "playerString key expected";
        }
        else
        {
            resourceCard1 = ((SetVisibility)move1).getKey();
            
            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "SetVisibility move expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(Constants.RESOURCECARDTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD key expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "playerString key expected";
            }
            else if(resourceCard1.equals(((SetVisibility)move2).getKey()))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUILDROAD expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "Duplicate to resource 1";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }

    // Returns whether the list of resources matches for a build road move
    private boolean matchRoadResourcesPt2(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        int lumber = 1;
        int brick = 1;
        
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUILDROAD expects: DELETE(RESOURCECARDAA + playerString)\n"
                + "Delete move expected";
        }
        else if(!move2.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "BUILDROAD expects: DELETE(RESOURCECARDBB + playerString)\n"
                + "Delete move expected";
        }
        else
        {
            String resourceCard1 = ((Delete)move1).getKey();
            String resourceCard2 = ((Delete)move2).getKey();
            
            switch(lastState.get(resourceCard1).toString())
            {
                case Constants.LUMBER:
                    lumber--;
                    break;
                case Constants.BRICK:
                    brick--;
                    break;
            }

            switch(lastState.get(resourceCard2).toString())
            {
                case Constants.LUMBER:
                    lumber--;
                    break;
                case Constants.BRICK:
                    brick--;
                    break;
            }
            
            if(lumber != 0)
            {
                err = "Incorrect BUILDROAD Resources\n"
                        + "Incorrect number of lumber";
            }
            else if(brick != 0)
            {
                err = "Incorrect BUILDROAD Resources\n"
                        + "Incorrect number of brick";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }

    // Returns whether the list of moves for a development card and development card type match
    @SuppressWarnings("unchecked")
    private boolean matchDevelopmentCardandDevelopmentCardTypePair(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString,
            String playerId)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + playerString)\n"
                + "Set move expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(Constants.DEVELOPMENTCARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + playerString)\n"
                + "DEVELOPMENTCARDTYPE key expected";
        }
        else if(((List<Integer>)((SetVisibility)move1).getVisibleToPlayerIds()).size() != 1)
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + playerString)\n"
                + "VisibleTo contains more than one user";
        }
        else if(!((List<Integer>)((SetVisibility)move1).getVisibleToPlayerIds()).contains(playerId))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + playerString)\n"
                + "VisibleTo does not map to current player";
        }
        else if(!((SetVisibility)move1).getKey().equals(findFirstOpenDevelopmentCard()))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + playerString)\n"
                + "DEVELOPMENTCARD is not the first available in the state";
        }
        else
        {
            status = true;
        }
        
        return status;
    }

    // Returns whether the list of moves for a development card and development card type match
    // for a specific card type
    @SuppressWarnings("unchecked")
    private boolean matchDevelopmentCardandDevelopmentCardTypePlayedPair(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString,
            String developmentCardType)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "PLAYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + nobody)\n"
                + "Set move expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(Constants.DEVELOPMENTCARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "PLAYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + nobody)\n"
                + "DEVELOPMENTCARD key expected";
        }
        else if(((ImmutableList<Integer>)((SetVisibility)move1).getVisibleToPlayerIds()).size() > 0)
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "PLAYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + playerString)\n"
                + "VisibleTo is not none";
        }
        else if(!(lastState.containsKey(((SetVisibility)move1).getKey())))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "PLAYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDXX, visibleTo + nobody)\n"
                + "DEVELOPMENTCARD is not owned by current player";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a buy development card move
    private boolean matchBuyDevelopmentCardResourcesPt1(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        String resourceCard1 = "";
        String resourceCard2 = "";
        
        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "SetVisibility move expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "RESOURCECARD key expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "playerString key expected";
        }
        else
        {
            resourceCard1 = ((SetVisibility)move1).getKey();
            
            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "SetVisibility move expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(Constants.RESOURCECARDTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD key expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "playerString key expected";
            }
            else if(resourceCard1.equals(((SetVisibility)move2).getKey()))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "Duplicate to resource 1";
            }
            else
            {
                resourceCard2 = ((SetVisibility)move2).getKey();
                
                if(!move3.getMessageName().equals("SetVisibility"))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "SetVisibility move expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(Constants.RESOURCECARDTOKEN))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "RESOURCECARD key expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(playerString))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "playerString key expected";
                }
                else if(resourceCard1.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 1";
                }
                else if(resourceCard2.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 2";
                }
                else
                {
                    status = true;
                }
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a buy development card move
    private boolean matchBuyDevelopmentCardResourcesPt2(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        int ore = 1;
        int grain = 1;
        int wool = 1;
        
        // Starting a new tree to increase visibility
        // All 3 resource cards are different and correctly formed

        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDAA + playerString)\n"
                + "Delete move expected";
        }
        else if(!move2.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDBB + playerString)\n"
                + "Delete move expected";
        }
        else if(!move3.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move3Num + "\n"
                + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDCC + playerString)\n"
                + "Delete move expected";
        }
        else
        {
            String resourceCard1 = ((Delete)move1).getKey();
            String resourceCard2 = ((Delete)move2).getKey();
            String resourceCard3 = ((Delete)move3).getKey();
            
            switch(lastState.get(resourceCard1).toString())
            {
                case Constants.ORE:
                    ore--;
                    break;
                case Constants.GRAIN:
                    grain--;
                    break;
                case Constants.WOOL:
                    wool--;
                    break;
            }

            switch(lastState.get(resourceCard2).toString())
            {
                case Constants.ORE:
                    ore--;
                    break;
                case Constants.GRAIN:
                    grain--;
                    break;
                case Constants.WOOL:
                    wool--;
                    break;
            }

            switch(lastState.get(resourceCard3).toString())
            {
                case Constants.ORE:
                    ore--;
                    break;
                case Constants.GRAIN:
                    grain--;
                    break;
                case Constants.WOOL:
                    wool--;
                    break;
            }
            
            if(ore != 0)
            {
                err = "Incorrect BUYDEVELOPMENTCARD Resources\n"
                    + "Incorrect number of ore";
            }
            else if(grain != 0)
            {
                err = "Incorrect BUYDEVELOPMENTCARD Resources\n"
                    + "Incorrect number of grain";
            }
            else if(wool != 0)
            {
                err = "Incorrect BUYDEVELOPMENTCARD Resources\n"
                    + "Incorrect number of wool";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a normal harbor trade
    private boolean matchNormalHarborTradeResourcesPt1(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        String resourceCard1 = "";
        String resourceCard2 = "";
        String resourceCard3 = "";
        
        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "SetVisibility move expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "RESOURCECARD key expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "playerString key expected";
        }
        else
        {
            resourceCard1 = ((SetVisibility)move1).getKey();
            
            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "SetVisibility move expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(Constants.RESOURCECARDTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD key expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "playerString key expected";
            }
            else if(resourceCard1.equals(((SetVisibility)move2).getKey()))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "Duplicate to resource 1";
            }
            else
            {
                resourceCard2 = ((SetVisibility)move2).getKey();
                
                if(!move3.getMessageName().equals("SetVisibility"))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "SetVisibility move expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(Constants.RESOURCECARDTOKEN))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "RESOURCECARD key expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(playerString))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "playerString key expected";
                }
                else if(resourceCard1.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 1";
                }
                else if(resourceCard2.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 2";
                }
                else
                {
                    resourceCard3 = ((SetVisibility)move3).getKey();
                    
                    if(!move4.getMessageName().equals("SetVisibility"))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "SetVisibility move expected";
                    }
                    else if(!((SetVisibility)move4).getKey().contains(Constants.RESOURCECARDTOKEN))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "RESOURCECARD key expected";
                    }
                    else if(!((SetVisibility)move4).getKey().contains(playerString))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "playerString key expected";
                    }
                    else if(resourceCard1.equals(((SetVisibility)move4).getKey()))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "Duplicate to resource 1";
                    }
                    else if(resourceCard2.equals(((SetVisibility)move4).getKey()))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "Duplicate to resource 2";
                    }
                    else if(resourceCard3.equals(((SetVisibility)move4).getKey()))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "NORMALHARBORTRADE expects: SETVISIBILITY(RESOURCECARDDD + playerString)\n"
                            + "Duplicate to resource 3";
                    }
                    else
                    {
                        status = true;
                    }
                }
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a normal harbor trade
    @SuppressWarnings("unchecked")
    private boolean matchNormalHarborTradeResourcesPt2(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Operation move5, int move5Num,
            Operation move6, int move6Num,
            Map<String, Object> lastState,
            String playerString,
            String playerId)
    {
        boolean status = false;
        
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
            
        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                + "Delete move expected";
        }
        else if(!move2.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                + "Delete move expected";
        }
        else if(!move3.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move3Num + "\n"
                + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDCC + playerString)\n"
                + "Delete move expected";
        }
        else if(!move4.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move4Num + "\n"
                + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDDD + playerString)\n"
                + "Delete move expected";
        }
        else
        {
            String resourceCard1 = ((Delete)move1).getKey();
            String resourceCard2 = ((Delete)move2).getKey();
            String resourceCard3 = ((Delete)move3).getKey();
            String resourceCard4 = ((Delete)move4).getKey();
            
            String resource = lastState.get(resourceCard1).toString();
            
            if ( !( resource.equals(lastState.get(resourceCard2).toString())
                 && resource.equals(lastState.get(resourceCard3).toString())
                 && resource.equals(lastState.get(resourceCard4).toString()) ) )
            {
                err = "Incorrect NORMALHARBORTRADE Resources\n"
                    + "Resources are not all the same";
            }
            else
            {
                if(!move5.getMessageName().equals("Set"))
                {
                    err = "Incorrect Move Number: " + move5Num + "\n"
                        + "NORMALHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "SetVisibility move expected";
                }
                else if(!((Set)move5).getKey().contains(Constants.RESOURCECARDTOKEN))
                {
                    err = "Incorrect Move Number: " + move5Num + "\n"
                        + "NORMALHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "RESOURCECARD key expected";
                }
                else if(!((Set)move5).getKey().contains(playerString))
                {
                    err = "Incorrect Move Number: " + move5Num + "\n"
                        + "NORMALHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "playerString key expected";
                }
                else if(!( ((Set)move5).getValue().toString().contains(Constants.ORE)
                         ||((Set)move5).getValue().toString().contains(Constants.GRAIN)
                         ||((Set)move5).getValue().toString().contains(Constants.LUMBER)
                         ||((Set)move5).getValue().toString().contains(Constants.WOOL)
                         ||((Set)move5).getValue().toString().contains(Constants.BRICK) ) )
                {
                    err = "Incorrect Move Number: " + move5Num + "\n"
                        + "NORMALHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "A type of resource value expected";
                }
                else
                {
                    String resourceString = ((Set)move5).getKey();
                
                    if(!move6.getMessageName().equals("SetVisibility"))
                    {
                        err = "Incorrect Move Number: " + move6Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Set move expected";
                    }
                    else if(!((SetVisibility) move6).getKey().contains(Constants.RESOURCECARDTOKEN))
                    {
                        err = "Incorrect Move Number: " + move6Num + "\n"
                                + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "ResourceCard key expected";
                    }
                    else if(!((SetVisibility) move6).getKey().contains(playerString))
                    {
                        err = "Incorrect Move Number: " + move6Num + "\n"
                                + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "playerString key expected";
                    }
                    else if(!((SetVisibility) move6).getKey().equals(resourceString))
                    {
                        err = "Incorrect Move Number: " + move6Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Resource pairs don't match";
                    }
                    else if(((List<Integer>)((SetVisibility) move6).getVisibleToPlayerIds()).size() != 1)
                    {
                        err = "Incorrect Move Number: " + move6Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Visible to not just user";
                    }
                    else if(!((List<Integer>)((SetVisibility) move6).getVisibleToPlayerIds()).equals(Arrays.asList(playerId)))
                    {
                        err = "Incorrect Move Number: " + move6Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Visible to not just user";
                    }
                    
                    status = true;
                }
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a 3 to 1 harbor trade
    private boolean matchThreeToOneHarborTradeResourcesPt1(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        String resourceCard1 = "";
        String resourceCard2 = "";
        
        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "SetVisibility move expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "RESOURCECARD key expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "playerString key expected";
        }
        else
        {
            resourceCard1 = ((SetVisibility)move1).getKey();
            
            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "SetVisibility move expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(Constants.RESOURCECARDTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD key expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "playerString key expected";
            }
            else if(resourceCard1.equals(((SetVisibility)move2).getKey()))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "Duplicate to resource 1";
            }
            else
            {
                resourceCard2 = ((SetVisibility)move2).getKey();
                
                if(!move3.getMessageName().equals("SetVisibility"))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "SetVisibility move expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(Constants.RESOURCECARDTOKEN))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "RESOURCECARD key expected";
                }
                else if(!((SetVisibility)move3).getKey().contains(playerString))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "playerString key expected";
                }
                else if(resourceCard1.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 1";
                }
                else if(resourceCard2.equals(((SetVisibility)move3).getKey()))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "THREETOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDCC + playerString)\n"
                        + "Duplicate to resource 2";
                }
                else
                {
                    status = true;
                }
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a 3 to 1 harbor trade
    @SuppressWarnings("unchecked")
    private boolean matchThreeToOneHarborTradeResourcesPt2(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Operation move5, int move5Num,
            Map<String, Object> lastState,
            String playerString,
            String playerId)
    {
        boolean status = false;
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
            
        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                + "Delete move expected";
        }
        else if(!move2.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                + "Delete move expected";
        }
        else if(!move3.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move3Num + "\n"
                + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDCC + playerString)\n"
                + "Delete move expected";
        }
        else
        {
            String resourceCard1 = ((Delete)move1).getKey();
            String resourceCard2 = ((Delete)move2).getKey();
            String resourceCard3 = ((Delete)move3).getKey();
            String resource = lastState.get(resourceCard1).toString();
            
            if ( !( resource.equals(lastState.get(resourceCard2).toString())
                 && resource.equals(lastState.get(resourceCard3).toString()) ) )
            {
                err = "Incorrect THREETOONEHARBORTRADE Resources\n"
                    + "Resources are not all the same";
            }
            else
            {
                if(!move4.getMessageName().equals("Set"))
                {
                    err = "Incorrect Move Number: " + move4Num + "\n"
                        + "THREETOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "SetVisibility move expected";
                }
                else if(!((Set)move4).getKey().contains(Constants.RESOURCECARDTOKEN))
                {
                    err = "Incorrect Move Number: " + move4Num + "\n"
                        + "THREETOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "RESOURCECARD key expected";
                }
                else if(!((Set)move4).getKey().contains(playerString))
                {
                    err = "Incorrect Move Number: " + move4Num + "\n"
                        + "THREETOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "playerString key expected";
                }
                else if(!( ((Set)move4).getValue().toString().contains(Constants.ORE)
                         ||((Set)move4).getValue().toString().contains(Constants.GRAIN)
                         ||((Set)move4).getValue().toString().contains(Constants.LUMBER)
                         ||((Set)move4).getValue().toString().contains(Constants.WOOL)
                         ||((Set)move4).getValue().toString().contains(Constants.BRICK) ) )
                {
                    err = "Incorrect Move Number: " + move4Num + "\n"
                        + "THREETOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "A type of resource value expected";
                }
                else if(!ownsThreeToOneHarbor(lastState, playerString))
                {
                    err = "Incorrect THREETOONEHARBORTRADE Resources\n"
                        + "playerString does not own a 3 to 1 port";
                }
                else
                {
                    String resourceString = ((Set)move4).getKey();
                    
                    if(!move5.getMessageName().equals("SetVisibility"))
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Set move expected";
                    }
                    else if(!((SetVisibility) move5).getKey().contains(Constants.RESOURCECARDTOKEN))
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
                                + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "ResourceCard key expected";
                    }
                    else if(!((SetVisibility) move5).getKey().contains(playerString))
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
                                + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "playerString key expected";
                    }
                    else if(!((SetVisibility) move5).getKey().equals(resourceString))
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Resource pairs don't match";
                    }
                    else if(((List<Integer>)((SetVisibility) move5).getVisibleToPlayerIds()).size() != 1)
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Visible to not just user";
                    }
                    else if(!((List<Integer>)((SetVisibility) move5).getVisibleToPlayerIds()).equals(Arrays.asList(playerId)))
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Visible to not just user";
                    }
                    
                    status = true;
                }
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a 2 to 1 harbor trade
    private boolean matchTwoToOneHarborTradeResourcesPt1(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        String resourceCard1 = "";
        
        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "TWOTOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "SetVisibility move expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "TWOTOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "RESOURCECARD key expected";
        }
        else if(!((SetVisibility)move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "TWOTOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDAA + playerString)\n"
                + "playerString key expected";
        }
        else
        {
            resourceCard1 = ((SetVisibility)move1).getKey();
            
            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "SetVisibility move expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(Constants.RESOURCECARDTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD key expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "playerString key expected";
            }
            else if(resourceCard1.equals(((SetVisibility)move2).getKey()))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: SETVISIBILITY(RESOURCECARDBB + playerString)\n"
                    + "Duplicate to resource 1";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a 2 to 1 harbor trade
    @SuppressWarnings("unchecked")
    private boolean matchTwoToOneHarborTradeResourcesPt2(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Map<String, Object> lastState,
            String playerString,
            String playerId)
    {
        boolean status = false;
        
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
        
        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "TWOTOONEHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                + "Delete move expected";
        }
        else if(!move2.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "TWOTOONEHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                + "Delete move expected";
        }
        else
        {
            String resourceCard1 = ((Delete)move1).getKey();
            String resourceCard2 = ((Delete)move2).getKey();
            String resource = lastState.get(resourceCard1).toString();
            
            if ( !resource.equals(lastState.get(resourceCard2).toString()) )
            {
                err = "Incorrect TWOTOONEHARBORTRADE Resources\n"
                    + "Resources are not all the same";
            }
            else
            {
                if(!move3.getMessageName().equals("Set"))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "TWOTOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "SetVisibility move expected";
                }
                else if(!((Set)move3).getKey().contains(Constants.RESOURCECARDTOKEN))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "TWOTOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "RESOURCECARD key expected";
                }
                else if(!((Set)move3).getKey().contains(playerString))
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "TWOTOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "playerString key expected";
                }
                else if(!( ((Set)move3).getValue().toString().contains(Constants.ORE)
                         ||((Set)move3).getValue().toString().contains(Constants.GRAIN)
                         ||((Set)move3).getValue().toString().contains(Constants.LUMBER)
                         ||((Set)move3).getValue().toString().contains(Constants.WOOL)
                         ||((Set)move3).getValue().toString().contains(Constants.BRICK) ) )
                {
                    err = "Incorrect Move Number: " + move3Num + "\n"
                        + "TWOTOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                        + "A type of resource value expected";
                }
                else if(!ownsTwoToOneHarbor(lastState, playerString, resource))
                {
                    err = "Incorrect TWOTOONEHARBORTRADE Resources\n"
                        + "playerString does not own a 2 to 1 port";
                }
                else
                {
                    String resourceString = ((Set)move3).getKey();
                    
                    if(!move4.getMessageName().equals("SetVisibility"))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Set move expected";
                    }
                    else if(!((SetVisibility) move4).getKey().contains(Constants.RESOURCECARDTOKEN))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                                + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "ResourceCard key expected";
                    }
                    else if(!((SetVisibility) move4).getKey().contains(playerString))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                                + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "playerString key expected";
                    }
                    else if(!((SetVisibility) move4).getKey().equals(resourceString))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Resource pairs don't match";
                    }
                    else if(((List<Integer>)((SetVisibility) move4).getVisibleToPlayerIds()).size() != 1)
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Visible to not just user";
                    }
                    else if(!((List<Integer>)((SetVisibility) move4).getVisibleToPlayerIds()).equals(Arrays.asList(playerId)))
                    {
                        err = "Incorrect Move Number: " + move4Num + "\n"
                            + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                            + "Visible to not just user";
                    }
                    
                    status = true;
                }
            }
        }
        
        return status;
    }
    
    // Returns whether the robber moves match
    private boolean matchRobber(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ROBBER expects: SET(ROBBER, HEX)\n"
                + "Set move expected";
        }
        else if(!((Set)move1).getKey().contains(Constants.ROBBER))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ROBBER expects: SET(ROBBER, HEX)\n"
                + "MONOPOLYRESOURCE key expected";
        }
        else if(!((Set)move1).getValue().toString().contains(Constants.HEXTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ROBBER expects: SET(ROBBER, HEX)\n"
                + "HEX value expected";
        }
        else if(lastState.get(Constants.ROBBER).toString().equals(((Set)move1).getValue().toString()))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ROBBER expects: SET(ROBBER, HEX)\n"
                + "ROBBER did not move";
        }
        else if(!move2.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "ROBBER expects: SET(SOLDIERCOUNT + playerId, VAL)\n"
                + "Set move expected";
        }
        else if(!((Set)move2).getKey().contains(Constants.SOLDIERCOUNTTOKEN))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "ROBBER expects: SET(SOLDIERCOUNT + playerId, VAL)\n"
                + "SOLDIERCOUNTTOKEN key expected";
        }
        else if(!((Set)move2).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "ROBBER expects: SET(SOLDIERCOUNT + playerId, VAL)\n"
                + "playerString key expected";
        }
        else if(!isInteger(((Set)move2).getValue().toString()))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "ROBBER expects: SET(SOLDIERCOUNT + playerId, VAL)\n"
                + "SOLDIERCOUNT value is not an integer";
        }
        else if( Integer.parseInt(lastState.get(((Set)move2).getKey()).toString()) + 1
              != Integer.parseInt((((Set)move2).getValue()).toString()) )
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "ROBBER expects: SET(SOLDIERCOUNT + playerId, VAL)\n"
                + "SOLDIERCOUNT not updated correctly";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the robber moves match
    private boolean matchRobberSolo(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ROBBER expects: SET(ROBBER, HEX)\n"
                + "Set move expected";
        }
        else if(!((Set)move1).getKey().contains(Constants.ROBBER))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ROBBER expects: SET(ROBBER, HEX)\n"
                + "MONOPOLYRESOURCE key expected";
        }
        else if(!((Set)move1).getValue().toString().contains(Constants.HEXTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ROBBER expects: SET(ROBBER, HEX)\n"
                + "HEX value expected";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the monopoly moves match
    private boolean matchMonopoly(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "MONOPOLY expects: SET(MONOPOLYRESOURCE, resource)\n"
                + "Set move expected";
        }
        else if(!((Set)move1).getKey().contains(Constants.MONOPOLYRESOURCE))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "MONOPOLY expects: SET(MONOPOLYRESOURCE, resource)\n"
                + "MONOPOLYRESOURCE key expected";
        }
        else if(!( ((Set)move1).getValue().toString().contains(Constants.ORE)
                 ||((Set)move1).getValue().toString().contains(Constants.GRAIN)
                 ||((Set)move1).getValue().toString().contains(Constants.LUMBER)
                 ||((Set)move1).getValue().toString().contains(Constants.WOOL)
                 ||((Set)move1).getValue().toString().contains(Constants.BRICK) ) )
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "MONOPOLY expects: SET(MONOPOLYRESOURCE, resource)\n"
                + "A type of resource value expected";
        }
        else if(!move2.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "MONOPOLY expects: SET(MONOPOLYBENEFACTOR, playerString)\n"
                + "Set move expected";
        }
        else if(!((Set)move2).getKey().contains(Constants.MONOPOLYBENEFACTOR))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "MONOPOLY expects: SET(MONOPOLYBENEFACTOR, playerString)\n"
                + "MONOPOLYBENEFACTOR key expected";
        }
        else if(!((Set)move2).getValue().toString().contains(playerString))
        {
            err = "Incorrect Move Number: " + move2Num + "\n"
                + "MONOPOLY expects: SET(MONOPOLYBENEFACTOR, playerString)\n"
                + "playerString value expected";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves matches for a single add resource
    private boolean matchAddResource(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "Set move expected";
        }
        else if(((Set) move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard key expected";
        }
        else if(((Set) move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "playerString key expected";
        }
        else if(lastState.containsKey(((Set) move1).getKey()))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard already assigned";
        }
        else if(!( ((Set)move1).getValue().toString().contains(Constants.ORE)
                 ||((Set)move1).getValue().toString().contains(Constants.GRAIN)
                 ||((Set)move1).getValue().toString().contains(Constants.LUMBER)
                 ||((Set)move1).getValue().toString().contains(Constants.WOOL)
                 ||((Set)move1).getValue().toString().contains(Constants.BRICK) ) )
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                + "A type of resource value expected";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves matches for a single add resource
    private boolean matchAHiddenResource(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("SetVisibility"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "Set move expected";
        }
        else if(!((SetVisibility) move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard key expected";
        }
        else if(!lastState.containsKey(((SetVisibility) move1).getKey()))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard already assigned";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves matches for a single add resource
    private boolean matchASingleDelete(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "Set move expected";
        }
        else if(!((Delete) move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard key expected";
        }
        else if(((Delete) move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "playerString key not expected";
        }
        else if(!lastState.containsKey(((Delete) move1).getKey()))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard doesn't exist";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves matches for a single add resource
    private boolean matchDieRoll(
            Operation move1, int move1Num,
            String expectedDie)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("SetRandomInteger"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "MATCHDIEROLL expects: SETRANDOMINTEGER(DIEX, 1, 6)\n"
                + "Set move expected";
        }
        else if(!((SetRandomInteger) move1).getKey().equals(expectedDie))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "MATCHDIEROLL expects: SETRANDOMINTEGER(DIEX, 1, 6)\n"
                + "expectedDie key expected";
        }
        else if(((SetRandomInteger) move1).getFrom() != 1)
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "MATCHDIEROLL expects: SETRANDOMINTEGER(DIEX, 1, 6)\n"
                + "From 1 expected";
        }
        else if(((SetRandomInteger) move1).getTo() != 6)
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "MATCHDIEROLL expects: SETRANDOMINTEGER(DIEX, 1, 6)\n"
                + "To 6 expected";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves matches for a die clear
    private boolean matchDieClear(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String expectedDie)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("Delete"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "CLEARDIEROLL expects: DELETE(DIEX)\n"
                + "Delete move expected";
        }
        else if(!((Delete) move1).getKey().equals(expectedDie))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "CLEARDIEROLL expects: DELETE(DIEX)\n"
                + "expectedDie key expected";
        }
        else if(!lastState.containsKey(((Delete) move1).getKey()))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "CLEARDIEROLL expects: DELETE(DIEX)\n"
                + "DIEX doesn't exist in state";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves matches for a single add resource
    @SuppressWarnings("unchecked")
    private boolean matchResourceCardAdd(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            String expectedResource)
    {
        boolean status = false;
        
        String resourceString = "";

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "Set move expected";
        }
        else if(!((Set) move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard key expected";
        }
        else if(!((Set) move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "playerString key expected";
        }
        else if(lastState.containsKey(((Set) move1).getKey()))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard already assigned";
        }
        else if(!( ((Set)move1).getValue().toString().contains(Constants.ORE)
                 ||((Set)move1).getValue().toString().contains(Constants.GRAIN)
                 ||((Set)move1).getValue().toString().contains(Constants.LUMBER)
                 ||((Set)move1).getValue().toString().contains(Constants.WOOL)
                 ||((Set)move1).getValue().toString().contains(Constants.BRICK) ) )
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                + "A type of resource value expected";
        }
        else if(!(((Set)move1).getValue().equals(expectedResource)))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                + "Not the expected resource";
        }
        else
        {
            resourceString = ((Set)move1).getKey();
            
            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "Set move expected";
            }
            else if(!((SetVisibility) move2).getKey().contains(Constants.RESOURCECARDTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                        + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "ResourceCard key expected";
            }
            else if(!((SetVisibility) move2).getKey().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                        + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "playerString key expected";
            }
            else if(!((SetVisibility) move2).getKey().equals(resourceString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "Resource pairs don't match";
            }
            else if(((List<Integer>)((SetVisibility) move2).getVisibleToPlayerIds()).size() != 1)
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "Visible to not just user";
            }
            else if(!((List<Integer>)((SetVisibility) move2).getVisibleToPlayerIds()).equals(Arrays.asList(playerId)))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "Visible to not just user";
            }
            
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves matches for a single add resource
    @SuppressWarnings("unchecked")
    private boolean matchResourceCardAddIgnoreExpected(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState)
    {
        boolean status = false;
        
        String resourceString = "";
        String playerId = "-1";
        String playerString = "";
        if(((Set) move1).getKey().contains(Constants.PB))
        {
            playerId = playerIds.get(0);
            playerString = Constants.PB;
        }
        else if(((Set) move1).getKey().contains(Constants.PR))
        {
            playerId = playerIds.get(1);
            playerString = Constants.PR;
        }
        else if(((Set) move1).getKey().contains(Constants.PY))
        {
            playerId = playerIds.get(2);
            playerString = Constants.PY;
        }
        else if(((Set) move1).getKey().contains(Constants.PG))
        {
            playerId = playerIds.get(3);
            playerString = Constants.PG;
        }

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "Set move expected";
        }
        else if(!((Set) move1).getKey().contains(Constants.RESOURCECARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard key expected";
        }
        else if(!((Set) move1).getKey().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "playerString key expected";
        }
        else if(lastState.containsKey(((Set) move1).getKey()))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA + playerString, resource)\n"
                + "ResourceCard already assigned";
        }
        else if(!( ((Set)move1).getValue().toString().contains(Constants.ORE)
                 ||((Set)move1).getValue().toString().contains(Constants.GRAIN)
                 ||((Set)move1).getValue().toString().contains(Constants.LUMBER)
                 ||((Set)move1).getValue().toString().contains(Constants.WOOL)
                 ||((Set)move1).getValue().toString().contains(Constants.BRICK) ) )
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ADDRESOURCE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                + "A type of resource value expected";
        }
        else
        {
            resourceString = ((Set)move1).getKey();
            
            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "Set move expected";
            }
            else if(!((SetVisibility) move2).getKey().contains(Constants.RESOURCECARDTOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                        + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "ResourceCard key expected";
            }
            else if(!((SetVisibility) move2).getKey().contains(playerString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                        + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "playerString key expected";
            }
            else if(!((SetVisibility) move2).getKey().equals(resourceString))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "Resource pairs don't match";
            }
            else if(((List<Integer>)((SetVisibility) move2).getVisibleToPlayerIds()).size() != 1)
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "Visible to not just user";
            }
            else if(!((List<Integer>)((SetVisibility) move2).getVisibleToPlayerIds()).equals(Arrays.asList(playerId)))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "ADDRESOURCE expects: SETVISIBILITY(RESOURCECARDAA + playerString, playerId)\n"
                    + "Visible to not just user";
            }
            
            status = true;
        }
        
        return status;
    }
    
    // Returns whether the list of moves matches for an End Game
    private boolean matchEndGame(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString,
            String playerId,
            String priorMoveAdds)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("EndGame"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ENDGAME expects: ENDGAME(playerId)\n"
                + "EndGame move expected";
        }
        else if(((EndGame)move1).getPlayerIdToScore().size() != 1)
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ENDGAME expects: ENDGAME(playerId)\n"
                + "Too many winners claimed";
        }
        else if(!((EndGame)move1).getPlayerIdToScore().containsKey(playerId))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "ENDGAME expects: ENDGAME(playerId)\n"
                + "playerId not declared winner";
        }
        else
        {
            status = processEndGameForPlayer(
                    move1, move1Num,
                    lastState,
                    playerString,
                    priorMoveAdds);
        }
        
        return status;
    }

    // Handles the claim of largest army
    private boolean processLargestArmyClaim(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString,
            String soldierCount)
    {
        boolean status = false;

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "LARGESTARMY expects: SET(LARGESTARMY, playerString)\n"
                + "Set move expected";
        }
        else if(!((Set) move1).getKey().contains(Constants.LARGESTARMY))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "LARGESTARMY expects: SET(LARGESTARMY, playerString)\n"
                + "LARGESTARMY key expected";
        }
        else if(!((Set) move1).getValue().toString().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "LARGESTARMY expects: SET(LARGESTARMY, playerString)\n"
                + "playerString key expected";
        }
        else
        {
            if( !lastState.containsKey(Constants.LARGESTARMY)
              && Integer.parseInt(lastState.get(soldierCount).toString()) > 2)
            {
                status = true;
            }
            else if( Integer.parseInt(lastState.get(soldierCount).toString())
                   > Integer.parseInt(lastState.get(Constants.SOLDIERCOUNTTOKEN + lastState.get(Constants.LARGESTARMY).toString()).toString()) )
            {
                status = true;
            }
            else
            {
                err = "Incorrect Move Number: " + move1Num + "\n"
                    + "LARGESTARMY expects: SET(LARGESTARMY, playerString)\n"
                    + "playerString army is not largest";
            }
        }
        
        return status;
    }
    
    // Handles the claim of longest road
    private boolean processLongestRoadClaim(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "LONGESTROAD expects: SET(LONGESTROAD, playerString)\n"
                + "Set move expected";
        }
        else if(!((Set) move1).getKey().contains(Constants.LONGESTROAD))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "LONGESTROAD expects: SET(LONGESTROAD, playerString)\n"
                + "LONGESTROAD key expected";
        }
        else if(!((Set) move1).getValue().toString().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "LONGESTROAD expects: SET(LONGESTROAD, playerString)\n"
                + "playerString key expected";
        }
        else
        {
            List<String> longestPath = null;
            String startingRoad = "";
            
            for (int i = 0; i < 15; i++)
            {
                List<String> pathToCheck = new ArrayList<String>();
                
                if(i < 10)
                    startingRoad = Constants.ROADTOKEN + "0" + i + playerString;
                else
                    startingRoad = Constants.ROADTOKEN + i + playerString;
                
                if(lastState.containsKey(startingRoad))
                {
                    pathToCheck = checkNextPath(
                            lastState,
                            pathToCheck,
                            lastState.get(startingRoad).toString(),
                            playerString);
                }
                
                if( longestPath == null
                 || pathToCheck.size() > longestPath.size())
                {
                    longestPath = pathToCheck;
                }
            }
            
            if(!lastState.containsKey(Constants.LONGESTROAD)
             && longestPath.size() > 4)
            {
                status = true;
            }
            else
            {
                String currentLongestPathHolder = lastState.get(Constants.LONGESTROAD).toString();
                List<String> longestPathHolder = null;
                
                for (int i = 0; i < 10; i++)
                {
                    List<String> pathToCheck = new ArrayList<String>();
                    
                    if(i < 10)
                        startingRoad = Constants.ROADTOKEN + "0" + i + currentLongestPathHolder;
                    else
                        startingRoad = Constants.ROADTOKEN + i + currentLongestPathHolder;
                    
                    if(lastState.containsKey(startingRoad))
                    {
                        pathToCheck = checkNextPath(
                                lastState,
                                pathToCheck,
                                lastState.get(startingRoad).toString(),
                                currentLongestPathHolder);
                    }
                    
                    if( longestPathHolder == null
                     || pathToCheck.size() > longestPathHolder.size())
                    {
                        longestPathHolder = pathToCheck;
                    }
                }
                
                if(longestPath.size() > longestPathHolder.size())
                {
                    status = true;
                }
                else
                {
                    err = "Incorrect Move Number: " + move1Num + "\n"
                            + "LONGESTROAD expects: SET(LONGESTROAD, playerString)\n"
                            + "playerString path is not longest";
                }
            }
        }
        
        return status;
    }

    // Recursive call to pursue Longest Path
    private List<String> checkNextPath(
            Map<String, Object> lastState,
            List<String> currentPath,
            String lastPath,
            String playerString)
    {
        List<String> longestPath = null;
        
        if( currentPath == null
         || !currentPath.contains(lastPath) )
        {
            ImmutableList<String> listOfNodes = getNodesFromPath(lastPath);
            List<String> pathToCheck = currentPath;
            pathToCheck.add(lastPath);
            
            for(int i = 0; i < listOfNodes.size(); i++)
            {
                ImmutableList<String> listOfPaths = getPathsFromNode(listOfNodes.get(i).toString());
                
                for(int j = 0; j < listOfPaths.size(); j++)
                {
                    
                    if( lastState.containsKey(listOfPaths.get(j))
                     && lastState.get(listOfPaths.get(j)).toString().contains(playerString)
                     && !currentPath.contains(listOfPaths.get(j)) )
                    {
                        checkNextPath(
                                lastState,
                                pathToCheck,
                                listOfPaths.get(j),
                                playerString);
                        
                        if( longestPath == null
                         || pathToCheck.size() > longestPath.size())
                        {
                            longestPath = pathToCheck;
                        }
                    }
                }
            }
        }
        
        return longestPath;
    }
    
    // Counts up victory points to determine if End Game called correctly
    private boolean processEndGameForPlayer(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString,
            String priorMoveAdds)
    {
        boolean status = false;
        
        int victoryPoints = 0;

        victoryPoints = victoryPoints + countPointsFromCities(lastState, playerString);
        victoryPoints = victoryPoints + countPointsFromSettlements(lastState, playerString);
        victoryPoints = victoryPoints + countPointsfromLongestRoad(lastState, playerString);
        victoryPoints = victoryPoints + countPointsfromLargestArmy(lastState, playerString);
        victoryPoints = victoryPoints + countPointsfromDevelopmentCards(lastState, playerString);
        
        switch(priorMoveAdds)
        {
            case Constants.ADDCITY:
                victoryPoints = victoryPoints + 1;
                break;
            case Constants.ADDSETTLEMENT:
                victoryPoints = victoryPoints + 1;
                break;
            case Constants.ADDLONGESTROAD:
                victoryPoints = victoryPoints + 2;
                break;
            case Constants.ADDLARGESTARMY:
                victoryPoints = victoryPoints + 2;
                break;
        }
        
        if(victoryPoints < 10)
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                    + "ENDGAME expects: ENDGAME(playerId)\n"
                    + "Not Enough Victory Points";
        }
        else
        {
            status = true;
        }
        
        return status;
    }
    
    // Counts victory points from cities for a specific player
    private int countPointsFromCities(
            Map<String, Object> lastState,
            String playerString)
    {
        int count = 0;
        
        switch(playerString)
        {
            case Constants.PB:
                if(lastState.containsKey(Constants.CITY00PB))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY01PB))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY02PB))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY03PB))
                    count = count + 2;
                break;
            case Constants.PR:
                if(lastState.containsKey(Constants.CITY00PR))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY01PR))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY02PR))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY03PR))
                    count = count + 2;
                break;
            case Constants.PY:
                if(lastState.containsKey(Constants.CITY00PY))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY01PY))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY02PY))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY03PY))
                    count = count + 2;
                break;
            case Constants.PG:
                if(lastState.containsKey(Constants.CITY00PG))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY01PG))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY02PG))
                    count = count + 2;
                if(lastState.containsKey(Constants.CITY03PG))
                    count = count + 2;
                break;
        }
        
        return count;
    }
    
    // Counts victory points from settlements for a specific player
    // Counts victory points from settlements for a specific player
    private int countPointsFromSettlements(
            Map<String, Object> lastState,
            String playerString)
    {
        int count = 0;
        
        switch(playerString)
        {
            case Constants.PB:
                if(lastState.containsKey(Constants.SETTLEMENT00PB))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT01PB))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT02PB))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT03PB))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT04PB))
                    count++;
                break;
            case Constants.PR:
                if(lastState.containsKey(Constants.SETTLEMENT00PR))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT01PR))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT02PR))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT03PR))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT04PR))
                    count++;
                break;
            case Constants.PY:
                if(lastState.containsKey(Constants.SETTLEMENT00PY))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT01PY))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT02PY))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT03PY))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT04PY))
                    count++;
                break;
            case Constants.PG:
                if(lastState.containsKey(Constants.SETTLEMENT00PG))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT01PG))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT02PG))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT03PG))
                    count++;
                if(lastState.containsKey(Constants.SETTLEMENT04PG))
                    count++;
                break;
        }
        
        return count;
    }
    
    // Counts victory points from longest road for a specific player
    private int countPointsfromLongestRoad(
            Map<String, Object> lastState,
            String playerString)
    {
        int count = 0;
        
        if( lastState.containsKey(Constants.LONGESTROAD)
         && lastState.get(Constants.LONGESTROAD).toString().equals(playerString) )
        {
            count = 2;
        }
        
        return count;
    }
    
    // Counts victory points from longest road for a specific player
    private int countPointsfromLargestArmy(
            Map<String, Object> lastState,
            String playerString)
    {
        int count = 0;
        
        if( lastState.containsKey(Constants.LARGESTARMY)
         && lastState.get(Constants.LARGESTARMY).toString().equals(playerString) )
        {
            count = 2;
        }
        
        return count;
    }
    
    // Counts victory points from development cards
    private int countPointsfromDevelopmentCards(
            Map<String, Object> lastState,
            String playerString)
    {
        int count = 0;
        
        if(lastState.containsKey(Constants.DEVELOPMENTCARDTYPEDEF04))
            count++;
        if(lastState.containsKey(Constants.DEVELOPMENTCARDTYPEDEF05))
            count++;
        if(lastState.containsKey(Constants.DEVELOPMENTCARDTYPEDEF06))
            count++;
        if(lastState.containsKey(Constants.DEVELOPMENTCARDTYPEDEF07))
            count++;
        if(lastState.containsKey(Constants.DEVELOPMENTCARDTYPEDEF08))
            count++;
        
        return count;
    }
    
    // Returns whether a specific player owns a 2 to 1 harbor for a specific resource
    // Identifies whether a specific player owns a 2-1 harbor for a specific resource
    private boolean ownsTwoToOneHarbor(
            Map<String, Object> state,
            String playerString,
            String resource)
    {
        boolean status = false;
        
        ImmutableList<String> harborBonusList = getHarborBonusList(state, playerString);
        
        for(int i = 0; i < harborBonusList.size(); i++)
        {
            switch(resource)
            {
                case Constants.ORE:
                    status = status || harborBonusList.get(i).equals(Constants.HARBORTYPE01);
                    break;
                case Constants.GRAIN:
                    status = status || harborBonusList.get(i).equals(Constants.HARBORTYPE02);
                    break;
                case Constants.LUMBER:
                    status = status || harborBonusList.get(i).equals(Constants.HARBORTYPE03);
                    break;
                case Constants.WOOL:
                    status = status || harborBonusList.get(i).equals(Constants.HARBORTYPE04);
                    break;
                case Constants.BRICK:
                    status = status || harborBonusList.get(i).equals(Constants.HARBORTYPE05);
                    break;
                
            }
        }
        
        return status;
    }
    

    // Returns whether a specific player owns a 3 to 1 harbor
    // Identifies whether a specific player owns a 3-1 harbor
    private boolean ownsThreeToOneHarbor(
            Map<String, Object> state,
            String playerString)
    {
        boolean status = false;
        
        ImmutableList<String> harborBonusList = getHarborBonusList(state, playerString);
        
        for(int i = 0; i < harborBonusList.size(); i++)
        {
            status = status || harborBonusList.get(i).equals(Constants.HARBORTYPE00);
        }
        
        return status;
    }
    

    // Returns a list of the harbor bonuses a player owns
    // Gets a list of harbor bonuses for a specific player
    private ImmutableList<String> getHarborBonusList(
            Map<String, Object> state,
            String playerString)
    {
        ImmutableList<String> harborBonusList = null;
        Builder<String> listBuilder = new Builder<String>();
        
        // Harbor 00
        if( ((((String) state.get(Constants.NODE00)) != null)
           && (((String) state.get(Constants.NODE00)).contains(playerString)))
         || ((((String) state.get(Constants.NODE03)) != null)
           && (((String) state.get(Constants.NODE03)).contains(playerString))))
        {
            listBuilder.add((String) state.get(Constants.HARBOR00));
        }

        // Harbor 01
        if( ((((String) state.get(Constants.NODE01)) != null)
           && (((String) state.get(Constants.NODE01)).contains(playerString)))
         || ((((String) state.get(Constants.NODE05)) != null)
           && (((String) state.get(Constants.NODE05)).contains(playerString))))
        {
            listBuilder.add((String) state.get(Constants.HARBOR01));
        }

        // Harbor 02
        if( ((((String) state.get(Constants.NODE10)) != null)
                && (((String) state.get(Constants.NODE10)).contains(playerString)))
              || ((((String) state.get(Constants.NODE15)) != null)
                && (((String) state.get(Constants.NODE15)).contains(playerString))))
        {
            listBuilder.add((String) state.get(Constants.HARBOR02));
        }

        // Harbor 03
        if( ((((String) state.get(Constants.NODE26)) != null)
                && (((String) state.get(Constants.NODE26)).contains(playerString)))
              || ((((String) state.get(Constants.NODE32)) != null)
                && (((String) state.get(Constants.NODE32)).contains(playerString))))
        {
            listBuilder.add((String) state.get(Constants.HARBOR03));
        }

        // Harbor 04
        if( ((((String) state.get(Constants.NODE42)) != null)
                && (((String) state.get(Constants.NODE42)).contains(playerString)))
              || ((((String) state.get(Constants.NODE46)) != null)
                && (((String) state.get(Constants.NODE46)).contains(playerString))))
        {
            listBuilder.add((String) state.get(Constants.HARBOR04));
        }

        // Harbor 05
        if( ((((String) state.get(Constants.NODE49)) != null)
                && (((String) state.get(Constants.NODE49)).contains(playerString)))
              || ((((String) state.get(Constants.NODE52)) != null)
                && (((String) state.get(Constants.NODE52)).contains(playerString))))
        {
            listBuilder.add((String) state.get(Constants.HARBOR05));
        }

        // Harbor 06
        if( ((((String) state.get(Constants.NODE47)) != null)
                && (((String) state.get(Constants.NODE47)).contains(playerString)))
              || ((((String) state.get(Constants.NODE51)) != null)
                && (((String) state.get(Constants.NODE51)).contains(playerString))))
        {
            listBuilder.add((String) state.get(Constants.HARBOR06));
        }

        // Harbor 07
        if( ((((String) state.get(Constants.NODE33)) != null)
                && (((String) state.get(Constants.NODE33)).contains(playerString)))
              || ((((String) state.get(Constants.NODE38)) != null)
                && (((String) state.get(Constants.NODE38)).contains(playerString))))
        {
            listBuilder.add((String) state.get(Constants.HARBOR07));
        }

        // Harbor 08
        if( ((((String) state.get(Constants.NODE11)) != null)
                && (((String) state.get(Constants.NODE11)).contains(playerString)))
              || ((((String) state.get(Constants.NODE16)) != null)
                && (((String) state.get(Constants.NODE16)).contains(playerString))))
        {
            listBuilder.add((String) state.get(Constants.HARBOR08));
        }
        
        harborBonusList = listBuilder.build();
        
        return harborBonusList;
    }
    

    // Returns a list of nodes connected to a path
    // Gets a list of nodes attached to a specific path
    private ImmutableList<String> getNodesFromPath(
            String path)
    {
        ImmutableList<String> nodeList = null;
        
        switch (path)
        {
            case Constants.PATH00:
                nodeList = ImmutableList.of(
                        Constants.NODE00,
                        Constants.NODE03);
                break;
            case Constants.PATH01:
                nodeList = ImmutableList.of(
                        Constants.NODE00,
                        Constants.NODE04);
                break;
            case Constants.PATH02:
                nodeList = ImmutableList.of(
                        Constants.NODE01,
                        Constants.NODE04);
                break;
            case Constants.PATH03:
                nodeList = ImmutableList.of(
                        Constants.NODE01,
                        Constants.NODE05);
                break;
            case Constants.PATH04:
                nodeList = ImmutableList.of(
                        Constants.NODE02,
                        Constants.NODE05);
                break;
            case Constants.PATH05:
                nodeList = ImmutableList.of(
                        Constants.NODE02,
                        Constants.NODE06);
                break;
            case Constants.PATH06:
                nodeList = ImmutableList.of(
                        Constants.NODE03,
                        Constants.NODE07);
                break;
            case Constants.PATH07:
                nodeList = ImmutableList.of(
                        Constants.NODE04,
                        Constants.NODE08);
                break;
            case Constants.PATH08:
                nodeList = ImmutableList.of(
                        Constants.NODE05,
                        Constants.NODE09);
                break;
            case Constants.PATH09:
                nodeList = ImmutableList.of(
                        Constants.NODE06,
                        Constants.NODE10);
            case Constants.PATH10:
                nodeList = ImmutableList.of(
                        Constants.NODE07,
                        Constants.NODE11);
                break;
            case Constants.PATH11:
                nodeList = ImmutableList.of(
                        Constants.NODE07,
                        Constants.NODE12);
                break;
            case Constants.PATH12:
                nodeList = ImmutableList.of(
                        Constants.NODE08,
                        Constants.NODE12);
                break;
            case Constants.PATH13:
                nodeList = ImmutableList.of(
                        Constants.NODE08,
                        Constants.NODE13);
                break;
            case Constants.PATH14:
                nodeList = ImmutableList.of(
                        Constants.NODE09,
                        Constants.NODE13);
                break;
            case Constants.PATH15:
                nodeList = ImmutableList.of(
                        Constants.NODE09,
                        Constants.NODE14);
                break;
            case Constants.PATH16:
                nodeList = ImmutableList.of(
                        Constants.NODE10,
                        Constants.NODE14);
                break;
            case Constants.PATH17:
                nodeList = ImmutableList.of(
                        Constants.NODE10,
                        Constants.NODE15);
                break;
            case Constants.PATH18:
                nodeList = ImmutableList.of(
                        Constants.NODE11,
                        Constants.NODE16);
                break;
            case Constants.PATH19:
                nodeList = ImmutableList.of(
                        Constants.NODE12,
                        Constants.NODE17);
                break;
            case Constants.PATH20:
                nodeList = ImmutableList.of(
                        Constants.NODE13,
                        Constants.NODE18);
                break;
            case Constants.PATH21:
                nodeList = ImmutableList.of(
                        Constants.NODE14,
                        Constants.NODE19);
                break;
            case Constants.PATH22:
                nodeList = ImmutableList.of(
                        Constants.NODE15,
                        Constants.NODE20);
                break;
            case Constants.PATH23:
                nodeList = ImmutableList.of(
                        Constants.NODE16,
                        Constants.NODE21);
                break;
            case Constants.PATH24:
                nodeList = ImmutableList.of(
                        Constants.NODE16,
                        Constants.NODE22);
                break;
            case Constants.PATH25:
                nodeList = ImmutableList.of(
                        Constants.NODE17,
                        Constants.NODE22);
                break;
            case Constants.PATH26:
                nodeList = ImmutableList.of(
                        Constants.NODE17,
                        Constants.NODE23);
                break;
            case Constants.PATH27:
                nodeList = ImmutableList.of(
                        Constants.NODE18,
                        Constants.NODE23);
                break;
            case Constants.PATH28:
                nodeList = ImmutableList.of(
                        Constants.NODE18,
                        Constants.NODE24);
                break;
            case Constants.PATH29:
                nodeList = ImmutableList.of(
                        Constants.NODE19,
                        Constants.NODE24);
                break;
            case Constants.PATH30:
                nodeList = ImmutableList.of(
                        Constants.NODE19,
                        Constants.NODE25);
                break;
            case Constants.PATH31:
                nodeList = ImmutableList.of(
                        Constants.NODE20,
                        Constants.NODE25);
                break;
            case Constants.PATH32:
                nodeList = ImmutableList.of(
                        Constants.NODE20,
                        Constants.NODE26);
                break;
            case Constants.PATH33:
                nodeList = ImmutableList.of(
                        Constants.NODE21,
                        Constants.NODE27);
                break;
            case Constants.PATH34:
                nodeList = ImmutableList.of(
                        Constants.NODE22,
                        Constants.NODE28);
                break;
            case Constants.PATH35:
                nodeList = ImmutableList.of(
                        Constants.NODE23,
                        Constants.NODE29);
                break;
            case Constants.PATH36:
                nodeList = ImmutableList.of(
                        Constants.NODE24,
                        Constants.NODE30);
                break;
            case Constants.PATH37:
                nodeList = ImmutableList.of(
                        Constants.NODE25,
                        Constants.NODE31);
                break;
            case Constants.PATH38:
                nodeList = ImmutableList.of(
                        Constants.NODE26,
                        Constants.NODE32);
                break;
            case Constants.PATH39:
                nodeList = ImmutableList.of(
                        Constants.NODE27,
                        Constants.NODE33);
                break;
            case Constants.PATH40:
                nodeList = ImmutableList.of(
                        Constants.NODE28,
                        Constants.NODE33);
                break;
            case Constants.PATH41:
                nodeList = ImmutableList.of(
                        Constants.NODE28,
                        Constants.NODE34);
                break;
            case Constants.PATH42:
                nodeList = ImmutableList.of(
                        Constants.NODE29,
                        Constants.NODE34);
                break;
            case Constants.PATH43:
                nodeList = ImmutableList.of(
                        Constants.NODE29,
                        Constants.NODE35);
                break;
            case Constants.PATH44:
                nodeList = ImmutableList.of(
                        Constants.NODE30,
                        Constants.NODE35);
                break;
            case Constants.PATH45:
                nodeList = ImmutableList.of(
                        Constants.NODE30,
                        Constants.NODE36);
                break;
            case Constants.PATH46:
                nodeList = ImmutableList.of(
                        Constants.NODE31,
                        Constants.NODE36);
                break;
            case Constants.PATH47:
                nodeList = ImmutableList.of(
                        Constants.NODE31,
                        Constants.NODE37);
                break;
            case Constants.PATH48:
                nodeList = ImmutableList.of(
                        Constants.NODE32,
                        Constants.NODE37);
                break;
            case Constants.PATH49:
                nodeList = ImmutableList.of(
                        Constants.NODE33,
                        Constants.NODE38);
                break;
            case Constants.PATH50:
                nodeList = ImmutableList.of(
                        Constants.NODE34,
                        Constants.NODE39);
                break;
            case Constants.PATH51:
                nodeList = ImmutableList.of(
                        Constants.NODE35,
                        Constants.NODE40);
                break;
            case Constants.PATH52:
                nodeList = ImmutableList.of(
                        Constants.NODE36,
                        Constants.NODE41);
                break;
            case Constants.PATH53:
                nodeList = ImmutableList.of(
                        Constants.NODE37,
                        Constants.NODE42);
                break;
            case Constants.PATH54:
                nodeList = ImmutableList.of(
                        Constants.NODE38,
                        Constants.NODE43);
                break;
            case Constants.PATH55:
                nodeList = ImmutableList.of(
                        Constants.NODE39,
                        Constants.NODE43);
                break;
            case Constants.PATH56:
                nodeList = ImmutableList.of(
                        Constants.NODE39,
                        Constants.NODE44);
                break;
            case Constants.PATH57:
                nodeList = ImmutableList.of(
                        Constants.NODE40,
                        Constants.NODE44);
                break;
            case Constants.PATH58:
                nodeList = ImmutableList.of(
                        Constants.NODE40,
                        Constants.NODE45);
                break;
            case Constants.PATH59:
                nodeList = ImmutableList.of(
                        Constants.NODE41,
                        Constants.NODE45);
                break;
            case Constants.PATH60:
                nodeList = ImmutableList.of(
                        Constants.NODE41,
                        Constants.NODE46);
                break;
            case Constants.PATH61:
                nodeList = ImmutableList.of(
                        Constants.NODE42,
                        Constants.NODE46);
                break;
            case Constants.PATH62:
                nodeList = ImmutableList.of(
                        Constants.NODE43,
                        Constants.NODE47);
                break;
            case Constants.PATH63:
                nodeList = ImmutableList.of(
                        Constants.NODE44,
                        Constants.NODE48);
                break;
            case Constants.PATH64:
                nodeList = ImmutableList.of(
                        Constants.NODE45,
                        Constants.NODE49);
                break;
            case Constants.PATH65:
                nodeList = ImmutableList.of(
                        Constants.NODE46,
                        Constants.NODE50);
                break;
            case Constants.PATH66:
                nodeList = ImmutableList.of(
                        Constants.NODE47,
                        Constants.NODE51);
                break;
            case Constants.PATH67:
                nodeList = ImmutableList.of(
                        Constants.NODE48,
                        Constants.NODE51);
                break;
            case Constants.PATH68:
                nodeList = ImmutableList.of(
                        Constants.NODE48,
                        Constants.NODE52);
                break;
            case Constants.PATH69:
                nodeList = ImmutableList.of(
                        Constants.NODE49,
                        Constants.NODE52);
                break;
            case Constants.PATH70:
                nodeList = ImmutableList.of(
                        Constants.NODE49,
                        Constants.NODE53);
                break;
            case Constants.PATH71:
                nodeList = ImmutableList.of(
                        Constants.NODE50,
                        Constants.NODE53);
                break;
        }
        
        return nodeList;
    }
	

    // Returns a list of paths connected to a node
    // Gets a list of paths attached to a specific node
	private ImmutableList<String> getPathsFromNode(
	        String node)
	{
	    ImmutableList<String> pathList = null;
	    
	    switch(node)
	    {
	        case Constants.NODE00:
	            pathList = ImmutableList.of(
	                    Constants.PATH00,
                        Constants.PATH01);
	            break;
            case Constants.NODE01:
                pathList = ImmutableList.of(
                        Constants.PATH02,
                        Constants.PATH03);
                break;
            case Constants.NODE02:
                pathList = ImmutableList.of(
                        Constants.PATH04,
                        Constants.PATH05);
                break;
            case Constants.NODE03:
                pathList = ImmutableList.of(
                        Constants.PATH00,
                        Constants.PATH06);
                break;
            case Constants.NODE04:
                pathList = ImmutableList.of(
                        Constants.PATH01,
                        Constants.PATH02,
                        Constants.PATH07);
                break;
            case Constants.NODE05:
                pathList = ImmutableList.of(
                        Constants.PATH03,
                        Constants.PATH04,
                        Constants.PATH08);
                break;
            case Constants.NODE06:
                pathList = ImmutableList.of(
                        Constants.PATH05,
                        Constants.PATH09);
                break;
            case Constants.NODE07:
                pathList = ImmutableList.of(
                        Constants.PATH06,
                        Constants.PATH10,
                        Constants.PATH11);
                break;
            case Constants.NODE08:
                pathList = ImmutableList.of(
                        Constants.PATH07,
                        Constants.PATH12,
                        Constants.PATH13);
                break;
            case Constants.NODE09:
                pathList = ImmutableList.of(
                        Constants.PATH08,
                        Constants.PATH14,
                        Constants.PATH15);
                break;
            case Constants.NODE10:
                pathList = ImmutableList.of(
                        Constants.PATH09,
                        Constants.PATH16,
                        Constants.PATH17);
                break;
            case Constants.NODE11:
                pathList = ImmutableList.of(
                        Constants.PATH10,
                        Constants.PATH18);
                break;
            case Constants.NODE12:
                pathList = ImmutableList.of(
                        Constants.PATH11,
                        Constants.PATH12,
                        Constants.PATH19);
                break;
            case Constants.NODE13:
                pathList = ImmutableList.of(
                        Constants.PATH13,
                        Constants.PATH14,
                        Constants.PATH20);
                break;
            case Constants.NODE14:
                pathList = ImmutableList.of(
                        Constants.PATH15,
                        Constants.PATH16,
                        Constants.PATH21);
                break;
            case Constants.NODE15:
                pathList = ImmutableList.of(
                        Constants.PATH17,
                        Constants.PATH22);
                break;
            case Constants.NODE16:
                pathList = ImmutableList.of(
                        Constants.PATH18,
                        Constants.PATH23,
                        Constants.PATH24);
                break;
            case Constants.NODE17:
                pathList = ImmutableList.of(
                        Constants.PATH19,
                        Constants.PATH25,
                        Constants.PATH26);
                break;
            case Constants.NODE18:
                pathList = ImmutableList.of(
                        Constants.PATH20,
                        Constants.PATH27,
                        Constants.PATH28);
                break;
            case Constants.NODE19:
                pathList = ImmutableList.of(
                        Constants.PATH21,
                        Constants.PATH29,
                        Constants.PATH30);
                break;
            case Constants.NODE20:
                pathList = ImmutableList.of(
                        Constants.PATH22,
                        Constants.PATH31,
                        Constants.PATH32);
                break;
            case Constants.NODE21:
                pathList = ImmutableList.of(
                        Constants.PATH23,
                        Constants.PATH33);
                break;
            case Constants.NODE22:
                pathList = ImmutableList.of(
                        Constants.PATH24,
                        Constants.PATH25,
                        Constants.PATH34);
                break;
            case Constants.NODE23:
                pathList = ImmutableList.of(
                        Constants.PATH26,
                        Constants.PATH27,
                        Constants.PATH35);
                break;
            case Constants.NODE24:
                pathList = ImmutableList.of(
                        Constants.PATH28,
                        Constants.PATH29,
                        Constants.PATH36);
                break;
            case Constants.NODE25:
                pathList = ImmutableList.of(
                        Constants.PATH30,
                        Constants.PATH31,
                        Constants.PATH37);
                break;
            case Constants.NODE26:
                pathList = ImmutableList.of(
                        Constants.PATH32,
                        Constants.PATH38);
                break;
            case Constants.NODE27:
                pathList = ImmutableList.of(
                        Constants.PATH33,
                        Constants.PATH39);
                break;
            case Constants.NODE28:
                pathList = ImmutableList.of(
                        Constants.PATH34,
                        Constants.PATH40,
                        Constants.PATH41);
                break;
            case Constants.NODE29:
                pathList = ImmutableList.of(
                        Constants.PATH35,
                        Constants.PATH42,
                        Constants.PATH43);
                break;
            case Constants.NODE30:
                pathList = ImmutableList.of(
                        Constants.PATH36,
                        Constants.PATH44,
                        Constants.PATH45);
                break;
            case Constants.NODE31:
                pathList = ImmutableList.of(
                        Constants.PATH37,
                        Constants.PATH46,
                        Constants.PATH47);
                break;
            case Constants.NODE32:
                pathList = ImmutableList.of(
                        Constants.PATH38,
                        Constants.PATH48);
                break;
            case Constants.NODE33:
                pathList = ImmutableList.of(
                        Constants.PATH39,
                        Constants.PATH40,
                        Constants.PATH49);
                break;
            case Constants.NODE34:
                pathList = ImmutableList.of(
                        Constants.PATH41,
                        Constants.PATH42,
                        Constants.PATH50);
                break;
            case Constants.NODE35:
                pathList = ImmutableList.of(
                        Constants.PATH43,
                        Constants.PATH44,
                        Constants.PATH51);
                break;
            case Constants.NODE36:
                pathList = ImmutableList.of(
                        Constants.PATH45,
                        Constants.PATH46,
                        Constants.PATH52);
                break;
            case Constants.NODE37:
                pathList = ImmutableList.of(
                        Constants.PATH47,
                        Constants.PATH48,
                        Constants.PATH53);
                break;
            case Constants.NODE38:
                pathList = ImmutableList.of(
                        Constants.PATH49,
                        Constants.PATH54);
                break;
            case Constants.NODE39:
                pathList = ImmutableList.of(
                        Constants.PATH50,
                        Constants.PATH55,
                        Constants.PATH56);
                break;
            case Constants.NODE40:
                pathList = ImmutableList.of(
                        Constants.PATH51,
                        Constants.PATH57,
                        Constants.PATH58);
                break;
            case Constants.NODE41:
                pathList = ImmutableList.of(
                        Constants.PATH52,
                        Constants.PATH59,
                        Constants.PATH60);
                break;
            case Constants.NODE42:
                pathList = ImmutableList.of(
                        Constants.PATH53,
                        Constants.PATH61);
                break;
            case Constants.NODE43:
                pathList = ImmutableList.of(
                        Constants.PATH54,
                        Constants.PATH55,
                        Constants.PATH62);
                break;
            case Constants.NODE44:
                pathList = ImmutableList.of(
                        Constants.PATH56,
                        Constants.PATH57,
                        Constants.PATH63);
                break;
            case Constants.NODE45:
                pathList = ImmutableList.of(
                        Constants.PATH58,
                        Constants.PATH59,
                        Constants.PATH64);
                break;
            case Constants.NODE46:
                pathList = ImmutableList.of(
                        Constants.PATH60,
                        Constants.PATH61,
                        Constants.PATH65);
                break;
            case Constants.NODE47:
                pathList = ImmutableList.of(
                        Constants.PATH62,
                        Constants.PATH66);
                break;
            case Constants.NODE48:
                pathList = ImmutableList.of(
                        Constants.PATH63,
                        Constants.PATH67,
                        Constants.PATH68);
                break;
            case Constants.NODE49:
                pathList = ImmutableList.of(
                        Constants.PATH64,
                        Constants.PATH69,
                        Constants.PATH70);
                break;
            case Constants.NODE50:
                pathList = ImmutableList.of(
                        Constants.PATH65,
                        Constants.PATH71);
                break;
            case Constants.NODE51:
                pathList = ImmutableList.of(
                        Constants.PATH66,
                        Constants.PATH67);
                break;
            case Constants.NODE52:
                pathList = ImmutableList.of(
                        Constants.PATH68,
                        Constants.PATH69);
                break;
            case Constants.NODE53:
                pathList = ImmutableList.of(
                        Constants.PATH70,
                        Constants.PATH71);
                break;
	    }
	    
	    return pathList;
	}
	
    // Gets whether a settlement can be added here for a specific player
	// Returns whether a specific player can add a settlement at a certain node
	public boolean canAddSettlementHere(
            Map<String, Object> lastState,
            String node,
            String playerString)
    {
        boolean noAdjacentNodeStatus = true;
        boolean roadPresetStatus = false;
        
        ImmutableList<String> pathsFromNode = getPathsFromNode(node);
        
        for(int i = 0; i < pathsFromNode.size(); i++)
        {
            ImmutableList<String> nodes = getNodesFromPath(pathsFromNode.get(i));
            
            for(int j = 0; j < nodes.size(); j++)
            {
                noAdjacentNodeStatus = noAdjacentNodeStatus && !lastState.containsKey(nodes.get(j));
            }
            
            roadPresetStatus = roadPresetStatus || (lastState.containsKey(pathsFromNode.get(i))
                                                 && lastState.get(pathsFromNode.get(i)).toString().contains(playerString));
        }
        
        return noAdjacentNodeStatus && roadPresetStatus;
    }
    
    // Gets whether a settlement can be added here for a specific player
    // Returns whether a specific player can add a settlement at a certain node
    public boolean canAddSettlementHereFirstMove(
            Map<String, Object> lastState,
            String node,
            String playerString)
    {
        boolean noAdjacentNodeStatus = true;
        
        ImmutableList<String> pathsFromNode = getPathsFromNode(node);
        
        for(int i = 0; i < pathsFromNode.size(); i++)
        {
            ImmutableList<String> nodes = getNodesFromPath(pathsFromNode.get(i));
            
            for(int j = 0; j < nodes.size(); j++)
            {
                noAdjacentNodeStatus = noAdjacentNodeStatus && !lastState.containsKey(nodes.get(j));
            }
        }
        
        return noAdjacentNodeStatus;
    }
    
	// Returns whether a specific player can add a road at a certain path
    // Gets whether a road can be added here for a specific player

    public boolean canAddRoadHere(
            Map<String, Object> lastState,
            String path,
            String playerString)
    {
        boolean status = false;
        
        ImmutableList<String> nodesFromPath = getNodesFromPath(path);
        
        for(int i = 0; i < nodesFromPath.size(); i++)
        {
            ImmutableList<String> paths = getPathsFromNode(nodesFromPath.get(i));
            
            for(int j = 0; j < paths.size(); j++)
            {
                status = status || ( lastState.containsKey(paths.get(j))
                                  && lastState.get(paths.get(j)).toString().contains(playerString));
            }
            
            status = status || ( lastState.containsKey(nodesFromPath.get(i))
                    && lastState.get(nodesFromPath.get(i)).toString().contains(playerString));
        }
        
        return status;
    }
    
    // Returns the Development Card that matches the given Development Card Type
    // Matches a development card to a development card type
    // Finds the first open development card on the stack
    // Returns the first available Development Card

    private String findFirstOpenDevelopmentCard()
    {
        String firstOpen = "";
        
        if(firstOpenDevelopmentCard < 10)
            firstOpen = Constants.DEVELOPMENTCARDTOKEN + "0" + firstOpenDevelopmentCard;
        else if(firstOpenDevelopmentCard < 25)
            firstOpen = Constants.DEVELOPMENTCARDTOKEN + firstOpenDevelopmentCard;
        
        return firstOpen;
    }

    // Determines what kind of move the player is attempting to make
    // Returns the move that is preliminarily expected based on the list of moves
    private String findExpectedMove(
            Map<String, Object> lastState,
            List<Operation> lastMove,
            String playerString,
            String nextPlayerString,
            String playerId)
    {
        String expectedMove = "";
        
        // Expects the first move from an empty state
        if ( lastState.isEmpty() )
        {
            initial = false;
            expectedMove = Constants.FIRSTMOVE;
            firstFreeMove = true;
            settlementTurn = true;
        }
        else if(firstFreeMove)
        {
            if(settlementTurn)
            {
                expectedMove = Constants.FIRSTROUNDSETTLEMENT;
            }
            else
            {
                expectedMove = Constants.FIRSTROUNDROAD;
            }
        }
        else if(secondFreeMove)
        {
            if(settlementTurn)
            {
                expectedMove = Constants.SECONDROUNDSETTLEMENT;
            }
            else
            {
                expectedMove = Constants.SECONDROUNDROAD;
            }
        }
        else if (finishRoadBuild)
        {
            expectedMove = Constants.BUILDROADPT2;
        }
        else if (finishSettlementBuild)
        {
            expectedMove = Constants.BUILDSETTLEMENTPT2;
        }
        else if (finishCityBuild)
        {
            expectedMove = Constants.BUILDCITYPT2;
        }
        else if (finishBuyingDevelopmentCard)
        {
            expectedMove = Constants.BUYDEVELOPMENTCARDPT2;
        }
        else if(finishHarborTrade)
        {
            expectedMove = Constants.HARBORTRADEPT2;
        }
        else if(finishRobberMove)
        {
            expectedMove = Constants.MOVEROBBERPT4;
        }
        else if( findASetRandomIntegerInMoves(lastMove) )
        {
            expectedMove = Constants.ROLLDICE;
        }
        else if( findADeleteDieInMoves(lastMove) )
        {
            expectedMove = Constants.CLEARROLL;
        }
        // Move contains a SET Turn command with the next player string
        // This is a CHANGETURN move
        else if ( findJustASetTurnMoveInMoves(lastMove, Constants.TURN) )
        {
            expectedMove = Constants.CHANGETURN;
        }
        // Move contains a SET City command
        // This is a BUILDCITY move
        else if ( findASetMoveInMoves(lastMove, Constants.CITYTOKEN, "") )
        {
            expectedMove = Constants.BUILDCITYPT1;
        }
        // Move contains a SET Settlement command
        // This is a BUILDSETTLEMENT move
        else if ( findASetMoveInMoves(lastMove, Constants.SETTLEMENTTOKEN, "") )
        {
            expectedMove = Constants.BUILDSETTLEMENTPT1;
        }
        // Move contains a SET Road command
        // This is a BUILDROAD move
        else if ( findASetMoveInMoves(lastMove, Constants.ROADTOKEN, "") )
        {
            expectedMove = Constants.BUILDROADPT1;
        }
        // Move contains a SET Development Card command with the current playerString
        // This is a BUYDEVELOPMENTCARD move
        else if ( findASetVisibleMoveInMoves(lastMove, Constants.DEVELOPMENTCARDTOKEN) )
        {
            expectedMove = Constants.BUYDEVELOPMENTCARDPT1;
        }
        // Move contains a SET Development Card command with the PLAYED tag
        // This is a PLAYDEVELOPMENTCARD move
        else if ( findASetMoveInMoves(lastMove, Constants.DEVELOPMENTCARDTOKEN, "") )
        {
            expectedMove = Constants.PLAYDEVELOPMENTCARD;
        }
        // Move contains a SET Robber Card
        // This is a PLAYDEVELOPMENTCARD move
        else if ( findASetMoveInMoves(lastMove, Constants.ROBBER, "") )
        {
            expectedMove = Constants.MOVEROBBERPT3;
        }
        // Move contains only a turn move and resources
        // This is a HARBORTRADE move
        else if ( findASetTurnMoveInMoves(lastMove, Constants.TURN)
               && !findASetMoveInMoves(lastMove, Constants.CITYTOKEN, "")
               && !findASetMoveInMoves(lastMove, Constants.SETTLEMENTTOKEN, "")
               && !findASetMoveInMoves(lastMove, Constants.ROADTOKEN, "")
               && !findASetMoveInMoves(lastMove, Constants.DEVELOPMENTCARDTOKEN, ""))
        {
            expectedMove = Constants.HARBORTRADEPT1;
        }
        else
        {
            expectedMove = "UNKNOWN";
        }
        
        System.out.println("EXPECTED MOVE - " + expectedMove);
        
        return expectedMove;
    }
    
    // Returns whether the list of moves contains a Set move with the given Key and Value
    // Searches all the SET moves in a move list and returns true if it finds
    // at least one move that has a key and value that contain the two input searches
    private boolean findJustASetTurnMoveInMoves(
            List<Operation> lastMove,
            String containsKeyString)
    {
        boolean status = false;
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            if(lastMove.get(i).getMessageName().equals("SetTurn"))
            {
                status = status
                      || ( lastMove.get(i).getMessageName().equals("SetTurn")
                      && lastMove.size() == 1);
            }
        }
        
        return status;
    }
    
    // Returns whether the list of moves contains a Set move with the given Key and Value
    // Searches all the SET moves in a move list and returns true if it finds
    // at least one move that has a key and value that contain the two input searches
    private boolean findASetTurnMoveInMoves(
            List<Operation> lastMove,
            String containsKeyString)
    {
        boolean status = false;
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            if(lastMove.get(i).getMessageName().equals("SetTurn"))
            {
                status = status
                      || lastMove.get(i).getMessageName().equals("SetTurn");
            }
        }
        
        return status;
    }
    
    // Returns whether the list of moves contains a Set move with the given Key and Value
    // Searches all the SET moves in a move list and returns true if it finds
    // at least one move that has a key and value that contain the two input searches
    private boolean findASetMoveInMoves(
            List<Operation> lastMove,
            String containsKeyString,
            String containsValueString)
    {
        boolean status = false;
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            if(lastMove.get(i).getMessageName().equals("Set"))
            {
                status = status
                      || (((Set)lastMove.get(i)).getKey().contains(containsKeyString)
                      &&  ((Set)lastMove.get(i)).getValue().toString().contains(containsValueString));
            }
        }
        
        return status;
    }
    
 // Returns whether the list of moves contains a Set move with the given Key and Value
    // Searches all the SET moves in a move list and returns true if it finds
    // at least one move that has a key and value that contain the two input searches
    private boolean findASetVisibleMoveInMoves(
            List<Operation> lastMove,
            String containsKeyString)
    {
        boolean status = false;
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            if(lastMove.get(i).getMessageName().equals("SetVisibility"))
            {
                status = status
                      || ((SetVisibility)lastMove.get(i)).getKey().contains(containsKeyString);
            }
        }
        
        return status;
    }
    
    // Returns whether the list of moves contains a SetRandomInteger move
    private boolean findASetRandomIntegerInMoves(
            List<Operation> lastMove)
    {
        boolean status = false;
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            if(lastMove.get(i).getMessageName().equals("SetRandomInteger"))
            {
                status = true;
            }
        }
        
        return status;
    }
    
    // Returns whether the list of moves contains a Delete move for DIE0
    private boolean findADeleteDieInMoves(
            List<Operation> lastMove)
    {
        boolean status = false;
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            if( lastMove.get(i).getMessageName().equals("Delete")
             && ((Delete)lastMove.get(i)).getKey().equals(Constants.DIE0))
            {
                status = true;
            }
        }
        
        return status;
    }
    
    // Returns the playerString for the current player
    // Gets the String Name of the player based on ID

    public String getPlayerId(
            List<String> playerIds,
            String lastMovePlayerId) {
        String playerString = "";
        
        if(playerIds.get(0) == lastMovePlayerId)
        {
            playerString = Constants.PB;
        }
        else if(playerIds.get(1) == lastMovePlayerId)
        {
            playerString = Constants.PR;
        }
        else if(playerIds.get(2) == lastMovePlayerId)
        {
            playerString = Constants.PY;
        }
        else if(playerIds.get(3) == lastMovePlayerId)
        {
            playerString = Constants.PG;
        }
        
        return playerString;
    }
    
    // Returns the playerString for the next player
    // Gets the String Name of the next player based on ID

    private String getNextPlayerId(
            List<String> playerIds,
            String lastMovePlayerId) {
        String playerString = "";
        
        if(playerIds.get(0) == lastMovePlayerId)
        {
            playerString = Constants.PR;
        }
        else if(playerIds.get(1) == lastMovePlayerId)
        {
            if(playerIds.size() == 2)
                playerString = Constants.PB;
            else
                playerString = Constants.PY;
        }
        else if(playerIds.get(2) == lastMovePlayerId)
        {
            if(playerIds.size() == 3)
                playerString = Constants.PB;
            else
                playerString = Constants.PG;
        }
        else if(playerIds.get(3) == lastMovePlayerId)
        {
            playerString = Constants.PB;
        }
        
        return playerString;
    }
    
    // Returns the playerString for the next player
    // Gets the String Name of the next player based on ID

    private String getPreviousPlayerId(
            List<String> playerIds,
            String lastMovePlayerId) {
        String playerString = "";
        
        if(playerIds.get(0) == lastMovePlayerId)
        {
            if(playerIds.size() == 4)
                playerString = Constants.PG;
            else if (playerIds.size() == 3)
                playerString = Constants.PY;
            else
                playerString = Constants.PR;
        }
        else if(playerIds.get(1) == lastMovePlayerId)
        {
            playerString = Constants.PB;
        }
        else if(playerIds.get(2) == lastMovePlayerId)
        {
            playerString = Constants.PR;
        }
        else if(playerIds.get(3) == lastMovePlayerId)
        {
            playerString = Constants.PY;
        }
        
        return playerString;
    }
    
    private List<String> getExpectedResourcesFromNode(Map<String, Object> state, String node)
    {
        List<String> expectedResources = new ArrayList<String>();
        
        switch(node)
        {
            case Constants.NODE00:
                expectedResources.add(state.get(Constants.HEX00).toString());
                break;
            case Constants.NODE01:
                expectedResources.add(state.get(Constants.HEX01).toString());
                break;
            case Constants.NODE02:
                expectedResources.add(state.get(Constants.HEX02).toString());
                break;
            case Constants.NODE03:
                expectedResources.add(state.get(Constants.HEX00).toString());
                break;
            case Constants.NODE04:
                expectedResources.add(state.get(Constants.HEX00).toString());
                expectedResources.add(state.get(Constants.HEX01).toString());
                break;
            case Constants.NODE05:
                expectedResources.add(state.get(Constants.HEX01).toString());
                expectedResources.add(state.get(Constants.HEX02).toString());
                break;
            case Constants.NODE06:
                expectedResources.add(state.get(Constants.HEX02).toString());
                break;
            case Constants.NODE07:
                expectedResources.add(state.get(Constants.HEX00).toString());
                expectedResources.add(state.get(Constants.HEX03).toString());
                break;
            case Constants.NODE08:
                expectedResources.add(state.get(Constants.HEX00).toString());
                expectedResources.add(state.get(Constants.HEX01).toString());
                expectedResources.add(state.get(Constants.HEX04).toString());
                break;
            case Constants.NODE09:
                expectedResources.add(state.get(Constants.HEX01).toString());
                expectedResources.add(state.get(Constants.HEX02).toString());
                expectedResources.add(state.get(Constants.HEX05).toString());
                break;
            case Constants.NODE10:
                expectedResources.add(state.get(Constants.HEX02).toString());
                expectedResources.add(state.get(Constants.HEX06).toString());
                break;
            case Constants.NODE11:
                expectedResources.add(state.get(Constants.HEX03).toString());
                break;
            case Constants.NODE12:
                expectedResources.add(state.get(Constants.HEX00).toString());
                expectedResources.add(state.get(Constants.HEX03).toString());
                expectedResources.add(state.get(Constants.HEX04).toString());
                break;
            case Constants.NODE13:
                expectedResources.add(state.get(Constants.HEX01).toString());
                expectedResources.add(state.get(Constants.HEX04).toString());
                expectedResources.add(state.get(Constants.HEX05).toString());
                break;
            case Constants.NODE14:
                expectedResources.add(state.get(Constants.HEX02).toString());
                expectedResources.add(state.get(Constants.HEX05).toString());
                expectedResources.add(state.get(Constants.HEX06).toString());
                break;
            case Constants.NODE15:
                expectedResources.add(state.get(Constants.HEX06).toString());
                break;
            case Constants.NODE16:
                expectedResources.add(state.get(Constants.HEX03).toString());
                expectedResources.add(state.get(Constants.HEX07).toString());
                break;
            case Constants.NODE17:
                expectedResources.add(state.get(Constants.HEX03).toString());
                expectedResources.add(state.get(Constants.HEX04).toString());
                expectedResources.add(state.get(Constants.HEX08).toString());
                break;
            case Constants.NODE18:
                expectedResources.add(state.get(Constants.HEX04).toString());
                expectedResources.add(state.get(Constants.HEX05).toString());
                expectedResources.add(state.get(Constants.HEX09).toString());
                break;
            case Constants.NODE19:
                expectedResources.add(state.get(Constants.HEX05).toString());
                expectedResources.add(state.get(Constants.HEX06).toString());
                expectedResources.add(state.get(Constants.HEX10).toString());
                break;
            case Constants.NODE20:
                expectedResources.add(state.get(Constants.HEX06).toString());
                expectedResources.add(state.get(Constants.HEX11).toString());
                break;
            case Constants.NODE21:
                expectedResources.add(state.get(Constants.HEX07).toString());
                break;
            case Constants.NODE22:
                expectedResources.add(state.get(Constants.HEX03).toString());
                expectedResources.add(state.get(Constants.HEX07).toString());
                expectedResources.add(state.get(Constants.HEX08).toString());
                break;
            case Constants.NODE23:
                expectedResources.add(state.get(Constants.HEX04).toString());
                expectedResources.add(state.get(Constants.HEX08).toString());
                expectedResources.add(state.get(Constants.HEX09).toString());
                break;
            case Constants.NODE24:
                expectedResources.add(state.get(Constants.HEX05).toString());
                expectedResources.add(state.get(Constants.HEX09).toString());
                expectedResources.add(state.get(Constants.HEX10).toString());
                break;
            case Constants.NODE25:
                expectedResources.add(state.get(Constants.HEX06).toString());
                expectedResources.add(state.get(Constants.HEX10).toString());
                expectedResources.add(state.get(Constants.HEX11).toString());
                break;
            case Constants.NODE26:
                expectedResources.add(state.get(Constants.HEX11).toString());
                break;
            case Constants.NODE27:
                expectedResources.add(state.get(Constants.HEX07).toString());
                break;
            case Constants.NODE28:
                expectedResources.add(state.get(Constants.HEX07).toString());
                expectedResources.add(state.get(Constants.HEX08).toString());
                expectedResources.add(state.get(Constants.HEX12).toString());
                break;
            case Constants.NODE29:
                expectedResources.add(state.get(Constants.HEX08).toString());
                expectedResources.add(state.get(Constants.HEX09).toString());
                expectedResources.add(state.get(Constants.HEX13).toString());
                break;
            case Constants.NODE30:
                expectedResources.add(state.get(Constants.HEX09).toString());
                expectedResources.add(state.get(Constants.HEX10).toString());
                expectedResources.add(state.get(Constants.HEX14).toString());
                break;
            case Constants.NODE31:
                expectedResources.add(state.get(Constants.HEX10).toString());
                expectedResources.add(state.get(Constants.HEX11).toString());
                expectedResources.add(state.get(Constants.HEX15).toString());
                break;
            case Constants.NODE32:
                expectedResources.add(state.get(Constants.HEX11).toString());
                break;
            case Constants.NODE33:
                expectedResources.add(state.get(Constants.HEX07).toString());
                expectedResources.add(state.get(Constants.HEX12).toString());
                break;
            case Constants.NODE34:
                expectedResources.add(state.get(Constants.HEX08).toString());
                expectedResources.add(state.get(Constants.HEX12).toString());
                expectedResources.add(state.get(Constants.HEX13).toString());
                break;
            case Constants.NODE35:
                expectedResources.add(state.get(Constants.HEX09).toString());
                expectedResources.add(state.get(Constants.HEX13).toString());
                expectedResources.add(state.get(Constants.HEX14).toString());
                break;
            case Constants.NODE36:
                expectedResources.add(state.get(Constants.HEX10).toString());
                expectedResources.add(state.get(Constants.HEX14).toString());
                expectedResources.add(state.get(Constants.HEX15).toString());
                break;
            case Constants.NODE37:
                expectedResources.add(state.get(Constants.HEX11).toString());
                expectedResources.add(state.get(Constants.HEX15).toString());
                break;
            case Constants.NODE38:
                expectedResources.add(state.get(Constants.HEX12).toString());
                break;
            case Constants.NODE39:
                expectedResources.add(state.get(Constants.HEX12).toString());
                expectedResources.add(state.get(Constants.HEX13).toString());
                expectedResources.add(state.get(Constants.HEX16).toString());
                break;
            case Constants.NODE40:
                expectedResources.add(state.get(Constants.HEX13).toString());
                expectedResources.add(state.get(Constants.HEX14).toString());
                expectedResources.add(state.get(Constants.HEX17).toString());
                break;
            case Constants.NODE41:
                expectedResources.add(state.get(Constants.HEX14).toString());
                expectedResources.add(state.get(Constants.HEX15).toString());
                expectedResources.add(state.get(Constants.HEX18).toString());
                break;
            case Constants.NODE42:
                expectedResources.add(state.get(Constants.HEX15).toString());
                break;
            case Constants.NODE43:
                expectedResources.add(state.get(Constants.HEX12).toString());
                expectedResources.add(state.get(Constants.HEX16).toString());
                break;
            case Constants.NODE44:
                expectedResources.add(state.get(Constants.HEX13).toString());
                expectedResources.add(state.get(Constants.HEX16).toString());
                expectedResources.add(state.get(Constants.HEX17).toString());
                break;
            case Constants.NODE45:
                expectedResources.add(state.get(Constants.HEX14).toString());
                expectedResources.add(state.get(Constants.HEX17).toString());
                expectedResources.add(state.get(Constants.HEX18).toString());
                break;
            case Constants.NODE46:
                expectedResources.add(state.get(Constants.HEX15).toString());
                expectedResources.add(state.get(Constants.HEX18).toString());
                break;
            case Constants.NODE47:
                expectedResources.add(state.get(Constants.HEX16).toString());
                break;
            case Constants.NODE48:
                expectedResources.add(state.get(Constants.HEX16).toString());
                expectedResources.add(state.get(Constants.HEX17).toString());
                break;
            case Constants.NODE49:
                expectedResources.add(state.get(Constants.HEX17).toString());
                expectedResources.add(state.get(Constants.HEX18).toString());
                break;
            case Constants.NODE50:
                expectedResources.add(state.get(Constants.HEX18).toString());
                break;
            case Constants.NODE51:
                expectedResources.add(state.get(Constants.HEX16).toString());
                break;
            case Constants.NODE52:
                expectedResources.add(state.get(Constants.HEX17).toString());
                break;
            case Constants.NODE53:
                expectedResources.add(state.get(Constants.HEX18).toString());
                break;
        }
        
        expectedResources.remove(Constants.DESERT);
        
        return expectedResources;
    }
    
    // Checks if String is an integer
    // From http://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
    private static boolean isInteger(String s)
    {
        try
        { 
            Integer.parseInt(s); 
        }
        catch(NumberFormatException e)
        { 
            return false; 
        }
        // only got here if we didn't return false
        return true;
    }
    
    public boolean checkLongestRoadClaim(String playerString, Map<String, Object> lastState)
    {
        boolean status = false;
        List<String> longestPath = null;
        String startingRoad = "";
        
        for (int i = 0; i < 15; i++)
        {
            List<String> pathToCheck = new ArrayList<String>();
            
            if(i < 10)
                startingRoad = Constants.ROADTOKEN + "0" + i + playerString;
            else
                startingRoad = Constants.ROADTOKEN + i + playerString;
            
            if(lastState.containsKey(startingRoad))
            {
                pathToCheck = checkNextPath(
                        lastState,
                        pathToCheck,
                        lastState.get(startingRoad).toString(),
                        playerString);
            }
            
            if( longestPath == null
             || pathToCheck.size() > longestPath.size())
            {
                longestPath = pathToCheck;
            }
        }
        
        if(!lastState.containsKey(Constants.LONGESTROAD)
         && longestPath.size() > 4)
        {
            status = true;
        }
        else if(longestPath.size() > 4)
        {
            String currentLongestPathHolder = lastState.get(Constants.LONGESTROAD).toString();
            List<String> longestPathHolder = null;
            
            for (int i = 0; i < 10; i++)
            {
                List<String> pathToCheck = new ArrayList<String>();
                
                if(i < 10)
                    startingRoad = Constants.ROADTOKEN + "0" + i + currentLongestPathHolder;
                else
                    startingRoad = Constants.ROADTOKEN + i + currentLongestPathHolder;
                
                if(lastState.containsKey(startingRoad))
                {
                    pathToCheck = checkNextPath(
                            lastState,
                            pathToCheck,
                            lastState.get(startingRoad).toString(),
                            currentLongestPathHolder);
                }
                
                if( longestPathHolder == null
                 || pathToCheck.size() > longestPathHolder.size())
                {
                    longestPathHolder = pathToCheck;
                }
            }
            
            if(longestPath.size() > longestPathHolder.size())
            {
                status = true;
            }
        }
        
        return status;
    }
}