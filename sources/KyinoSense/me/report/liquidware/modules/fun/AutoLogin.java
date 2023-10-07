/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C01PacketChatMessage
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S45PacketTitle
 *  net.minecraft.util.IChatComponent
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.fun;

import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.utils.misc.SoundFxPlayer;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;
import obfuscator.NativeMethod;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoLogin", description="Automatically login into some servers for you.", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\u0010\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0017H\u0007J\u0010\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0019H\u0007J\u0010\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u001bH\u0007J\b\u0010\u001c\u001a\u00020\u0014H\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u0010!\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2={"Lme/report/liquidware/modules/fun/AutoLogin;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "logTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "loginCmd", "Lnet/ccbluex/liquidbounce/value/TextValue;", "loginPackets", "Ljava/util/ArrayList;", "Lnet/minecraft/network/play/client/C01PacketChatMessage;", "Lkotlin/collections/ArrayList;", "loginRegex", "password", "regCmd", "regRegex", "regTimer", "registerPackets", "onEnable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "resetEverything", "sendLogin", "", "str", "", "sendRegister", "KyinoClient"})
public final class AutoLogin
extends Module {
    private final TextValue password = new TextValue("Password", "example@01");
    private final TextValue regRegex = new TextValue("RegisterRegex", "/register");
    private final TextValue loginRegex = new TextValue("LoginRegex", "/login");
    private final TextValue regCmd = new TextValue("RegisterCmd", "/register %p %p");
    private final TextValue loginCmd = new TextValue("LoginCmd", "/login %p");
    private final IntegerValue delayValue = new IntegerValue("Delay", 5000, 0, 5000);
    private final ArrayList<C01PacketChatMessage> loginPackets;
    private final ArrayList<C01PacketChatMessage> registerPackets;
    private final MSTimer regTimer;
    private final MSTimer logTimer;

    @Override
    public void onEnable() {
        this.resetEverything();
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.resetEverything();
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onUpdate(@NotNull UpdateEvent event) {
        C01PacketChatMessage packet;
        Iterator<C01PacketChatMessage> iterator2;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.registerPackets.isEmpty()) {
            this.regTimer.reset();
        } else if (this.regTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            iterator2 = this.registerPackets.iterator();
            while (iterator2.hasNext()) {
                C01PacketChatMessage c01PacketChatMessage = packet = iterator2.next();
                Intrinsics.checkExpressionValueIsNotNull(c01PacketChatMessage, "packet");
                PacketUtils.sendPacketNoEvent((Packet)c01PacketChatMessage);
            }
            new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Successfully registered", Notification.Type.INFO));
            this.registerPackets.clear();
            this.regTimer.reset();
        }
        if (this.loginPackets.isEmpty()) {
            this.logTimer.reset();
        } else if (this.logTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            iterator2 = this.loginPackets.iterator();
            while (iterator2.hasNext()) {
                C01PacketChatMessage c01PacketChatMessage = packet = iterator2.next();
                Intrinsics.checkExpressionValueIsNotNull(c01PacketChatMessage, "packet");
                PacketUtils.sendPacketNoEvent((Packet)c01PacketChatMessage);
            }
            new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.VICTORY, -8.0f);
            LiquidBounce.INSTANCE.getHud().addNotification(new Notification("Successfully logined", Notification.Type.INFO));
            this.loginPackets.clear();
            this.logTimer.reset();
        }
    }

    @EventTarget
    @NativeMethod.Obfuscation(flags="+native,+tiger-black")
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (AutoLogin.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof S45PacketTitle) {
            Packet<?> packet = event.getPacket();
            if (packet == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.S45PacketTitle");
            }
            S45PacketTitle s45 = (S45PacketTitle)packet;
            IChatComponent iChatComponent = s45.func_179805_b();
            if (iChatComponent == null) {
                return;
            }
            IChatComponent messageOrigin = iChatComponent;
            String string = messageOrigin.func_150260_c();
            Intrinsics.checkExpressionValueIsNotNull(string, "messageOrigin.getUnformattedText()");
            String message = string;
            if (StringsKt.contains((CharSequence)message, (CharSequence)this.loginRegex.get(), true)) {
                this.sendLogin(StringsKt.replace((String)this.loginCmd.get(), "%p", (String)this.password.get(), true));
            }
            if (StringsKt.contains((CharSequence)message, (CharSequence)this.regRegex.get(), true)) {
                this.sendRegister(StringsKt.replace((String)this.regCmd.get(), "%p", (String)this.password.get(), true));
            }
        }
        if (event.getPacket() instanceof S02PacketChat) {
            Packet<?> packet = event.getPacket();
            if (packet == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.play.server.S02PacketChat");
            }
            S02PacketChat s02 = (S02PacketChat)packet;
            String string = s02.func_148915_c().func_150260_c();
            Intrinsics.checkExpressionValueIsNotNull(string, "s02.getChatComponent().getUnformattedText()");
            String message = string;
            if (StringsKt.contains((CharSequence)message, (CharSequence)this.loginRegex.get(), true)) {
                this.sendLogin(StringsKt.replace((String)this.loginCmd.get(), "%p", (String)this.password.get(), true));
            }
            if (StringsKt.contains((CharSequence)message, (CharSequence)this.regRegex.get(), true)) {
                this.sendRegister(StringsKt.replace((String)this.regCmd.get(), "%p", (String)this.password.get(), true));
            }
        }
    }

    private final boolean sendLogin(String str) {
        return this.loginPackets.add(new C01PacketChatMessage(str));
    }

    private final boolean sendRegister(String str) {
        return this.registerPackets.add(new C01PacketChatMessage(str));
    }

    private final void resetEverything() {
        this.registerPackets.clear();
        this.loginPackets.clear();
        this.regTimer.reset();
        this.logTimer.reset();
    }

    public AutoLogin() {
        AutoLogin autoLogin = this;
        boolean bl = false;
        ArrayList arrayList = new ArrayList();
        autoLogin.loginPackets = arrayList;
        autoLogin = this;
        bl = false;
        arrayList = new ArrayList();
        autoLogin.registerPackets = arrayList;
        this.regTimer = new MSTimer();
        this.logTimer = new MSTimer();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

