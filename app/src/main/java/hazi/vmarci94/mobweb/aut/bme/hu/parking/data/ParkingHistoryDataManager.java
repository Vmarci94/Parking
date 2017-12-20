package hazi.vmarci94.mobweb.aut.bme.hu.parking.data;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vmarci94 on 2017. 12. 15..
 */

public class ParkingHistoryDataManager  implements Iterable<ParkingHistory>{

    private static ParkingHistoryDataManager instance = null;
    private final List<ParkingHistory> parkingHistoryList;

    private ParkingHistoryDataManager() {
        parkingHistoryList = new ArrayList<ParkingHistory>();
    }

    public static ParkingHistoryDataManager getInstance(){
        if(instance == null){
            instance = new ParkingHistoryDataManager();
        }
        return instance;
    }

    public void addItem(ParkingHistory parkingHistory) {
        parkingHistoryList.add(parkingHistory);
    }

    public void removeItem(ParkingHistory parkingHistory) {
        parkingHistoryList.remove(parkingHistory);
    }

    public void removeAll(){
        ParkingHistory.deleteAll(ParkingHistory.class);
        parkingHistoryList.clear();
    }

    public void update(List<ParkingHistory> parkingHistories) {
        parkingHistoryList.clear();
        parkingHistoryList.addAll(parkingHistories);
    }

    @NonNull
    @Override
    public Iterator<ParkingHistory> iterator() {
        return this.parkingHistoryList.iterator();
    }
}
