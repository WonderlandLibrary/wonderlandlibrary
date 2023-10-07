package me.errordev.http.request;

import java.net.HttpURLConnection;

public interface HTTPRequestCallback {
    void onCallback(HTTPRequest request, HttpURLConnection connection);
}