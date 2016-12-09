package com.sd.rafael.sweetdreams.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.helper.DreamsHelper;
import com.sd.rafael.sweetdreams.models.Dream;

public class DreamsActivity extends AppCompatActivity {

    private DreamsHelper helper;
    private Dream dream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dreams);

        helper = new DreamsHelper(this);

        Intent intent = getIntent();
        dream = (Dream) intent.getSerializableExtra("dream");

        if(dream != null)
            helper.makeDream(dream);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dream, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dream dream = helper.getDream();
        switch (item.getItemId())
        {
            case R.id.menu_dream_edit:
                Intent intentForm = new Intent(DreamsActivity.this, FormDreamsActivity.class);
                intentForm.putExtra("dream", dream);
                startActivity(intentForm);
                finish();
                break;
            case R.id.menu_dream_delete:
                AlertDialog.Builder alert = new AlertDialog.Builder(DreamsActivity.this);
                alert.setMessage("Excluir sonho '"+ dream.getTitle()+"'?").setCancelable(false)
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        Dream dream = helper.getDream();
                        DreamDAO dao = new DreamDAO(DreamsActivity.this);
                        dao.Read();
                        dao.Remove(dream);
                        dao.close();
                        Intent intentMain = new Intent(DreamsActivity.this, MainNavDrawerActivity.class);
                        startActivity(intentMain);
                        finish();
                    }
                });

                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
