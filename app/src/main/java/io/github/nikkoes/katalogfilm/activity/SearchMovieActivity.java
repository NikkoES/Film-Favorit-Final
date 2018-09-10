package io.github.nikkoes.katalogfilm.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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

public class SearchMovieActivity extends AppCompatActivity {

    @BindView(io.github.nikkoes.katalogfilm.R.id.rv_movies)
    RecyclerView rvMovies;

    private MovieAdapter adapter;
    List<Movies> listMovies = new ArrayList<>();

    BaseApiService apiService;

    private final String api_key = BuildConfig.MY_MOVIE_DB_API_KEY;
    private final String language = "en-US";
    private final String include_adult = "false";
    private final String page = "1";
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(io.github.nikkoes.katalogfilm.R.layout.activity_search_movie);

        keyword = getIntent().getStringExtra("keyword");

        getSupportActionBar().setTitle(keyword);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        apiService = UtilsApi.getAPIService();

        adapter = new MovieAdapter(getApplicationContext(), listMovies);

        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvMovies.setAdapter(adapter);

        searchMovie(keyword);
    }

    public void searchMovie(String keyword){
        apiService.searchMovie(api_key, language, keyword, page, include_adult).enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                if (response.isSuccessful()){
                    listMovies = response.body().getMovies();

                    rvMovies.setAdapter(new MovieAdapter(getApplicationContext(), listMovies));
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed to Fetch Data !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMovies> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to Connect Internet !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home : {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
