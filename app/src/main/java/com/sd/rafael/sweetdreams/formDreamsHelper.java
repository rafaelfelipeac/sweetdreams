package com.sd.rafael.sweetdreams;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    private Activity activity;
    private String separator = ",";

    public FormDreamsHelper(FormDreamsActivity activity) {
        this.activity = activity;
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

        String[] arDate = date.getText().toString().split("/");
        dream.setDay(Integer.parseInt(arDate[0]));
        dream.setMonth(Integer.parseInt(arDate[1]));
        dream.setYear(Integer.parseInt(arDate[2]));

        String[] arTime = time.getText().toString().split("h");
        dream.setHour(Integer.parseInt(arTime[0]));
        dream.setMinute(Integer.parseInt(arTime[1]));

        String txt = convertArrayToString();
        dream.setTags(txt);

        return dream;
    }

    public void makeDream(Dream dream) {
        title.setText(dream.getTitle());
        description.setText(dream.getDescription());
        grade.setProgress(dream.getGrade().intValue());
        date.setText(dream.getDay() + "/" + dream.getMonth() + "/" + dream.getYear());
        time.setText(dream.getHour() + "h" + dream.getMinute());

        makeSelected(dream);

        this.dream = dream;
    }

    public String[] convertStringToArray(String str) {
        String[] array = str.split(separator);
        return array;
    }

    public String convertArrayToString() {
        String[] array = checkSelected();
        String s = "";

        for(int i = 0; i< array.length; i++) {
            if(array[i] != null && array[i] != "null") {
                s += array[i];

                if(i < array.length -1)
                    s += separator;
            }
        }

        return s;
    }

    private String[] checkSelected() {
        final LinearLayout ll = (LinearLayout) activity.findViewById(R.id.form_dreams_ll);
        String[] array = new String[ll.getChildCount()];
        int cont = 0;

        for(int i = 0; i< ll.getChildCount(); i++) {
            CheckBox cb = (CheckBox) ll.getChildAt(i);

            if(cb.isChecked()) {
                array[cont] = cb.getText().toString();
                cont++;
            }
        }

        return array;
    }

    public void makeSelected(Dream dream) {
        String s = dream.getTags();

        if(s == null)
            return;
        else {
            String[] array = convertStringToArray(s);
            final LinearLayout ll = (LinearLayout) activity.findViewById(R.id.form_dreams_ll);

            for(int i = 0; i< array.length; i++) {
                if(array[i] != "null" || array[i] != null) {
                    CheckBox cb = new CheckBox(activity);
                    cb.setChecked(true);
                    cb.setText(array[i]);
                    ll.addView(cb);
                }
            }
        }
    }
}
