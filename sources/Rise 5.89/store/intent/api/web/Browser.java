package store.intent.api.web;

import store.intent.api.EnvironmentConstants;
import store.intent.api.util.ConstructableEntry;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public class Browser implements EnvironmentConstants {

    public static String getResponse(final String getParameters) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) new URL(BASE + getParameters).openConnection();
        connection.addRequestProperty("User-Agent", USER_AGENT);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String lineBuffer;
        final StringBuilder response = new StringBuilder();
        while ((lineBuffer = reader.readLine()) != null)
            response.append(lineBuffer);

        return response.toString();
    }

    @SafeVarargs
    public static String postResponse(final String getParameters, final ConstructableEntry<String, String>... post) throws IOException {
        final HttpsURLConnection connection = (HttpsURLConnection) new URL(BASE + getParameters).openConnection();
        connection.addRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        final StringJoiner sj = new StringJoiner("&");
        for (final Map.Entry<String, String> entry : post)
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                    + URLEncoder.encode(entry.getValue(), "UTF-8"));
        final byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
        final int length = out.length;
        connection.setFixedLengthStreamingMode(length);
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.connect();
        try (OutputStream os = connection.getOutputStream()) {
            os.write(out);
        }

        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String lineBuffer;
        StringBuilder response = new StringBuilder();
        while ((lineBuffer = reader.readLine()) != null)
            response.append(lineBuffer);

        return response.toString();
    }
}