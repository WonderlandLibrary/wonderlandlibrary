// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestUtils
{
    public static String get(final URL url) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        final BufferedReader bfr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuilder sb = new StringBuilder();
        String s;
        while ((s = bfr.readLine()) != null) {
            sb.append(s);
            sb.append('\n');
        }
        bfr.close();
        conn.disconnect();
        return sb.toString();
    }
    
    public static String get(final URL url, final String agent) throws IOException {
        final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", agent);
        final BufferedReader bfr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuilder sb = new StringBuilder();
        String s;
        while ((s = bfr.readLine()) != null) {
            sb.append(s);
            sb.append('\n');
        }
        bfr.close();
        conn.disconnect();
        return sb.toString();
    }
}
