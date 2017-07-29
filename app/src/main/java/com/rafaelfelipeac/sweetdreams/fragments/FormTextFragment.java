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
import com.rafaelfelipeac.sweetdreams.activities.FormDreamsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

public class FormTextFragment extends Fragment {

    private View view;

    @BindView(R.id.form_dreams_description)
    EditText description;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_form_text, container, false);

        ButterKnife.bind(this, view);

        ((FormDreamsActivity)getActivity()).getViewFromTextFragment(view);

        return view;
    }

    @OnTouch(R.id.form_dreams_description)
    public boolean descriptionTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
    }
}
