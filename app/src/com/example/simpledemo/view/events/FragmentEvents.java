package com.example.simpledemo.view.events;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.pojo.domain.Event;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentEvents extends Fragment implements EventsListContract.View {

    @BindView(R.id.recycler_view) protected RecyclerView eventsList;
    protected EventsListContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        initRecyclerView();
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
