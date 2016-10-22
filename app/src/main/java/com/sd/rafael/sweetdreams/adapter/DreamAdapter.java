package com.sd.rafael.sweetdreams.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.List;

/**
 * Created by rafae on 22/10/2016.
 */

public class DreamAdapter extends BaseAdapter{
    private final List<Dream> dreams;
    private final Context context;

    public DreamAdapter(List<Dream> dreams, Context context) {
        this.dreams = dreams;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dreams.size();
    }

    @Override
    public Object getItem(int position) {
        return dreams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dreams.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Dream dream = dreams.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if(convertView == null)
            view = inflater.inflate(R.layout.list_item, parent, false);

        TextView title = (TextView) view.findViewById(R.id.item_title);
        title.setText(dream.getTitle());

        TextView description = (TextView) view.findViewById(R.id.item_description);
        description.setText(dream.getDescription());

        return view;
    }
}
