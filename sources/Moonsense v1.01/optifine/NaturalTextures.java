// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;

public class NaturalTextures
{
    private static NaturalProperties[] propertiesByIndex;
    
    static {
        NaturalTextures.propertiesByIndex = new NaturalProperties[0];
    }
    
    public static void update() {
        NaturalTextures.propertiesByIndex = new NaturalProperties[0];
        if (Config.isNaturalTextures()) {
            final String fileName = "optifine/natural.properties";
            try {
                final ResourceLocation e = new ResourceLocation(fileName);
                if (!Config.hasResource(e)) {
                    Config.dbg("NaturalTextures: configuration \"" + fileName + "\" not found");
                    return;
                }
                final boolean defaultConfig = Config.isFromDefaultResourcePack(e);
                final InputStream in = Config.getResourceStream(e);
                final ArrayList list = new ArrayList(256);
                final String configStr = Config.readInputStream(in);
                in.close();
                final String[] configLines = Config.tokenize(configStr, "\n\r");
                if (defaultConfig) {
                    Config.dbg("Natural Textures: Parsing default configuration \"" + fileName + "\"");
                    Config.dbg("Natural Textures: Valid only for textures from default resource pack");
                }
                else {
                    Config.dbg("Natural Textures: Parsing configuration \"" + fileName + "\"");
                }
                final TextureMap textureMapBlocks = TextureUtils.getTextureMapBlocks();
                for (int i = 0; i < configLines.length; ++i) {
                    final String line = configLines[i].trim();
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
                                    if (defaultConfig && !Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/" + key + ".png"))) {
                                        return;
                                    }
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
            catch (FileNotFoundException var19) {
                Config.warn("NaturalTextures: configuration \"" + fileName + "\" not found");
            }
            catch (Exception var18) {
                var18.printStackTrace();
            }
        }
    }
    
    public static BakedQuad getNaturalTexture(final BlockPos blockPosIn, final BakedQuad quad) {
        final TextureAtlasSprite sprite = quad.getSprite();
        if (sprite == null) {
            return quad;
        }
        final NaturalProperties nps = getNaturalProperties(sprite);
        if (nps == null) {
            return quad;
        }
        final int side = ConnectedTextures.getSide(quad.getFace());
        final int rand = Config.getRandom(blockPosIn, side);
        int rotate = 0;
        boolean flipU = false;
        if (nps.rotation > 1) {
            rotate = (rand & 0x3);
        }
        if (nps.rotation == 2) {
            rotate = rotate / 2 * 2;
        }
        if (nps.flip) {
            flipU = ((rand & 0x4) != 0x0);
        }
        return nps.getQuad(quad, rotate, flipU);
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
}
