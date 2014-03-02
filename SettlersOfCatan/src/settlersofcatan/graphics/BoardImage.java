package settlersofcatan.graphics;

import java.util.List;

import settlersofcatan.client.Board;
import settlersofcatan.client.Hex;
import settlersofcatan.client.Node;
import settlersofcatan.client.Path;

import com.google.gwt.user.client.ui.Image;

public class BoardImage {
    
    List<Hex> hexList;
    List<Node> nodeList;
    List<Path> pathList;
    
    public BoardImage(List<Hex> hexList, List<Node> nodeList, List<Path> pathList) {
        this.hexList = hexList;
        this.nodeList = nodeList;
        this.pathList = pathList;
    }
    
    //public

}
