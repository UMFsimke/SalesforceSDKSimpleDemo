package com.example.simpledemo.view.eventDetails;

import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.model.pojo.domain.User;

public interface EventDetailsContract {

    interface View {
        void showEvent(User user, Event event);
        void showError(String error);
    }

    interface Presenter {
        void setView(View view);
        void getEvent(String eventId);
        void viewDestroyed();
    }
}
