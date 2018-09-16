package com.example.simpledemo.model.di;

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
}
