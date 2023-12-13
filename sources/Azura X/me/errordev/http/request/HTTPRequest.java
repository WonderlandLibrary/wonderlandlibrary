package me.errordev.http.request;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@SuppressWarnings("unused")
public class HTTPRequest {

    private final HTTPMethod method;
    private final boolean secure;
    private final HashMap<String, String> argumentsMap;
    private final HTTPRequestCallback callback;
    private final int connectTimeout, readTimeout;

    public HTTPRequest(final boolean secure, final HTTPMethod method, final HashMap<String, String> argumentsMap,
                       final HTTPRequestCallback callback, final int connectTimeout, final int readTimeout) {
        this.secure = secure;
        this.method = method;
        this.argumentsMap = new HashMap<>();
        this.argumentsMap.put("Accept", "*/*");
        this.argumentsMap.put("Accept-Encoding", "gzip, deflate, br");
        this.argumentsMap.put("Accept-Language", "de,en-US;q=0.7,en;q=0.3");
        this.argumentsMap.put("Connection", "keep-alive");
        this.argumentsMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0");
        if (argumentsMap != null) this.argumentsMap.putAll(argumentsMap);
        this.callback = callback;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    public void connect(final URL targetURL) throws Exception {
        HttpURLConnection urlConnection;
        if (secure) urlConnection = (HttpsURLConnection) targetURL.openConnection();
        else urlConnection = (HttpURLConnection) targetURL.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setReadTimeout(readTimeout);
        urlConnection.setConnectTimeout(connectTimeout);
        urlConnection.setRequestMethod(method.name());
        for (final String key : argumentsMap.keySet()) urlConnection.setRequestProperty(key, argumentsMap.get(key));
        urlConnection.connect();
        if (urlConnection.getHeaderFields().containsKey("Location")) {
            connect(new URL(urlConnection.getHeaderField("Location")));
            return;
        }
        if (callback != null) callback.onCallback(this, urlConnection);
        urlConnection.disconnect();
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public boolean isSecure() {
        return secure;
    }

    public HashMap<String, String> getArgumentsMap() {
        return argumentsMap;
    }

    public HTTPRequestCallback getCallback() {
        return callback;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }
}