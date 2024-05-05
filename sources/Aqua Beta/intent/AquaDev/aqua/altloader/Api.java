package intent.AquaDev.aqua.altloader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import intent.AquaDev.aqua.altloader.Callback;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.HttpsURLConnection;

public class Api {
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private static final String API_URL = "https://api.easymc.io/v1";
    private static final Gson gson = new Gson();

    public static void redeem(String token, Callback<Object> callback) {
        EXECUTOR_SERVICE.execute((Runnable)new /* Unavailable Anonymous Inner Class!! */);
    }

    static HttpsURLConnection preparePostRequest(String url, String body) {
        try {
            HttpsURLConnection con = (HttpsURLConnection)new URL(url).openConnection();
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write(body.getBytes("UTF-8"));
            wr.flush();
            wr.close();
            return con;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static Object getResult(HttpsURLConnection connection) {
        try {
            String line;
            InputStreamReader inputStreamReader = connection.getResponseCode() != 200 ? new InputStreamReader(connection.getErrorStream()) : new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader((Reader)inputStreamReader);
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            JsonElement jsonElement = (JsonElement)gson.fromJson(result.toString(), JsonElement.class);
            if (!jsonElement.isJsonObject()) {
                return "Could not parse response.";
            }
            if (jsonElement.getAsJsonObject().has("error")) {
                return jsonElement.getAsJsonObject().get("error").getAsString();
            }
            if (!(jsonElement.getAsJsonObject().has("session") && jsonElement.getAsJsonObject().has("uuid") && jsonElement.getAsJsonObject().has("mcName"))) {
                return "Response is invalid.";
            }
            return jsonElement.getAsJsonObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
