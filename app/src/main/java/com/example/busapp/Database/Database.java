package com.example.busapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {

    private static Database sqLiteManager;
    private Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "e-ticket.db";
    public static final String TABLE_NAME = "Locations";
    public static final String TABLE_NAME2 = "TokenTable";
    public static final String COUNTER = "Counter";
    private static final String TOKEN = "Token";
    private static final String LONG_FROM_LOCATION = "FromLocation";
    private static final  String LONG_TO_LOCATION = "ToLocation";

    public Database(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        this.context=context;
    }

    // creates a single instance of database object
  /*  public static Database instanceOfDatabase(Context context){
        if(sqLiteManager == null)
            sqLiteManager = new Database(context);

        return sqLiteManager;
    } */

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql;

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(LONG_FROM_LOCATION)
                .append(" TEXT, ")
                .append(LONG_TO_LOCATION)
                .append(" TEXT )");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME2)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(TOKEN)
                .append(" TEXT )");

        db.execSQL(sql.toString());
        Log.d("********", "onCreate: "+"db created");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            onCreate(db);

    }

    public void addNewLongLocation(String from_location, String to_location){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(LONG_FROM_LOCATION, from_location);
        contentValues.put(LONG_TO_LOCATION, to_location);

        long result = db.insert(TABLE_NAME,null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Location Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getLocations(Database db){
        SQLiteDatabase SQ = db.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME +" WHERE ID = 1";

        String[]columns = {LONG_FROM_LOCATION, LONG_TO_LOCATION};
        Cursor cursor = SQ.query(TABLE_NAME, columns, null,null,null, null, null);

        return cursor;
    }
    public boolean IsTableEmpty(Database table){
        SQLiteDatabase db = table.getWritableDatabase();
        String count = "SELECT count(*) FROM "+TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0)
            //leave
            return false;
        else
            return true;
    }
    public void UpdateLocations(Database table, String fromLoc, String toLoc){
        SQLiteDatabase db = table.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LONG_FROM_LOCATION, fromLoc);
        cv.put(LONG_TO_LOCATION, toLoc);
        String whereClause = "Counter = ?";
        String[] whereArgs = {String.valueOf(1)};
        long result =  db.update(TABLE_NAME,cv,whereClause,whereArgs);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Location Updated :)", Toast.LENGTH_SHORT).show();
        }
    }


    public void addNewToken(String token){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TOKEN, token);


        long result = db.insert(TABLE_NAME2,null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to save token", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Location Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean IsTokenTableEmpty(Database table){
        SQLiteDatabase db = table.getWritableDatabase();
        String count = "SELECT count(*) FROM "+TABLE_NAME2;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0)
            //leave
            return false;
        else
            return true;
    }
    public void UpdateToken(Database table, String token){
        SQLiteDatabase db = table.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TOKEN, token);

        String whereClause = "Counter = ?";
        String[] whereArgs = {String.valueOf(1)};
        long result =  db.update(TABLE_NAME2,cv,whereClause,whereArgs);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Location Updated :)", Toast.LENGTH_SHORT).show();
        }
    }

}
