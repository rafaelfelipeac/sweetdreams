package com.sd.rafael.sweetdreams.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.RecyclerViewClickPosition;
import com.sd.rafael.sweetdreams.adapter.CardViewAdapter;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements RecyclerViewClickPosition {

    private RecyclerView listDreams;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        listDreams = (RecyclerView)findViewById(R.id.recyclerview_favorite);
        listDreams.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listDreams.setLayoutManager(mLayoutManager);

        loadListFavorite();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadListFavorite();
    }

    @Override
    public void getRecyclerViewAdapterPosition(int position) {
        DreamDAO dao = new DreamDAO(this);
        List<Dream> dreams = dao.Read();
        List<Dream> dreamsFavorite = new ArrayList<>(dreams.size());

        for(Dream dream : dreams)
            if(dream.getFavorite())
                dreamsFavorite.add(dream);

        Dream dream = dreamsFavorite.get(position);

        Intent intentDreamsActivity = new Intent(FavoriteActivity.this, DreamsActivity.class);
        intentDreamsActivity.putExtra("dream", dream);
        startActivity(intentDreamsActivity);
    }

    private void loadListFavorite() {
        DreamDAO dao = new DreamDAO(this);
        List<Dream> dreams = dao.Read();
        List<Dream> dreamsFavorite = new ArrayList<>(dreams.size());

        for(Dream dream : dreams)
            if(dream.getFavorite())
                dreamsFavorite.add(dream);

        mAdapter = new CardViewAdapter(dreamsFavorite, this);
        listDreams.setAdapter(mAdapter);
    }
}
