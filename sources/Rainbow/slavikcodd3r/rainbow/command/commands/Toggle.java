// 
// Decompiled by Procyon v0.5.30
// 

package slavikcodd3r.rainbow.command.commands;

import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.ModuleManager;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "toggle", "t", "tog" })
public class Toggle extends Command
{
    @Override
    public void runCommand(final String[] args) {
        String modName = "";
        if (args.length > 1) {
            modName = args[1];
        }
        final Module module = ModuleManager.getModule(modName);
        if (module.getId().equalsIgnoreCase("null")) {
            ClientUtils.sendMessage("Invalid Module.");
            return;
        }
        module.toggle();
        ClientUtils.sendMessage(String.valueOf(module.getDisplayName()) + " is now " + (module.isEnabled() ? "enabled" : "disabled"));
        ModuleManager.save();
    }
    
    @Override
    public String getHelp() {
        return "toggle";
    }
}
