package dev.tenacity.util.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CloudUtils {

    private static final String API_KEY = "id4uwJLq";
    private static final String PRODUCT_CODE = "K3r30n8A";

    public static JsonObject request(String url, Map<String, String> get, Map<String, String> post) throws IOException {

        return null;
    }


    public static JsonArray listAllData() {
        HashMap<String, String> get = createGetMap();
        HashMap<String, String> post = new HashMap<>();
        try {
            JsonObject response = request("list", get, post);
            if (response != null && response.has("response") && response.get("response").getAsString().equals("success")) {
                return response.get("configurations").getAsJsonArray();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonObject getData(String shareCode) {
        HashMap<String, String> get = createGetMap();
        HashMap<String, String> post = new HashMap<>();

        get.put("share_code", shareCode);

        try {
            JsonObject response = request("get", get, post);
            if (response != null && response.has("response") && response.get("response").getAsString().equals("success")) {
                return response.get("configuration").getAsJsonObject();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static HashMap<String, String> createGetMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", API_KEY);
        map.put("product_code", PRODUCT_CODE);
        return map;
    }

}
