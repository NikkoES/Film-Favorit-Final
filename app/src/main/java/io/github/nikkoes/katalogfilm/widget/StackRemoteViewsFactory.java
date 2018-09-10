package io.github.nikkoes.katalogfilm.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

import io.github.nikkoes.katalogfilm.R;
import io.github.nikkoes.katalogfilm.model.Favorite;

import static io.github.nikkoes.katalogfilm.db.DatabaseContract.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;

    private Cursor list;

    public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        list = mContext.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Favorite item = getItem(i);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.stack_widget_item);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load("http://image.tmdb.org/t/p/w185" + item.getBackdrop())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.imageViewStack, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(StackWidget.EXTRA_ITEM, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageViewStack, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private Favorite getItem(int position) {
        if (!list.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid!");
        }

        return new Favorite(list);
    }
}
