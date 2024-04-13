package com.canon.majik.api.utils.turok.render.image.management;

import com.canon.majik.api.utils.turok.render.image.TurokImage;
import com.canon.majik.api.utils.turok.render.opengl.TurokRenderGL;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author SrRina
 * @since 28/09/20 at 1:23pm
 */
public class TurokImageManager {
	public static void render(TurokImage image, int x, int y, float xx, float yy, int w, int h, float ww, float hh, Color color) {
		TurokRenderGL.enable(GL11.GL_BLEND);

		TurokRenderGL.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		TurokRenderGL.enable(GL11.GL_TEXTURE_2D);
		TurokRenderGL.enable(GL11.GL_CULL_FACE);
		TurokRenderGL.disable(GL11.GL_DEPTH_TEST);

		GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

		Minecraft.getMinecraft().renderEngine.bindTexture(image.getResourceLocation());

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		TurokRenderGL.drawTextureInterpolated(x, y, xx, yy, w, h, ww, hh);

		TurokRenderGL.disable(GL11.GL_BLEND);
		TurokRenderGL.disable(GL11.GL_TEXTURE_2D);
		TurokRenderGL.disable(GL11.GL_CULL_FACE);
		TurokRenderGL.enable(GL11.GL_DEPTH_TEST);
	}

	public static void render(TurokImage image, int x, int y, int w, int h, Color color) {
		render(image, x, y, w, h, 0, 0, 1, 1, color);
	}
}