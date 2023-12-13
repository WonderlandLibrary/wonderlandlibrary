// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.cmd.impl;

import net.minecraft.util.EnumChatFormatting;
import Lavish.modules.Module;
import Lavish.utils.misc.Console;
import org.lwjgl.input.Keyboard;
import Lavish.Client;
import java.util.List;
import net.minecraft.client.entity.EntityPlayerSP;
import Lavish.cmd.CommandExecutor;

public class Bind implements CommandExecutor
{
    @Override
    public void execute(final EntityPlayerSP sender, final List<String> args) {
        if (args.size() == 2) {
            final Module m = Client.instance.moduleManager.getModuleByName(args.get(0));
            if (m != null && !args.get(1).equalsIgnoreCase("none")) {
                m.setKey(Keyboard.getKeyIndex(args.get(1).toUpperCase()));
                Console.sendChatToPlayerWithPrefix(String.valueOf(m.getName()) + " has been bound to " + Keyboard.getKeyName(m.getKey()) + ".");
            }
            else if (m != null) {
                m.setKey(0);
                Console.sendChatToPlayerWithPrefix("Unbound " + m.getName() + ".");
            }
            else {
                this.sendSyntax();
            }
        }
        else {
            this.sendSyntax();
        }
    }
    
    public void sendSyntax() {
        Console.sendChatToPlayerWithPrefix(EnumChatFormatting.WHITE + "Invalid syntax" + EnumChatFormatting.WHITE + ", please use " + EnumChatFormatting.AQUA + ".bind (module) (key/none)");
    }
}
