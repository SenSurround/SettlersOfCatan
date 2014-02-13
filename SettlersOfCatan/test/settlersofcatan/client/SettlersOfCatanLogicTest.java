package settlersofcatan.client;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import settlersofcatan.client.Constants;

import settlersofcatan.client.GameApi.Delete;
import settlersofcatan.client.GameApi.EndGame;
import settlersofcatan.client.GameApi.Operation;
import settlersofcatan.client.GameApi.Set;
import settlersofcatan.client.GameApi.SetVisibility;
import settlersofcatan.client.GameApi.Shuffle;
import settlersofcatan.client.GameApi.VerifyMove;
import settlersofcatan.client.GameApi.VerifyMoveDone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;

public class SettlersOfCatanLogicTest {
    SettlersOfCatanLogic settlersOfCatanLogic = new SettlersOfCatanLogic();
    
    private void assertMoveOk(VerifyMove verifyMove) {
        settlersOfCatanLogic.checkMoveIsLegal(verifyMove);
    }

    private void assertHacker(VerifyMove verifyMove) {
        VerifyMoveDone verifyDone = settlersOfCatanLogic.verify(verifyMove);
        assertEquals(verifyMove.getLastMovePlayerId(), verifyDone.getHackerPlayerId());
    }
    
    private Map<String, Object> applyMoveToState(
            Map<String, Object> initialState, ImmutableList<Operation> move) {
        Map<String, Object> newState = initialState;
        
        for (int i = 0; i < move.size(); i++) {
            Operation current = move.get(i);
            switch(current.getClassName()) {
                case "Set":
                    newState.put(((Set) current).getKey(), ((Set) current).getValue());
                    break;
                    
                case "Delete":
                  //newState.put(((Delete) current).getKey(), Constants.NOTHING);
                    newState.remove(((Delete) current).getKey());
                    break;
                    
                default:
                    break;
            }
        }
        
        return newState;
    }
    
    
    private Map<String, Object> changeState(
            Map<String, Object> previousState, String propString, Object propObject) {
        Map<String, Object> newState = previousState;
        
        newState.put(propString, propObject);
        
        return newState;
    }
    
    private Map<String, Object> createEmptyState() {
        Map<String, Object> emptyState = Maps.<String, Object>newHashMap();
        emptyState.put(Constants.TURN, Constants.PB);
      //emptyState.put(Constants.DIE0, Constants.NOTHING);
      //emptyState.put(Constants.DIE1, Constants.NOTHING);
        // These will be randomized in real game
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
      /*emptyState.put(Constants.NODE00, Constants.NOTHING);
        emptyState.put(Constants.NODE01, Constants.NOTHING);
        emptyState.put(Constants.NODE02, Constants.NOTHING);
        emptyState.put(Constants.NODE03, Constants.NOTHING);
        emptyState.put(Constants.NODE04, Constants.NOTHING);
        emptyState.put(Constants.NODE05, Constants.NOTHING);
        emptyState.put(Constants.NODE06, Constants.NOTHING);
        emptyState.put(Constants.NODE07, Constants.NOTHING);
        emptyState.put(Constants.NODE08, Constants.NOTHING);
        emptyState.put(Constants.NODE09, Constants.NOTHING);
        emptyState.put(Constants.NODE10, Constants.NOTHING);
        emptyState.put(Constants.NODE11, Constants.NOTHING);
        emptyState.put(Constants.NODE12, Constants.NOTHING);
        emptyState.put(Constants.NODE13, Constants.NOTHING);
        emptyState.put(Constants.NODE14, Constants.NOTHING);
        emptyState.put(Constants.NODE15, Constants.NOTHING);
        emptyState.put(Constants.NODE16, Constants.NOTHING);
        emptyState.put(Constants.NODE17, Constants.NOTHING);
        emptyState.put(Constants.NODE18, Constants.NOTHING);
        emptyState.put(Constants.NODE19, Constants.NOTHING);
        emptyState.put(Constants.NODE20, Constants.NOTHING);
        emptyState.put(Constants.NODE21, Constants.NOTHING);
        emptyState.put(Constants.NODE22, Constants.NOTHING);
        emptyState.put(Constants.NODE23, Constants.NOTHING);
        emptyState.put(Constants.NODE24, Constants.NOTHING);
        emptyState.put(Constants.NODE25, Constants.NOTHING);
        emptyState.put(Constants.NODE26, Constants.NOTHING);
        emptyState.put(Constants.NODE27, Constants.NOTHING);
        emptyState.put(Constants.NODE28, Constants.NOTHING);
        emptyState.put(Constants.NODE29, Constants.NOTHING);
        emptyState.put(Constants.NODE30, Constants.NOTHING);
        emptyState.put(Constants.NODE31, Constants.NOTHING);
        emptyState.put(Constants.NODE32, Constants.NOTHING);
        emptyState.put(Constants.NODE33, Constants.NOTHING);
        emptyState.put(Constants.NODE34, Constants.NOTHING);
        emptyState.put(Constants.NODE35, Constants.NOTHING);
        emptyState.put(Constants.NODE36, Constants.NOTHING);
        emptyState.put(Constants.NODE37, Constants.NOTHING);
        emptyState.put(Constants.NODE38, Constants.NOTHING);
        emptyState.put(Constants.NODE39, Constants.NOTHING);
        emptyState.put(Constants.NODE40, Constants.NOTHING);
        emptyState.put(Constants.NODE41, Constants.NOTHING);
        emptyState.put(Constants.NODE42, Constants.NOTHING);
        emptyState.put(Constants.NODE43, Constants.NOTHING);
        emptyState.put(Constants.NODE44, Constants.NOTHING);
        emptyState.put(Constants.NODE45, Constants.NOTHING);
        emptyState.put(Constants.NODE46, Constants.NOTHING);
        emptyState.put(Constants.NODE47, Constants.NOTHING);
        emptyState.put(Constants.NODE48, Constants.NOTHING);
        emptyState.put(Constants.NODE49, Constants.NOTHING);
        emptyState.put(Constants.NODE50, Constants.NOTHING);
        emptyState.put(Constants.NODE51, Constants.NOTHING);
        emptyState.put(Constants.NODE52, Constants.NOTHING);
        emptyState.put(Constants.NODE53, Constants.NOTHING);
        emptyState.put(Constants.PATH00, Constants.NOTHING);
        emptyState.put(Constants.PATH01, Constants.NOTHING);
        emptyState.put(Constants.PATH02, Constants.NOTHING);
        emptyState.put(Constants.PATH03, Constants.NOTHING);
        emptyState.put(Constants.PATH04, Constants.NOTHING);
        emptyState.put(Constants.PATH05, Constants.NOTHING);
        emptyState.put(Constants.PATH06, Constants.NOTHING);
        emptyState.put(Constants.PATH07, Constants.NOTHING);
        emptyState.put(Constants.PATH08, Constants.NOTHING);
        emptyState.put(Constants.PATH09, Constants.NOTHING);
        emptyState.put(Constants.PATH10, Constants.NOTHING);
        emptyState.put(Constants.PATH11, Constants.NOTHING);
        emptyState.put(Constants.PATH12, Constants.NOTHING);
        emptyState.put(Constants.PATH13, Constants.NOTHING);
        emptyState.put(Constants.PATH14, Constants.NOTHING);
        emptyState.put(Constants.PATH15, Constants.NOTHING);
        emptyState.put(Constants.PATH16, Constants.NOTHING);
        emptyState.put(Constants.PATH17, Constants.NOTHING);
        emptyState.put(Constants.PATH18, Constants.NOTHING);
        emptyState.put(Constants.PATH19, Constants.NOTHING);
        emptyState.put(Constants.PATH20, Constants.NOTHING);
        emptyState.put(Constants.PATH21, Constants.NOTHING);
        emptyState.put(Constants.PATH22, Constants.NOTHING);
        emptyState.put(Constants.PATH23, Constants.NOTHING);
        emptyState.put(Constants.PATH24, Constants.NOTHING);
        emptyState.put(Constants.PATH25, Constants.NOTHING);
        emptyState.put(Constants.PATH26, Constants.NOTHING);
        emptyState.put(Constants.PATH27, Constants.NOTHING);
        emptyState.put(Constants.PATH28, Constants.NOTHING);
        emptyState.put(Constants.PATH29, Constants.NOTHING);
        emptyState.put(Constants.PATH30, Constants.NOTHING);
        emptyState.put(Constants.PATH31, Constants.NOTHING);
        emptyState.put(Constants.PATH32, Constants.NOTHING);
        emptyState.put(Constants.PATH33, Constants.NOTHING);
        emptyState.put(Constants.PATH34, Constants.NOTHING);
        emptyState.put(Constants.PATH35, Constants.NOTHING);
        emptyState.put(Constants.PATH36, Constants.NOTHING);
        emptyState.put(Constants.PATH37, Constants.NOTHING);
        emptyState.put(Constants.PATH38, Constants.NOTHING);
        emptyState.put(Constants.PATH39, Constants.NOTHING);
        emptyState.put(Constants.PATH40, Constants.NOTHING);
        emptyState.put(Constants.PATH41, Constants.NOTHING);
        emptyState.put(Constants.PATH42, Constants.NOTHING);
        emptyState.put(Constants.PATH43, Constants.NOTHING);
        emptyState.put(Constants.PATH44, Constants.NOTHING);
        emptyState.put(Constants.PATH45, Constants.NOTHING);
        emptyState.put(Constants.PATH46, Constants.NOTHING);
        emptyState.put(Constants.PATH47, Constants.NOTHING);
        emptyState.put(Constants.PATH48, Constants.NOTHING);
        emptyState.put(Constants.PATH49, Constants.NOTHING);
        emptyState.put(Constants.PATH50, Constants.NOTHING);
        emptyState.put(Constants.PATH51, Constants.NOTHING);
        emptyState.put(Constants.PATH52, Constants.NOTHING);
        emptyState.put(Constants.PATH53, Constants.NOTHING);
        emptyState.put(Constants.PATH54, Constants.NOTHING);
        emptyState.put(Constants.PATH55, Constants.NOTHING);
        emptyState.put(Constants.PATH56, Constants.NOTHING);
        emptyState.put(Constants.PATH57, Constants.NOTHING);
        emptyState.put(Constants.PATH58, Constants.NOTHING);
        emptyState.put(Constants.PATH59, Constants.NOTHING);
        emptyState.put(Constants.PATH60, Constants.NOTHING);
        emptyState.put(Constants.PATH61, Constants.NOTHING);
        emptyState.put(Constants.PATH62, Constants.NOTHING);
        emptyState.put(Constants.PATH63, Constants.NOTHING);
        emptyState.put(Constants.PATH64, Constants.NOTHING);
        emptyState.put(Constants.PATH65, Constants.NOTHING);
        emptyState.put(Constants.PATH66, Constants.NOTHING);
        emptyState.put(Constants.PATH67, Constants.NOTHING);
        emptyState.put(Constants.PATH68, Constants.NOTHING);
        emptyState.put(Constants.PATH69, Constants.NOTHING);
        emptyState.put(Constants.PATH70, Constants.NOTHING);
        emptyState.put(Constants.PATH71, Constants.NOTHING);*/
        // These will be randomized in real game
        emptyState.put(Constants.HARBOR00, Constants.HARBORTYPE03);
        emptyState.put(Constants.HARBOR01, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR02, Constants.HARBORTYPE01);
        emptyState.put(Constants.HARBOR03, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR04, Constants.HARBORTYPE05);
        emptyState.put(Constants.HARBOR05, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR06, Constants.HARBORTYPE02);
        emptyState.put(Constants.HARBOR07, Constants.HARBORTYPE00);
        emptyState.put(Constants.HARBOR08, Constants.HARBORTYPE04);
      //emptyState.put(Constants.ROBBER, Constants.NOTHING);
        // These will be randomized in real game
        // These will also need to be set to visible to no one pre game start
        emptyState.put(Constants.DEVELOPMENTCARD00, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD01, Constants.DEVELOPMENTCARDTYPE03);
        emptyState.put(Constants.DEVELOPMENTCARD02, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD03, Constants.DEVELOPMENTCARDTYPE05);
        emptyState.put(Constants.DEVELOPMENTCARD04, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD05, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD06, Constants.DEVELOPMENTCARDTYPE04);
        emptyState.put(Constants.DEVELOPMENTCARD07, Constants.DEVELOPMENTCARDTYPE02);
        emptyState.put(Constants.DEVELOPMENTCARD08, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD09, Constants.DEVELOPMENTCARDTYPE07);
        emptyState.put(Constants.DEVELOPMENTCARD10, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD11, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD12, Constants.DEVELOPMENTCARDTYPE06);
        emptyState.put(Constants.DEVELOPMENTCARD13, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD14, Constants.DEVELOPMENTCARDTYPE01);
        emptyState.put(Constants.DEVELOPMENTCARD15, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD16, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD17, Constants.DEVELOPMENTCARDTYPE08);
        emptyState.put(Constants.DEVELOPMENTCARD18, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD19, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD20, Constants.DEVELOPMENTCARDTYPE03);
        emptyState.put(Constants.DEVELOPMENTCARD21, Constants.DEVELOPMENTCARDTYPE01);
        emptyState.put(Constants.DEVELOPMENTCARD22, Constants.DEVELOPMENTCARDTYPE00);
        emptyState.put(Constants.DEVELOPMENTCARD23, Constants.DEVELOPMENTCARDTYPE02);
        emptyState.put(Constants.DEVELOPMENTCARD24, Constants.DEVELOPMENTCARDTYPE00);
      /*emptyState.put(Constants.CITY00PB, Constants.NOTHING);
        emptyState.put(Constants.CITY01PB, Constants.NOTHING);
        emptyState.put(Constants.CITY02PB, Constants.NOTHING);
        emptyState.put(Constants.CITY03PB, Constants.NOTHING);
        emptyState.put(Constants.CITY00PR, Constants.NOTHING);
        emptyState.put(Constants.CITY01PR, Constants.NOTHING);
        emptyState.put(Constants.CITY02PR, Constants.NOTHING);
        emptyState.put(Constants.CITY03PR, Constants.NOTHING);
        emptyState.put(Constants.CITY00PY, Constants.NOTHING);
        emptyState.put(Constants.CITY01PY, Constants.NOTHING);
        emptyState.put(Constants.CITY02PY, Constants.NOTHING);
        emptyState.put(Constants.CITY03PY, Constants.NOTHING);
        emptyState.put(Constants.CITY00PG, Constants.NOTHING);
        emptyState.put(Constants.CITY01PG, Constants.NOTHING);
        emptyState.put(Constants.CITY02PG, Constants.NOTHING);
        emptyState.put(Constants.CITY03PG, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT00PB, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT01PB, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT02PB, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT03PB, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT04PB, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT00PR, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT01PR, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT02PR, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT03PR, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT04PR, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT00PY, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT01PY, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT02PY, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT03PY, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT04PY, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT00PG, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT01PG, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT02PG, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT03PG, Constants.NOTHING);
        emptyState.put(Constants.SETTLEMENT04PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD00PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD01PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD02PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD03PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD04PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD05PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD06PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD07PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD08PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD09PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD10PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD11PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD12PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD13PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD14PB, Constants.NOTHING);
        emptyState.put(Constants.ROAD00PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD01PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD02PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD03PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD04PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD05PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD06PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD07PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD08PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD09PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD10PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD11PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD12PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD13PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD14PR, Constants.NOTHING);
        emptyState.put(Constants.ROAD00PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD01PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD02PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD03PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD04PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD05PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD06PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD07PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD08PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD09PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD10PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD11PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD12PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD13PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD14PY, Constants.NOTHING);
        emptyState.put(Constants.ROAD00PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD01PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD02PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD03PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD04PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD05PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD06PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD07PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD08PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD09PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD10PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD11PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD12PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD13PG, Constants.NOTHING);
        emptyState.put(Constants.ROAD14PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD00PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD01PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD02PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD03PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD04PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD05PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD06PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD07PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD08PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD09PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD10PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD11PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD12PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD13PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD14PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD15PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD16PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD17PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD18PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD19PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD20PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD21PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD22PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD23PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD24PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD25PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD26PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD27PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD28PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD29PB, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD00PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD01PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD02PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD03PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD04PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD05PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD06PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD07PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD08PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD09PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD10PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD11PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD12PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD13PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD14PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD15PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD16PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD17PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD18PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD19PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD20PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD21PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD22PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD23PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD24PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD25PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD26PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD27PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD28PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD29PR, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD00PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD01PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD02PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD03PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD04PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD05PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD06PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD07PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD08PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD09PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD10PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD11PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD12PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD13PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD14PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD15PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD16PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD17PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD18PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD19PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD20PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD21PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD22PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD23PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD24PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD25PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD26PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD27PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD28PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD29PY, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD00PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD01PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD02PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD03PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD04PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD05PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD06PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD07PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD08PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD09PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD10PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD11PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD12PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD13PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD14PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD15PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD16PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD17PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD18PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD19PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD20PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD21PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD22PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD23PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD24PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD25PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD26PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD27PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD28PG, Constants.NOTHING);
        emptyState.put(Constants.RESOURCECARD29PG, Constants.NOTHING);*/
        emptyState.put(Constants.SOLDIERCOUNTPB, 0);
        emptyState.put(Constants.SOLDIERCOUNTPR, 0);
        emptyState.put(Constants.SOLDIERCOUNTPY, 0);
        emptyState.put(Constants.SOLDIERCOUNTPG, 0);
        return emptyState;
    }
    
    private Map<String, Object> createAddAssetState() {
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
    
    private Map<String, Object> createAllAssetsUsedState() {
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
    
    private Map<String, Object> createPrepForVictoryState() {
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
        
        return addRoadState;
    }
    
    // VerifyMove - YourPlayerId - int
    //              Player List - List
    //              State - Map
    //                LastState - Map
    //                LastMove - List
    //                LastMovePlayerId - Int
    
    @Test
    public void testLegalNormalHarborTrade() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD03PB, Constants.LUMBER);
        
        
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
    @Test
    public void testIllegalNormalHarborTradeResourcesNotSame() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD03PB, Constants.WOOL);
        
        
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId);
        
        assertHacker(verifyMove);
    }
    
    @Test
    public void testIllegalNormalHarborTradeNotEnoughResources() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        
        
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId);
        
        assertHacker(verifyMove);
    }
    
    @Test
    public void testLegalThreeForOneHarborTrade() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(harborTradeState, Constants.NODE05, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE05);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD02PB, Constants.LUMBER);
        
        
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
    @Test
    public void testIllegalThreeForOneHarborTradeResourcesNotSame() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(harborTradeState, Constants.NODE05, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE05);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD02PB, Constants.WOOL);
        
        
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId);
        
        assertHacker(verifyMove);
    }
    
    @Test
    public void testIllegalThreeForOneHarborTradeNotEnoughResources() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(harborTradeState, Constants.NODE05, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE05);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId);
        
        assertHacker(verifyMove);
    }
    
    @Test
    public void testLegalTwoForOneLumberHarborTrade() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(harborTradeState, Constants.NODE03, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE03);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD01PB, Constants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
    @Test
    public void testIllegalTwoForOneLumberHarborTradeResourcesNotSame() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(harborTradeState, Constants.NODE03, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE03);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD01PB, Constants.WOOL);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD01PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId);
        
        assertHacker(verifyMove);
    }
    
    @Test
    public void testIllegalTwoForOneLumberHarborTradeNotEnoughResources() {
        Map<String, Object> harborTradeState = createEmptyState();
        harborTradeState = changeState(harborTradeState, Constants.TURN, Constants.PB);
        harborTradeState = changeState(harborTradeState, Constants.NODE03, Constants.SETTLEMENT00PB);
        harborTradeState = changeState(harborTradeState, Constants.SETTLEMENT00PB, Constants.NODE03);
        harborTradeState = changeState(harborTradeState, Constants.RESOURCECARD00PB, Constants.LUMBER);
        
        
        ImmutableList<Operation> harborTrade = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Set(Constants.RESOURCECARD00PB, Constants.ORE)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(harborTradeState, harborTrade),
                harborTradeState,
                harborTrade,
                Constants.pbId);
        
        assertHacker(verifyMove);
    }
    
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
    @Test
    public void testIllegalAddRoadNoAdjacentPath() {
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
    @Test
    public void testIllegalAddRoadIncorrectResources() {
        ImmutableList<Operation> addRoad = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.PATH66, Constants.ROAD02PB),
                new Set(Constants.ROAD02PB, Constants.PATH66),
                new SetVisibility(Constants.RESOURCECARD00PB),
                new SetVisibility(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD00PB),
                new Delete(Constants.RESOURCECARD02PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addRoad),
                createAddAssetState(),
                addRoad,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addRoad),
                createAllAssetsUsedState(),
                addRoad,
                Constants.pbId);

        assertHacker(verifyMove);
    }

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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addSettlement),
                createAddAssetState(),
                addSettlement,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addSettlement),
                createAllAssetsUsedState(),
                addSettlement,
                Constants.pbId);

        assertHacker(verifyMove);
    }

    @Test
    public void testLegalAddCity() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE23, Constants.CITY00PB),
                new Set(Constants.CITY00PB, Constants.NODE23),
                new Delete(Constants.SETTLEMENT00PB),
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addCity),
                createAddAssetState(),
                addCity,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addCity),
                createAddAssetState(),
                addCity,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
    @Test
    public void testIllegalAddCityIncorrectResources() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE21, Constants.CITY03PB),
                new Set(Constants.CITY03PB, Constants.NODE21),
                new Delete(Constants.SETTLEMENT00PB),
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addCity),
                createAllAssetsUsedState(),
                addCity,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
    @Test
    public void testIllegalAddCityNoneLeft() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE21, Constants.CITY03PB),
                new Set(Constants.CITY03PB, Constants.NODE21),
                new Delete(Constants.SETTLEMENT00PB),
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAllAssetsUsedState(), addCity),
                createAllAssetsUsedState(),
                addCity,
                Constants.pbId);

        assertHacker(verifyMove);
    }

    @Test
    public void testLegalPurchaseDevelopmentCard() {
        ImmutableList<Operation> addDevelopmentCard = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.DEVELOPMENTCARD00, Constants.visibleToPB),
                new Delete(Constants.RESOURCECARD02PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD05PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addDevelopmentCard),
                createAddAssetState(),
                addDevelopmentCard,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
    @Test
    public void testIllegalPurchaseDevelopmentCardIncorrectResources() {
        ImmutableList<Operation> addDevelopmentCard = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.DEVELOPMENTCARD00, Constants.visibleToPB),
                new Delete(Constants.RESOURCECARD01PB),
                new Delete(Constants.RESOURCECARD03PB),
                new Delete(Constants.RESOURCECARD05PB)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createAddAssetState(), addDevelopmentCard),
                createAddAssetState(),
                addDevelopmentCard,
                Constants.pbId);

        assertHacker(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), addSettlement),
                createPrepForVictoryState(),
                addSettlement,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
    @Test
    public void testEndGameByBuildingCity() {
        ImmutableList<Operation> addCity = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new Set(Constants.NODE21, Constants.CITY03PB),
                new Set(Constants.CITY03PB, Constants.NODE21),
                new Delete(Constants.SETTLEMENT00PB),
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), addCity),
                createPrepForVictoryState(),
                addCity,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
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
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), addRoad),
                createPrepForVictoryState(),
                addRoad,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
    
    @Test
    public void testEndGameByGettingLargestArmy() {
        ImmutableList<Operation> playDevelopmentCard = ImmutableList.<Operation>of(
                new Set(Constants.TURN, Constants.PB),
                new SetVisibility(Constants.DEVELOPMENTCARD00), //this is a soldier card
                new Delete(Constants.DEVELOPMENTCARD00),
                new Set(Constants.ROBBER, Constants.HEX12),
                new Set(Constants.SOLDIERCOUNTPB, 7),
                new Set(Constants.LARGESTARMY, Constants.PB),
                new EndGame(Constants.pbId)
                );
        
        VerifyMove verifyMove = new VerifyMove(
                Constants.pbId,
                Constants.playersInfo,
                applyMoveToState(createPrepForVictoryState(), playDevelopmentCard),
                createPrepForVictoryState(),
                playDevelopmentCard,
                Constants.pbId);
        
        assertMoveOk(verifyMove);
    }
}
