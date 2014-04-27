package org.settlersofcatan.graphics;

import java.util.ArrayList;
import java.util.List;

import org.settlersofcatan.client.SettlersOfCatanConstants;
import org.game_api.GameApi.Operation;
import org.settlersofcatan.client.Hex;
import org.settlersofcatan.client.Node;
import org.settlersofcatan.client.Path;
import org.settlersofcatan.client.SettlersOfCatanPresenter;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.dom.client.Element;
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
    private boolean choosePathEnabledBegin;
    private boolean choosePathEnabledEnd;
    private boolean choosePathEnabledDND;
    private boolean chooseSettlementEnabledBegin;
    private boolean chooseSettlementEnabledEnd;
    private boolean chooseSettlementEnabledDND;
    private boolean chooseCityEnabledBegin;
    private boolean chooseCityEnabledEnd;
    private boolean chooseCityEnabledDND;
    private boolean chooseResourceCardEnabled;
    private boolean chooseDevelopmentCardEnabled;
    private boolean myTurn;
    
    private Image settlementImage;
    private Image cityImage;
    private Image roadImage;
    
    
    private BoardPieceMovingAnimation currentAnimation;
    private PickupDragController dragController;
    
    public SettlersOfCatanGraphics() {
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
        final AbsolutePanel preAp = new AbsolutePanel();
        preAp.setHeight("800px");
        preAp.setWidth("950px");
        
        dragController = new PickupDragController(preAp, false);
        dragController.setBehaviorDragStartSensitivity(3);
        dragController.addDragHandler(new DragHandlerAdapter(){});
        
        // Draw road/city/settlement cache
        if(presenter.myPlayer > -1 && presenter.myPlayer < 4)
        {
            settlementImage = new Image(boardImageSupplier.getNodeTokenSolo(
                    presenter.myPlayer,
                    1));
            
            if(myTurn)
            {
                settlementImage.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                              if (chooseSettlementEnabledBegin) {
                                  chooseSettlementEnabledBegin = false;
                                  currentAnimation = new BoardPieceMovingAnimation(
                                          boardArea, preAp, presenter, true, settlementImage);
                                  currentAnimation.setStartPoint(20,40);
                                  chooseSettlementEnabledEnd = true;
                              }
                            }
                          });
            }
            
            Label sc = new Label(" x " + presenter.getBoard().numAvailableSettlements(presenter.myPlayer));
            
            preAp.add(settlementImage, 20,40);
            preAp.add(sc, 70, 52);

            cityImage = new Image(boardImageSupplier.getNodeTokenSolo(
                    presenter.myPlayer,
                    2));

            if(myTurn)
            {
                if(chooseCityEnabledDND)
                    dragController.makeDraggable(cityImage);
                
                cityImage.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                              if (chooseCityEnabledBegin) {
                                  chooseCityEnabledBegin = false;
                                  currentAnimation = new BoardPieceMovingAnimation(
                                          boardArea, preAp, presenter, true, cityImage);
                                  currentAnimation.setStartPoint(20,90);
                                  chooseCityEnabledEnd = true;
                              }
                            }
                          });
            }
            
            sc = new Label(" x " + presenter.getBoard().numAvailableCities(presenter.myPlayer));
            
            preAp.add(cityImage, 20,90);
            preAp.add(sc, 70, 102);

            roadImage = new Image(boardImageSupplier.getRoadToken(
                    presenter.myPlayer,
                    6));
            

            if(myTurn)
            {
                roadImage.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                if (choosePathEnabledBegin) {
                                    choosePathEnabledBegin = false;
                                    currentAnimation = new BoardPieceMovingAnimation(
                                            boardArea, preAp, presenter, false, roadImage);
                                    currentAnimation.setStartPoint(35,145);
                                    choosePathEnabledEnd = true;
                                }
                            }
                          });
            }
            
            sc = new Label(" x " + presenter.getBoard().numAvailableRoads(presenter.myPlayer));
            
            preAp.add(roadImage, 35,145);
            preAp.add(sc, 70, 152);
        }

        AbsolutePanel ap = createHexImage(hexList, preAp);
        ap = createNodeImage(nodeList, ap);
        ap = createPathImage(pathList, ap);
        
        if(victoryPoints != -1)
        {
            Label vp = new Label("Victory Points = " + victoryPoints);
            ap.add(vp, 5, 5);
        }
        
        if(hasLongestRoad)
        {
            Image lr = new Image(boardImageSupplier.getLongestRoad());
            ap.add(lr, 785, 100);
        }
        
        if(hasLargestArmy)
        {
            Image la = new Image(boardImageSupplier.getLargestArmy());
            ap.add(la, 785, 500);
        }
        
        boardArea.add(ap);
    }
    
    // Creates the main board area with the HEX
    // These are non interactive
    private AbsolutePanel createHexImage(List<Hex> hexList, AbsolutePanel ap)
    {
        final List<Hex> hexCheck = new ArrayList<Hex>(hexList); 
        
        for(int i = 0; i < 19; i++)
        {
            final int num = i;
            Image hexImage = new Image(boardImageSupplier.getResourceBoardToken(hexList.get(i).getResource()));
            hexImage.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                  if (chooseHexEnabled && !hexCheck.get(num).getRobber()) {
                      chooseHexEnabled = false;
                      createInfoArea(presenter.setRobber(num));
                  }
                }
              });
            ap.add(hexImage, SettlersOfCatanConstants.HEX_XY[2*i], SettlersOfCatanConstants.HEX_XY[(2*i)+1]);

            if(hexList.get(i).getRobber())
            {
                Image dieRollImage = new Image(boardImageSupplier.getDieRollToken(0));
                ap.add(dieRollImage, SettlersOfCatanConstants.ROBBER_XY[2*i], SettlersOfCatanConstants.ROBBER_XY[(2*i)+1]);
            }
            else if(!hexList.get(i).getResource().equals(SettlersOfCatanConstants.DESERT))
            {
                Image dieRollImage = new Image(boardImageSupplier.getDieRollToken(hexList.get(i).getDieRoll()));
                ap.add(dieRollImage, SettlersOfCatanConstants.ROBBER_XY[2*i], SettlersOfCatanConstants.ROBBER_XY[(2*i)+1]);
            }
        }
        
        Image harborLines;
        
        for(int i = 0; i < 9; i++)
        {
            harborLines = new Image(boardImageSupplier.getHarborLines(i));
            ap.add(harborLines, SettlersOfCatanConstants.HARBOR_LINES_XY[2*i], SettlersOfCatanConstants.HARBOR_LINES_XY[(2*i) + 1]);
        }
        
        return ap;
    }
    
    private AbsolutePanel createNodeImage(List<Node> nodeList, AbsolutePanel ap)
    {
        Image nodeImage;
        
        for(int i = 0; i < 54; i++)
        {
            nodeImage = new Image(boardImageSupplier.getNodeToken(
                    nodeList.get(i).getPlayer(),
                    nodeList.get(i).getSettlement()));
            if(nodeList.get(i).getPlayer() == -1 && (presenter.canPlaceSettlement(i) || presenter.canPlaceCity(i)))
            {
                final int num = i;
                
                SimpleDropController dropController = new SimpleDropController(nodeImage)
                {
                    @Override
                    public void onDrop(DragContext context) {
                        if (chooseSettlementEnabledDND) {
                            chooseSettlementEnabledDND = false;
                            dragController.makeNotDraggable(settlementImage);
                            presenter.setNodeToBuild(num);
                        }
                    }
                };
                
                dragController.registerDropController(dropController);
                
                nodeImage.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                if (chooseSettlementEnabledEnd) {
                                    chooseSettlementEnabledEnd = false;
                                    currentAnimation.runAnimation(SettlersOfCatanConstants.NODE_XY[2*num], SettlersOfCatanConstants.NODE_XY[(2*num)+1], num);
                                }
                            }
                          });
            }
            ap.add(nodeImage, SettlersOfCatanConstants.NODE_XY[2*i], SettlersOfCatanConstants.NODE_XY[(2*i)+1]);
        }
        
        Image harbor;
        
        for(int i = 0; i < 9; i++)
        {
            harbor = new Image(boardImageSupplier.getHarbor(nodeList.get(SettlersOfCatanConstants.HARBOR_NODE[i]).getHarborBonus()));
            ap.add(harbor,  SettlersOfCatanConstants.HARBOR_XY[2*i], SettlersOfCatanConstants.HARBOR_XY[(2*i) + 1]);
        }
        
        return ap;
    }
    
    private AbsolutePanel createPathImage(List<Path> pathList, AbsolutePanel ap)
    {
        Image pathImage;
        
        for(int i = 0; i < 72; i++)
        {
            pathImage = new Image(boardImageSupplier.getRoadToken(
                    pathList.get(i).getPlayer(),
                    i));
            if(pathList.get(i).getPlayer() == -1 && presenter.canPlaceRoad(i))
            {
                final int num = i;
                
                SimpleDropController dropController = new SimpleDropController(pathImage)
                {
                    @Override
                    public void onDrop(DragContext context) {
                        if (choosePathEnabledDND) {
                            choosePathEnabledDND = false;
                            dragController.makeNotDraggable(roadImage);
                            presenter.setPathToBuild(num);
                        }
                    }
                };
                
                dragController.registerDropController(dropController);
                
                pathImage.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                if (choosePathEnabledEnd) {
                                    choosePathEnabledEnd = false;
                                    currentAnimation.runAnimation(SettlersOfCatanConstants.PATH_XY[2*num], SettlersOfCatanConstants.PATH_XY[(2*num)+1], num);
                                }
                            }
                          });
            }
            ap.add(pathImage, SettlersOfCatanConstants.PATH_XY[2*i], SettlersOfCatanConstants.PATH_XY[(2*i)+1]);
        }
        
        return ap;
    }
    
    private void createResourceCardImages(List<String> resourceCards) {
        playerResourceCardArea.clear();
        //resourceCards = Arrays.asList(SettlersOfCatanConstants.ORE, SettlersOfCatanConstants.BRICK, SettlersOfCatanConstants.LUMBER);
        
        AbsolutePanel ap = new AbsolutePanel();
        ap.setHeight("110px");
        ap.setWidth("950px");
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
        //        SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF06, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF05);
        
        AbsolutePanel ap = new AbsolutePanel();
        ap.setHeight("210px");
        ap.setWidth("950px");
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
        ap.setWidth("950px");
        FlowPanel fp = new FlowPanel();
        fp.setHeight("40px");
        fp.setWidth("950px");
        
        Label message;
        
        String secondary = "";
        
        if(  info.contains(SettlersOfCatanConstants.MOVEROBBERPT2)
          && info.length() > 13)
        {
            secondary = info.substring(13);
            info = SettlersOfCatanConstants.MOVEROBBERPT2;
        }
        
        if(info.equals("VIEWER"))
        {
            message = new Label("ENJOY WATCHING");
            chooseSettlementEnabledBegin = false;
            choosePathEnabledBegin = false;
            chooseCityEnabledBegin = false;
            chooseSettlementEnabledEnd = false;
            choosePathEnabledEnd = false;
            chooseCityEnabledEnd = false;
            chooseSettlementEnabledDND = false;
            choosePathEnabledDND = false;
            chooseCityEnabledDND = false;
            chooseResourceCardEnabled = false;
            ap.add(message, 5, 5);
        }
        else if(myTurn)
        {
            switch(info) {
                case SettlersOfCatanConstants.MAKEFIRSTFREEMOVESETTLEMENT:
                    message = new Label("Place one free settlement: Please choose a node");
                    ap.add(message, 5, 5);
                    chooseSettlementEnabledBegin = true;
                    chooseSettlementEnabledDND = true;
                    dragController.makeDraggable(settlementImage);
                    break;
                case SettlersOfCatanConstants.MAKEFIRSTFREEMOVEROAD:
                    message = new Label("Place one free road: Please choose a path");
                    ap.add(message, 5, 5);
                    choosePathEnabledBegin = true;
                    choosePathEnabledDND = true;
                    dragController.makeDraggable(roadImage);
                    break;
                case SettlersOfCatanConstants.MAKESECONDFREEMOVESETTLEMENT:
                    message = new Label("Place one free settlement: Please choose a node. "
                                      + "You will receive resource cards for the adjoining nodes");
                    ap.add(message, 5, 5);
                    chooseSettlementEnabledBegin = true;
                    chooseSettlementEnabledDND = true;
                    dragController.makeDraggable(settlementImage);
                    break;
                case SettlersOfCatanConstants.MAKESECONDFREEMOVEROAD:
                    message = new Label("Place one free road: Please choose a path");
                    ap.add(message, 5, 5);
                    choosePathEnabledBegin = true;
                    choosePathEnabledDND = true;
                    dragController.makeDraggable(roadImage);
                    break;
                case SettlersOfCatanConstants.ROLLDICE:
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
                case SettlersOfCatanConstants.MOVEROBBERPT1:
                    message = new Label("You rolled a 7! Choose a new location for the robber");
                    fp.add(message);
                    chooseHexEnabled = true;
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.MOVEROBBERPT2:
                    if(!secondary.equals(""))
                    {
                        message = new Label("Who do you want to steal from?");
                        fp.add(message);
                        
                        if(secondary.contains(SettlersOfCatanConstants.PB))
                        {
                            Button player = new Button("Blue");
                            player.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    presenter.finishRobber(SettlersOfCatanConstants.PB);
                                }
                             });
                            fp.add(player);
                        }
                        
                        if(secondary.contains(SettlersOfCatanConstants.PR))
                        {
                            Button player = new Button("Red");
                            player.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    presenter.finishRobber(SettlersOfCatanConstants.PR);
                                }
                             });
                            fp.add(player);
                        }
                        
                        if(secondary.contains(SettlersOfCatanConstants.PY))
                        {
                            Button player = new Button("Yellow");
                            player.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    presenter.finishRobber(SettlersOfCatanConstants.PY);
                                }
                             });
                            fp.add(player);
                        }
                        
                        if(secondary.contains(SettlersOfCatanConstants.PG))
                        {
                            Button player = new Button("Green");
                            player.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    presenter.finishRobber(SettlersOfCatanConstants.PG);
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
                case SettlersOfCatanConstants.MOVEROBBERPT3:
                    presenter.finishMoveRobber();
                    break;
                case SettlersOfCatanConstants.MOVEROBBERPT4:
                    message = new Label("You moved the robber! Good for you!");
                    fp.add(message);
                    Button robberDone = new Button("Make another move!");
                    robberDone.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(robberDone);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED2:
                    message = new Label("You Rolled a 2! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove2 = new Button("Make a move!");
                    makeAMove2.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove2);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED3:
                    message = new Label("You Rolled a 3! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove3 = new Button("Make a move!");
                    makeAMove3.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove3);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED4:
                    message = new Label("You Rolled a 4! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove4 = new Button("Make a move!");
                    makeAMove4.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove4);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED5:
                    message = new Label("You Rolled a 5! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove5 = new Button("Make a move!");
                    makeAMove5.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove5);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED6:
                    message = new Label("You Rolled a 6! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove6 = new Button("Make a move!");
                    makeAMove6.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove6);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED8:
                    message = new Label("You Rolled an 8! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove8 = new Button("Make a move!");
                    makeAMove8.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove8);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED9:
                    message = new Label("You Rolled a 9! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove9 = new Button("Make a move!");
                    makeAMove9.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove9);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED10:
                    message = new Label("You Rolled a 10! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove10 = new Button("Make a move!");
                    makeAMove10.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove10);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED11:
                    message = new Label("You Rolled an 11! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove11 = new Button("Make a move!");
                    makeAMove11.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove11);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ROLLED12:
                    message = new Label("You Rolled a 12! Dispersing Resources!");
                    fp.add(message);
                    Button makeAMove12 = new Button("Make a move!");
                    makeAMove12.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(makeAMove12);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.BUILDROADPT1:
                    presenter.finishRoadBuild();
                    break;
                case SettlersOfCatanConstants.BUILDROADPT2:
                    message = new Label("You built a road! Good for you!");
                    fp.add(message);
                    Button roadBuild = new Button("Make another move!");
                    roadBuild.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(roadBuild);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.BUILDSETTLEMENTPT1:
                    presenter.finishSettlementBuild();
                    break;
                case SettlersOfCatanConstants.BUILDSETTLEMENTPT2:
                    message = new Label("You built a settlement! Good for you!");
                    fp.add(message);
                    Button settlementBuild = new Button("Make another move!");
                    settlementBuild.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(settlementBuild);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.BUILDCITYPT1:
                    presenter.finishCityBuild();
                    break;
                case SettlersOfCatanConstants.BUILDCITYPT2:
                    message = new Label("You built a city! Good for you!");
                    fp.add(message);
                    Button cityBuild = new Button("Make another move!");
                    cityBuild.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(cityBuild);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.BUYDEVELOPMENTCARDPT1:
                    presenter.finishBuyingDevelopmentCard();
                    break;
                case SettlersOfCatanConstants.BUYDEVELOPMENTCARDPT2:
                    message = new Label("You bought a Development Card! Good for you!");
                    fp.add(message);
                    Button buyDevelopmentCard = new Button("Make another move!");
                    buyDevelopmentCard.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(buyDevelopmentCard);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.ENDGAME:
                    message = new Label("You win!");
                    fp.add(message);
                    infoArea.add(fp);
                    break;
                case SettlersOfCatanConstants.HARBORTRADEPT1:
                case SettlersOfCatanConstants.THREETOONEHARBORTRADEPT1:
                case SettlersOfCatanConstants.TWOTOONEBRIHARBORTRADEPT1:
                case SettlersOfCatanConstants.TWOTOONEGRAHARBORTRADEPT1:
                case SettlersOfCatanConstants.TWOTOONELUMHARBORTRADEPT1:
                case SettlersOfCatanConstants.TWOTOONEOREHARBORTRADEPT1:
                case SettlersOfCatanConstants.TWOTOONEWOOHARBORTRADEPT1:
                    presenter.finishHarborTrade();
                    break;
                case SettlersOfCatanConstants.HARBORTRADEPT2:
                case SettlersOfCatanConstants.THREETOONEHARBORTRADEPT2:
                case SettlersOfCatanConstants.TWOTOONEBRIHARBORTRADEPT2:
                case SettlersOfCatanConstants.TWOTOONEGRAHARBORTRADEPT2:
                case SettlersOfCatanConstants.TWOTOONELUMHARBORTRADEPT2:
                case SettlersOfCatanConstants.TWOTOONEOREHARBORTRADEPT2:
                case SettlersOfCatanConstants.TWOTOONEWOOHARBORTRADEPT2:
                    message = new Label("You made a harbor trade! Good for you!");
                    fp.add(message);
                    Button harborTrade = new Button("Make another move!");
                    harborTrade.addClickHandler(new ClickHandler() {
                       @Override
                       public void onClick(ClickEvent event) {
                           presenter.makeMove();
                           createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                       }
                    });
                    fp.add(harborTrade);
                    infoArea.add(fp);
                    break;
                    
                case SettlersOfCatanConstants.MAKEMOVE:
                    message = new Label("Make a move!!");
                    fp.add(message);
                    
                    if(presenter.canBuildSettlement())
                    {
                        Button buildSettlement = new Button("BuildSettlement");
                        
                        //add a clickListener to the button
                        buildSettlement.addClickHandler(new ClickHandler() {
                           @Override
                           public void onClick(ClickEvent event) {
                              chooseSettlementEnabledBegin = true;
                              chooseSettlementEnabledDND = true;
                              dragController.makeDraggable(settlementImage);
                              infoArea.clear();
                              
                              FlowPanel fp2 = new FlowPanel();
                              
                              Label text = new Label("Choose a node or cancel");
                              Button cancel = new Button("Cancel");
                              cancel.addClickHandler(new ClickHandler() {
                                  @Override
                                  public void onClick(ClickEvent event) {
                                      chooseSettlementEnabledBegin = false;
                                      chooseSettlementEnabledDND = false;
                                      dragController.makeNotDraggable(settlementImage);
                                      presenter.makeMove();
                                      createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
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
                                chooseCityEnabledBegin = true;
                                chooseCityEnabledDND = true;
                                infoArea.clear();
                                
                                FlowPanel fp2 = new FlowPanel();
                                
                                Label text = new Label("Choose a node or cancel");
                                Button cancel = new Button("Cancel");
                                cancel.addClickHandler(new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                        presenter.lookingForCity = false;
                                        chooseCityEnabledBegin = false;
                                        chooseCityEnabledDND = false;
                                        presenter.makeMove();
                                        createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
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
                               choosePathEnabledBegin = true;
                               choosePathEnabledDND = true;
                               dragController.makeDraggable(roadImage);
                               infoArea.clear();
                               
                               FlowPanel fp2 = new FlowPanel();
                               
                               Label text = new Label("Choose a path or cancel");
                               Button cancel = new Button("Cancel");
                               cancel.addClickHandler(new ClickHandler() {
                                   @Override
                                   public void onClick(ClickEvent event) {
                                       choosePathEnabledBegin = false;
                                       choosePathEnabledDND = false;
                                       dragController.makeNotDraggable(roadImage);
                                       createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
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
                                       createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                                   }
                               });
                               
                               fp2.add(text);
                               fp2.add(cancel);
                               infoArea.add(fp2);
                            }
                        });
                        fp.add(canPlayDevelopmentCard);
                    }
                    
                    if( presenter.canHarborTrade(SettlersOfCatanConstants.ORE)
                     || presenter.canHarborTrade(SettlersOfCatanConstants.BRICK)
                     || presenter.canHarborTrade(SettlersOfCatanConstants.LUMBER)
                     || presenter.canHarborTrade(SettlersOfCatanConstants.GRAIN)
                     || presenter.canHarborTrade(SettlersOfCatanConstants.WOOL))
                    {
                        Button canHarborTrade = new Button("HarborTrade");
                        
                      //add a clickListener to the button
                        canHarborTrade.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                               infoArea.clear();
                               
                               FlowPanel fp2 = new FlowPanel();
                               
                               Label text = new Label("Choose a Resource Type to Trade");
                               
                               if(presenter.canHarborTrade(SettlersOfCatanConstants.ORE))
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
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.ORE, SettlersOfCatanConstants.BRICK);                                                   
                                               }
                                           });
                                           fp3.add(brick);
                                           
                                           Button lumber = new Button("Lumber");
                                           lumber.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.ORE, SettlersOfCatanConstants.LUMBER);                                                   
                                               }
                                           });
                                           fp3.add(lumber);
                                           
                                           Button grain = new Button("Grain");
                                           grain.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.ORE, SettlersOfCatanConstants.GRAIN);                                                   
                                               }
                                           });
                                           fp3.add(grain);
                                           
                                           Button wool = new Button("Wool");
                                           wool.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.ORE, SettlersOfCatanConstants.WOOL);                                                   
                                               }
                                           });
                                           fp3.add(wool);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                                               }
                                           });
                                           fp3.add(cancel);
                                           
                                           infoArea.add(fp3);
                                       }
                                   });
                                   fp2.add(ore);
                               }
                               
                               if(presenter.canHarborTrade(SettlersOfCatanConstants.BRICK))
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
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.BRICK, SettlersOfCatanConstants.ORE);                                                   
                                               }
                                           });
                                           fp3.add(ore);
                                           
                                           Button lumber = new Button("Lumber");
                                           lumber.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.BRICK, SettlersOfCatanConstants.LUMBER);                                                   
                                               }
                                           });
                                           fp3.add(lumber);
                                           
                                           Button grain = new Button("Grain");
                                           grain.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.BRICK, SettlersOfCatanConstants.GRAIN);                                                   
                                               }
                                           });
                                           fp3.add(grain);
                                           
                                           Button wool = new Button("Wool");
                                           wool.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.BRICK, SettlersOfCatanConstants.WOOL);                                                   
                                               }
                                           });
                                           fp3.add(wool);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                                               }
                                           });
                                           fp3.add(cancel);
                                           
                                           infoArea.add(fp3);
                                       }
                                   });
                                   fp2.add(brick);
                               }
                               
                               if(presenter.canHarborTrade(SettlersOfCatanConstants.LUMBER))
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
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.LUMBER, SettlersOfCatanConstants.ORE);                                                   
                                               }
                                           });
                                           fp3.add(ore);
                                           
                                           Button brick = new Button("Brick");
                                           brick.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.LUMBER, SettlersOfCatanConstants.BRICK);                                                   
                                               }
                                           });
                                           fp3.add(brick);
                                           
                                           Button grain = new Button("Grain");
                                           grain.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.LUMBER, SettlersOfCatanConstants.GRAIN);                                                   
                                               }
                                           });
                                           fp3.add(grain);
                                           
                                           Button wool = new Button("Wool");
                                           wool.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.LUMBER, SettlersOfCatanConstants.WOOL);                                                   
                                               }
                                           });
                                           fp3.add(wool);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                                               }
                                           });
                                           fp3.add(cancel);
                                           
                                           infoArea.add(fp3);
                                       }
                                   });
                                   fp2.add(lumber);
                               }
                               
                               if(presenter.canHarborTrade(SettlersOfCatanConstants.GRAIN))
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
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.GRAIN, SettlersOfCatanConstants.ORE);                                                   
                                               }
                                           });
                                           fp3.add(ore);
                                           
                                           Button brick = new Button("Brick");
                                           brick.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.GRAIN, SettlersOfCatanConstants.BRICK);                                                   
                                               }
                                           });
                                           fp3.add(brick);
                                           
                                           Button lumber = new Button("Lumber");
                                           lumber.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.GRAIN, SettlersOfCatanConstants.LUMBER);                                                   
                                               }
                                           });
                                           fp3.add(lumber);
                                           
                                           Button wool = new Button("Wool");
                                           wool.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.GRAIN, SettlersOfCatanConstants.WOOL);                                                   
                                               }
                                           });
                                           fp3.add(wool);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
                                               }
                                           });
                                           fp3.add(cancel);
                                           
                                           infoArea.add(fp3);
                                       }
                                   });
                                   fp2.add(grain);
                               }
                               
                               if(presenter.canHarborTrade(SettlersOfCatanConstants.WOOL))
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
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.WOOL, SettlersOfCatanConstants.ORE);                                                   
                                               }
                                           });
                                           fp3.add(ore);
                                           
                                           Button brick = new Button("Brick");
                                           brick.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.WOOL, SettlersOfCatanConstants.BRICK);                                                   
                                               }
                                           });
                                           fp3.add(brick);
                                           
                                           Button lumber = new Button("Lumber");
                                           lumber.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.WOOL, SettlersOfCatanConstants.LUMBER);                                                   
                                               }
                                           });
                                           fp3.add(lumber);
                                           
                                           Button grain = new Button("Grain");
                                           grain.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   presenter.doHarborTrade(SettlersOfCatanConstants.WOOL, SettlersOfCatanConstants.GRAIN);                                                   
                                               }
                                           });
                                           fp3.add(grain);
                                           
                                           Button cancel = new Button("Cancel");
                                           cancel.addClickHandler(new ClickHandler() {
                                               @Override
                                               public void onClick(ClickEvent event) {
                                                   createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
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
                                       createInfoArea(SettlersOfCatanConstants.MAKEMOVE);
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
                    
                    default:
                        FlowPanel fp7 = new FlowPanel();
                        
                        Label txt = new Label("Unknown Info - " + info);
                        fp7.add(txt);
                        infoArea.add(fp7);
                        break;
                    
            }
        }
        else
        {
            message = new Label("NOT YOUR TURN.");
            chooseSettlementEnabledBegin = false;
            choosePathEnabledBegin = false;
            chooseCityEnabledBegin = false;
            chooseSettlementEnabledEnd = false;
            choosePathEnabledEnd = false;
            chooseCityEnabledEnd = false;
            chooseSettlementEnabledDND = false;
            choosePathEnabledDND = false;
            chooseCityEnabledDND = false;
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
        this.myTurn = myTurn;
        createBoard(hexList, nodeList, pathList, victoryPoints, hasLongestRoad, hasLargestArmy);
        createResourceCardImages(resourceCards);
        createDevelopmentCardImages(developmentCards);
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