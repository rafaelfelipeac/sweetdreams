package com.sd.rafael.sweetdreams.helper;

import android.widget.RatingBar;
import android.widget.TextView;

import com.sd.rafael.sweetdreams.CheckMakeSelected;
import com.sd.rafael.sweetdreams.activity.DreamsActivity;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.models.Dream;

/**
 * Created by rafae on 28/10/2016.
 */

public class DreamsHelper {
    private TextView datetime;
    private TextView title;
    private TextView description;
    private TextView tags;
    //private RatingBar grade;

    private CheckMakeSelected cmS;

    private Dream dream;

    public DreamsHelper(DreamsActivity activity) {
        datetime = (TextView) activity.findViewById(R.id.dreams_datetime);
        title = (TextView) activity.findViewById(R.id.dreams_title);
        description = (TextView) activity.findViewById(R.id.dreams_description);
        tags = (TextView) activity.findViewById(R.id.dreams_tags);
//        grade = (RatingBar) activity.findViewById(R.id.dreams_grade);


        dream = new Dream();
        cmS = new CheckMakeSelected(activity);
    }

    public Dream getDream() {
        dream.setTitle(title.getText().toString());
        dream.setDescription(description.getText().toString());
        //dream.setGrade(Double.valueOf(grade.getProgress()));

        String[] datetimeArr = datetime.getText().toString().split(" - ");

        String[] arDate = datetimeArr[0].toString().split("/");
        dream.setDay(Integer.parseInt(arDate[0]));
        dream.setMonth(Integer.parseInt(arDate[1]));
        dream.setYear(Integer.parseInt(arDate[2]));

        dream.setTags(tags.getText().toString());

        return dream;
    }

    public void makeDream(Dream dream) {
        title.setText(dream.getTitle());
        description.setText(dream.getDescription());
        //grade.setProgress(dream.getGrade().intValue());
        datetime.setText(dream.getDay() + "/" + dream.getMonth() + "/" + dream.getYear());
        tags.setText(dream.getTags());

        this.dream = dream;
    }



}
