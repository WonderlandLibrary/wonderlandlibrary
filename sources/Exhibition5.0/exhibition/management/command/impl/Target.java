// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import net.minecraft.entity.EntityLivingBase;
import exhibition.util.misc.ChatUtil;
import exhibition.module.impl.combat.Killaura;
import net.minecraft.client.Minecraft;
import exhibition.management.command.Command;

public class Target extends Command
{
    protected final Minecraft mc;
    
    public Target(final String[] names, final String description) {
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
            final String name = args[0];
            if (this.mc.theWorld.getPlayerEntityByName(name) != null) {
                final EntityLivingBase vip = Killaura.vip = this.mc.theWorld.getPlayerEntityByName(name);
                ChatUtil.printChat("§4[§cE§4]§8 Now targeting " + args[0]);
                return;
            }
            Killaura.vip = null;
            ChatUtil.printChat("§4[§cE§4]§8 No entity with the name \"" + args[0] + "\" currently exists.");
        }
        this.printUsage();
    }
    
    @Override
    public String getUsage() {
        return "Target <Target>";
    }
}
