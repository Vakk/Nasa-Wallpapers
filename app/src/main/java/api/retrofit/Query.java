package api.retrofit;

import android.support.annotation.Nullable;

import api.ResponseListener;

/**
 * Created by vakk on 3/16/16.
 */
public interface Query {
    void getCurrentDayPicture(ResponseListener listene,@Nullable String day);
    void getPictureByDay(ResponseListener listene,String date);
    void getDayByRange(ResponseListener listene,String date1, String date2);
}
