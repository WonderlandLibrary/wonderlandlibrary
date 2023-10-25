package wtf.evolution.module;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import wtf.evolution.Main;
import wtf.evolution.editor.DragManager;
import wtf.evolution.event.EventManager;
import wtf.evolution.helpers.MusicHelper;
import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.impl.EaseInOutQuad;
import wtf.evolution.helpers.render.Translate;

import wtf.evolution.module.impl.Render.ClickGui;
import wtf.evolution.module.impl.Render.ClientSound;
import wtf.evolution.notifications.NotificationType;
import wtf.evolution.settings.Config;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.*;

import java.util.ArrayList;

import static wtf.evolution.Main.c;

public class Module extends Config {
    public ModuleInfo info = this.getClass().getAnnotation(ModuleInfo.class);
    public String name;
    public int bind;
    public boolean state;

    private String displayName;
    public Animation animation = new EaseInOutQuad(250, 1);

    public Translate a = new Translate(0, 0);
    public Category category;
    public boolean opened = true;
    public boolean isRender = true;
    public Minecraft mc = Minecraft.getMinecraft();
    public Module() {
        name = info.name();
        bind = 0;
        category = info.type();
        displayName = name;
        state = false;
        System.out.println("Module: " + name + " loaded.");
    }


    public void onEnable() {

        //print in chat ingame that module is enabled
        if (Main.m.getModule(ClientSound.class).state) {
            if (!(this instanceof ClickGui))
                MusicHelper.playSound("skidproject/enable.wav", 1);
        }
        c.saveConfig("default");
        DragManager.saveDragData();
        Main.notify.call(name, "module was enabled", NotificationType.INFO);
        EventManager.register(this);
    }

    public void onDisable() {
        if (Main.m.getModule(ClientSound.class).state) {
            if (!(this instanceof ClickGui))
                MusicHelper.playSound("skidproject/disable.wav", 1);
        }
        c.saveConfig("default");
        DragManager.saveDragData();
        Main.notify.call(name, "module was disabled", NotificationType.INFO);
        EventManager.unregister(this);
    }

    public JsonObject save() {
        JsonObject object = new JsonObject();
        if (state)
            object.addProperty("state", state);
        if (bind > 0)
            object.addProperty("keyIndex", this.bind);
        JsonObject propertiesObject = new JsonObject();
        for (Setting set : getSettings()) {
            if (set instanceof BooleanSetting) {
                propertiesObject.addProperty(set.name, ((BooleanSetting) set).get());
            } else if (set instanceof ModeSetting) {
                propertiesObject.addProperty(set.name, ((ModeSetting) set).currentMode);
            } else if (set instanceof SliderSetting) {
                propertiesObject.addProperty(set.name, ((SliderSetting) set).current);
            } else if (set instanceof ColorSetting) {
                propertiesObject.addProperty(set.name, ((ColorSetting) set).get());
            } else if (set instanceof ListSetting) {
                String out = "";
                for (String s : ((ListSetting) set).selected) {
                    out += s + ",";
                }
                propertiesObject.addProperty(set.name, out);
            }
        }
        object.add("Settings", propertiesObject);
        return object;
    }

    public void setSuffix(String suffix) {
        this.displayName = name + (suffix.length() > 0 ? " " + ChatFormatting.GRAY + suffix : "");
    }

    public String getDisplayName() {
        return displayName;
    }

    public void load(JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                this.setState(object.get("state").getAsBoolean());
            }

            if (object.has("keyIndex")) {
                bind = (object.get("keyIndex").getAsInt());
            }

            for (Setting set : getSettings()) {
                JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null)
                    continue;
                if (propertiesObject == null)
                    continue;
                if (!propertiesObject.has(set.name))
                    continue;
                    if (set instanceof BooleanSetting) {
                        ((BooleanSetting) set).set(propertiesObject.get(set.name).getAsBoolean());
                    }  else if (set instanceof ModeSetting) {
                        ((ModeSetting) set).currentMode = propertiesObject.get(set.name).getAsString();
                    } else if (set instanceof SliderSetting) {
                        ((SliderSetting) set).current = (propertiesObject.get(set.name).getAsFloat());
                    } else if (set instanceof ColorSetting) {
                        ((ColorSetting) set).color = (propertiesObject.get(set.name).getAsInt());
                    } else if (set instanceof ListSetting) {
                        String[] split = propertiesObject.get(set.name).getAsString().split(",");
                        ((ListSetting) set).selected = new ArrayList<>();

                        for (String s : split) {
                            if (((ListSetting) set).list.contains(s)) {
                                ((ListSetting) set).selected.add(s);
                            }
                        }
                    }
                }
        }

    }

public void toggle() {
    state = ! state;
    if (state) {
        onEnable();
    } else {
        onDisable();
    }
}

public void setState(boolean state) {
    this.state = state;
    if (state) {
        onEnable();
    } else {
        onDisable();
    }
}
}
