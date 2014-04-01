package org.settlersofcatan.client;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.game_api.GameApi.Delete;
import org.game_api.GameApi.EndGame;
import org.game_api.GameApi.Operation;
import org.game_api.GameApi.Set;
import org.game_api.GameApi.SetVisibility;
import org.game_api.GameApi.VerifyMove;
import org.game_api.GameApi.VerifyMoveDone;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class SettlersOfCatanLogicTest {
    private String testName = "";
    
    // The Logic of the Settlers Of Catan game
    SettlersOfCatanLogic settlersOfCatanLogic = new SettlersOfCatanLogic();
    
    // Assert to JUnit that this move is expected to be OK
    private void assertMoveOk(VerifyMove verifyMove) {
        settlersOfCatanLogic.checkMoveIsLegal(verifyMove);
    }

    // Assert to JUnit that this move is expected to fail
    // A hacker must have submitted the move
    private void assertHacker(VerifyMove verifyMove) {
        VerifyMoveDone verifyDone = settlersOfCatanLogic.verify(verifyMove);
        
        System.out.println(testName);
        System.out.println(verifyDone.getMessage());
        System.out.println();
        
        assertEquals(verifyMove.getLastMovePlayerId(), verifyDone.getHackerPlayerId());
    }

    // Take a move and an input state
    // return the new state after the move is applied
    private Map<String, Object> applyMoveToState(
            Map<String, Object> initialState, ImmutableList<Operation> move) {
        Map<String, Object> newState = new HashMap<String, Object>(initialState);
        
        for (int i = 0; i < move.size(); i++) {
            Operation current = move.get(i);
            switch(current.getMessageName()) {
                case "Set":
                    newState.put(((Set) current).getKey(), ((Set) current).getValue());
                    break;
                    
                case "Delete":
                    newState.remove(((Delete) current).getKey());
                    break;
                    
                default:
                    break;
            }
        }
        
        return newState;
    }
    
    // Take an initial state and a new String/Object pair
    // return the new state after the new String/Object pair is applied
    public static Map<String, Object> changeState(
            Map<String, Object> previousState, String propString, Object propObject) {
        Map<String, Object> newState = previousState;
        
        newState.put(propString, propObject);
        
        return newState;
    }
    
    // Create an initial empty state for testing
    // The normally randomized elements are defaulted to specific values
    // to allow for testing
    public static Map<String, Object> createEmptyState() {
        Map<String, Object> emptyState = Maps.<String, Object>newHashMap();
        emptyState.put(Constants.TURN, Constants.PB);
        // These will be randomized in real game, set for testing purposes
        emptyState.put(Constants.HEX00, Constants.ORE);
        emptyState.put(Constants.HEX01, Constants.LUMBER);
        emptyState.put(Constants.HEX02, Constants.DESERT);
        emptyState.put(Constants.HEX03, Constants.GRAIN);
        emptyState.put(Constants.HEX04, Constants.LUMBER);
        emptyState.put(Constants.HEX05, Constants.WOOL);
        emptyState.put(Constants.HEX06, Constants.GRAIN);
        emptyState.put(Constants.HEX07, Constants.WOOL);
        emptyState.put(Constants.HEX08, Constants.BRICK);
        emptyState.put(Constants.HEX09, Constants.BRICK);
        emptyState.put(Constants.HEX10, Constants.WOOL);
        emptyState.put(Constants.HEX11, Constants.ORE);
        emptyState.put(Constants.HEX12, Constants.ORE);
        emptyState.put(Constants.HEX13, Constants.LUMBER);
        emptyState.put(Constants.HEX14, Constants.GRAIN);
        emptyState.put(Constants.HEX15, Constants.LUMBER);
        emptyState.put(Constants.HEX16, Constants.GRAIN);
        emptyState.put(Constants.HEX17, Constants.BRICK);
        emptyState.put(Constants.HEX18, Constants.LUMBER);
        // These will be randomized in real game, set for testing purposes
        emptyState.put(Constants.HARBOR00, Constants.HARBORTYPE03);
        emptyState.put(Constants.HARBOR01, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR02, Constants.HARBORTYPE01);
        emptyState.put(Constants.HARBOR03, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR04, Constants.HARBORTYPE05);
        emptyState.put(Constants.HARBOR05, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR06, Constants.HARBORTYPE02);
        emptyState.put(Constants.HARBOR07, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR08, Constants.HARBORTYPE04);
        // These will be randomized in real game, set for testing purposes
        // These will also need to be set to visible to no one pre game start
        emptyState.put(Constants.DEVELOPMENTCARD00, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD01, Constants.DEVELOPMENTCARDTYPEDEF03);
        emptyState.put(Constants.DEVELOPMENTCARD02, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD03, Constants.DEVELOPMENTCARDTYPEDEF05);
        emptyState.put(Constants.DEVELOPMENTCARD04, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD05, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD06, Constants.DEVELOPMENTCARDTYPEDEF04);
        emptyState.put(Constants.DEVELOPMENTCARD07, Constants.DEVELOPMENTCARDTYPEDEF02);
        emptyState.put(Constants.DEVELOPMENTCARD08, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD09, Constants.DEVELOPMENTCARDTYPEDEF07);
        emptyState.put(Constants.DEVELOPMENTCARD10, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD11, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD12, Constants.DEVELOPMENTCARDTYPEDEF06);
        emptyState.put(Constants.DEVELOPMENTCARD13, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD14, Constants.DEVELOPMENTCARDTYPEDEF01);
        emptyState.put(Constants.DEVELOPMENTCARD15, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD16, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD17, Constants.DEVELOPMENTCARDTYPEDEF08);
        emptyState.put(Constants.DEVELOPMENTCARD18, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD19, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD20, Constants.DEVELOPMENTCARDTYPEDEF03);
        emptyState.put(Constants.DEVELOPMENTCARD21, Constants.DEVELOPMENTCARDTYPEDEF01);
        emptyState.put(Constants.DEVELOPMENTCARD22, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.DEVELOPMENTCARD23, Constants.DEVELOPMENTCARDTYPEDEF02);
        emptyState.put(Constants.DEVELOPMENTCARD24, Constants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(Constants.SOLDIERCOUNTPB, 0);
        emptyState.put(Constants.SOLDIERCOUNTPR, 0);
        emptyState.put(Constants.SOLDIERCOUNTPY, 0);
        emptyState.put(Constants.SOLDIERCOUNTPG, 0);
        emptyState.put(Constants.ROBBER, Constants.HEX02);
        return emptyState;
    }
    
    // Create an initial empty state for testing
    // The normally randomized elements are defaulted to specific values
    // to allow for testing
    public static Map<String, Object> createEmptyStateNoDevCards() {
        Map<String, Object> emptyState = Maps.<String, Object>newHashMap();
        emptyState.put(Constants.TURN, Constants.PB);
        // These will be randomized in real game, set for testing purposes
        emptyState.put(Constants.HEX00, Constants.ORE);
        emptyState.put(Constants.HEX01, Constants.LUMBER);
        emptyState.put(Constants.HEX02, Constants.DESERT);
        emptyState.put(Constants.HEX03, Constants.GRAIN);
        emptyState.put(Constants.HEX04, Constants.LUMBER);
        emptyState.put(Constants.HEX05, Constants.WOOL);
        emptyState.put(Constants.HEX06, Constants.GRAIN);
        emptyState.put(Constants.HEX07, Constants.WOOL);
        emptyState.put(Constants.HEX08, Constants.BRICK);
        emptyState.put(Constants.HEX09, Constants.BRICK);
        emptyState.put(Constants.HEX10, Constants.WOOL);
        emptyState.put(Constants.HEX11, Constants.ORE);
        emptyState.put(Constants.HEX12, Constants.ORE);
        emptyState.put(Constants.HEX13, Constants.LUMBER);
        emptyState.put(Constants.HEX14, Constants.GRAIN);
        emptyState.put(Constants.HEX15, Constants.LUMBER);
        emptyState.put(Constants.HEX16, Constants.GRAIN);
        emptyState.put(Constants.HEX17, Constants.BRICK);
        emptyState.put(Constants.HEX18, Constants.LUMBER);
        // These will be randomized in real game, set for testing purposes
        emptyState.put(Constants.HARBOR00, Constants.HARBORTYPE03);
        emptyState.put(Constants.HARBOR01, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR02, Constants.HARBORTYPE01);
        emptyState.put(Constants.HARBOR03, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR04, Constants.HARBORTYPE05);
        emptyState.put(Constants.HARBOR05, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR06, Constants.HARBORTYPE02);
        emptyState.put(Constants.HARBOR07, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR08, Constants.HARBORTYPE04);
        // These will be randomized in real game, set for testing purposes
        // These will also need to be set to visible to no one pre game start
        emptyState.put(Constants.SOLDIERCOUNTPB, 0);
        emptyState.put(Constants.SOLDIERCOUNTPR, 0);
        emptyState.put(Constants.SOLDIERCOUNTPY, 0);
        emptyState.put(Constants.SOLDIERCOUNTPG, 0);
        emptyState.put(Constants.ROBBER, Constants.HEX02);
        return emptyState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createAddAssetState() {
        Map<String, Object> addRoadState = createEmptyState();
        addRoadState = changeState(addRoadState, Constants.TURN, Constants.PB);
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.PATH19, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH19);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD01PB, Constants.BRICK);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD02PB, Constants.WOOL);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD03PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD04PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD05PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD06PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD07PB, Constants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStateForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.PATH19, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH19);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD01PB, Constants.BRICK);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD02PB, Constants.WOOL);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD03PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD04PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD05PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD06PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD07PB, Constants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStatePlusDevelopmentCardForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.PATH19, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH19);
        addRoadState = changeState(addRoadState, Constants.DEVELOPMENTCARD00, Constants.DEVELOPMENTCARDTYPEDEF00);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD01PB, Constants.BRICK);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD04PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD06PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD07PB, Constants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStatePlusCityForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.CITY00PB);
        addRoadState = changeState(addRoadState, Constants.CITY00PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.PATH19, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH19);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD01PB, Constants.BRICK);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD02PB, Constants.WOOL);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStatePlusSettlementForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.NODE12, Constants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT01PB, Constants.NODE12);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.PATH19, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH19);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD04PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD05PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD06PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD07PB, Constants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStatePlusRoadForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.PATH19, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.PATH12, Constants.ROAD02PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH19);
        addRoadState = changeState(addRoadState, Constants.ROAD02PB, Constants.PATH12);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD02PB, Constants.WOOL);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD03PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD04PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD05PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD06PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD07PB, Constants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueNormalHarborTradeStateForBlue() {
        Map<String, Object> harborTradeState = createEmptyStateNoDevCards();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD03PB, Constants.LUMBER);
        
        return harborTradeState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBluePostAnyHarborTradeStateForBlue() {
        Map<String, Object> harborTradeState = createEmptyStateNoDevCards();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.ORE);
        
        return harborTradeState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueThreeToOneHarborTradeStateForBlue() {
        Map<String, Object> harborTradeState = createEmptyStateNoDevCards();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.NODE05, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE05);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        
        return harborTradeState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueTwoToOneLumberHarborTradeStateForBlue() {
        Map<String, Object> harborTradeState = createEmptyStateNoDevCards();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.NODE03, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE03);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        
        return harborTradeState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetPlusDevCardStateForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, Constants.TURN, Constants.PB);
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.PATH19, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH19);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD01PB, Constants.BRICK);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD02PB, Constants.WOOL);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD03PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD04PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD05PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD06PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD07PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.DEVELOPMENTCARD00, Constants.DEVELOPMENTCARDTYPEDEF00);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStateForNotBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, Constants.TURN, Constants.PB);
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.PATH19, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH19);
        
        return addRoadState;
    }

    // A specialized state to test failures caused by a player having exhausted their
    // allotment of specific assets
    public static Map<String, Object> createAllAssetsUsedState() {
        Map<String, Object> addRoadState = createEmptyState();
        addRoadState = changeState(addRoadState, Constants.TURN, Constants.PB);
        addRoadState = changeState(addRoadState, Constants.NODE21, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.NODE22, Constants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT02PB);
        addRoadState = changeState(addRoadState, Constants.NODE24, Constants.SETTLEMENT03PB);
        addRoadState = changeState(addRoadState, Constants.NODE25, Constants.SETTLEMENT04PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE21);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT01PB, Constants.NODE22);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT02PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT03PB, Constants.NODE24);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT04PB, Constants.NODE25);
        addRoadState = changeState(addRoadState, Constants.NODE43, Constants.CITY00PB);
        addRoadState = changeState(addRoadState, Constants.NODE44, Constants.CITY01PB);
        addRoadState = changeState(addRoadState, Constants.NODE45, Constants.CITY02PB);
        addRoadState = changeState(addRoadState, Constants.NODE46, Constants.CITY03PB);
        addRoadState = changeState(addRoadState, Constants.CITY00PB, Constants.NODE43);
        addRoadState = changeState(addRoadState, Constants.CITY01PB, Constants.NODE44);
        addRoadState = changeState(addRoadState, Constants.CITY02PB, Constants.NODE45);
        addRoadState = changeState(addRoadState, Constants.CITY03PB, Constants.NODE46);
        addRoadState = changeState(addRoadState, Constants.PATH23, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.PATH24, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.PATH25, Constants.ROAD02PB);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD03PB);
        addRoadState = changeState(addRoadState, Constants.PATH27, Constants.ROAD04PB);
        addRoadState = changeState(addRoadState, Constants.PATH28, Constants.ROAD05PB);
        addRoadState = changeState(addRoadState, Constants.PATH29, Constants.ROAD06PB);
        addRoadState = changeState(addRoadState, Constants.PATH30, Constants.ROAD07PB);
        addRoadState = changeState(addRoadState, Constants.PATH55, Constants.ROAD08PB);
        addRoadState = changeState(addRoadState, Constants.PATH56, Constants.ROAD09PB);
        addRoadState = changeState(addRoadState, Constants.PATH57, Constants.ROAD10PB);
        addRoadState = changeState(addRoadState, Constants.PATH58, Constants.ROAD11PB);
        addRoadState = changeState(addRoadState, Constants.PATH59, Constants.ROAD12PB);
        addRoadState = changeState(addRoadState, Constants.PATH60, Constants.ROAD13PB);
        addRoadState = changeState(addRoadState, Constants.PATH61, Constants.ROAD14PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH23);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH24);
        addRoadState = changeState(addRoadState, Constants.ROAD02PB, Constants.PATH25);
        addRoadState = changeState(addRoadState, Constants.ROAD03PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.ROAD04PB, Constants.PATH27);
        addRoadState = changeState(addRoadState, Constants.ROAD05PB, Constants.PATH28);
        addRoadState = changeState(addRoadState, Constants.ROAD06PB, Constants.PATH29);
        addRoadState = changeState(addRoadState, Constants.ROAD07PB, Constants.PATH30);
        addRoadState = changeState(addRoadState, Constants.ROAD08PB, Constants.PATH55);
        addRoadState = changeState(addRoadState, Constants.ROAD09PB, Constants.PATH56);
        addRoadState = changeState(addRoadState, Constants.ROAD10PB, Constants.PATH57);
        addRoadState = changeState(addRoadState, Constants.ROAD11PB, Constants.PATH58);
        addRoadState = changeState(addRoadState, Constants.ROAD12PB, Constants.PATH59);
        addRoadState = changeState(addRoadState, Constants.ROAD13PB, Constants.PATH60);
        addRoadState = changeState(addRoadState, Constants.ROAD14PB, Constants.PATH61);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD01PB, Constants.BRICK);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD02PB, Constants.WOOL);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD03PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD04PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD05PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD06PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD07PB, Constants.ORE);
        
        return addRoadState;
    }
    
    // a specialized state that allows for a user to claim victory the next move in
    // a variety of ways
    public static Map<String, Object> createPrepForVictoryState() {
        Map<String, Object> addRoadState = createEmptyState();
        addRoadState = changeState(addRoadState, Constants.TURN, Constants.PB);
        addRoadState = changeState(addRoadState, Constants.NODE21, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE21);
        addRoadState = changeState(addRoadState, Constants.NODE22, Constants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT01PB, Constants.NODE22);
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT02PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT02PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.NODE24, Constants.CITY00PB);
        addRoadState = changeState(addRoadState, Constants.CITY00PB, Constants.NODE24);
        addRoadState = changeState(addRoadState, Constants.NODE25, Constants.CITY01PB);
        addRoadState = changeState(addRoadState, Constants.CITY01PB, Constants.NODE25);
        addRoadState = changeState(addRoadState, Constants.NODE26, Constants.CITY02PB);
        addRoadState = changeState(addRoadState, Constants.CITY02PB, Constants.NODE26);
        addRoadState = changeState(addRoadState, Constants.PATH23, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH23);
        addRoadState = changeState(addRoadState, Constants.PATH24, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH24);
        addRoadState = changeState(addRoadState, Constants.PATH25, Constants.ROAD02PB);
        addRoadState = changeState(addRoadState, Constants.ROAD02PB, Constants.PATH25);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD03PB);
        addRoadState = changeState(addRoadState, Constants.ROAD03PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.PATH27, Constants.ROAD04PB);
        addRoadState = changeState(addRoadState, Constants.ROAD04PB, Constants.PATH27);
        addRoadState = changeState(addRoadState, Constants.PATH28, Constants.ROAD05PB);
        addRoadState = changeState(addRoadState, Constants.ROAD05PB, Constants.PATH28);
        addRoadState = changeState(addRoadState, Constants.PATH29, Constants.ROAD06PB);
        addRoadState = changeState(addRoadState, Constants.ROAD06PB, Constants.PATH29);
        addRoadState = changeState(addRoadState, Constants.PATH30, Constants.ROAD07PB);
        addRoadState = changeState(addRoadState, Constants.ROAD07PB, Constants.PATH30);
        addRoadState = changeState(addRoadState, Constants.PATH31, Constants.ROAD08PB);
        addRoadState = changeState(addRoadState, Constants.ROAD08PB, Constants.PATH31);
        addRoadState = changeState(addRoadState, Constants.PATH32, Constants.ROAD09PB);
        addRoadState = changeState(addRoadState, Constants.ROAD09PB, Constants.PATH32);
        addRoadState = changeState(addRoadState, Constants.PATH38, Constants.ROAD10PB);
        addRoadState = changeState(addRoadState, Constants.ROAD10PB, Constants.PATH38);
        addRoadState = changeState(addRoadState, Constants.PATH48, Constants.ROAD11PB);
        addRoadState = changeState(addRoadState, Constants.ROAD11PB, Constants.PATH38);
        addRoadState = changeState(addRoadState, Constants.NODE33, Constants.SETTLEMENT00PR);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PR, Constants.NODE33);
        addRoadState = changeState(addRoadState, Constants.PATH39, Constants.ROAD00PR);
        addRoadState = changeState(addRoadState, Constants.ROAD00PR, Constants.PATH39);
        addRoadState = changeState(addRoadState, Constants.PATH40, Constants.ROAD01PR);
        addRoadState = changeState(addRoadState, Constants.ROAD01PR, Constants.PATH40);
        addRoadState = changeState(addRoadState, Constants.PATH41, Constants.ROAD02PR);
        addRoadState = changeState(addRoadState, Constants.ROAD02PR, Constants.PATH41);
        addRoadState = changeState(addRoadState, Constants.PATH42, Constants.ROAD03PR);
        addRoadState = changeState(addRoadState, Constants.ROAD03PR, Constants.PATH42);
        addRoadState = changeState(addRoadState, Constants.PATH43, Constants.ROAD04PR);
        addRoadState = changeState(addRoadState, Constants.ROAD04PR, Constants.PATH43);
        addRoadState = changeState(addRoadState, Constants.PATH44, Constants.ROAD05PR);
        addRoadState = changeState(addRoadState, Constants.ROAD05PR, Constants.PATH44);
        addRoadState = changeState(addRoadState, Constants.PATH45, Constants.ROAD06PR);
        addRoadState = changeState(addRoadState, Constants.ROAD06PR, Constants.PATH45);
        addRoadState = changeState(addRoadState, Constants.PATH46, Constants.ROAD07PR);
        addRoadState = changeState(addRoadState, Constants.ROAD07PR, Constants.PATH46);
        addRoadState = changeState(addRoadState, Constants.PATH47, Constants.ROAD08PR);
        addRoadState = changeState(addRoadState, Constants.ROAD08PR, Constants.PATH47);
        addRoadState = changeState(addRoadState, Constants.PATH53, Constants.ROAD09PR);
        addRoadState = changeState(addRoadState, Constants.ROAD09PR, Constants.PATH53);
        addRoadState = changeState(addRoadState, Constants.PATH61, Constants.ROAD10PR);
        addRoadState = changeState(addRoadState, Constants.ROAD10PR, Constants.PATH61);
        addRoadState = changeState(addRoadState, Constants.PATH65, Constants.ROAD11PR);
        addRoadState = changeState(addRoadState, Constants.ROAD11PR, Constants.PATH65);
        addRoadState = changeState(addRoadState, Constants.LONGESTROAD, Constants.PR);
        addRoadState = changeState(addRoadState, Constants.SOLDIERCOUNTPB, 6);
        addRoadState = changeState(addRoadState, Constants.SOLDIERCOUNTPR, 6);
        addRoadState = changeState(addRoadState, Constants.LARGESTARMY, Constants.PR);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD01PB, Constants.BRICK);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD02PB, Constants.WOOL);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD03PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD04PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD05PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD06PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD07PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.DEVELOPMENTCARD00, Constants.DEVELOPMENTCARDTYPEDEF00);
        
        return addRoadState;
    }
    
    // a specialized state that allows for a user to test the victory screen
    public static Map<String, Object> createBlueVictoryStateForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, Constants.TURN, Constants.PB);
        addRoadState = changeState(addRoadState, Constants.NODE21, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE21);
        addRoadState = changeState(addRoadState, Constants.NODE22, Constants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT01PB, Constants.NODE22);
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT02PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT02PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.NODE24, Constants.CITY00PB);
        addRoadState = changeState(addRoadState, Constants.CITY00PB, Constants.NODE24);
        addRoadState = changeState(addRoadState, Constants.NODE25, Constants.CITY01PB);
        addRoadState = changeState(addRoadState, Constants.CITY01PB, Constants.NODE25);
        addRoadState = changeState(addRoadState, Constants.NODE26, Constants.CITY02PB);
        addRoadState = changeState(addRoadState, Constants.CITY02PB, Constants.NODE26);
        addRoadState = changeState(addRoadState, Constants.PATH23, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH23);
        addRoadState = changeState(addRoadState, Constants.PATH24, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH24);
        addRoadState = changeState(addRoadState, Constants.PATH25, Constants.ROAD02PB);
        addRoadState = changeState(addRoadState, Constants.ROAD02PB, Constants.PATH25);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD03PB);
        addRoadState = changeState(addRoadState, Constants.ROAD03PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.PATH27, Constants.ROAD04PB);
        addRoadState = changeState(addRoadState, Constants.ROAD04PB, Constants.PATH27);
        addRoadState = changeState(addRoadState, Constants.PATH28, Constants.ROAD05PB);
        addRoadState = changeState(addRoadState, Constants.ROAD05PB, Constants.PATH28);
        addRoadState = changeState(addRoadState, Constants.PATH29, Constants.ROAD06PB);
        addRoadState = changeState(addRoadState, Constants.ROAD06PB, Constants.PATH29);
        addRoadState = changeState(addRoadState, Constants.PATH30, Constants.ROAD07PB);
        addRoadState = changeState(addRoadState, Constants.ROAD07PB, Constants.PATH30);
        addRoadState = changeState(addRoadState, Constants.PATH31, Constants.ROAD08PB);
        addRoadState = changeState(addRoadState, Constants.ROAD08PB, Constants.PATH31);
        addRoadState = changeState(addRoadState, Constants.PATH32, Constants.ROAD09PB);
        addRoadState = changeState(addRoadState, Constants.ROAD09PB, Constants.PATH32);
        addRoadState = changeState(addRoadState, Constants.PATH38, Constants.ROAD10PB);
        addRoadState = changeState(addRoadState, Constants.ROAD10PB, Constants.PATH38);
        addRoadState = changeState(addRoadState, Constants.PATH48, Constants.ROAD11PB);
        addRoadState = changeState(addRoadState, Constants.ROAD11PB, Constants.PATH38);
        addRoadState = changeState(addRoadState, Constants.NODE33, Constants.SETTLEMENT00PR);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PR, Constants.NODE33);
        addRoadState = changeState(addRoadState, Constants.PATH39, Constants.ROAD00PR);
        addRoadState = changeState(addRoadState, Constants.ROAD00PR, Constants.PATH39);
        addRoadState = changeState(addRoadState, Constants.PATH40, Constants.ROAD01PR);
        addRoadState = changeState(addRoadState, Constants.ROAD01PR, Constants.PATH40);
        addRoadState = changeState(addRoadState, Constants.PATH41, Constants.ROAD02PR);
        addRoadState = changeState(addRoadState, Constants.ROAD02PR, Constants.PATH41);
        addRoadState = changeState(addRoadState, Constants.PATH42, Constants.ROAD03PR);
        addRoadState = changeState(addRoadState, Constants.ROAD03PR, Constants.PATH42);
        addRoadState = changeState(addRoadState, Constants.PATH43, Constants.ROAD04PR);
        addRoadState = changeState(addRoadState, Constants.ROAD04PR, Constants.PATH43);
        addRoadState = changeState(addRoadState, Constants.PATH44, Constants.ROAD05PR);
        addRoadState = changeState(addRoadState, Constants.ROAD05PR, Constants.PATH44);
        addRoadState = changeState(addRoadState, Constants.PATH45, Constants.ROAD06PR);
        addRoadState = changeState(addRoadState, Constants.ROAD06PR, Constants.PATH45);
        addRoadState = changeState(addRoadState, Constants.PATH46, Constants.ROAD07PR);
        addRoadState = changeState(addRoadState, Constants.ROAD07PR, Constants.PATH46);
        addRoadState = changeState(addRoadState, Constants.PATH47, Constants.ROAD08PR);
        addRoadState = changeState(addRoadState, Constants.ROAD08PR, Constants.PATH47);
        addRoadState = changeState(addRoadState, Constants.PATH53, Constants.ROAD09PR);
        addRoadState = changeState(addRoadState, Constants.ROAD09PR, Constants.PATH53);
        addRoadState = changeState(addRoadState, Constants.PATH61, Constants.ROAD10PR);
        addRoadState = changeState(addRoadState, Constants.ROAD10PR, Constants.PATH61);
        addRoadState = changeState(addRoadState, Constants.PATH65, Constants.ROAD11PR);
        addRoadState = changeState(addRoadState, Constants.ROAD11PR, Constants.PATH65);
        addRoadState = changeState(addRoadState, Constants.LONGESTROAD, Constants.PR);
        addRoadState = changeState(addRoadState, Constants.SOLDIERCOUNTPB, 6);
        addRoadState = changeState(addRoadState, Constants.SOLDIERCOUNTPR, 6);
        addRoadState = changeState(addRoadState, Constants.LARGESTARMY, Constants.PR);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD01PB, Constants.BRICK);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD02PB, Constants.WOOL);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD03PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD04PB, Constants.GRAIN);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD05PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD06PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.RESOURCECARD07PB, Constants.ORE);
        addRoadState = changeState(addRoadState, Constants.DEVELOPMENTCARD00, Constants.DEVELOPMENTCARDTYPEDEF00);
        addRoadState = changeState(addRoadState, Constants.NODE37, Constants.SETTLEMENT03PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT03PB, Constants.NODE37);
        
        return addRoadState;
    }
    
    // a specialized state that allows for a user to test the victory screen
    public static Map<String, Object> createBlueVictoryStateForNotBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, Constants.TURN, Constants.PB);
        addRoadState = changeState(addRoadState, Constants.NODE21, Constants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PB, Constants.NODE21);
        addRoadState = changeState(addRoadState, Constants.NODE22, Constants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT01PB, Constants.NODE22);
        addRoadState = changeState(addRoadState, Constants.NODE23, Constants.SETTLEMENT02PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT02PB, Constants.NODE23);
        addRoadState = changeState(addRoadState, Constants.NODE24, Constants.CITY00PB);
        addRoadState = changeState(addRoadState, Constants.CITY00PB, Constants.NODE24);
        addRoadState = changeState(addRoadState, Constants.NODE25, Constants.CITY01PB);
        addRoadState = changeState(addRoadState, Constants.CITY01PB, Constants.NODE25);
        addRoadState = changeState(addRoadState, Constants.NODE26, Constants.CITY02PB);
        addRoadState = changeState(addRoadState, Constants.CITY02PB, Constants.NODE26);
        addRoadState = changeState(addRoadState, Constants.PATH23, Constants.ROAD00PB);
        addRoadState = changeState(addRoadState, Constants.ROAD00PB, Constants.PATH23);
        addRoadState = changeState(addRoadState, Constants.PATH24, Constants.ROAD01PB);
        addRoadState = changeState(addRoadState, Constants.ROAD01PB, Constants.PATH24);
        addRoadState = changeState(addRoadState, Constants.PATH25, Constants.ROAD02PB);
        addRoadState = changeState(addRoadState, Constants.ROAD02PB, Constants.PATH25);
        addRoadState = changeState(addRoadState, Constants.PATH26, Constants.ROAD03PB);
        addRoadState = changeState(addRoadState, Constants.ROAD03PB, Constants.PATH26);
        addRoadState = changeState(addRoadState, Constants.PATH27, Constants.ROAD04PB);
        addRoadState = changeState(addRoadState, Constants.ROAD04PB, Constants.PATH27);
        addRoadState = changeState(addRoadState, Constants.PATH28, Constants.ROAD05PB);
        addRoadState = changeState(addRoadState, Constants.ROAD05PB, Constants.PATH28);
        addRoadState = changeState(addRoadState, Constants.PATH29, Constants.ROAD06PB);
        addRoadState = changeState(addRoadState, Constants.ROAD06PB, Constants.PATH29);
        addRoadState = changeState(addRoadState, Constants.PATH30, Constants.ROAD07PB);
        addRoadState = changeState(addRoadState, Constants.ROAD07PB, Constants.PATH30);
        addRoadState = changeState(addRoadState, Constants.PATH31, Constants.ROAD08PB);
        addRoadState = changeState(addRoadState, Constants.ROAD08PB, Constants.PATH31);
        addRoadState = changeState(addRoadState, Constants.PATH32, Constants.ROAD09PB);
        addRoadState = changeState(addRoadState, Constants.ROAD09PB, Constants.PATH32);
        addRoadState = changeState(addRoadState, Constants.PATH38, Constants.ROAD10PB);
        addRoadState = changeState(addRoadState, Constants.ROAD10PB, Constants.PATH38);
        addRoadState = changeState(addRoadState, Constants.PATH48, Constants.ROAD11PB);
        addRoadState = changeState(addRoadState, Constants.ROAD11PB, Constants.PATH38);
        addRoadState = changeState(addRoadState, Constants.NODE33, Constants.SETTLEMENT00PR);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT00PR, Constants.NODE33);
        addRoadState = changeState(addRoadState, Constants.PATH39, Constants.ROAD00PR);
        addRoadState = changeState(addRoadState, Constants.ROAD00PR, Constants.PATH39);
        addRoadState = changeState(addRoadState, Constants.PATH40, Constants.ROAD01PR);
        addRoadState = changeState(addRoadState, Constants.ROAD01PR, Constants.PATH40);
        addRoadState = changeState(addRoadState, Constants.PATH41, Constants.ROAD02PR);
        addRoadState = changeState(addRoadState, Constants.ROAD02PR, Constants.PATH41);
        addRoadState = changeState(addRoadState, Constants.PATH42, Constants.ROAD03PR);
        addRoadState = changeState(addRoadState, Constants.ROAD03PR, Constants.PATH42);
        addRoadState = changeState(addRoadState, Constants.PATH43, Constants.ROAD04PR);
        addRoadState = changeState(addRoadState, Constants.ROAD04PR, Constants.PATH43);
        addRoadState = changeState(addRoadState, Constants.PATH44, Constants.ROAD05PR);
        addRoadState = changeState(addRoadState, Constants.ROAD05PR, Constants.PATH44);
        addRoadState = changeState(addRoadState, Constants.PATH45, Constants.ROAD06PR);
        addRoadState = changeState(addRoadState, Constants.ROAD06PR, Constants.PATH45);
        addRoadState = changeState(addRoadState, Constants.PATH46, Constants.ROAD07PR);
        addRoadState = changeState(addRoadState, Constants.ROAD07PR, Constants.PATH46);
        addRoadState = changeState(addRoadState, Constants.PATH47, Constants.ROAD08PR);
        addRoadState = changeState(addRoadState, Constants.ROAD08PR, Constants.PATH47);
        addRoadState = changeState(addRoadState, Constants.PATH53, Constants.ROAD09PR);
        addRoadState = changeState(addRoadState, Constants.ROAD09PR, Constants.PATH53);
        addRoadState = changeState(addRoadState, Constants.PATH61, Constants.ROAD10PR);
        addRoadState = changeState(addRoadState, Constants.ROAD10PR, Constants.PATH61);
        addRoadState = changeState(addRoadState, Constants.PATH65, Constants.ROAD11PR);
        addRoadState = changeState(addRoadState, Constants.ROAD11PR, Constants.PATH65);
        addRoadState = changeState(addRoadState, Constants.LONGESTROAD, Constants.PR);
        addRoadState = changeState(addRoadState, Constants.SOLDIERCOUNTPB, 6);
        addRoadState = changeState(addRoadState, Constants.SOLDIERCOUNTPR, 6);
        addRoadState = changeState(addRoadState, Constants.LARGESTARMY, Constants.PR);
        addRoadState = changeState(addRoadState, Constants.NODE37, Constants.SETTLEMENT03PB);
        addRoadState = changeState(addRoadState, Constants.SETTLEMENT03PB, Constants.NODE37);
        
        return addRoadState;
    }
    
    // A Mapping of what parameters VerifyMove is looking for
    //
    // VerifyMove - YourPlayerId - int
    //              Player List - List
    //              State - Map
    //              LastState - Map
    //              LastMove - List
    //              LastMovePlayerId - Int
    
    /*------------------------------------------------------------------------------------*/
    /*                                   TEST CASES                                       */
    /*------------------------------------------------------------------------------------*/
    
    
    
    // Legal Test
    // Normal Harbor Trade
    @Test
    public void testLegalNormalHarborTrade() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD03PB, Constants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // Normal Harbor Trade
    // Resources Not The Same
    @Test
    public void testIllegalNormalHarborTradeResourcesNotSame() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD03PB, Constants.WOOL);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        testName = "testIllegalNormalHarborTradeResourcesNotSame";
        
        assertHacker(verifyMove);
    }

    // Illegal Test
    // Normal Harbor Trade
    // Not Enough Resources
    @Test
    public void testIllegalNormalHarborTradeNotEnoughResources() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalNormalHarborTradeNotEnoughResources";
        
        assertHacker(verifyMove);
    }

    // Legal Test
    // 3 For 1 Harbor Trade
    @Test
    public void testLegalThreeForOneHarborTrade() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.NODE05, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE05);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // 3 For 1 Harbor Trade
    // Resources Not The Same
    @Test
    public void testIllegalThreeForOneHarborTradeResourcesNotSame() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.NODE05, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE05);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD02PB, Constants.WOOL);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalThreeForOneHarborTradeResourcesNotSame";
        
        assertHacker(verifyMove);
    }

    // Illegal Test
    // 3 For 1 Harbor Trade
    // Not Enough Resources
    @Test
    public void testIllegalThreeForOneHarborTradeNotEnoughResources() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.NODE05, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE05);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalThreeForOneHarborTradeNotEnoughResources";
        
        assertHacker(verifyMove);
    }

    // Legal Test
    // 2 For 1 Lumber Harbor Trade
    @Test
    public void testLegalTwoForOneLumberHarborTrade() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.NODE03, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE03);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // 2 For 1 Lumber Harbor Trade
    // Resources Not The Same
    @Test
    public void testIllegalTwoForOneLumberHarborTradeResourcesNotSame() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.NODE03, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE03);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD01PB, Constants.WOOL);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalTwoForOneLumberHarborTradeResourcesNotSame";
        
        assertHacker(verifyMove);
    }

    // Illegal Test
    // 2 For 1 Lumber Harbor Trade
    // Not Enough Resources
    @Test
    public void testIllegalTwoForOneLumberHarborTradeNotEnoughResources() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(
                harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(
                harborTradeState, Constants.NODE03, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE03);
        harborTradeState = changeState(
                harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalTwoForOneLumberHarborTradeNotEnoughResources";
        
        assertHacker(verifyMove);
    }
    
    // Legal Test
    // Add Road
    @Test
    public void testLegalAddRoad() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.PATH12, Constants.ROAD02PB),
                new Set(Constants.ROAD02PB, Constants.PATH12),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // Add Road
    // No Adjacent Road
    @Test
    public void testIllegalAddRoadNoAdjacentRoad() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.PATH66, Constants.ROAD02PB),
                new Set(Constants.ROAD02PB, Constants.PATH66),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddRoadNoAdjacentRoad";

        assertHacker(verifyMove);
    }

    // Illegal Test
    // Add Road
    // Incorrect Resources
    @Test
    public void testIllegalAddRoadIncorrectResources() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.PATH12, Constants.ROAD02PB),
                new Set(Constants.ROAD02PB, Constants.PATH12),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD02PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddRoadIncorrectResources";

        assertHacker(verifyMove);
    }

    // Illegal Test
    // Add Road
    // Path Taken
    @Test
    public void testIllegalAddRoadPathTaken() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.PATH26, Constants.ROAD02PB),
                new Set(Constants.ROAD02PB, Constants.PATH26),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddRoadPathTaken";

        assertHacker(verifyMove);
    }

    // Illegal Test
    // Add Road
    // None Left
    @Test
    public void testIllegalAddRoadNoneLeft() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.PATH31, Constants.ROAD14PB),
                new Set(Constants.ROAD14PB, Constants.PATH31),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addRoad),
                createAllAssetsUsedState(),
                addRoad,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddRoadNoneLeft";

        assertHacker(verifyMove);
    }

    // Legal Test
    // Add Settlement
    @Test
    public void testLegalAddSettlement() {
        ImmutableList<Operation> addSettlement = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE12, Constants.SETTLEMENT01PB),
                new Set(Constants.SETTLEMENT01PB, Constants.NODE12),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // Add Settlement
    // No Road
    @Test
    public void testIllegalAddSettlementNoRoad() {
        ImmutableList<Operation> addSettlement = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE25, Constants.SETTLEMENT01PB),
                new Set(Constants.SETTLEMENT01PB, Constants.NODE25),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddSettlementNoRoad";

        assertHacker(verifyMove);
    }

    // Illegal Test
    // Add Settlement
    // Settlement Too Close
    @Test
    public void testIllegalAddSettlementTooClose() {
        ImmutableList<Operation> addSettlement = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE17, Constants.SETTLEMENT01PB),
                new Set(Constants.SETTLEMENT01PB, Constants.NODE17),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddSettlementTooClose";

        assertHacker(verifyMove);
    }

    // Illegal Test
    // Add Settlement
    // Incorrect Resources
    @Test
    public void testIllegalAddSettlementIncorrectResources() {
        ImmutableList<Operation> addSettlement = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE12, Constants.SETTLEMENT01PB),
                new Set(Constants.SETTLEMENT01PB, Constants.NODE12),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD05PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD05PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddSettlementIncorrectResources";

        assertHacker(verifyMove);
    }

    // Illegal Test
    // Add Settlement
    // Node Taken
    @Test
    public void testIllegalAddSettlementNodeTaken() {
        ImmutableList<Operation> addSettlement = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE23, Constants.SETTLEMENT01PB),
                new Set(Constants.SETTLEMENT01PB, Constants.NODE23),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddSettlementNodeTaken";

        assertHacker(verifyMove);
    }

    // Illegal Test
    // Add Settlement
    // None Left
    @Test
    public void testIllegalAddSettlementNoneLeft() {
        ImmutableList<Operation> addSettlement = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE12, Constants.SETTLEMENT04PB),
                new Set(Constants.SETTLEMENT04PB, Constants.NODE12),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addSettlement),
                createAllAssetsUsedState(),
                addSettlement,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddSettlementNoneLeft";

        assertHacker(verifyMove);
    }

    // Legal Test
    // Add City
    @Test
    public void testLegalAddCity() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Delete(Constants.SETTLEMENT00PB),
                new Set(Constants.NODE23, Constants.CITY00PB),
                new Set(Constants.CITY00PB, Constants.NODE23),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new SetVisibility(Constants.RESOURCECARD04PB),
                new SetVisibility(Constants.RESOURCECARD05PB),
                new SetVisibility(Constants.RESOURCECARD06PB),
                new SetVisibility(Constants.RESOURCECARD07PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD04PB),
                new Delete(Constants.RESOURCECARD05PB),
                new Delete(Constants.RESOURCECARD06PB),
                new Delete(Constants.RESOURCECARD07PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addCity),
                createAddAssetState(),
                addCity,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // Add City
    // No Settlement
    @Test
    public void testIllegalAddCityNoSettlement() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE24, Constants.CITY00PB),
                new Set(Constants.CITY00PB, Constants.NODE24),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new SetVisibility(Constants.RESOURCECARD04PB),
                new SetVisibility(Constants.RESOURCECARD05PB),
                new SetVisibility(Constants.RESOURCECARD06PB),
                new SetVisibility(Constants.RESOURCECARD07PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD04PB),
                new Delete(Constants.RESOURCECARD05PB),
                new Delete(Constants.RESOURCECARD06PB),
                new Delete(Constants.RESOURCECARD07PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addCity),
                createAddAssetState(),
                addCity,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddCityNoSettlement";

        assertHacker(verifyMove);
    }

    // Illegal Test
    // Add City
    // Incorrect Resources
    @Test
    public void testIllegalAddCityIncorrectResources() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Delete(Constants.SETTLEMENT00PB),
                new Set(Constants.NODE23, Constants.CITY00PB),
                new Set(Constants.CITY00PB, Constants.NODE23),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD04PB),
                new SetVisibility(Constants.RESOURCECARD05PB),
                new SetVisibility(Constants.RESOURCECARD06PB),
                new SetVisibility(Constants.RESOURCECARD07PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD04PB),
                new Delete(Constants.RESOURCECARD05PB),
                new Delete(Constants.RESOURCECARD06PB),
                new Delete(Constants.RESOURCECARD07PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addCity),
                createAddAssetState(),
                addCity,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddCityIncorrectResources";

        assertHacker(verifyMove);
    }

    // Illegal Test
    // Add City
    // None Left
    @Test
    public void testIllegalAddCityNoneLeft() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Delete(Constants.SETTLEMENT00PB),
                new Set(Constants.NODE21, Constants.CITY03PB),
                new Set(Constants.CITY03PB, Constants.NODE21),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new SetVisibility(Constants.RESOURCECARD04PB),
                new SetVisibility(Constants.RESOURCECARD05PB),
                new SetVisibility(Constants.RESOURCECARD06PB),
                new SetVisibility(Constants.RESOURCECARD07PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD04PB),
                new Delete(Constants.RESOURCECARD05PB),
                new Delete(Constants.RESOURCECARD06PB),
                new Delete(Constants.RESOURCECARD07PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addCity),
                createAllAssetsUsedState(),
                addCity,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddCityNoneLeft";

        assertHacker(verifyMove);
    }

    // Legal Test
    // Purchase Development Card
    @Test
    public void testLegalPurchaseDevelopmentCard() {
        ImmutableList<Operation> addDevelopmentCard = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.DEVELOPMENTCARD00, Constants.visibleToPB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new SetVisibility(Constants.RESOURCECARD05PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD05PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addDevelopmentCard),
                createAddAssetState(),
                addDevelopmentCard,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // Purchase Development Card
    // Incorrect Resources
    @Test
    public void testIllegalPurchaseDevelopmentCardIncorrectResources() {
        ImmutableList<Operation> addDevelopmentCard = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.DEVELOPMENTCARD00, Constants.visibleToPB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new SetVisibility(Constants.RESOURCECARD05PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD05PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addDevelopmentCard),
                createAddAssetState(),
                addDevelopmentCard,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalPurchaseDevelopmentCardIncorrectResources";

        assertHacker(verifyMove);
    }

    // Legal Test
    // End Game
    // Via Building Settlement
    @Test
    public void testEndGameByBuildingSettlement() {
        ImmutableList<Operation> addSettlement = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE37, Constants.SETTLEMENT03PB),
                new Set(Constants.SETTLEMENT03PB, Constants.NODE37),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB),
                new EndGame(Constants.pbId)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), addSettlement),
                createPrepForVictoryState(),
                addSettlement,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Legal Test
    // End Game
    // Via Building City
    @Test
    public void testEndGameByBuildingCity() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Delete(Constants.SETTLEMENT00PB),
                new Set(Constants.NODE21, Constants.CITY03PB),
                new Set(Constants.CITY03PB, Constants.NODE21),
                new SetVisibility(Constants.RESOURCECARD03PB),
                new SetVisibility(Constants.RESOURCECARD04PB),
                new SetVisibility(Constants.RESOURCECARD05PB),
                new SetVisibility(Constants.RESOURCECARD06PB),
                new SetVisibility(Constants.RESOURCECARD07PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD04PB),
                new Delete(Constants.RESOURCECARD05PB),
                new Delete(Constants.RESOURCECARD06PB),
                new Delete(Constants.RESOURCECARD07PB),
                new EndGame(Constants.pbId)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), addCity),
                createPrepForVictoryState(),
                addCity,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Legal Test
    // End Game
    // Via Building Road And Taking Longest Road
    @Test
    public void testEndGameByGettingLongestRoad() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.PATH33, Constants.ROAD12PB),
                new Set(Constants.ROAD12PB, Constants.PATH33),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Set(Constants.LONGESTROAD, Constants.PB),
                new EndGame(Constants.pbId)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), addRoad),
                createPrepForVictoryState(),
                addRoad,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Legal Test
    // End Game
    // Via Playing Soldier Card And Taking Largest Army
    @Test
    public void testEndGameByGettingLargestArmy() {
        ImmutableList<Operation> playDevelopmentCard = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.DEVELOPMENTCARD00, Constants.visibleToNone), //this is a soldier card
                new Set(Constants.ROBBER, Constants.HEX12),
                new Set(Constants.SOLDIERCOUNTPB, 7),
                new Set(Constants.LARGESTARMY, Constants.PB),
                new EndGame(Constants.pbId)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), playDevelopmentCard),
                createPrepForVictoryState(),
                playDevelopmentCard,
                Constants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }
}
