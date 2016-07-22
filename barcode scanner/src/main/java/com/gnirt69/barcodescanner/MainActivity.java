package com.gnirt69.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onClick(View v) {
        Intent intent;
        intent = new Intent(this, BarCodeScanner.class);
        startActivity(intent);
    }

    public void listView(View v) {
        Intent intent;
        intent = new Intent(this, ListViewAndroid.class);
        startActivity(intent);
    }

}
