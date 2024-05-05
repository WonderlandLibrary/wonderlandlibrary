package intent.AquaDev.aqua.command;

import intent.AquaDev.aqua.command.Command;
import intent.AquaDev.aqua.command.impl.commands.bind;
import intent.AquaDev.aqua.command.impl.commands.config;
import intent.AquaDev.aqua.command.impl.commands.toggle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandSystem {
    public static List<Command> commands = new ArrayList();
    public final String Chat_Prefix = ".";

    public CommandSystem() {
        this.addCommand((Command)new bind());
        this.addCommand((Command)new toggle());
        this.addCommand((Command)new config());
    }

    public void addCommand(Command cmd) {
        commands.add((Object)cmd);
    }

    public boolean execute(String text) {
        if (!text.startsWith(this.Chat_Prefix)) {
            return false;
        }
        text = text.substring(1);
        Object[] arguments = text.split(" ");
        for (Command cmd : commands) {
            if (!cmd.getName().equalsIgnoreCase(arguments[0])) continue;
            String[] args = (String[])Arrays.copyOfRange((Object[])arguments, (int)1, (int)arguments.length);
            cmd.execute(args);
            return true;
        }
        return false;
    }
}
