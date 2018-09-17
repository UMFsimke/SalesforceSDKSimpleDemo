package com.example.simpledemo.view.users;

import com.example.simpledemo.model.pojo.domain.User;

import java.util.List;

public interface UsersListContract {

    interface View {
        void showUsers(List<User> users);
        void showError(String error);
    }

    interface Presenter {
        void setView(View view);
        void viewShown();
        void viewDestroyed();
    }
}
