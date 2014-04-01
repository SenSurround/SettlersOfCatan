package org.settlersofcatan.client;

public class Path {
    private int node1;
    private int node2;
    private int player;
    
    public Path(int node1, int node2) {
        this.node1 = node1;
        this.node2 = node2;
        player = -1;
    }
    
    public int getNode1() {
        return node1;
    }
    
    public int getNode2() {
        return node2;
    }
    
    public int getPlayer() {
        return player;
    }
    
    public void setPlayer(int player) {
        this.player = player;
    }
}
