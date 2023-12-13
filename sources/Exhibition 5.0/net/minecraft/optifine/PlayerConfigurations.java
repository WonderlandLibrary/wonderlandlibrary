// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.util.StringUtils;
import java.util.HashMap;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import java.util.Map;

public class PlayerConfigurations
{
    private static Map mapConfigurations;
    
    public static void renderPlayerItems(final ModelBiped modelBiped, final AbstractClientPlayer player, final float scale, final float partialTicks) {
        final PlayerConfiguration cfg = getPlayerConfiguration(player);
        if (cfg != null) {
            cfg.renderPlayerItems(modelBiped, player, scale, partialTicks);
        }
    }
    
    public static synchronized PlayerConfiguration getPlayerConfiguration(final AbstractClientPlayer player) {
        final String name = getPlayerName(player);
        PlayerConfiguration pc = getMapConfigurations().get(name);
        if (pc == null) {
            pc = new PlayerConfiguration();
            getMapConfigurations().put(name, pc);
            final PlayerConfigurationReceiver pcl = new PlayerConfigurationReceiver(name);
            final String url = "http://s.optifine.net/users/" + name + ".cfg";
            final FileDownloadThread fdt = new FileDownloadThread(url, pcl);
            fdt.start();
        }
        return pc;
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
    
    public static String getPlayerName(final AbstractClientPlayer player) {
        String username = player.getName();
        if (username != null && !username.isEmpty()) {
            username = StringUtils.stripControlCodes(username);
        }
        return username;
    }
    
    static {
        PlayerConfigurations.mapConfigurations = null;
    }
}
