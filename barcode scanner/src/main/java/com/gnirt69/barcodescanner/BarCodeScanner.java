package com.gnirt69.barcodescanner;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarCodeScanner extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private ItemsDataSource datasource;
    public String upc;
    public String name;
    public String price;
    public String imageurl;
    public String producturl;
    public String storename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_scanner);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        datasource = new ItemsDataSource(this);
        datasource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        datasource.close();
    }

    @Override
    public void handleResult(final Result result) {
        upc = result.getText();
        Log.w("handleResult", result.getText());
        String url = "http://www.searchupc.com/handlers/upcsearch.ashx?" +
                "request_type=3&access_token=C4D521E6-37BA-4F33-AF34-5AD38AA318C8&upc="
                + result.getText();

        httpRequest(url);
        if (name != null) {
            alertBox();
        }
        mScannerView.resumeCameraPreview(this);
    }

    private void httpRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("0");

                            name = data.getString("productname");
                            price = data.getString("currency") + data.getString("price");
                            imageurl = data.getString("imageurl");
                            producturl = data.getString("producturl");
                            storename = data.getString("storename");
                            Log.w("info", name + price);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w("volley", error.getMessage());
                    }
                }
        );
        if (name == null) {
            queue.add(jsonObjReq);
        }
    }


    private void alertBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan result");
        builder.setMessage("UPC = " + upc +
                "\nName = " + name +
                "\nprice = " + price +
                "\nAdd to Wish List?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        datasource.createItem(upc, name, price, imageurl, producturl, storename);
                        createToast(dialog);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createToast(DialogInterface dialog) {
        Toast.makeText(
                getApplicationContext(),
                "item :" + upc + " added to wishlist ",
                Toast.LENGTH_SHORT).show();
    }

}