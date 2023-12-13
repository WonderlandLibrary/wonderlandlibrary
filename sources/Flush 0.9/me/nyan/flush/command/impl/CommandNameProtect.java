package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.module.impl.misc.NameProtect;
import me.nyan.flush.utils.other.ChatUtils;

public class CommandNameProtect extends Command {
    private final NameProtect module = flush.getModuleManager().getModule(NameProtect.class);

    public CommandNameProtect() {
        super("NameProtect", "Manages the NameProtect module", "NameProtect set <text> | get");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "set":
                    if (args.length > 1) {
                        module.setCustomName(buildStringFromArgs(args, 1, true));
                        ChatUtils.println("§9Changed NameProtect name to: §r" + module.getCustomName());
                        break;
                    }

                    sendSyntaxHelpMessage();
                    break;

                case "get":
                    ChatUtils.println("§9Current NameProtect name is: §r" + module.getCustomName());
                    break;

                default:
                    sendSyntaxHelpMessage();
                    break;
            }
            return;
        }

        sendSyntaxHelpMessage();
    }
}
