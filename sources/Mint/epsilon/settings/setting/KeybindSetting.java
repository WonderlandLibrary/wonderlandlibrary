package epsilon.settings.setting;

import epsilon.settings.Setting;

public class KeybindSetting extends Setting {

	public int code;
	
	public KeybindSetting(int code) {
		this.name = "Keybind";
		this.code = code;
	}

	public int getKeycode() {
		return code;
	}

	public void setKeycode(int code) {
		this.code = code;
	}
	
}
