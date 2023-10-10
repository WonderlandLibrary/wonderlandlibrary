/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import tk.rektsky.Client;
import tk.rektsky.utils.LaunchWrapper;

public class DiscordManager {
    private static final long created = System.currentTimeMillis();
    public static boolean running = true;
    public static DiscordUser discordUser = null;

    public static void startUp() {
        try {
            running = true;
            DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(discordUser -> {
                DiscordManager.discordUser = discordUser;
                DiscordManager.updateRPC("RektSky B5  |  Private", "IGN:" + Minecraft.getMinecraft().session.getUsername());
            }).build();
            DiscordRPC.discordInitialize("797038611098370108", handlers, true);
            new Thread("DiscordRPC"){

                @Override
                public void run() {
                    while (running) {
                        try {
                            DiscordRPC.discordRunCallbacks();
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            LaunchWrapper.logger.info("DiscordRPC failed to start!");
                        }
                    }
                }
            }.start();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            LaunchWrapper.logger.info("DiscordRPC failed to start!");
        }
    }

    public static DynamicTexture getAvatar() {
        try {
            URL url = new URL(Client.user.discordAvatar);
            File avatarFile = new File("RektSky/avatars/");
            LaunchWrapper.logger.info(url.toString());
            avatarFile.mkdirs();
            avatarFile = new File("RektSky/avatars/" + Client.user.discordId + ".png");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.101 Safari/537.36");
            connection.connect();
            Files.copy(connection.getInputStream(), Paths.get("RektSky/avatars/" + Client.user.discordId + ".png", new String[0]), StandardCopyOption.REPLACE_EXISTING);
            BufferedImage image = ImageIO.read(new FileInputStream("RektSky/avatars/" + Client.user.discordId + ".png"));
            DynamicTexture dynamicTexture = new DynamicTexture(image);
            dynamicTexture.updateDynamicTexture();
            return dynamicTexture;
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static void updateRPC(String firstLine, String secondLine) {
        DiscordRichPresence rpc = new DiscordRichPresence.Builder(secondLine).setBigImage("icon", "Rekt Client").setSmallImage("member", "Member").setDetails(firstLine).setStartTimestamps(created).build();
        DiscordRPC.discordUpdatePresence(rpc);
    }

    public static void shutdownRPC() {
        running = false;
        DiscordRPC.discordShutdown();
    }
}

