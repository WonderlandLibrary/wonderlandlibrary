// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.combat;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.augustus.events.EventUpdate;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.modules.Module;

public class FastBow extends Module
{
    public FastBow() {
        super("FastBow", Color.ORANGE, Categorys.COMBAT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (FastBow.mc.thePlayer.getHealth() > 0.0f && (FastBow.mc.thePlayer.onGround || FastBow.mc.thePlayer.capabilities.isCreativeMode) && FastBow.mc.thePlayer.inventory.getCurrentItem() != null && FastBow.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow && FastBow.mc.gameSettings.keyBindUseItem.pressed) {
            FastBow.mc.playerController.sendUseItem(FastBow.mc.thePlayer, FastBow.mc.theWorld, FastBow.mc.thePlayer.inventory.getCurrentItem());
            FastBow.mc.thePlayer.inventory.getCurrentItem().getItem().onItemRightClick(FastBow.mc.thePlayer.inventory.getCurrentItem(), FastBow.mc.theWorld, FastBow.mc.thePlayer);
            for (int i = 0; i < 20; ++i) {
                FastBow.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
            }
            FastBow.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            FastBow.mc.thePlayer.inventory.getCurrentItem().getItem().onPlayerStoppedUsing(FastBow.mc.thePlayer.inventory.getCurrentItem(), FastBow.mc.theWorld, FastBow.mc.thePlayer, 10);
        }
    }
}
