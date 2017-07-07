package com.kimathi.rfid.invision;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvisionController  {
    static final String BASE_URL = "Test1";
    public Gson gson;
    public Retrofit retrofit;
    public ActivityResource activityResource;

    public InvisionController(){
        gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        activityResource = retrofit.create(ActivityResource.class);
    }
    public Boolean logActivity(String userid, String activityIdStr){

        try {
            return new AsyncTask<String, Void, Boolean>() {

                @Override
                protected Boolean doInBackground(String... params) {
                    try {
                        Response response = activityResource.logActivity(params[0], Integer.valueOf(params[1])).execute();
                        return response.isSuccessful();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.execute(userid, activityIdStr).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<InvisionActivity> getActivities() {
        final List<InvisionActivity> result = new ArrayList<>();

        try {
            return new AsyncTask<Void, Void, List<InvisionActivity>>() {

                @Override
                protected  List<InvisionActivity> doInBackground(Void... params) {
                    try {
                        result.addAll(activityResource.getActivities().execute().body());
                        ((ArrayList)result).add(0, new InvisionActivity(-1, null));
                        return  result;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.execute().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}