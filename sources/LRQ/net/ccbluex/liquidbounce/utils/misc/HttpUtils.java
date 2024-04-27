/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.io.TextStreamsKt
 *  kotlin.jvm.JvmStatic
 *  kotlin.text.Charsets
 *  org.apache.commons.io.FileUtils
 */
package net.ccbluex.liquidbounce.utils.misc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import kotlin.TypeCastException;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.JvmStatic;
import kotlin.text.Charsets;
import org.apache.commons.io.FileUtils;

public final class HttpUtils {
    private static final String DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
    public static final HttpUtils INSTANCE;

    private final HttpURLConnection make(String url, String method, String agent) {
        URLConnection uRLConnection = new URL(url).openConnection();
        if (uRLConnection == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.net.HttpURLConnection");
        }
        HttpURLConnection httpConnection = (HttpURLConnection)uRLConnection;
        httpConnection.setRequestMethod(method);
        httpConnection.setConnectTimeout(2000);
        httpConnection.setReadTimeout(10000);
        httpConnection.setRequestProperty("User-Agent", agent);
        httpConnection.setInstanceFollowRedirects(true);
        httpConnection.setDoOutput(true);
        return httpConnection;
    }

    static /* synthetic */ HttpURLConnection make$default(HttpUtils httpUtils, String string, String string2, String string3, int n, Object object) {
        if ((n & 4) != 0) {
            string3 = DEFAULT_AGENT;
        }
        return httpUtils.make(string, string2, string3);
    }

    public final String request(String url, String method, String agent) throws IOException {
        HttpURLConnection connection = this.make(url, method, agent);
        InputStream inputStream = connection.getInputStream();
        Charset charset = Charsets.UTF_8;
        boolean bl = false;
        return TextStreamsKt.readText((Reader)new InputStreamReader(inputStream, charset));
    }

    public static /* synthetic */ String request$default(HttpUtils httpUtils, String string, String string2, String string3, int n, Object object) throws IOException {
        if ((n & 4) != 0) {
            string3 = DEFAULT_AGENT;
        }
        return httpUtils.request(string, string2, string3);
    }

    public final InputStream requestStream(String url, String method, String agent) throws IOException {
        HttpURLConnection connection = this.make(url, method, agent);
        return connection.getInputStream();
    }

    public static /* synthetic */ InputStream requestStream$default(HttpUtils httpUtils, String string, String string2, String string3, int n, Object object) throws IOException {
        if ((n & 4) != 0) {
            string3 = DEFAULT_AGENT;
        }
        return httpUtils.requestStream(string, string2, string3);
    }

    @JvmStatic
    public static final String get(String url) throws IOException {
        return HttpUtils.request$default(INSTANCE, url, "GET", null, 4, null);
    }

    @JvmStatic
    public static final void download(String url, File file) throws IOException {
        FileUtils.copyInputStreamToFile((InputStream)HttpUtils.make$default(INSTANCE, url, "GET", null, 4, null).getInputStream(), (File)file);
    }

    private HttpUtils() {
    }

    static {
        HttpUtils httpUtils;
        INSTANCE = httpUtils = new HttpUtils();
        HttpURLConnection.setFollowRedirects(true);
    }
}

