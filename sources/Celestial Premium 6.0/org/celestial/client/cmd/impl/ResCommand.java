/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.helpers.misc.ChatHelper;

public class ResCommand
extends CommandAbstract {
    public ResCommand() {
        super("res", "res", ".res", "res");
    }

    @Override
    public void execute(String ... args) {
        if (args.length == 1) {
            if (args[0].equals("res")) {
                ResCommand.mc.player.respawnPlayer();
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

