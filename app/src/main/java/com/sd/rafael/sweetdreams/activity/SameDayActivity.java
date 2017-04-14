package com.sd.rafael.sweetdreams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.RecyclerViewClickPosition;
import com.sd.rafael.sweetdreams.adapter.CardViewAdapter;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.List;

public class SameDayActivity extends BaseActivity implements RecyclerViewClickPosition {

    private RecyclerView listDreams;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Dream> dreams;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_day);

        toolbar = getSupportActionBar();

        listDreams = (RecyclerView)findViewById(R.id.recyclerview_same_day);
        listDreams.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listDreams.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        dreams = (List<Dream>) intent.getSerializableExtra("dreams");
        Dream dayDream = dreams.get(0);

        //date.setText(String.format("%02d", dayX) + "/" + String.format("%02d", monthX+1) + "/" + yearX);
        String date = (String.format("%02d", dayDream.getDay()) + "/" + (String.format("%02d", dayDream.getMonth()))) + "/" + dayDream.getYear();
        toolbar.setTitle(date);

        loadList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @Override
    public void getRecyclerViewAdapterPosition(int position) {
        Dream dream = dreams.get(position);

        Intent intentDreamsActivity = new Intent(SameDayActivity.this, DreamsActivity.class);
        intentDreamsActivity.putExtra("dream", dream);
        startActivity(intentDreamsActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    private void loadList() {
        mAdapter = new CardViewAdapter(dreams, this);
        listDreams.setAdapter(mAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0, 0);
                return true;
        }

        return false;
    }
}
