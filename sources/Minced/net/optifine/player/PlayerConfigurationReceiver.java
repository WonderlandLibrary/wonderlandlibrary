// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.player;

import com.google.gson.JsonElement;
import net.minecraft.src.Config;
import com.google.gson.JsonParser;
import net.optifine.http.IFileDownloadListener;

public class PlayerConfigurationReceiver implements IFileDownloadListener
{
    private String player;
    
    public PlayerConfigurationReceiver(final String player) {
        this.player = null;
        this.player = player;
    }
    
    @Override
    public void fileDownloadFinished(final String url, final byte[] bytes, final Throwable exception) {
        if (bytes != null) {
            try {
                final String s = new String(bytes, "ASCII");
                final JsonParser jsonparser = new JsonParser();
                final JsonElement jsonelement = jsonparser.parse(s);
                final PlayerConfigurationParser playerconfigurationparser = new PlayerConfigurationParser(this.player);
                final PlayerConfiguration playerconfiguration = playerconfigurationparser.parsePlayerConfiguration(jsonelement);
                if (playerconfiguration != null) {
                    playerconfiguration.setInitialized(true);
                    PlayerConfigurations.setPlayerConfiguration(this.player, playerconfiguration);
                }
            }
            catch (Exception exception2) {
                Config.dbg("Error parsing configuration: " + url + ", " + exception2.getClass().getName() + ": " + exception2.getMessage());
            }
        }
    }
}
