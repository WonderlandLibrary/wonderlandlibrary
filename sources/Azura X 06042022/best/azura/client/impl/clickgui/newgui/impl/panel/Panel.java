package best.azura.client.impl.clickgui.newgui.impl.panel;

import best.azura.client.impl.Client;
import best.azura.client.impl.clickgui.newgui.impl.elements.ModuleElement;
import best.azura.client.impl.ui.gui.ScrollRegion;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;

import java.util.ArrayList;

public class Panel {

	public int x, y, x2, y2, width, height;
	public ArrayList<PanelElement> elements = new ArrayList<>();
	public double animation = 0, extendAnimation;
	public Category parent;
	public boolean extended = false, hovered = false, dragged = false;
	public long start;
	public ScrollRegion region;

	public Panel(int x, int y, int width, int height, Category category) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.parent = category;
		int count = 0;
		this.extended = true;
		this.start = System.currentTimeMillis();
		this.extendAnimation = 0;
		region = new ScrollRegion(x, y, width, height);
		if (category == null) return;
		for (Module module : Client.INSTANCE.getModuleManager().getModules(category)) {
			elements.add(new ModuleElement(this, x, y + height + count * 30, width, 30, module));
			count++;
		}
	}

	public void render(int mouseX, int mouseY) {
	}

	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (this.hovered) {
			switch (button) {
				case 0:
					dragged = true;
					x2 = x - mouseX;
					y2 = y - mouseY;
					break;
				case 1:
					if (this.extended && extendAnimation == 1) {
						this.start = System.currentTimeMillis();
						this.extended = false;
					} else if (extendAnimation == 0 && !this.extended) {
						this.extended = true;
						this.start = System.currentTimeMillis();
					}
					break;
			}
			return;
		}

		for (PanelElement element : elements) {
			element.mouseClicked(mouseX, mouseY, button);
		}

	}

	public void mouseReleased(int mouseX, int mouseY) {
		dragged = false;

		for (PanelElement element : elements) {
			element.mouseReleased(mouseX, mouseY);
		}

	}

	public void handleInput() {
		region.handleMouseInput();
	}

	public void onTick() {
		region.onTick();
	}

	public void keyTyped(char typed, int keyCode) {

		for (PanelElement element : elements) {
			element.keyTyped(typed, keyCode);
		}

	}

}
