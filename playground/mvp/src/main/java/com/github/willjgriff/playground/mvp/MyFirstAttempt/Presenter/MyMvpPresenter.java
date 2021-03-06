package com.github.willjgriff.playground.mvp.MyFirstAttempt.Presenter;

import com.github.willjgriff.playground.mvp.MyFirstAttempt.View.MyMvpView;

/**
 * Created by Will on 05/04/2016.
 */
public interface MyMvpPresenter<VIEW extends MyMvpView> {

    VIEW getView();

    void setView(VIEW view);

    void removeView();
}
