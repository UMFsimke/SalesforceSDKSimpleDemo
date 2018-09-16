package com.example.simpledemo.view.events;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.model.repository.EventsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventsListPresenter implements EventsListContract.Presenter {

    private EventsListContract.View view;
    private EventsRepository eventsRepository;
    private CompositeDisposable compositeDisposable;

    public EventsListPresenter() {
        compositeDisposable = new CompositeDisposable();
        eventsRepository = MainApplication.getInstance().graph().getEventsRepository();
    }

    @Override
    public void setView(EventsListContract.View view) {
        this.view = view;
    }

    @Override
    public void viewShown() {
        Disposable disposable = eventsRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(events -> {
                    if (view == null) { return; }
                    view.showEvents(events);
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