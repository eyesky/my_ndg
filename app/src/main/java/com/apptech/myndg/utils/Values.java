package com.apptech.myndg.utils;

import android.os.Environment;

/**
 * Created by nirob on 5/11/18.
 */

public class Values {

    public static final String PATH = Environment.getExternalStorageDirectory() + "/.AllDriverGuide";

    public static int carType = 0;

    public static String car_path = "";

    //Folder Name of cars in sdcard
    public static final String qashqai_folder = "/qashqai";
    public static final String qashqai_folder_rus = "/qashqai_rus";
    public static final String juke_folder = "/juke";
    public static final String xtrail_folder = "/xtrail";
    public static final String xtrail_folder_rus = "/xtrail_rus";
    public static final String pulsar_folder = "/pulsar";
    public static final String micra_folder = "/micra";
    public static final String note_folder = "/note";
    public static final String leaf_folder = "/leaf";
    public static final String navara_folder = "/navara";
    public static final String micra_new_folder = "/micrak14";
    public static final String qashqai_2017 = "/qashqai2017";

    public static final String AVAILABLE_FOR_DOWNLOAD = "0";
    public static final String ALREADY_DOWNLOADED = "1";
    public static final String PREVIOUS_CAR = "2";

}
