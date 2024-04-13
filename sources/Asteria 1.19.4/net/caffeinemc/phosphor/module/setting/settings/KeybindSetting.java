package net.caffeinemc.phosphor.module.setting.settings;

import imgui.ImGui;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import org.lwjgl.glfw.GLFW;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.api.event.events.KeyPressEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.event.orbit.EventPriority;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.RenderableSetting;
import net.caffeinemc.phosphor.module.setting.Setting;

public class KeybindSetting extends Setting implements RenderableSetting {
	
	public int code;
	private boolean isButtonWasPressed = false;

	public KeybindSetting(String name, int code, Module parent) {
		this.name = name;
		this.code = code;
		this.parent = parent;
		parent.addSettings(this);
	}

	public int getKeyCode() {
		return this.code;
	}

	public void setKeyCode(int code) {
		this.code = code;
	}

	@Override
	public void render() {
		ImGui.pushID(parent.getName()+"/"+this.getName());

		ImGui.text(this.name);

		if (!isButtonWasPressed) {
			isButtonWasPressed = ImGui.button(KeyUtils.getKeyName(getKeyCode()));
		} else {
			ImGui.button("Press key...");
			Phosphor.EVENTBUS.subscribe(this);
		}

		ImGui.popID();
	}

	@EventHandler(priority = EventPriority.LOW)
	private void onKeyPress(KeyPressEvent event) {
		if (event.action != GLFW.GLFW_RELEASE) {
			isButtonWasPressed = false;
			Phosphor.EVENTBUS.unsubscribe(this);

			if (event.key == GLFW.GLFW_KEY_ESCAPE)
				return;

			setKeyCode(event.key == GLFW.GLFW_KEY_DELETE ? 0 : event.key);
		}
	}
}