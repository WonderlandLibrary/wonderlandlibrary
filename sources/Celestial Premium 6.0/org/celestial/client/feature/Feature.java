/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.hud.ArrayList;
import org.celestial.client.feature.impl.hud.Notifications;
import org.celestial.client.feature.impl.misc.ClientSounds;
import org.celestial.client.helpers.render.ScreenHelper;
import org.celestial.client.helpers.sound.SoundHelper;
import org.celestial.client.settings.Configurable;
import org.celestial.client.settings.Setting;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class Feature
extends Configurable {
    protected static Minecraft mc;
    public Type type;
    public boolean state;
    public boolean visible = true;
    public ScreenHelper screenHelper;
    private String label;
    private String suffix;
    private int bind;
    private String desc;

    public Feature(String label, String desc, Type type) {
        if (mc == null) {
            mc = Minecraft.getMinecraft();
        }
        this.screenHelper = new ScreenHelper(0.0f, 0.0f);
        this.label = label;
        this.desc = desc;
        this.type = type;
        this.bind = 0;
        this.state = false;
    }

    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("state", this.getState());
        object.addProperty("keyIndex", this.getBind());
        object.addProperty("visible", this.isVisible());
        JsonObject propertiesObject = new JsonObject();
        if (this.getOptions() != null) {
            for (Setting set : this.getOptions()) {
                if (set instanceof BooleanSetting) {
                    propertiesObject.addProperty(set.getName(), ((BooleanSetting)set).getCurrentValue());
                    continue;
                }
                if (set instanceof ListSetting) {
                    propertiesObject.addProperty(set.getName(), ((ListSetting)set).getCurrentMode());
                    continue;
                }
                if (set instanceof NumberSetting) {
                    propertiesObject.addProperty(set.getName(), Float.valueOf(((NumberSetting)set).getCurrentValue()));
                    continue;
                }
                if (!(set instanceof ColorSetting)) continue;
                propertiesObject.addProperty(set.getName(), ((ColorSetting)set).getColor());
            }
            object.add("Settings", propertiesObject);
        }
        return object;
    }

    public void load(JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                this.setState(object.get("state").getAsBoolean());
            }
            if (object.has("visible")) {
                this.setVisible(object.get("visible").getAsBoolean());
            }
            if (object.has("keyIndex")) {
                this.setBind(object.get("keyIndex").getAsInt());
            }
            for (Setting set : this.getOptions()) {
                JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null || propertiesObject == null || !propertiesObject.has(set.getName())) continue;
                if (set instanceof BooleanSetting) {
                    ((BooleanSetting)set).setValue(propertiesObject.get(set.getName()).getAsBoolean());
                    continue;
                }
                if (set instanceof ListSetting) {
                    ((ListSetting)set).setCurrentMode(propertiesObject.get(set.getName()).getAsString());
                    continue;
                }
                if (set instanceof NumberSetting) {
                    ((NumberSetting)set).setCurrentValue(propertiesObject.get(set.getName()).getAsFloat());
                    continue;
                }
                if (!(set instanceof ColorSetting)) continue;
                ((ColorSetting)set).setColor(propertiesObject.get(set.getName()).getAsInt());
            }
        }
    }

    public ScreenHelper getTranslate() {
        return this.screenHelper;
    }

    public String getSuffix() {
        return this.suffix == null ? this.label : this.suffix;
    }

    public void setSuffix(String suffix) {
        if (!ArrayList.showSuffix.getCurrentValue()) {
            this.suffix = this.getLabel();
        } else {
            String prefix1;
            String string = ArrayList.suffixType.currentMode.equals("None") ? "" : (ArrayList.suffixType.currentMode.equals("<>") ? "<" : (ArrayList.suffixType.currentMode.equals("()") ? "(" : (ArrayList.suffixType.currentMode.equals("[]") ? "[" : (ArrayList.suffixType.currentMode.equals("|") ? "| " : (ArrayList.suffixType.currentMode.equals("-") ? "- " : (prefix1 = ArrayList.suffixType.currentMode.equals("~") ? "~" : ""))))));
            String prefix2 = ArrayList.suffixType.currentMode.equals("None") ? "" : (ArrayList.suffixType.currentMode.equals("<>") ? ">" : (ArrayList.suffixType.currentMode.equals("()") ? ")" : (ArrayList.suffixType.currentMode.equals("[]") ? "]" : (ArrayList.suffixType.currentMode.equals("|") ? "" : (ArrayList.suffixType.currentMode.equals("-") ? "" : (ArrayList.suffixType.currentMode.equals("~") ? "~" : ""))))));
            this.suffix = suffix;
            this.suffix = this.getLabel() + " " + (Object)((Object)TextFormatting.GRAY) + prefix1 + suffix + prefix2;
        }
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isHidden() {
        return !this.visible;
    }

    public void setHidden(boolean visible) {
        this.visible = !visible;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getBind() {
        return this.bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Type getType() {
        return this.type;
    }

    public void setCategory(Type type) {
        this.type = type;
    }

    public void onEnable() {
        EventManager.register(this);
        if (!(this.getLabel().contains("ClickGui") || this.getLabel().contains("Client Font") || this.getLabel().contains("Notifications"))) {
            if (Celestial.instance.featureManager.getFeatureByClass(ClientSounds.class).getState()) {
                float volume = ClientSounds.volume.getCurrentValue() / 10.0f;
                if (ClientSounds.soundMode.currentMode.equals("Button")) {
                    Feature.mc.player.playSound(SoundEvents.BLOCK_NOTE_PLING, ClientSounds.volume.getCurrentValue() / 100.0f, ClientSounds.pitch.getCurrentValue());
                } else {
                    SoundHelper.playSound("enable.wav", -30.0f + volume * 3.0f, false);
                }
            }
            if (Notifications.state.getCurrentValue()) {
                NotificationManager.publicity("Feature", this.getLabel() + " was" + (Object)((Object)ChatFormatting.GREEN) + " Enabled!", 1, NotificationType.INFO);
            }
        }
    }

    public void onDisable() {
        EventManager.unregister(this);
        if (!(this.getLabel().contains("ClickGui") || this.getLabel().contains("Client Font") || this.getLabel().contains("Notifications"))) {
            if (Celestial.instance.featureManager.getFeatureByClass(ClientSounds.class).getState()) {
                float volume = ClientSounds.volume.getCurrentValue() / 10.0f;
                if (ClientSounds.soundMode.currentMode.equals("Button")) {
                    Feature.mc.player.playSound(SoundEvents.BLOCK_NOTE_PLING, ClientSounds.volume.getCurrentValue() / 100.0f, ClientSounds.pitch.getCurrentValue());
                } else {
                    SoundHelper.playSound("disable.wav", -30.0f + volume * 3.0f, false);
                }
            }
            if (Notifications.state.getCurrentValue()) {
                NotificationManager.publicity("Feature", this.getLabel() + " was" + (Object)((Object)ChatFormatting.RED) + " Disabled!", 1, NotificationType.INFO);
            }
        }
    }

    public void toggle() {
        boolean bl = this.state = !this.state;
        if (this.state) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        if (state) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.state = state;
    }
}

