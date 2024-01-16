// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature;

import ru.fluger.client.ui.notification.NotificationManager;
import ru.fluger.client.ui.notification.NotificationType;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.feature.impl.hud.Notifications;
import ru.fluger.client.helpers.misc.SoundHelper;
import ru.fluger.client.feature.impl.misc.ModuleSoundAlert;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.EventManager;
import java.util.Iterator;
import com.google.gson.JsonElement;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.Setting;
import com.google.gson.JsonObject;
import ru.fluger.client.helpers.render.ScreenHelper;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.Configurable;

public class Feature extends Configurable
{
    protected static bib mc;
    public Type type;
    public boolean state;
    public boolean visible;
    private String label;
    private String suffix;
    private int bind;
    public float animYto;
    private String desc;
    public ScreenHelper screenHelper;
    
    public Feature(final String label, final String desc, final Type type) {
        this.visible = true;
        if (Feature.mc == null) {
            Feature.mc = bib.z();
        }
        this.screenHelper = new ScreenHelper(0.0f, 0.0f);
        this.label = label;
        this.desc = desc;
        this.type = type;
        this.bind = 0;
        this.state = false;
    }
    
    public JsonObject save() {
        final JsonObject object = new JsonObject();
        object.addProperty("state", Boolean.valueOf(this.getState()));
        object.addProperty("keyIndex", (Number)this.getBind());
        object.addProperty("visible", Boolean.valueOf(this.isVisible()));
        final JsonObject propertiesObject = new JsonObject();
        if (this.getOptions() != null) {
            for (final Setting set : this.getOptions()) {
                if (set instanceof BooleanSetting) {
                    propertiesObject.addProperty(set.getName(), Boolean.valueOf(((BooleanSetting)set).getCurrentValue()));
                }
                else if (set instanceof ListSetting) {
                    propertiesObject.addProperty(set.getName(), ((ListSetting)set).getCurrentMode());
                }
                else if (set instanceof NumberSetting) {
                    propertiesObject.addProperty(set.getName(), (Number)((NumberSetting)set).getCurrentValue());
                }
                else {
                    if (!(set instanceof ColorSetting)) {
                        continue;
                    }
                    propertiesObject.addProperty(set.getName(), (Number)((ColorSetting)set).getColor());
                }
            }
            object.add("Settings", (JsonElement)propertiesObject);
        }
        return object;
    }
    
    public void load(final JsonObject object) {
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
            for (final Setting set : this.getOptions()) {
                final JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null) {
                    continue;
                }
                if (propertiesObject == null) {
                    continue;
                }
                if (!propertiesObject.has(set.getName())) {
                    continue;
                }
                if (set instanceof BooleanSetting) {
                    ((BooleanSetting)set).setValue(propertiesObject.get(set.getName()).getAsBoolean());
                }
                else if (set instanceof ListSetting) {
                    ((ListSetting)set).setCurrentMode(propertiesObject.get(set.getName()).getAsString());
                }
                else if (set instanceof NumberSetting) {
                    ((NumberSetting)set).setCurrentValue(propertiesObject.get(set.getName()).getAsFloat());
                }
                else {
                    if (!(set instanceof ColorSetting)) {
                        continue;
                    }
                    ((ColorSetting)set).setColor(propertiesObject.get(set.getName()).getAsInt());
                }
            }
        }
    }
    
    public ScreenHelper getTranslate() {
        return this.screenHelper;
    }
    
    public String getSuffix() {
        return (this.suffix == null) ? this.label : this.suffix;
    }
    
    public void setSuffix(final String suffix) {
        this.suffix = suffix;
        this.suffix = this.getLabel() + a.h + " " + suffix;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public boolean isHidden() {
        return !this.visible;
    }
    
    public void setHidden(final boolean visible) {
        this.visible = !visible;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    public int getBind() {
        return this.bind;
    }
    
    public void setBind(final int bind) {
        this.bind = bind;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setCategory(final Type type) {
        this.type = type;
    }
    
    public void onEnable() {
        EventManager.register(this);
        if (!this.getLabel().contains("ClickGui") && !this.getLabel().contains("Client Font") && !this.getLabel().contains("Notifications")) {
            if (Fluger.instance.featureManager.getFeatureByClass(ModuleSoundAlert.class).getState()) {
                final float volume = ModuleSoundAlert.volume.getCurrentValue() / 10.0f;
                if (ModuleSoundAlert.soundMode.currentMode.equals("Button")) {
                    Feature.mc.h.a(qf.eC, ModuleSoundAlert.volume.getCurrentValue() / 100.0f, ModuleSoundAlert.pitch.getCurrentValue());
                }
                else {
                    SoundHelper.playSound("enable.wav", -30.0f + volume * 3.0f, false);
                }
            }
            if (Notifications.state.getCurrentValue()) {
                NotificationManager.publicity("Feature", this.getLabel() + " was" + ChatFormatting.GREEN + " Enabled!", 1, NotificationType.INFO);
            }
        }
    }
    
    public void onDisable() {
        EventManager.unregister(this);
        if (!this.getLabel().contains("ClickGui") && !this.getLabel().contains("Client Font") && !this.getLabel().contains("Notifications")) {
            if (Fluger.instance.featureManager.getFeatureByClass(ModuleSoundAlert.class).getState()) {
                final float volume = ModuleSoundAlert.volume.getCurrentValue() / 10.0f;
                if (ModuleSoundAlert.soundMode.currentMode.equals("Button")) {
                    Feature.mc.h.a(qf.eC, ModuleSoundAlert.volume.getCurrentValue() / 100.0f, ModuleSoundAlert.pitch.getCurrentValue());
                }
                else {
                    SoundHelper.playSound("disable.wav", -30.0f + volume * 3.0f, false);
                }
            }
            if (Notifications.state.getCurrentValue()) {
                NotificationManager.publicity("Feature", this.getLabel() + " was" + ChatFormatting.RED + " Disabled!", 1, NotificationType.INFO);
            }
        }
    }
    
    public void toggle() {
        final boolean state = !this.state;
        this.state = state;
        final boolean bl = state;
        if (this.state) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    public boolean getState() {
        return this.state;
    }
    
    public void setState(final boolean state) {
        if (state) {
            EventManager.register(this);
        }
        else {
            EventManager.unregister(this);
        }
        this.state = state;
    }
}
