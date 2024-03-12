// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.intent.cloud;

import java.util.Map;
import java.math.BigInteger;
import java.security.MessageDigest;
import lombok.NonNull;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.misc.FileUtils;
import dev.tenacity.intent.cloud.data.CloudData;
import dev.tenacity.intent.cloud.data.VoteType;
import dev.tenacity.intent.api.account.IntentAccount;
import com.google.gson.JsonParser;
import dev.tenacity.utils.misc.StringUtils;
import com.google.gson.JsonArray;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import dev.tenacity.Tenacity;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import dev.tenacity.utils.misc.NetworkingUtils;
import com.google.gson.JsonObject;

public class CloudUtils
{
    public static String postOnlineConfig(final String name, final String description, final String server, final String data) {
        final String s;
        final JsonObject response = Cloud.begin(RequestType.UPLOAD).modifyGetMap(map -> {
            map.put("name", name);
            map.put("meta", "script:false:version:6.0");
            map.put("server", server);
            map.put("description", description);
            map.put("published", "1");
            return;
        }).modifyPostMap(map -> s = map.put("body", data)).post();
        if (response != null && response.has("configuration")) {
            final JsonObject config = response.get("configuration").getAsJsonObject();
            return config.get("share_code").getAsString();
        }
        return null;
    }
    
    public static String postOnlineScript(final String name, final String description, final String data) {
        final String s;
        final JsonObject response = Cloud.begin(RequestType.UPLOAD).modifyGetMap(map -> {
            map.put("name", name);
            map.put("meta", "script:true:version:6.0");
            map.put("description", description);
            map.put("published", "1");
            return;
        }).modifyPostMap(map -> s = map.put("body", data)).post();
        if (response != null && response.has("configuration")) {
            System.out.println(response);
            final JsonObject script = response.get("configuration").getAsJsonObject();
            return script.get("share_code").getAsString();
        }
        return null;
    }
    
    public static boolean updateData(final String shareCode, final String description, final String data, final boolean script) {
        final String s;
        final JsonObject response = Cloud.begin(RequestType.UPDATE).modifyGetMap(map -> {
            map.put("share_code", shareCode);
            map.put("meta", "script:" + script + ":version:" + "6.0");
            map.put("description", description);
            return;
        }).modifyPostMap(map -> s = map.put("body", data)).post();
        return response != null && response.has("configuration") && response.has("configuration");
    }
    
    public static JsonObject getData(final String shareCode) {
        final String s;
        final JsonObject response = Cloud.begin(RequestType.RETRIEVE).modifyGetMap(map -> s = map.put("share_code", shareCode)).post();
        if (response != null && response.has("configuration")) {
            return response.get("configuration").getAsJsonObject();
        }
        return null;
    }
    
    public static boolean deleteData(final String shareCode) {
        try {
            final String s;
            final JsonObject response = Cloud.begin(RequestType.DELETE).modifyGetMap(map -> s = map.put("share_code", shareCode)).post();
            if (response == null) {
                return false;
            }
            if (response.get("response").isJsonObject()) {
                final String errorResponse = response.get("response").getAsJsonObject().get("error").getAsString();
                System.err.println(errorResponse);
                return false;
            }
            System.out.println(response);
            NetworkingUtils.bypassSSL();
            final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://senoe.win/tenacity/configs").openConnection();
            connection.setRequestProperty("User-Agent", "tenacity/6.0");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            final int uid = Tenacity.INSTANCE.getIntentAccount().client_uid;
            final JsonObject obj = new JsonObject();
            obj.addProperty("action", "delete");
            obj.addProperty("uid", (Number)uid);
            obj.addProperty("secret", md5("aa" + uid + "zz"));
            obj.addProperty("share_code", shareCode);
            try (final OutputStream os = connection.getOutputStream()) {
                final byte[] input = new Gson().toJson((JsonElement)obj).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            connection.connect();
            return true;
        }
        catch (Throwable $ex) {
            throw $ex;
        }
    }
    
    public static JsonArray listAllData() {
        final JsonObject response = Cloud.begin(RequestType.LIST).post();
        final int pagesLeft = response.get("total_pages").getAsInt() - 1;
        JsonArray jsonArray = new JsonArray();
        if (response.has("configurations")) {
            jsonArray = response.get("configurations").getAsJsonArray();
        }
        if (pagesLeft > 0) {
            for (int i = 0; i < pagesLeft; ++i) {
                final int finalI = i + 2;
                final String s;
                final JsonObject pageResponse = Cloud.begin(RequestType.LIST).modifyGetMap(map -> s = map.put("page", String.valueOf(finalI))).post();
                jsonArray.addAll(pageResponse.get("configurations").getAsJsonArray());
            }
        }
        return jsonArray;
    }
    
    public static JsonObject getVoteData() {
        final IntentAccount intentAccount = Tenacity.INSTANCE.getIntentAccount();
        final NetworkingUtils.HttpResponse r = NetworkingUtils.httpsConnection(String.format("https://senoe.win/tenacity/configs?uid=%s&i=%s&e=%s&u=%s&ak=%s", intentAccount.client_uid, intentAccount.intent_uid, StringUtils.b64(intentAccount.email).replace('=', '_'), StringUtils.b64(intentAccount.username).replace('=', '_'), StringUtils.b64(intentAccount.api_key).replace('=', '_')));
        if (r != null && r.getResponse() == 200) {
            final JsonElement el = JsonParser.parseString(r.getContent());
            if (el.isJsonObject()) {
                final JsonObject obj = el.getAsJsonObject();
                if (obj.get("success").getAsBoolean()) {
                    return obj;
                }
            }
        }
        return null;
    }
    
    public static void vote(VoteType type, final CloudData config) {
        try {
            if ((type == VoteType.UP && config.isUpvoted()) || (type == VoteType.DOWN && config.isDownvoted())) {
                type = VoteType.UNVOTE;
            }
            NetworkingUtils.bypassSSL();
            final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://senoe.win/tenacity/configs").openConnection();
            connection.setRequestProperty("User-Agent", "tenacity/6.0");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            final int uid = Tenacity.INSTANCE.getIntentAccount().client_uid;
            final JsonObject obj = new JsonObject();
            obj.addProperty("action", type.getActionName());
            obj.addProperty("uid", (Number)uid);
            obj.addProperty("secret", md5("aa" + uid + "zz"));
            obj.addProperty("share_code", config.getShareCode());
            try (final OutputStream os = connection.getOutputStream()) {
                final byte[] input = new Gson().toJson((JsonElement)obj).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            connection.connect();
            final NetworkingUtils.HttpResponse r = new NetworkingUtils.HttpResponse(FileUtils.readInputStream((connection.getResponseCode() >= 400) ? connection.getErrorStream() : connection.getInputStream()), connection.getResponseCode());
            if (r.getResponse() == 200) {
                if (type == VoteType.UNVOTE) {
                    config.unvote();
                }
                else if (type == VoteType.UP) {
                    config.upvote();
                }
                else {
                    config.downvote();
                }
                Tenacity.INSTANCE.getCloudDataManager().refreshVotes();
            }
            else {
                NotificationManager.post(NotificationType.DISABLE, "Cloud Configs", "Failed to " + type.getActionName() + " \"" + config.getName() + "\".", 1.0f);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String md5(@NonNull final String input) {
        try {
            if (input == null) {
                throw new NullPointerException("input is marked non-null but is null");
            }
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            final BigInteger hash = new BigInteger(1, md.digest());
            final StringBuilder result = new StringBuilder(hash.toString(16));
            while (result.length() < 32) {
                result.insert(0, "0");
            }
            return result.toString();
        }
        catch (Throwable $ex) {
            throw $ex;
        }
    }
}
