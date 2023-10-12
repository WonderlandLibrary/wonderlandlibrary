package wtf.diablo.settings.impl;

import wtf.diablo.settings.Setting;

import java.util.HashMap;

public class ModeSetting extends Setting {
    public int index;
    public String[] options;
    public HashMap<Setting, String> children = new HashMap<>(); //Eviates Favorite!

    public ModeSetting(String name, String... options) {
        this.options = options;
        this.name = name;
        this.index = 0;
    }

    public String[] getModes() {
        return options;
    }
    public int getIndex(){
        return index;
    }
    public Object getObjectValue() {
        return getIndex();
    }
    public String getMode() {
        return options[index];
    }
    public void setMode(int index){
        this.index = index;
    }

    public void addChild(Setting child, String mode) {
        child.setParent(this, mode);
    }

    public void addChild(String mode, Setting... children) {
        for(Setting child : children)
            child.setParent(this, mode);
    }
}
