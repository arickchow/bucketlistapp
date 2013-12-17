package com.ap.mylist.sql;

/**
 * Created by Arick Chow on 12/1/13.
 */
public interface ListItemInterface {

    String getName();
    long getId();
    void delete(DatabaseHandler db);
}
