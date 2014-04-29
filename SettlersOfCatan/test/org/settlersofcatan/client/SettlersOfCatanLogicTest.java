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
        emptyState.put(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        // These will be randomized in real game, set for testing purposes
        emptyState.put(SettlersOfCatanConstants.HEX00, SettlersOfCatanConstants.ORE);
        emptyState.put(SettlersOfCatanConstants.HEX01, SettlersOfCatanConstants.LUMBER);
        emptyState.put(SettlersOfCatanConstants.HEX02, SettlersOfCatanConstants.DESERT);
        emptyState.put(SettlersOfCatanConstants.HEX03, SettlersOfCatanConstants.GRAIN);
        emptyState.put(SettlersOfCatanConstants.HEX04, SettlersOfCatanConstants.LUMBER);
        emptyState.put(SettlersOfCatanConstants.HEX05, SettlersOfCatanConstants.WOOL);
        emptyState.put(SettlersOfCatanConstants.HEX06, SettlersOfCatanConstants.GRAIN);
        emptyState.put(SettlersOfCatanConstants.HEX07, SettlersOfCatanConstants.WOOL);
        emptyState.put(SettlersOfCatanConstants.HEX08, SettlersOfCatanConstants.BRICK);
        emptyState.put(SettlersOfCatanConstants.HEX09, SettlersOfCatanConstants.BRICK);
        emptyState.put(SettlersOfCatanConstants.HEX10, SettlersOfCatanConstants.WOOL);
        emptyState.put(SettlersOfCatanConstants.HEX11, SettlersOfCatanConstants.ORE);
        emptyState.put(SettlersOfCatanConstants.HEX12, SettlersOfCatanConstants.ORE);
        emptyState.put(SettlersOfCatanConstants.HEX13, SettlersOfCatanConstants.LUMBER);
        emptyState.put(SettlersOfCatanConstants.HEX14, SettlersOfCatanConstants.GRAIN);
        emptyState.put(SettlersOfCatanConstants.HEX15, SettlersOfCatanConstants.LUMBER);
        emptyState.put(SettlersOfCatanConstants.HEX16, SettlersOfCatanConstants.GRAIN);
        emptyState.put(SettlersOfCatanConstants.HEX17, SettlersOfCatanConstants.BRICK);
        emptyState.put(SettlersOfCatanConstants.HEX18, SettlersOfCatanConstants.LUMBER);
        // These will be randomized in real game, set for testing purposes
        emptyState.put(SettlersOfCatanConstants.HARBOR00, SettlersOfCatanConstants.HARBORTYPE03);
        emptyState.put(SettlersOfCatanConstants.HARBOR01, SettlersOfCatanConstants.HARBORTYPE00);
        emptyState.put(SettlersOfCatanConstants.HARBOR02, SettlersOfCatanConstants.HARBORTYPE01);
        emptyState.put(SettlersOfCatanConstants.HARBOR03, SettlersOfCatanConstants.HARBORTYPE00);
        emptyState.put(SettlersOfCatanConstants.HARBOR04, SettlersOfCatanConstants.HARBORTYPE05);
        emptyState.put(SettlersOfCatanConstants.HARBOR05, SettlersOfCatanConstants.HARBORTYPE00);
        emptyState.put(SettlersOfCatanConstants.HARBOR06, SettlersOfCatanConstants.HARBORTYPE02);
        emptyState.put(SettlersOfCatanConstants.HARBOR07, SettlersOfCatanConstants.HARBORTYPE00);
        emptyState.put(SettlersOfCatanConstants.HARBOR08, SettlersOfCatanConstants.HARBORTYPE04);
        // These will be randomized in real game, set for testing purposes
        // These will also need to be set to visible to no one pre game start
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD00, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD01, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF03);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD02, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD03, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF05);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD04, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD05, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD06, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF04);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD07, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF02);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD08, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD09, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF07);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD10, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD11, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD12, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF06);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD13, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD14, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF01);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD15, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD16, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD17, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF08);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD18, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD19, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD20, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF03);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD21, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF01);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD22, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD23, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF02);
        emptyState.put(SettlersOfCatanConstants.DEVELOPMENTCARD24, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        emptyState.put(SettlersOfCatanConstants.SOLDIERCOUNTPB, 0);
        emptyState.put(SettlersOfCatanConstants.SOLDIERCOUNTPR, 0);
        emptyState.put(SettlersOfCatanConstants.SOLDIERCOUNTPY, 0);
        emptyState.put(SettlersOfCatanConstants.SOLDIERCOUNTPG, 0);
        emptyState.put(SettlersOfCatanConstants.ROBBER, SettlersOfCatanConstants.HEX02);
        return emptyState;
    }
    
    // Create an initial empty state for testing
    // The normally randomized elements are defaulted to specific values
    // to allow for testing
    public static Map<String, Object> createEmptyStateNoDevCards() {
        Map<String, Object> emptyState = Maps.<String, Object>newHashMap();
        emptyState.put(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        // These will be randomized in real game, set for testing purposes
        emptyState.put(SettlersOfCatanConstants.HEX00, SettlersOfCatanConstants.ORE);
        emptyState.put(SettlersOfCatanConstants.HEX01, SettlersOfCatanConstants.LUMBER);
        emptyState.put(SettlersOfCatanConstants.HEX02, SettlersOfCatanConstants.DESERT);
        emptyState.put(SettlersOfCatanConstants.HEX03, SettlersOfCatanConstants.GRAIN);
        emptyState.put(SettlersOfCatanConstants.HEX04, SettlersOfCatanConstants.LUMBER);
        emptyState.put(SettlersOfCatanConstants.HEX05, SettlersOfCatanConstants.WOOL);
        emptyState.put(SettlersOfCatanConstants.HEX06, SettlersOfCatanConstants.GRAIN);
        emptyState.put(SettlersOfCatanConstants.HEX07, SettlersOfCatanConstants.WOOL);
        emptyState.put(SettlersOfCatanConstants.HEX08, SettlersOfCatanConstants.BRICK);
        emptyState.put(SettlersOfCatanConstants.HEX09, SettlersOfCatanConstants.BRICK);
        emptyState.put(SettlersOfCatanConstants.HEX10, SettlersOfCatanConstants.WOOL);
        emptyState.put(SettlersOfCatanConstants.HEX11, SettlersOfCatanConstants.ORE);
        emptyState.put(SettlersOfCatanConstants.HEX12, SettlersOfCatanConstants.ORE);
        emptyState.put(SettlersOfCatanConstants.HEX13, SettlersOfCatanConstants.LUMBER);
        emptyState.put(SettlersOfCatanConstants.HEX14, SettlersOfCatanConstants.GRAIN);
        emptyState.put(SettlersOfCatanConstants.HEX15, SettlersOfCatanConstants.LUMBER);
        emptyState.put(SettlersOfCatanConstants.HEX16, SettlersOfCatanConstants.GRAIN);
        emptyState.put(SettlersOfCatanConstants.HEX17, SettlersOfCatanConstants.BRICK);
        emptyState.put(SettlersOfCatanConstants.HEX18, SettlersOfCatanConstants.LUMBER);
        // These will be randomized in real game, set for testing purposes
        emptyState.put(SettlersOfCatanConstants.HARBOR00, SettlersOfCatanConstants.HARBORTYPE03);
        emptyState.put(SettlersOfCatanConstants.HARBOR01, SettlersOfCatanConstants.HARBORTYPE00);
        emptyState.put(SettlersOfCatanConstants.HARBOR02, SettlersOfCatanConstants.HARBORTYPE01);
        emptyState.put(SettlersOfCatanConstants.HARBOR03, SettlersOfCatanConstants.HARBORTYPE00);
        emptyState.put(SettlersOfCatanConstants.HARBOR04, SettlersOfCatanConstants.HARBORTYPE05);
        emptyState.put(SettlersOfCatanConstants.HARBOR05, SettlersOfCatanConstants.HARBORTYPE00);
        emptyState.put(SettlersOfCatanConstants.HARBOR06, SettlersOfCatanConstants.HARBORTYPE02);
        emptyState.put(SettlersOfCatanConstants.HARBOR07, SettlersOfCatanConstants.HARBORTYPE00);
        emptyState.put(SettlersOfCatanConstants.HARBOR08, SettlersOfCatanConstants.HARBORTYPE04);
        // These will be randomized in real game, set for testing purposes
        // These will also need to be set to visible to no one pre game start
        emptyState.put(SettlersOfCatanConstants.SOLDIERCOUNTPB, 0);
        emptyState.put(SettlersOfCatanConstants.SOLDIERCOUNTPR, 0);
        emptyState.put(SettlersOfCatanConstants.SOLDIERCOUNTPY, 0);
        emptyState.put(SettlersOfCatanConstants.SOLDIERCOUNTPG, 0);
        emptyState.put(SettlersOfCatanConstants.ROBBER, SettlersOfCatanConstants.HEX02);
        return emptyState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createAddAssetState() {
        Map<String, Object> addRoadState = createEmptyState();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH19, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH19);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.BRICK);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.WOOL);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD04PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD05PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD06PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD07PB, SettlersOfCatanConstants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStateForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH19, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH19);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.BRICK);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.WOOL);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD04PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD05PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD06PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD07PB, SettlersOfCatanConstants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStatePlusDevelopmentCardForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH19, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH19);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.DEVELOPMENTCARD00, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.BRICK);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD04PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD06PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD07PB, SettlersOfCatanConstants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStatePlusCityForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.CITY00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY00PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH19, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH19);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.BRICK);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.WOOL);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStatePlusSettlementForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE12, SettlersOfCatanConstants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE12);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH19, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH19);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD04PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD05PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD06PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD07PB, SettlersOfCatanConstants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStatePlusRoadForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH19, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH12, SettlersOfCatanConstants.ROAD02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH19);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH12);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.WOOL);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD04PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD05PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD06PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD07PB, SettlersOfCatanConstants.ORE);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueNormalHarborTradeStateForBlue() {
        Map<String, Object> harborTradeState = createEmptyStateNoDevCards();
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.LUMBER);
        
        return harborTradeState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBluePostAnyHarborTradeStateForBlue() {
        Map<String, Object> harborTradeState = createEmptyStateNoDevCards();
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE);
        
        return harborTradeState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueThreeToOneHarborTradeStateForBlue() {
        Map<String, Object> harborTradeState = createEmptyStateNoDevCards();
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.NODE05, SettlersOfCatanConstants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE05);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.LUMBER);
        
        return harborTradeState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueTwoToOneLumberHarborTradeStateForBlue() {
        Map<String, Object> harborTradeState = createEmptyStateNoDevCards();
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.NODE03, SettlersOfCatanConstants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE03);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        
        return harborTradeState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetPlusDevCardStateForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH19, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH19);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.BRICK);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.WOOL);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD04PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD05PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD06PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD07PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.DEVELOPMENTCARD00, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        
        return addRoadState;
    }
    
    // A specialized state to test adding assets via resource purchase
    public static Map<String, Object> createBlueAddAssetStateForNotBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH19, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH19);
        
        return addRoadState;
    }

    // A specialized state to test failures caused by a player having exhausted their
    // allotment of specific assets
    public static Map<String, Object> createAllAssetsUsedState() {
        Map<String, Object> addRoadState = createEmptyState();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE21, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE22, SettlersOfCatanConstants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE24, SettlersOfCatanConstants.SETTLEMENT03PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE25, SettlersOfCatanConstants.SETTLEMENT04PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE21);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE22);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT02PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT03PB, SettlersOfCatanConstants.NODE24);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT04PB, SettlersOfCatanConstants.NODE25);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE43, SettlersOfCatanConstants.CITY00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE44, SettlersOfCatanConstants.CITY01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE45, SettlersOfCatanConstants.CITY02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE46, SettlersOfCatanConstants.CITY03PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY00PB, SettlersOfCatanConstants.NODE43);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY01PB, SettlersOfCatanConstants.NODE44);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY02PB, SettlersOfCatanConstants.NODE45);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY03PB, SettlersOfCatanConstants.NODE46);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH23, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH24, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH25, SettlersOfCatanConstants.ROAD02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD03PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH27, SettlersOfCatanConstants.ROAD04PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH28, SettlersOfCatanConstants.ROAD05PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH29, SettlersOfCatanConstants.ROAD06PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH30, SettlersOfCatanConstants.ROAD07PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH55, SettlersOfCatanConstants.ROAD08PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH56, SettlersOfCatanConstants.ROAD09PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH57, SettlersOfCatanConstants.ROAD10PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH58, SettlersOfCatanConstants.ROAD11PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH59, SettlersOfCatanConstants.ROAD12PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH60, SettlersOfCatanConstants.ROAD13PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH61, SettlersOfCatanConstants.ROAD14PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH24);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH25);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD03PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD04PB, SettlersOfCatanConstants.PATH27);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD05PB, SettlersOfCatanConstants.PATH28);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD06PB, SettlersOfCatanConstants.PATH29);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD07PB, SettlersOfCatanConstants.PATH30);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD08PB, SettlersOfCatanConstants.PATH55);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD09PB, SettlersOfCatanConstants.PATH56);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD10PB, SettlersOfCatanConstants.PATH57);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD11PB, SettlersOfCatanConstants.PATH58);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD12PB, SettlersOfCatanConstants.PATH59);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD13PB, SettlersOfCatanConstants.PATH60);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD14PB, SettlersOfCatanConstants.PATH61);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.BRICK);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.WOOL);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD04PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD05PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD06PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD07PB, SettlersOfCatanConstants.ORE);
        
        return addRoadState;
    }
    
    // a specialized state that allows for a user to claim victory the next move in
    // a variety of ways
    public static Map<String, Object> createPrepForVictoryState() {
        Map<String, Object> addRoadState = createEmptyState();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE21, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE21);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE22, SettlersOfCatanConstants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE22);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT02PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE24, SettlersOfCatanConstants.CITY00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY00PB, SettlersOfCatanConstants.NODE24);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE25, SettlersOfCatanConstants.CITY01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY01PB, SettlersOfCatanConstants.NODE25);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE26, SettlersOfCatanConstants.CITY02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY02PB, SettlersOfCatanConstants.NODE26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH23, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH24, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH24);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH25, SettlersOfCatanConstants.ROAD02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH25);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD03PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD03PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH27, SettlersOfCatanConstants.ROAD04PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD04PB, SettlersOfCatanConstants.PATH27);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH28, SettlersOfCatanConstants.ROAD05PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD05PB, SettlersOfCatanConstants.PATH28);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH29, SettlersOfCatanConstants.ROAD06PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD06PB, SettlersOfCatanConstants.PATH29);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH30, SettlersOfCatanConstants.ROAD07PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD07PB, SettlersOfCatanConstants.PATH30);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH31, SettlersOfCatanConstants.ROAD08PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD08PB, SettlersOfCatanConstants.PATH31);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH32, SettlersOfCatanConstants.ROAD09PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD09PB, SettlersOfCatanConstants.PATH32);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH38, SettlersOfCatanConstants.ROAD10PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD10PB, SettlersOfCatanConstants.PATH38);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH48, SettlersOfCatanConstants.ROAD11PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD11PB, SettlersOfCatanConstants.PATH38);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE33, SettlersOfCatanConstants.SETTLEMENT00PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PR, SettlersOfCatanConstants.NODE33);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH39, SettlersOfCatanConstants.ROAD00PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PR, SettlersOfCatanConstants.PATH39);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH40, SettlersOfCatanConstants.ROAD01PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PR, SettlersOfCatanConstants.PATH40);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH41, SettlersOfCatanConstants.ROAD02PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD02PR, SettlersOfCatanConstants.PATH41);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH42, SettlersOfCatanConstants.ROAD03PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD03PR, SettlersOfCatanConstants.PATH42);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH43, SettlersOfCatanConstants.ROAD04PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD04PR, SettlersOfCatanConstants.PATH43);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH44, SettlersOfCatanConstants.ROAD05PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD05PR, SettlersOfCatanConstants.PATH44);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH45, SettlersOfCatanConstants.ROAD06PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD06PR, SettlersOfCatanConstants.PATH45);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH46, SettlersOfCatanConstants.ROAD07PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD07PR, SettlersOfCatanConstants.PATH46);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH47, SettlersOfCatanConstants.ROAD08PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD08PR, SettlersOfCatanConstants.PATH47);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH53, SettlersOfCatanConstants.ROAD09PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD09PR, SettlersOfCatanConstants.PATH53);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH61, SettlersOfCatanConstants.ROAD10PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD10PR, SettlersOfCatanConstants.PATH61);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH65, SettlersOfCatanConstants.ROAD11PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD11PR, SettlersOfCatanConstants.PATH65);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.LONGESTROAD, SettlersOfCatanConstants.PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SOLDIERCOUNTPB, 6);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SOLDIERCOUNTPR, 6);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.LARGESTARMY, SettlersOfCatanConstants.PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.BRICK);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.WOOL);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD04PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD05PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD06PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD07PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.DEVELOPMENTCARD00, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        
        return addRoadState;
    }
    
    // a specialized state that allows for a user to test the victory screen
    public static Map<String, Object> createBlueVictoryStateForBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE21, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE21);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE22, SettlersOfCatanConstants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE22);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT02PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE24, SettlersOfCatanConstants.CITY00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY00PB, SettlersOfCatanConstants.NODE24);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE25, SettlersOfCatanConstants.CITY01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY01PB, SettlersOfCatanConstants.NODE25);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE26, SettlersOfCatanConstants.CITY02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY02PB, SettlersOfCatanConstants.NODE26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH23, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH24, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH24);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH25, SettlersOfCatanConstants.ROAD02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH25);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD03PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD03PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH27, SettlersOfCatanConstants.ROAD04PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD04PB, SettlersOfCatanConstants.PATH27);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH28, SettlersOfCatanConstants.ROAD05PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD05PB, SettlersOfCatanConstants.PATH28);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH29, SettlersOfCatanConstants.ROAD06PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD06PB, SettlersOfCatanConstants.PATH29);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH30, SettlersOfCatanConstants.ROAD07PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD07PB, SettlersOfCatanConstants.PATH30);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH31, SettlersOfCatanConstants.ROAD08PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD08PB, SettlersOfCatanConstants.PATH31);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH32, SettlersOfCatanConstants.ROAD09PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD09PB, SettlersOfCatanConstants.PATH32);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH38, SettlersOfCatanConstants.ROAD10PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD10PB, SettlersOfCatanConstants.PATH38);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH48, SettlersOfCatanConstants.ROAD11PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD11PB, SettlersOfCatanConstants.PATH38);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE33, SettlersOfCatanConstants.SETTLEMENT00PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PR, SettlersOfCatanConstants.NODE33);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH39, SettlersOfCatanConstants.ROAD00PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PR, SettlersOfCatanConstants.PATH39);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH40, SettlersOfCatanConstants.ROAD01PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PR, SettlersOfCatanConstants.PATH40);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH41, SettlersOfCatanConstants.ROAD02PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD02PR, SettlersOfCatanConstants.PATH41);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH42, SettlersOfCatanConstants.ROAD03PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD03PR, SettlersOfCatanConstants.PATH42);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH43, SettlersOfCatanConstants.ROAD04PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD04PR, SettlersOfCatanConstants.PATH43);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH44, SettlersOfCatanConstants.ROAD05PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD05PR, SettlersOfCatanConstants.PATH44);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH45, SettlersOfCatanConstants.ROAD06PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD06PR, SettlersOfCatanConstants.PATH45);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH46, SettlersOfCatanConstants.ROAD07PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD07PR, SettlersOfCatanConstants.PATH46);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH47, SettlersOfCatanConstants.ROAD08PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD08PR, SettlersOfCatanConstants.PATH47);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH53, SettlersOfCatanConstants.ROAD09PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD09PR, SettlersOfCatanConstants.PATH53);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH61, SettlersOfCatanConstants.ROAD10PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD10PR, SettlersOfCatanConstants.PATH61);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH65, SettlersOfCatanConstants.ROAD11PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD11PR, SettlersOfCatanConstants.PATH65);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.LONGESTROAD, SettlersOfCatanConstants.PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SOLDIERCOUNTPB, 6);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SOLDIERCOUNTPR, 6);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.LARGESTARMY, SettlersOfCatanConstants.PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.BRICK);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.WOOL);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD04PB, SettlersOfCatanConstants.GRAIN);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD05PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD06PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.RESOURCECARD07PB, SettlersOfCatanConstants.ORE);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.DEVELOPMENTCARD00, SettlersOfCatanConstants.DEVELOPMENTCARDTYPEDEF00);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE37, SettlersOfCatanConstants.SETTLEMENT03PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT03PB, SettlersOfCatanConstants.NODE37);
        
        return addRoadState;
    }
    
    // a specialized state that allows for a user to test the victory screen
    public static Map<String, Object> createBlueVictoryStateForNotBlue() {
        Map<String, Object> addRoadState = createEmptyStateNoDevCards();
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE21, SettlersOfCatanConstants.SETTLEMENT00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE21);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE22, SettlersOfCatanConstants.SETTLEMENT01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE22);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT02PB, SettlersOfCatanConstants.NODE23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE24, SettlersOfCatanConstants.CITY00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY00PB, SettlersOfCatanConstants.NODE24);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE25, SettlersOfCatanConstants.CITY01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY01PB, SettlersOfCatanConstants.NODE25);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE26, SettlersOfCatanConstants.CITY02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.CITY02PB, SettlersOfCatanConstants.NODE26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH23, SettlersOfCatanConstants.ROAD00PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PB, SettlersOfCatanConstants.PATH23);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH24, SettlersOfCatanConstants.ROAD01PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PB, SettlersOfCatanConstants.PATH24);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH25, SettlersOfCatanConstants.ROAD02PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH25);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD03PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD03PB, SettlersOfCatanConstants.PATH26);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH27, SettlersOfCatanConstants.ROAD04PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD04PB, SettlersOfCatanConstants.PATH27);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH28, SettlersOfCatanConstants.ROAD05PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD05PB, SettlersOfCatanConstants.PATH28);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH29, SettlersOfCatanConstants.ROAD06PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD06PB, SettlersOfCatanConstants.PATH29);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH30, SettlersOfCatanConstants.ROAD07PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD07PB, SettlersOfCatanConstants.PATH30);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH31, SettlersOfCatanConstants.ROAD08PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD08PB, SettlersOfCatanConstants.PATH31);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH32, SettlersOfCatanConstants.ROAD09PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD09PB, SettlersOfCatanConstants.PATH32);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH38, SettlersOfCatanConstants.ROAD10PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD10PB, SettlersOfCatanConstants.PATH38);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH48, SettlersOfCatanConstants.ROAD11PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD11PB, SettlersOfCatanConstants.PATH38);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE33, SettlersOfCatanConstants.SETTLEMENT00PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT00PR, SettlersOfCatanConstants.NODE33);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH39, SettlersOfCatanConstants.ROAD00PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD00PR, SettlersOfCatanConstants.PATH39);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH40, SettlersOfCatanConstants.ROAD01PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD01PR, SettlersOfCatanConstants.PATH40);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH41, SettlersOfCatanConstants.ROAD02PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD02PR, SettlersOfCatanConstants.PATH41);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH42, SettlersOfCatanConstants.ROAD03PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD03PR, SettlersOfCatanConstants.PATH42);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH43, SettlersOfCatanConstants.ROAD04PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD04PR, SettlersOfCatanConstants.PATH43);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH44, SettlersOfCatanConstants.ROAD05PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD05PR, SettlersOfCatanConstants.PATH44);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH45, SettlersOfCatanConstants.ROAD06PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD06PR, SettlersOfCatanConstants.PATH45);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH46, SettlersOfCatanConstants.ROAD07PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD07PR, SettlersOfCatanConstants.PATH46);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH47, SettlersOfCatanConstants.ROAD08PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD08PR, SettlersOfCatanConstants.PATH47);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH53, SettlersOfCatanConstants.ROAD09PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD09PR, SettlersOfCatanConstants.PATH53);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH61, SettlersOfCatanConstants.ROAD10PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD10PR, SettlersOfCatanConstants.PATH61);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.PATH65, SettlersOfCatanConstants.ROAD11PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.ROAD11PR, SettlersOfCatanConstants.PATH65);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.LONGESTROAD, SettlersOfCatanConstants.PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SOLDIERCOUNTPB, 6);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SOLDIERCOUNTPR, 6);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.LARGESTARMY, SettlersOfCatanConstants.PR);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.NODE37, SettlersOfCatanConstants.SETTLEMENT03PB);
        addRoadState = changeState(addRoadState, SettlersOfCatanConstants.SETTLEMENT03PB, SettlersOfCatanConstants.NODE37);
        
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
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                SettlersOfCatanConstants.pbId,
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
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD03PB, SettlersOfCatanConstants.WOOL);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                SettlersOfCatanConstants.pbId,
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
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalNormalHarborTradeNotEnoughResources";
        
        assertHacker(verifyMove);
    }

    // Legal Test
    // 3 For 1 Harbor Trade
    @Test
    public void testLegalThreeForOneHarborTrade() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.NODE05, SettlersOfCatanConstants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE05);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                SettlersOfCatanConstants.pbId,
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
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.NODE05, SettlersOfCatanConstants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE05);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD02PB, SettlersOfCatanConstants.WOOL);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                SettlersOfCatanConstants.pbId,
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
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.NODE05, SettlersOfCatanConstants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE05);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                SettlersOfCatanConstants.pbId,
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
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.NODE03, SettlersOfCatanConstants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE03);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                SettlersOfCatanConstants.pbId,
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
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.NODE03, SettlersOfCatanConstants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE03);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD01PB, SettlersOfCatanConstants.WOOL);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                SettlersOfCatanConstants.pbId,
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
                harborTradeState, SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.NODE03, SettlersOfCatanConstants.SETTLEMENT00PB);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.SETTLEMENT00PB, SettlersOfCatanConstants.NODE03);
        harborTradeState = changeState(
                harborTradeState, SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Set(SettlersOfCatanConstants.RESOURCECARD00PB, SettlersOfCatanConstants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalTwoForOneLumberHarborTradeNotEnoughResources";
        
        assertHacker(verifyMove);
    }
    
    // Legal Test
    // Add Road
    @Test
    public void testLegalAddRoad() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.PATH12, SettlersOfCatanConstants.ROAD02PB),
                new Set(SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH12),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // Add Road
    // No Adjacent Road
    @Test
    public void testIllegalAddRoadNoAdjacentRoad() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.PATH66, SettlersOfCatanConstants.ROAD02PB),
                new Set(SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH66),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.PATH12, SettlersOfCatanConstants.ROAD02PB),
                new Set(SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH12),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.PATH26, SettlersOfCatanConstants.ROAD02PB),
                new Set(SettlersOfCatanConstants.ROAD02PB, SettlersOfCatanConstants.PATH26),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.PATH31, SettlersOfCatanConstants.ROAD14PB),
                new Set(SettlersOfCatanConstants.ROAD14PB, SettlersOfCatanConstants.PATH31),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addRoad),
                createAllAssetsUsedState(),
                addRoad,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddRoadNoneLeft";

        assertHacker(verifyMove);
    }

    // Legal Test
    // Add Settlement
    @Test
    public void testLegalAddSettlement() {
        ImmutableList<Operation> addSettlement = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.NODE12, SettlersOfCatanConstants.SETTLEMENT01PB),
                new Set(SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE12),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // Add Settlement
    // No Road
    @Test
    public void testIllegalAddSettlementNoRoad() {
        ImmutableList<Operation> addSettlement = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.NODE25, SettlersOfCatanConstants.SETTLEMENT01PB),
                new Set(SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE25),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.NODE17, SettlersOfCatanConstants.SETTLEMENT01PB),
                new Set(SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE17),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.NODE12, SettlersOfCatanConstants.SETTLEMENT01PB),
                new Set(SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE12),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD05PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.SETTLEMENT01PB),
                new Set(SettlersOfCatanConstants.SETTLEMENT01PB, SettlersOfCatanConstants.NODE23),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.NODE12, SettlersOfCatanConstants.SETTLEMENT04PB),
                new Set(SettlersOfCatanConstants.SETTLEMENT04PB, SettlersOfCatanConstants.NODE12),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addSettlement),
                createAllAssetsUsedState(),
                addSettlement,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddSettlementNoneLeft";

        assertHacker(verifyMove);
    }

    // Legal Test
    // Add City
    @Test
    public void testLegalAddCity() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Delete(SettlersOfCatanConstants.SETTLEMENT00PB),
                new Set(SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.CITY00PB),
                new Set(SettlersOfCatanConstants.CITY00PB, SettlersOfCatanConstants.NODE23),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD04PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD06PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD07PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD04PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD05PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD06PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD07PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addCity),
                createAddAssetState(),
                addCity,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // Add City
    // No Settlement
    @Test
    public void testIllegalAddCityNoSettlement() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.NODE24, SettlersOfCatanConstants.CITY00PB),
                new Set(SettlersOfCatanConstants.CITY00PB, SettlersOfCatanConstants.NODE24),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD04PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD06PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD07PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD04PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD05PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD06PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD07PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addCity),
                createAddAssetState(),
                addCity,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Delete(SettlersOfCatanConstants.SETTLEMENT00PB),
                new Set(SettlersOfCatanConstants.NODE23, SettlersOfCatanConstants.CITY00PB),
                new Set(SettlersOfCatanConstants.CITY00PB, SettlersOfCatanConstants.NODE23),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD04PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD06PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD07PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD04PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD05PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD06PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD07PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addCity),
                createAddAssetState(),
                addCity,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Delete(SettlersOfCatanConstants.SETTLEMENT00PB),
                new Set(SettlersOfCatanConstants.NODE21, SettlersOfCatanConstants.CITY03PB),
                new Set(SettlersOfCatanConstants.CITY03PB, SettlersOfCatanConstants.NODE21),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD04PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD06PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD07PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD04PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD05PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD06PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD07PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addCity),
                createAllAssetsUsedState(),
                addCity,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());

        testName = "testIllegalAddCityNoneLeft";

        assertHacker(verifyMove);
    }

    // Legal Test
    // Purchase Development Card
    @Test
    public void testLegalPurchaseDevelopmentCard() {
        ImmutableList<Operation> addDevelopmentCard = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.DEVELOPMENTCARD00, SettlersOfCatanConstants.visibleToPB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD05PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addDevelopmentCard),
                createAddAssetState(),
                addDevelopmentCard,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Illegal Test
    // Purchase Development Card
    // Incorrect Resources
    @Test
    public void testIllegalPurchaseDevelopmentCardIncorrectResources() {
        ImmutableList<Operation> addDevelopmentCard = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.DEVELOPMENTCARD00, SettlersOfCatanConstants.visibleToPB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD05PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createAddAssetState(), addDevelopmentCard),
                createAddAssetState(),
                addDevelopmentCard,
                SettlersOfCatanConstants.pbId,
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
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.NODE37, SettlersOfCatanConstants.SETTLEMENT03PB),
                new Set(SettlersOfCatanConstants.SETTLEMENT03PB, SettlersOfCatanConstants.NODE37),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD02PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD02PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB),
                new EndGame(SettlersOfCatanConstants.pbId)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), addSettlement),
                createPrepForVictoryState(),
                addSettlement,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Legal Test
    // End Game
    // Via Building City
    @Test
    public void testEndGameByBuildingCity() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Delete(SettlersOfCatanConstants.SETTLEMENT00PB),
                new Set(SettlersOfCatanConstants.NODE21, SettlersOfCatanConstants.CITY03PB),
                new Set(SettlersOfCatanConstants.CITY03PB, SettlersOfCatanConstants.NODE21),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD03PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD04PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD05PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD06PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD07PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD03PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD04PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD05PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD06PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD07PB),
                new EndGame(SettlersOfCatanConstants.pbId)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), addCity),
                createPrepForVictoryState(),
                addCity,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Legal Test
    // End Game
    // Via Building Road And Taking Longest Road
    @Test
    public void testEndGameByGettingLongestRoad() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new Set(SettlersOfCatanConstants.PATH33, SettlersOfCatanConstants.ROAD12PB),
                new Set(SettlersOfCatanConstants.ROAD12PB, SettlersOfCatanConstants.PATH33),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD00PB),
                new SetVisibility(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD00PB),
                new Delete(SettlersOfCatanConstants.RESOURCECARD01PB),
                new Set(SettlersOfCatanConstants.LONGESTROAD, SettlersOfCatanConstants.PB),
                new EndGame(SettlersOfCatanConstants.pbId)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), addRoad),
                createPrepForVictoryState(),
                addRoad,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }

    // Legal Test
    // End Game
    // Via Playing Soldier Card And Taking Largest Army
    @Test
    public void testEndGameByGettingLargestArmy() {
        ImmutableList<Operation> playDevelopmentCard = ImmutableList.<Operation>of(
                new Set(SettlersOfCatanConstants.TURN, SettlersOfCatanConstants.PB),
                new SetVisibility(SettlersOfCatanConstants.DEVELOPMENTCARD00, SettlersOfCatanConstants.visibleToNone), //this is a soldier card
                new Set(SettlersOfCatanConstants.ROBBER, SettlersOfCatanConstants.HEX12),
                new Set(SettlersOfCatanConstants.SOLDIERCOUNTPB, 7),
                new Set(SettlersOfCatanConstants.LARGESTARMY, SettlersOfCatanConstants.PB),
                new EndGame(SettlersOfCatanConstants.pbId)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                SettlersOfCatanConstants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), playDevelopmentCard),
                createPrepForVictoryState(),
                playDevelopmentCard,
                SettlersOfCatanConstants.pbId,
                ImmutableMap.<String, Integer>of());
        
        assertMoveOk(verifyMove);
    }
}
