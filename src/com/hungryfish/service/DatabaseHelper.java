package com.hungryfish.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.hungryfish.util.FishType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Breku
 * Date: 07.10.13
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "myDB_hungry_fish";

    // Generic column
    private static final String COLUMN_ID = "ID";

    // Main Table
    private static final String TABLE_MAIN_OPTIONS = "MAIN_OPTIONS_TABLE";
    private static final String COLUMN_OPTION_SOUND = "OPTION_SOUND";
    private static final String COLUMN_OPTION_MONEY = "OPTION_MONEY";


    // Highscores Table
    private static final String TABLE_HIGHSCORES = "HIGHSCORES_TABLE";
    private static final String COLUMN_SCORE = "SCORE";

    // Player options table
    private static final String TABLE_PLAYER_OPTIONS = "PLAYER_OPTIONS_TABLE";
    private static final String COLUMN_FISH_TYPE = "FISH_TYPE";
    private static final String COLUMN_FISH_POWER = "FISH_POWER";
    private static final String COLUMN_FISH_SPEED = "FISH_SPEED";
    private static final String COLUMN_FISH_VALUE = "FISH_VALUE";
    private static final String COLUMN_FISH_LOCKED = "FISH_LOCKED";
    private static final String COLUMN_FISH_PRICE = "FISH_PRICE";


    private static final int DATABASE_VERSION = 17;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when database does not exists
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MAIN_OPTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OPTION_SOUND + " INTEGER, " +
                COLUMN_OPTION_MONEY + " INTEGER" +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PLAYER_OPTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FISH_TYPE + " TEXT, " +
                COLUMN_FISH_POWER + " FLOAT, " +
                COLUMN_FISH_SPEED + " FLOAT, " +
                COLUMN_FISH_VALUE + " FLOAT, " +
                COLUMN_FISH_LOCKED + " INTEGER, " +
                COLUMN_FISH_PRICE + " INTEGER " +
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_HIGHSCORES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCORE + " INTEGER" +
                ")");


        createDefaultDBValues(sqLiteDatabase);
    }


    /**
     * Is called when DATABASE_VERSION is upgraded
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN_OPTIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER_OPTIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);
        onCreate(sqLiteDatabase);
    }


    private void createDefaultDBValues(SQLiteDatabase sqLiteDatabase) {

        // Main options table
        createDefaultMainOptions(sqLiteDatabase);

        createDefaultHighScores(sqLiteDatabase);

        // Player options table
        for (FishType fishType : FishType.values()) {
            if (fishType.getFishLevel() == 0) {
                createDefaultFishRecord(sqLiteDatabase, fishType, 0);
            } else {
                createDefaultFishRecord(sqLiteDatabase, fishType, 1);
            }
        }
    }

    private void createDefaultHighScores(SQLiteDatabase sqLiteDatabase) {
        for (int i = 0; i < 5; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SCORE, 0);
            sqLiteDatabase.insert(TABLE_HIGHSCORES, null, contentValues);
        }

    }

    private void createDefaultMainOptions(SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_OPTION_MONEY, 0);
        contentValues.put(COLUMN_OPTION_SOUND, 1);

        sqLiteDatabase.insert(TABLE_MAIN_OPTIONS, null, contentValues);

    }

    private void createDefaultFishRecord(SQLiteDatabase sqLiteDatabase, FishType fishType, Integer locked) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_FISH_LOCKED, locked);
        contentValues.put(COLUMN_FISH_POWER, fishType.getFishPower());
        contentValues.put(COLUMN_FISH_SPEED, fishType.getFishSpeed());
        contentValues.put(COLUMN_FISH_VALUE, fishType.getFishValue());
        contentValues.put(COLUMN_FISH_TYPE, fishType.name());
        contentValues.put(COLUMN_FISH_PRICE, fishType.getFishPrice());

        sqLiteDatabase.insert(TABLE_PLAYER_OPTIONS, null, contentValues);

    }

    public boolean isFishLocked(FishType fishType) {
        Integer result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_FISH_LOCKED + " FROM " + TABLE_PLAYER_OPTIONS + " WHERE " +
                COLUMN_FISH_TYPE + " = ?", new String[]{fishType.name()});
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result == 1;
    }

    public Integer getFishPrice(FishType fishType) {
        Integer result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_FISH_PRICE + " FROM " + TABLE_PLAYER_OPTIONS + " WHERE " +
                COLUMN_FISH_TYPE + " = ?", new String[]{fishType.name()});
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }


    public float getFishSpeed(FishType fishType) {
        Float result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_FISH_SPEED + " FROM " + TABLE_PLAYER_OPTIONS + " WHERE " +
                COLUMN_FISH_TYPE + " = ?", new String[]{fishType.name()});
        while (cursor.moveToNext()) {
            result = cursor.getFloat(0);
        }
        cursor.close();
        database.close();
        return result;
    }

    public Float getFishPower(FishType fishType) {
        Float result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_FISH_POWER + " FROM " + TABLE_PLAYER_OPTIONS + " WHERE " +
                COLUMN_FISH_TYPE + " = ?", new String[]{fishType.name()});
        while (cursor.moveToNext()) {
            result = cursor.getFloat(0);
        }
        cursor.close();
        database.close();
        return result;

    }

    public Float getFishValue(FishType fishType) {
        Float result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_FISH_VALUE + " FROM " + TABLE_PLAYER_OPTIONS + " WHERE " +
                COLUMN_FISH_TYPE + " = ?", new String[]{fishType.name()});
        while (cursor.moveToNext()) {
            result = cursor.getFloat(0);
        }
        cursor.close();
        database.close();
        return result;

    }

    public void addScore(Integer score) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SCORE, score);
        database.insert(TABLE_HIGHSCORES, null, contentValues);

        database.close();
    }

    public List<Integer> getHighScores() {
        List<Integer> result = new ArrayList<Integer>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_SCORE + " FROM " + TABLE_HIGHSCORES, new String[]{});
        while (cursor.moveToNext()) {
            result.add(cursor.getInt(0));
        }
        cursor.close();
        database.close();
        Collections.sort(result, Collections.reverseOrder());

        return result.subList(0, 5);
    }

    public void updateMoney(Integer points) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + TABLE_MAIN_OPTIONS + "  SET " + COLUMN_OPTION_MONEY + " = " + COLUMN_OPTION_MONEY + " + ?", new String[]{String.valueOf(points)});
        database.close();
    }


    public Integer getMoney() {
        Integer result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_OPTION_MONEY + " FROM " + TABLE_MAIN_OPTIONS, new String[]{});
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result;
    }


    public void increaseSpeedFor(FishType fishType, float speedValue) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + TABLE_PLAYER_OPTIONS + "  SET " + COLUMN_FISH_SPEED + " = " + COLUMN_FISH_SPEED + " + ? WHERE " +
                COLUMN_FISH_TYPE + " = ?", new String[]{String.valueOf(speedValue), fishType.name()});
        database.close();
    }

    public void increaseValueFor(FishType fishType, float fishValue) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + TABLE_PLAYER_OPTIONS + "  SET " + COLUMN_FISH_VALUE + " = " + COLUMN_FISH_VALUE + " + ? WHERE " +
                COLUMN_FISH_TYPE + " = ?", new String[]{String.valueOf(fishValue), fishType.name()});
        database.close();
    }


    public void increasePowerFor(FishType fishType, float fishPower) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + TABLE_PLAYER_OPTIONS + "  SET " + COLUMN_FISH_POWER + " = " + COLUMN_FISH_POWER + " + ? WHERE " +
                COLUMN_FISH_TYPE + " = ?", new String[]{String.valueOf(fishPower), fishType.name()});
        database.close();
    }

    public void unlockFish(FishType fishType) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + TABLE_PLAYER_OPTIONS + "  SET " + COLUMN_FISH_LOCKED + " =  0 " + " WHERE " +
                COLUMN_FISH_TYPE + " = ?", new String[]{fishType.name()});
        database.close();
    }

    public void setMoney(Integer money) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("UPDATE " + TABLE_MAIN_OPTIONS + "  SET " + COLUMN_OPTION_MONEY + " = ?", new String[]{String.valueOf(money)});
        database.close();
    }

}
