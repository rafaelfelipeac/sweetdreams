package com.sd.rafael.sweetdreams.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.helper.FormDreamsHelper;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.Calendar;
import java.util.List;

public class FormDreamsActivity extends AppCompatActivity {

    private FormDreamsHelper helper;
    private Dream dream;
    private TagView tagGroup;
    private ScrollView sv;

    static final int DIALOG_DATE_ID = 2;
    int yearX;
    int monthX;
    int dayX;

    private DatePickerDialog.OnDateSetListener dpickerListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    yearX = year;
                    monthX = month + 1;
                    dayX = dayOfMonth;

                    TextView date = (TextView) findViewById(R.id.form_dreams_date);
                    date.setText(dayX + "/" + monthX + "/" + yearX);

                    Snackbar.make(findViewById(R.id.activity_form_dreams), dayX + "/" + monthX + "/" + yearX, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            };

    public void showDatePickerDialog() {
        Button btnSetDate = (Button) findViewById(R.id.form_dreams_btnSetDate);

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE_ID);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_DATE_ID)
            return new DatePickerDialog(this, dpickerListener, yearX, monthX, dayX);
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_dreams);

        final Calendar cal = Calendar.getInstance();

        sv = (ScrollView) findViewById(R.id.form_dreams);
        tagGroup = (TagView) findViewById(R.id.tag_group_form);

        yearX = (cal.get(Calendar.YEAR));
        monthX = (cal.get(Calendar.MONTH));
        dayX = (cal.get(Calendar.DAY_OF_MONTH));

        showDatePickerDialog();

        TextView date = (TextView) findViewById(R.id.form_dreams_date);
        date.setText(dayX + "/" + (monthX+1) + "/" + yearX);

        helper = new FormDreamsHelper(this);

        Intent intent = getIntent();
        dream = (Dream) intent.getSerializableExtra("dream");

        if(dream != null)
            helper.makeDream(dream);

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
                view.remove(position);
                Snackbar.make(sv, "Delete Click: " + tag.text, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        Button btnNewTag = (Button) findViewById(R.id.form_dreams_btnNewTag);
        btnNewTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newTag = (EditText) findViewById(R.id.form_dreams_tag);

                if(!newTag.getText().toString().isEmpty() && !newTag.getText().toString().equals(" ")) {
                    com.cunoraz.tagview.Tag tag = new Tag(newTag.getText().toString());
                    tag.radius = 10f;
                    tag.layoutColor = Color.rgb(0, 149, 255);
                    tag.isDeletable = true;
                    newTag.setText("");
                    tagGroup.addTag(tag);
                }
                else
                    Snackbar.make(sv, "Tag vazia.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.form_confirm, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dream dream = helper.getDream();
        Intent intentDream;
        switch (item.getItemId()) {
            case R.id.menu_form_confirm:
                if(emptyDream(2)) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(FormDreamsActivity.this);
                    alert.setMessage("Necessário que o sonho tenha um título e uma descrição.").setCancelable(false)
                            .setPositiveButton("OK", null);
                    alert.show();
                }
                else {
                    DreamDAO dao = new DreamDAO(FormDreamsActivity.this);

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
                    intentDream = new Intent(FormDreamsActivity.this, DreamsActivity.class);
                    intentDream.putExtra("dream", dream);
                    startActivity(intentDream);

                    finish();
                }
                break;
            case android.R.id.home:

                DreamDAO dao = new DreamDAO(this);
                List<Dream> dreams = dao.Read();
                Dream originalDream = new Dream();
                if(this.dream != null) {
                    for(Dream d : dreams) {
                        if(d.getId().equals(this.dream.getId()))
                            originalDream = d;
                    }
                }

                if(emptyDream(1) || ((compareDreams(originalDream, dream)))) {
                    if(dream.getId() == null)
                        intentDream = new Intent(FormDreamsActivity.this, MainNavDrawerActivity.class);
                    else
                        intentDream = new Intent(FormDreamsActivity.this, DreamsActivity.class);

                    intentDream.putExtra("dream", originalDream);
                    startActivity(intentDream);
                    finish();
                }
                else {
                    showDialogExitSave();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogExitSave() {
        final Intent intentA = new Intent(FormDreamsActivity.this, MainNavDrawerActivity.class);
        AlertDialog.Builder alert = new AlertDialog.Builder(FormDreamsActivity.this);
        alert.setMessage("Sair sem salvar o sonho?").setCancelable(false)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intentA);
                    }
                });
        alert.show();
    }

    public boolean emptyDream(int n) {
        Dream dream = helper.getDream();

        switch(n) {
            case 1:
                return (dream.getTitle().equals("") && dream.getDescription().equals("")) || (dream.getTitle().equals(" ") && dream.getDescription().equals(" "));
            case 2:
                return (dream.getTitle().equals("") || dream.getDescription().equals("")) || (dream.getTitle().equals(" ") || dream.getDescription().equals(" "));

        }
        return false;
    }

    public boolean compareDreams(Dream dream, Dream dream2) {
        if(dream.getTitle() != null) {

            return dream.getTitle().equals(dream2.getTitle()) &&
                    dream.getDescription().equals(dream2.getDescription()) &&
                    dream.getTags().equals(dream2.getTags());
        }
        return false;
    }
}
