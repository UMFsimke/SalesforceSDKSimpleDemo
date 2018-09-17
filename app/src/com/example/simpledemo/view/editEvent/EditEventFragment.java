package com.example.simpledemo.view.editEvent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.model.pojo.domain.User;
import com.example.simpledemo.view.eventDetails.EventDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditEventFragment extends Fragment implements EditEventContract.View,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    private static final String FORMAT_PATTERN = "dd MMM yyyy, HH:mm";

    @BindView(R.id.event_name) protected EditText eventName;
    @BindView(R.id.event_description) protected EditText eventDescription;
    @BindView(R.id.event_location) protected EditText eventLocation;
    @BindView(R.id.event_start_time) protected TextView eventStartTime;
    @BindView(R.id.event_end_time) protected TextView eventEndTime;
    @BindView(R.id.event_organizer) protected Spinner eventOrganizer;

    private String eventId;
    protected EditEventContract.Presenter presenter;
    private boolean pickingStartDate;
    private int selectedYear, selectedMonth, selectedDay;

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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter = MainApplication.getInstance().graph().getEditEventPresenter();
        presenter.setView(this);
        presenter.getEvent(eventId);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_EVENT_ID, eventId);
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

        SimpleDateFormat format = new SimpleDateFormat(FORMAT_PATTERN);
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

    @OnClick(R.id.event_start_time)
    void onStartTimeClicked() {
        pickingStartDate = true;
        showDatePicker(eventStartTime.getText().toString());

    }

    private void showDatePicker(String date) {
        Date actualDate;
        if (TextUtils.isEmpty(date)) {
            actualDate = new Date();
        } else {
            SimpleDateFormat format = new SimpleDateFormat(FORMAT_PATTERN);
            try {
                actualDate = format.parse(date);
            } catch (ParseException e) {
                actualDate = new Date();
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(actualDate);
        new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        this.selectedDay = day;
        this.selectedMonth = month;
        this.selectedYear = year;
        showTimePicker();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), this, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selectedYear);
        calendar.set(Calendar.MONTH, selectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_PATTERN);
        if (pickingStartDate) {
            eventStartTime.setText(format.format(calendar.getTime()));
        } else {
            eventEndTime.setText(format.format(calendar.getTime()));
        }
    }

    @OnClick(R.id.event_end_time)
    void onEndTimeClicked() {
        pickingStartDate = false;
        showDatePicker(eventEndTime.getText().toString());
    }

    @OnClick(R.id.save)
    void onSaveClicked() {
        String name = eventName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Event name cant be empty", Toast.LENGTH_LONG).show();
            return;
        }

        String location = eventLocation.getText().toString();
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(getContext(), "Event location cant be empty", Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat(FORMAT_PATTERN);
        String startAsString = eventStartTime.getText().toString();
        String endAsString = eventEndTime.getText().toString();
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = format.parse(startAsString);
            endDate = format.parse(endAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (startDate == null || endDate == null) {
            Toast.makeText(getContext(), "Event start and end cant be empty", Toast.LENGTH_LONG).show();
            return;
        }

        presenter.saveEvent(name,
                eventDescription.getText().toString(),
                eventLocation.getText().toString(),
                startDate,
                endDate,
                eventOrganizer.getSelectedItemPosition());
    }

    @Override
    public void showEventSaved() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.event_saved_title)
                .setMessage(R.string.event_saved_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    getActivity().setResult(EventDetailsActivity.EVENT_CHANGED);
                    getActivity().finish();
                })
                .create()
                .show();
    }

    @Override
    public void showEventFailedToSave() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.error_title)
                .setMessage(R.string.event_failed_to_save)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }

    @OnClick(R.id.delete)
    void onDeleteClicked() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_event)
                .setMessage(R.string.delete_event_are_you_sure)
                .setPositiveButton(R.string.btn_continue, (dialog, i) -> presenter.deleteEvent())
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    @Override
    public void showEventDeleted() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.event_deleted_title)
                .setMessage(R.string.event_deleted_message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    getActivity().setResult(EventDetailsActivity.EVENT_DELETED);
                    getActivity().finish();
                })
                .create()
                .show();
    }

    @Override
    public void showEventFailedToDelete() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.error_title)
                .setMessage(R.string.event_failed_to_delete)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }
}
