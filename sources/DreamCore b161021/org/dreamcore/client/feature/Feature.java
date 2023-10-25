package org.dreamcore.client.feature;

import com.google.gson.JsonObject;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventManager;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.feature.impl.hud.ClientSounds;
import org.dreamcore.client.feature.impl.hud.Notifications;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.misc.MusicHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.settings.Configurable;
import org.dreamcore.client.settings.Setting;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

public class Feature extends Configurable implements Helper {

    public Type type;
    public boolean state;
    public boolean visible = true;
    public ScreenHelper screenHelper = new ScreenHelper(0, 0);
    private String label, suffix;
    private int bind;
    private String desc;

    public Feature(String label, String desc, Type type) {
        this.label = label;
        this.desc = desc;
        this.type = type;
        this.bind = 0;
        this.state = false;
    }

    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("state", getState());
        object.addProperty("keyIndex", getBind());
        object.addProperty("visible", isVisible());
        JsonObject propertiesObject = new JsonObject();
        for (Setting set : this.getSettings()) {
            if (this.getSettings() != null) {
                if (set instanceof BooleanSetting) {
                    propertiesObject.addProperty(set.getName(), ((BooleanSetting) set).getBoolValue());
                } else if (set instanceof ListSetting) {
                    propertiesObject.addProperty(set.getName(), ((ListSetting) set).getCurrentMode());
                } else if (set instanceof NumberSetting) {
                    propertiesObject.addProperty(set.getName(), ((NumberSetting) set).getNumberValue());
                } else if (set instanceof ColorSetting) {
                    propertiesObject.addProperty(set.getName(), ((ColorSetting) set).getColorValue());
                }
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
            for (Setting set : getSettings()) {
                JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null)
                    continue;
                if (propertiesObject == null)
                    continue;
                if (!propertiesObject.has(set.getName()))
                    continue;
                if (set instanceof BooleanSetting) {
                    ((BooleanSetting) set).setBoolValue(propertiesObject.get(set.getName()).getAsBoolean());
                } else if (set instanceof ListSetting) {
                    ((ListSetting) set).setListMode(propertiesObject.get(set.getName()).getAsString());
                } else if (set instanceof NumberSetting) {
                    ((NumberSetting) set).setValueNumber(propertiesObject.get(set.getName()).getAsFloat());
                } else if (set instanceof ColorSetting) {
                    ((ColorSetting) set).setColorValue(propertiesObject.get(set.getName()).getAsInt());
                }
            }
        }
    }

    public ScreenHelper getScreenHelper() {
        return this.screenHelper;
    }

    public String getSuffix() {
        return suffix == null ? label : suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.suffix = getLabel() + " - " + suffix;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isHidden() {
        return !visible;
    }

    public void setHidden(boolean visible) {
        this.visible = !visible;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Type getType() {
        return type;
    }

    public void setCategory(Type type) {
        this.type = type;
    }

    public void onEnable() {
        if (dreamcore.instance.featureManager.getFeatureByClass(ClientSounds.class).getState()) {
            MusicHelper.playSound("enable.wav");
        }
        EventManager.register(this);
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.state.getBoolValue()) {
            NotificationManager.publicity(getLabel(), "was enabled!", 1, NotificationType.INFO);
        }
    }

    public void onDisable() {
        if (dreamcore.instance.featureManager.getFeatureByClass(ClientSounds.class).getState()) {
            MusicHelper.playSound("disable.wav");
        }
        EventManager.unregister(this);
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.state.getBoolValue()) {
            NotificationManager.publicity(getLabel(), "was disabled!", 1, NotificationType.INFO);
        }
    }

    public void toggle() {
        this.state = !this.state;

        if (state) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public boolean getState() {
        return state;
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
