package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.utils.other.ChatUtils;

public class Target extends Command {
    public Target() {
        super("Target", "Makes aura target a player.", "target add <playerName> | target remove <playerName> | target list | target clear");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "add":
                    if (args.length == 2) {
                        String playername = args[1];
                        if (flush.getTargetManager().addTarget(playername)) {
                            flush.getTargetManager().save();
                            ChatUtils.println("\"" + playername + "\" is now a target.");
                            break;
                        }

                        ChatUtils.println("\"" + playername + "\" is already a target!");
                        break;
                    }

                    sendSyntaxHelpMessage();
                    break;

                case "remove":
                    if (args.length == 2) {
                        String playername = args[1];
                        if (flush.getTargetManager().removeTarget(playername)) {
                            flush.getTargetManager().save();
                            ChatUtils.println("\"" + playername + "\" is no longer a target.");
                            break;
                        }

                        ChatUtils.println("\"" + playername + "\" is not a target.");
                        break;
                    }

                    sendSyntaxHelpMessage();
                    break;

                case "list":
                    flush.getTargetManager().load();
                    if (flush.getTargetManager().getTargets().size() != 0) {
                        ChatUtils.println("§9Current targets:");
                        flush.getTargetManager().getTargets().forEach(ChatUtils::println);
                        break;
                    }

                    ChatUtils.println("§9You currently don't have any targets.");
                    break;

                case "clear":
                    flush.getTargetManager().getTargets().clear();
                    flush.getTargetManager().save();
                    ChatUtils.println("§9Cleared all targets.");
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