package io.github.raze.modules.system;

import io.github.raze.Raze;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.utilities.system.Methods;

public abstract class AbstractModule implements Methods {

    public String name;
    public String description;
    public ModuleCategory category;

    public int keyCode;

    public boolean enabled;
    public boolean hidden;

    public AbstractModule(String name, String description, ModuleCategory category, int keyCode) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keyCode = keyCode;
    }

    public AbstractModule(String name, String description, ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void onEnable() { /* */ }
    public void onDisable() { /* */ }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (isEnabled()) {
            if(mc.theWorld != null || mc.thePlayer != null) {
                Raze.INSTANCE.managerRegistry.eventManager.subscribe(this);
                onEnable();
            }
        } else {
            if(mc.theWorld != null || mc.thePlayer != null) {
                onDisable();
                Raze.INSTANCE.managerRegistry.eventManager.unsubscribe(this);
            }
        }
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void toggle() {
        setEnabled(!isEnabled());
    }
}
