package api.retrofit;

import android.support.annotation.Nullable;

import api.ResponseListener;

/**
 * Created by vakk on 3/16/16.
 */
public interface Query {
    /**
     * @param listener response listener. Callback to user.
     * @param day      can be nullable. If nullable - get current day picture, else get picture by day
     */
    void getImageByDay(ResponseListener listener, @Nullable String day);

    /**
     * get all images in range
     *
     * @param listener listener for server response
     * @param date1    min date
     * @param date2    maximum date
     */
    void getImageByRange(ResponseListener listener, String date1, String date2);
}
