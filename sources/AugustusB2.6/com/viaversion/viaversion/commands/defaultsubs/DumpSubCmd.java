// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.commands.defaultsubs;

import java.util.Map;
import java.io.OutputStream;
import java.io.InvalidObjectException;
import com.viaversion.viaversion.util.GsonUtil;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.google.common.io.CharStreams;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import com.viaversion.viaversion.libs.gson.GsonBuilder;
import java.io.IOException;
import java.util.logging.Level;
import java.net.URL;
import java.net.HttpURLConnection;
import com.viaversion.viaversion.dump.DumpTemplate;
import java.util.Set;
import com.viaversion.viaversion.dump.VersionInfo;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.command.ViaCommandSender;
import com.viaversion.viaversion.api.command.ViaSubCommand;

public class DumpSubCmd extends ViaSubCommand
{
    @Override
    public String name() {
        return "dump";
    }
    
    @Override
    public String description() {
        return "Dump information about your server, this is helpful if you report bugs.";
    }
    
    @Override
    public boolean execute(final ViaCommandSender sender, final String[] args) {
        final VersionInfo version = new VersionInfo(System.getProperty("java.version"), System.getProperty("os.name"), Via.getAPI().getServerVersion().lowestSupportedVersion(), Via.getManager().getProtocolManager().getSupportedVersions(), Via.getPlatform().getPlatformName(), Via.getPlatform().getPlatformVersion(), Via.getPlatform().getPluginVersion(), "git-ViaVersion-4.1.2-SNAPSHOT:a1a0926", Via.getManager().getSubPlatforms());
        final Map<String, Object> configuration = Via.getPlatform().getConfigurationProvider().getValues();
        final DumpTemplate template = new DumpTemplate(version, configuration, Via.getPlatform().getDump(), Via.getManager().getInjector().getDump());
        Via.getPlatform().runAsync(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    con = (HttpURLConnection)new URL("https://dump.viaversion.com/documents").openConnection();
                }
                catch (IOException e) {
                    sender.sendMessage("§4Failed to dump, please check the console for more information");
                    Via.getPlatform().getLogger().log(Level.WARNING, "Could not paste ViaVersion dump to ViaVersion Dump", e);
                    return;
                }
                try {
                    con.setRequestProperty("Content-Type", "application/json");
                    con.addRequestProperty("User-Agent", "ViaVersion/" + version.getPluginVersion());
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    final OutputStream out = con.getOutputStream();
                    out.write(new GsonBuilder().setPrettyPrinting().create().toJson(template).getBytes(StandardCharsets.UTF_8));
                    out.close();
                    if (con.getResponseCode() == 429) {
                        sender.sendMessage("§4You can only paste once every minute to protect our systems.");
                        return;
                    }
                    final String rawOutput = CharStreams.toString(new InputStreamReader(con.getInputStream()));
                    con.getInputStream().close();
                    final JsonObject output = GsonUtil.getGson().fromJson(rawOutput, JsonObject.class);
                    if (!output.has("key")) {
                        throw new InvalidObjectException("Key is not given in Hastebin output");
                    }
                    sender.sendMessage("§2We've made a dump with useful information, report your issue and provide this url: " + DumpSubCmd.this.getUrl(output.get("key").getAsString()));
                }
                catch (Exception e2) {
                    sender.sendMessage("§4Failed to dump, please check the console for more information");
                    Via.getPlatform().getLogger().log(Level.WARNING, "Could not paste ViaVersion dump to Hastebin", e2);
                    try {
                        if (con.getResponseCode() < 200 || con.getResponseCode() > 400) {
                            final String rawOutput = CharStreams.toString(new InputStreamReader(con.getErrorStream()));
                            con.getErrorStream().close();
                            Via.getPlatform().getLogger().log(Level.WARNING, "Page returned: " + rawOutput);
                        }
                    }
                    catch (IOException e3) {
                        Via.getPlatform().getLogger().log(Level.WARNING, "Failed to capture further info", e3);
                    }
                }
            }
        });
        return true;
    }
    
    private String getUrl(final String id) {
        return String.format("https://dump.viaversion.com/%s", id);
    }
}
