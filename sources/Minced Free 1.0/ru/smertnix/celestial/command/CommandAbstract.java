package ru.smertnix.celestial.command;


import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.other.ChatUtils;


public abstract class CommandAbstract implements Command, Helper {


    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;

    public CommandAbstract(String name, String description, String usage, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
    }

    public void usage() {
        ChatUtils.addChatMessage("§fКоманда §c\"" + usage + "\"§f не найдена");
        ChatUtils.addChatMessage("§fПожалуйста напишите §c.help§f для получения списка всех команд!");
    }

    public String getUsage() {
        return this.usage;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String[] getAliases() {
        return this.aliases;
    }
}
