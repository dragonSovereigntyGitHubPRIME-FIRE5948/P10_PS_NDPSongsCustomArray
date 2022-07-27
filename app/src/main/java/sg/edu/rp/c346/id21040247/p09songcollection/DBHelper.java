package sg.edu.rp.c346.id21040247.p09songcollection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "song.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SONG_COLLECTION = "songcollection";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_SINGER = "singer";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_STARS = "stars";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* -------------------------------------------------- CRUD: CREATE, READ, UPDATE, DELETE -------------------------------------------------- */

    /** create */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSongCollectionTableSql = "CREATE TABLE " + TABLE_SONG_COLLECTION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_SINGER + " TEXT, "
                + COLUMN_YEAR + " INTEGER, "
                + COLUMN_STARS + " REAL ) ";
        db.execSQL(createSongCollectionTableSql);
    }

    /** upgrade */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG_COLLECTION);
        onCreate(db);
    }

    /** insert */
    public long insertSong(String title, String singer, int year, float stars) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SINGER, singer);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_STARS, stars);
        long result = db.insert(TABLE_SONG_COLLECTION, null, values);
        db.close();
        return result;
    }

    /** update */
    public int updateSong(int id, String title, String singer, int year, float stars){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_SINGER, singer);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_STARS, stars);

        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.update(TABLE_SONG_COLLECTION, values, condition, args);
        db.close();
        return result;
    }

    /** delete  */
    public int deleteSong(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_SONG_COLLECTION, condition, args);
        db.close();
        return result;
    }

    /** Read (show song collection (full list or filtered)) */
    //add array method
    public static String[] add(String[] original, String add) {
        String[] added = new String[original.length+1];

        for(int i = 0; i<original.length; i++){
            added[i] = original[i];
        }
        added[added.length-1] = add;
        return added;
    }

    //show filtered songs method
    public ArrayList<Songs> showSongs(String filterTitle, String filterSinger, String filterYear, String filterStars, int sortID) {

        ArrayList<Songs> filteredSongs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns= {COLUMN_ID, COLUMN_TITLE, COLUMN_SINGER, COLUMN_YEAR, COLUMN_STARS};

        /*
        1. dynamic conditions and args
        2. user can choose to filter any way they want e.g. only title, only singers, both title and singers, etc
        3. method add defined above this method
        4. returns filtered arraylist, unsorted
         */
        String condition = "";
        String[] args = {};

        if (filterTitle.trim().length() > 0) {
            condition = COLUMN_TITLE + " Like ? ";
            args = add(args, filterTitle+"%" );
        }

        if (filterSinger.trim().length() > 0) {
            if(condition != ""){
                condition += "AND ";
            }
            condition += COLUMN_SINGER + " Like ? ";
            args = add(args, filterSinger+"%" );
        }

        if (filterYear.trim().length() > 0) {
            if(condition != ""){
                condition += "AND ";
            }
            condition += COLUMN_YEAR + " Like ? ";
            args = add(args, "%"+filterYear+"%" );
        }

        if (filterStars.trim().length() > 0) {
            if(condition != ""){
                condition += "AND ";
            }
            condition += COLUMN_STARS + " Like ? ";
            args = add(args, "%"+filterStars+"%");
        }

        //show full collection
        if (filterTitle.trim().length() == 0 && filterSinger.trim().length() == 0 && filterYear.trim().length() == 0 && filterStars.trim().length() == 0){
            condition = null;
            args = null;
        }

        //by right i should use Comparator class because show songs returns arraylist but i havent learn and no time
        String order = null;
        switch (sortID){
            case 0: order = null;
                break;
            case 1: order = COLUMN_TITLE+" ASC";
                break;
            case 2: order = COLUMN_TITLE+" DESC";
                break;
            case 3: order = COLUMN_SINGER+" ASC";
                break;
            case 4: order = COLUMN_SINGER+" DESC";
                break;
            case 5: order = COLUMN_YEAR+" ASC";
                break;
            case 6: order = COLUMN_YEAR+" DESC";
                break;
            case 7: order = COLUMN_STARS+" ASC";
                break;
            case 8: order = COLUMN_STARS+" DESC";
                break;
        }

        Cursor cursor = db.query(TABLE_SONG_COLLECTION, columns, condition, args,
                null, null, order, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String singer = cursor.getString(2);
                int year = cursor.getInt(3);
                float stars = cursor.getFloat(4);

                Songs song = new Songs(id, title, singer, year, stars);
                filteredSongs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return filteredSongs;
    }

    /** Read (show song years) */
    //get years for spinner
    /*
    1. before adding to arraylist, checks for duplicate
    2. returns arraylist of song years available for filter
     */
    public ArrayList<String> showYears() {
            ArrayList<String> alYears = new ArrayList<>();
            alYears.add("");
            SQLiteDatabase db = this.getReadableDatabase();

            String[] columns= {COLUMN_YEAR};
            Cursor cursor = db.query(TABLE_SONG_COLLECTION, columns, null, null,
                    null, null, COLUMN_YEAR+" DESC", null);

            if (cursor.moveToFirst()) {
                do {
                    String year = cursor.getString(0);

                    if(!alYears.contains(year)) {
                        alYears.add(year);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return alYears;
        }

    }
