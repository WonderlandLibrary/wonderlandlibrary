// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules;

import ru.tuskevich.util.chat.ChatUtility;
import java.util.Iterator;
import com.google.gson.JsonElement;
import ru.tuskevich.ui.dropui.setting.imp.MultiBoxSetting;
import ru.tuskevich.ui.dropui.setting.imp.ColorSetting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.ui.dropui.setting.Setting;
import com.google.gson.JsonObject;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.modules.impl.HUD.Notifications;
import net.minecraft.util.text.TextFormatting;
import ru.tuskevich.util.world.SoundUtility;
import ru.tuskevich.modules.impl.MISC.ClientSound;
import ru.tuskevich.Minced;
import ru.tuskevich.util.Utility;
import ru.tuskevich.ui.dropui.setting.Configurable;

public class Module extends Configurable implements Utility
{
    public ModuleAnnotation info;
    public String name;
    public String desc;
    public Type category;
    public int bind;
    public float animYto;
    public float guiAnimation;
    public boolean state;
    
    public Module() {
        this.info = this.getClass().getAnnotation(ModuleAnnotation.class);
        this.name = this.info.name();
        this.desc = this.info.desc();
        this.category = this.info.type();
        this.state = false;
        this.bind = 0;
    }
    
    public void toggle() {
        this.state = !this.state;
        if (this.state) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    public void setState(final boolean state) {
        this.state = state;
        if (state) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    public void onEnable() {
        Minced.getInstance().configManager.saveConfig("default");
        final float volume = ClientSound.volume.getFloatValue();
        if (Minced.getInstance().manager.getModule(ClientSound.class).state) {
            SoundUtility.playSound(2.0f, volume);
        }
        Notifications.notify("Module toggled", this.name + " was " + TextFormatting.GREEN + "enabled", Notifications.Notify.NotifyType.SUCCESS, 1);
        EventManager.register(this);
    }
    
    public void onDisable() {
        Minced.getInstance().configManager.saveConfig("default");
        final float volume = ClientSound.volume.getFloatValue();
        if (Minced.getInstance().manager.getModule(ClientSound.class).state) {
            SoundUtility.playSound(1.5f, volume);
        }
        Notifications.notify("Module toggled", this.name + " was " + TextFormatting.RED + "disabled", Notifications.Notify.NotifyType.ERROR, 1);
        EventManager.unregister(this);
    }
    
    public JsonObject save() {
        final JsonObject object = new JsonObject();
        if (this.state) {
            object.addProperty("state", Boolean.valueOf(true));
        }
        if (this.bind > 0) {
            object.addProperty("keyIndex", (Number)this.bind);
        }
        final JsonObject propertiesObject = new JsonObject();
        for (final Setting set : this.get()) {
            if (set instanceof BooleanSetting) {
                propertiesObject.addProperty(set.getName(), Boolean.valueOf(((BooleanSetting)set).get()));
            }
            else if (set instanceof ModeSetting) {
                propertiesObject.addProperty(set.getName(), ((ModeSetting)set).getCurrentMode());
            }
            else if (set instanceof SliderSetting) {
                propertiesObject.addProperty(set.getName(), (Number)((SliderSetting)set).getFloatValue());
            }
            else if (set instanceof ColorSetting) {
                propertiesObject.addProperty(set.getName(), (Number)((ColorSetting)set).getColorValue());
            }
            else {
                if (!(set instanceof MultiBoxSetting)) {
                    continue;
                }
                final StringBuilder builder = new StringBuilder();
                int i = 0;
                for (final String s : ((MultiBoxSetting)set).values) {
                    if (((MultiBoxSetting)set).selectedValues.get(i)) {
                        builder.append(s + "\n");
                    }
                    ++i;
                }
                propertiesObject.addProperty(set.getName(), builder.toString());
            }
        }
        object.add("Settings", (JsonElement)propertiesObject);
        return object;
    }
    
    public void load(final JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                this.setState(object.get("state").getAsBoolean());
            }
            if (object.has("keyIndex")) {
                this.bind = object.get("keyIndex").getAsInt();
            }
            for (final Setting set : this.get()) {
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
                    ((BooleanSetting)set).state = propertiesObject.get(set.getName()).getAsBoolean();
                }
                else if (set instanceof ModeSetting) {
                    ((ModeSetting)set).setListMode(propertiesObject.get(set.getName()).getAsString());
                }
                else if (set instanceof SliderSetting) {
                    ((SliderSetting)set).current = propertiesObject.get(set.getName()).getAsFloat();
                }
                else if (set instanceof ColorSetting) {
                    ((ColorSetting)set).setColorValue(propertiesObject.get(set.getName()).getAsInt());
                }
                else {
                    if (!(set instanceof MultiBoxSetting)) {
                        continue;
                    }
                    for (int i = 0; i < ((MultiBoxSetting)set).selectedValues.size(); ++i) {
                        ((MultiBoxSetting)set).selectedValues.set(i, false);
                    }
                    int i = 0;
                    final String[] strs = propertiesObject.get(set.getName()).getAsString().split("\n");
                    for (final String s : ((MultiBoxSetting)set).values) {
                        for (final String str : strs) {
                            if (str.equalsIgnoreCase(s)) {
                                ((MultiBoxSetting)set).selectedValues.set(i, true);
                            }
                        }
                        ++i;
                    }
                }
            }
        }
    }
    
    public void sendMessage(final String message) {
        ChatUtility.addChatMessage(message);
    }
}
