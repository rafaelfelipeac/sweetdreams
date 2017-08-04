package com.rafaelfelipeac.sweetdreams.activities;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends BaseActivity  implements RecyclerViewClickPosition {

    @BindView(R.id.recyclerview_favorite)
    RecyclerView listDreams;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DreamDAO dao;
    private List<Dream> dreams;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ButterKnife.bind(this);

        toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.favorite_activity);

        listDreams.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listDreams.setLayoutManager(mLayoutManager);

        dao = new DreamDAO(this);
        dreams = dao.Read();

        loadListFavorite();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadListFavorite();
    }

    @Override
    public void getRecyclerViewAdapterPosition(int position) {
        List<Dream> dreamsFavorite = getDreamsFavorite();

        Dream dream = dreamsFavorite.get(position);

        Intent intentDreamsActivity = new Intent(FavoriteActivity.this, DreamsActivity.class);
        intentDreamsActivity.putExtra("dream", dream);
        startActivity(intentDreamsActivity);
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
    }

    private void loadListFavorite() {
        List<Dream> dreamsFavorite = getDreamsFavorite();

        mAdapter = new CardViewAdapter(dreamsFavorite, this);
        listDreams.setAdapter(mAdapter);
    }

    private List<Dream> getDreamsFavorite() {
        List<Dream> dreamsFavorite = new ArrayList<>(dreams.size());

        for(Dream dream : dreams)
            if(dream.getFavorite())
                dreamsFavorite.add(dream);

        return dreamsFavorite;
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
                Intent intent = new Intent(FavoriteActivity.this, MainNavDrawerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
