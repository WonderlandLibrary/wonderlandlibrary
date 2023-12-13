// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.update;

import java.io.IOException;
import java.net.MalformedURLException;
import com.viaversion.viaversion.libs.gson.JsonParseException;
import com.viaversion.viaversion.util.GsonUtil;
import com.viaversion.viaversion.libs.gson.JsonObject;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import com.viaversion.viaversion.api.Via;
import java.util.UUID;

public class UpdateUtil
{
    private static final String PREFIX = "§a§l[ViaVersion] §a";
    private static final String URL = "https://api.spiget.org/v2/resources/";
    private static final int PLUGIN = 19254;
    private static final String LATEST_VERSION = "/versions/latest";
    
    public static void sendUpdateMessage(final UUID uuid) {
        Via.getPlatform().runAsync(() -> {
            final String message = getUpdateMessage(false);
            if (message != null) {
                Via.getPlatform().runSync(() -> Via.getPlatform().sendMessage(uuid, "§a§l[ViaVersion] §a" + message));
            }
        });
    }
    
    public static void sendUpdateMessage() {
        Via.getPlatform().runAsync(() -> {
            final String message = getUpdateMessage(true);
            if (message != null) {
                Via.getPlatform().runSync(() -> Via.getPlatform().getLogger().warning(message));
            }
        });
    }
    
    private static String getUpdateMessage(final boolean console) {
        if (Via.getPlatform().getPluginVersion().equals("${version}")) {
            return "You are using a debug/custom version, consider updating.";
        }
        final String newestString = getNewestVersion();
        if (newestString == null) {
            if (console) {
                return "Could not check for updates, check your connection.";
            }
            return null;
        }
        else {
            Version current;
            try {
                current = new Version(Via.getPlatform().getPluginVersion());
            }
            catch (final IllegalArgumentException e) {
                return "You are using a custom version, consider updating.";
            }
            final Version newest = new Version(newestString);
            if (current.compareTo(newest) < 0) {
                return "There is a newer plugin version available: " + newest + ", you're on: " + current;
            }
            if (!console || current.compareTo(newest) == 0) {
                return null;
            }
            final String tag = current.getTag().toLowerCase(Locale.ROOT);
            if (tag.startsWith("dev") || tag.startsWith("snapshot")) {
                return "You are running a development version of the plugin, please report any bugs to GitHub.";
            }
            return "You are running a newer version of the plugin than is released!";
        }
    }
    
    private static String getNewestVersion() {
        try {
            final URL url = new URL("https://api.spiget.org/v2/resources/19254/versions/latest?" + System.currentTimeMillis());
            final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "ViaVersion " + Via.getPlatform().getPluginVersion() + " " + Via.getPlatform().getPlatformName());
            connection.setDoOutput(true);
            final BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder builder = new StringBuilder();
            String input;
            while ((input = br.readLine()) != null) {
                builder.append(input);
            }
            br.close();
            JsonObject statistics;
            try {
                statistics = GsonUtil.getGson().fromJson(builder.toString(), JsonObject.class);
            }
            catch (final JsonParseException e) {
                e.printStackTrace();
                return null;
            }
            return statistics.get("name").getAsString();
        }
        catch (final MalformedURLException e2) {
            return null;
        }
        catch (final IOException e3) {
            return null;
        }
    }
}
