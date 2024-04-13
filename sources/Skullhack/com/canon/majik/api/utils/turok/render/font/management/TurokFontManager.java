package com.canon.majik.api.utils.turok.render.font.management;

import com.canon.majik.api.utils.turok.render.font.TurokFont;
import com.canon.majik.api.utils.turok.render.opengl.TurokRenderGL;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 26/09/20 at 1:23pm
 */
public class TurokFontManager {
	public static void render(TurokFont fontRenderer, String string, int x, int y, boolean shadow, Color color) {
		TurokRenderGL.enable(GL11.GL_TEXTURE_2D);
		TurokRenderGL.enableAlphaBlend();

		TurokRenderGL.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

		if (shadow) {
			if (fontRenderer.isRenderingCustomFont()) {
				fontRenderer.drawStringWithShadow(string, x, y, color.getRGB());
			} else {
				Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(string, x, y, color.getRGB());
			}
		} else {
			if (fontRenderer.isRenderingCustomFont()) {
				fontRenderer.drawString(string, x, y, color.getRGB());
			} else {
				Minecraft.getMinecraft().fontRenderer.drawString(string, x, y, color.getRGB());
			}
		}

		TurokRenderGL.disable(GL11.GL_TEXTURE_2D);
	}

	public static int getStringWidth(TurokFont fontRenderer, String string) {
		return fontRenderer.isRenderingCustomFont() ? (int) fontRenderer.getStringWidth(string) : Minecraft.getMinecraft().fontRenderer.getStringWidth(string);
	}

	public static int getStringHeight(TurokFont fontRenderer, String string) {
		return fontRenderer.isRenderingCustomFont() ? (int) fontRenderer.getStringHeight(string) : Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * fontRenderer.getFontSize();
	}
}
