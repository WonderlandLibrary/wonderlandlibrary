// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules;

import java.util.Iterator;
import java.util.List;
import com.google.gson.JsonElement;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import Lavish.event.Event;
import com.google.gson.JsonObject;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.Minecraft;

public abstract class Module
{
    public String name;
    public String displayName;
    private int key;
    public Category category;
    public String desc;
    public boolean enabled;
    public boolean enabled2;
    public float animationX;
    public float animationY;
    public boolean inProcess;
    public boolean visible;
    public static Minecraft mc;
    
    static {
        Module.mc = Minecraft.getMinecraft();
    }
    
    public Module(final String name, final int key, final boolean visible, final Category cat, final String desc) {
        this.name = name;
        this.displayName = name;
        this.key = key;
        this.visible = visible;
        this.category = cat;
        this.desc = desc;
        this.enabled = false;
        this.setup();
    }
    
    public void setDisplayName(final String name) {
        this.displayName = String.valueOf(this.name) + EnumChatFormatting.GRAY + " " + "[" + name + "]";
    }
    
    public void saveToJson(final JsonObject json) {
        json.addProperty("keyBind", (Number)this.getKey());
        json.addProperty("toggled", Boolean.valueOf(this.isEnabled()));
    }
    
    public String getName() {
        return this.name;
    }
    
    public void onUpdate() {
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void setup() {
    }
    
    public void toggle() {
        this.enabled = !this.enabled;
        if (this.enabled) {
            this.onEnable();
            this.enabled2 = true;
        }
        if (!this.enabled) {
            this.onDisable();
            this.enabled2 = false;
        }
    }
    
    public void onEvent(final Event e) {
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(final Category category) {
        this.category = category;
    }
    
    public JsonObject save(final boolean isConfig) {
        final JsonObject object = new JsonObject();
        object.addProperty("toggled", Boolean.valueOf(this.isEnabled()));
        if (!isConfig) {
            object.addProperty("key", (Number)this.getKey());
        }
        final List<Setting> properties = Client.instance.setmgr.getSettingsByMod(this);
        if (properties != null && !properties.isEmpty()) {
            final JsonObject propertiesObject = new JsonObject();
            for (final Setting property : properties) {
                final String upperCase;
                switch ((upperCase = property.mode.toUpperCase()).hashCode()) {
                    case -1846317855: {
                        if (!upperCase.equals("SLIDER")) {
                            continue;
                        }
                        propertiesObject.addProperty(property.getName(), (Number)property.getValDouble());
                        continue;
                    }
                    case 64089320: {
                        if (!upperCase.equals("CHECK")) {
                            continue;
                        }
                        propertiesObject.addProperty(property.getName(), Boolean.valueOf(property.getValBoolean()));
                        continue;
                    }
                    case 64305518: {
                        if (!upperCase.equals("COMBO")) {
                            continue;
                        }
                        propertiesObject.addProperty(property.getName(), property.getValString());
                        continue;
                    }
                    default: {
                        continue;
                    }
                }
            }
            object.add("Properties", (JsonElement)propertiesObject);
        }
        return object;
    }
    
    public void load(final JsonObject object, final boolean isConfig) {
        if (object.has("toggled")) {
            this.enabled = object.get("toggled").getAsBoolean();
        }
        if (!isConfig && object.has("key")) {
            this.setKey(object.get("key").getAsInt());
        }
        final List<Setting> settings = Client.instance.setmgr.getSettingsByMod(this);
        if (object.has("Properties") && settings != null && !settings.isEmpty()) {
            final JsonObject propertiesObject = object.getAsJsonObject("Properties");
            for (final Setting property : settings) {
                if (propertiesObject.has(property.getName())) {
                    final String upperCase;
                    switch ((upperCase = property.mode.toUpperCase()).hashCode()) {
                        case -1846317855: {
                            if (!upperCase.equals("SLIDER")) {
                                continue;
                            }
                            property.setValDouble(propertiesObject.get(property.getName()).getAsDouble());
                            continue;
                        }
                        case 64089320: {
                            if (!upperCase.equals("CHECK")) {
                                continue;
                            }
                            property.setValBoolean(propertiesObject.get(property.getName()).getAsBoolean());
                            continue;
                        }
                        case 64305518: {
                            if (!upperCase.equals("COMBO")) {
                                continue;
                            }
                            property.setValString(propertiesObject.get(property.getName()).getAsString());
                            continue;
                        }
                        default: {
                            continue;
                        }
                    }
                }
            }
        }
    }
}
