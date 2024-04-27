/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.json.XMLTokener
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.ArrayList;
import java.util.HashMap;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.IINetHandlerPlayClient;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
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
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.json.XMLTokener;

@ModuleInfo(name="Criticals", description="\u5200\u7206", category=ModuleCategory.COMBAT)
public final class Criticals
extends Module {
    private final IntegerValue delayValue;
    private int n;
    private final ListValue modeValue = new ListValue("Mode", new String[]{"GrimAC", "GrimAC2", "HytGrimAC", "Grim", "Spartan", "Vulcan", "HytSpartan", "Jump", "Visual", "Verus", "Packet", "NewPacket", "GrimACTest-1", "GrimACTest-2", "GrimACTest-3", "HuaYuTing", "AAC4Hyt", "AAC4", "HytTest", "More", "PacketJump", "HytTest4v4", "Autumn"}, "Jump");
    private int attacks;
    private final IntegerValue hurtTimeValue;
    private final BoolValue lookValue;
    private final MSTimer msTimer;

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)((String)this.modeValue.get()), (String)"NoGround", (boolean)true)) {
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.jump();
        }
    }

    static void sendCriticalPacket$default(Criticals criticals, double d, double d2, double d3, boolean bl, int n, Object object) {
        if ((n & 1) != 0) {
            d = 0.0;
        }
        if ((n & 2) != 0) {
            d2 = 0.0;
        }
        if ((n & 4) != 0) {
            d3 = 0.0;
        }
        criticals.sendCriticalPacket(d, d2, d3, bl);
    }

    public final IntegerValue getDelayValue() {
        return this.delayValue;
    }

    public final int getN() {
        return this.n;
    }

    public final MSTimer getMsTimer() {
        return this.msTimer;
    }

    private final void sendCriticalPacket(double d, double d2, double d3, boolean bl) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        double d4 = iEntityPlayerSP.getPosX() + d;
        IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        double d5 = iEntityPlayerSP2.getPosY() + d2;
        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP3 == null) {
            Intrinsics.throwNpe();
        }
        double d6 = iEntityPlayerSP3.getPosZ() + d3;
        if (((Boolean)this.lookValue.get()).booleanValue()) {
            IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
            IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP4 == null) {
                Intrinsics.throwNpe();
            }
            float f = iEntityPlayerSP4.getRotationYaw();
            IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP5 == null) {
                Intrinsics.throwNpe();
            }
            iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosLook(d4, d5, d6, f, iEntityPlayerSP5.getRotationPitch(), bl));
        } else {
            MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d4, d5, d6, bl));
        }
    }

    public final void setAttacks(int n) {
        this.attacks = n;
    }

    @Override
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public final int getAttacks() {
        return this.attacks;
    }

    public Criticals() {
        this.delayValue = new IntegerValue("Delay", 0, 0, 500);
        this.lookValue = new BoolValue("SendC06", false);
        this.hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
        this.msTimer = new MSTimer();
    }

    @EventTarget
    public final void onAttack(AttackEvent attackEvent) {
        if (MinecraftInstance.classProvider.isEntityLivingBase(attackEvent.getTargetEntity())) {
            String string;
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                return;
            }
            IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
            IEntity iEntity = attackEvent.getTargetEntity();
            if (iEntity == null) {
                Intrinsics.throwNpe();
            }
            IEntityLivingBase iEntityLivingBase = iEntity.asEntityLivingBase();
            if (!iEntityPlayerSP2.getOnGround() || iEntityPlayerSP2.isOnLadder() || iEntityPlayerSP2.isInWeb() || iEntityPlayerSP2.isInWater() || iEntityPlayerSP2.isInLava() || iEntityPlayerSP2.getRidingEntity() != null || iEntityLivingBase.getHurtTime() > ((Number)this.hurtTimeValue.get()).intValue() || LiquidBounce.INSTANCE.getModuleManager().get(Fly.class).getState() || !this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
                return;
            }
            double d = iEntityPlayerSP2.getPosX();
            double d2 = iEntityPlayerSP2.getPosY();
            double d3 = iEntityPlayerSP2.getPosZ();
            String string2 = (String)this.modeValue.get();
            boolean bl = false;
            String string3 = string2;
            if (string3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            switch (string = string3.toLowerCase()) {
                case "grimac": {
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    ICPacketPlayer iCPacketPlayer = MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.06250000001304, d3, true);
                    iINetHandlerPlayClient.addToSendQueue(iCPacketPlayer);
                    IINetHandlerPlayClient iINetHandlerPlayClient2 = MinecraftInstance.mc.getNetHandler();
                    ICPacketPlayer iCPacketPlayer2 = MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.00150000001304, d3, false);
                    iINetHandlerPlayClient2.addToSendQueue(iCPacketPlayer2);
                    IINetHandlerPlayClient iINetHandlerPlayClient3 = MinecraftInstance.mc.getNetHandler();
                    ICPacketPlayer iCPacketPlayer3 = MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.014400000001304, d3, false);
                    iINetHandlerPlayClient3.addToSendQueue(iCPacketPlayer3);
                    IINetHandlerPlayClient iINetHandlerPlayClient4 = MinecraftInstance.mc.getNetHandler();
                    ICPacketPlayer iCPacketPlayer4 = MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.001150000001304, d3, false);
                    iINetHandlerPlayClient4.addToSendQueue(iCPacketPlayer4);
                    HashMap hashMap = XMLTokener.entity;
                    IEntity iEntity2 = (IEntity)((Object)hashMap);
                    iEntityPlayerSP2.onCriticalHit(iEntity2);
                    break;
                }
                case "spartan": {
                    double d4 = 0.0;
                    double d5 = 0.0;
                    if (MovementUtils.isMoving()) {
                        IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP3 == null) {
                            Intrinsics.throwNpe();
                        }
                        d4 = iEntityPlayerSP3.getMotionX();
                        IEntityPlayerSP iEntityPlayerSP4 = MinecraftInstance.mc.getThePlayer();
                        if (iEntityPlayerSP4 == null) {
                            Intrinsics.throwNpe();
                        }
                        d5 = iEntityPlayerSP4.getMotionZ();
                    } else {
                        d4 = 0.0;
                        d5 = 0.0;
                    }
                    this.sendCriticalPacket(d4 / 3.0, 0.20000004768372, d5 / 3.0, false);
                    this.sendCriticalPacket(d4 / 1.5, 0.12160004615784, d5 / 1.5, false);
                    break;
                }
                case "hytspartan": {
                    this.n = this.attacks;
                    this.attacks = this.n + 1;
                    if (this.attacks <= 6) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.01, d3, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 1.0E-10, d3, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.114514, d3, false));
                    this.attacks = 0;
                    break;
                }
                case "grim": 
                case "vulcan": {
                    int n = this.attacks;
                    this.attacks = n + 1;
                    if (this.attacks <= 6) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.2, d3, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.1216, d3, false));
                    this.attacks = 0;
                    break;
                }
                case "jump": {
                    iEntityPlayerSP2.setMotionY(0.42);
                    break;
                }
                case "visual": {
                    iEntityPlayerSP2.onCriticalHit(iEntityLivingBase);
                    break;
                }
                case "verus": {
                    int n = this.attacks;
                    this.attacks = n + 1;
                    if (this.attacks == 1) {
                        Criticals.sendCriticalPacket$default(this, 0.0, 0.001, 0.0, true, 10, null);
                        Criticals.sendCriticalPacket$default(this, 0.0, 0.0, 0.0, false, 14, null);
                    }
                    if (this.attacks < 5) break;
                    this.attacks = 0;
                    break;
                }
                case "packet": {
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.0625, d3, true));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2, d3, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 1.1E-5, d3, false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2, d3, false));
                    iEntityPlayerSP2.onCriticalHit(iEntityLivingBase);
                    break;
                }
                case "newpacket": {
                    Criticals criticals = (Criticals)LiquidBounce.moduleManager.getModule(Criticals.class);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.05250000001304, 0.0, true, 10, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.00150000001304, 0.0, false, 10, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.01400000001304, 0.0, false, 10, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.00150000001304, 0.0, false, 10, null);
                    break;
                }
                case "grimactest-1": {
                    if (iEntityPlayerSP2.isAirBorne()) {
                        mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(d, d2 + 0.06250000001304, d3, true));
                    }
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(d, d2 + 0.06150000001304, d3, false));
                    break;
                }
                case "grimactest-2": {
                    ++this.attacks;
                    if (this.attacks <= 6) break;
                    if (iEntityPlayerSP2.getOnGround()) {
                        mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(d, d2 + 0.01, d3, false));
                    }
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(d, d2 + 1.0E-10, d3, false));
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(d, d2 + 0.114514, d3, false));
                    this.attacks = 0;
                    break;
                }
                case "grimactest-3": {
                    if (iEntityPlayerSP2.isAirBorne()) {
                        return;
                    }
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(d, d2 + 0.021500070001304, d3, false));
                    break;
                }
                case "huayuting": {
                    ++this.attacks;
                    ArrayList<Double> arrayList = new ArrayList<Double>();
                    arrayList.add(0.12);
                    arrayList.add(0.023);
                    arrayList.add(0.3);
                    if (this.attacks > 2) {
                        iEntityPlayerSP2.setSprinting(false);
                        iEntityPlayerSP2.setServerSprintState(false);
                        for (Double d6 : arrayList) {
                            this.sendCriticalPacket(0.0, d6, 0.0, false);
                            this.attacks = 0;
                        }
                        break;
                    }
                    if (this.attacks != 0) break;
                    iEntityPlayerSP2.setSprinting(true);
                    iEntityPlayerSP2.setServerSprintState(true);
                    break;
                }
                case "aac4hyt": {
                    ++this.attacks;
                    if (this.attacks <= 5) break;
                    this.sendCriticalPacket(0.0, 0.0114514, 0.0, false);
                    this.sendCriticalPacket(0.0, 0.0019, 0.0, false);
                    this.sendCriticalPacket(0.0, 1.0E-6, 0.0, false);
                    this.attacks = 0;
                    break;
                }
                case "aac4": {
                    this.sendCriticalPacket(0.0, 2.593E-14, 0.0, false);
                    this.sendCriticalPacket(0.0, 0.01400000001304, 0.0, false);
                    this.sendCriticalPacket(0.0, 0.0012016413, 0.0, false);
                    break;
                }
                case "hyttest": {
                    IINetHandlerPlayClient iINetHandlerPlayClient = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP5 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP5 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d7 = iEntityPlayerSP5.getPosX();
                    IEntityPlayerSP iEntityPlayerSP6 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP6 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d8 = iEntityPlayerSP6.getPosY() + 1.100134977413E-5;
                    IEntityPlayerSP iEntityPlayerSP7 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP7 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d7, d8, iEntityPlayerSP7.getPosZ(), false));
                    IINetHandlerPlayClient iINetHandlerPlayClient5 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP8 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP8 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d9 = iEntityPlayerSP8.getPosX();
                    IEntityPlayerSP iEntityPlayerSP9 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP9 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d10 = iEntityPlayerSP9.getPosY() + 1.3487744E-10;
                    IEntityPlayerSP iEntityPlayerSP10 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP10 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient5.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d9, d10, iEntityPlayerSP10.getPosZ(), false));
                    IINetHandlerPlayClient iINetHandlerPlayClient6 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP11 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP11 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d11 = iEntityPlayerSP11.getPosX();
                    IEntityPlayerSP iEntityPlayerSP12 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP12 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d12 = iEntityPlayerSP12.getPosY() + 5.71003114589E-6;
                    IEntityPlayerSP iEntityPlayerSP13 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP13 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient6.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d11, d12, iEntityPlayerSP13.getPosZ(), false));
                    IINetHandlerPlayClient iINetHandlerPlayClient7 = MinecraftInstance.mc.getNetHandler();
                    IEntityPlayerSP iEntityPlayerSP14 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP14 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d13 = iEntityPlayerSP14.getPosX();
                    IEntityPlayerSP iEntityPlayerSP15 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP15 == null) {
                        Intrinsics.throwNpe();
                    }
                    double d14 = iEntityPlayerSP15.getPosY() + 1.578887744E-8;
                    IEntityPlayerSP iEntityPlayerSP16 = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP16 == null) {
                        Intrinsics.throwNpe();
                    }
                    iINetHandlerPlayClient7.addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d13, d14, iEntityPlayerSP16.getPosZ(), false));
                    break;
                }
                case "more": {
                    Criticals criticals = (Criticals)LiquidBounce.moduleManager.getModule(Criticals.class);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 1.0E-11, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.0, 0.0, false, 7, null);
                    break;
                }
                case "packetjump": {
                    Criticals criticals = (Criticals)LiquidBounce.moduleManager.getModule(Criticals.class);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.41999998688698, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.7531999805212, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 1.00133597911214, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 1.16610926093821, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 1.24918707874468, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 1.1707870772188, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 1.0155550727022, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.78502770378924, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.4807108763317, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.10408037809304, 0.0, false, 5, null);
                    Criticals.sendCriticalPacket$default(criticals, 0.0, 0.0, 0.0, true, 5, null);
                    break;
                }
                case "hytgrimac": {
                    this.n = this.attacks;
                    this.attacks = this.n + 1;
                    if (this.attacks <= 6) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.01, iEntityPlayerSP2.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 1.0E-10, iEntityPlayerSP2.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.114514, iEntityPlayerSP2.getPosZ(), false));
                    this.attacks = 0;
                    break;
                }
                case "hyttest4v4": {
                    this.n = this.attacks;
                    this.attacks = this.n + 1;
                    if (this.attacks < 6) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.01011, iEntityPlayerSP2.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 1.0E-10, iEntityPlayerSP2.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.10114, iEntityPlayerSP2.getPosZ(), false));
                    iEntityPlayerSP2.onCriticalHit(iEntityLivingBase);
                    this.attacks = 0;
                    break;
                }
                case "grimac2": {
                    this.n = this.attacks;
                    this.attacks = this.n + 1;
                    if (this.attacks < 12) break;
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.01011, iEntityPlayerSP2.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 1.0E-10, iEntityPlayerSP2.getPosZ(), false));
                    MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerPosition(d, d2 + 0.05014, iEntityPlayerSP2.getPosZ(), false));
                    iEntityPlayerSP2.onCriticalHit(iEntityLivingBase);
                    this.attacks = 0;
                    break;
                }
                case "autumn": {
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(d, d2 + 0.06250000001304, d3, true));
                    mc.getNetHandler().addToSendQueue(classProvider.createCPacketPlayerPosition(d, d2 + 0.06150000001304, d3, false));
                }
            }
            this.msTimer.reset();
        }
    }

    public final void setN(int n) {
        this.n = n;
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayer(iPacket) && StringsKt.equals((String)((String)this.modeValue.get()), (String)"NoGround", (boolean)true)) {
            iPacket.asCPacketPlayer().setOnGround(false);
        }
    }

    @Override
    public void onDisable() {
    }

    public final ListValue getModeValue() {
        return this.modeValue;
    }
}

