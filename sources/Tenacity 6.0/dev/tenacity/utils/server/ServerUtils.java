// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.server;

import net.minecraft.entity.EntityLivingBase;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.ServerData;
import dev.tenacity.utils.Utils;

public class ServerUtils implements Utils
{
    public static ServerData lastServer;
    private static boolean redirecting;
    
    public static boolean serverCheck(String ip) {
        if (ServerUtils.mc.getCurrentServerData() == null) {
            return false;
        }
        ip = ip.toLowerCase();
        final String server = ServerUtils.mc.isSingleplayer() ? "" : ServerUtils.mc.getCurrentServerData().serverIP.toLowerCase();
        return server.endsWith("." + ip) || server.equals(ip);
    }
    
    public static boolean isGeniuneHypixel() {
        return isOnHypixel() && !ServerUtils.redirecting;
    }
    
    public static boolean isOnHypixel() {
        if (ServerUtils.mc.isSingleplayer() || ServerUtils.mc.getCurrentServerData() == null || ServerUtils.mc.getCurrentServerData().serverIP == null) {
            return false;
        }
        final String ip = ServerUtils.mc.getCurrentServerData().serverIP.toLowerCase();
        if (!ip.contains("hypixel")) {
            return false;
        }
        if (ServerUtils.mc.thePlayer == null) {
            return true;
        }
        final String brand = ServerUtils.mc.thePlayer.getClientBrand();
        return brand != null && brand.startsWith("Hypixel BungeeCord");
    }
    
    public static boolean isInLobby() {
        if (ServerUtils.mc.theWorld == null) {
            return true;
        }
        final List<Entity> entities = ServerUtils.mc.theWorld.getLoadedEntityList();
        for (int i = 0; i < entities.size(); ++i) {
            final Entity entity = entities.get(i);
            if (entity != null && entity.getName().equals("§e§lCLICK TO PLAY")) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isHostsRedirectingHypixel() throws IOException {
        final Path value = Paths.get(System.getenv("SystemDrive") + "\\Windows\\System32\\drivers\\etc\\hosts", new String[0]);
        return !Files.notExists(value, new LinkOption[0]) && Files.lines(value).anyMatch(s -> s.toLowerCase().contains("hypixel"));
    }
    
    public static void updateRedirecting() throws IOException {
        ServerUtils.redirecting = isHostsRedirectingHypixel();
    }
    
    public static boolean isOnSameTeam(final EntityLivingBase ent) {
        if (ServerUtils.mc.thePlayer != null) {
            final String displayName = ServerUtils.mc.thePlayer.getDisplayName().getUnformattedText();
            if (displayName.contains("§")) {
                final int start = displayName.indexOf("§");
                final String substring = displayName.substring(start, start + 2);
                return ent != null && ent.getDisplayName().getFormattedText().contains(substring);
            }
        }
        return false;
    }
}
