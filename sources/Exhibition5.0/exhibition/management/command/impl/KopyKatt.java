// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import java.util.Iterator;
import exhibition.util.misc.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import exhibition.management.command.Command;

public class KopyKatt extends Command
{
    protected final Minecraft mc;
    
    public KopyKatt(final String[] names, final String description) {
        super(names, description);
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void fire(final String[] args) {
        if (args == null) {
            this.printUsage();
            return;
        }
        if (args.length > 0) {
            for (final Object o : this.mc.theWorld.getLoadedEntityList()) {
                if (o instanceof EntityPlayer) {
                    final EntityPlayer ent = (EntityPlayer)o;
                    if (ent.getName().equalsIgnoreCase(args[0])) {
                        exhibition.module.impl.other.KopyKatt.target = ent;
                        ChatUtil.printChat("§4[§cE§4]§8 " + ent.getName() + " is now being copied. :)");
                        return;
                    }
                    continue;
                }
            }
        }
        this.printUsage();
    }
    
    @Override
    public String getUsage() {
        return "Target <Target>";
    }
}
