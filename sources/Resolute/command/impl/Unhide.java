// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command.impl;

import java.util.Iterator;
import vip.Resolute.modules.Module;
import vip.Resolute.Resolute;
import net.minecraft.client.Minecraft;
import vip.Resolute.command.Command;

public class Unhide extends Command
{
    public Minecraft mc;
    
    public Unhide() {
        super("Unhide", "Unhides all", ".unhide all", new String[] { "unhide" });
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            for (final Module module : Resolute.modules) {
                module.setHidden(false);
            }
        }
    }
}
