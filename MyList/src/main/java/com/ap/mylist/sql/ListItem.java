package com.ap.mylist.sql;

/**
 * Created by Arick Chow on 12/1/13.
 */
public class ListItem implements ListItemInterface{

    long id;
    String name;

    public ListItem(){

    }
    public void delete(DatabaseHandler db){}

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String toString(){
        return name;
    }
}
