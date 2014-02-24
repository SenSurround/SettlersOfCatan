package settlersofcatan.client;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import settlersofcatan.client.GameApi.Container;
import settlersofcatan.client.GameApi.Operation;
import settlersofcatan.client.GameApi.SetTurn;
import settlersofcatan.client.GameApi.UpdateUI;
import settlersofcatan.client.SettlersOfCatanPresenter.View;

@RunWith(JUnit4.class)
public class SettlersOfCatanPresenterTest {
    /** The class under test. */
    private SettlersOfCatanPresenter settlersOfCatanPresenter;
    private final SettlersOfCatanLogic settlersOfCatanLogic = new SettlersOfCatanLogic();
    private View mockView;
    private Container mockContainer;
    
    private final ImmutableList<Integer> playerIds =
            ImmutableList.of(
                    Constants.pbId,
                    Constants.prId,
                    Constants.pyId,
                    Constants.pgId);
    
    
    // States to test
    private final ImmutableMap<String, Object> emptyState = ImmutableMap.<String, Object>of();
    private final ImmutableMap<String, Object> blueAddState =
            (ImmutableMap<String, Object>) SettlersOfCatanLogicTest.createAddAssetState();
    private final ImmutableMap<String, Object> blueUnableToAddState =
            (ImmutableMap<String, Object>) SettlersOfCatanLogicTest.createAllAssetsUsedState();
    private final ImmutableMap<String, Object> bluePrepareForVictoryState =
            (ImmutableMap<String, Object>) SettlersOfCatanLogicTest.createPrepForVictoryState();
    private final ImmutableMap<String, Object> blueVictoryState =
            (ImmutableMap<String, Object>) SettlersOfCatanLogicTest.createVictoryState();
    
    @Before
    public void runBefore() {
      mockView = Mockito.mock(View.class);
      mockContainer = Mockito.mock(Container.class);
      SettlersOfCatanPresenter settlersOfCatanPresenter = new SettlersOfCatanPresenter(mockView, mockContainer);
      verify(mockView).setPresenter(settlersOfCatanPresenter);
    }

    @After
    public void runAfter() {
      verifyNoMoreInteractions(mockContainer);
      verifyNoMoreInteractions(mockView);
    }
    
    @Test
    public void testEmptyStateForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, 0, emptyState));
        verify(mockContainer).sendMakeMove(settlersOfCatanLogic.getMoveInitial(playerIds));
    }

    @Test
    public void testEmptyStateForRed() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.prId, 0, emptyState));
    }

    @Test
    public void testEmptyStateForYellow() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pyId, 0, emptyState));
    }

    @Test
    public void testEmptyStateForGreen() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pgId, 0, emptyState));
    }

    @Test
    public void testEmptyStateForViewer() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, 0, emptyState));
    }

    @Test
    public void testGameOverStateForWinner() {
        
        settlersOfCatanPresenter.updateUI(
                createUpdateUI(Constants.pbId, Constants.pbId, blueVictoryState));
        verify(mockView).setPlayerState(
                settlersOfCatanLogic.getResourceCardsFromState(blueVictoryState),
                settlersOfCatanLogic.getDevelopmentCardsFromState(blueVictoryState),
                10,
                false,
                false);
    }

    @Test
    public void testGameOverStateForLoser() {
        settlersOfCatanPresenter.updateUI(
                createUpdateUI(Constants.prId, Constants.pbId, blueVictoryState));
        verify(mockView).setPlayerState(
                settlersOfCatanLogic.getResourceCardsFromState(blueVictoryState),
                settlersOfCatanLogic.getDevelopmentCardsFromState(blueVictoryState),
                10,
                false,
                false);
    }

    @Test
    public void testGameOverStateForViewer() {
        settlersOfCatanPresenter.updateUI(
                createUpdateUI(GameApi.VIEWER_ID, Constants.pbId, blueVictoryState));
        verify(mockView).setViewerState(0, 52, 0, CheaterMessage.INVISIBLE, claimAbsent);
    }
    
    private UpdateUI createUpdateUI(
            int yourPlayerId,
            int turnOfPlayerId,
            Map<String, Object> state)
    {
          // Our UI only looks at the current state
          // (we ignore: lastState, lastMovePlayerId, playerIdToNumberOfTokensInPot)
          return new UpdateUI(
                  yourPlayerId,
                  Constants.playersInfo,
                  state,
                  emptyState, // we ignore lastState
                  ImmutableList.<Operation>of(new SetTurn(turnOfPlayerId)),
                  0,
                  ImmutableMap.<Integer, Integer>of());
        }

}
