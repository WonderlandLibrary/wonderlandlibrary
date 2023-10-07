package best.azura.client.impl.clickgui.azura.impl.setting;

import best.azura.client.impl.clickgui.azura.SettingElement;
import best.azura.client.impl.module.impl.render.ClickGUI;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.ComboSelection;
import best.azura.client.impl.value.ComboValue;

import java.awt.*;

public class SelectionElement extends SettingElement {

	private final ComboValue value;
	private long start;
	private double clickAnimation;
	private boolean extended;

	public SelectionElement(int x, int y, int width, int height, ComboValue value) {
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
		this.height = (int) ((30 + 30 * value.getObject().size() * this.clickAnimation) * visibilityAnimation);
		if (clickAnimation >= 0.01) {
			RenderUtil.INSTANCE.drawRoundedRect(x + 10 + 5 * clickAnimation, y, width - 20 - 10 * clickAnimation, 30 + (value.getObject().size() * 30) * clickAnimation, 5.0 * clickAnimation,
					new Color(0, 0, 0, (int) (100 * (animation == 1 ? visibilityAnimation == 1 ? clickAnimation : visibilityAnimation : animation))));
		}
		int textColor = new Color(255, 255, 255, (int) (255 * (visibilityAnimation == 1 ? animation : visibilityAnimation))).getRGB();
		Fonts.INSTANCE.arial15.drawString(value.getName(), x + 30, y + 15 - Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0, textColor);

		RenderUtil.INSTANCE.drawLine(x + width - 30, y + 15, x + width - 40, y + 15, 2f, textColor);
		RenderUtil.INSTANCE.drawLine(x + width - 35, y + 15 + 5 * (1 - clickAnimation), x + width - 35, y + 15, 2f, textColor);
		RenderUtil.INSTANCE.drawLine(x + width - 35, y + 15 - 5 * (1 - clickAnimation), x + width - 35, y + 15, 2f, textColor);

		double generalAni = Math.min(animation, Math.min(visibilityAnimation, clickAnimation));
		if (clickAnimation != 0) {
			int count = 0;
			for (ComboSelection s : value.getObject()) {
				s.updateAnimation();
				Color color = new Color(30, 30, 30, (int) (255 * generalAni));
				Color selectColor = RenderUtil.INSTANCE.modifiedAlpha(ClickGUI.color.getObject().getColor(), (int) (255 * generalAni));
				int elementY = (int) (y + (45 + count * 30) * generalAni);
				RenderUtil.INSTANCE.drawRoundedRect(x + 30, elementY - 5, 10, 10, 3, color);
				RenderUtil.INSTANCE.drawRoundedRect(x + 30, elementY - 5, 10 * s.animation, 10, 3 * s.animation, selectColor);
				Fonts.INSTANCE.arial15.drawString(s.getName(), x + 48, elementY - Fonts.INSTANCE.arial15.FONT_HEIGHT / 2.0,
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
			for (ComboSelection s : value.getObject()) {
				int elementY = y + 30 + count * 30;
				if (RenderUtil.INSTANCE.isHovered(x, elementY, width, 30, mouseX, mouseY)) {
					s.setObject(!s.getObject());
				}
				count++;
			}

		}

	}

	public ComboValue getValue() {
		return value;
	}
}
