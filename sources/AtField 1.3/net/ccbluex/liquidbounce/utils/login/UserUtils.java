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

    public final String getUsername(String string) {
        Object object;
        HttpGet httpGet;
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute((HttpUriRequest)(httpGet = new HttpGet("https://api.mojang.com/user/profiles/" + string + "/names")));
        if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
            return null;
        }
        try {
            object = new JSONArray(EntityUtils.toString((HttpEntity)closeableHttpResponse.getEntity()));
            object = new JSONObject(object.get(object.length() - 1).toString()).getString("name");
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        Object object2 = object;
        return object2;
    }

    public final String getUUID(String string) {
        InputStreamReader inputStreamReader;
        Throwable throwable;
        Closeable closeable;
        block5: {
            String string2;
            URLConnection uRLConnection = new URL("https://api.mojang.com/users/profiles/minecraft/" + string).openConnection();
            if (uRLConnection == null) {
                throw new TypeCastException("null cannot be cast to non-null type javax.net.ssl.HttpsURLConnection");
            }
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection)uRLConnection;
            httpsURLConnection.setConnectTimeout(2000);
            httpsURLConnection.setReadTimeout(2000);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            HttpURLConnection.setFollowRedirects(true);
            httpsURLConnection.setDoOutput(true);
            if (httpsURLConnection.getResponseCode() != 200) {
                return "";
            }
            closeable = new InputStreamReader(httpsURLConnection.getInputStream());
            boolean bl = false;
            throwable = null;
            try {
                inputStreamReader = (InputStreamReader)closeable;
                boolean bl2 = false;
                JsonElement jsonElement = new JsonParser().parse((Reader)inputStreamReader);
                if (!jsonElement.isJsonObject()) break block5;
                string2 = jsonElement.getAsJsonObject().get("id").getAsString();
            }
            catch (Throwable throwable2) {
                throwable = throwable2;
                throw throwable2;
            }
            CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
            return string2;
        }
        inputStreamReader = Unit.INSTANCE;
        CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
        return "";
    }

    public final boolean isValidToken(String string) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        BasicHeader[] basicHeaderArray = new BasicHeader[]{new BasicHeader("Content-Type", "application/json")};
        HttpPost httpPost = new HttpPost("https://authserver.mojang.com/validate");
        httpPost.setHeaders((Header[])basicHeaderArray);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("accessToken", (Object)string);
        httpPost.setEntity((HttpEntity)new StringEntity(jSONObject.toString()));
        CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute((HttpUriRequest)httpPost);
        boolean bl = closeableHttpResponse.getStatusLine().getStatusCode() == 204;
        return bl;
    }

    static {
        UserUtils userUtils;
        INSTANCE = userUtils = new UserUtils();
    }

    public final boolean isValidTokenOffline(String string) {
        return string.length() >= 32;
    }

    private UserUtils() {
    }
}

