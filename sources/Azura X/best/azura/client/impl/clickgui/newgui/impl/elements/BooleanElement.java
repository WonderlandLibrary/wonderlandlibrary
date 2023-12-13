package best.azura.client.impl.clickgui.newgui.impl.elements;

import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.BooleanValue;

public class BooleanElement extends SettingElement {

	private final BooleanValue value;
	private final double clickAnimation = 0;
	private long start;

	public BooleanElement(int x, int y, int width, int height, BooleanValue value) {
		super(x, y, width, height, value);
		this.value = value;
		this.start = System.currentTimeMillis();
	}

	@Override
	public void render(int mouseX, int mouseY) {
		super.render(mouseX, mouseY);
		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.hovered && this.animation >= 0.99) {
			this.value.setObject(!this.value.getObject());
			this.start = System.currentTimeMillis();
		}
	}

	public BooleanValue getValue() {
		return value;
	}
}
