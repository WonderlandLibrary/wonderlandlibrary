package com.enjoytheban.utils.render.gl;

import org.lwjgl.opengl.GL11;

/**
 * Easily set the GLColor
 * @author purity
 */

public class GLColor {

	/**
	 * set the GL color using color4f
	 * @param color
	 */
	
	public static void color(int color) {
		float alpha = (color >> 24 & 255) /255.0f;
		float red = (color >> 16 & 255) / 255.0f;
		float green = (color >> 8 & 255) / 255.0f;
		float blue = (color & 255) / 255.0f;
		GL11.glColor4f(red, green, blue, alpha);
	}
}
