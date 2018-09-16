package com.example.simpledemo.view.userDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.simpledemo.view.events.EventsAdapter;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentUserDetails extends Fragment implements UserDetailsContract.View {

    private static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    @BindView(R.id.recycler_view) protected RecyclerView eventsList;
    @BindView(R.id.username) protected TextView username;
    @BindView(R.id.about_me) protected TextView aboutMe;
    @BindView(R.id.company) protected TextView company;
    @BindView(R.id.mobile) protected TextView mobile;
    @BindView(R.id.phone) protected TextView phone;
    @BindView(R.id.address) protected TextView address;
    @BindView(R.id.name) protected TextView name;
    @BindView(R.id.initials) protected TextView initials;
    @BindView(R.id.profile_photo) protected ImageView profilePhoto;
    @BindView(R.id.banner_photo) protected ImageView bannerPhoto;

    protected UserDetailsContract.Presenter presenter;
    private String userId;

    public static FragmentUserDetails newInstance(String userId) {
        FragmentUserDetails fragment = new FragmentUserDetails();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_ID, userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_details, container, false);
        ButterKnife.bind(this, rootView);
        initRecyclerView();

        userId = savedInstanceState == null ? getArguments().getString(EXTRA_USER_ID) :
                savedInstanceState.getString(EXTRA_USER_ID);

        presenter = MainApplication.getInstance().graph().getUserDetailsPresenter();
        presenter.setView(this);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_USER_ID, userId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.getUser(userId);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        eventsList.setLayoutManager(manager);
    }

    @Override
    public void showUser(User user, List<Event> events) {
        renderUser(user);
        renderEvents(events);
    }

    private void renderUser(User user) {
        username.setText(user.getUsername());
        name.setText(user.getName());
        aboutMe.setText(user.getAboutMe());
        address.setText(user.getAddress().formattedName());
        company.setText(user.getCompanyName());
        mobile.setText(user.getMobilePhone());
        phone.setText(user.getPhone());

        if (TextUtils.isEmpty(user.getPhotoUrl())) {
            initials.setVisibility(View.VISIBLE);
            profilePhoto.setVisibility(View.GONE);
            initials.setText(user.getName().substring(0, 2));
        } else {
            initials.setVisibility(View.GONE);
            profilePhoto.setVisibility(View.VISIBLE);
            new Picasso.Builder(MainApplication.getInstance())
                    .loggingEnabled(true)
                    .downloader(new OkHttp3Downloader(MainApplication.getInstance().graph().getOkHttpClient()))
                    .build()
                    .load(user.getPhotoUrl())
                    .fit()
                    .centerCrop()
                    .transform(new CircleTransform())
                    .into(profilePhoto);
        }

        if (TextUtils.isEmpty(user.getBannerPhotoUrl())) {
            bannerPhoto.setBackgroundResource(R.drawable.empty_background);
        } else {
            new Picasso.Builder(MainApplication.getInstance())
                    .loggingEnabled(true)
                    .downloader(new OkHttp3Downloader(MainApplication.getInstance().graph().getOkHttpClient()))
                    .build()
                    .load(user.getBannerPhotoUrl())
                    .fit()
                    .centerCrop()
                    .into(bannerPhoto);
        }
    }

    private void renderEvents(List<Event> events) {
        EventsAdapter adapter = (EventsAdapter) eventsList.getAdapter();
        if (adapter == null) {
            adapter = new EventsAdapter();
            eventsList.setAdapter(adapter);
        }

        adapter.replaceItems(events);
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
