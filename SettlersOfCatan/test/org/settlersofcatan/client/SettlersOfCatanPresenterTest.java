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
                    Constants.pbId,
                    Constants.prId,
                    Constants.pyId,
                    Constants.pgId);
    
    
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
            Constants.LUMBER,
            Constants.BRICK,
            Constants.WOOL,
            Constants.GRAIN,
            Constants.GRAIN,
            Constants.ORE,
            Constants.ORE,
            Constants.ORE);
    
    private List<String> blueAddStateResourcesMinusRoad = Arrays.asList(
            Constants.WOOL,
            Constants.GRAIN,
            Constants.GRAIN,
            Constants.ORE,
            Constants.ORE,
            Constants.ORE);

    private List<String> blueAddStateResourcesMinusCity = Arrays.asList(
            Constants.LUMBER,
            Constants.BRICK,
            Constants.WOOL);
    
    private List<String> blueAddStateResourcesMinusSettlement = Arrays.asList(
            Constants.GRAIN,
            Constants.ORE,
            Constants.ORE,
            Constants.ORE);
    
    private List<String> blueAddStateResourcesMinusDevelopmentCard = Arrays.asList(
            Constants.LUMBER,
            Constants.BRICK,
            Constants.GRAIN,
            Constants.ORE,
            Constants.ORE);
    
    private List<String> blueNormalHarborTradeResources = Arrays.asList(
            Constants.LUMBER,
            Constants.LUMBER,
            Constants.LUMBER,
            Constants.LUMBER);
    
    private List<String> blueThreetoOneHarborTradeResources = Arrays.asList(
            Constants.LUMBER,
            Constants.LUMBER,
            Constants.LUMBER);
    
    private List<String> blueTwotoOneLumberHarborTradeResources = Arrays.asList(
            Constants.LUMBER,
            Constants.LUMBER);
    
    private List<String> bluePostHarborTradeResources = Arrays.asList(
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
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, "0", emptyState));
        verify(mockContainer).sendMakeMove(settlersOfCatanLogic.getMoveInitial(playerIds));
    }

    @Test
    public void testEmptyStateForRed() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.prId, "0", emptyState));
    }

    @Test
    public void testEmptyStateForYellow() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pyId, "0", emptyState));
    }

    @Test
    public void testEmptyStateForGreen() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pgId, "0", emptyState));
    }

    @Test
    public void testEmptyStateForViewer() {
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, "0", emptyState));
    }
    
    @Test
    public void testBlueAddRoadStateForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.MAKEMOVE);
        settlersOfCatanPresenter.setPathToBuild(12);
        List<Operation> addRoad = new ArrayList<Operation>();
            addRoad.add(new SetTurn(playerIds.get(0)));
            addRoad.add(new Set(Constants.PATH12, Constants.ROAD02PB));
            addRoad.add(new Set(Constants.ROAD02PB, Constants.PATH12));
            addRoad.add(new SetVisibility(Constants.RESOURCECARD01PB));
            addRoad.add(new SetVisibility(Constants.RESOURCECARD00PB));
        verify(mockContainer).sendMakeMove(addRoad);

        settlersOfCatanPresenter.finishRoadBuild();
        addRoad.clear();
            addRoad.add(new Delete(Constants.RESOURCECARD01PB));
            addRoad.add(new Delete(Constants.RESOURCECARD00PB));
            verify(mockContainer).sendMakeMove(addRoad);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStatePlusRoadForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResourcesMinusRoad,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.BUILDROADPT2);
    }
    
    @Test
    public void testBlueSettlementAddedStateForRed() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.prId, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                false,
                Constants.MAKEMOVE);
    }
    
    @Test
    public void testBlueSettlementAddedStateForViewer() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    @Test
    public void testBlueCityAddedStateForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.lookingForCity = true;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.MAKEMOVE);
        settlersOfCatanPresenter.setNodeToBuild(23);
        List<Operation> addCity = new ArrayList<Operation>();
            addCity.add(new SetTurn(playerIds.get(0)));
            addCity.add(new Set(Constants.NODE23, Constants.CITY00PB));
            addCity.add(new Set(Constants.CITY00PB, Constants.NODE23));
            addCity.add(new Delete(Constants.SETTLEMENT00PB));
            addCity.add(new SetVisibility(Constants.RESOURCECARD05PB));
            addCity.add(new SetVisibility(Constants.RESOURCECARD06PB));
            addCity.add(new SetVisibility(Constants.RESOURCECARD07PB));
            addCity.add(new SetVisibility(Constants.RESOURCECARD03PB));
            addCity.add(new SetVisibility(Constants.RESOURCECARD04PB));
        verify(mockContainer).sendMakeMove(addCity);

        settlersOfCatanPresenter.finishCityBuild();
        addCity.clear();
            addCity.add(new Delete(Constants.RESOURCECARD05PB));
            addCity.add(new Delete(Constants.RESOURCECARD06PB));
            addCity.add(new Delete(Constants.RESOURCECARD07PB));
            addCity.add(new Delete(Constants.RESOURCECARD03PB));
            addCity.add(new Delete(Constants.RESOURCECARD04PB));
            verify(mockContainer).sendMakeMove(addCity);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStatePlusCityForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResourcesMinusCity,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStatePlusCityForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStatePlusCityForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStatePlusCityForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.BUILDCITYPT2);
    }
    
    @Test
    public void testBlueCityAddedStateForRed() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.prId, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                false,
                Constants.MAKEMOVE);
    }
    
    @Test
    public void testBlueCityAddedStateForViewer() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    @Test
    public void testBlueCityAddedSettlementForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.MAKEMOVE);
        settlersOfCatanPresenter.setNodeToBuild(12);
        List<Operation> addSettlement = new ArrayList<Operation>();
            addSettlement.add(new SetTurn(playerIds.get(0)));
            addSettlement.add(new Set(Constants.NODE12, Constants.SETTLEMENT01PB));
            addSettlement.add(new Set(Constants.SETTLEMENT01PB, Constants.NODE12));
            addSettlement.add(new SetVisibility(Constants.RESOURCECARD01PB));
            addSettlement.add(new SetVisibility(Constants.RESOURCECARD00PB));
            addSettlement.add(new SetVisibility(Constants.RESOURCECARD02PB));
            addSettlement.add(new SetVisibility(Constants.RESOURCECARD03PB));
        verify(mockContainer).sendMakeMove(addSettlement);

        settlersOfCatanPresenter.finishSettlementBuild();
        addSettlement.clear();
            addSettlement.add(new Delete(Constants.RESOURCECARD01PB));
            addSettlement.add(new Delete(Constants.RESOURCECARD00PB));
            addSettlement.add(new Delete(Constants.RESOURCECARD02PB));
            addSettlement.add(new Delete(Constants.RESOURCECARD03PB));
            verify(mockContainer).sendMakeMove(addSettlement);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStatePlusSettlementForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResourcesMinusSettlement,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStatePlusSettlementForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStatePlusSettlementForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStatePlusSettlementForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.BUILDSETTLEMENTPT2);
    }
    
    @Test
    public void testBlueCityAddedRoadForRed() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.prId, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                new ArrayList<String>(),
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                false,
                Constants.MAKEMOVE);
    }
    
    @Test
    public void testBlueCityAddedRoadForViewer() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(GameApi.VIEWER_ID, Constants.pbId, blueAddStateForNotBlue));
        verify(mockView).setViewerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList());
    }
    
    @Test
    public void testBlueDevelopmentCardAddedForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.MAKEMOVE);
        settlersOfCatanPresenter.buyDevelopmentCard();
        List<Operation> buyDevelopmentCard = new ArrayList<Operation>();
        buyDevelopmentCard.add(new SetTurn(playerIds.get(0)));
            buyDevelopmentCard.add(new SetVisibility(Constants.DEVELOPMENTCARD00, Arrays.asList(playerIds.get(0))));
            buyDevelopmentCard.add(new SetVisibility(Constants.RESOURCECARD05PB));
            buyDevelopmentCard.add(new SetVisibility(Constants.RESOURCECARD02PB));
            buyDevelopmentCard.add(new SetVisibility(Constants.RESOURCECARD03PB));
        verify(mockContainer).sendMakeMove(buyDevelopmentCard);

        settlersOfCatanPresenter.finishBuyingDevelopmentCard();
        buyDevelopmentCard.clear();
            buyDevelopmentCard.add(new Delete(Constants.RESOURCECARD05PB));
            buyDevelopmentCard.add(new Delete(Constants.RESOURCECARD02PB));
            buyDevelopmentCard.add(new Delete(Constants.RESOURCECARD03PB));
            verify(mockContainer).sendMakeMove(buyDevelopmentCard);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueAddStatePlusDevelopmentCardForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueAddStateResourcesMinusDevelopmentCard,
                Arrays.asList(Constants.DEVELOPMENTCARDTYPEDEF00),
                settlersOfCatanLogic.getVictoryPointCount(blueAddStatePlusDevelopmentCardForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueAddStatePlusDevelopmentCardForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueAddStatePlusDevelopmentCardForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.BUYDEVELOPMENTCARDPT2);
    }
    
    @Test
    public void testBlueNormalHarborTradeForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueNormalHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueNormalHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueNormalHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueNormalHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueNormalHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.MAKEMOVE);
        settlersOfCatanPresenter.doHarborTrade(Constants.LUMBER, Constants.ORE);
        List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new SetTurn(playerIds.get(0)));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD00PB));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD01PB));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD02PB));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD03PB));
        verify(mockContainer).sendMakeMove(harborTrade);

        settlersOfCatanPresenter.finishHarborTrade();
        harborTrade.clear();
            harborTrade.add(new Delete(Constants.RESOURCECARD00PB));
            harborTrade.add(new Delete(Constants.RESOURCECARD01PB));
            harborTrade.add(new Delete(Constants.RESOURCECARD02PB));
            harborTrade.add(new Delete(Constants.RESOURCECARD03PB));
            harborTrade.add(new Set(Constants.RESOURCECARD00PB, Constants.ORE));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD00PB, Arrays.asList(playerIds.get(0))));
            verify(mockContainer).sendMakeMove(harborTrade);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, bluePostAnyHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                bluePostHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.HARBORTRADEPT2);
    }
    
    @Test
    public void testBlueThreeToOneHarborTradeForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueThreeToOneHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueThreetoOneHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueThreeToOneHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueThreeToOneHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueThreeToOneHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.MAKEMOVE);
        settlersOfCatanPresenter.doHarborTrade(Constants.LUMBER, Constants.ORE);
        List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new SetTurn(playerIds.get(0)));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD00PB));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD01PB));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD02PB));
        verify(mockContainer).sendMakeMove(harborTrade);

        settlersOfCatanPresenter.finishHarborTrade();
        harborTrade.clear();
            harborTrade.add(new Delete(Constants.RESOURCECARD00PB));
            harborTrade.add(new Delete(Constants.RESOURCECARD01PB));
            harborTrade.add(new Delete(Constants.RESOURCECARD02PB));
            harborTrade.add(new Set(Constants.RESOURCECARD00PB, Constants.ORE));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD00PB, Arrays.asList(playerIds.get(0))));
            verify(mockContainer).sendMakeMove(harborTrade);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, bluePostAnyHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                bluePostHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.THREETOONEHARBORTRADEPT2);
    }
    
    @Test
    public void testBlueTwoToOneLumberHarborTradeForBlue() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, blueTwoToOneLumberHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                blueTwotoOneLumberHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(blueTwoToOneLumberHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(blueTwoToOneLumberHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(blueTwoToOneLumberHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.MAKEMOVE);
        settlersOfCatanPresenter.doHarborTrade(Constants.LUMBER, Constants.ORE);
        List<Operation> harborTrade = new ArrayList<Operation>();
            harborTrade.add(new SetTurn(playerIds.get(0)));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD00PB));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD01PB));
        verify(mockContainer).sendMakeMove(harborTrade);

        settlersOfCatanPresenter.finishHarborTrade();
        harborTrade.clear();
            harborTrade.add(new Delete(Constants.RESOURCECARD00PB));
            harborTrade.add(new Delete(Constants.RESOURCECARD01PB));
            harborTrade.add(new Set(Constants.RESOURCECARD00PB, Constants.ORE));
            harborTrade.add(new SetVisibility(Constants.RESOURCECARD00PB, Arrays.asList(playerIds.get(0))));
            verify(mockContainer).sendMakeMove(harborTrade);
            
        settlersOfCatanPresenter.updateUI(createUpdateUI(Constants.pbId, Constants.pbId, bluePostAnyHarborTradeStateForBlue));
        verify(mockView).setPlayerState(
                settlersOfCatanPresenter.getBoard().getHexList(),
                settlersOfCatanPresenter.getBoard().getNodeList(),
                settlersOfCatanPresenter.getBoard().getPathList(),
                bluePostHarborTradeResources,
                new ArrayList<String>(),
                settlersOfCatanLogic.getVictoryPointCount(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLongestRoad(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                settlersOfCatanLogic.hasLargestArmy(bluePostAnyHarborTradeStateForBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.pbId)),
                true,
                Constants.TWOTOONELUMHARBORTRADEPT2);
    }
    /*
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
    }*/

    @Test
    public void testGameOverStateForLoser() {
        settlersOfCatanPresenter.currentPlayer = 0;
        settlersOfCatanPresenter.infoMessage = Constants.MAKEMOVE;
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
                settlersOfCatanLogic.hasLargestArmy(blueVictoryStateForNotBlue, settlersOfCatanLogic.getPlayerId(playerIds, Constants.prId)),
                false,
                Constants.MAKEMOVE);
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
            String yourPlayerId,
            String turnOfPlayerId,
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
                  "0",
                  ImmutableMap.<String, Integer>of());
        }

}
