package settlersofcatan.client;


import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import settlersofcatan.client.GameApi.Delete;
import settlersofcatan.client.GameApi.Operation;
import settlersofcatan.client.GameApi.Set;
import settlersofcatan.client.GameApi.SetVisibility;
import settlersofcatan.client.GameApi.VerifyMove;
import settlersofcatan.client.GameApi.VerifyMoveDone;



public class SettlersOfCatanLogic {  
    
    private Vector<String> attemptedDevelopmentCardPurchaseList = new Vector<String>();
    
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
	    
	    boolean status = moveIsLegal(lastMove, lastState, playerString);
	    
	    if(!status)
	    {
	        throw new RuntimeException("We have a hacker!\n" + err);
	    }
	}
	
	private String getPlayerId(List<Integer> playerIds, int lastMovePlayerId) {
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

    private boolean moveIsLegal(List<Operation> lastMove, Map<String, Object> lastState, String playerString)
	{
	    boolean status = true;
        int ore = 0;
        int grain = 0;
	    int lumber = 0;
	    int wool = 0;
        int brick = 0;
        
        attemptedDevelopmentCardPurchaseList.clear();
	    
	    for(int i = 0; i < lastMove.size() && status; i++)
	    {
	        // Only checking set/delete at the moment
	        status = status && individualMoveIsLegal(lastMove.get(i), lastState, playerString);
	        
	        if(lastMove.get(i).getClassName().equals("Delete"))
	        {
	            String key = ((Delete)lastMove.get(i)).getKey();
	            String val = (String) lastState.get(key);
	            
	            if(key.contains(playerString))
	            {
	                switch(val)
	                {
	                    case Constants.ORE:
	                        ore++;
	                        break;
                        case Constants.GRAIN:
                            grain++;
                            break;
                        case Constants.LUMBER:
                            lumber++;
                            break;
                        case Constants.WOOL:
                            wool++;
                            break;
                        case Constants.BRICK:
                            brick++;
                            break;
	                }
	            }
	        }
	    }
	    
	    if(status)
	    {
    	    // ensure card requests are paired correctly
            if(status && !attemptedDevelopmentCardPurchaseList.isEmpty())
            {
                status = false;
                err = "Development Card not paired with Development Card Type";
            }
    	    // Settlement
            else if(status && ore == 0 && grain == 1 && lumber == 1 && wool == 1 && brick == 1)
    	    {
                status = ensureSettlementMoveCountCorrect(lastMove, playerString);
    	    }
    	    // Road
    	    else if(status && ore == 0 && grain == 0 && lumber == 1 && wool == 0 && brick == 1)
            {
                status = ensureRoadMoveCountCorrect(lastMove, playerString);
            }
    	    // City
            else if(status && ore == 3 && grain == 2 && lumber == 0 && wool == 0 && brick == 0)
            {
                status = ensureCityMoveCountCorrect(lastMove, lastState, playerString);
            }
    	    // Development Card
            else if(status && ore == 1 && grain == 1 && lumber == 0 && wool == 1 && brick == 0)
            {
                status = ensureBuyDevelopmentCardMoveCountCorrect(lastMove, playerString);
            }
    	    // Normal Harbor Trade
            else if( status
                   &&( (ore == 4 && grain == 0 && lumber == 0 && wool == 0 && brick == 0)
                     ||(ore == 0 && grain == 4 && lumber == 0 && wool == 0 && brick == 0)
                     ||(ore == 0 && grain == 0 && lumber == 4 && wool == 0 && brick == 0)
                     ||(ore == 0 && grain == 0 && lumber == 0 && wool == 4 && brick == 0)
                     ||(ore == 0 && grain == 0 && lumber == 0 && wool == 0 && brick == 4)))
            {
                status = ensureTotalTradeMoveCountCorrect(lastMove, playerString, 4);
            }
    	    // 3 for 1 Harbor Trade
            else if( status && ownsThreeToOneHarbor(lastState, playerString)
                   && ( (ore == 3 && grain == 0 && lumber == 0 && wool == 0 && brick == 0)
                      ||(ore == 0 && grain == 3 && lumber == 0 && wool == 0 && brick == 0)
                      ||(ore == 0 && grain == 0 && lumber == 3 && wool == 0 && brick == 0)
                      ||(ore == 0 && grain == 0 && lumber == 0 && wool == 3 && brick == 0)
                      ||(ore == 0 && grain == 0 && lumber == 0 && wool == 0 && brick == 3)))
            {
                status = ensureTotalTradeMoveCountCorrect(lastMove, playerString, 3);
            }
            // 2 for 1 Ore Trade
            else if( status && ownsTwoToOneHarbor(lastState, playerString, Constants.ORE)
                     && ore == 2 && grain == 0 && lumber == 0 && wool == 0 && brick == 0)
            {
                status = ensureTotalTradeMoveCountCorrect(lastMove, playerString, 2);
            }
            // 2 for 1 Grain Trade
            else if( status && ownsTwoToOneHarbor(lastState, playerString, Constants.GRAIN)
                     && ore == 0 && grain == 2 && lumber == 0 && wool == 0 && brick == 0)
            {
                status = ensureTotalTradeMoveCountCorrect(lastMove, playerString, 2);
            }
            // 2 for 1 Lumber Trade
            else if( status && ownsTwoToOneHarbor(lastState, playerString, Constants.LUMBER)
                     && ore == 0 && grain == 0 && lumber == 2 && wool == 0 && brick == 0)
            {
                status = ensureTotalTradeMoveCountCorrect(lastMove, playerString, 2);
            }
            // 2 for 1 Wool Trade
            else if( status && ownsTwoToOneHarbor(lastState, playerString, Constants.WOOL)
                     && ore == 0 && grain == 0 && lumber == 0 && wool ==2 && brick == 0)
            {
                status = ensureTotalTradeMoveCountCorrect(lastMove, playerString, 2);
            }
            // 2 for 1 Brick Trade
            else if( status && ownsTwoToOneHarbor(lastState, playerString, Constants.BRICK)
                     && ore == 0 && grain == 0 && lumber == 0 && wool == 0 && brick == 2)
            {
                status = ensureTotalTradeMoveCountCorrect(lastMove, playerString, 2);
            }
    	    // No correct resources for any move
            else
            {
                status = false;
                err = "Incorrect Resources";
            }
	    }
	    
	    return status;
	}
    
    private boolean individualMoveIsLegal(Operation move, Map<String, Object> lastState, String playerString)
    {
        boolean status = false;
        
        switch(move.getClassName())
        {
        case "Set":
            if(((Set)move).getKey().equals(Constants.TURN))
            {
                status = ((String)lastState.get(((Set)move).getKey())).equals(playerString);
            }
            else if(((Set)move).getKey().contains("DEV"))
            {
                if( (((Set)move).getKey()).equals(findFirstOpenDevelopmentCard(lastState))
                    && ( (((Set)move).getValue()).equals(Constants.PLAYED)
                       ||((String)((Set)move).getValue()).contains(playerString)) )
                {
                    status = attemptedDevelopmentCardPurchaseList.add(((Set)move).getKey());
                }
                
            }
            else if(((Set)move).getKey().contains("RC"))
            {
                status = true;
            }
            else
            {
                if(((Set)move).getKey().contains("CIT") && ((Set)move).getKey().contains(playerString))
                {
                    status = (lastState.containsKey(((Set)move).getValue()))
                           &&((String)lastState.get(((Set)move).getValue())).contains("SET")
                           &&((String)lastState.get(((Set)move).getValue())).contains(playerString)
                           &&!lastState.containsKey(((Set)move).getKey());
                }
                else if(((String)((Set)move).getValue()).contains("CIT") && ((Set)move).getValue().toString().contains(playerString))
                {
                    status = (lastState.containsKey(((Set)move).getKey()))
                           &&((String)lastState.get(((Set)move).getKey())).contains("SET")
                           &&((String)lastState.get(((Set)move).getKey())).contains(playerString)
                           &&!lastState.containsKey(((Set)move).getValue());
                }
                else if(((Set)move).getKey().contains("SET") && ((Set)move).getKey().contains(playerString))
                {
                    status = (!lastState.containsKey(((Set)move).getValue()))
                            && canAddNodeHere(lastState, ((Set)move).getValue().toString(), playerString)
                            &&!lastState.containsKey(((Set)move).getKey());
                }
                else if(((String)((Set)move).getValue()).contains("SET") && ((Set)move).getValue().toString().contains(playerString))
                {
                    status = (!lastState.containsKey(((Set)move).getKey()))
                            && canAddNodeHere(lastState, ((Set)move).getKey(), playerString)
                            &&!lastState.containsKey(((Set)move).getValue());
                }
                else if(((Set)move).getKey().contains("RD") && ((Set)move).getKey().contains(playerString))
                {
                    status = (!lastState.containsKey(((Set)move).getValue()))
                            && canAddRoadHere(lastState, ((Set)move).getValue().toString(), playerString)
                            &&!lastState.containsKey(((Set)move).getKey());
                }
                else if(((String)((Set)move).getValue()).contains("RD") && ((Set)move).getValue().toString().contains(playerString))
                {
                    status = (!lastState.containsKey(((Set)move).getKey()))
                            && canAddRoadHere(lastState, ((Set)move).getKey(), playerString)
                            &&!lastState.containsKey(((Set)move).getValue());
                }
            }
            
            if(!status)
            {
                err = "Individual move is not legal\n"
                    + "Set: " + ((Set)move).getKey() + ", " + ((Set)move).getValue().toString();
            }
            break;
        case "Delete":
            status = !((String)lastState.get(((Delete)move).getKey())).isEmpty();
            
            if(!status)
            {
                err = "Individual move is not legal\n"
                    + "Delete: " + ((Delete)move).getKey();
            }
            break;
        case "SetVisibility":
            if(((SetVisibility)move).getKey().contains("DCT"))
            {
                if(attemptedDevelopmentCardPurchaseList.contains(findDevelopmentCardFromCardType(((SetVisibility)move).getKey())))
                {
                    status = attemptedDevelopmentCardPurchaseList.remove(findDevelopmentCardFromCardType(((SetVisibility)move).getKey()));
                }
                
            }
            else if(((SetVisibility)move).getKey().contains(playerString))
            {
                status = true;
            }

            if(!status)
            {
                err = "Individual move is not legal\n"
                    + "SetVisibility: " + ((SetVisibility)move).getKey();
            }
            break;
        default:
            status = true;
            break;
        }
        
        return status;
    }
    
    private String findDevelopmentCardFromCardType(String developmentCardType)
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

    private String findFirstOpenDevelopmentCard(Map<String, Object> lastState)
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
    

    private boolean canAddNodeHere(Map<String, Object> lastState, String node, String playerString)
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
    
    private boolean canAddRoadHere(Map<String, Object> lastState, String path, String playerString)
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
    
    private boolean ensureSettlementMoveCountCorrect(List<Operation> lastMove, String playerString)
    {
        boolean status = false;
        
        int turnVerify = 1;
        int oldCardVisibleAndDelete = 4;
        int nodeAdd = 1;
        int unknown = 0;
        Vector<String> setVisibleList = new Vector<String>();
        String nodeAddName = "";
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            switch(lastMove.get(i).getClassName())
            {
                case "Set":
                    if( ((Set) lastMove.get(i)).getKey().equals(Constants.TURN)
                      &&((Set) lastMove.get(i)).getValue().equals(playerString))
                    {
                        turnVerify--;
                    }
                    else if( ((Set) lastMove.get(i)).getValue().toString().contains("SET")
                           &&((Set) lastMove.get(i)).getValue().toString().contains(playerString))
                    {
                        nodeAddName = ((Set) lastMove.get(i)).getKey();
                    }
                    else if( ((Set) lastMove.get(i)).getKey().contains("SET")
                            &&((Set) lastMove.get(i)).getKey().contains(playerString))
                     {
                         if(((Set) lastMove.get(i)).getValue().toString().equals(nodeAddName))
                         {
                             nodeAddName = "";
                             nodeAdd--;
                         }
                         else
                         {
                             unknown--;
                         }
                     }
                    else
                    {
                        unknown--;
                    }
                    break;
                case "SetVisibility":
                    setVisibleList.add(((SetVisibility) lastMove.get(i)).getKey());
                    break;
                case "Delete":
                    if(setVisibleList.contains(((Delete)lastMove.get(i)).getKey()))
                    {
                        setVisibleList.remove(((Delete)lastMove.get(i)).getKey());
                        oldCardVisibleAndDelete--;
                    }
                    else
                    {
                        unknown--;
                    }
                    break;
                default:
                    unknown = -1;
                    break;
            }
        }
        
        if( setVisibleList.isEmpty() && nodeAddName.equals("") && turnVerify == 0
                && nodeAdd == 0 && oldCardVisibleAndDelete == 0 && unknown == 0)
        {
            status = true;
        }
        else
        {
            err = "ensureBuyDevelopmentCardMoveCountCorrect failed"
                + "setVisibleList.isEmpty(): " + setVisibleList.isEmpty() + "\n"
                + "nodeAddName.equals(\"\"): " + nodeAddName.equals("") + "\n"
                + "turnVerify: " + turnVerify + "\n"
                + "nodeAdd: " + nodeAdd + "\n"
                + "oldCardVisibleAndDelete: " + oldCardVisibleAndDelete + "\n"
                + "unknown: " + unknown;
        }
        
        return status;
    }
    
    private boolean ensureRoadMoveCountCorrect(List<Operation> lastMove, String playerString)
    {
        boolean status = false;
        
        int turnVerify = 1;
        int oldCardVisibleAndDelete = 2;
        int pathAdd = 1;
        int unknown = 0;
        Vector<String> setVisibleList = new Vector<String>();
        String pathAddName = "";
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            switch(lastMove.get(i).getClassName())
            {
                case "Set":
                    if( ((Set) lastMove.get(i)).getKey().equals(Constants.TURN)
                      &&((Set) lastMove.get(i)).getValue().equals(playerString))
                    {
                        turnVerify--;
                    }
                    else if( ((Set) lastMove.get(i)).getValue().toString().contains("RD")
                           &&((Set) lastMove.get(i)).getValue().toString().contains(playerString))
                    {
                        pathAddName = ((Set) lastMove.get(i)).getKey();
                    }
                    else if( ((Set) lastMove.get(i)).getKey().contains("RD")
                            &&((Set) lastMove.get(i)).getKey().contains(playerString))
                     {
                         if(((Set) lastMove.get(i)).getValue().toString().equals(pathAddName))
                         {
                             pathAddName = "";
                             pathAdd--;
                         }
                         else
                         {
                             unknown--;
                         }
                     }
                    else
                    {
                        unknown--;
                    }
                    break;
                case "SetVisibility":
                    setVisibleList.add(((SetVisibility) lastMove.get(i)).getKey());
                    break;
                case "Delete":
                    if(setVisibleList.contains(((Delete)lastMove.get(i)).getKey()))
                    {
                        setVisibleList.remove(((Delete)lastMove.get(i)).getKey());
                        oldCardVisibleAndDelete--;
                    }
                    else
                    {
                        unknown--;
                    }
                    break;
                default:
                    unknown = -1;
                    break;
            }
        }
        
        if( setVisibleList.isEmpty() && pathAddName.equals("") && turnVerify == 0
                && pathAdd == 0 && oldCardVisibleAndDelete == 0 && unknown == 0)
        {
            status = true;
        }
        else
        {
            err = "ensureBuyDevelopmentCardMoveCountCorrect failed"
                + "setVisibleList.isEmpty(): " + setVisibleList.isEmpty() + "\n"
                + "pathAddName.equals(\"\"): " + pathAddName.equals("") + "\n"
                + "turnVerify: " + turnVerify + "\n"
                + "pathAdd: " + pathAdd + "\n"
                + "oldCardVisibleAndDelete: " + oldCardVisibleAndDelete + "\n"
                + "unknown: " + unknown;
        }
        
        return status;
    }

    private boolean ensureCityMoveCountCorrect(List<Operation> lastMove, Map<String, Object> lastState, String playerString)
    {
        boolean status = false;
        
        int turnVerify = 1;
        int oldCardVisibleAndDelete = 5;
        int cityAdd = 1;
        int settlementRemove = 1;
        int unknown = 0;
        Vector<String> setVisibleList = new Vector<String>();
        String cityAddName = "";
        String settlementRemoveName = "";
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            switch(lastMove.get(i).getClassName())
            {
                case "Set":
                    if( ((Set) lastMove.get(i)).getKey().equals(Constants.TURN)
                      &&((Set) lastMove.get(i)).getValue().equals(playerString))
                    {
                        turnVerify--;
                    }
                    else if( ((Set) lastMove.get(i)).getValue().toString().contains("CIT")
                           &&((Set) lastMove.get(i)).getValue().toString().contains(playerString))
                    {
                        cityAddName = ((Set) lastMove.get(i)).getKey();
                        settlementRemoveName = lastState.get(cityAddName).toString();
                    }
                    else if( ((Set) lastMove.get(i)).getKey().contains("CIT")
                            &&((Set) lastMove.get(i)).getKey().contains(playerString))
                     {
                         if(((Set) lastMove.get(i)).getValue().toString().equals(cityAddName))
                         {
                             cityAddName = "";
                             cityAdd--;
                         }
                         else
                         {
                             unknown--;
                         }
                     }
                    else
                    {
                        unknown--;
                    }
                    break;
                case "SetVisibility":
                    setVisibleList.add(((SetVisibility) lastMove.get(i)).getKey());
                    break;
                case "Delete":
                    if(setVisibleList.contains(((Delete)lastMove.get(i)).getKey()))
                    {
                        setVisibleList.remove(((Delete)lastMove.get(i)).getKey());
                        oldCardVisibleAndDelete--;
                    }
                    else if((((Delete)lastMove.get(i)).getKey()).equals(settlementRemoveName))
                    {
                        settlementRemoveName = "";
                        settlementRemove--;
                    }
                    else
                    {
                        unknown--;
                    }
                    break;
                default:
                    unknown = -1;
                    break;
            }
        }
        
        if( setVisibleList.isEmpty() && cityAddName.equals("") && turnVerify == 0
                && cityAdd == 0 && oldCardVisibleAndDelete == 0 && settlementRemove == 0
                && settlementRemoveName.equals("") && unknown == 0)
        {
            status = true;
        }
        else
        {
            err = "ensureBuyDevelopmentCardMoveCountCorrect failed"
                + "setVisibleList.isEmpty(): " + setVisibleList.isEmpty() + "\n"
                + "cityAddName.equals(\"\"): " + cityAddName.equals("") + "\n"
                + "turnVerify: " + turnVerify + "\n"
                + "cityAdd: " + cityAdd + "\n"
                + "oldCardVisibleAndDelete: " + oldCardVisibleAndDelete + "\n"
                + "settlementRemove: " + settlementRemove + "\n"
                + "settlementRemoveName.equals(\"\"): " + settlementRemoveName.equals("") + "\n"
                + "unknown: " + unknown;
        }
        
        return status;
    }
    
    private boolean ensureBuyDevelopmentCardMoveCountCorrect(List<Operation> lastMove, String playerString)
    {
        boolean status = false;
        
        int turnVerify = 1;
        int newDevelopmentCardVisible = 1;
        int newDevelopmentCardAssignment = 1;
        int oldCardVisibleAndDelete = 3;
        int unknown = 0;
        Vector<String> setVisibleList = new Vector<String>();
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            switch(lastMove.get(i).getClassName())
            {
                case "Set":
                    if( ((Set) lastMove.get(i)).getKey().equals(Constants.TURN)
                      &&((Set) lastMove.get(i)).getValue().equals(playerString))
                    {
                        turnVerify--;
                    }
                    else if(((Set) lastMove.get(i)).getKey().contains("DEV"))
                    {
                        newDevelopmentCardAssignment--;
                    }
                    else
                    {
                        unknown--;
                    }
                    break;
                case "SetVisibility":
                    if(((SetVisibility)lastMove.get(i)).getKey().contains("DCT"))
                    {
                        newDevelopmentCardVisible--;
                    }
                    else
                    {
                        setVisibleList.add(((SetVisibility) lastMove.get(i)).getKey());
                    }
                    break;
                case "Delete":
                    if(setVisibleList.contains(((Delete)lastMove.get(i)).getKey()))
                    {
                        setVisibleList.remove(((Delete)lastMove.get(i)).getKey());
                        oldCardVisibleAndDelete--;
                    }
                    else
                    {
                        unknown--;
                    }
                    break;
                default:
                    unknown = -1;
                    break;
            }
        }
        
        if( setVisibleList.isEmpty() && turnVerify == 0 && newDevelopmentCardVisible == 0 && 
                newDevelopmentCardAssignment == 0 && oldCardVisibleAndDelete == 0 && unknown == 0)
        {
            status = true;
        }
        else
        {
            err = "ensureBuyDevelopmentCardMoveCountCorrect failed"
                + "setVisibleList.isEmpty(): " + setVisibleList.isEmpty() + "\n"
                + "turnVerify: " + turnVerify + "\n"
                + "newDevelopmentCardVisible: " + newDevelopmentCardVisible + "\n"
                + "newDevelopmentCardAssignment: " + newDevelopmentCardAssignment + "\n"
                + "oldCardVisibleAndDelete: " + oldCardVisibleAndDelete + "\n"
                + "unknown: " + unknown;
        }
        
        return status;
    }
    
    private boolean ensureTotalTradeMoveCountCorrect(List<Operation> lastMove, String playerString, int count)
    {
        boolean status = false;
        
        int turnVerify = 1;
        int newCardSet = 1;
        int oldCardVisibleAndDelete = count;
        int unknown = 0;
        Vector<String> setVisibleList = new Vector<String>();
        
        for(int i = 0; i < lastMove.size(); i++)
        {
            switch(lastMove.get(i).getClassName())
            {
                case "Set":
                    if( ((Set) lastMove.get(i)).getKey().equals(Constants.TURN)
                      &&((Set) lastMove.get(i)).getValue().equals(playerString))
                    {
                        turnVerify--;
                    }
                    else if(((Set) lastMove.get(i)).getKey().contains(playerString))
                    {
                        newCardSet--;
                    }
                    else
                    {
                        unknown--;
                    }
                    break;
                case "SetVisibility":
                    setVisibleList.add(((SetVisibility) lastMove.get(i)).getKey());
                    break;
                case "Delete":
                    if(setVisibleList.contains(((Delete)lastMove.get(i)).getKey()))
                    {
                        setVisibleList.remove(((Delete)lastMove.get(i)).getKey());
                        oldCardVisibleAndDelete--;
                    }
                    else
                    {
                        unknown--;
                    }
                    break;
                default:
                    unknown = -1;
                    break;
            }
        }
        
        if( setVisibleList.isEmpty() && turnVerify == 0 && newCardSet == 0 && oldCardVisibleAndDelete == 0 && unknown == 0)
        {
            status = true;
        }
        else
        {
            err = "ensureTotalTradeMoveCountCorrect failed"
                + "setVisibleList.isEmpty(): " + setVisibleList.isEmpty() + "\n"
                + "turnVerify: " + turnVerify + "\n"
                + "newCardSet: " + newCardSet + "\n"
                + "oldCardVisibleAndDelete: " + oldCardVisibleAndDelete + "\n"
                + "unknown: " + unknown;
        }
        
        return status;
    }
    

    private boolean ownsTwoToOneHarbor(Map<String, Object> state, String playerString, String resource)
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
    

    private boolean ownsThreeToOneHarbor(Map<String, Object> state, String playerString)
    {
        boolean status = false;
        
        ImmutableList<String> harborBonusList = getHarborBonusList(state, playerString);
        
        for(int i = 0; i < harborBonusList.size(); i++)
        {
            status = status || harborBonusList.get(i).equals(Constants.HARBORTYPE00);
        }
        
        return status;
    }
    

    private ImmutableList<String> getHarborBonusList(Map<String, Object> state, String playerString)
    {
        ImmutableList<String> harborBonusList = null;
        Builder<String> listBuilder = new Builder<String>();
        
        String check = (String) state.get(Constants.NODE00);
        
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
    

    private ImmutableList<String> getNodesFromPath(String path)
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
	

	private ImmutableList<String> getPathsFromNode(String node)
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
}
