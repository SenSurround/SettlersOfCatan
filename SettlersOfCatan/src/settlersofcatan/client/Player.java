/* THIS CLASS IS DEPRECATED

package settlersofcatan.client;

import java.util.Vector;

import settlersofcatan.client.Hex.Resource;

public class Player {
    public enum Color { BLUE, RED, YELLOW, GREEN };
    Color color;
    
    int ore;
    int grain;
    int lumber;
    int wool;
    int brick;
    
    int city;
    int settlement;
    int road;
    
    int soldier;
    int savedVictoryPoint;
    
    Vector<String> developmentCards;
    
    public Player(int playerNum) {
        switch(playerNum) {
            case 0:
                color = Color.BLUE;
                break;
            case 1:
                color = Color.RED;
                break;
            case 2:
                color = Color.YELLOW;
                break;
            case 3:
                color = Color.GREEN;
                break;
            default:
                color = Color.BLUE;
                break;
        }
        
        ore = 0;
        grain = 0;
        lumber = 0;
        wool = 0;
        brick = 0;
        
        city = 4;
        settlement = 5;
        road = 15;
        
        soldier = 0;
        savedVictoryPoint = 0;
        
        developmentCards.clear();
    }
    
    public int getResourceCount(Resource resource) {
        int retVal = -1;
        
        switch (resource) {
            case ORE:
                retVal = ore;
                break;
            case GRAIN:
                retVal = grain;
                break;
            case LUMBER:
                retVal = lumber;
                break;
            case WOOL:
                retVal = wool;
                break;
            case BRICK:
                retVal = brick;
                break;
            default:
                break;
        }
        
        return retVal;
    }
    
    public int getSettlementCount() {
        return settlement;
    }
    
    public int getCityCount() {
        return city;
    }
    
    public int getRoadCount() {
        return road;
    }
}
*/