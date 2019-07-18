package com.example.charts;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ConstraintLayout mConstraintLayout;
    ArrayList<Item> list;
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConstraintLayout = findViewById(R.id.card_layout);

        list = new ArrayList<>();

        total = 23;

        list.add(new Item(3, "Item 1"));
        list.add(new Item(2, "Item 2"));
        list.add(new Item(4, "Item 3"));
        list.add(new Item(1, "Item 4"));
        list.add(new Item(6, "Item 5"));
        list.add(new Item(7, "Item 6"));

setBar();

    }

    private void setBar(){
        mConstraintLayout.removeAllViews();
        BarChart mChart = new HorizontalBarChart(this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mChart.setLayoutParams(params);

        List<BarEntry> entries = new ArrayList<>();
        final String[] labels = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            String x = i + "f";
            BarEntry entry = new BarEntry(Float.valueOf(x), list.get(i).getValue());

            entries.add(entry);
            labels[i] = list.get(i).getLabel();
        }
        BarDataSet set = new BarDataSet(entries, "");
        set.setColors(ColorTemplate.COLORFUL_COLORS);

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labels[(int) value];
            }
        };
        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        BarData data = new BarData(set);


        data.setBarWidth(0.9f); // set custom bar width
        mChart.setData(data);
        mChart.getLegend().setEnabled(false);
        mChart.setFitBars(true); // make the x-axis fit exactly all bars
        mChart.invalidate(); // refresh

        mConstraintLayout.addView(mChart);

//
//
    }

    private void setPie(){
        mConstraintLayout.removeAllViews();

        final PieChart mChart = new PieChart(this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mChart.setLayoutParams(params);
        List<PieEntry> pieEntryList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Item answer = list.get(i);

            PieEntry entry = new PieEntry((float) answer.getValue(), answer.getLabel(), answer);
            pieEntryList.add(entry);
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
//        pieDataSet.setColors(mColorList);
        pieDataSet.setSliceSpace(3);

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);

        mChart.setData(pieData);
        mChart.setPadding(8, 0, 8, 0);
        mChart.setDrawEntryLabels(false);
        mChart.setHoleRadius(85);
        mChart.setTransparentCircleRadius(90);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Item answer = (Item) e.getData();
                String description = answer.getLabel();

                Log.d(TAG, "onValueSelected: item " + answer.getValue());

                Double percentage = 0.0;
                Double val = (double) answer.getValue() / total;
                Log.d(TAG, "onValueSelected: val " + val);
                percentage = (val * 100);
                Log.d(TAG, "onValueSelected: percentage " + percentage);

                SpannableString spannableString = new SpannableString(description + "\n" + String.format("%.1f", percentage) + "%");
                spannableString.setSpan(new RelativeSizeSpan(2f), 0, description.length(), 0);
                spannableString.setSpan(new RelativeSizeSpan(1.5f), description.length(), spannableString.length(), 0);
                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), 0, description.length() + 1, 0);// set color
                spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), description.length() + 1, spannableString.length(), 0);// set color

                mChart.setCenterText(spannableString);
                mChart.setCenterTextSize(12);

            }

            @Override
            public void onNothingSelected() {
                mChart.setCenterText("");

            }
        });

        mChart.highlightValue(new Highlight(0, 0, 0), true);
        mChart.invalidate(); // refresh

        mConstraintLayout.addView(mChart);
    }
}


class Item {
    private int value;
    private String label;

    public Item(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
