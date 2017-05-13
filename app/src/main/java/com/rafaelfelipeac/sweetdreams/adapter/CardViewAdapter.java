package com.rafaelfelipeac.sweetdreams.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rafaelfelipeac.sweetdreams.R;
import com.rafaelfelipeac.sweetdreams.RecyclerViewClickPosition;
import com.rafaelfelipeac.sweetdreams.models.Dream;

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

        viewHolder.date.setText(String.format("%02d", dreams[position].getDay()) + "/" + String.format("%02d", dreams[position].getMonth()) + "/" + dreams[position].getYear());

        viewHolder.image.setVisibility((!dreams[position].getAudioPath().isEmpty()) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return dreams.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView date;
        public ImageView image;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            itemLayoutView.setOnClickListener(this);

            title = (TextView) itemLayoutView.findViewById(R.id.title);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            image = (ImageView) itemLayoutView.findViewById(R.id.audio_icon);
        }

        @Override
        public void onClick(View v) {
            mPositionInterface.getRecyclerViewAdapterPosition(this.getLayoutPosition());
        }
    }


}
