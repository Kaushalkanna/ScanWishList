package com.gnirt69.barcodescanner;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.net.URL;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
            showDialog();
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
                            price = data.getString("currency") + " " + data.getString("price");
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


    private void showDialog() {
        Drawable drawable = getImage();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_box);
        dialog.setTitle("Details");
        TextView productName = (TextView) dialog.findViewById(R.id.productname);
        TextView priceField = (TextView) dialog.findViewById(R.id.price);
        TextView upcCode = (TextView) dialog.findViewById(R.id.upc);
        TextView storeName = (TextView) dialog.findViewById(R.id.storename);
        ImageView image = (ImageView) dialog.findViewById(R.id.imageView);



        productName.setText(name);
        priceField.setText(price);
        upcCode.setText(upc);
        storeName.setText(storename);
        try{
            URL url = new URL("http://www.cnmuqi.com/data/out/22/random-picture-8193585.jpg");
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            image.setImageBitmap(bmp);
        }catch(Exception e){
            e.printStackTrace();
        }


        Button dialogYes = (Button) dialog.findViewById(R.id.yes);
        Button dialogNo = (Button) dialog.findViewById(R.id.no);

        dialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasource.createItem(upc, name, price, imageurl, producturl, storename);
                createToast();
                dialog.dismiss();
            }
        });
        dialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private Drawable getImage() {
        Drawable drawable = null;
        try {

            URL newurl = new URL(imageurl);
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            drawable = new BitmapDrawable(getResources(), mIcon_val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    private void createToast() {
        Toast.makeText(
                getApplicationContext(),
                "item :" + upc + " added to wishlist ",
                Toast.LENGTH_SHORT).show();
    }

}