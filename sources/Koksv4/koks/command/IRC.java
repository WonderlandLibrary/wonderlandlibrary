package koks.command;

import koks.Koks;
import koks.api.registry.command.Command;
import koks.api.registry.module.Module;

import java.util.Arrays;

/**
 * @author kroko
 * @created on 12.02.2021 : 23:21
 */
@Command.Info(name = "IRC", description = "IRC Settings")
public class IRC extends Command {
    @Override
    public boolean execute(String[] args) {
        String command = "";
        if(args.length > 0) {
            for (int i = 0; i < args.length; i++)
                command += args[i] + " ";
            command = command.substring(0, command.length() - 1);
        }
        Koks.getKoks().irc.executeCommand(".irc " + command);
        return true;
    }
}
