package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.handshake.client.ICPacketHandshake;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\n\n\n\b\n\n\u0000\n\b\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\u0000\u00002020B¢J\b0\t2\f\n\b00H¢\fJ\b\r0HJ0\t20HR0X¢\n\u0000R0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/ServerInfoCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "Lnet/ccbluex/liquidbounce/event/Listenable;", "()V", "ip", "", "port", "", "execute", "", "args", "", "([Ljava/lang/String;)V", "handleEvents", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "Pride"})
public final class ServerInfoCommand
extends Command
implements Listenable {
    private String ip;
    private int port;

    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (MinecraftInstance.mc.getCurrentServerData() == null) {
            this.chat("This command does not work in single player.");
            return;
        }
        IServerData iServerData = MinecraftInstance.mc.getCurrentServerData();
        if (iServerData == null) {
            return;
        }
        IServerData data = iServerData;
        this.chat("Server infos:");
        this.chat("§7Name: §8" + data.getServerName());
        this.chat("§7IP: §8" + this.ip + ':' + this.port);
        this.chat("§7Players: §8" + data.getPopulationInfo());
        this.chat("§7MOTD: §8" + data.getServerMOTD());
        this.chat("§7ServerVersion: §8" + data.getGameVersion());
        this.chat("§7ProtocolVersion: §8" + data.getVersion());
        this.chat("§7Ping: §8" + data.getPingToServer());
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketHandshake(packet)) {
            ICPacketHandshake handshake = packet.asCPacketHandshake();
            this.ip = handshake.getIp();
            this.port = handshake.getPort();
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
}
