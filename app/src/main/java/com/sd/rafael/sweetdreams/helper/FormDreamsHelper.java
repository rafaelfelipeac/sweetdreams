package com.sd.rafael.sweetdreams.helper;


import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sd.rafael.sweetdreams.CheckMakeSelected;
import com.sd.rafael.sweetdreams.activity.FormDreamsActivity;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.models.Dream;

/**
 * Created by rafae on 22/10/2016.
 */

public class FormDreamsHelper {
    private final EditText title;
    private final EditText description;
    private final RatingBar grade;
    private final TextView date;
    private final CheckMakeSelected cmS;

    private Dream dream;


    public FormDreamsHelper(FormDreamsActivity activity) {
        title = (EditText) activity.findViewById(R.id.form_dreams_title);
        description = (EditText) activity.findViewById(R.id.form_dreams_description);
        grade = (RatingBar) activity.findViewById(R.id.form_dreams_grade);
        date = (TextView) activity.findViewById(R.id.form_dreams_date);

        cmS = new CheckMakeSelected(activity);
        dream = new Dream();
    }

    public Dream getDream() {
        dream.setTitle(title.getText().toString());
        dream.setDescription(description.getText().toString());
        dream.setGrade(Double.valueOf(grade.getProgress()));

        String[] arDate = date.getText().toString().split("/");
        dream.setDay(Integer.parseInt(arDate[0]));
        dream.setMonth(Integer.parseInt(arDate[1]));
        dream.setYear(Integer.parseInt(arDate[2]));

        String txt = dream.tagConvertArrayToString(cmS.checkSelected());
        dream.setTags(txt);

        return dream;
    }

    public void makeDream(Dream dream) {
        title.setText(dream.getTitle());
        description.setText(dream.getDescription());
        grade.setProgress(dream.getGrade().intValue());
        date.setText(dream.getDay() + "/" + dream.getMonth() + "/" + dream.getYear());

        cmS.makeSelected(dream);

        this.dream = dream;
    }



}
