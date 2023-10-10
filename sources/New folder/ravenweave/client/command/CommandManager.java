package ravenweave.client.command;

import ravenweave.client.clickgui.raven.Terminal;
import ravenweave.client.command.commands.*;
import ravenweave.client.main.Raven;
import ravenweave.client.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public List<Command> commandList;
    public List<Command> sortedCommandList;

    public CommandManager() {
        this.commandList = new ArrayList<>();
        this.sortedCommandList = new ArrayList<>();
        this.addCommand(new Help());
        this.addCommand(new SetKey());
        this.addCommand(new ConfigCommand());
        this.addCommand(new Clear());
        this.addCommand(new Debug());
        this.addCommand(new Duels());
        this.addCommand(new Fakechat());
        this.addCommand(new Ping());
        this.addCommand(new Shoutout());
        this.addCommand(new Friends());
        this.addCommand(new CFakeHud());
        this.addCommand(new CHideModule());

    }

    public void addCommand(Command c) {
        this.commandList.add(c);
    }

    public List<Command> getCommandList() {
        return this.commandList;
    }

    public Command getCommandByName(String name) {
        for (Command command : this.commandList) {
            if (command.getName().equalsIgnoreCase(name))
                return command;
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(name))
                    return command;
            }
        }
        return null;
    }

    public void noSuchCommand(String name) {
        Terminal.print("Command '" + name + "' not found!");
    }

    public void executeCommand(String commandName, String[] args) {
        Command command = Raven.commandManager.getCommandByName(commandName);

        if (command == null) {
            this.noSuchCommand(commandName);
            return;
        }

        command.onCall(args);
    }

    public void sort() {
        this.sortedCommandList.sort((o1, o2) -> Utils.mc.fontRendererObj.getStringWidth(o2.getName())
                - Utils.mc.fontRendererObj.getStringWidth(o1.getName()));
    }
}
