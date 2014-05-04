package org.settlersofcatan.client;

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

import org.game_api.GameApi;
import org.game_api.GameApi.Container;
import org.game_api.GameApi.Delete;
import org.game_api.GameApi.Operation;
import org.game_api.GameApi.Set;
import org.game_api.GameApi.SetTurn;
import org.game_api.GameApi.SetVisibility;
import org.game_api.GameApi.UpdateUI;
import org.settlersofcatan.client.SettlersOfCatanPresenter.View;

@RunWith(JUnit4.class)
public class SettlersOfCatanPresenterTest {
    /** The class under test. */
    private SettlersOfCatanPresenter settlersOfCatanPresenter;
    private final SettlersOfCatanLogic settlersOfCatanLogic = new SettlersOfCatanLogic();
    private View mockView;
    private Container mockContainer;
    
    private final ImmutableList<String> playerIds =
            ImmutableList.of(
                    SettlersOfCatanConstants.pbId,
                    SettlersOfCatanConstants.prId,
                    SettlersOfCatanConstants.pyId,
                    SettlersOfCatanConstants.pgId);
    
    
    // States to test
    private final ImmutableMap<String, Object> emptyState = ImmutableMap.<String, Object>of();
    private final Map<String, Object> blueAddStateForBlue =
            SettlersOfCatanLogicTest.createBlueAddAssetStateForBlue();
    private final Map<String, Object> blueAddStatePlusRoadForBlue =
            SettlersOfCatanLogicTest.createBlueAddAssetStatePlusRoadForBlue();
    private final Map<String, Object> blueAddStatePlusCityForBlue =
            SettlersOfCatanLogicTest.createBlueAddAssetStatePlusCityForBlue();
    private final Map<String, Object> blueAddStatePlusSettlementForBlue =
            SettlersOfCatanLogicTest.createBlueAddAssetStatePlusSettlementForBlue();
    private final Map<String, Object> blueAddStatePlusDevelopmentCardForBlue =
            SettlersOfCatanLogicTest.createBlueAddAssetStatePlusDevelopmentCardForBlue();
    private final Map<String, Object> blueNormalHarborTradeStateForBlue =
            SettlersOfCatanLogicTest.createBlueNormalHarborTradeStateForBlue();
    private final Map<String, Object> bluePostAnyHarborTradeStateForBlue =
            SettlersOfCatanLogicTest.createBluePostAnyHarborTradeStateForBlue();
    private final Map<String, Object> blueThreeToOneHarborTradeStateForBlue =
            SettlersOfCatanLogicTest.createBlueThreeToOneHarborTradeStateForBlue();
    private final Map<String, Object> blueTwoToOneLumberHarborTradeStateForBlue =
            SettlersOfCatanLogicTest.createBlueTwoToOneLumberHarborTradeStateForBlue();
    private final Map<String, Object> blueAddStateForNotBlue =
            SettlersOfCatanLogicTest.createBlueAddAssetStateForNotBlue();
    private final Map<String, Object> blueVictoryStateForNotBlue =
            SettlersOfCatanLogicTest.createBlueVictoryStateForNotBlue();
    
    private List<String> blueAddStateResources = Arrays.asList(
            SettlersOfCatanConstants.LUMBER,
            SettlersOfCatanConstants.BRICK,
            SettlersOfCatanConstants.WOOL,
            SettlersOfCatanConstants.GRAIN,
            SettlersOfCatanConstants.GRAIN,
            SettlersOfCatanConstants.ORE,
            SettlersOfCatanConstants.ORE,
            SettlersOfCatanConstants.ORE);
    
    private List<String> blueAddStateResourcesMinusRoad = Arrays.asList(
            SettlersOfCatanConstants.WOOL,
            SettlersOfCatanConstants.GRAIN,
            SettlersOfCatanConstants.GRAIN,
            SettlersOfCatanConstants.ORE,
            SettlersOfCatanConstants.ORE,
            SettlersOfCatanConstants.ORE);

    private List<String> blueAddStateResourcesMinusCity = Arrays.asList(
            SettlersOfCatanConstants.LUMBER,
            SettlersOfCatanConstants.BRICK,
            SettlersOfCatanConstants.WOOL);
    
    private List<String> blueAddStateResourcesMinusSettlement = Arrays.asList(
            SettlersOfCatanConstants.GRAIN,
            SettlersOfCatanConstants.ORE,
            SettlersOfCatanConstants.ORE,
            SettlersOfCatanConstants.ORE);
    
    private List<String> blueAddStateResourcesMinusDevelopmentCard = Arrays.asList(
            SettlersOfCatanConstants.LUMBER,
            SettlersOfCatanConstants.BRICK,
            SettlersOfCatanConstants.GRAIN,
            SettlersOfCatanConstants.ORE,
            SettlersOfCatanConstants.ORE);
    
    private List<String> blueNormalHarborTradeResources = Arrays.asList(
            SettlersOfCatanConstants.LUMBER,
            SettlersOfCatanConstants.LUMBER,
            SettlersOfCatanConstants.LUMBER,
            SettlersOfCatanConstants.LUMBER);
    
    private List<String> blueThreetoOneHarborTradeResources = Arrays.asList(
            SettlersOfCatanConstants.LUMBER,
            SettlersOfCatanConstants.LUMBER,
            SettlersOfCatanConstants.LUMBER);
    
    private List<String> blueTwotoOneLumberHarborTradeResources = Arrays.asList(
            SettlersOfCatanConstants.LUMBER,
            SettlersOfCatanConstants.LUMBER);
    
    private List<String> bluePostHarborTradeResources = Arrays.asList(
            SettlersOfCatanConstants.ORE);
    
    @Before
    public void runBefore() {
      mockView = Mockito.mock(View.class);
      mockContainer = Mockito.mock(Container.class);
      settlersOfCatanPresenter = new SettlersOfCatanPresenter(mockView, mockContainer, settlersOfCatanLogic);
      verify(mockView).setPresenter(settlersOfCatanPresenter);
    }

    @After
    public void runAfter() {
      verifyNoMoreInteractions(mockContainer);
      verifyNoMoreInteractions(mockView);
    }
    
    @Test
    public void testEmptyStateForBlue() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, "0", emptyState));
        verify(mockContainer).sendMakeMove(settlersOfCatanLogic.getMoveInitial(playerIds));
    }

    @Test
    public void testEmptyStateForRed() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.prId, "0", emptyState));
    }

    @Test
    public void testEmptyStateForYellow() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pyId, "0", emptyState));
    }

    @Test
    public void testEmptyStateForGreen() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pgId, "0", emptyState));
    }

    @Test
    public void testEmptyStateForViewer() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, "0", emptyState));
    }
    
    @Test
    public void testBlueAddRoadStateForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueAddStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.MAKEMOVE);
        settlersOfCatanPresenter.setPathToBuild(12);
        List<Operation> addRoad = new ArrayList<Operation>();
            addRoad.add(new SetTurn(playerIds.get(0)));
            addRoad.add(new Set(SettlersOfCatanConstants.PATH12, SettlersOfCatanConstants.ROAD02PB));
            addRoad.add(new Set(SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH12));
            addRoad.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB));
            addRoad.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB));
        verify(mockContainer).sendMakeMove(addRoad);

        settlersOfCatanPresenter.finishRoadBuild();
        addRoad.clear();
            addRoad.add(new Delete(SettlersOfCatanConstants.RESOURCECARD01PB));
            addRoad.add(new Delete(SettlersOfCatanConstants.RESOURCECARD00PB));
            verify(mockContainer).sendMakeMove(addRoad);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueAddStatePlusRoadForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResourcesMinusRoad,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.BUILDROADPT2);
    }
    
    @Test
    public void testBlueSettlementAddedStateForRed() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.prId, SettlersOfCatanConstants.pbId, blueAddStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                false,
                SettlersOfCatanConstants.MAKEMOVE);
    }
    
    @Test
    public void testBlueSettlementAddedStateForViewer() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, SettlersOfCatanConstants.pbId, blueAddStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    @Test
    public void testBlueCityAddedStateForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.lookingForCity = true;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueAddStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.MAKEMOVE);
        settlersOfCatanPresenter.setNodeToBuild(23);
        List<Operation> addCity = new ArrayList<Operation>();
            addCity.add(new SetTurn(playerIds.get(0)));
            addCity.add(new Set(SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.CITY00PB));
            addCity.add(new Set(SettlersOfCatanConstants.CITY00PB, SettlersOfCatanConstants.NODE23));
            addCity.add(new Delete(SettlersOfCatanConstants.SETTLEMENT00PB));
            addCity.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB));
            addCity.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD06PB));
            addCity.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD07PB));
            addCity.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB));
            addCity.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD04PB));
        verify(mockContainer).sendMakeMove(addCity);

        settlersOfCatanPresenter.finishCityBuild();
        addCity.clear();
            addCity.add(new Delete(SettlersOfCatanConstants.RESOURCECARD05PB));
            addCity.add(new Delete(SettlersOfCatanConstants.RESOURCECARD06PB));
            addCity.add(new Delete(SettlersOfCatanConstants.RESOURCECARD07PB));
            addCity.add(new Delete(SettlersOfCatanConstants.RESOURCECARD03PB));
            addCity.add(new Delete(SettlersOfCatanConstants.RESOURCECARD04PB));
            verify(mockContainer).sendMakeMove(addCity);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueAddStatePlusCityForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResourcesMinusCity,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStatePlusCityForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStatePlusCityForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStatePlusCityForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.BUILDCITYPT2);
    }
    
    @Test
    public void testBlueCityAddedStateForRed() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.prId, SettlersOfCatanConstants.pbId, blueAddStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                false,
                SettlersOfCatanConstants.MAKEMOVE);
    }
    
    @Test
    public void testBlueCityAddedStateForViewer() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, SettlersOfCatanConstants.pbId, blueAddStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    @Test
    public void testBlueCityAddedSettlementForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueAddStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.MAKEMOVE);
        settlersOfCatanPresenter.setNodeToBuild(12);
        List<Operation> addSettlement = new ArrayList<Operation>();
            addSettlement.add(new SetTurn(playerIds.get(0)));
            addSettlement.add(new Set(SettlersOfCatanConstants.NODE12, SettlersOfCatanConstants.SETTLEMENT01PB));
            addSettlement.add(new Set(SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE12));
            addSettlement.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB));
            addSettlement.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB));
            addSettlement.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB));
            addSettlement.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB));
        verify(mockContainer).sendMakeMove(addSettlement);

        settlersOfCatanPresenter.finishSettlementBuild();
        addSettlement.clear();
            addSettlement.add(new Delete(SettlersOfCatanConstants.RESOURCECARD01PB));
            addSettlement.add(new Delete(SettlersOfCatanConstants.RESOURCECARD00PB));
            addSettlement.add(new Delete(SettlersOfCatanConstants.RESOURCECARD02PB));
            addSettlement.add(new Delete(SettlersOfCatanConstants.RESOURCECARD03PB));
            verify(mockContainer).sendMakeMove(addSettlement);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueAddStatePlusSettlementForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResourcesMinusSettlement,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStatePlusSettlementForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStatePlusSettlementForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStatePlusSettlementForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.BUILDSETTLEMENTPT2);
    }
    
    @Test
    public void testBlueCityAddedRoadForRed() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.prId, SettlersOfCatanConstants.pbId, blueAddStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                false,
                SettlersOfCatanConstants.MAKEMOVE);
    }
    
    @Test
    public void testBlueCityAddedRoadForViewer() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, SettlersOfCatanConstants.pbId, blueAddStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    @Test
    public void testBlueDevelopmentCardAddedForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueAddStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.MAKEMOVE);
        settlersOfCatanPresenter.buyDevelopmentCard();
        List<Operation> buyDevelopmentCard = new ArrayList<Operation>();
        buyDevelopmentCard.add(new SetTurn(playerIds.get(0)));
            buyDevelopmentCard.add(new SetVisibility(SettlersOfCatanConstants.DEVELOPMENTCARD00, Arrays.asList(playerIds.get(0))));
            buyDevelopmentCard.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB));
            buyDevelopmentCard.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB));
            buyDevelopmentCard.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB));
        verify(mockContainer).sendMakeMove(buyDevelopmentCard);

        settlersOfCatanPresenter.finishBuyingDevelopmentCard();
        buyDevelopmentCard.clear();
            buyDevelopmentCard.add(new Delete(SettlersOfCatanConstants.RESOURCECARD05PB));
            buyDevelopmentCard.add(new Delete(SettlersOfCatanConstants.RESOURCECARD02PB));
            buyDevelopmentCard.add(new Delete(SettlersOfCatanConstants.RESOURCECARD03PB));
            verify(mockContainer).sendMakeMove(buyDevelopmentCard);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueAddStatePlusDevelopmentCardForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResourcesMinusDevelopmentCard,
                Arrays.asList(SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStatePlusDevelopmentCardForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStatePlusDevelopmentCardForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStatePlusDevelopmentCardForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.BUYDEVELOPMENTCARDPT2);
    }
    
    @Test
    public void testBlueNormalHarborTradeForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueNormalHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueNormalHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueNormalHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueNormalHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueNormalHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.MAKEMOVE);
        settlersOfCatanPresenter.doHarborTrade(SettlersOfCatanConstants.LUMBER, SettlersOfCatanConstants.ORE);
        List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new SetTurn(playerIds.get(0)));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB));
        verify(mockContainer).sendMakeMove(harborTrade);

        settlersOfCatanPresenter.finishHarborTrade();
        harborTrade.clear();
            harborTrade.add(new Delete(SettlersOfCatanConstants.RESOURCECARD00PB));
            harborTrade.add(new Delete(SettlersOfCatanConstants.RESOURCECARD01PB));
            harborTrade.add(new Delete(SettlersOfCatanConstants.RESOURCECARD02PB));
            harborTrade.add(new Delete(SettlersOfCatanConstants.RESOURCECARD03PB));
            harborTrade.add(new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB, Arrays.asList(playerIds.get(0))));
            verify(mockContainer).sendMakeMove(harborTrade);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, bluePostAnyHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                bluePostHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.HARBORTRADEPT2);
    }
    
    @Test
    public void testBlueThreeToOneHarborTradeForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueThreeToOneHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueThreetoOneHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueThreeToOneHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueThreeToOneHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueThreeToOneHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.MAKEMOVE);
        settlersOfCatanPresenter.doHarborTrade(SettlersOfCatanConstants.LUMBER, SettlersOfCatanConstants.ORE);
        List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new SetTurn(playerIds.get(0)));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB));
        verify(mockContainer).sendMakeMove(harborTrade);

        settlersOfCatanPresenter.finishHarborTrade();
        harborTrade.clear();
            harborTrade.add(new Delete(SettlersOfCatanConstants.RESOURCECARD00PB));
            harborTrade.add(new Delete(SettlersOfCatanConstants.RESOURCECARD01PB));
            harborTrade.add(new Delete(SettlersOfCatanConstants.RESOURCECARD02PB));
            harborTrade.add(new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB, Arrays.asList(playerIds.get(0))));
            verify(mockContainer).sendMakeMove(harborTrade);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, bluePostAnyHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                bluePostHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.THREETOONEHARBORTRADEPT2);
    }
    
    @Test
    public void testBlueTwoToOneLumberHarborTradeForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueTwoToOneLumberHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueTwotoOneLumberHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueTwoToOneLumberHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueTwoToOneLumberHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueTwoToOneLumberHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.MAKEMOVE);
        settlersOfCatanPresenter.doHarborTrade(SettlersOfCatanConstants.LUMBER, SettlersOfCatanConstants.ORE);
        List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new SetTurn(playerIds.get(0)));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB));
        verify(mockContainer).sendMakeMove(harborTrade);

        settlersOfCatanPresenter.finishHarborTrade();
        harborTrade.clear();
            harborTrade.add(new Delete(SettlersOfCatanConstants.RESOURCECARD00PB));
            harborTrade.add(new Delete(SettlersOfCatanConstants.RESOURCECARD01PB));
            harborTrade.add(new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE));
            harborTrade.add(new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB, Arrays.asList(playerIds.get(0))));
            verify(mockContainer).sendMakeMove(harborTrade);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, bluePostAnyHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                bluePostHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                true,
                SettlersOfCatanConstants.TWOTOONELUMHARBORTRADEPT2);
    }
    /*
    @Test
    public void testGameOverStateForWinner() {
        settlersOfCatanPresenter.updateUI(
                createUpdateUI(SettlersOfCatanConstants.pbId, SettlersOfCatanConstants.pbId, blueVictoryStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                Arrays.asList(SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00),
                settlersOfCatanLogic.getVictoryPointCount(blueVictoryStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueVictoryStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueVictoryStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.pbId)));
    }*/

    @Test
    public void testGameOverStateForLoser() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = SettlersOfCatanConstants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(
                createUpdateUI(SettlersOfCatanConstants.prId, SettlersOfCatanConstants.pbId, blueVictoryStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueVictoryStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueVictoryStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueVictoryStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, SettlersOfCatanConstants.prId)),
                false,
                SettlersOfCatanConstants.MAKEMOVE);
    }

    @Test
    public void testGameOverStateForViewer() {
        settlersOfCatanPresenter.updateUI(
                createUpdateUI(GameApi.VIEWER_ID, SettlersOfCatanConstants.pbId, blueVictoryStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    private UpdateUI createUpdateUI(
            String yourPlayerId,
            String turnOfPlayerId,
            Map<String, Object> state)
    {
          // Our UI only looks at the current state
          // (we ignore: lastState, lastMovePlayerId, playerIdToNumberOfTokensInPot)
          return new UpdateUI(
                  yourPlayerId,
                  SettlersOfCatanConstants.playersInfo,
                  state,
                  emptyState, // we ignore lastState
                  ImmutableList.<Operation>of(new SetTurn(turnOfPlayerId)),
                  "0",
                  ImmutableMap.<String, Integer>of());
        }

}
