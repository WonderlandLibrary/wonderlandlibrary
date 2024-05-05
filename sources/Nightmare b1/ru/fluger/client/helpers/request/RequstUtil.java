// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.helpers.request;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class RequstUtil
{
    public String sendGet(final String s) {
        try {
            final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(s).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "");
            httpURLConnection.getResponseCode();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            final StringBuffer sb = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
            return sb.toString();
        }
        catch (Exception e) {
            return null;
        }
    }
}
