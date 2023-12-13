package koks.manager.module;

import koks.Koks;
import koks.api.interfaces.Debug;
import koks.api.interfaces.Methods;
import koks.api.util.*;
import koks.manager.event.Event;
import koks.api.settings.Setting;
import koks.manager.module.impl.player.SendPublic;
import koks.api.interfaces.Wrapper;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:36
 */
public abstract class Module implements Methods, Wrapper, Debug {

    private String name, description, info = "";
    private int key;
    private double animation;
    private Category category;
    public AtomicBoolean toggled = new AtomicBoolean(false), bypass = new AtomicBoolean(false);

    public TimeHelper timeHelper = new TimeHelper();

    public Module() {
        ModuleInfo moduleInfo = getClass().getAnnotation(ModuleInfo.class);
        this.category = moduleInfo.category();
        this.name = moduleInfo.name();
        this.description = moduleInfo.description();
        this.key = moduleInfo.key();

        /* for(Field field : getClass().getDeclaredFields()) {
            try{
                SettingInfo settingInfo = field.getAnnotation(SettingInfo.class);
                switch (field.getType()) {
                    case Float.class:
                        registerSetting(new Setting(settingInfo.name(), field.getFloat()));
                        break;
                }
            }catch (Exception ignore) {
            }
        }*/
    }

    public boolean isBypass() {
        return bypass.get();
    }

    public void setBypass(boolean bypass) {
        this.bypass.compareAndSet(this.bypass.get(), bypass);
    }

    public boolean isToggled() {
        return toggled.get();
    }

    public void toggle() {
        setToggled(!isToggled());
    }

    public synchronized void setToggled(boolean toggled) {
        this.toggled.compareAndSet(this.toggled.get(), toggled);
        try {
            if (!this.toggled.get()) {
                animation = 0;
                onDisable();
            } else {
                animation = 0;
                onEnable();
            }

            if (Koks.getKoks().moduleManager.getModule(SendPublic.class).isToggled()) {
                String toggle = toggled ? "Enabled" : "Disabled";
                getPlayer().sendChatMessage(toggle + " " + this.getName());
            }
        } catch (Exception ignored) {
        }


    }

    public abstract void onEvent(Event event);

    public abstract void onEnable();

    public abstract void onDisable();

    public void registerSetting(Setting setting) {
        Koks.getKoks().settingsManager.registerSetting(setting);
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info.equals("") ? "" : " " + info;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public double getAnimation() {
        return animation;
    }

    public void setAnimation(double animation) {
        this.animation = animation;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getArrayName(String colorCode, boolean showTags) {
        return name + (showTags ? colorCode + info : "");
    }

    public enum Category {
        COMBAT, MOVEMENT, RENDER, GUI, UTILITIES, PLAYER, DEBUG, WORLD;

        public Color getCategoryColor() {
            switch (this) {
                case COMBAT:
                    return new Color(0xFF555D);
                case MOVEMENT:
                    return new Color(0xDEE955);
                case RENDER:
                    return new Color(0xFC56FF);
                case GUI:
                    return new Color(0xFF8056);
                case UTILITIES:
                    return new Color(0xCCCCCC);
                case PLAYER:
                    return new Color(0x52EE61);
                case DEBUG:
                    return new Color(0x3AEFB6);
                case WORLD:
                    return new Color(0x4CDDF3);
            }
            return Color.white;
        }
    }
}
