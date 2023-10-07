package best.azura.client.impl.clickgui.azura.impl.panel;

import best.azura.client.api.module.Module;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.clickgui.azura.Panel;
import best.azura.client.impl.clickgui.azura.PanelElement;
import best.azura.client.impl.clickgui.azura.SettingElement;
import best.azura.client.impl.clickgui.azura.impl.setting.*;
import best.azura.client.impl.module.impl.render.ClickGUI;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.value.*;
import best.azura.client.util.render.RenderUtil;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class ModuleElement extends PanelElement {

	public final Module parent;
	public final Panel panel;
	public boolean waiting = false;
	public final ArrayList<SettingElement> settings = new ArrayList<>();

	public boolean extended = false;
	public long start = 0;

	public ModuleElement(Panel panel, int x, int y, int width, int height, Module parent) {
		super(x, y, width, height);
		this.panel = panel;
		this.parent = parent;
		int calcHeight = 0;
		if (Client.INSTANCE.getValueManager().getValues(parent) == null) return;
		for (Value<?> value : Client.INSTANCE.getValueManager().getValues(parent)) {
			int elementHeight = 0;
			if (value instanceof BooleanValue) {
				settings.add(new BooleanElement(x, y + height + calcHeight, width, 30, (BooleanValue) value));
				elementHeight = 30;
			} else if (value instanceof NumberValue) {
				settings.add(new NumberElement(x, y + height + calcHeight, width, 50, (NumberValue<?>) value));
				elementHeight = 50;
			} else if (value instanceof ModeValue) {
				settings.add(new ModeElement(x, y + height + calcHeight, width, 30, (ModeValue) value));
				elementHeight = 30;
			} else if (value instanceof ComboValue) {
				settings.add(new SelectionElement(x, y + height + calcHeight, width, 30, (ComboValue) value));
				elementHeight = 30;
			} else if (value instanceof ColorValue) {
				settings.add(new ColorElement(x, y + height + calcHeight, width, 30, (ColorValue) value));
				elementHeight = 30;
			} else if (value instanceof StringValue) {
				settings.add(new StringElement(x, y + height + calcHeight, width, 30, (StringValue) value));
				elementHeight = 30;
			} else if (value instanceof ClickValue) {
				settings.add(new ClickElement(x, y + height + calcHeight, width, 30, (ClickValue) value));
				elementHeight = 30;
			}
			calcHeight += elementHeight;
		}
	}

	@Override
	public void render(int mouseX, int mouseY) {

		double extendAnimation;
		if (!extended) {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			extendAnimation = -1 * Math.pow(anim - 1, 6) + 1;
			extendAnimation = 1 - extendAnimation;
		} else {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			extendAnimation = -1 * Math.pow(anim - 1, 6) + 1;
		}

		int calcHeight = ClickGUI.theme.getObject().equals("Azura X") ? 10 : 0;
		for (SettingElement element : settings) {
			if (element.visibilityAnimation == 0) continue;
			calcHeight += element.height;
		}

		this.hovered = RenderUtil.INSTANCE.isHovered(x, y, width, 30, mouseX, mouseY) && this.animation == 1;
		if (ClickGUI.theme.getObject().equals("Azura X")) {
			RenderUtil.INSTANCE.drawRoundedRect(x + 15, y + 15 - 5 + (ClickGUI.theme.getObject().equals("Azura X") ? 10 * extendAnimation : 0), 10, 10, 3,
					parent.isEnabled() ? RenderUtil.INSTANCE.modifiedAlpha(ClickGUI.color.getObject().getColor(),
							(int) (255 * animation)) : new Color(30, 30, 30, (int) (255 * animation)));
		}

		if ((this.hovered || extendAnimation >= 0.05) && ClickGUI.theme.getObject().equals("Azura X")) {
			RenderUtil.INSTANCE.drawRoundedRect(x + 10 * extendAnimation, y + 5 * extendAnimation, width - 20 * extendAnimation, 30 + (calcHeight - 10) * extendAnimation, 10 * extendAnimation, new Color(70, 70, 70, (int) (50 * animation)));
		} else if ((this.hovered || extendAnimation >= 0.05) && ClickGUI.theme.getObject().equals("Material")) {
			RenderUtil.INSTANCE.drawRoundedRect(x, y, width, 30 + calcHeight * extendAnimation, 0, new Color(15, 14, 16, (int) (255 * animation)));
		}
		if (parent.isEnabled() && ClickGUI.theme.getObject().equals("Material")) {
			RenderUtil.INSTANCE.drawRoundedRect(x, y, width, 30, 0, RenderUtil.INSTANCE.modifiedAlpha(ClickGUI.color.getObject().getColor(), (int) (255 * animation)));
		}

		this.height = (int) (30 + calcHeight * extendAnimation);
		String text = parent.getName() + (waiting ? " [...]" : "");
		if (ClickGUI.theme.getObject().equals("Azura X")) {
			Fonts.INSTANCE.arial15bold.drawString(text, x + 33, y + 15 - Fonts.INSTANCE.arial15bold.FONT_HEIGHT / 2.0 + 5 * extendAnimation,
					new Color(255, 255, 255, (int) (255 * animation)).getRGB());
		} else {
			Fonts.INSTANCE.arial15bold.drawString(text, x + 10, y + 15 - Fonts.INSTANCE.arial15bold.FONT_HEIGHT / 2.0,
					new Color(255, 255, 255, (int) (255 * animation)).getRGB());
		}

		if (extendAnimation != 0) {
			calcHeight = 30 + (ClickGUI.theme.getObject().equals("Azura X") ? 5 : 0);
			for (SettingElement element : settings) {
				element.checkVisibility();
				if (element.visibilityAnimation == 0 && !element.visible) continue;
				element.x = x;
				element.animation = animation == 1 ? extendAnimation : animation;
				element.y = (int) (y + calcHeight * extendAnimation);
				if (element.y - panel.y > 800) continue;
				element.width = width;
				element.render(mouseX, mouseY);
				calcHeight += element.height;
			}
		}

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {

		waiting = false;
		if (this.hovered) {
			switch (button) {
				case 0:
					parent.setEnabled(!parent.isEnabled());
					break;
				case 1:
					if (settings.isEmpty()) return;
					extended = !extended;
					start = System.currentTimeMillis();
					break;
				case 2:
					waiting = true;
					break;
			}
		}

		if (this.extended) {
			for (SettingElement element : settings) {
				element.mouseClicked(mouseX, mouseY, button);
			}
		}

	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {

		if (this.extended) {
			for (SettingElement element : settings) {
				element.mouseReleased(mouseX, mouseY);
			}
		}

	}

	@Override
	public void keyTyped(char typed, int keyCode) {
		if (waiting) {
			if (keyCode == Keyboard.KEY_ESCAPE) {
				parent.setKeyBind(0);
			} else parent.setKeyBind(keyCode);
			waiting = false;
		} else {
			for (SettingElement element : settings) {
				element.keyTyped(typed, keyCode);
			}
		}
	}
}
