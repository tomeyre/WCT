package eyresapps.com.data;

import java.util.ArrayList;

public class AreaList {

    private ArrayList<StreetList> areaList;

    AreaList(ArrayList<StreetList> areaList){
        this.areaList = areaList;
    }

    public ArrayList<StreetList> getAreaList() {
        return areaList;
    }
}
