package com.apptech.myndg.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apptech.myndg.model.CarInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by nirob on 5/11/18.
 */

public class CommonDao {

    private static CommonDao instance = new CommonDao();

    public CommonDao() {

    }

    public static CommonDao getInstance() {
        return instance;
    }


    public long insertInCarInfoTable(Context context, CarInfo carInfo) {
        ContentValues libraryListValues = prepareMyTableListContentValues(carInfo);
        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        if (db.isOpen()) {
            long newHeadId = db.insert(CarInfoTableEntity.TABLE_NAME, "null", libraryListValues);
            db.close();
            return newHeadId;
        } else {
            return -1;
        }
    }

    public ArrayList<Object> getAllOderCarList(Context context) {
        int downloadCar = 0;

        ArrayList<Object> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        Cursor cursor = db.rawQuery("SELECT * FROM car_info WHERE region = 'EUR' AND status = '1' ORDER BY _id ASC;", null);
        Cursor cursor2 = db.rawQuery("SELECT * FROM car_info WHERE region = 'EUR' AND status = '0' ORDER BY _id ASC;", null);
        Cursor cursor3 = db.rawQuery("SELECT * FROM car_info WHERE region = 'EUR' AND status = '2' ORDER BY _id ASC;", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    CarInfo info = new CarInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), 0);
                    if (info.getId() >= 11) {
                        list.add(0, info);
                    } else {
                        list.add(info);
                    }
                    downloadCar++;
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        try {
            if (cursor2.moveToFirst()) {
                do {
                    CarInfo info = new CarInfo(cursor2.getInt(0), cursor2.getString(1), cursor2.getString(2), cursor2.getString(3), cursor2.getString(4), cursor2.getString(5), 0);
                    if (info.getId() >= 11) {
                        list.add(downloadCar, info);
                    } else {
                        list.add(info);
                    }
                } while (cursor2.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor2 != null) {
                cursor2.close();
            }
        }

        try {
            if (cursor3.moveToFirst()) {
                do {
                    CarInfo info = new CarInfo(cursor3.getInt(0), cursor3.getString(1), cursor3.getString(2), cursor3.getString(3), cursor3.getString(4), cursor3.getString(5), 0);
                    if (info.getId() >= 11) {
                        list.add(downloadCar, info);
                    } else {
                        list.add(info);
                    }
                } while (cursor3.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor3 != null) {
                cursor3.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }

        return list;
    }

    public ArrayList<CarInfo> getAllCarList(Context context) {

        ArrayList<CarInfo> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        Cursor cursor = db.rawQuery("SELECT * FROM car_info WHERE region = 'EUR';", null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    CarInfo info = new CarInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), 0);
                    if (info.getId() >= 11) {
                        list.add(0, info);
                    } else {
                        list.add(info);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
        return list;
    }


    public int getLastID(Context context) {
        int status = 0;
        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        String selectQuery = "SELECT " + "_id" + " FROM " + "car_info" + " order by _id DESC limit 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    status = cursor.getInt(i);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
        return status;
    }


    public int getStatus(Context context, int id) {
        int status = 0;
        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        String selectQuery = "SELECT " + "status" + " FROM " + "car_info" + " WHERE " + "_id" + "=" + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    status = cursor.getInt(i);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
        return status;
    }



    public int updateDateAndStatus(Context context, int id, String status, String dateTime, String region) {
        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        ContentValues values = new ContentValues();
        values.put("status", status);
        values.put("dateTime", dateTime);
        values.put("region", region);
        int result = db.update("car_info", values, "_id" + "=" + id, null);
        if (db.isOpen()) {
            db.close();
        }
        return result;
    }


    public CarInfo getCarInfo(Context context, int carID) {
        CarInfo info = null;

        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        String selectQuery = "SELECT * FROM car_info WHERE _id = " + carID;
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    info = new CarInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), 0);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
        return info;
    }



    public String getLanguageStatus(Context context, int id) {
        String status = "";
        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        String selectQuery = "SELECT " + "language" + " FROM " + "car_info" + " WHERE " + "_id" + "=" + id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    status = cursor.getString(i);
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                if (db.isOpen()) {
                    db.close();
                }
            }
        }
        return status;
    }



    public int updateLanguageStatus(Context context, int id, String lang) {
        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        ContentValues values = new ContentValues();
        values.put("language", lang);
        int result = db.update("car_info", values, "_id" + "=" + id, null);
        if (db.isOpen()) {
            db.close();
        }
        return result;
    }

    public int updateCarStatus(Context context, int id, int status) {
        SQLiteDatabase db = getWritableDatabase(context, DbHelper.class);
        ContentValues values = new ContentValues();
        values.put("status", status);
        int result = db.update("car_info", values, "_id" + "=" + id, null);
        if (db.isOpen()) {
            db.close();
        }
        return result;
    }



    private ContentValues prepareMyTableListContentValues(CarInfo info) {
        ContentValues headValues = new ContentValues(5);
        headValues.put(CarInfoTableEntity.NAME, info.getName());
        headValues.put(CarInfoTableEntity.STATUS, info.getStatus());
        headValues.put(CarInfoTableEntity.DATE_TIME, info.getDateTime());
        headValues.put(CarInfoTableEntity.REGION, info.getRegion());
        headValues.put(CarInfoTableEntity.LANGUAGE, info.getSelectedLanguage());

        return headValues;
    }

    @SuppressWarnings("unchecked")
    private SQLiteDatabase getWritableDatabase(Context context, Class helperClass) {
        try {
            SQLiteOpenHelper helper = (SQLiteOpenHelper) helperClass.getConstructor(Context.class).newInstance(context);
            return helper.getWritableDatabase();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }


}
