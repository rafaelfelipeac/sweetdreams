package com.sd.rafael.sweetdreams.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.sd.rafael.sweetdreams.AppPreferences;
import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.RecyclerViewClickPosition;
import com.sd.rafael.sweetdreams.adapter.CardViewAdapter;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

public class MainNavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewClickPosition {

    private String separator = ",";
    private FloatingActionButton fabBtn;
    private RecyclerView listDreams;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_drawer);

        listDreams = (RecyclerView)findViewById(R.id.recyclerview);
        listDreams.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listDreams.setLayoutManager(mLayoutManager);

        loadList();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForm = new Intent(MainNavDrawerActivity.this, FormDreamsActivity.class);
                startActivity(intentForm);
            }
        });

        fabBtn = (FloatingActionButton)findViewById(R.id.fab);

        registerForContextMenu(listDreams);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void loadList() {
        DreamDAO dao = new DreamDAO(this);
        List<Dream> dreams = dao.Read();
        mAdapter = new CardViewAdapter(dreams, this);
        listDreams.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fabBtn.hide();
                cleanList();
                loadListWithTags(newText);

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                loadList();
                fabBtn.show();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSearch:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            Intent intentAdd = new Intent(MainNavDrawerActivity.this, FormDreamsActivity.class);
            startActivity(intentAdd);
        }
        if(id == R.id.nav_calendar) {
            Intent intentCalendar = new Intent(MainNavDrawerActivity.this, CalendarActivity.class);
            startActivity(intentCalendar);
        }
        if(id == R.id.nav_favorite) {
            Intent intentFavorite = new Intent(MainNavDrawerActivity.this, FavoriteActivity.class);
            startActivity(intentFavorite);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void getRecyclerViewAdapterPosition(int position) {
        Dream dream = new DreamDAO(this).Read().get(position);

        Intent intentDreamsActivity = new Intent(MainNavDrawerActivity.this, DreamsActivity.class);
        intentDreamsActivity.putExtra("dream", dream);
        startActivity(intentDreamsActivity);
    }

    public String[] convertStringToArray(String str) {
        String[] array = str.split(separator);
        return array;
    }

    private void loadListWithTags(String query) {
        DreamDAO dao = new DreamDAO(this);
        List<Dream> lst = dao.Read();
        dao.close();

        List<Dream> dreams = new ArrayList<>();

        for(Dream dream : lst) {
            String[] tag = convertStringToArray(dream.getTags());

            for(int i = 0; i < tag.length; i++)
                tag[i].replace(" ", "");

            for(String str: tag) {
                if(str != "") {
                    String tagUser = query.toLowerCase().replace(" ", "");
                    str = str.toLowerCase().replaceAll(" ", "");
                    
                    if (str.startsWith(tagUser)) {
                        dreams.add(dream);
                        break;
                    }
                }
            }
        }

        mAdapter = new CardViewAdapter(dreams, this);
        listDreams.setAdapter(mAdapter);
    }

    public void cleanList() {
        List<Dream> l = new ArrayList<>();

        mAdapter = new CardViewAdapter(l, this);
        listDreams.setAdapter(mAdapter);
    }
}
