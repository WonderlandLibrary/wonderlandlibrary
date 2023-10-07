/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.report.liquidware.utils.fonts;

import java.awt.Color;
import java.awt.Font;
import me.kiras.aimwhere.libraries.slick.SlickException;
import me.kiras.aimwhere.libraries.slick.UnicodeFont;
import me.kiras.aimwhere.libraries.slick.font.effects.ColorEffect;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.TextEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UnicodeFontRenderer
extends FontRenderer {
    private final UnicodeFont font;

    public UnicodeFontRenderer(Font awtFont, boolean unicode) {
        super(Minecraft.func_71410_x().field_71474_y, new ResourceLocation("textures/font/ascii.png"), Minecraft.func_71410_x().func_110434_K(), false);
        this.font = new UnicodeFont(awtFont.deriveFont(0));
        this.font.addAsciiGlyphs();
        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        if (unicode) {
            this.font.addGlyphs(0, 65535);
        }
        try {
            this.font.loadGlyphs();
        }
        catch (SlickException slickException) {
            throw new RuntimeException(slickException);
        }
        this.field_78288_b = this.font.getHeight("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789") / 2;
    }

    public String replaceColor(String str) {
        str = str.replaceAll("\u00a71", "\u00a70");
        str = str.replaceAll("\u00a72", "\u00a70");
        str = str.replaceAll("\u00a73", "\u00a70");
        str = str.replaceAll("\u00a74", "\u00a70");
        str = str.replaceAll("\u00a75", "\u00a70");
        str = str.replaceAll("\u00a76", "\u00a70");
        str = str.replaceAll("\u00a77", "\u00a70");
        str = str.replaceAll("\u00a78", "\u00a70");
        str = str.replaceAll("\u00a79", "\u00a70");
        str = str.replaceAll("\u00a70", "\u00a70");
        str = str.replaceAll("\u00a7a", "\u00a70");
        str = str.replaceAll("\u00a7b", "\u00a70");
        str = str.replaceAll("\u00a7c", "\u00a70");
        str = str.replaceAll("\u00a7d", "\u00a70");
        str = str.replaceAll("\u00a7e", "\u00a70");
        str = str.replaceAll("\u00a7f", "\u00a70");
        str = str.replaceAll("\u00a7r", "\u00a70");
        str = str.replaceAll("\u00a7A", "\u00a70");
        str = str.replaceAll("\u00a7B", "\u00a70");
        str = str.replaceAll("\u00a7C", "\u00a70");
        str = str.replaceAll("\u00a7D", "\u00a70");
        str = str.replaceAll("\u00a7E", "\u00a70");
        str = str.replaceAll("\u00a7F", "\u00a70");
        str = str.replaceAll("\u00a7R", "\u00a70");
        str = str.replaceAll("\u00a7r", "\u00a70");
        return str;
    }

    public int drawString(String text, float x, float y, boolean always) {
        String[] array;
        String Nameprotect = text;
        if (Nameprotect == null) {
            return 0;
        }
        Nameprotect = "\u00a7r" + Nameprotect + "\u00a7r";
        float len = -1.0f;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            Color col = Color.BLACK;
            str = str.substring(1, str.length());
            this.Draw(str, x + len, y, new Color(col.getRed(), col.getGreen(), col.getBlue()).getRGB());
            len += (float)(this.func_78256_a(str) + 1);
        }
        return (int)len;
    }

    public int func_78276_b(String text, int x, int y, int color) {
        String[] array;
        int ColorBak = color;
        String Nameprotect = text;
        if (Nameprotect == null) {
            return 0;
        }
        Nameprotect = "\u00a7r" + Nameprotect + "\u00a7r";
        float len = -1.0f;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = ColorBak;
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            this.Draw(str, (float)x + len, y, new Color(col.getRed(), col.getGreen(), col.getBlue()).getRGB());
            len += (float)(this.func_78256_a(str) + 1);
        }
        return (int)len;
    }

    public int func_175065_a(String text, float x, float y, int color, boolean dorpshadow) {
        String[] array;
        int ColorBak = color;
        String Nameprotect = text;
        if (Nameprotect == null) {
            return 0;
        }
        Nameprotect = "\u00a7r" + Nameprotect + "\u00a7r";
        float len = -1.0f;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = ColorBak;
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            if (dorpshadow) {
                this.Draw(str, x + len + 0.5f, y + 0.5f, this.getColor(0, 0, 0, 80));
            }
            this.Draw(str, x + len, y, new Color(col.getRed(), col.getGreen(), col.getBlue()).getRGB());
            len += (float)(this.func_78256_a(str) + 1);
        }
        return (int)len;
    }

    public int drawStringForChat(String text, float x, float y, int color, boolean dorpshadow) {
        String[] array;
        int ColorBak = color;
        String Nameprotect = text;
        if (Nameprotect == null) {
            return 0;
        }
        Nameprotect = "\u00a7r" + Nameprotect + "\u00a7r";
        float len = -1.0f;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = ColorBak;
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            if (dorpshadow) {
                this.Draw(str, x + len + 0.5f, y + 1.0f, this.getColor(0, 0, 0, 150));
            }
            this.Draw(str, x + len, y, new Color(col.getRed(), col.getGreen(), col.getBlue()).getRGB());
            len += (float)(this.func_78256_a(str) + 1);
        }
        return (int)len;
    }

    public int drawString(String text, float x, float y, int color, int alpha) {
        String[] array;
        int ColorBak = color;
        String Nameprotect = text;
        if (Nameprotect == null) {
            return 0;
        }
        Nameprotect = "\u00a7r" + Nameprotect + "\u00a7r";
        float len = -1.0f;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = ColorBak;
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            this.Draw(str, x + len, y, new Color(col.getRed(), col.getGreen(), alpha).getRGB());
            len += (float)(this.func_78256_a(str) + 1);
        }
        return (int)len;
    }

    public int drawStringWithShadow(String text, float x, float y, int color, int alpha) {
        String[] array;
        int ColorBak = color;
        String Nameprotect = text;
        if (Nameprotect == null) {
            return 0;
        }
        Nameprotect = "\u00a7r" + Nameprotect + "\u00a7r";
        float len = -1.0f;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = ColorBak;
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            int Shadowcolor = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
            this.Draw(str, x + len + 0.5f, y + 0.5f, this.getColor(0, 0, 0, 150));
            this.Draw(str, x + len, y, this.getColor(col.getRed(), col.getGreen(), col.getBlue(), alpha));
            len += (float)(this.func_78256_a(str) + 1);
        }
        return (int)len;
    }

    public int drawString(String text, float x, float y, int color) {
        String[] array;
        int ColorBak = color;
        String Nameprotect = text;
        if (Nameprotect == null) {
            return 0;
        }
        Nameprotect = "\u00a7r" + Nameprotect + "\u00a7r";
        float len = -1.0f;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = ColorBak;
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            this.Draw(str, x + len, y, new Color(col.getRed(), col.getGreen(), col.getBlue()).getRGB());
            len += (float)(this.func_78256_a(str) + 1);
        }
        return (int)len;
    }

    public int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        int color1 = color | alpha << 24;
        color1 |= red << 16;
        color1 |= green << 8;
        return color1 |= blue;
    }

    private void Draw(String string, float x, float y, int color) {
        TextEvent event = new TextEvent(string);
        LiquidBounce.eventManager.callEvent(event);
        GL11.glPushMatrix();
        GL11.glScaled((double)0.5, (double)0.5, (double)0.5);
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean lighting = GL11.glIsEnabled((int)2896);
        y = y * 2.0f - 8.0f;
        this.font.drawString(x *= 2.0f, y, event.getText(), new me.kiras.aimwhere.libraries.slick.Color(color));
        GlStateManager.func_179124_c((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glPopMatrix();
    }

    public int drawStringWithShadow(String text, int x, int y, int color) {
        String[] array;
        int ColorBak = color;
        String Nameprotect = text;
        if (Nameprotect == null) {
            return 0;
        }
        Nameprotect = "\u00a7r" + Nameprotect + "\u00a7r";
        float len = -1.0f;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = ColorBak;
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            int Shadowcolor = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
            this.Draw(str, (float)x + len + 0.5f, (float)y + 0.5f, this.getColor(0, 0, 0, 150));
            this.Draw(str, (float)x + len, y, this.getColor(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha()));
            len += (float)(this.func_78256_a(str) + 1);
        }
        return (int)len;
    }

    public int func_175063_a(String text, float x, float y, int color) {
        String[] array;
        int ColorBak = color;
        String Nameprotect = text;
        if (Nameprotect == null) {
            return 0;
        }
        Nameprotect = "\u00a7r" + Nameprotect + "\u00a7r";
        float len = -1.0f;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            switch (str.charAt(0)) {
                case '0': {
                    color = new Color(0, 0, 0).getRGB();
                    break;
                }
                case '1': {
                    color = new Color(0, 0, 170).getRGB();
                    break;
                }
                case '2': {
                    color = new Color(0, 170, 0).getRGB();
                    break;
                }
                case '3': {
                    color = new Color(0, 170, 170).getRGB();
                    break;
                }
                case '4': {
                    color = new Color(170, 0, 0).getRGB();
                    break;
                }
                case '5': {
                    color = new Color(170, 0, 170).getRGB();
                    break;
                }
                case '6': {
                    color = new Color(255, 170, 0).getRGB();
                    break;
                }
                case '7': {
                    color = new Color(170, 170, 170).getRGB();
                    break;
                }
                case '8': {
                    color = new Color(85, 85, 85).getRGB();
                    break;
                }
                case '9': {
                    color = new Color(85, 85, 255).getRGB();
                    break;
                }
                case 'a': {
                    color = new Color(85, 255, 85).getRGB();
                    break;
                }
                case 'b': {
                    color = new Color(85, 255, 255).getRGB();
                    break;
                }
                case 'c': {
                    color = new Color(255, 85, 85).getRGB();
                    break;
                }
                case 'd': {
                    color = new Color(255, 85, 255).getRGB();
                    break;
                }
                case 'e': {
                    color = new Color(255, 255, 85).getRGB();
                    break;
                }
                case 'f': {
                    color = new Color(255, 255, 255).getRGB();
                    break;
                }
                case 'r': {
                    color = ColorBak;
                }
            }
            Color col = new Color(color);
            str = str.substring(1, str.length());
            int Shadowcolor = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
            this.Draw(str, x + len + 0.5f, y + 0.5f, this.getColor(0, 0, 0, 150));
            this.Draw(str, x + len, y, this.getColor(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha()));
            len += (float)(this.func_78256_a(str) + 1);
        }
        return (int)len;
    }

    public int func_78263_a(char c) {
        return this.func_78256_a(Character.toString(c));
    }

    public int func_78256_a(String string) {
        String[] array;
        String Nameprotect = string;
        float len = -1.0f;
        Nameprotect = "\u00a7r" + Nameprotect;
        String[] arrstring = array = Nameprotect.split("\u00a7");
        int n = array.length;
        for (int i = 0; i < n; ++i) {
            String str = arrstring[i];
            if (str.length() < 1) continue;
            str = str.substring(1, str.length());
            len += (float)(this.font.getWidth(str) / 2 + 1);
        }
        return (int)len;
    }

    public int getStringHeight(String string) {
        return this.font.getHeight(string) / 2;
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        this.drawString(text, x - (float)(this.func_78256_a(text) / 2), y, color);
    }
}

