// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.player;

import java.util.HashMap;
import net.optifine.http.IFileDownloadListener;
import net.optifine.http.FileDownloadThread;
import net.optifine.http.HttpUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import java.util.Map;

public class PlayerConfigurations
{
    private static Map mapConfigurations;
    private static boolean reloadPlayerItems;
    private static long timeReloadPlayerItemsMs;
    
    public static void renderPlayerItems(final ModelBiped modelBiped, final AbstractClientPlayer player, final float scale, final float partialTicks) {
        final PlayerConfiguration playerconfiguration = getPlayerConfiguration(player);
        if (playerconfiguration != null) {
            playerconfiguration.renderPlayerItems(modelBiped, player, scale, partialTicks);
        }
    }
    
    public static synchronized PlayerConfiguration getPlayerConfiguration(final AbstractClientPlayer player) {
        if (PlayerConfigurations.reloadPlayerItems && System.currentTimeMillis() > PlayerConfigurations.timeReloadPlayerItemsMs + 5000L) {
            final AbstractClientPlayer abstractclientplayer = Minecraft.getMinecraft().thePlayer;
            if (abstractclientplayer != null) {
                setPlayerConfiguration(abstractclientplayer.getNameClear(), null);
                PlayerConfigurations.timeReloadPlayerItemsMs = System.currentTimeMillis();
            }
        }
        final String s1 = player.getNameClear();
        if (s1 == null) {
            return null;
        }
        PlayerConfiguration playerconfiguration = getMapConfigurations().get(s1);
        if (playerconfiguration == null) {
            playerconfiguration = new PlayerConfiguration();
            getMapConfigurations().put(s1, playerconfiguration);
            final PlayerConfigurationReceiver playerconfigurationreceiver = new PlayerConfigurationReceiver(s1);
            final String s2 = HttpUtils.getPlayerItemsUrl() + "/users/" + s1 + ".cfg";
            final FileDownloadThread filedownloadthread = new FileDownloadThread(s2, playerconfigurationreceiver);
            filedownloadthread.start();
        }
        return playerconfiguration;
    }
    
    public static synchronized void setPlayerConfiguration(final String player, final PlayerConfiguration pc) {
        getMapConfigurations().put(player, pc);
    }
    
    private static Map getMapConfigurations() {
        if (PlayerConfigurations.mapConfigurations == null) {
            PlayerConfigurations.mapConfigurations = new HashMap();
        }
        return PlayerConfigurations.mapConfigurations;
    }
    
    static {
        PlayerConfigurations.mapConfigurations = null;
        PlayerConfigurations.reloadPlayerItems = Boolean.getBoolean("player.models.reload");
        PlayerConfigurations.timeReloadPlayerItemsMs = System.currentTimeMillis();
    }
}
