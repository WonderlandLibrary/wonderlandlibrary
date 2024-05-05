// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.cmd.impl;

import Lavish.utils.misc.Console;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.client.entity.EntityPlayerSP;
import Lavish.cmd.CommandExecutor;

public class VClip implements CommandExecutor
{
    @Override
    public void execute(final EntityPlayerSP sender, final List<String> args) {
        if (args.size() == 1) {
            final double blocks = Double.parseDouble(args.get(0));
            Minecraft.getMinecraft().thePlayer.setEntityBoundingBox(Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(0.0, blocks, 0.0));
            Console.sendChatToPlayerWithPrefix("Teleported " + blocks + " blocks.");
        }
        else {
            Console.sendChatToPlayerWithPrefix("Please enter an amount of blocks.");
        }
    }
}
