package ru.smertnix.celestial.feature;

import com.google.gson.JsonObject;

import net.minecraft.client.main.Main;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventManager;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.visual.Notifications;
import ru.smertnix.celestial.ui.clickgui.ScreenHelper;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.ui.settings.Configurable;
import ru.smertnix.celestial.ui.settings.Setting;
import ru.smertnix.celestial.ui.settings.impl.*;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.other.ChatUtils;
import ru.smertnix.celestial.utils.render.SoundUtils;

import java.util.Set;


public class Feature extends Configurable implements Helper {
    public ScreenHelper screenHelper;
    public FeatureCategory category;
    private boolean enabled;
    public float animYto;
    private String label, suffix;
    private int bind;
    private String desc;
    public float anim, anim2;
    public Feature(String label, String desc, FeatureCategory category) {
        this.label = label;
        this.desc = desc;
        this.category = category;
        this.bind = 0;
        enabled = false;
    }

    public Feature(String label, FeatureCategory category) {
        this.screenHelper = new ScreenHelper(0.0f, 0.0f);
        this.label = label;
        this.category = category;
        this.bind = 0;
        enabled = false;
    }

    public String getSuffix() {
        return suffix == null ? label : suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.suffix = getLabel() + TextFormatting.GRAY + " " + suffix;
    }
    public ScreenHelper getTranslate()
    {
        return this.screenHelper;
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

    public FeatureCategory getCategory() {
        return category;
    }

    public void setCategory(FeatureCategory category) {
        this.category = category;
    }

    public void onEnable() {
    	String label = getLabel().replace(" ", "");
    	
        if (!(getLabel().contains("Click Gui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.state.getBoolValue()) {
            NotificationRenderer.queue("Module Debug", label + " " + "was " + "enabled", 1, NotificationMode.INFO);
        }
        EventManager.register(this);
    }

    public void onDisable() {
    	String label = getLabel().replace(" ", "");
    	
        if (!(getLabel().contains("Click Gui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.state.getBoolValue()) {
            NotificationRenderer.queue("Module Debug", label + " " + "was " + "disabled", 1, NotificationMode.INFO);
        }
        EventManager.unregister(this);
    }

    public void toggle() {
        this.enabled = !this.enabled;

        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public ScreenHelper getScreenHelper() {
        return this.screenHelper;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.enabled = enabled;
    }

    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("state", isEnabled());
        object.addProperty("keyIndex", getBind());
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
                }/* else if (set instanceof MultipleBoolSetting) {
                	for (BooleanSetting s : ((MultipleBoolSetting) set).getBoolSettings()) {
                		propertiesObject.addProperty(set.getName(), (s).getBoolValue());
                	}
                }*/ else if (set instanceof StringSetting) {
                    propertiesObject.addProperty(set.getName(), ((StringSetting) set).getCurrentText());
                }
            }
            object.add("Settings", propertiesObject);
        }
        return object;
    }

    public void load(JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                this.setEnabled(object.get("state").getAsBoolean());
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
                }/* else if (set instanceof MultipleBoolSetting) {
                	for (BooleanSetting s : ((MultipleBoolSetting) set).getBoolSettings()) {
                        s.setBoolValue(propertiesObject.get(set.getName()).getAsBoolean());
                	}
                }*/ else if (set instanceof StringSetting) {
                    ((StringSetting) set).setCurrentText(propertiesObject.get(set.getName()).getAsString());
                }
            }
        }
    }
}
