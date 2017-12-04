package com.sajorahasan.tiffincounter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.sajorahasan.tiffincounter.adapter.TiffinFiltersAdapter;
import com.sajorahasan.tiffincounter.room.AppDatabase;
import com.sajorahasan.tiffincounter.room.Tiffin;
import com.sajorahasan.tiffincounter.utils.Constant;
import com.sajorahasan.tiffincounter.utils.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SummaryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "SummaryActivity";

    private TextView tvTotalLunch, tvTotalDinner, tvTtlLunchAmt, tvTtlDinnerAmt;
    private PieChart pieChart;
    private Spinner dateSpinner;
    private LinearLayout emptyLayout;
    private RecyclerView recyclerView;
    private TiffinFiltersAdapter adapter;

    private AppDatabase appDatabase;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Initializing Views
        initViews();

        // Updating UI
        updateUI();

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pieChart = findViewById(R.id.tiffinPieChart);
        dateSpinner = findViewById(R.id.dateSpinner);
        emptyLayout = findViewById(R.id.emptyLayout);
        recyclerView = findViewById(R.id.filterRecyclerView);
        tvTotalLunch = findViewById(R.id.txvTotalLunch);
        tvTotalDinner = findViewById(R.id.txvTotalDinner);
        tvTtlLunchAmt = findViewById(R.id.txvTotalLunchAmt);
        tvTtlDinnerAmt = findViewById(R.id.txvTotalDinnerAmt);

        disposable = new CompositeDisposable();
        appDatabase = AppDatabase.getInstance(SummaryActivity.this);

        // Clearing Pie chart legend description text
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight h) {
                Description description = new Description();
                description.setText(((int) entry.getY()) + " Tiffins");
                pieChart.setDescription(description);
            }

            @Override
            public void onNothingSelected() {
                Description description = new Description();
                description.setText("");
                pieChart.setDescription(description);
            }
        });

        // SettingUp Spinner
        List<String> stringList = new ArrayList<>();
        stringList.add("All");
        stringList.add("Lunch");
        stringList.add("Dinner");
        stringList.add("Today Tiffins");
        stringList.add("Tiffins this week");
        stringList.add("Tiffins this month");

        ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(SummaryActivity.this,
                android.R.layout.simple_spinner_item, stringList);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(eventAdapter);

        dateSpinner.setOnItemSelectedListener(this);

        // SettingUp RecyclerView
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SummaryActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<Tiffin> tiffinList = new ArrayList<>();
        adapter = new TiffinFiltersAdapter(tiffinList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void updateUI() {
        disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(SummaryActivity.this.getAllTiffins()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void updateUI(String filter) {
        disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(SummaryActivity.this.filterTiffins(filter)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void updateUI(Date startDate, Date endDate) {
        disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e ->
                e.onSuccess(SummaryActivity.this.filterTiffinsByDate(startDate, endDate)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void updateUIByDate(Date date) {
        disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(SummaryActivity.this.getTiffinsByDate(date)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private List<Tiffin> getTiffinsByDate(Date date) {
        List<Tiffin> tiffins = appDatabase.getTiffinDao().getTodayTiffins(date);
        for (Tiffin t : tiffins) {
            Log.d(TAG, "getTiffinsByDate: " + t.getAdded());
        }
        return tiffins;
    }

    private List<Tiffin> filterTiffinsByDate(Date startDate, Date endDate) {
        List<Tiffin> tiffins = appDatabase.getTiffinDao().filterTiffinsByDate(startDate, endDate);
        for (Tiffin t : tiffins) {
            Log.d(TAG, "filterTiffinsByDate: " + t.getAdded());
        }
        return tiffins;
    }

    private List<Tiffin> filterTiffins(String filter) {
        return appDatabase.getTiffinDao().filterTiffins(filter);
    }

    private List<Tiffin> getAllTiffins() {
        return appDatabase.getTiffinDao().getAllTiffins();
    }

    private void handleResponse(List<Tiffin> tiffinList) {
        if (tiffinList != null && tiffinList.size() != 0) {

            int totalLunch = 0;
            int totalDinner = 0;
            int totalLunchAmount = 0;
            int totalDinnerAmount = 0;
            int totalAmount = 0;

            for (int i = 0; i < tiffinList.size(); i++) {

                totalAmount = totalAmount + tiffinList.get(i).getAmount();

                if (tiffinList.get(i).getType().equalsIgnoreCase(Constant.LUNCH)) {
                    totalLunch = totalLunch + 1;
                    totalLunchAmount = totalLunchAmount + tiffinList.get(i).getAmount();
                }
                if (tiffinList.get(i).getType().equalsIgnoreCase(Constant.DINNER)) {
                    totalDinner = totalDinner + 1;
                    totalDinnerAmount = totalDinnerAmount + tiffinList.get(i).getAmount();
                }
            }

            initPie();

            List<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(totalLunch, "Lunch"));
            entries.add(new PieEntry(totalDinner, "Dinner"));

            //PieDataSet set = new PieDataSet(entries, "Total Tiffins : " + tiffinList.size());
            pieChart.setCenterText(tiffinList.size() + " Tiffins");

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(3f);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(getResources().getColor(android.R.color.holo_green_light));
            colors.add(getResources().getColor(android.R.color.holo_red_light));
            dataSet.setColors(colors);

            PieData pieData = new PieData(dataSet);
            pieData.setValueTextSize(11f);
            pieData.setValueTextColor(Color.WHITE);
            pieChart.setData(pieData);
            pieChart.highlightValues(null);
            pieChart.invalidate(); // refresh

            tvTotalLunch.setText(String.valueOf(totalLunch));
            tvTotalDinner.setText(String.valueOf(totalDinner));
            tvTtlLunchAmt.setText(String.valueOf(totalLunchAmount));
            tvTtlDinnerAmt.setText(String.valueOf(totalDinnerAmount));

            // SettingUp recyclerView
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new TiffinFiltersAdapter(tiffinList);
            recyclerView.setAdapter(adapter);

        } else {
            tvTotalLunch.setText("0");
            tvTotalDinner.setText("0");
            tvTtlLunchAmt.setText("0");
            tvTtlDinnerAmt.setText("0");
            pieChart.clear();
            pieChart.invalidate();
            recyclerView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initPie() {
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    private void handleError(Throwable throwable) {
        Log.d(TAG, "handleError: " + throwable.getLocalizedMessage());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.clear();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.clear();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DOB_FORMAT);

        String filter = parent.getItemAtPosition(position).toString();
        if (filter.equalsIgnoreCase("All")) {
            updateUI();

        } else if (filter.equalsIgnoreCase("Today Tiffins")) {
            try {
                Date date = sdf.parse(Tools.getFormattedDateSimple(new Date()));
                Log.d(TAG, "onItemSelected: date --> " + Tools.getFormattedDateSimple(new Date()));
                updateUIByDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (filter.equalsIgnoreCase("Tiffins this week")) {
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Date startDate = sdf.parse(Tools.getFormattedDateSimple(calendar.getTime()));

                calendar.add(Calendar.DATE, 6);
                Date endDate = sdf.parse(Tools.getFormattedDateSimple(calendar.getTime()));

                Log.d(TAG, "onItemSelected: startDate --> " + Tools.getFormattedDateSimple(startDate)
                        + "  <-- endDate --> " + Tools.getFormattedDateSimple(endDate));
                updateUI(startDate, endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (filter.equalsIgnoreCase("Tiffins this month")) {
            try {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                Date startDate = sdf.parse(Tools.getFormattedDateSimple(c.getTime()));

                Calendar c2 = Calendar.getInstance();
                c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date endDate = sdf.parse(Tools.getFormattedDateSimple(c2.getTime()));

                Log.d(TAG, "onItemSelected: startDate --> " + Tools.getFormattedDateSimple(startDate)
                        + "  <-- endDate --> " + Tools.getFormattedDateSimple(endDate));
                updateUI(startDate, endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            updateUI(filter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
