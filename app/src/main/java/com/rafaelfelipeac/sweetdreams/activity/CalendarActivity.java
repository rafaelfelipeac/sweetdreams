package com.rafaelfelipeac.sweetdreams.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.rafaelfelipeac.sweetdreams.DAO.DreamDAO;
import com.rafaelfelipeac.sweetdreams.R;
import com.rafaelfelipeac.sweetdreams.models.Dream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends BaseActivity {

    CompactCalendarView calendar;
    TextView textView;
    SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        final DreamDAO dao = new DreamDAO(this);
        final List<Dream> dreams = dao.Read();
        final Button btnPreviousMonth = (Button) findViewById(R.id.btnPreviousMonth);
        final Button btnNextMonth = (Button) findViewById(R.id.btnNextMonth);

        toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.calendar_activity);

        calendar = (CompactCalendarView) findViewById(R.id.compactCalendarView);
        calendar.setShouldShowMondayAsFirstDay(false);

        textView = (TextView) findViewById(R.id.textViewCalendar);
        textView.setText(dateFormatForMonth.format(calendar.getFirstDayOfCurrentMonth()));

        for(Dream dream : dreams) {

            Date date = null;
            try {
                date = sdf.parse(dream.getDay() + "/" + dream.getMonth() + "/" + dream.getYear());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Event event = new Event(Color.GREEN, date.getTime());
            calendar.addEvent(event);
        }

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            ArrayList<Dream> dreamsSameDay;

            @Override
            public void onDayClick(Date dateClicked) {
                textView.setText(dateFormatForMonth.format(dateClicked));

                List<Event> events = calendar.getEvents(dateClicked);
                dreamsSameDay = new ArrayList<Dream>(events.size());
                if(events.size() > 0) {
                    for(Event event : events) {
                        if (event.getTimeInMillis() == dateClicked.getTime()) {
                            for (Dream dream : dreams) {
                                String dateA = sdf.format(dateClicked);
                                String dateB;

                                if(dream.getDay() < 10)
                                    dateB = "0" + dream.getDay() + "/";
                                else
                                    dateB = dream.getDay() + "/";

                                if(dream.getMonth() < 10)
                                    dateB += "0" + dream.getMonth() + "/" + dream.getYear();
                                else
                                    dateB += dream.getMonth() + "/" + dream.getYear();

                                if (dateA.equals(dateB))
                                    dreamsSameDay.add(dream);
                            }
                        }
                        break;
                    }

                    Intent intentDreamsActivity;
                    if(dreamsSameDay.size() > 1) {
                        intentDreamsActivity = new Intent(CalendarActivity.this, SameDayActivity.class);
                        intentDreamsActivity.putExtra("dreams", dreamsSameDay);
                        startActivity(intentDreamsActivity);
                        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                    }
                    else {
                        intentDreamsActivity = new Intent(CalendarActivity.this, DreamsActivity.class);
                        intentDreamsActivity.putExtra("dream", dreamsSameDay.get(0));
                        startActivity(intentDreamsActivity);
                        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                    }
                }
                else Snackbar.make(calendar, R.string.calendar_without_dreams, Snackbar.LENGTH_SHORT).setAction("Action", null).show();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                textView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        btnPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.showPreviousMonth();
            }
        });

        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.showNextMonth();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CalendarActivity.this, MainNavDrawerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                Intent intent = new Intent(CalendarActivity.this, MainNavDrawerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
