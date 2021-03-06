package com.github.willjgriff.playground.signup;

import com.github.willjgriff.playground.mvp.RxMvp.RxPresenter.RxBasePresenter;
import com.github.willjgriff.playground.network.api.Etherchain.EtherchainApiCalls;
import com.github.willjgriff.playground.network.model.ethereum.Block;
import com.github.willjgriff.playground.network.utils.PlaygroundSubscriber;

import java.util.List;

import rx.observables.ConnectableObservable;

/**
 * Created by Will on 02/05/2016.
 */
public class SignupPresenterImpl extends RxBasePresenter<SignupView> implements SignupPresenter {

    @Override
    public ConnectableObservable<?> signup(String username, String password) {
        // Imagine this is a signup request sending the username and password instead of '0, 300'
        return addConnectableSubscription(EtherchainApiCalls.blockList(0, 300), new PlaygroundSubscriber<List<Block>>() {
            @Override
            public void onError(Throwable e) {
                // Show error? Error also handled in View. Not sure about Mvp responsibilities here.
            }

            @Override
            public void onNext(List<Block> blocks) {
                // Successfully completed signup, go to next screen or show confirmation message.
            }
        });
    }
}
