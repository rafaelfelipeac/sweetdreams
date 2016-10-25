package com.sd.rafael.sweetdreams;

import android.app.Activity;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sd.rafael.sweetdreams.models.Dream;

/**
 * Created by rafae on 22/10/2016.
 */

public class FormDreamsHelper {
    private final EditText title;
    private final EditText description;
    private final RatingBar grade;
    private final TextView date;
    private final TextView time;



    private Dream dream;

    public FormDreamsHelper(FormDreamsActivity activity) {
        title = (EditText) activity.findViewById(R.id.form_dreams_title);
        description = (EditText) activity.findViewById(R.id.form_dreams_description);
        grade = (RatingBar) activity.findViewById(R.id.form_dreams_grade);
        date = (TextView) activity.findViewById(R.id.form_dreams_date);
        time = (TextView) activity.findViewById(R.id.form_dreams_time);

        dream = new Dream();
    }

    public Dream getDream() {
        dream.setTitle(title.getText().toString());
        dream.setDescription(description.getText().toString());
        dream.setGrade(Double.valueOf(grade.getProgress()));
        //dream.setHour();

        return dream;
    }

    public void makeDream(Dream dream) {
        title.setText(dream.getTitle());
        description.setText(dream.getDescription());
        grade.setProgress(dream.getGrade().intValue());
        date.setText(dream.getDay() + "/" + dream.getMonth() + "/" + dream.getYear());

        this.dream = dream;
    }
}
