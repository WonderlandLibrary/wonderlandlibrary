package dev.monsoon.command.impl;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;
import dev.monsoon.module.base.Module;
import dev.monsoon.notification.Notification;
import dev.monsoon.notification.NotificationManager;
import dev.monsoon.notification.NotificationType;
import org.lwjgl.input.Keyboard;

public class WebhookSpammerCommand extends Command {
    public WebhookSpammerCommand() {
        super("Wspammer", "Set up the WebhookSpammer", "wspammer url <url> | wspammer amount <amount> | wspammer delay <delay>", "b");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args[0].equalsIgnoreCase("url")) {
            Monsoon.manager.wSpammer.setWebhookurl(args[1]);
        } else if(args[0].equalsIgnoreCase("amount")) {
            Monsoon.manager.wSpammer.setAmount(Integer.parseInt(args[1]));
        } else if(args[0].equalsIgnoreCase("delay")) {
            Monsoon.manager.wSpammer.setDelay(Integer.parseInt(args[1]));
        } else if(args[0].equalsIgnoreCase("message")) {
            Monsoon.manager.wSpammer.setMessage(args[1]);
        }
    }
}
