package wtf.dawn.module;

import wtf.dawn.event.Event;
import net.minecraft.client.Minecraft;

import java.util.Objects;

public class Module {
    private final String name = getClass().getAnnotation(ModuleInfo.class).getName();
    private final Category category = getClass().getAnnotation(ModuleInfo.class).getCategory();
    private final String description = getClass().getAnnotation(ModuleInfo.class).getDescription();
    private String displayName;

    private String suffix;
    private int keyCode;
    private boolean enabled;

    protected static Minecraft mc;

    public Module() {
        this.mc = Minecraft.getMinecraft();

    }

    public void onEnable() {}
    public void onDisable() {}
    public void onEvent(Event<Event> e) {}

    public void toggle() {
        setEnabled(!enabled);
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getSuffix() {
        return suffix != null ? " " + suffix : "";
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

        if(enabled)
            onEnable();
        else
            onDisable();
    }
}

