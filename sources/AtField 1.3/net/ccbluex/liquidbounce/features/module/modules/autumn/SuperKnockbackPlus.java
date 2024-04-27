/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.features.module.modules.autumn;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;

@ModuleInfo(name="SuperKnockbackPlus", description="\u8d85\u7ea7\u51fb\u9000", category=ModuleCategory.AUTUMN)
public final class SuperKnockbackPlus
extends Module {
    private final ListValue modeValue;
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);

    public SuperKnockbackPlus() {
        this.modeValue = new ListValue("Mode", new String[]{"VioLent", "Autumn", "New", "Autumn2", "Text", "Hyt"}, "Autumn");
    }

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        AttackEvent attackEvent2 = attackEvent;
        if (MinecraftInstance.classProvider.isEntityLivingBase(attackEvent.getTargetEntity())) {
            IEntity iEntity = attackEvent.getTargetEntity();
            if (iEntity == null) {
                Intrinsics.throwNpe();
            }
            Object object = this.hurtTimeValue.get();
            if (object == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
            }
            if (iEntity.asEntityLivingBase().getHurtTime() > ((Number)object).intValue()) {
                return;
            }
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                return;
            }
            IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
            if (iEntityPlayerSP2.getSprinting()) {
                String string = (String)this.modeValue.get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                switch (string2.toLowerCase()) {
                    case "New": {
                        IEntityPlayerSP iEntityPlayerSP3 = iEntityPlayerSP2;
                        if (iEntityPlayerSP3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity");
                        }
                        ICPacketEntityAction iCPacketEntityAction = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP3, ICPacketEntityAction.WAction.STOP_SPRINTING);
                        if (iCPacketEntityAction == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction);
                        ICPacketEntityAction iCPacketEntityAction2 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING);
                        if (iCPacketEntityAction2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction2);
                        ICPacketEntityAction iCPacketEntityAction3 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SPRINTING);
                        if (iCPacketEntityAction3 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction3);
                        ICPacketEntityAction iCPacketEntityAction4 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING);
                        if (iCPacketEntityAction4 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction4);
                        ICPacketEntityAction iCPacketEntityAction5 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SPRINTING);
                        if (iCPacketEntityAction5 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction5);
                        ICPacketEntityAction iCPacketEntityAction6 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING);
                        if (iCPacketEntityAction6 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction6);
                        ICPacketEntityAction iCPacketEntityAction7 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SPRINTING);
                        if (iCPacketEntityAction7 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction7);
                        ICPacketEntityAction iCPacketEntityAction8 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING);
                        if (iCPacketEntityAction8 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction8);
                        iEntityPlayerSP2.setSprinting(true);
                        iEntityPlayerSP2.setServerSprintState(true);
                        break;
                    }
                    case "Autumn": {
                        IEntityPlayerSP iEntityPlayerSP4 = iEntityPlayerSP2;
                        if (iEntityPlayerSP4 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity");
                        }
                        ICPacketEntityAction iCPacketEntityAction = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP4, ICPacketEntityAction.WAction.STOP_SPRINTING);
                        if (iCPacketEntityAction == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction);
                        ICPacketEntityAction iCPacketEntityAction9 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING);
                        if (iCPacketEntityAction9 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction9);
                        iEntityPlayerSP2.setSprinting(true);
                        iEntityPlayerSP2.setServerSprintState(true);
                        break;
                    }
                    case "VioLent": {
                        IEntityPlayerSP iEntityPlayerSP5 = iEntityPlayerSP2;
                        if (iEntityPlayerSP5 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity");
                        }
                        ICPacketEntityAction iCPacketEntityAction = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP5, ICPacketEntityAction.WAction.STOP_SPRINTING);
                        if (iCPacketEntityAction == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction);
                        ICPacketEntityAction iCPacketEntityAction10 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING);
                        if (iCPacketEntityAction10 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction10);
                        ICPacketEntityAction iCPacketEntityAction11 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.STOP_SPRINTING);
                        if (iCPacketEntityAction11 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction11);
                        ICPacketEntityAction iCPacketEntityAction12 = MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING);
                        if (iCPacketEntityAction12 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.network.IPacket");
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(iCPacketEntityAction12);
                        iEntityPlayerSP2.setSprinting(true);
                        iEntityPlayerSP2.setServerSprintState(true);
                        break;
                    }
                    case "Autumn2": {
                        if (MinecraftInstance.mc2.field_71439_g.func_70051_ag()) {
                            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                            IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                            if (iEntityPlayerSP6 == null) {
                                Intrinsics.throwNpe();
                            }
                            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP6, ICPacketEntityAction.WAction.STOP_SPRINTING));
                        }
                        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                        IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP7 == null) {
                            Intrinsics.throwNpe();
                        }
                        iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP7, ICPacketEntityAction.WAction.START_SPRINTING));
                        IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
                        IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP8 == null) {
                            Intrinsics.throwNpe();
                        }
                        iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP8, ICPacketEntityAction.WAction.STOP_SPRINTING));
                        IINetHandlerPlayClient iINetHandlerPlayClient3 = MinecraftInstance.mc.getNetHandler();
                        IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP9 == null) {
                            Intrinsics.throwNpe();
                        }
                        iINetHandlerPlayClient3.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP9, ICPacketEntityAction.WAction.START_SPRINTING));
                        MinecraftInstance.mc2.field_71439_g.func_70031_b(true);
                        IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP10 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP10.setServerSprintState(true);
                        break;
                    }
                    case "Text": {
                        if (MinecraftInstance.mc2.field_71439_g.func_70051_ag()) {
                            MinecraftInstance.mc2.field_71439_g.func_70031_b(false);
                        }
                        IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                        IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP11 == null) {
                            Intrinsics.throwNpe();
                        }
                        iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP11, ICPacketEntityAction.WAction.START_SPRINTING));
                        IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP12 == null) {
                            Intrinsics.throwNpe();
                        }
                        iEntityPlayerSP12.setServerSprintState(true);
                        break;
                    }
                    case "Hyt": {
                        if (!MinecraftInstance.classProvider.isEntityLivingBase(attackEvent.getTargetEntity())) break;
                        IEntity iEntity2 = attackEvent.getTargetEntity();
                        if (iEntity2 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (iEntity2.asEntityLivingBase().getHurtTime() > ((Number)this.hurtTimeValue.get()).intValue()) {
                            return;
                        }
                        IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP13 == null) {
                            return;
                        }
                        IEntityPlayerSP iEntityPlayerSP14 = iEntityPlayerSP13;
                        if (iEntityPlayerSP14.getSprinting()) {
                            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP14, ICPacketEntityAction.WAction.STOP_SPRINTING));
                        }
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP14, ICPacketEntityAction.WAction.START_SPRINTING));
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP14, ICPacketEntityAction.WAction.STOP_SPRINTING));
                        MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP14, ICPacketEntityAction.WAction.START_SPRINTING));
                        iEntityPlayerSP14.setSprinting(true);
                        iEntityPlayerSP14.setServerSprintState(true);
                        break;
                    }
                }
            }
        }
    }
}

