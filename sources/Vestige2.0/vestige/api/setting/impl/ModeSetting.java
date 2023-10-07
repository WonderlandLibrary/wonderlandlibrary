package vestige.api.setting.impl;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vestige.api.module.Module;
import vestige.api.setting.Setting;

public class ModeSetting extends Setting {
	
	@Getter
	@Setter
    private int index;
	
    private List<String> modes;

    public ModeSetting(String name, Module parent, String defaultMode, String... modes) {
        super(name, parent);
        this.modes = Arrays.asList(modes);
        index = this.modes.indexOf(defaultMode);
    }

    public String getMode() {
        return modes.get(index);
    }

    public boolean is(String mode) {
        return index == modes.indexOf(mode);
    }

    public void increment() {
        if(index < modes.size() - 1) {
            index++;
        } else {
            index = 0;
        }
    }

    public void decrement() {
        if(index > 0) {
            index--;
        } else {
            index = modes.size() - 1;
        }
    }

    public void setMode(String mode) {
        boolean found = false;
        for (String s : modes) {
            if (s.equals(mode)) {
                found = true;
            }
        }

        if (found) {
            this.index = this.modes.indexOf(mode);
        }
    }

}
