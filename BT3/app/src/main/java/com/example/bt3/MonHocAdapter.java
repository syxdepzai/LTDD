package com.example.bt3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MonHocAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<MonHoc> monHocList;

    public MonHocAdapter(Context context, int layout, ArrayList<MonHoc> monHocList) {
        this.context = context;
        this.layout = layout;
        this.monHocList = monHocList;
    }

    @Override
    public int getCount() {
        return monHocList.size();
    }

    @Override
    public Object getItem(int position) {
        return monHocList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imgMonHoc;
        TextView tvTenMonHoc;
        TextView tvMoTa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            viewHolder = new ViewHolder();
            viewHolder.imgMonHoc = convertView.findViewById(R.id.imgMonHoc);
            viewHolder.tvTenMonHoc = convertView.findViewById(R.id.tvTenMonHoc);
            viewHolder.tvMoTa = convertView.findViewById(R.id.tvMoTa);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MonHoc monHoc = monHocList.get(position);
        viewHolder.imgMonHoc.setImageResource(monHoc.getPic());
        viewHolder.tvTenMonHoc.setText(monHoc.getName());
        viewHolder.tvMoTa.setText(monHoc.getDesc());

        return convertView;
    }
}
