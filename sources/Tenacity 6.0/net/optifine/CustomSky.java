// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.optifine.render.Blender;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.util.Properties;
import java.io.InputStream;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;
import net.optifine.util.TextureUtils;
import net.optifine.util.PropertiesOrdered;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import net.minecraft.src.Config;

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
        final CustomSkyLayer[][] acustomskylayer = new CustomSkyLayer[10][0];
        final String s = "mcpatcher/sky/world";
        int i = -1;
        for (int j = 0; j < acustomskylayer.length; ++j) {
            final String s2 = s + j + "/sky";
            final List list = new ArrayList();
            for (int k = 1; k < 1000; ++k) {
                final String s3 = s2 + k + ".properties";
                try {
                    final ResourceLocation resourcelocation = new ResourceLocation(s3);
                    final InputStream inputstream = Config.getResourceStream(resourcelocation);
                    if (inputstream == null) {
                        break;
                    }
                    final Properties properties = new PropertiesOrdered();
                    properties.load(inputstream);
                    inputstream.close();
                    Config.dbg("CustomSky properties: " + s3);
                    final String s4 = s2 + k + ".png";
                    final CustomSkyLayer customskylayer = new CustomSkyLayer(properties, s4);
                    if (customskylayer.isValid(s3)) {
                        final ResourceLocation resourcelocation2 = new ResourceLocation(customskylayer.source);
                        final ITextureObject itextureobject = TextureUtils.getTexture(resourcelocation2);
                        if (itextureobject == null) {
                            Config.log("CustomSky: Texture not found: " + resourcelocation2);
                        }
                        else {
                            customskylayer.textureId = itextureobject.getGlTextureId();
                            list.add(customskylayer);
                            inputstream.close();
                        }
                    }
                }
                catch (FileNotFoundException var15) {
                    break;
                }
                catch (IOException ioexception) {
                    ioexception.printStackTrace();
                }
            }
            if (list.size() > 0) {
                final CustomSkyLayer[] acustomskylayer2 = list.toArray(new CustomSkyLayer[list.size()]);
                acustomskylayer[j] = acustomskylayer2;
                i = j;
            }
        }
        if (i < 0) {
            return null;
        }
        final int l = i + 1;
        final CustomSkyLayer[][] acustomskylayer3 = new CustomSkyLayer[l][0];
        for (int i2 = 0; i2 < acustomskylayer3.length; ++i2) {
            acustomskylayer3[i2] = acustomskylayer[i2];
        }
        return acustomskylayer3;
    }
    
    public static void renderSky(final World world, final TextureManager re, final float partialTicks) {
        if (CustomSky.worldSkyLayers != null) {
            final int i = world.provider.getDimensionId();
            if (i >= 0 && i < CustomSky.worldSkyLayers.length) {
                final CustomSkyLayer[] acustomskylayer = CustomSky.worldSkyLayers[i];
                if (acustomskylayer != null) {
                    final long j = world.getWorldTime();
                    final int k = (int)(j % 24000L);
                    final float f = world.getCelestialAngle(partialTicks);
                    final float f2 = world.getRainStrength(partialTicks);
                    float f3 = world.getThunderStrength(partialTicks);
                    if (f2 > 0.0f) {
                        f3 /= f2;
                    }
                    for (int l = 0; l < acustomskylayer.length; ++l) {
                        final CustomSkyLayer customskylayer = acustomskylayer[l];
                        if (customskylayer.isActive(world, k)) {
                            customskylayer.render(world, k, f, f2, f3);
                        }
                    }
                    final float f4 = 1.0f - f2;
                    Blender.clearBlend(f4);
                }
            }
        }
    }
    
    public static boolean hasSkyLayers(final World world) {
        if (CustomSky.worldSkyLayers == null) {
            return false;
        }
        final int i = world.provider.getDimensionId();
        if (i >= 0 && i < CustomSky.worldSkyLayers.length) {
            final CustomSkyLayer[] acustomskylayer = CustomSky.worldSkyLayers[i];
            return acustomskylayer != null && acustomskylayer.length > 0;
        }
        return false;
    }
    
    static {
        CustomSky.worldSkyLayers = null;
    }
}
