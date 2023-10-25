/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0CPacketInput
implements Packet<INetHandlerPlayServer> {
    private boolean jumping;
    private float forwardSpeed;
    private float strafeSpeed;
    private boolean sneaking;

    @Override
    public void processPacket(INetHandlerPlayServer iNetHandlerPlayServer) {
        iNetHandlerPlayServer.processInput(this);
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.strafeSpeed = packetBuffer.readFloat();
        this.forwardSpeed = packetBuffer.readFloat();
        byte by = packetBuffer.readByte();
        this.jumping = (by & 1) > 0;
        this.sneaking = (by & 2) > 0;
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeFloat(this.strafeSpeed);
        packetBuffer.writeFloat(this.forwardSpeed);
        byte by = 0;
        if (this.jumping) {
            by = (byte)(by | 1);
        }
        if (this.sneaking) {
            by = (byte)(by | 2);
        }
        packetBuffer.writeByte(by);
    }

    public C0CPacketInput(float f, float f2, boolean bl, boolean bl2) {
        this.strafeSpeed = f;
        this.forwardSpeed = f2;
        this.jumping = bl;
        this.sneaking = bl2;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public float getForwardSpeed() {
        return this.forwardSpeed;
    }

    public C0CPacketInput() {
    }

    public float getStrafeSpeed() {
        return this.strafeSpeed;
    }
}

