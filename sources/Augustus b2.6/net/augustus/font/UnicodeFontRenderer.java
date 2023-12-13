// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.font;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import org.newdawn.slick.font.HieroSettings;
import java.awt.Font;
import net.minecraft.client.gui.ScaledResolution;
import org.newdawn.slick.UnicodeFont;

public class UnicodeFontRenderer
{
    private final UnicodeFont font;
    private int FONT_HEIGHT;
    ScaledResolution sr;
    
    public UnicodeFontRenderer(final Font awtFont) {
        this.FONT_HEIGHT = 9;
        final HieroSettings hieroSettings = new HieroSettings();
        hieroSettings.setGlyphPageWidth(2048);
        hieroSettings.setGlyphPageHeight(2048);
        this.sr = new ScaledResolution(Minecraft.getMinecraft());
        hieroSettings.setFontSize((int)(awtFont.getSize() / 2.0f * this.sr.getScaleFactor()));
        (this.font = new UnicodeFont(awtFont, hieroSettings)).addAsciiGlyphs();
        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.font.loadGlyphs();
        }
        catch (SlickException var3) {
            throw new RuntimeException(var3);
        }
        this.FONT_HEIGHT = this.font.getHeight("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789") / this.sr.getScaleFactor();
    }
    
    public float drawString(final String string, float x, float y, final int color) {
        if (string == null) {
            return 0.0f;
        }
        GL11.glPushMatrix();
        GL11.glScaled(1.0f / this.sr.getScaleFactor(), 1.0f / this.sr.getScaleFactor(), 1.0f / this.sr.getScaleFactor());
        final boolean blend = GL11.glIsEnabled(3042);
        final boolean lighting = GL11.glIsEnabled(2896);
        final boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (lighting) {
            GL11.glDisable(2896);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        x *= this.sr.getScaleFactor();
        y *= this.sr.getScaleFactor();
        this.font.drawString(x, y, string, new org.newdawn.slick.Color(color));
        if (texture) {
            GL11.glEnable(3553);
        }
        if (lighting) {
            GL11.glEnable(2896);
        }
        if (!blend) {
            GL11.glDisable(3042);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glPopMatrix();
        GlStateManager.bindTexture(0);
        return x;
    }
    
    public float drawStringWithShadow(final String text, final float x, final float y, final int color) {
        this.drawString(text, x + 0.5f, y + 0.5f, -16777216);
        return this.drawString(text, x, y, color);
    }
    
    public int getCharWidth(final char c) {
        return this.getStringWidth(Character.toString(c));
    }
    
    public int getStringWidth(final String string) {
        return this.font.getWidth(string) / this.sr.getScaleFactor();
    }
    
    public int getStringHeight(final String string) {
        return this.font.getHeight(string) / this.sr.getScaleFactor();
    }
    
    public void drawCenteredString(final String text, final float x, final float y, final int color) {
        this.drawString(text, x - this.getStringWidth(text) / 2, y, color);
    }
}
