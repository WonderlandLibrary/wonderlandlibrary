package ravenweave.client.module;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.EventBus;
import org.lwjgl.input.Keyboard;
import ravenweave.client.clickgui.raven.components.ModuleComponent;
import ravenweave.client.module.modules.client.ClickGuiModule;
import ravenweave.client.module.setting.Setting;
import ravenweave.client.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Module {
    protected ArrayList<Setting> settings;
    private final String moduleName;
    private final ModuleCategory moduleCategory;
    protected boolean hasBind = true, showInHud = true, clientConfig, enabled;
    protected boolean defaultEnabled = enabled;
    protected int keycode;
    protected int defualtKeyCode = keycode;

    protected ModuleComponent component;

    protected static Minecraft mc;
    private boolean isToggled;

    protected boolean registered;

    public void guiUpdate() {

    }

    public Module(String name, ModuleCategory moduleCategory) {
        this.moduleName = name;
        this.moduleCategory = moduleCategory;
        this.settings = new ArrayList<>();
        mc = Minecraft.getMinecraft();
    }

    protected <E extends Module> E withKeycode(int i) {
        this.keycode = i;
        this.defualtKeyCode = i;
        return (E) this;
    }

    protected <E extends Module> E withEnabled(boolean i) {
        this.enabled = i;
        this.defaultEnabled = i;
        try {
            setToggled(i);
        } catch (Exception ignore) {
        }
        return (E) this;
    }

    public <E extends Module> E withDescription(String i) {
        return (E) this;
    }

    public JsonObject getConfigAsJson() {
        JsonObject settings = new JsonObject();

        for (Setting setting : this.settings)
            if (setting != null) {
                JsonObject settingData = setting.getConfigAsJson();
                settings.add(setting.settingName, settingData);
            }

        JsonObject data = new JsonObject();
        data.addProperty("enabled", enabled);
        if (hasBind)
            data.addProperty("keycode", keycode);
        data.addProperty("showInHud", showInHud);
        data.add("settings", settings);

        return data;
    }

    public void applyConfigFromJson(JsonObject data) {
        try {
            if (hasBind)
                this.keycode = data.get("keycode").getAsInt();
            setToggled(data.get("enabled").getAsBoolean());
            JsonObject settingsData = data.get("settings").getAsJsonObject();
            for (Setting setting : getSettings())
                if (settingsData.has(setting.getName()))
                    setting.applyConfigFromJson(settingsData.get(setting.getName()).getAsJsonObject());
            this.showInHud = data.get("showInHud").getAsBoolean();
        } catch (NullPointerException ignored) {

        }
        postApplyConfig();
    }

    public void postApplyConfig() {

    }

    public void keybind() {
        if ((this.keycode != 0) && this.canBeEnabled())
            if (!this.isToggled && Keyboard.isKeyDown(this.keycode)) {
                this.toggle();
                this.isToggled = true;
            } else if (!Keyboard.isKeyDown(this.keycode))
                this.isToggled = false;
    }

    public boolean canBeEnabled() {
        return true;
    }

    public boolean showInHud() {
        return showInHud;
    }

    public void enable() {
        if(!canBeEnabled())
            return;
        this.enabled = true;
        this.onEnable();
        if (enabled && !registered) {
            EventBus.subscribe(this);
            registered = true;
        }

        if (this.moduleCategory == ModuleCategory.category) return;
        if (!ClickGuiModule.notifications.isToggled()) return;
        Utils.Player.sendMessageToSelf(this.moduleName + " has been &aenabled");
    }

    public void disable() {
        if(!canBeEnabled())
            return;
        this.enabled = false;
        if (registered) {
            EventBus.unsubscribe(this);
            registered = false;
        }
        this.onDisable();

        // Skip categories
        if (this.moduleCategory == ModuleCategory.category) return;

        // Only show if the user enables notifications
        if (!ClickGuiModule.notifications.isToggled()) return;

        // Hacky fix to remove double notifications
        if (this.moduleName.equals("Gui")) return;
        Utils.Player.sendMessageToSelf(this.moduleName + " has been &4disabled");
    }

    public void setToggled(boolean enabled) {
        if(!canBeEnabled())
            return;
        if (enabled)
            enable();
        else
            disable();
    }

    public boolean isBindable() {
        return hasBind;
    }

    public String getName() {
        return this.moduleName;
    }

    public ArrayList<Setting> getSettings() {
        return this.settings;
    }

    public Setting getSettingByName(String name) {
        for (Setting setting : this.settings)
            if (setting.getName().equalsIgnoreCase(name))
                return setting;
        return null;
    }

    public void registerSetting(Setting Setting) {
        this.settings.add(Setting);
    }

    public void registerSettings(Setting... settings) {
        Collections.addAll(this.settings, settings);
    }

    public void setVisibleInHud(boolean vis) {
        this.showInHud = vis;
    }

    public ModuleCategory moduleCategory() {
        return this.moduleCategory;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void toggle() {
        if (this.enabled)
            this.disable();
        else
            this.enable();
    }

    public void guiButtonToggled(Setting b) {

    }

    public int getKeycode() {
        return this.keycode;
    }

    public void setBind(int keybind) {
        this.keycode = keybind;
    }

    public void resetToDefaults() {
        this.keycode = defualtKeyCode;
        this.setToggled(defaultEnabled);

        for (Setting setting : this.settings)
            setting.resetToDefaults();
    }

    public void setModuleComponent(ModuleComponent component) {
        this.component = component;
    }

    public void onGuiClose() {

    }

    public String getBindAsString() {
        return keycode == 0 ? "None" : Keyboard.getKeyName(keycode);
    }

    public void clearBinds() {
        this.keycode = 0;
    }

    public boolean isClientConfig() {
        return clientConfig;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void unRegister() {
        if(registered) {
            registered = false;
            EventBus.unsubscribe(this);
            onDisable();
        }
    }

    public enum ModuleCategory {
        category(true, null, "Raven B++"),
        combat(false, category, "Combat"),
        movement(false, category, "Movement"),
        player(false, category, "Player"),
        world(false, category, "World"),
        render(false, category, "Render"),
        minigames(false, category, "Minigames"),
        other(false, category, "Other"),
        client(false, category, "Client"),
        hotkey(false, category, "Hotkey"),
        config(false, client, "Config"),
        beta(false, category, "BETA");

        private final boolean defaultShown;
        private final ModuleCategory topCategory;
        private final String name;
        private List<ModuleCategory> childCategories = new ArrayList<ModuleCategory>();

        ModuleCategory(boolean defaultShown, ModuleCategory topCategory, String name) {
            if(topCategory != null)
                topCategory.addChildCategory(this);
            this.defaultShown = defaultShown;
            this.topCategory = topCategory;
            this.name = name;
        }

        public void addChildCategory(ModuleCategory moduleCategory) {
            childCategories.add(moduleCategory);
        }

        public List<ModuleCategory> getChildCategories() {
            return childCategories;
        }

        public String getName() {
            return name;
        }

        public boolean isShownByDefault() {
            return defaultShown;
        }

        public ModuleCategory getParentCategory() {
            return topCategory;
        }
    }
}
