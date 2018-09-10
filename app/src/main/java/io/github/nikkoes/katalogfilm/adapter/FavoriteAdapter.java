package io.github.nikkoes.katalogfilm.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.nikkoes.katalogfilm.activity.DetailMovieActivity;
import io.github.nikkoes.katalogfilm.model.Favorite;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context context;
    private Cursor listFavorite;

    public FavoriteAdapter(Context context, Cursor listFavorite){
        this.context = context;
        this.listFavorite = listFavorite;
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
        final Favorite favorite = getItem(position);
        Glide.with(context)
                .load("http://image.tmdb.org/t/p/w185"+favorite.getBackdrop())
                .into(holder.imageMovies);
        holder.txtNamaMovies.setText(favorite.getName());
        holder.txtDeskripsiMovies.setText(favorite.getDescription());
        holder.txtTanggalMovies.setText(favorite.getDate());
        Log.e("DATE", ""+favorite.getDate());
    }

    @Override
    public int getItemCount() {
        if (listFavorite == null){
            return 0;
        }
        return listFavorite.getCount();
    }

    private Favorite getItem(int position){
        if (!listFavorite.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Favorite(listFavorite);
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
                    Favorite favorite = getItem(mPosition);

                    Intent i = new Intent(context, DetailMovieActivity.class);
                    i.putExtra("title", favorite.getName());
                    i.putExtra("backdrop", favorite.getBackdrop());
                    i.putExtra("overview", favorite.getDescription());
                    i.putExtra("release_date", favorite.getDate());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }
    }

}
