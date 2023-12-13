package best.azura.client.impl.clickgui.azura.impl.setting;

import best.azura.client.impl.clickgui.azura.SettingElement;
import best.azura.client.impl.module.impl.render.ClickGUI;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.ModeValue;

import java.awt.*;

public class ModeElement extends SettingElement {

	private final ModeValue value;
	private boolean extended = false;
	private double clickAnimation = 0, switchAnimation = 0;
	private long start, switchStart;
	private String before = "";

	public ModeElement(int x, int y, int width, int height, ModeValue value) {
		super(x, y, width, height, value);
		this.value = value;
	}

	@Override
	public void render(int mouseX, int mouseY) {

		super.render(mouseX, mouseY);

		if (!extended) {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			clickAnimation = -1 * Math.pow(anim - 1, 6) + 1;
			clickAnimation = 1 - clickAnimation;
		} else {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			clickAnimation = -1 * Math.pow(anim - 1, 6) + 1;
		}

		if (this.visibilityAnimation == 0) {
			this.height = 0;
			return;
		}

		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, 30, mouseX, mouseY);
		this.height = (int) ((30 + 30 * value.getObjects().length * this.clickAnimation) * visibilityAnimation);
		if (clickAnimation >= 0.05 && ClickGUI.theme.getObject().equals("Azura X")) {
			RenderUtil.INSTANCE.drawRoundedRect(x + 10 + 5 * clickAnimation, y, width - 20 - 10 * clickAnimation, 30 + (value.getObjects().length * 30) * clickAnimation, 5.0 * clickAnimation,
					new Color(0, 0, 0, (int) (100 * (animation == 1 ? visibilityAnimation == 1 ? clickAnimation : visibilityAnimation : animation))));
		}

		Fonts.INSTANCE.arial15.drawString(value.getName() + ": " + value.getObject(), x + 30, y + 15 - Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0,
				new Color(255, 255, 255, (int) (255 * (visibilityAnimation == 1 ? animation : visibilityAnimation))).getRGB());

		float anim = Math.min(1, (System.currentTimeMillis() - switchStart) / 500f);
		switchAnimation = -1 * Math.pow(anim - 1, 6) + 1;

		double generalAni = Math.min(animation, Math.min(visibilityAnimation, clickAnimation));
		if (clickAnimation != 0) {
			int count = 0;
			for (String s : value.getObjects()) {
				Color color = new Color(30, 30, 30, (int) (255 * generalAni));
				Color selectColor = RenderUtil.INSTANCE.modifiedAlpha(ClickGUI.color.getObject().getColor(), (int) (255 * generalAni));
				Color color1 = new Color(120, 120, 120, (int) (255 * generalAni));
				int elementY = (int) (y + (45 + count * 30) * generalAni);
				RenderUtil.INSTANCE.drawRoundedRect(x + 30, elementY - 5, 10, 10, 3, color);
				if (value.getObject().equals(s)) {
					RenderUtil.INSTANCE.drawRoundedRect(x + 30, elementY - 5, 10 * switchAnimation, 10, 3 * switchAnimation, selectColor);
				} else if (before.equals(s)) {
					RenderUtil.INSTANCE.drawRoundedRect(x + 30, elementY - 5, 10 * (1 - switchAnimation), 10, 3 * (1 - switchAnimation), selectColor);
				}
				Fonts.INSTANCE.arial15.drawString(s, x + 48, elementY - Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0,
						new Color(255, 255, 255, (int) (255 * generalAni)).getRGB());
				count++;
			}
		}
		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {

		if (RenderUtil.INSTANCE.isHovered(x, y, width, 30, mouseX, mouseY) && animation >= 0.99 && visibilityAnimation >= 0.99) {
			extended = !extended;
			start = System.currentTimeMillis();
			return;
		}

		if (this.hovered && visibilityAnimation >= 0.99 && clickAnimation >= 0.99) {

			int count = 0;
			for (String s : value.getObjects()) {
				int elementY = y + 30 + count * 30;
				if (RenderUtil.INSTANCE.isHovered(x, elementY, width, 30, mouseX, mouseY)) {
					before = value.getObject();
					value.setObject(s);
					if (!before.equals(value.getObject())) {
						switchAnimation = 0;
						switchStart = System.currentTimeMillis();
					}
				}
				count++;
			}

		}

	}

	public ModeValue getValue() {
		return value;
	}
}
