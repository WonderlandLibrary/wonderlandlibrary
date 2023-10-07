// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import exhibition.module.Module;
import exhibition.util.misc.ChatUtil;
import exhibition.Client;
import exhibition.management.command.Command;

public class Toggle extends Command
{
    public Toggle(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        Module module = null;
        if (args.length > 0) {
            module = Client.getModuleManager().get(args[0]);
        }
        if (module == null) {
            this.printUsage();
            return;
        }
        if (args.length == 1) {
            module.toggle();
            ChatUtil.printChat("§4[§cE§4]§8 " + module.getName() + " has been" + (module.isEnabled() ? "§a Enabled." : "§c Disabled."));
        }
    }
    
    @Override
    public String getUsage() {
        return "toggle <module name>";
    }
}
