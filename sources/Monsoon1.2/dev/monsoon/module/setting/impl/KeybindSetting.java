package dev.monsoon.module.setting.impl;

import dev.monsoon.module.setting.Setting;
import org.lwjgl.input.Keyboard;

import dev.monsoon.module.base.Module;

public class KeybindSetting extends Setting {
	
	public int code;
	public Module parent;
	
	public KeybindSetting(int code,Module parent) {
		this.name = "Keybind";
		this.code = code;
		this.parent = parent;
	}

	public int getKeyCode() {
		return code;
	}

	public void setKeyCode(int code) {
		this.code = code;
	}

	public int getKey() {
		return code;
	}

	public void setKey(int key) {
		this.code = key;
	}
	
	public void setKey2(KeybindSetting key) {
		this.code = key.getKeyCode();
	}

	public String getKeyName() {
		return Keyboard.getKeyName(code);
	}
	
}
