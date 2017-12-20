package hazi.vmarci94.mobweb.aut.bme.hu.parking;

import android.graphics.Color;
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
import hazi.vmarci94.mobweb.aut.bme.hu.parking.data.ParkingHistoryDataManager;

/**
 * Created by vmarci94 on 2017. 11. 27..
 */

public class ParkingHistoryActivity extends AppCompatActivity {

    private PieChart chartParkingHistory;
    private ParkingHistoryDataManager parkingHistoryDataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        chartParkingHistory = (PieChart) findViewById(R.id.chartParking);
        Button btnClear = (Button) findViewById(R.id.btnClear);
        parkingHistoryDataManager = ParkingHistoryDataManager.getInstance();
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartParkingHistory.clear();
                parkingHistoryDataManager.removeAll();
            }
        });
        loadChart();
    }

    private void loadChart() {
        List<PieEntry> entries = new ArrayList<>();

        for (ParkingHistory tmp : parkingHistoryDataManager){
            entries.add(new PieEntry(tmp.getPrice(), tmp.getName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.payment));
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        chartParkingHistory.setData(data);
        chartParkingHistory.invalidate();
        chartParkingHistory.setCenterText(getString(R.string.pieChartCenterString));
        chartParkingHistory.setCenterTextSize(14f);
        chartParkingHistory.setCenterTextColor(Color.GREEN);

    }

}
