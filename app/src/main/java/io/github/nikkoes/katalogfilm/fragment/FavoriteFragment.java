package io.github.nikkoes.katalogfilm.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.nikkoes.katalogfilm.adapter.FavoriteAdapter;
import io.github.nikkoes.katalogfilm.db.DatabaseContract;

import static android.provider.BaseColumns._ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    @BindView(io.github.nikkoes.katalogfilm.R.id.rv_movies)
    RecyclerView rvMovies;

    private FavoriteAdapter adapter;
    private Cursor listFavorite;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(io.github.nikkoes.katalogfilm.R.layout.fragment_favorite, container, false);

        ButterKnife.bind(this, v);

        listFavorite = getActivity().getContentResolver().query(DatabaseContract.CONTENT_URI,null,null,null,_ID + " DESC" );

        adapter = new FavoriteAdapter(getContext(), listFavorite);
        adapter.notifyDataSetChanged();

        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovies.setAdapter(adapter);

        return v;
    }

}
