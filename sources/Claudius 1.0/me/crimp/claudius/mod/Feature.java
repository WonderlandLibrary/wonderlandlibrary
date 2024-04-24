package me.crimp.claudius.mod;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.gui.ClickGui;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.managers.TextManager;
import me.crimp.claudius.utils.Util;

import java.util.ArrayList;
import java.util.List;

public abstract class Feature implements Util {
    public List<Setting<?>> settings = new ArrayList<>();
    public TextManager renderer = claudius.textManager;
    private String name;

    public Feature() {
    }

    public Feature(String name) {
        this.name = name;
    }

    public static boolean nullCheck() {
        return Feature.mc.player == null;
    }

    public static boolean fullNullCheck() {
        return Feature.mc.player == null || Feature.mc.world == null;
    }

    public String getName() {
        return this.name;
    }

    public List<Setting<?>> getSettings() {
        return this.settings;
    }

    public boolean isEnabled() {
        if (this instanceof Module) {
            return ((Module) this).isOn();
        }
        return false;
    }

    public <T> Setting<T> register(Setting<T> setting) {
        setting.setFeature(this);
        this.settings.add(setting);
        if (this instanceof Module && Feature.mc.currentScreen instanceof ClickGui) {
            ClickGui.getInstance().updateModule((Module) this);
        }
        return setting;
    }

    public Setting<?> getSettingByName(String name) {
        for (Setting<?> setting : this.settings) {
            if (!setting.getName().equalsIgnoreCase(name)) continue;
            return setting;
        }
        return null;
    }

    public static int amountPerCategory(Module.Category category) {
        List<Module> categoryModules = new ArrayList<>();
        for (Module module : claudius.INSTANCE.moduleManager.modules) {
            if (module.getCategory().equals(category)) {
                categoryModules.add(module);
            }
        }
        return categoryModules.size();
    }

    public static Module.Category getCategoryFromString(String id) {
        Module.Category finalCategory = null;
        for (Module.Category category : Module.Category.values()) {
            if (category.toString().equalsIgnoreCase(id)) {
                finalCategory = category;
                break;
            }
        }
        return finalCategory;
    }

    public void reset() {
        for (Setting setting : this.settings) setting.setValue(setting.getDefaultValue());
    }

    public void clearSettings() {
        this.settings = new ArrayList<>();
    }
}

