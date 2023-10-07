package best.azura.client.impl.ui.gui.impl;

import best.azura.client.api.ui.buttons.Button;
import best.azura.client.impl.Client;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.impl.ui.gui.impl.buttons.TextButton;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.StencilUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class Window {
	
	public boolean hidden = false, hovered = false;
	public String text;
	public int x, y, width, height;
	private long start;
	public double animation = 0;
	public final Minecraft mc = Minecraft.getMinecraft();
	public Color backgroundColor = new Color(22, 22, 22, 170);
	public ArrayList<Button> buttons = new ArrayList<>();
	
	public Window(String text, int width, int height) {
		this.text = text;
		this.x = mc.displayWidth / 2 - width / 2;
		this.y = mc.displayHeight / 2 - height / 2;
		this.width = width;
		this.height = height;
		start = System.currentTimeMillis();
	}

	public Window(String text, int x, int y, int width, int height) {
		this(text, width, height);
		this.x = x;
		this.y = y;
	}

	public Window(int x, int y, int width, int height) {
		this(null, x, y, width, height);
	}
	
	public void draw(int mouseX, int mouseY) {
		if (animation == 0 && hidden) return;
		final ScaledResolution sr = new ScaledResolution(this.mc);
		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX * sr.getScaleFactor(), mouseY * sr.getScaleFactor());

		if (hidden) {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			animation = -1 * Math.pow(anim - 1, 6) + 1;
			animation = 1 - animation;
		} else {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			animation = -1 * Math.pow(anim - 1, 6) + 1;
		}

		final boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurMenu.getObject();
		if (blur) {
			GL11.glPushMatrix();
			RenderUtil.INSTANCE.invertScaleFix(1.0);
			StencilUtil.initStencilToWrite();
			GlStateManager.scale(1.0 / sr.getScaleFactor(), 1.0 / sr.getScaleFactor(), 1);
		}
		RenderUtil.INSTANCE.drawRoundedRect(x - 3, y - 3, width + 6, height + 6, 10, new Color(backgroundColor.getRed(), backgroundColor.getGreen(),
				backgroundColor.getBlue(), (int) (backgroundColor.getAlpha() * animation)));
		RenderUtil.INSTANCE.drawRoundedRect(x, y, width, height, 10, new Color(0, 0, 0, (int) (50 * animation)));
		if (blur) {
			GlStateManager.scale(sr.getScaleFactor(), sr.getScaleFactor(), 1);
			StencilUtil.readStencilBuffer(1);
			BlurModule.blurShader.blur();
			StencilUtil.uninitStencilBuffer();
			RenderUtil.INSTANCE.scaleFix(1.0);
			GL11.glPopMatrix();
		}

		if (text != null) {
			Fonts.INSTANCE.arial20.drawString(text, x + width / 2 - Fonts.INSTANCE.arial20.getStringWidth(text) / 2,
					y + 10, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
		}

		for (Button button : buttons) {
			if (button instanceof ButtonImpl) ((ButtonImpl)button).animation = animation;
			button.draw(mouseX, mouseY);
		}

	}
	
	public void hide() {
		hidden = true;
		start = System.currentTimeMillis();
	}
	
	public void keyTyped(char typed, int keyCode) {
		for (Button button : buttons) {
			button.keyTyped(typed, keyCode);
		}
	}
	
	public Button clickedButton(int mouseX, int mouseY) {
		if (hidden) return null;

		Button clicked = null;
		for (Button button : buttons) {
			if (button instanceof TextButton) button.mouseClicked();
			if (button instanceof ButtonImpl) {
				if (((ButtonImpl)button).hovered) {
					button.mouseClicked();
					clicked = button;
				}
			}
		}
		
		if (!RenderUtil.INSTANCE.isHoveredScaled(x, y, width, height, mouseX, mouseY, 1.0)) {
			hidden = true;
			start = System.currentTimeMillis();
		}
		return clicked;
	}
	
}
