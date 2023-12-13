// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.util.List;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;

public class NaturalTextures
{
    private static NaturalProperties[] propertiesByIndex;
    
    public static void update() {
        NaturalTextures.propertiesByIndex = new NaturalProperties[0];
        if (Config.isNaturalTextures()) {
            final String fileName = "optifine/natural.properties";
            try {
                final ResourceLocation e = new ResourceLocation(fileName);
                if (!Config.hasResource(e)) {
                    Config.dbg("NaturalTextures: configuration \"" + fileName + "\" not found");
                    NaturalTextures.propertiesByIndex = makeDefaultProperties();
                    return;
                }
                final InputStream in = Config.getResourceStream(e);
                final ArrayList list = new ArrayList(256);
                final String configStr = Config.readInputStream(in);
                in.close();
                final String[] configLines = Config.tokenize(configStr, "\n\r");
                Config.dbg("Natural Textures: Parsing configuration \"" + fileName + "\"");
                final TextureMap textureMapBlocks = TextureUtils.getTextureMapBlocks();
                for (final String configLine : configLines) {
                    final String line = configLine.trim();
                    if (!line.startsWith("#")) {
                        final String[] strs = Config.tokenize(line, "=");
                        if (strs.length != 2) {
                            Config.warn("Natural Textures: Invalid \"" + fileName + "\" line: " + line);
                        }
                        else {
                            final String key = strs[0].trim();
                            final String type = strs[1].trim();
                            final TextureAtlasSprite ts = textureMapBlocks.getSpriteSafe("minecraft:blocks/" + key);
                            if (ts == null) {
                                Config.warn("Natural Textures: Texture not found: \"" + fileName + "\" line: " + line);
                            }
                            else {
                                final int tileNum = ts.getIndexInMap();
                                if (tileNum < 0) {
                                    Config.warn("Natural Textures: Invalid \"" + fileName + "\" line: " + line);
                                }
                                else {
                                    final NaturalProperties props = new NaturalProperties(type);
                                    if (props.isValid()) {
                                        while (list.size() <= tileNum) {
                                            list.add(null);
                                        }
                                        list.set(tileNum, props);
                                        Config.dbg("NaturalTextures: " + key + " = " + type);
                                    }
                                }
                            }
                        }
                    }
                }
                NaturalTextures.propertiesByIndex = list.toArray(new NaturalProperties[list.size()]);
            }
            catch (FileNotFoundException var18) {
                Config.warn("NaturalTextures: configuration \"" + fileName + "\" not found");
                NaturalTextures.propertiesByIndex = makeDefaultProperties();
            }
            catch (Exception var17) {
                var17.printStackTrace();
            }
        }
    }
    
    public static NaturalProperties getNaturalProperties(final TextureAtlasSprite icon) {
        if (!(icon instanceof TextureAtlasSprite)) {
            return null;
        }
        final int tileNum = icon.getIndexInMap();
        if (tileNum >= 0 && tileNum < NaturalTextures.propertiesByIndex.length) {
            final NaturalProperties props = NaturalTextures.propertiesByIndex[tileNum];
            return props;
        }
        return null;
    }
    
    private static NaturalProperties[] makeDefaultProperties() {
        Config.dbg("NaturalTextures: Creating default configuration.");
        final ArrayList propsList = new ArrayList();
        setIconProperties(propsList, "grass_top", "4F");
        setIconProperties(propsList, "stone", "2F");
        setIconProperties(propsList, "dirt", "4F");
        setIconProperties(propsList, "grass_side", "F");
        setIconProperties(propsList, "grass_side_overlay", "F");
        setIconProperties(propsList, "stone_slab_top", "F");
        setIconProperties(propsList, "bedrock", "2F");
        setIconProperties(propsList, "sand", "4F");
        setIconProperties(propsList, "gravel", "2");
        setIconProperties(propsList, "log_oak", "2F");
        setIconProperties(propsList, "log_oak_top", "4F");
        setIconProperties(propsList, "gold_ore", "2F");
        setIconProperties(propsList, "iron_ore", "2F");
        setIconProperties(propsList, "coal_ore", "2F");
        setIconProperties(propsList, "diamond_ore", "2F");
        setIconProperties(propsList, "redstone_ore", "2F");
        setIconProperties(propsList, "lapis_ore", "2F");
        setIconProperties(propsList, "obsidian", "4F");
        setIconProperties(propsList, "leaves_oak", "2F");
        setIconProperties(propsList, "leaves_jungle", "2");
        setIconProperties(propsList, "snow", "4F");
        setIconProperties(propsList, "grass_side_snowed", "F");
        setIconProperties(propsList, "cactus_side", "2F");
        setIconProperties(propsList, "clay", "4F");
        setIconProperties(propsList, "mycelium_side", "F");
        setIconProperties(propsList, "mycelium_top", "4F");
        setIconProperties(propsList, "farmland_wet", "2F");
        setIconProperties(propsList, "farmland_dry", "2F");
        setIconProperties(propsList, "netherrack", "4F");
        setIconProperties(propsList, "soul_sand", "4F");
        setIconProperties(propsList, "glowstone", "4");
        setIconProperties(propsList, "log_spruce", "2F");
        setIconProperties(propsList, "log_birch", "F");
        setIconProperties(propsList, "leaves_spruce", "2F");
        setIconProperties(propsList, "log_jungle", "2F");
        setIconProperties(propsList, "end_stone", "4");
        setIconProperties(propsList, "sandstone_top", "4");
        setIconProperties(propsList, "sandstone_bottom", "4F");
        setIconProperties(propsList, "redstone_lamp_on", "4F");
        final NaturalProperties[] terrainProps = propsList.toArray(new NaturalProperties[propsList.size()]);
        return terrainProps;
    }
    
    private static void setIconProperties(final List propsList, final String iconName, final String propStr) {
        final TextureMap terrainMap = TextureUtils.getTextureMapBlocks();
        final TextureAtlasSprite icon = terrainMap.getSpriteSafe("minecraft:blocks/" + iconName);
        if (icon == null) {
            Config.warn("*** NaturalProperties: Icon not found: " + iconName + " ***");
        }
        else if (!(icon instanceof TextureAtlasSprite)) {
            Config.warn("*** NaturalProperties: Icon is not IconStitched: " + iconName + ": " + icon.getClass().getName() + " ***");
        }
        else {
            final int index = icon.getIndexInMap();
            if (index < 0) {
                Config.warn("*** NaturalProperties: Invalid index for icon: " + iconName + ": " + index + " ***");
            }
            else if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/" + iconName + ".png"))) {
                while (index >= propsList.size()) {
                    propsList.add(null);
                }
                final NaturalProperties props = new NaturalProperties(propStr);
                propsList.set(index, props);
                Config.dbg("NaturalTextures: " + iconName + " = " + propStr);
            }
        }
    }
    
    static {
        NaturalTextures.propertiesByIndex = new NaturalProperties[0];
    }
}
