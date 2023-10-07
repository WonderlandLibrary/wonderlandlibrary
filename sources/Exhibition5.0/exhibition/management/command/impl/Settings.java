// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import java.util.HashMap;
import java.util.Iterator;
import exhibition.module.data.SettingsMap;
import exhibition.module.Module;
import exhibition.util.StringConversions;
import exhibition.module.data.Setting;
import exhibition.util.misc.ChatUtil;
import net.minecraft.util.EnumChatFormatting;
import exhibition.Client;
import exhibition.management.command.Command;

public class Settings extends Command
{
    public Settings(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
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
            final SettingsMap moduleSettings = module.getSettings();
            ChatUtil.printChat("§4[§cE§4]§8 [" + EnumChatFormatting.DARK_RED + module.getName() + EnumChatFormatting.DARK_GRAY + "] - Settings: " + EnumChatFormatting.DARK_RED + moduleSettings.size());
            for (final Setting setting : ((HashMap<K, Setting>)moduleSettings).values()) {
                if (setting != null) {
                    this.printSetting(setting);
                }
            }
        }
        else if (args.length >= 2) {
            final Setting setting2 = this.getSetting(module.getSettings(), args[1]);
            if (setting2 == null) {
                this.printUsage();
                return;
            }
            if (args.length == 2) {
                this.printSetting(setting2);
            }
            else if (args.length >= 3) {
                final String objText = args[2];
                try {
                    if (setting2.getValue() instanceof Number) {
                        final Object newValue = StringConversions.castNumber(objText, setting2.getValue());
                        if (newValue != null) {
                            ChatUtil.printChat("§4[§cE§4]§8 " + module.getName() + "'s " + setting2.getName().toLowerCase() + " has been changed to: " + EnumChatFormatting.DARK_AQUA + newValue);
                            setting2.setValue(newValue);
                            module.getSettings();
                            module.save();
                            return;
                        }
                    }
                    else {
                        if (setting2.getValue().getClass().equals(String.class)) {
                            final String parsed = objText.toString().replaceAll("_", " ");
                            ChatUtil.printChat("§4[§cE§4]§8 " + module.getName() + "'s " + setting2.getName().toLowerCase() + " has been changed to: " + EnumChatFormatting.DARK_RED + parsed);
                            setting2.setValue(parsed);
                            module.getSettings();
                            module.save();
                            return;
                        }
                        if (setting2.getValue().getClass().equals(Boolean.class)) {
                            ChatUtil.printChat("§4[§cE§4]§8 " + module.getName() + "'s " + setting2.getName().toLowerCase() + " has been changed to: " + EnumChatFormatting.DARK_RED + objText);
                            setting2.setValue(Boolean.parseBoolean(objText));
                            module.getSettings();
                            module.save();
                            return;
                        }
                    }
                }
                catch (Exception ex) {}
                ChatUtil.printChat("§4[§cE§4]§8 ERROR: Could not apply the value '" + objText + "' to " + module.getName() + "'s " + setting2.getName());
            }
        }
    }
    
    private Setting getSetting(final SettingsMap map, String settingText) {
        settingText = settingText.toUpperCase();
        if (map.containsKey(settingText)) {
            return ((HashMap<K, Setting>)map).get(settingText);
        }
        for (final String key : ((HashMap<String, V>)map).keySet()) {
            if (key.startsWith(settingText)) {
                return ((HashMap<K, Setting>)map).get(key);
            }
        }
        return null;
    }
    
    private void printSetting(final Setting setting) {
        if (setting == null) {
            this.printUsage();
            return;
        }
        String typeStr = (setting.getType() == null) ? setting.getValue().getClass().getSimpleName() : setting.getType().getTypeName();
        if (typeStr.contains(".")) {
            typeStr = typeStr.substring(typeStr.lastIndexOf(".") + 1);
        }
        final String settingText = EnumChatFormatting.GRAY + "" + setting.getName().toLowerCase() + ": " + EnumChatFormatting.RESET + EnumChatFormatting.DARK_RED + setting.getValue();
        ChatUtil.printChat(settingText);
    }
    
    @Override
    public String getUsage() {
        return "set <Module> " + EnumChatFormatting.ITALIC + "[optional]" + EnumChatFormatting.RESET + "<Option> " + EnumChatFormatting.ITALIC + "[optional]" + EnumChatFormatting.RESET + "<Value>";
    }
}
