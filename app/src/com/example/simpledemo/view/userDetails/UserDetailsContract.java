package com.example.simpledemo.view.userDetails;

import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.model.pojo.domain.User;

import java.util.List;

public interface UserDetailsContract {

    interface View {
        void showUser(User user, List<Event> events);
        void showError(String error);
    }

    interface Presenter {
        void setView(View view);
        void getUser(String userId);
        void viewDestroyed();
    }
}
