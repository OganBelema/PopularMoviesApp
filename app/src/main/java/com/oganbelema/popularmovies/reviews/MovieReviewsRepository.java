package com.oganbelema.popularmovies.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oganbelema.database.PopularMoviesDB;
import com.oganbelema.database.entity.MovieReviewEntity;
import com.oganbelema.database.mapper.EntityMapper;
import com.oganbelema.network.NetworkUtil;
import com.oganbelema.network.model.review.Review;
import com.oganbelema.network.source.MovieReviewNetworkSource;
import com.oganbelema.popularmovies.mapper.MovieReviewEntityMapperImpl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class MovieReviewsRepository {

    private final NetworkUtil mNetworkUtil;

    private final MovieReviewNetworkSource mMovieReviewNetworkSource;

    private final PopularMoviesDB mPopularMoviesDB;

    private final EntityMapper<MovieReviewEntity, Review> mMovieReviewEntityMapper;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Review>> mReviews = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mNetworkStatus = new MutableLiveData<>();

    @Inject
    public MovieReviewsRepository(NetworkUtil networkUtil, MovieReviewNetworkSource
            mMovieReviewNetworkSource, PopularMoviesDB popularMoviesDB,
                                  EntityMapper<MovieReviewEntity, Review> movieReviewEntityMapper) {
        this.mNetworkUtil = networkUtil;
        this.mMovieReviewNetworkSource = mMovieReviewNetworkSource;
        this.mPopularMoviesDB = popularMoviesDB;
        this.mMovieReviewEntityMapper = movieReviewEntityMapper;
    }

    public LiveData<Boolean> getNetworkStatus(){
        return mNetworkStatus;
    }

    public LiveData<List<Review>> getMovieReviews(int movieId){
        mNetworkStatus.setValue(mNetworkUtil.isConnected());

        ((MovieReviewEntityMapperImpl) mMovieReviewEntityMapper).setMovieId(movieId);

        if (mNetworkUtil.isConnected()){
            return getMovieReviewsRemote(movieId);
        } else {
            return getMoviesLocal(movieId);
        }
    }

    private LiveData<List<Review>> getMovieReviewsRemote(int movieId) {
        return mMovieReviewNetworkSource.getReviewsRemote(movieId);
    }

    public LiveData<Throwable> getError(){
        return mMovieReviewNetworkSource.getError();
    }

    public void saveMovieReviews(List<Review> reviews){
        disposables.add(
                Observable.fromCallable(() -> mMovieReviewEntityMapper.toEntityList(reviews))
                        .subscribeOn(Schedulers.io())
                        .subscribe(movieReviewEntities ->
                                mPopularMoviesDB.getMovieReviewDao()
                                        .insertMovieReviews(movieReviewEntities))
        );
    }

    private LiveData<List<Review>> getMoviesLocal(int movieId){
        disposables.add(
                mPopularMoviesDB.getMovieReviewDao().getReviews(movieId)
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::mapMovieReviewEntitiesToReview)
        );

        return mReviews;
    }

    private void mapMovieReviewEntitiesToReview(List<MovieReviewEntity> movieReviewEntities) {
        disposables.add(
                Observable.fromCallable(() ->
                        mMovieReviewEntityMapper.fromEntityList(movieReviewEntities))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mReviews::postValue)
        );
    }

    public void dispose(){
        disposables.dispose();
    }

}
