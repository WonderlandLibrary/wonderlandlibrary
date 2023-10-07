/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.misc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0007J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H\u0007J\"\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u00042\b\b\u0002\u0010\u000e\u001a\u00020\u0004H\u0002J \u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u00042\b\b\u0002\u0010\u000e\u001a\u00020\u0004J\"\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u00042\b\b\u0002\u0010\u000e\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/utils/misc/HttpUtils;", "", "()V", "DEFAULT_AGENT", "", "download", "", "url", "file", "Ljava/io/File;", "get", "make", "Ljava/net/HttpURLConnection;", "method", "agent", "request", "requestStream", "Ljava/io/InputStream;", "KyinoClient"})
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

    @NotNull
    public final String request(@NotNull String url, @NotNull String method, @NotNull String agent) throws IOException {
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.checkParameterIsNotNull(agent, "agent");
        HttpURLConnection connection = this.make(url, method, agent);
        InputStream inputStream = connection.getInputStream();
        Intrinsics.checkExpressionValueIsNotNull(inputStream, "connection.inputStream");
        InputStream inputStream2 = inputStream;
        Charset charset = Charsets.UTF_8;
        boolean bl = false;
        return TextStreamsKt.readText(new InputStreamReader(inputStream2, charset));
    }

    public static /* synthetic */ String request$default(HttpUtils httpUtils, String string, String string2, String string3, int n, Object object) throws IOException {
        if ((n & 4) != 0) {
            string3 = DEFAULT_AGENT;
        }
        return httpUtils.request(string, string2, string3);
    }

    @Nullable
    public final InputStream requestStream(@NotNull String url, @NotNull String method, @NotNull String agent) throws IOException {
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(method, "method");
        Intrinsics.checkParameterIsNotNull(agent, "agent");
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
    @NotNull
    public static final String get(@NotNull String url) throws IOException {
        Intrinsics.checkParameterIsNotNull(url, "url");
        return HttpUtils.request$default(INSTANCE, url, "GET", null, 4, null);
    }

    @JvmStatic
    public static final void download(@NotNull String url, @NotNull File file) throws IOException {
        Intrinsics.checkParameterIsNotNull(url, "url");
        Intrinsics.checkParameterIsNotNull(file, "file");
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

