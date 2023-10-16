package com.enjoytheban.ui.font;

import java.awt.Font;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class FontLoaders {

	public static CFontRenderer kiona16 = new CFontRenderer(getKiona(16), true, true);
	public static CFontRenderer kiona18 = new CFontRenderer(getKiona(18), true, true);
	public static CFontRenderer kiona20 = new CFontRenderer(getKiona(20), true, true);
	public static CFontRenderer kiona22 = new CFontRenderer(getKiona(22), true, true);
	public static CFontRenderer kiona24 = new CFontRenderer(getKiona(24), true, true);
	public static CFontRenderer kiona26 = new CFontRenderer(getKiona(26), true, true);
	public static CFontRenderer kiona28 = new CFontRenderer(getKiona(28), true, true);

	private static Font getKiona(int size) {
		Font font;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("ETB/raleway.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, size);
		}
		return font;
	}

}
