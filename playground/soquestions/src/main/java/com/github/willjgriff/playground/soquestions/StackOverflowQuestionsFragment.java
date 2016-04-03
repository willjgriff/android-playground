package com.github.willjgriff.playground.soquestions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.will.Playground.R;
import com.github.willjgriff.playground.api.RetrofitCalls;
import com.github.willjgriff.playground.api.model.stackoverflow.StackOverflowQuestions;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Will on 13/03/2016.
 */
public class StackOverflowQuestionsFragment extends Fragment {

    ArrayAdapter<Object> mAdapter;
    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stack_overflow_questions, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_stack_overflow_questions_progress_bar);
        ListView soQuestionsList = (ListView) view.findViewById(R.id.fragment_stack_overflow_questions_list);
        mAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new ArrayList<>());
        soQuestionsList.setAdapter(mAdapter);

        showProgressBar();
        loadStackOverflowQuestions();

        return view;
    }

    private void loadStackOverflowQuestions() {
        //asynchronous call (use call.execute() on a new thread for synchronous)
        RetrofitCalls.stackOverflowQuestionsCall().enqueue(new Callback<StackOverflowQuestions>() {
            @Override
            public void onResponse(Response<StackOverflowQuestions> response, Retrofit retrofit) {
                mAdapter.addAll(response.body().getQuestions());
                dismissProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Tag", "Failed to connect to Stack Overflow");
            }
        });
    }

    private void showProgressBar() {
        mAdapter.clear();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }


}