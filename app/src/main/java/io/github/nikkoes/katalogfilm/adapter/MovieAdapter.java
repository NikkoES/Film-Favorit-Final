package io.github.nikkoes.katalogfilm.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.nikkoes.katalogfilm.activity.DetailMovieActivity;
import io.github.nikkoes.katalogfilm.model.Movies;
import io.github.nikkoes.katalogfilm.utils.StringUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private List<Movies> listMovies;

    public MovieAdapter(Context context, List<Movies> listMovies){
        this.context = context;
        this.listMovies = listMovies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(io.github.nikkoes.katalogfilm.R.layout.list_item_movie, null, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movies movies = listMovies.get(position);
        Glide.with(context)
                .load("http://image.tmdb.org/t/p/w185"+movies.getPosterPath())
                .into(holder.imageMovies);
        holder.txtNamaMovies.setText(movies.getTitle());
        holder.txtDeskripsiMovies.setText(movies.getOverview());
        holder.txtTanggalMovies.setText(StringUtils.dateConverter(movies.getReleaseDate()));
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(io.github.nikkoes.katalogfilm.R.id.image_movie)
        ImageView imageMovies;
        @BindView(io.github.nikkoes.katalogfilm.R.id.txt_nama_movie)
        TextView txtNamaMovies;
        @BindView(io.github.nikkoes.katalogfilm.R.id.txt_deskripsi)
        TextView txtDeskripsiMovies;
        @BindView(io.github.nikkoes.katalogfilm.R.id.txt_tanggal)
        TextView txtTanggalMovies;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mPosition = getLayoutPosition();
                    Movies movies = listMovies.get(mPosition);

                    Intent i = new Intent(context, DetailMovieActivity.class);
                    i.putExtra("title", movies.getTitle());
                    i.putExtra("backdrop", movies.getBackdropPath());
                    i.putExtra("overview", movies.getOverview());
                    i.putExtra("release_date", StringUtils.dateConverter(movies.getReleaseDate()));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }
    }

}
