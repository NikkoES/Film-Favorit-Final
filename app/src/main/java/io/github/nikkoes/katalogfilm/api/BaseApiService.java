package io.github.nikkoes.katalogfilm.api;

import io.github.nikkoes.katalogfilm.model.ResponseMovies;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaseApiService {

    @GET("3/discover/movie")
    Call<ResponseMovies> getAllMovies(@Query("api_key") String api_key,
                                      @Query("language") String language,
                                      @Query("sort_by") String sort_by,
                                      @Query("include_adult") String include_adult,
                                      @Query("include_video") String include_video,
                                      @Query("page") String page );
    @GET("/3/search/movie")
    Call<ResponseMovies> searchMovie(@Query("api_key") String api_key,
                                     @Query("language") String language,
                                     @Query("query") String query,
                                     @Query("page") String page,
                                     @Query("include_adult") String include_adult);

    @GET("3/movie/now_playing")
    Call<ResponseMovies> getNowPlaying(@Query("api_key") String api_key,
                                      @Query("language") String language);

    @GET("3/movie/upcoming")
    Call<ResponseMovies> getUpcoming(@Query("api_key") String api_key,
                                       @Query("language") String language);

}
