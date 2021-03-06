package com.rafaelfelipeac.sweetdreams.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalendarActivity extends BaseActivity {

    SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private ActionBar toolbar;

    private List<Dream> dreams;

    @BindView(R.id.textViewCalendar)
    TextView textView;
    @BindView(R.id.compactCalendarView)
    CompactCalendarView calendar;
    @BindView(R.id.btnPreviousMonth)
    Button btnPreviousMonth;
    @BindView(R.id.btnNextMonth)
    Button btnNextMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ButterKnife.bind(this);

        toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.calendar_activity);

        calendar.setShouldShowMondayAsFirstDay(false);

        textView.setText(dateFormatForMonth.format(calendar.getFirstDayOfCurrentMonth()));

        this.dreams = new DreamDAO(this).Read();

        loadDreams();

        setCalendar();
    }

    private void setCalendar() {
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                textView.setText(dateFormatForMonth.format(dateClicked));

                List<Event> events = calendar.getEvents(dateClicked);
                ArrayList<Dream> dreamsSameDay = new ArrayList<Dream>(events.size());
                if(events.size() > 0) {
                    //for(Event event : events) {
                        //if (event.getTimeInMillis() == dateClicked.getTime()) {

                        //}
                    //}

                    for (Dream dream : dreams) {
                        String dateA = sdf.format(dateClicked);
                        String dateB = setDateB(dream);

                        if (dateA.equals(dateB))
                            dreamsSameDay.add(dream);
                    }

                    Intent intentDreamsActivity;
                    if(dreamsSameDay.size() > 1) {
                        intentDreamsActivity = new Intent(CalendarActivity.this, SameDayActivity.class);
                        intentDreamsActivity.putExtra("dreams", dreamsSameDay);
                    }
                    else {
                        intentDreamsActivity = new Intent(CalendarActivity.this, DreamsActivity.class);
                        intentDreamsActivity.putExtra("dream", dreamsSameDay.get(0));
                    }
                    startActivity(intentDreamsActivity);
                    overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                }
                else
                    Snackbar.make(calendar, R.string.calendar_without_dreams, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                textView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });
    }

    private String setDateB(Dream dream) {
        String dateB;

        dateB = (dream.getDay() < 10) ? "0" + dream.getDay() + "/" : dream.getDay() + "/";
        dateB += (dream.getMonth() < 10) ? "0" + dream.getMonth() + "/" + dream.getYear() : dream.getMonth() + "/" + dream.getYear();

        return dateB;
    }

    private void loadDreams() {
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
    }

    @OnClick(R.id.btnPreviousMonth)
    public void btnPreviousMonthClick() {
        calendar.showPreviousMonth();
    }

    @OnClick(R.id.btnNextMonth)
    public void btnNextMonthClick() {
        calendar.showNextMonth();
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
                Intent intent = new Intent(CalendarActivity.this, MainNavDrawerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }
}
