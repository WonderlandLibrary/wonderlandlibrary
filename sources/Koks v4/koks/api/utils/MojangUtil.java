package koks.api.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.Tuple;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class MojangUtil {
    private static MojangUtil instance;

    public static MojangUtil getInstance() {
        if(instance == null)
            instance = new MojangUtil();
        return instance;
    }

    public Tuple<Integer, String> changeSkin(SkinVariant variant, String skinUrl, String token) throws IOException {
        final URL url = new URL("https://api.minecraftservices.com/minecraft/profile/skins");
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");

        final JsonObject object = new JsonObject();
        object.addProperty("variant", variant.name());
        object.addProperty("url", skinUrl);

        final String json = object.toString();

        byte[] out = json.getBytes(StandardCharsets.UTF_8);
        final OutputStream outputStream = connection.getOutputStream();
        outputStream.write(out);
        final int response = connection.getResponseCode();
        final String message = connection.getResponseMessage();
        connection.disconnect();
        return new Tuple<>(response, message);
    }

    public Tuple<Integer, String> changeName(String name, String token) throws IOException {
        final URL url = new URL("https://api.minecraftservices.com/minecraft/profile/name/" + name);
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Bearer " + token);
        connection.setRequestProperty("Content-Type", "application/json");
        final int response = connection.getResponseCode();
        final String message = connection.getResponseMessage();
        connection.disconnect();
        return new Tuple<>(response, message);
    }

    public String getUUID(String name) throws IOException {
        final URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        final InputStream inputStream = connection.getInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        final StringBuilder builder = new StringBuilder();
        while((line = reader.readLine()) != null)
            builder.append(line).append("\n");
        final JsonParser parser = new JsonParser();
        final JsonObject json = (JsonObject) parser.parse(builder.toString());
        final String uuid = json.get("id").getAsString();
        connection.disconnect();
        return uuid;
    }

    public String getSkin(String uuid) throws IOException {
        final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        final InputStream inputStream = connection.getInputStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        final StringBuilder builder = new StringBuilder();
        while((line = reader.readLine()) != null)
            builder.append(line).append("\n");
        final JsonParser parser = new JsonParser();
        final JsonObject json = (JsonObject) parser.parse(builder.toString());
        final JsonArray array = json.getAsJsonArray("properties");
        final JsonObject properties = array.get(0).getAsJsonObject();
        final String encoded = properties.get("value").getAsString();
        final String decoded = new String(Base64.getDecoder().decode(encoded));
        final JsonObject propertiesObject = (JsonObject) parser.parse(decoded);
        final JsonObject textures = propertiesObject.getAsJsonObject("textures");
        final JsonObject skin = textures.getAsJsonObject("SKIN");
        final String skinUrl = skin.get("url").getAsString();
        connection.disconnect();
        return skinUrl;
    }

    public enum SkinVariant {
        CLASSIC("classic"), SLIM("slim");

        SkinVariant(String name) {
        }
    }
}
