package settlersofcatan.graphics;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;

public class BoardPieceMovingAnimation extends Animation{
    
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    Element element;
    
    public BoardPieceMovingAnimation(Element element)
    {
        this.element = element;
    }
    
    public void setStartPoint(int x, int y)
    {
        startX = x;
        startY = y;
    }
    
    public void runAnimation(int x, int y)
    {
        endX = x;
        endY = y;
        
        run(10000);
    }

    @Override
    protected void onUpdate(double progress) {
        double positionX = startX + (progress * (endX - startX));
        double positionY = startY + (progress * (endY - startY));
 
        this.element.getStyle().setLeft(positionX, Unit.PX);
        this.element.getStyle().setTop(positionY, Unit.PX);
        
    }

}
