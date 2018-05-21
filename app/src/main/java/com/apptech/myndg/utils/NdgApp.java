package com.apptech.myndg.utils;

import android.widget.ImageView;

import com.apptech.myndg.R;
import com.apptech.myndg.model.CarInfo;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by nirob on 5/11/18.
 */

public class NdgApp {

    public static NdgApp instance;

    public static NdgApp getInstance() {
        if (instance == null) {
            instance = new NdgApp();
        }
        return instance;
    }

    private ArrayList<Object> carList;
    private ArrayList<CarInfo> carAllList;

    public ArrayList<Object> getCarList() {
        return carList;
    }

    public void setCarList(ArrayList<Object> carList) {
        this.carList = carList;
    }

    public ArrayList<CarInfo> getCarAllList() {
        return carAllList;
    }

    public void setCarAllList(ArrayList<CarInfo> carAllList) {
        this.carAllList = carAllList;
    }


    public String getDateTime() {
        return DateFormat.getDateTimeInstance().format(new Date());
    }



    public void setCarImage(int position, ImageView imageView) {
        switch (position) {
            case 1:
            case 2:
                imageView.setBackgroundResource(R.drawable.qashqai_bnw);
                break;

            case 3:
                imageView.setBackgroundResource(R.drawable.juke);
                break;

            case 4:
            case 5:
                imageView.setBackgroundResource(R.drawable.xtrail_bnw);
                break;

            case 6:
                imageView.setBackgroundResource(R.drawable.pulsar);
                break;

            case 7:
                imageView.setBackgroundResource(R.drawable.micra_bnw);
                break;

            case 8:
                imageView.setBackgroundResource(R.drawable.note);
                break;

            case 9:
                imageView.setBackgroundResource(R.drawable.leaf_bnw);
                break;

            case 10:
                imageView.setBackgroundResource(R.drawable.navara);
                break;

            case 11:
                imageView.setBackgroundResource(R.drawable.micra_k14);
                break;

            case 12:
                imageView.setBackgroundResource(R.drawable.qashqai_2017_download);
                break;

            case 13:
                imageView.setBackgroundResource(R.drawable.xtrail_2017_download);
                break;

            case 14:
                imageView.setBackgroundResource(R.drawable.leaf_2017_download);
                break;

            default:
                break;
        }
    }


    public void setPreviousCarImage(int position, ImageView imageView) {
        switch (position) {
            case 1:
            case 2:
                imageView.setBackgroundResource(R.drawable.qashqai_bnw);
                break;

            case 4:
            case 5:
                imageView.setBackgroundResource(R.drawable.xtrail_bnw);
                break;

            case 7:
                imageView.setBackgroundResource(R.drawable.micra_bnw);
                break;

            case 9:
                imageView.setBackgroundResource(R.drawable.leaf_bnw);
                break;

            default:
                break;
        }
    }

    public boolean isFileExists(String path) {
        if (new File(path).exists()) {
            return true;
        } else {
            return false;
        }
    }



    public String getCarPath(int carType) {
        String carPath = "";
        switch (carType) {
            case 1:
                carPath = Values.PATH + Values.qashqai_folder;
                break;

            case 2:
                carPath = Values.PATH + Values.qashqai_folder_rus;
                break;

            case 3:
                carPath = Values.PATH + Values.juke_folder;
                break;

            case 4:
                carPath = Values.PATH + Values.xtrail_folder;
                break;

            case 5:
                carPath = Values.PATH + Values.xtrail_folder_rus;
                break;

            case 6:
                carPath = Values.PATH + Values.pulsar_folder;
                break;

            case 7:
                carPath = Values.PATH + Values.micra_folder;
                break;

            case 8:
                carPath = Values.PATH + Values.note_folder;
                break;

            case 9:
                carPath = Values.PATH + Values.leaf_folder;
                break;

            case 10:
                carPath = Values.PATH + Values.navara_folder;
                break;

            case 11:
                carPath = Values.PATH + Values.micra_new_folder;
                break;

            case 12:
                carPath = Values.PATH + Values.qashqai_2017;
                break;

            default:
                break;
        }

        return carPath;
    }

}
