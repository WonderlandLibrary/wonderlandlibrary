package koks.spoof;

import god.buddy.aot.BCompiler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import koks.api.registry.spoof.Spoof;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Badlion implements Spoof {

    @Override
    public Type type() {
        return Type.BADLION;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void handleSpoof() {

        /*Not Working atm ._.*/
        sendPacket(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));

        /*final ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes("heartbeat".getBytes());
        byteBuf.writeBytes("true".getBytes());
        sendPacket(new C17PacketCustomPayload("BungeeCord", new PacketBuffer(byteBuf)));*/

    }

    @Override
    public void onPacket(Packet<?> packet) {
        try {
            if (packet instanceof S3FPacketCustomPayload) {
                final S3FPacketCustomPayload s3FPacketCustomPayload = (S3FPacketCustomPayload) packet;
                if (s3FPacketCustomPayload.getChannelName().equals("BungeeCord")) {
                    final ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                    final DataOutputStream dataOutputStream2 = new DataOutputStream(byteArrayOutputStream2);
                    dataOutputStream2.writeUTF("heartbeat");
                    dataOutputStream2.writeUTF("true");
                    sendPacket(new C17PacketCustomPayload("BungeeCord", new PacketBuffer((Unpooled.buffer()).writeBytes(byteArrayOutputStream2.toByteArray()))));
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
