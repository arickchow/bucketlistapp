package com.ap.mylist.sql;

/**
 * Created by Arick Chow on 12/1/13.
 */
public class Items extends ListItem{

    public static final int INPROGRESS = 0;
    public static final int COMPLETE = 1;
    public static final int INCOMPLETE = 2;

    int status;
    long bucket_id;

    public Items(){

    }

    public Items(String name){
        this.name = name;
    }

    public Items(String name, int id, int status){
        super();
        this.name = name;
        this.id = id;
        this.status = status;
    }


    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBucketId(){
        return this.bucket_id;
    }

    public void setBucket_id(long id){
        this.bucket_id = id;
    }

    public void delete(DatabaseHandler db){
        db.deleteItemById(this.id);
    }

}
