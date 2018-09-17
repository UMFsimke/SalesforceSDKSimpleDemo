package com.example.simpledemo.view.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.view.editEvent.EditEventActivity;
import com.example.simpledemo.view.eventDetails.EventDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentEvents extends Fragment implements EventsListContract.View, EventsAdapter.OnEventClickedListener {

    @BindView(R.id.recycler_view) protected RecyclerView eventsList;
    @BindView(R.id.add_btn) protected FloatingActionButton addBtn;
    protected EventsListContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        addBtn.setVisibility(View.VISIBLE);
        presenter = MainApplication.getInstance().graph().getEventsListPresenter();
        presenter.setView(this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.viewShown();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        eventsList.setLayoutManager(manager);
    }

    @Override
    public void showEvents(List<Event> events) {
        EventsAdapter adapter = (EventsAdapter) eventsList.getAdapter();
        if (adapter == null) {
            adapter = new EventsAdapter();
            adapter.setOnEventClickedListener(this);
            eventsList.setAdapter(adapter);
        }

        adapter.replaceItems(events);
    }

    @Override
    public void onEventClicked(Event event) {
        Intent intent = new Intent(getContext(), EventDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EventDetailsActivity.EXTRA_EVENT_ID, event.getId());
        intent.putExtras(bundle);
        startActivity(intent);
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

    @OnClick(R.id.add_btn)
    void onAddClicked() {
        Intent intent = EditEventActivity.newInstance(getContext(), null);
        startActivity(intent);
    }
}
