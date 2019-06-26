package com.oganbelema.popularmovies.trailer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.oganbelema.network.model.trailer.Trailer;

import java.util.List;

public class MovieTrailerViewModel extends ViewModel {

    private final MovieTrailerRepository mMovieTrailerRepository;

    private final MovieTrailerAdapter mMovieTrailerAdapter;

    public MovieTrailerViewModel(MovieTrailerRepository movieTrailerRepository,
                                 MovieTrailerAdapter movieTrailerAdapter) {
        this.mMovieTrailerRepository = movieTrailerRepository;
        this.mMovieTrailerAdapter = movieTrailerAdapter;
    }

    public MovieTrailerAdapter getMovieTrailerAdapter(){
        return mMovieTrailerAdapter;
    }

    public Boolean getNetworkStatus(){
        return mMovieTrailerRepository.getNetworkStatus().getValue();
    }

    public LiveData<List<Trailer>> getMovieTrailers(int movieId){
        return mMovieTrailerRepository.getMovieTrailers(movieId);
    }

    public LiveData<Throwable> getError(){
        return mMovieTrailerRepository.getError();
    }

    @Override
    protected void onCleared() {
        mMovieTrailerRepository.dispose();
        super.onCleared();
    }
}
