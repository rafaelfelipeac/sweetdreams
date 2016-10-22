package com.sd.rafael.sweetdreams;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
