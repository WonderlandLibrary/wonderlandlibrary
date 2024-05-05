package net.optifine.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.src.Config;
import net.optifine.http.IFileDownloadListener;
import net.optifine.player.PlayerConfiguration;
import net.optifine.player.PlayerConfigurationParser;
import net.optifine.player.PlayerConfigurations;

public class PlayerConfigurationReceiver
implements IFileDownloadListener {
    private String player = null;

    public PlayerConfigurationReceiver(String player) {
        this.player = player;
    }

    public void fileDownloadFinished(String url, byte[] bytes, Throwable exception) {
        if (bytes != null) {
            try {
                String s = new String(bytes, "ASCII");
                JsonParser jsonparser = new JsonParser();
                JsonElement jsonelement = jsonparser.parse(s);
                PlayerConfigurationParser playerconfigurationparser = new PlayerConfigurationParser(this.player);
                PlayerConfiguration playerconfiguration = playerconfigurationparser.parsePlayerConfiguration(jsonelement);
                if (playerconfiguration != null) {
                    playerconfiguration.setInitialized(true);
                    PlayerConfigurations.setPlayerConfiguration((String)this.player, (PlayerConfiguration)playerconfiguration);
                }
            }
            catch (Exception exception1) {
                Config.dbg((String)("Error parsing configuration: " + url + ", " + exception1.getClass().getName() + ": " + exception1.getMessage()));
            }
        }
    }
}
