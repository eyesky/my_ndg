package com.apptech.myndg.database;

import android.provider.BaseColumns;

/**
 * Created by nirob on 5/11/18.
 */

public class CarInfoTableEntity implements BaseColumns {

    public static final String TABLE_NAME = "car_info";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String DATE_TIME = "dateTime";
    public static final String REGION = "region";
    public static final String LANGUAGE = "language";

    public CarInfoTableEntity() {

    }
}
