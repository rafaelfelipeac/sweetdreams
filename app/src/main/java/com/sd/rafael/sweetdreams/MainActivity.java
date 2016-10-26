package com.sd.rafael.sweetdreams;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.adapter.DreamAdapter;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listDreams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listDreams = (ListView) findViewById(R.id.list_dreams);

        listDreams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dream dream = (Dream) listDreams.getItemAtPosition(position);

                Intent intentForm = new Intent(MainActivity.this, FormDreamsActivity.class);
                intentForm.putExtra("dream", dream);
                startActivity(intentForm);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForm = new Intent(MainActivity.this, FormDreamsActivity.class);
                startActivity(intentForm);
            }
        });

        registerForContextMenu(listDreams);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_settings:
                break;
            case R.id.menu_search:
                Intent toSearch = new Intent(this, SearchActivity.class);
                startActivity(toSearch);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Dream dream = (Dream) listDreams.getItemAtPosition(info.position);

        MenuItem delete = menu.add("Delete");
        delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DreamDAO dao = new DreamDAO(MainActivity.this);
                dao.Remove(dream);
                dao.close();
                loadList();

                Snackbar.make(findViewById(R.id.activity_main), "Sonho removido", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                return false;
            }
        });
    }
}
