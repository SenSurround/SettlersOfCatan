package settlersofcatan.client;

public class Hex {
    // The array that holds the index of the nodes
    // These are universal numbers for the greater board
    int[] locations = {0, 0, 0, 0, 0, 0};
    
    // The array that holds the six nodes at the corners of the HEX
    // Cities / Settlements can be placed here
    // THIS NEEDS TO BE GLOBAL AND NOT SPECIFIC TO THE HEX
    //Node[] nodes = {new Node(), new Node(), new Node(), new Node(), new Node(), new Node()};
    
    // The array that holds the six paths between the corners of the HEX
    // Roads can be placed here
    // THIS NEEDS TO BE GLOBAL AND NOT SPECIFIC TO THE HEX
    //Path[] paths = {new Path(), new Path(), new Path(), new Path(), new Path(), new Path()};
    
    // An enum to define all the possible resource types.
    // ORE, GRAIN, LUMBER, WOOL, BRICK can be collected by the player
    // DESERT is the absence of usable resources.
    public enum Resource { ORE, GRAIN, LUMBER, WOOL, BRICK, DESERT };
    
    // The type of resource produced by this HEX
    Resource resource;
    
    // The die roll value to produce a resource by this HEX
    int dieRoll = 0;
    
    // Whether or not the robber is currently camped on this HEX
    // This prevents resources from being produced.
    boolean robber;
    
    public Hex() {
    }
    
    public Hex(int loc0, int loc1, int loc2, int loc3, int loc4, int loc5, Resource resource)
    {
        // Key the global locations to the nodes attached to this HEX
        locations[0] = loc0;
        locations[1] = loc1;
        locations[2] = loc2;
        locations[3] = loc3;
        locations[4] = loc4;
        locations[5] = loc5;
        
        // Define the paths between the global locations
        //paths[0].setNodes(locations[0], locations[1]);
        //paths[1].setNodes(locations[1], locations[2]);
        //paths[2].setNodes(locations[2], locations[3]);
        //paths[3].setNodes(locations[3], locations[4]);
        //paths[4].setNodes(locations[4], locations[5]);
        //paths[5].setNodes(locations[5], locations[0]);
        
        this.resource = resource;
        
        if (resource == Resource.DESERT) {
            robber = true;
        } else {
            robber = false;
        }
    }
    
    // Sets the die roll value of the HEX to provide resources
    public void setDieRoll(int dieRoll) {
        this.dieRoll = dieRoll;
    }
    
    // Returns the resource type of the HEX
    public Resource getResource() {
        return resource;
    }

}
