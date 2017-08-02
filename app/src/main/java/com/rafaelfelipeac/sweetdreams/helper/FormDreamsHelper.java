package com.rafaelfelipeac.sweetdreams.helper;


import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.rafaelfelipeac.sweetdreams.activities.BaseActivity;
import com.rafaelfelipeac.sweetdreams.activities.FormDreamsActivity;
import com.rafaelfelipeac.sweetdreams.R;
import com.rafaelfelipeac.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * Created by Rafael Cordeiro on 22/10/2016.
 */

public class FormDreamsHelper extends BaseActivity {
    @BindView(R.id.form_dreams_title)
    EditText title;
    @BindView(R.id.form_dreams_date)
    TextView date;
    @BindView(R.id.tag_group_form)
    TagView tagGroup;

    private Dream dream;

    public FormDreamsHelper(FormDreamsActivity activity) {
        ButterKnife.bind(this, activity);
        dream = new Dream();
    }

    @OnTouch(R.id.form_dreams_title)
    public boolean titleTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
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
            lstTags += tag.text + ",";
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
