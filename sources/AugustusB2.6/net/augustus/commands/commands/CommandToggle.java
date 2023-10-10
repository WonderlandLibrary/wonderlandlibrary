// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.commands.commands;

import java.util.Iterator;
import net.augustus.modules.Module;
import net.augustus.Augustus;
import net.augustus.commands.Command;

public class CommandToggle extends Command
{
    public CommandToggle() {
        super(".t");
    }
    
    @Override
    public void commandAction(final String[] message) {
        super.commandAction(message);
        if (message.length > 1) {
            for (final Module module : Augustus.getInstance().getModuleManager().getModules()) {
                if (message[1].equalsIgnoreCase(module.getName())) {
                    module.toggle();
                    this.sendChat("§c" + module.getName() + " §7toggled");
                }
            }
        }
        else {
            this.sendChat("§7.t [§cModule§7]");
        }
    }
    
    @Override
    public void helpMessage() {
        this.sendChat("§c.t§7 (Toggles a module)");
    }
}
