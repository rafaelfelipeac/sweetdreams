package com.rafaelfelipeac.sweetdreams.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rafaelfelipeac.sweetdreams.R;
import com.rafaelfelipeac.sweetdreams.activity.FormDreamsActivity;

public class FormTextFragment extends Fragment {

    private View view;
    private EditText description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_form_text, container, false);

        description = (EditText) view.findViewById(R.id.form_dreams_description);
        description.setOnTouchListener(new View.OnTouchListener() {
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

        ((FormDreamsActivity)getActivity()).getViewFromTextFragment(view);


        return view;


    }
}
