package wtf.evolution.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import org.lwjgl.input.Keyboard;
import wtf.evolution.Main;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;
import wtf.evolution.module.Module;

import java.util.ArrayList;
import java.util.List;

@CommandInfo(name = "bind")
public class BindCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        Main.m.getModule(args[1]).bind = Keyboard.getKeyIndex(args[2].toUpperCase());
        ChatUtil.print("Клавиша " + args[2] + " назначена на " + args[1]);
    }

    @Override
    public void onError() {
        super.onError();
        ChatUtil.print(ChatFormatting.RED + "Ошибка в команде -> bind <имя модуля> <клавиша>");
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
        return null;
    }
}
