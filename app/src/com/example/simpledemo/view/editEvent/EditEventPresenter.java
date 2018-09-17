package com.example.simpledemo.view.editEvent;

import android.support.v4.util.Pair;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.model.pojo.domain.User;
import com.example.simpledemo.model.repository.EventsRepository;
import com.example.simpledemo.model.repository.UserRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditEventPresenter implements EditEventContract.Presenter {

    private EditEventContract.View view;
    private EventsRepository eventsRepository;
    private UserRepository userRepository;
    private CompositeDisposable compositeDisposable;
    private Event event;
    private List<User> users;

    public EditEventPresenter() {
        compositeDisposable = new CompositeDisposable();
        eventsRepository = MainApplication.getInstance().graph().getEventsRepository();
        userRepository = MainApplication.getInstance().graph().getUserRepository();
    }

    @Override
    public void setView(EditEventContract.View view) {
        this.view = view;
    }

    @Override
    public void getEvent(String eventId) {
        if (eventId != null) {
            Disposable disposable = Observable.combineLatest(eventsRepository.getEvent(eventId),
                    userRepository.getAll(), Pair::new)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(eventAndUsers -> {
                        if (view == null) {
                            return;
                        }

                        event = eventAndUsers.first;
                        users = eventAndUsers.second;
                        view.showEvent(eventAndUsers.first, eventAndUsers.second);
                    }, error -> {
                        if (view == null) {
                            return;
                        }
                        view.showError(error.getLocalizedMessage());
                    });

            compositeDisposable.add(disposable);
        } else {
            Disposable disposable = userRepository.getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(users -> {
                        if (view == null) { return; }

                        event = null;
                        EditEventPresenter.this.users = users;
                        view.showEvent(null, users);
                    }, error -> {
                        if (view == null) {
                            return;
                        }
                        view.showError(error.getLocalizedMessage());
                    });
            compositeDisposable.add(disposable);
        }
    }

    @Override
    public void viewDestroyed() {
        view = null;
        compositeDisposable.dispose();
    }
}