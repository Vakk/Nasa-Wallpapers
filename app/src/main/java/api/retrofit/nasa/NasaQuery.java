package api.retrofit.nasa;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.vakk.nasaapod.helpers.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class NasaQuery implements Query {
    private static NasaQuery instance;
    private Call<Data> call;
    @Override
    public void getCurrentDayPicture(final ResponseListener listener,@Nullable String date) {
        final NASAClient client = ServiceGenerator.createService(NASAClient.class);
        if (call != null) {
            if (call.isExecuted())
                call.cancel();
        }
        if (date==null) {
            call = client.getCurrentDayImage(Constants.API_KEY);
        }
        else {
            call = client.getImageByDay(Constants.API_KEY, date);
        }
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                if (response.body() != null) {
                    Data data;
                    data = response.body();
                    Image image = new Image(
                            data.getTitle(),
                            data.getImageUrl(),
                            data.getExplanation(),
                            data.getDate());
                    listener.done(image);
                }
                else listener.fail("body is null, wrong answer");
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                t.printStackTrace();
                listener.fail(t.getMessage());
            }
        });

    }

    @Override
    public void getPictureByDay(final ResponseListener listener,String date) {
        NASAClient client = ServiceGenerator.createService(NASAClient.class);
        if (call != null) {
            if (call.isExecuted())
                call.cancel();
        }
        call=client.getImageByDay(Constants.API_KEY,date);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.body() != null) {
                    Data data;
                    data = response.body();
                    Image image = new Image(
                            data.getTitle(),
                            data.getImageUrl(),
                            data.getExplanation(),
                            data.getDate());
                    listener.done(image);
                }
                else listener.fail("body is null, wrong answer");}
            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    @Override
    public void getDayByRange(final ResponseListener listener, String date1, String date2) {
        final NASAClient client = ServiceGenerator.createService(NASAClient.class);

        if (call != null) {
            if (call.isExecuted())
                call.cancel();
        }
        int count =0;
        while (!date1.equals(date2)||count<=10){
            count++;
            date1=incrementDate(date1);
            call = client.getImageByDay(Constants.API_KEY, date1);
            call.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    Data data = response.body();
                    Image image = new Image(
                            data.getTitle(),
                            data.getImageUrl(),
                            data.getExplanation(),
                            data.getDate());
                    listener.done(image);
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {

                }
            });
        }
    }
    String incrementDate(String date){
        String [] splitedDate = date.split("-");
        if (Integer.parseInt(splitedDate[2])<28) {
            splitedDate[2] = Integer.toString(Integer.parseInt(splitedDate[2]) + 1);
        }
        else if (Integer.parseInt(splitedDate[1])<12){
            splitedDate[2]="01";
            splitedDate[1] = Integer.toString(Integer.parseInt(splitedDate[1]) + 1);
        }
        else {
            splitedDate[2]="01";
            splitedDate[1]="01";
            splitedDate[0]= Integer.toString(Integer.parseInt(splitedDate[0])+1);
        }
        date = splitedDate[0] + "-" + splitedDate[1] + "-" + splitedDate[2];
        return date;
    }

    public static NasaQuery getInstance(){
        if (instance==null){
            instance = new NasaQuery();
        }
        return instance;
    }
    }
