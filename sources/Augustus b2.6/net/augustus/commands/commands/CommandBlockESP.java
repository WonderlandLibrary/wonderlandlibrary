// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.commands.commands;

import net.augustus.utils.interfaces.MM;
import net.augustus.commands.Command;

public class CommandBlockESP extends Command implements MM
{
    public CommandBlockESP() {
        super(".blockesp");
    }
    
    @Override
    public void commandAction(final String[] message) {
        super.commandAction(message);
        if (message.length > 1) {
            if (message[1].equalsIgnoreCase("setID") && message.length >= 3) {
                int i = -1;
                try {
                    i = Integer.parseInt(message[2]);
                }
                catch (Exception e) {
                    System.err.println("No Integer");
                }
                if (i != -1) {
                    CommandBlockESP.mm.blockESP.id.setValue(i);
                    this.sendChat("Set BlockESP ID to §2" + (int)CommandBlockESP.mm.blockESP.id.getValue());
                    return;
                }
            }
            else if (message[1].equalsIgnoreCase("getID")) {
                this.sendChat("BlockID: §2" + (int)CommandBlockESP.mm.blockESP.id.getValue());
                return;
            }
        }
        this.errorMessage();
    }
    
    public void errorMessage() {
        this.sendChat("§7.blockESP [§csetID/getID§7] [§2BlockID§7]");
    }
    
    @Override
    public void helpMessage() {
        this.sendChat("§c.blockESP§7 (Get or set the blockID for BlockESP)");
    }
}
