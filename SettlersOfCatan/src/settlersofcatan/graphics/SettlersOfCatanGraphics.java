package settlersofcatan.graphics;

import java.util.ArrayList;
import java.util.List;

import settlersofcatan.client.Constants;
import settlersofcatan.client.GameApi.Operation;
import settlersofcatan.client.Hex;
import settlersofcatan.client.Node;
import settlersofcatan.client.Path;
import settlersofcatan.client.SettlersOfCatanPresenter;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class SettlersOfCatanGraphics extends Composite implements SettlersOfCatanPresenter.View {
    public interface SettlersOfCatanGraphicsUiBinder extends UiBinder<Widget, SettlersOfCatanGraphics> {
    }

    @UiField
    HorizontalPanel boardArea;
    
    @UiField
    HorizontalPanel playerResourceCardArea;
    
    @UiField
    HorizontalPanel playerDevelopmentCardArea;
    
    @UiField
    HorizontalPanel infoArea;

    private final BoardImageSupplier boardImageSupplier;
    private SettlersOfCatanPresenter presenter;
    private boolean chooseHexEnabled;
    private boolean choosePathEnabled;
    private boolean chooseNodeEnabled;
    private boolean chooseResourceCardEnabled;
    private boolean chooseDevelopmentCardEnabled;
    private boolean myTurn;

    public SettlersOfCatanGraphics() {
        choosePathEnabled = false;
        BoardImages boardImages = GWT.create(BoardImages.class);
        this.boardImageSupplier = new BoardImageSupplier(boardImages);
        SettlersOfCatanGraphicsUiBinder uiBinder = GWT.create(SettlersOfCatanGraphicsUiBinder.class);
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    private void createBoard(
            List<Hex> hexList,
            List<Node> nodeList,
            List<Path> pathList,
            int victoryPoints,
            boolean hasLongestRoad,
            boolean hasLargestArmy)
    {
        boardArea.clear();
        AbsolutePanel ap = new AbsolutePanel();
        ap.setHeight("800px");
        ap.setWidth("1200px");
        ap = createHexImage(hexList, ap);
        ap = createNodeImage(nodeList, ap);
        ap = createPathImage(pathList, ap);
        
        // Draw road/city/settlement cache
        if(presenter.myPlayer > -1 || presenter.myPlayer < 4)
        {
            Image settlementImage = new Image(boardImageSupplier.getNodeToken(
                    presenter.myPlayer,
                    1));
            
            settlementImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              // animation plus drag and drop
                          }
                        }
                      });
            
            Label sc = new Label(" x " + presenter.getBoard().numAvailableSettlements(presenter.myPlayer));
            
            ap.add(settlementImage, 20,40);
            ap.add(sc, 70, 52);
        }
        
        if(victoryPoints != -1)
        {
            Label vp = new Label("Victory Points = " + victoryPoints);
            ap.add(vp, 1020, 5);
        }
        
        if(hasLongestRoad)
        {
            Image lr = new Image(boardImageSupplier.getLongestRoad());
            ap.add(lr, 1020, 105);
        }
        
        if(hasLargestArmy)
        {
            Image la = new Image(boardImageSupplier.getLargestArmy());
            ap.add(la, 1020, 405);
        }
        
        boardArea.add(ap);
    }
    
    // Creates the main board area with the HEX
    // These are non interactive
    private AbsolutePanel createHexImage(List<Hex> hexList, AbsolutePanel ap)
    {
        final List<Hex> hexCheck = new ArrayList<Hex>(hexList); 
        
        Image hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(0).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(0).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(0));
              }
            }
          });
        ap.add(hexImage, 200, 100);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(1).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(1).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(1));
              }
            }
          });
        ap.add(hexImage, 350, 100);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(2).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(2).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(2));
              }
            }
          });
        ap.add(hexImage, 500, 100);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(3).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(3).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(3));
              }
            }
          });
        ap.add(hexImage, 125, 211);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(4).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(4).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(4));
              }
            }
          });
        ap.add(hexImage, 275, 211);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(5).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(5).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(5));
              }
            }
          });
        ap.add(hexImage, 425, 211);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(6).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(6).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(6));
              }
            }
          });
        ap.add(hexImage, 575, 211);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(7).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(7).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(7));
              }
            }
          });
        ap.add(hexImage, 50, 322);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(8).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(8).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(8));
              }
            }
          });
        ap.add(hexImage, 200, 322);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(9).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(9).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(9));
              }
            }
          });
        ap.add(hexImage, 350, 322);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(10).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(10).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(10));
              }
            }
          });
        ap.add(hexImage, 500, 322);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(11).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(11).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(11));
              }
            }
          });
        ap.add(hexImage, 650, 322);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(12).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(12).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(12));
              }
            }
          });
        ap.add(hexImage, 125, 433);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(13).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(13).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(13));
              }
            }
          });
        ap.add(hexImage, 275, 433);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(14).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(14).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(14));
              }
            }
          });
        ap.add(hexImage, 425, 433);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(15).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(15).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(15));
              }
            }
          });
        ap.add(hexImage, 575, 433);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(16).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(16).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(16));
              }
            }
          });
        ap.add(hexImage, 200, 544);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(17).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(17).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(17));
              }
            }
          });
        ap.add(hexImage, 350, 544);
        
        hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(18).getResource()));
        hexImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
              if (chooseHexEnabled && !hexCheck.get(18).getRobber()) {
                  chooseHexEnabled = false;
                  createInfoArea(presenter.setRobber(18));
              }
            }
          });
        ap.add(hexImage, 500, 544);
        
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
        if(nodeList.get(0).getPlayer() == -1 && (presenter.canPlaceSettlement(0) || presenter.canPlaceCity(0)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(0);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 80);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(1).getPlayer(),
                nodeList.get(1).getSettlement()));
        if(nodeList.get(1).getPlayer() == -1 && (presenter.canPlaceSettlement(1) || presenter.canPlaceCity(1)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(1);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 80);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(2).getPlayer(),
                nodeList.get(2).getSettlement()));
        if(nodeList.get(2).getPlayer() == -1 && (presenter.canPlaceSettlement(2) || presenter.canPlaceCity(2)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(2);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 80);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(3).getPlayer(),
                nodeList.get(3).getSettlement()));
        if(nodeList.get(3).getPlayer() == -1 && (presenter.canPlaceSettlement(3) || presenter.canPlaceCity(3)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(3);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 119);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(4).getPlayer(),
                nodeList.get(4).getSettlement()));
        if(nodeList.get(4).getPlayer() == -1 && (presenter.canPlaceSettlement(4) || presenter.canPlaceCity(4)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(4);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 119);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(5).getPlayer(),
                nodeList.get(5).getSettlement()));
        if(nodeList.get(5).getPlayer() == -1 && (presenter.canPlaceSettlement(5) || presenter.canPlaceCity(5)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(5);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 119);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(6).getPlayer(),
                nodeList.get(6).getSettlement()));
        if(nodeList.get(6).getPlayer() == -1 && (presenter.canPlaceSettlement(6) || presenter.canPlaceCity(6)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(6);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 119);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(7).getPlayer(),
                nodeList.get(7).getSettlement()));
        if(nodeList.get(7).getPlayer() == -1 && (presenter.canPlaceSettlement(7) || presenter.canPlaceCity(7)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(7);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 191);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(8).getPlayer(),
                nodeList.get(8).getSettlement()));
        if(nodeList.get(8).getPlayer() == -1 && (presenter.canPlaceSettlement(8) || presenter.canPlaceCity(8)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(8);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 191);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(9).getPlayer(),
                nodeList.get(9).getSettlement()));
        if(nodeList.get(9).getPlayer() == -1 && (presenter.canPlaceSettlement(9) || presenter.canPlaceCity(9)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(9);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 191);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(10).getPlayer(),
                nodeList.get(10).getSettlement()));
        if(nodeList.get(10).getPlayer() == -1 && (presenter.canPlaceSettlement(10) || presenter.canPlaceCity(10)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(10);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 191);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(11).getPlayer(),
                nodeList.get(11).getSettlement()));
        if(nodeList.get(11).getPlayer() == -1 && (presenter.canPlaceSettlement(11) || presenter.canPlaceCity(11)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(11);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 105, 230);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(12).getPlayer(),
                nodeList.get(12).getSettlement()));
        if(nodeList.get(12).getPlayer() == -1 && (presenter.canPlaceSettlement(12) || presenter.canPlaceCity(12)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(12);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 230);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(13).getPlayer(),
                nodeList.get(13).getSettlement()));
        if(nodeList.get(13).getPlayer() == -1 && (presenter.canPlaceSettlement(13) || presenter.canPlaceCity(13)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(13);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 230);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(14).getPlayer(),
                nodeList.get(14).getSettlement()));
        if(nodeList.get(14).getPlayer() == -1 && (presenter.canPlaceSettlement(14) || presenter.canPlaceCity(14)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(14);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 230);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(15).getPlayer(),
                nodeList.get(15).getSettlement()));
        if(nodeList.get(15).getPlayer() == -1 && (presenter.canPlaceSettlement(15) || presenter.canPlaceCity(15)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(15);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 705, 230);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(16).getPlayer(),
                nodeList.get(16).getSettlement()));
        if(nodeList.get(16).getPlayer() == -1 && (presenter.canPlaceSettlement(16) || presenter.canPlaceCity(16)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(16);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 105, 302);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(17).getPlayer(),
                nodeList.get(17).getSettlement()));
        if(nodeList.get(17).getPlayer() == -1 && (presenter.canPlaceSettlement(17) || presenter.canPlaceCity(17)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(17);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 302);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(18).getPlayer(),
                nodeList.get(18).getSettlement()));
        if(nodeList.get(18).getPlayer() == -1 && (presenter.canPlaceSettlement(18) || presenter.canPlaceCity(18)))
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
        if(nodeList.get(19).getPlayer() == -1 && (presenter.canPlaceSettlement(19) || presenter.canPlaceCity(19)))
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
        if(nodeList.get(20).getPlayer() == -1 && (presenter.canPlaceSettlement(20) || presenter.canPlaceCity(20)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(20);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 705, 302);
        

        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(21).getPlayer(),
                nodeList.get(21).getSettlement()));
        if(nodeList.get(21).getPlayer() == -1 && (presenter.canPlaceSettlement(21) || presenter.canPlaceCity(21)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(21);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 30, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(22).getPlayer(),
                nodeList.get(22).getSettlement()));
        if(nodeList.get(22).getPlayer() == -1 && (presenter.canPlaceSettlement(22) || presenter.canPlaceCity(22)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(22);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(23).getPlayer(),
                nodeList.get(23).getSettlement()));
        if(nodeList.get(23).getPlayer() == -1 && (presenter.canPlaceSettlement(23) || presenter.canPlaceCity(23)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(23);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(24).getPlayer(),
                nodeList.get(24).getSettlement()));
        if(nodeList.get(24).getPlayer() == -1 && (presenter.canPlaceSettlement(24) || presenter.canPlaceCity(24)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(24);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(25).getPlayer(),
                nodeList.get(25).getSettlement()));
        if(nodeList.get(25).getPlayer() == -1 && (presenter.canPlaceSettlement(25) || presenter.canPlaceCity(25)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(25);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 341);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(26).getPlayer(),
                nodeList.get(26).getSettlement()));
        if(nodeList.get(26).getPlayer() == -1 && (presenter.canPlaceSettlement(26) || presenter.canPlaceCity(26)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(26);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 780, 341);
        

        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(27).getPlayer(),
                nodeList.get(27).getSettlement()));
        if(nodeList.get(27).getPlayer() == -1 && (presenter.canPlaceSettlement(27) || presenter.canPlaceCity(27)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(27);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 30, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(28).getPlayer(),
                nodeList.get(28).getSettlement()));
        if(nodeList.get(28).getPlayer() == -1 && (presenter.canPlaceSettlement(28) || presenter.canPlaceCity(28)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(28);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(29).getPlayer(),
                nodeList.get(29).getSettlement()));
        if(nodeList.get(29).getPlayer() == -1 && (presenter.canPlaceSettlement(29) || presenter.canPlaceCity(29)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(29);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(30).getPlayer(),
                nodeList.get(30).getSettlement()));
        if(nodeList.get(30).getPlayer() == -1 && (presenter.canPlaceSettlement(30) || presenter.canPlaceCity(30)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(30);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(31).getPlayer(),
                nodeList.get(31).getSettlement()));
        if(nodeList.get(31).getPlayer() == -1 && (presenter.canPlaceSettlement(31) || presenter.canPlaceCity(31)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(31);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 413);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(32).getPlayer(),
                nodeList.get(32).getSettlement()));
        if(nodeList.get(32).getPlayer() == -1 && (presenter.canPlaceSettlement(32) || presenter.canPlaceCity(32)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(32);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 780, 413);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(33).getPlayer(),
                nodeList.get(33).getSettlement()));
        if(nodeList.get(33).getPlayer() == -1 && (presenter.canPlaceSettlement(33) || presenter.canPlaceCity(33)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(33);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 105, 452);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(34).getPlayer(),
                nodeList.get(34).getSettlement()));
        if(nodeList.get(34).getPlayer() == -1 && (presenter.canPlaceSettlement(34) || presenter.canPlaceCity(34)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(34);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 452);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(35).getPlayer(),
                nodeList.get(35).getSettlement()));
        if(nodeList.get(35).getPlayer() == -1 && (presenter.canPlaceSettlement(35) || presenter.canPlaceCity(35)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(35);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 452);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(36).getPlayer(),
                nodeList.get(36).getSettlement()));
        if(nodeList.get(36).getPlayer() == -1 && (presenter.canPlaceSettlement(36) || presenter.canPlaceCity(36)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(36);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 452);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(37).getPlayer(),
                nodeList.get(37).getSettlement()));
        if(nodeList.get(37).getPlayer() == -1 && (presenter.canPlaceSettlement(37) || presenter.canPlaceCity(37)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(37);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 705, 452);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(38).getPlayer(),
                nodeList.get(38).getSettlement()));
        if(nodeList.get(38).getPlayer() == -1 && (presenter.canPlaceSettlement(38) || presenter.canPlaceCity(38)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(38);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 105, 524);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(39).getPlayer(),
                nodeList.get(39).getSettlement()));
        if(nodeList.get(39).getPlayer() == -1 && (presenter.canPlaceSettlement(39) || presenter.canPlaceCity(39)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(39);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 524);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(40).getPlayer(),
                nodeList.get(40).getSettlement()));
        if(nodeList.get(40).getPlayer() == -1 && (presenter.canPlaceSettlement(40) || presenter.canPlaceCity(40)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(40);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 524);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(41).getPlayer(),
                nodeList.get(41).getSettlement()));
        if(nodeList.get(41).getPlayer() == -1 && (presenter.canPlaceSettlement(41) || presenter.canPlaceCity(41)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(41);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 555, 524);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(42).getPlayer(),
                nodeList.get(42).getSettlement()));
        if(nodeList.get(42).getPlayer() == -1 && (presenter.canPlaceSettlement(42) || presenter.canPlaceCity(42)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(42);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 705, 524);
        

        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(43).getPlayer(),
                nodeList.get(43).getSettlement()));
        if(nodeList.get(43).getPlayer() == -1 && (presenter.canPlaceSettlement(43) || presenter.canPlaceCity(43)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(43);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 563);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(44).getPlayer(),
                nodeList.get(44).getSettlement()));
        if(nodeList.get(44).getPlayer() == -1 && (presenter.canPlaceSettlement(44) || presenter.canPlaceCity(44)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(44);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 563);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(45).getPlayer(),
                nodeList.get(45).getSettlement()));
        if(nodeList.get(45).getPlayer() == -1 && (presenter.canPlaceSettlement(45) || presenter.canPlaceCity(45)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(45);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 563);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(46).getPlayer(),
                nodeList.get(46).getSettlement()));
        if(nodeList.get(46).getPlayer() == -1 && (presenter.canPlaceSettlement(46) || presenter.canPlaceCity(46)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(46);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 563);
        

        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(47).getPlayer(),
                nodeList.get(47).getSettlement()));
        if(nodeList.get(47).getPlayer() == -1 && (presenter.canPlaceSettlement(47) || presenter.canPlaceCity(47)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(47);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 180, 635);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(48).getPlayer(),
                nodeList.get(48).getSettlement()));
        if(nodeList.get(48).getPlayer() == -1 && (presenter.canPlaceSettlement(48) || presenter.canPlaceCity(48)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(48);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 330, 635);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(49).getPlayer(),
                nodeList.get(49).getSettlement()));
        if(nodeList.get(49).getPlayer() == -1 && (presenter.canPlaceSettlement(49) || presenter.canPlaceCity(49)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(49);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 480, 635);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(50).getPlayer(),
                nodeList.get(50).getSettlement()));
        if(nodeList.get(50).getPlayer() == -1 && (presenter.canPlaceSettlement(50) || presenter.canPlaceCity(50)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(50);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 630, 635);
        
        
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(51).getPlayer(),
                nodeList.get(51).getSettlement()));
        if(nodeList.get(51).getPlayer() == -1 && (presenter.canPlaceSettlement(51) || presenter.canPlaceCity(51)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(51);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 255, 674);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(52).getPlayer(),
                nodeList.get(52).getSettlement()));
        if(nodeList.get(52).getPlayer() == -1 && (presenter.canPlaceSettlement(52) || presenter.canPlaceCity(52)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
                            presenter.setNodeToBuild(52);
                          }
                        }
                      });
        }
        ap.add(nodeImage, 405, 674);
        
        nodeImage = new Image(boardImageSupplier.getNodeToken(
                nodeList.get(53).getPlayer(),
                nodeList.get(53).getSettlement()));
        if(nodeList.get(53).getPlayer() == -1 && (presenter.canPlaceSettlement(53) || presenter.canPlaceCity(53)))
        {
            nodeImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (chooseNodeEnabled) {
                              chooseNodeEnabled = false;
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
        if(pathList.get(0).getPlayer() == -1 && presenter.canPlaceRoad(0))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(0);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 104);
        

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(1).getPlayer(),
                1));
        if(pathList.get(1).getPlayer() == -1 && presenter.canPlaceRoad(1))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(1);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 104);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(2).getPlayer(),
                2));
        if(pathList.get(2).getPlayer() == -1 && presenter.canPlaceRoad(2))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(2);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 104);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(3).getPlayer(),
                3));
        if(pathList.get(3).getPlayer() == -1 && presenter.canPlaceRoad(3))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(3);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 104);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(4).getPlayer(),
                4));
        if(pathList.get(4).getPlayer() == -1 && presenter.canPlaceRoad(4))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(4);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 104);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(5).getPlayer(),
                5));
        if(pathList.get(5).getPlayer() == -1 && presenter.canPlaceRoad(5))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(5);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 104);
        
        
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(6).getPlayer(),
                6));
        if(pathList.get(6).getPlayer() == -1 && presenter.canPlaceRoad(6))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(6);
                          }
                        }
                      });
        }
        ap.add(pathImage, 190, 160);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(7).getPlayer(),
                7));
        if(pathList.get(7).getPlayer() == -1 && presenter.canPlaceRoad(7))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(7);
                          }
                        }
                      });
        }
        ap.add(pathImage, 340, 160);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(8).getPlayer(),
                8));
        if(pathList.get(8).getPlayer() == -1 && presenter.canPlaceRoad(8))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(8);
                          }
                        }
                      });
        }
        ap.add(pathImage, 490, 160);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(9).getPlayer(),
                9));
        if(pathList.get(9).getPlayer() == -1 && presenter.canPlaceRoad(9))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(9);
                          }
                        }
                      });
        }
        ap.add(pathImage, 640, 160);
        


        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(10).getPlayer(),
                10));
        if(pathList.get(10).getPlayer() == -1 && presenter.canPlaceRoad(10))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(10);
                          }
                        }
                      });
        }
        ap.add(pathImage, 142, 215);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(11).getPlayer(),
                11));
        if(pathList.get(11).getPlayer() == -1 && presenter.canPlaceRoad(11))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(11);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 215);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(12).getPlayer(),
                12));
        if(pathList.get(12).getPlayer() == -1 && presenter.canPlaceRoad(12))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(12);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 215);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(13).getPlayer(),
                13));
        if(pathList.get(13).getPlayer() == -1 && presenter.canPlaceRoad(13))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(13);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 215);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(14).getPlayer(),
                14));
        if(pathList.get(14).getPlayer() == -1 && presenter.canPlaceRoad(14))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(14);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 215);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(15).getPlayer(),
                15));
        if(pathList.get(15).getPlayer() == -1 && presenter.canPlaceRoad(15))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(15);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 215);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(16).getPlayer(),
                16));
        if(pathList.get(16).getPlayer() == -1 && presenter.canPlaceRoad(16))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(16);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 215);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(17).getPlayer(),
                17));
        if(pathList.get(17).getPlayer() == -1 && presenter.canPlaceRoad(17))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(17);
                          }
                        }
                      });
        }
        ap.add(pathImage, 667, 215);
        

        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(18).getPlayer(),
                18));
        if(pathList.get(18).getPlayer() == -1 && presenter.canPlaceRoad(18))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(18);
                          }
                        }
                      });
        }
        ap.add(pathImage, 115, 271);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(19).getPlayer(),
                19));
        if(pathList.get(19).getPlayer() == -1 && presenter.canPlaceRoad(19))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(19);
                          }
                        }
                      });
        }
        ap.add(pathImage, 265, 271);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(20).getPlayer(),
                20));
        if(pathList.get(20).getPlayer() == -1 && presenter.canPlaceRoad(20))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(20);
                          }
                        }
                      });
        }
        ap.add(pathImage, 415, 271);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(21).getPlayer(),
                21));
        if(pathList.get(21).getPlayer() == -1 && presenter.canPlaceRoad(21))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(21);
                          }
                        }
                      });
        }
        ap.add(pathImage, 565, 271);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(22).getPlayer(),
                22));
        if(pathList.get(22).getPlayer() == -1 && presenter.canPlaceRoad(22))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(22);
                          }
                        }
                      });
        }
        ap.add(pathImage, 715, 271);
        


        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(23).getPlayer(),
                23));
        if(pathList.get(23).getPlayer() == -1 && presenter.canPlaceRoad(23))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(23);
                          }
                        }
                      });
        }
        ap.add(pathImage, 67, 326);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(24).getPlayer(),
                24));
        if(pathList.get(24).getPlayer() == -1 && presenter.canPlaceRoad(24))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(24);
                          }
                        }
                      });
        }
        ap.add(pathImage, 142, 326);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(25).getPlayer(),
                25));
        if(pathList.get(25).getPlayer() == -1 && presenter.canPlaceRoad(25))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(25);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 326);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(26).getPlayer(),
                26));
        if(pathList.get(26).getPlayer() == -1 && presenter.canPlaceRoad(26))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(26);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 326);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(27).getPlayer(),
                27));
        if(pathList.get(27).getPlayer() == -1 && presenter.canPlaceRoad(27))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(27);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(28).getPlayer(),
                28));
        if(pathList.get(28).getPlayer() == -1 && presenter.canPlaceRoad(28))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(28);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(29).getPlayer(),
                29));
        if(pathList.get(29).getPlayer() == -1 && presenter.canPlaceRoad(29))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(29);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(30).getPlayer(),
                30));
        if(pathList.get(30).getPlayer() == -1 && presenter.canPlaceRoad(30))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(30);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(31).getPlayer(),
                31));
        if(pathList.get(31).getPlayer() == -1 && presenter.canPlaceRoad(31))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(31);
                          }
                        }
                      });
        }
        ap.add(pathImage, 667, 326);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(32).getPlayer(),
                32));
        if(pathList.get(32).getPlayer() == -1 && presenter.canPlaceRoad(32))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(32);
                          }
                        }
                      });
        }
        ap.add(pathImage, 742, 326);
        

        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(33).getPlayer(),
                33));
        if(pathList.get(33).getPlayer() == -1 && presenter.canPlaceRoad(33))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(33);
                          }
                        }
                      });
        }
        ap.add(pathImage, 40, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(34).getPlayer(),
                34));
        if(pathList.get(34).getPlayer() == -1 && presenter.canPlaceRoad(34))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(34);
                          }
                        }
                      });
        }
        ap.add(pathImage, 190, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(35).getPlayer(),
                35));
        if(pathList.get(35).getPlayer() == -1 && presenter.canPlaceRoad(35))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(35);
                          }
                        }
                      });
        }
        ap.add(pathImage, 340, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(36).getPlayer(),
                36));
        if(pathList.get(36).getPlayer() == -1 && presenter.canPlaceRoad(36))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(36);
                          }
                        }
                      });
        }
        ap.add(pathImage, 490, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(37).getPlayer(),
                37));
        if(pathList.get(37).getPlayer() == -1 && presenter.canPlaceRoad(37))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(37);
                          }
                        }
                      });
        }
        ap.add(pathImage, 640, 382);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(38).getPlayer(),
                38));
        if(pathList.get(38).getPlayer() == -1 && presenter.canPlaceRoad(38))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(38);
                          }
                        }
                      });
        }
        ap.add(pathImage, 790, 382);
        


        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(39).getPlayer(),
                39));
        if(pathList.get(39).getPlayer() == -1 && presenter.canPlaceRoad(39))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(39);
                          }
                        }
                      });
        }
        ap.add(pathImage, 67, 437);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(40).getPlayer(),
                40));
        if(pathList.get(40).getPlayer() == -1 && presenter.canPlaceRoad(40))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(40);
                          }
                        }
                      });
        }
        ap.add(pathImage, 142, 437);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(41).getPlayer(),
                41));
        if(pathList.get(41).getPlayer() == -1 && presenter.canPlaceRoad(41))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(41);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 437);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(42).getPlayer(),
                42));
        if(pathList.get(42).getPlayer() == -1 && presenter.canPlaceRoad(42))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(42);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 437);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(43).getPlayer(),
                43));
        if(pathList.get(43).getPlayer() == -1 && presenter.canPlaceRoad(43))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(43);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(44).getPlayer(),
                44));
        if(pathList.get(44).getPlayer() == -1 && presenter.canPlaceRoad(44))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(44);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(45).getPlayer(),
                45));
        if(pathList.get(45).getPlayer() == -1 && presenter.canPlaceRoad(45))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(45);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(46).getPlayer(),
                46));
        if(pathList.get(46).getPlayer() == -1 && presenter.canPlaceRoad(46))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(46);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(47).getPlayer(),
                47));
        if(pathList.get(47).getPlayer() == -1 && presenter.canPlaceRoad(47))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(47);
                          }
                        }
                      });
        }
        ap.add(pathImage, 667, 437);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(48).getPlayer(),
                48));
        if(pathList.get(48).getPlayer() == -1 && presenter.canPlaceRoad(48))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(48);
                          }
                        }
                      });
        }
        ap.add(pathImage, 742, 437);
        

        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(49).getPlayer(),
                49));
        if(pathList.get(49).getPlayer() == -1 && presenter.canPlaceRoad(49))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(49);
                          }
                        }
                      });
        }
        ap.add(pathImage, 115, 493);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(50).getPlayer(),
                50));
        if(pathList.get(50).getPlayer() == -1 && presenter.canPlaceRoad(50))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(50);
                          }
                        }
                      });
        }
        ap.add(pathImage, 265, 493);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(51).getPlayer(),
                51));
        if(pathList.get(51).getPlayer() == -1 && presenter.canPlaceRoad(51))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(51);
                          }
                        }
                      });
        }
        ap.add(pathImage, 415, 493);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(52).getPlayer(),
                52));
        if(pathList.get(52).getPlayer() == -1 && presenter.canPlaceRoad(52))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(52);
                          }
                        }
                      });
        }
        ap.add(pathImage, 565, 493);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(53).getPlayer(),
                53));
        if(pathList.get(53).getPlayer() == -1 && presenter.canPlaceRoad(53))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(53);
                          }
                        }
                      });
        }
        ap.add(pathImage, 715, 493);
        


        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(54).getPlayer(),
                54));
        if(pathList.get(54).getPlayer() == -1 && presenter.canPlaceRoad(54))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(54);
                          }
                        }
                      });
        }
        ap.add(pathImage, 142, 548);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(55).getPlayer(),
                55));
        if(pathList.get(55).getPlayer() == -1 && presenter.canPlaceRoad(55))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(55);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 548);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(56).getPlayer(),
                56));
        if(pathList.get(56).getPlayer() == -1 && presenter.canPlaceRoad(56))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(56);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 548);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(57).getPlayer(),
                57));
        if(pathList.get(57).getPlayer() == -1 && presenter.canPlaceRoad(57))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(57);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 548);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(58).getPlayer(),
                58));
        if(pathList.get(58).getPlayer() == -1 && presenter.canPlaceRoad(58))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(58);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 548);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(59).getPlayer(),
                59));
        if(pathList.get(59).getPlayer() == -1 && presenter.canPlaceRoad(59))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(59);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 548);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(60).getPlayer(),
                60));
        if(pathList.get(60).getPlayer() == -1 && presenter.canPlaceRoad(60))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(60);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 548);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(61).getPlayer(),
                61));
        if(pathList.get(61).getPlayer() == -1 && presenter.canPlaceRoad(61))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(61);
                          }
                        }
                      });
        }
        ap.add(pathImage, 667, 548);
        
        
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(62).getPlayer(),
                62));
        if(pathList.get(62).getPlayer() == -1 && presenter.canPlaceRoad(62))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(62);
                          }
                        }
                      });
        }
        ap.add(pathImage, 190, 604);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(63).getPlayer(),
                63));
        if(pathList.get(63).getPlayer() == -1 && presenter.canPlaceRoad(63))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(63);
                          }
                        }
                      });
        }
        ap.add(pathImage, 340, 604);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(64).getPlayer(),
                64));
        if(pathList.get(64).getPlayer() == -1 && presenter.canPlaceRoad(64))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(64);
                          }
                        }
                      });
        }
        ap.add(pathImage, 490, 604);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(65).getPlayer(),
                65));
        if(pathList.get(65).getPlayer() == -1 && presenter.canPlaceRoad(65))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(65);
                          }
                        }
                      });
        }
        ap.add(pathImage, 640, 604);
        
        

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(66).getPlayer(),
                66));
        if(pathList.get(66).getPlayer() == -1 && presenter.canPlaceRoad(66))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(66);
                          }
                        }
                      });
        }
        ap.add(pathImage, 217, 659);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(67).getPlayer(),
                67));
        if(pathList.get(67).getPlayer() == -1 && presenter.canPlaceRoad(67))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(67);
                          }
                        }
                      });
        }
        ap.add(pathImage, 292, 659);

        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(68).getPlayer(),
                68));
        if(pathList.get(68).getPlayer() == -1 && presenter.canPlaceRoad(68))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(68);
                          }
                        }
                      });
        }
        ap.add(pathImage, 367, 659);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(69).getPlayer(),
                69));
        if(pathList.get(69).getPlayer() == -1 && presenter.canPlaceRoad(69))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(69);
                          }
                        }
                      });
        }
        ap.add(pathImage, 442, 659);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(70).getPlayer(),
                70));
        if(pathList.get(70).getPlayer() == -1 && presenter.canPlaceRoad(70))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(70);
                          }
                        }
                      });
        }
        ap.add(pathImage, 517, 659);
        
        pathImage = new Image(boardImageSupplier.getRoadToken(
                pathList.get(71).getPlayer(),
                71));
        if(pathList.get(71).getPlayer() == -1 && presenter.canPlaceRoad(71))
        {
            pathImage.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                          if (choosePathEnabled) {
                              choosePathEnabled = false;
                            presenter.setPathToBuild(71);
                          }
                        }
                      });
        }
        ap.add(pathImage, 592, 659);
        
        return ap;
    }
    
    private void createResourceCardImages(List<String> resourceCards) {
        playerResourceCardArea.clear();
        //resourceCards = Arrays.asList(Constants.ORE, Constants.BRICK, Constants.LUMBER);
        
        AbsolutePanel ap = new AbsolutePanel();
        ap.setHeight("110px");
        ap.setWidth("1000px");
        for (int i = 0; i < resourceCards.size(); i++) {
            final int j = i;
            Image resourceCardImage = new Image(
                    boardImageSupplier.getResourceCard(resourceCards.get(i)));
            resourceCardImage.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                  if (chooseResourceCardEnabled) {
                    presenter.setResourceCardSelected(j);
                  }
                }
              });
            
            ap.add(resourceCardImage, 5 + (i*70), 5);
        }
        
        playerResourceCardArea.add(ap);
    }
    
    private void createDevelopmentCardImages(List<String> developmentCards) {
        playerDevelopmentCardArea.clear();
        
        //developmentCards = Arrays.asList(
        //        Constants.DEVELOPMENTCARDTYPEDEF00, Constants.DEVELOPMENTCARDTYPEDEF06, Constants.DEVELOPMENTCARDTYPEDEF05);
        
        AbsolutePanel ap = new AbsolutePanel();
        ap.setHeight("210px");
        ap.setWidth("1000px");
        for (int i = 0; i < developmentCards.size(); i++) {
            final int j = i;
            Image resourceCardImage = new Image(
                    boardImageSupplier.getDevelopmentCard(developmentCards.get(i)));
            resourceCardImage.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                  if (chooseResourceCardEnabled) {
                    presenter.setResourceCardSelected(j);
                  }
                }
              });
            
            ap.add(resourceCardImage, 5 + (i*140), 5);
        }
        
        playerDevelopmentCardArea.add(ap);
    }
    
    private void createInfoArea(String info)
    {
        infoArea.clear();
        
        AbsolutePanel ap = new AbsolutePanel();
        ap.setHeight("40px");
        ap.setWidth("1200px");
        FlowPanel fp = new FlowPanel();
        fp.setHeight("40px");
        fp.setWidth("1200px");
        
        Label message;
        
        String secondary = "";
        
        if(  info.contains(Constants.MOVEROBBERPT2)
          && info.length() > 13)
        {
            secondary = info.substring(13);
            info = Constants.MOVEROBBERPT2;
        }
        
        if(info.equals("VIEWER"))
        {
            message = new Label("ENJOY WATCHING");
            chooseNodeEnabled = false;
            choosePathEnabled = false;
            chooseResourceCardEnabled = false;
            ap.add(message, 5, 5);
        }
        else if(myTurn)
        {
            switch(info) {
                case Constants.MAKEFIRSTFREEMOVESETTLEMENT:
                    message = new Label("Place one free settlement: Please choose a node");
                    ap.add(message, 5, 5);
                    chooseNodeEnabled = true;
                    break;
                case Constants.MAKEFIRSTFREEMOVEROAD:
                    message = new Label("Place one free road: Please choose a path");
                    ap.add(message, 5, 5);
                    choosePathEnabled = true;
                    break;
                case Constants.MAKESECONDFREEMOVESETTLEMENT:
                    message = new Label("Place one free settlement: Please choose a node. "
                                      + "You will receive resource cards for the adjoining nodes");
                    ap.add(message, 5, 5);
                    chooseNodeEnabled = true;
                    break;
                case Constants.MAKESECONDFREEMOVEROAD:
                    message = new Label("Place one free road: Please choose a path");
                    ap.add(message, 5, 5);
                    choosePathEnabled = true;
                    break;
                case Constants.ROLLDICE:
                    message = new Label("Roll the Dice!");
                    ap.add(message, 5, 5);
                    Button rollDiceButton = new Button("Roll Dice");
                    
                    //add a clickListener to the button
                    rollDiceButton.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                          presenter.rollDice();
                       }
                    });
                    ap.add(rollDiceButton, 100, 5);
                    break;
                case Constants.MOVEROBBERPT1:
                    message = new Label("You rolled a 7! Choose a new location for the robber");
                    fp.add(message);
                    chooseHexEnabled = true;
                    infoArea.add(fp);
                    break;
                case Constants.MOVEROBBERPT2:
                    if(!secondary.equals(""))
                    {
                        message = new Label("Who do you want to steal from?");
                        fp.add(message);
                        
                        if(secondary.contains(Constants.PB))
                        {
                            Button player = new Button("Blue");
                            player.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    presenter.finishRobber(Constants.PB);
                                }
                             });
                            fp.add(player);
                        }
                        
                        if(secondary.contains(Constants.PR))
                        {
                            Button player = new Button("Red");
                            player.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    presenter.finishRobber(Constants.PR);
                                }
                             });
                            fp.add(player);
                        }
                        
                        if(secondary.contains(Constants.PY))
                        {
                            Button player = new Button("Yellow");
                            player.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    presenter.finishRobber(Constants.PY);
                                }
                             });
                            fp.add(player);
                        }
                        
                        if(secondary.contains(Constants.PG))
                        {
                            Button player = new Button("Green");
                            player.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    presenter.finishRobber(Constants.PG);
                                }
                             });
                            fp.add(player);
                        }
                        infoArea.add(fp);
                    }
                    else
                    {
                        presenter.finishRobber("");
                    }
                    break;
                case Constants.MOVEROBBERPT3:
                    presenter.finishMoveRobber();
                    break;
                case Constants.MOVEROBBERPT4:
                    message = new Label("You moved the robber! Good for you!");
                    fp.add(message);
                    Button robberDone = new Button("Make another move!");
                    robberDone.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(robberDone);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED2:
                    message = new Label("You Rolled a 2! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove2 = new Button("Make a move!");
                    makeAMove2.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove2);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED3:
                    message = new Label("You Rolled a 3! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove3 = new Button("Make a move!");
                    makeAMove3.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove3);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED4:
                    message = new Label("You Rolled a 4! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove4 = new Button("Make a move!");
                    makeAMove4.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove4);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED5:
                    message = new Label("You Rolled a 5! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove5 = new Button("Make a move!");
                    makeAMove5.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove5);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED6:
                    message = new Label("You Rolled a 6! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove6 = new Button("Make a move!");
                    makeAMove6.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove6);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED8:
                    message = new Label("You Rolled an 8! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove8 = new Button("Make a move!");
                    makeAMove8.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove8);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED9:
                    message = new Label("You Rolled a 9! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove9 = new Button("Make a move!");
                    makeAMove9.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove9);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED10:
                    message = new Label("You Rolled a 10! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove10 = new Button("Make a move!");
                    makeAMove10.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove10);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED11:
                    message = new Label("You Rolled an 11! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove11 = new Button("Make a move!");
                    makeAMove11.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove11);
                    infoArea.add(fp);
                    break;
                case Constants.ROLLED12:
                    message = new Label("You Rolled a 12! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove12 = new Button("Make a move!");
                    makeAMove12.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove12);
                    infoArea.add(fp);
                    break;
                case Constants.BUILDROADPT1:
                    presenter.finishRoadBuild();
                    break;
                case Constants.BUILDROADPT2:
                    message = new Label("You built a road! Good for you!");
                    fp.add(message);
                    Button roadBuild = new Button("Make another move!");
                    roadBuild.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(roadBuild);
                    infoArea.add(fp);
                    break;
                case Constants.BUILDSETTLEMENTPT1:
                    presenter.finishSettlementBuild();
                    break;
                case Constants.BUILDSETTLEMENTPT2:
                    message = new Label("You built a settlement! Good for you!");
                    fp.add(message);
                    Button settlementBuild = new Button("Make another move!");
                    settlementBuild.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(settlementBuild);
                    infoArea.add(fp);
                    break;
                case Constants.BUILDCITYPT1:
                    presenter.finishCityBuild();
                    break;
                case Constants.BUILDCITYPT2:
                    message = new Label("You built a city! Good for you!");
                    fp.add(message);
                    Button cityBuild = new Button("Make another move!");
                    cityBuild.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(cityBuild);
                    infoArea.add(fp);
                    break;
                case Constants.BUYDEVELOPMENTCARDPT1:
                    presenter.finishBuyingDevelopmentCard();
                    break;
                case Constants.BUYDEVELOPMENTCARDPT2:
                    message = new Label("You bought a Development Card! Good for you!");
                    fp.add(message);
                    Button buyDevelopmentCard = new Button("Make another move!");
                    buyDevelopmentCard.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(buyDevelopmentCard);
                    infoArea.add(fp);
                    break;
                case Constants.ENDGAME:
                    message = new Label("You win!");
                    fp.add(message);
                    infoArea.add(fp);
                    break;
                case Constants.HARBORTRADEPT1:
                case Constants.THREETOONEHARBORTRADEPT1:
                case Constants.TWOTOONEBRIHARBORTRADEPT1:
                case Constants.TWOTOONEGRAHARBORTRADEPT1:
                case Constants.TWOTOONELUMHARBORTRADEPT1:
                case Constants.TWOTOONEOREHARBORTRADEPT1:
                case Constants.TWOTOONEWOOHARBORTRADEPT1:
                    presenter.finishHarborTrade();
                    break;
                case Constants.HARBORTRADEPT2:
                case Constants.THREETOONEHARBORTRADEPT2:
                case Constants.TWOTOONEBRIHARBORTRADEPT2:
                case Constants.TWOTOONEGRAHARBORTRADEPT2:
                case Constants.TWOTOONELUMHARBORTRADEPT2:
                case Constants.TWOTOONEOREHARBORTRADEPT2:
                case Constants.TWOTOONEWOOHARBORTRADEPT2:
                    message = new Label("You made a harbor trade! Good for you!");
                    fp.add(message);
                    Button harborTrade = new Button("Make another move!");
                    harborTrade.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(Constants.MAKEMOVE);
                       }
                    });
                    fp.add(harborTrade);
                    infoArea.add(fp);
                    break;
                    
                case Constants.MAKEMOVE:
                    message = new Label("Make a move!!");
                    fp.add(message);
                    
                    if(presenter.canBuildSettlement())
                    {
                        Button buildSettlement = new Button("BuildSettlement");
                        
                        //add a clickListener to the button
                        buildSettlement.addClickHandler(new ClickHandler() {
                           @Override
                           public void onClick(ClickEvent event) {
                              chooseNodeEnabled = true;
                              infoArea.clear();
                              
                              FlowPanel fp2 = new FlowPanel();
                              
                              Label text = new Label("Choose a node or cancel");
                              Button cancel = new Button("Cancel");
                              cancel.addClickHandler(new ClickHandler() {
                                  @Override
                                  public void onClick(ClickEvent event) {
                                      chooseNodeEnabled = false;
                                      presenter.makeMove();
                                      createInfoArea(Constants.MAKEMOVE);
                                  }
                              });
                              
                              fp2.add(text);
                              fp2.add(cancel);
                              infoArea.add(fp2);
                           }
                        });
                        fp.add(buildSettlement);
                    }
                    
                    if(presenter.canBuildCity())
                    {
                        Button buildCity = new Button("BuildCity");
                        
                        //add a clickListener to the button
                        buildCity.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                presenter.lookingForCity = true;
                                chooseNodeEnabled = true;
                                infoArea.clear();
                                
                                FlowPanel fp2 = new FlowPanel();
                                
                                Label text = new Label("Choose a node or cancel");
                                Button cancel = new Button("Cancel");
                                cancel.addClickHandler(new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                        presenter.lookingForCity = false;
                                        chooseNodeEnabled = false;
                                        presenter.makeMove();
                                        createInfoArea(Constants.MAKEMOVE);
                                    }
                                });
                                
                                fp2.add(text);
                                fp2.add(cancel);
                                infoArea.add(fp2);
                            }
                        });
                        fp.add(buildCity);
                    }
                    
                    if(presenter.canBuildRoad())
                    {
                        Button buildRoad = new Button("BuildRoad");
                        
                        //add a clickListener to the button
                        buildRoad.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                               choosePathEnabled = true;
                               infoArea.clear();
                               
                               FlowPanel fp2 = new FlowPanel();
                               
                               Label text = new Label("Choose a path or cancel");
                               Button cancel = new Button("Cancel");
                               cancel.addClickHandler(new ClickHandler() {
                                   @Override
                                   public void onClick(ClickEvent event) {
                                       choosePathEnabled = false;
                                       createInfoArea(Constants.MAKEMOVE);
                                   }
                               });
                               
                               fp2.add(text);
                               fp2.add(cancel);
                               infoArea.add(fp2);
                            }
                        });
                        fp.add(buildRoad);
                    }
                    
                    if(presenter.canBuyDevelopmentCard())
                    {
                        Button buyDevelopementCard = new Button("BuyDevelopementCard");
                        
                        //add a clickListener to the button
                        buyDevelopementCard.addClickHandler(new ClickHandler() {
                           @Override
                           public void onClick(ClickEvent event) {
                              presenter.buyDevelopmentCard();
                           }
                        });
                        fp.add(buyDevelopementCard);
                    }
                    
                    if(presenter.canPlayDevelopmentCard())
                    {
                        Button canPlayDevelopmentCard = new Button("PlayDevelopementCard");
                        
                      //add a clickListener to the button
                        canPlayDevelopmentCard.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                               chooseDevelopmentCardEnabled = true;
                               infoArea.clear();
                               
                               FlowPanel fp2 = new FlowPanel();
                               
                               Label text = new Label("Choose a Development Card or cancel");
                               Button cancel = new Button("Cancel");
                               cancel.addClickHandler(new ClickHandler() {
                                   @Override
                                   public void onClick(ClickEvent event) {
                                       chooseDevelopmentCardEnabled = false;
                                       createInfoArea(Constants.MAKEMOVE);
                                   }
                               });
                               
                               fp2.add(text);
                               fp2.add(cancel);
                               infoArea.add(fp2);
                            }
                        });
                        fp.add(canPlayDevelopmentCard);
                    }
                    
                    if( presenter.canHarborTrade(Constants.ORE)
                     || presenter.canHarborTrade(Constants.BRICK)
                     || presenter.canHarborTrade(Constants.LUMBER)
                     || presenter.canHarborTrade(Constants.GRAIN)
                     || presenter.canHarborTrade(Constants.WOOL))
                    {
                        Button canHarborTrade = new Button("HarborTrade");
                        
                      //add a clickListener to the button
                        canHarborTrade.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                               infoArea.clear();
                               
                               FlowPanel fp2 = new FlowPanel();
                               
                               Label text = new Label("Choose a Resource Type to Trade");
                               
                               if(presenter.canHarborTrade(Constants.ORE))
                               {
                                   Button ore = new Button("Ore");
                                   ore.addClickHandler(new ClickHandler() {
                                       @Override
                                       public void onClick(ClickEvent event) {
                                           infoArea.clear();
                                           
                                           FlowPanel fp3 = new FlowPanel();
                                           
                                           Label newText = new Label("Choose a desired resource");
                                           fp3.add(newText);
                                           
                                           Button brick = new Button("Brick");
                                           brick.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.ORE, Constants.BRICK);                                                   
                                               }
                                           });
                                           fp3.add(brick);
                                           
                                           Button lumber = new Button("Lumber");
                                           lumber.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.ORE, Constants.LUMBER);                                                   
                                               }
                                           });
                                           fp3.add(lumber);
                                           
                                           Button grain = new Button("Grain");
                                           grain.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.ORE, Constants.GRAIN);                                                   
                                               }
                                           });
                                           fp3.add(grain);
                                           
                                           Button wool = new Button("Wool");
                                           wool.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.ORE, Constants.WOOL);                                                   
                                               }
                                           });
                                           fp3.add(wool);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(Constants.MAKEMOVE);
                                               }
                                           });
                                           fp3.add(cancel);
                                           
                                           infoArea.add(fp3);
                                       }
                                   });
                                   fp2.add(ore);
                               }
                               
                               if(presenter.canHarborTrade(Constants.BRICK))
                               {
                                   Button brick = new Button("Brick");
                                   brick.addClickHandler(new ClickHandler() {
                                       @Override
                                       public void onClick(ClickEvent event) {
                                           infoArea.clear();
                                           
                                           FlowPanel fp3 = new FlowPanel();
                                           
                                           Label newText = new Label("Choose a desired resource");
                                           fp3.add(newText);
                                           
                                           Button ore = new Button("Ore");
                                           ore.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.BRICK, Constants.ORE);                                                   
                                               }
                                           });
                                           fp3.add(ore);
                                           
                                           Button lumber = new Button("Lumber");
                                           lumber.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.BRICK, Constants.LUMBER);                                                   
                                               }
                                           });
                                           fp3.add(lumber);
                                           
                                           Button grain = new Button("Grain");
                                           grain.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.BRICK, Constants.GRAIN);                                                   
                                               }
                                           });
                                           fp3.add(grain);
                                           
                                           Button wool = new Button("Wool");
                                           wool.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.BRICK, Constants.WOOL);                                                   
                                               }
                                           });
                                           fp3.add(wool);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(Constants.MAKEMOVE);
                                               }
                                           });
                                           fp3.add(cancel);
                                           
                                           infoArea.add(fp3);
                                       }
                                   });
                                   fp2.add(brick);
                               }
                               
                               if(presenter.canHarborTrade(Constants.LUMBER))
                               {
                                   Button lumber = new Button("Lumber");
                                   lumber.addClickHandler(new ClickHandler() {
                                       @Override
                                       public void onClick(ClickEvent event) {
                                           infoArea.clear();
                                           
                                           FlowPanel fp3 = new FlowPanel();
                                           
                                           Label newText = new Label("Choose a desired resource");
                                           fp3.add(newText);
                                           
                                           Button ore = new Button("Ore");
                                           ore.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.LUMBER, Constants.ORE);                                                   
                                               }
                                           });
                                           fp3.add(ore);
                                           
                                           Button brick = new Button("Brick");
                                           brick.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.LUMBER, Constants.BRICK);                                                   
                                               }
                                           });
                                           fp3.add(brick);
                                           
                                           Button grain = new Button("Grain");
                                           grain.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.LUMBER, Constants.GRAIN);                                                   
                                               }
                                           });
                                           fp3.add(grain);
                                           
                                           Button wool = new Button("Wool");
                                           wool.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.LUMBER, Constants.WOOL);                                                   
                                               }
                                           });
                                           fp3.add(wool);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(Constants.MAKEMOVE);
                                               }
                                           });
                                           fp3.add(cancel);
                                           
                                           infoArea.add(fp3);
                                       }
                                   });
                                   fp2.add(lumber);
                               }
                               
                               if(presenter.canHarborTrade(Constants.GRAIN))
                               {
                                   Button grain = new Button("Grain");
                                   grain.addClickHandler(new ClickHandler() {
                                       @Override
                                       public void onClick(ClickEvent event) {
                                           infoArea.clear();
                                           
                                           FlowPanel fp3 = new FlowPanel();
                                           
                                           Label newText = new Label("Choose a desired resource");
                                           fp3.add(newText);
                                           
                                           Button ore = new Button("Ore");
                                           ore.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.GRAIN, Constants.ORE);                                                   
                                               }
                                           });
                                           fp3.add(ore);
                                           
                                           Button brick = new Button("Brick");
                                           brick.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.GRAIN, Constants.BRICK);                                                   
                                               }
                                           });
                                           fp3.add(brick);
                                           
                                           Button lumber = new Button("Lumber");
                                           lumber.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.GRAIN, Constants.LUMBER);                                                   
                                               }
                                           });
                                           fp3.add(lumber);
                                           
                                           Button wool = new Button("Wool");
                                           wool.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.GRAIN, Constants.WOOL);                                                   
                                               }
                                           });
                                           fp3.add(wool);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(Constants.MAKEMOVE);
                                               }
                                           });
                                           fp3.add(cancel);
                                           
                                           infoArea.add(fp3);
                                       }
                                   });
                                   fp2.add(grain);
                               }
                               
                               if(presenter.canHarborTrade(Constants.WOOL))
                               {
                                   Button wool = new Button("Wool");
                                   wool.addClickHandler(new ClickHandler() {
                                       @Override
                                       public void onClick(ClickEvent event) {
                                           infoArea.clear();
                                           
                                           FlowPanel fp3 = new FlowPanel();
                                           
                                           Label newText = new Label("Choose a desired resource");
                                           fp3.add(newText);
                                           
                                           Button ore = new Button("Ore");
                                           ore.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.WOOL, Constants.ORE);                                                   
                                               }
                                           });
                                           fp3.add(ore);
                                           
                                           Button brick = new Button("Brick");
                                           brick.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.WOOL, Constants.BRICK);                                                   
                                               }
                                           });
                                           fp3.add(brick);
                                           
                                           Button lumber = new Button("Lumber");
                                           lumber.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.WOOL, Constants.LUMBER);                                                   
                                               }
                                           });
                                           fp3.add(lumber);
                                           
                                           Button grain = new Button("Grain");
                                           grain.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(Constants.WOOL, Constants.GRAIN);                                                   
                                               }
                                           });
                                           fp3.add(grain);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(Constants.MAKEMOVE);
                                               }
                                           });
                                           fp3.add(cancel);
                                           
                                           infoArea.add(fp3);
                                       }
                                   });
                                   fp2.add(wool);
                               }
                               
                               
                               Button cancel = new Button("Cancel");
                               cancel.addClickHandler(new ClickHandler() {
                                   @Override
                                   public void onClick(ClickEvent event) {
                                       createInfoArea(Constants.MAKEMOVE);
                                   }
                               });
                               
                               fp2.add(text);
                               fp2.add(cancel);
                               infoArea.add(fp2);
                            }
                        });
                        fp.add(canHarborTrade);
                    }

                    Button endTurn = new Button("EndTurn");
                    
                    //add a clickListener to the button
                    endTurn.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                          presenter.endTurn();
                       }
                    });
                    fp.add(endTurn);
                    
                    infoArea.add(fp);
                    
                    break;
                    
            }
        }
        else
        {
            message = new Label("NOT YOUR TURN.");
            chooseNodeEnabled = false;
            choosePathEnabled = false;
            chooseResourceCardEnabled = false;
            ap.add(message, 5, 5);
        }
        
        infoArea.add(ap);
    }

    @Override
    public void setPresenter(SettlersOfCatanPresenter settlersOfCatanPresenter) {
        this.presenter = settlersOfCatanPresenter;
        
    }

    @Override
    public void setViewerState(List<Hex> hexList, List<Node> nodeList,
            List<Path> pathList) {
        createBoard(hexList, nodeList, pathList, -1, false, false);
        createResourceCardImages(new ArrayList<String>());
        createDevelopmentCardImages(new ArrayList<String>());
        createInfoArea("VIEWER");
        
    }

    @Override
    public void setPlayerState(List<Hex> hexList, List<Node> nodeList,
            List<Path> pathList, List<String> resourceCards,
            List<String> developmentCards, int victoryPoints,
            boolean hasLongestRoad, boolean hasLargestArmy,
            boolean myTurn, String infoMessage) {
        createBoard(hexList, nodeList, pathList, victoryPoints, hasLongestRoad, hasLargestArmy);
        createResourceCardImages(resourceCards);
        createDevelopmentCardImages(developmentCards);
        this.myTurn = myTurn;
        createInfoArea(infoMessage);
    }

    @Override
    public void makeMove(String moveType, List<Operation> move) {
        
        
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
    public void endTurn(int nextPlayer) {
        // TODO Auto-generated method stub
        
    }

    public void handleDiceRoll(int roll0, int roll1)
    {
        switch(roll0 + roll1)
        {
        case 7:
            presenter.clearRollAndMoveRobber();
            break;
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
            presenter.dispenseResources(roll0 + roll1);
            break;
        }
    }

}