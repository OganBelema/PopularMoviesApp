package com.oganbelema.popularmovies.trailer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.oganbelema.database.PopularMoviesDB;
import com.oganbelema.database.entity.MovieTrailerEntity;
import com.oganbelema.database.mapper.EntityMapper;
import com.oganbelema.network.NetworkUtil;
import com.oganbelema.network.model.trailer.Trailer;
import com.oganbelema.network.source.MovieTrailersNetworkSource;
import com.oganbelema.popularmovies.mapper.MovieTrailerEntityMapperImpl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class MovieTrailerRepository {

    private final NetworkUtil mNetworkUtil;

    private final MovieTrailersNetworkSource mMovieTrailersNetworkSource;

    private final PopularMoviesDB mPopularMoviesDB;

    private final EntityMapper<MovieTrailerEntity, Trailer> mMovieTrailerEntityMapper;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<List<Trailer>> mTrailers = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mNetworkStatus = new MutableLiveData<>();

    @Inject
    public MovieTrailerRepository(NetworkUtil networkUtil, MovieTrailersNetworkSource
            movieTrailersNetworkSource, PopularMoviesDB popularMoviesDB,
                                  EntityMapper<MovieTrailerEntity, Trailer> movieTrailerEntityMapper) {
        this.mNetworkUtil = networkUtil;
        this.mMovieTrailersNetworkSource = movieTrailersNetworkSource;
        this.mPopularMoviesDB = popularMoviesDB;
        this.mMovieTrailerEntityMapper = movieTrailerEntityMapper;
    }

    public LiveData<Boolean> getNetworkStatus(){
        return mNetworkStatus;
    }

    public LiveData<List<Trailer>> getMovieTrailers(int movieId){
        mNetworkStatus.setValue(mNetworkUtil.isConnected());

        ((MovieTrailerEntityMapperImpl) mMovieTrailerEntityMapper).setMovieId(movieId);

        if (mNetworkUtil.isConnected()){
            return getMovieTrailersRemote(movieId);
        } else {
            return getMovieTrailersLocal(movieId);
        }
    }

    private LiveData<List<Trailer>> getMovieTrailersRemote(int movieId) {
        return mMovieTrailersNetworkSource.getTrailers(movieId);
    }

    public LiveData<Throwable> getError(){
        return mMovieTrailersNetworkSource.getError();
    }

    public void saveMovieTrailers(List<Trailer> trailers){
        disposables.add(
                Observable.fromCallable(() -> mMovieTrailerEntityMapper.toEntityList(trailers))
                        .subscribeOn(Schedulers.io())
                        .subscribe(movieTrailerEntities ->
                                mPopularMoviesDB.getMovieTrailerDao()
                                        .insertMovieTrailers(movieTrailerEntities))
        );
    }

    private LiveData<List<Trailer>> getMovieTrailersLocal(int movieId){
        disposables.add(
                mPopularMoviesDB.getMovieTrailerDao().getMovieTrailers(movieId)
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::mapMovieTrailerEntitiesToTrailer)
        );

        return mTrailers;
    }

    private void mapMovieTrailerEntitiesToTrailer(List<MovieTrailerEntity> movieTrailerEntities) {
        disposables.add(
                Observable.fromCallable(() ->
                        mMovieTrailerEntityMapper.fromEntityList(movieTrailerEntities))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mTrailers::postValue)
        );
    }

    public void dispose(){
        disposables.dispose();
    }
}
