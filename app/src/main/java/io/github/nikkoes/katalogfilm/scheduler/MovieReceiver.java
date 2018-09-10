package io.github.nikkoes.katalogfilm.scheduler;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import io.github.nikkoes.katalogfilm.BuildConfig;
import io.github.nikkoes.katalogfilm.R;
import io.github.nikkoes.katalogfilm.api.UtilsApi;
import io.github.nikkoes.katalogfilm.model.Movies;
import io.github.nikkoes.katalogfilm.model.ResponseMovies;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieReceiver extends BroadcastReceiver {

    public static final String TYPE_RELEASED = "ReleasedTodayAlarm";
    public static final String TYPE_REPEATING = "RepeatingAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    String dateNow;
    String message;

    private final int NOTIF_ID_RELEASED = 100;
    private final int NOTIF_ID_REPEATING = 101;

    public MovieReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final String type = intent.getStringExtra(EXTRA_TYPE);
        message = intent.getStringExtra(EXTRA_MESSAGE);

        String title = "Movie Reminder";
        int notifId = type.equalsIgnoreCase(TYPE_RELEASED) ? NOTIF_ID_RELEASED : NOTIF_ID_REPEATING;

        Log.v("ON RECIEVE",title+" "+notifId);

        if (message.equals(context.getResources().getString(R.string.label_alarm_released_today))){
            getTitleMovie(context, notifId);
        }
        else {
            showAlarmNotification(context, title, message, notifId);
        }

    }

    private void showAlarmNotification(Context context, String title, String message, int notifId){
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId, builder.build());
    }


    public void setRepeatingAlarm(Context context, String type, String time, String message){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, MovieReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

        int requestCode = type.equalsIgnoreCase(TYPE_RELEASED) ? NOTIF_ID_RELEASED : NOTIF_ID_REPEATING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    public void cancelAlarm(Context context, String type){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MovieReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_RELEASED) ? NOTIF_ID_RELEASED : NOTIF_ID_REPEATING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public void getTitleMovie(final Context context, final int notifId){
        final String language = "en-US";

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        int bulan = today.month+1;
        int hari = today.monthDay;

        if (bulan<10 && hari>9)
            dateNow = today.year + "-0" + bulan + "-" + hari;
        else if (hari<10 && bulan>9)
            dateNow = today.year + "-" + bulan + "-0" + hari;
        else if (bulan<10 && hari<10)
            dateNow = today.year + "-0" + bulan + "-0" + hari;
        else dateNow = today.year + "-" + bulan + "-" + hari;

        Call<ResponseMovies> call = UtilsApi.getAPIService().getUpcoming(BuildConfig.MY_MOVIE_DB_API_KEY, language);
        call.enqueue(new Callback<ResponseMovies>() {
            @Override
            public void onResponse(Call<ResponseMovies> call, Response<ResponseMovies> response) {
                if (response.isSuccessful()) {
                    List<Movies> listMovies = response.body().getMovies();
                    for (int i = 0; i < listMovies.size(); i++) {
                        String releaseDate = listMovies.get(i).getReleaseDate();
                        String title = listMovies.get(i).getTitle();
                        if (releaseDate.equals(dateNow)){
                            message = title + message;
                            showAlarmNotification(context, title, message, notifId);
                        }
                    }
                }
                else{
                    Toast.makeText(context, "Failed fetch data !", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseMovies> call, Throwable t) {
                Toast.makeText(context, "Connection failed !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
