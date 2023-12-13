package koks.api.registry.command;

import koks.api.registry.Registry;
import koks.api.registry.module.Module;
import koks.command.*;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */
public class CommandRegistry implements Registry {

    private static final ArrayList<Command> COMMANDS = new ArrayList<Command>();

    @Override
    public void initialize() {
        final Reflections reflections = new Reflections("koks");
        reflections.getTypesAnnotatedWith(Command.Info.class).forEach(aClass -> {
            try {
                addCommand((Command) aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        COMMANDS.sort(Comparator.comparing(Command::getName));
    }

    public void addCommand(Command command) {
        COMMANDS.add(command);
    }

    public static ArrayList<Command> getCommands() {
        return COMMANDS;
    }
}
