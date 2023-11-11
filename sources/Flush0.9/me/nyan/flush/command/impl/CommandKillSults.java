package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.module.impl.misc.KillSults;
import me.nyan.flush.utils.other.ChatUtils;

public class CommandKillSults extends Command {
    private final KillSults ks = flush.getModuleManager().getModule(KillSults.class);

    public CommandKillSults() {
        super("KillSults", "Killsults command", "killsults add <killsult> | " +
                "killsults remove <killsult> | killsults list | killsults clear", "ks");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "list":
                    ks.load();
                    if (!ks.getKillsults().isEmpty()) {
                        ChatUtils.println("§9These killsults are in your killsults file:");
                        ks.getKillsults().forEach(ChatUtils::println);
                        break;
                    }

                    ChatUtils.println("§9There are no killsults in your killsults file.");
                    break;

                case "clear":
                    ks.getKillsults().clear();
                    ks.save();
                    ChatUtils.println("§9Cleared all killsults.");
                    break;

                case "add":
                    if (args.length > 1) {
                        String killsult = buildStringFromArgs(args, 1);
                        ks.getKillsults().add(killsult);
                        ks.save();
                        ChatUtils.println("§9Added killsult \"" + killsult + "\".");
                        break;
                    }

                    sendSyntaxHelpMessage();
                    break;

                case "remove":
                    if (args.length > 1) {
                        String killsult1 = buildStringFromArgs(args, 1);
                        ks.load();
                        if (ks.getKillsults().contains(killsult1)) {
                            ks.getKillsults().remove(killsult1);
                            ks.save();
                            ChatUtils.println("§9Removed killsult \"" + killsult1 + "\".");
                            break;
                        }

                        ChatUtils.println("§9Killsult \"" + killsult1 + "\" doesn't exist.");
                        break;
                    }

                    sendSyntaxHelpMessage();
                default:
                    sendSyntaxHelpMessage();
                    break;
            }
            return;
        }

        sendSyntaxHelpMessage();
    }
}
