package summer.base.manager.config;

import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringUtils;
import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.Event;
import summer.cheat.eventsystem.EventManager;
import summer.base.utilities.TimerUtils;
import summer.base.utilities.Translate;

public class Cheats implements Serializable {

    protected static Minecraft mc = Minecraft.getMinecraft();

    private boolean selected;
    public boolean hidden = false;
    public String name, displayName, description;
    public Selection category;
    public int key;
    public boolean toggled;
    private boolean state;
    public Translate translate = new Translate(0.0F, 0.0F);
    public int transition;

    TimerUtils antiNoise = new TimerUtils();

    // test

    public Cheats(String nm, String de, Selection ca, boolean h) {
        this.name = nm;
        this.description = de;
        this.category = ca;
        this.hidden = h;

        toggled = false;
        this.onSetup();
    }

    public Cheats(String nm, String de, Selection ca) {
        this.name = nm;
        this.description = de;
        this.category = ca;
        this.hidden = false;

        toggled = false;
        this.onSetup();
    }

    public void onSetup() {
    }

    public void toggle() {
        setToggled(!isToggled());
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onUpdate() {
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSuffix(String suffix) {
        this.setDisplayName(this.getName() + " " + EnumChatFormatting.GRAY + suffix);
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggle) {
        if (this.toggled != toggle) {
            toggled = toggle;
            onToggle();
            if (toggled) {
                if (Minecraft.thePlayer != null)
                    Minecraft.thePlayer.playSound("random.click", 0.5F, 1.0F);
                EventManager.register(this);
                this.transition = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.getName())) - 10;
                this.onEnable();
            } else {
                if (Minecraft.thePlayer != null)
                    Minecraft.thePlayer.playSound("random.click", 0.4F, 0.8F);
                EventManager.unregister(this);
                this.transition = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.getName())) + 10;
                this.onDisable();
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Selection getCategory() {
        return this.category;
    }

    public void updateSettings() {
    }

    public int getKey() {
        return this.key;
    }

    public void onToggle() {
    }

    public void onEvent(Event e) {

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isState() {
        return toggled;
    }

    @Override
    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("toggled", isToggled());
        object.addProperty("key", getKey());
        List<Setting> properties = Summer.INSTANCE.settingsManager.getSettingsByMod(this);
        if (properties != null && !properties.isEmpty()) {
            JsonObject propertiesObject = new JsonObject();

            for (Setting property : properties) {
                switch (property.mode.toUpperCase()) {
                    case "CHECK": {
                        propertiesObject.addProperty(property.getName(), property.getValBoolean());
                        break;
                    }
                    case "COMBO": {
                        propertiesObject.addProperty(property.getName(), property.getValString());
                        break;
                    }
                    case "SLIDER": {
                        propertiesObject.addProperty(property.getName(), property.getValDouble());
                        break;
                    }
                }
            }

            object.add("Properties", propertiesObject);
        }
        return object;
    }

    @Override
    public void load(JsonObject object) {
        if (object.has("toggled"))
            setToggled(object.get("toggled").getAsBoolean());

        if (object.has("key"))
            setKey(object.get("key").getAsInt());

        List<Setting> settings = Summer.INSTANCE.settingsManager.getSettingsByMod(this);

        if (object.has("Properties") && settings != null && !settings.isEmpty()) {
            JsonObject propertiesObject = object.getAsJsonObject("Properties");
            for (Setting property : settings) {
                if (propertiesObject.has(property.getName())) {
                    switch (property.mode.toUpperCase()) {
                        case "CHECK": {
                            property.setValBoolean(propertiesObject.get(property.getName()).getAsBoolean());
                            break;
                        }
                        case "COMBO": {
                            property.setValString(propertiesObject.get(property.getName()).getAsString());
                            break;
                        }
                        case "SLIDER": {
                            property.setValDouble(propertiesObject.get(property.getName()).getAsDouble());
                            break;
                        }
                    }
                }
            }
        }
    }

    public int getTransition() {
        return this.transition;
    }

    public void setTransition(final int transition) {
        this.transition = transition;
    }

}
