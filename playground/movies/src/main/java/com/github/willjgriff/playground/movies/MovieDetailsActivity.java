package com.github.willjgriff.playground.movies;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.willjgriff.playground.R;
import com.github.willjgriff.playground.movies.Presenters.MovieDetailsPresenter;
import com.github.willjgriff.playground.movies.Presenters.MovieDetailsPresenterImpl;
import com.github.willjgriff.playground.movies.Views.MovieDetailsView;
import com.github.willjgriff.playground.mvp.Remind101ExampleAdapted.View.MvpActivity;
import com.github.willjgriff.playground.network.api.TheMovieDb.TheMovieDbCalls;
import com.github.willjgriff.playground.network.utils.MovieApiImageUtils;
import com.github.willjgriff.playground.utils.UiUtils;

import static com.github.willjgriff.playground.network.utils.MovieImageSize.ImageSize.LARGE;
import static com.github.willjgriff.playground.network.utils.MovieImageSize.ImageType.POSTER;

/**
 * Created by Will on 11/04/2016.
 */
public class MovieDetailsActivity extends MvpActivity<MovieDetailsPresenter> implements MovieDetailsView {

    public static final String EXTRA_MOVIE_ID = "com.github.willjgriff.playground.movies.Views.MovieDetailsActivity;EXTRA_MOVIE_ID";

    private TextView mTitle;
    private ImageView mPoster;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_movie_details_toolbar);
        ViewCompat.setElevation(toolbar, UiUtils.convertDpToPixel(4, this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindViews();
    }

    private void bindViews() {
        mTitle = (TextView) findViewById(R.id.activity_movie_details_title);
        mPoster = (ImageView) findViewById(R.id.activity_movie_details_poster);
    }

    @Override
    protected MovieDetailsPresenter setPresenter() {
        if (getIntent().getStringExtra(EXTRA_MOVIE_ID) != null) {
            String movieId = getIntent().getStringExtra(EXTRA_MOVIE_ID);
            return new MovieDetailsPresenterImpl(TheMovieDbCalls.movieDetailsCall(movieId));
        } else {
            throw new RuntimeException("No MovieId for Movie Details Activity");
        }
    }

    @Override
    public void setMovieName(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setPoster(String posterImage) {
        MovieApiImageUtils
                .showImage(posterImage, mPoster)
                .withPlaceholder(R.drawable.movie_poster_placeholder)
                .withTypeSize(POSTER, LARGE)
                .now();
    }

}
