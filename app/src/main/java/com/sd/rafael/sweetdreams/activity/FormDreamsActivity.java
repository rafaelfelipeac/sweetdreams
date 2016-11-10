package com.sd.rafael.sweetdreams.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.helper.FormDreamsHelper;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.Calendar;
import java.util.List;

public class FormDreamsActivity extends AppCompatActivity {

    private FormDreamsHelper helper;
    private Dream dream;

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

        final LinearLayout ll = (LinearLayout) findViewById(R.id.form_dreams_ll);
        Button btnNewTag = (Button) findViewById(R.id.form_dreams_btnNewTag);
        btnNewTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newTag = (EditText) findViewById(R.id.form_dreams_tag);
                CheckBox cb = new CheckBox(FormDreamsActivity.this);
                cb.setChecked(true);
                cb.setText(newTag.getText());
                newTag.setText("");
                ll.addView(cb);
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
                for(Dream d : dreams) {
                    if(d.getId().equals(this.dream.getId()))
                        originalDream = d;
                }

                if(emptyDream(1) || (compareDreams(originalDream, dream))) {
                    if(dream.getId() == null)
                        intentDream = new Intent(FormDreamsActivity.this, MainNavDrawerActivity.class);
                    else
                        intentDream = new Intent(FormDreamsActivity.this, DreamsActivity.class);

                    intentDream.putExtra("dream", dream);
                    startActivity(intentDream);
                    finish();
                }
                else {
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

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean emptyDream(int n) {
        Dream dream = helper.getDream();

        switch(n) {
            case 1:
                return dream.getTitle().equals("") && dream.getDescription().equals("") && dream.getTitle().equals(" ") && dream.getDescription().equals(" ");
            case 2:
                return dream.getTitle().equals("") || dream.getDescription().equals("") || dream.getTitle().equals(" ") || dream.getDescription().equals(" ");

        }
        return false;
    }

    public boolean compareDreams(Dream dream, Dream dream2) {
        return dream.getTitle().equals(dream2.getTitle()) &&
                dream.getDescription().equals(dream2.getDescription()) &&
                dream.getTags().equals(dream2.getTags());
    }
}
