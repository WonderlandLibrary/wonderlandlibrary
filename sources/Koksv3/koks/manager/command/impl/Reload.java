package koks.manager.command.impl;

import koks.Koks;
import koks.manager.command.Command;
import koks.manager.command.CommandInfo;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 12:25
 */

@CommandInfo(name = "reload", alias = "rl")
public class Reload extends Command {

    @Override
    public void execute(String[] args) {
        if (args.length != 0)
            return;

        Koks.getKoks().stopClient();
        Koks.getKoks().startClient();
    }

}