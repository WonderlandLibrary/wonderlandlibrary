/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParser
 *  org.apache.http.Header
 *  org.apache.http.HttpEntity
 *  org.apache.http.client.methods.CloseableHttpResponse
 *  org.apache.http.client.methods.HttpGet
 *  org.apache.http.client.methods.HttpPost
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.entity.StringEntity
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.impl.client.HttpClients
 *  org.apache.http.message.BasicHeader
 *  org.apache.http.util.EntityUtils
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.utils.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0004J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0004J\u000e\u0010\u000b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0004\u00a8\u0006\f"}, d2={"Lnet/dev/important/utils/login/UserUtils;", "", "()V", "getUUID", "", "username", "getUsername", "uuid", "isValidToken", "", "token", "isValidTokenOffline", "LiquidBounce"})
public final class UserUtils {
    @NotNull
    public static final UserUtils INSTANCE = new UserUtils();

    private UserUtils() {
    }

    public final boolean isValidTokenOffline(@NotNull String token) {
        Intrinsics.checkNotNullParameter(token, "token");
        return token.length() >= 32;
    }

    public final boolean isValidToken(@NotNull String token) {
        Intrinsics.checkNotNullParameter(token, "token");
        CloseableHttpClient client = HttpClients.createDefault();
        BasicHeader[] basicHeaderArray = new BasicHeader[]{new BasicHeader("Content-Type", "application/json")};
        BasicHeader[] headers = basicHeaderArray;
        HttpPost request = new HttpPost("https://authserver.mojang.com/validate");
        request.setHeaders((Header[])headers);
        JSONObject body = new JSONObject();
        body.put("accessToken", token);
        request.setEntity((HttpEntity)new StringEntity(body.toString()));
        CloseableHttpResponse response = client.execute((HttpUriRequest)request);
        boolean valid = response.getStatusLine().getStatusCode() == 204;
        return valid;
    }

    @Nullable
    public final String getUsername(@NotNull String uuid) {
        String string;
        Intrinsics.checkNotNullParameter(uuid, "uuid");
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://api.mojang.com/user/profiles/" + uuid + "/names");
        CloseableHttpResponse response = client.execute((HttpUriRequest)request);
        if (response.getStatusLine().getStatusCode() != 200) {
            return null;
        }
        try {
            JSONArray names = new JSONArray(EntityUtils.toString((HttpEntity)response.getEntity()));
            string = new JSONObject(names.get(names.length() - 1).toString()).getString("name");
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String username = string;
        return username;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @NotNull
    public final String getUUID(@NotNull String username) {
        Intrinsics.checkNotNullParameter(username, "username");
        try {
            URLConnection uRLConnection = new URL(Intrinsics.stringPlus("https://api.mojang.com/users/profiles/minecraft/", username)).openConnection();
            if (uRLConnection == null) {
                throw new NullPointerException("null cannot be cast to non-null type javax.net.ssl.HttpsURLConnection");
            }
            HttpsURLConnection httpConnection = (HttpsURLConnection)uRLConnection;
            httpConnection.setConnectTimeout(2000);
            httpConnection.setReadTimeout(2000);
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            HttpURLConnection.setFollowRedirects(true);
            httpConnection.setDoOutput(true);
            if (httpConnection.getResponseCode() != 200) {
                return "";
            }
            Closeable closeable = new InputStreamReader(httpConnection.getInputStream());
            Throwable throwable = null;
            try {
                InputStreamReader it = (InputStreamReader)closeable;
                boolean bl = false;
                JsonElement jsonElement = new JsonParser().parse((Reader)it);
                if (jsonElement.isJsonObject()) {
                    String string = jsonElement.getAsJsonObject().get("id").getAsString();
                    Intrinsics.checkNotNullExpressionValue(string, "jsonElement.asJsonObject.get(\"id\").asString");
                    String string2 = string;
                    return string2;
                }
                Unit unit = Unit.INSTANCE;
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
            finally {
                CloseableKt.closeFinally(closeable, throwable);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return "";
    }
}

