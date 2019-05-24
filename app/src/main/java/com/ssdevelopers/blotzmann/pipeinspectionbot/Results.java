package com.ssdevelopers.blotzmann.pipeinspectionbot;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;

public class Results extends AppCompatActivity {
    private static final String TAG = "ResultsData";
    private LineChart dp1;
    private LineChart dp2;
    private LineChart dp3;
    private LineChart dp4;
    private LineChart dp5;
    private LineChart dp6;
    private LineChart dp7;
    private LineChart dp8;
    private LineChart dp9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("data");
        ArrayList<Float> dl1 = (ArrayList<Float>) args.getSerializable("DataList1");
        Log.i(TAG,String.valueOf(dl1.size()));
        ArrayList<Float> dl2 = (ArrayList<Float>) args.getSerializable("DataList2");
        ArrayList<Float> dl3 = (ArrayList<Float>) args.getSerializable("DataList3");
        ArrayList<Float> dl4 = (ArrayList<Float>) args.getSerializable("DataList4");
        ArrayList<Float> dl5 = (ArrayList<Float>) args.getSerializable("DataList5");
        ArrayList<Float> dl6 = (ArrayList<Float>) args.getSerializable("DataList6");
        ArrayList<Float> dl7 = (ArrayList<Float>) args.getSerializable("DataList7");
        ArrayList<Float> dl8 = (ArrayList<Float>) args.getSerializable("DataList8");
        ArrayList<Float> dl9 = (ArrayList<Float>) args.getSerializable("DataList9");
        ArrayList<Float> dis = (ArrayList<Float>) args.getSerializable("distanceList");


        dp1=(LineChart) findViewById(R.id.dp1);
        dp2=(LineChart) findViewById(R.id.dp2);
        dp3=(LineChart) findViewById(R.id.dp3);
        dp4=(LineChart) findViewById(R.id.dp4);
        dp5=(LineChart) findViewById(R.id.dp5);
        dp6=(LineChart) findViewById(R.id.dp6);
        dp7=(LineChart) findViewById(R.id.dp7);
        dp8=(LineChart) findViewById(R.id.dp8);
        dp9=(LineChart) findViewById(R.id.dp9);


        ArrayList<Entry> de1 = new ArrayList<>();
        ArrayList<Entry> de2 = new ArrayList<>();
        ArrayList<Entry> de3 = new ArrayList<>();
        ArrayList<Entry> de4 = new ArrayList<>();
        ArrayList<Entry> de5 = new ArrayList<>();
        ArrayList<Entry> de6 = new ArrayList<>();
        ArrayList<Entry> de7 = new ArrayList<>();
        ArrayList<Entry> de8 = new ArrayList<>();
        ArrayList<Entry> de9 = new ArrayList<>();

        for(int i=0;i<dl1.size();i++){
            de1.add(new Entry(dis.get(i),dl1.get(i)));
            de2.add(new Entry(dis.get(i),dl2.get(i)));
            de3.add(new Entry(dis.get(i),dl3.get(i)));
            de4.add(new Entry(dis.get(i),dl4.get(i)));
            de5.add(new Entry(dis.get(i),dl5.get(i)));
            de6.add(new Entry(dis.get(i),dl6.get(i)));
            de7.add(new Entry(dis.get(i),dl7.get(i)));
            de8.add(new Entry(dis.get(i),dl8.get(i)));
            de9.add(new Entry(dis.get(i),dl9.get(i)));
        }
        Log.i(TAG,String.valueOf(de1.size()));

        Collections.sort(de1,new EntryXComparator());
        Collections.sort(de2,new EntryXComparator());
        Collections.sort(de3,new EntryXComparator());
        Collections.sort(de4,new EntryXComparator());
        Collections.sort(de5,new EntryXComparator());
        Collections.sort(de6,new EntryXComparator());
        Collections.sort(de7,new EntryXComparator());
        Collections.sort(de8,new EntryXComparator());
        Collections.sort(de9,new EntryXComparator());

        Log.i(TAG,de1.get(0).toString());
        Log.i(TAG,de1.get(1).toString());

        ArrayList<ILineDataSet> lineDataSet1 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSet2 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSet3 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSet4 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSet5 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSet6 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSet7 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSet8 = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSet9 = new ArrayList<>();

        LineDataSet lsd1=new LineDataSet(de1,"Data1");
        LineDataSet lsd2=new LineDataSet(de2,"Data2");
        LineDataSet lsd3=new LineDataSet(de3,"Data3");
        LineDataSet lsd4=new LineDataSet(de4,"Data4");
        LineDataSet lsd5=new LineDataSet(de5,"Data5");
        LineDataSet lsd6=new LineDataSet(de6,"Data6");
        LineDataSet lsd7=new LineDataSet(de7,"Data7");
        LineDataSet lsd8=new LineDataSet(de8,"Data8");
        LineDataSet lsd9=new LineDataSet(de9,"Data9");

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
        lsd1.setFillDrawable(drawable);
        lsd2.setFillDrawable(drawable);
        lsd3.setFillDrawable(drawable);
        lsd4.setFillDrawable(drawable);
        lsd5.setFillDrawable(drawable);
        lsd6.setFillDrawable(drawable);
        lsd7.setFillDrawable(drawable);
        lsd8.setFillDrawable(drawable);
        lsd9.setFillDrawable(drawable);

        lsd1.setDrawFilled(true);
        lsd2.setDrawFilled(true);
        lsd3.setDrawFilled(true);
        lsd4.setDrawFilled(true);
        lsd5.setDrawFilled(true);
        lsd6.setDrawFilled(true);
        lsd7.setDrawFilled(true);
        lsd8.setDrawFilled(true);
        lsd9.setDrawFilled(true);

        lineDataSet1.add(lsd1);
        lineDataSet2.add(lsd2);
        lineDataSet3.add(lsd3);
        lineDataSet4.add(lsd4);
        lineDataSet5.add(lsd5);
        lineDataSet6.add(lsd6);
        lineDataSet7.add(lsd7);
        lineDataSet8.add(lsd8);
        lineDataSet9.add(lsd9);

        dp1.setData(new LineData(lineDataSet1));
        dp2.setData(new LineData(lineDataSet2));
        dp3.setData(new LineData(lineDataSet3));
        dp4.setData(new LineData(lineDataSet4));
        dp5.setData(new LineData(lineDataSet5));
        dp6.setData(new LineData(lineDataSet6));
        dp7.setData(new LineData(lineDataSet7));
        dp8.setData(new LineData(lineDataSet8));
        dp9.setData(new LineData(lineDataSet9));



    }
}
