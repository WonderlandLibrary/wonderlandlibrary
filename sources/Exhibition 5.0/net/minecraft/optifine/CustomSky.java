// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;

public class CustomSky
{
    private static CustomSkyLayer[][] worldSkyLayers;
    
    public static void reset() {
        CustomSky.worldSkyLayers = null;
    }
    
    public static void update() {
        reset();
        if (Config.isCustomSky()) {
            CustomSky.worldSkyLayers = readCustomSkies();
        }
    }
    
    private static CustomSkyLayer[][] readCustomSkies() {
        final CustomSkyLayer[][] wsls = new CustomSkyLayer[10][0];
        final String prefix = "mcpatcher/sky/world";
        int lastWorldId = -1;
        for (int worldCount = 0; worldCount < wsls.length; ++worldCount) {
            final String wslsTrim = prefix + worldCount + "/sky";
            final ArrayList i = new ArrayList();
            for (int sls = 1; sls < 1000; ++sls) {
                final String path = wslsTrim + sls + ".properties";
                try {
                    final ResourceLocation e = new ResourceLocation(path);
                    final InputStream in = Config.getResourceStream(e);
                    if (in == null) {
                        break;
                    }
                    final Properties props = new Properties();
                    props.load(in);
                    Config.dbg("CustomSky properties: " + path);
                    final String defSource = wslsTrim + sls + ".png";
                    final CustomSkyLayer sl = new CustomSkyLayer(props, defSource);
                    if (sl.isValid(path)) {
                        final ResourceLocation locSource = new ResourceLocation(sl.source);
                        final ITextureObject tex = TextureUtils.getTexture(locSource);
                        if (tex == null) {
                            Config.log("CustomSky: Texture not found: " + locSource);
                        }
                        else {
                            sl.textureId = tex.getGlTextureId();
                            i.add(sl);
                            in.close();
                        }
                    }
                }
                catch (FileNotFoundException var20) {
                    break;
                }
                catch (IOException var16) {
                    var16.printStackTrace();
                }
            }
            if (i.size() > 0) {
                final CustomSkyLayer[] var17 = i.toArray(new CustomSkyLayer[i.size()]);
                wsls[worldCount] = var17;
                lastWorldId = worldCount;
            }
        }
        if (lastWorldId < 0) {
            return null;
        }
        int worldCount = lastWorldId + 1;
        final CustomSkyLayer[][] var18 = new CustomSkyLayer[worldCount][0];
        for (int var19 = 0; var19 < var18.length; ++var19) {
            var18[var19] = wsls[var19];
        }
        return var18;
    }
    
    public static void renderSky(final World world, final TextureManager re, final float celestialAngle, final float rainBrightness) {
        if (CustomSky.worldSkyLayers != null && Config.getGameSettings().renderDistanceChunks >= 8) {
            final int dimId = world.provider.getDimensionId();
            if (dimId >= 0 && dimId < CustomSky.worldSkyLayers.length) {
                final CustomSkyLayer[] sls = CustomSky.worldSkyLayers[dimId];
                if (sls != null) {
                    final long time = world.getWorldTime();
                    final int timeOfDay = (int)(time % 24000L);
                    for (final CustomSkyLayer sl : sls) {
                        if (sl.isActive(timeOfDay)) {
                            sl.render(timeOfDay, celestialAngle, rainBrightness);
                        }
                    }
                    Blender.clearBlend(rainBrightness);
                }
            }
        }
    }
    
    public static boolean hasSkyLayers(final World world) {
        if (CustomSky.worldSkyLayers == null) {
            return false;
        }
        if (Config.getGameSettings().renderDistanceChunks < 8) {
            return false;
        }
        final int dimId = world.provider.getDimensionId();
        if (dimId >= 0 && dimId < CustomSky.worldSkyLayers.length) {
            final CustomSkyLayer[] sls = CustomSky.worldSkyLayers[dimId];
            return sls != null && sls.length > 0;
        }
        return false;
    }
    
    static {
        CustomSky.worldSkyLayers = null;
    }
}
