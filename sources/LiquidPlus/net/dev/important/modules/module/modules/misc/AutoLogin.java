/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C01PacketChatMessage
 *  net.minecraft.network.play.server.S02PacketChat
 *  net.minecraft.network.play.server.S45PacketTitle
 *  net.minecraft.util.IChatComponent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.misc;

import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.PacketUtils;
import net.dev.important.utils.timer.MSTimer;
import net.dev.important.value.IntegerValue;
import net.dev.important.value.TextValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="AutoLogin", spacedName="Auto Login", description="Automatically login into some servers for you.", category=Category.MISC, cnName="\u81ea\u52a8\u767b\u5f55")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\u0010\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u001bH\u0007J\u0010\u0010\u001c\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u001dH\u0007J\u0010\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u001fH\u0007J\b\u0010 \u001a\u00020\u0018H\u0002J\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0014H\u0002J\u0010\u0010$\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0013\u001a\u0004\u0018\u00010\u00148VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006%"}, d2={"Lnet/dev/important/modules/module/modules/misc/AutoLogin;", "Lnet/dev/important/modules/module/Module;", "()V", "delayValue", "Lnet/dev/important/value/IntegerValue;", "logTimer", "Lnet/dev/important/utils/timer/MSTimer;", "loginCmd", "Lnet/dev/important/value/TextValue;", "loginPackets", "Ljava/util/ArrayList;", "Lnet/minecraft/network/play/client/C01PacketChatMessage;", "Lkotlin/collections/ArrayList;", "loginRegex", "password", "regCmd", "regRegex", "regTimer", "registerPackets", "tag", "", "getTag", "()Ljava/lang/String;", "onEnable", "", "onPacket", "event", "Lnet/dev/important/event/PacketEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "onWorld", "Lnet/dev/important/event/WorldEvent;", "resetEverything", "sendLogin", "", "str", "sendRegister", "LiquidBounce"})
public final class AutoLogin
extends Module {
    @NotNull
    private final TextValue password = new TextValue("Password", "example@01");
    @NotNull
    private final TextValue regRegex = new TextValue("Register-Regex", "/register");
    @NotNull
    private final TextValue loginRegex = new TextValue("Login-Regex", "/login");
    @NotNull
    private final TextValue regCmd = new TextValue("Register-Cmd", "/register %p %p");
    @NotNull
    private final TextValue loginCmd = new TextValue("Login-Cmd", "/login %p");
    @NotNull
    private final IntegerValue delayValue = new IntegerValue("Delay", 5000, 0, 5000, "ms");
    @NotNull
    private final ArrayList<C01PacketChatMessage> loginPackets = new ArrayList();
    @NotNull
    private final ArrayList<C01PacketChatMessage> registerPackets = new ArrayList();
    @NotNull
    private final MSTimer regTimer = new MSTimer();
    @NotNull
    private final MSTimer logTimer = new MSTimer();

    @Override
    public void onEnable() {
        this.resetEverything();
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.resetEverything();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.registerPackets.isEmpty()) {
            this.regTimer.reset();
        } else if (this.regTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            for (C01PacketChatMessage packet : this.registerPackets) {
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)packet));
            }
            Client.INSTANCE.getHud().addNotification(new Notification("Successfully registered.", Notification.Type.SUCCESS));
            this.registerPackets.clear();
            this.regTimer.reset();
        }
        if (this.loginPackets.isEmpty()) {
            this.logTimer.reset();
        } else if (this.logTimer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            for (C01PacketChatMessage packet : this.loginPackets) {
                PacketUtils.sendPacketNoEvent((Packet<INetHandlerPlayServer>)((Packet)packet));
            }
            Client.INSTANCE.getHud().addNotification(new Notification("Successfully logined.", Notification.Type.SUCCESS));
            this.loginPackets.clear();
            this.logTimer.reset();
        }
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        Packet<?> packet = event.getPacket();
        if (packet instanceof S45PacketTitle) {
            IChatComponent iChatComponent = ((S45PacketTitle)packet).func_179805_b();
            if (iChatComponent == null) {
                return;
            }
            IChatComponent messageOrigin = iChatComponent;
            String string = messageOrigin.func_150260_c();
            Intrinsics.checkNotNullExpressionValue(string, "messageOrigin.getUnformattedText()");
            String message = string;
            if (StringsKt.contains((CharSequence)message, (CharSequence)this.loginRegex.get(), true)) {
                this.sendLogin(StringsKt.replace((String)this.loginCmd.get(), "%p", (String)this.password.get(), true));
            }
            if (StringsKt.contains((CharSequence)message, (CharSequence)this.regRegex.get(), true)) {
                this.sendRegister(StringsKt.replace((String)this.regCmd.get(), "%p", (String)this.password.get(), true));
            }
        }
        if (packet instanceof S02PacketChat) {
            String string = ((S02PacketChat)packet).func_148915_c().func_150260_c();
            Intrinsics.checkNotNullExpressionValue(string, "packet.getChatComponent().getUnformattedText()");
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

    @Override
    @Nullable
    public String getTag() {
        return (String)this.password.get();
    }
}

