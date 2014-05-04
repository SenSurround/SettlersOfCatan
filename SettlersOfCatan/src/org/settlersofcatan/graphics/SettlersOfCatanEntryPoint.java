package org.settlersofcatan.graphics;

import org.settlersofcatan.client.SettlersOfCatanLogic;
import org.settlersofcatan.client.SettlersOfCatanPresenter;
import org.game_api.GameApi;
import org.game_api.GameApi.Game;
import org.game_api.GameApi.IteratingPlayerContainer;
import org.game_api.GameApi.ContainerConnector;
import org.game_api.GameApi.UpdateUI;
import org.game_api.GameApi.VerifyMove;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SettlersOfCatanEntryPoint implements EntryPoint {
  //IteratingPlayerContainer container;
  ContainerConnector container;
  SettlersOfCatanPresenter settlersOfCatanPresenter;
  SettlersOfCatanLogic settlersOfCatanLogic;

  @Override
  public void onModuleLoad() {
      if(settlersOfCatanLogic == null)
          settlersOfCatanLogic = new SettlersOfCatanLogic();
    Game game = new Game() {
      @Override
      public void sendVerifyMove(VerifyMove verifyMove) {
        container.sendVerifyMoveDone(settlersOfCatanLogic.verify(verifyMove));
      }

      @Override
      public void sendUpdateUI(UpdateUI updateUI) {
        settlersOfCatanPresenter.updateUI(updateUI);
      }
    };
    //container = new IteratingPlayerContainer(game, 2);
    container = new ContainerConnector(game);
    SettlersOfCatanGraphics settlersOfCatanGraphics = new SettlersOfCatanGraphics();
    if(settlersOfCatanPresenter == null)
    {
        settlersOfCatanPresenter =
            new SettlersOfCatanPresenter(settlersOfCatanGraphics, container, settlersOfCatanLogic);
        settlersOfCatanPresenter.settlersOfCatanLogic = settlersOfCatanLogic;
    }
    //final ListBox playerSelect = new ListBox();
    //playerSelect.addItem("BluePlayer");
    //playerSelect.addItem("RedPlayer");
    //playerSelect.addItem("YellowPlayer");
    //playerSelect.addItem("GreenPlayer");
    //playerSelect.addItem("Viewer");
    //playerSelect.addChangeHandler(new ChangeHandler() {
    //  @Override
    //  public void onChange(ChangeEvent event) {
    //    int selectedIndex = playerSelect.getSelectedIndex();
    //    String playerId = selectedIndex == 4 ? GameApi.VIEWER_ID
    //        : container.getPlayerIds().get(selectedIndex);
    //    container.updateUi(playerId);
    //  }
    //});
    
    FlowPanel flowPanel = new FlowPanel();
    //flowPanel.add(playerSelect);
    flowPanel.add(settlersOfCatanGraphics);
    RootPanel.get("mainDiv").add(flowPanel);
    container.sendGameReady();
    //container.updateUi(container.getPlayerIds().get(0));
  }
}