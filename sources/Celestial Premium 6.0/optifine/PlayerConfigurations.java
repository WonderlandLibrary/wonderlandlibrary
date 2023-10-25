/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import optifine.FileDownloadThread;
import optifine.HttpUtils;
import optifine.PlayerConfiguration;
import optifine.PlayerConfigurationReceiver;

public class PlayerConfigurations {
    private static Map mapConfigurations = null;
    private static boolean reloadPlayerItems = Boolean.getBoolean("player.models.reload");
    private static long timeReloadPlayerItemsMs = System.currentTimeMillis();

    public static void renderPlayerItems(ModelBiped p_renderPlayerItems_0_, AbstractClientPlayer p_renderPlayerItems_1_, float p_renderPlayerItems_2_, float p_renderPlayerItems_3_) {
        PlayerConfiguration playerconfiguration = PlayerConfigurations.getPlayerConfiguration(p_renderPlayerItems_1_);
        if (playerconfiguration != null) {
            playerconfiguration.renderPlayerItems(p_renderPlayerItems_0_, p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_);
        }
    }

    public static synchronized PlayerConfiguration getPlayerConfiguration(AbstractClientPlayer p_getPlayerConfiguration_0_) {
        String s1;
        EntityPlayerSP abstractclientplayer;
        if (reloadPlayerItems && System.currentTimeMillis() > timeReloadPlayerItemsMs + 5000L && (abstractclientplayer = Minecraft.getMinecraft().player) != null) {
            PlayerConfigurations.setPlayerConfiguration(abstractclientplayer.getNameClear(), null);
            timeReloadPlayerItemsMs = System.currentTimeMillis();
        }
        if ((s1 = p_getPlayerConfiguration_0_.getNameClear()) == null) {
            return null;
        }
        PlayerConfiguration playerconfiguration = (PlayerConfiguration)PlayerConfigurations.getMapConfigurations().get(s1);
        if (playerconfiguration == null) {
            playerconfiguration = new PlayerConfiguration();
            PlayerConfigurations.getMapConfigurations().put(s1, playerconfiguration);
            PlayerConfigurationReceiver playerconfigurationreceiver = new PlayerConfigurationReceiver(s1);
            String s = HttpUtils.getPlayerItemsUrl() + "/users/" + s1 + ".cfg";
            FileDownloadThread filedownloadthread = new FileDownloadThread(s, playerconfigurationreceiver);
            filedownloadthread.start();
        }
        return playerconfiguration;
    }

    public static synchronized void setPlayerConfiguration(String p_setPlayerConfiguration_0_, PlayerConfiguration p_setPlayerConfiguration_1_) {
        PlayerConfigurations.getMapConfigurations().put(p_setPlayerConfiguration_0_, p_setPlayerConfiguration_1_);
    }

    private static Map getMapConfigurations() {
        if (mapConfigurations == null) {
            mapConfigurations = new HashMap();
        }
        return mapConfigurations;
    }
}

