/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001b\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bH\u0016\u00a2\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/ServerInfoCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "ip", "", "port", "", "execute", "", "args", "", "([Ljava/lang/String;)V", "handleEvents", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class ServerInfoCommand
extends Command
implements Listenable {
    private String ip;
    private int port;

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkParameterIsNotNull(args2, "args");
        Minecraft minecraft = ServerInfoCommand.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        if (minecraft.func_147104_D() == null) {
            this.chat("This command does not work in single player.");
            return;
        }
        Minecraft minecraft2 = ServerInfoCommand.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
        ServerData serverData = minecraft2.func_147104_D();
        if (serverData == null) {
            return;
        }
        ServerData data = serverData;
        this.chat("Server infos:");
        this.chat("\u00a77Name: \u00a78" + data.field_78847_a);
        this.chat("\u00a77IP: \u00a78" + this.ip + ':' + this.port);
        this.chat("\u00a77Players: \u00a78" + data.field_78846_c);
        this.chat("\u00a77MOTD: \u00a78" + data.field_78843_d);
        this.chat("\u00a77ServerVersion: \u00a78" + data.field_82822_g);
        this.chat("\u00a77ProtocolVersion: \u00a78" + data.field_82821_f);
        this.chat("\u00a77Ping: \u00a78" + data.field_78844_e);
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof C00Handshake) {
            String string = ((C00Handshake)packet).field_149598_b;
            Intrinsics.checkExpressionValueIsNotNull(string, "packet.ip");
            this.ip = string;
            this.port = ((C00Handshake)packet).field_149599_c;
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public ServerInfoCommand() {
        super("serverinfo", new String[0]);
        LiquidBounce.INSTANCE.getEventManager().registerListener(this);
        this.ip = "";
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

