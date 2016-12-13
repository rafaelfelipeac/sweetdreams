package com.sd.rafael.sweetdreams.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.cunoraz.tagview.TagView;
import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.helper.DreamsHelper;
import com.sd.rafael.sweetdreams.models.Dream;

public class DreamsActivity extends AppCompatActivity {

    private DreamsHelper helper;
    private Dream dream;
    private TagView tagGroup;
    private ScrollView sv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dreams);

        helper = new DreamsHelper(this);

        Intent intent = getIntent();
        dream = (Dream) intent.getSerializableExtra("dream");

        if(dream != null)
            helper.makeDream(dream);

        sv = (ScrollView) findViewById(R.id.activity_dreams);
        tagGroup = (TagView) findViewById(R.id.tag_group);

        tagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(com.cunoraz.tagview.Tag tag, int position) {
                Snackbar.make(sv, "Long Click: " + tag.text, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(com.cunoraz.tagview.Tag tag, int position) {
                Snackbar.make(sv, "Click: " + tag.text, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {

            @Override
            public void onTagDeleted(final TagView view, com.cunoraz.tagview.Tag tag, final int position) {
                Snackbar.make(sv, "Delete Click: " + tag.text, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });
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
