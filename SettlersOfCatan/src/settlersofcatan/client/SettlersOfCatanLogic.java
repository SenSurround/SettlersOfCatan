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
	public VerifyMoveDone verify(VerifyMove verifyMove) {
        // TODO: I will implement this method in HW2
        return new VerifyMoveDone();
    }
	
	public void checkMoveIsLegal(VerifyMove verifyMove) {
	    List<Operation> lastMove = verifyMove.getLastMove();
	    Map<String, Object> lastState = verifyMove.getLastState();
	    int lastMovePlayerId = verifyMove.getLastMovePlayerId();
	    
	    String playerString = getPlayerId(verifyMove.getPlayerIds(), lastMovePlayerId);
	    
	    boolean status = moveIsLegal(lastMove, lastState, playerString);
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
	    boolean status = false;
        int ore = 0;
        int grain = 0;
	    int lumber = 0;
	    int wool = 0;
        int brick = 0;
	    
	    for(int i = 0; i < lastMove.size(); i++)
	    {
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
	    
	    // Settlement
	    if(ore == 0 && grain == 1 && lumber == 1 && wool == 1 && brick == 1)
	    {
	        
	    }
	    // Road
	    else if(ore == 0 && grain == 0 && lumber == 1 && wool == 0 && brick == 1)
        {
            
        }
	    // City
        else if(ore == 3 && grain == 2 && lumber == 0 && wool == 0 && brick == 0)
        {
            
        }
	    // Development Card
        else if(ore == 1 && grain == 1 && lumber == 0 && wool == 1 && brick == 0)
        {
            
        }
	    // Normal Harbor Trade
        else if( (ore == 4 && grain == 0 && lumber == 0 && wool == 0 && brick == 0)
               ||(ore == 0 && grain == 4 && lumber == 0 && wool == 0 && brick == 0)
               ||(ore == 0 && grain == 0 && lumber == 4 && wool == 0 && brick == 0)
               ||(ore == 0 && grain == 0 && lumber == 0 && wool == 4 && brick == 0)
               ||(ore == 0 && grain == 0 && lumber == 0 && wool == 0 && brick == 4))
        {
            
        }
	    // 3 for 1 Harbor Trade
        else if( ownsThreeToOneHarbor(lastState, playerString)
               && ( (ore == 3 && grain == 0 && lumber == 0 && wool == 0 && brick == 0)
                  ||(ore == 0 && grain == 3 && lumber == 0 && wool == 0 && brick == 0)
                  ||(ore == 0 && grain == 0 && lumber == 3 && wool == 0 && brick == 0)
                  ||(ore == 0 && grain == 0 && lumber == 0 && wool == 3 && brick == 0)
                  ||(ore == 0 && grain == 0 && lumber == 0 && wool == 0 && brick == 3)))
        {
            
        }
        // 2 for 1 Ore Trade
        else if( ownsTwoToOneHarbor(lastState, playerString, Constants.ORE)
                 && ore == 2 && grain == 0 && lumber == 0 && wool == 0 && brick == 0)
        {
            status = ensureTotalTwoTradeMoveCountCorrect(lastMove, playerString);
        }
        // 2 for 1 Grain Trade
        else if( ownsTwoToOneHarbor(lastState, playerString, Constants.GRAIN)
                 && ore == 0 && grain == 2 && lumber == 0 && wool == 0 && brick == 0)
        {
            status = ensureTotalTwoTradeMoveCountCorrect(lastMove, playerString);
        }
        // 2 for 1 Lumber Trade
        else if( ownsTwoToOneHarbor(lastState, playerString, Constants.LUMBER)
                 && ore == 0 && grain == 0 && lumber == 2 && wool == 0 && brick == 0)
        {
            status = ensureTotalTwoTradeMoveCountCorrect(lastMove, playerString);
        }
        // 2 for 1 Wool Trade
        else if( ownsTwoToOneHarbor(lastState, playerString, Constants.WOOL)
                 && ore == 0 && grain == 0 && lumber == 0 && wool ==2 && brick == 0)
        {
            status = ensureTotalTwoTradeMoveCountCorrect(lastMove, playerString);
        }
        // 2 for 1 Brick Trade
        else if( ownsTwoToOneHarbor(lastState, playerString, Constants.BRICK)
                 && ore == 0 && grain == 0 && lumber == 0 && wool == 0 && brick == 2)
        {
            status = ensureTotalTwoTradeMoveCountCorrect(lastMove, playerString);
        }
	    
	    return status;
	}
    
    private boolean ensureTotalTwoTradeMoveCountCorrect(List<Operation> lastMove, String playerString)
    {
        boolean status = false;
        
        int turnVerify = 1;
        int newCardSet = 1;
        int oldCardVisibleAndDelete = 2;
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
                    break;
                default:
                    unknown = -1;
                    break;
            }
        }
        
        if( turnVerify == 0 && newCardSet == 0 && oldCardVisibleAndDelete == 0 && unknown == 0)
        {
            status = true;
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
        
        // Harbor 00
        if( ((String) state.get(Constants.NODE00)).contains(playerString)
         || ((String) state.get(Constants.NODE03)).contains(playerString))
        {
            listBuilder.add((String) state.get(Constants.HARBOR00));
        }

        // Harbor 01
        if( ((String) state.get(Constants.NODE01)).contains(playerString)
         || ((String) state.get(Constants.NODE05)).contains(playerString))
        {
            listBuilder.add((String) state.get(Constants.HARBOR01));
        }

        // Harbor 02
        if( ((String) state.get(Constants.NODE10)).contains(playerString)
         || ((String) state.get(Constants.NODE15)).contains(playerString))
        {
            listBuilder.add((String) state.get(Constants.HARBOR02));
        }

        // Harbor 03
        if( ((String) state.get(Constants.NODE26)).contains(playerString)
         || ((String) state.get(Constants.NODE32)).contains(playerString))
        {
            listBuilder.add((String) state.get(Constants.HARBOR03));
        }

        // Harbor 04
        if( ((String) state.get(Constants.NODE42)).contains(playerString)
         || ((String) state.get(Constants.NODE46)).contains(playerString))
        {
            listBuilder.add((String) state.get(Constants.HARBOR04));
        }

        // Harbor 05
        if( ((String) state.get(Constants.NODE49)).contains(playerString)
         || ((String) state.get(Constants.NODE52)).contains(playerString))
        {
            listBuilder.add((String) state.get(Constants.HARBOR05));
        }

        // Harbor 06
        if( ((String) state.get(Constants.NODE47)).contains(playerString)
         || ((String) state.get(Constants.NODE51)).contains(playerString))
        {
            listBuilder.add((String) state.get(Constants.HARBOR06));
        }

        // Harbor 07
        if( ((String) state.get(Constants.NODE33)).contains(playerString)
         || ((String) state.get(Constants.NODE38)).contains(playerString))
        {
            listBuilder.add((String) state.get(Constants.HARBOR07));
        }

        // Harbor 08
        if( ((String) state.get(Constants.NODE11)).contains(playerString)
         || ((String) state.get(Constants.NODE16)).contains(playerString))
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
