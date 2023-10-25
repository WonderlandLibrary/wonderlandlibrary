package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.evolution.Main;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.module.Module;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(name = "set")
public class SettingCommand extends Command {
    @Override
    public void execute(String[] args) {
        super.execute(args);
        Setting s = null;
        for (Setting set : Main.m.getModule(args[1]).getSettings()) {
            if (set.name.contains(args[3])) {
                s = set;
            }
        }
        if (s == null) {
            ChatUtil.print(ChatFormatting.RED + "Нет такой настройки.");
            return;
        }
        if (args[2].contains("boolean")) {
            ((BooleanSetting) s).set(Boolean.parseBoolean(args[4]));
        }
        else if (args[2].contains("slider")) {
            ((SliderSetting) s).current = (Float.parseFloat(args[4]));
        }
        else if (args[2].contains("mode")) {
            if ( ((ModeSetting) s).modes.contains(args[4])) {
                ((ModeSetting) s).currentMode = (args[4]);
            }
            else {
                ChatUtil.print(ChatFormatting.RED + "Нет такого мода.");
                return;
            }
        }

        ChatUtil.print("Установлено значение " + args[4] + " на " + args[3] + " для модуля " + args[1]);

    }

    @Override
    public void onError() {
        super.onError();
        ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> set <имя модуля> <тип настройки> <имя настройки> <значение>");
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        if (args.length <= 1) {
            List<String> modules = new ArrayList<>();
            for (Module m : Main.m.m) {
                modules.add(m.name);
            }
            return modules;
        }
        else if (args.length <= 2) {
            List<String> types = new ArrayList<>();
            types.add("boolean");
            types.add("slider");
            types.add("mode");
            return types;
        }
        else if (args.length <= 3) {
            List<String> types = new ArrayList<>();
            for (Setting s : Main.m.getModule(args[1]).getSettings()) {
                if (s.name.contains(" ")) continue;
                types.add(s.name);
            }
            return types;
        }

        return null;
    }

}
