package com.sd.rafael.sweetdreams;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.adapter.DreamAdapter;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView listDreams;
    private EditText tags;
    private String separator = ",";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listDreams = (ListView) findViewById(R.id.list_dreams_search);

        listDreams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dream dream = (Dream) listDreams.getItemAtPosition(position);

                Intent intentForm = new Intent(SearchActivity.this, FormDreamsActivity.class);
                intentForm.putExtra("dream", dream);
                startActivity(intentForm);
            }
        });

        Button btnSearch = (Button) findViewById(R.id.search_btn);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListWithTags();
            }
        });

        //carregar lista
        //pegar as tags digitadas pelo usuario
        //procurar por elas na lista
        //mostrar na tela o resultado
        //
        //

        registerForContextMenu(listDreams);
    }

    public String[] convertStringToArray(String str) {
        String[] array = str.split(separator);
        return array;
    }

    private void loadListWithTags() {
        DreamDAO dao = new DreamDAO(this);
        List<Dream> lst = dao.Read();
        dao.close();
        tags = (EditText) findViewById(R.id.search_tags);

        List<Dream> dreams = new ArrayList<>();

        for(Dream dream : lst) {
            String[] tag = convertStringToArray(dream.getTags());

            for(int i = 0; i < tag.length; i++)
            {
                String tt = tag[i];
                tag[i] = tt.replace(" ", "");
            }

            for(String str: tag) {

                if(str != "") {
                    String tagUser = tags.getText().toString().toLowerCase();

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

    @Override
    protected void onResume() {
        super.onResume();
        //loadListWithTags();
    }
}
