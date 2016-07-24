package com.gnirt69.barcodescanner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ListViewAndroid extends Activity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        listView = (ListView) findViewById(R.id.list);
        ItemsDataSource datasource = new ItemsDataSource(this);
        datasource.open();
        List<Item> values = datasource.getAllItems();
        datasource.close();
        listView.setAdapter(new CustomAdapter(this, values));
    }

}
