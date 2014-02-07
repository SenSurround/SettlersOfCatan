package settlersofcatan.client;

import java.util.Vector;

import settlersofcatan.client.Hex.Resource;
import settlersofcatan.client.Node.Settlement;

public class State {
    
    //                           MAP PICTURE
    //                             N = NODE
    //                             P = PATH
    //                             H = HEX
    //
    //                   N00         N01         N02
    //                P00   P01   P02   P03   P04   P05
    //             N03         N04         N05         N06
    //             P06   H00   P07   H01   P08   H02   P09
    //             N07         N08         N09         N10
    //          P10   P11   P12   P13   P14   P15   P16   P17
    //       N11         N12         N13         N14         N15
    //       P18   H03   P19   H04   P20   H05   P21   H06   P22
    //       N16         N17         N18         N19         N20
    //    P23   P24   P25   P26   P27   P28   P29   P30   P31   P32
    // N21         N22         N23         N24         N25         N26
    // P33   H07   P34   H08   P35   H09   P36   H10   P37   H11   P38
    // N27         N28         N29         N30         N31         N32
    //    P39   P40   P41   P42   P43   P44   P45   P46   P47   P48
    //       N33         N34         N35         N36         N37
    //       P49   H12   P50   H13   P51   H14   P52   H15   P53
    //       N38         N39         N40         N41         N42
    //          P54   P55   P56   P57   P58   P59   P60   P61
    //             N43         N44         N45         N46
    //             P62   H16   P63   H17   P64   H18   P65
    //             N47         N48         N49         N50
    //                P66   P67   P68   P69   P70   P71
    //                   N51         N52         N53
    
    static final int MAX_PLAYERS = 4;
    
    int[] hexList = {16, 17, 18, 15, 11, 6, 2, 1, 0, 3, 7, 12, 13, 14, 10, 5, 4, 8, 9 };
    int[] dieList = {5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11 };
    Resource[] resourceList = { Resource.ORE,    Resource.ORE,    Resource.ORE,
                                Resource.GRAIN,  Resource.GRAIN,  Resource.GRAIN,  Resource.GRAIN,
                                Resource.LUMBER, Resource.LUMBER, Resource.LUMBER, Resource.LUMBER,
                                Resource.WOOL,   Resource.WOOL,   Resource.WOOL,   Resource.WOOL,
                                Resource.BRICK,  Resource.BRICK,  Resource.BRICK,
                                Resource.DESERT};
    
    String[] developmentCard = {
            "SOLDIER", "SOLDIER", "SOLDIER", "SOLDIER", "SOLDIER", "SOLDIER", "SOLDIER",
            "SOLDIER", "SOLDIER", "SOLDIER", "SOLDIER", "SOLDIER", "SOLDIER", "SOLDIER",
            "YEAR OF PLENTY", "YEAR OF PLENTY",
            "MONOPOLY", "MONOPOLY",
            "ROAD BUILDING", "ROAD BUILDING",
            "LIBRARY",
            "GOVERNOR'S HOUSE",
            "UNIVERSITY OF CATAN",
            "CHAPEL",
            "MARKET" };
    
    Vector<String> developmentCardStack;
    
    int playerTurn;
    Player[] player = {null, null, null, null};

    Node[] node = { null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null };
    
    Path[] path = { null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null };

    Hex[] board = { null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null};
    
    public State() {
        this(MAX_PLAYERS);
    }
    
    public State(int players) {
        playerTurn = 0;
        developmentCardStack.clear();
        
        // Shuffle(resourceList);
        // Shuffle(developmentCard);
        
        for (int i = 0; i < developmentCard.length; i++) {
            developmentCardStack.add(developmentCard[i]);
        }
        
        initNodes();
        initPaths();
        initHex();
        
        for (int i = 0; i < MAX_PLAYERS; i++) {
            player[i] = new Player(i);
        }
    }

/*********************************************************************************/
    
    public boolean canPlaceSettlement(int nodeId, int playerId) {
        boolean retVal = true;
        
        retVal = retVal && isAPathConnectedToNodeOwnedByPlayer(nodeId, playerId);
        retVal = retVal && isThreeClosestNodesClear(nodeId);
        retVal = retVal && (player[playerId].getResourceCount(Resource.BRICK) > 0);
        retVal = retVal && (player[playerId].getResourceCount(Resource.LUMBER) > 0);
        retVal = retVal && (player[playerId].getResourceCount(Resource.WOOL) > 0);
        retVal = retVal && (player[playerId].getResourceCount(Resource.GRAIN) > 0);
        retVal = retVal && (player[playerId].getSettlementCount() > 0);
        
        return retVal;
    }
    
    public boolean canPlaceRoad(int pathId, int playerId) {
        boolean retVal = true;
        
        retVal = retVal
                 && (isAPathConnectedToPathOwnedByPlayer(pathId, playerId)
                    || doesPlayerHaveSettlementOnNodeAttachedToPath(pathId, playerId));
        retVal = retVal && (player[playerId].getResourceCount(Resource.BRICK) > 0);
        retVal = retVal && (player[playerId].getResourceCount(Resource.LUMBER) > 0);
        retVal = retVal && (player[playerId].getRoadCount() > 0);
        
        return retVal;
    }
    
    public boolean canUpgradeSettlementToCity(int nodeId, int playerId) {
        boolean retVal = true;
        
        retVal = retVal && doesPlayerHaveSettlementOnNode(nodeId, playerId);
        retVal = retVal && (player[playerId].getResourceCount(Resource.ORE) > 2);
        retVal = retVal && (player[playerId].getResourceCount(Resource.GRAIN) > 1);
        retVal = retVal && (player[playerId].getCityCount() > 0);
        
        return retVal;
    }
    
    public boolean canPurchaseDevelopmentCard(int playerId) {
        boolean retVal = true;
        
        retVal = retVal && !developmentCardStack.isEmpty();
        retVal = retVal && (player[playerId].getResourceCount(Resource.ORE) > 0);
        retVal = retVal && (player[playerId].getResourceCount(Resource.WOOL) > 0);
        retVal = retVal && (player[playerId].getResourceCount(Resource.GRAIN) > 0);
        
        return retVal;
    }
    
    public boolean canTradeResourceNormal(int playerId, Resource resource) {
        boolean retVal = true;
        
        retVal = retVal && (player[playerId].getResourceCount(resource) > 3);
        
        return retVal;
    }
    
    public boolean canTradeResourceDiscount(int playerId, Resource resource) {
        boolean retVal = true;
        
        retVal = retVal && (player[playerId].getResourceCount(resource) > 2);
        
        return retVal;
    }
    
/*********************************************************************************/
    
    private void initNodes() {
        for (int i = 0; i < 54; i++) {
            node[i] = new Node(i);
        }
    }
    
    private void initPaths() {
        path[0]  = new Path(0,   3);
        path[1]  = new Path(0,   4);
        path[2]  = new Path(1,   4);
        path[3]  = new Path(1,   5);
        path[4]  = new Path(2,   5);
        path[5]  = new Path(2,   6);
        path[6]  = new Path(3,   7);
        path[7]  = new Path(4,   8);
        path[8]  = new Path(5,   9);
        path[9]  = new Path(6,  10);
        path[10] = new Path(7,  11);
        path[11] = new Path(7,  12);
        path[12] = new Path(8,  12);
        path[13] = new Path(8,  13);
        path[14] = new Path(9,  13);
        path[15] = new Path(9,  14);
        path[16] = new Path(10, 14);
        path[17] = new Path(10, 15);
        path[18] = new Path(11, 16);
        path[19] = new Path(12, 17);
        path[20] = new Path(13, 18);
        path[21] = new Path(14, 19);
        path[22] = new Path(15, 20);
        path[23] = new Path(16, 21);
        path[24] = new Path(16, 22);
        path[25] = new Path(17, 22);
        path[26] = new Path(17, 23);
        path[27] = new Path(18, 23);
        path[28] = new Path(18, 24);
        path[29] = new Path(19, 24);
        path[30] = new Path(19, 25);
        path[31] = new Path(20, 25);
        path[32] = new Path(20, 26);
        path[33] = new Path(21, 27);
        path[34] = new Path(22, 28);
        path[35] = new Path(23, 29);
        path[36] = new Path(24, 30);
        path[37] = new Path(25, 31);
        path[38] = new Path(26, 32);
        path[39] = new Path(27, 33);
        path[40] = new Path(28, 33);
        path[41] = new Path(28, 34);
        path[42] = new Path(29, 34);
        path[43] = new Path(29, 35);
        path[44] = new Path(30, 35);
        path[45] = new Path(30, 36);
        path[46] = new Path(31, 36);
        path[47] = new Path(31, 37);
        path[48] = new Path(32, 37);
        path[49] = new Path(33, 38);
        path[50] = new Path(34, 39);
        path[51] = new Path(35, 40);
        path[52] = new Path(36, 41);
        path[53] = new Path(37, 42);
        path[54] = new Path(38, 43);
        path[55] = new Path(39, 43);
        path[56] = new Path(39, 44);
        path[57] = new Path(40, 44);
        path[58] = new Path(40, 45);
        path[59] = new Path(41, 45);
        path[60] = new Path(41, 46);
        path[61] = new Path(42, 46);
        path[62] = new Path(43, 47);
        path[63] = new Path(44, 48);
        path[64] = new Path(45, 49);
        path[65] = new Path(46, 50);
        path[66] = new Path(47, 51);
        path[67] = new Path(48, 51);
        path[68] = new Path(48, 52);
        path[69] = new Path(49, 52);
        path[70] = new Path(49, 53);
        path[71] = new Path(50, 53);
    }
    
    private void initHex() {
        board[0]  = new Hex(0 , 4 , 8 , 12, 7 , 3 , resourceList[0]);
        board[1]  = new Hex(1 , 5 , 9 , 13, 8 , 4 , resourceList[1]);
        board[2]  = new Hex(2 , 6 , 10, 14, 9 , 5 , resourceList[2]);
        
        board[3]  = new Hex(7 , 12, 17, 22, 16, 11, resourceList[3]);
        board[4]  = new Hex(8 , 13, 18, 23, 17, 12, resourceList[4]);
        board[5]  = new Hex(9 , 14, 19, 24, 18, 13, resourceList[5]);
        board[6]  = new Hex(10, 15, 20, 25, 29, 14, resourceList[6]);
        
        board[7]  = new Hex(16, 22, 28, 33, 27, 21, resourceList[7]);
        board[8]  = new Hex(17, 23, 29, 34, 28, 22, resourceList[8]);
        board[9]  = new Hex(18, 24, 30, 35, 29, 23, resourceList[9]);
        board[10] = new Hex(19, 25, 31, 36, 30, 24, resourceList[10]);
        board[11] = new Hex(20, 26, 32, 37, 31, 25, resourceList[11]);
        
        board[12] = new Hex(28, 34, 39, 43, 38, 33, resourceList[12]);
        board[13] = new Hex(29, 35, 40, 44, 39, 34, resourceList[13]);
        board[14] = new Hex(30, 36, 41, 45, 40, 35, resourceList[14]);
        board[15] = new Hex(31, 37, 42, 46, 41, 36, resourceList[15]);
        
        board[16] = new Hex(39, 44, 48, 51, 47, 43, resourceList[16]);
        board[17] = new Hex(40, 45, 49, 52, 48, 44, resourceList[17]);
        board[18] = new Hex(41, 46, 50, 53, 49, 45, resourceList[18]);
        
        int adjust = 0;
        
        for (int i = 0; i < 19; i++) {
            if (board[hexList[i]].getResource() != Resource.DESERT) {
                board[hexList[i]].setDieRoll(dieList[i - adjust]);
            } else {
                adjust++;
            }
        }
    }
    
    private boolean isAPathConnectedToNodeOwnedByPlayer(int nodeId, int playerId)
    {
        boolean retVal = false;
        
        Vector<Integer> connectedPaths = listPathsConnectedToANode(nodeId);
        
        for (int i = 0; i < connectedPaths.size(); i++) {
            if (path[connectedPaths.elementAt(i)].getPlayer() == playerId) {
                retVal = true;
            }
        }
        
        return retVal;
    }
    
    private Vector<Integer> listPathsConnectedToANode(int nodeId) {
        Vector<Integer> connectedPaths = new Vector<Integer>();
        
        connectedPaths.clear();
        
        for (int i = 0; i < 72; i++) {
            if ((path[i].getNode1() == nodeId)
               || (path[i].getNode2() == nodeId)) {
                connectedPaths.add(i);
            }
        }
        
        return connectedPaths;
    }
    
    private boolean isThreeClosestNodesClear(int nodeId) {
        boolean retVal = true;
        
        Vector<Integer> closestNodes = listNodesClosestToANode(nodeId);
        
        for (int i = 0; i < closestNodes.size(); i++) {
            if (node[closestNodes.elementAt(i)].getSettlement() != Settlement.NONE) {
                retVal = false;
            }
        }
        
        return retVal;
    }
    
    private boolean isAPathConnectedToPathOwnedByPlayer(int pathId, int playerId)
    {
        boolean retVal = false;
        
        Vector<Integer> connectedPaths = listPathsConnectedToAPath(pathId);
        
        for (int i = 0; i < connectedPaths.size(); i++) {
            if (path[connectedPaths.elementAt(i)].getPlayer() == playerId) {
                retVal = true;
            }
        }
        
        return retVal;
    }
    
    private boolean doesPlayerHaveSettlementOnNode(int nodeId, int playerId) {
        boolean retVal = true;
        
        retVal = retVal && node[nodeId].getPlayer() == playerId;
        retVal = retVal && node[nodeId].getSettlement() == Settlement.SETTLEMENT;
        
        return retVal;
    }
    
    private boolean doesPlayerHaveSettlementOnNodeAttachedToPath(int pathId, int playerId) {
        boolean retVal = false;
        
        retVal = (node[path[pathId].getNode1()].getPlayer() == playerId
                 || node[path[pathId].getNode2()].getPlayer() == playerId);
        
        return retVal;
    }
    
    private Vector<Integer> listNodesClosestToANode(int nodeId) {
        Vector<Integer> connectedPaths = listPathsConnectedToANode(nodeId);
        
        for (int i = 0; i < connectedPaths.size(); i++) {
            if (path[connectedPaths.elementAt(i)].getNode1() == nodeId) {
                connectedPaths.add(path[connectedPaths.elementAt(i)].getNode2());
            } else {
                connectedPaths.add(path[connectedPaths.elementAt(i)].getNode1());
            }
        }
        
        return connectedPaths;
    }
    
    private Vector<Integer> listPathsConnectedToAPath(int pathId) {
        int node1 = path[pathId].getNode1();
        int node2 = path[pathId].getNode2();
        Vector<Integer> connectedPaths = new Vector<Integer>();
        
        connectedPaths.clear();
        
        for (int i = 0; i < 72; i++) {
            if ((i != pathId)
               && ((path[i].getNode1() == node1)
                  || (path[i].getNode2() == node1)
                  || (path[i].getNode1() == node2)
                  || (path[i].getNode2() == node2))) {
                connectedPaths.add(i);
            }
        }
        
        return connectedPaths;
    }
    
    
    
    private String processDevelopmentCard(String card) {
        String retVal = "";
        
        switch(card) {
            case "LIBRARY":
            case "GOVERNOR'S HOUSE":
            case "UNIVERSITY OF CATAN":
            case "CHAPEL":
            case "MARKET":
                retVal = "1 victory point!";
                break;

            case "ROAD BUILDING":
                retVal = "Place 2 new roads as if\n"
                       + "you had just built them.";
                break;

            case "MONOPOLY":
                retVal = "When you play this card, announce one type\n"
                       + "of resource. All other players must give you\n"
                       + "all their resource cards of that type.";
                break;

            case "YEAR OF PLENTY":
                retVal = "Take any 2 resource cards from the bank and\n"
                       + "add them to your hand. They can be two dif-\n"
                       + "ferent resources or two of the same resource.\n"
                       + "They may immediately be used to build.";
                break;

            case "SOLDIER":
                retVal = "Move the robber. Steal one resource card from\n"
                       + "the owner of an adjacent settlement or city.";
                break;
            
            default:
                retVal = "Unknown card";
        }
        
        return retVal;
    }
    
}
