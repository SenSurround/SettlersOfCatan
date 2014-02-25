package settlersofcatan.client;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private final Map<String, Object> blueAddStateForBlue =
            SettlersOfCatanLogicTest.createBlueAddAssetStateForBlue();
    private final Map<String, Object> blueAddPlusDevCardStateForBlue =
            SettlersOfCatanLogicTest.createBlueAddAssetPlusDevCardStateForBlue();
    private final Map<String, Object> blueNormalHarborTradeStateForBlue =
            SettlersOfCatanLogicTest.createBlueNormalHarborTradeStateForBlue();
    private final Map<String, Object> blueThreeToOneHarborTradeStateForBlue =
            SettlersOfCatanLogicTest.createBlueThreeToOneHarborTradeStateForBlue();
    private final Map<String, Object> blueTwoToOneLumberHarborTradeStateForBlue =
            SettlersOfCatanLogicTest.createBlueTwoToOneLumberHarborTradeStateForBlue();
    private final Map<String, Object> blueAddStateForNotBlue =
            SettlersOfCatanLogicTest.createBlueAddAssetStateForNotBlue();
    private final Map<String, Object> blueVictoryStateForBlue =
            SettlersOfCatanLogicTest.createBlueVictoryStateForBlue();
    private final Map<String, Object> blueVictoryStateForNotBlue =
            SettlersOfCatanLogicTest.createBlueVictoryStateForNotBlue();
    
    private List<String> blueAddStateResources = Arrays.asList(
            Constants.LUMBER,
            Constants.BRICK,
            Constants.WOOL,
            Constants.GRAIN,
            Constants.GRAIN,
            Constants.ORE,
            Constants.ORE,
            Constants.ORE);
    
    @Before
    public void runBefore() {
      mockView = Mockito.mock(View.class);
      mockContainer = Mockito.mock(Container.class);
      settlersOfCatanPresenter = new SettlersOfCatanPresenter(mockView, mockContainer);
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
    public void testBlueAddSettlementStateForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStateForBlue));
        settlersOfCatanPresenter.selectSettlement();
        settlersOfCatanPresenter.verifyTwoStep();
        settlersOfCatanPresenter.endTurn();
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)));
        verify(mockView).makeMove(Constants.BUILDSETTLEMENT);
        verify(mockView).twoStepValidation();
        verify(mockView).endTurn(playerIds.indexOf(Constants.pbId) + 1 % playerIds.size());
                
                
    }
    
    @Test
    public void testBlueSettlementAddedStateForRed() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.prId, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)));
    }
    
    @Test
    public void testBlueSettlementAddedStateForViewer() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    @Test
    public void testBlueCityAddedStateForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStateForBlue));
        settlersOfCatanPresenter.selectCity();
        settlersOfCatanPresenter.verifyTwoStep();
        settlersOfCatanPresenter.endTurn();
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)));
        verify(mockView).makeMove(Constants.BUILDCITY);
        verify(mockView).twoStepValidation();
        verify(mockView).endTurn(playerIds.indexOf(Constants.pbId) + 1 % playerIds.size());
    }
    
    @Test
    public void testBlueCityAddedStateForRed() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.prId, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)));
    }
    
    @Test
    public void testBlueCityAddedStateForViewer() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    @Test
    public void testBlueCityAddedRoadForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStateForBlue));
        settlersOfCatanPresenter.selectRoad();
        settlersOfCatanPresenter.verifyTwoStep();
        settlersOfCatanPresenter.endTurn();
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)));
        verify(mockView).makeMove(Constants.BUILDROAD);
        verify(mockView).twoStepValidation();
        verify(mockView).endTurn(playerIds.indexOf(Constants.pbId) + 1 % playerIds.size());
    }
    
    @Test
    public void testBlueCityAddedRoadForRed() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.prId, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)));
    }
    
    @Test
    public void testBlueCityAddedRoadForViewer() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    @Test
    public void testBlueDevelopmentCardAddedForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStateForBlue));
        settlersOfCatanPresenter.selectPurchaseDevelopmentCard();
        settlersOfCatanPresenter.verifyTwoStep();
        settlersOfCatanPresenter.endTurn();
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)));
        verify(mockView).makeMove(Constants.BUYDEVELOPMENTCARD);
        verify(mockView).twoStepValidation();
        verify(mockView).endTurn(playerIds.indexOf(Constants.pbId) + 1 % playerIds.size());
    }
    
    @Test
    public void testBlueNormalHarborTradeForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueNormalHarborTradeStateForBlue));
        settlersOfCatanPresenter.selectNormalHarborTrade();
        settlersOfCatanPresenter.selectResourceCard(0);
        settlersOfCatanPresenter.selectResourceCard(1);
        settlersOfCatanPresenter.selectResourceCard(2);
        settlersOfCatanPresenter.selectResourceCard(3);
        settlersOfCatanPresenter.verifyTwoStep();
        settlersOfCatanPresenter.endTurn();
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                Arrays.asList(Constants.LUMBER, Constants.LUMBER, Constants.LUMBER, Constants.LUMBER),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueNormalHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueNormalHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueNormalHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)));
        verify(mockView).chooseCards(new ArrayList<String>(), Arrays.asList(Constants.LUMBER, Constants.LUMBER, Constants.LUMBER, Constants.LUMBER));
        verify(mockView).chooseCards(Arrays.asList(Constants.LUMBER), Arrays.asList(Constants.LUMBER, Constants.LUMBER, Constants.LUMBER));
        verify(mockView).chooseCards(Arrays.asList(Constants.LUMBER, Constants.LUMBER), Arrays.asList(Constants.LUMBER, Constants.LUMBER));
        verify(mockView).chooseCards(Arrays.asList(Constants.LUMBER, Constants.LUMBER, Constants.LUMBER), Arrays.asList(Constants.LUMBER));
        verify(mockView).chooseCards(Arrays.asList(Constants.LUMBER, Constants.LUMBER, Constants.LUMBER, Constants.LUMBER), new ArrayList<String>());
        verify(mockView).makeMove(Constants.HARBORTRADE);
        verify(mockView).twoStepValidation();
        verify(mockView).endTurn(playerIds.indexOf(Constants.pbId) + 1 % playerIds.size());
    }
    
    @Test
    public void testBlueThreeToOneHarborTradeForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueThreeToOneHarborTradeStateForBlue));
        settlersOfCatanPresenter.selectThreeToOneHarborTrade();
        settlersOfCatanPresenter.selectResourceCard(0);
        settlersOfCatanPresenter.selectResourceCard(1);
        settlersOfCatanPresenter.selectResourceCard(2);
        settlersOfCatanPresenter.verifyTwoStep();
        settlersOfCatanPresenter.endTurn();
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                Arrays.asList(Constants.LUMBER, Constants.LUMBER, Constants.LUMBER),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueThreeToOneHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueThreeToOneHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueThreeToOneHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)));
        verify(mockView).chooseCards(new ArrayList<String>(), Arrays.asList(Constants.LUMBER, Constants.LUMBER, Constants.LUMBER));
        verify(mockView).chooseCards(Arrays.asList(Constants.LUMBER), Arrays.asList(Constants.LUMBER, Constants.LUMBER));
        verify(mockView).chooseCards(Arrays.asList(Constants.LUMBER, Constants.LUMBER), Arrays.asList(Constants.LUMBER));
        verify(mockView).chooseCards(Arrays.asList(Constants.LUMBER, Constants.LUMBER, Constants.LUMBER), new ArrayList<String>());
        verify(mockView).makeMove(Constants.THREETOONEHARBORTRADE);
        verify(mockView).twoStepValidation();
        verify(mockView).endTurn(playerIds.indexOf(Constants.pbId) + 1 % playerIds.size());
    }
    
    @Test
    public void testBlueTwoToOneLumberHarborTradeForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueTwoToOneLumberHarborTradeStateForBlue));
        settlersOfCatanPresenter.selectTwoToOneHarborTrade(Constants.LUMBER);
        settlersOfCatanPresenter.selectResourceCard(0);
        settlersOfCatanPresenter.selectResourceCard(1);
        settlersOfCatanPresenter.verifyTwoStep();
        settlersOfCatanPresenter.endTurn();
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                Arrays.asList(Constants.LUMBER, Constants.LUMBER),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueTwoToOneLumberHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueTwoToOneLumberHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueTwoToOneLumberHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)));
        verify(mockView).chooseCards(new ArrayList<String>(), Arrays.asList(Constants.LUMBER, Constants.LUMBER));
        verify(mockView).chooseCards(Arrays.asList(Constants.LUMBER), Arrays.asList(Constants.LUMBER));
        verify(mockView).chooseCards(Arrays.asList(Constants.LUMBER, Constants.LUMBER), new ArrayList<String>());
        verify(mockView).makeMove(Constants.TWOTOONELUMHARBORTRADE);
        verify(mockView).twoStepValidation();
        verify(mockView).endTurn(playerIds.indexOf(Constants.pbId) + 1 % playerIds.size());
    }
    
    @Test
    public void testBlueDevelopmentCardPlayedForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddPlusDevCardStateForBlue));
        settlersOfCatanPresenter.selectPlayDevelopmentCard();
        settlersOfCatanPresenter.selectDevelopmentCard(0);
        settlersOfCatanPresenter.verifyTwoStep();
        settlersOfCatanPresenter.endTurn();
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                Arrays.asList(Constants.DEVELOPMENTCARDTYPEDEF00),
                settlersOfCatanLogic.getVictoryPointCount(blueAddPlusDevCardStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddPlusDevCardStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddPlusDevCardStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)));
        verify(mockView).makeMove(Constants.PLAYDEVELOPMENTCARD);
        verify(mockView).chooseDevelopmentCard("");
        verify(mockView).chooseDevelopmentCard(Constants.DEVELOPMENTCARDTYPEDEF00);
        verify(mockView).twoStepValidation();
        verify(mockView).endTurn(playerIds.indexOf(Constants.pbId) + 1 % playerIds.size());
    }

    @Test
    public void testGameOverStateForWinner() {
        settlersOfCatanPresenter.updateUI(
                createUpdateUI(Constants.pbId, Constants.pbId, blueVictoryStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                Arrays.asList(Constants.DEVELOPMENTCARDTYPEDEF00),
                settlersOfCatanLogic.getVictoryPointCount(blueVictoryStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueVictoryStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueVictoryStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)));
    }

    @Test
    public void testGameOverStateForLoser() {
        settlersOfCatanPresenter.updateUI(
                createUpdateUI(Constants.prId, Constants.pbId, blueVictoryStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueVictoryStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueVictoryStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueVictoryStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)));
    }

    @Test
    public void testGameOverStateForViewer() {
        settlersOfCatanPresenter.updateUI(
                createUpdateUI(GameApi.VIEWER_ID, Constants.pbId, blueVictoryStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
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
