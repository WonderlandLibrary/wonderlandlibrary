/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import optifine.Blender;
import optifine.Config;
import optifine.CustomSkyLayer;
import optifine.TextureUtils;

public class CustomSky {
    private static CustomSkyLayer[][] worldSkyLayers = null;

    public static void reset() {
        worldSkyLayers = null;
    }

    public static void update() {
        CustomSky.reset();
        if (Config.isCustomSky()) {
            worldSkyLayers = CustomSky.readCustomSkies();
        }
    }

    private static CustomSkyLayer[][] readCustomSkies() {
        int worldCount;
        CustomSkyLayer[][] wsls = new CustomSkyLayer[10][0];
        String prefix = "mcpatcher/sky/world";
        int lastWorldId = -1;
        for (worldCount = 0; worldCount < wsls.length; ++worldCount) {
            String wslsTrim = String.valueOf(prefix) + worldCount + "/sky";
            ArrayList<CustomSkyLayer> i = new ArrayList<CustomSkyLayer>();
            for (int sls = 1; sls < 1000; ++sls) {
                String path = String.valueOf(wslsTrim) + sls + ".properties";
                try {
                    ResourceLocation e = new ResourceLocation(path);
                    InputStream in = Config.getResourceStream(e);
                    if (in == null) break;
                    Properties props = new Properties();
                    props.load(in);
                    in.close();
                    Config.dbg("CustomSky properties: " + path);
                    String defSource = String.valueOf(wslsTrim) + sls + ".png";
                    CustomSkyLayer sl = new CustomSkyLayer(props, defSource);
                    if (!sl.isValid(path)) continue;
                    ResourceLocation locSource = new ResourceLocation(sl.source);
                    ITextureObject tex = TextureUtils.getTexture(locSource);
                    if (tex == null) {
                        Config.log("CustomSky: Texture not found: " + locSource);
                        continue;
                    }
                    sl.textureId = tex.getGlTextureId();
                    i.add(sl);
                    in.close();
                    continue;
                }
                catch (FileNotFoundException var15) {
                    break;
                }
                catch (IOException var16) {
                    var16.printStackTrace();
                }
            }
            if (i.size() <= 0) continue;
            CustomSkyLayer[] var19 = i.toArray(new CustomSkyLayer[i.size()]);
            wsls[worldCount] = var19;
            lastWorldId = worldCount;
        }
        if (lastWorldId < 0) {
            return null;
        }
        worldCount = lastWorldId + 1;
        CustomSkyLayer[][] var17 = new CustomSkyLayer[worldCount][0];
        for (int var18 = 0; var18 < var17.length; ++var18) {
            var17[var18] = wsls[var18];
        }
        return var17;
    }

    public static void renderSky(World world, TextureManager re, float celestialAngle, float rainBrightness) {
        CustomSkyLayer[] sls;
        int dimId;
        if (worldSkyLayers != null && Config.getGameSettings().renderDistanceChunks >= 8 && (dimId = world.provider.getDimensionId()) >= 0 && dimId < worldSkyLayers.length && (sls = worldSkyLayers[dimId]) != null) {
            long time = world.getWorldTime();
            int timeOfDay = (int)(time % 24000L);
            for (int i = 0; i < sls.length; ++i) {
                CustomSkyLayer sl = sls[i];
                if (!sl.isActive(world, timeOfDay)) continue;
                sl.render(timeOfDay, celestialAngle, rainBrightness);
            }
            Blender.clearBlend(rainBrightness);
        }
    }

    public static boolean hasSkyLayers(World world) {
        if (worldSkyLayers == null) {
            return false;
        }
        if (Config.getGameSettings().renderDistanceChunks < 8) {
            return false;
        }
        int dimId = world.provider.getDimensionId();
        if (dimId >= 0 && dimId < worldSkyLayers.length) {
            CustomSkyLayer[] sls = worldSkyLayers[dimId];
            return sls == null ? false : sls.length > 0;
        }
        return false;
    }
}

