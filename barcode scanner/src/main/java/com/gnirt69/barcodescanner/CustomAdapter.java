package com.gnirt69.barcodescanner;

/**
 * Created by xkxd061 on 7/24/16.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    List<Item> result;
    Context context;
    ItemsDataSource datasource;
    private static LayoutInflater inflater = null;

    public CustomAdapter(ListViewAndroid activity, List<Item> values) {
        result = values;
        context = activity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        datasource = new ItemsDataSource(context);
        datasource.open();
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(R.layout.list_element, null);
        TextView listName = (TextView) rowView.findViewById(R.id.listName);
        TextView listPrice = (TextView) rowView.findViewById(R.id.listPrice);
        ImageView listImage = (ImageView) rowView.findViewById(R.id.listImage);
        listName.setText(result.get(position).getName());
        listPrice.setText(result.get(position).getPrice());
//        listImage.setImageResource(result.get(position).getPrice());
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = result.get(position).getProductUrl();
                String toastText;
                if (url!= null &&!url.equals("") && !url.equals("N/A")) {
                    toastText = "opening "+url+" in browser";
                    if (!url.startsWith("http://") && !url.startsWith("https://"))
                        url = "http://" + url;
                }else{
                    toastText = "URL not found. Searching for product in google";
                    url = "https://www.google.com/#tbm=shop&q=" + result.get(position).getUPC();
                }
                Toast.makeText(context,toastText,Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder adb=new AlertDialog.Builder(context);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + result.get(position).getName());
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        datasource.deleteItem(result.get(position));
//                        context.notifyDataSetChanged();
                        Toast.makeText(context,"I am the one",Toast.LENGTH_SHORT).show();
                    }});
                adb.show();
                return true;
            }
        });
        return rowView;
    }

}
