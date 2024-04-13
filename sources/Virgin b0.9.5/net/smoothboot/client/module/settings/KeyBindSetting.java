package net.smoothboot.client.module.settings;

import net.minecraft.client.MinecraftClient;

public class KeyBindSetting extends Setting {

    private int key;
    private boolean enabled;
    protected MinecraftClient mc = MinecraftClient.getInstance();
    public KeyBindSetting(String name, int defaultKey) {
        super(name);
        this.key = defaultKey;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }
}