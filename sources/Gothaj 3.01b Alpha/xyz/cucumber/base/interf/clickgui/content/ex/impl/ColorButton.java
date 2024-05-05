package xyz.cucumber.base.interf.clickgui.content.ex.impl;

import java.awt.Color;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.interf.clickgui.ClickGui;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

public class ColorButton extends SettingsButton {

	private ColorSettings setting;

	private PositionUtils color1, hue1, color2, hue2, alpha, mode;

	private boolean open;

	private float h1, h2;

	private double height, rollAnimation;

	public ColorButton(ColorSettings setting, PositionUtils position, PositionUtils color1, PositionUtils color2,
			PositionUtils alpha, PositionUtils mode) {
		this.settingMain =setting;
		this.position = position;
		this.setting = setting;
		this.color1 = color1;
		this.color2 = color2;
		this.alpha = alpha;
		this.mode = mode;
		this.hue1 = new PositionUtils(0, 0, 65, 4, 1);
		this.hue2 = new PositionUtils(0, 0, 65, 4, 1);
		this.height = position.getHeight();
		h1 = 0;
		h2 = 0;
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		Fonts.getFont("rb-r").drawString(setting.getName(), position.getX() + 8, position.getY() + 3, -1);

		RenderUtils.drawRoundedRect(position.getX() + position.getWidth() / 2 - 15, position.getY() + 2,
				position.getX() + position.getWidth() / 2 - 1, position.getY() + height - 4, setting.getMainColor(),
				0.2F);
		RenderUtils.drawRoundedRect(position.getX() + position.getWidth() / 2 + 1, position.getY() + 2,
				position.getX() + position.getWidth() / 2 + 15, position.getY() + height - 4,
				setting.getSecondaryColor(), 0.2F);

		Fonts.getFont("rb-r").drawString("Alpha: §f" + setting.getAlpha() + "%",
				position.getX2() - Fonts.getFont("rb-r").getWidth("Alpha: " + setting.getAlpha() + "%") - 5,
				position.getY() + 3, 0xff4269f5);

		if (open) {
			rollAnimation = (rollAnimation * 11 + 65) / 12;
			this.position.setHeight(height + rollAnimation);
			this.color1.setX(position.getX() + 15);
			this.color1.setY(position.getY() + height + 2.5);
			this.hue1.setX(color1.getX());
			this.hue1.setY(color1.getY2() + 2.5);

			RenderUtils.drawColorPicker(color1.getX(), color1.getY(), color1.getWidth(), color1.getHeight(), h1);

			RenderUtils.drawColorSlider(hue1.getX(), hue1.getY(), hue1.getWidth(), hue1.getHeight());

			this.color2.setX(color1.getX2() + 5);
			this.color2.setY(position.getY() + height + 2.5);
			this.hue2.setX(color2.getX());
			this.hue2.setY(color2.getY2() + 2.5);

			RenderUtils.drawColorPicker(color2.getX(), color2.getY(), color2.getWidth(), color2.getHeight(), h2);

			RenderUtils.drawColorSlider(hue2.getX(), hue2.getY(), hue2.getWidth(), hue2.getHeight());

			this.mode.setX(color2.getX2() + 2.5);
			this.mode.setY(position.getY() + height + (position.getHeight() - height) / 2
					- Fonts.getFont("rb-r").getHeight("Mode: " + setting.getMode()) / 2);
			this.mode.setWidth(Fonts.getFont("rb-r").getWidth("Mode: " + setting.getMode()));
			this.mode.setHeight(10);
			
			Fonts.getFont("rb-r").drawString("Mode: §f" + setting.getMode(), mode.getX(), mode.getY()+3, 0xff4269f5);

			this.alpha.setX(color1.getX());
			this.alpha.setY(hue1.getY2() + 2.5);
			RenderUtils.drawImage(alpha.getX() + 1, alpha.getY() + 1, alpha.getWidth() - 1, alpha.getHeight() - 1,
					new ResourceLocation("client/images/alpha.png"), -1);
			RenderUtils.drawGradientRectSideways(alpha.getX(), alpha.getY(), alpha.getX2(), alpha.getY2(), 0x00000000,
					0xff000000);
			// color1
			if (ClickGui.getContent().getPosition().isInside(mouseX, mouseY)) {
				if (color1.isInside(mouseX, mouseY)) {
					for (double x = color1.getX(); x < color1.getX2(); x++) {
						for (double y = color1.getY(); y < color1.getY2(); y++) {
							if (mouseX > x && mouseX < x + 1 && mouseY > y && mouseY < y + 1 && Mouse.isButtonDown(0)) {
								double diffX = x - color1.getX();
								double diffY = color1.getY2() - y;

								setting.setMainColor(Color.HSBtoRGB(h1, (float) (diffX / color1.getWidth()),
										(float) (diffY / color1.getHeight())));
							}
						}
					}
				}
				if (color2.isInside(mouseX, mouseY)) {
					for (double x = color2.getX(); x < color2.getX2(); x++) {
						for (double y = color2.getY(); y < color2.getY2(); y++) {
							if (mouseX > x && mouseX < x + 1 && mouseY > y && mouseY < y + 1 && Mouse.isButtonDown(0)) {
								double diffX = x - color2.getX();
								double diffY = color2.getY2() - y;

								setting.setSecondaryColor(Color.HSBtoRGB(h2, (float) (diffX / color2.getWidth()),
										(float) (diffY / color2.getHeight())));
							}
						}
					}
				}
				if (hue1.isInside(mouseX, mouseY)) {
					for (double x = hue1.getX(); x < hue1.getX2(); x++) {
						if (mouseX > x && mouseX < x + 1 && mouseY > hue1.getY() && mouseY < hue1.getY2()
								&& Mouse.isButtonDown(0)) {
							double diffX = x - hue1.getX();

							h1 = (float) (diffX / hue1.getWidth());
						}
					}
				}
				if (hue2.isInside(mouseX, mouseY)) {
					for (double x = hue2.getX(); x < hue2.getX2(); x++) {
						if (mouseX > x && mouseX < x + 1 && mouseY > hue2.getY() && mouseY < hue2.getY2()
								&& Mouse.isButtonDown(0)) {
							double diffX = x - hue2.getX();

							h2 = (float) (diffX / hue2.getWidth());
						}
					}
				}

				if (alpha.isInside(mouseX, mouseY)) {
					for (double x = alpha.getX(); x < alpha.getX2(); x++) {
						if (mouseX > x && mouseX < x + 1 && mouseY > alpha.getY() && mouseY < alpha.getY2()
								&& Mouse.isButtonDown(0)) {
							double diffX = x - alpha.getX() - 1;

							setting.setAlpha((int) ((alpha.getWidth() - diffX) / alpha.getWidth() * 100));
						}
					}
				}
			}
		} else {
			rollAnimation = (rollAnimation * 11 + height) / 12;
			this.position.setHeight(rollAnimation);
		}
	}

	@Override
	public void click(int mouseX, int mouseY, int button) {
		if (position.isInside(mouseX, mouseY) && button == 1) {
			open = !open;
		}
		if (position.isInside(mouseX, mouseY) && mode.isInside(mouseX, mouseY) && button == 0) {
			setting.cycleModes();
		}
	}

	public ColorSettings getSetting() {
		return setting;
	}

	public void setSetting(ColorSettings setting) {
		this.setting = setting;
	}

	public PositionUtils getColor1() {
		return color1;
	}

	public void setColor1(PositionUtils color1) {
		this.color1 = color1;
	}

	public PositionUtils getColor2() {
		return color2;
	}

	public void setColor2(PositionUtils color2) {
		this.color2 = color2;
	}

	public PositionUtils getAlpha() {
		return alpha;
	}

	public void setAlpha(PositionUtils alpha) {
		this.alpha = alpha;
	}

	public PositionUtils getMode() {
		return mode;
	}

	public void setMode(PositionUtils mode) {
		this.mode = mode;
	}

}
