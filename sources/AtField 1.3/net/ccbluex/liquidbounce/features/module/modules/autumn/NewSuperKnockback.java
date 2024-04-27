/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.autumn;

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

@ModuleInfo(name="NewSuperKnockback", description="\u767e\u5206\u767e\u51fb\u9000", category=ModuleCategory.AUTUMN)
public final class NewSuperKnockback
extends Module {
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        if (MinecraftInstance.classProvider.isEntityLivingBase(attackEvent.getTargetEntity())) {
            IEntity iEntity = attackEvent.getTargetEntity();
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
            IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
            if (iEntityPlayerSP2.getSprinting()) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SPRINTING));
            }
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SPRINTING));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SPRINTING));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SPRINTING));
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING));
            iEntityPlayerSP2.setSprinting(true);
            iEntityPlayerSP2.setServerSprintState(true);
        }
    }
}

