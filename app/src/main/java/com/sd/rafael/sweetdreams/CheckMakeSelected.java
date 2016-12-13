package com.sd.rafael.sweetdreams;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.sd.rafael.sweetdreams.models.Dream;

/**
 * Created by rafae on 28/10/2016.
 */

public class CheckMakeSelected {
    private Activity activity;

    public CheckMakeSelected(Activity activity) {
        this.activity = activity;
    }

    /*public String[] checkSelected() {
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
            String[] array = dream.tagConvertStringToArray();
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
    }*/
}
