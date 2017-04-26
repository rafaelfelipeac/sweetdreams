package com.sd.rafael.sweetdreams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.RecyclerViewClickPosition;
import com.sd.rafael.sweetdreams.adapter.CardViewAdapter;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SameDayActivity extends BaseActivity implements RecyclerViewClickPosition {

    private RecyclerView listDreams;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Dream> dreams;
    private ActionBar toolbar;
    private Dream dream;

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
        dream = dreams.get(0);

        //date.setText(String.format("%02d", dayX) + "/" + String.format("%02d", monthX+1) + "/" + yearX);
        String date = (String.format("%02d", dream.getDay()) + "/" + (String.format("%02d", dream.getMonth()))) + "/" + dream.getYear();
        toolbar.setTitle(date);

        loadList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        dreams = new ArrayList<>();
        DreamDAO dao = new DreamDAO(this);
        for(Dream d : dao.Read()) {
            if(d.getDay() == dream.getDay() && d.getMonth() == dream.getMonth() && d.getYear() == dream.getYear())
                dreams.add(d);
        }

        loadList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sameday_add, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void getRecyclerViewAdapterPosition(int position) {
        Dream dream = dreams.get(position);

        Intent intentDreamsActivity = new Intent(SameDayActivity.this, DreamsActivity.class);
        intentDreamsActivity.putExtra("dream", dream);
        startActivity(intentDreamsActivity);
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
    }

    private void loadList() {
        mAdapter = new CardViewAdapter(dreams, this);
        listDreams.setAdapter(mAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                return true;
            case R.id.menu_sameday_add:
                Intent intentForm = new Intent(SameDayActivity.this, FormDreamsActivity.class);
                intentForm.putExtra("dreamDay", dream);
                startActivity(intentForm);
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
        }

        return false;
    }
}
