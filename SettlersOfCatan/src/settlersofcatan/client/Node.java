package settlersofcatan.client;

public class Node {
    private int player;
    private int location;
    
    public enum Settlement { NONE, SETTLEMENT, CITY };
    Settlement settlement;
    
    public Node(int location) {
        player = -1;
        settlement = Settlement.NONE;
        this.location = location;
    }
    
    public int getLocation() {
        return location;
    }
    
    public int getPlayer() {
        return player;
    }
    
    public Settlement getSettlement() {
        return settlement;
    }
    
    public void setPlayer(int player) {
        this.player = player;
    }
    
    public void setSettlement(Settlement settlement) {
        this.settlement = settlement;
    }
}
