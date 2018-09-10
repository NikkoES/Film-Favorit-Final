package io.github.nikkoes.katalogfilm.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.nikkoes.katalogfilm.adapter.ViewPagerAdapter;
import io.github.nikkoes.katalogfilm.fragment.FavoriteFragment;
import io.github.nikkoes.katalogfilm.fragment.NowPlayingFragment;
import io.github.nikkoes.katalogfilm.fragment.UpcomingFragment;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(io.github.nikkoes.katalogfilm.R.id.tabs)
    TabLayout tabLayout;
    @BindView(io.github.nikkoes.katalogfilm.R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(io.github.nikkoes.katalogfilm.R.layout.activity_main);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(io.github.nikkoes.katalogfilm.R.layout.action_bar_title);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        setupTab();
    }

    private void setupTab() {
        TextView tabSatu = (TextView) LayoutInflater.from(this).inflate(io.github.nikkoes.katalogfilm.R.layout.custom_tab, null);
        tabSatu.setText(getString(io.github.nikkoes.katalogfilm.R.string.now_playing));
        tabLayout.getTabAt(0).setCustomView(tabSatu);

        TextView tabDua = (TextView) LayoutInflater.from(this).inflate(io.github.nikkoes.katalogfilm.R.layout.custom_tab, null);
        tabDua.setText(getString(io.github.nikkoes.katalogfilm.R.string.upcoming));
        tabLayout.getTabAt(1).setCustomView(tabDua);

        TextView tabTiga = (TextView) LayoutInflater.from(this).inflate(io.github.nikkoes.katalogfilm.R.layout.custom_tab, null);
        tabTiga.setText(getString(io.github.nikkoes.katalogfilm.R.string.favorite));
        tabLayout.getTabAt(2).setCustomView(tabTiga);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NowPlayingFragment(), getString(io.github.nikkoes.katalogfilm.R.string.now_playing));
        adapter.addFragment(new UpcomingFragment(), getString(io.github.nikkoes.katalogfilm.R.string.upcoming));
        adapter.addFragment(new FavoriteFragment(), getString(io.github.nikkoes.katalogfilm.R.string.favorite));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(io.github.nikkoes.katalogfilm.R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case io.github.nikkoes.katalogfilm.R.id.action_search :
                SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(this);
                break;

            case io.github.nikkoes.katalogfilm.R.id.setting :
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent i = new Intent(MainActivity.this, SearchMovieActivity.class);
        i.putExtra("keyword", query);
        startActivity(i);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

}
