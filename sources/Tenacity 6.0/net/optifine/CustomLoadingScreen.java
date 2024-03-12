// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.src.Config;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;

public class CustomLoadingScreen
{
    private ResourceLocation locationTexture;
    private int scaleMode;
    private int scale;
    private boolean center;
    private static final int SCALE_DEFAULT = 2;
    private static final int SCALE_MODE_FIXED = 0;
    private static final int SCALE_MODE_FULL = 1;
    private static final int SCALE_MODE_STRETCH = 2;
    
    public CustomLoadingScreen(final ResourceLocation locationTexture, final int scaleMode, final int scale, final boolean center) {
        this.scaleMode = 0;
        this.scale = 2;
        this.locationTexture = locationTexture;
        this.scaleMode = scaleMode;
        this.scale = scale;
        this.center = center;
    }
    
    public static CustomLoadingScreen parseScreen(final String path, final int dimId, final Properties props) {
        final ResourceLocation resourcelocation = new ResourceLocation(path);
        final int i = parseScaleMode(getProperty("scaleMode", dimId, props));
        final int j = (i == 0) ? 2 : 1;
        final int k = parseScale(getProperty("scale", dimId, props), j);
        final boolean flag = Config.parseBoolean(getProperty("center", dimId, props), false);
        final CustomLoadingScreen customloadingscreen = new CustomLoadingScreen(resourcelocation, i, k, flag);
        return customloadingscreen;
    }
    
    private static String getProperty(final String key, final int dim, final Properties props) {
        if (props == null) {
            return null;
        }
        String s = props.getProperty("dim" + dim + "." + key);
        if (s != null) {
            return s;
        }
        s = props.getProperty(key);
        return s;
    }
    
    private static int parseScaleMode(String str) {
        if (str == null) {
            return 0;
        }
        str = str.toLowerCase().trim();
        if (str.equals("fixed")) {
            return 0;
        }
        if (str.equals("full")) {
            return 1;
        }
        if (str.equals("stretch")) {
            return 2;
        }
        CustomLoadingScreens.warn("Invalid scale mode: " + str);
        return 0;
    }
    
    private static int parseScale(String str, final int def) {
        if (str == null) {
            return def;
        }
        str = str.trim();
        final int i = Config.parseInt(str, -1);
        if (i < 1) {
            CustomLoadingScreens.warn("Invalid scale: " + str);
            return def;
        }
        return i;
    }
    
    public void drawBackground(final int width, final int height) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        Config.getTextureManager().bindTexture(this.locationTexture);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        double d0 = 16 * this.scale;
        double d2 = width / d0;
        double d3 = height / d0;
        double d4 = 0.0;
        double d5 = 0.0;
        if (this.center) {
            d4 = (d0 - width) / (d0 * 2.0);
            d5 = (d0 - height) / (d0 * 2.0);
        }
        switch (this.scaleMode) {
            case 1: {
                d0 = Math.max(width, height);
                d2 = this.scale * width / d0;
                d3 = this.scale * height / d0;
                if (this.center) {
                    d4 = this.scale * (d0 - width) / (d0 * 2.0);
                    d5 = this.scale * (d0 - height) / (d0 * 2.0);
                    break;
                }
                break;
            }
            case 2: {
                d2 = this.scale;
                d3 = this.scale;
                d4 = 0.0;
                d5 = 0.0;
                break;
            }
        }
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, height, 0.0).tex(d4, d5 + d3).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(width, height, 0.0).tex(d4 + d2, d5 + d3).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(width, 0.0, 0.0).tex(d4 + d2, d5).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(d4, d5).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
    }
}
