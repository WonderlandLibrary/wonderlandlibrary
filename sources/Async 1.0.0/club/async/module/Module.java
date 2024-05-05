package club.async.module;

import club.async.Async;
import club.async.interfaces.MinecraftInterface;
import club.async.module.impl.hud.ModuleList;
import club.async.module.setting.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Module implements MinecraftInterface {

    private final ModuleInfo moduleInfo = getClass().getAnnotation(ModuleInfo.class);
    private final Category category = moduleInfo.category();
    private final String name = moduleInfo.name(), description = moduleInfo.description();
    private boolean toggled;
    private int key = Keyboard.KEY_NONE;
    private ArrayList<Setting> settings = new ArrayList<Setting>();
    private String extraTag = "";

    public Module() {
        initialize();
    }

    public void initialize() {
    }

    /*
     Registering the Event
    */

    public void onEnable() {
        Async.INSTANCE.getPubSub().subscribe(this);
    }

     /*
     Unregistering the Event
    */

    public void onDisable() {
        Async.INSTANCE.getPubSub().unsubscribe(this);
    }

    /*
    Enabling and disabling the Module
    */

    public final void toggle() {
        Async.INSTANCE.getModuleManager().getModuleListMods().sort(new ModuleList.ModuleComparator());
        toggled = !toggled;

        if (toggled)
            onEnable();
        else
            onDisable();

    }

    /*
     Registering settings
    */

    public final void addSetting(Setting setting) {
        settings.add(setting);
    }

    /*
    Some getter and setters
    */

    public final Category getCategory() {
        return category;
    }
    public final String getName() {
        return name;
    }
    public final String getDescription() {
        return description;
    }
    public final boolean isEnabled() {
        return toggled;
    }
    public final boolean isDisabled() {
        return !toggled;
    }
    public final void setToggled(boolean toggled) {
        if(toggled != this.toggled) toggle();
    }
    public final ArrayList<Setting> getSettings() {
        return settings;
    }
    public final Setting getSetting(String name) {
        return settings.stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    public final int getKey() {
        return key;
    }
    public final void setKey(int key) {
        this.key = key;
    }
    public final boolean gotSettings() {
        return settings != null && settings.toArray() != null && !settings.isEmpty();
    }
    public final String getDisplayName() {
        return name + " §7" + extraTag;
    }
    public final void setExtraTag(String extraTag) {
        this.extraTag = extraTag;
    }
    public final void setExtraTag(double extraTag) {
        this.extraTag = String.valueOf(extraTag);
    }

}
