package settlersofcatan.client;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

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
    

    private static final String TURN = "turn"; // turn of which player (PB,PR,PY,PG)
    private static final String PLAYER_ID = "playerId";
    private static final String HEX_ID = "hexId";
    private static final String NODE_ID = "nodeId";
    private static final String PATH_ID = "pathId";
    private static final String PB = "PB"; // Blue Player
    private static final String PR = "PR"; // Red Player
    private static final String PY = "PY"; // Yellow Player
    private static final String PG = "PG"; // Green Player
    private static final String HEX00 = "Hex00";
    private static final String HEX01 = "Hex01";
    private static final String HEX02 = "Hex02";
    private static final String HEX03 = "Hex03";
    private static final String HEX04 = "Hex04";
    private static final String HEX05 = "Hex05";
    private static final String HEX06 = "Hex06";
    private static final String HEX07 = "Hex07";
    private static final String HEX08 = "Hex08";
    private static final String HEX09 = "Hex09";
    private static final String HEX10 = "Hex10";
    private static final String HEX11 = "Hex11";
    private static final String HEX12 = "Hex12";
    private static final String HEX13 = "Hex13";
    private static final String HEX14 = "Hex14";
    private static final String HEX15 = "Hex15";
    private static final String HEX16 = "Hex16";
    private static final String HEX17 = "Hex17";
    private static final String HEX18 = "Hex18";
    private static final String NODE00 = "Node00";
    private static final String NODE01 = "Node01";
    private static final String NODE02 = "Node02";
    private static final String NODE03 = "Node03";
    private static final String NODE04 = "Node04";
    private static final String NODE05 = "Node05";
    private static final String NODE06 = "Node06";
    private static final String NODE07 = "Node07";
    private static final String NODE08 = "Node08";
    private static final String NODE09 = "Node09";
    private static final String NODE10 = "Node10";
    private static final String NODE11 = "Node11";
    private static final String NODE12 = "Node12";
    private static final String NODE13 = "Node13";
    private static final String NODE14 = "Node14";
    private static final String NODE15 = "Node15";
    private static final String NODE16 = "Node16";
    private static final String NODE17 = "Node17";
    private static final String NODE18 = "Node18";
    private static final String NODE19 = "Node19";
    private static final String NODE20 = "Node20";
    private static final String NODE21 = "Node21";
    private static final String NODE22 = "Node22";
    private static final String NODE23 = "Node23";
    private static final String NODE24 = "Node24";
    private static final String NODE25 = "Node25";
    private static final String NODE26 = "Node26";
    private static final String NODE27 = "Node27";
    private static final String NODE28 = "Node28";
    private static final String NODE29 = "Node29";
    private static final String NODE30 = "Node30";
    private static final String NODE31 = "Node31";
    private static final String NODE32 = "Node32";
    private static final String NODE33 = "Node33";
    private static final String NODE34 = "Node34";
    private static final String NODE35 = "Node35";
    private static final String NODE36 = "Node36";
    private static final String NODE37 = "Node37";
    private static final String NODE38 = "Node38";
    private static final String NODE39 = "Node39";
    private static final String NODE40 = "Node40";
    private static final String NODE41 = "Node41";
    private static final String NODE42 = "Node42";
    private static final String NODE43 = "Node43";
    private static final String NODE44 = "Node44";
    private static final String NODE45 = "Node45";
    private static final String NODE46 = "Node46";
    private static final String NODE47 = "Node47";
    private static final String NODE48 = "Node48";
    private static final String NODE49 = "Node49";
    private static final String NODE50 = "Node50";
    private static final String NODE51 = "Node51";
    private static final String NODE52 = "Node52";
    private static final String NODE53 = "Node53";
    private static final String PATH00 = "Path00";
    private static final String PATH01 = "Path01";
    private static final String PATH02 = "Path02";
    private static final String PATH03 = "Path03";
    private static final String PATH04 = "Path04";
    private static final String PATH05 = "Path05";
    private static final String PATH06 = "Path06";
    private static final String PATH07 = "Path07";
    private static final String PATH08 = "Path08";
    private static final String PATH09 = "Path09";
    private static final String PATH10 = "Path10";
    private static final String PATH11 = "Path11";
    private static final String PATH12 = "Path12";
    private static final String PATH13 = "Path13";
    private static final String PATH14 = "Path14";
    private static final String PATH15 = "Path15";
    private static final String PATH16 = "Path16";
    private static final String PATH17 = "Path17";
    private static final String PATH18 = "Path18";
    private static final String PATH19 = "Path19";
    private static final String PATH20 = "Path20";
    private static final String PATH21 = "Path21";
    private static final String PATH22 = "Path22";
    private static final String PATH23 = "Path23";
    private static final String PATH24 = "Path24";
    private static final String PATH25 = "Path25";
    private static final String PATH26 = "Path26";
    private static final String PATH27 = "Path27";
    private static final String PATH28 = "Path28";
    private static final String PATH29 = "Path29";
    private static final String PATH30 = "Path30";
    private static final String PATH31 = "Path31";
    private static final String PATH32 = "Path32";
    private static final String PATH33 = "Path33";
    private static final String PATH34 = "Path34";
    private static final String PATH35 = "Path35";
    private static final String PATH36 = "Path36";
    private static final String PATH37 = "Path37";
    private static final String PATH38 = "Path38";
    private static final String PATH39 = "Path39";
    private static final String PATH40 = "Path40";
    private static final String PATH41 = "Path41";
    private static final String PATH42 = "Path42";
    private static final String PATH43 = "Path43";
    private static final String PATH44 = "Path44";
    private static final String PATH45 = "Path45";
    private static final String PATH46 = "Path46";
    private static final String PATH47 = "Path47";
    private static final String PATH48 = "Path48";
    private static final String PATH49 = "Path49";
    private static final String PATH50 = "Path50";
    private static final String PATH51 = "Path51";
    private static final String PATH52 = "Path52";
    private static final String PATH53 = "Path53";
    private static final String PATH54 = "Path54";
    private static final String PATH55 = "Path55";
    private static final String PATH56 = "Path56";
    private static final String PATH57 = "Path57";
    private static final String PATH58 = "Path58";
    private static final String PATH59 = "Path59";
    private static final String PATH60 = "Path60";
    private static final String PATH61 = "Path61";
    private static final String PATH62 = "Path62";
    private static final String PATH63 = "Path63";
    private static final String PATH64 = "Path64";
    private static final String PATH65 = "Path65";
    private static final String PATH66 = "Path66";
    private static final String PATH67 = "Path67";
    private static final String PATH68 = "Path68";
    private static final String PATH69 = "Path69";
    private static final String PATH70 = "Path70";
    private static final String PATH71 = "Path71";
    private final int pbId = 0;
    private final int prId = 1;
    private final int pyId = 2;
    private final int pgId = 3;
    private final int hex00Id = 00;
    private final int hex01Id = 01;
    private final int hex02Id = 02;
    private final int hex03Id = 03;
    private final int hex04Id = 04;
    private final int hex05Id = 05;
    private final int hex06Id = 06;
    private final int hex07Id = 07;
    private final int hex08Id = 08;
    private final int hex09Id = 09;
    private final int hex10Id = 10;
    private final int hex11Id = 11;
    private final int hex12Id = 12;
    private final int hex13Id = 13;
    private final int hex14Id = 14;
    private final int hex15Id = 15;
    private final int hex16Id = 16;
    private final int hex17Id = 17;
    private final int hex18Id = 18;
    private final List<Integer> visibleToPB = ImmutableList.of(pbId);
    private final List<Integer> visibleToPR = ImmutableList.of(prId);
    private final List<Integer> visibleToPY = ImmutableList.of(pyId);
    private final List<Integer> visibleToPG = ImmutableList.of(pgId);
    private final Map<String, Object> pbInfo = ImmutableMap.<String, Object>of(PLAYER_ID, pbId);
    private final Map<String, Object> prInfo = ImmutableMap.<String, Object>of(PLAYER_ID, prId);
    private final Map<String, Object> pyInfo = ImmutableMap.<String, Object>of(PLAYER_ID, pyId);
    private final Map<String, Object> pgInfo = ImmutableMap.<String, Object>of(PLAYER_ID, pgId);
    private final List<Map<String, Object>> playersInfo = ImmutableList.of(pbInfo, prInfo, pyInfo, pgInfo);
    private final Map<String, Object> emptyState = ImmutableMap.<String, Object>of();
    private final Map<String, Object> nonEmptyState = ImmutableMap.<String, Object>of("k", "v");
    
    private VerifyMove move(int lastMovePlayerId,
    		Map<String, Object> lastState, List<Operation> lastMove) {
        return new VerifyMove(pbId, playersInfo,
    	        emptyState,lastState, lastMove, lastMovePlayerId);
    }

    
    
    
    @Test
    public void testIllegalRoadLocation() {
        assertHacker(move());
    }
    
    
    Map<String,Object> pbIllegalRoadSetupState = ImmutableMap.<String, Object>of("",null);
}
