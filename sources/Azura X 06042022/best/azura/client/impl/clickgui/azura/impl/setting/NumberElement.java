package best.azura.client.impl.clickgui.azura.impl.setting;

import best.azura.client.impl.clickgui.azura.SettingElement;
import best.azura.client.impl.module.impl.render.ClickGUI;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.NumberValue;

import java.awt.*;

public class NumberElement extends SettingElement {

	private final NumberValue<?> value;
	private boolean dragging = false;

	public NumberElement(int x, int y, int width, int height, NumberValue<?> value) {
		super(x, y, width, height, value);
		this.value = value;
	}

	@Override
	public void render(int mouseX, int mouseY) {

		super.render(mouseX, mouseY);
		this.height = this.visibilityAnimation == 0 ? 0 : (int) (40 * this.visibilityAnimation);
		if (this.visibilityAnimation == 0) return;

		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);
		Color startColor = ClickGUI.color.getObject().getColor();
		Number number = value.getObject();

		int sliderWidth = width - 60;
		int sliderX = x + 30;
		double diff = value.getMax().doubleValue() - value.getMin().doubleValue();
		if (this.dragging) {
			NumberValue<Number> value = (NumberValue<Number>) this.value;
			if (value.getObject() instanceof Long) {
				double val = value.getMin().longValue() + (((double) (mouseX - sliderX) / sliderWidth) * (value.getMax().longValue() - value.getMin().longValue()));
				value.setObject((long) val);
			} else if (value.getObject() instanceof Float) {
				double val = value.getMin().floatValue() + (((double) (mouseX - sliderX) / sliderWidth) * (value.getMax().floatValue() - value.getMin().floatValue()));
				value.setObject((float) val);
			} else if (value.getObject() instanceof Integer) {
				double val = value.getMin().intValue() + (((double) (mouseX - sliderX) / sliderWidth) * (value.getMax().intValue() - value.getMin().intValue()));
				value.setObject((int) val);
			} else if (value.getObject() instanceof Double) {
				double val = value.getMin().doubleValue() + (((double) (mouseX - sliderX) / sliderWidth) * (value.getMax().doubleValue() - value.getMin().doubleValue()));
				value.setObject(val);
			} else if (value.getObject() instanceof Short) {
				double val = value.getMin().shortValue() + (((double) (mouseX - sliderX) / sliderWidth) * (value.getMax().shortValue() - value.getMin().shortValue()));
				value.setObject((short) val);
			} else if (value.getObject() instanceof Byte) {
				double val = value.getMin().byteValue() + (((double) (mouseX - sliderX) / sliderWidth) * (value.getMax().byteValue() - value.getMin().byteValue()));
				value.setObject((byte) val);
			}
		}

		double val = number.doubleValue();
		double percentage = (val - value.getMin().doubleValue()) / diff;

		double generalAni = animation == 1 ? visibilityAnimation : animation;
		RenderUtil.INSTANCE.modifiedAlpha(startColor, (int) (255 * generalAni));
		String displayValue = Math.round(100D * number.doubleValue() * generalAni) / 100D + "";
		Fonts.INSTANCE.arial15.drawString(value.getName().length() > 17 ? value.getName().substring(0, 17) + "..." : value.getName(), x + 30, y + 5, new Color(255, 255, 255, (int) (255 * generalAni)).getRGB());
		Fonts.INSTANCE.arial15.drawString(displayValue, x + width - 30 - Fonts.INSTANCE.arial15.getStringWidth(displayValue), y + 5, new Color(255, 255, 255, (int) (255 * generalAni)).getRGB());
		RenderUtil.INSTANCE.drawRect(sliderX, y + 30, (int) (sliderX + (sliderWidth * percentage) * generalAni), y + 33, RenderUtil.INSTANCE.modifiedAlpha(startColor, (int) (255 * animation)));
		RenderUtil.INSTANCE.drawRect(sliderX + (sliderWidth * percentage) * generalAni - 1, y + 27, sliderX + (sliderWidth * percentage) * generalAni + 1, y + 36, new Color(255, 255, 255, (int) (255 * generalAni)));
		this.height = (int) (40 * visibilityAnimation);

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.hovered && animation >= 0.95 && visibilityAnimation >= 0.95) {
			this.dragging = true;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		this.dragging = false;
	}
}
