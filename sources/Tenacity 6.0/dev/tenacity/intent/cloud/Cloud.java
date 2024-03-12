// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud;

import com.google.gson.JsonElement;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringJoiner;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import java.util.Map;

public class Cloud
{
    private static String apiKey;
    private static final String productCode = "K3r30n8A";
    
    public static Request begin(final RequestType requestType) {
        return new Request(requestType.getExtension());
    }
    
    public static JsonObject request(String url, final Map<String, String> get, final Map<String, String> post) {
        url = url + "?" + get.entrySet().stream().map(entry -> {
            try {
                return URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()) + "=" + URLEncoder.encode((String)entry.getValue(), StandardCharsets.UTF_8.toString());
            }
            catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }).collect((Collector<? super Object, ?, String>)Collectors.joining("&"));
        try {
            final HttpsURLConnection conn = (HttpsURLConnection)new URL("https://intent.store/api/configuration/" + url).openConnection();
            conn.setRequestProperty("User-Agent", "Intent-API/1.0 TenacityBot");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            final StringJoiner sj = new StringJoiner("&");
            for (final Map.Entry<String, String> entry2 : post.entrySet()) {
                sj.add(URLEncoder.encode(entry2.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry2.getValue(), "UTF-8"));
            }
            final byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
            final int length = out.length;
            conn.setFixedLengthStreamingMode(length);
            conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.connect();
            final OutputStream os = conn.getOutputStream();
            os.write(out);
            final InputStream stream = (conn.getResponseCode() / 100 == 2) ? conn.getInputStream() : conn.getErrorStream();
            if (stream == null) {
                return null;
            }
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            final JsonElement element = JsonParser.parseReader((Reader)reader);
            if (element.isJsonObject()) {
                return element.getAsJsonObject();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    public static String getApiKey() {
        return Cloud.apiKey;
    }
    
    public static void setApiKey(final String apiKey) {
        Cloud.apiKey = apiKey;
    }
    
    public static String getProductCode() {
        return "K3r30n8A";
    }
}
