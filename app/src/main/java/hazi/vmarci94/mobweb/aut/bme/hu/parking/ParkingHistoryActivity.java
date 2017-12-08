package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import hazi.vmarci94.mobweb.aut.bme.hu.parking.data.ParkingHistory;

/**
 * Created by vmarci94 on 2017. 11. 27..
 */

public class ParkingHistoryActivity extends AppCompatActivity {

    private PieChart chartHoliday;
    private Button btnClear;
    private final List<ParkingHistory> parkingHistories = new ArrayList<>(); //szebb lenne a konstruktorban inicializálni, de activitynél a konstruktor nincs sok értelme, az onCreate-ben való initért pedig sír.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        chartHoliday = (PieChart) findViewById(R.id.chartParking);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO reset pie chart and clear db
            }
        });

        //loadParkingHistorysInBackground();
        loadChart();

    }

    private void loadChart() {
        List<PieEntry> entries = new ArrayList<>();

        for (ParkingHistory tmp : parkingHistories){
            entries.add(new PieEntry(tmp.getPrice(), tmp.getName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "PaymentAndTaxes");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        chartHoliday.setData(data);
        chartHoliday.invalidate();

    }

    private void loadParkingHistorysInBackground() {
        new AsyncTask<Void, Void, List<hazi.vmarci94.mobweb.aut.bme.hu.parking.data.ParkingHistory>>() {

            @Override
            protected List<ParkingHistory> doInBackground(Void... voids) {
                return ParkingHistory.listAll(ParkingHistory.class);
            }

            @Override
            protected void onPostExecute(List<ParkingHistory> shoppingItems) {
                super.onPostExecute(shoppingItems);
                update(shoppingItems);
            }
        }.execute();
    }


    public void addItem(ParkingHistory parkingHistory){
        parkingHistories.add(parkingHistory);
    }

    public void remove(ParkingHistory parkingHistory){
        parkingHistories.remove(parkingHistory);
    }

    public void update(List<hazi.vmarci94.mobweb.aut.bme.hu.parking.data.ParkingHistory> parkingHistoryList){
        parkingHistories.clear();
        parkingHistories.addAll(parkingHistoryList);
    }

    public void clear(){
        this.parkingHistories.clear();
    }

}
