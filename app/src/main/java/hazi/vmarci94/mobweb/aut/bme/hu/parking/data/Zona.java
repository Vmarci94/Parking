package hazi.vmarci94.mobweb.aut.bme.hu.parking.data;

import com.google.android.gms.maps.model.Polygon;

import java.util.Date;

/**
 * Created by vmarci94 on 2017. 11. 19..
 */

public class Zona {
    private String name;
    private Date start, end;
    private int price;
    private String description;
    private String phone;
    private Polygon polygon;

    public Zona() {
    }

    public Zona(String name, Date start, Date end, int price, String description, String phone) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.price = price;
        this.description = description;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
