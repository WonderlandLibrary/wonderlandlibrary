package Velo.impl.Settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import Velo.api.setting.Setting;

public class BooleanSetting extends Setting {
	
	@Expose
    @SerializedName("setting")
	public boolean enabled;

	public BooleanSetting(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public void toggle() {
		enabled = !enabled;
	}
	
}
