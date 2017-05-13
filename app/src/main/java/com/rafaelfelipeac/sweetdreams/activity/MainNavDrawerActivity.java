package com.rafaelfelipeac.sweetdreams.activity;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rafaelfelipeac.sweetdreams.DAO.DreamDAO;
import com.rafaelfelipeac.sweetdreams.R;
import com.rafaelfelipeac.sweetdreams.RecyclerViewClickPosition;
import com.rafaelfelipeac.sweetdreams.adapter.CardViewAdapter;
import com.rafaelfelipeac.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;


public class MainNavDrawerActivity extends BaseActivity
implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewClickPosition {

    private String separator = ",";
    private FloatingActionButton fabBtn;
    private RecyclerView listDreams;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String newTextTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_drawer);

        listDreams = (RecyclerView)findViewById(R.id.recyclerview);
        listDreams.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        listDreams.setLayoutManager(mLayoutManager);

        loadList();

        listDreams.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabBtn.isShown())
                    fabBtn.hide();
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fabBtn.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForm = new Intent(MainNavDrawerActivity.this, FormDreamsActivity.class);
                startActivity(intentForm);
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
            }
        });

        fabBtn = (FloatingActionButton)findViewById(R.id.fab);

        registerForContextMenu(listDreams);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }
        };

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
            overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
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
                fabBtn.hide();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fabBtn.hide();
                cleanList();
                newTextTag = newText;
                loadListWithTags(newText);

                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabBtn.hide();
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
            overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
        }
        if(id == R.id.nav_calendar) {
            Intent intentCalendar = new Intent(MainNavDrawerActivity.this, CalendarActivity.class);
            startActivity(intentCalendar);
            overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
        }
        if(id == R.id.nav_favorite) {
            Intent intentFavorite = new Intent(MainNavDrawerActivity.this, FavoriteActivity.class);
            startActivity(intentFavorite);
            overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
        }
        if(id == R.id.nav_settings) {
            Intent intentFavorite = new Intent(MainNavDrawerActivity.this, SettingsActivity.class);
            startActivity(intentFavorite);
            overridePendingTransition(R.xml.fade_in, R.xml.fade_out);

        }

        finish();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawers();
        return true;
    }

    @Override
    public void getRecyclerViewAdapterPosition(int position) {
        Dream dream;
        DreamDAO ddao = new DreamDAO(this);;

        if(newTextTag != null) {
            List<Dream> dreams = new DreamDAO(this).Read();
            List<Dream> dreamsWT = new ArrayList<>();

            for(Dream d : dreams) {
                if(d.getTags().contains(newTextTag))
                    dreamsWT.add(d);
            }

            dream = dreamsWT.get(position);
        }
        else {
            List<Dream> ddreams = ddao.Read();
            dream = ddreams.get(position);
        }

        Intent intentDreamsActivity = new Intent(MainNavDrawerActivity.this, DreamsActivity.class);
        intentDreamsActivity.putExtra("dream", dream);
        startActivity(intentDreamsActivity);
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
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