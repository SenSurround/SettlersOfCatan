package settlersofcatan.client;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import settlersofcatan.client.GameApi.Delete;
import settlersofcatan.client.GameApi.EndGame;
import settlersofcatan.client.GameApi.Operation;
import settlersofcatan.client.GameApi.Set;
import settlersofcatan.client.GameApi.SetVisibility;
import settlersofcatan.client.GameApi.VerifyMove;
import settlersofcatan.client.GameApi.VerifyMoveDone;



public class SettlersOfCatanLogic {  
    
    // Global Error String
    // For Debug Purposes
    private String err = "";
    
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
	    int lastMovePlayerId = verifyMove.getLastMovePlayerId();
	    String playerString = getPlayerId(verifyMove.getPlayerIds(), lastMovePlayerId);
        String nextPlayerString = getNextPlayerId(verifyMove.getPlayerIds(), lastMovePlayerId);
	    
	    // Attempt to determine which move is being attempted
	    String expectedMove = findExpectedMove(lastMove, playerString, nextPlayerString);
	    
	    // See whether the set of moves matches the expected move
	    // It also verifies whether the move is legal given the last state
	    boolean status = moveIsLegal(
	            expectedMove,
	            lastMove,
	            lastState,
	            playerString,
	            lastMovePlayerId,
	            nextPlayerString);
	    
	    if(!status)
	    {
	        throw new RuntimeException("We have a hacker!\n" + err);
	    }
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
	        int playerId,
	        String nextPlayerString)
	{
	    boolean status = false;
	    
	    switch(expectedMove)
	    {
    	    case Constants.CHANGETURN:
                status = isChangeTurnMoveLegal(lastMove, nextPlayerString);
    	        break;
    	    case Constants.BUILDCITY:
                status = isBuildCityMoveLegal(lastMove, lastState, playerString, playerId);
                break;
            case Constants.BUILDSETTLEMENT:
                status = isBuildSettlementMoveLegal(lastMove, lastState, playerString, playerId);
                break;
            case Constants.BUILDROAD:
                status = isBuildRoadMoveLegal(lastMove, lastState, playerString, playerId);
                break;
            case Constants.BUYDEVELOPMENTCARD:
                status = isBuyDevelopmentCardMoveLegal(lastMove, lastState, playerString, playerId);
                break;
            case Constants.PLAYDEVELOPMENTCARD:
                status = isPlayDevelopmentCardMoveLegal(lastMove, lastState, playerString, playerId);
                break;
            case Constants.HARBORTRADE:
                status = isHarborTradeMoveLegal(lastMove, lastState, playerString);
                break;
            default:
                err = "No Legal Move Detected";
                break;
	    }
	    
	    return status;
	}
	    

    // Parent Function for Change Turn
    // Determines if this entire move matches for changing turns
    private boolean isChangeTurnMoveLegal(
            List<Operation> lastMove,
            String nextPlayerString)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, nextPlayerString)
        
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
                          nextPlayerString);
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "CHANGETURN expects: 1";
        }
        
        return status;
    }

    // Parent Function for Building City
    // Determines if this entire move matches for building a city
    private boolean isBuildCityMoveLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            int playerId)
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
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // DELETE(RESOURCECARDCC + playerString)
        // DELETE(RESOURCECARDDD + playerString)
        // DELETE(RESOURCECARDEE + playerString)
        // OPTIONAL ENDGAME(playerId)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2,3 - NODE and CITY must be the same
        //               NODE must contain SETTLEMENT
        //               CITY must not have an attached different NODE prior
        //               SETTLEMENT must have an attached this NODE prior
        // Lines 4,5,6,7,8 - RESOURCECARD must match lines 9,10,11,12,13
        //                   AA, BB, CC, DD, EE must be 3 ore, 2 grain
        // Line 14 - Asserts the game has ended for playerId
        
        // Ensure move is exactly 14 or 15 lines
        boolean status = lastMove.size() == 14
                      || lastMove.size() == 15;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString);
            
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
                  && matchCityResources(
                          lastMove.get(4), 4, 
                          lastMove.get(5), 5, 
                          lastMove.get(6), 6, 
                          lastMove.get(7), 7, 
                          lastMove.get(8), 8, 
                          lastMove.get(9), 9, 
                          lastMove.get(10), 10, 
                          lastMove.get(11), 11, 
                          lastMove.get(12), 12, 
                          lastMove.get(13), 13, 
                          lastState,
                          playerString
                          );
            
            if(lastMove.size() == 15)
            {
                // Line 14
                status = status
                      && matchEndGame(
                              lastMove.get(14), 14, 
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
                + "BUILDCITY expects: 14";
        }
        
        return status;
    }

    // Parent Function for Building Settlements
    // Determines if this entire move matches for building a settlement
    private boolean isBuildSettlementMoveLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            int playerId)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // SET(NODEXX, SETTLEMENTYY + playerString)
        // SET(SETTLEMENTYY + playerString, NODEXX)
        // SETVISIBILITY(RESOURCECARDAA + playerString)
        // SETVISIBILITY(RESOURCECARDBB + playerString)
        // SETVISIBILITY(RESOURCECARDCC + playerString)
        // SETVISIBILITY(RESOURCECARDDD + playerString)
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // DELETE(RESOURCECARDCC + playerString)
        // DELETE(RESOURCECARDDD + playerString)
        // OPTIONAL ENDGAME(playerId)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - NODE and SETTLEMENT must be the same
        //             NODE must be empty prior
        //             NODE must pass canAddNodeHere
        //             SETTLEMENT must not have an attached NODE prior
        // Lines 3,4,5,6 - RESOURCECARD must match lines 7,8,9,10
        //                 AA, BB, CC, DD must be 1 brick, 1 lumber, 1 wool, 1 grain
        // Line 11 - Asserts the game has ended for playerId
        
        // Ensure move is exactly 11 or 12 lines
        boolean status = lastMove.size() == 11
                      || lastMove.size() == 12;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString);
            
            // Lines 1,2
            status = status
                  && matchSettlementAndNodePair(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString);
            
            // Lines 3,4,5,6,7,8,9,10
            status = status
                  && matchSettlementResources(
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastMove.get(5), 5, 
                          lastMove.get(6), 6, 
                          lastMove.get(7), 7, 
                          lastMove.get(8), 8, 
                          lastMove.get(9), 9, 
                          lastMove.get(10), 10, 
                          lastState,
                          playerString
                          );
            
            if(lastMove.size() == 12)
            {
                // Line 11
                status = status
                      && matchEndGame(
                              lastMove.get(11), 11, 
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
    private boolean isBuildRoadMoveLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            int playerId)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // SET(PATHXX, ROADYY + playerString)
        // SET(ROADYY + playerString, PATHXX)
        // SETVISIBILITY(RESOURCECARDAA + playerString)
        // SETVISIBILITY(RESOURCECARDBB + playerString)
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // OPTIONAL - SET(LONGESTROAD, playerString)
        // OPTIONAL - ENDGAME(playerId)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - PATH and ROAD must be the same
        //             PATH must be empty prior
        //             PATH must pass canAddRoadHere
        //             ROAD must not have an attached NODE prior
        // Lines 3,4 - RESOURCECARD must match lines 5,6
        //             AA, BB, must be 1 brick, 1 lumber
        // Line 8 - Asserts the game has ended for playerId
        
        // Ensure move is exactly 7,8,9 lines
        boolean status = lastMove.size() == 7
                      || lastMove.size() == 8
                      || lastMove.size() == 9;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString);
            
            // Lines 1,2
            status = status
                  && matchRoadAndPathPair(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString);
            
            // Lines 3,4,5,6
            status = status
                  && matchRoadResources(
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastMove.get(5), 5, 
                          lastMove.get(6), 6, 
                          lastState,
                          playerString
                          );
            
            if(lastMove.size() > 7)
            {
                // Create a new state with the path added, to verify longest road claim
                Map<String, Object> newState = new HashMap<String, Object>(lastState);
                newState.put(((Set)lastMove.get(1)).getKey(), ((Set)lastMove.get(1)).getValue());
                newState.put(((Set)lastMove.get(2)).getKey(), ((Set)lastMove.get(2)).getValue());
                
                // Line 7
                status = status
                      && processLongestRoadClaim(
                              lastMove.get(7), 7, 
                              newState,
                              playerString);
                
                if(lastMove.size() == 9)
                {
                    // Line 8
                    status = status
                          && matchEndGame(
                                  lastMove.get(8), 8, 
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
                + "BUILDROAD expects: 7";
        }
        
        return status;
    }

    // Parent Function for Buying Development Cards
    // Determines if this entire move matches for buying development cards
    private boolean isBuyDevelopmentCardMoveLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            int playerId)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // SET(DEVELOPMENTCARDXX, playerString)
        // SETVISIBILITY(DEVELOPMENTCARDTYPEXX, visibleTo + playerId)
        // SETVISIBILITY(RESOURCECARDAA + playerString)
        // SETVISIBILITY(RESOURCECARDBB + playerString)
        // SETVISIBILITY(RESOURCECARDCC + playerString)
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // DELETE(RESOURCECARDCC + playerString)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - DEVELOPMENTCARDXX must map to DEVELOPMENTCARDTYPEXX
        //             DEVELOPMENTCARDXX must be the first card available
        //             Must assign the DEVELOPMENTCARDXX to playerString
        // Lines 3,4,5 - RESOURCECARD must match lines 6,7,8
        //               AA, BB, CC, must be 1 ore, 1 grain, 1 wool
        
        // Ensure move is exactly 9 lines
        boolean status = lastMove.size() == 9;
        
        if(status)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString);
            
            // Lines 1,2
            status = status
                  && matchDevelopmentCardandDevelopmentCardTypePair(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString,
                          playerId);
            
            // Lines 3,4,5,6,7,8,9,10
            status = status
                  && matchBuyDevelopmentCardResources(
                          lastMove.get(3), 3, 
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
                + "BUYDEVELOPMENTCARD expects: 9";
        }
        
        return status;
    }

    // Parent Function for Play Development Card
    // Determines if this entire move matches for playing a development card
    private boolean isPlayDevelopmentCardMoveLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString,
            int playerId)
    {
        // There are 4 types of cards that can be played

        // EXPECTED PLAY SOLDIER FORM
        // SET(TURN, playerString)
        // SET(DEVELOPMENTCARDXX, PLAYED)
        // SETVISIBILITY(DEVELOPMENTCARDTYPEXX)
        // SET(ROBBER, HEX)
        // SET(SOLDIERCOUNT + playerId, VAL)
        // OPTIONAL - SET(LARGESTARMY, playerString)
        // OPTIONAL - ENDGAME(playerId)
        //
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - DEVELOPMENTCARDXX must map to DEVELOPMENTCARDTYPEXX
        //             DEVELOPMENTCARDXX must be owned by playerString
        //             Must assign DEVELOPMENTCARDXX to PLAYED
        // Line 3 - Determine where to move the robber
        //          Must be different than the current robber HEX
        // Line 4 - Update the SOLDIERCOUNT for the current player
        //          Must increment the previous value
        // Line 5 - Set the LARGESTARMY to the current player.
        //          This line only happens when the SOLDIERCOUNT set
        //          earlier is one greater than the current holder of
        //          LARGESTARMY
        // Line 6 - Sets the ENDGAME for the current player.
        //          This line only happens if LARGESTARMY is set
        //          for the current player and it pushes the current player
        //          at or over 10 victory points
        
        boolean status = false;
        
        if( findASetMoveInMoves(lastMove, Constants.ROBBER, "")
         && ( lastMove.size() == 5
           || lastMove.size() == 6
           || lastMove.size() == 7 ) )
        {
            // Line 0
            status = matchTurnMoveForSamePlayer(
                            lastMove.get(0), 0, 
                            playerString);
            
            // Lines 1,2
            status = status
                  && matchDevelopmentCardandDevelopmentCardTypePlayedPair(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastState, 
                          playerString,
                          Constants.DEVELOPMENTCARDTYPEDEF00);
              
              // Lines 3,4
              status = status
                    && matchRobber(
                            lastMove.get(3), 3,
                            lastMove.get(4), 4,
                            lastState,
                            playerString
                            );
              
              if(lastMove.size() > 5)
              {
                  // Create a new state with the path added, to verify longest road claim
                  Map<String, Object> newState = new HashMap<String, Object>(lastState);
                  newState.put(((Set)lastMove.get(4)).getKey(), ((Set)lastMove.get(4)).getValue());
                  
                  // Line 5
                  status = status
                        && processLargestArmyClaim(
                                lastMove.get(5), 5, 
                                newState,
                                playerString,
                                ((Set)lastMove.get(4)).getKey());
                  
                  if(lastMove.size() == 7)
                  {
                      // Line 6
                      status = status
                            && matchEndGame(
                                    lastMove.get(6), 6, 
                                    newState,
                                    playerString,
                                    playerId,
                                    Constants.ADDLONGESTROAD);
                  }
              }
        }

        // EXPECTED YEAR OF PLENTY FORM
        // SET(TURN, playerString)
        // SET(DEVELOPMENTCARDXX, PLAYED)
        // SETVISIBILITY(DEVELOPMENTCARDTYPEXX)
        // SET(RESOURCECARDAA + playerString, resource)
        // SET(RESOURCECARDBB + playerString, resource)
        //
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - DEVELOPMENTCARDXX must map to DEVELOPMENTCARDTYPEXX
        //             DEVELOPMENTCARDXX must be owned by playerString
        //             Must assign DEVELOPMENTCARDXX to PLAYED
        // Lines 3,4 - Two RESOURCE cards for playerString to choose

        else if( findASetMoveInMoves(lastMove, Constants.RESOURCECARDTOKEN, "")
              && lastMove.size() == 5 )
         {
             // Line 0
             status = matchTurnMoveForSamePlayer(
                             lastMove.get(0), 0, 
                             playerString);
             
             // Lines 1,2
             status = status
                   && matchDevelopmentCardandDevelopmentCardTypePlayedPair(
                           lastMove.get(1), 1, 
                           lastMove.get(2), 2, 
                           lastState, 
                           playerString,
                           Constants.DEVELOPMENTCARDTYPEDEF01);
               
               // Lines 3
               status = status
                     && matchAddResource(
                             lastMove.get(3), 3,
                             lastState,
                             playerString
                             );
               
               // Lines 4
               status = status
                     && matchAddResource(
                             lastMove.get(4), 4,
                             lastState,
                             playerString
                             );
         }
        
        // EXPECTED MONOPOLY FORM
        // SET(TURN, playerString)
        // SET(DEVELOPMENTCARDXX, PLAYED)
        // SETVISIBILITY(DEVELOPMENTCARDTYPEXX)
        // SET(MONOPOLYRESOURCE, resource)
        // SET(MONOPOLYBENEFACTOR, playerString)
        //
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - DEVELOPMENTCARDXX must map to DEVELOPMENTCARDTYPEXX
        //             DEVELOPMENTCARDXX must be owned by playerString
        //             Must assign DEVELOPMENTCARDXX to PLAYED
        // Lines 3 - Assign the MONOPOLYRESOURCE to a specific resource
        // Lines 4 - Assign the MONOPOLYBENEFACTOR to playerString

        else if( findASetMoveInMoves(lastMove, Constants.MONOPOLYRESOURCE, "")
                && lastMove.size() == 5 )
        {
            // Line 0
            status = matchTurnMoveForSamePlayer(
                            lastMove.get(0), 0, 
                            playerString);
           
            // Lines 1,2
            status = status
                    && matchDevelopmentCardandDevelopmentCardTypePlayedPair(
                            lastMove.get(1), 1, 
                            lastMove.get(2), 2, 
                            lastState, 
                            playerString,
                            Constants.DEVELOPMENTCARDTYPEDEF02);
             
            // Lines 3,4
            status = status
                    && matchMonopoly(
                            lastMove.get(3), 3,
                            lastMove.get(4), 4,
                            lastState,
                            playerString
                            );
        }
        
        // EXPECTED ROAD BUILDING
        // SET(TURN, playerString)
        // SET(DEVELOPMENTCARDXX, PLAYED)
        // SETVISIBILITY(DEVELOPMENTCARDTYPEXX)
        // SET(PATHXX, ROADYY + playerString)
        // SET(ROADYY + playerString, PATHXX)
        // SET(PATHZZ, ROADTT + playerString)
        // SET(ROADTT + playerString, PATHZZ)
        // OPTIONAL - SET(LONGESTROAD, playerString)
        // OPTIONAL - ENDGAME(playerId)
        //
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // Lines 1,2 - DEVELOPMENTCARDXX must map to DEVELOPMENTCARDTYPEXX
        //             DEVELOPMENTCARDXX must be owned by playerString
        //             Must assign DEVELOPMENTCARDXX to PLAYED
        // Lines 3,4 - PATH and ROAD must be the same
        //             PATH must be empty prior
        //             PATH must pass canAddRoadHere
        //             ROAD must not have an attached NODE prior
        // Lines 5,6 - PATH and ROAD must be the same
        //             PATH must be empty prior
        //             PATH must pass canAddRoadHere
        //             ROAD must not have an attached NODE prior
        // Line 7 - Set the LONGESTROAD to the current player.
        //          This line only happens when the length of the
        //          current player's longest road is at least
        //          one greater than the current holder of
        //          LONGESTROAD
        // Line 8 - Sets the ENDGAME for the current player.
        //          This line only happens if LARGESTARMY is set
        //          for the current player and it pushes the current player
        //          at or over 10 victory points
        
        else if( findASetMoveInMoves(lastMove, Constants.PATHTOKEN, playerString)
                && ( lastMove.size() == 7
                  || lastMove.size() == 8
                  || lastMove.size() == 9 ) )
         {
             // Line 0
             status = matchTurnMoveForSamePlayer(
                             lastMove.get(0), 0, 
                             playerString);
             
             // Lines 1,2
             status = status
                   && matchDevelopmentCardandDevelopmentCardTypePlayedPair(
                           lastMove.get(1), 1, 
                           lastMove.get(2), 2, 
                           lastState, 
                           playerString,
                           Constants.DEVELOPMENTCARDTYPEDEF03);

             // Lines 3,4
             status = status
                   && matchRoadAndPathPair(
                           lastMove.get(3), 3, 
                           lastMove.get(4), 4, 
                           lastState, 
                           playerString);

             // Lines 5,6
             status = status
                   && matchRoadAndPathPair(
                           lastMove.get(5), 5, 
                           lastMove.get(6), 6, 
                           lastState, 
                           playerString);
               
               if(lastMove.size() > 7)
               {
                   // Create a new state with the paths added, to verify longest road claim
                   Map<String, Object> newState = new HashMap<String, Object>(lastState);
                   newState.put(((Set)lastMove.get(3)).getKey(), ((Set)lastMove.get(3)).getValue());
                   newState.put(((Set)lastMove.get(4)).getKey(), ((Set)lastMove.get(4)).getValue());
                   newState.put(((Set)lastMove.get(5)).getKey(), ((Set)lastMove.get(5)).getValue());
                   newState.put(((Set)lastMove.get(6)).getKey(), ((Set)lastMove.get(6)).getValue());
                   
                   // Line 7
                   status = status
                         && processLongestRoadClaim(
                                 lastMove.get(7), 7, 
                                 newState,
                                 playerString);
                   
                   if(lastMove.size() == 9)
                   {
                       // Line 8
                       status = status
                             && matchEndGame(
                                     lastMove.get(8), 8, 
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
                + "BUYDEVELOPMENTCARD expects: 9";
        }
        
        return status;
    }
    
    // Parent Function for Harbor Trades
    // Determines if this entire move matches for a harbor trade
    private boolean isHarborTradeMoveLegal(
            List<Operation> lastMove,
            Map<String, Object> lastState,
            String playerString)
    {
        // EXPECTED MOVE FORM
        // SET(TURN, playerString)
        // SETVISIBILITY(RESOURCECARDAA + playerString)
        // SETVISIBILITY(RESOURCECARDBB + playerString)
        // ***
        // DELETE(RESOURCECARDAA + playerString)
        // DELETE(RESOURCECARDBB + playerString)
        // ***
        // SET(RESOURCECARDAA+ playerString, resource)
        
        // REQUIREMENTS
        // Line 0 - Turn must match playerString
        // 
        // DIFFERING BRANCHES
        //
        // Normal Harbor trade
        // Lines 1,2,3,4 - RESOURCECARD must match lines 5,6,7,8
        //                 AA, BB, CC, DD must be the same resource
        // Line 9 - New resource requested
        // 
        // Three to One Harbor trade
        // Lines 1,2,3 - RESOURCECARD must match lines 4,5,6
        //               AA, BB, CC, must be the same resource
        //               playerString must own a city or settlement
        //               on a harbor allowing 3-1 trading
        // Line 7 - New resource requested
        // 
        // Two to One Specific Resource Harbor trade
        // Lines 1,2 - RESOURCECARD must match lines 3,4
        //             AA, BB, must be the same resource
        //             playerString must own a city or settlement
        //             on a harbor allowing 2-1 trading
        //             That harbor must be specifically catered
        //             to the resources being traded
        // Line 5 - New resource requested
        
        // Ensure move is exactly 6,8,10 lines
        boolean status = lastMove.size() == 6
                      || lastMove.size() == 8
                      || lastMove.size() == 10;
        
        if(status && lastMove.size() == 10)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString);
            
            // Lines 1,2,3,4,5,6,7,8,9
            status = status
                  && matchNormalHarborTradeResources(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastMove.get(5), 5, 
                          lastMove.get(6), 6, 
                          lastMove.get(7), 7, 
                          lastMove.get(8), 8, 
                          lastMove.get(9), 9, 
                          lastState,
                          playerString
                          );
        }
        else if(status && lastMove.size() == 8)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString);
            
            // Lines 1,2,3,4,5,6,7
            status = status
                  && matchThreeToOneHarborTradeResources(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastMove.get(5), 5, 
                          lastMove.get(6), 6, 
                          lastMove.get(7), 7, 
                          lastState,
                          playerString
                          );
        }
        else if(status && lastMove.size() == 6)
        {
            // Line 0
            status = status
                  && matchTurnMoveForSamePlayer(
                          lastMove.get(0), 0, 
                          playerString);
            
            // Lines 1,2,3,4,5,6,7,8,9
            status = status
                  && matchTwoToOneHarborTradeResources(
                          lastMove.get(1), 1, 
                          lastMove.get(2), 2, 
                          lastMove.get(3), 3, 
                          lastMove.get(4), 4, 
                          lastMove.get(5), 5, 
                          lastState,
                          playerString
                          );
        }
        else
        {
            err = "Incorrect Number Of Moves: "
                + lastMove.size() + "\n"
                + "HARBORTRADE expects: 6,8,10";
        }
        
        return status;
    }
    
    // Returns whether the list of moves for a turn move match
    private boolean matchTurnMoveForSamePlayer(
            Operation move, int moveNum,
            String playerString)
    {
        boolean status = false;

        if(!move.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "BUILDSETTLEMENT expects: SET(TURN, playerString)\n"
                + "Set move expected";
        }
        else if(!((Set)move).getKey().equals(Constants.TURN))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "BUILDSETTLEMENT expects: SET(TURN, playerString)\n"
                + "TURN key expected";
        }
        else if(!((Set)move).getValue().toString().contains(playerString))
        {
            err = "Incorrect Move Number: moveNum\n"
                + "BUILDSETTLEMENT expects: SET(TURN, playerString)\n"
                + "playerString value expected";
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
    private boolean matchCityResources(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Operation move5, int move5Num,
            Operation move6, int move6Num,
            Operation move7, int move7Num,
            Operation move8, int move8Num,
            Operation move9, int move9Num,
            Operation move10, int move10Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        int ore = 3;
        int grain = 2;
        
        String resourceCard1 = "";
        String resourceCard2 = "";
        String resourceCard3 = "";
        String resourceCard4 = "";
        String resourceCard5 = "";
        
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
                            resourceCard5 = ((SetVisibility)move5).getKey();
                            status = true;
                        }
                    }
                }
            }
        }
        
        // Starting a new tree to increase visibility
        // All 5 resource cards are different and correctly formed
        if(status)
        {
            status = false;
            
            if(!move6.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move6).getKey().equals(resourceCard1))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard1))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move7.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move7Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move7).getKey().equals(resourceCard2))
            {
                err = "Incorrect Move Number: " + move7Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard2))
            {
                err = "Incorrect Move Number: " + move7Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move8.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move8Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move8).getKey().equals(resourceCard3))
            {
                err = "Incorrect Move Number: " + move8Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard3))
            {
                err = "Incorrect Move Number: " + move8Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move9.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move9Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDDD + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move9).getKey().equals(resourceCard4))
            {
                err = "Incorrect Move Number: " + move9Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDDD + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard4))
            {
                err = "Incorrect Move Number: " + move9Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDDD + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move10.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move10Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDEE + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move10).getKey().equals(resourceCard5))
            {
                err = "Incorrect Move Number: " + move10Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDEE + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard5))
            {
                err = "Incorrect Move Number: " + move10Num + "\n"
                    + "BUILDCITY expects: DELETE(RESOURCECARDEE + playerString)\n"
                    + "playerString does not own resource";
            }
            else
            {
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
    
    // Returns whether the list of resources matches for a build settlement move
    private boolean matchSettlementResources(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Operation move5, int move5Num,
            Operation move6, int move6Num,
            Operation move7, int move7Num,
            Operation move8, int move8Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        int grain = 1;
        int lumber = 1;
        int wool = 1;
        int brick = 1;
        
        String resourceCard1 = "";
        String resourceCard2 = "";
        String resourceCard3 = "";
        String resourceCard4 = "";
        
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
                        resourceCard4 = ((SetVisibility)move4).getKey();
                        status = true;
                    }
                }
            }
        }
        
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
        if(status)
        {
            status = false;
            
            if(!move5.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move5).getKey().equals(resourceCard1))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard1))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move6.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move6).getKey().equals(resourceCard2))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard2))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move7.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move7Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move7).getKey().equals(resourceCard3))
            {
                err = "Incorrect Move Number: " + move7Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard3))
            {
                err = "Incorrect Move Number: " + move7Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move8.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move8Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDDD + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move8).getKey().equals(resourceCard4))
            {
                err = "Incorrect Move Number: " + move8Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDDD + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard4))
            {
                err = "Incorrect Move Number: " + move8Num + "\n"
                    + "BUILDSETTLEMENT expects: DELETE(RESOURCECARDDD + playerString)\n"
                    + "playerString does not own resource";
            }
            else
            {
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
    private boolean matchRoadResources(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        int lumber = 1;
        int brick = 1;
        
        String resourceCard1 = "";
        String resourceCard2 = "";
        
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
                resourceCard2 = ((SetVisibility)move2).getKey();
                status = true;
            }
        }
        
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
        if(status)
        {
            status = false;
            
            if(!move3.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move3Num + "\n"
                    + "BUILDROAD expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move3).getKey().equals(resourceCard1))
            {
                err = "Incorrect Move Number: " + move3Num + "\n"
                    + "BUILDROAD expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard1))
            {
                err = "Incorrect Move Number: " + move3Num + "\n"
                    + "BUILDROAD expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move4.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "BUILDROAD expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move4).getKey().equals(resourceCard2))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "BUILDROAD expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard2))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "BUILDROAD expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "playerString does not own resource";
            }
            else
            {
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
        }
        
        return status;
    }

    // Returns whether the list of moves for a development card and development card type match
    private boolean matchDevelopmentCardandDevelopmentCardTypePair(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString,
            int playerId)
    {
        String developmentCardXX = "";

        boolean status = false;

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SET(DEVELOPMENTCARDXX, playerString)\n"
                + "Set move expected";
        }
        else if(!((Set)move1).getKey().contains(Constants.DEVELOPMENTCARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SET(DEVELOPMENTCARDXX, playerString)\n"
                + "DEVELOPMENTCARD key expected";
        }
        else if(!((Set)move1).getValue().toString().equals(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SET(DEVELOPMENTCARDXX, playerString)\n"
                + "playerString value expected";
        }
        else if(!((Set)move1).getKey().equals(findFirstOpenDevelopmentCard(lastState)))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "BUYDEVELOPMENTCARD expects: SET(DEVELOPMENTCARDXX, playerString)\n"
                + "DEVELOPMENTCARD key is not the first available";
        }
        else
        {
            developmentCardXX = ((Set)move1).getKey();

            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDTYPEXX, visibleTo + playerString)\n"
                    + "Set move expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(Constants.DEVELOPMENTCARDTYPETOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDTYPEXX, visibleTo + playerString)\n"
                    + "DEVELOPMENTCARDTYPE key expected";
            }
            else if(((ImmutableList<Integer>)((SetVisibility)move2).getVisibleToPlayerIds()).size() != 1)
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDTYPEXX, visibleTo + playerString)\n"
                    + "VisibleTo contains more than one user";
            }
            else if(!((ImmutableList<Integer>)((SetVisibility)move2).getVisibleToPlayerIds()).contains(playerId))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDTYPEXX, visibleTo + playerString)\n"
                    + "VisibleTo does not map to current player";
            }
            else if(!developmentCardXX.equals(findDevelopmentCardFromCardType(((SetVisibility)move2).getKey())))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDTYPEXX, visibleTo + playerString)\n"
                    + "DEVELOPMENTCARD does not match DEVELOPMENTCARDTYPE";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }

    // Returns whether the list of moves for a development card and development card type match
    // for a specific card type
    private boolean matchDevelopmentCardandDevelopmentCardTypePlayedPair(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Map<String, Object> lastState,
            String playerString,
            String developmentCardType)
    {
        String developmentCardXX = "";

        boolean status = false;

        if(!move1.getMessageName().equals("Set"))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "PLAYDEVELOPMENTCARD expects: SET(DEVELOPMENTCARDXX, PLAYED)\n"
                + "Set move expected";
        }
        else if(!((Set)move1).getKey().contains(Constants.DEVELOPMENTCARDTOKEN))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "PLAYDEVELOPMENTCARD expects: SET(DEVELOPMENTCARDXX, PLAYED)\n"
                + "DEVELOPMENTCARD key expected";
        }
        else if(!((Set)move1).getValue().toString().equals(Constants.PLAYED))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "PLAYDEVELOPMENTCARD expects: SET(DEVELOPMENTCARDXX, PLAYED)\n"
                + "PLAYED value expected";
        }
        else if(!lastState.containsKey((((Set)move1).getKey())))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "PLAYDEVELOPMENTCARD expects: SET(DEVELOPMENTCARDXX, PLAYED)\n"
                + "DEVELOPMENTCARD is not owned";
        }
        else if(!lastState.get((((Set)move1).getKey())).toString().contains(playerString))
        {
            err = "Incorrect Move Number: " + move1Num + "\n"
                + "PLAYDEVELOPMENTCARD expects: SET(DEVELOPMENTCARDXX, PLAYED)\n"
                + "DEVELOPMENTCARD is not owned by player";
        }
        else
        {
            developmentCardXX = ((Set)move1).getKey();

            if(!move2.getMessageName().equals("SetVisibility"))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDTYPEXX)\n"
                    + "Set move expected";
            }
            else if(!((SetVisibility)move2).getKey().contains(Constants.DEVELOPMENTCARDTYPETOKEN))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDTYPEXX)\n"
                    + "DEVELOPMENTCARDTYPE key expected";
            }
            else if(!developmentCardXX.equals(findDevelopmentCardFromCardType(((SetVisibility)move2).getKey())))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDTYPEXX)\n"
                    + "DEVELOPMENTCARD does not match DEVELOPMENTCARDTYPE";
            }
            else if(!developmentCardType.equals(lastState.get(((SetVisibility)move2).getKey())))
            {
                err = "Incorrect Move Number: " + move2Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: SETVISIBILITY(DEVELOPMENTCARDTYPEXX)\n"
                    + "DEVELOPMENTCARDTYPE does not match input developmentCardType";
            }
            else
            {
                status = true;
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a buy development card move
    private boolean matchBuyDevelopmentCardResources(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Operation move5, int move5Num,
            Operation move6, int move6Num,
            Map<String, Object> lastState,
            String playerString)
    {
        boolean status = false;
        
        int ore = 1;
        int grain = 1;
        int wool = 1;
        
        String resourceCard1 = "";
        String resourceCard2 = "";
        String resourceCard3 = "";
        
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
                    resourceCard3 = ((SetVisibility)move3).getKey();
                    status = true;
                }
            }
        }
        
        // Starting a new tree to increase visibility
        // All 3 resource cards are different and correctly formed
        if(status)
        {
            status = false;
            
            if(!move4.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move4).getKey().equals(resourceCard1))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard1))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move5.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move5).getKey().equals(resourceCard2))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard2))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move6.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move6).getKey().equals(resourceCard3))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard3))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "BUYDEVELOPMENTCARD expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "playerString does not own resource";
            }
            else
            {
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
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a normal harbor trade
    private boolean matchNormalHarborTradeResources(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Operation move5, int move5Num,
            Operation move6, int move6Num,
            Operation move7, int move7Num,
            Operation move8, int move8Num,
            Operation move9, int move9Num,
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
                        resourceCard4 = ((SetVisibility)move4).getKey();
                        status = true;
                    }
                }
            }
        }
        
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
        if(status)
        {
            status = false;
            
            if(!move5.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move5).getKey().equals(resourceCard1))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard1))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move6.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move6).getKey().equals(resourceCard2))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard2))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move7.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move7Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move7).getKey().equals(resourceCard3))
            {
                err = "Incorrect Move Number: " + move7Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard3))
            {
                err = "Incorrect Move Number: " + move7Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move8.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move8Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDDD + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move8).getKey().equals(resourceCard4))
            {
                err = "Incorrect Move Number: " + move8Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDDD + playerString)\n"
                    + "RESOURCECARD does not match 4 lines ago";
            }
            else if(!lastState.containsKey(resourceCard4))
            {
                err = "Incorrect Move Number: " + move8Num + "\n"
                    + "NORMALHARBORTRADE expects: DELETE(RESOURCECARDDD + playerString)\n"
                    + "playerString does not own resource";
            }
            else
            {
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
                    if(!move9.getMessageName().equals("Set"))
                    {
                        err = "Incorrect Move Number: " + move9Num + "\n"
                            + "NORMALHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "SetVisibility move expected";
                    }
                    else if(!((Set)move9).getKey().contains(Constants.RESOURCECARDTOKEN))
                    {
                        err = "Incorrect Move Number: " + move9Num + "\n"
                            + "NORMALHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "RESOURCECARD key expected";
                    }
                    else if(!((Set)move9).getKey().contains(playerString))
                    {
                        err = "Incorrect Move Number: " + move9Num + "\n"
                            + "NORMALHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "playerString key expected";
                    }
                    else if(!( ((Set)move9).getValue().toString().contains(Constants.ORE)
                             ||((Set)move9).getValue().toString().contains(Constants.GRAIN)
                             ||((Set)move9).getValue().toString().contains(Constants.LUMBER)
                             ||((Set)move9).getValue().toString().contains(Constants.WOOL)
                             ||((Set)move9).getValue().toString().contains(Constants.BRICK) ) )
                    {
                        err = "Incorrect Move Number: " + move9Num + "\n"
                            + "NORMALHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "A type of resource value expected";
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
    
    // Returns whether the list of resources matches for a 3 to 1 harbor trade
    private boolean matchThreeToOneHarborTradeResources(
            Operation move1, int move1Num,
            Operation move2, int move2Num,
            Operation move3, int move3Num,
            Operation move4, int move4Num,
            Operation move5, int move5Num,
            Operation move6, int move6Num,
            Operation move7, int move7Num,
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
                    resourceCard3 = ((SetVisibility)move3).getKey();
                    status = true;
                }
            }
        }
        
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
        if(status)
        {
            status = false;
            
            if(!move4.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move4).getKey().equals(resourceCard1))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "RESOURCECARD does not match 3 lines ago";
            }
            else if(!lastState.containsKey(resourceCard1))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move5.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move5).getKey().equals(resourceCard2))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD does not match 3 lines ago";
            }
            else if(!lastState.containsKey(resourceCard2))
            {
                err = "Incorrect Move Number: " + move5Num + "\n"
                    + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move6.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move6).getKey().equals(resourceCard3))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "RESOURCECARD does not match 3 lines ago";
            }
            else if(!lastState.containsKey(resourceCard3))
            {
                err = "Incorrect Move Number: " + move6Num + "\n"
                    + "THREETOONEHARBORTRADE expects: DELETE(RESOURCECARDCC + playerString)\n"
                    + "playerString does not own resource";
            }
            else
            {
                String resource = lastState.get(resourceCard1).toString();
                
                if ( !( resource.equals(lastState.get(resourceCard2).toString())
                     && resource.equals(lastState.get(resourceCard3).toString()) ) )
                {
                    err = "Incorrect THREETOONEHARBORTRADE Resources\n"
                        + "Resources are not all the same";
                }
                else
                {
                    if(!move7.getMessageName().equals("Set"))
                    {
                        err = "Incorrect Move Number: " + move7Num + "\n"
                            + "THREETOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "SetVisibility move expected";
                    }
                    else if(!((Set)move7).getKey().contains(Constants.RESOURCECARDTOKEN))
                    {
                        err = "Incorrect Move Number: " + move7Num + "\n"
                            + "THREETOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "RESOURCECARD key expected";
                    }
                    else if(!((Set)move7).getKey().contains(playerString))
                    {
                        err = "Incorrect Move Number: " + move7Num + "\n"
                            + "THREETOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "playerString key expected";
                    }
                    else if(!( ((Set)move7).getValue().toString().contains(Constants.ORE)
                             ||((Set)move7).getValue().toString().contains(Constants.GRAIN)
                             ||((Set)move7).getValue().toString().contains(Constants.LUMBER)
                             ||((Set)move7).getValue().toString().contains(Constants.WOOL)
                             ||((Set)move7).getValue().toString().contains(Constants.BRICK) ) )
                    {
                        err = "Incorrect Move Number: " + move7Num + "\n"
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
                        status = true;
                    }
                }
            }
        }
        
        return status;
    }
    
    // Returns whether the list of resources matches for a 2 to 1 harbor trade
    private boolean matchTwoToOneHarborTradeResources(
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
                resourceCard2 = ((SetVisibility)move2).getKey();
                status = true;
            }
        }
        
        // Starting a new tree to increase visibility
        // All 4 resource cards are different and correctly formed
        if(status)
        {
            status = false;
            
            if(!move3.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move3Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move3).getKey().equals(resourceCard1))
            {
                err = "Incorrect Move Number: " + move3Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "RESOURCECARD does not match 2 lines ago";
            }
            else if(!lastState.containsKey(resourceCard1))
            {
                err = "Incorrect Move Number: " + move3Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: DELETE(RESOURCECARDAA + playerString)\n"
                    + "playerString does not own resource";
            }
            else if(!move4.getMessageName().equals("Delete"))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "Delete move expected";
            }
            else if(!((Delete)move4).getKey().equals(resourceCard2))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "RESOURCECARD does not match 2 lines ago";
            }
            else if(!lastState.containsKey(resourceCard2))
            {
                err = "Incorrect Move Number: " + move4Num + "\n"
                    + "TWOTOONEHARBORTRADE expects: DELETE(RESOURCECARDBB + playerString)\n"
                    + "playerString does not own resource";
            }
            else
            {
                String resource = lastState.get(resourceCard1).toString();
                
                if ( !resource.equals(lastState.get(resourceCard2).toString()) )
                {
                    err = "Incorrect TWOTOONEHARBORTRADE Resources\n"
                        + "Resources are not all the same";
                }
                else
                {
                    if(!move5.getMessageName().equals("Set"))
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
                            + "TWOTOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "SetVisibility move expected";
                    }
                    else if(!((Set)move5).getKey().contains(Constants.RESOURCECARDTOKEN))
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
                            + "TWOTOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "RESOURCECARD key expected";
                    }
                    else if(!((Set)move5).getKey().contains(playerString))
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
                            + "TWOTOONEHARBORTRADE expects: SET(RESOURCECARDAA+ playerString, resource)\n"
                            + "playerString key expected";
                    }
                    else if(!( ((Set)move5).getValue().toString().contains(Constants.ORE)
                             ||((Set)move5).getValue().toString().contains(Constants.GRAIN)
                             ||((Set)move5).getValue().toString().contains(Constants.LUMBER)
                             ||((Set)move5).getValue().toString().contains(Constants.WOOL)
                             ||((Set)move5).getValue().toString().contains(Constants.BRICK) ) )
                    {
                        err = "Incorrect Move Number: " + move5Num + "\n"
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
                        status = true;
                    }
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
    
    // Returns whether the list of moves matches for an End Game
    private boolean matchEndGame(
            Operation move1, int move1Num,
            Map<String, Object> lastState,
            String playerString,
            int playerId,
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
            
            for (int i = 0; i < 10; i++)
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
         && lastState.get(Constants.LONGESTROAD).toString().contains(playerString) );
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
         && lastState.get(Constants.LARGESTARMY).toString().contains(playerString) );
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
        
        if( lastState.containsKey(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF04))
         && lastState.get(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF04)).toString().contains(playerString) )
            count++;
        if( lastState.containsKey(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF05))
         && lastState.get(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF04)).toString().contains(playerString) )
            count++;
        if( lastState.containsKey(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF06))
         && lastState.get(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF04)).toString().contains(playerString) )
            count++;
        if( lastState.containsKey(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF07))
         && lastState.get(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF04)).toString().contains(playerString) )
            count++;
        if( lastState.containsKey(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF08))
         && lastState.get(findDevelopmentCardFromCardType(Constants.DEVELOPMENTCARDTYPEDEF04)).toString().contains(playerString) )
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
	private boolean canAddSettlementHere(
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
    
	// Returns whether a specific player can add a road at a certain path
    // Gets whether a road can be added here for a specific player
    private boolean canAddRoadHere(
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
    private String findDevelopmentCardFromCardType(
            String developmentCardType)
    {
        String developmentCard = "";
        
        switch(developmentCardType)
        {
        case Constants.DEVELOPMENTCARDTYPE00:
            developmentCard = Constants.DEVELOPMENTCARD00;
            break;
        case Constants.DEVELOPMENTCARDTYPE01:
            developmentCard = Constants.DEVELOPMENTCARD01;
            break;
        case Constants.DEVELOPMENTCARDTYPE02:
            developmentCard = Constants.DEVELOPMENTCARD02;
            break;
        case Constants.DEVELOPMENTCARDTYPE03:
            developmentCard = Constants.DEVELOPMENTCARD03;
            break;
        case Constants.DEVELOPMENTCARDTYPE04:
            developmentCard = Constants.DEVELOPMENTCARD04;
            break;
        case Constants.DEVELOPMENTCARDTYPE05:
            developmentCard = Constants.DEVELOPMENTCARD05;
            break;
        case Constants.DEVELOPMENTCARDTYPE06:
            developmentCard = Constants.DEVELOPMENTCARD06;
            break;
        case Constants.DEVELOPMENTCARDTYPE07:
            developmentCard = Constants.DEVELOPMENTCARD07;
            break;
        case Constants.DEVELOPMENTCARDTYPE08:
            developmentCard = Constants.DEVELOPMENTCARD08;
            break;
        case Constants.DEVELOPMENTCARDTYPE09:
            developmentCard = Constants.DEVELOPMENTCARD09;
            break;
        case Constants.DEVELOPMENTCARDTYPE10:
            developmentCard = Constants.DEVELOPMENTCARD10;
            break;
        case Constants.DEVELOPMENTCARDTYPE11:
            developmentCard = Constants.DEVELOPMENTCARD11;
            break;
        case Constants.DEVELOPMENTCARDTYPE12:
            developmentCard = Constants.DEVELOPMENTCARD12;
            break;
        case Constants.DEVELOPMENTCARDTYPE13:
            developmentCard = Constants.DEVELOPMENTCARD13;
            break;
        case Constants.DEVELOPMENTCARDTYPE14:
            developmentCard = Constants.DEVELOPMENTCARD14;
            break;
        case Constants.DEVELOPMENTCARDTYPE15:
            developmentCard = Constants.DEVELOPMENTCARD15;
            break;
        case Constants.DEVELOPMENTCARDTYPE16:
            developmentCard = Constants.DEVELOPMENTCARD16;
            break;
        case Constants.DEVELOPMENTCARDTYPE17:
            developmentCard = Constants.DEVELOPMENTCARD17;
            break;
        case Constants.DEVELOPMENTCARDTYPE18:
            developmentCard = Constants.DEVELOPMENTCARD18;
            break;
        case Constants.DEVELOPMENTCARDTYPE19:
            developmentCard = Constants.DEVELOPMENTCARD19;
            break;
        case Constants.DEVELOPMENTCARDTYPE20:
            developmentCard = Constants.DEVELOPMENTCARD20;
            break;
        case Constants.DEVELOPMENTCARDTYPE21:
            developmentCard = Constants.DEVELOPMENTCARD21;
            break;
        case Constants.DEVELOPMENTCARDTYPE22:
            developmentCard = Constants.DEVELOPMENTCARD22;
            break;
        case Constants.DEVELOPMENTCARDTYPE23:
            developmentCard = Constants.DEVELOPMENTCARD23;
            break;
        case Constants.DEVELOPMENTCARDTYPE24:
            developmentCard = Constants.DEVELOPMENTCARD24;
            break;
        }
        
        return developmentCard;
    }

    // Finds the first open development card on the stack

    // Returns the first available Development Card
    private String findFirstOpenDevelopmentCard(
            Map<String, Object> lastState)
    {
        String firstOpen = "";
        
        if(!lastState.containsKey(Constants.DEVELOPMENTCARD00))
        {
            firstOpen = Constants.DEVELOPMENTCARD00;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD01))
        {
            firstOpen = Constants.DEVELOPMENTCARD01;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD02))
        {
            firstOpen = Constants.DEVELOPMENTCARD02;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD03))
        {
            firstOpen = Constants.DEVELOPMENTCARD03;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD04))
        {
            firstOpen = Constants.DEVELOPMENTCARD04;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD05))
        {
            firstOpen = Constants.DEVELOPMENTCARD05;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD06))
        {
            firstOpen = Constants.DEVELOPMENTCARD06;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD07))
        {
            firstOpen = Constants.DEVELOPMENTCARD07;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD08))
        {
            firstOpen = Constants.DEVELOPMENTCARD08;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD09))
        {
            firstOpen = Constants.DEVELOPMENTCARD09;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD10))
        {
            firstOpen = Constants.DEVELOPMENTCARD10;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD11))
        {
            firstOpen = Constants.DEVELOPMENTCARD11;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD12))
        {
            firstOpen = Constants.DEVELOPMENTCARD12;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD13))
        {
            firstOpen = Constants.DEVELOPMENTCARD13;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD14))
        {
            firstOpen = Constants.DEVELOPMENTCARD14;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD15))
        {
            firstOpen = Constants.DEVELOPMENTCARD15;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD16))
        {
            firstOpen = Constants.DEVELOPMENTCARD16;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD17))
        {
            firstOpen = Constants.DEVELOPMENTCARD17;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD18))
        {
            firstOpen = Constants.DEVELOPMENTCARD18;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD19))
        {
            firstOpen = Constants.DEVELOPMENTCARD19;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD20))
        {
            firstOpen = Constants.DEVELOPMENTCARD20;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD21))
        {
            firstOpen = Constants.DEVELOPMENTCARD21;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD22))
        {
            firstOpen = Constants.DEVELOPMENTCARD22;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD23))
        {
            firstOpen = Constants.DEVELOPMENTCARD23;
        }
        else if(!lastState.containsKey(Constants.DEVELOPMENTCARD24))
        {
            firstOpen = Constants.DEVELOPMENTCARD24;
        }
        
        return firstOpen;
    }

    // Determines what kind of move the player is attempting to make

    // Returns the move that is preliminarily expected based on the list of moves
    private String findExpectedMove(
            List<Operation> lastMove,
            String playerString,
            String nextPlayerString)
    {
        String expectedMove = "";
        
        // Move contains a SET Turn command with the next player string
        // This is a CHANGETURN move
        if ( findASetMoveInMoves(lastMove, Constants.TURN, nextPlayerString) )
        {
            expectedMove = Constants.CHANGETURN;
        }
        // Move contains a SET City command
        // This is a BUILDCITY move
        else if ( findASetMoveInMoves(lastMove, Constants.CITYTOKEN, "") )
        {
            expectedMove = Constants.BUILDCITY;
        }
        // Move contains a SET Settlement command
        // This is a BUILDSETTLEMENT move
        else if ( findASetMoveInMoves(lastMove, Constants.SETTLEMENTTOKEN, "") )
        {
            expectedMove = Constants.BUILDSETTLEMENT;
        }
        // Move contains a SET Road command
        // This is a BUILDROAD move
        else if ( findASetMoveInMoves(lastMove, Constants.ROADTOKEN, "") )
        {
            expectedMove = Constants.BUILDROAD;
        }
        // Move contains a SET Development Card command with the current playerString
        // This is a BUYDEVELOPMENTCARD move
        else if ( findASetMoveInMoves(lastMove, Constants.DEVELOPMENTCARDTOKEN, playerString) )
        {
            expectedMove = Constants.BUYDEVELOPMENTCARD;
        }
        // Move contains a SET Development Card command with the PLAYED tag
        // This is a PLAYDEVELOPMENTCARD move
        else if ( findASetMoveInMoves(lastMove, Constants.DEVELOPMENTCARDTOKEN, Constants.PLAYED) )
        {
            expectedMove = Constants.PLAYDEVELOPMENTCARD;
        }
        // Move contains only a turn move and resources
        // This is a HARBORTRADE move
        else if ( findASetMoveInMoves(lastMove, Constants.TURN, playerString)
               && !findASetMoveInMoves(lastMove, Constants.CITYTOKEN, "")
               && !findASetMoveInMoves(lastMove, Constants.SETTLEMENTTOKEN, "")
               && !findASetMoveInMoves(lastMove, Constants.ROADTOKEN, "")
               && !findASetMoveInMoves(lastMove, Constants.DEVELOPMENTCARDTOKEN, ""))
        {
            expectedMove = Constants.HARBORTRADE;
        }
        else
        {
            expectedMove = "UNKNOWN";
        }
        
        return expectedMove;
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
    
    // Returns the playerString for the current player
    // Gets the String Name of the player based on ID
    private String getPlayerId(
            List<Integer> playerIds,
            int lastMovePlayerId) {
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
            List<Integer> playerIds,
            int lastMovePlayerId) {
        String playerString = "";
        
        if(playerIds.get(0) == lastMovePlayerId)
        {
            playerString = Constants.PR;
        }
        else if(playerIds.get(1) == lastMovePlayerId)
        {
            playerString = Constants.PY;
        }
        else if(playerIds.get(2) == lastMovePlayerId)
        {
            playerString = Constants.PG;
        }
        else if(playerIds.get(3) == lastMovePlayerId)
        {
            playerString = Constants.PB;
        }
        
        return playerString;
    }

    // Checks if String is an integer
    // From http://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java
    public static boolean isInteger(String s)
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
}