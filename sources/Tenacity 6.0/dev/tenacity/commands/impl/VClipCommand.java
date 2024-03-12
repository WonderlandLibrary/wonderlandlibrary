// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import dev.tenacity.utils.player.ChatUtil;
import dev.tenacity.commands.Command;

public final class VClipCommand extends Command
{
    public VClipCommand() {
        super("vclip", "vertical clip", ".vclip [distance]", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            this.usage();
        }
        else {
            try {
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + Double.parseDouble(args[0]), this.mc.thePlayer.posZ);
                ChatUtil.print("Clipped " + Double.parseDouble(args[0]) + " blocks!");
            }
            catch (NumberFormatException e) {
                this.usage();
            }
        }
    }
}
