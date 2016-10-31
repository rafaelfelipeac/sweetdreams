package com.sd.rafael.sweetdreams;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.adapter.DreamAdapter;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

public class MainNavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listDreams;
    private String separator = ",";
    private FloatingActionButton fabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_drawer);

        listDreams = (ListView) findViewById(R.id.list_dreams);

        listDreams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dream dream = (Dream) listDreams.getItemAtPosition(position);

                Intent intentDream = new Intent(MainNavDrawerActivity.this, DreamsActivity.class);
                intentDream.putExtra("dream", dream);
                startActivity(intentDream);
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
        dao.close();

        DreamAdapter adapter = new DreamAdapter(dreams, this);
        listDreams.setAdapter(adapter);
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
            case R.id.menu_settings:
                break;
            case R.id.menuSearch:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Dream dream = (Dream) listDreams.getItemAtPosition(info.position);

        MenuItem delete = menu.add("Delete");
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DreamDAO dao = new DreamDAO(MainNavDrawerActivity.this);
                dao.Remove(dream);
                dao.close();
                loadList();
                return false;
            }
        });
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

            for(int i = 0; i < tag.length; i++) {
                String tt = tag[i];
                tag[i] = tt.replace(" ", "");
            }

            for(String str: tag) {
                if(str != "") {
                    String tagUser = query.toLowerCase();

                    if (tagUser.contains(str.replace(" ", ""))) {
                        dreams.add(dream);
                        break;
                    }
                }
            }
        }

        DreamAdapter adapter = new DreamAdapter(dreams, this);
        listDreams.setAdapter(adapter);
    }

    public void cleanList() {
        List<Dream> l = new ArrayList<>();

        DreamAdapter adapter = new DreamAdapter(l, this);
        listDreams.setAdapter(adapter);
    }
}
