package best.azura.client.impl.ui.customhud;

import best.azura.client.api.ui.customhud.Element;
import best.azura.client.api.value.Value;
import best.azura.client.impl.clickgui.azura.SettingElement;
import best.azura.client.impl.clickgui.azura.impl.setting.*;
import best.azura.client.impl.ui.gui.ScrollRegion;
import best.azura.client.impl.ui.gui.impl.Window;
import best.azura.client.impl.value.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;

public class SettingsWindow extends Window {

	private final ArrayList<SettingElement> elements = new ArrayList<>();
	private final ScrollRegion region;

	public SettingsWindow(String text, int width, int height, Element element) {
		super(text, width, height);
		region = new ScrollRegion(x, y, width, height);
		region.mouse = 0;
		region.offset = 0;
		int calcHeight = 30;
		if (element.getValues() == null) return;
		for (Value<?> value : element.getValues()) {
			int elementHeight = 0;
			if (value instanceof BooleanValue) {
				elements.add(new BooleanElement(x, y + height + calcHeight, width, 30, (BooleanValue) value));
				elementHeight = 30;
			} else if (value instanceof NumberValue) {
				elements.add(new NumberElement(x, y + height + calcHeight, width, 50, (NumberValue<?>) value));
				elementHeight = 50;
			} else if (value instanceof ModeValue) {
				elements.add(new ModeElement(x, y + height + calcHeight, width, 30, (ModeValue) value));
				elementHeight = 30;
			} else if (value instanceof ComboValue) {
				elements.add(new SelectionElement(x, y + height + calcHeight, width, 30, (ComboValue) value));
				elementHeight = 30;
			} else if (value instanceof ColorValue) {
				elements.add(new ColorElement(x, y + height + calcHeight, width, 30, (ColorValue) value));
				elementHeight = 30;
			} else if (value instanceof StringValue) {
				elements.add(new StringElement(x, y + height + calcHeight, width, 30, (StringValue) value));
				elementHeight = 30;
			} else if (value instanceof ClickValue) {
				elements.add(new ClickElement(x, y + height + calcHeight, width, 30, (ClickValue) value));
				elementHeight = 30;
			}
			calcHeight += elementHeight;
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		GlStateManager.pushMatrix();
		super.draw(mouseX, mouseY);

		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		mouseX *= sr.getScaleFactor();
		mouseY *= sr.getScaleFactor();
		int calcHeight = 50;
		for (SettingElement element : elements) {
			element.checkVisibility();
			if (element.visibilityAnimation == 0 && !element.visible) continue;
			element.animation = animation;
			element.x = x;
			element.y = y + calcHeight;
			element.render(mouseX, mouseY);
			calcHeight += element.height;
		}

		GlStateManager.popMatrix();

	}

	@Override
	public void keyTyped(char typed, int keyCode) {
		super.keyTyped(typed, keyCode);

		for (SettingElement element : elements) {
			element.keyTyped(typed, keyCode);
		}

	}

	public void onTick() {
		region.onTick();
	}

	public void onMouse() {
		region.handleMouseInput();
	}

	public void mouseClicked(int mouseX, int mouseY, int button) {
		for (SettingElement element : elements) {
			element.mouseClicked(mouseX, mouseY, button);
		}
	}

	public void mouseReleased(int mouseX, int mouseY) {
		for (SettingElement element : elements) {
			element.mouseReleased(mouseX, mouseY);
		}
	}

}
