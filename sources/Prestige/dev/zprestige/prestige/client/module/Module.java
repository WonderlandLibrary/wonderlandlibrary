package dev.zprestige.prestige.client.module;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.managers.SettingManager;
import dev.zprestige.prestige.client.setting.Setting;
import dev.zprestige.prestige.client.setting.impl.BindSetting;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.util.MC;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Module extends SettingManager implements MC {
    private String name;
    private String description;
    private Category category;
    private ArrayList<Setting<?>> moduleSettings;
    private final BooleanSetting enabled;
    private final BindSetting keybind;

    public Module(String name, Category category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
        moduleSettings = new ArrayList<>();
        enabled = new BooleanSetting("Enabled", false);
        keybind = new BindSetting("Bind", -1).description("Keybind for " + this.name);
        moduleSettings.addAll(Arrays.asList(enabled, keybind));
    }

    public void onEnable() {}

    public void onDisable() {}

    public void toggle() {
        enabled.invokeValue(!this.isEnabled());
        if (isEnabled()) {
            Prestige.Companion.getEventBus().registerListener(this);
            onEnable();
        } else {
            Prestige.Companion.getEventBus().unregisterListener(this);
            onDisable();
        }
    }

    public void clear() {
        name = null;
        description = null;
        List<Setting<?>> list = Stream.concat(getSettings().stream(), settings.stream()).toList();;
        for (Setting<?> setting : list) {
            setting.setName(null);
            setting.description(null);
            setting.invokeValue(null);
        }
    }

    public MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public ArrayList<Setting<?>> getSettings() {
        return settings;
    }

    public ArrayList<Setting<?>> getModuleSettings() {
        return moduleSettings;
    }


    public boolean isEnabled() {
        return enabled.getObject();
    }

    public BooleanSetting getEnabled() {
        return enabled;
    }

    public int getKey() {
        return keybind.getObject();
    }

    public BindSetting getKeybind() {
        return keybind;
    }

    public String method224() {
        return "";
    }

    public float getStringWidthFull() {
        return Prestige.Companion.getFontManager().getFontRenderer().getStringWidth(name + " " + this.method224()) + (!method224().equals("") ? Prestige.Companion.getFontManager().getFontRenderer().getStringWidth(" ") : 0);
    }
}
