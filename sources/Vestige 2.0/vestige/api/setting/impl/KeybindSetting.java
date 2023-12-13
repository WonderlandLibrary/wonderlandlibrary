package vestige.api.setting.impl;

import lombok.Getter;
import lombok.Setter;
import vestige.api.module.Module;
import vestige.api.setting.Setting;

@Getter
@Setter
public class KeybindSetting extends Setting {

    private int key;
    private boolean focused;
    
    public KeybindSetting(String name, Module parent, int key) {
        super(name, parent);
        this.key = key;
    }
    
    @Override
    public String getDisplayName() {
    	return focused ? "Waiting" : name;
    }

}
