package com.example.simpledemo.model.repository;

import io.reactivex.Observable;

public interface Repository {

    Observable<Boolean> syncDown();
    Observable<Boolean> syncUp();
}
