package com.rafaelfelipeac.sweetdreams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
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

public class SameDayActivity extends BaseActivity implements RecyclerViewClickPosition {

    @BindView(R.id.recyclerview_same_day)
    RecyclerView listDreams;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Dream> dreams;
    private ActionBar toolbar;
    private Dream dream;
    private DreamDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_day);

        ButterKnife.bind(this);

        toolbar = getSupportActionBar();

        listDreams.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listDreams.setLayoutManager(mLayoutManager);

        dao = new DreamDAO(this);

        dreams = (List<Dream>) getIntent().getSerializableExtra("dreams");
        dream = dreams.get(0);

        String date = (String.format("%02d", dream.getDay()) + "/" + (String.format("%02d", dream.getMonth()))) + "/" + dream.getYear();
        toolbar.setTitle(date);

        loadList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        dreams = new ArrayList<>();
        for(Dream day : dao.Read()) {
            if(isSameday(day))
                dreams.add(day);
        }

        loadList();
    }

    private boolean isSameday(Dream day) {
        return day.getDay() == dream.getDay() && day.getMonth() == dream.getMonth() && day.getYear() == dream.getYear();
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
