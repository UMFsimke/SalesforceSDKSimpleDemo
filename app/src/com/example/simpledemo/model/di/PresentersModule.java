package com.example.simpledemo.model.di;

import com.example.simpledemo.view.editEvent.EditEventContract;
import com.example.simpledemo.view.editEvent.EditEventPresenter;
import com.example.simpledemo.view.eventDetails.EventDetailsContract;
import com.example.simpledemo.view.eventDetails.EventDetailsPresenter;
import com.example.simpledemo.view.events.EventsListContract;
import com.example.simpledemo.view.events.EventsListPresenter;
import com.example.simpledemo.view.userDetails.UserDetailsContract;
import com.example.simpledemo.view.userDetails.UserDetailsPresenter;
import com.example.simpledemo.view.users.UserListPresenter;
import com.example.simpledemo.view.users.UsersListContract;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentersModule {

    @Provides
    UsersListContract.Presenter providesUsersListPresenter() {
        return new UserListPresenter();
    }

    @Provides
    EventsListContract.Presenter providesEventsListPresenter() {
        return new EventsListPresenter();
    }

    @Provides
    UserDetailsContract.Presenter providesUserDetailsPresenter() {
        return new UserDetailsPresenter();
    }

    @Provides
    EventDetailsContract.Presenter providesEventDetailsPresenter() {
        return new EventDetailsPresenter();
    }

    @Provides
    EditEventContract.Presenter providesEditEventPresenter() {
        return new EditEventPresenter();
    }
}
