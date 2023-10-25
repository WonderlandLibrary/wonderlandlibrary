package wtf.evolution.command.impl;

import wtf.evolution.Main;
import wtf.evolution.command.Command;
import wtf.evolution.command.CommandInfo;
import wtf.evolution.helpers.ChatUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandInfo(name = "config")
public class ConfigCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        if (args[1].startsWith("load")) {
            Main.c.loadConfig(args[2]);
            ChatUtil.print("Конфигурация загружена.");
        }
        if (args[1].startsWith("save")) {
            Main.c.saveConfig(args[2]);
            ChatUtil.print("Конфигурация сохранена.");
        }
        if (args[1].startsWith("list")) {
            File file = new File(mc.gameDir + "\\evolution\\configs");
            for (File s : file.listFiles()) {
                ChatUtil.print(s.getName());
            }
        }
    }

    @Override
    public List<String> getSuggestions(String[] args) {
        try {
            if (args[1].startsWith("load")) {
                File file = new File(mc.gameDir + "\\evolution\\configs");
                return Arrays.asList(file.listFiles()).stream().map(File::getName).collect(Collectors.toList());
            }
            if (args[1].startsWith("save")) {
                File file = new File(mc.gameDir + "\\evolution\\configs");
                return Arrays.asList(file.listFiles()).stream().map(File::getName).collect(Collectors.toList());
            }
            return Arrays.asList("load", "save", "list");
        } catch (Exception ex) {
            return null;
        }
    }
}
