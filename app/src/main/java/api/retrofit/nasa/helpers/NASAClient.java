package api.retrofit.nasa.helpers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by vakk on 3/16/16.
 */
public interface NASAClient {
    @GET ("/planetary/apod")
    Call<Data> getCurrentDayImage(@Query("api_key") String key);
    @GET ("/planetary/apod")
    Call<Data> getImageByDay(@Query("api_key") String key,@Query("date") String date);
    @GET ("/planetary/apod")
    Call<Data> getImageByRange(@Query("api_key") String key,@Query("start_date") String date1,@Query("end_date") String date2);
}
