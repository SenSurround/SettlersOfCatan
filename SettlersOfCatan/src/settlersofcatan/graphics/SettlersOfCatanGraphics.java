package settlersofcatan.graphics;

import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import settlersofcatan.client.Constants;
import settlersofcatan.client.Hex;
import settlersofcatan.client.Node;
import settlersofcatan.client.Path;
import settlersofcatan.client.SettlersOfCatanPresenter;

import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class SettlersOfCatanGraphics extends Composite implements SettlersOfCatanPresenter.View {
    public interface SettlersOfCatanGraphicsUiBinder extends UiBinder<Widget, SettlersOfCatanGraphics> {
    }
    
    //@UiField
    //VerticalPanel playerArea;

    @UiField
    HorizontalPanel boardArea;
    
    @UiField
    HorizontalPanel playerResourceCardArea;
    
    @UiField
    HorizontalPanel playerDevelopmentCardArea;

    private final BoardImageSupplier boardImageSupplier;
    private SettlersOfCatanPresenter presenter;
    private boolean choosePathEnabled;
    private boolean chooseNodeEnabled;

    public SettlersOfCatanGraphics() {
        choosePathEnabled = false;
        BoardImages boardImages = GWT.create(BoardImages.class);
        this.boardImageSupplier = new BoardImageSupplier(boardImages);
        SettlersOfCatanGraphicsUiBinder uiBinder = GWT.create(SettlersOfCatanGraphicsUiBinder.class);
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    private void createBoard(List<Hex> hexList, List<Node> nodeList, List<Path> pathList)
    {
        boardArea.clear();
        AbsolutePanel ap = new AbsolutePanel();
        ap.setHeight("800px");
        ap.setWidth("1000px");
        ap = createHexImage(hexList, ap);
        ap = createNodeImage(nodeList, ap);
        ap = createPathImage(pathList, ap);
        
        boardArea.add(ap);
    }
    
    // Creates the main board area with the HEX
    // These are non interactive
    private AbsolutePanel createHexImage(List<Hex> hexList, AbsolutePanel ap)
    {
        
        Image hexImage0 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(0).getResource()));
        ap.add(hexImage0, 200, 100);
        
        Image hexImage1 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(1).getResource()));
        ap.add(hexImage1, 350, 100);
        
        Image hexImage2 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(2).getResource()));
        ap.add(hexImage2, 500, 100);
        
        Image hexImage3 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(3).getResource()));
        ap.add(hexImage3, 125, 211);
        
        Image hexImage4 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(4).getResource()));
        ap.add(hexImage4, 275, 211);
        
        Image hexImage5 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(5).getResource()));
        ap.add(hexImage5, 425, 211);
        
        Image hexImage6 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(6).getResource()));
        ap.add(hexImage6, 575, 211);
        
        Image hexImage7 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(7).getResource()));
        ap.add(hexImage7, 50, 322);
        
        Image hexImage8 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(8).getResource()));
        ap.add(hexImage8, 200, 322);
        
        Image hexImage9 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(9).getResource()));
        ap.add(hexImage9, 350, 322);
        
        Image hexImage10 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(10).getResource()));
        ap.add(hexImage10, 500, 322);
        
        Image hexImage11 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(11).getResource()));
        ap.add(hexImage11, 650, 322);
        
        Image hexImage12 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(12).getResource()));
        ap.add(hexImage12, 125, 433);
        
        Image hexImage13 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(13).getResource()));
        ap.add(hexImage13, 275, 433);
        
        Image hexImage14 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(14).getResource()));
        ap.add(hexImage14, 425, 433);
        
        Image hexImage15 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(15).getResource()));
        ap.add(hexImage15, 575, 433);
        
        Image hexImage16 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(16).getResource()));
        ap.add(hexImage16, 200, 544);
        
        Image hexImage17 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(17).getResource()));
        ap.add(hexImage17, 350, 544);
        
        Image hexImage18 = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(18).getResource()));
        ap.add(hexImage18, 500, 544);
        
        if(hexList.get(0).getRobber())
        {
            Image dieRollImage0 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage0, 255, 155);
        }
        else if(!hexList.get(0).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage0 = new Image(boardImageSupplier.getDieRollToken(hexList.get(0).getDieRoll()));
            ap.add(dieRollImage0, 255, 155);
        }

        if(hexList.get(1).getRobber())
        {
            Image dieRollImage1 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage1, 405, 155);
        }
        else if(!hexList.get(1).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage1 = new Image(boardImageSupplier.getDieRollToken(hexList.get(1).getDieRoll()));
            ap.add(dieRollImage1, 405, 155);
        }

        if(hexList.get(2).getRobber())
        {
            Image dieRollImage2 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage2, 555, 155);
        }
        else if(!hexList.get(2).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage2 = new Image(boardImageSupplier.getDieRollToken(hexList.get(2).getDieRoll()));
            ap.add(dieRollImage2, 555, 155);
        }

        if(hexList.get(3).getRobber())
        {
            Image dieRollImage3 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage3, 180, 266);
        }
        else if(!hexList.get(3).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage3 = new Image(boardImageSupplier.getDieRollToken(hexList.get(3).getDieRoll()));
            ap.add(dieRollImage3, 180, 266);
        }

        if(hexList.get(4).getRobber())
        {
            Image dieRollImage4 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage4, 330, 266);
        }
        else if(!hexList.get(4).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage4 = new Image(boardImageSupplier.getDieRollToken(hexList.get(4).getDieRoll()));
            ap.add(dieRollImage4, 330, 266);
        }

        if(hexList.get(5).getRobber())
        {
            Image dieRollImage5 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage5, 480, 266);
        }
        else if(!hexList.get(5).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage5 = new Image(boardImageSupplier.getDieRollToken(hexList.get(5).getDieRoll()));
            ap.add(dieRollImage5, 480, 266);
        }

        if(hexList.get(6).getRobber())
        {
            Image dieRollImage6 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage6, 630, 266);
        }
        else if(!hexList.get(6).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage6 = new Image(boardImageSupplier.getDieRollToken(hexList.get(6).getDieRoll()));
            ap.add(dieRollImage6, 630, 266);
        }

        if(hexList.get(7).getRobber())
        {
            Image dieRollImage7 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage7, 105, 377);
        }
        else if(!hexList.get(7).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage7 = new Image(boardImageSupplier.getDieRollToken(hexList.get(7).getDieRoll()));
            ap.add(dieRollImage7, 105, 377);
        }

        if(hexList.get(8).getRobber())
        {
            Image dieRollImage8 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage8, 255, 377);
        }
        else if(!hexList.get(8).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage8 = new Image(boardImageSupplier.getDieRollToken(hexList.get(8).getDieRoll()));
            ap.add(dieRollImage8, 255, 377);
        }

        if(hexList.get(9).getRobber())
        {
            Image dieRollImage9 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage9, 405, 377);
        }
        else if(!hexList.get(9).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage9 = new Image(boardImageSupplier.getDieRollToken(hexList.get(9).getDieRoll()));
            ap.add(dieRollImage9, 405, 377);
        }

        if(hexList.get(10).getRobber())
        {
            Image dieRollImage10 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage10, 555, 377);
        }
        else if(!hexList.get(10).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage10 = new Image(boardImageSupplier.getDieRollToken(hexList.get(10).getDieRoll()));
            ap.add(dieRollImage10, 555, 377);
        }

        if(hexList.get(11).getRobber())
        {
            Image dieRollImage11 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage11, 705, 377);
        }
        else if(!hexList.get(11).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage11 = new Image(boardImageSupplier.getDieRollToken(hexList.get(11).getDieRoll()));
            ap.add(dieRollImage11, 705, 377);
        }

        if(hexList.get(12).getRobber())
        {
            Image dieRollImage12 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage12, 180, 488);
        }
        else if(!hexList.get(12).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage12 = new Image(boardImageSupplier.getDieRollToken(hexList.get(12).getDieRoll()));
            ap.add(dieRollImage12, 180, 488);
        }

        if(hexList.get(13).getRobber())
        {
            Image dieRollImage13 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage13, 330, 488);
        }
        else if(!hexList.get(13).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage13 = new Image(boardImageSupplier.getDieRollToken(hexList.get(13).getDieRoll()));
            ap.add(dieRollImage13, 330, 488);
        }

        if(hexList.get(14).getRobber())
        {
            Image dieRollImage14 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage14, 480, 488);
        }
        else if(!hexList.get(14).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage14 = new Image(boardImageSupplier.getDieRollToken(hexList.get(14).getDieRoll()));
            ap.add(dieRollImage14, 480, 488);
        }

        if(hexList.get(15).getRobber())
        {
            Image dieRollImage15 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage15, 630, 488);
        }
        else if(!hexList.get(15).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage15 = new Image(boardImageSupplier.getDieRollToken(hexList.get(15).getDieRoll()));
            ap.add(dieRollImage15, 630, 488);
        }

        if(hexList.get(16).getRobber())
        {
            Image dieRollImage16 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage16, 255, 599);
        }
        else if(!hexList.get(16).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage16 = new Image(boardImageSupplier.getDieRollToken(hexList.get(16).getDieRoll()));
            ap.add(dieRollImage16, 255, 599);
        }

        if(hexList.get(17).getRobber())
        {
            Image dieRollImage17 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage17, 405, 599);
        }
        else if(!hexList.get(17).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage17 = new Image(boardImageSupplier.getDieRollToken(hexList.get(17).getDieRoll()));
            ap.add(dieRollImage17, 405, 599);
        }

        if(hexList.get(18).getRobber())
        {
            Image dieRollImage18 = new Image(boardImageSupplier.getDieRollToken(0));
            ap.add(dieRollImage18, 555, 599);
        }
        else if(!hexList.get(18).getResource().equals(Constants.DESERT))
        {
            Image dieRollImage18 = new Image(boardImageSupplier.getDieRollToken(hexList.get(18).getDieRoll()));
            ap.add(dieRollImage18, 555, 599);
        }
        
        Image harborLines;
        
        harborLines = new Image(boardImageSupplier.getHarborLines(0));
        ap.add(harborLines, 190, 70);
        
        harborLines = new Image(boardImageSupplier.getHarborLines(1));
        ap.add(harborLines, 450, 70);
        
        harborLines = new Image(boardImageSupplier.getHarborLines(2));
        ap.add(harborLines, 675, 181);
        
        harborLines = new Image(boardImageSupplier.getHarborLines(3));
        ap.add(harborLines, 820, 360);
        
        harborLines = new Image(boardImageSupplier.getHarborLines(4));
        ap.add(harborLines, 675, 569);
        
        harborLines = new Image(boardImageSupplier.getHarborLines(5));
        ap.add(harborLines, 450, 680);
        
        harborLines = new Image(boardImageSupplier.getHarborLines(6));
        ap.add(harborLines, 190, 680);
        
        harborLines = new Image(boardImageSupplier.getHarborLines(7));
        ap.add(harborLines, 70, 475);
        
        harborLines = new Image(boardImageSupplier.getHarborLines(8));
        ap.add(harborLines, 70, 255);
        
        return ap;
    }
    
    private AbsolutePanel createNodeImage(List<Node> nodeList, AbsolutePanel ap)
    {
        Image nodeImage;
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(0).getPlayer(),
                nodeList.get(0).getSettlement()));
        if(nodeList.get(0).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(0);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 80);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(1).getPlayer(),
                nodeList.get(1).getSettlement()));
        if(nodeList.get(1).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(1);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 80);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(2).getPlayer(),
                nodeList.get(2).getSettlement()));
        if(nodeList.get(2).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(2);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 80);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(3).getPlayer(),
                nodeList.get(3).getSettlement()));
        if(nodeList.get(3).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(3);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 119);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(4).getPlayer(),
                nodeList.get(4).getSettlement()));
        if(nodeList.get(4).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(4);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 119);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(5).getPlayer(),
                nodeList.get(5).getSettlement()));
        if(nodeList.get(5).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(5);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 119);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(6).getPlayer(),
                nodeList.get(6).getSettlement()));
        if(nodeList.get(6).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(6);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 119);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(7).getPlayer(),
                nodeList.get(7).getSettlement()));
        if(nodeList.get(7).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(7);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 191);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(8).getPlayer(),
                nodeList.get(8).getSettlement()));
        if(nodeList.get(8).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(8);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 191);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(9).getPlayer(),
                nodeList.get(9).getSettlement()));
        if(nodeList.get(9).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(9);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 191);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(10).getPlayer(),
                nodeList.get(10).getSettlement()));
        if(nodeList.get(10).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(10);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 191);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(11).getPlayer(),
                nodeList.get(11).getSettlement()));
        if(nodeList.get(11).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(11);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 105, 230);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(12).getPlayer(),
                nodeList.get(12).getSettlement()));
        if(nodeList.get(12).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(12);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 230);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(13).getPlayer(),
                nodeList.get(13).getSettlement()));
        if(nodeList.get(13).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(13);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 230);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(14).getPlayer(),
                nodeList.get(14).getSettlement()));
        if(nodeList.get(14).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(14);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 230);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(15).getPlayer(),
                nodeList.get(15).getSettlement()));
        if(nodeList.get(15).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(15);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 705, 230);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(16).getPlayer(),
                nodeList.get(16).getSettlement()));
        if(nodeList.get(16).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(16);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 105, 302);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(17).getPlayer(),
                nodeList.get(17).getSettlement()));
        if(nodeList.get(17).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(17);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 302);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(18).getPlayer(),
                nodeList.get(18).getSettlement()));
        if(nodeList.get(18).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(18);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 302);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(19).getPlayer(),
                nodeList.get(19).getSettlement()));
        if(nodeList.get(19).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(19);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 302);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(20).getPlayer(),
                nodeList.get(20).getSettlement()));
        if(nodeList.get(20).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(20);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 705, 302);
        

        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(21).getPlayer(),
                nodeList.get(21).getSettlement()));
        if(nodeList.get(21).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(21);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 30, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(22).getPlayer(),
                nodeList.get(22).getSettlement()));
        if(nodeList.get(22).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(22);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(23).getPlayer(),
                nodeList.get(23).getSettlement()));
        if(nodeList.get(23).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(23);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(24).getPlayer(),
                nodeList.get(24).getSettlement()));
        if(nodeList.get(24).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(24);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(25).getPlayer(),
                nodeList.get(25).getSettlement()));
        if(nodeList.get(25).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(25);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(26).getPlayer(),
                nodeList.get(26).getSettlement()));
        if(nodeList.get(26).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(26);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 780, 341);
        

        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(27).getPlayer(),
                nodeList.get(27).getSettlement()));
        if(nodeList.get(27).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(27);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 30, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(28).getPlayer(),
                nodeList.get(28).getSettlement()));
        if(nodeList.get(28).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(28);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(29).getPlayer(),
                nodeList.get(29).getSettlement()));
        if(nodeList.get(29).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(29);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(30).getPlayer(),
                nodeList.get(30).getSettlement()));
        if(nodeList.get(30).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(30);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(31).getPlayer(),
                nodeList.get(31).getSettlement()));
        if(nodeList.get(31).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(31);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(32).getPlayer(),
                nodeList.get(32).getSettlement()));
        if(nodeList.get(32).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(32);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 780, 413);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(33).getPlayer(),
                nodeList.get(33).getSettlement()));
        if(nodeList.get(33).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(33);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 105, 452);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(34).getPlayer(),
                nodeList.get(34).getSettlement()));
        if(nodeList.get(34).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(34);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 452);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(35).getPlayer(),
                nodeList.get(35).getSettlement()));
        if(nodeList.get(35).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(35);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 452);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(36).getPlayer(),
                nodeList.get(36).getSettlement()));
        if(nodeList.get(36).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(36);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 452);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(37).getPlayer(),
                nodeList.get(37).getSettlement()));
        if(nodeList.get(37).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(37);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 705, 452);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(38).getPlayer(),
                nodeList.get(38).getSettlement()));
        if(nodeList.get(38).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(38);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 105, 524);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(39).getPlayer(),
                nodeList.get(39).getSettlement()));
        if(nodeList.get(39).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(39);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 524);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(40).getPlayer(),
                nodeList.get(40).getSettlement()));
        if(nodeList.get(40).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(40);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 524);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(41).getPlayer(),
                nodeList.get(41).getSettlement()));
        if(nodeList.get(41).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(41);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 524);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(42).getPlayer(),
                nodeList.get(42).getSettlement()));
        if(nodeList.get(42).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(42);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 705, 524);
        

        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(43).getPlayer(),
                nodeList.get(43).getSettlement()));
        if(nodeList.get(43).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(43);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 563);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(44).getPlayer(),
                nodeList.get(44).getSettlement()));
        if(nodeList.get(44).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(44);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 563);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(45).getPlayer(),
                nodeList.get(45).getSettlement()));
        if(nodeList.get(45).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(45);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 563);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(46).getPlayer(),
                nodeList.get(46).getSettlement()));
        if(nodeList.get(46).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(46);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 563);
        

        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(47).getPlayer(),
                nodeList.get(47).getSettlement()));
        if(nodeList.get(47).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(47);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 635);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(48).getPlayer(),
                nodeList.get(48).getSettlement()));
        if(nodeList.get(48).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(48);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 635);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(49).getPlayer(),
                nodeList.get(49).getSettlement()));
        if(nodeList.get(49).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(49);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 635);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(50).getPlayer(),
                nodeList.get(50).getSettlement()));
        if(nodeList.get(50).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(50);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 635);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(51).getPlayer(),
                nodeList.get(51).getSettlement()));
        if(nodeList.get(51).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(51);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 674);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(52).getPlayer(),
                nodeList.get(52).getSettlement()));
        if(nodeList.get(52).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(52);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 674);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(53).getPlayer(),
                nodeList.get(53).getSettlement()));
        if(nodeList.get(53).getPlayer() == -1)
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                            presenter.setNodeToBuild(53);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 674);
        
        Image harbor;
        
        harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(0).getHarborBonus()));
        ap.add(harbor, 175, 35);
        
        harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(1).getHarborBonus()));
        ap.add(harbor, 475, 35);
        
        harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(10).getHarborBonus()));
        ap.add(harbor, 700, 146);
        
        harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(26).getHarborBonus()));
        ap.add(harbor, 855, 370);
        
        harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(42).getHarborBonus()));
        ap.add(harbor, 700, 604);
        
        harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(49).getHarborBonus()));
        ap.add(harbor, 475, 715);
        
        harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(47).getHarborBonus()));
        ap.add(harbor, 175, 715);
        
        harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(33).getHarborBonus()));
        ap.add(harbor, 20, 490);
        
        harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(11).getHarborBonus()));
        ap.add(harbor, 20, 270);
        
        return ap;
    }
    
    private AbsolutePanel createPathImage(List<Path> pathList, AbsolutePanel ap)
    {
        Image pathImage;

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(0).getPlayer(),
                0));
        if(pathList.get(0).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(0);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 104);
        

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(1).getPlayer(),
                1));
        if(pathList.get(1).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(1);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 104);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(2).getPlayer(),
                2));
        if(pathList.get(2).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(2);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 104);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(3).getPlayer(),
                3));
        if(pathList.get(3).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(3);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 104);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(4).getPlayer(),
                4));
        if(pathList.get(4).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(4);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 104);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(5).getPlayer(),
                5));
        if(pathList.get(5).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(5);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 104);
        
        
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(6).getPlayer(),
                6));
        if(pathList.get(6).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(6);
                          }
                        }
                      });
        }
        ap.add(pathImage, 190, 160);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(7).getPlayer(),
                7));
        if(pathList.get(7).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(7);
                          }
                        }
                      });
        }
        ap.add(pathImage, 340, 160);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(8).getPlayer(),
                8));
        if(pathList.get(8).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(8);
                          }
                        }
                      });
        }
        ap.add(pathImage, 490, 160);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(9).getPlayer(),
                9));
        if(pathList.get(9).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(9);
                          }
                        }
                      });
        }
        ap.add(pathImage, 640, 160);
        


        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(10).getPlayer(),
                10));
        if(pathList.get(10).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(10);
                          }
                        }
                      });
        }
        ap.add(pathImage, 142, 215);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(11).getPlayer(),
                11));
        if(pathList.get(11).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(11);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 215);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(12).getPlayer(),
                12));
        if(pathList.get(12).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(12);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 215);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(13).getPlayer(),
                13));
        if(pathList.get(13).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(13);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 215);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(14).getPlayer(),
                14));
        if(pathList.get(14).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(14);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 215);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(15).getPlayer(),
                15));
        if(pathList.get(15).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(15);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 215);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(16).getPlayer(),
                16));
        if(pathList.get(16).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(16);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 215);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(17).getPlayer(),
                17));
        if(pathList.get(17).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(17);
                          }
                        }
                      });
        }
        ap.add(pathImage, 667, 215);
        

        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(18).getPlayer(),
                18));
        if(pathList.get(18).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(18);
                          }
                        }
                      });
        }
        ap.add(pathImage, 115, 271);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(19).getPlayer(),
                19));
        if(pathList.get(19).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(19);
                          }
                        }
                      });
        }
        ap.add(pathImage, 265, 271);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(20).getPlayer(),
                20));
        if(pathList.get(20).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(20);
                          }
                        }
                      });
        }
        ap.add(pathImage, 415, 271);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(21).getPlayer(),
                21));
        if(pathList.get(21).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(21);
                          }
                        }
                      });
        }
        ap.add(pathImage, 565, 271);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(22).getPlayer(),
                22));
        if(pathList.get(22).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(22);
                          }
                        }
                      });
        }
        ap.add(pathImage, 715, 271);
        


        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(23).getPlayer(),
                23));
        if(pathList.get(23).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(23);
                          }
                        }
                      });
        }
        ap.add(pathImage, 67, 326);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(24).getPlayer(),
                24));
        if(pathList.get(24).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(24);
                          }
                        }
                      });
        }
        ap.add(pathImage, 142, 326);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(25).getPlayer(),
                25));
        if(pathList.get(25).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(25);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 326);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(26).getPlayer(),
                26));
        if(pathList.get(26).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(26);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 326);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(27).getPlayer(),
                27));
        if(pathList.get(27).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(27);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(28).getPlayer(),
                28));
        if(pathList.get(28).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(28);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(29).getPlayer(),
                29));
        if(pathList.get(29).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(29);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(30).getPlayer(),
                30));
        if(pathList.get(30).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(30);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(31).getPlayer(),
                31));
        if(pathList.get(31).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(31);
                          }
                        }
                      });
        }
        ap.add(pathImage, 667, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(32).getPlayer(),
                32));
        if(pathList.get(32).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(32);
                          }
                        }
                      });
        }
        ap.add(pathImage, 742, 326);
        

        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(33).getPlayer(),
                33));
        if(pathList.get(33).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(33);
                          }
                        }
                      });
        }
        ap.add(pathImage, 40, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(34).getPlayer(),
                34));
        if(pathList.get(34).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(34);
                          }
                        }
                      });
        }
        ap.add(pathImage, 190, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(35).getPlayer(),
                35));
        if(pathList.get(35).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(35);
                          }
                        }
                      });
        }
        ap.add(pathImage, 340, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(36).getPlayer(),
                36));
        if(pathList.get(36).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(36);
                          }
                        }
                      });
        }
        ap.add(pathImage, 490, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(37).getPlayer(),
                37));
        if(pathList.get(37).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(37);
                          }
                        }
                      });
        }
        ap.add(pathImage, 640, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(38).getPlayer(),
                38));
        if(pathList.get(38).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(38);
                          }
                        }
                      });
        }
        ap.add(pathImage, 790, 382);
        


        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(39).getPlayer(),
                39));
        if(pathList.get(39).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(39);
                          }
                        }
                      });
        }
        ap.add(pathImage, 67, 437);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(40).getPlayer(),
                40));
        if(pathList.get(40).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(40);
                          }
                        }
                      });
        }
        ap.add(pathImage, 142, 437);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(41).getPlayer(),
                41));
        if(pathList.get(41).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(41);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 437);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(42).getPlayer(),
                42));
        if(pathList.get(42).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(42);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 437);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(43).getPlayer(),
                43));
        if(pathList.get(43).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(43);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(44).getPlayer(),
                44));
        if(pathList.get(44).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(44);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(45).getPlayer(),
                45));
        if(pathList.get(45).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(45);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(46).getPlayer(),
                46));
        if(pathList.get(46).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(46);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(47).getPlayer(),
                47));
        if(pathList.get(47).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(47);
                          }
                        }
                      });
        }
        ap.add(pathImage, 667, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(48).getPlayer(),
                48));
        if(pathList.get(48).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(48);
                          }
                        }
                      });
        }
        ap.add(pathImage, 742, 437);
        

        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(49).getPlayer(),
                49));
        if(pathList.get(49).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(49);
                          }
                        }
                      });
        }
        ap.add(pathImage, 115, 493);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(50).getPlayer(),
                50));
        if(pathList.get(50).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(50);
                          }
                        }
                      });
        }
        ap.add(pathImage, 265, 493);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(51).getPlayer(),
                51));
        if(pathList.get(51).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(51);
                          }
                        }
                      });
        }
        ap.add(pathImage, 415, 493);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(52).getPlayer(),
                52));
        if(pathList.get(52).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(52);
                          }
                        }
                      });
        }
        ap.add(pathImage, 565, 493);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(53).getPlayer(),
                53));
        if(pathList.get(53).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(53);
                          }
                        }
                      });
        }
        ap.add(pathImage, 715, 493);
        


        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(54).getPlayer(),
                54));
        if(pathList.get(54).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(54);
                          }
                        }
                      });
        }
        ap.add(pathImage, 142, 548);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(55).getPlayer(),
                55));
        if(pathList.get(55).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(55);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 548);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(56).getPlayer(),
                56));
        if(pathList.get(56).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(56);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 548);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(57).getPlayer(),
                57));
        if(pathList.get(57).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(57);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 548);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(58).getPlayer(),
                58));
        if(pathList.get(58).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(58);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 548);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(59).getPlayer(),
                59));
        if(pathList.get(59).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(59);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 548);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(60).getPlayer(),
                60));
        if(pathList.get(60).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(60);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 548);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(61).getPlayer(),
                61));
        if(pathList.get(61).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(61);
                          }
                        }
                      });
        }
        ap.add(pathImage, 667, 548);
        
        
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(62).getPlayer(),
                62));
        if(pathList.get(62).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(62);
                          }
                        }
                      });
        }
        ap.add(pathImage, 190, 604);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(63).getPlayer(),
                63));
        if(pathList.get(63).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(63);
                          }
                        }
                      });
        }
        ap.add(pathImage, 340, 604);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(64).getPlayer(),
                64));
        if(pathList.get(64).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(64);
                          }
                        }
                      });
        }
        ap.add(pathImage, 490, 604);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(65).getPlayer(),
                65));
        if(pathList.get(65).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(65);
                          }
                        }
                      });
        }
        ap.add(pathImage, 640, 604);
        
        

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(66).getPlayer(),
                66));
        if(pathList.get(66).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(66);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 659);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(67).getPlayer(),
                67));
        if(pathList.get(67).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(67);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 659);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(68).getPlayer(),
                68));
        if(pathList.get(68).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(68);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 659);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(69).getPlayer(),
                69));
        if(pathList.get(69).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(69);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 659);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(70).getPlayer(),
                70));
        if(pathList.get(70).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(70);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 659);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(71).getPlayer(),
                71));
        if(pathList.get(71).getPlayer() == -1)
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                            presenter.setPathToBuild(71);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 659);
        
        return ap;
    }
    
    private void placeImages(HorizontalPanel panel, List<Image> images) {
        panel.clear();
        Image last = images.isEmpty() ? null : images.get(images.size() - 1);
        for (Image image : images) {
            FlowPanel imageContainer = new FlowPanel();
            imageContainer.setStyleName(image != last ? "imgShortContainer" : "imgContainer");
            imageContainer.add(image);
            panel.add(imageContainer);
        }
    }

    @Override
    public void setPresenter(SettlersOfCatanPresenter settlersOfCatanPresenter) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setViewerState(List<Hex> hexList, List<Node> nodeList,
            List<Path> pathList) {
        createBoard(hexList, nodeList, pathList);
        
    }

    @Override
    public void setPlayerState(List<Hex> hexList, List<Node> nodeList,
            List<Path> pathList, List<String> resourceCards,
            List<String> developmentCards, int victoryPoints,
            boolean hasLongestRoad, boolean hasLargestArmy) {
        createBoard(hexList, nodeList, pathList);
    }

    @Override
    public void makeMove(String moveType) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void chooseCards(List<String> selectedCards,
            List<String> remainingCards) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void chooseDevelopmentCard(String selectedCard) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void twoStepValidation() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endTurn(int nextPlayer) {
        // TODO Auto-generated method stub
        
    }
}