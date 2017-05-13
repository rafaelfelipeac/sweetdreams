package com.rafaelfelipeac.sweetdreams.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.rafaelfelipeac.sweetdreams.DAO.DreamDAO;
import com.rafaelfelipeac.sweetdreams.R;
import com.rafaelfelipeac.sweetdreams.RecyclerViewClickPosition;
import com.rafaelfelipeac.sweetdreams.adapter.CardViewAdapter;
import com.rafaelfelipeac.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends BaseActivity  implements RecyclerViewClickPosition {

    private RecyclerView listDreams;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.favorite_activity);

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
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FavoriteActivity.this, MainNavDrawerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                Intent intent = new Intent(FavoriteActivity.this, MainNavDrawerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
