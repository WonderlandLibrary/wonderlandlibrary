package best.azura.client.impl.clickgui.newgui.impl.elements;

import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.clickgui.newgui.impl.panel.Panel;
import best.azura.client.impl.clickgui.newgui.impl.panel.PanelElement;
import best.azura.client.api.module.Module;
import best.azura.client.impl.value.*;
import org.lwjgl.input.Keyboard;

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
