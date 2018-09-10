package io.github.nikkoes.katalogfilm.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.nikkoes.katalogfilm.BuildConfig;
import io.github.nikkoes.katalogfilm.adapter.MovieAdapter;
import io.github.nikkoes.katalogfilm.api.BaseApiService;
import io.github.nikkoes.katalogfilm.api.UtilsApi;
import io.github.nikkoes.katalogfilm.model.Movies;
import io.github.nikkoes.katalogfilm.model.ResponseMovies;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment {

    @BindView(io.github.nikkoes.katalogfilm.R.id.rv_movies)
    RecyclerView rvMovies;

    private MovieAdapter adapter;
    List<Movies> listMovies = new ArrayList<>();

    ProgressDialog loading;

    BaseApiService apiService;

    private final String api_key = BuildConfig.MY_MOVIE_DB_API_KEY;
    private final String language = "en-US";
    private final String include_adult = "false";
    private final String page = "1";

    public NowPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(io.github.nikkoes.katalogfilm.R.layout.fragment_now_playing, container, false);

        ButterKnife.bind(this, v);

        apiService = UtilsApi.getAPIService();

        adapter = new MovieAdapter(getContext(), listMovies);

        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovies.setAdapter(adapter);

        refresh();

        return v;
    }

    private void refresh(){
        loading = ProgressDialog.show(getContext(), null, "Please wait...", true, false);

        apiService.getNowPlaying(api_key, language).enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                if (response.isSuccessful()){
                    loading.dismiss();

                    listMovies = response.body().getMovies();

                    rvMovies.setAdapter(new MovieAdapter(getContext(), listMovies));
                    adapter.notifyDataSetChanged();
                }
                else {
                    loading.dismiss();
                    Toast.makeText(getContext(), "Failed to Fetch Data !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMovies> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getContext(), "Failed to Connect Internet !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchMovie(String keyword){
        apiService.searchMovie(api_key, language, keyword, page, include_adult).enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                if (response.isSuccessful()){
                    listMovies = response.body().getMovies();

                    rvMovies.setAdapter(new MovieAdapter(getContext(), listMovies));
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getContext(), "Failed to Fetch Data !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMovies> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to Connect Internet !", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
