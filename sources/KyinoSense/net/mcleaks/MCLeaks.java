/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.mcleaks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.HttpsURLConnection;
import net.mcleaks.Callback;
import net.mcleaks.RedeemResponse;
import net.mcleaks.Session;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class MCLeaks {
    private static Session session;
    private static final ExecutorService EXECUTOR_SERVICE;
    private static final Gson gson;

    public static boolean isAltActive() {
        return session != null;
    }

    public static Session getSession() {
        return session;
    }

    public static void refresh(Session session) {
        MCLeaks.session = session;
    }

    public static void remove() {
        session = null;
    }

    public static void redeem(String token, Callback<Object> callback) {
        EXECUTOR_SERVICE.execute(() -> {
            URLConnection connection = MCLeaks.preparePostRequest("{\"token\":\"" + token + "\"}");
            if (connection == null) {
                callback.done("An error occured! [R1]");
                return;
            }
            Object o = MCLeaks.getResult(connection);
            if (o instanceof String) {
                callback.done(o);
                return;
            }
            JsonObject jsonObject = (JsonObject)o;
            if (jsonObject == null) {
                return;
            }
            if (!jsonObject.has("mcname") || !jsonObject.has("session")) {
                callback.done("An error occured! [R2]");
                return;
            }
            callback.done(new RedeemResponse(jsonObject.get("mcname").getAsString(), jsonObject.get("session").getAsString()));
        });
    }

    private static URLConnection preparePostRequest(String body) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection)new URL("https://auth.mcleaks.net/v1/redeem").openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; rv:2.2) Gecko/20110201");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
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

    private static Object getResult(URLConnection urlConnection) {
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            JsonElement jsonElement = (JsonElement)gson.fromJson(stringBuilder.toString(), JsonElement.class);
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

