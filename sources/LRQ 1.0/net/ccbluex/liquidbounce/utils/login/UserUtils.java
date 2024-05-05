/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParser
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.io.CloseableKt
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
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package net.ccbluex.liquidbounce.utils.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.Closeable;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.io.CloseableKt;
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
import org.json.JSONArray;
import org.json.JSONObject;

public final class UserUtils {
    public static final UserUtils INSTANCE;

    public final boolean isValidTokenOffline(String token) {
        return token.length() >= 32;
    }

    public final boolean isValidToken(String token) {
        CloseableHttpClient client2 = HttpClients.createDefault();
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Content-Type", "application/json")};
        HttpPost request = new HttpPost("https://authserver.mojang.com/validate");
        request.setHeaders((Header[])headers);
        JSONObject body = new JSONObject();
        body.put("accessToken", (Object)token);
        request.setEntity((HttpEntity)new StringEntity(body.toString()));
        CloseableHttpResponse response = client2.execute((HttpUriRequest)request);
        boolean valid = response.getStatusLine().getStatusCode() == 204;
        return valid;
    }

    public final String getUsername(String uuid) {
        String string;
        HttpGet request;
        CloseableHttpClient client2 = HttpClients.createDefault();
        CloseableHttpResponse response = client2.execute((HttpUriRequest)(request = new HttpGet("https://api.mojang.com/user/profiles/" + uuid + "/names")));
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
    public final String getUUID(String username) {
        try {
            URLConnection uRLConnection = new URL("https://api.mojang.com/users/profiles/minecraft/" + username).openConnection();
            if (uRLConnection == null) {
                throw new TypeCastException("null cannot be cast to non-null type javax.net.ssl.HttpsURLConnection");
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
            boolean bl = false;
            Throwable throwable = null;
            try {
                InputStreamReader it = (InputStreamReader)closeable;
                boolean bl2 = false;
                JsonElement jsonElement = new JsonParser().parse((Reader)it);
                if (jsonElement.isJsonObject()) {
                    String string = jsonElement.getAsJsonObject().get("id").getAsString();
                    return string;
                }
                Unit unit = Unit.INSTANCE;
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
            finally {
                CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return "";
    }

    private UserUtils() {
    }

    static {
        UserUtils userUtils;
        INSTANCE = userUtils = new UserUtils();
    }
}

