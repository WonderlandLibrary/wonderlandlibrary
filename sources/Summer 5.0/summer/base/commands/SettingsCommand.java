package summer.base.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import summer.Summer;
import summer.base.manager.Command;
import summer.base.manager.config.Cheats;
import summer.base.utilities.ChatUtils;
import summer.cheat.guiutil.Setting;

import java.util.ArrayList;

public class SettingsCommand implements Command {

    @Override
    public boolean run(String[] args) {
        if (args.length == 3) {
            Cheats cheats = Summer.INSTANCE.cheatManager.getModuleByName(args[0]);
            if (cheats != null) {
                Setting setting = Summer.INSTANCE.settingsManager.getSettingByName(cheats, args[1]);
                if (setting != null) {
                    if (setting.isCheck()) {
                        if (validSetting(setting, args[2])) {
                            setting.setValBoolean(Boolean.parseBoolean(args[2]));
                            ChatUtils.sendMessage(setting.getName() + " set to " + args[2] + ".");
                        } else {
                            ChatUtils.sendMessage(setting.getName() + ": [True or False].");
                        }
                        return true;
                    } else if (setting.isSlider()) {
                        if (validSetting(setting, args[2])) {
                            setting.setValDouble(Double.parseDouble(args[2]));
                            ChatUtils.sendMessage(setting.getName() + " set to " + args[2] + ".");
                        } else {
                            ChatUtils.sendMessage(setting.getName() + ": [min = " + setting.getMin() + " , max = " + setting.getMax() + "].");
                        }
                        return true;
                    } else if (setting.isCombo()) {
                        if (validSetting(setting, args[2])) {
                            setting.setValString(args[2]);
                            ChatUtils.sendMessage(setting.getName() + " set to " + args[2] + ".");
                        } else {
                            ChatUtils.sendMessage(setting.getName() + ": " + setting.getOptions().toString() + ".");
                        }
                        return true;
                    }
                }
            }

        }
        return false;
    }

    private boolean validSetting(Setting setting, String text) {
        try {
            if (setting.isSlider()) {
                if (setting.onlyInt())
                    return Integer.parseInt(text) >= setting.getMin() && Integer.parseInt(text) <= setting.getMax();
                else
                    return Double.parseDouble(text) >= setting.getMin() && Double.parseDouble(text) <= setting.getMax();
            } else if (setting.isCombo()) {
                return contains(setting.getOptions(), text);
            } else if (setting.isCheck()) {
                return text.equalsIgnoreCase("true") || text.equalsIgnoreCase("false");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean contains(ArrayList<String> arrayList, String text) {
        for (String s : arrayList) {
            if (s.equalsIgnoreCase(text))
                return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return ChatFormatting.WHITE + "s | setting <module> <setting> <value>";
    }
}