// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import dev.tenacity.event.impl.player.AttackEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;

public final class SuperKnockback extends Module
{
    public SuperKnockback() {
        super("SuperKnockback", "Super Knockback", Category.COMBAT, "Makes the player your attacking take extra knockback");
    }
    
    @Override
    public void onAttackEvent(final AttackEvent event) {
        if (event.getTargetEntity() != null) {
            if (SuperKnockback.mc.thePlayer.isSprinting()) {
                PacketUtils.sendPacketNoEvent(new C0BPacketEntityAction(SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            PacketUtils.sendPacketNoEvent(new C0BPacketEntityAction(SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            PacketUtils.sendPacketNoEvent(new C0BPacketEntityAction(SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            PacketUtils.sendPacketNoEvent(new C0BPacketEntityAction(SuperKnockback.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        }
    }
}
