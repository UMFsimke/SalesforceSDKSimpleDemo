package com.example.simpledemo.view.editEvent;

import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.model.pojo.domain.User;

import java.util.Date;
import java.util.List;

public interface EditEventContract {

    interface View {
        void showEvent(Event event, List<User> users);
        void showError(String error);
        void showEventSaved();
        void showEventFailedToSave();
        void showEventDeleted();
        void showEventFailedToDelete();
    }

    interface Presenter {
        void setView(View view);
        void getEvent(String eventId);
        void viewDestroyed();
        void deleteEvent();
        void saveEvent(String name, String description, String location,
                       Date startDate, Date endDate, int selectedOrganizer);
    }
}
