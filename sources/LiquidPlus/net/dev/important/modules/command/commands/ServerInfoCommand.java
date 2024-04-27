/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ServerData
 *  net.minecraft.network.Packet
 *  net.minecraft.network.handshake.client.C00Handshake
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Listenable;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.command.Command;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\u001b\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000bH\u0016\u00a2\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/dev/important/modules/command/commands/ServerInfoCommand;", "Lnet/dev/important/modules/command/Command;", "Lnet/dev/important/event/Listenable;", "()V", "ip", "", "port", "", "execute", "", "args", "", "([Ljava/lang/String;)V", "handleEvents", "", "onPacket", "event", "Lnet/dev/important/event/PacketEvent;", "LiquidBounce"})
public final class ServerInfoCommand
extends Command
implements Listenable {
    @NotNull
    private String ip;
    private int port;

    public ServerInfoCommand() {
        boolean $i$f$emptyArray = false;
        super("serverinfo", new String[0]);
        Client.INSTANCE.getEventManager().registerListener(this);
        this.ip = "";
    }

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        if (MinecraftInstance.mc.func_147104_D() == null) {
            this.chat("This command does not work in single player.");
            return;
        }
        ServerData data = MinecraftInstance.mc.func_147104_D();
        this.chat("Server infos:");
        this.chat(Intrinsics.stringPlus("\u00a77Name: \u00a78", data.field_78847_a));
        this.chat("\u00a77IP: \u00a78" + this.ip + ':' + this.port);
        this.chat(Intrinsics.stringPlus("\u00a77Players: \u00a78", data.field_78846_c));
        this.chat(Intrinsics.stringPlus("\u00a77MOTD: \u00a78", data.field_78843_d));
        this.chat(Intrinsics.stringPlus("\u00a77ServerVersion: \u00a78", data.field_82822_g));
        this.chat(Intrinsics.stringPlus("\u00a77ProtocolVersion: \u00a78", data.field_82821_f));
        this.chat(Intrinsics.stringPlus("\u00a77Ping: \u00a78", data.field_78844_e));
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof C00Handshake) {
            String string = ((C00Handshake)packet).field_149598_b;
            Intrinsics.checkNotNullExpressionValue(string, "packet.ip");
            this.ip = string;
            this.port = ((C00Handshake)packet).field_149599_c;
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }
}

