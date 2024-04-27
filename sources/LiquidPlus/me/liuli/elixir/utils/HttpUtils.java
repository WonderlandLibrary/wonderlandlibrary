/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.liuli.elixir.utils;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.MapsKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\bJ@\u0010\t\u001a\u00020\n2\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u00042\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\b2\b\b\u0002\u0010\r\u001a\u00020\u0004J,\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u00042\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\bJ@\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u00042\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\b2\b\b\u0002\u0010\r\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2={"Lme/liuli/elixir/utils/HttpUtils;", "", "()V", "DEFAULT_AGENT", "", "get", "url", "header", "", "make", "Ljava/net/HttpURLConnection;", "method", "data", "agent", "post", "request", "Elixir"})
public final class HttpUtils {
    @NotNull
    public static final HttpUtils INSTANCE = new HttpUtils();
    @NotNull
    private static final String DEFAULT_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";

    private HttpUtils() {
    }

    @NotNull
    public final HttpURLConnection make(@NotNull String url, @NotNull String method, @NotNull String data, @NotNull Map<String, String> header, @NotNull String agent) {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(data, "data");
        Intrinsics.checkNotNullParameter(header, "header");
        Intrinsics.checkNotNullParameter(agent, "agent");
        URLConnection uRLConnection = new URL(url).openConnection();
        if (uRLConnection == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.net.HttpURLConnection");
        }
        HttpURLConnection httpConnection = (HttpURLConnection)uRLConnection;
        httpConnection.setRequestMethod(method);
        httpConnection.setConnectTimeout(2000);
        httpConnection.setReadTimeout(10000);
        httpConnection.setRequestProperty("User-Agent", agent);
        Map<String, String> $this$forEach$iv = header;
        boolean $i$f$forEach = false;
        Iterator<Map.Entry<String, String>> iterator2 = $this$forEach$iv.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<String, String> element$iv;
            Map.Entry<String, String> $dstr$key$value = element$iv = iterator2.next();
            boolean bl = false;
            String key = $dstr$key$value.getKey();
            String value = $dstr$key$value.getValue();
            httpConnection.setRequestProperty(key, value);
        }
        httpConnection.setInstanceFollowRedirects(true);
        httpConnection.setDoOutput(true);
        if (((CharSequence)data).length() > 0) {
            DataOutputStream dataOutputStream = new DataOutputStream(httpConnection.getOutputStream());
            dataOutputStream.writeBytes(data);
            dataOutputStream.flush();
        }
        httpConnection.connect();
        return httpConnection;
    }

    public static /* synthetic */ HttpURLConnection make$default(HttpUtils httpUtils, String string, String string2, String string3, Map map, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string3 = "";
        }
        if ((n & 8) != 0) {
            map = MapsKt.emptyMap();
        }
        if ((n & 0x10) != 0) {
            string4 = DEFAULT_AGENT;
        }
        return httpUtils.make(string, string2, string3, map, string4);
    }

    @NotNull
    public final String request(@NotNull String url, @NotNull String method, @NotNull String data, @NotNull Map<String, String> header, @NotNull String agent) {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(data, "data");
        Intrinsics.checkNotNullParameter(header, "header");
        Intrinsics.checkNotNullParameter(agent, "agent");
        HttpURLConnection connection = this.make(url, method, data, header, agent);
        InputStream inputStream = connection.getInputStream();
        Intrinsics.checkNotNullExpressionValue(inputStream, "connection.inputStream");
        Charset charset = Charsets.UTF_8;
        return TextStreamsKt.readText(new InputStreamReader(inputStream, charset));
    }

    public static /* synthetic */ String request$default(HttpUtils httpUtils, String string, String string2, String string3, Map map, String string4, int n, Object object) {
        if ((n & 4) != 0) {
            string3 = "";
        }
        if ((n & 8) != 0) {
            map = MapsKt.emptyMap();
        }
        if ((n & 0x10) != 0) {
            string4 = DEFAULT_AGENT;
        }
        return httpUtils.request(string, string2, string3, map, string4);
    }

    @NotNull
    public final String get(@NotNull String url, @NotNull Map<String, String> header) {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(header, "header");
        return HttpUtils.request$default(this, url, "GET", null, header, null, 20, null);
    }

    public static /* synthetic */ String get$default(HttpUtils httpUtils, String string, Map map, int n, Object object) {
        if ((n & 2) != 0) {
            map = MapsKt.emptyMap();
        }
        return httpUtils.get(string, map);
    }

    @NotNull
    public final String post(@NotNull String url, @NotNull String data, @NotNull Map<String, String> header) {
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(data, "data");
        Intrinsics.checkNotNullParameter(header, "header");
        return HttpUtils.request$default(this, url, "POST", data, header, null, 16, null);
    }

    public static /* synthetic */ String post$default(HttpUtils httpUtils, String string, String string2, Map map, int n, Object object) {
        if ((n & 4) != 0) {
            map = MapsKt.emptyMap();
        }
        return httpUtils.post(string, string2, map);
    }
}

