package com.example.simpledemo.view.userDetails;

import android.support.v4.util.Pair;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.model.repository.EventsRepository;
import com.example.simpledemo.model.repository.UserRepository;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserDetailsPresenter implements UserDetailsContract.Presenter {

    private UserDetailsContract.View view;
    private UserRepository userRepository;
    private EventsRepository eventsRepository;
    private CompositeDisposable compositeDisposable;

    public UserDetailsPresenter() {
        compositeDisposable = new CompositeDisposable();
        userRepository = MainApplication.getInstance().graph().getUserRepository();
        eventsRepository = MainApplication.getInstance().graph().getEventsRepository();
    }

    @Override
    public void setView(UserDetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void getUser(String userId) {
        Disposable disposable = Observable.combineLatest(userRepository.fetch(userId),
                eventsRepository.getEventsForUser(userId),
                    (user, events) -> new Pair<>(user, events))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( userAndEvents -> {
                    if (view == null) { return; }
                    view.showUser(userAndEvents.first, userAndEvents.second);
                }, error -> {
                    if (view == null) { return; }
                    view.showError(error.getLocalizedMessage());
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void viewDestroyed() {
        view = null;
        compositeDisposable.dispose();
    }
}