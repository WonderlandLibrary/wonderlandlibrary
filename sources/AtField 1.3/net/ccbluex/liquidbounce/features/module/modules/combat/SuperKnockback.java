/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.entity.EntityLivingBase
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.jvm.internal.Intrinsics;
import liying.utils.PlayerUtil;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.entity.EntityLivingBase;

@ModuleInfo(name="SuperKnockback", description="Fix by JIEMO", category=ModuleCategory.COMBAT)
public final class SuperKnockback
extends Module {
    private final MSTimer timer;
    private final BoolValue onlyGroundValue;
    private final IntegerValue delay;
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "MCYC", "ExtraPacket", "WTap", "Packet", "HYTPacket", "Tick"}, "Normal");
    private final BoolValue onlyMoveValue = new BoolValue("OnlyMove", false);

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public final MSTimer getTimer() {
        return this.timer;
    }

    public SuperKnockback() {
        this.onlyGroundValue = new BoolValue("OnlyGround", false);
        this.delay = new IntegerValue("Delay", 0, 0, 500);
        this.timer = new MSTimer();
    }

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        block54: {
            block56: {
                block55: {
                    if (!(attackEvent.getTargetEntity() instanceof EntityLivingBase)) break block54;
                    if (((EntityLivingBase)attackEvent.getTargetEntity()).field_70737_aN > ((Number)this.hurtTimeValue.get()).intValue() || !this.timer.hasTimePassed(((Number)this.delay.get()).intValue()) || !PlayerUtil.isMoving() && ((Boolean)this.onlyMoveValue.get()).booleanValue()) break block55;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (iEntityPlayerSP.getOnGround() || !((Boolean)this.onlyGroundValue.get()).booleanValue()) break block56;
                }
                return;
            }
            switch ((String)this.modeValue.get()) {
                case "extrapacket": {
                    if (MinecraftInstance.mc2.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc2.field_71439_g.func_70031_b(true);
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.STOP_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP2 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient2.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP2, ICPacketEntityAction.WAction.START_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient3 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP3 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient3.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP3, ICPacketEntityAction.WAction.STOP_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient4 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP4 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient4.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP4, ICPacketEntityAction.WAction.START_SPRINTING));
                    IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP5 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP5.setServerSprintState(true);
                    break;
                }
                case "normal": {
                    if (MinecraftInstance.mc2.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc2.field_71439_g.func_70031_b(true);
                    }
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP.setServerSprintState(true);
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP6, ICPacketEntityAction.WAction.STOP_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient5 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP7 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient5.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP7, ICPacketEntityAction.WAction.START_SPRINTING));
                    break;
                }
                case "mcyc": {
                    if (MinecraftInstance.mc2.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc2.field_71439_g.func_70031_b(false);
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.START_SPRINTING));
                    MinecraftInstance.mc2.field_71439_g.func_70031_b(true);
                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP8 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP8.setServerSprintState(true);
                    break;
                }
                case "hytpacket": {
                    if (MinecraftInstance.mc2.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc2.field_71439_g.func_70031_b(true);
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.STOP_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient6 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP9 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient6.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP9, ICPacketEntityAction.WAction.START_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient7 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient7.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP10, ICPacketEntityAction.WAction.STOP_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient8 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient8.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP11, ICPacketEntityAction.WAction.START_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient9 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP12 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient9.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP12, ICPacketEntityAction.WAction.STOP_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient10 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP13 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient10.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP13, ICPacketEntityAction.WAction.START_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient11 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP14 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient11.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP14, ICPacketEntityAction.WAction.STOP_SPRINTING));
                    IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP15 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP15.setServerSprintState(true);
                    break;
                }
                case "tick": {
                    if (MinecraftInstance.mc2.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc2.field_71439_g.func_70031_b(false);
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.START_SPRINTING));
                    IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP16 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP16.setServerSprintState(true);
                    break;
                }
                case "wtap": {
                    if (MinecraftInstance.mc2.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc2.field_71439_g.func_70031_b(false);
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.START_SPRINTING));
                    IEntityPlayerSP iEntityPlayerSP17 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP17 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP17.setServerSprintState(true);
                    break;
                }
                case "packet": {
                    if (MinecraftInstance.mc2.field_71439_g.func_70051_ag()) {
                        MinecraftInstance.mc2.field_71439_g.func_70031_b(true);
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.STOP_SPRINTING));
                    IINetHandlerPlayClient iINetHandlerPlayClient12 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP18 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP18 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient12.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP18, ICPacketEntityAction.WAction.START_SPRINTING));
                    IEntityPlayerSP iEntityPlayerSP19 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP19 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP19.setServerSprintState(true);
                    break;
                }
            }
            this.timer.reset();
        }
    }
}

