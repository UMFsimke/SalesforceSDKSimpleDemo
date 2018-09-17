package com.example.simpledemo.view.events;

import com.example.simpledemo.model.pojo.domain.Event;

import java.util.List;

public interface EventsListContract {

    interface View {
        void showEvents(List<Event> events);
        void showError(String error);
    }

    interface Presenter {
        void setView(View view);
        void viewShown();
        void viewDestroyed();
    }
}
