// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules;

import java.util.Iterator;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.BooleanSetting;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.settings.impl.NumberSetting;
import com.google.gson.JsonObject;
import vip.Resolute.ui.notification.Notification;
import vip.Resolute.ui.notification.NotificationType;
import vip.Resolute.Resolute;
import vip.Resolute.events.Event;
import java.util.ArrayList;
import vip.Resolute.settings.Setting;
import java.util.List;
import vip.Resolute.util.render.Translate;
import vip.Resolute.settings.impl.KeybindSetting;
import net.minecraft.client.Minecraft;

public class Module
{
    public static Minecraft mc;
    public static int categoryCount;
    public Category category;
    public String name;
    public String suffix;
    public boolean toggled;
    public boolean hidden;
    public KeybindSetting keyBind;
    public static int enabledTicks;
    public String description;
    public Translate translate;
    public static boolean expanded;
    public int index;
    private float xanim;
    private float yanim;
    public List<Setting> settings;
    public static Module instance;
    
    public Module(final String name, final int key, final String description, final Category c) {
        this.keyBind = new KeybindSetting("Keybind", 0);
        this.description = "";
        this.translate = new Translate(0.0, 0.0);
        this.settings = new ArrayList<Setting>();
        this.name = name;
        this.description = description;
        this.getKeyBind().setCode(key);
        this.category = c;
        this.addSetting(this.getKeyBind());
        this.xanim = 0.0f;
        this.yanim = 0.0f;
        this.setup();
    }
    
    public void setKey(final int key) {
        this.keyBind.setCode(key);
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Translate getTranslate() {
        return this.translate;
    }
    
    public void setSuffix(final String suffix) {
        this.suffix = suffix;
    }
    
    public String getDisplayName() {
        return (this.suffix == null || this.suffix.isEmpty()) ? this.name : (this.name + " §7" + this.suffix);
    }
    
    public void onEvent(final Event e) {
    }
    
    public static Module getInstance() {
        return Module.instance;
    }
    
    public boolean isEnabled() {
        return this.isToggled();
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    public void toggle() {
        this.toggled = !this.toggled;
        if (this.toggled) {
            Module.enabledTicks = 0;
            this.onEnable();
            Resolute.getNotificationManager().add(new Notification("Enabled", this.getName(), 1500L, NotificationType.INFO));
        }
        else {
            this.onDisable();
            Resolute.getNotificationManager().add(new Notification("Disabled", this.getName(), 1500L, NotificationType.INFO));
        }
    }
    
    public void setState(final boolean state) {
        if (this.toggled != state) {
            this.toggled = state;
            if (state) {
                this.onEnable();
                Resolute.getNotificationManager().add(new Notification("Enabled", this.getName(), 1500L, NotificationType.INFO));
            }
            else {
                this.onDisable();
                Resolute.getNotificationManager().add(new Notification("Disabled", this.getName(), 1500L, NotificationType.INFO));
            }
        }
    }
    
    public JsonObject save() {
        final JsonObject object = new JsonObject();
        object.addProperty("toggled", Boolean.valueOf(this.isEnabled()));
        object.addProperty("key", (Number)this.getKey());
        object.addProperty("hidden", Boolean.valueOf(this.isHidden()));
        final List<Setting> properties = this.getSettings();
        if (!properties.isEmpty()) {
            final JsonObject propertiesObject = new JsonObject();
            for (final Setting<?> property : properties) {
                if (property instanceof NumberSetting) {
                    propertiesObject.addProperty(property.name, (Number)((NumberSetting)property).getValue());
                }
                else if (property instanceof ModeSetting) {
                    final ModeSetting enumProperty = (ModeSetting)property;
                    propertiesObject.add(property.name, (JsonElement)new JsonPrimitive(enumProperty.getMode()));
                }
                else if (property instanceof BooleanSetting) {
                    propertiesObject.addProperty(property.name, Boolean.valueOf(((BooleanSetting)property).isEnabled()));
                }
                else {
                    if (!(property instanceof ColorSetting)) {
                        continue;
                    }
                    final ColorSetting colorSetting = (ColorSetting)property;
                    propertiesObject.addProperty(property.name, (Number)colorSetting.getColor());
                }
            }
            object.add("Properties", (JsonElement)propertiesObject);
        }
        return object;
    }
    
    public void load(final JsonObject object) {
        if (object.has("toggled")) {
            this.setState(object.get("toggled").getAsBoolean());
        }
        if (object.has("key")) {
            this.setKey(object.get("key").getAsInt());
        }
        if (object.has("hidden")) {
            this.setHidden(object.get("hidden").getAsBoolean());
        }
        if (object.has("Properties") && !this.getSettings().isEmpty()) {
            final JsonObject propertiesObject = object.getAsJsonObject("Properties");
            for (final Setting<?> property : this.getSettings()) {
                if (propertiesObject.has(property.name)) {
                    if (property instanceof NumberSetting) {
                        ((NumberSetting)property).setValue(propertiesObject.get(property.name).getAsDouble());
                    }
                    else if (property instanceof ModeSetting) {
                        this.findEnumValue(property, propertiesObject);
                    }
                    else if (property instanceof BooleanSetting) {
                        ((BooleanSetting)property).setEnabled(propertiesObject.get(property.name).getAsBoolean());
                    }
                    else {
                        if (!(property instanceof ColorSetting)) {
                            continue;
                        }
                        ((ColorSetting)property).setValue(propertiesObject.get(property.name).getAsInt());
                    }
                }
            }
        }
    }
    
    private <T extends Enum<T>> void findEnumValue(final Setting<?> property, final JsonObject propertiesObject) {
        final ModeSetting enumProperty = (ModeSetting)property;
        final String value = propertiesObject.getAsJsonPrimitive(property.name).getAsString();
        List<String> values;
        for (int length = (values = enumProperty.getModes()).size(), i = 0; i < length; ++i) {
            final String possibleValue = values.get(i);
            if (possibleValue.equalsIgnoreCase(value)) {
                enumProperty.setSelected(possibleValue);
                break;
            }
        }
    }
    
    public void setEnabled() {
        this.toggled = true;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setToggled(final boolean toggled) {
        Module.enabledTicks = 0;
        this.toggled = toggled;
    }
    
    public void addSettings(final Setting... settings) {
        for (final Setting setting : settings) {
            this.addSetting(setting);
        }
    }
    
    public void addSetting(final Setting setting) {
        this.getSettings().add(setting);
    }
    
    public List<Setting> getSettings() {
        return this.settings;
    }
    
    public void setSettings(final List<Setting> settings) {
        this.settings = settings;
    }
    
    public boolean isExpanded() {
        return Module.expanded;
    }
    
    public void setExpanded(final boolean expanded) {
        Module.expanded = expanded;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setup() {
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public float getXAnim() {
        return this.xanim;
    }
    
    public void setXAnim(final float anim) {
        this.xanim = anim;
    }
    
    public float getYAnim() {
        return this.yanim;
    }
    
    public void setYAnim(final float anim) {
        this.yanim = anim;
    }
    
    public KeybindSetting getKeyBind() {
        return this.keyBind;
    }
    
    public int getKey() {
        return this.getKeyBind().getCode();
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    static {
        Module.mc = Minecraft.getMinecraft();
    }
    
    public enum Category
    {
        COMBAT("Combat"), 
        MOVEMENT("Movement"), 
        PLAYER("Player"), 
        RENDER("Render"), 
        EXPLOIT("Exploit");
        
        public float offset;
        public static Category instance;
        public String name;
        public int moduleIndex;
        public int posX;
        public int posY;
        public boolean expanded;
        
        private Category(final String name) {
            this.name = name;
            this.posX = 150 + Module.categoryCount * 95;
            this.posY = 85;
            this.offset = 0.0f;
            this.expanded = true;
            ++Module.categoryCount;
        }
    }
}
