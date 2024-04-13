package com.canon.majik.impl.setting;

import com.canon.majik.impl.modules.api.Module;

public class Setting<T>{
    private T value;
    private String name;
    private Module parent;

    public Setting(String name, T value, Module parent){
        this.name = name;
        this.value = value;
        this.parent = parent;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Module getParent() {
        return parent;
    }

    public void setParent(Module parent) {
        this.parent = parent;
    }
}
