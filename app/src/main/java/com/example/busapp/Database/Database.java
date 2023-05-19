package com.example.busapp.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.busapp.LoginActivity;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static Database sqLiteManager;
    private Context context;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "e-ticket.db";
    public static final String LOCATION_TABLE = "Locations";
    public static final String TOKENTABLE = "TokenTable";
    public static final String USERNAME = "Username";
    public static final String POINTSTABLE = "PointsTable";
    public static final String ROUTESTABLE = "RoutesTable";
    public static final String ID = "ID";
    public static final String STARTING_POINT_NAME = "starting_point_name";
    public static final String ENDING_POINTS_NAME = "ending_point_name";
    public static final String FAIR = "fair";
    public static final String DISTANCE = "distance";
    public static final String STARTING_POINT = "starting_point";
    public static final String ENDING_POINT = "ending_point";


    public static final String LOCATIONNAME = "name";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String COUNTER = "Counter";
    private static final String TOKEN = "Token";
    private static final String LONG_FROM_LOCATION = "FromLocation";
    private static final  String LONG_TO_LOCATION = "ToLocation";

    private static final String SHORTLOCATIONCACHE = "CacheTable";
    private static final String SHORTFROMLOCATIONSELECTED = "FromLocationSelected";

    private static final String MACTABLE = "MacTable";

    private static final String MACADDRESS = "Address";

    private static final String PRICECOUNTERTABLE = "PriceTable";
    public static final String SEATNUMBER = "Seat";
    public static final String AMOUNT = "Amount";

    public static final String TICKETSELLSAVEDTABLE = "TicketTable";
    public static final String ROUTEID = "route_id";
    public static final String SEATS = "seats";

    public Database(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        this.context=context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql;

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(LOCATION_TABLE)
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
                .append(SHORTLOCATIONCACHE)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(SHORTFROMLOCATIONSELECTED)
                .append(" TEXT )");

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TOKENTABLE)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(TOKEN)
                .append(" TEXT, ")
                .append(USERNAME)
                .append(" TEXT )");

        db.execSQL(sql.toString());
        Log.d("********", "onCreate: "+"db created");

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(POINTSTABLE)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(LOCATIONNAME)
                .append(" TEXT, ")
                .append(LATITUDE)
                .append(" TEXT, ")
                .append(LONGITUDE)
                .append(" TEXT )")
        ;

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(ROUTESTABLE)
                .append("(")
                .append(ID)
                .append(" INTEGER PRIMARY KEY , ")
                .append(STARTING_POINT_NAME)
                .append(" TEXT, ")
                .append(ENDING_POINTS_NAME)
                .append(" TEXT, ")
                .append(FAIR)
                .append(" REAL, ")
                .append(DISTANCE)
                .append(" REAL, ")
                .append(STARTING_POINT)
                .append(" INTEGER, ")
                .append(ENDING_POINT)
                .append(" INTEGER )")
        ;

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(MACTABLE)
                .append("(")
                .append(ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(MACADDRESS)
                .append(" TEXT ) ")

        ;

        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(PRICECOUNTERTABLE)
                .append("(")
                .append(ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(SEATNUMBER)
                .append(" INTEGER , ")
                .append(AMOUNT)
                .append(" INTEGER )")

        ;
        db.execSQL(sql.toString());

        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TICKETSELLSAVEDTABLE)
                .append("(")
                .append(ID)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ROUTEID)
                .append(" INTEGER , ")
                .append(SEATS)
                .append(" INTEGER )");

        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS "+ LOCATION_TABLE);
            onCreate(db);

    }


    // ************** Price Counter Table
    @SuppressLint("Range")
    public ArrayList<Integer> getPricesAndSeats(){
        SQLiteDatabase db = this.getReadableDatabase();

        int TotalSeat=0;
        int TotalAmount=0;
        ArrayList<Integer> integerList = new ArrayList<>();


        String query = "SELECT * FROM "+ PRICECOUNTERTABLE +" WHERE "+ID+" = 1";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            TotalSeat = cursor.getInt(cursor.getColumnIndex(SEATNUMBER));
            TotalAmount = cursor.getInt(cursor.getColumnIndex(AMOUNT));

            integerList.add(TotalSeat);
            integerList.add(TotalAmount);

            // Do something with the token value
        }
        cursor.close();
        return integerList;
    }

    public boolean doesPriceTableExist(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ PRICECOUNTERTABLE ;

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int TotalSeats = cursor.getInt(cursor.getColumnIndex(SEATNUMBER));
            return true;
        }
        else return false;
    }

    public void setPriceTable(int seats, int amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SEATNUMBER, seats);
        cv.put(AMOUNT, amount);

        String whereClause = "ID = ?";
        String[] whereArgs = {String.valueOf(1)};
        long result =  db.update(PRICECOUNTERTABLE,cv,whereClause,whereArgs);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void newPriceTable(int seats, int amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SEATNUMBER, seats);
        cv.put(AMOUNT, amount);

        long result =  db.insert(PRICECOUNTERTABLE,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }
    }

    // ************** Saved Ticket Sell Table *****************
    @SuppressLint("Range")
    public Cursor getSoldTickets(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> integerList = new ArrayList<>();
        String query = "SELECT * FROM "+ TICKETSELLSAVEDTABLE ;

        Cursor cursor = db.rawQuery(query,null);

        return cursor;
    }

    public void deleteAllRecordsFromTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableName = TICKETSELLSAVEDTABLE;
        db.delete(tableName, null, null);
        db.close();
    }

    public boolean doesTicketSoldTableExist(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TICKETSELLSAVEDTABLE ;

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") int Seats = cursor.getInt(cursor.getColumnIndex(SEATS));
            return true;
        }
        else return false;
    }



    public void newTicketSold(int route_id, int seat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ROUTEID, route_id);
        cv.put(SEATS, seat);

        long result =  db.insert(TICKETSELLSAVEDTABLE,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }
    }



    // ************** MAC table ***************
    @SuppressLint("Range")
    public String getMacAddress(){
        SQLiteDatabase db = this.getReadableDatabase();

        String address=null;
        String query = "SELECT "+ MACADDRESS +" FROM "+ MACTABLE +" WHERE "+ID+" = 1";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            address = cursor.getString(cursor.getColumnIndex(MACADDRESS));
            // Do something with the token value
        }
        cursor.close();
        return address;
    }
    public boolean doesMacExist(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ MACTABLE ;

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            String loc = cursor.getString(cursor.getColumnIndex(MACADDRESS));
            return true;
        }
        else return false;
    }

    public void setMacAddress(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MACADDRESS, address);

        String whereClause = "ID = ?";
        String[] whereArgs = {String.valueOf(1)};
        long result =  db.update(MACTABLE,cv,whereClause,whereArgs);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void newMacAddress(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MACADDRESS, address);

        long result =  db.insert(MACTABLE,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }
    }



    // ************** Short Location Cache ***************
    public String getShortLocationCache(){
        SQLiteDatabase db = this.getReadableDatabase();

        String location=null;
        String query = "SELECT "+ SHORTFROMLOCATIONSELECTED +" FROM "+ SHORTLOCATIONCACHE +" WHERE "+COUNTER+" = 1";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            location = cursor.getString(cursor.getColumnIndex(SHORTFROMLOCATIONSELECTED));
            // Do something with the token value
        }
        cursor.close();
        return location;
    }
    public boolean doesCacheExist(){
        SQLiteDatabase db = this.getReadableDatabase();



        String query = "SELECT * FROM "+ SHORTLOCATIONCACHE ;

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            String loc = cursor.getString(cursor.getColumnIndex("FromLocationSelected"));
            return true;
        }
        else return false;
    }

    public void setShortLocationCache(String locationName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SHORTFROMLOCATIONSELECTED, locationName);

        String whereClause = "Counter = ?";
        String[] whereArgs = {String.valueOf(1)};
        long result =  db.update(SHORTLOCATIONCACHE,cv,whereClause,whereArgs);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void newShortLocationCache(String locationName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SHORTFROMLOCATIONSELECTED, locationName);



        long result =  db.insert(SHORTLOCATIONCACHE,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }
    }


    // ************** Routes Table ***************
    public Cursor returnEndingLocation(String name){
        SQLiteDatabase db = this.getReadableDatabase();

       String query = "SELECT * FROM "+ ROUTESTABLE +" WHERE starting_point_name = '"+ name+"'";

        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public boolean doesRouteExist(int Id){
        SQLiteDatabase db = this.getReadableDatabase();



        String query = "SELECT * FROM "+ ROUTESTABLE +" WHERE ID = '"+ Id+"'";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            String theName = cursor.getString(cursor.getColumnIndex("starting_point_name"));

            return true;
        }
        else return false;
    }

    public void addNewRoutes(int id , String starting_point_name, String ending_point_name, double fair, double distance, int starting_point, int ending_point){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, id);
        contentValues.put(STARTING_POINT_NAME, starting_point_name);
        contentValues.put(ENDING_POINTS_NAME, ending_point_name);
        contentValues.put(FAIR, fair);
        contentValues.put(DISTANCE, distance);
        contentValues.put(STARTING_POINT, starting_point);
        contentValues.put(ENDING_POINT, ending_point);

        long result = db.insert(ROUTESTABLE,null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }

    }


    // ************** Points Table ***************
    public Cursor getPoints(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ POINTSTABLE ;
        Cursor cursor = db.rawQuery(query,null);

        return cursor;
    }

    public boolean doesPointExist(String name){
        SQLiteDatabase db = this.getReadableDatabase();



        String query = "SELECT * FROM "+ POINTSTABLE +" WHERE name = '"+ name+"'";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
          String theName = cursor.getString(cursor.getColumnIndex("name"));
            return true;
        }
        else return false;
    }
    public void addNewPoints(String name, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(LOCATIONNAME, name);
        contentValues.put(LATITUDE, latitude);
        contentValues.put(LONGITUDE, longitude);

        long result = db.insert(POINTSTABLE,null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }


    // ************** Locations Table ***************
    public void addNewLongLocation(String from_location, String to_location){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(LONG_FROM_LOCATION, from_location);
        contentValues.put(LONG_TO_LOCATION, to_location);

        long result = db.insert(LOCATION_TABLE,null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Location Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getLocations(Database db){
        SQLiteDatabase SQ = db.getReadableDatabase();
        String query = "SELECT * FROM "+ LOCATION_TABLE +" WHERE ID = 1";

        String[]columns = {LONG_FROM_LOCATION, LONG_TO_LOCATION};
        Cursor cursor = SQ.query(LOCATION_TABLE, columns, null,null,null, null, null);

        return cursor;
    }
    public boolean IsTableEmpty(Database table){
        SQLiteDatabase db = table.getWritableDatabase();
        String count = "SELECT count(*) FROM "+ LOCATION_TABLE;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0){

            return false;
        }

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
        long result =  db.update(LOCATION_TABLE,cv,whereClause,whereArgs);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }
    }




    // ************** Token Table ***************
    public void addNewToken(String token, String username){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(TOKEN, token);
        contentValues.put(USERNAME, username);

        long result = db.insert(TOKENTABLE,null, contentValues);
        if(result == -1){
            Toast.makeText(context, "Failed to save token", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean IsTokenTableEmpty(Database table){
        SQLiteDatabase db = table.getWritableDatabase();
        String count = "SELECT count(*) FROM "+ TOKENTABLE;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0)
            //leave
            return false;
        else
            return true;
    }
    public void UpdateToken(Database table, String token, String username){
        SQLiteDatabase db = table.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TOKEN, token);
        cv.put(USERNAME, username);
        String whereClause = "Counter = ?";
        String[] whereArgs = {String.valueOf(1)};
        long result =  db.update(TOKENTABLE,cv,whereClause,whereArgs);
        if(result == -1){
            Toast.makeText(context, "Failed :(", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Location Updated :)", Toast.LENGTH_SHORT).show();
        }
    }

    public String GetToken(Database db){
        SQLiteDatabase SQ = db.getReadableDatabase();
        String token=null;
        String query = "SELECT "+ TOKEN +" FROM "+ TOKENTABLE +" WHERE "+COUNTER+" = 1";

        Cursor cursor = SQ.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            token = cursor.getString(cursor.getColumnIndex("Token"));
            // Do something with the token value
        }
        cursor.close();
        return token;
    }

    public String GetUsername(Database db){
        SQLiteDatabase SQ = db.getReadableDatabase();
        String username=null;
        String query = "SELECT "+ USERNAME +" FROM "+ TOKENTABLE +" WHERE "+COUNTER+" = 1";

        Cursor cursor = SQ.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndex(USERNAME));
            // Do something with the token value
        }
        cursor.close();
        return username;
    }

    public boolean DeleteToken(Database db){SQLiteDatabase SQ = db.getWritableDatabase();

        // Get the first row in the table
        Cursor cursor = SQ.rawQuery("SELECT * FROM TOKENTABLE ORDER BY COUNTER LIMIT 1", null);

        // Check if there is a row
        if (cursor.moveToFirst()) {
            // Get the ID of the first row
            int id = cursor.getInt(cursor.getColumnIndex("Counter"));

            // Delete the row
            SQ.delete(TOKENTABLE, "Counter = ?", new String[] { String.valueOf(id) });

            return true;
        }

        return false;
    }


}
