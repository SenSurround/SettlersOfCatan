package settlersofcatan.graphics;

import settlersofcatan.client.SettlersOfCatanLogic;
import settlersofcatan.client.SettlersOfCatanPresenter;
import settlersofcatan.client.GameApi;
import settlersofcatan.client.GameApi.Game;
import settlersofcatan.client.GameApi.IteratingPlayerContainer;
import settlersofcatan.client.GameApi.UpdateUI;
import settlersofcatan.client.GameApi.VerifyMove;

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
  IteratingPlayerContainer container;
  SettlersOfCatanPresenter settlersOfCatanPresenter;

  @Override
  public void onModuleLoad() {
    Game game = new Game() {
      @Override
      public void sendVerifyMove(VerifyMove verifyMove) {
        container.sendVerifyMoveDone(new SettlersOfCatanLogic().verify(verifyMove));
      }

      @Override
      public void sendUpdateUI(UpdateUI updateUI) {
        settlersOfCatanPresenter.updateUI(updateUI);
      }
    };
    container = new IteratingPlayerContainer(game, 2);
    SettlersOfCatanGraphics settlersOfCatanGraphics = new SettlersOfCatanGraphics();
    settlersOfCatanPresenter =
        new SettlersOfCatanPresenter(settlersOfCatanGraphics, container);
    final ListBox playerSelect = new ListBox();
    playerSelect.addItem("BluePlayer");
    playerSelect.addItem("RedPlayer");
    playerSelect.addItem("YellowPlayer");
    playerSelect.addItem("GreenPlayer");
    playerSelect.addItem("Viewer");
    playerSelect.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        int selectedIndex = playerSelect.getSelectedIndex();
        int playerId = selectedIndex == 2 ? GameApi.VIEWER_ID
            : container.getPlayerIds().get(selectedIndex);
        container.updateUi(playerId);
      }
    });
    FlowPanel flowPanel = new FlowPanel();
    flowPanel.add(settlersOfCatanGraphics);
    flowPanel.add(playerSelect);
    RootPanel.get("mainDiv").add(flowPanel);
    container.sendGameReady();
    container.updateUi(container.getPlayerIds().get(0));
  }
}