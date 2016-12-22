package com.sd.rafael.sweetdreams.helper;


import android.graphics.Color;
import android.widget.EditText;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.sd.rafael.sweetdreams.activity.FormDreamsActivity;
import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.models.Dream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafae on 22/10/2016.
 */

public class FormDreamsHelper {
    private final EditText title;
    private final EditText description;
    private final TextView date;

    private TagView tagGroup;

    private Dream dream;


    public FormDreamsHelper(FormDreamsActivity activity) {
        title = (EditText) activity.findViewById(R.id.form_dreams_title);
        description = (EditText) activity.findViewById(R.id.form_dreams_description);
        date = (TextView) activity.findViewById(R.id.form_dreams_date);
        tagGroup = (TagView) activity.findViewById(R.id.tag_group_form);

        dream = new Dream();
    }

    public Dream getDream() {
        dream.setTitle(title.getText().toString());
        dream.setDescription(description.getText().toString());

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
        description.setText(dream.getDescription());
        date.setText(dream.getDay() + "/" + dream.getMonth() + "/" + dream.getYear());

        List<com.cunoraz.tagview.Tag> tags = new ArrayList<>(dream.tagConvertStringToArray().length);
        String[] lstTags = dream.tagConvertStringToArray();

        for(String s : lstTags) {
            if(!s.equals("")) {
                com.cunoraz.tagview.Tag tag = new com.cunoraz.tagview.Tag(s);
                tag.radius = 10f;
                tag.layoutColor = Color.rgb(0, 149, 255);
                tag.isDeletable = true;
                tags.add(tag);
            }
        }

        tagGroup.addTags(tags);


        this.dream = dream;
    }



}
