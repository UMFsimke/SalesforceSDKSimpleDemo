package com.example.simpledemo.view.editEvent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.model.pojo.domain.User;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditEventFragment extends Fragment implements EditEventContract.View {

    private static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";

    @BindView(R.id.event_name) protected EditText eventName;
    @BindView(R.id.event_description) protected EditText eventDescription;
    @BindView(R.id.event_location) protected EditText eventLocation;
    @BindView(R.id.event_start_time) protected TextView eventStartTime;
    @BindView(R.id.event_end_time) protected TextView eventEndTime;
    @BindView(R.id.event_organizer) protected Spinner eventOrganizer;

    private String eventId;
    protected EditEventContract.Presenter presenter;

    public static EditEventFragment newInstance(String eventId) {
        EditEventFragment fragment = new EditEventFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_EVENT_ID, eventId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_event, container, false);
        ButterKnife.bind(this, rootView);

        eventId = savedInstanceState == null ? getArguments().getString(EXTRA_EVENT_ID) :
                savedInstanceState.getString(EXTRA_EVENT_ID);


        presenter = MainApplication.getInstance().graph().getEditEventPresenter();
        presenter.setView(this);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_EVENT_ID, eventId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getEvent(eventId);
    }

    @Override
    public void showEvent(Event event, List<User> users) {
        renderUsers(users);
        renderEvent(event);
    }

    private void renderUsers(List<User> users) {
        String[] arraySpinner = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            arraySpinner[i] = users.get(i).getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventOrganizer.setAdapter(adapter);
    }

    private void renderEvent(Event event) {
        eventName.setText(event != null ? event.getSubject() : null);
        eventDescription.setText(event != null ? event.getDescription() : null);

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, HH:mm");
        eventStartTime.setText(event != null ? format.format(event.getStartDateTime()) : getString(R.string.set_start_date));
        eventEndTime.setText(event != null ? format.format(event.getEndDateTime()) : getString(R.string.set_end_date));
        eventLocation.setText(event != null ? event.getLocation() : null);
    }

    @Override
    public void showError(String error) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.error_title)
                .setMessage(error)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.viewDestroyed();
    }
}
