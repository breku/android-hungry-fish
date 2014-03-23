package com.hungryfish.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.hungryfish.util.FishType;

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
    private static final String COLUMN_RECORD = "RECORD";


    // Player options table
    private static final String TABLE_PLAYER_OPTIONS = "PLAYER_OPTIONS_TABLE";
    private static final String COLUMN_FISH_TYPE = "FISH_TYPE";
    private static final String COLUMN_FISH_POWER = "FISH_POWER";
    private static final String COLUMN_FISH_SPEED = "FISH_SPEED";
    private static final String COLUMN_FISH_VALUE = "FISH_VALUE";
    private static final String COLUMN_FISH_LOCKED = "FISH_LOCKED";

    private static final int DATABASE_VERSION = 2;


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
                COLUMN_OPTION_MONEY + " INTEGER, "+
                COLUMN_RECORD + " INTEGER"+
                ")");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PLAYER_OPTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FISH_TYPE + " TEXT, " +
                COLUMN_FISH_POWER + " INTEGER, "+
                COLUMN_FISH_SPEED + " FLOAT, "+
                COLUMN_FISH_VALUE  + " INTEGER, "+
                COLUMN_FISH_LOCKED  + " INTEGER"+
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
        onCreate(sqLiteDatabase);
    }


    private void createDefaultDBValues(SQLiteDatabase sqLiteDatabase) {

        // Main options table
        createDefaultMainOptions(sqLiteDatabase);


        // Player options table
        for (FishType fishType : FishType.values()) {
            if(fishType.ordinal() == 0){
                createDefaultFishRecord(sqLiteDatabase,fishType,0);
            }else {
                createDefaultFishRecord(sqLiteDatabase,fishType,1);
            }
        }
    }

    private void createDefaultMainOptions(SQLiteDatabase sqLiteDatabase) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_OPTION_MONEY, 0);
        contentValues.put(COLUMN_OPTION_SOUND, 1);
        contentValues.put(COLUMN_RECORD, 0);

        sqLiteDatabase.insert(TABLE_MAIN_OPTIONS, null, contentValues);

    }

    private void createDefaultFishRecord(SQLiteDatabase sqLiteDatabase, FishType fishType, Integer locked) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_FISH_LOCKED, locked);
        contentValues.put(COLUMN_FISH_POWER, fishType.getFishPower());
        contentValues.put(COLUMN_FISH_SPEED, fishType.getFishSpeed());
        contentValues.put(COLUMN_FISH_VALUE, fishType.getFishValue());
        contentValues.put(COLUMN_FISH_TYPE, fishType.name());

        sqLiteDatabase.insert(TABLE_PLAYER_OPTIONS, null, contentValues);

    }

    public boolean isFishLocked(FishType fishType) {
        Integer result = null;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + COLUMN_FISH_LOCKED+ " FROM " + TABLE_PLAYER_OPTIONS+ " WHERE " +
                COLUMN_FISH_TYPE + " = ?",new String[]{fishType.name()});
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        database.close();
        return result == 1;
    }
}
