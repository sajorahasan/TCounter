package com.sajorahasan.tiffincounter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.sajorahasan.tiffincounter.adapter.TiffinAdapter;
import com.sajorahasan.tiffincounter.room.AppDatabase;
import com.sajorahasan.tiffincounter.room.Tiffin;
import com.sajorahasan.tiffincounter.utils.Constant;
import com.sajorahasan.tiffincounter.utils.SharedPrefsUtils;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppDatabase appDatabase;
    private CompositeDisposable disposable;
    private SharedPrefsUtils prefsUtils;

    private TextView tvTotalAmount;
    private LinearLayout emptyLayout;
    private RecyclerView recyclerView;
    private TiffinAdapter tiffinAdapter;

    private int lunchPrice, dinnerPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Views
        initViews();

        // Updating UI
        updateUI();

//        Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(MainActivity.this.makeBlockingTask()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()) //Run on UI Thread
//                .doOnSubscribe(__ -> showLoadingIndicator())
//                .doFinally(() -> hideLoadingIndicator())
//                .subscribe(this::processResult, this::processError);

    }

    private void initViews() {
        recyclerView = findViewById(R.id.tiffinRecyclerView);
        emptyLayout = findViewById(R.id.emptyLayout);
        tvTotalAmount = findViewById(R.id.txvTotalAmount);
        Button btnAddLunch = findViewById(R.id.btnAddLunch);
        Button btnAddDinner = findViewById(R.id.btnAddDinner);
        Button btnClearAll = findViewById(R.id.btnClearAll);

        disposable = new CompositeDisposable();
        appDatabase = AppDatabase.getInstance(MainActivity.this);
        prefsUtils = SharedPrefsUtils.getInstance(MainActivity.this);

        lunchPrice = prefsUtils.getInt(Constant.LUNCH);
        dinnerPrice = prefsUtils.getInt(Constant.DINNER);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<Tiffin> tiffinList = new ArrayList<>();
        tiffinAdapter = new TiffinAdapter(tiffinList);
        recyclerView.setAdapter(tiffinAdapter);

        tiffinAdapter.setOnItemClickListener(new TiffinAdapter.OnItemClickListener() {
            @Override
            public void onDateClick(View view, int position) {
                showDateEditDialog(tiffinAdapter.getItem(position));
            }

            @Override
            public void onAmountClick(View view, int position) {
                showAmountEditDialog(tiffinAdapter.getItem(position));
            }
        });

        tiffinAdapter.setOnItemLongClickListener((view, position) ->
                new MaterialDialog.Builder(this)
                        .title("Delete " + tiffinAdapter.getItem(position).getTiffinDate() + " Tiffin")
                        .content("Are you sure you want to delete this tiffin?")
                        .positiveText("Yes")
                        .negativeText("No")
                        .iconRes(R.drawable.ic_delete)
                        .onPositive((dialog, which) ->
                                disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(MainActivity.this.deleteTiffin(tiffinAdapter.getItem(position))))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(this::handleResponse, this::handleError)))
                        .show());

        Tools.rippleBlack(btnAddLunch);
        Tools.rippleBlack(btnAddDinner);
        Tools.rippleBlack(btnClearAll);

        btnClearAll.setOnClickListener(v -> deleteAll());

        btnAddLunch.setOnClickListener(v -> {
            if (lunchPrice != 0) {
                addTiffin(Constant.LUNCH, lunchPrice);
            } else {
                setLunchPrice();
            }
        });

        btnAddDinner.setOnClickListener(v -> {
            if (dinnerPrice != 0) {
                addTiffin(Constant.DINNER, dinnerPrice);
            } else {
                setDinnerPrice();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_info:
                startActivity(new Intent(MainActivity.this, SummaryActivity.class));
                return true;
            case R.id.action_update_lunch:
                setLunchPrice();
                return true;
            case R.id.action_update_dinner:
                setDinnerPrice();
                return true;
            case R.id.action_help:
                showHelpDialog();
                return true;
            case R.id.action_about:
                showAboutDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAboutDialog() {
        new MaterialDialog.Builder(this)
                .title("Welcome to Tiffin Counter app")
                .content(R.string.about_content)
                .positiveText("Okay")
                .show();
    }

    private void showHelpDialog() {
        new MaterialDialog.Builder(this)
                .title("Help")
                .content(R.string.help_content)
                .positiveText("Okay")
                .show();
    }

    private void showDateEditDialog(Tiffin tiffin) {
        // Get Tiffin Date
        Calendar cal = Calendar.getInstance();
        cal.setTime(tiffin.getAdded());
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a   E");
                    Calendar c1 = Calendar.getInstance();
                    c1.set(year, monthOfYear, dayOfMonth);

                    tiffin.setAdded(c1.getTime());
                    tiffin.setTiffinDate(sdf.format(c1.getTime()));

                    disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(MainActivity.this.updateTiffin(tiffin)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(tiffinList -> handleResponse(tiffinList), throwable -> handleError(throwable)));
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void showAmountEditDialog(Tiffin tiffin) {
        new MaterialDialog.Builder(this)
                .title(tiffin.getType() + " Tiffin Price")
                .content("Update tiffin price for " + tiffin.getTiffinDate())
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Price", null, (dialog, input) -> {
                    if (!TextUtils.isEmpty(input)) {
                        tiffin.setAmount(Integer.parseInt(input.toString()));
                        disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(MainActivity.this.updateTiffin(tiffin)))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::handleResponse, this::handleError));
                    } else {
                        Tools.toastError(MainActivity.this, "Price cannot be empty");
                    }
                }).show();
    }

    private List<Tiffin> updateTiffin(Tiffin tiffin) {
        appDatabase.getTiffinDao().updateTiffin(tiffin);
        return appDatabase.getTiffinDao().getAllTiffins();
    }

    private void setLunchPrice() {
        new MaterialDialog.Builder(this)
                .title(Constant.LUNCH + " Tiffin Price")
                .content("Please set price for " + Constant.LUNCH + " tiffin before adding tiffin")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Price", null, (dialog, input) -> {
                    if (!TextUtils.isEmpty(input)) {
                        lunchPrice = Integer.parseInt(input.toString().trim());
                        prefsUtils.putInt(Constant.LUNCH, lunchPrice);
                    } else {
                        Tools.toastError(MainActivity.this, "Price cannot be empty");
                    }
                }).show();
    }

    private void setDinnerPrice() {
        new MaterialDialog.Builder(this)
                .title(Constant.LUNCH + " Tiffin Price")
                .content("Please set price for " + Constant.LUNCH + " tiffin before adding tiffin")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input("Price", null, (dialog, input) -> {
                    if (!TextUtils.isEmpty(input)) {
                        dinnerPrice = Integer.parseInt(input.toString().trim());
                        prefsUtils.putInt(Constant.DINNER, dinnerPrice);
                    } else {
                        Tools.toastError(MainActivity.this, "Price cannot be empty");
                    }
                }).show();
    }

    private void deleteAll() {
        new MaterialDialog.Builder(this)
                .title("Delete All Tiffins Data")
                .content("Are you sure you want to delete all tiffins data?")
                .positiveText("Yes")
                .negativeText("No")
                .iconRes(R.drawable.ic_delete)
                .onPositive((dialog, which) ->
                        disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(MainActivity.this.deleteAllTiffins()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(this::handleResponse, this::handleError)))
                .show();
    }

    private void updateUI() {
        disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(MainActivity.this.getAllTiffins()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }

    private List<Tiffin> getAllTiffins() {
        return appDatabase.getTiffinDao().getAllTiffins();
    }

    private void addTiffin(String type, int amt) {
        disposable.add(Single.create((SingleOnSubscribe<List<Tiffin>>) e -> e.onSuccess(MainActivity.this.addToDb(type, amt)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError));
    }


    private List<Tiffin> addToDb(String type, int amt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DOB_FORMAT);

        Date newDate = sdf.parse(Tools.getFormattedDateSimple());
        String date = sdf.format(newDate);

        Log.d(TAG, "addToDb: " + date);
        appDatabase.getTiffinDao().insert(new Tiffin(Tools.getFormattedDate(), type, amt, newDate));
        return appDatabase.getTiffinDao().getAllTiffins();
    }

    private List<Tiffin> deleteTiffin(Tiffin tiffin) {
        appDatabase.getTiffinDao().delete(tiffin);
        return appDatabase.getTiffinDao().getAllTiffins();
    }

    private List<Tiffin> deleteAllTiffins() {
        appDatabase.getTiffinDao().deleteAll();
        return appDatabase.getTiffinDao().getAllTiffins();
    }

    private void handleResponse(List<Tiffin> tiffinList) {
        if (tiffinList != null && tiffinList.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);

            // calculating tiffin price
            int total = 0;
            for (Tiffin t : tiffinList) {
                total = total + t.getAmount();
            }
            tvTotalAmount.setText(String.valueOf(total));

            tiffinAdapter = new TiffinAdapter(tiffinList);
            recyclerView.scrollToPosition(tiffinAdapter.getItemCount() - 1);
            recyclerView.setAdapter(tiffinAdapter);
            tiffinAdapter.notifyDataSetChanged();
        } else {
            tvTotalAmount.setText("0");
            recyclerView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
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
}
