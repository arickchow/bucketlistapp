package com.ap.mylist.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Arick Chow on 12/1/13.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler sInstance = null;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Buckets";
    private static final String LOG = "DatabaseHelper";

    //Table Names
    private static final String TABLE_BUCKETS = "buckets";
    private static final String TABLE_ITEMS = "items";
    private static final String TABLE_BUCKET_ITEMS = "bucket_items";

    //Bucket Columns
    private static final String KEY_ID = "id";
    private static final String KEY_BUCKET_NAME ="bucket_name";

    //Item Columns
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_ITEM_STATUS = "status";

    //bucket_items Columns
    private static final String KEY_BUCKET_ID = "bucket_id";
    private static final String KEY_ITEM_ID = "item_id";

    //Creation Statements
    private static final String CREATE_BUCKETS = "CREATE TABLE %s(%s INTEGER PRIMARY KEY,%s TEXT)";
    private static final String CREATE_TABLE_BUCKETS = String.format(CREATE_BUCKETS,TABLE_BUCKETS,KEY_ID,KEY_BUCKET_NAME);

    private static final String CREATE_ITEMS = "CREATE TABLE %s(%s INTEGER PRIMARY KEY,%s TEXT, %s INTEGER, %s INTEGER)";
    private static final String CREATE_TABLE_ITEMS = String.format(CREATE_ITEMS,TABLE_ITEMS,KEY_ID,KEY_ITEM_NAME,KEY_ITEM_STATUS,KEY_BUCKET_ID);

    private static final String CREATE_BUCKET_ITEM = "CREATE TABLE %s(%s INTEGER PRIMARY KEY,%s INTEGER, %s INTEGER";
    private static final String CREATE_TABLE_BUCKET_ITEM = String.format(CREATE_BUCKET_ITEM,TABLE_BUCKET_ITEMS,KEY_ID,KEY_BUCKET_ID,KEY_ITEM_ID);

    public DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    public static DatabaseHandler getInstance(Context context){
        if(sInstance == null)
            sInstance = new DatabaseHandler(context.getApplicationContext());
        return sInstance;
    }

    public void onCreate(SQLiteDatabase db){
        Log.e(LOG,CREATE_TABLE_BUCKETS);
        db.execSQL(CREATE_TABLE_BUCKETS);
        db.execSQL(CREATE_TABLE_ITEMS);
        //db.execSQL(CREATE_TABLE_BUCKET_ITEM);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUCKETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUCKET_ITEMS);

        onCreate(db);
    }

    /*
    * Creation and Selection Methods for TABLE_BUCKET
    *
    * */
    public Buckets createBucket(Buckets bucket){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BUCKET_NAME,bucket.getName());

        long bucket_id = db.insert(TABLE_BUCKETS,null,values);
        bucket.setId(bucket_id);
        return bucket;
    }

    public void deleteBucketById(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("DELETE FROM %s WHERE %s=%d",TABLE_BUCKETS,KEY_ID,id);
        deleteItemByBucketId(id);
        db.execSQL(query);
    }

    public ArrayList<ListItem> getAllBuckets(){
        ArrayList<ListItem> buckets = new ArrayList<ListItem>();
        String query = String.format("SELECT * FROM %s",TABLE_BUCKETS);

        Log.e("DEBUG",query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query,null);

        if(c.moveToFirst()){
            do{
                Buckets bucket = new Buckets();
                bucket.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                bucket.setName(c.getString(c.getColumnIndex(KEY_BUCKET_NAME)));
                buckets.add(bucket);
            } while(c.moveToNext());
        }
        return buckets;
    }

    /*
    Creation/deletion and selection Methods for ITEMS
     */
    public Items createItem(Items item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(KEY_ITEM_NAME,item.getName());
        value.put(KEY_ITEM_STATUS,item.getStatus());
        value.put(KEY_BUCKET_ID,item.getBucketId());

        long item_id = db.insert(TABLE_ITEMS,null,value);
        item.setId(item_id);
        return item;
    }

    public void deleteItemById(long id){
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("DELETE FROM %s WHERE id=%d",TABLE_ITEMS,id);
        Log.e("DEBUG",query);
        db.execSQL(query);
    }

    public void deleteItemByBucketId(long id){
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("DELETE FROM %s WHERE %s=%d",TABLE_ITEMS,KEY_BUCKET_ID,id);
        Log.e("DEBUG",query);
        db.execSQL(query);
    }

    public ArrayList<ListItem> getItemByBucketId(long id){
        ArrayList<ListItem> items = new ArrayList<ListItem>();
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s=%d",TABLE_ITEMS,KEY_BUCKET_ID,id);
        Log.e("DEBUG",query);
        Cursor c = db.rawQuery(query,null);

        if(c.moveToFirst()){
            do{
                Items item = new Items();
                item.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                item.setName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
                item.setStatus(c.getInt(c.getColumnIndex(KEY_ITEM_STATUS)));
                item.setBucket_id(c.getInt(c.getColumnIndex(KEY_BUCKET_ID)));
                items.add(item);
            } while(c.moveToNext());
        }
        return items;
    }

    public ArrayList<ListItem> getItemByBucketAndStatusId(long id, long status){
        ArrayList<ListItem> items = new ArrayList<ListItem>();
        SQLiteDatabase db = getReadableDatabase();

        String query = String.format("SELECT * FROM %s WHERE %s=%d AND %s=%d",TABLE_ITEMS,KEY_BUCKET_ID,id,KEY_ITEM_STATUS,status);
        Log.e("DEBUG",query);
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                Items item = new Items();
                item.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                item.setName(c.getString(c.getColumnIndex(KEY_ITEM_NAME)));
                item.setStatus(c.getInt(c.getColumnIndex(KEY_ITEM_STATUS)));
                item.setBucket_id(c.getInt(c.getColumnIndex(KEY_BUCKET_ID)));
                items.add(item);
            } while(c.moveToNext());
        }
        return items;
    }

    //close db connection
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen())
            db.close();
    }

}
