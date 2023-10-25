/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.scene.transform.Translate
 */
package me.Tengoku.Terror.module;

import javafx.scene.transform.Translate;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.Event;
import me.Tengoku.Terror.event.EventManager;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.ui.Notification.Notification;
import me.Tengoku.Terror.ui.Notification.NotificationManager;
import me.Tengoku.Terror.ui.Notification.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

public class Module {
    public boolean extended;
    public static Minecraft mc = Minecraft.getMinecraft();
    private Category category;
    private int key;
    private String name;
    public String description;
    protected boolean toggled;
    public int transition;
    public boolean visible;
    private String displayName;
    public static int settings;
    public Translate translate = new Translate(0.0, 0.0);

    public String getDisplayName() {
        return this.displayName == null ? this.name : this.displayName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void onEnable() {
        EventManager cfr_ignored_0 = Exodus.INSTANCE.eventManager;
        EventManager.register(this);
        NotificationManager.show(new Notification(NotificationType.ONMODULE, "Module toggled", String.valueOf(this.getName()) + " was" + " " + "\ufffdaenabled" + "\ufffdf!", 1));
    }

    public Category getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public int getTransition() {
        return this.transition;
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void toggle() {
        this.toggled = !this.toggled;
        this.onToggle();
        if (this.toggled) {
            this.onEnable();
            this.transition = Minecraft.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.getName())) - 10;
        } else {
            this.onDisable();
            this.transition = Minecraft.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.getName())) + 10;
        }
    }

    public void onToggle() {
    }

    public void setName(String string) {
        this.name = string;
    }

    public void onDisable() {
        EventManager cfr_ignored_0 = Exodus.INSTANCE.eventManager;
        EventManager.unregister(this);
        NotificationManager.show(new Notification(NotificationType.OFFMODULE, "Module toggled", String.valueOf(this.getName()) + " was" + " " + "\ufffdcdisabled" + "\ufffdf!", 1));
    }

    public void setKey(int n) {
        this.key = n;
        if (Exodus.INSTANCE.configManager != null) {
            Exodus.INSTANCE.configManager.save();
        }
    }

    public String getDescription() {
        return this.description;
    }

    public void setTransition(int n) {
        this.transition = n;
    }

    public void setup() {
    }

    public boolean setToggled(boolean bl) {
        if (Exodus.INSTANCE.configManager != null) {
            Exodus.INSTANCE.configManager.save();
        }
        if (bl) {
            if (!this.toggled) {
                this.toggle();
            }
        } else if (this.toggled) {
            this.toggle();
        }
        return bl;
    }

    public int getSettings() {
        return settings;
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public void onEvent(Event event) {
    }

    public void setDisplayName(String string) {
        this.displayName = string;
    }

    public Module(String string, int n, Category category, String string2) {
        this.name = string;
        this.key = n;
        this.category = category;
        this.description = string2;
        this.toggled = false;
        this.setup();
    }

    public int getKey() {
        return this.key;
    }
}

