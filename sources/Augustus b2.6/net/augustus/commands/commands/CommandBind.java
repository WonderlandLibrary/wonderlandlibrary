// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.commands.commands;

import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import net.augustus.modules.Module;
import net.augustus.Augustus;
import net.augustus.commands.Command;

public class CommandBind extends Command
{
    public CommandBind() {
        super(".bind");
    }
    
    @Override
    public void commandAction(final String[] message) {
        super.commandAction(message);
        if (message.length >= 3) {
            for (final Module module : Augustus.getInstance().getModuleManager().getModules()) {
                if (message[1].equalsIgnoreCase(module.getName())) {
                    for (int i = 0; i < 84; ++i) {
                        if (Keyboard.getKeyName(i).equalsIgnoreCase(message[2])) {
                            this.setKey(i, module, message[2].toUpperCase());
                            return;
                        }
                    }
                }
            }
        }
        this.errorMessage();
    }
    
    private void setKey(final int key, final Module module, final String keyName) {
        module.setKey(key);
        this.sendChat("§c" + module.getName() + " §7bound to §2" + keyName.toUpperCase());
    }
    
    @Override
    public void helpMessage() {
        this.sendChat("§c.bind§7 (Binds a module to a key)");
    }
    
    public void errorMessage() {
        this.sendChat("§7.bind [§cModule§7] [§2Key§7]");
    }
}
