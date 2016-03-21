package api.retrofit.nasa;

import android.support.annotation.Nullable;

import com.vakk.nasaapod.helpers.Image;

import api.Constants;
import api.ResponseListener;
import api.retrofit.Query;
import api.retrofit.nasa.helpers.Data;
import api.retrofit.nasa.helpers.NASAClient;
import api.retrofit.nasa.helpers.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vakk on 3/16/16.
 */

/**
 *
 */
public class NasaQuery implements Query {
    // singleton
    private static NasaQuery instance;
    // need instance for cancel request
    private Call<Data> call;

    /**
     * @see NASAClient
     */
    @Override
    public void getImageByDay(final ResponseListener listener, @Nullable final String date) {
        final NASAClient client = ServiceGenerator.createService(NASAClient.class);
        if (call != null) {
            if (call.isExecuted())
                call.cancel();
        }
        // if date is null - need picture
        if (date == null) {
            call = client.getCurrentDayImage(Constants.API_KEY);
        }
        //else need picture for this date
        else {
            call = client.getImageByDay(Constants.API_KEY, date);
        }

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                if (response.body() != null) {
                    Data data;
                    data = response.body();
                    // create image instance
                    Image image = new Image(
                            data.getTitle(),
                            data.getImageUrl(),
                            data.getExplanation(),
                            data.getDate(),
                            data.getPhotographer());
                    listener.done(image);
                } else listener.fail("Wrong response, image is not received");
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                t.printStackTrace();
                listener.fail(t.getMessage());
            }
        });

    }

    /**
     * @see NASAClient
     */
    @Override
    public void getImageByRange(final ResponseListener listener, String date1, String date2) {
        final NASAClient client = ServiceGenerator.createService(NASAClient.class);

        if (call != null) {
            if (call.isExecuted())
                call.cancel();
        }
        int count = 0;
        // repeat only 10 times
        while (!date1.equals(date2) && count <= 10) {
            count++;
            call = client.getImageByDay(Constants.API_KEY, date1);
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    Data data = response.body();
                    Image image = new Image(
                            data.getTitle(),
                            data.getImageUrl(),
                            data.getExplanation(),
                            data.getDate(),
                            data.getPhotographer());
                    listener.done(image);
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    listener.fail(t.getMessage());
                }
            });
            // get next day
            date1 = incrementDate(date1);
        }
    }

    String incrementDate(String date) {
        String[] splitedDate = date.split("-");
        if (Integer.parseInt(splitedDate[2]) < 28) {
            splitedDate[2] = Integer.toString(Integer.parseInt(splitedDate[2]) + 1);
        } else if (Integer.parseInt(splitedDate[1]) < 12) {
            splitedDate[2] = "01";
            splitedDate[1] = Integer.toString(Integer.parseInt(splitedDate[1]) + 1);
        } else {
            splitedDate[2] = "01";
            splitedDate[1] = "01";
            splitedDate[0] = Integer.toString(Integer.parseInt(splitedDate[0]) + 1);
        }
        date = splitedDate[0] + "-" + splitedDate[1] + "-" + splitedDate[2];
        return date;
    }

    /**
     * Like singleton
     *
     * @return instance for this class
     */
    public static NasaQuery getInstance() {
        if (instance == null) {
            instance = new NasaQuery();
        }
        return instance;
    }
}
