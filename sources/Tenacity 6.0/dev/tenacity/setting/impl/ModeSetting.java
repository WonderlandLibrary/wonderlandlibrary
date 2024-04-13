package dev.tenacity.setting.impl;

import dev.tenacity.setting.Setting;

import java.util.Arrays;
import java.util.List;

public final class ModeSetting extends Setting<String> {

    private final List<String> modeList;

    private String currentMode;
    private int modeIndex;

    public ModeSetting(final String name, final String... modes) {
        this.name = name;
        this.modeList = Arrays.asList(modes);
        this.modeIndex = this.modeList.indexOf(modes[0]);
        this.currentMode = modes[0];
    }

    public boolean isMode(final String mode) {
        return currentMode.equalsIgnoreCase(mode);
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public List<String> getModeList() {
        return modeList;
    }

    public int getModeIndex() {
        return modeIndex;
    }

    public void cycleForward() {
        modeIndex++;
        if(modeIndex > modeList.size() - 1) modeIndex = 0;
        currentMode = modeList.get(modeIndex);
    }

    public void setCurrentMode(final String currentMode) {
        this.currentMode = currentMode;
    }

    public void setCurrentMode(final int modeIndex) {
        this.currentMode = modeList.get(modeIndex);
    }

    public void setModeIndex(final int modeIndex) {
        this.modeIndex = modeIndex;
    }

    @Override
    public String getConfigValue() {
        return currentMode;
    }
}
