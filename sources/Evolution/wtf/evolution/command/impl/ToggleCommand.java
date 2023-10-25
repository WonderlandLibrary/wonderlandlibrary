package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import wtf.evolution.Main;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.module.Module;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(name = "toggle")
public class ToggleCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        Main.m.getModule(args[1]).toggle();
    }

    @Override
    public void onError() {
        super.onError();
        ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> toggle <имя модуля>");
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        List<String> modules = new ArrayList<>();
        for (Module m : Main.m.m) {
            modules.add(m.name);
        }
        return modules;
    }
}
