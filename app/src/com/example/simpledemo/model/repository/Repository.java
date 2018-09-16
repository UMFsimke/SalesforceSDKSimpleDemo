package com.example.simpledemo.model.repository;

import io.reactivex.Single;

public interface Repository {

    Single<Boolean> syncDown();
    Single<Boolean> syncUp();
}
