package com.sd.rafael.sweetdreams.activity;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        DreamDAO dao = new DreamDAO(this);
        final List<Dream> dreams = dao.Read();

        textView = (TextView) findViewById(R.id.textViewCalendar);

        calendar = (CompactCalendarView) findViewById(R.id.compactCalendarView);
        calendar.setShouldShowMondayAsFirstDay(false);

        for(Dream dream : dreams) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
                Snackbar.make(calendar, dateClicked.toString(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //toolbar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
                textView.setText(dateFormatForMonth.format(firstDayOfNewMonth));

            }
        });
    }
}
