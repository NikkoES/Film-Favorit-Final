package io.github.nikkoes.katalogfilm.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.nikkoes.katalogfilm.R;
import io.github.nikkoes.katalogfilm.db.DatabaseContract;

public class DetailMovieActivity extends AppCompatActivity {

    @BindView(io.github.nikkoes.katalogfilm.R.id.image_backdrop)
    ImageView imageBackdrop;
    @BindView(io.github.nikkoes.katalogfilm.R.id.btn_favorite)
    ImageView btnFavorite;
    @BindView(io.github.nikkoes.katalogfilm.R.id.txt_nama_movie)
    TextView txtNamaMovie;
    @BindView(io.github.nikkoes.katalogfilm.R.id.txt_tanggal_movie)
    TextView txtTanggalMovie;
    @BindView(io.github.nikkoes.katalogfilm.R.id.txt_deskripsi_movie)
    TextView txtDeskripsiMovie;

    private long id;
    String backdropPath, title, releaseDate, overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(io.github.nikkoes.katalogfilm.R.layout.activity_detail_movie);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(io.github.nikkoes.katalogfilm.R.string.detail_movie));

        setMovieData();
        checkFavorite();
    }

    private void setMovieData(){
        backdropPath = getIntent().getStringExtra("backdrop");
        title = getIntent().getStringExtra("title");
        releaseDate = getIntent().getStringExtra("release_date");
        overview = getIntent().getStringExtra("overview");

        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w185"+backdropPath)
                .into(imageBackdrop);
        txtNamaMovie.setText(title);
        txtTanggalMovie.setText(releaseDate);
        txtDeskripsiMovie.setText(overview);
        btnFavorite.setImageResource(R.drawable.ic_star_unchecked);
    }

    @OnClick(io.github.nikkoes.katalogfilm.R.id.btn_favorite)
    public void btnFavorite(){
        if(checkFavorite()){
            Uri uri = Uri.parse(DatabaseContract.CONTENT_URI+"/"+id);
            getContentResolver().delete(uri, null, null);
            btnFavorite.setImageResource(R.drawable.ic_star_unchecked);
        }
        else{
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.FavoriteColumns.NAME, title);
            values.put(DatabaseContract.FavoriteColumns.BACKDROP, backdropPath);
            values.put(DatabaseContract.FavoriteColumns.RELEASE_DATE, releaseDate);
            values.put(DatabaseContract.FavoriteColumns.DESCRIPTION, overview);

            getContentResolver().insert(DatabaseContract.CONTENT_URI, values);
            setResult(101);

            btnFavorite.setImageResource(io.github.nikkoes.katalogfilm.R.drawable.ic_star_checked);
        }
    }

    public boolean checkFavorite(){
        Uri uri = Uri.parse(DatabaseContract.CONTENT_URI+"");
        boolean favorite = false;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        String getTitle;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(0);
                getTitle = cursor.getString(1);
                if (getTitle.equals(getIntent().getStringExtra("title"))){
                    btnFavorite.setImageResource(io.github.nikkoes.katalogfilm.R.drawable.ic_star_checked);
                    favorite = true;
                }
            } while (cursor.moveToNext());

        }

        return favorite;

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
