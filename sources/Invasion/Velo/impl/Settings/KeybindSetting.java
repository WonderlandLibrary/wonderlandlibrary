package Velo.impl.Settings;

import Velo.api.setting.Setting;

public class KeybindSetting extends Setting {
	
	public int code;
public boolean ClickGuiSelected = false;
	
	public KeybindSetting(int code) {
		this.name = "Bind";
		this.code = code;
	}
	
	public int getKeyCode() {
		return code;
	}
	
	public void setKeyCode(int code) {
		this.code = code;
	}
}
