package sudo.utils.world;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import sudo.utils.TimerUtil;

public enum MCAPIHelper {
    INSTANCE;

    private final String NAME_API_URL = "https://api.mojang.com/users/profiles/minecraft/%s";
    private final String UUID_API_URL = "https://api.mojang.com/user/profiles/%s/names";
    @SuppressWarnings("unused")
    private final String PROFILE_REQUEST_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s";
    private final String STATUS_URL = "https://status.mojang.com/check";

    private static final Identifier STEVE_SKIN = new Identifier("textures/entity/steve.png");

    private HashMap<UUID, String> uuidMap = Maps.newHashMap();
    private HashMap<UUID, Identifier> playerSkins = Maps.newHashMap();
    private HashMap<String, UUID> nameMap = Maps.newHashMap();
    private HashMap<APIServer, APIStatus> serverStatusMap = Maps.newHashMap();
    private ArrayList<String> avatarsRequested = new ArrayList<>();
    private TimerUtil timer = new TimerUtil();
    Gson gson = new Gson();

    public String getNameFromUUID(UUID uuid) {
        if (uuid == null)
            return "UUID null";
        if (getStatus(APIServer.API_MOJANG) == APIStatus.RED)
            return "API Server down";
        if (uuidMap.containsKey(uuid))
            return uuidMap.get(uuid);
        String result = null;
        try {
            result = readURL(new URL(String.format(UUID_API_URL, uuid.toString().replace("-", ""))));
        } catch (IOException e) {
        }
        if (result == null)
            return "Player Not found";
        JsonArray nameArray = gson.fromJson(result.toString(), JsonArray.class);
        try {
            JsonObject object = nameArray.get(nameArray.size() - 1).getAsJsonObject();

            String name = object.get("name").getAsString();
            uuidMap.putIfAbsent(uuid, name);
            return name;
        } catch (Exception e) {
            return "Error";
        }
    }

    public String readURL(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10 * 1000);
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder buffer = new StringBuilder();
        for (String line; (line = input.readLine()) != null; ) {
            buffer.append(line);
            buffer.append("\n");
        }
        input.close();
        return buffer.toString();
    }

    public boolean openLink(String url) {
        try {
            openLinkOnOS(url);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openLinkOnOS(String url) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("win") >= 0) {
            Runtime rt = Runtime.getRuntime();
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else if (os.indexOf("mac") >= 0) {
            Runtime rt = Runtime.getRuntime();
            rt.exec("open " + url);
        } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            Runtime rt = Runtime.getRuntime();
            String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                    "netscape", "opera", "links", "lynx"};

            StringBuffer cmd = new StringBuffer();
            for (int i = 0; i < browsers.length; i++)
                if (i == 0)
                    cmd.append(String.format("%s \"%s\"", browsers[i], url));
                else
                    cmd.append(String.format(" || %s \"%s\"", browsers[i], url));

            rt.exec(new String[]{"sh", "-c", cmd.toString()});

        }
    }

    public UUID getUUIDFromName(String name) {
        try {
            if (nameMap.containsKey(name))
                return nameMap.get(name);
            String result = null;
            try {
                result = readURL(new URL(String.format(NAME_API_URL, name)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result == null)
                return null;
            JsonObject object =  gson.fromJson(result, JsonObject.class);
            UUID uuid = UUID.fromString(object.get("id").getAsString().replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            ));
            nameMap.putIfAbsent(name, uuid);
            return uuid;
        } catch (Exception e) {
            return null;
        }
    }

    public void downloadPlayerSkin(UUID uuid) {
        if (uuid == null || avatarsRequested.contains(uuid.toString().replace("-", "")))
            return;
        GameProfile gameProfile = new GameProfile(uuid, "skindl");//name doesn't matter because the url uses the uuid
        avatarsRequested.add(uuid.toString().replace("-", ""));
        //using the handy dandy method Minecraft uses because it actually lets you do something with it rather than just automatically storing them
        MinecraftClient.getInstance().getSkinProvider().loadSkin(gameProfile, (type, identifier, minecraftProfileTexture) -> {
            if (type == MinecraftProfileTexture.Type.SKIN) {
                playerSkins.put(uuid, identifier);
            }

        }, true);
    }

    public Identifier getPlayerSkin(UUID uuid) {
        if (playerSkins.containsKey(uuid)) {
            return playerSkins.get(uuid);
        } else {
            downloadPlayerSkin(uuid);
        }
        return STEVE_SKIN;
    }

    public APIStatus getStatus(APIServer server) {
        updateStatus();
        if (!serverStatusMap.containsKey(server))
            return APIStatus.RED;
        return serverStatusMap.get(server);
    }

    private void updateStatus() {
        if (!timer.hasTimeElapsed(30000, true))
            return;
        serverStatusMap.clear();
        String result = null;
        try {
            result = readURL(new URL(STATUS_URL));
        } catch (IOException e) {
        }
        if (result == null)
            return;
        JsonArray nameArray = gson.fromJson(result, JsonArray.class);
        for (int i = 0; i < nameArray.size(); i++) {
            JsonObject object1 = nameArray.get(i).getAsJsonObject();
            String serverName = object1.toString().split("\"")[1];
            String response = object1.get(serverName).getAsString();

            serverStatusMap.put(getServer(serverName), APIStatus.valueOf(response.toUpperCase()));
        }
        timer.reset();
    }

    private APIServer getServer(String server) {
        switch (server) {
            case "minecraft.net":
                return APIServer.MINECRAFT_NET;
            case "session.minecraft.net":
                return APIServer.SESSION;
            case "account.mojang.com":
                return APIServer.ACCOUNT;
            case "authserver.mojang.com"://
                return APIServer.AUTHSERVER;
            case "sessionserver.mojang.com"://
                return APIServer.SESSIONSERVER;
            case "api.mojang.com"://
                return APIServer.API_MOJANG;
            case "textures.minecraft.net"://
                return APIServer.TEXTURES;
            case "mojang.com"://
                return APIServer.MOJANG_COM;
        }
        return null;
    }


    public enum APIStatus {
        GREEN, YELLOW, RED
    }

    public enum APIServer {
        MINECRAFT_NET("Minecraft.net"),
        SESSION("session.Minecraft.net"),
        ACCOUNT("account.Mojang.com"),
        AUTHSERVER("authserver.Mojang.com"),
        SESSIONSERVER("sessionserver.Mojang.com"),
        API_MOJANG("api.Mojang.com"),
        TEXTURES("textures.Minecraft.net"),
        MOJANG_COM("Mojang.com");

        private final String name;

        APIServer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
