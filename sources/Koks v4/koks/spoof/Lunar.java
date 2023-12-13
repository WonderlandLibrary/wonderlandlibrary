package koks.spoof;

import god.buddy.aot.BCompiler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import koks.api.registry.spoof.Spoof;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */
public class Lunar implements Spoof {

    @Override
    public Type type() {
        return Type.LUNAR;
    }

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void handleSpoof() {
        final ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes("Lunar-Client".getBytes());
        sendPacket(new C17PacketCustomPayload("REGISTER", new PacketBuffer(byteBuf)));
    }
}
