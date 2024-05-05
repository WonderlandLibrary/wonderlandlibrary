/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C0CPacketInput
 *  net.minecraft.network.play.client.C0FPacketConfirmTransaction
 *  net.minecraft.network.play.client.C18PacketSpectate
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.fun;

import java.util.LinkedList;
import java.util.Locale;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.misc.RandomUtils;
import net.ccbluex.liquidbounce.utils.timerNightX.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C18PacketSpectate;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="DisablerFixer", category=ModuleCategory.FUN, description="This is a new Disabler that is fully customized to your requirements.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u00103\u001a\u0002042\u0006\u00105\u001a\u000206J\b\u00107\u001a\u000204H\u0016J\b\u00108\u001a\u000204H\u0016J\u0010\u00109\u001a\u0002042\u0006\u0010:\u001a\u00020;H\u0007J\u0010\u0010<\u001a\u0002042\u0006\u0010:\u001a\u00020=H\u0007J\u0010\u0010>\u001a\u0002042\u0006\u0010:\u001a\u00020?H\u0007R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010&\u001a\b\u0012\u0004\u0012\u00020'0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00100\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006@"}, d2={"Lme/report/liquidware/modules/fun/DisablerFixer;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "alive", "Ljava/util/LinkedList;", "Lnet/minecraft/network/play/client/C00PacketKeepAlive;", "aliveBusMinSize", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "aliveDelay", "", "aliveDelayMode", "Lnet/ccbluex/liquidbounce/value/ListValue;", "aliveDupe", "aliveEnabled", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "aliveSendMethod", "alive_pTickDelay", "alive_poll_Max", "alive_poll_Min", "alive_sTick_MaxDelay", "alive_sTick_MinDelay", "cancelAlive", "cancelTrans", "clearAliveSent", "clearTransSent", "debugValue", "dynAliveDelay", "dynTransDelay", "flushWhenDisable", "lastAliveTick", "", "lastTransTick", "msTimerAlive", "Lnet/ccbluex/liquidbounce/utils/timerNightX/MSTimer;", "msTimerTrans", "ridingPacket", "ridingPriority", "spectatePacket", "trans", "Lnet/minecraft/network/play/client/C0FPacketConfirmTransaction;", "transBusMinSize", "transDelay", "transDelayMode", "transDupe", "transEnabled", "transSendMethod", "trans_pTickDelay", "trans_poll_Max", "trans_poll_Min", "trans_sTick_MaxDelay", "trans_sTick_MinDelay", "debug", "", "s", "", "onDisable", "onEnable", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "KyinoClient"})
public final class DisablerFixer
extends Module {
    private final BoolValue cancelTrans = new BoolValue("CancelTransactions", false);
    private final BoolValue cancelAlive = new BoolValue("CancelKeepAlive", false);
    private final BoolValue transEnabled = new BoolValue("Transactions", false, new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final BoolValue aliveEnabled = new BoolValue("KeepAlive", false, new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final ListValue ridingPacket = new ListValue("Riding", new String[]{"None", "EmptyArgs", "Valid"}, "None");
    private final ListValue spectatePacket = new ListValue("Spectate", new String[]{"None", "Random", "Player"}, "None");
    private final BoolValue ridingPriority = new BoolValue("Riding-Priority", true, new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return !StringsKt.equals((String)DisablerFixer.access$getRidingPacket$p(this.this$0).get(), "none", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final ListValue transDelayMode = new ListValue("Transactions-DelayMode", new String[]{"PlayerTick", "SystemTick", "Dynamic", "BusSize"}, "Dynamic", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final ListValue aliveDelayMode = new ListValue("KeepAlive-DelayMode", new String[]{"PlayerTick", "SystemTick", "Dynamic", "BusSize"}, "Dynamic", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final ListValue transSendMethod = new ListValue("Transactions-SendMethod", new String[]{"PollFirst", "PollLast", "FlushAll"}, "PollFirst", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final ListValue aliveSendMethod = new ListValue("KeepAlive-SendMethod", new String[]{"PollFirst", "PollLast", "FlushAll"}, "PollFirst", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final IntegerValue trans_pTickDelay = new IntegerValue("Transactions-PlayerTick-Delay", 1, 1, 2000, new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false && StringsKt.equals((String)DisablerFixer.access$getTransDelayMode$p(this.this$0).get(), "playertick", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final IntegerValue trans_sTick_MinDelay = new IntegerValue(this, "Transactions-SystemTick-MinDelay", 0, 0, 30000, "ms", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false && StringsKt.equals((String)DisablerFixer.access$getTransDelayMode$p(this.this$0).get(), "systemtick", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    }){
        final /* synthetic */ DisablerFixer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)DisablerFixer.access$getTrans_sTick_MaxDelay$p(this.this$0).get()).intValue();
            if (v < newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5, $super_call_param$6);
        }
    };
    private final IntegerValue trans_sTick_MaxDelay = new IntegerValue(this, "Transactions-SystemTick-MaxDelay", 0, 0, 30000, "ms", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false && StringsKt.equals((String)DisablerFixer.access$getTransDelayMode$p(this.this$0).get(), "systemtick", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    }){
        final /* synthetic */ DisablerFixer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)DisablerFixer.access$getTrans_sTick_MinDelay$p(this.this$0).get()).intValue();
            if (v > newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5, $super_call_param$6);
        }
    };
    private final IntegerValue alive_pTickDelay = new IntegerValue("KeepAlive-PlayerTick-Delay", 1, 1, 2000, new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false && StringsKt.equals((String)DisablerFixer.access$getAliveDelayMode$p(this.this$0).get(), "playertick", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final IntegerValue alive_sTick_MinDelay = new IntegerValue(this, "KeepAlive-SystemTick-MinDelay", 0, 0, 30000, "ms", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false && StringsKt.equals((String)DisablerFixer.access$getAliveDelayMode$p(this.this$0).get(), "systemtick", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    }){
        final /* synthetic */ DisablerFixer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)DisablerFixer.access$getAlive_sTick_MaxDelay$p(this.this$0).get()).intValue();
            if (v < newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5, $super_call_param$6);
        }
    };
    private final IntegerValue alive_sTick_MaxDelay = new IntegerValue(this, "KeepAlive-SystemTick-MaxDelay", 0, 0, 30000, "ms", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false && StringsKt.equals((String)DisablerFixer.access$getAliveDelayMode$p(this.this$0).get(), "systemtick", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    }){
        final /* synthetic */ DisablerFixer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)DisablerFixer.access$getAlive_sTick_MinDelay$p(this.this$0).get()).intValue();
            if (v > newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5, $super_call_param$6);
        }
    };
    private final IntegerValue trans_poll_Min = new IntegerValue(this, "Transactions-Poll-MinAmount", 1, 1, 300, (Function0)new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false && !StringsKt.equals((String)DisablerFixer.access$getTransSendMethod$p(this.this$0).get(), "flushall", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    }){
        final /* synthetic */ DisablerFixer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)DisablerFixer.access$getTrans_poll_Max$p(this.this$0).get()).intValue();
            if (v < newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final IntegerValue trans_poll_Max = new IntegerValue(this, "Transactions-Poll-MaxAmount", 1, 1, 300, (Function0)new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false && !StringsKt.equals((String)DisablerFixer.access$getTransSendMethod$p(this.this$0).get(), "flushall", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    }){
        final /* synthetic */ DisablerFixer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)DisablerFixer.access$getTrans_poll_Min$p(this.this$0).get()).intValue();
            if (v > newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final IntegerValue alive_poll_Min = new IntegerValue(this, "KeepAlive-Poll-MinAmount", 1, 1, 300, (Function0)new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false && !StringsKt.equals((String)DisablerFixer.access$getAliveSendMethod$p(this.this$0).get(), "flushall", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    }){
        final /* synthetic */ DisablerFixer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)DisablerFixer.access$getAlive_poll_Max$p(this.this$0).get()).intValue();
            if (v < newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final IntegerValue alive_poll_Max = new IntegerValue(this, "KeepAlive-Poll-MaxAmount", 1, 1, 300, (Function0)new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false && !StringsKt.equals((String)DisablerFixer.access$getAliveSendMethod$p(this.this$0).get(), "flushall", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    }){
        final /* synthetic */ DisablerFixer this$0;

        protected void onChanged(int oldValue, int newValue) {
            int v = ((Number)DisablerFixer.access$getAlive_poll_Min$p(this.this$0).get()).intValue();
            if (v > newValue) {
                this.set(v);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4, $super_call_param$5);
        }
    };
    private final IntegerValue transBusMinSize = new IntegerValue("Transactions-MinBusSize", 0, 0, 300, new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false && StringsKt.equals((String)DisablerFixer.access$getTransDelayMode$p(this.this$0).get(), "bussize", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final IntegerValue aliveBusMinSize = new IntegerValue("KeepAlive-MinBusSize", 0, 0, 300, new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false && StringsKt.equals((String)DisablerFixer.access$getAliveDelayMode$p(this.this$0).get(), "bussize", true);
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final IntegerValue transDupe = new IntegerValue("Transactions-DupeAmount", 1, 1, 100, "x", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final IntegerValue aliveDupe = new IntegerValue("KeepAlive-DupeAmount", 1, 1, 100, "x", new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final BoolValue clearTransSent = new BoolValue("Clear-Transactions-After-Send", false, new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelTrans$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getTransEnabled$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final BoolValue clearAliveSent = new BoolValue("Clear-Alive-After-Send", false, new Function0<Boolean>(this){
        final /* synthetic */ DisablerFixer this$0;

        public final boolean invoke() {
            return (Boolean)DisablerFixer.access$getCancelAlive$p(this.this$0).get() == false && (Boolean)DisablerFixer.access$getAliveEnabled$p(this.this$0).get() != false;
        }
        {
            this.this$0 = disablerFixer;
            super(0);
        }
    });
    private final BoolValue flushWhenDisable = new BoolValue("Flush-When-Disable", false);
    private final BoolValue debugValue = new BoolValue("Debug", false);
    private final LinkedList<C0FPacketConfirmTransaction> trans = new LinkedList();
    private final LinkedList<C00PacketKeepAlive> alive = new LinkedList();
    private final MSTimer msTimerTrans = new MSTimer();
    private final MSTimer msTimerAlive = new MSTimer();
    private int dynTransDelay;
    private int dynAliveDelay;
    private long lastTransTick;
    private long lastAliveTick;
    private int transDelay;
    private int aliveDelay;

    public final void debug(@NotNull String s) {
        Intrinsics.checkParameterIsNotNull(s, "s");
        if (((Boolean)this.debugValue.get()).booleanValue()) {
            ClientUtils.displayChatMessage("\u00a7c\u00a7l>> " + "\u00a7f" + s);
        }
    }

    @Override
    public void onEnable() {
        this.lastTransTick = 0L;
        this.lastAliveTick = 0L;
        this.transDelay = RandomUtils.nextInt(((Number)this.trans_sTick_MinDelay.get()).intValue(), ((Number)this.trans_sTick_MaxDelay.get()).intValue());
        this.aliveDelay = RandomUtils.nextInt(((Number)this.alive_sTick_MinDelay.get()).intValue(), ((Number)this.alive_sTick_MaxDelay.get()).intValue());
        this.alive.clear();
        this.trans.clear();
        this.msTimerTrans.reset();
        this.msTimerAlive.reset();
    }

    @Override
    public void onDisable() {
        if (((Boolean)this.flushWhenDisable.get()).booleanValue()) {
            try {
                C00PacketKeepAlive it;
                boolean $i$f$forEach;
                Iterable $this$forEach$iv;
                if (((Boolean)this.aliveEnabled.get()).booleanValue() && !this.alive.isEmpty()) {
                    $this$forEach$iv = this.alive;
                    $i$f$forEach = false;
                    for (Object element$iv : $this$forEach$iv) {
                        it = (C00PacketKeepAlive)element$iv;
                        boolean bl = false;
                        PacketUtils.sendPacketNoEvent((Packet)it);
                    }
                }
                if (((Boolean)this.transEnabled.get()).booleanValue() && !this.trans.isEmpty()) {
                    $this$forEach$iv = this.trans;
                    $i$f$forEach = false;
                    for (Object element$iv : $this$forEach$iv) {
                        it = (C0FPacketConfirmTransaction)element$iv;
                        boolean bl = false;
                        PacketUtils.sendPacketNoEvent((Packet)it);
                    }
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        this.lastTransTick = 0L;
        this.lastAliveTick = 0L;
        this.transDelay = RandomUtils.nextInt(((Number)this.trans_sTick_MinDelay.get()).intValue(), ((Number)this.trans_sTick_MaxDelay.get()).intValue());
        this.aliveDelay = RandomUtils.nextInt(((Number)this.alive_sTick_MinDelay.get()).intValue(), ((Number)this.alive_sTick_MaxDelay.get()).intValue());
        this.alive.clear();
        this.trans.clear();
        this.msTimerTrans.reset();
        this.msTimerAlive.reset();
        DisablerFixer.access$getMc$p$s1046033730().field_71428_T.field_74278_d = 1.0f;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        int it;
        int n;
        int n2;
        boolean bl;
        int n3;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (DisablerFixer.access$getMc$p$s1046033730().field_71439_g == null || DisablerFixer.access$getMc$p$s1046033730().field_71441_e == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof C0FPacketConfirmTransaction && ((Boolean)this.cancelTrans.get()).booleanValue()) {
            event.cancelEvent();
            this.debug("cancelled c0f");
            return;
        }
        if (packet instanceof C00PacketKeepAlive && ((Boolean)this.cancelAlive.get()).booleanValue()) {
            event.cancelEvent();
            this.debug("cancelled c00");
            return;
        }
        if (packet instanceof C03PacketPlayer) {
            if (!StringsKt.equals((String)this.ridingPacket.get(), "none", true) && ((Boolean)this.ridingPriority.get()).booleanValue()) {
                Minecraft minecraft = DisablerFixer.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)(StringsKt.equals((String)this.ridingPacket.get(), "emptyargs", true) ? new C0CPacketInput() : new C0CPacketInput(RangesKt.coerceAtMost(DisablerFixer.access$getMc$p$s1046033730().field_71439_g.field_70702_br, 0.98f), RangesKt.coerceAtMost(DisablerFixer.access$getMc$p$s1046033730().field_71439_g.field_70701_bs, 0.98f), DisablerFixer.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78901_c, DisablerFixer.access$getMc$p$s1046033730().field_71439_g.field_71158_b.field_78899_d)));
                this.debug("pre c0c");
            }
            if (!StringsKt.equals((String)this.spectatePacket.get(), "none", true)) {
                UUID uUID;
                Minecraft minecraft = DisablerFixer.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                NetHandlerPlayClient netHandlerPlayClient = minecraft.func_147114_u();
                if (StringsKt.equals((String)this.spectatePacket.get(), "player", true)) {
                    EntityPlayerSP entityPlayerSP = DisablerFixer.access$getMc$p$s1046033730().field_71439_g;
                    Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
                    uUID = entityPlayerSP.func_110124_au();
                } else {
                    uUID = UUID.randomUUID();
                }
                netHandlerPlayClient.func_147297_a((Packet)new C18PacketSpectate(uUID));
                this.debug("spectate");
            }
            if (!StringsKt.equals((String)this.ridingPacket.get(), "none", true) && !((Boolean)this.ridingPriority.get()).booleanValue()) {
                Minecraft minecraft = DisablerFixer.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)(StringsKt.equals((String)this.ridingPacket.get(), "emptyargs", true) ? new C0CPacketInput() : new C0CPacketInput(0.98f, 0.98f, false, false)));
                this.debug("post c0c");
            }
        }
        if (packet instanceof C0FPacketConfirmTransaction && ((Boolean)this.transEnabled.get()).booleanValue()) {
            if (this.lastTransTick != 0L) {
                this.dynTransDelay = (int)(System.currentTimeMillis() - this.lastTransTick);
            }
            this.lastTransTick = System.currentTimeMillis();
            n3 = ((Number)this.transDupe.get()).intValue();
            bl = false;
            n2 = 0;
            n2 = 0;
            n = n3;
            while (n2 < n) {
                it = n2++;
                boolean bl2 = false;
                this.trans.add((C0FPacketConfirmTransaction)packet);
            }
            event.cancelEvent();
            this.debug("duped c0f " + ((Number)this.transDupe.get()).intValue() + "x, dynamic delay: " + this.dynTransDelay + "ms. detail: uid " + ((C0FPacketConfirmTransaction)packet).field_149534_b + ", windowId " + ((C0FPacketConfirmTransaction)packet).field_149536_a);
        }
        if (packet instanceof C00PacketKeepAlive && ((Boolean)this.aliveEnabled.get()).booleanValue()) {
            if (this.lastAliveTick != 0L) {
                this.dynAliveDelay = (int)(System.currentTimeMillis() - this.lastAliveTick);
            }
            this.lastAliveTick = System.currentTimeMillis();
            n3 = ((Number)this.aliveDupe.get()).intValue();
            bl = false;
            n2 = 0;
            n2 = 0;
            n = n3;
            while (n2 < n) {
                it = n2++;
                boolean bl3 = false;
                this.alive.add((C00PacketKeepAlive)packet);
            }
            event.cancelEvent();
            this.debug("duped c00 " + ((Number)this.aliveDupe.get()).intValue() + "x, dynamic delay: " + this.dynAliveDelay + "ms. detail: key " + ((C00PacketKeepAlive)packet).field_149461_a);
        }
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.lastTransTick = 0L;
        this.lastAliveTick = 0L;
        this.transDelay = RandomUtils.nextInt(((Number)this.trans_sTick_MinDelay.get()).intValue(), ((Number)this.trans_sTick_MaxDelay.get()).intValue());
        this.aliveDelay = RandomUtils.nextInt(((Number)this.alive_sTick_MinDelay.get()).intValue(), ((Number)this.alive_sTick_MaxDelay.get()).intValue());
        this.alive.clear();
        this.trans.clear();
        this.msTimerTrans.reset();
        this.msTimerAlive.reset();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        int it;
        int n;
        boolean sendWhen;
        int n2;
        Locale locale;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (DisablerFixer.access$getMc$p$s1046033730().field_71439_g == null || DisablerFixer.access$getMc$p$s1046033730().field_71441_e == null) {
            return;
        }
        if (((Boolean)this.transEnabled.get()).booleanValue() && !((Boolean)this.cancelTrans.get()).booleanValue()) {
            String string = (String)this.transDelayMode.get();
            Locale locale2 = Locale.getDefault();
            Intrinsics.checkExpressionValueIsNotNull(locale2, "Locale.getDefault()");
            locale = locale2;
            n2 = 0;
            String string2 = string;
            if (string2 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string3 = string2.toLowerCase(locale);
            Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase(locale)");
            switch (string3) {
                case "playertick": {
                    boolean bl;
                    if (DisablerFixer.access$getMc$p$s1046033730().field_71439_g.field_70173_aa > 0 && DisablerFixer.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % ((Number)this.trans_pTickDelay.get()).intValue() == 0) {
                        bl = true;
                        break;
                    }
                    bl = false;
                    break;
                }
                case "systemtick": {
                    boolean bl = this.msTimerTrans.hasTimePassed(this.transDelay);
                    break;
                }
                case "dynamic": {
                    boolean bl = this.msTimerTrans.hasTimePassed(this.dynTransDelay);
                    break;
                }
                default: {
                    boolean bl = sendWhen = this.trans.size() >= ((Number)this.transBusMinSize.get()).intValue();
                }
            }
            if (!this.trans.isEmpty() && sendWhen) {
                if (StringsKt.equals((String)this.transSendMethod.get(), "flushall", true)) {
                    while (this.trans.size() > 0) {
                        C0FPacketConfirmTransaction c0FPacketConfirmTransaction = this.trans.poll();
                        Intrinsics.checkExpressionValueIsNotNull(c0FPacketConfirmTransaction, "trans.poll()");
                        PacketUtils.sendPacketNoEvent((Packet)c0FPacketConfirmTransaction);
                    }
                    this.debug("flushed.");
                } else {
                    int hake = RangesKt.coerceAtMost(RandomUtils.nextInt(((Number)this.trans_poll_Min.get()).intValue(), ((Number)this.trans_poll_Max.get()).intValue()), this.trans.size());
                    boolean bl = false;
                    n2 = 0;
                    n = hake;
                    for (n2 = 0; n2 < n; ++n2) {
                        it = n2;
                        boolean bl2 = false;
                        if (((String)this.transSendMethod.get()).equals("pollfirst")) {
                            C0FPacketConfirmTransaction c0FPacketConfirmTransaction = this.trans.pollFirst();
                            Intrinsics.checkExpressionValueIsNotNull(c0FPacketConfirmTransaction, "trans.pollFirst()");
                            PacketUtils.sendPacketNoEvent((Packet)c0FPacketConfirmTransaction);
                            continue;
                        }
                        C0FPacketConfirmTransaction c0FPacketConfirmTransaction = this.trans.pollLast();
                        Intrinsics.checkExpressionValueIsNotNull(c0FPacketConfirmTransaction, "trans.pollLast()");
                        PacketUtils.sendPacketNoEvent((Packet)c0FPacketConfirmTransaction);
                    }
                    this.debug("poll " + hake + " times.");
                }
                if (((Boolean)this.clearTransSent.get()).booleanValue()) {
                    this.trans.clear();
                }
                this.transDelay = RandomUtils.nextInt(((Number)this.trans_sTick_MinDelay.get()).intValue(), ((Number)this.trans_sTick_MaxDelay.get()).intValue());
                this.msTimerTrans.reset();
            }
        }
        if (((Boolean)this.aliveEnabled.get()).booleanValue() && !((Boolean)this.cancelAlive.get()).booleanValue()) {
            String hake = (String)this.aliveDelayMode.get();
            Locale locale3 = Locale.getDefault();
            Intrinsics.checkExpressionValueIsNotNull(locale3, "Locale.getDefault()");
            locale = locale3;
            n2 = 0;
            String string = hake;
            if (string == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string4 = string.toLowerCase(locale);
            Intrinsics.checkExpressionValueIsNotNull(string4, "(this as java.lang.String).toLowerCase(locale)");
            switch (string4) {
                case "playertick": {
                    boolean bl;
                    if (DisablerFixer.access$getMc$p$s1046033730().field_71439_g.field_70173_aa > 0 && DisablerFixer.access$getMc$p$s1046033730().field_71439_g.field_70173_aa % ((Number)this.alive_pTickDelay.get()).intValue() == 0) {
                        bl = true;
                        break;
                    }
                    bl = false;
                    break;
                }
                case "systemtick": {
                    boolean bl = this.msTimerAlive.hasTimePassed(this.aliveDelay);
                    break;
                }
                case "dynamic": {
                    boolean bl = this.msTimerAlive.hasTimePassed(this.dynAliveDelay);
                    break;
                }
                default: {
                    boolean bl = sendWhen = this.alive.size() >= ((Number)this.aliveBusMinSize.get()).intValue();
                }
            }
            if (!this.alive.isEmpty() && sendWhen) {
                if (StringsKt.equals((String)this.aliveSendMethod.get(), "flushall", true)) {
                    while (this.alive.size() > 0) {
                        C00PacketKeepAlive c00PacketKeepAlive = this.alive.poll();
                        Intrinsics.checkExpressionValueIsNotNull(c00PacketKeepAlive, "alive.poll()");
                        PacketUtils.sendPacketNoEvent((Packet)c00PacketKeepAlive);
                    }
                    this.debug("flushed.");
                } else {
                    int hake2 = RangesKt.coerceAtMost(RandomUtils.nextInt(((Number)this.alive_poll_Min.get()).intValue(), ((Number)this.alive_poll_Max.get()).intValue()), this.alive.size());
                    boolean bl = false;
                    n2 = 0;
                    n = hake2;
                    for (n2 = 0; n2 < n; ++n2) {
                        it = n2;
                        boolean bl3 = false;
                        if (((String)this.aliveSendMethod.get()).equals("pollfirst")) {
                            C00PacketKeepAlive c00PacketKeepAlive = this.alive.pollFirst();
                            Intrinsics.checkExpressionValueIsNotNull(c00PacketKeepAlive, "alive.pollFirst()");
                            PacketUtils.sendPacketNoEvent((Packet)c00PacketKeepAlive);
                            continue;
                        }
                        C00PacketKeepAlive c00PacketKeepAlive = this.alive.pollLast();
                        Intrinsics.checkExpressionValueIsNotNull(c00PacketKeepAlive, "alive.pollLast()");
                        PacketUtils.sendPacketNoEvent((Packet)c00PacketKeepAlive);
                    }
                    this.debug("poll " + hake2 + " times.");
                }
                if (((Boolean)this.clearAliveSent.get()).booleanValue()) {
                    this.alive.clear();
                }
                this.aliveDelay = RandomUtils.nextInt(((Number)this.alive_sTick_MinDelay.get()).intValue(), ((Number)this.alive_sTick_MaxDelay.get()).intValue());
                this.msTimerAlive.reset();
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }

    public static final /* synthetic */ BoolValue access$getCancelTrans$p(DisablerFixer $this) {
        return $this.cancelTrans;
    }

    public static final /* synthetic */ BoolValue access$getCancelAlive$p(DisablerFixer $this) {
        return $this.cancelAlive;
    }

    public static final /* synthetic */ ListValue access$getRidingPacket$p(DisablerFixer $this) {
        return $this.ridingPacket;
    }

    public static final /* synthetic */ BoolValue access$getTransEnabled$p(DisablerFixer $this) {
        return $this.transEnabled;
    }

    public static final /* synthetic */ BoolValue access$getAliveEnabled$p(DisablerFixer $this) {
        return $this.aliveEnabled;
    }

    public static final /* synthetic */ ListValue access$getTransDelayMode$p(DisablerFixer $this) {
        return $this.transDelayMode;
    }

    public static final /* synthetic */ IntegerValue access$getTrans_sTick_MaxDelay$p(DisablerFixer $this) {
        return $this.trans_sTick_MaxDelay;
    }

    public static final /* synthetic */ IntegerValue access$getTrans_sTick_MinDelay$p(DisablerFixer $this) {
        return $this.trans_sTick_MinDelay;
    }

    public static final /* synthetic */ ListValue access$getAliveDelayMode$p(DisablerFixer $this) {
        return $this.aliveDelayMode;
    }

    public static final /* synthetic */ IntegerValue access$getAlive_sTick_MaxDelay$p(DisablerFixer $this) {
        return $this.alive_sTick_MaxDelay;
    }

    public static final /* synthetic */ IntegerValue access$getAlive_sTick_MinDelay$p(DisablerFixer $this) {
        return $this.alive_sTick_MinDelay;
    }

    public static final /* synthetic */ IntegerValue access$getTrans_poll_Max$p(DisablerFixer $this) {
        return $this.trans_poll_Max;
    }

    public static final /* synthetic */ ListValue access$getTransSendMethod$p(DisablerFixer $this) {
        return $this.transSendMethod;
    }

    public static final /* synthetic */ IntegerValue access$getTrans_poll_Min$p(DisablerFixer $this) {
        return $this.trans_poll_Min;
    }

    public static final /* synthetic */ IntegerValue access$getAlive_poll_Max$p(DisablerFixer $this) {
        return $this.alive_poll_Max;
    }

    public static final /* synthetic */ ListValue access$getAliveSendMethod$p(DisablerFixer $this) {
        return $this.aliveSendMethod;
    }

    public static final /* synthetic */ IntegerValue access$getAlive_poll_Min$p(DisablerFixer $this) {
        return $this.alive_poll_Min;
    }
}

