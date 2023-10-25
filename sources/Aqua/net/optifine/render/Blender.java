package net.optifine.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.src.Config;

public class Blender {
    public static final int BLEND_ALPHA = 0;
    public static final int BLEND_ADD = 1;
    public static final int BLEND_SUBSTRACT = 2;
    public static final int BLEND_MULTIPLY = 3;
    public static final int BLEND_DODGE = 4;
    public static final int BLEND_BURN = 5;
    public static final int BLEND_SCREEN = 6;
    public static final int BLEND_OVERLAY = 7;
    public static final int BLEND_REPLACE = 8;
    public static final int BLEND_DEFAULT = 1;

    public static int parseBlend(String str) {
        if (str == null) {
            return 1;
        }
        if ((str = str.toLowerCase().trim()).equals((Object)"alpha")) {
            return 0;
        }
        if (str.equals((Object)"add")) {
            return 1;
        }
        if (str.equals((Object)"subtract")) {
            return 2;
        }
        if (str.equals((Object)"multiply")) {
            return 3;
        }
        if (str.equals((Object)"dodge")) {
            return 4;
        }
        if (str.equals((Object)"burn")) {
            return 5;
        }
        if (str.equals((Object)"screen")) {
            return 6;
        }
        if (str.equals((Object)"overlay")) {
            return 7;
        }
        if (str.equals((Object)"replace")) {
            return 8;
        }
        Config.warn((String)("Unknown blend: " + str));
        return 1;
    }

    public static void setupBlend(int blend, float brightness) {
        switch (blend) {
            case 0: {
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((int)770, (int)771);
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)brightness);
                break;
            }
            case 1: {
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((int)770, (int)1);
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)brightness);
                break;
            }
            case 2: {
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((int)775, (int)0);
                GlStateManager.color((float)brightness, (float)brightness, (float)brightness, (float)1.0f);
                break;
            }
            case 3: {
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((int)774, (int)771);
                GlStateManager.color((float)brightness, (float)brightness, (float)brightness, (float)brightness);
                break;
            }
            case 4: {
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((int)1, (int)1);
                GlStateManager.color((float)brightness, (float)brightness, (float)brightness, (float)1.0f);
                break;
            }
            case 5: {
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((int)0, (int)769);
                GlStateManager.color((float)brightness, (float)brightness, (float)brightness, (float)1.0f);
                break;
            }
            case 6: {
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((int)1, (int)769);
                GlStateManager.color((float)brightness, (float)brightness, (float)brightness, (float)1.0f);
                break;
            }
            case 7: {
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc((int)774, (int)768);
                GlStateManager.color((float)brightness, (float)brightness, (float)brightness, (float)1.0f);
                break;
            }
            case 8: {
                GlStateManager.enableAlpha();
                GlStateManager.disableBlend();
                GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)brightness);
            }
        }
        GlStateManager.enableTexture2D();
    }

    public static void clearBlend(float rainBrightness) {
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc((int)770, (int)1);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)rainBrightness);
    }
}
