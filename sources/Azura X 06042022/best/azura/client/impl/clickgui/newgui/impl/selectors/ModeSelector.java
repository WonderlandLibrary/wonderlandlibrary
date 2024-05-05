package best.azura.client.impl.clickgui.newgui.impl.selectors;

import best.azura.client.impl.clickgui.azura.impl.setting.ModeElement;

public class ModeSelector {

	public int x, y, width, height;
	public final ModeElement parent;
	public double animation;
	public long start;
	public boolean hovered = false;

	public ModeSelector(int x, int y, int width, ModeElement element) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 0;
		this.parent = element;
		this.animation = 0;
		this.start = System.currentTimeMillis();
	}

	public void render(int mouseX, int mouseY) {

	}

	public void mouseClicked() {
		if (!this.hovered) {
			animation = 0;
		}
	}

}
