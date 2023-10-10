// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.commands.commands;

import net.augustus.utils.interfaces.MM;
import net.augustus.commands.Command;

public class CommandFucker extends Command implements MM
{
    public CommandFucker() {
        super(".fucker");
    }
    
    @Override
    public void commandAction(final String[] message) {
        super.commandAction(message);
        if (message.length > 1) {
            if (message[1].equalsIgnoreCase("set") && message.length >= 3) {
                int i = -1;
                try {
                    i = Integer.parseInt(message[2]);
                }
                catch (Exception e) {
                    System.err.println("No Integer");
                }
                if (i != -1) {
                    CommandFucker.mm.fucker.customID.setValue(i);
                    this.sendChat("Set Fucker ID to §2" + (int)CommandFucker.mm.fucker.customID.getValue());
                    return;
                }
            }
            else if (message[1].equalsIgnoreCase("get")) {
                this.sendChat("BlockID: §2" + (int)CommandFucker.mm.fucker.customID.getValue());
                return;
            }
        }
        this.errorMessage();
    }
    
    public void errorMessage() {
        this.sendChat("§7.fucker [§cset/get§7] [§2BlockID§7]");
    }
    
    @Override
    public void helpMessage() {
        this.sendChat("§c.fucker§7 (Get or set the blockID for custom mode)");
    }
}
