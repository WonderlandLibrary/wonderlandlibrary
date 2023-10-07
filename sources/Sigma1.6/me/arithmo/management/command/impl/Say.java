/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import me.arithmo.util.misc.ChatUtil;

public class Say
extends Command {
    public Say(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            return;
        }
        if (args.length > 0) {
            StringBuilder out = new StringBuilder();
            for (String word : args) {
                out.append(word + " ");
            }
            String message = out.substring(0, out.length() - 1);
            message = message.replaceAll("&", "\u00a7");
            ChatUtil.printChat(message);
        }
    }

    @Override
    public String getUsage() {
        return "Say <Message>";
    }

    public void onEvent(Event event) {
    }
}

