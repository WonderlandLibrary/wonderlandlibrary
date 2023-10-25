package fun.expensive.client.feature;

import com.google.gson.JsonObject;
import fun.expensive.client.Expensive;
import fun.rich.client.event.EventManager;
import fun.expensive.client.feature.impl.FeatureCategory;
import fun.rich.client.feature.impl.hud.Notifications;
import fun.rich.client.feature.impl.misc.ModuleSoundAlert;
import fun.expensive.client.ui.clickgui.ScreenHelper;
import fun.expensive.client.ui.notification.NotificationMode;
import fun.expensive.client.ui.notification.NotificationRenderer;
import fun.rich.client.ui.settings.Configurable;
import fun.rich.client.ui.settings.Setting;
import fun.rich.client.ui.settings.impl.*;
import fun.rich.client.utils.Helper;
import fun.rich.client.utils.other.ChatUtils;
import fun.rich.client.utils.render.SoundUtils;
import net.minecraft.client.main.Main;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;

import java.util.Set;


public class Feature extends Configurable implements Helper {
    public ScreenHelper screenHelper;
    public FeatureCategory category;
    private boolean enabled;
    public float animYto;
    private String label, suffix;
    private int bind;
    private String desc;

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
        if (Rich.instance.featureManager.getFeature(ModuleSoundAlert.class).isEnabled()) {
            float volume = ModuleSoundAlert.volume.getNumberValue() / 10.0f;
            if (ModuleSoundAlert.soundMode.currentMode.equals("Button")) {
                Feature.mc.player.playSound(SoundEvents.BLOCK_NOTE_PLING, ModuleSoundAlert.volume.getNumberValue() / 100.0f, ModuleSoundAlert.pitch.getNumberValue());
            } else if (ModuleSoundAlert.soundMode.currentMode.equalsIgnoreCase("Wav")) {
                SoundUtils.playSound("enable.wav", -30.0f + volume * 3.0f, false);
            }
        }
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.state.getBoolValue()) {
            NotificationRenderer.queue("Feature", getLabel() + " " + "was " + TextFormatting.GREEN + "Enabled!", 1, NotificationMode.INFO);
        } else if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getBoolValue()) {
            ChatUtils.addChatMessage(TextFormatting.GRAY + "[Notifications] " + TextFormatting.WHITE + getLabel() + " was" + TextFormatting.GREEN + " enabled!");
        }
        EventManager.register(this);
    }

    public void onDisable() {
        if (Rich.instance.featureManager.getFeature(ModuleSoundAlert.class).isEnabled()) {
            float volume = ModuleSoundAlert.volume.getNumberValue() / 10.0f;
            if (ModuleSoundAlert.soundMode.currentMode.equals("Button")) {
                Feature.mc.player.playSound(SoundEvents.BLOCK_NOTE_PLING, ModuleSoundAlert.volume.getNumberValue() / 100.0f, ModuleSoundAlert.pitch.getNumberValue());
            } else if (ModuleSoundAlert.soundMode.currentMode.equalsIgnoreCase("Wav")) {
                SoundUtils.playSound("disable.wav", -30.0f + volume * 3.0f, false);
            }
        }
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.state.getBoolValue()) {
            NotificationRenderer.queue("Feature", getLabel() + " " + "was " + TextFormatting.RED + "Disabled!", 1, NotificationMode.INFO);
        } else if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getBoolValue()) {
            ChatUtils.addChatMessage(TextFormatting.GRAY + "[Notifications] " + TextFormatting.WHITE + getLabel() + " was" + TextFormatting.RED + " disabled!");
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
                }
            }
        }
    }
}
