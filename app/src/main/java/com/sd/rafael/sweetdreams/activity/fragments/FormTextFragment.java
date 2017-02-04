package com.sd.rafael.sweetdreams.activity.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sd.rafael.sweetdreams.R;
import com.sd.rafael.sweetdreams.activity.FormDreamsActivity;

public class FormTextFragment extends Fragment {

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //descSetText("passaro");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_form_text, container, false);

        ((FormDreamsActivity)getActivity()).getViewFromTextFragment(view);

        return view;
    }

    public String descGetText() {
        EditText description = (EditText) view.findViewById(R.id.form_dreams_description);
        return description.getText().toString();
    }

    public void descSetText(String text) {
        EditText description = (EditText) view.findViewById(R.id.form_dreams_description);
        description.setText(text);
    }
}
