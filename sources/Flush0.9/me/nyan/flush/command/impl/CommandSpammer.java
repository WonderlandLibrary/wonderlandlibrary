package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.module.impl.misc.Spammer;
import me.nyan.flush.utils.other.ChatUtils;

public class CommandSpammer extends Command {
    private final Spammer module;

    public CommandSpammer() {
        super("Spammer", "Sets or gets spammer module's message", "Spammer get | Spammer set <message>");
        module = flush.getModuleManager().getModule(Spammer.class);
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 0) {
            sendSyntaxHelpMessage();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "get":
                module.load();
                ChatUtils.println("§9Current spammer message: §F" + module.getMessage());
                break;

            case "set":
                if (args.length > 1) {
                    module.setMessage(buildStringFromArgs(args, 1));
                    module.save();
                    ChatUtils.println("§9Changed spammer message to: §F" + module.getMessage());
                } else {
                    sendSyntaxHelpMessage();
                }
                break;

            default:
                sendSyntaxHelpMessage();
                break;
        }
    }
}
