package net.ccbluex.liquidbounce.chat;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\n\n\u0000\bf\u000020J\b0H&J\b0H&J\b0H&J020\bH&J\t02\n0H&J\b\f0H&J\r020H&¨"}, d2={"Lnet/ccbluex/liquidbounce/chat/ClientListener;", "", "onConnect", "", "onConnected", "onDisconnect", "onError", "cause", "", "onHandshake", "success", "", "onLogon", "onPacket", "packet", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "Pride"})
public interface ClientListener {
    public void onConnect();

    public void onConnected();

    public void onHandshake(boolean var1);

    public void onDisconnect();

    public void onLogon();

    public void onPacket(@NotNull Packet var1);

    public void onError(@NotNull Throwable var1);
}
