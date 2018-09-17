package com.example.simpledemo.view.users;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.model.repository.UserRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserListPresenter implements UsersListContract.Presenter {

    private UsersListContract.View view;
    private UserRepository userRepository;
    private CompositeDisposable compositeDisposable;

    public UserListPresenter() {
        compositeDisposable = new CompositeDisposable();
        userRepository = MainApplication.getInstance().graph().getUserRepository();
    }

    @Override
    public void setView(UsersListContract.View view) {
        this.view = view;
    }

    @Override
    public void viewShown() {
        Disposable disposable = userRepository.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    if (view == null) { return; }
                    view.showUsers(users);
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
