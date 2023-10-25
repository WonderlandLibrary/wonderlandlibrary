/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumParticleTypes;

public class S2APacketParticles
implements Packet<INetHandlerPlayClient> {
    private int[] particleArguments;
    private float yCoord;
    private float particleSpeed;
    private float xCoord;
    private float zCoord;
    private int particleCount;
    private float xOffset;
    private boolean longDistance;
    private EnumParticleTypes particleType;
    private float yOffset;
    private float zOffset;

    public float getYOffset() {
        return this.yOffset;
    }

    public double getXCoordinate() {
        return this.xCoord;
    }

    public int[] getParticleArgs() {
        return this.particleArguments;
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleParticles(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeInt(this.particleType.getParticleID());
        packetBuffer.writeBoolean(this.longDistance);
        packetBuffer.writeFloat(this.xCoord);
        packetBuffer.writeFloat(this.yCoord);
        packetBuffer.writeFloat(this.zCoord);
        packetBuffer.writeFloat(this.xOffset);
        packetBuffer.writeFloat(this.yOffset);
        packetBuffer.writeFloat(this.zOffset);
        packetBuffer.writeFloat(this.particleSpeed);
        packetBuffer.writeInt(this.particleCount);
        int n = this.particleType.getArgumentCount();
        int n2 = 0;
        while (n2 < n) {
            packetBuffer.writeVarIntToBuffer(this.particleArguments[n2]);
            ++n2;
        }
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.particleType = EnumParticleTypes.getParticleFromId(packetBuffer.readInt());
        if (this.particleType == null) {
            this.particleType = EnumParticleTypes.BARRIER;
        }
        this.longDistance = packetBuffer.readBoolean();
        this.xCoord = packetBuffer.readFloat();
        this.yCoord = packetBuffer.readFloat();
        this.zCoord = packetBuffer.readFloat();
        this.xOffset = packetBuffer.readFloat();
        this.yOffset = packetBuffer.readFloat();
        this.zOffset = packetBuffer.readFloat();
        this.particleSpeed = packetBuffer.readFloat();
        this.particleCount = packetBuffer.readInt();
        int n = this.particleType.getArgumentCount();
        this.particleArguments = new int[n];
        int n2 = 0;
        while (n2 < n) {
            this.particleArguments[n2] = packetBuffer.readVarIntFromBuffer();
            ++n2;
        }
    }

    public float getParticleSpeed() {
        return this.particleSpeed;
    }

    public S2APacketParticles(EnumParticleTypes enumParticleTypes, boolean bl, float f, float f2, float f3, float f4, float f5, float f6, float f7, int n, int ... nArray) {
        this.particleType = enumParticleTypes;
        this.longDistance = bl;
        this.xCoord = f;
        this.yCoord = f2;
        this.zCoord = f3;
        this.xOffset = f4;
        this.yOffset = f5;
        this.zOffset = f6;
        this.particleSpeed = f7;
        this.particleCount = n;
        this.particleArguments = nArray;
    }

    public EnumParticleTypes getParticleType() {
        return this.particleType;
    }

    public S2APacketParticles() {
    }

    public double getZCoordinate() {
        return this.zCoord;
    }

    public int getParticleCount() {
        return this.particleCount;
    }

    public double getYCoordinate() {
        return this.yCoord;
    }

    public float getZOffset() {
        return this.zOffset;
    }

    public boolean isLongDistance() {
        return this.longDistance;
    }

    public float getXOffset() {
        return this.xOffset;
    }
}

