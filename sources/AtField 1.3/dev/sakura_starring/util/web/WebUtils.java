/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.jna.Native
 *  com.sun.jna.Pointer
 *  com.sun.jna.win32.StdCallLibrary
 *  com.sun.jna.win32.StdCallLibrary$StdCallCallback
 */
package dev.sakura_starring.util.web;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import liying.utils.WebUtils;

public class WebUtils {
    public static String readContent(String string) throws IOException {
        String string2;
        HttpURLConnection httpURLConnection = string.toLowerCase().startsWith("https://") ? (HttpURLConnection)new URL(string).openConnection() : (HttpURLConnection)new URL(string).openConnection();
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
        HttpURLConnection.setFollowRedirects(true);
        httpURLConnection.setDoOutput(true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        while ((string2 = bufferedReader.readLine()) != null) {
            stringBuilder.append(string2).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    public static String get(String string) throws IOException {
        String string2;
        HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        while ((string2 = bufferedReader.readLine()) != null) {
            stringBuilder.append(string2);
            stringBuilder.append("\n");
        }
        bufferedReader.close();
        String string3 = stringBuilder.toString();
        return string3.substring(0, string3.length() - 1);
    }

    public static interface User32
    extends StdCallLibrary {
        public static final WebUtils.User32 INSTANCE = (WebUtils.User32)Native.loadLibrary((String)"user32", WebUtils.User32.class);

        public boolean EnumWindows(WebUtils.User32.WNDENUMPROC var1, Pointer var2);

        public int GetWindowTextA(Pointer var1, byte[] var2, int var3);

        public static interface WNDENUMPROC
        extends StdCallLibrary.StdCallCallback {
            public boolean callback(Pointer var1, Pointer var2);
        }
    }
}

