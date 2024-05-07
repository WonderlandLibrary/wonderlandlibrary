package com.masterof13fps.features.commands.impl;

import java.util.ArrayList;

import com.masterof13fps.Client;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.utils.NotifyUtil;
import com.masterof13fps.manager.notificationmanager.NotificationType;

public class Friend extends Command {

    String syntax = Client.main().getClientPrefix() + "friend add <name> / remove <name> / list";
    public static ArrayList<String> friends = new ArrayList<String>();

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                notify.chat("Deine Freunde:");
                notify.chat(friends.toString());
            } else {
                notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
            }
        } else {
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("add")) {
                    if (!friends.contains(args[1])) {
                        friends.add(args[1]);
                        notify.chat(args[1] + " wurde zu deiner Freundesliste hinzugefügt!");
                    } else {
                        if (friends.contains(args[1])) {
                            notify.chat(args[1] + " ist bereits in deiner Freundesliste!");
                        }
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (friends.contains(args[1])) {
                        friends.remove(args[1]);
                        notify.chat(args[1] + " wurde aus deiner Freundesliste entfernt!");
                    } else {
                        if (!friends.contains(args[1])) {
                            notify.chat(args[1] + " ist nicht in deiner Freundesliste!");
                        }
                    }
                } else {
                    notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
                }
            } else {
                notify.notification("Falscher Syntax!", "Nutze §c" + syntax + "§r!", NotificationType.ERROR, 5);
            }
        }
    }

    public Friend() {
        super("friend", "fa");
    }

}
