package mseron.worktimelogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Martial Séron <martial.seron@gmail.com>
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "WorkTimeLogger";

    // Contacts table name
    private static final String TABLE_CLOCKS = Clock.TABLE_NAME;

    // Contacts Table Columns names
    private static final String COL_ID              = Clock.COL_ID;
    private static final String COL_NAME            = Clock.COL_NAME;
    private static final String COL_START_DATE      = Clock.COL_START_DATE;
    private static final String COL_END_DATE        = Clock.COL_END_DATE;
    private static final String COL_EVENT_TYPE_ID   = Clock.COL_EVENT_TYPE_ID;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CLOCK_TABLE = "CREATE TABLE " + TABLE_CLOCKS + "("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_NAME + " TEXT,"
                + COL_START_DATE + " DATETIME,"
                + COL_END_DATE + " DATETIME, "
                + COL_EVENT_TYPE_ID + " INTEGER"
                + ")";
        sqLiteDatabase.execSQL(CREATE_CLOCK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CLOCKS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public long addClock(Clock clock){
        Log.d("DatabaseHandler", "addClock");
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String name = clock.get_name();
        String startDate =  sdf.format(clock.get_startDate());
        String endDate = (clock.get_endDate() == null) ? "0000-00-00 00:00:00" : sdf.format(clock.get_endDate());
        String eventTypeId = String.valueOf(clock.get_eventTypeId());

        Log.d("Value of COL_NAME",name);
        Log.d("Value of COL_START_DATE",startDate);
        Log.d("Value of COL_END_DATE",endDate);
        Log.d("Value of COL_EVENT_TYPE_ID",eventTypeId);

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_START_DATE, startDate);
        values.put(COL_END_DATE, endDate);
        values.put(COL_EVENT_TYPE_ID, eventTypeId);

        long id = db.insert(TABLE_CLOCKS, null, values);
        db.close();

        return id;
    }

    public Clock getClock(int id) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CLOCKS,
                new String[] { COL_ID, COL_NAME, COL_START_DATE, COL_END_DATE, COL_EVENT_TYPE_ID },
                COL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null
        );
        if (cursor != null)
            cursor.moveToFirst();

        Long clockId            = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)));
        String clockName        = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
        Integer clockEventType  = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_TYPE_ID)));
        Date clockStartDate     = new Date();
        Date clockEndDate       = new Date();
        try {
            clockStartDate  = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_START_DATE)));
            clockEndDate    = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_END_DATE)));
        } catch(ParseException e) {
            Log.e("Exception", "Parsing ISO8601 datetime failed", e);
        }

        Clock clock = new Clock(clockId, clockName, clockStartDate, clockEndDate, clockEventType );
        // return clock
        return clock;
    }

    // Getting All Clocks
    public List<Clock> getAllClocks() {
        List<Clock> clockList = new ArrayList<Clock>();
        // Select All Query
        String selectQuery = "SELECT "
                + Clock.COL_ID + ", "
                + Clock.COL_NAME + ", "
                + Clock.COL_START_DATE + ", "
                + Clock.COL_END_DATE + ", "
                + Clock.COL_EVENT_TYPE_ID + " "
                + "FROM " + TABLE_CLOCKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);



        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Long clockId            = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(COL_ID)));
                String clockName        = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                Integer clockEventType  = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COL_EVENT_TYPE_ID)));
                Date clockStartDate     = new Date();
                Date clockEndDate       = new Date();
                try {
                    clockStartDate  = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_START_DATE)));
                    clockEndDate    = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow(COL_END_DATE)));
                } catch(ParseException e) {
                    Log.e("Exception", "Parsing ISO8601 datetime failed", e);
                }

                Clock clock = new Clock(clockId, clockName, clockStartDate, clockEndDate, clockEventType );
                // Adding contact to list
                clockList.add(clock);
            } while (cursor.moveToNext());
        }

        // return contact list
        return clockList;
    }

    // Updating single clock
    public int updateClock(Clock clock) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ContentValues values = new ContentValues();
        values.put(COL_NAME, clock.get_name());
        values.put(COL_START_DATE, sdf.format(clock.get_startDate()));
        values.put(COL_END_DATE, sdf.format(clock.get_endDate()));
        values.put(COL_EVENT_TYPE_ID, clock.get_eventTypeId());

        // updating row
        return db.update(TABLE_CLOCKS, values, COL_ID + " = ?",
                new String[] { String.valueOf(clock.get_id()) });
    }

    // Deleting single clock
    public void deleteClock(Clock clock) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CLOCKS, COL_ID + " = ?",
                new String[] { String.valueOf(clock.get_id()) });
        db.close();
    }


    // Getting clocks Count
    public int getClocksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CLOCKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
