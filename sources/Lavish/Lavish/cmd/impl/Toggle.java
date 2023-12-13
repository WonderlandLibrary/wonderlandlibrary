// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.cmd.impl;

import Lavish.modules.Module;
import Lavish.utils.misc.Console;
import Lavish.Client;
import java.util.List;
import net.minecraft.client.entity.EntityPlayerSP;
import Lavish.cmd.CommandExecutor;

public class Toggle implements CommandExecutor
{
    @Override
    public void execute(final EntityPlayerSP sender, final List<String> args) {
        if (args.size() == 1) {
            try {
                final Module m = Client.instance.moduleManager.getModuleByName(args.get(0));
                if (args.get(0).equalsIgnoreCase(m.getName())) {
                    m.toggle();
                }
                Console.sendChatToPlayerWithPrefix(String.valueOf(m.getName()) + (m.isEnabled() ? " has been enabled" : " has been disabled"));
            }
            catch (Exception e) {
                Console.sendChatToPlayerWithPrefix("Module not found.");
            }
        }
        else {
            Console.sendChatToPlayerWithPrefix("Please enter a module.");
        }
    }
}
