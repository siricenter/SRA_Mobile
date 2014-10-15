package DataSync.httprequests;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.*;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import DataSync.GetSync;

/**
 * Created by Chad Carey on 10/15/2014.
 */
public class AsyncGetter {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String LOG_TAG = "AsyncGetter";

    public void start(GetSync caller) {
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
                Log.w(LOG_TAG, "onSuccess(int, Header[], JSONObject) was not overriden, but callback was received");
            }

            /**
             * Returns when request succeeds
             *
             * @param statusCode http response status line
             * @param headers    response headers if any
             * @param response   parsed response if any
             */
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.w(LOG_TAG, "onSuccess(int, Header[], JSONArray) was not overriden, but callback was received");
            }

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
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.w(LOG_TAG, "onFailure(int, Header[], Throwable, JSONArray) was not overriden, but callback was received", throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w(LOG_TAG, "onFailure(int, Header[], String, Throwable) was not overriden, but callback was received", throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.w(LOG_TAG, "onSuccess(int, Header[], String) was not overriden, but callback was received");
            }
        });
    }
}
