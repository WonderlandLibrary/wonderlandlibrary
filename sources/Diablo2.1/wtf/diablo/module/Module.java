package wtf.diablo.module;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import wtf.diablo.Diablo;
import wtf.diablo.gui.clickGui.ClickGui;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.module.impl.Render.Hud;
import wtf.diablo.settings.Setting;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.KeybindSetting;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.animations.Animation;
import wtf.diablo.utils.chat.ChatUtil;
import wtf.diablo.utils.render.ChatColor;
import wtf.diablo.utils.render.ColorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
public class Module {
    public Minecraft mc = Minecraft.getMinecraft();
    private String name;
    private Category category;
    private String suffix = "";
    private String desc;
    private int key;
    private boolean toggled = false;
    private ServerType serverType;
    public ArrayList<Setting> settings = new ArrayList<>();
    private boolean canBeToggled = true;
    private boolean protectedModule = false;
    public Animation animation = new Animation();

    public Module(String name, String desc, Category category, ServerType serverType) {
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.serverType = serverType;
        this.settings.add(new KeybindSetting(this));
    }

    //Called when you toggle
    public void startAnimation() {
        animation.setReverse(!toggled);
        animation.setSpeed(0.2);
        animation.setAmount(50);
        animation.start();
    }

    //Call when rendering module in hud
    public void updateRender() {
        animation.updateAnimation();
    }

    //If should count as toggled
    public boolean isAnimating() {
        return !animation.hasFinished();
    }

    public void setProtected(boolean val) {
        this.protectedModule = val;
    }

    public boolean isProtected() {
        return protectedModule;
    }

    public void addSettings(Setting... settings) {
        this.settings.clear();
        this.settings.addAll(Arrays.asList(settings));
        this.settings.add(new KeybindSetting(this));
    }

    public String getDesc() {
        return desc;
    }

    public void setTogglable(boolean value) {
        canBeToggled = value;
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public boolean isToggled() {
        return toggled;
    }

    public Category getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        startAnimation();
        if (this.toggled) {
            onEnable();
        } else {
            this.onDisable();
        }
    }

    public boolean toggle() {
        setToggled(!toggled);
        return toggled;

    }

    public void onEnable() {
        Diablo.eventBus.register(this);
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        try{
        Diablo.eventBus.unregister(this);
    }catch(Exception ignored){}}

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public Setting findSettingByName(String name) {
        for (Setting setting : getSettings()) {
            if (Objects.equals(setting.getName(), name))
                return setting;
        }
        return null;
    }
    public boolean getBool(String name) {
        Setting setting = findSettingByName(name);
        if (setting instanceof BooleanSetting) {
            return ((BooleanSetting) setting).getValue();
        }
        return false;
    }


    public void updateSettings() {
    } //Called on ClickGUI

    public void load(JsonObject obj, boolean loadKey) {
        obj.entrySet().forEach(data -> {
            switch (data.getKey()) {
                case "name": {
                    break;
                }
                case "enabled": {
                    if (!(isToggled() && data.getValue().getAsBoolean()) && !(!isToggled() && !data.getValue().getAsBoolean()))
                        setToggled(data.getValue().getAsBoolean());
                    break;
                }
                case "keybind":
                    if (loadKey)
                        this.setKey(data.getValue().getAsInt());
                    break;
            }
            Setting val = findSettingByName(String.valueOf(data.getKey()));
            if (val != null) {
                if (val instanceof NumberSetting) {
                    ((NumberSetting) val).setValue(Double.parseDouble(data.getValue().getAsString()));
                }
                if (val instanceof BooleanSetting) {
                    ((BooleanSetting) val).setValue(Boolean.parseBoolean(data.getValue().getAsString()));
                }
                if (val instanceof ModeSetting) {
                    ((ModeSetting) val).setMode(Integer.parseInt(data.getValue().getAsString()));
                }
            }
        });
    }

    public JsonObject save() {
        JsonObject json = new JsonObject();
        json.addProperty("name", this.name);
        json.addProperty("enabled", this.toggled);
        json.addProperty("keybind", this.key);
        getSettings().forEach(value -> {
            try {
                if (value instanceof KeybindSetting)
                    return;
                json.addProperty(value.getName(), String.valueOf(value.getObjectValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return json;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFormattedName(Boolean seperator) {
        if (!Objects.equals(this.suffix, "")) {
            return this.name + ColorUtil.COLORRESET + (seperator ? " - " : " ") + this.suffix;
        } else {
            return this.name;
        }
    }

    public String getFormattedName() {
        String suffix = "";
        switch (Hud.suffixMode.getMode()) {
            case "Dash":
                suffix = ColorUtil.COLORRESET + " - " + this.suffix;
                break;
            case "Boxed":
                suffix = ChatColor.WHITE + " [" + ColorUtil.COLORRESET + this.suffix + ChatColor.WHITE + "]";
                break;
            case "None":
                suffix = ColorUtil.COLORRESET + " " + this.suffix;
                break;
        }

        if (!Objects.equals(this.suffix, "")) {
            return this.name + suffix;
        } else {
            return this.name;
        }
    }
}

