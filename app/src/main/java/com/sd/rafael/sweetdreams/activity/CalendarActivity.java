package com.sd.rafael.sweetdreams.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.sd.rafael.sweetdreams.DAO.DreamDAO;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.models.Dream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    CompactCalendarView calendar;
    TextView textView;
    SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_calendar);

        final DreamDAO dao = new DreamDAO(this);
        final List<Dream> dreams = dao.Read();
        final Button btnPreviousMonth = (Button) findViewById(R.id.btnPreviousMonth);
        final Button btnNextMonth = (Button) findViewById(R.id.btnNextMonth);

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
            @Override
            public void onDayClick(Date dateClicked) {
                textView.setText(dateFormatForMonth.format(dateClicked));

                List<Event> events = calendar.getEvents(dateClicked);
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


                                if (dateA.equals(dateB)) {
                                    Intent intentDreamsActivity = new Intent(CalendarActivity.this, DreamsActivity.class);
                                    intentDreamsActivity.putExtra("dream", dream);
                                    startActivity(intentDreamsActivity);
                                }
                            }
                        }
                    }
                }
                else
                    Snackbar.make(calendar, "Não há sonhos salvos nesse dia.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
}
