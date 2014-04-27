package org.settlersofcatan.client;

import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.core.shared.GWT;

public class SettlersOfCatanConstants {
    
    public void useSettlersOfCanaLanguageFormatConstants()
    {
        SettlersOfCatanLanguageFormatConstants constants = (SettlersOfCatanLanguageFormatConstants) GWT.create(SettlersOfCatanLanguageFormatConstants.class);
        
    }
    
    public static final String MAKEFIRSTFREEMOVESETTLEMENT = "MAKEFIRSTFREEMOVESETTLEMENT";
    public static final String MAKEFIRSTFREEMOVEROAD = "MAKEFIRSTFREEMOVEROAD";
    public static final String MAKESECONDFREEMOVESETTLEMENT = "MAKESECONDFREEMOVESETTLEMENT";
    public static final String MAKESECONDFREEMOVEROAD = "MAKESECONDFREEMOVEROAD";
    public static final String ROLLDICE = "ROLLDICE";
    public static final String ROLLEDTOK = "ROLLED";
    public static final String ROLLED2 = "ROLLED2";
    public static final String ROLLED3 = "ROLLED3";
    public static final String ROLLED4 = "ROLLED4";
    public static final String ROLLED5 = "ROLLED5";
    public static final String ROLLED6 = "ROLLED6";
    public static final String ROLLED8 = "ROLLED8";
    public static final String ROLLED9 = "ROLLED9";
    public static final String ROLLED10 = "ROLLED10";
    public static final String ROLLED11 = "ROLLED11";
    public static final String ROLLED12 = "ROLLED12";
    public static final String CLEARROLL = "CLEARROLL";
    public static final String MAKEMOVE = "MAKEMOVE";
    public static final String CLICKNODE = "CLICKNODE";
    public static final String CLICKPATH = "CLICKPATH";
    public static final String CLICKRESOURCECARDS = "CLICKRESOURCECARDS";
    public static final String ENDGAME = "ENDGAME";
    
    public static final String FIRSTMOVE = "FIRSTMOVE";
    public static final String FIRSTROUNDSETTLEMENT = "FIRSTROUNDSETTLEMENT";
    public static final String FIRSTROUNDROAD = "FIRSTROUNDROAD";
    public static final String SECONDROUNDSETTLEMENT = "SECONDROUNDSETTLEMENT";
    public static final String SECONDROUNDROAD = "SECONDROUNDROAD";
    public static final String MOVEROBBERPT1 = "MOVEROBBERPT1";
    public static final String MOVEROBBERPT2 = "MOVEROBBERPT2";
    public static final String MOVEROBBERPT3 = "MOVEROBBERPT3";
    public static final String MOVEROBBERPT4 = "MOVEROBBERPT4";
    public static final String CHANGETURN = "CHANGETURN";
    public static final String BUILDCITYPT1 = "BUILDCITYPT1";
    public static final String BUILDCITYPT2 = "BUILDCITYPT2";
    public static final String BUILDSETTLEMENTPT1 = "BUILDSETTLEMENTPT1";
    public static final String BUILDSETTLEMENTPT2 = "BUILDSETTLEMENTPT2";
    public static final String BUILDROADPT1 = "BUILDROADPT1";
    public static final String BUILDROADPT2 = "BUILDROADPT2";
    public static final String BUYDEVELOPMENTCARDPT1 = "BUYDEVELOPMENTCARDPT1";
    public static final String BUYDEVELOPMENTCARDPT2 = "BUYDEVELOPMENTCARDPT2";
    public static final String HARBORTRADEPT1 = "HARBORTRADEPT1";
    public static final String HARBORTRADEPT2 = "HARBORTRADEPT2";
    public static final String THREETOONEHARBORTRADEPT1 = "THREETOONEHARBORTRADEPT1";
    public static final String THREETOONEHARBORTRADEPT2 = "THREETOONEHARBORTRADEPT2";
    public static final String TWOTOONEOREHARBORTRADEPT1 = "TWOTOONEOREHARBORTRADEPT1";
    public static final String TWOTOONEOREHARBORTRADEPT2 = "TWOTOONEOREHARBORTRADEPT2";
    public static final String TWOTOONEGRAHARBORTRADEPT1 = "TWOTOONEGRAHARBORTRADEPT1";
    public static final String TWOTOONEGRAHARBORTRADEPT2 = "TWOTOONEGRAHARBORTRADEPT2";
    public static final String TWOTOONELUMHARBORTRADEPT1 = "TWOTOONELUMHARBORTRADEPT1";
    public static final String TWOTOONELUMHARBORTRADEPT2 = "TWOTOONELUMHARBORTRADEPT2";
    public static final String TWOTOONEWOOHARBORTRADEPT1 = "TWOTOONEWOOHARBORTRADEPT1";
    public static final String TWOTOONEWOOHARBORTRADEPT2 = "TWOTOONEWOOHARBORTRADEPT2";
    public static final String TWOTOONEBRIHARBORTRADEPT1 = "TWOTOONBRIEHARBORTRADEPT1";
    public static final String TWOTOONEBRIHARBORTRADEPT2 = "TWOTOONBRIEHARBORTRADEPT2";
    public static final String PLAYDEVELOPMENTCARD = "PLAYDEVELOPMENTCARD";

    public static final String NODETOKEN = "NOD";
    public static final String CITYTOKEN = "CIT";
    public static final String SETTLEMENTTOKEN = "SET";
    public static final String PATHTOKEN = "PAT";
    public static final String ROADTOKEN = "RD";
    public static final String DEVELOPMENTCARDTOKEN = "DEV";
    public static final String RESOURCECARDTOKEN = "RC";
    public static final String HEXTOKEN = "HEX";
    public static final String SOLDIERCOUNTTOKEN = "SOL";
    public static final String HARBORTRADETOKEN = "MAR";
    
    public static final String ADDCITY = "ADDCITY";
    public static final String ADDSETTLEMENT = "ADDSETTLEMENT";
    public static final String ADDLONGESTROAD = "ADDLONGESTROAD";
    public static final String ADDLARGESTARMY = "ADDLARGESTARMY";
    
    public static final String TURN = "turn"; // turn of which player (PB,PR,PY,PG)
    public static final String NOTHING = "";
    public static final String ALL = "";
    public static final String PLAYER_ID = "playerId";
    public static final String HEX_ID = "hexId";
    public static final String NODE_ID = "nodeId";
    public static final String PATH_ID = "pathId";
    public static final String HARBOR_ID = "harborId";
    public static final String DEVELOPMENTCARD_ID = "developmentCardId";
    public static final String CITY_PB_ID = "cityPBId";
    public static final String CITY_PR_ID = "cityPRId";
    public static final String CITY_PY_ID = "cityPYId";
    public static final String CITY_PG_ID = "cityPGId";
    public static final String SETTLEMENT_PB_ID = "settlementPBId";
    public static final String SETTLEMENT_PR_ID = "settlementPRId";
    public static final String SETTLEMENT_PY_ID = "settlementPYId";
    public static final String SETTLEMENT_PG_ID = "settlementPGId";
    public static final String ROAD_PB_ID = "roadPBId";
    public static final String ROAD_PR_ID = "roadPRId";
    public static final String ROAD_PY_ID = "roadPYId";
    public static final String ROAD_PG_ID = "roadPGId";
    public static final String RESOURCECARD_PB_ID = "resourceCardPBId";
    public static final String RESOURCECARD_PR_ID = "resourceCardPRId";
    public static final String RESOURCECARD_PY_ID = "resourceCardPYId";
    public static final String RESOURCECARD_PG_ID = "resourceCardPGId";
    public static final String SOLDIERCOUNT_ID = "soldierCountId";
    public static final String LONGESTROAD_ID = "longestRoadId";
    public static final String LARGESTARMY_ID = "largestArmyId";
    public static final String PB = "PB"; // Blue Player
    public static final String PR = "PR"; // Red Player
    public static final String PY = "PY"; // Yellow Player
    public static final String PG = "PG"; // Green Player
    public static final String DIE0 = "DIE0";
    public static final String DIE1 = "DIE1";
    public static final String HEX00 = "HEX00";
    public static final String HEX01 = "HEX01";
    public static final String HEX02 = "HEX02";
    public static final String HEX03 = "HEX03";
    public static final String HEX04 = "HEX04";
    public static final String HEX05 = "HEX05";
    public static final String HEX06 = "HEX06";
    public static final String HEX07 = "HEX07";
    public static final String HEX08 = "HEX08";
    public static final String HEX09 = "HEX09";
    public static final String HEX10 = "HEX10";
    public static final String HEX11 = "HEX11";
    public static final String HEX12 = "HEX12";
    public static final String HEX13 = "HEX13";
    public static final String HEX14 = "HEX14";
    public static final String HEX15 = "HEX15";
    public static final String HEX16 = "HEX16";
    public static final String HEX17 = "HEX17";
    public static final String HEX18 = "HEX18";
    public static final String NODE00 = "NOD00";
    public static final String NODE01 = "NOD01";
    public static final String NODE02 = "NOD02";
    public static final String NODE03 = "NOD03";
    public static final String NODE04 = "NOD04";
    public static final String NODE05 = "NOD05";
    public static final String NODE06 = "NOD06";
    public static final String NODE07 = "NOD07";
    public static final String NODE08 = "NOD08";
    public static final String NODE09 = "NOD09";
    public static final String NODE10 = "NOD10";
    public static final String NODE11 = "NOD11";
    public static final String NODE12 = "NOD12";
    public static final String NODE13 = "NOD13";
    public static final String NODE14 = "NOD14";
    public static final String NODE15 = "NOD15";
    public static final String NODE16 = "NOD16";
    public static final String NODE17 = "NOD17";
    public static final String NODE18 = "NOD18";
    public static final String NODE19 = "NOD19";
    public static final String NODE20 = "NOD20";
    public static final String NODE21 = "NOD21";
    public static final String NODE22 = "NOD22";
    public static final String NODE23 = "NOD23";
    public static final String NODE24 = "NOD24";
    public static final String NODE25 = "NOD25";
    public static final String NODE26 = "NOD26";
    public static final String NODE27 = "NOD27";
    public static final String NODE28 = "NOD28";
    public static final String NODE29 = "NOD29";
    public static final String NODE30 = "NOD30";
    public static final String NODE31 = "NOD31";
    public static final String NODE32 = "NOD32";
    public static final String NODE33 = "NOD33";
    public static final String NODE34 = "NOD34";
    public static final String NODE35 = "NOD35";
    public static final String NODE36 = "NOD36";
    public static final String NODE37 = "NOD37";
    public static final String NODE38 = "NOD38";
    public static final String NODE39 = "NOD39";
    public static final String NODE40 = "NOD40";
    public static final String NODE41 = "NOD41";
    public static final String NODE42 = "NOD42";
    public static final String NODE43 = "NOD43";
    public static final String NODE44 = "NOD44";
    public static final String NODE45 = "NOD45";
    public static final String NODE46 = "NOD46";
    public static final String NODE47 = "NOD47";
    public static final String NODE48 = "NOD48";
    public static final String NODE49 = "NOD49";
    public static final String NODE50 = "NOD50";
    public static final String NODE51 = "NOD51";
    public static final String NODE52 = "NOD52";
    public static final String NODE53 = "NOD53";
    public static final String PATH00 = "PAT00";
    public static final String PATH01 = "PAT01";
    public static final String PATH02 = "PAT02";
    public static final String PATH03 = "PAT03";
    public static final String PATH04 = "PAT04";
    public static final String PATH05 = "PAT05";
    public static final String PATH06 = "PAT06";
    public static final String PATH07 = "PAT07";
    public static final String PATH08 = "PAT08";
    public static final String PATH09 = "PAT09";
    public static final String PATH10 = "PAT10";
    public static final String PATH11 = "PAT11";
    public static final String PATH12 = "PAT12";
    public static final String PATH13 = "PAT13";
    public static final String PATH14 = "PAT14";
    public static final String PATH15 = "PAT15";
    public static final String PATH16 = "PAT16";
    public static final String PATH17 = "PAT17";
    public static final String PATH18 = "PAT18";
    public static final String PATH19 = "PAT19";
    public static final String PATH20 = "PAT20";
    public static final String PATH21 = "PAT21";
    public static final String PATH22 = "PAT22";
    public static final String PATH23 = "PAT23";
    public static final String PATH24 = "PAT24";
    public static final String PATH25 = "PAT25";
    public static final String PATH26 = "PAT26";
    public static final String PATH27 = "PAT27";
    public static final String PATH28 = "PAT28";
    public static final String PATH29 = "PAT29";
    public static final String PATH30 = "PAT30";
    public static final String PATH31 = "PAT31";
    public static final String PATH32 = "PAT32";
    public static final String PATH33 = "PAT33";
    public static final String PATH34 = "PAT34";
    public static final String PATH35 = "PAT35";
    public static final String PATH36 = "PAT36";
    public static final String PATH37 = "PAT37";
    public static final String PATH38 = "PAT38";
    public static final String PATH39 = "PAT39";
    public static final String PATH40 = "PAT40";
    public static final String PATH41 = "PAT41";
    public static final String PATH42 = "PAT42";
    public static final String PATH43 = "PAT43";
    public static final String PATH44 = "PAT44";
    public static final String PATH45 = "PAT45";
    public static final String PATH46 = "PAT46";
    public static final String PATH47 = "PAT47";
    public static final String PATH48 = "PAT48";
    public static final String PATH49 = "PAT49";
    public static final String PATH50 = "PAT50";
    public static final String PATH51 = "PAT51";
    public static final String PATH52 = "PAT52";
    public static final String PATH53 = "PAT53";
    public static final String PATH54 = "PAT54";
    public static final String PATH55 = "PAT55";
    public static final String PATH56 = "PAT56";
    public static final String PATH57 = "PAT57";
    public static final String PATH58 = "PAT58";
    public static final String PATH59 = "PAT59";
    public static final String PATH60 = "PAT60";
    public static final String PATH61 = "PAT61";
    public static final String PATH62 = "PAT62";
    public static final String PATH63 = "PAT63";
    public static final String PATH64 = "PAT64";
    public static final String PATH65 = "PAT65";
    public static final String PATH66 = "PAT66";
    public static final String PATH67 = "PAT67";
    public static final String PATH68 = "PAT68";
    public static final String PATH69 = "PAT69";
    public static final String PATH70 = "PAT70";
    public static final String PATH71 = "PAT71";
    public static final String HARBOR00 = "MAR00";
    public static final String HARBOR01 = "MAR01";
    public static final String HARBOR02 = "MAR02";
    public static final String HARBOR03 = "MAR03";
    public static final String HARBOR04 = "MAR04";
    public static final String HARBOR05 = "MAR05";
    public static final String HARBOR06 = "MAR06";
    public static final String HARBOR07 = "MAR07";
    public static final String HARBOR08 = "MAR08";
    public static final String HARBORTYPE00 = "3_TO_1";
    public static final String HARBORTYPE01 = "2_TO_1_ORE";
    public static final String HARBORTYPE02 = "2_TO_1_GRA";
    public static final String HARBORTYPE03 = "2_TO_1_LUM";
    public static final String HARBORTYPE04 = "2_TO_1_WOO";
    public static final String HARBORTYPE05 = "2_TO_1_BRI";
    public static final String ROBBER = "ROB";
    public static final String DEVELOPMENTCARD00 = "DEV00";
    public static final String DEVELOPMENTCARD01 = "DEV01";
    public static final String DEVELOPMENTCARD02 = "DEV02";
    public static final String DEVELOPMENTCARD03 = "DEV03";
    public static final String DEVELOPMENTCARD04 = "DEV04";
    public static final String DEVELOPMENTCARD05 = "DEV05";
    public static final String DEVELOPMENTCARD06 = "DEV06";
    public static final String DEVELOPMENTCARD07 = "DEV07";
    public static final String DEVELOPMENTCARD08 = "DEV08";
    public static final String DEVELOPMENTCARD09 = "DEV09";
    public static final String DEVELOPMENTCARD10 = "DEV10";
    public static final String DEVELOPMENTCARD11 = "DEV11";
    public static final String DEVELOPMENTCARD12 = "DEV12";
    public static final String DEVELOPMENTCARD13 = "DEV13";
    public static final String DEVELOPMENTCARD14 = "DEV14";
    public static final String DEVELOPMENTCARD15 = "DEV15";
    public static final String DEVELOPMENTCARD16 = "DEV16";
    public static final String DEVELOPMENTCARD17 = "DEV17";
    public static final String DEVELOPMENTCARD18 = "DEV18";
    public static final String DEVELOPMENTCARD19 = "DEV19";
    public static final String DEVELOPMENTCARD20 = "DEV20";
    public static final String DEVELOPMENTCARD21 = "DEV21";
    public static final String DEVELOPMENTCARD22 = "DEV22";
    public static final String DEVELOPMENTCARD23 = "DEV23";
    public static final String DEVELOPMENTCARD24 = "DEV24";
    public static final String DEVELOPMENTCARDTYPEDEF00 = "SOLDIER";
    public static final String DEVELOPMENTCARDTYPEDEF01 = "YEAR OF PLENTY";
    public static final String DEVELOPMENTCARDTYPEDEF02 = "MONOPOLY";
    public static final String DEVELOPMENTCARDTYPEDEF03 = "ROAD BUILDING";
    public static final String DEVELOPMENTCARDTYPEDEF04 = "LIBRARY";
    public static final String DEVELOPMENTCARDTYPEDEF05 = "GOVERNOR'S HOUSE";
    public static final String DEVELOPMENTCARDTYPEDEF06 = "UNIVERSITY OF CATAN";
    public static final String DEVELOPMENTCARDTYPEDEF07 = "CHAPEL";
    public static final String DEVELOPMENTCARDTYPEDEF08 = "MARKET";
    public static final String MONOPOLYRESOURCE = "MONOPOLYRESOURCE";
    public static final String MONOPOLYBENEFACTOR = "MONOPOLYBENEFACTOR";
    public static final String CITY00PB = "CIT00PB";
    public static final String CITY01PB = "CIT01PB";
    public static final String CITY02PB = "CIT02PB";
    public static final String CITY03PB = "CIT03PB";
    public static final String CITY00PR = "CIT00PR";
    public static final String CITY01PR = "CIT01PR";
    public static final String CITY02PR = "CIT02PR";
    public static final String CITY03PR = "CIT03PR";
    public static final String CITY00PY = "CIT00PY";
    public static final String CITY01PY = "CIT01PY";
    public static final String CITY02PY = "CIT02PY";
    public static final String CITY03PY = "CIT03PY";
    public static final String CITY00PG = "CIT00PG";
    public static final String CITY01PG = "CIT01PG";
    public static final String CITY02PG = "CIT02PG";
    public static final String CITY03PG = "CIT03PG";
    public static final String SETTLEMENT00PB = "SET00PB";
    public static final String SETTLEMENT01PB = "SET01PB";
    public static final String SETTLEMENT02PB = "SET02PB";
    public static final String SETTLEMENT03PB = "SET03PB";
    public static final String SETTLEMENT04PB = "SET04PB";
    public static final String SETTLEMENT00PR = "SET00PR";
    public static final String SETTLEMENT01PR = "SET01PR";
    public static final String SETTLEMENT02PR = "SET02PR";
    public static final String SETTLEMENT03PR = "SET03PR";
    public static final String SETTLEMENT04PR = "SET04PR";
    public static final String SETTLEMENT00PY = "SET00PY";
    public static final String SETTLEMENT01PY = "SET01PY";
    public static final String SETTLEMENT02PY = "SET02PY";
    public static final String SETTLEMENT03PY = "SET03PY";
    public static final String SETTLEMENT04PY = "SET04PY";
    public static final String SETTLEMENT00PG = "SET00PG";
    public static final String SETTLEMENT01PG = "SET01PG";
    public static final String SETTLEMENT02PG = "SET02PG";
    public static final String SETTLEMENT03PG = "SET03PG";
    public static final String SETTLEMENT04PG = "SET04PG";
    public static final String ROAD00PB = "RD00PB";
    public static final String ROAD01PB = "RD01PB";
    public static final String ROAD02PB = "RD02PB";
    public static final String ROAD03PB = "RD03PB";
    public static final String ROAD04PB = "RD04PB";
    public static final String ROAD05PB = "RD05PB";
    public static final String ROAD06PB = "RD06PB";
    public static final String ROAD07PB = "RD07PB";
    public static final String ROAD08PB = "RD08PB";
    public static final String ROAD09PB = "RD09PB";
    public static final String ROAD10PB = "RD10PB";
    public static final String ROAD11PB = "RD11PB";
    public static final String ROAD12PB = "RD12PB";
    public static final String ROAD13PB = "RD13PB";
    public static final String ROAD14PB = "RD14PB";
    public static final String ROAD00PR = "RD00PR";
    public static final String ROAD01PR = "RD01PR";
    public static final String ROAD02PR = "RD02PR";
    public static final String ROAD03PR = "RD03PR";
    public static final String ROAD04PR = "RD04PR";
    public static final String ROAD05PR = "RD05PR";
    public static final String ROAD06PR = "RD06PR";
    public static final String ROAD07PR = "RD07PR";
    public static final String ROAD08PR = "RD08PR";
    public static final String ROAD09PR = "RD09PR";
    public static final String ROAD10PR = "RD10PR";
    public static final String ROAD11PR = "RD11PR";
    public static final String ROAD12PR = "RD12PR";
    public static final String ROAD13PR = "RD13PR";
    public static final String ROAD14PR = "RD14PR";
    public static final String ROAD00PY = "RD00PY";
    public static final String ROAD01PY = "RD01PY";
    public static final String ROAD02PY = "RD02PY";
    public static final String ROAD03PY = "RD03PY";
    public static final String ROAD04PY = "RD04PY";
    public static final String ROAD05PY = "RD05PY";
    public static final String ROAD06PY = "RD06PY";
    public static final String ROAD07PY = "RD07PY";
    public static final String ROAD08PY = "RD08PY";
    public static final String ROAD09PY = "RD09PY";
    public static final String ROAD10PY = "RD10PY";
    public static final String ROAD11PY = "RD11PY";
    public static final String ROAD12PY = "RD12PY";
    public static final String ROAD13PY = "RD13PY";
    public static final String ROAD14PY = "RD14PY";
    public static final String ROAD00PG = "RD00PG";
    public static final String ROAD01PG = "RD01PG";
    public static final String ROAD02PG = "RD02PG";
    public static final String ROAD03PG = "RD03PG";
    public static final String ROAD04PG = "RD04PG";
    public static final String ROAD05PG = "RD05PG";
    public static final String ROAD06PG = "RD06PG";
    public static final String ROAD07PG = "RD07PG";
    public static final String ROAD08PG = "RD08PG";
    public static final String ROAD09PG = "RD09PG";
    public static final String ROAD10PG = "RD10PG";
    public static final String ROAD11PG = "RD11PG";
    public static final String ROAD12PG = "RD12PG";
    public static final String ROAD13PG = "RD13PG";
    public static final String ROAD14PG = "RD14PG";
    public static final String RESOURCECARD00PB = "RC00PB";
    public static final String RESOURCECARD01PB = "RC01PB";
    public static final String RESOURCECARD02PB = "RC02PB";
    public static final String RESOURCECARD03PB = "RC03PB";
    public static final String RESOURCECARD04PB = "RC04PB";
    public static final String RESOURCECARD05PB = "RC05PB";
    public static final String RESOURCECARD06PB = "RC06PB";
    public static final String RESOURCECARD07PB = "RC07PB";
    public static final String RESOURCECARD08PB = "RC08PB";
    public static final String RESOURCECARD09PB = "RC09PB";
    public static final String RESOURCECARD10PB = "RC10PB";
    public static final String RESOURCECARD11PB = "RC11PB";
    public static final String RESOURCECARD12PB = "RC12PB";
    public static final String RESOURCECARD13PB = "RC13PB";
    public static final String RESOURCECARD14PB = "RC14PB";
    public static final String RESOURCECARD15PB = "RC15PB";
    public static final String RESOURCECARD16PB = "RC16PB";
    public static final String RESOURCECARD17PB = "RC17PB";
    public static final String RESOURCECARD18PB = "RC18PB";
    public static final String RESOURCECARD19PB = "RC19PB";
    public static final String RESOURCECARD20PB = "RC20PB";
    public static final String RESOURCECARD21PB = "RC21PB";
    public static final String RESOURCECARD22PB = "RC22PB";
    public static final String RESOURCECARD23PB = "RC23PB";
    public static final String RESOURCECARD24PB = "RC24PB";
    public static final String RESOURCECARD25PB = "RC25PB";
    public static final String RESOURCECARD26PB = "RC26PB";
    public static final String RESOURCECARD27PB = "RC27PB";
    public static final String RESOURCECARD28PB = "RC28PB";
    public static final String RESOURCECARD29PB = "RC29PB";
    public static final String RESOURCECARD00PR = "RC00PR";
    public static final String RESOURCECARD01PR = "RC01PR";
    public static final String RESOURCECARD02PR = "RC02PR";
    public static final String RESOURCECARD03PR = "RC03PR";
    public static final String RESOURCECARD04PR = "RC04PR";
    public static final String RESOURCECARD05PR = "RC05PR";
    public static final String RESOURCECARD06PR = "RC06PR";
    public static final String RESOURCECARD07PR = "RC07PR";
    public static final String RESOURCECARD08PR = "RC08PR";
    public static final String RESOURCECARD09PR = "RC09PR";
    public static final String RESOURCECARD10PR = "RC10PR";
    public static final String RESOURCECARD11PR = "RC11PR";
    public static final String RESOURCECARD12PR = "RC12PR";
    public static final String RESOURCECARD13PR = "RC13PR";
    public static final String RESOURCECARD14PR = "RC14PR";
    public static final String RESOURCECARD15PR = "RC15PR";
    public static final String RESOURCECARD16PR = "RC16PR";
    public static final String RESOURCECARD17PR = "RC17PR";
    public static final String RESOURCECARD18PR = "RC18PR";
    public static final String RESOURCECARD19PR = "RC19PR";
    public static final String RESOURCECARD20PR = "RC20PR";
    public static final String RESOURCECARD21PR = "RC21PR";
    public static final String RESOURCECARD22PR = "RC22PR";
    public static final String RESOURCECARD23PR = "RC23PR";
    public static final String RESOURCECARD24PR = "RC24PR";
    public static final String RESOURCECARD25PR = "RC25PR";
    public static final String RESOURCECARD26PR = "RC26PR";
    public static final String RESOURCECARD27PR = "RC27PR";
    public static final String RESOURCECARD28PR = "RC28PR";
    public static final String RESOURCECARD29PR = "RC29PR";
    public static final String RESOURCECARD00PY = "RC00PY";
    public static final String RESOURCECARD01PY = "RC01PY";
    public static final String RESOURCECARD02PY = "RC02PY";
    public static final String RESOURCECARD03PY = "RC03PY";
    public static final String RESOURCECARD04PY = "RC04PY";
    public static final String RESOURCECARD05PY = "RC05PY";
    public static final String RESOURCECARD06PY = "RC06PY";
    public static final String RESOURCECARD07PY = "RC07PY";
    public static final String RESOURCECARD08PY = "RC08PY";
    public static final String RESOURCECARD09PY = "RC09PY";
    public static final String RESOURCECARD10PY = "RC10PY";
    public static final String RESOURCECARD11PY = "RC11PY";
    public static final String RESOURCECARD12PY = "RC12PY";
    public static final String RESOURCECARD13PY = "RC13PY";
    public static final String RESOURCECARD14PY = "RC14PY";
    public static final String RESOURCECARD15PY = "RC15PY";
    public static final String RESOURCECARD16PY = "RC16PY";
    public static final String RESOURCECARD17PY = "RC17PY";
    public static final String RESOURCECARD18PY = "RC18PY";
    public static final String RESOURCECARD19PY = "RC19PY";
    public static final String RESOURCECARD20PY = "RC20PY";
    public static final String RESOURCECARD21PY = "RC21PY";
    public static final String RESOURCECARD22PY = "RC22PY";
    public static final String RESOURCECARD23PY = "RC23PY";
    public static final String RESOURCECARD24PY = "RC24PY";
    public static final String RESOURCECARD25PY = "RC25PY";
    public static final String RESOURCECARD26PY = "RC26PY";
    public static final String RESOURCECARD27PY = "RC27PY";
    public static final String RESOURCECARD28PY = "RC28PY";
    public static final String RESOURCECARD29PY = "RC29PY";
    public static final String RESOURCECARD00PG = "RC00PG";
    public static final String RESOURCECARD01PG = "RC01PG";
    public static final String RESOURCECARD02PG = "RC02PG";
    public static final String RESOURCECARD03PG = "RC03PG";
    public static final String RESOURCECARD04PG = "RC04PG";
    public static final String RESOURCECARD05PG = "RC05PG";
    public static final String RESOURCECARD06PG = "RC06PG";
    public static final String RESOURCECARD07PG = "RC07PG";
    public static final String RESOURCECARD08PG = "RC08PG";
    public static final String RESOURCECARD09PG = "RC09PG";
    public static final String RESOURCECARD10PG = "RC10PG";
    public static final String RESOURCECARD11PG = "RC11PG";
    public static final String RESOURCECARD12PG = "RC12PG";
    public static final String RESOURCECARD13PG = "RC13PG";
    public static final String RESOURCECARD14PG = "RC14PG";
    public static final String RESOURCECARD15PG = "RC15PG";
    public static final String RESOURCECARD16PG = "RC16PG";
    public static final String RESOURCECARD17PG = "RC17PG";
    public static final String RESOURCECARD18PG = "RC18PG";
    public static final String RESOURCECARD19PG = "RC19PG";
    public static final String RESOURCECARD20PG = "RC20PG";
    public static final String RESOURCECARD21PG = "RC21PG";
    public static final String RESOURCECARD22PG = "RC22PG";
    public static final String RESOURCECARD23PG = "RC23PG";
    public static final String RESOURCECARD24PG = "RC24PG";
    public static final String RESOURCECARD25PG = "RC25PG";
    public static final String RESOURCECARD26PG = "RC26PG";
    public static final String RESOURCECARD27PG = "RC27PG";
    public static final String RESOURCECARD28PG = "RC28PG";
    public static final String RESOURCECARD29PG = "RC29PG";
    public static final String ORE = "ORE";
    public static final String GRAIN = "GRA";
    public static final String LUMBER = "LUM";
    public static final String WOOL = "WOO";
    public static final String BRICK = "BRI";
    public static final String DESERT = "DES";
    public static final String SOLDIERCOUNTPB = "SOLPB";
    public static final String SOLDIERCOUNTPR = "SOLPR";
    public static final String SOLDIERCOUNTPY = "SOLPY";
    public static final String SOLDIERCOUNTPG = "SOLPG";
    public static final String LONGESTROAD = "LR";
    public static final String LARGESTARMY = "LA";
    public static final String pbId = "70";
    public static final String prId = "71";
    public static final String pyId = "72";
    public static final String pgId = "73";
    public static final int hex00Id = 0;
    public static final int hex01Id = 1;
    public static final int hex02Id = 2;
    public static final int hex03Id = 3;
    public static final int hex04Id = 4;
    public static final int hex05Id = 5;
    public static final int hex06Id = 6;
    public static final int hex07Id = 7;
    public static final int hex08Id = 8;
    public static final int hex09Id = 9;
    public static final int hex10Id = 10;
    public static final int hex11Id = 11;
    public static final int hex12Id = 12;
    public static final int hex13Id = 13;
    public static final int hex14Id = 14;
    public static final int hex15Id = 15;
    public static final int hex16Id = 16;
    public static final int hex17Id = 17;
    public static final int hex18Id = 18;
    public static final int node00Id = 0;
    public static final int node01Id = 1;
    public static final int node02Id = 2;
    public static final int node03Id = 3;
    public static final int node04Id = 4;
    public static final int node05Id = 5;
    public static final int node06Id = 6;
    public static final int node07Id = 7;
    public static final int node08Id = 8;
    public static final int node09Id = 9;
    public static final int node10Id = 10;
    public static final int node11Id = 11;
    public static final int node12Id = 12;
    public static final int node13Id = 13;
    public static final int node14Id = 14;
    public static final int node15Id = 15;
    public static final int node16Id = 16;
    public static final int node17Id = 17;
    public static final int node18Id = 18;
    public static final int node19Id = 19;
    public static final int node20Id = 20;
    public static final int node21Id = 21;
    public static final int node22Id = 22;
    public static final int node23Id = 23;
    public static final int node24Id = 24;
    public static final int node25Id = 25;
    public static final int node26Id = 26;
    public static final int node27Id = 27;
    public static final int node28Id = 28;
    public static final int node29Id = 29;
    public static final int node30Id = 30;
    public static final int node31Id = 31;
    public static final int node32Id = 32;
    public static final int node33Id = 33;
    public static final int node34Id = 34;
    public static final int node35Id = 35;
    public static final int node36Id = 36;
    public static final int node37Id = 37;
    public static final int node38Id = 38;
    public static final int node39Id = 39;
    public static final int node40Id = 40;
    public static final int node41Id = 41;
    public static final int node42Id = 42;
    public static final int node43Id = 43;
    public static final int node44Id = 44;
    public static final int node45Id = 45;
    public static final int node46Id = 46;
    public static final int node47Id = 47;
    public static final int node48Id = 48;
    public static final int node49Id = 49;
    public static final int node50Id = 50;
    public static final int node51Id = 51;
    public static final int node52Id = 52;
    public static final int node53Id = 53;
    public static final int path00Id = 0;
    public static final int path01Id = 1;
    public static final int path02Id = 2;
    public static final int path03Id = 3;
    public static final int path04Id = 4;
    public static final int path05Id = 5;
    public static final int path06Id = 6;
    public static final int path07Id = 7;
    public static final int path08Id = 8;
    public static final int path09Id = 9;
    public static final int path10Id = 10;
    public static final int path11Id = 11;
    public static final int path12Id = 12;
    public static final int path13Id = 13;
    public static final int path14Id = 14;
    public static final int path15Id = 15;
    public static final int path16Id = 16;
    public static final int path17Id = 17;
    public static final int path18Id = 18;
    public static final int path19Id = 19;
    public static final int path20Id = 20;
    public static final int path21Id = 21;
    public static final int path22Id = 22;
    public static final int path23Id = 23;
    public static final int path24Id = 24;
    public static final int path25Id = 25;
    public static final int path26Id = 26;
    public static final int path27Id = 27;
    public static final int path28Id = 28;
    public static final int path29Id = 29;
    public static final int path30Id = 30;
    public static final int path31Id = 31;
    public static final int path32Id = 32;
    public static final int path33Id = 33;
    public static final int path34Id = 34;
    public static final int path35Id = 35;
    public static final int path36Id = 36;
    public static final int path37Id = 37;
    public static final int path38Id = 38;
    public static final int path39Id = 39;
    public static final int path40Id = 40;
    public static final int path41Id = 41;
    public static final int path42Id = 42;
    public static final int path43Id = 43;
    public static final int path44Id = 44;
    public static final int path45Id = 45;
    public static final int path46Id = 46;
    public static final int path47Id = 47;
    public static final int path48Id = 48;
    public static final int path49Id = 49;
    public static final int path50Id = 50;
    public static final int path51Id = 51;
    public static final int path52Id = 52;
    public static final int path53Id = 53;
    public static final int path54Id = 54;
    public static final int path55Id = 55;
    public static final int path56Id = 56;
    public static final int path57Id = 57;
    public static final int path58Id = 58;
    public static final int path59Id = 59;
    public static final int path60Id = 60;
    public static final int path61Id = 61;
    public static final int path62Id = 62;
    public static final int path63Id = 63;
    public static final int path64Id = 64;
    public static final int path65Id = 65;
    public static final int path66Id = 66;
    public static final int path67Id = 67;
    public static final int path68Id = 68;
    public static final int path69Id = 69;
    public static final int path70Id = 70;
    public static final int path71Id = 71;
    public static final int harbor00Id = 0;
    public static final int harbor01Id = 1;
    public static final int harbor02Id = 2;
    public static final int harbor03Id = 3;
    public static final int harbor04Id = 4;
    public static final int harbor05Id = 5;
    public static final int harbor06Id = 6;
    public static final int harbor07Id = 7;
    public static final int harbor08Id = 8;
    public static final int developmentCard00Id = 0;
    public static final int developmentCard01Id = 1;
    public static final int developmentCard02Id = 2;
    public static final int developmentCard03Id = 3;
    public static final int developmentCard04Id = 4;
    public static final int developmentCard05Id = 5;
    public static final int developmentCard06Id = 6;
    public static final int developmentCard07Id = 7;
    public static final int developmentCard08Id = 8;
    public static final int developmentCard09Id = 9;
    public static final int developmentCard10Id = 10;
    public static final int developmentCard11Id = 11;
    public static final int developmentCard12Id = 12;
    public static final int developmentCard13Id = 13;
    public static final int developmentCard14Id = 14;
    public static final int developmentCard15Id = 15;
    public static final int developmentCard16Id = 16;
    public static final int developmentCard17Id = 17;
    public static final int developmentCard18Id = 18;
    public static final int developmentCard19Id = 19;
    public static final int developmentCard20Id = 20;
    public static final int developmentCard21Id = 21;
    public static final int developmentCard22Id = 22;
    public static final int developmentCard23Id = 23;
    public static final int developmentCard24Id = 24;
    public static final int city00PBId = 0;
    public static final int city01PBId = 1;
    public static final int city02PBId = 2;
    public static final int city03PBId = 3;
    public static final int city00PRId = 0;
    public static final int city01PRId = 1;
    public static final int city02PRId = 2;
    public static final int city03PRId = 3;
    public static final int city00PYId = 0;
    public static final int city01PYId = 1;
    public static final int city02PYId = 2;
    public static final int city03PYId = 3;
    public static final int city00PGId = 0;
    public static final int city01PGId = 1;
    public static final int city02PGId = 2;
    public static final int city03PGId = 3;
    public static final int settlement00PBId = 0;
    public static final int settlement01PBId = 1;
    public static final int settlement02PBId = 2;
    public static final int settlement03PBId = 3;
    public static final int settlement04PBId = 4;
    public static final int settlement00PRId = 0;
    public static final int settlement01PRId = 1;
    public static final int settlement02PRId = 2;
    public static final int settlement03PRId = 3;
    public static final int settlement04PRId = 4;
    public static final int settlement00PYId = 0;
    public static final int settlement01PYId = 1;
    public static final int settlement02PYId = 2;
    public static final int settlement03PYId = 3;
    public static final int settlement04PYId = 4;
    public static final int settlement00PGId = 0;
    public static final int settlement01PGId = 1;
    public static final int settlement02PGId = 2;
    public static final int settlement03PGId = 3;
    public static final int settlement04PGId = 4;
    public static final int road00PBId = 0;
    public static final int road01PBId = 1;
    public static final int road02PBId = 2;
    public static final int road03PBId = 3;
    public static final int road04PBId = 4;
    public static final int road05PBId = 5;
    public static final int road06PBId = 6;
    public static final int road07PBId = 7;
    public static final int road08PBId = 8;
    public static final int road09PBId = 9;
    public static final int road10PBId = 10;
    public static final int road11PBId = 11;
    public static final int road12PBId = 12;
    public static final int road13PBId = 13;
    public static final int road14PBId = 14;
    public static final int road00PRId = 0;
    public static final int road01PRId = 1;
    public static final int road02PRId = 2;
    public static final int road03PRId = 3;
    public static final int road04PRId = 4;
    public static final int road05PRId = 5;
    public static final int road06PRId = 6;
    public static final int road07PRId = 7;
    public static final int road08PRId = 8;
    public static final int road09PRId = 9;
    public static final int road10PRId = 10;
    public static final int road11PRId = 11;
    public static final int road12PRId = 12;
    public static final int road13PRId = 13;
    public static final int road14PRId = 14;
    public static final int road00PYId = 0;
    public static final int road01PYId = 1;
    public static final int road02PYId = 2;
    public static final int road03PYId = 3;
    public static final int road04PYId = 4;
    public static final int road05PYId = 5;
    public static final int road06PYId = 6;
    public static final int road07PYId = 7;
    public static final int road08PYId = 8;
    public static final int road09PYId = 9;
    public static final int road10PYId = 10;
    public static final int road11PYId = 11;
    public static final int road12PYId = 12;
    public static final int road13PYId = 13;
    public static final int road14PYId = 14;
    public static final int road00PGId = 0;
    public static final int road01PGId = 1;
    public static final int road02PGId = 2;
    public static final int road03PGId = 3;
    public static final int road04PGId = 4;
    public static final int road05PGId = 5;
    public static final int road06PGId = 6;
    public static final int road07PGId = 7;
    public static final int road08PGId = 8;
    public static final int road09PGId = 9;
    public static final int road10PGId = 10;
    public static final int road11PGId = 11;
    public static final int road12PGId = 12;
    public static final int road13PGId = 13;
    public static final int road14PGId = 14;
    public static final int resourceCard00PBId = 0;
    public static final int resourceCard01PBId = 1;
    public static final int resourceCard02PBId = 2;
    public static final int resourceCard03PBId = 3;
    public static final int resourceCard04PBId = 4;
    public static final int resourceCard05PBId = 5;
    public static final int resourceCard06PBId = 6;
    public static final int resourceCard07PBId = 7;
    public static final int resourceCard08PBId = 8;
    public static final int resourceCard09PBId = 9;
    public static final int resourceCard10PBId = 10;
    public static final int resourceCard11PBId = 11;
    public static final int resourceCard12PBId = 12;
    public static final int resourceCard13PBId = 13;
    public static final int resourceCard14PBId = 14;
    public static final int resourceCard15PBId = 15;
    public static final int resourceCard16PBId = 16;
    public static final int resourceCard17PBId = 17;
    public static final int resourceCard18PBId = 18;
    public static final int resourceCard19PBId = 19;
    public static final int resourceCard20PBId = 20;
    public static final int resourceCard21PBId = 21;
    public static final int resourceCard22PBId = 22;
    public static final int resourceCard23PBId = 23;
    public static final int resourceCard24PBId = 24;
    public static final int resourceCard25PBId = 25;
    public static final int resourceCard26PBId = 26;
    public static final int resourceCard27PBId = 27;
    public static final int resourceCard28PBId = 28;
    public static final int resourceCard29PBId = 29;
    public static final int resourceCard00PRId = 0;
    public static final int resourceCard01PRId = 1;
    public static final int resourceCard02PRId = 2;
    public static final int resourceCard03PRId = 3;
    public static final int resourceCard04PRId = 4;
    public static final int resourceCard05PRId = 5;
    public static final int resourceCard06PRId = 6;
    public static final int resourceCard07PRId = 7;
    public static final int resourceCard08PRId = 8;
    public static final int resourceCard09PRId = 9;
    public static final int resourceCard10PRId = 10;
    public static final int resourceCard11PRId = 11;
    public static final int resourceCard12PRId = 12;
    public static final int resourceCard13PRId = 13;
    public static final int resourceCard14PRId = 14;
    public static final int resourceCard15PRId = 15;
    public static final int resourceCard16PRId = 16;
    public static final int resourceCard17PRId = 17;
    public static final int resourceCard18PRId = 18;
    public static final int resourceCard19PRId = 19;
    public static final int resourceCard20PRId = 20;
    public static final int resourceCard21PRId = 21;
    public static final int resourceCard22PRId = 22;
    public static final int resourceCard23PRId = 23;
    public static final int resourceCard24PRId = 24;
    public static final int resourceCard25PRId = 25;
    public static final int resourceCard26PRId = 26;
    public static final int resourceCard27PRId = 27;
    public static final int resourceCard28PRId = 28;
    public static final int resourceCard29PRId = 29;
    public static final int resourceCard00PYId = 0;
    public static final int resourceCard01PYId = 1;
    public static final int resourceCard02PYId = 2;
    public static final int resourceCard03PYId = 3;
    public static final int resourceCard04PYId = 4;
    public static final int resourceCard05PYId = 5;
    public static final int resourceCard06PYId = 6;
    public static final int resourceCard07PYId = 7;
    public static final int resourceCard08PYId = 8;
    public static final int resourceCard09PYId = 9;
    public static final int resourceCard10PYId = 10;
    public static final int resourceCard11PYId = 11;
    public static final int resourceCard12PYId = 12;
    public static final int resourceCard13PYId = 13;
    public static final int resourceCard14PYId = 14;
    public static final int resourceCard15PYId = 15;
    public static final int resourceCard16PYId = 16;
    public static final int resourceCard17PYId = 17;
    public static final int resourceCard18PYId = 18;
    public static final int resourceCard19PYId = 19;
    public static final int resourceCard20PYId = 20;
    public static final int resourceCard21PYId = 21;
    public static final int resourceCard22PYId = 22;
    public static final int resourceCard23PYId = 23;
    public static final int resourceCard24PYId = 24;
    public static final int resourceCard25PYId = 25;
    public static final int resourceCard26PYId = 26;
    public static final int resourceCard27PYId = 27;
    public static final int resourceCard28PYId = 28;
    public static final int resourceCard29PYId = 29;
    public static final int resourceCard00PGId = 0;
    public static final int resourceCard01PGId = 1;
    public static final int resourceCard02PGId = 2;
    public static final int resourceCard03PGId = 3;
    public static final int resourceCard04PGId = 4;
    public static final int resourceCard05PGId = 5;
    public static final int resourceCard06PGId = 6;
    public static final int resourceCard07PGId = 7;
    public static final int resourceCard08PGId = 8;
    public static final int resourceCard09PGId = 9;
    public static final int resourceCard10PGId = 10;
    public static final int resourceCard11PGId = 11;
    public static final int resourceCard12PGId = 12;
    public static final int resourceCard13PGId = 13;
    public static final int resourceCard14PGId = 14;
    public static final int resourceCard15PGId = 15;
    public static final int resourceCard16PGId = 16;
    public static final int resourceCard17PGId = 17;
    public static final int resourceCard18PGId = 18;
    public static final int resourceCard19PGId = 19;
    public static final int resourceCard20PGId = 20;
    public static final int resourceCard21PGId = 21;
    public static final int resourceCard22PGId = 22;
    public static final int resourceCard23PGId = 23;
    public static final int resourceCard24PGId = 24;
    public static final int resourceCard25PGId = 25;
    public static final int resourceCard26PGId = 26;
    public static final int resourceCard27PGId = 27;
    public static final int resourceCard28PGId = 28;
    public static final int resourceCard29PGId = 29;
    public static final ImmutableList<String> visibleToPB = ImmutableList.of(pbId);
    public static final ImmutableList<String> visibleToPR = ImmutableList.of(prId);
    public static final ImmutableList<String> visibleToPY = ImmutableList.of(pyId);
    public static final ImmutableList<String> visibleToPG = ImmutableList.of(pgId);
    public static final ImmutableList<String> visibleToNone = ImmutableList.of();
    public static final ImmutableMap<String, Object> pbInfo =
            ImmutableMap.<String, Object>of(PLAYER_ID, pbId);
    public static final ImmutableMap<String, Object> prInfo =
            ImmutableMap.<String, Object>of(PLAYER_ID, prId);
    public static final ImmutableMap<String, Object> pyInfo =
            ImmutableMap.<String, Object>of(PLAYER_ID, pyId);
    public static final ImmutableMap<String, Object> pgInfo =
            ImmutableMap.<String, Object>of(PLAYER_ID, pgId);
    public static final ImmutableMap<String, Object> hex00Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex00Id);
    public static final ImmutableMap<String, Object> hex01Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex01Id);
    public static final ImmutableMap<String, Object> hex02Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex02Id);
    public static final ImmutableMap<String, Object> hex03Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex03Id);
    public static final ImmutableMap<String, Object> hex04Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex04Id);
    public static final ImmutableMap<String, Object> hex05Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex05Id);
    public static final ImmutableMap<String, Object> hex06Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex06Id);
    public static final ImmutableMap<String, Object> hex07Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex07Id);
    public static final ImmutableMap<String, Object> hex08Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex08Id);
    public static final ImmutableMap<String, Object> hex09Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex09Id);
    public static final ImmutableMap<String, Object> hex10Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex10Id);
    public static final ImmutableMap<String, Object> hex11Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex11Id);
    public static final ImmutableMap<String, Object> hex12Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex12Id);
    public static final ImmutableMap<String, Object> hex13Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex13Id);
    public static final ImmutableMap<String, Object> hex14Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex14Id);
    public static final ImmutableMap<String, Object> hex15Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex15Id);
    public static final ImmutableMap<String, Object> hex16Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex16Id);
    public static final ImmutableMap<String, Object> hex17Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex17Id);
    public static final ImmutableMap<String, Object> hex18Info =
            ImmutableMap.<String, Object>of(HEX_ID, hex18Id);
    public static final ImmutableMap<String, Object> node00Info =
            ImmutableMap.<String, Object>of(NODE_ID, node00Id);
    public static final ImmutableMap<String, Object> node01Info =
            ImmutableMap.<String, Object>of(NODE_ID, node01Id);
    public static final ImmutableMap<String, Object> node02Info =
            ImmutableMap.<String, Object>of(NODE_ID, node02Id);
    public static final ImmutableMap<String, Object> node03Info =
            ImmutableMap.<String, Object>of(NODE_ID, node03Id);
    public static final ImmutableMap<String, Object> node04Info =
            ImmutableMap.<String, Object>of(NODE_ID, node04Id);
    public static final ImmutableMap<String, Object> node05Info =
            ImmutableMap.<String, Object>of(NODE_ID, node05Id);
    public static final ImmutableMap<String, Object> node06Info =
            ImmutableMap.<String, Object>of(NODE_ID, node06Id);
    public static final ImmutableMap<String, Object> node07Info =
            ImmutableMap.<String, Object>of(NODE_ID, node07Id);
    public static final ImmutableMap<String, Object> node08Info =
            ImmutableMap.<String, Object>of(NODE_ID, node08Id);
    public static final ImmutableMap<String, Object> node09Info =
            ImmutableMap.<String, Object>of(NODE_ID, node09Id);
    public static final ImmutableMap<String, Object> node10Info =
            ImmutableMap.<String, Object>of(NODE_ID, node10Id);
    public static final ImmutableMap<String, Object> node11Info =
            ImmutableMap.<String, Object>of(NODE_ID, node11Id);
    public static final ImmutableMap<String, Object> node12Info =
            ImmutableMap.<String, Object>of(NODE_ID, node12Id);
    public static final ImmutableMap<String, Object> node13Info =
            ImmutableMap.<String, Object>of(NODE_ID, node13Id);
    public static final ImmutableMap<String, Object> node14Info =
            ImmutableMap.<String, Object>of(NODE_ID, node14Id);
    public static final ImmutableMap<String, Object> node15Info =
            ImmutableMap.<String, Object>of(NODE_ID, node15Id);
    public static final ImmutableMap<String, Object> node16Info =
            ImmutableMap.<String, Object>of(NODE_ID, node16Id);
    public static final ImmutableMap<String, Object> node17Info =
            ImmutableMap.<String, Object>of(NODE_ID, node17Id);
    public static final ImmutableMap<String, Object> node18Info =
            ImmutableMap.<String, Object>of(NODE_ID, node18Id);
    public static final ImmutableMap<String, Object> node19Info =
            ImmutableMap.<String, Object>of(NODE_ID, node19Id);
    public static final ImmutableMap<String, Object> node20Info =
            ImmutableMap.<String, Object>of(NODE_ID, node20Id);
    public static final ImmutableMap<String, Object> node21Info =
            ImmutableMap.<String, Object>of(NODE_ID, node21Id);
    public static final ImmutableMap<String, Object> node22Info =
            ImmutableMap.<String, Object>of(NODE_ID, node22Id);
    public static final ImmutableMap<String, Object> node23Info =
            ImmutableMap.<String, Object>of(NODE_ID, node23Id);
    public static final ImmutableMap<String, Object> node24Info =
            ImmutableMap.<String, Object>of(NODE_ID, node24Id);
    public static final ImmutableMap<String, Object> node25Info =
            ImmutableMap.<String, Object>of(NODE_ID, node25Id);
    public static final ImmutableMap<String, Object> node26Info =
            ImmutableMap.<String, Object>of(NODE_ID, node26Id);
    public static final ImmutableMap<String, Object> node27Info =
            ImmutableMap.<String, Object>of(NODE_ID, node27Id);
    public static final ImmutableMap<String, Object> node28Info =
            ImmutableMap.<String, Object>of(NODE_ID, node28Id);
    public static final ImmutableMap<String, Object> node29Info =
            ImmutableMap.<String, Object>of(NODE_ID, node29Id);
    public static final ImmutableMap<String, Object> node30Info =
            ImmutableMap.<String, Object>of(NODE_ID, node30Id);
    public static final ImmutableMap<String, Object> node31Info =
            ImmutableMap.<String, Object>of(NODE_ID, node31Id);
    public static final ImmutableMap<String, Object> node32Info =
            ImmutableMap.<String, Object>of(NODE_ID, node32Id);
    public static final ImmutableMap<String, Object> node33Info =
            ImmutableMap.<String, Object>of(NODE_ID, node33Id);
    public static final ImmutableMap<String, Object> node34Info =
            ImmutableMap.<String, Object>of(NODE_ID, node34Id);
    public static final ImmutableMap<String, Object> node35Info =
            ImmutableMap.<String, Object>of(NODE_ID, node35Id);
    public static final ImmutableMap<String, Object> node36Info =
            ImmutableMap.<String, Object>of(NODE_ID, node36Id);
    public static final ImmutableMap<String, Object> node37Info =
            ImmutableMap.<String, Object>of(NODE_ID, node37Id);
    public static final ImmutableMap<String, Object> node38Info =
            ImmutableMap.<String, Object>of(NODE_ID, node38Id);
    public static final ImmutableMap<String, Object> node39Info =
            ImmutableMap.<String, Object>of(NODE_ID, node39Id);
    public static final ImmutableMap<String, Object> node40Info =
            ImmutableMap.<String, Object>of(NODE_ID, node40Id);
    public static final ImmutableMap<String, Object> node41Info =
            ImmutableMap.<String, Object>of(NODE_ID, node41Id);
    public static final ImmutableMap<String, Object> node42Info =
            ImmutableMap.<String, Object>of(NODE_ID, node42Id);
    public static final ImmutableMap<String, Object> node43Info =
            ImmutableMap.<String, Object>of(NODE_ID, node43Id);
    public static final ImmutableMap<String, Object> node44Info =
            ImmutableMap.<String, Object>of(NODE_ID, node44Id);
    public static final ImmutableMap<String, Object> node45Info =
            ImmutableMap.<String, Object>of(NODE_ID, node45Id);
    public static final ImmutableMap<String, Object> node46Info =
            ImmutableMap.<String, Object>of(NODE_ID, node46Id);
    public static final ImmutableMap<String, Object> node47Info =
            ImmutableMap.<String, Object>of(NODE_ID, node47Id);
    public static final ImmutableMap<String, Object> node48Info =
            ImmutableMap.<String, Object>of(NODE_ID, node48Id);
    public static final ImmutableMap<String, Object> node49Info =
            ImmutableMap.<String, Object>of(NODE_ID, node49Id);
    public static final ImmutableMap<String, Object> node50Info =
            ImmutableMap.<String, Object>of(NODE_ID, node50Id);
    public static final ImmutableMap<String, Object> node51Info =
            ImmutableMap.<String, Object>of(NODE_ID, node51Id);
    public static final ImmutableMap<String, Object> node52Info =
            ImmutableMap.<String, Object>of(NODE_ID, node52Id);
    public static final ImmutableMap<String, Object> node53Info =
            ImmutableMap.<String, Object>of(NODE_ID, node53Id);
    public static final ImmutableMap<String, Object> path00Info =
            ImmutableMap.<String, Object>of(PATH_ID, path00Id);
    public static final ImmutableMap<String, Object> path01Info =
            ImmutableMap.<String, Object>of(PATH_ID, path01Id);
    public static final ImmutableMap<String, Object> path02Info =
            ImmutableMap.<String, Object>of(PATH_ID, path02Id);
    public static final ImmutableMap<String, Object> path03Info =
            ImmutableMap.<String, Object>of(PATH_ID, path03Id);
    public static final ImmutableMap<String, Object> path04Info =
            ImmutableMap.<String, Object>of(PATH_ID, path04Id);
    public static final ImmutableMap<String, Object> path05Info =
            ImmutableMap.<String, Object>of(PATH_ID, path05Id);
    public static final ImmutableMap<String, Object> path06Info =
            ImmutableMap.<String, Object>of(PATH_ID, path06Id);
    public static final ImmutableMap<String, Object> path07Info =
            ImmutableMap.<String, Object>of(PATH_ID, path07Id);
    public static final ImmutableMap<String, Object> path08Info =
            ImmutableMap.<String, Object>of(PATH_ID, path08Id);
    public static final ImmutableMap<String, Object> path09Info =
            ImmutableMap.<String, Object>of(PATH_ID, path09Id);
    public static final ImmutableMap<String, Object> path10Info =
            ImmutableMap.<String, Object>of(PATH_ID, path10Id);
    public static final ImmutableMap<String, Object> path11Info =
            ImmutableMap.<String, Object>of(PATH_ID, path11Id);
    public static final ImmutableMap<String, Object> path12Info =
            ImmutableMap.<String, Object>of(PATH_ID, path12Id);
    public static final ImmutableMap<String, Object> path13Info =
            ImmutableMap.<String, Object>of(PATH_ID, path13Id);
    public static final ImmutableMap<String, Object> path14Info =
            ImmutableMap.<String, Object>of(PATH_ID, path14Id);
    public static final ImmutableMap<String, Object> path15Info =
            ImmutableMap.<String, Object>of(PATH_ID, path15Id);
    public static final ImmutableMap<String, Object> path16Info =
            ImmutableMap.<String, Object>of(PATH_ID, path16Id);
    public static final ImmutableMap<String, Object> path17Info =
            ImmutableMap.<String, Object>of(PATH_ID, path17Id);
    public static final ImmutableMap<String, Object> path18Info =
            ImmutableMap.<String, Object>of(PATH_ID, path18Id);
    public static final ImmutableMap<String, Object> path19Info =
            ImmutableMap.<String, Object>of(PATH_ID, path19Id);
    public static final ImmutableMap<String, Object> path20Info =
            ImmutableMap.<String, Object>of(PATH_ID, path20Id);
    public static final ImmutableMap<String, Object> path21Info =
            ImmutableMap.<String, Object>of(PATH_ID, path21Id);
    public static final ImmutableMap<String, Object> path22Info =
            ImmutableMap.<String, Object>of(PATH_ID, path22Id);
    public static final ImmutableMap<String, Object> path23Info =
            ImmutableMap.<String, Object>of(PATH_ID, path23Id);
    public static final ImmutableMap<String, Object> path24Info =
            ImmutableMap.<String, Object>of(PATH_ID, path24Id);
    public static final ImmutableMap<String, Object> path25Info =
            ImmutableMap.<String, Object>of(PATH_ID, path25Id);
    public static final ImmutableMap<String, Object> path26Info =
            ImmutableMap.<String, Object>of(PATH_ID, path26Id);
    public static final ImmutableMap<String, Object> path27Info =
            ImmutableMap.<String, Object>of(PATH_ID, path27Id);
    public static final ImmutableMap<String, Object> path28Info =
            ImmutableMap.<String, Object>of(PATH_ID, path28Id);
    public static final ImmutableMap<String, Object> path29Info =
            ImmutableMap.<String, Object>of(PATH_ID, path29Id);
    public static final ImmutableMap<String, Object> path30Info =
            ImmutableMap.<String, Object>of(PATH_ID, path30Id);
    public static final ImmutableMap<String, Object> path31Info =
            ImmutableMap.<String, Object>of(PATH_ID, path31Id);
    public static final ImmutableMap<String, Object> path32Info =
            ImmutableMap.<String, Object>of(PATH_ID, path32Id);
    public static final ImmutableMap<String, Object> path33Info =
            ImmutableMap.<String, Object>of(PATH_ID, path33Id);
    public static final ImmutableMap<String, Object> path34Info =
            ImmutableMap.<String, Object>of(PATH_ID, path34Id);
    public static final ImmutableMap<String, Object> path35Info =
            ImmutableMap.<String, Object>of(PATH_ID, path35Id);
    public static final ImmutableMap<String, Object> path36Info =
            ImmutableMap.<String, Object>of(PATH_ID, path36Id);
    public static final ImmutableMap<String, Object> path37Info =
            ImmutableMap.<String, Object>of(PATH_ID, path37Id);
    public static final ImmutableMap<String, Object> path38Info =
            ImmutableMap.<String, Object>of(PATH_ID, path38Id);
    public static final ImmutableMap<String, Object> path39Info =
            ImmutableMap.<String, Object>of(PATH_ID, path39Id);
    public static final ImmutableMap<String, Object> path40Info =
            ImmutableMap.<String, Object>of(PATH_ID, path40Id);
    public static final ImmutableMap<String, Object> path41Info =
            ImmutableMap.<String, Object>of(PATH_ID, path41Id);
    public static final ImmutableMap<String, Object> path42Info =
            ImmutableMap.<String, Object>of(PATH_ID, path42Id);
    public static final ImmutableMap<String, Object> path43Info =
            ImmutableMap.<String, Object>of(PATH_ID, path43Id);
    public static final ImmutableMap<String, Object> path44Info =
            ImmutableMap.<String, Object>of(PATH_ID, path44Id);
    public static final ImmutableMap<String, Object> path45Info =
            ImmutableMap.<String, Object>of(PATH_ID, path45Id);
    public static final ImmutableMap<String, Object> path46Info =
            ImmutableMap.<String, Object>of(PATH_ID, path46Id);
    public static final ImmutableMap<String, Object> path47Info =
            ImmutableMap.<String, Object>of(PATH_ID, path47Id);
    public static final ImmutableMap<String, Object> path48Info =
            ImmutableMap.<String, Object>of(PATH_ID, path48Id);
    public static final ImmutableMap<String, Object> path49Info =
            ImmutableMap.<String, Object>of(PATH_ID, path49Id);
    public static final ImmutableMap<String, Object> path50Info =
            ImmutableMap.<String, Object>of(PATH_ID, path50Id);
    public static final ImmutableMap<String, Object> path51Info =
            ImmutableMap.<String, Object>of(PATH_ID, path51Id);
    public static final ImmutableMap<String, Object> path52Info =
            ImmutableMap.<String, Object>of(PATH_ID, path52Id);
    public static final ImmutableMap<String, Object> path53Info =
            ImmutableMap.<String, Object>of(PATH_ID, path53Id);
    public static final ImmutableMap<String, Object> path54Info =
            ImmutableMap.<String, Object>of(PATH_ID, path54Id);
    public static final ImmutableMap<String, Object> path55Info =
            ImmutableMap.<String, Object>of(PATH_ID, path55Id);
    public static final ImmutableMap<String, Object> path56Info =
            ImmutableMap.<String, Object>of(PATH_ID, path56Id);
    public static final ImmutableMap<String, Object> path57Info =
            ImmutableMap.<String, Object>of(PATH_ID, path57Id);
    public static final ImmutableMap<String, Object> path58Info =
            ImmutableMap.<String, Object>of(PATH_ID, path58Id);
    public static final ImmutableMap<String, Object> path59Info =
            ImmutableMap.<String, Object>of(PATH_ID, path59Id);
    public static final ImmutableMap<String, Object> path60Info =
            ImmutableMap.<String, Object>of(PATH_ID, path60Id);
    public static final ImmutableMap<String, Object> path61Info =
            ImmutableMap.<String, Object>of(PATH_ID, path61Id);
    public static final ImmutableMap<String, Object> path62Info =
            ImmutableMap.<String, Object>of(PATH_ID, path62Id);
    public static final ImmutableMap<String, Object> path63Info =
            ImmutableMap.<String, Object>of(PATH_ID, path63Id);
    public static final ImmutableMap<String, Object> path64Info =
            ImmutableMap.<String, Object>of(PATH_ID, path64Id);
    public static final ImmutableMap<String, Object> path65Info =
            ImmutableMap.<String, Object>of(PATH_ID, path65Id);
    public static final ImmutableMap<String, Object> path66Info =
            ImmutableMap.<String, Object>of(PATH_ID, path66Id);
    public static final ImmutableMap<String, Object> path67Info =
            ImmutableMap.<String, Object>of(PATH_ID, path67Id);
    public static final ImmutableMap<String, Object> path68Info =
            ImmutableMap.<String, Object>of(PATH_ID, path68Id);
    public static final ImmutableMap<String, Object> path69Info =
            ImmutableMap.<String, Object>of(PATH_ID, path69Id);
    public static final ImmutableMap<String, Object> path70Info =
            ImmutableMap.<String, Object>of(PATH_ID, path70Id);
    public static final ImmutableMap<String, Object> path71Info =
            ImmutableMap.<String, Object>of(PATH_ID, path71Id);
    public static final ImmutableMap<String, Object> harbor00Info =
            ImmutableMap.<String, Object>of(HARBOR_ID, harbor00Id);
    public static final ImmutableMap<String, Object> harbor01Info =
            ImmutableMap.<String, Object>of(HARBOR_ID, harbor01Id);
    public static final ImmutableMap<String, Object> harbor02Info =
            ImmutableMap.<String, Object>of(HARBOR_ID, harbor02Id);
    public static final ImmutableMap<String, Object> harbor03Info =
            ImmutableMap.<String, Object>of(HARBOR_ID, harbor03Id);
    public static final ImmutableMap<String, Object> harbor04Info =
            ImmutableMap.<String, Object>of(HARBOR_ID, harbor04Id);
    public static final ImmutableMap<String, Object> harbor05Info =
            ImmutableMap.<String, Object>of(HARBOR_ID, harbor05Id);
    public static final ImmutableMap<String, Object> harbor06Info =
            ImmutableMap.<String, Object>of(HARBOR_ID, harbor06Id);
    public static final ImmutableMap<String, Object> harbor07Info =
            ImmutableMap.<String, Object>of(HARBOR_ID, harbor07Id);
    public static final ImmutableMap<String, Object> harbor08Info =
            ImmutableMap.<String, Object>of(HARBOR_ID, harbor08Id);
    public static final ImmutableMap<String, Object> developmentCard00Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard00Id);
    public static final ImmutableMap<String, Object> developmentCard01Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard01Id);
    public static final ImmutableMap<String, Object> developmentCard02Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard02Id);
    public static final ImmutableMap<String, Object> developmentCard03Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard03Id);
    public static final ImmutableMap<String, Object> developmentCard04Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard04Id);
    public static final ImmutableMap<String, Object> developmentCard05Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard05Id);
    public static final ImmutableMap<String, Object> developmentCard06Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard06Id);
    public static final ImmutableMap<String, Object> developmentCard07Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard07Id);
    public static final ImmutableMap<String, Object> developmentCard08Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard08Id);
    public static final ImmutableMap<String, Object> developmentCard09Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard09Id);
    public static final ImmutableMap<String, Object> developmentCard10Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard10Id);
    public static final ImmutableMap<String, Object> developmentCard11Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard11Id);
    public static final ImmutableMap<String, Object> developmentCard12Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard12Id);
    public static final ImmutableMap<String, Object> developmentCard13Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard13Id);
    public static final ImmutableMap<String, Object> developmentCard14Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard14Id);
    public static final ImmutableMap<String, Object> developmentCard15Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard15Id);
    public static final ImmutableMap<String, Object> developmentCard16Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard16Id);
    public static final ImmutableMap<String, Object> developmentCard17Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard17Id);
    public static final ImmutableMap<String, Object> developmentCard18Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard18Id);
    public static final ImmutableMap<String, Object> developmentCard19Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard19Id);
    public static final ImmutableMap<String, Object> developmentCard20Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard20Id);
    public static final ImmutableMap<String, Object> developmentCard21Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard21Id);
    public static final ImmutableMap<String, Object> developmentCard22Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard22Id);
    public static final ImmutableMap<String, Object> developmentCard23Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard23Id);
    public static final ImmutableMap<String, Object> developmentCard24Info =
            ImmutableMap.<String, Object>of(DEVELOPMENTCARD_ID, developmentCard24Id);
    public static final ImmutableMap<String, Object> city00PBInfo =
            ImmutableMap.<String, Object>of(CITY_PB_ID, city00PBId);
    public static final ImmutableMap<String, Object> city01PBInfo =
            ImmutableMap.<String, Object>of(CITY_PB_ID, city01PBId);
    public static final ImmutableMap<String, Object> city02PBInfo =
            ImmutableMap.<String, Object>of(CITY_PB_ID, city02PBId);
    public static final ImmutableMap<String, Object> city03PBInfo =
            ImmutableMap.<String, Object>of(CITY_PB_ID, city03PBId);
    public static final ImmutableMap<String, Object> city00PRInfo =
            ImmutableMap.<String, Object>of(CITY_PR_ID, city00PRId);
    public static final ImmutableMap<String, Object> city01PRInfo =
            ImmutableMap.<String, Object>of(CITY_PR_ID, city01PRId);
    public static final ImmutableMap<String, Object> city02PRInfo =
            ImmutableMap.<String, Object>of(CITY_PR_ID, city02PRId);
    public static final ImmutableMap<String, Object> city03PRInfo =
            ImmutableMap.<String, Object>of(CITY_PR_ID, city03PRId);
    public static final ImmutableMap<String, Object> city00PYInfo =
            ImmutableMap.<String, Object>of(CITY_PY_ID, city00PYId);
    public static final ImmutableMap<String, Object> city01PYInfo =
            ImmutableMap.<String, Object>of(CITY_PY_ID, city01PYId);
    public static final ImmutableMap<String, Object> city02PYInfo =
            ImmutableMap.<String, Object>of(CITY_PY_ID, city02PYId);
    public static final ImmutableMap<String, Object> city03PYInfo =
            ImmutableMap.<String, Object>of(CITY_PY_ID, city03PYId);
    public static final ImmutableMap<String, Object> city00PGInfo =
            ImmutableMap.<String, Object>of(CITY_PG_ID, city00PGId);
    public static final ImmutableMap<String, Object> city01PGInfo =
            ImmutableMap.<String, Object>of(CITY_PG_ID, city01PGId);
    public static final ImmutableMap<String, Object> city02PGInfo =
            ImmutableMap.<String, Object>of(CITY_PG_ID, city02PGId);
    public static final ImmutableMap<String, Object> city03PGInfo =
            ImmutableMap.<String, Object>of(CITY_PG_ID, city03PGId);
    public static final ImmutableMap<String, Object> settlement00PBInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PB_ID, settlement00PBId);
    public static final ImmutableMap<String, Object> settlement01PBInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PB_ID, settlement01PBId);
    public static final ImmutableMap<String, Object> settlement02PBInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PB_ID, settlement02PBId);
    public static final ImmutableMap<String, Object> settlement03PBInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PB_ID, settlement03PBId);
    public static final ImmutableMap<String, Object> settlement04PBInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PB_ID, settlement04PBId);
    public static final ImmutableMap<String, Object> settlement00PRInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PR_ID, settlement00PRId);
    public static final ImmutableMap<String, Object> settlement01PRInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PR_ID, settlement01PRId);
    public static final ImmutableMap<String, Object> settlement02PRInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PR_ID, settlement02PRId);
    public static final ImmutableMap<String, Object> settlement03PRInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PR_ID, settlement03PRId);
    public static final ImmutableMap<String, Object> settlement04PRInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PR_ID, settlement04PRId);
    public static final ImmutableMap<String, Object> settlement00PYInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PY_ID, settlement00PYId);
    public static final ImmutableMap<String, Object> settlement01PYInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PY_ID, settlement01PYId);
    public static final ImmutableMap<String, Object> settlement02PYInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PY_ID, settlement02PYId);
    public static final ImmutableMap<String, Object> settlement03PYInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PY_ID, settlement03PYId);
    public static final ImmutableMap<String, Object> settlement04PYInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PY_ID, settlement04PYId);
    public static final ImmutableMap<String, Object> settlement00PGInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PG_ID, settlement00PGId);
    public static final ImmutableMap<String, Object> settlement01PGInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PG_ID, settlement01PGId);
    public static final ImmutableMap<String, Object> settlement02PGInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PG_ID, settlement02PGId);
    public static final ImmutableMap<String, Object> settlement03PGInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PG_ID, settlement03PGId);
    public static final ImmutableMap<String, Object> settlement04PGInfo =
            ImmutableMap.<String, Object>of(SETTLEMENT_PG_ID, settlement04PGId);
    public static final ImmutableMap<String, Object> road00PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road00PBId);
    public static final ImmutableMap<String, Object> road01PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road01PBId);
    public static final ImmutableMap<String, Object> road02PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road02PBId);
    public static final ImmutableMap<String, Object> road03PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road03PBId);
    public static final ImmutableMap<String, Object> road04PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road04PBId);
    public static final ImmutableMap<String, Object> road05PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road05PBId);
    public static final ImmutableMap<String, Object> road06PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road06PBId);
    public static final ImmutableMap<String, Object> road07PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road07PBId);
    public static final ImmutableMap<String, Object> road08PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road08PBId);
    public static final ImmutableMap<String, Object> road09PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road09PBId);
    public static final ImmutableMap<String, Object> road10PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road10PBId);
    public static final ImmutableMap<String, Object> road11PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road11PBId);
    public static final ImmutableMap<String, Object> road12PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road12PBId);
    public static final ImmutableMap<String, Object> road13PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road13PBId);
    public static final ImmutableMap<String, Object> road14PBInfo =
            ImmutableMap.<String, Object>of(ROAD_PB_ID, road14PBId);
    public static final ImmutableMap<String, Object> road00PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road00PRId);
    public static final ImmutableMap<String, Object> road01PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road01PRId);
    public static final ImmutableMap<String, Object> road02PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road02PRId);
    public static final ImmutableMap<String, Object> road03PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road03PRId);
    public static final ImmutableMap<String, Object> road04PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road04PRId);
    public static final ImmutableMap<String, Object> road05PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road05PRId);
    public static final ImmutableMap<String, Object> road06PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road06PRId);
    public static final ImmutableMap<String, Object> road07PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road07PRId);
    public static final ImmutableMap<String, Object> road08PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road08PRId);
    public static final ImmutableMap<String, Object> road09PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road09PRId);
    public static final ImmutableMap<String, Object> road10PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road10PRId);
    public static final ImmutableMap<String, Object> road11PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road11PRId);
    public static final ImmutableMap<String, Object> road12PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road12PRId);
    public static final ImmutableMap<String, Object> road13PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road13PRId);
    public static final ImmutableMap<String, Object> road14PRInfo =
            ImmutableMap.<String, Object>of(ROAD_PR_ID, road14PRId);
    public static final ImmutableMap<String, Object> road00PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road00PYId);
    public static final ImmutableMap<String, Object> road01PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road01PYId);
    public static final ImmutableMap<String, Object> road02PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road02PYId);
    public static final ImmutableMap<String, Object> road03PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road03PYId);
    public static final ImmutableMap<String, Object> road04PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road04PYId);
    public static final ImmutableMap<String, Object> road05PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road05PYId);
    public static final ImmutableMap<String, Object> road06PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road06PYId);
    public static final ImmutableMap<String, Object> road07PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road07PYId);
    public static final ImmutableMap<String, Object> road08PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road08PYId);
    public static final ImmutableMap<String, Object> road09PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road09PYId);
    public static final ImmutableMap<String, Object> road10PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road10PYId);
    public static final ImmutableMap<String, Object> road11PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road11PYId);
    public static final ImmutableMap<String, Object> road12PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road12PYId);
    public static final ImmutableMap<String, Object> road13PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road13PYId);
    public static final ImmutableMap<String, Object> road14PYInfo =
            ImmutableMap.<String, Object>of(ROAD_PY_ID, road14PYId);
    public static final ImmutableMap<String, Object> road00PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road00PGId);
    public static final ImmutableMap<String, Object> road01PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road01PGId);
    public static final ImmutableMap<String, Object> road02PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road02PGId);
    public static final ImmutableMap<String, Object> road03PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road03PGId);
    public static final ImmutableMap<String, Object> road04PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road04PGId);
    public static final ImmutableMap<String, Object> road05PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road05PGId);
    public static final ImmutableMap<String, Object> road06PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road06PGId);
    public static final ImmutableMap<String, Object> road07PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road07PGId);
    public static final ImmutableMap<String, Object> road08PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road08PGId);
    public static final ImmutableMap<String, Object> road09PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road09PGId);
    public static final ImmutableMap<String, Object> road10PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road10PGId);
    public static final ImmutableMap<String, Object> road11PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road11PGId);
    public static final ImmutableMap<String, Object> road12PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road12PGId);
    public static final ImmutableMap<String, Object> road13PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road13PGId);
    public static final ImmutableMap<String, Object> road14PGInfo =
            ImmutableMap.<String, Object>of(ROAD_PG_ID, road14PGId);
    public static final ImmutableMap<String, Object> resourceCard00PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard00PBId);
    public static final ImmutableMap<String, Object> resourceCard01PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard01PBId);
    public static final ImmutableMap<String, Object> resourceCard02PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard02PBId);
    public static final ImmutableMap<String, Object> resourceCard03PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard03PBId);
    public static final ImmutableMap<String, Object> resourceCard04PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard04PBId);
    public static final ImmutableMap<String, Object> resourceCard05PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard05PBId);
    public static final ImmutableMap<String, Object> resourceCard06PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard06PBId);
    public static final ImmutableMap<String, Object> resourceCard07PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard07PBId);
    public static final ImmutableMap<String, Object> resourceCard08PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard08PBId);
    public static final ImmutableMap<String, Object> resourceCard09PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard09PBId);
    public static final ImmutableMap<String, Object> resourceCard10PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard10PBId);
    public static final ImmutableMap<String, Object> resourceCard11PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard11PBId);
    public static final ImmutableMap<String, Object> resourceCard12PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard12PBId);
    public static final ImmutableMap<String, Object> resourceCard13PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard13PBId);
    public static final ImmutableMap<String, Object> resourceCard14PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard14PBId);
    public static final ImmutableMap<String, Object> resourceCard15PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard15PBId);
    public static final ImmutableMap<String, Object> resourceCard16PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard16PBId);
    public static final ImmutableMap<String, Object> resourceCard17PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard17PBId);
    public static final ImmutableMap<String, Object> resourceCard18PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard18PBId);
    public static final ImmutableMap<String, Object> resourceCard19PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard19PBId);
    public static final ImmutableMap<String, Object> resourceCard20PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard20PBId);
    public static final ImmutableMap<String, Object> resourceCard21PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard21PBId);
    public static final ImmutableMap<String, Object> resourceCard22PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard22PBId);
    public static final ImmutableMap<String, Object> resourceCard23PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard23PBId);
    public static final ImmutableMap<String, Object> resourceCard24PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard24PBId);
    public static final ImmutableMap<String, Object> resourceCard25PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard25PBId);
    public static final ImmutableMap<String, Object> resourceCard26PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard26PBId);
    public static final ImmutableMap<String, Object> resourceCard27PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard27PBId);
    public static final ImmutableMap<String, Object> resourceCard28PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard28PBId);
    public static final ImmutableMap<String, Object> resourceCard29PBInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PB_ID, resourceCard29PBId);
    public static final ImmutableMap<String, Object> resourceCard00PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard00PRId);
    public static final ImmutableMap<String, Object> resourceCard01PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard01PRId);
    public static final ImmutableMap<String, Object> resourceCard02PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard02PRId);
    public static final ImmutableMap<String, Object> resourceCard03PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard03PRId);
    public static final ImmutableMap<String, Object> resourceCard04PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard04PRId);
    public static final ImmutableMap<String, Object> resourceCard05PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard05PRId);
    public static final ImmutableMap<String, Object> resourceCard06PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard06PRId);
    public static final ImmutableMap<String, Object> resourceCard07PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard07PRId);
    public static final ImmutableMap<String, Object> resourceCard08PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard08PRId);
    public static final ImmutableMap<String, Object> resourceCard09PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard09PRId);
    public static final ImmutableMap<String, Object> resourceCard10PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard10PRId);
    public static final ImmutableMap<String, Object> resourceCard11PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard11PRId);
    public static final ImmutableMap<String, Object> resourceCard12PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard12PRId);
    public static final ImmutableMap<String, Object> resourceCard13PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard13PRId);
    public static final ImmutableMap<String, Object> resourceCard14PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard14PRId);
    public static final ImmutableMap<String, Object> resourceCard15PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard15PRId);
    public static final ImmutableMap<String, Object> resourceCard16PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard16PRId);
    public static final ImmutableMap<String, Object> resourceCard17PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard17PRId);
    public static final ImmutableMap<String, Object> resourceCard18PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard18PRId);
    public static final ImmutableMap<String, Object> resourceCard19PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard19PRId);
    public static final ImmutableMap<String, Object> resourceCard20PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard20PRId);
    public static final ImmutableMap<String, Object> resourceCard21PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard21PRId);
    public static final ImmutableMap<String, Object> resourceCard22PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard22PRId);
    public static final ImmutableMap<String, Object> resourceCard23PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard23PRId);
    public static final ImmutableMap<String, Object> resourceCard24PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard24PRId);
    public static final ImmutableMap<String, Object> resourceCard25PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard25PRId);
    public static final ImmutableMap<String, Object> resourceCard26PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard26PRId);
    public static final ImmutableMap<String, Object> resourceCard27PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard27PRId);
    public static final ImmutableMap<String, Object> resourceCard28PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard28PRId);
    public static final ImmutableMap<String, Object> resourceCard29PRInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PR_ID, resourceCard29PRId);
    public static final ImmutableMap<String, Object> resourceCard00PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard00PYId);
    public static final ImmutableMap<String, Object> resourceCard01PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard01PYId);
    public static final ImmutableMap<String, Object> resourceCard02PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard02PYId);
    public static final ImmutableMap<String, Object> resourceCard03PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard03PYId);
    public static final ImmutableMap<String, Object> resourceCard04PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard04PYId);
    public static final ImmutableMap<String, Object> resourceCard05PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard05PYId);
    public static final ImmutableMap<String, Object> resourceCard06PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard06PYId);
    public static final ImmutableMap<String, Object> resourceCard07PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard07PYId);
    public static final ImmutableMap<String, Object> resourceCard08PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard08PYId);
    public static final ImmutableMap<String, Object> resourceCard09PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard09PYId);
    public static final ImmutableMap<String, Object> resourceCard10PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard10PYId);
    public static final ImmutableMap<String, Object> resourceCard11PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard11PYId);
    public static final ImmutableMap<String, Object> resourceCard12PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard12PYId);
    public static final ImmutableMap<String, Object> resourceCard13PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard13PYId);
    public static final ImmutableMap<String, Object> resourceCard14PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard14PYId);
    public static final ImmutableMap<String, Object> resourceCard15PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard15PYId);
    public static final ImmutableMap<String, Object> resourceCard16PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard16PYId);
    public static final ImmutableMap<String, Object> resourceCard17PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard17PYId);
    public static final ImmutableMap<String, Object> resourceCard18PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard18PYId);
    public static final ImmutableMap<String, Object> resourceCard19PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard19PYId);
    public static final ImmutableMap<String, Object> resourceCard20PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard20PYId);
    public static final ImmutableMap<String, Object> resourceCard21PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard21PYId);
    public static final ImmutableMap<String, Object> resourceCard22PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard22PYId);
    public static final ImmutableMap<String, Object> resourceCard23PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard23PYId);
    public static final ImmutableMap<String, Object> resourceCard24PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard24PYId);
    public static final ImmutableMap<String, Object> resourceCard25PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard25PYId);
    public static final ImmutableMap<String, Object> resourceCard26PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard26PYId);
    public static final ImmutableMap<String, Object> resourceCard27PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard27PYId);
    public static final ImmutableMap<String, Object> resourceCard28PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard28PYId);
    public static final ImmutableMap<String, Object> resourceCard29PYInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PY_ID, resourceCard29PYId);
    public static final ImmutableMap<String, Object> resourceCard00PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard00PGId);
    public static final ImmutableMap<String, Object> resourceCard01PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard01PGId);
    public static final ImmutableMap<String, Object> resourceCard02PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard02PGId);
    public static final ImmutableMap<String, Object> resourceCard03PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard03PGId);
    public static final ImmutableMap<String, Object> resourceCard04PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard04PGId);
    public static final ImmutableMap<String, Object> resourceCard05PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard05PGId);
    public static final ImmutableMap<String, Object> resourceCard06PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard06PGId);
    public static final ImmutableMap<String, Object> resourceCard07PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard07PGId);
    public static final ImmutableMap<String, Object> resourceCard08PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard08PGId);
    public static final ImmutableMap<String, Object> resourceCard09PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard09PGId);
    public static final ImmutableMap<String, Object> resourceCard10PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard10PGId);
    public static final ImmutableMap<String, Object> resourceCard11PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard11PGId);
    public static final ImmutableMap<String, Object> resourceCard12PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard12PGId);
    public static final ImmutableMap<String, Object> resourceCard13PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard13PGId);
    public static final ImmutableMap<String, Object> resourceCard14PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard14PGId);
    public static final ImmutableMap<String, Object> resourceCard15PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard15PGId);
    public static final ImmutableMap<String, Object> resourceCard16PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard16PGId);
    public static final ImmutableMap<String, Object> resourceCard17PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard17PGId);
    public static final ImmutableMap<String, Object> resourceCard18PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard18PGId);
    public static final ImmutableMap<String, Object> resourceCard19PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard19PGId);
    public static final ImmutableMap<String, Object> resourceCard20PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard20PGId);
    public static final ImmutableMap<String, Object> resourceCard21PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard21PGId);
    public static final ImmutableMap<String, Object> resourceCard22PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard22PGId);
    public static final ImmutableMap<String, Object> resourceCard23PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard23PGId);
    public static final ImmutableMap<String, Object> resourceCard24PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard24PGId);
    public static final ImmutableMap<String, Object> resourceCard25PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard25PGId);
    public static final ImmutableMap<String, Object> resourceCard26PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard26PGId);
    public static final ImmutableMap<String, Object> resourceCard27PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard27PGId);
    public static final ImmutableMap<String, Object> resourceCard28PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard28PGId);
    public static final ImmutableMap<String, Object> resourceCard29PGInfo =
            ImmutableMap.<String, Object>of(RESOURCECARD_PG_ID, resourceCard29PGId);
    public static final ImmutableList<Map<String, Object>> playersInfo =
            ImmutableList.<Map<String, Object>>of(pbInfo, prInfo, pyInfo, pgInfo);
    public static final ImmutableList<Map<String, Object>> harborInfo =
            ImmutableList.<Map<String, Object>>of(
                    harbor00Info, harbor01Info, harbor02Info, harbor03Info, harbor04Info,
                    harbor05Info, harbor06Info, harbor07Info, harbor08Info);
    public static final ImmutableList<Map<String, Object>> cityPBInfo =
            ImmutableList.<Map<String, Object>>of(
                    city00PBInfo, city01PBInfo, city02PBInfo, city03PBInfo);
    public static final ImmutableList<Map<String, Object>> cityPRInfo =
            ImmutableList.<Map<String, Object>>of(
                    city00PRInfo, city01PRInfo, city02PRInfo, city03PRInfo);
    public static final ImmutableList<Map<String, Object>> cityPYInfo =
            ImmutableList.<Map<String, Object>>of(
                    city00PYInfo, city01PYInfo, city02PYInfo, city03PYInfo);
    public static final ImmutableList<Map<String, Object>> cityPGInfo =
            ImmutableList.<Map<String, Object>>of(
                    city00PGInfo, city01PGInfo, city02PGInfo, city03PGInfo);
    public static final ImmutableList<Map<String, Object>> settlementPBInfo =
            ImmutableList.<Map<String, Object>>of(
                    settlement00PBInfo, settlement01PBInfo, settlement02PBInfo,
                    settlement03PBInfo, settlement04PBInfo);
    public static final ImmutableList<Map<String, Object>> settlementPRInfo =
            ImmutableList.<Map<String, Object>>of(
                    settlement00PRInfo, settlement01PRInfo, settlement02PRInfo,
                    settlement03PRInfo, settlement04PRInfo);
    public static final ImmutableList<Map<String, Object>> settlementPYInfo =
            ImmutableList.<Map<String, Object>>of(
                    settlement00PYInfo, settlement01PYInfo, settlement02PYInfo,
                    settlement03PYInfo, settlement04PYInfo);
    public static final ImmutableList<Map<String, Object>> settlementPGInfo =
            ImmutableList.<Map<String, Object>>of(
                    settlement00PGInfo, settlement01PGInfo, settlement02PGInfo,
                    settlement03PGInfo, settlement04PGInfo);
    
    
    
    
    
    
    
    /* CONSTANTS FOR GRAPHICS */
    
    public static final int[] HEX_XY = {
        200, 100, // 00
        350, 100, // 01
        500, 100, // 02
        125, 211, // 03
        275, 211, // 04
        425, 211, // 05
        575, 211, // 06
         50, 322, // 07
        200, 322, // 08
        350, 322, // 09
        500, 322, // 10
        650, 322, // 11
        125, 433, // 12
        275, 433, // 13
        425, 433, // 14
        575, 433, // 15
        200, 544, // 16
        350, 544, // 17
        500, 544  // 18
    };
    
    public static final int[] ROBBER_XY = {
        255, 155, // 00
        405, 155, // 01
        555, 155, // 02
        180, 266, // 03
        330, 266, // 04
        480, 266, // 05
        630, 266, // 06
        105, 377, // 07
        255, 377, // 08
        405, 377, // 09
        555, 377, // 10
        705, 377, // 11
        180, 488, // 12
        330, 488, // 13
        480, 488, // 14
        630, 488, // 15
        255, 599, // 16
        405, 599, // 17
        555, 599  // 18
    };
    
    public static final int[] HARBOR_LINES_XY = {
        190,  70, // 00
        450,  70, // 01
        675, 181, // 02
        820, 360, // 03
        675, 569, // 04
        450, 680, // 05
        190, 680, // 06
         70, 475, // 07
         70, 255  // 08
    };

    public static final int[] NODE_XY = {
        255,  80, // 00
        405,  80, // 01
        555,  80, // 02
        180, 119, // 03
        330, 119, // 04
        480, 119, // 05
        630, 119, // 06
        180, 191, // 07
        330, 191, // 08
        480, 191, // 09
        630, 191, // 10
        105, 230, // 11
        255, 230, // 12
        405, 230, // 13
        555, 230, // 14
        705, 230, // 15
        105, 302, // 16
        255, 302, // 17
        405, 302, // 18
        555, 302, // 19
        705, 302, // 20
        30,  341, // 21
        180, 341, // 22
        330, 341, // 23
        480, 341, // 24
        630, 341, // 25
        780, 341, // 26
        30,  413, // 27
        180, 413, // 28
        330, 413, // 29
        480, 413, // 30
        630, 413, // 31
        780, 413, // 32
        105, 452, // 33
        255, 452, // 34
        405, 452, // 35
        555, 452, // 36
        705, 452, // 37
        105, 524, // 38
        255, 524, // 39
        405, 524, // 40
        555, 524, // 41
        705, 524, // 42
        180, 563, // 43
        330, 563, // 44
        480, 563, // 45
        630, 563, // 46
        180, 635, // 47
        330, 635, // 48
        480, 635, // 49
        630, 635, // 50
        255, 674, // 51
        405, 674, // 52
        555, 674  // 53
};
    
    public static final int[] HARBOR_XY = {
        170,  35, // 00
        475,  35, // 01
        700, 146, // 02
        855, 370, // 03
        700, 604, // 04
        475, 715, // 05
        175, 715, // 06
         20, 490, // 07
         20, 270  // 08
    };
    
    public static final int[] HARBOR_NODE = {
         0, // 00
         1, // 01
        10, // 02
        26, // 03
        42, // 04
        49, // 05
        47, // 06
        33, // 07
        11  // 08
    };
    
    public static final int[] PATH_XY = {
        217, 104, // 00
        292, 104, // 01
        367, 104, // 02
        442, 104, // 03
        517, 104, // 04
        592, 104, // 05
        194, 160, // 06
        344, 160, // 07
        494, 160, // 08
        644, 160, // 09
        142, 215, // 10
        217, 215, // 11
        292, 215, // 12
        367, 215, // 13
        442, 215, // 14
        517, 215, // 15
        592, 215, // 16
        667, 215, // 17
        119, 271, // 18
        269, 271, // 19
        419, 271, // 20
        569, 271, // 21
        719, 271, // 22
         67, 326, // 23
        142, 326, // 24
        217, 326, // 25
        292, 326, // 26
        367, 326, // 27
        442, 326, // 28
        517, 326, // 29
        592, 326, // 30
        667, 326, // 31
        742, 326, // 32
         42, 382, // 33
        194, 382, // 34
        344, 382, // 35
        494, 382, // 36
        644, 382, // 37
        794, 382, // 38
         67, 437, // 39
        142, 437, // 40
        217, 437, // 41
        292, 437, // 42
        367, 437, // 43
        442, 437, // 44
        517, 437, // 45
        592, 437, // 46
        667, 437, // 47
        742, 437, // 48
        119, 493, // 49
        269, 493, // 50
        419, 493, // 51
        569, 493, // 52
        719, 493, // 53
        142, 548, // 54
        217, 548, // 55
        292, 548, // 56
        367, 548, // 57
        442, 548, // 58
        517, 548, // 59
        592, 548, // 60
        667, 548, // 61
        194, 604, // 62
        344, 604, // 63
        494, 604, // 64
        644, 604, // 65
        217, 659, // 66
        292, 659, // 67
        367, 659, // 68
        442, 659, // 69
        517, 659, // 70
        592, 659  // 71
    };

}
