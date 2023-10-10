// 
// Decompiled by Procyon v0.5.36
// 

package net.mcleaks;

import java.util.concurrent.Executors;
import com.google.gson.JsonElement;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.DataOutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.URLConnection;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import java.util.concurrent.ExecutorService;

public class MCLeaks
{
    private static Session session;
    private static final ExecutorService EXECUTOR_SERVICE;
    private static final Gson gson;
    
    public static boolean isAltActive() {
        return MCLeaks.session != null;
    }
    
    public static Session getSession() {
        return MCLeaks.session;
    }
    
    public static void refresh(final Session session) {
        MCLeaks.session = session;
    }
    
    public static void remove() {
        MCLeaks.session = null;
    }
    
    public static void redeem(final String token, final Callback<Object> callback) {
        final URLConnection connection;
        Object o;
        JsonObject jsonObject;
        MCLeaks.EXECUTOR_SERVICE.execute(() -> {
            connection = preparePostRequest("{\"token\":\"" + token + "\"}");
            if (connection == null) {
                callback.done("An error occured! [R1]");
            }
            else {
                o = getResult(connection);
                if (o instanceof String) {
                    callback.done(o);
                }
                else {
                    jsonObject = (JsonObject)o;
                    if (jsonObject != null) {
                        if (!jsonObject.has("mcname") || !jsonObject.has("session")) {
                            callback.done("An error occured! [R2]");
                        }
                        else {
                            callback.done(new RedeemResponse(jsonObject.get("mcname").getAsString(), jsonObject.get("session").getAsString()));
                        }
                    }
                }
            }
        });
    }
    
    private static URLConnection preparePostRequest(final String body) {
        try {
            final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://auth.mcleaks.net/v1/redeem").openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; rv:2.2) Gecko/20110201");
            connection.setDoOutput(true);
            final DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(body.getBytes(StandardCharsets.UTF_8));
            dataOutputStream.flush();
            dataOutputStream.close();
            return connection;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static Object getResult(final URLConnection urlConnection) {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            final StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            final JsonElement jsonElement = MCLeaks.gson.fromJson(stringBuilder.toString(), JsonElement.class);
            if (!jsonElement.isJsonObject() || !jsonElement.getAsJsonObject().has("success")) {
                return "An error occured! [G1]";
            }
            if (!jsonElement.getAsJsonObject().get("success").getAsBoolean()) {
                return jsonElement.getAsJsonObject().has("errorMessage") ? jsonElement.getAsJsonObject().get("errorMessage").getAsString() : "An error occured! [G4]";
            }
            if (!jsonElement.getAsJsonObject().has("result")) {
                return "An error occured! [G3]";
            }
            return jsonElement.getAsJsonObject().get("result").isJsonObject() ? jsonElement.getAsJsonObject().get("result").getAsJsonObject() : null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "An error occured! [G2]";
        }
    }
    
    static {
        EXECUTOR_SERVICE = Executors.newCachedThreadPool();
        gson = new Gson();
    }
}
