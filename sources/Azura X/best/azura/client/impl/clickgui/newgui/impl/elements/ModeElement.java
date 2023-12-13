package best.azura.client.impl.clickgui.newgui.impl.elements;

import best.azura.client.util.render.RenderUtil;
import best.azura.client.impl.value.ModeValue;

public class ModeElement extends SettingElement {

	private final ModeValue value;
	private final boolean extended = false;
	private final double clickAnimation = 0;
	private final double switchAnimation = 0;
	private long start, switchStart;
	private final String before = "";

	public ModeElement(int x, int y, int width, int height, ModeValue value) {
		super(x, y, width, height, value);
		this.value = value;
	}

	@Override
	public void render(int mouseX, int mouseY) {
		super.render(mouseX, mouseY);
		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		super.mouseClicked(mouseX, mouseY, button);
	}

	public ModeValue getValue() {
		return value;
	}
}
