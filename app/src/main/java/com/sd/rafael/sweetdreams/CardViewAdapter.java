package com.sd.rafael.sweetdreams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafae on 01/11/2016.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    public Dream[] dreams;
    private static RecyclerViewClickPosition mPositionInterface;

    public CardViewAdapter(List<Dream> dreams, RecyclerViewClickPosition positionInterface) {
        this.dreams = dreams.toArray(new Dream[dreams.size()]);
        mPositionInterface = positionInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.title.setText(dreams[position].getTitle());
        viewHolder.description.setText(dreams[position].getDescription());
    }

    @Override
    public int getItemCount() {
        return dreams.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView description;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            itemLayoutView.setOnClickListener(this);

            title = (TextView) itemLayoutView.findViewById(R.id.title);
            description = (TextView) itemLayoutView.findViewById(R.id.description);
        }

        @Override
        public void onClick(View v) {
            mPositionInterface.getRecyclerViewAdapterPosition(this.getLayoutPosition());
        }
    }


}
