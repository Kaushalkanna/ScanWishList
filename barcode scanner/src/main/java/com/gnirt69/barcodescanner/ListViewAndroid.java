package com.gnirt69.barcodescanner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ListViewAndroid extends Activity {
    ListView listView;
    private ItemsDataSource datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        listView = (ListView) findViewById(R.id.list);

        datasource = new ItemsDataSource(this);
        datasource.open();

        List<Item> values = datasource.getAllItems();

        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(
                this,
                android.R.layout.simple_list_item_1,
                values);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                int itemPosition = position;
//                String itemValue = (String) listView.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//        });
    }

}
