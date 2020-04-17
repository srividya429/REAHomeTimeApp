package au.com.realestate.hometime;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import au.com.realestate.hometime.models.ApiResponse;
import au.com.realestate.hometime.models.Token;
import au.com.realestate.hometime.models.Tram;
import au.com.realestate.hometime.models.TramDetails;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btnClear, btnRefresh;
    private List<Tram> southTrams;
    private List<Tram> northTrams;

    private RelativeLayout splashLayout;
    private LinearLayout tramInfoLayout;

    private ListView northListView;
    private ListView southListView;

    private TramListAdapter northTramAdapter;
    private TramListAdapter southTramAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnClear = findViewById(R.id.clearButton);
        btnRefresh = findViewById(R.id.refreshButton);

        splashLayout = findViewById(R.id.splashLayout);
        tramInfoLayout = findViewById(R.id.tramInfoLayout);
        tramInfoLayout.setVisibility(View.GONE);
        splashLayout.setVisibility(View.VISIBLE);

        northListView = findViewById(R.id.northListView);
        southListView = findViewById(R.id.southListView);
    }

    public void refreshClick(View view) {
        tramInfoLayout.setVisibility(View.VISIBLE);
        splashLayout.setVisibility(View.GONE);
        TramsApi tramsApi = createApiClient();

        try {
            String token = new RequestToken(tramsApi).execute("").get();
            this.northTrams = new RequestTrams(tramsApi, token).execute("4055").get();
            this.southTrams = new RequestTrams(tramsApi, token).execute("4155").get();
            showTrams();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void clearClick(View view) {
        tramInfoLayout.setVisibility(View.GONE);
        splashLayout.setVisibility(View.VISIBLE);
        northTrams = new ArrayList<>();
        southTrams = new ArrayList<>();
        showTrams();
    }

    private void showTrams() {

        List<TramDetails> northValues = new ArrayList<>();
        List<TramDetails> southValues = new ArrayList<>();

        for (Tram tram : northTrams) {
            String date = dateFromDotNetDate(tram.predictedArrival).toString();
            long inMinutes = getTramTimeInMins(date);
            northValues.add(new TramDetails(Long.toString(inMinutes), tram.routeNo, tram.vehicleNo, tram.destination));
        }

        for (Tram tram : southTrams) {
            String date = dateFromDotNetDate(tram.predictedArrival).toString();
            long inMinutes = getTramTimeInMins(date);
            southValues.add(new TramDetails(Long.toString(inMinutes), tram.routeNo, tram.vehicleNo, tram.destination));
        }

        northTramAdapter = new TramListAdapter(northValues, this);
        northListView.setAdapter(northTramAdapter);

        southTramAdapter = new TramListAdapter(southValues, this);
        southListView.setAdapter(southTramAdapter);
    }

    private long getTramTimeInMins(String date) {

        String currentDateStr = new Date().toString();
        Log.d(TAG, "getTimeDiffInMinutes: " + currentDateStr);
        String scheduledDateStr = date;
        Log.d(TAG, "getTimeDiffInMinutes: " + scheduledDateStr);

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd hh:mm:ss Z yyyy");

        Date scheduledDate = null;
        Date currentDate = null;

        try {
            scheduledDate = sdf.parse(scheduledDateStr);
            currentDate = sdf.parse(currentDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //in milliseconds
        long diff = scheduledDate.getTime() - currentDate.getTime();
        long diffMinutes = diff / (60 * 1000) % 60;
        Log.d("MainActivity", "getTimeDiffInMinutes: " + diffMinutes);
        return diffMinutes;
    }

    /////////////
    // Convert .NET Date to Date
    ////////////
    private Date dateFromDotNetDate(String dotNetDate) {

        int startIndex = dotNetDate.indexOf("(") + 1;
        int endIndex = dotNetDate.indexOf("+");
        String date = dotNetDate.substring(startIndex, endIndex);

        Long unixTime = Long.parseLong(date);
        return new Date(unixTime);
    }

    ////////////
    // API
    ////////////

    private interface TramsApi {

        @GET("/TramTracker/RestService/GetDeviceToken/?aid=TTIOSJSON&devInfo=HomeTimeAndroid")
        Call<ApiResponse<Token>> token();

        @GET("/TramTracker/RestService//GetNextPredictedRoutesCollection/{stopId}/78/false/?aid=TTIOSJSON&cid=2")
        Call<ApiResponse<Tram>> trams(
                @Path("stopId") String stopId,
                @Query("tkn") String token
        );
    }

    private TramsApi createApiClient() {

        String BASE_URL = "http://ws3.tramtracker.com.au";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        return retrofit.create(TramsApi.class);
    }

    private class RequestToken extends AsyncTask<String, Integer, String> {

        TramsApi api;

        RequestToken(TramsApi api) {
            this.api = api;
        }

        @Override
        protected String doInBackground(String... params) {
            Call<ApiResponse<Token>> call = api.token();
            try {
                return call.execute().body().responseObject.get(0).value;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class RequestTrams extends AsyncTask<String, Integer, List<Tram>> {

        private TramsApi api;
        private String token;

        RequestTrams(TramsApi api, String token) {
            this.api = api;
            this.token = token;
        }

        @Override
        protected List<Tram> doInBackground(String... stops) {

            Call<ApiResponse<Tram>> call = api.trams(stops[0], token);
            try {
                Response<ApiResponse<Tram>> resp = call.execute();
                return resp.body().responseObject;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
