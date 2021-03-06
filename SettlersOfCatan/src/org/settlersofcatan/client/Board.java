package org.settlersofcatan.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Board {
    
    List<Hex> hexList;
    List<Node> nodeList;
    List<Path> pathList;

    int[] dieList = {5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11 };
    int[] dieHexList = {16, 17, 18, 15, 11, 6, 2, 1, 0, 3, 7, 12, 13, 14, 10, 5, 4, 8, 9 };
    
    public Board(Map<String, Object> state)
    {
        prepHexList(state);
        prepNodeList(state);
        prepPathList(state);
    }
    
    private void prepHexList(Map<String, Object> state)
    {
        int desert = -1;
        hexList = Arrays.asList(
        new Hex(0 , 4 , 8 , 12, 7 , 3 , state.get(SettlersOfCatanConstants.HEX00).toString()),
        new Hex(1 , 5 , 9 , 13, 8 , 4 , state.get(SettlersOfCatanConstants.HEX01).toString()),
        new Hex(2 , 6 , 10, 14, 9 , 5 , state.get(SettlersOfCatanConstants.HEX02).toString()),
                
        new Hex(7 , 12, 17, 22, 16, 11, state.get(SettlersOfCatanConstants.HEX03).toString()),
        new Hex(8 , 13, 18, 23, 17, 12, state.get(SettlersOfCatanConstants.HEX04).toString()),
        new Hex(9 , 14, 19, 24, 18, 13, state.get(SettlersOfCatanConstants.HEX05).toString()),
        new Hex(10, 15, 20, 25, 19, 14, state.get(SettlersOfCatanConstants.HEX06).toString()),
                
        new Hex(16, 22, 28, 33, 27, 21, state.get(SettlersOfCatanConstants.HEX07).toString()),
        new Hex(17, 23, 29, 34, 28, 22, state.get(SettlersOfCatanConstants.HEX08).toString()),
        new Hex(18, 24, 30, 35, 29, 23, state.get(SettlersOfCatanConstants.HEX09).toString()),
        new Hex(19, 25, 31, 36, 30, 24, state.get(SettlersOfCatanConstants.HEX10).toString()),
        new Hex(20, 26, 32, 37, 31, 25, state.get(SettlersOfCatanConstants.HEX11).toString()),
                
        new Hex(28, 34, 39, 43, 38, 33, state.get(SettlersOfCatanConstants.HEX12).toString()),
        new Hex(29, 35, 40, 44, 39, 34, state.get(SettlersOfCatanConstants.HEX13).toString()),
        new Hex(30, 36, 41, 45, 40, 35, state.get(SettlersOfCatanConstants.HEX14).toString()),
        new Hex(31, 37, 42, 46, 41, 36, state.get(SettlersOfCatanConstants.HEX15).toString()),
                
        new Hex(39, 44, 48, 51, 47, 43, state.get(SettlersOfCatanConstants.HEX16).toString()),
        new Hex(40, 45, 49, 52, 48, 44, state.get(SettlersOfCatanConstants.HEX17).toString()),
        new Hex(41, 46, 50, 53, 49, 45, state.get(SettlersOfCatanConstants.HEX18).toString()));
        
        int adjust = 0;
        
        for (int i = 0; i < 19; i++) {
            if (!hexList.get(dieHexList[i]).getResource().equals(SettlersOfCatanConstants.DESERT)) {
                hexList.get(dieHexList[i]).setDieRoll(dieList[i - adjust]);
            } else {
                desert = dieHexList[i];
                adjust++;
            }
        }
        if(state.containsKey(SettlersOfCatanConstants.ROBBER))
            hexList.get(Integer.parseInt(state.get(SettlersOfCatanConstants.ROBBER).toString().substring(3))).setRobber(true);
        else
            hexList.get(desert).setRobber(true);
            
    }
    
    private void prepNodeList(Map<String, Object> state)
    {
        nodeList = Arrays.asList(
                new Node(0),
                new Node(1),
                new Node(2),
                new Node(3),
                new Node(4),
                new Node(5),
                new Node(6),
                new Node(7),
                new Node(8),
                new Node(9),
                new Node(10),
                new Node(11),
                new Node(12),
                new Node(13),
                new Node(14),
                new Node(15),
                new Node(16),
                new Node(17),
                new Node(18),
                new Node(19),
                new Node(20),
                new Node(21),
                new Node(22),
                new Node(23),
                new Node(24),
                new Node(25),
                new Node(26),
                new Node(27),
                new Node(28),
                new Node(29),
                new Node(30),
                new Node(31),
                new Node(32),
                new Node(33),
                new Node(34),
                new Node(35),
                new Node(36),
                new Node(37),
                new Node(38),
                new Node(39),
                new Node(40),
                new Node(41),
                new Node(42),
                new Node(43),
                new Node(44),
                new Node(45),
                new Node(46),
                new Node(47),
                new Node(48),
                new Node(49),
                new Node(50),
                new Node(51),
                new Node(52),
                new Node(53));
        

        
        String searchMe = "";
        
        for(int i = 0; i < 54; i++)
        {
            if(i < 10)
                searchMe = SettlersOfCatanConstants.NODETOKEN + "0" + i;
            else
                searchMe = SettlersOfCatanConstants.NODETOKEN + i;
            
            if(state.containsKey(searchMe))
            {
                if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.PB))
                    nodeList.get(i).setPlayer(0);
                else if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.PR))
                    nodeList.get(i).setPlayer(1);
                else if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.PY))
                    nodeList.get(i).setPlayer(2);
                else if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.PG))
                    nodeList.get(i).setPlayer(3);
                
                if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.SETTLEMENTTOKEN))
                    nodeList.get(i).setSettlement(1);
                else if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.CITYTOKEN))
                    nodeList.get(i).setSettlement(2);
            }
        }
        
        nodeList.get(0).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR00).toString());
        nodeList.get(3).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR00).toString());
        nodeList.get(1).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR01).toString());
        nodeList.get(5).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR01).toString());
        nodeList.get(10).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR02).toString());
        nodeList.get(15).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR02).toString());
        nodeList.get(26).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR03).toString());
        nodeList.get(32).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR03).toString());
        nodeList.get(42).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR04).toString());
        nodeList.get(46).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR04).toString());
        nodeList.get(49).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR05).toString());
        nodeList.get(52).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR05).toString());
        nodeList.get(47).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR06).toString());
        nodeList.get(51).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR06).toString());
        nodeList.get(33).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR07).toString());
        nodeList.get(38).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR07).toString());
        nodeList.get(11).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR08).toString());
        nodeList.get(16).setHarborBonus(state.get(SettlersOfCatanConstants.HARBOR08).toString());
    }
    
    private void prepPathList(Map<String, Object> state)
    {
        pathList = Arrays.asList(
            new Path(0,   3),
            new Path(0,   4),
            new Path(1,   4),
            new Path(1,   5),
            new Path(2,   5),
            new Path(2,   6),
            new Path(3,   7),
            new Path(4,   8),
            new Path(5,   9),
            new Path(6,  10),
            new Path(7,  11),
            new Path(7,  12),
            new Path(8,  12),
            new Path(8,  13),
            new Path(9,  13),
            new Path(9,  14),
            new Path(10, 14),
            new Path(10, 15),
            new Path(11, 16),
            new Path(12, 17),
            new Path(13, 18),
            new Path(14, 19),
            new Path(15, 20),
            new Path(16, 21),
            new Path(16, 22),
            new Path(17, 22),
            new Path(17, 23),
            new Path(18, 23),
            new Path(18, 24),
            new Path(19, 24),
            new Path(19, 25),
            new Path(20, 25),
            new Path(20, 26),
            new Path(21, 27),
            new Path(22, 28),
            new Path(23, 29),
            new Path(24, 30),
            new Path(25, 31),
            new Path(26, 32),
            new Path(27, 33),
            new Path(28, 33),
            new Path(28, 34),
            new Path(29, 34),
            new Path(29, 35),
            new Path(30, 35),
            new Path(30, 36),
            new Path(31, 36),
            new Path(31, 37),
            new Path(32, 37),
            new Path(33, 38),
            new Path(34, 39),
            new Path(35, 40),
            new Path(36, 41),
            new Path(37, 42),
            new Path(38, 43),
            new Path(39, 43),
            new Path(39, 44),
            new Path(40, 44),
            new Path(40, 45),
            new Path(41, 45),
            new Path(41, 46),
            new Path(42, 46),
            new Path(43, 47),
            new Path(44, 48),
            new Path(45, 49),
            new Path(46, 50),
            new Path(47, 51),
            new Path(48, 51),
            new Path(48, 52),
            new Path(49, 52),
            new Path(49, 53),
            new Path(50, 53));
        
        String searchMe = "";
        
        for(int i = 0; i < 72; i++)
        {
            if(i < 10)
                searchMe = SettlersOfCatanConstants.PATHTOKEN + "0" + i;
            else
                searchMe = SettlersOfCatanConstants.PATHTOKEN + i;
            
            if(state.containsKey(searchMe))
            {
                if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.PB))
                        pathList.get(i).setPlayer(0);
                else if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.PR))
                        pathList.get(i).setPlayer(1);
                else if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.PY))
                        pathList.get(i).setPlayer(2);
                else if(state.get(searchMe).toString().contains(SettlersOfCatanConstants.PG))
                        pathList.get(i).setPlayer(3);
            }
        }
        
    }

    public List<Hex> getHexList()
    {
        return hexList;
    }
    
    public List<Node> getNodeList()
    {
        return nodeList;
    }
    
    public List<Path> getPathList()
    {
        return pathList;
    }

    public boolean hasAvailableSettlements(int playerId)
    {
        boolean retVal = false;
        
        int settlementCount = 0;
        
        for(int i = 0; i < 54; i++)
        {
            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getSettlement() == 1)
            {
                settlementCount++;
            }
        }
        
        if(settlementCount < 5)
            retVal = true;
        
        return retVal;
    }

    public int numAvailableSettlements(int playerId)
    {
        int settlementCount = 5;
        
        for(int i = 0; i < 54; i++)
        {
            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getSettlement() == 1)
            {
                settlementCount--;
            }
        }
        
        return settlementCount;
    }

    public boolean hasOneSettlementOut(int playerId)
    {
        boolean retVal = false;
        
        for(int i = 0; i < 54; i++)
        {
            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getSettlement() == 1)
            {
                retVal = true;
            }
        }
        
        return retVal;
    }
    
    public boolean hasAvailableCities(int playerId)
    {
        boolean retVal = false;
        
        int cityCount = 0;
        
        for(int i = 0; i < 54; i++)
        {
            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getSettlement() == 2)
            {
                cityCount++;
            }
        }
        
        if(cityCount < 4)
            retVal = true;
        
        return retVal;
    }
    
    public int numAvailableCities(int playerId)
    {
        int cityCount = 4;
        
        for(int i = 0; i < 54; i++)
        {
            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getSettlement() == 2)
            {
                cityCount--;
            }
        }
        
        return cityCount;
    }
    
    public boolean hasAvailableRoads(int playerId)
    {
        boolean retVal = false;
        
        int roadCount = 0;
        
        for(int i = 0; i < 72; i++)
        {
            if( pathList.get(i).getPlayer() == playerId)
            {
                roadCount++;
            }
        }
        
        if(roadCount < 15)
            retVal = true;
        
        return retVal;
    }
    
    public int numAvailableRoads(int playerId)
    {
        int roadCount = 15;
        
        for(int i = 0; i < 72; i++)
        {
            if( pathList.get(i).getPlayer() == playerId)
            {
                roadCount--;
            }
        }
        
        return roadCount;
    }

    public boolean ownsThreeToOnePort(int playerId)
    {
        boolean retVal = false;
        
        for(int i = 0; i < 54; i++)
        {
            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getHarborBonus().equals(SettlersOfCatanConstants.HARBORTYPE00))
                retVal = true;
        }
        
        return retVal;
    }
    
    public boolean ownsTwoToOnePort(int playerId, String resource)
    {
        boolean retVal = false;
        
        for(int i = 0; i < 54; i++)
        {
            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getHarborBonus().equals(SettlersOfCatanConstants.HARBORTYPE01)
             && resource.equals(SettlersOfCatanConstants.ORE))
                retVal = true;

            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getHarborBonus().equals(SettlersOfCatanConstants.HARBORTYPE02)
             && resource.equals(SettlersOfCatanConstants.GRAIN))
                retVal = true;

            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getHarborBonus().equals(SettlersOfCatanConstants.HARBORTYPE03)
             && resource.equals(SettlersOfCatanConstants.LUMBER))
                retVal = true;

            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getHarborBonus().equals(SettlersOfCatanConstants.HARBORTYPE04)
             && resource.equals(SettlersOfCatanConstants.WOOL))
                retVal = true;

            if( nodeList.get(i).getPlayer() == playerId
             && nodeList.get(i).getHarborBonus().equals(SettlersOfCatanConstants.HARBORTYPE05)
             && resource.equals(SettlersOfCatanConstants.BRICK))
                retVal = true;
        }
        
        return retVal;
    }
    
    public List<String> getResoucesForNode(int node)
    {
        List<String> resources = new ArrayList<String>();
        
        for(int i = 0; i < 19; i++)
        {
            for(int j = 0; j < 6; j++)
            {
                if(hexList.get(i).getLocation(j) == node)
                {
                    if(hexList.get(i).getResource() != SettlersOfCatanConstants.DESERT)
                        resources.add(hexList.get(i).getResource());
                }
            }
        }
        
        return resources;
    }
    
    public List<Integer> getNodesPerDieRoll(int dieRoll)
    {
        List<Integer> nodes = new ArrayList<Integer>();
        
        for(int i = 0; i < 19; i++)
        {
            if(hexList.get(i).getDieRoll() == dieRoll
           && !hexList.get(i).getRobber())
            {
                nodes.add(hexList.get(i).locations[0]);
                nodes.add(hexList.get(i).locations[1]);
                nodes.add(hexList.get(i).locations[2]);
                nodes.add(hexList.get(i).locations[3]);
                nodes.add(hexList.get(i).locations[4]);
                nodes.add(hexList.get(i).locations[5]);
            }
        }
        
        return nodes;
    }
    
    public List<String> getResourcesPerDieRoll(int dieRoll)
    {
        List<String> nodes = new ArrayList<String>();
        
        for(int i = 0; i < 19; i++)
        {
            if(hexList.get(i).getDieRoll() == dieRoll
            && !hexList.get(i).getRobber())
            {
                nodes.add(hexList.get(i).getResource());
                nodes.add(hexList.get(i).getResource());
                nodes.add(hexList.get(i).getResource());
                nodes.add(hexList.get(i).getResource());
                nodes.add(hexList.get(i).getResource());
                nodes.add(hexList.get(i).getResource());
            }
        }
        
        return nodes;
    }
    
    public String addWhoToRob(int hex, int playerId, Map<String, Object> lastState)
    {
        String output = "";
        
        boolean canStealFromBlue = false;
        boolean canStealFromRed = false;
        boolean canStealFromYellow = false;
        boolean canStealFromGreen = false;

        String blueString = "";
        String redString = "";
        String yellowString = "";
        String greenString = "";
        
        for(int i = 0; i < 30; i++)
        {
            if(i < 10)
            {
                blueString = SettlersOfCatanConstants.RESOURCECARDTOKEN + "0" + i + SettlersOfCatanConstants.PB;
                redString = SettlersOfCatanConstants.RESOURCECARDTOKEN + "0" + i + SettlersOfCatanConstants.PR;
                yellowString = SettlersOfCatanConstants.RESOURCECARDTOKEN + "0" + i + SettlersOfCatanConstants.PY;
                greenString = SettlersOfCatanConstants.RESOURCECARDTOKEN + "0" + i + SettlersOfCatanConstants.PG;
            }
            else
            {
                blueString = SettlersOfCatanConstants.RESOURCECARDTOKEN + i + SettlersOfCatanConstants.PB;
                redString = SettlersOfCatanConstants.RESOURCECARDTOKEN + i + SettlersOfCatanConstants.PR;
                yellowString = SettlersOfCatanConstants.RESOURCECARDTOKEN + i + SettlersOfCatanConstants.PY;
                greenString = SettlersOfCatanConstants.RESOURCECARDTOKEN + i + SettlersOfCatanConstants.PG;
            }
            
            if(lastState.containsKey(blueString))
            {
                canStealFromBlue = true;
            }
            if(lastState.containsKey(redString))
            {
                canStealFromRed = true;
            }
            if(lastState.containsKey(yellowString))
            {
                canStealFromYellow = true;
            }
            if(lastState.containsKey(greenString))
            {
                canStealFromGreen = true;
            }
        }
        
        for(int i = 0; i < 6; i++)
        {
            if( nodeList.get(hexList.get(hex).locations[i]).getPlayer() == 0
             && nodeList.get(hexList.get(hex).locations[i]).getPlayer() != playerId
             && canStealFromBlue)
            {
                output = output + SettlersOfCatanConstants.PB;
            }
            else if( nodeList.get(hexList.get(hex).locations[i]).getPlayer() == 1
                  && nodeList.get(hexList.get(hex).locations[i]).getPlayer() != playerId
                  && canStealFromRed)
            {
                output = output + SettlersOfCatanConstants.PR;
            }
            else if( nodeList.get(hexList.get(hex).locations[i]).getPlayer() == 2
                  && nodeList.get(hexList.get(hex).locations[i]).getPlayer() != playerId
                  && canStealFromYellow)
            {
                output = output + SettlersOfCatanConstants.PY;
            }
            else if( nodeList.get(hexList.get(hex).locations[i]).getPlayer() == 3
                  && nodeList.get(hexList.get(hex).locations[i]).getPlayer() != playerId
                  && canStealFromBlue)
            {
                output = output + SettlersOfCatanConstants.PG;
            }
        }
        
        return output;
    }
    
}
