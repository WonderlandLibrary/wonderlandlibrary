/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.management.notifications.Notifications;
import me.arithmo.util.misc.ChatUtil;

public class NotificationTest
extends Command {
    public NotificationTest(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        Notifications not = Notifications.getManager();
        if (args[0].equalsIgnoreCase("notify")) {
            not.post("Player Warning", "Some one called you a \u00a7chacker!", 2500, Notifications.Type.NOTIFY);
        } else if (args[0].equalsIgnoreCase("warning")) {
            not.post("Warning Alert", "\u00a7cBob \u00a7fis now \u00a76Vanished!", 2500, Notifications.Type.WARNING);
        } else if (args[0].equalsIgnoreCase("info")) {
            not.post("Friend Info", "\u00a7bArithmo \u00a7fhas \u00a7cdied!", 2500, Notifications.Type.INFO);
        } else if (args[0].equalsIgnoreCase("f")) {
            not.post("Friend Info", "\u00a7aA \u00a7fG \u00a7cD!", 2500, Notifications.Type.INFO);
        } else {
            ChatUtil.printChat("\u00a7c[\u00a7fS\u00a7c]\u00a77 ???");
        }
    }

    @Override
    public String getUsage() {
        return null;
    }

    public void onEvent(Event event) {
    }
}

