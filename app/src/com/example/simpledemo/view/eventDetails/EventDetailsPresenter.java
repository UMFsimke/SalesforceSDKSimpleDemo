package com.example.simpledemo.view.eventDetails;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.model.repository.EventsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventDetailsPresenter implements EventDetailsContract.Presenter {

    private EventDetailsContract.View view;
    private EventsRepository eventsRepository;
    private CompositeDisposable compositeDisposable;

    public EventDetailsPresenter() {
        compositeDisposable = new CompositeDisposable();
        eventsRepository = MainApplication.getInstance().graph().getEventsRepository();
    }

    @Override
    public void setView(EventDetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void getEvent(String eventId) {
        Disposable disposable = eventsRepository.getEvent(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (view == null) { return; }
                    view.showEvent(event.getCreatedBy(), event);
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