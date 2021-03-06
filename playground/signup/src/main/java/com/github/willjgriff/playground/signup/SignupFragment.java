package com.github.willjgriff.playground.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.willjgriff.playground.R;
import com.github.willjgriff.playground.mvp.RxMvp.RxView.RxMvpFragment;
import com.github.willjgriff.playground.network.utils.PlaygroundSubscriber;

import rx.Observable;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;
import rx.android.widget.WidgetObservable;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

/**
 * Created by Will on 02/05/2016.
 */
public class SignupFragment extends RxMvpFragment<SignupPresenter> implements SignupView {

    private EditText mUsername;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mSignup;

    private Observable<Boolean> mUsernameObservable;
    private Observable<Boolean> mPasswordObservable;
    private Observable<Boolean> mConfirmPasswordObservable;
    private Observable<OnClickEvent> mSignupObservable;

    // TODO: Ideally generalise the type here somehow.
    private ConnectableObservable<?> mSignupCallObservable;

    @Override
    protected SignupPresenter createPresenter() {
        return new SignupPresenterImpl();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        mUsername = (EditText) view.findViewById(R.id.fragment_signup_username);
        mPassword = (EditText) view.findViewById(R.id.fragment_signup_password1);
        mConfirmPassword = (EditText) view.findViewById(R.id.fragment_signup_password2);
        mSignup = (Button) view.findViewById(R.id.fragment_signup_button);

        setupObservables();
        setupSubscribers();

        return view;
    }

    private void setupObservables() {
        mUsernameObservable = WidgetObservable.text(mUsername)
                // distinctUntilChanged() Prevents the observable outputting until
                // the output will be different from the previous. For performance.
                .distinctUntilChanged()
                .map(u -> u.text().length() > 4);

        // TODO: Would like to understand how to do multiple mappings without using a filter.
        // Using a large regex here instead.
        mPasswordObservable = WidgetObservable.text(mPassword)
                .distinctUntilChanged()
                .map(p -> String.valueOf(p.text()).matches("^(?=.*\\d)(?=.*[a-zA-Z]).{8,}$"));

        mConfirmPasswordObservable = WidgetObservable.text(mConfirmPassword)
                .distinctUntilChanged()
                .map(cp -> String.valueOf(cp.text()).equals(String.valueOf(mPassword.getText())));

        mSignupObservable = ViewObservable.clicks(mSignup);
    }

    private void setupSubscribers() {
        mUsernameObservable.subscribe(usernameValid -> {
            if (!usernameValid) {
                mUsername.setError(getContext().getString(R.string.fragment_signup_username_error));
            }
        });

        mPasswordObservable.subscribe(passwordValid -> {
            if (!passwordValid) {
                mPassword.setError(getContext().getString(R.string.fragment_signup_password_error));
            }
        });

        mConfirmPasswordObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean confirmPasswordValid) {
                if (!confirmPasswordValid) {
                    mConfirmPassword.setError(getContext().getString(R.string.fragment_signup_confirm_password_error));
                }
            }
        });

        Observable.combineLatest(mUsernameObservable, mPasswordObservable, mConfirmPasswordObservable,
                (usernameValid, passwordValid, confirmValid) -> usernameValid && passwordValid && confirmValid)
                .subscribe(buttonClickable -> mSignup.setEnabled(buttonClickable));

        mSignupObservable.subscribe(onClickEvent -> {
            // Not sure sticking all of this stuff here is very Rx, but for now...
            signupEnabled(false);
            String username = String.valueOf(mUsername.getText());
            String password = String.valueOf(mPassword.getText());

            // I expect there's a better way of using Rx in Mvp, needs playing with though.
            mSignupCallObservable = getPresenter().signup(username, password);
            mSignupCallObservable.subscribe(new PlaygroundSubscriber<Object>() {
                @Override
                public void onError(Throwable e) {
                    signupEnabled(true);
                }

                @Override
                public void onNext(Object blocks) {
                    // Just for testing purposes, this call would actually only belong in onError().
                    // This would probably do nothing and the presenters onNext would direct
                    // the user to a new screen.
                    signupEnabled(true);
                }
            });
            mSignupCallObservable.connect();
        });
    }

    @Override
    public void signupEnabled(boolean enabled) {
        mUsername.setEnabled(enabled);
        mPassword.setEnabled(enabled);
        mConfirmPassword.setEnabled(enabled);
        mSignup.setEnabled(enabled);
    }
}
