package org.settlersofcatan.graphics;

import org.settlersofcatan.client.SettlersOfCatanPresenter;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class BoardPieceMovingAnimation extends Animation{
    
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    Image element;
    HorizontalPanel board;
    AbsolutePanel ap;
    SettlersOfCatanPresenter presenter;
    int updateLoc;
    boolean isNode;
    
    public BoardPieceMovingAnimation(
            HorizontalPanel board,
            AbsolutePanel ap,
            SettlersOfCatanPresenter presenter,
            boolean isNode,
            Image element)
    {
        this.board = board;
        this.element = element;
        this.ap = ap;
        this.presenter = presenter;
        this.isNode = isNode;
    }
    
    public void setStartPoint(int x, int y)
    {
        startX = x;
        startY = y;
    }
    
    public void runAnimation(int x, int y, int updateLoc)
    {
        this.updateLoc = updateLoc;
        endX = x;
        endY = y;

        board.remove(ap);
        ap.add(element, (int)startX, (int)startY);
        board.add(ap);
        run(2000);
    }

    @Override
    protected void onUpdate(double progress) {
        board.remove(ap);
        
        double positionX = startX + (progress * (endX - startX));
        double positionY = startY + (progress * (endY - startY));

        ap.add(element, (int)positionX, (int)positionY);
        board.add(ap);
    }

    @Override
    protected void onComplete() {
        if(isNode)
            presenter.setNodeToBuild(updateLoc);
        else
            presenter.setPathToBuild(updateLoc);
    }

}
