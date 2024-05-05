package club.spectrum.modules;

import club.spectrum.modules.Category;
import net.minecraft.client.Minecraft;

public class Module {

    public static Minecraft mc = Minecraft.getMinecraft();

    public String name;
    public String displayName;
    public Category category;

    public int keyBind;

    public boolean toggled;

    public Module(String name, String displayName, int keyBind, Category category) {
        this.name = name;
        this.displayName = name;
        this.keyBind = keyBind;
        this.category = category;
    }

    public int getKey() {
        return keyBind;
    }

    public void setKey(int key) {
        this.keyBind = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getKeyBind() {
        return keyBind;
    }

    public void setKeyBind(int keyBind) {
        this.keyBind = keyBind;
    }

    public boolean isEnabled() {
        return toggled;
    }

    public void setEnable(boolean toggled) {
        this.toggled = toggled;
    }

    public void enable() {
        toggled = !toggled;
        if (toggled) {
            onEnable();
        }
    }

    public void disable() {
        toggled = !toggled;
        if (toggled) {
            onDisable();
        }
    }

    public void toggle() {
        if (toggled) {
            toggled = false;
            onDisable();
        } else {
            toggled = true;
            onEnable();
        }
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onRender() {}
    public void onUpdate() {}

}
