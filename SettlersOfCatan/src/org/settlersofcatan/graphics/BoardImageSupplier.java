// Copyright 2012 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// //////////////////////////////////////////////////////////////////////////////
package org.settlersofcatan.graphics;

import org.settlersofcatan.client.Constants;

import com.google.gwt.resources.client.ImageResource;

/**
 * A mapping from Card to its ImageResource.
 * The images are all of size 73x97 (width x height).
 */
public class BoardImageSupplier {
    private final BoardImages boardImages;

    public BoardImageSupplier(BoardImages boardImages) {
        this.boardImages = boardImages;
    }

    public ImageResource getResourceBoardToken(String boardTokenKind) {
        switch (boardTokenKind) {
            case Constants.BRICK:
                return boardImages.brickToken();
            case Constants.DESERT:
                return boardImages.desertToken();
            case Constants.GRAIN:
                return boardImages.grainToken();
            case Constants.LUMBER:
                return boardImages.lumberToken();
            case Constants.ORE:
                return boardImages.oreToken();
            case Constants.WOOL:
                return boardImages.woolToken();
            default:
                throw new RuntimeException("Forgot kind=" + boardTokenKind);
        }
    }
  
    public ImageResource getDieRollToken(int dieRollTokenKind)
    {
        switch (dieRollTokenKind) {
            case 0:
                return boardImages.robber();
            case 2:
                return boardImages.twoToken();
            case 3:
                return boardImages.threeToken();
            case 4:
                return boardImages.fourToken();
            case 5:
                return boardImages.fiveToken();
            case 6:
                return boardImages.sixToken();
            case 8:
                return boardImages.eightToken();
            case 9:
                return boardImages.nineToken();
            case 10:
                return boardImages.tenToken();
            case 11:
                return boardImages.elevenToken();
            case 12:
                return boardImages.twelveToken();
            default:
                throw new RuntimeException("Forgot kind=" + dieRollTokenKind);
        }
    }
  
    public ImageResource getNodeToken(int color, int type)
    {
        switch (color) {
            case -1:
                return boardImages.emptyNode();
            case 0:
                if(type == 2)
                    return boardImages.blueCity();
                else
                    return boardImages.blueSettlement();
            case 1:
                if(type == 2)
                    return boardImages.redCity();
                else
                    return boardImages.redSettlement();
            case 2:
                if(type == 2)
                    return boardImages.yellowCity();
                else
                    return boardImages.yellowSettlement();
            case 3:
                if(type == 2)
                    return boardImages.greenCity();
                else
                    return boardImages.greenSettlement();
            default:
                throw new RuntimeException("Forgot kind=" + color);
        }
    }
  
    public ImageResource getNodeTokenSolo(int color, int type)
    {
        switch (color) {
            case -1:
                return boardImages.emptyNode();
            case 0:
                if(type == 2)
                    return boardImages.blueCitySolo();
                else
                    return boardImages.blueSettlementSolo();
            case 1:
                if(type == 2)
                    return boardImages.redCitySolo();
                else
                    return boardImages.redSettlementSolo();
            case 2:
                if(type == 2)
                    return boardImages.yellowCitySolo();
                else
                    return boardImages.yellowSettlementSolo();
            case 3:
                if(type == 2)
                    return boardImages.greenCitySolo();
                else
                    return boardImages.greenSettlementSolo();
            default:
                throw new RuntimeException("Forgot kind=" + color);
        }
    }
  
    public ImageResource getHarborLines(int harbor)
    {
        switch (harbor) {
            case 0:
                return boardImages.harbor00Lines();
            case 1:
                return boardImages.harbor01Lines();
            case 2:
                return boardImages.harbor02Lines();
            case 3:
                return boardImages.harbor03Lines();
            case 4:
                return boardImages.harbor04Lines();
            case 5:
                return boardImages.harbor05Lines();
            case 6:
                return boardImages.harbor06Lines();
            case 7:
                return boardImages.harbor07Lines();
            case 8:
                return boardImages.harbor08Lines();
            default:
                throw new RuntimeException("Forgot kind=" + harbor);
        }
    }
  
    public ImageResource getHarbor(String resource)
    {
        switch (resource) {
            case Constants.HARBORTYPE00:
                return boardImages.threeToOneHarber();
            case Constants.HARBORTYPE01:
                return boardImages.twoToOneOreHarbor();
            case Constants.HARBORTYPE02:
                return boardImages.twoToOneGrainHarbor();
            case Constants.HARBORTYPE03:
                return boardImages.twoToOneLumberHarbor();
            case Constants.HARBORTYPE04:
                return boardImages.twoToOneWoolHarbor();
            case Constants.HARBORTYPE05:
                return boardImages.twoToOneBrickHarbor();
            default:
                throw new RuntimeException("Forgot kind=" + resource);
        }
    }
  
    public ImageResource getRoadToken(int color, int path)
    {
        switch(path)
        {
            case 6:
            case 7:
            case 8:
            case 9:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 62:
            case 63:
            case 64:
            case 65:
                if(color == 0)
                    return boardImages.blueVerticalRoad();
                else if(color == 1)
                    return boardImages.redVerticalRoad();
                else if(color == 2)
                    return boardImages.yellowVerticalRoad();
                else if (color == 3)
                    return boardImages.greenVerticalRoad();
                else
                    return boardImages.emptyVerticalRoad();
            case 0:
            case 2:
            case 4:
            case 10:
            case 12:
            case 14:
            case 16:
            case 23:
            case 25:
            case 27:
            case 29:
            case 31:
            case 40:
            case 42:
            case 44:
            case 46:
            case 48:
            case 55:
            case 57:
            case 59:
            case 61:
            case 67:
            case 69:
            case 71:
                if(color == 0)
                    return boardImages.blueDownLeftUpRightRoad();
                else if(color == 1)
                    return boardImages.redDownLeftUpRightRoad();
                else if(color == 2)
                    return boardImages.yellowDownLeftUpRightRoad();
                else if(color == 3)
                    return boardImages.greenDownLeftUpRightRoad();
                else
                    return boardImages.emptyDownLeftUpRightRoad();
            case 1:
            case 3:
            case 5:
            case 11:
            case 13:
            case 15:
            case 17:
            case 24:
            case 26:
            case 28:
            case 30:
            case 32:
            case 39:
            case 41:
            case 43:
            case 45:
            case 47:
            case 54:
            case 56:
            case 58:
            case 60:
            case 66:
            case 68:
            case 70:
                if(color == 0)
                    return boardImages.blueUpLeftDownRightRoad();
                else if(color == 1)
                    return boardImages.redUpLeftDownRightRoad();
                else if(color == 2)
                    return boardImages.yellowUpLeftDownRightRoad();
                else if(color == 3)
                    return boardImages.greenUpLeftDownRightRoad();
                else
                    return boardImages.emptyUpLeftDownRightRoad();
            default:
                throw new RuntimeException("Forgot kind=" + path);
        }
    }

    public ImageResource getResourceCard(String resource) {
        switch (resource) {
            case Constants.BRICK:
                return boardImages.brickCard();
            case Constants.GRAIN:
                return boardImages.grainCard();
            case Constants.LUMBER:
                return boardImages.lumberCard();
            case Constants.ORE:
                return boardImages.oreCard();
            case Constants.WOOL:
                return boardImages.woolCard();
            default:
                throw new RuntimeException("Forgot kind=" + resource);
        }
    }
    
    public ImageResource getDevelopmentCard(String developmentCard) {
        switch (developmentCard) {
            case Constants.DEVELOPMENTCARDTYPEDEF00:
                return boardImages.soldierDevelopmentCard();
            case Constants.DEVELOPMENTCARDTYPEDEF01:
                return boardImages.yearOfPlentyDevelopmentCard();
            case Constants.DEVELOPMENTCARDTYPEDEF02:
                return boardImages.monopolyDevelopmentCard();
            case Constants.DEVELOPMENTCARDTYPEDEF03:
                return boardImages.roadBuildingDevelopmentCard();
            case Constants.DEVELOPMENTCARDTYPEDEF04:
                return boardImages.libraryDevelopmentCard();
            case Constants.DEVELOPMENTCARDTYPEDEF05:
                return boardImages.governorsHouseDevelopmentCard();
            case Constants.DEVELOPMENTCARDTYPEDEF06:
                return boardImages.universityOfCatanDevelopmentCard();
            case Constants.DEVELOPMENTCARDTYPEDEF07:
                return boardImages.chapelDevelopmentCard();
            case Constants.DEVELOPMENTCARDTYPEDEF08:
                return boardImages.marketDevelopmentCard();
            default:
                throw new RuntimeException("Forgot kind=" + developmentCard);
        }
    }
    
    public ImageResource getLargestArmy()
    {
        return boardImages.largestArmy();
    }
    
    public ImageResource getLongestRoad()
    {
        return boardImages.longestRoad();
    }
  
    public ImageResource getEmptyBoard() {
        return boardImages.empty();
    }
}