package com.example.busapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {

    private static Database sqLiteManager;
    private Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "e-ticket.db";
    public static final String TABLE_NAME = "Locations";
    public static final String COUNTER = "Counter";

    private static final String LONG_FROM_LOCATION = "FromLocation";
    private static final  String LONG_TO_LOCATION = "ToLocation";

    public Database(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
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
}
