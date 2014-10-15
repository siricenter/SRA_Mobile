package DataSync.httprequests;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.*;


import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import DataSync.GetSync;

/**
 * Created by Chad Carey on 10/15/2014.
 */
public class AsyncGetRequest {
    private AsyncHttpClient client = new AsyncHttpClient();
    private final String LOG_TAG = "AsyncGet";
    private static AsyncGetRequest instance;

    private AsyncGetRequest() {
    }

    public static AsyncGetRequest getInstance() {
        if(instance == null) {
            instance = new AsyncGetRequest();
        }
        return instance;
    }

    public void start(final GetSync caller) {
        Log.d("Started start() function", "");
        client.get(caller.getSyncAddress(), new JsonHttpResponseHandler () {

            @Override
            public void onStart() {
                Log.d(LOG_TAG, "Starting sync");
            }

            /**
             * Returns when request succeeds
             *
             * @param statusCode http response status line
             * @param headers    response headers if any
             * @param response   parsed response if any
             */
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(LOG_TAG, "Get was Successful");
                caller.update(response);
            }

            /**
             * Returns when request succeeds
             *
             * @param statusCode http response status line
             * @param headers    response headers if any
             * @param response   parsed response if any
             */
            //public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
              //  Log.w(LOG_TAG, "onSuccess(int, Header[], JSONArray) was not overriden, but callback was received");
            //}

            /**
             * Returns when request failed
             *
             * @param statusCode    http response status line
             * @param headers       response headers if any
             * @param throwable     throwable describing the way request failed
             * @param errorResponse parsed response if any
             */
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.w(LOG_TAG, "onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received", throwable);
            }

            /**
             * Returns when request failed
             *
             * @param statusCode    http response status line
             * @param headers       response headers if any
             * @param throwable     throwable describing the way request failed
             * @param errorResponse parsed response if any
             */
            //public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
              //  Log.w(LOG_TAG, "onFailure(int, Header[], Throwable, JSONArray) was not overriden, but callback was received", throwable);
            //}

            //@Override
            //public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
              //  Log.w(LOG_TAG, "onFailure(int, Header[], String, Throwable) was not overriden, but callback was received", throwable);
            //}

            //@Override
            //public void onSuccess(int statusCode, Header[] headers, String responseString) {
              //  Log.w(LOG_TAG, "onSuccess(int, Header[], String) was not overriden, but callback was received");
            //}
        });
    }
}
