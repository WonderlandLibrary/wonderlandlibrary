/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.command.impl;

import me.wintware.client.command.AbstractCommand;
import me.wintware.client.module.hud.HUD;
import me.wintware.client.utils.other.ChatUtils;

public final class ClientNameCommand
extends AbstractCommand {
    public ClientNameCommand() {
        super("clientname", "Change text displayed on watermark.", "clientname <name>", "clientname", "name", "rename");
    }

    @Override
    public void execute(String ... arguments) {
        if (arguments.length >= 2) {
            StringBuilder string = new StringBuilder();
            for (int i = 1; i < arguments.length; ++i) {
                String tempString = arguments[i];
                tempString = tempString.replace('&', '\u00a7');
                string.append(tempString).append(" ");
            }
            ChatUtils.addChatMessage(String.format("Changed client name to '%s\u00a77' was '%s\u00a77'.", string.toString().trim(), HUD.clientName));
            HUD.clientName = string.toString().trim();
        } else {
            this.usage();
        }
    }
}

