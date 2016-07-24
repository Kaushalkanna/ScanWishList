package com.gnirt69.barcodescanner;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.util.List;

public class ListViewAndroid extends Activity {
    ListView listView;
    CustomAdapter adapter;
    ItemsDataSource datasource;
    List<Item> values;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        listView = (ListView) findViewById(R.id.list);
        datasource = new ItemsDataSource(this);
        datasource.open();
        values = datasource.getAllItems();
        context = this;
        adapter = new CustomAdapter(this, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                String url = values.get(position).getProductUrl();
                String toastText;
                if (url!= null &&!url.equals("") && !url.equals("N/A")) {
                    toastText = "opening "+url+" in browser";
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                }else{
                    toastText = "URL not found. Searching for product in google";
                    url = "https://www.google.com/#tbm=shop&q=" + values.get(position).getUPC();
                }
                Toast.makeText(context,toastText,Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> a, View v, final int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(context);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + values.get(position).getName());
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        datasource.deleteItem(values.get(position));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(context,"Deleted " + values.get(position).getName(),Toast.LENGTH_SHORT).show();
                    }});
                adb.show();
                return true;
            }
        });
    }

}
