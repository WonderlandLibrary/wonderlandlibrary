/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ThreadLocalRandom
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import io.netty.util.internal.ThreadLocalRandom;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.AttackEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.Criticals;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PlayerUtil;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="Criticals", description="Automatically deals critical hits.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020'H\u0007J\b\u0010(\u001a\u00020%H\u0016J\u0010\u0010)\u001a\u00020%2\u0006\u0010&\u001a\u00020*H\u0007J\u0018\u0010+\u001a\u00020\u00062\u0006\u0010,\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0015\u001a\u00020\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0019\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u001c\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001e\u001a\u0004\u0018\u00010\u001f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u000e\u0010\"\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/Criticals;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "aac5lowjumptimer", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "attacked", "", "canCrits", "", "counter", "debugValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "getDelayValue", "()Lnet/ccbluex/liquidbounce/value/IntegerValue;", "hurtTimeValue", "hytMorePacketValue", "jumpHeightValue", "minemoraTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "msTimer", "getMsTimer", "()Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "onlyKillAuraValue", "readyCrits", "tag", "", "getTag", "()Ljava/lang/String;", "timerValue", "usedTimer", "onAttack", "", "event", "Lnet/ccbluex/liquidbounce/event/AttackEvent;", "onEnable", "onPacket", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "randomNumber", "max", "min", "KyinoClient"})
public final class Criticals
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Vulcan", "BlocksmcTest", "NewPacket", "Packet", "NCPPacket", "NoGround", "AACv3", "AACv4", "Hop", "TPHop", "Jump", "Visual", "LowJump", "Redesky", "Edit", "HuaYuTing", "Watchdog", "Watchdoge", "AAC5.0.14HYT", "AAC5LowJump", "AAC5", "AAC5.0.4", "AAC5.2.0", "AAC5Packet"}, "Packet");
    private final BoolValue onlyKillAuraValue = new BoolValue("OnlyAura", true);
    @NotNull
    private final IntegerValue delayValue = new IntegerValue("Delay", 0, 0, 500);
    private final IntegerValue hurtTimeValue = new IntegerValue("HurtTime", 10, 0, 10);
    private final FloatValue jumpHeightValue = new FloatValue("JumpHeight", 0.42f, 0.1f, 0.42f);
    private final BoolValue hytMorePacketValue = new BoolValue("HuaYuTing-MorePacket", false);
    private final FloatValue timerValue = new FloatValue("Timer", 0.82f, 0.1f, 1.0f);
    private final FloatValue aac5lowjumptimer = new FloatValue("AAC5-LowJumpTimer", 1.0f, 0.1f, 10.0f);
    private final BoolValue debugValue = new BoolValue("DebugMessage", false);
    private final MSTimer minemoraTimer = new MSTimer();
    private boolean usedTimer;
    @NotNull
    private final MSTimer msTimer = new MSTimer();
    private boolean readyCrits;
    private boolean canCrits = true;
    private int counter;
    private int attacked;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    @NotNull
    public final IntegerValue getDelayValue() {
        return this.delayValue;
    }

    @NotNull
    public final MSTimer getMsTimer() {
        return this.msTimer;
    }

    @Override
    public void onEnable() {
        if (StringsKt.equals((String)this.modeValue.get(), "NoGround", true)) {
            Criticals.access$getMc$p$s1046033730().field_71439_g.func_70664_aZ();
        }
        this.canCrits = true;
        this.counter = 0;
    }

    @EventTarget
    public final void onAttack(@NotNull AttackEvent event) {
        block54: {
            Entity entity;
            block56: {
                block55: {
                    Intrinsics.checkParameterIsNotNull(event, "event");
                    if (!(event.getTargetEntity() instanceof EntityLivingBase)) break block54;
                    entity = event.getTargetEntity();
                    if (!Criticals.access$getMc$p$s1046033730().field_71439_g.field_70122_E) break block55;
                    EntityPlayerSP entityPlayerSP = Criticals.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    if (entityPlayerSP.func_70617_f_() || Criticals.access$getMc$p$s1046033730().field_71439_g.field_70134_J) break block55;
                    EntityPlayerSP entityPlayerSP2 = Criticals.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP2, "mc.thePlayer");
                    if (entityPlayerSP2.func_70090_H()) break block55;
                    EntityPlayerSP entityPlayerSP3 = Criticals.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP3, "mc.thePlayer");
                    if (entityPlayerSP3.func_180799_ab() || Criticals.access$getMc$p$s1046033730().field_71439_g.field_70154_o != null || ((EntityLivingBase)entity).field_70737_aN > ((Number)this.hurtTimeValue.get()).intValue()) break block55;
                    Module module = LiquidBounce.INSTANCE.getModuleManager().get(Fly.class);
                    if (module == null) {
                        Intrinsics.throwNpe();
                    }
                    if (!module.getState() && this.msTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) break block56;
                }
                return;
            }
            double x = Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
            double y = Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
            double z = Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
            double[] llllllllllllllllIllIIIllIlllllIl = null;
            int llllllllllllllllIllIIIllIllllllI = 0;
            int llllllllllllllllIllIIIllIllIlIll = 0;
            onAttack.1 $fun$sendCriticalPacket$1 = onAttack.1.INSTANCE;
            String string = (String)this.modeValue.get();
            int n = 0;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
            switch (string3) {
                case "vulcan": {
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.0695, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.0034561124114514, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 1.66412E-4, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                    break;
                }
                case "blocksmctest": {
                    n = this.attacked;
                    this.attacked = n + 1;
                    if (this.attacked < 5) break;
                    Minecraft minecraft = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.058293536E-5, z, false));
                    Minecraft minecraft2 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                    minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 9.16580235E-6, z, false));
                    Minecraft minecraft3 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                    minecraft3.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.0371854E-7, z, false));
                    this.attacked = 0;
                    break;
                }
                case "aac5packet": {
                    Minecraft minecraft = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 2.593E-14, z, true));
                    Minecraft minecraft4 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft4, "mc");
                    minecraft4.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    break;
                }
                case "aac5lowjump": {
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.114514886;
                    Criticals.access$getMc$p$s1046033730().field_71428_T.field_74278_d = ((Number)this.aac5lowjumptimer.get()).floatValue();
                    break;
                }
                case "aac5.0.4": {
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.00133545, 0.0, false, 5, null);
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, -4.33E-7, 0.0, false, 5, null);
                    break;
                }
                case "watchdoge": {
                    Criticals.access$getMc$p$s1046033730().field_71439_g.func_71009_b(entity);
                    break;
                }
                case "huayuting": {
                    if (((Number)this.timerValue.get()).floatValue() != 1.0f) {
                        this.minemoraTimer.reset();
                        this.usedTimer = true;
                        Criticals.access$getMc$p$s1046033730().field_71428_T.field_74278_d = ((Number)this.timerValue.get()).floatValue();
                    }
                    if (((Boolean)this.hytMorePacketValue.get()).booleanValue()) {
                        onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.05250000000101, 0.0, false, 5, null);
                    }
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.00133545, 0.0, false, 5, null);
                    if (((Boolean)this.hytMorePacketValue.get()).booleanValue()) {
                        onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.0140000000101, 0.0, false, 5, null);
                    }
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, -4.33E-7, 0.0, false, 5, null);
                    break;
                }
                case "aac5.0.14hyt": {
                    if (((Number)this.timerValue.get()).floatValue() != 1.0f) {
                        this.minemoraTimer.reset();
                        this.usedTimer = true;
                        Criticals.access$getMc$p$s1046033730().field_71428_T.field_74278_d = ((Number)this.timerValue.get()).floatValue();
                    }
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.05250000001304, 0.0, false, 5, null);
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.02324649713461, 0.0, false, 5, null);
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.0014749900000101, 0.0, false, 5, null);
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.12, 0.0, false, 5, null);
                    break;
                }
                case "aac5": {
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.0625, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.09858, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.04114514, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_71174_a.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 0.025, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v, false));
                    break;
                }
                case "watchdog": {
                    llllllllllllllllIllIIIllIlllllIl = new double[]{0.1, 0.0513 + (double)ThreadLocalRandom.current().nextFloat() / 1000.0, 0.04863327};
                    Minecraft minecraft = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.10897235992938976, z, false));
                    llllllllllllllllIllIIIllIllIlIll = llllllllllllllllIllIIIllIlllllIl.length;
                    for (llllllllllllllllIllIIIllIllllllI = 0; llllllllllllllllIllIIIllIllllllI < llllllllllllllllIllIIIllIllIlIll; ++llllllllllllllllIllIIIllIllllllI) {
                        Minecraft minecraft5 = Criticals.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft5, "mc");
                        minecraft5.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + llllllllllllllllIllIIIllIlllllIl[llllllllllllllllIllIIIllIllllllI], z, false));
                    }
                    break;
                }
                case "newpacket": {
                    Minecraft minecraft = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.05250000001304, z, true));
                    Minecraft minecraft6 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft6, "mc");
                    minecraft6.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.00150000001304, z, false));
                    Minecraft minecraft7 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft7, "mc");
                    minecraft7.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.01400000001304, z, false));
                    Minecraft minecraft8 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft8, "mc");
                    minecraft8.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.00150000001304, z, false));
                    Criticals.access$getMc$p$s1046033730().field_71439_g.func_71009_b(entity);
                    break;
                }
                case "packet": {
                    Minecraft minecraft = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625, z, true));
                    Minecraft minecraft9 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft9, "mc");
                    minecraft9.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    Minecraft minecraft10 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft10, "mc");
                    minecraft10.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.1E-5, z, false));
                    Minecraft minecraft11 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft11, "mc");
                    minecraft11.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                    Criticals.access$getMc$p$s1046033730().field_71439_g.func_71009_b(entity);
                    break;
                }
                case "ncppacket": {
                    Minecraft minecraft = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.11, z, false));
                    Minecraft minecraft12 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft12, "mc");
                    minecraft12.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.1100013579, z, false));
                    Minecraft minecraft13 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft13, "mc");
                    minecraft13.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.3579E-6, z, false));
                    Criticals.access$getMc$p$s1046033730().field_71439_g.func_71009_b(entity);
                    break;
                }
                case "aacv3": {
                    Minecraft minecraft = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x + (double)250, y + 0.005, z + (double)250, true));
                    Minecraft minecraft14 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft14, "mc");
                    minecraft14.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x + 0.1, y + 0.1, z + 0.1, false));
                    Minecraft minecraft15 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft15, "mc");
                    minecraft15.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.782, z, false));
                    Minecraft minecraft16 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft16, "mc");
                    minecraft16.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z + (double)150, false));
                    Criticals.access$getMc$p$s1046033730().field_71439_g.func_71009_b(entity);
                    break;
                }
                case "aacv4": {
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_70179_y *= 0.0;
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_70159_w *= 0.0;
                    Minecraft minecraft = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 3.0E-14, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                    Minecraft minecraft17 = Criticals.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft17, "mc");
                    minecraft17.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Criticals.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70163_u + 8.0E-15, Criticals.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
                    break;
                }
                case "jump": {
                    if (Criticals.access$getMc$p$s1046033730().field_71439_g.field_70122_E) {
                        Criticals.access$getMc$p$s1046033730().field_71439_g.field_70181_x = ((Number)this.jumpHeightValue.get()).floatValue();
                        break;
                    }
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_70181_x -= (double)0.0f;
                    break;
                }
                case "hypixel": {
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.04132332, 0.0, false, 5, null);
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.023243243674, 0.0, false, 5, null);
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.01, 0.0, false, 5, null);
                    onAttack.1.invoke$default($fun$sendCriticalPacket$1, 0.0, 0.0011, 0.0, false, 5, null);
                    break;
                }
                case "visual": {
                    Criticals.access$getMc$p$s1046033730().field_71439_g.func_71009_b(entity);
                    break;
                }
                case "lowjump": {
                    Criticals.access$getMc$p$s1046033730().field_71439_g.field_70181_x = 0.3425;
                }
            }
            if (((Boolean)this.debugValue.get()).booleanValue()) {
                PlayerUtil.tellPlayer("\u00a7oCrit: \u00a7c\u00a7o" + this.randomNumber(-9999, 9999));
            }
            this.readyCrits = true;
            this.msTimer.reset();
        }
    }

    private final int randomNumber(int max, int min) {
        return (int)(Math.random() * (double)(max - min)) + min;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (((Boolean)this.onlyKillAuraValue.get()).booleanValue()) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().get(KillAura.class);
            if (module == null) {
                Intrinsics.throwNpe();
            }
            if (!module.getState()) {
                return;
            }
        }
        Packet<?> packet = event.getPacket();
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "aac5.2.0": {
                if (packet instanceof C03PacketPlayer) {
                    C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
                    if (Criticals.access$getMc$p$s1046033730().field_71439_g.field_70122_E && this.canCrits) {
                        packetPlayer.field_149477_b += 1.0E-6;
                        packetPlayer.field_149474_g = false;
                    }
                    if (Criticals.access$getMc$p$s1046033730().field_71441_e.func_72945_a((Entity)Criticals.access$getMc$p$s1046033730().field_71439_g, Criticals.access$getMc$p$s1046033730().field_71439_g.func_174813_aQ().func_72317_d(0.0, (Criticals.access$getMc$p$s1046033730().field_71439_g.field_70181_x - 0.08) * 0.98, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                        packetPlayer.field_149474_g = true;
                    }
                }
                if (!(packet instanceof C07PacketPlayerDigging)) break;
                if (((C07PacketPlayerDigging)packet).func_180762_c() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    this.canCrits = false;
                    break;
                }
                if (((C07PacketPlayerDigging)packet).func_180762_c() != C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK && ((C07PacketPlayerDigging)packet).func_180762_c() != C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) break;
                this.canCrits = true;
                break;
            }
            case "redesky": {
                if (packet instanceof C03PacketPlayer) {
                    C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
                    if (Criticals.access$getMc$p$s1046033730().field_71439_g.field_70122_E && this.canCrits) {
                        packetPlayer.field_149477_b += 1.0E-6;
                        packetPlayer.field_149474_g = false;
                    }
                    if (Criticals.access$getMc$p$s1046033730().field_71441_e.func_72945_a((Entity)Criticals.access$getMc$p$s1046033730().field_71439_g, Criticals.access$getMc$p$s1046033730().field_71439_g.func_174813_aQ().func_72317_d(0.0, (Criticals.access$getMc$p$s1046033730().field_71439_g.field_70181_x - 0.08) * 0.98, 0.0).func_72314_b(0.0, 0.0, 0.0)).isEmpty()) {
                        packetPlayer.field_149474_g = true;
                    }
                }
                if (!(packet instanceof C07PacketPlayerDigging)) break;
                if (((C07PacketPlayerDigging)packet).func_180762_c() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    this.canCrits = false;
                    break;
                }
                if (((C07PacketPlayerDigging)packet).func_180762_c() != C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK && ((C07PacketPlayerDigging)packet).func_180762_c() != C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) break;
                this.canCrits = true;
                break;
            }
            case "noground": {
                if (!(packet instanceof C03PacketPlayer)) break;
                ((C03PacketPlayer)packet).field_149474_g = false;
                break;
            }
            case "edit": {
                if (!this.readyCrits) break;
                if (packet instanceof C03PacketPlayer) {
                    ((C03PacketPlayer)packet).field_149474_g = false;
                }
                this.readyCrits = false;
            }
        }
    }

    @Override
    @Nullable
    public String getTag() {
        return (String)this.modeValue.get();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

