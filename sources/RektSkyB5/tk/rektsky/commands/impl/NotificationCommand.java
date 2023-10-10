/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import tk.rektsky.commands.Command;
import tk.rektsky.commands.impl.HelpCommand;
import tk.rektsky.module.impl.render.Notification;

public class NotificationCommand
extends Command {
    public NotificationCommand() {
        super("noti", "<Message>", "Sends notification.");
    }

    @Override
    public void onCommand(String label, String[] args) {
        if (args.length < 1) {
            HelpCommand.displayCommandInfomation(this);
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg + " ");
        }
        Notification.displayNotification(new Notification.PopupMessage("Command", builder.toString(), 10944353, -9240676, 60));
    }
}

