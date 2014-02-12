package settlersofcatan.client;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import settlersofcatan.client.Constants;

import settlersofcatan.client.GameApi.Delete;
import settlersofcatan.client.GameApi.Operation;
import settlersofcatan.client.GameApi.Set;
import settlersofcatan.client.GameApi.SetVisibility;
import settlersofcatan.client.GameApi.Shuffle;
import settlersofcatan.client.GameApi.VerifyMove;
import settlersofcatan.client.GameApi.VerifyMoveDone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SettlersOfCatanLogicTest {
	SettlersOfCatanLogic settlersOfCatanLogic = new SettlersOfCatanLogic();
	
	private void assertMoveOk(VerifyMove verifyMove) {
        settlersOfCatanLogic.checkMoveIsLegal(verifyMove);
    }

    private void assertHacker(VerifyMove verifyMove) {
        VerifyMoveDone verifyDone = settlersOfCatanLogic.verify(verifyMove);
        assertEquals(verifyMove.getLastMovePlayerId(), verifyDone.getHackerPlayerId());
    }
    
    private Map<String, Object> createEmptyState()
    {
    	Map<String, Object> emptyState;
		emptyState.put(Constants.TURN, Constants.PB);
		emptyState.put(Constants.DIE0, "");
		emptyState.put(Constants.DIE1, "");
		emptyState.put(Constants.HEX00, Constants.ORE); // This will be randomized in a real game
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
		emptyState.put(Constants.NODE00, "");
		emptyState.put(Constants.NODE01, "");
		emptyState.put(Constants.NODE02, "");
		emptyState.put(Constants.NODE03, "");
		emptyState.put(Constants.NODE04, "");
		emptyState.put(Constants.NODE05, "");
		emptyState.put(Constants.NODE06, "");
		emptyState.put(Constants.NODE07, "");
		emptyState.put(Constants.NODE08, "");
		emptyState.put(Constants.NODE09, "");
		emptyState.put(Constants.NODE10, "");
		emptyState.put(Constants.NODE11, "");
		emptyState.put(Constants.NODE12, "");
		emptyState.put(Constants.NODE13, "");
		emptyState.put(Constants.NODE14, "");
		emptyState.put(Constants.NODE15, "");
		emptyState.put(Constants.NODE16, "");
		emptyState.put(Constants.NODE17, "");
		emptyState.put(Constants.NODE18, "");
		emptyState.put(Constants.NODE19, "");
		emptyState.put(Constants.NODE20, "");
		emptyState.put(Constants.NODE21, "");
		emptyState.put(Constants.NODE22, "");
		emptyState.put(Constants.NODE23, "");
		emptyState.put(Constants.NODE24, "");
		emptyState.put(Constants.NODE25, "");
		emptyState.put(Constants.NODE26, "");
		emptyState.put(Constants.NODE27, "");
		emptyState.put(Constants.NODE28, "");
		emptyState.put(Constants.NODE29, "");
		emptyState.put(Constants.NODE30, "");
		emptyState.put(Constants.NODE31, "");
		emptyState.put(Constants.NODE32, "");
		emptyState.put(Constants.NODE33, "");
		emptyState.put(Constants.NODE34, "");
		emptyState.put(Constants.NODE35, "");
		emptyState.put(Constants.NODE36, "");
		emptyState.put(Constants.NODE37, "");
		emptyState.put(Constants.NODE38, "");
		emptyState.put(Constants.NODE39, "");
		emptyState.put(Constants.NODE40, "");
		emptyState.put(Constants.NODE41, "");
		emptyState.put(Constants.NODE42, "");
		emptyState.put(Constants.NODE43, "");
		emptyState.put(Constants.NODE44, "");
		emptyState.put(Constants.NODE45, "");
		emptyState.put(Constants.NODE46, "");
		emptyState.put(Constants.NODE47, "");
		emptyState.put(Constants.NODE48, "");
		emptyState.put(Constants.NODE49, "");
		emptyState.put(Constants.NODE50, "");
		emptyState.put(Constants.NODE51, "");
		emptyState.put(Constants.NODE52, "");
		emptyState.put(Constants.NODE53, "");
		emptyState.put(Constants.PATH00, "");
		emptyState.put(Constants.PATH01, "");
		emptyState.put(Constants.PATH02, "");
		emptyState.put(Constants.PATH03, "");
		emptyState.put(Constants.PATH04, "");
		emptyState.put(Constants.PATH05, "");
		emptyState.put(Constants.PATH06, "");
		emptyState.put(Constants.PATH07, "");
		emptyState.put(Constants.PATH08, "");
		emptyState.put(Constants.PATH09, "");
		emptyState.put(Constants.PATH10, "");
		emptyState.put(Constants.PATH11, "");
		emptyState.put(Constants.PATH12, "");
		emptyState.put(Constants.PATH13, "");
		emptyState.put(Constants.PATH14, "");
		emptyState.put(Constants.PATH15, "");
		emptyState.put(Constants.PATH16, "");
		emptyState.put(Constants.PATH17, "");
		emptyState.put(Constants.PATH18, "");
		emptyState.put(Constants.PATH19, "");
		emptyState.put(Constants.PATH20, "");
		emptyState.put(Constants.PATH21, "");
		emptyState.put(Constants.PATH22, "");
		emptyState.put(Constants.PATH23, "");
		emptyState.put(Constants.PATH24, "");
		emptyState.put(Constants.PATH25, "");
		emptyState.put(Constants.PATH26, "");
		emptyState.put(Constants.PATH27, "");
		emptyState.put(Constants.PATH28, "");
		emptyState.put(Constants.PATH29, "");
		emptyState.put(Constants.PATH30, "");
		emptyState.put(Constants.PATH31, "");
		emptyState.put(Constants.PATH32, "");
		emptyState.put(Constants.PATH33, "");
		emptyState.put(Constants.PATH34, "");
		emptyState.put(Constants.PATH35, "");
		emptyState.put(Constants.PATH36, "");
		emptyState.put(Constants.PATH37, "");
		emptyState.put(Constants.PATH38, "");
		emptyState.put(Constants.PATH39, "");
		emptyState.put(Constants.PATH40, "");
		emptyState.put(Constants.PATH41, "");
		emptyState.put(Constants.PATH42, "");
		emptyState.put(Constants.PATH43, "");
		emptyState.put(Constants.PATH44, "");
		emptyState.put(Constants.PATH45, "");
		emptyState.put(Constants.PATH46, "");
		emptyState.put(Constants.PATH47, "");
		emptyState.put(Constants.PATH48, "");
		emptyState.put(Constants.PATH49, "");
		emptyState.put(Constants.PATH50, "");
		emptyState.put(Constants.PATH51, "");
		emptyState.put(Constants.PATH52, "");
		emptyState.put(Constants.PATH53, "");
		emptyState.put(Constants.PATH54, "");
		emptyState.put(Constants.PATH55, "");
		emptyState.put(Constants.PATH56, "");
		emptyState.put(Constants.PATH57, "");
		emptyState.put(Constants.PATH58, "");
		emptyState.put(Constants.PATH59, "");
		emptyState.put(Constants.PATH60, "");
		emptyState.put(Constants.PATH61, "");
		emptyState.put(Constants.PATH62, "");
		emptyState.put(Constants.PATH63, "");
		emptyState.put(Constants.PATH64, "");
		emptyState.put(Constants.PATH65, "");
		emptyState.put(Constants.PATH66, "");
		emptyState.put(Constants.PATH67, "");
		emptyState.put(Constants.PATH68, "");
		emptyState.put(Constants.PATH69, "");
		emptyState.put(Constants.PATH70, "");
		emptyState.put(Constants.PATH71, "");
		emptyState.put(Constants.HARBOR00, Constants.HARBORTYPE03); // This will be randomized in real game
		emptyState.put(Constants.HARBOR01, Constants.HARBORTYPE00);
		emptyState.put(Constants.HARBOR02, Constants.HARBORTYPE01);
		emptyState.put(Constants.HARBOR03, Constants.HARBORTYPE00);
		emptyState.put(Constants.HARBOR04, Constants.HARBORTYPE05);
		emptyState.put(Constants.HARBOR05, Constants.HARBORTYPE00);
		emptyState.put(Constants.HARBOR06, Constants.HARBORTYPE02);
		emptyState.put(Constants.HARBOR07, Constants.HARBORTYPE00);
		emptyState.put(Constants.HARBOR08, Constants.HARBORTYPE04);
		emptyState.put(Constants.ROBBER, "");
		emptyState.put(Constants.DEVELOPMENTCARD00, Constants.DEVELOPMENTCARDTYPE00); // This will be randomized in real game
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
		emptyState.put(Constants.CITY00PB, "");
		emptyState.put(Constants.CITY01PB, "");
		emptyState.put(Constants.CITY02PB, "");
		emptyState.put(Constants.CITY03PB, "");
		emptyState.put(Constants.CITY00PR, "");
		emptyState.put(Constants.CITY01PR, "");
		emptyState.put(Constants.CITY02PR, "");
		emptyState.put(Constants.CITY03PR, "");
		emptyState.put(Constants.CITY00PY, "");
		emptyState.put(Constants.CITY01PY, "");
		emptyState.put(Constants.CITY02PY, "");
		emptyState.put(Constants.CITY03PY, "");
		emptyState.put(Constants.CITY00PG, "");
		emptyState.put(Constants.CITY01PG, "");
		emptyState.put(Constants.CITY02PG, "");
		emptyState.put(Constants.CITY03PG, "");
		emptyState.put(Constants.SETTLEMENT00PB, "");
		emptyState.put(Constants.SETTLEMENT01PB, "");
		emptyState.put(Constants.SETTLEMENT02PB, "");
		emptyState.put(Constants.SETTLEMENT03PB, "");
		emptyState.put(Constants.SETTLEMENT04PB, "");
		emptyState.put(Constants.SETTLEMENT00PR, "");
		emptyState.put(Constants.SETTLEMENT01PR, "");
		emptyState.put(Constants.SETTLEMENT02PR, "");
		emptyState.put(Constants.SETTLEMENT03PR, "");
		emptyState.put(Constants.SETTLEMENT04PR, "");
		emptyState.put(Constants.SETTLEMENT00PY, "");
		emptyState.put(Constants.SETTLEMENT01PY, "");
		emptyState.put(Constants.SETTLEMENT02PY, "");
		emptyState.put(Constants.SETTLEMENT03PY, "");
		emptyState.put(Constants.SETTLEMENT04PY, "");
		emptyState.put(Constants.SETTLEMENT00PG, "");
		emptyState.put(Constants.SETTLEMENT01PG, "");
		emptyState.put(Constants.SETTLEMENT02PG, "");
		emptyState.put(Constants.SETTLEMENT03PG, "");
		emptyState.put(Constants.SETTLEMENT04PG, "");
		emptyState.put(Constants.ROAD00PB, "");
		emptyState.put(Constants.ROAD01PB, "");
		emptyState.put(Constants.ROAD02PB, "");
		emptyState.put(Constants.ROAD03PB, "");
		emptyState.put(Constants.ROAD04PB, "");
		emptyState.put(Constants.ROAD05PB, "");
		emptyState.put(Constants.ROAD06PB, "");
		emptyState.put(Constants.ROAD07PB, "");
		emptyState.put(Constants.ROAD08PB, "");
		emptyState.put(Constants.ROAD09PB, "");
		emptyState.put(Constants.ROAD10PB, "");
		emptyState.put(Constants.ROAD11PB, "");
		emptyState.put(Constants.ROAD12PB, "");
		emptyState.put(Constants.ROAD13PB, "");
		emptyState.put(Constants.ROAD14PB, "");
		emptyState.put(Constants.ROAD00PR, "");
		emptyState.put(Constants.ROAD01PR, "");
		emptyState.put(Constants.ROAD02PR, "");
		emptyState.put(Constants.ROAD03PR, "");
		emptyState.put(Constants.ROAD04PR, "");
		emptyState.put(Constants.ROAD05PR, "");
		emptyState.put(Constants.ROAD06PR, "");
		emptyState.put(Constants.ROAD07PR, "");
		emptyState.put(Constants.ROAD08PR, "");
		emptyState.put(Constants.ROAD09PR, "");
		emptyState.put(Constants.ROAD10PR, "");
		emptyState.put(Constants.ROAD11PR, "");
		emptyState.put(Constants.ROAD12PR, "");
		emptyState.put(Constants.ROAD13PR, "");
		emptyState.put(Constants.ROAD14PR, "");
		emptyState.put(Constants.ROAD00PY, "");
		emptyState.put(Constants.ROAD01PY, "");
		emptyState.put(Constants.ROAD02PY, "");
		emptyState.put(Constants.ROAD03PY, "");
		emptyState.put(Constants.ROAD04PY, "");
		emptyState.put(Constants.ROAD05PY, "");
		emptyState.put(Constants.ROAD06PY, "");
		emptyState.put(Constants.ROAD07PY, "");
		emptyState.put(Constants.ROAD08PY, "");
		emptyState.put(Constants.ROAD09PY, "");
		emptyState.put(Constants.ROAD10PY, "");
		emptyState.put(Constants.ROAD11PY, "");
		emptyState.put(Constants.ROAD12PY, "");
		emptyState.put(Constants.ROAD13PY, "");
		emptyState.put(Constants.ROAD14PY, "");
		emptyState.put(Constants.ROAD00PG, "");
		emptyState.put(Constants.ROAD01PG, "");
		emptyState.put(Constants.ROAD02PG, "");
		emptyState.put(Constants.ROAD03PG, "");
		emptyState.put(Constants.ROAD04PG, "");
		emptyState.put(Constants.ROAD05PG, "");
		emptyState.put(Constants.ROAD06PG, "");
		emptyState.put(Constants.ROAD07PG, "");
		emptyState.put(Constants.ROAD08PG, "");
		emptyState.put(Constants.ROAD09PG, "");
		emptyState.put(Constants.ROAD10PG, "");
		emptyState.put(Constants.ROAD11PG, "");
		emptyState.put(Constants.ROAD12PG, "");
		emptyState.put(Constants.ROAD13PG, "");
		emptyState.put(Constants.ROAD14PG, "");
		emptyState.put(Constants.RESOURCECARD00PB, "");
		emptyState.put(Constants.RESOURCECARD01PB, "");
		emptyState.put(Constants.RESOURCECARD02PB, "");
		emptyState.put(Constants.RESOURCECARD03PB, "");
		emptyState.put(Constants.RESOURCECARD04PB, "");
		emptyState.put(Constants.RESOURCECARD05PB, "");
		emptyState.put(Constants.RESOURCECARD06PB, "");
		emptyState.put(Constants.RESOURCECARD07PB, "");
		emptyState.put(Constants.RESOURCECARD08PB, "");
		emptyState.put(Constants.RESOURCECARD09PB, "");
		emptyState.put(Constants.RESOURCECARD10PB, "");
		emptyState.put(Constants.RESOURCECARD11PB, "");
		emptyState.put(Constants.RESOURCECARD12PB, "");
		emptyState.put(Constants.RESOURCECARD13PB, "");
		emptyState.put(Constants.RESOURCECARD14PB, "");
		emptyState.put(Constants.RESOURCECARD15PB, "");
		emptyState.put(Constants.RESOURCECARD16PB, "");
		emptyState.put(Constants.RESOURCECARD17PB, "");
		emptyState.put(Constants.RESOURCECARD18PB, "");
		emptyState.put(Constants.RESOURCECARD19PB, "");
		emptyState.put(Constants.RESOURCECARD20PB, "");
		emptyState.put(Constants.RESOURCECARD21PB, "");
		emptyState.put(Constants.RESOURCECARD22PB, "");
		emptyState.put(Constants.RESOURCECARD23PB, "");
		emptyState.put(Constants.RESOURCECARD24PB, "");
		emptyState.put(Constants.RESOURCECARD25PB, "");
		emptyState.put(Constants.RESOURCECARD26PB, "");
		emptyState.put(Constants.RESOURCECARD27PB, "");
		emptyState.put(Constants.RESOURCECARD28PB, "");
		emptyState.put(Constants.RESOURCECARD29PB, "");
		emptyState.put(Constants.RESOURCECARD00PR, "");
		emptyState.put(Constants.RESOURCECARD01PR, "");
		emptyState.put(Constants.RESOURCECARD02PR, "");
		emptyState.put(Constants.RESOURCECARD03PR, "");
		emptyState.put(Constants.RESOURCECARD04PR, "");
		emptyState.put(Constants.RESOURCECARD05PR, "");
		emptyState.put(Constants.RESOURCECARD06PR, "");
		emptyState.put(Constants.RESOURCECARD07PR, "");
		emptyState.put(Constants.RESOURCECARD08PR, "");
		emptyState.put(Constants.RESOURCECARD09PR, "");
		emptyState.put(Constants.RESOURCECARD10PR, "");
		emptyState.put(Constants.RESOURCECARD11PR, "");
		emptyState.put(Constants.RESOURCECARD12PR, "");
		emptyState.put(Constants.RESOURCECARD13PR, "");
		emptyState.put(Constants.RESOURCECARD14PR, "");
		emptyState.put(Constants.RESOURCECARD15PR, "");
		emptyState.put(Constants.RESOURCECARD16PR, "");
		emptyState.put(Constants.RESOURCECARD17PR, "");
		emptyState.put(Constants.RESOURCECARD18PR, "");
		emptyState.put(Constants.RESOURCECARD19PR, "");
		emptyState.put(Constants.RESOURCECARD20PR, "");
		emptyState.put(Constants.RESOURCECARD21PR, "");
		emptyState.put(Constants.RESOURCECARD22PR, "");
		emptyState.put(Constants.RESOURCECARD23PR, "");
		emptyState.put(Constants.RESOURCECARD24PR, "");
		emptyState.put(Constants.RESOURCECARD25PR, "");
		emptyState.put(Constants.RESOURCECARD26PR, "");
		emptyState.put(Constants.RESOURCECARD27PR, "");
		emptyState.put(Constants.RESOURCECARD28PR, "");
		emptyState.put(Constants.RESOURCECARD29PR, "");
		emptyState.put(Constants.RESOURCECARD00PY, "");
		emptyState.put(Constants.RESOURCECARD01PY, "");
		emptyState.put(Constants.RESOURCECARD02PY, "");
		emptyState.put(Constants.RESOURCECARD03PY, "");
		emptyState.put(Constants.RESOURCECARD04PY, "");
		emptyState.put(Constants.RESOURCECARD05PY, "");
		emptyState.put(Constants.RESOURCECARD06PY, "");
		emptyState.put(Constants.RESOURCECARD07PY, "");
		emptyState.put(Constants.RESOURCECARD08PY, "");
		emptyState.put(Constants.RESOURCECARD09PY, "");
		emptyState.put(Constants.RESOURCECARD10PY, "");
		emptyState.put(Constants.RESOURCECARD11PY, "");
		emptyState.put(Constants.RESOURCECARD12PY, "");
		emptyState.put(Constants.RESOURCECARD13PY, "");
		emptyState.put(Constants.RESOURCECARD14PY, "");
		emptyState.put(Constants.RESOURCECARD15PY, "");
		emptyState.put(Constants.RESOURCECARD16PY, "");
		emptyState.put(Constants.RESOURCECARD17PY, "");
		emptyState.put(Constants.RESOURCECARD18PY, "");
		emptyState.put(Constants.RESOURCECARD19PY, "");
		emptyState.put(Constants.RESOURCECARD20PY, "");
		emptyState.put(Constants.RESOURCECARD21PY, "");
		emptyState.put(Constants.RESOURCECARD22PY, "");
		emptyState.put(Constants.RESOURCECARD23PY, "");
		emptyState.put(Constants.RESOURCECARD24PY, "");
		emptyState.put(Constants.RESOURCECARD25PY, "");
		emptyState.put(Constants.RESOURCECARD26PY, "");
		emptyState.put(Constants.RESOURCECARD27PY, "");
		emptyState.put(Constants.RESOURCECARD28PY, "");
		emptyState.put(Constants.RESOURCECARD29PY, "");
		emptyState.put(Constants.RESOURCECARD00PG, "");
		emptyState.put(Constants.RESOURCECARD01PG, "");
		emptyState.put(Constants.RESOURCECARD02PG, "");
		emptyState.put(Constants.RESOURCECARD03PG, "");
		emptyState.put(Constants.RESOURCECARD04PG, "");
		emptyState.put(Constants.RESOURCECARD05PG, "");
		emptyState.put(Constants.RESOURCECARD06PG, "");
		emptyState.put(Constants.RESOURCECARD07PG, "");
		emptyState.put(Constants.RESOURCECARD08PG, "");
		emptyState.put(Constants.RESOURCECARD09PG, "");
		emptyState.put(Constants.RESOURCECARD10PG, "");
		emptyState.put(Constants.RESOURCECARD11PG, "");
		emptyState.put(Constants.RESOURCECARD12PG, "");
		emptyState.put(Constants.RESOURCECARD13PG, "");
		emptyState.put(Constants.RESOURCECARD14PG, "");
		emptyState.put(Constants.RESOURCECARD15PG, "");
		emptyState.put(Constants.RESOURCECARD16PG, "");
		emptyState.put(Constants.RESOURCECARD17PG, "");
		emptyState.put(Constants.RESOURCECARD18PG, "");
		emptyState.put(Constants.RESOURCECARD19PG, "");
		emptyState.put(Constants.RESOURCECARD20PG, "");
		emptyState.put(Constants.RESOURCECARD21PG, "");
		emptyState.put(Constants.RESOURCECARD22PG, "");
		emptyState.put(Constants.RESOURCECARD23PG, "");
		emptyState.put(Constants.RESOURCECARD24PG, "");
		emptyState.put(Constants.RESOURCECARD25PG, "");
		emptyState.put(Constants.RESOURCECARD26PG, "");
		emptyState.put(Constants.RESOURCECARD27PG, "");
		emptyState.put(Constants.RESOURCECARD28PG, "");
		emptyState.put(Constants.RESOURCECARD29PG, "");
    }

    /*@Test
    public void testLegalMoveXXX() {
      VerifyMove verifyMove = new VerifyMove(...); // The move we're testing is legal
      VerifyMoveDone verifyDone = new YourGameLogic().verify(verifyMove);
      assertEquals(0, verifyDone.getHackerPlayerId());
    }

    @Test
    public void testIllegalMoveYYY() {
      VerifyMove verifyMove = new VerifyMove(...); // The move we're testing is ILLEGAL!
      VerifyMoveDone verifyDone = new YourGameLogic().verify(verifyMove);
      assertEquals(verifyMove.getLastMovePlayerId(), verifyDone.getHackerPlayerId());
    }*/
    
    // VerifyMove - YourPlayerId - int
    //              Player List - List
    //              State - Map
    //				LastState - Map
    //				LastMove - List
    //				LastMovePlayerId - Int
    
    
    private Map<String[]Object> addRoadState = ImmutableMap.<String, Object>of(
    		TURN, PB
    		);
    
    @Test
    public void testlegalAddRoad() {
        VerifyMove verifyMove = new VerifyMove(pbId, (List) playersInfo, emptyState, emptyState, null, pbId);
    }
    
    
    private Map<String, Object> changeState(ImmutableMap<String, Object> previousState, String propString, Object propObject)
    {
    	Map<String, Object> newState = previousState;
    	
    	newState.put(propString, propObject);
    	
    	return newState;
    }
}
