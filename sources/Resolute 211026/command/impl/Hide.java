// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command.impl;

import java.util.Iterator;
import vip.Resolute.modules.Module;
import vip.Resolute.Resolute;
import net.minecraft.client.Minecraft;
import vip.Resolute.command.Command;

public class Hide extends Command
{
    public Minecraft mc;
    
    public Hide() {
        super("Hide", "Hides modules / Unhides modules", ".hide <name> | .hide all", new String[] { "hide" });
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("all")) {
                for (final Module module : Resolute.modules) {
                    module.setHidden(true);
                }
            }
            final String moduleName = args[0];
            boolean foundModule = false;
            for (final Module.Category c : Module.Category.values()) {
                if (c.name.equalsIgnoreCase(moduleName)) {
                    for (final Module m : Resolute.getModulesByCategory(c)) {
                        m.setHidden(true);
                    }
                }
            }
            for (final Module module2 : Resolute.modules) {
                if (module2.name.equalsIgnoreCase(moduleName)) {
                    if (module2.isHidden()) {
                        module2.setHidden(false);
                    }
                    else {
                        module2.setHidden(true);
                    }
                    Resolute.addChatMessage(module2.isHidden() ? ("Hidden: " + moduleName) : ("Shown: " + moduleName));
                    foundModule = true;
                    break;
                }
            }
            if (!foundModule) {
                Resolute.addChatMessage("Could not locate given module");
            }
        }
    }
}
