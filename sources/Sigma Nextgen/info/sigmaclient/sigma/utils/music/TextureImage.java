package info.sigmaclient.sigma.utils.music;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import top.fl0wowp4rty.phantomshield.annotations.Native;
@Native

public class TextureImage {
	public BufferedImage pixels;
	public String location;
	public void rectTextureMasked2(float x, float y, float w, float h, float mx, float my, float u, float v) {
		if(pixels == null) return;
		GlStateManager.resetColor();
		final boolean enableBlend = GL11.glIsEnabled(3042);
		final boolean disableAlpha = !GL11.glIsEnabled(3008);
		if (!enableBlend) {
			GL11.glEnable(3042);
		}
		if (!disableAlpha) {
			GL11.glDisable(3008);
		}
		Draw.rectTextureMasked2(x, y, w,h,u,v,mx,my, pixels);
		if (!enableBlend) {
			GL11.glDisable(3042);
		}
		if (!disableAlpha) {
			GL11.glEnable(3008);
		}
	}
	public void rectTextureMasked(float x, float y, float w, float h, float mx, float my) {
		if(pixels == null) return;
		GlStateManager.resetColor();
		final boolean enableBlend = GL11.glIsEnabled(3042);
		final boolean disableAlpha = !GL11.glIsEnabled(3008);
		if (!enableBlend) {
			GL11.glEnable(3042);
		}
		if (!disableAlpha) {
			GL11.glDisable(3008);
		}
		Draw.rectTextureMasked(x, y, w,h,(int)mx,(int)my, pixels);
		if (!enableBlend) {
			GL11.glDisable(3042);
		}
		if (!disableAlpha) {
			GL11.glEnable(3008);
		}
	}
	public void rectTextureMasked(double x, double y, double w, double h, double mx, double my) {
		if(pixels == null) return;
		Draw.rectTextureMasked((float) x, (float) y, (float) w, (float) h,(int)mx,(int)my, pixels);
	}
}
