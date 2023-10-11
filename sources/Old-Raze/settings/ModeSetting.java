package markgg.settings;

import java.util.Arrays;
import java.util.List;

import markgg.RazeClient;
import markgg.modules.Module;

public class ModeSetting extends Setting {

	public int index;
	public List<String> modes;
	private Module parent;

	public ModeSetting(String name, Module parent, String defaultMode, String... modes) {
		this.name = name;
		this.modes = Arrays.asList(modes);
		this.parent = parent;
		index = this.modes.indexOf(defaultMode);
		parent.addSettings(this);
	}

	public String getMode() {
		if (index >= 0 && index < modes.size()) {
	        return modes.get(index);
	    } else {
	        return "invalid mode";
	    }
	}

	public void setMode(String name) {
		modes.set(index, name);
	}

	public boolean is(String mode) {
		return index == modes.indexOf(mode);
	}

	public void cycle() {
		if(index < modes.size() - 1)
			index++;
		else
			index = 0;
	}
	
	public void invertCycle() {
		if(this.index <= 0)
			this.index = this.modes.size() - 1;
		else
			this.index--;
	}

	public Module getParentMod(){
		return parent;
	}
	
	

}
