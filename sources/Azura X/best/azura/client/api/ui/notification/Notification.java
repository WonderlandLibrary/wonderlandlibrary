package best.azura.client.api.ui.notification;

import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.ui.Texture;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.StencilUtil;
import best.azura.client.util.textures.JordanTextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

public class Notification {

	public final String title, text;
	public long start, length, animationStart;
	public double animation;
	public double width, yOffset;
	public Type type;
	public Texture texture;

	public Notification(final String title, final String text, final long length, Type type) {
		this.title = title;
		this.text = text;
		this.start = System.currentTimeMillis();
		this.length = length;
		this.animationStart = -1;
		this.animation = 0;
		this.type = type;
		this.width = 60 + Math.max(Fonts.INSTANCE.arial15.getStringWidth(text), Fonts.INSTANCE.arial15.getStringWidth(title)) + Fonts.INSTANCE.arial15.getStringWidth(String.valueOf(length));
	}

	public void reset() {
		animation = 0;
		animationStart = -1;
		start = System.currentTimeMillis();
		switch (type) {
			case INFO:
				texture = JordanTextureUtil.createTexture(new ResourceLocation("custom/notif/info.png"));
				break;
			case WARNING:
				texture = JordanTextureUtil.createTexture(new ResourceLocation("custom/notif/notify.png"));
				break;
			case ERROR:
				texture = JordanTextureUtil.createTexture(new ResourceLocation("custom/notif/warning.png"));
				break;
			case SUCCESS:
				texture = JordanTextureUtil.createTexture(new ResourceLocation("custom/notif/okay.png"));
				break;
			default:
				texture = null;
				break;
		}
		if (texture != null) texture = new Texture(texture.getBufferedImage(), true);
	}

	public void render() {
		Minecraft mc = Minecraft.getMinecraft();
		double secs = Math.round(((length - (System.currentTimeMillis() - start)) / 1000.0) * 10.0) / 10.0;
		String title = this.title + " (" + (secs < 0 ? 0 : secs) + ")";
		if (isFinished() && animationStart == -1) animationStart = System.currentTimeMillis();
		if (!isFinished()) {
			double anim = Math.min(1, (System.currentTimeMillis() - start) / 1300D);
			animation = -1 * Math.pow(anim - 1, 6) + 1;
		} else {
			double anim = Math.min(1, (System.currentTimeMillis() - animationStart) / 1300D);
			animation = -1 * Math.pow(anim, 6) + 1;
		}
		glPushMatrix();
		RenderUtil.INSTANCE.scaleFix(1.0);
		boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurNotifications.getObject();
		final ScaledResolution sr = new ScaledResolution(mc);
		if (blur) {
			GL11.glPushMatrix();
			RenderUtil.INSTANCE.invertScaleFix(1.0);
			StencilUtil.initStencilToWrite();
			GlStateManager.scale(1.0 / sr.getScaleFactor(), 1.0 / sr.getScaleFactor(), 1);
		}

		RenderUtil.INSTANCE.drawRoundedRect(mc.displayWidth - (width + 10) * animation, mc.displayHeight - 100 - yOffset, width, 60, 10, new Color(24, 24, 24, 175));

		if (blur) {
			GlStateManager.scale(sr.getScaleFactor(), sr.getScaleFactor(), 1);
			StencilUtil.readStencilBuffer(1);
			BlurModule.blurShader.blur();
			StencilUtil.uninitStencilBuffer();
			RenderUtil.INSTANCE.scaleFix(1.0);
			GL11.glPopMatrix();
		}

		if (texture != null) {
			Gui.drawRect(0,0,0,0,-1);
			GlStateManager.resetColor();
			GlStateManager.color(1, 1, 1, 1);
			texture.bind();
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			RenderUtil.INSTANCE.drawTexture(mc.displayWidth - (width + 10) * animation + 8, mc.displayHeight - 100 - yOffset + 14,
					mc.displayWidth - (width + 10) * animation + 40, mc.displayHeight - 100 - yOffset + 46);
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			texture.unbind();
		}

		Fonts.INSTANCE.arial15bold.drawStringWithOutline(title, mc.displayWidth - (width - 40) * animation, mc.displayHeight - 90 - yOffset, -1);
		Fonts.INSTANCE.arial15.drawStringWithOutline(text.replace("%s", String.valueOf(Math.max(0, secs))), mc.displayWidth - (width - 40) * animation, mc.displayHeight - 70 - yOffset, -1);
		glPopMatrix();


	}

	public boolean isFinished() {
		return System.currentTimeMillis() > length + start;
	}

	public boolean isAnimationFinished() {
		return animation == 0 && isFinished();
	}
}
