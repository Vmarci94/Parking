package hazi.vmarci94.mobweb.aut.bme.hu.parking.data;

import com.orm.SugarRecord;

/**
 * Created by vmarci94 on 2017. 12. 04..
 */

public class ParkingHistory extends SugarRecord {
    private String name;
    private int price;

    public ParkingHistory(){}

    public ParkingHistory(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
