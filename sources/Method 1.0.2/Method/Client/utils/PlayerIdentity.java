package Method.Client.utils;

import com.google.gson.*;

import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PlayerIdentity implements Serializable {
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final LinkedHashMap<String, PlayerIdentity> identityCacheMap = new LinkedHashMap<>();

    private String displayName;
    private final String stringUuid;
    private final String originalUuid;

    public PlayerIdentity(String stringUuid) {
        this.originalUuid = stringUuid;
        this.stringUuid = stringUuid.replace("-", "");
        this.displayName = "Loading...";

        executor.submit(() -> {
            try {
                updateNameAndCache();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateNameAndCache() throws IOException {
        this.displayName = getNameFromAPI(this.stringUuid);
        synchronized (identityCacheMap) {
            identityCacheMap.put(this.originalUuid, this);
        }
    }

    public static synchronized PlayerIdentity getPlayerIdentity(String UUID) {
        return identityCacheMap.getOrDefault(UUID, new PlayerIdentity(UUID));
    }

    private static String getNameFromAPI(String UUID) throws IOException {
        String jsonURL = "https://api.mojang.com/user/profiles/" + UUID + "/names";
        try (InputStream inputStream = new URL(jsonURL).openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String formattedjson = reader.lines().map(line -> line + "\n").collect(Collectors.joining());

            JsonArray array = new JsonParser().parse(formattedjson).getAsJsonArray();
            JsonObject obj = array.get(array.size() - 1).getAsJsonObject();

            String nameform = obj.get("name").getAsString();
            Instant changedAt = Instant.ofEpochMilli(obj.get("changedToAt").getAsLong());

            System.out.println("Name: " + nameform + ", Changed at: " + changedAt);
            return nameform;
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            throw new IOException("Error while parsing username history", e);
        }
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getStringUuid() {
        return this.originalUuid;
    }
}

