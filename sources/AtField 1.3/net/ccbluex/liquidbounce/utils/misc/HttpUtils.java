/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteStreams
 *  kotlin.TypeCastException
 *  kotlin.io.CloseableKt
 *  kotlin.io.TextStreamsKt
 *  kotlin.jvm.JvmStatic
 *  kotlin.text.Charsets
 */
package net.ccbluex.liquidbounce.utils.misc;

import com.google.common.io.ByteStreams;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import kotlin.TypeCastException;
import kotlin.io.CloseableKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.JvmStatic;
import kotlin.text.Charsets;

public final class HttpUtils {
    public static final HttpUtils INSTANCE;
    private static final String DEFAULT_AGENT;

    @JvmStatic
    public static final long download(String string, File file) throws IOException {
        long l;
        Closeable closeable = new FileOutputStream(file);
        boolean bl = false;
        Throwable throwable = null;
        try {
            FileOutputStream fileOutputStream = (FileOutputStream)closeable;
            boolean bl2 = false;
            l = ByteStreams.copy((InputStream)HttpUtils.make$default(INSTANCE, string, "GET", null, 4, null).getInputStream(), (OutputStream)fileOutputStream);
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
        return l;
    }

    public final String request(String string, String string2, String string3) throws IOException {
        HttpURLConnection httpURLConnection = this.make(string, string2, string3);
        InputStream inputStream = httpURLConnection.getInputStream();
        Charset charset = Charsets.UTF_8;
        boolean bl = false;
        return TextStreamsKt.readText((Reader)new InputStreamReader(inputStream, charset));
    }

    public static InputStream requestStream$default(HttpUtils httpUtils, String string, String string2, String string3, int n, Object object) throws IOException {
        if ((n & 4) != 0) {
            string3 = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
        }
        return httpUtils.requestStream(string, string2, string3);
    }

    public static String request$default(HttpUtils httpUtils, String string, String string2, String string3, int n, Object object) throws IOException {
        if ((n & 4) != 0) {
            string3 = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
        }
        return httpUtils.request(string, string2, string3);
    }

    public final InputStream requestStream(String string, String string2, String string3) throws IOException {
        HttpURLConnection httpURLConnection = this.make(string, string2, string3);
        return httpURLConnection.getInputStream();
    }

    private final HttpURLConnection make(String string, String string2, String string3) {
        URLConnection uRLConnection = new URL(string).openConnection();
        if (uRLConnection == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.net.HttpURLConnection");
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRLConnection;
        httpURLConnection.setRequestMethod(string2);
        httpURLConnection.setConnectTimeout(2000);
        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setRequestProperty("User-Agent", string3);
        httpURLConnection.setInstanceFollowRedirects(true);
        httpURLConnection.setDoOutput(true);
        return httpURLConnection;
    }

    private HttpUtils() {
    }

    static {
        HttpUtils httpUtils;
        DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
        INSTANCE = httpUtils = new HttpUtils();
        HttpURLConnection.setFollowRedirects(true);
    }

    @JvmStatic
    public static final String get(String string) throws IOException {
        return HttpUtils.request$default(INSTANCE, string, "GET", null, 4, null);
    }

    static HttpURLConnection make$default(HttpUtils httpUtils, String string, String string2, String string3, int n, Object object) {
        if ((n & 4) != 0) {
            string3 = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
        }
        return httpUtils.make(string, string2, string3);
    }
}

