package settlersofcatan.client;

public class Node {
    private int player;
    private int location;
    private int settlement;
    private String harborBonus;
    
    public Node(int location) {
        player = -1;
        settlement = 0;
        this.location = location;
        harborBonus = "";
    }
    
    public int getLocation() {
        return location;
    }
    
    public int getPlayer() {
        return player;
    }
    
    public int getSettlement() {
        return settlement;
    }
    
    public void setPlayer(int player) {
        this.player = player;
    }
    
    public void setSettlement(int settlement) {
        this.settlement = settlement;
    }
    
    public String getHarborBonus()
    {
        return harborBonus;
    }
    
    public void setHarborBonus(String harborBonus)
    {
        this.harborBonus = harborBonus;
    }
}
