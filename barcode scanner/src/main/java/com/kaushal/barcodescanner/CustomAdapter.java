package com.kaushal.barcodescanner;

/**
 * Created by xkxd061 on 7/24/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

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

    public void swapItems(List<Item> items) {
        result = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        View rowView;
        rowView = inflater.inflate(R.layout.list_element, null);
        TextView listName = (TextView) rowView.findViewById(R.id.listName);
        TextView listPrice = (TextView) rowView.findViewById(R.id.listPrice);
        TextView listStore = (TextView) rowView.findViewById(R.id.listStore);
        ImageView listImage = (ImageView) rowView.findViewById(R.id.listImage);
        listName.setText(result.get(position).getName());
        listPrice.setText(result.get(position).getPrice());
        listStore.setText(result.get(position).getStoreName());
        imageLoader.displayImage(result.get(position).getImageUrl(), listImage);
        return rowView;
    }
}
