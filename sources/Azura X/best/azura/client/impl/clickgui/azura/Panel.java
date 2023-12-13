package best.azura.client.impl.clickgui.azura;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.impl.Client;
import best.azura.client.impl.clickgui.azura.impl.panel.ModuleElement;
import best.azura.client.impl.module.impl.render.ClickGUI;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.gui.ScrollRegion;
import best.azura.client.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
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

		if (!extended) {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			extendAnimation = -1 * Math.pow(anim - 1, 6) + 1;
			extendAnimation = 1 - extendAnimation;
		} else {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			extendAnimation = -1 * Math.pow(anim - 1, 6) + 1;
		}

		int calcHeight = height + 5;
		int maxHeight = 805;
		for (PanelElement element : elements) {
			calcHeight += element.height;
		}

		if (calcHeight > maxHeight) {
			region.offset = maxHeight - calcHeight;
		} else region.offset = 0;

		region.x = x;
		region.y = y + height;
		region.width = width;
		region.height = Math.min(calcHeight, maxHeight) - height;

		if (dragged) {
			x = x2 + mouseX;
			y = y2 + mouseY;
		}

		int elementHeight = Math.min(calcHeight, maxHeight);
		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, height, mouseX, mouseY);
		Color backgroundColor = ClickGUI.theme.getObject().equals("Azura X") ? new Color(0, 0, 0, (int) (150 * animation)) : new Color(21, 20, 22, (int) (255 * animation));
		Color backgroundColor2 = new Color(10, 9, 12, (int) (255 * animation));
		RenderUtil.INSTANCE.drawRoundedRect(x, y, width, this.extendAnimation != 0 ? height + (elementHeight - height) * extendAnimation : height, ClickGUI.theme.getObject().equals("Material") ? 0 : 5, backgroundColor);
		if (!ClickGUI.theme.getObject().equals("Azura X")) {
			RenderUtil.INSTANCE.drawRect(x, y, x + width, y + height, backgroundColor2);
		}
		if (parent != null) {
			String text = parent.toString().charAt(0) + parent.toString().substring(1).toLowerCase();
			Fonts.INSTANCE.arial15bold.drawString(text, x + width / 2.0 - Fonts.INSTANCE.arial15bold.getStringWidth(text) / 2.0, y + 11, new Color(255, 255, 255, (int) (255 * animation)).getRGB());
		}
		GL11.glPushMatrix();
		region.prepare();
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		region.render(mouseX / sr.getScaleFactor(), mouseY / sr.getScaleFactor());
		if (region.offset == 0) region.mouse = 0;

		if (this.extendAnimation != 0) {
			calcHeight = height + region.mouse;
			for (PanelElement element : elements) {
				element.animation = extendAnimation == 1 ? animation : extendAnimation;
				element.x = x;
				element.y = (int) (y + calcHeight * extendAnimation);
				element.width = width;
				if (calcHeight > maxHeight) continue;
				element.render(mouseX, mouseY);
				calcHeight += element.height;
			}
		}

		region.end();
		GL11.glPopMatrix();
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
