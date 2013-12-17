package com.ap.mylist.sql;

/**
 * Created by Arick Chow on 12/1/13.
 */
public class Buckets extends ListItem {

    public Buckets(){

    }

    public Buckets(String name){
        super();
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setId(long id){
        this.id = id;
    }

    public void delete(DatabaseHandler db){
        db.deleteBucketById(this.id);
    }

}
