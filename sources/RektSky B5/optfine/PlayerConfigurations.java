/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.util.StringUtils;
import optfine.FileDownloadThread;
import optfine.PlayerConfiguration;
import optfine.PlayerConfigurationReceiver;

public class PlayerConfigurations {
    private static Map mapConfigurations = null;

    public static void renderPlayerItems(ModelBiped p_renderPlayerItems_0_, AbstractClientPlayer p_renderPlayerItems_1_, float p_renderPlayerItems_2_, float p_renderPlayerItems_3_) {
        PlayerConfiguration playerconfiguration = PlayerConfigurations.getPlayerConfiguration(p_renderPlayerItems_1_);
        if (playerconfiguration != null) {
            playerconfiguration.renderPlayerItems(p_renderPlayerItems_0_, p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_);
        }
    }

    public static synchronized PlayerConfiguration getPlayerConfiguration(AbstractClientPlayer p_getPlayerConfiguration_0_) {
        String s2 = PlayerConfigurations.getPlayerName(p_getPlayerConfiguration_0_);
        PlayerConfiguration playerconfiguration = (PlayerConfiguration)PlayerConfigurations.getMapConfigurations().get(s2);
        if (playerconfiguration == null) {
            playerconfiguration = new PlayerConfiguration();
            PlayerConfigurations.getMapConfigurations().put(s2, playerconfiguration);
            PlayerConfigurationReceiver playerconfigurationreceiver = new PlayerConfigurationReceiver(s2);
            String s1 = "http://s.optifine.net/users/" + s2 + ".cfg";
            FileDownloadThread filedownloadthread = new FileDownloadThread(s1, playerconfigurationreceiver);
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

    public static String getPlayerName(AbstractClientPlayer p_getPlayerName_0_) {
        String s2 = p_getPlayerName_0_.getName();
        if (s2 != null && !s2.isEmpty()) {
            s2 = StringUtils.stripControlCodes(s2);
        }
        return s2;
    }
}

