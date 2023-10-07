/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import me.arithmo.Client;
import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.module.Module;
import me.arithmo.module.data.Options;
import me.arithmo.module.data.Setting;
import me.arithmo.module.data.SettingsMap;
import me.arithmo.util.StringConversions;
import me.arithmo.util.misc.ChatUtil;
import net.minecraft.util.EnumChatFormatting;

public class Settings
extends Command {
    public Settings(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        Module module = null;
        if (args.length > 0) {
            module = Client.getModuleManager().get(args[0]);
        }
        if (module == null) {
            this.printUsage();
            return;
        }
        if (args.length == 1) {
            SettingsMap moduleSettings = module.getSettings();
            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 [" + (Object)((Object)EnumChatFormatting.WHITE) + module.getName() + (Object)((Object)EnumChatFormatting.DARK_GRAY) + "] - Settings: " + (Object)((Object)EnumChatFormatting.WHITE) + moduleSettings.size());
            for (Setting setting : moduleSettings.values()) {
                if (setting == null) continue;
                this.printSetting(setting);
            }
        } else if (args.length >= 2) {
            Setting setting = this.getSetting(module.getSettings(), args[1]);
            if (setting == null) {
                this.printUsage();
                return;
            }
            if (args.length == 2) {
                this.printSetting(setting);
            } else if (args.length >= 3) {
                String objText = args[2];
                try {
                    if (setting.getValue() instanceof Number) {
                        Object newValue = StringConversions.castNumber(objText, setting.getValue());
                        if (newValue != null) {
                            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 " + module.getName() + "'s " + setting.getName().toLowerCase() + " has been changed to: " + (Object)((Object)EnumChatFormatting.DARK_AQUA) + newValue);
                            setting.setValue(newValue);
                            module.getSettings();
                            module.save();
                            return;
                        }
                    } else {
                        if (setting.getValue().getClass().equals(String.class)) {
                            String str = objText;
                            if (args.length > 3) {
                                for (int i = 3; i < args.length; ++i) {
                                    str = str + " " + args[i];
                                }
                            }
                            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 " + module.getName() + "'s " + setting.getName().toLowerCase() + " has been changed to: " + (Object)((Object)EnumChatFormatting.DARK_RED) + "\"" + str + "\"");
                            setting.setValue(str);
                            module.getSettings();
                            module.save();
                            return;
                        }
                        if (setting.getValue().getClass().equals(Boolean.class)) {
                            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 " + module.getName() + "'s " + setting.getName().toLowerCase() + " has been changed to: " + (Object)((Object)EnumChatFormatting.DARK_RED) + objText);
                            setting.setValue(Boolean.parseBoolean(objText));
                            module.getSettings();
                            module.save();
                            return;
                        }
                        if (setting.getValue() instanceof Options) {
                            for (String string : ((Options)setting.getValue()).getOptions()) {
                                if (!string.toLowerCase().equalsIgnoreCase(objText)) continue;
                                ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 " + module.getName() + "'s " + ((Options)setting.getValue()).getName() + " has been changed to: " + (Object)((Object)EnumChatFormatting.DARK_RED) + string);
                                ((Options)setting.getValue()).setSelected(string);
                                return;
                            }
                        }
                    }
                }
                catch (Exception str) {
                    // empty catch block
                }
                ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 ERROR: Could not apply the value '" + objText + "' to " + module.getName() + "'s " + setting.getName());
            }
        }
    }

    private Setting getSetting(SettingsMap map, String settingText) {
        if (map.containsKey(settingText = settingText.toUpperCase())) {
            return (Setting)map.get(settingText);
        }
        for (String key : map.keySet()) {
            if (!key.startsWith(settingText)) continue;
            return (Setting)map.get(key);
        }
        return null;
    }

    private void printSetting(Setting setting) {
        String typeStr;
        if (setting == null) {
            this.printUsage();
            return;
        }
        String string = typeStr = setting.getType() == null ? setting.getValue().getClass().getSimpleName() : setting.getType().getTypeName();
        if (typeStr.contains(".")) {
            typeStr = typeStr.substring(typeStr.lastIndexOf(".") + 1);
        }
        String settingText = (Object)((Object)EnumChatFormatting.GRAY) + setting.getName().toLowerCase() + "\u00a78: " + (Object)((Object)EnumChatFormatting.RESET) + (Object)((Object)EnumChatFormatting.RED) + setting.getValue();
        if (setting.getValue().getClass().equals(Boolean.class)) {
            settingText = (Object)((Object)EnumChatFormatting.GRAY) + setting.getName().toLowerCase() + "\u00a78: " + (Object)((Object)EnumChatFormatting.RESET) + (Object)((Object)EnumChatFormatting.RED) + (Boolean.parseBoolean(setting.getValue().toString()) ? "True" : "False");
        }
        if (setting.getValue() instanceof Options) {
            settingText = (Object)((Object)EnumChatFormatting.GRAY) + setting.getName().toLowerCase() + "\u00a78: " + (Object)((Object)EnumChatFormatting.RESET) + (Object)((Object)EnumChatFormatting.RED) + ((Options)setting.getValue()).getSelected() + (Object)((Object)EnumChatFormatting.DARK_GRAY) + " " + Arrays.toString(((Options)setting.getValue()).getOptions());
        }
        ChatUtil.printChat(settingText);
    }

    @Override
    public String getUsage() {
        return "set <Module> " + (Object)((Object)EnumChatFormatting.ITALIC) + "[optional]" + (Object)((Object)EnumChatFormatting.RESET) + "<Option> " + (Object)((Object)EnumChatFormatting.ITALIC) + "[optional]" + (Object)((Object)EnumChatFormatting.RESET) + "<Value>";
    }

    public void onEvent(Event event) {
    }
}

