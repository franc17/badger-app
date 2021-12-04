package com.frances.badger.data;

import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.frances.badger.R;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BadgerListAdapter extends ArrayAdapter<TableEntry>{
    public ArrayList<TableEntry> tableEntries = new ArrayList<>();
    Context mContext;

    public BadgerListAdapter(Context context, int list_item_id, ArrayList<TableEntry> entries){
        super(context, list_item_id, entries);
        tableEntries = entries;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item, null);
        TextView itemId = v.findViewById(R.id.item_id);
        TextView itemTitle = v.findViewById(R.id.item_title);
        TextView itemDue = v.findViewById(R.id.item_due);
        itemId.setText(String.valueOf(tableEntries.get(position).getId()));
        itemTitle.setText(tableEntries.get(position).getBadgerTitle());
        itemDue.setText(tableEntries.get(position).getStringTimeDue());
        return v;
    }
}
