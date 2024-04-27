/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.IntegerValue;

@ModuleInfo(name="SuperKnockback", description="Increases knockback dealt to other entities.", category=ModuleCategory.COMBAT)
public final class SuperKnockback
extends Module {
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);

    @EventTarget
    public final void onAttack(AttackEvent event) {
        if (MinecraftInstance.classProvider.isEntityLivingBase(event.getTargetEntity())) {
            IEntity iEntity = event.getTargetEntity();
            if (iEntity == null) {
                Intrinsics.throwNpe();
            }
            if (iEntity.asEntityLivingBase().getHurtTime() > ((Number)this.hurtTimeValue.get()).intValue()) {
                return;
            }
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                return;
            }
            IEntityPlayerSP player = iEntityPlayerSP;
            if (player.getSprinting()) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.STOP_SPRINTING));
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.START_SPRINTING));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.STOP_SPRINTING));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(player, ICPacketEntityAction.WAction.START_SPRINTING));
            player.setSprinting(true);
            player.setServerSprintState(true);
        }
    }
}

