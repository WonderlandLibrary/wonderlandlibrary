/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import tk.rektsky.event.Event;
import tk.rektsky.event.EventManager;
import tk.rektsky.event.impl.ModuleTogglePreEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.module.impl.render.ToggleNotifications;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.Setting;
import tk.rektsky.utils.display.ColorUtil;

public class Module {
    public String name;
    public String description;
    public int keyCode;
    public Category category;
    private boolean toggled = false;
    public Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<Setting> settings = new ArrayList();
    public int enabledTicks;
    public boolean bypassing = true;
    public BooleanSetting hideFromArrayList = new BooleanSetting("Hidden", false);

    public Module(String name, String description, int key, Category cat) {
        this.name = name;
        this.keyCode = key;
        this.category = cat;
        this.description = description;
    }

    public Module(String name, String description, Category cat) {
        this.name = name;
        this.keyCode = 0;
        this.category = cat;
        this.description = description;
    }

    public Module(String name, String description, Category cat, boolean bypassing) {
        this.name = name;
        this.keyCode = 0;
        this.category = cat;
        this.description = description;
        this.bypassing = bypassing;
    }

    public void toggle() {
        if (this.toggled) {
            ModuleTogglePreEvent event = new ModuleTogglePreEvent(this, false);
            EventManager.callEvent(event);
            if (event.isCanceled()) {
                return;
            }
            this.toggled = false;
            if (ModulesManager.getModuleByClass(ToggleNotifications.class).isToggled()) {
                Notification.displayNotification(new Notification.PopupMessage(this.name, "Disabled " + this.name, ColorUtil.NotificationColors.RED, 40));
            }
            this.onDisable();
            try {
                if (EventManager.EVENT_BUS.isRegistered(this)) {
                    EventManager.EVENT_BUS.unregister(this);
                }
            }
            catch (Exception exception) {}
        } else {
            ModuleTogglePreEvent event = new ModuleTogglePreEvent(this, true);
            EventManager.callEvent(event);
            if (event.isCanceled()) {
                return;
            }
            this.toggled = true;
            this.enabledTicks = 0;
            if (ModulesManager.getModuleByClass(ToggleNotifications.class).isToggled()) {
                Notification.displayNotification(new Notification.PopupMessage(this.name, "Enabled " + this.name, ColorUtil.NotificationColors.GREEN, 40));
            }
            this.onEnable();
            if (!EventManager.EVENT_BUS.isRegistered(this)) {
                try {
                    EventManager.EVENT_BUS.register(this);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    public void setToggled(boolean value) {
        if (!value) {
            ModuleTogglePreEvent event = new ModuleTogglePreEvent(this, false);
            EventManager.callEvent(event);
            if (event.isCanceled()) {
                return;
            }
            this.toggled = false;
            if (ModulesManager.getModuleByClass(ToggleNotifications.class).isToggled()) {
                Notification.displayNotification(new Notification.PopupMessage(this.name, "Disabled " + this.name, ColorUtil.NotificationColors.RED, 40));
            }
            this.onDisable();
            try {
                if (EventManager.EVENT_BUS.isRegistered(this)) {
                    EventManager.EVENT_BUS.unregister(this);
                }
            }
            catch (Exception exception) {}
        } else {
            ModuleTogglePreEvent event = new ModuleTogglePreEvent(this, true);
            EventManager.callEvent(event);
            if (event.isCanceled()) {
                return;
            }
            this.toggled = true;
            this.enabledTicks = 0;
            if (ModulesManager.getModuleByClass(ToggleNotifications.class).isToggled()) {
                Notification.displayNotification(new Notification.PopupMessage(this.name, "Enabled " + this.name, ColorUtil.NotificationColors.GREEN, 40));
            }
            this.onEnable();
            if (!EventManager.EVENT_BUS.isRegistered(this)) {
                try {
                    EventManager.EVENT_BUS.register(this);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    public void rawSetToggled(boolean value) {
        if (!value) {
            this.toggled = false;
            try {
                if (EventManager.EVENT_BUS.isRegistered(this)) {
                    EventManager.EVENT_BUS.unregister(this);
                }
            }
            catch (Exception exception) {}
        } else {
            this.toggled = true;
            if (!EventManager.EVENT_BUS.isRegistered(this)) {
                try {
                    EventManager.EVENT_BUS.register(this);
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    @Deprecated
    public void onEvent(Event event) {
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public String getSuffix() {
        return "";
    }

    public void registerSetting(Setting setting) {
    }

    public void unregisterListeners() {
    }
}

