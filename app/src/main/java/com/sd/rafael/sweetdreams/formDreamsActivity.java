package com.sd.rafael.sweetdreams;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.Calendar;
import java.util.Date;

public class FormDreamsActivity extends AppCompatActivity {

    private FormDreamsHelper helper;
    private Dream dream;

    static final int DIALOG_TIME_ID = 1;
    static final int DIALOG_DATE_ID = 2;
//    int hourX;
//    int minuteX;
//    int yearX;
//    int monthX;
//    int dayX;

    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    dream.setHour(hourOfDay);
                    dream.setMinute(minute);
                    Toast.makeText(FormDreamsActivity.this, dream.getHour() + ":" + dream.getMinute(), Toast.LENGTH_SHORT).show();
                }
            };

    protected DatePickerDialog.OnDateSetListener kDatePickerListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    dream.setYear(year);
                    dream.setMonth(month + 1);
                    dream.setDay(dayOfMonth);
                    Snackbar.make(findViewById(R.id.activity_form_dreams), dream.getDay() + "/" + dream.getMonth() + "/" + dream.getYear(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    //Toast.makeText(FormDreamsActivity.this, dayX + "/" + monthX + "/" + yearX, Toast.LENGTH_SHORT).show();
                }
            };

    public void showTimePickerDialog() {
        Button btnSetTime = (Button) findViewById(R.id.form_dreams_btnSetTime);

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME_ID);
            }
        });
    }

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
        if(id == DIALOG_TIME_ID)
            return new TimePickerDialog(FormDreamsActivity.this, kTimePickerListener, dream.getHour(), dream.getMinute(), false);
        if(id == DIALOG_DATE_ID)
            return new DatePickerDialog(this, kDatePickerListener, dream.getYear(), dream.getMonth(), dream.getDay());
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_dreams);

        final Calendar cal = Calendar.getInstance();

        dream.setHour(cal.get(Calendar.HOUR));
        dream.setMinute(cal.get(Calendar.MINUTE));
        dream.setYear(cal.get(Calendar.YEAR));
        dream.setMonth(cal.get(Calendar.MONTH));
        dream.setDay(cal.get(Calendar.DAY_OF_MONTH));

        showTimePickerDialog();
        showDatePickerDialog();

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
