package com.sd.rafael.sweetdreams;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.models.Dream;

public class FormDreamsActivity extends AppCompatActivity {

    private FormDreamsHelper helper;
    private Dream dream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_dreams);

        helper = new FormDreamsHelper(this);

        Intent intent = getIntent();
        dream = (Dream) intent.getSerializableExtra("dream");

        if(dream != null)
            helper.makeDream(dream);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form_confirm, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_form_confirm:
                Dream dream = helper.getDream();
                DreamDAO dao = new DreamDAO(this);

                LinearLayout ll = (LinearLayout) findViewById(R.id.activity_form_dreams);

                if(dream.getId() != null) {
                    dao.Update(dream);
                    Snackbar.make(ll, "Sonho editado.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else {
                    dao.Insert(dream);
                    Snackbar.make(ll, "Sonho adicionado.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
                dao.close();

                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
