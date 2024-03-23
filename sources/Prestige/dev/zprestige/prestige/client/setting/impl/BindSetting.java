package dev.zprestige.prestige.client.setting.impl;

import dev.zprestige.prestige.client.setting.Setting;

import java.util.function.Predicate;

public class BindSetting extends Setting<Integer> {

    public boolean listening;

    public BindSetting(String name, Integer value) {
        super(name, value);
    }

    @Override
    public BindSetting invokeVisibility(Predicate<Integer> visible) {
        super.invokeVisibility(visible);
        return this;
    }

    @Override
    public BindSetting description(String description) {
        super.description(description);
        return this;
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean listening) {
        this.listening = listening;
    }
}