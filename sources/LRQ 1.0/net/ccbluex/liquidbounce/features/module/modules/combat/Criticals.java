/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.network.play.client.CPacketPlayer
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.enums.StatType;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.network.play.client.CPacketPlayer;

@ModuleInfo(name="Criticals", description="Automatically deals critical hits.", category=ModuleCategory.COMBAT)
public final class Criticals
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Packet", "Packet2", "Packet3", "Packet4", "SuperPacket", "NoGround", "NoGround2", "Hop", "TPHop", "FakeJump", "FakeJump2", "Jump", "LowJump", "LowJump2", "Visual"}, "Packet");
    private final IntegerValue delayValue = new IntegerValue("Delay", 0, 0, 500);
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    private final MSTimer msTimer = new MSTimer();

    public final ListValue getModeValue() {
        return this.modeValue;
    }

    public final IntegerValue getDelayValue() {
        return this.delayValue;
    }

    private final void fakeJump() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        thePlayer.setAirBorne(true);
        thePlayer.triggerAchievement(MinecraftInstance.classProvider.getStatEnum(StatType.JUMP_STAT));
    }

    public final MSTimer getMsTimer() {
        return this.msTimer;
    }

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"NoGround", (boolean)true)) {
            thePlayer.jump();
        }
    }

    @EventTarget
    public final void onAttack(AttackEvent event) {
        block39: {
            IEntityLivingBase entity;
            IEntityPlayerSP thePlayer;
            block41: {
                block40: {
                    if (!MinecraftInstance.classProvider.isEntityLivingBase(event.getTargetEntity())) break block39;
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        return;
                    }
                    thePlayer = iEntityPlayerSP;
                    IEntity iEntity = event.getTargetEntity();
                    if (iEntity == null) {
                        Intrinsics.throwNpe();
                    }
                    entity = iEntity.asEntityLivingBase();
                    if (!thePlayer.getOnGround() || thePlayer.isOnLadder() || thePlayer.isInWeb() || thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.getRidingEntity() != null || entity.getHurtTime() > ((Number)this.hurtTimeValue.get()).intValue()) break block40;
                    Module module = LiquidBounce.INSTANCE.getModuleManager().get(Fly.class);
                    if (module == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!module.getState() && this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) break block41;
                }
                return;
            }
            double x = thePlayer.getPosX();
            double y = thePlayer.getPosY();
            double z = thePlayer.getPosZ();
            String string = (String)this.modeValue.get();
            boolean bl = false;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string2.toLowerCase()) {
                case "lowjump": {
                    thePlayer.setMotionY(0.3425);
                    break;
                }
                case "jump": {
                    thePlayer.setMotionY(0.42);
                    break;
                }
                case "visual": {
                    thePlayer.onCriticalHit(entity);
                    break;
                }
                case "lowjump2": {
                    if (!thePlayer.getOnGround() || MinecraftInstance.mc.getGameSettings().getKeyBindJump().isKeyDown() || MovementUtils.isMoving()) break;
                    thePlayer.setMotionY(0.2);
                    break;
                }
                case "hop": {
                    thePlayer.setMotionY(0.1);
                    thePlayer.setFallDistance(0.1f);
                    thePlayer.setOnGround(false);
                    break;
                }
                case "tphop": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.02, z, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.01, z, false));
                    thePlayer.setPosition(x, y + 0.01, z);
                    break;
                }
                case "noground2": {
                    thePlayer.onCriticalHit(entity);
                    break;
                }
                case "packet": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.0625, z, true));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y, z, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 1.1E-5, z, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y, z, false));
                    break;
                }
                case "packet2": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.11, z, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 0.1100013579, z, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(x, y + 1.3579E-6, z, false));
                    break;
                }
                case "packet3": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + (double)0.05f, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + (double)0.012511f, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), false));
                    break;
                }
                case "packet4": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.0031311231111, thePlayer.getPosZ(), false));
                    break;
                }
                case "superpacket": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.1625, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 4.0E-6, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 1.0E-6, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayer(false));
                    break;
                }
                case "fakejump": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.42, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), false));
                    break;
                }
                case "fakejump2": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY() + 0.42, thePlayer.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(thePlayer.getPosX(), thePlayer.getPosY(), thePlayer.getPosZ(), false));
                    this.fakeJump();
                }
            }
            this.msTimer.reset();
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent event) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        IPacket packet = event.getPacket();
        if (packet instanceof CPacketPlayer && StringsKt.equals((String)((String)this.modeValue.get()), (String)"NoGround", (boolean)true)) {
            ((CPacketPlayer)packet).field_149474_g = false;
        }
        if (packet instanceof CPacketPlayer && StringsKt.equals((String)((String)this.modeValue.get()), (String)"NoGround2", (boolean)true) && !thePlayer.getOnGround() && !thePlayer.isCollidedVertically() && thePlayer.getFallDistance() < (float)2) {
            ((CPacketPlayer)packet).field_149474_g = true;
        }
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }
}

