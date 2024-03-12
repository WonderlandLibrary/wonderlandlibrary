// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.viamcp.utils;

import dev.tenacity.viamcp.protocols.ProtocolCollection;
import dev.tenacity.viamcp.ViaMCP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.client.Minecraft;

public class AttackOrder
{
    private static final Minecraft mc;
    private static final int VER_1_8_ID = 47;
    
    public static void sendConditionalSwing(final MovingObjectPosition mop) {
        if (mop != null && mop.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
            AttackOrder.mc.thePlayer.swingItem();
        }
    }
    
    public static void sendFixedAttack(final EntityPlayer entityIn, final Entity target) {
        if (ViaMCP.getInstance().getVersion() <= ProtocolCollection.getProtocolById(47).getVersion()) {
            AttackOrder.mc.thePlayer.swingItem();
            AttackOrder.mc.playerController.attackEntity(entityIn, target);
        }
        else {
            AttackOrder.mc.playerController.attackEntity(entityIn, target);
            AttackOrder.mc.thePlayer.swingItem();
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
