package com.sd.rafael.sweetdreams.helper;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.sd.rafael.sweetdreams.activity.BaseActivity;
import com.sd.rafael.sweetdreams.activity.FormDreamsActivity;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.activity.fragments.FormTextFragment;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafae on 22/10/2016.
 */

public class FormDreamsHelper extends BaseActivity {
    private final EditText title;
    //private EditText description;
    private final TextView date;

    private TagView tagGroup;

    private Dream dream;

    public FormDreamsHelper(FormDreamsActivity activity) {
        title = (EditText) activity.findViewById(R.id.form_dreams_title);

        title.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        date = (TextView) activity.findViewById(R.id.form_dreams_date);
        tagGroup = (TagView) activity.findViewById(R.id.tag_group_form);

        dream = new Dream();
    }

    public Dream getDream() {
        dream.setTitle(title.getText().toString());

        String[] arDate = date.getText().toString().split("/");
        dream.setDay(Integer.parseInt(arDate[0]));
        dream.setMonth(Integer.parseInt(arDate[1]));
        dream.setYear(Integer.parseInt(arDate[2]));

        List<Tag> tags = tagGroup.getTags();
        String lstTags = "";

        for(com.cunoraz.tagview.Tag tag : tags) {
            lstTags += tag.text + ", ";
        }

        dream.setTags(lstTags);

        return dream;
    }

    public void makeDream(Dream dream) {
        title.setText(dream.getTitle());
        date.setText(String.format("%02d", dream.getDay()) + "/" + String.format("%02d", dream.getMonth()) + "/" + dream.getYear());

        List<com.cunoraz.tagview.Tag> tags = new ArrayList<>(dream.tagConvertStringToArray().length);
        String[] lstTags = dream.tagConvertStringToArray();

        for(String s : lstTags) {
            if(!s.equals("")) {
                com.cunoraz.tagview.Tag tag = new com.cunoraz.tagview.Tag(s);
                tag.radius = 10f;
                tag.layoutColor = Color.rgb(95, 170, 223);
                tag.isDeletable = true;
                tags.add(tag);
            }
        }

        tagGroup.addTags(tags);

        this.dream = dream;
    }
}
