package settlersofcatan.client;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import settlersofcatan.client.GameApi.Container;
import settlersofcatan.client.SettlersOfCatanPresenter.View;

@RunWith(JUnit4.class)
public class SettlersOfCatanPresenterTest {
    /** The class under test. */
    private SettlersOfCatanPresenter settlersOfCatanPresenter;
    private final SettlersOfCatanLogic settlersOfCatanLogic = new SettlersOfCatanLogic();
    private View mockView;
    private Container mockContainer;
    
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
    public void testEmptyStateForW() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(wId, 0, emptyState));
        verify(mockContainer).sendMakeMove(settlersOfCatanLogic.getMoveInitial(playerIds));
    }

    @Test
    public void testEmptyStateForB() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(bId, 0, emptyState));
    }

    @Test
    public void testEmptyStateForViewer() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(viewerId, 0, emptyState));
    }

}
