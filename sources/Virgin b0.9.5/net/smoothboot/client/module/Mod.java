package net.smoothboot.client.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.smoothboot.client.Virginclient;
import net.smoothboot.client.events.EventEmitter;
import net.smoothboot.client.module.settings.KeyBindSetting;
import net.smoothboot.client.module.settings.Setting;

import java.util.ArrayList;
import java.util.List;

public abstract class Mod {
    private String name;
    private String displayname;
    private String description;
    private Category category;
    private int key;
    private boolean enabled;
    private List<Setting> settings = new ArrayList<>();

    public static MinecraftClient mc = MinecraftClient.getInstance();
    public void toggle() {
        this.enabled = !this.enabled;

        if (enabled) {
            onEnable();
        }
        else onDisable();
    }

    private final EventEmitter emitter = Virginclient.getInstance().emitter;

    public void addsetting(Setting setting) {
        settings.add(setting);
    }

    public void addsettings(Setting... settings) {
        for (Setting setting : settings) {
            addsetting(setting);
        }
    }


    public void onWorldRender(MatrixStack matrices) {
    }

    public void onEnable() {

    }
    public void onDisable() {

    }

    public void onTick() {

    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) onEnable();
        else onDisable();
    }

    public static boolean nullCheck(){
        return MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().player == null;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Mod(String name, String description, Category category) {
        this.name = name;
        this.displayname = name;
        this.description = description;
        this.category = category;
        addsetting(new KeyBindSetting("Key", 0));
    }

    public List<Setting> getSettings() {
        return settings;
    }

    public enum Category {
        Combat("Combat"),
        Player("Player"),
        Render("Render"),
        Misc("Misc"),
        Client("Client");

        public String name;

        private Category(String name) {
            this.name = name;
        }
    }
}
