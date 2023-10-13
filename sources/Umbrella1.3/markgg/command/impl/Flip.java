/*
 * Decompiled with CFR 0.150.
 */
package markgg.command.impl;

import markgg.Client;
import markgg.command.Command;
import net.minecraft.client.Minecraft;

public class Flip
extends Command {
    public Flip() {
        super("Flip", "Does a jonathan backflip.", "flip", "f");
    }

    @Override
    public void onCommand(String[] args, String command) {
        Minecraft.getMinecraft().thePlayer.rotationYaw += 180.0f;
        Client.addChatMessage("Did a jonathan backflip!");
    }
}

