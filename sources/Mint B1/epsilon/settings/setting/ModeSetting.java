package epsilon.settings.setting;

import java.util.Arrays;
import java.util.List;

import epsilon.Epsilon;
import epsilon.settings.Setting;

public class ModeSetting extends Setting {
	
	public int index;
	public List<String> modes;
	public boolean chosen;
	
	public ModeSetting(String name, String defaultMode, String... modes) {
		this.name = name;
		this.modes = Arrays.asList(modes);
		index = this.modes.indexOf(defaultMode);
		chosen = false;
	}
	
	public String getMode() {
		return modes.get(index);
	}
	
	public void setMode(String m) {
		while(!is(m)) {
			cycle(true);
		}
	}
	
	public boolean is(String mode) {
		return index == modes.indexOf(mode);
	}
	
	public boolean isChosen() {
		return chosen;
	}
	
	public void cycle(boolean direction) {
		if(direction) {
			if(index < modes.size() - 1) {
				index++;
			}else {
				index = 0;
			}
		}else {
			if(index-1==-1) 
				index = modes.size()-1;
			else index--;
		
		}
	}
	
}
