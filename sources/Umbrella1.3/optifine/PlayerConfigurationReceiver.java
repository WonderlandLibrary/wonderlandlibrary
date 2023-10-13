/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParser
 */
package optifine;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import optifine.Config;
import optifine.IFileDownloadListener;
import optifine.PlayerConfiguration;
import optifine.PlayerConfigurationParser;
import optifine.PlayerConfigurations;

public class PlayerConfigurationReceiver
implements IFileDownloadListener {
    private String player = null;

    public PlayerConfigurationReceiver(String player) {
        this.player = player;
    }

    @Override
    public void fileDownloadFinished(String url, byte[] bytes, Throwable exception) {
        if (bytes != null) {
            try {
                String e = new String(bytes, "ASCII");
                JsonParser jp = new JsonParser();
                JsonElement je = jp.parse(e);
                PlayerConfigurationParser pcp = new PlayerConfigurationParser(this.player);
                PlayerConfiguration pc = pcp.parsePlayerConfiguration(je);
                if (pc != null) {
                    pc.setInitialized(true);
                    PlayerConfigurations.setPlayerConfiguration(this.player, pc);
                }
            }
            catch (Exception var9) {
                Config.dbg("Error parsing configuration: " + url + ", " + var9.getClass().getName() + ": " + var9.getMessage());
            }
        }
    }
}

