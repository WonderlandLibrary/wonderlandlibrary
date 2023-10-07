// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.network.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemMapBase extends Item
{
    private static final String __OBFID = "CL_00000004";
    
    @Override
    public boolean isMap() {
        return true;
    }
    
    public Packet createMapDataPacket(final ItemStack p_150911_1_, final World worldIn, final EntityPlayer p_150911_3_) {
        return null;
    }
}
