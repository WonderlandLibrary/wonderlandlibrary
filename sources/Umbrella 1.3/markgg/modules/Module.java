/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import markgg.Client;
import markgg.events.Event;
import markgg.settings.KeybindSetting;
import markgg.settings.Setting;
import net.minecraft.client.Minecraft;

public class Module {
    public String name;
    public boolean toggled;
    public KeybindSetting keyCode = new KeybindSetting(0);
    public Category category;
    public Minecraft mc = Minecraft.getMinecraft();
    public boolean expanded;
    public int index;
    public List<Setting> settings = new ArrayList<Setting>();

    public Module(String name, int key, Category c) {
        this.name = name;
        this.keyCode.code = key;
        this.category = c;
        this.addSettings(this.keyCode);
    }

    public void addSettings(Setting ... settings) {
        this.settings.addAll(Arrays.asList(settings));
        this.settings.sort(Comparator.comparingInt(s -> s == this.keyCode ? 1 : 0));
    }

    public boolean isEnabled() {
        return this.toggled;
    }

    public int getKey() {
        return this.keyCode.code;
    }

    public List<Setting> ListSettings() {
        ArrayList<Setting> s = new ArrayList<Setting>();
        for (Setting s1 : this.settings) {
            s.add(s1);
        }
        return s;
    }

    public void onEvent(Event e) {
    }

    public void toggle() {
        boolean bl = this.toggled = !this.toggled;
        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public Category getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public static Setting getSettingByName(String name) {
        ArrayList setting = new ArrayList();
        for (Setting s : setting) {
            if (!s.name.equals(name)) continue;
            return s;
        }
        return null;
    }

    public void enableOnStartup() {
        this.toggled = true;
        try {
            this.toggle();
            this.onEnable();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Module getModuleByName1(String moduleName) {
        for (Module m : Client.modules) {
            if (!m.name.trim().equalsIgnoreCase(moduleName) && !m.toString().equalsIgnoreCase(moduleName.trim())) continue;
            return m;
        }
        return null;
    }

    public static enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        RENDER("Render"),
        GHOST("Ghost");

        public String name;
        public int moduleIndex;

        private Category(String name) {
            this.name = name;
        }
    }
}

