package net.caffeinemc.phosphor.api.font;

import com.mojang.blaze3d.platform.GlStateManager;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.modules.client.AsteriaSettingsModule;

import java.awt.*;

public class JColor extends Color {
	
	private static final long serialVersionUID = 1L;

	public static JColor getGuiColor() {
		AsteriaSettingsModule radiumSettings = Phosphor.moduleManager().getModule(AsteriaSettingsModule.class);
		return (radiumSettings != null) ? radiumSettings.color.getColor() : new JColor(0.90f, 0.27f, 0.33f);
	}

	public JColor(int rgb) {
		super(rgb);
	}
	
	public JColor(int rgba, boolean hasalpha) {
		super(rgba, hasalpha);
	}
	
	public JColor(int r, int g, int b) {
		super(r, g, b);
	}
	
	public JColor(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

	public JColor(float r, float g, float b, float a) {
		super(r, g, b, a);
	}

	public JColor(float r, float g, float b) {
		super(r, g, b, 1.0f);
	}
	
	public JColor(Color color) {
		super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
	public JColor(JColor color, int a) {
		super(color.getRed(), color.getGreen(), color.getBlue(), a);
	}
	
	public static JColor fromHSB (float hue, float saturation, float brightness) {
		return new JColor(Color.getHSBColor(hue, saturation, brightness));
	}
	
	public float getHue() {
		return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[0];
	}
	
	public float getSaturation() {
		return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[1];
	}
	
	public float getBrightness() {
		return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[2];
	}

	public JColor jDarker() {
		return new JColor(this.darker());
	}

	public JColor jBrighter() {
		return new JColor(this.brighter());
	}

	public float[] getFloatColorWAlpha() {
		return new float[] { getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f, getAlpha() / 255.0f };
	}

	public float[] getFloatColor() {
		return new float[] { getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f, getAlpha() / 255.0f };
	}
	
	public void glColor() {
		GlStateManager._clearColor(getRed() / 255.0f, getGreen() / 255.0f, getBlue() / 255.0f, getAlpha() / 255.0f);
	}
}