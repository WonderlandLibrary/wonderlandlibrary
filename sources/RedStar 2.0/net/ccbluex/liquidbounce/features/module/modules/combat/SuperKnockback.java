package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.utils.player.PlayerUtil;
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
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="SuperKnockback", description="Fix", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J020HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n0\tX¢\n\u0000R0\f8VX¢\b\rR0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/SuperKnockback;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delay", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "hurtTimeValue", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onlyGroundValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "onlyMoveValue", "tag", "", "getTag", "()Ljava/lang/String;", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "getTimer", "()Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "Pride"})
public final class SuperKnockback
extends Module {
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Normal", "MCYC", "ExtraPacket", "WTap", "Packet", "HYTPacket", "Tick"}, "Normal");
    private final BoolValue onlyMoveValue = new BoolValue("OnlyMove", false);
    private final BoolValue onlyGroundValue = new BoolValue("OnlyGround", false);
    private final IntegerValue delay = new IntegerValue("Delay", 0, 0, 500);
    @NotNull
    private final MSTimer timer = new MSTimer();

    @NotNull
    public final MSTimer getTimer() {
        return this.timer;
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        block54: {
            block56: {
                block55: {
                    Intrinsics.checkParameterIsNotNull(event, "event");
                    if (!(event.getTargetEntity() instanceof EntityLivingBase)) break block54;
                    if (((EntityLivingBase)event.getTargetEntity()).hurtTime > ((Number)this.hurtTimeValue.get()).intValue() || !this.timer.hasTimePassed(((Number)this.delay.get()).intValue()) || !PlayerUtil.isMoving() && ((Boolean)this.onlyMoveValue.get()).booleanValue()) break block55;
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
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc2.player;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc2.player");
                    if (entityPlayerSP.func_70051_ag()) {
                        EntityPlayerSP entityPlayerSP2 = MinecraftInstance.mc2.player;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc2.player");
                        entityPlayerSP2.setSprinting(true);
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
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc2.player;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc2.player");
                    if (entityPlayerSP.func_70051_ag()) {
                        EntityPlayerSP entityPlayerSP3 = MinecraftInstance.mc2.player;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc2.player");
                        entityPlayerSP3.setSprinting(true);
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
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc2.player;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc2.player");
                    if (entityPlayerSP.func_70051_ag()) {
                        EntityPlayerSP entityPlayerSP4 = MinecraftInstance.mc2.player;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP4, "mc2.player");
                        entityPlayerSP4.setSprinting(false);
                    }
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketEntityAction(iEntityPlayerSP, ICPacketEntityAction.WAction.START_SPRINTING));
                    EntityPlayerSP entityPlayerSP5 = MinecraftInstance.mc2.player;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP5, "mc2.player");
                    entityPlayerSP5.setSprinting(true);
                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP8 == null) {
                        Intrinsics.throwNpe();
                    }
                    iEntityPlayerSP8.setServerSprintState(true);
                    break;
                }
                case "hytpacket": {
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc2.player;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc2.player");
                    if (entityPlayerSP.func_70051_ag()) {
                        EntityPlayerSP entityPlayerSP6 = MinecraftInstance.mc2.player;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP6, "mc2.player");
                        entityPlayerSP6.setSprinting(true);
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
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc2.player;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc2.player");
                    if (entityPlayerSP.func_70051_ag()) {
                        EntityPlayerSP entityPlayerSP7 = MinecraftInstance.mc2.player;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP7, "mc2.player");
                        entityPlayerSP7.setSprinting(false);
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
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc2.player;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc2.player");
                    if (entityPlayerSP.func_70051_ag()) {
                        EntityPlayerSP entityPlayerSP8 = MinecraftInstance.mc2.player;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP8, "mc2.player");
                        entityPlayerSP8.setSprinting(false);
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
                    EntityPlayerSP entityPlayerSP = MinecraftInstance.mc2.player;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc2.player");
                    if (entityPlayerSP.func_70051_ag()) {
                        EntityPlayerSP entityPlayerSP9 = MinecraftInstance.mc2.player;
                        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP9, "mc2.player");
                        entityPlayerSP9.setSprinting(true);
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

    @Override
    @NotNull
    public String getTag() {
        return (String)this.modeValue.get();
    }
}
