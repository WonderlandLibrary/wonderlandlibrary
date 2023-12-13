// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.player;

import net.minecraft.item.ItemSword;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class PacketSwing extends Module
{
    public PacketSwing() {
        super("PacketSwing", "Swings with packets lol", 0, Category.PLAYER, true);
    }
    
    @Override
    public void onUpdate() {
        if (PacketSwing.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && PacketSwing.mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
            PacketSwing.mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
            PacketSwing.mc.entityRenderer.itemRenderer.itemStackMainHand = PacketSwing.mc.player.getHeldItemMainhand();
        }
    }
}
