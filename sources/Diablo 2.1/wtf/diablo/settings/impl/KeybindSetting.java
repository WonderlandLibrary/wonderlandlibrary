package wtf.diablo.settings.impl;

import org.lwjgl.input.Keyboard;
import wtf.diablo.module.Module;
import wtf.diablo.settings.Setting;

public class KeybindSetting extends Setting {
    public Module parent;

    public KeybindSetting(Module parent) {
        this.parent = parent;
        this.name = "Keybind Setting";
    }

    public int getKeybind() {
        return this.parent.getKey();
    }

    public void setKeybind(int key) {
        this.parent.setKey(key);
    }

    public String getKeybindString() {
        return Keyboard.getKeyName(getKeybind());
    }

}
