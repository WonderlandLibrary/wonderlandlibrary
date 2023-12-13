/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import net.minecraft.util.ResourceLocation;
import optfine.Config;
import optfine.HttpUtils;
import optfine.Json;
import optfine.PlayerConfiguration;
import optfine.PlayerItemModel;
import optfine.PlayerItemParser;

public class PlayerConfigurationParser {
    private String player = null;
    public static final String CONFIG_ITEMS = "items";
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_ACTIVE = "active";

    public PlayerConfigurationParser(String p_i47_1_) {
        this.player = p_i47_1_;
    }

    public PlayerConfiguration parsePlayerConfiguration(JsonElement p_parsePlayerConfiguration_1_) {
        if (p_parsePlayerConfiguration_1_ == null) {
            throw new JsonParseException("JSON object is null, player: " + this.player);
        }
        JsonObject jsonobject = (JsonObject)p_parsePlayerConfiguration_1_;
        PlayerConfiguration playerconfiguration = new PlayerConfiguration();
        JsonArray jsonarray = (JsonArray)jsonobject.get(CONFIG_ITEMS);
        if (jsonarray != null) {
            for (int i2 = 0; i2 < jsonarray.size(); ++i2) {
                PlayerItemModel playeritemmodel;
                JsonObject jsonobject1 = (JsonObject)jsonarray.get(i2);
                boolean flag = Json.getBoolean(jsonobject1, ITEM_ACTIVE, true);
                if (!flag) continue;
                String s2 = Json.getString(jsonobject1, ITEM_TYPE);
                if (s2 == null) {
                    Config.warn("Item type is null, player: " + this.player);
                    continue;
                }
                String s1 = Json.getString(jsonobject1, "model");
                if (s1 == null) {
                    s1 = "items/" + s2 + "/model.cfg";
                }
                if ((playeritemmodel = this.downloadModel(s1)) == null) continue;
                if (!playeritemmodel.isUsePlayerTexture()) {
                    BufferedImage bufferedimage;
                    String s22 = Json.getString(jsonobject1, "texture");
                    if (s22 == null) {
                        s22 = "items/" + s2 + "/users/" + this.player + ".png";
                    }
                    if ((bufferedimage = this.downloadTextureImage(s22)) == null) continue;
                    playeritemmodel.setTextureImage(bufferedimage);
                    ResourceLocation resourcelocation = new ResourceLocation("optifine.net", s22);
                    playeritemmodel.setTextureLocation(resourcelocation);
                }
                playerconfiguration.addPlayerItemModel(playeritemmodel);
            }
        }
        return playerconfiguration;
    }

    private BufferedImage downloadTextureImage(String p_downloadTextureImage_1_) {
        String s2 = "http://s.optifine.net/" + p_downloadTextureImage_1_;
        try {
            BufferedImage bufferedimage = ImageIO.read(new URL(s2));
            return bufferedimage;
        }
        catch (IOException ioexception) {
            Config.warn("Error loading item texture " + p_downloadTextureImage_1_ + ": " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
            return null;
        }
    }

    private PlayerItemModel downloadModel(String p_downloadModel_1_) {
        String s2 = "http://s.optifine.net/" + p_downloadModel_1_;
        try {
            byte[] abyte = HttpUtils.get(s2);
            String s1 = new String(abyte, "ASCII");
            JsonParser jsonparser = new JsonParser();
            JsonObject jsonobject = (JsonObject)jsonparser.parse(s1);
            PlayerItemParser playeritemparser = new PlayerItemParser();
            PlayerItemModel playeritemmodel = PlayerItemParser.parseItemModel(jsonobject);
            return playeritemmodel;
        }
        catch (Exception exception) {
            Config.warn("Error loading item model " + p_downloadModel_1_ + ": " + exception.getClass().getName() + ": " + exception.getMessage());
            return null;
        }
    }
}

