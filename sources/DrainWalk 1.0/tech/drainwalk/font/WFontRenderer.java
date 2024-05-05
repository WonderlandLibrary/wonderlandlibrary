package tech.drainwalk.font;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import tech.drainwalk.utility.shader.Shader;


import java.awt.*;

public class WFontRenderer extends WFont {
	private static final Shader FONT_SUBSTRING = new Shader("drainwalk/shaders/font_substring.fsh", true);
	private final int[] colorCode = new int[32];
	protected WFont.CharData[] boldChars = new WFont.CharData[1110];
	protected WFont.CharData[] italicChars = new WFont.CharData[1110];
	protected WFont.CharData[] boldItalicChars = new WFont.CharData[1110];

	public WFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
		super(font, antiAlias, fractionalMetrics);

		for (int index = 0; index < 32; index++) {
			int noClue = (index >> 3 & 0x1) * 85;
			int red = (index >> 2 & 0x1) * 170 + noClue;
			int green = (index >> 1 & 0x1) * 170 + noClue;
			int blue = (index & 0x1) * 170 + noClue;

			if (index == 6) {
				red += 85;
			}

			if (index >= 16) {
				red /= 4;
				green /= 4;
				blue /= 4;
			}

			this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF);
		}
	}

	public void drawSubstringDefault(String text, float x, float y, int color, float maxWidth) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		FONT_SUBSTRING.useProgram();
		FONT_SUBSTRING.setupUniform4f("inColor", (color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F,
				(color & 255) / 255F, (color >> 24 & 255) / 255F);
		FONT_SUBSTRING.setupUniform1f("width", maxWidth);
		FONT_SUBSTRING.setupUniform1f("maxWidth", (x + maxWidth) * 2);
		drawString(text, x, y, color);
		FONT_SUBSTRING.unloadProgram();
		GlStateManager.disableBlend();

	}



	public static void drawStringWithOutline(WFontRenderer fontRenderer, String text, float x, float y, int color) {
		fontRenderer.drawString(text, x - 0.8F, y, Color.BLACK.getRGB());
		fontRenderer.drawString(text, x + 0.8F, y, Color.BLACK.getRGB());
		fontRenderer.drawString(text, x, y - 0.8F, Color.BLACK.getRGB());
		fontRenderer.drawString(text, x, y + 0.8F, Color.BLACK.getRGB());
		fontRenderer.drawString(text, x, y, color);
	}

	public void drawCenteredStringWithOutline(WFontRenderer fontRenderer, String text, float x, float y, int color) {
		drawCenteredString(text, x - 1, y, Color.BLACK.getRGB());
		drawCenteredString(text, x + 1, y, Color.BLACK.getRGB());
		drawCenteredString(text, x, y - 1, Color.BLACK.getRGB());
		drawCenteredString(text, x, y + 1, Color.BLACK.getRGB());
		drawCenteredString(text, x, y, color);
	}

	public float drawStringWithShadow(String text, double x, double y, int color) {
		float shadowWidth = this.drawString(text, (double) x + 0.5D, (double) y + 0.5D, color, true);
		return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
	}

	public float drawString(String text, float x, float y, int color) {
		return drawString(text, x, y, color, false);
	}

	public float drawCenteredString(String text, float x, float y, int color) {
		return drawString(text, x - getStringWidth(text) / 2F, y, color);
	}

	public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
		return drawStringWithShadow(text, x - getStringWidth(text) / 2F, y, color);
	}

	public float drawString(String text, double x, double y, int color, boolean shadow) {
		try {
			if (text == null) {
				return 0;
			}
			x -= 1;
			text = fixStr(text);
			GL11.glColor4f(1, 1, 1, 1);
			GlStateManager.color(1, 1, 1, 1);
			GL11.glEnable(GL11.GL_BLEND);

			if (text == null) {
				return 0;
			}

			if (color == 553648127) {
				color = 16777215;
			}

			if ((color & 0xFC000000) == 0) {
				color |= -16777216;
			}

			if (shadow) {
				color = (color & 0xFCFCFC) >> 2 | color & new Color(20, 20, 20, 200).getRGB();
			}

			float alpha = (color >> 24 & 0xFF) / 255.0F;
			x *= 2.0D;
			y = (y - 3.0D) * 2.0D;

			GL11.glPushMatrix();
			GlStateManager.scale(0.5f, 0.5f, 0.5f);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.color((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F, (color & 255) / 255F, alpha);
			GlStateManager.enableTexture2D();
			GlStateManager.bindTexture(tex.getGlTextureId());
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getGlTextureId());
			bufferbuilder.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);

			for (int i = 0; i < text.length(); i++) {
				char character = text.charAt(i);
				if (String.valueOf(character).equals("\247")) {
					char next = text.charAt(i + 1);
					int colorIndex = "0123456789abcdefklmnor".indexOf(next);
					if (next == 'r') {
						GlStateManager.color((color >> 16 & 255) / 255F, (color >> 8 & 255) / 255F,
								(color & 255) / 255F, alpha);
					} else if (colorIndex < 16) {
						GlStateManager.bindTexture(tex.getGlTextureId());

						if (colorIndex < 0) {
							colorIndex = 15;
						}

						if (shadow) {
							colorIndex += 16;
						}

						int colorcode = this.colorCode[colorIndex];
						GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F,
								(colorcode & 0xFF) / 255.0F, alpha);
					}
					i++;
				} else if (character < charData.length) {
					drawChar(charData, character, (float) x, (float) y);
					x += charData[character].width - 8 + charOffset;
				}
			}
			tessellator.draw();
			GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
			GL11.glPopMatrix();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (float) x / 2;
	}

	public String fixStr(String input) {
		StringBuilder builder = new StringBuilder();
		char[] buffer = input.toCharArray();
		for (char c : buffer) {
			if ((int) c < this.charData.length && this.charData[(int) c] != null)
				builder.append(c);
		}
		return builder.toString();
	}
	private final byte[] glyphWidth = new byte[65536];
	private float[] charWidthFloat = new float[256];
	private float getCharWidthFloat(char p_getCharWidthFloat_1_)
	{
		if (p_getCharWidthFloat_1_ == 167)
		{
			return -1.0F;
		}
		else if (p_getCharWidthFloat_1_ != ' ' && p_getCharWidthFloat_1_ != 160)
		{
			int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(p_getCharWidthFloat_1_);

			if (p_getCharWidthFloat_1_ > 0 && i != -1)
			{
				return this.charWidthFloat[i];
			}
			else if (this.glyphWidth[p_getCharWidthFloat_1_] != 0)
			{
				int j = this.glyphWidth[p_getCharWidthFloat_1_] & 255;
				int k = j >>> 4;
				int l = j & 15;
				++l;
				return (float)((l - k) / 2 + 1);
			}
			else
			{
				return 0.0F;
			}
		}
		else
		{
			return this.charWidthFloat[32];
		}
	}
	public String trimStringToWidth(String text, int width)
	{
		return this.trimStringToWidth(text, width, false);
	}

	/**
	 * Trims a string to a specified width, and will reverse it if par3 is set.
	 */
	public String trimStringToWidth(String text, int width, boolean reverse)
	{
		StringBuilder stringbuilder = new StringBuilder();
		float f = 0.0F;
		int i = reverse ? text.length() - 1 : 0;
		int j = reverse ? -1 : 1;
		boolean flag = false;
		boolean flag1 = false;

		for (int k = i; k >= 0 && k < text.length() && f < (float)width; k += j)
		{
			char c0 = text.charAt(k);
			float f1 = this.getCharWidthFloat(c0);

			if (flag)
			{
				flag = false;

				if (c0 != 'l' && c0 != 'L')
				{
					if (c0 == 'r' || c0 == 'R')
					{
						flag1 = false;
					}
				}
				else
				{
					flag1 = true;
				}
			}
			else if (f1 < 0.0F)
			{
				flag = true;
			}
			else
			{
				f += f1;

				if (flag1)
				{
					++f;
				}
			}

			if (f > (float)width)
			{
				break;
			}

			if (reverse)
			{
				stringbuilder.insert(0, c0);
			}
			else
			{
				stringbuilder.append(c0);
			}
		}

		return stringbuilder.toString();
	}
	@Override
	public int getStringWidth(String text) {
		int x = 0;
		if (text == null)
			return 0;
		text = fixStr(text);
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			if (character == '§') {
				i++;
			} else if (character < charData.length) {
				x += charData[character].width - 8 + charOffset;
			}
		}
		return x / 2;
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
	}

	@Override
	public void setAntiAlias(boolean antiAlias) {
		super.setAntiAlias(antiAlias);
	}

	@Override
	public void setFractionalMetrics(boolean fractionalMetrics) {
		super.setFractionalMetrics(fractionalMetrics);
	}

	private void drawLine(double x, double y, double x1, double y1, float width) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glLineWidth(width);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x1, y1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void drawStringWithOutline(String text, double x, double y, int color) {
		drawString(text, x - 0.5, y, Color.BLACK.getRGB(), false);
		drawString(text, x + 0.5F, y, Color.BLACK.getRGB(), false);
		drawString(text, x, y - 0.5F, Color.BLACK.getRGB(), false);
		drawString(text, x, y + 0.5F, Color.BLACK.getRGB(), false);
		drawString(text, x, y, color, false);
	}

	public void drawCenteredStringWithOutline(String text, float x, float y, int color) {
		drawCenteredString(text, x - 0.5F, y, Color.BLACK.getRGB());
		drawCenteredString(text, x + 0.5F, y, Color.BLACK.getRGB());
		drawCenteredString(text, x, y - 0.5F, Color.BLACK.getRGB());
		drawCenteredString(text, x, y + 0.5F, Color.BLACK.getRGB());
		drawCenteredString(text, x, y, color);
	}
}