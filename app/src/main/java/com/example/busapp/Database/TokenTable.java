package com.example.busapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class TokenTable extends SQLiteOpenHelper {

    private static TokenTable sqLiteManager;
    private Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "e-ticket.db";
    public static final String TABLE_NAME = "TokenTable";
    public static final String COUNTER = "Counter";

    private static final String TOKEN = "Token";


    public TokenTable(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
        this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql;

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
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

    public void addNewToken(String token){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TOKEN, token);


        long result = db.insert(TABLE_NAME,null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to save token", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Location Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean IsTableEmpty(TokenTable table){
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
    public void UpdateToken(TokenTable table, String token){
        SQLiteDatabase db = table.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TOKEN, token);

        String whereClause = "Counter = ?";
        String[] whereArgs = {String.valueOf(1)};
        long result =  db.update(TABLE_NAME,cv,whereClause,whereArgs);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Location Updated :)", Toast.LENGTH_SHORT).show();
        }
    }
}
