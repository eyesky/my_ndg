package com.apptech.myndg;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apptech.myndg.adapter.CarDownloadAdapter;
import com.apptech.myndg.customviews.DialogController;
import com.apptech.myndg.database.CommonDao;
import com.apptech.myndg.database.PreferenceUtil;
import com.apptech.myndg.model.CarInfo;
import com.apptech.myndg.utils.NdgApp;
import com.apptech.myndg.utils.Values;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PreferenceUtil preferenceUtil;
    private CommonDao commonDao;
    private CarDownloadAdapter adapter;
    private int position;

    private String[] carNames = {"Qashqai EUR Specs", "Qashqai RUS Specs", "Juke", "X-Trail EUR Specs", "X-Trail RUS Specs",
            "Pulsar", "Micra", "Note", "Leaf", "Navara", "All New Nissan Micra", "New Nissan QASHQAI", "New Nissan X-TRAIL", "New Nissan LEAF"};

    private int[] previousCarArray = {1, 2, 4, 5, 7, 9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onResume() {
        new LoadDataBase().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        super.onResume();

        List<Object> list = commonDao.getAllOderCarList(this);
        for (int i = 0; i < list.size(); i++) {
            CarInfo c = (CarInfo) list.get(i);
//            Log.e("onResume: ", "" + list.get(i).getId() + " = " + list.get(i).getName() + " -- " + list.get(i).getStatus() + " -- " + list.get(i).getRegion());
            Log.e("onResume: ", "" + c.getId() + " = " + c.getName() + " -- " + c.getStatus() + " -- " + c.getRegion());
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_landing);
        preferenceUtil = new PreferenceUtil(getApplicationContext());
        commonDao = CommonDao.getInstance();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View view) {
            position = (Integer) view.getTag();

            if (adapter.getItem(position).getClass() == CarInfo.class) {
                CarInfo info = (CarInfo) adapter.getItem(position);
                if ("1".equalsIgnoreCase(info.getStatus())) {
                    adapter.setSelection(position);

                } else {
                    if (info.getId() == 1 || info.getId() == 2 || info.getId() == 4 || info.getId() == 5) {
                        showCarDownloadDialog(info.getId());
                    } else {
                        showCarDownloadDialogForSingleCar(info.getId());
                    }
                }
            }
        }
    };

    private void showCarDownloadDialogForSingleCar(final int carID) {
        final Dialog dialog = new DialogController(MainActivity.this).langDialog();

        TextView txtViewTitle = (TextView) dialog.findViewById(R.id.txt_title);
        txtViewTitle.setText(getResources().getString(R.string.car_download_msg));

        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                checkingDownloadedCar(carID);
            }
        });

        dialog.show();
    }


    public class LoadDataBase extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            if (preferenceUtil.getIsDatabaseEmpty()) {
                for (int i = 0; i < carNames.length; i++) {
                    CarInfo carInfo;
                    if (i == 1 || i == 4) {
                        carInfo = new CarInfo((i + 1), carNames[i], "0", NdgApp.getInstance().getDateTime(), "RUS", "en", 0);
                    } else {
                        carInfo = new CarInfo((i + 1), carNames[i], "0", NdgApp.getInstance().getDateTime(), "EUR", "en", 0);
                    }
                    commonDao.insertInCarInfoTable(getBaseContext(), carInfo);
                }
                preferenceUtil.setIsDatabaseEmpty(false);

            } else {
                int lastID = commonDao.getLastID(getBaseContext());
                for (int i = lastID; i < carNames.length; i++) {
                    commonDao.insertInCarInfoTable(getBaseContext(), new CarInfo((lastID + 1), carNames[lastID], "0", NdgApp.getInstance().getDateTime(), "EUR", "en", 0));
                }
            }

            for (int i = 0; i < previousCarArray.length; i++) {
                if (commonDao.getStatus(getBaseContext(), previousCarArray[i]) == 0) {
                    commonDao.updateCarStatus(getBaseContext(), previousCarArray[i], 2);
                }
            }

            return "Executed";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            NdgApp.getInstance().setCarList(commonDao.getAllOderCarList(getBaseContext()));
            NdgApp.getInstance().setCarAllList(commonDao.getAllCarList(getBaseContext()));

            setHeaderData();

            for (int i = 0; i < NdgApp.getInstance().getCarList().size(); i++) {
                if (NdgApp.getInstance().getCarList().get(i).getClass() == CarInfo.class) {
                    CarInfo info = (CarInfo) NdgApp.getInstance().getCarList().get(i);
                    if (info.getStatus().equalsIgnoreCase("1")) {
                        if (preferenceUtil.getSelectedCarType() == info.getId()) {
                            info.setSelectedCar(1);
                        } else {
                            info.setSelectedCar(0);
                        }
                    }
                }
            }

            if (NdgApp.getInstance().getCarList() != null) {
                adapter = new CarDownloadAdapter(getApplicationContext(), MainActivity.this, NdgApp.getInstance().getCarList());
                adapter.setItemOnClickListener(clickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);
            }
        }
    }

    public void setHeaderData() {
        for (int i = 0; i < NdgApp.getInstance().getCarList().size(); i++) {
            CarInfo info = (CarInfo) NdgApp.getInstance().getCarList().get(i);
            if (("1".equalsIgnoreCase(info.getStatus()))) {
                if (!NdgApp.getInstance().getCarList().contains("Downloaded Car")) {
                    NdgApp.getInstance().getCarList().add(i, "Downloaded Car");
                }
            } else if ("2".equalsIgnoreCase(info.getStatus())) {
                if (!NdgApp.getInstance().getCarList().contains("Previous Car")) {
                    NdgApp.getInstance().getCarList().add(i, "Previous Car");
                }
            } else {
                if (!NdgApp.getInstance().getCarList().contains("Available for Download")) {
                    NdgApp.getInstance().getCarList().add(i, "Available for Download");
                }
            }
        }
    }

    private void showCarDownloadDialog(final int carType) {
        final Dialog dialog = new DialogController(MainActivity.this).carDialog();

        TextView txtViewTitle = (TextView) dialog.findViewById(R.id.txt_title);
        txtViewTitle.setText(getResources().getString(R.string.download_msg));

        ImageButton btnEUR = (ImageButton) dialog.findViewById(R.id.btn_eur);
        ImageButton btnRUS = (ImageButton) dialog.findViewById(R.id.btn_rus);

        ImageButton imgBtnEur = (ImageButton) dialog.findViewById(R.id.img_btn_eur_delete_donwload);
        ImageButton imgBtnRus = (ImageButton) dialog.findViewById(R.id.img_btn_rus_delete_donwload);

        imgBtnEur.setVisibility(View.GONE);
        imgBtnRus.setVisibility(View.GONE);

        if (carType == 1 || carType == 4) {
            if (commonDao.getStatus(getBaseContext(), carType) == 1) {
                btnEUR.setAlpha(0.2f);
                btnEUR.setEnabled(false);
            }

            if (commonDao.getStatus(getBaseContext(), carType + 1) == 1) {
                btnRUS.setAlpha(0.2f);
                btnRUS.setEnabled(false);
            }
        } else if (carType == 2 || carType == 5) {
            if (commonDao.getStatus(getBaseContext(), carType - 1) == 1) {
                btnEUR.setAlpha(0.2f);
                btnEUR.setEnabled(false);
            }
            if (commonDao.getStatus(getBaseContext(), carType) == 1) {
                btnRUS.setAlpha(0.2f);
                btnRUS.setEnabled(false);
            }
        }

        btnEUR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (carType == 1 || carType == 4) {
                    checkingDownloadedCar(carType);
                }
            }
        });

        btnRUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (carType == 2 || carType == 5) {
                    checkingDownloadedCar(carType);

                } else {
                    checkingDownloadedCar(carType + 1);
                }
            }
        });

        dialog.show();
    }

    private void checkingDownloadedCar(int carType) {
        Values.carType = carType;
        preferenceUtil.setSelectedCarType(carType);
        Values.car_path = NdgApp.getInstance().getCarPath(Values.carType);

        commonDao.updateDateAndStatus(getBaseContext(), Values.carType, "1", NdgApp.getInstance().getDateTime(), "EUR");
        if (Values.carType == 1 || Values.carType == 4) {
            if (commonDao.getStatus(getBaseContext(), Values.carType + 1) == 2) {
                commonDao.updateDateAndStatus(getBaseContext(), Values.carType + 1, "2", NdgApp.getInstance().getDateTime(), "EUR");
            }
        }

        NdgApp.getInstance().setCarList(commonDao.getAllOderCarList(this));
        setHeaderData();

        for (int i = 0; i < NdgApp.getInstance().getCarList().size(); i++) {
            if (NdgApp.getInstance().getCarList().get(i).getClass() == CarInfo.class) {
                CarInfo info = (CarInfo) NdgApp.getInstance().getCarList().get(i);
                if (info.getStatus().equalsIgnoreCase("1")) {
                    if (Values.carType == info.getId()) {
                        info.setSelectedCar(1);
//                        info.setSection(true);
                    } else {
                        info.setSelectedCar(0);
//                        info.setSection(false);
                    }
                }
            }
        }
        adapter.setData(NdgApp.getInstance().getCarList());
    }


}
