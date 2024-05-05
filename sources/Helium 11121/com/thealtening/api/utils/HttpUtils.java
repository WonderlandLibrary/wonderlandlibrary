package com.thealtening.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpUtils {

    protected String connect(String link) throws IOException {
        final URL url = new URL(link);
        final InputStream inputStream = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = br.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }
}
