package com.ap.mylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.mylist.sql.DatabaseHandler;

import java.util.ArrayList;
import com.ap.mylist.sql.ListItem;


/**
 * Created by Arick Chow on 11/27/13.
 */
public class ItemListArrayAdapter<T> extends ArrayAdapter<ListItem> {

    private final Context context;
    private ArrayList<ListItem> content = new ArrayList<ListItem>();
    private int mDelete = View.INVISIBLE;
    private DatabaseHandler db;

    public ItemListArrayAdapter(Context context, int resource,int textViewResource, ArrayList<ListItem> objects , DatabaseHandler db){
        super(context,resource,textViewResource,objects);
        this.content = objects;
        this.context = context;
        this.db = db;
    }

    public void deleteToggle(boolean deleteFlag){
        mDelete = deleteFlag ? View.VISIBLE : View.INVISIBLE;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_layout,null);

        TextView textView = (TextView)view.findViewById(R.id.item_description);
        int size = content.get(position).toString().length();
        textView.setText(content.get(position).toString().toCharArray(),0,size);

        final ListItem value = content.get(position);
        ImageView imageView = (ImageView)view.findViewById(R.id.item_icon);
        imageView.setVisibility(mDelete);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!content.isEmpty()){
                    content.remove(value);
                    //db.deleteBucket(value.getId());
                    value.delete(db);
                    notifyDataSetChanged();
                    Toast.makeText(context, value + " deleted!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
