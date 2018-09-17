package com.example.simpledemo.view.eventDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.model.pojo.domain.User;
import com.example.simpledemo.utils.CircleTransform;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailsFragment extends Fragment implements EventDetailsContract.View {

    private static final String EXTRA_EVENT_ID = "EVENT_ID";

    @BindView(R.id.event_name) protected TextView eventName;
    @BindView(R.id.event_details) protected TextView eventLocation;
    @BindView(R.id.event_description) protected TextView eventDescription;
    @BindView(R.id.name) protected TextView name;
    @BindView(R.id.initials) protected TextView initials;
    @BindView(R.id.company) protected TextView company;
    @BindView(R.id.profile_photo) protected ImageView profilePhoto;

    protected EventDetailsContract.Presenter presenter;
    private String eventId;

    public static EventDetailsFragment newInstance(String eventId) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_EVENT_ID, eventId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, rootView);

        eventId = savedInstanceState == null ? getArguments().getString(EXTRA_EVENT_ID) :
                savedInstanceState.getString(EXTRA_EVENT_ID);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter = MainApplication.getInstance().graph().getEventDetailsPresenter();
        presenter.setView(this);
        presenter.getEvent(eventId);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_EVENT_ID, eventId);
    }


    @Override
    public void showEvent(User user, Event event) {
        renderUser(user);
        renderEvent(event);
    }

    private void renderUser(User user) {
        name.setText(user.getName());
        company.setText(user.getCompanyName());

        if (TextUtils.isEmpty(user.getPhotoUrl())) {
            initials.setVisibility(View.VISIBLE);
            profilePhoto.setVisibility(View.GONE);
            initials.setText(user.getName().substring(0, 2));
        } else {
            initials.setVisibility(View.GONE);
            profilePhoto.setVisibility(View.VISIBLE);
            new Picasso.Builder(MainApplication.getInstance())
                    .downloader(new OkHttp3Downloader(MainApplication.getInstance().graph().getOkHttpClient()))
                    .build()
                    .load(user.getPhotoUrl())
                    .fit()
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(profilePhoto);
        }
    }

    private void renderEvent(Event event) {
        eventName.setText(event.getSubject());
        eventDescription.setText(event.getDescription());

        SimpleDateFormat format = new SimpleDateFormat("MMM dd, HH:mm");
        String activityTime = format.format(event.getStartDateTime());
        activityTime += " - " + format.format(event.getEndDateTime());
        eventLocation.setText(activityTime + " @ " + event.getLocation());
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
