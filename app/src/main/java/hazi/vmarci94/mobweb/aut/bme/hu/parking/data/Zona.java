package hazi.vmarci94.mobweb.aut.bme.hu.parking.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import java.util.List;

/**
 * Created by vmarci94 on 2017. 11. 19..
 */

public class Zona {
    private String name;
    private String price;
    private String phone;
    private KmlPolygon kmlPolygon;
    private Polygon polygon;

    public Zona() {
    }

    public Zona(KmlPlacemark placemark){
        name = placemark.getProperty("name");

        String[] phoneNumAndPrice = placemark.getProperty("description").split("-");
        phone = phoneNumAndPrice[0];
        price = phoneNumAndPrice[1];
        kmlPolygon = ((KmlPolygon) placemark.getGeometry());
    }

    public List<LatLng> getPolygonCoords(){
        return kmlPolygon.getGeometryObject().get(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public KmlPolygon getKmlPolygon() {
        return kmlPolygon;
    }

    public void setKmlPolygon(KmlPolygon kmlPolygon) {
        this.kmlPolygon = kmlPolygon;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
