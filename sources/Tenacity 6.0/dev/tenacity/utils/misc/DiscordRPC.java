// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.misc;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.Minecraft;
import java.time.Instant;
import dev.tenacity.Tenacity;
import de.jcm.discordgamesdk.CreateParams;
import java.util.zip.ZipEntry;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.CopyOption;
import java.io.IOException;
import java.util.zip.ZipInputStream;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Locale;
import java.io.File;
import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.activity.Activity;

public class DiscordRPC
{
    public boolean running;
    public boolean canLoad;
    private Activity activity;
    private Core core;
    
    public static File downloadNativeLibrary() throws IOException {
        final String name = "discord_game_sdk";
        final String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
        String suffix;
        if (osName.contains("windows")) {
            suffix = ".dll";
        }
        else if (osName.contains("linux")) {
            suffix = ".so";
        }
        else {
            if (!osName.contains("mac os")) {
                throw new RuntimeException("cannot determine OS type: " + osName);
            }
            suffix = ".dylib";
        }
        if (arch.equals("amd64")) {
            arch = "x86_64";
        }
        final String zipPath = "lib/" + arch + "/" + name + suffix;
        final URL downloadUrl = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");
        final HttpsURLConnection connection = (HttpsURLConnection)downloadUrl.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
        final ZipInputStream zin = new ZipInputStream(connection.getInputStream());
        ZipEntry entry;
        while ((entry = zin.getNextEntry()) != null) {
            if (entry.getName().equals(zipPath)) {
                final File tempDir = new File(System.getProperty("java.io.tmpdir"), "java-" + name + System.nanoTime());
                if (!tempDir.mkdir()) {
                    throw new IOException("Cannot create temporary directory");
                }
                tempDir.deleteOnExit();
                final File temp = new File(tempDir, name + suffix);
                temp.deleteOnExit();
                Files.copy(zin, temp.toPath(), new CopyOption[0]);
                zin.close();
                return temp;
            }
            else {
                zin.closeEntry();
            }
        }
        zin.close();
        return null;
    }
    
    public void start() {
        if (!this.canLoad || this.running) {
            return;
        }
        this.running = true;
        try {
            final CreateParams params = new CreateParams();
            params.setClientID(979902812840419328L);
            params.setFlags(new CreateParams.Flags[] { CreateParams.Flags.NO_REQUIRE_DISCORD });
            this.core = new Core(params);
            (this.activity = new Activity()).setDetails("Release: " + Tenacity.RELEASE.getName());
            this.activity.timestamps().setStart(Instant.now());
            this.activity.assets().setLargeImage("mc");
            this.activity.assets().setLargeText("Tenacity 6.0 @ intent.store");
            this.core.activityManager().updateActivity(this.activity);
            new Thread("Discord RPC") {
                @Override
                public void run() {
                    while (DiscordRPC.this.running) {
                        try {
                            final ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
                            if (serverData != null) {
                                DiscordRPC.this.activity.setState("Playing on " + serverData.serverIP);
                            }
                            else if (Minecraft.getMinecraft().isSingleplayer()) {
                                DiscordRPC.this.activity.setState("In singleplayer");
                            }
                            else {
                                DiscordRPC.this.activity.setState("Currently idle");
                            }
                            DiscordRPC.this.core.activityManager().updateActivity(DiscordRPC.this.activity);
                            DiscordRPC.this.core.runCallbacks();
                            Thread.sleep(20L);
                            continue;
                        }
                        catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e2) {
                            e2.printStackTrace();
                            continue;
                        }
                        break;
                    }
                }
            }.start();
        }
        catch (Exception e) {
            e.printStackTrace();
            this.running = false;
            this.canLoad = false;
        }
    }
}
