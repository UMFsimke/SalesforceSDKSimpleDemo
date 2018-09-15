package com.example.simpledemo.model.network.executor;


import com.example.simpledemo.model.repository.Repository;
import com.example.simpledemo.utils.ListUtils;
import com.salesforce.androidsdk.accounts.UserAccountManager;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class SyncExecutorImpl implements SyncExecutor {

    private static final Boolean FULL_SYNC_FLAG = false;
    private static final Boolean SYNC_DOWN_ONLY_FLAG = true;

    private PublishSubject<Boolean> syncSubject;
    private SmartSyncSDKManager salesforceSDKManager;
    private UserAccountManager userAccountManager;
    private List<Repository> repositories;
    private Disposable disposable;

    public SyncExecutorImpl(SmartSyncSDKManager salesforceSDKManager,
                            UserAccountManager userAccountManager,
                            List<Repository> repositories) {
        syncSubject = PublishSubject.create();
        this.salesforceSDKManager = salesforceSDKManager;
        this.userAccountManager = userAccountManager;
        this.repositories = repositories;
        bindSync();
    }

    private void bindSync() {
        disposable = syncSubject
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .skipWhile(syncDownOnly -> !isSyncAllowed())
                .flatMap(syncDownOnly -> syncDownOnly ? getSyncDownObservable() :
                        getSyncUpObservable())
                .onErrorReturnItem(false)
                .ignoreElements()
                .subscribe();
    }

    private boolean isSyncAllowed() {
        return !salesforceSDKManager.isLoggingOut() && userAccountManager.getCurrentUser() != null;
    }

    private Observable<Boolean> getSyncDownObservable() {
        if (ListUtils.isEmpty(repositories)) {
            return Observable.empty();
        }

        List<Observable<Boolean>> syncDownObservables = new ArrayList<>();
        for (Repository repository : repositories) {
            syncDownObservables.add(repository.syncDown());
        }

        return Observable.zip(syncDownObservables, objects -> true);
    }

    private Observable<Boolean> getSyncUpObservable() {
        if (ListUtils.isEmpty(repositories)) {
            return Observable.empty();
        }

        List<Observable<Boolean>> synncUpObservables = new ArrayList<>();
        for (Repository repository : repositories) {
            synncUpObservables.add(repository.syncUp());
        }

        return Observable.zip(synncUpObservables, objects -> true);
    }

    @Override
    public void performFullSync() {
        syncSubject.onNext(FULL_SYNC_FLAG);
    }

    @Override
    public void performSyncDownOnly() {
        syncSubject.onNext(SYNC_DOWN_ONLY_FLAG);
    }
}
