/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util.fontRenderer;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

public class UnicodeFontRenderer
extends FontRenderer {
    private final UnicodeFont font;

    public UnicodeFontRenderer(Font awtFont) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        this.font = new UnicodeFont(awtFont);
        this.font.addAsciiGlyphs();
        this.font.getEffects().add(new ColorEffect(Color.WHITE));
        try {
            this.font.loadGlyphs();
        }
        catch (SlickException exception) {
            throw new RuntimeException(exception);
        }
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        this.FONT_HEIGHT = this.font.getHeight(alphabet) / 2;
    }

    public void drawTotalCenteredStringWithShadow(String text, double x2, double y2, int color) {
        this.drawStringWithShadow(text, (float)(x2 - (double)(this.getStringWidth(text) / 2)), (float)(y2 - (double)((float)this.FONT_HEIGHT / 2.0f)), color);
    }

    public void drawTotalCenteredString(String text, int x2, int y2, int color) {
        this.drawString(text, x2 - this.getStringWidth(text) / 2, y2 - this.FONT_HEIGHT / 2, color);
    }

    public void drawTotalCenteredString(String text, double x2, double y2, int color) {
        this.drawString(text, x2 - (double)(this.getStringWidth(text) / 2), y2 - (double)(this.FONT_HEIGHT / 2), color);
    }

    public float drawString(String string, float x2, float y2, int color) {
        if (string == null) {
            return 0.0f;
        }
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        boolean blend = GL11.glIsEnabled(3042);
        boolean lighting = GL11.glIsEnabled(2896);
        boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (lighting) {
            GL11.glDisable(2896);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        this.font.drawString(x2 *= 2.0f, y2 *= 2.0f, string, new org.newdawn.slick.Color(color));
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
        return (int)x2;
    }

    public float drawString(String string, double x2, double y2, int color) {
        if (string == null) {
            return 0.0f;
        }
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);
        boolean blend = GL11.glIsEnabled(3042);
        boolean lighting = GL11.glIsEnabled(2896);
        boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (lighting) {
            GL11.glDisable(2896);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        this.font.drawString((float)(x2 *= 2.0), (float)(y2 *= 2.0), string, new org.newdawn.slick.Color(color));
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
        return (int)x2;
    }

    @Override
    public float drawStringWithShadow(String text, float x2, float y2, String c) {
        this.drawString(text, x2 + 0.5f, y2 + 0.5f, -16777216);
        return (int)this.drawString(text, x2, y2, c);
    }

    private int drawString(String text, float x2, float y2, String c) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
    public int getCharWidth(char c2) {
        return this.getStringWidth(Character.toString(c2));
    }

    @Override
    public int getStringWidth(String string) {
        return this.font.getWidth(string) / 2;
    }

    public int getStringHeight(String string) {
        return this.font.getHeight(string) / 2;
    }

    public void drawCenteredString(String text, float x2, float y2, int color) {
        this.drawString(text, x2 - (float)(this.getStringWidth(text) / 2), y2, color);
    }

    public void drawCenteredString(String text, double x2, double y2, int color) {
        this.drawString(text, x2 - (double)(this.getStringWidth(text) / 2), y2, color);
    }
}

