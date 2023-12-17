// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketParticles implements Packet<INetHandlerPlayClient>
{
    private EnumParticleTypes particleType;
    private float xCoord;
    private float yCoord;
    private float zCoord;
    private float xOffset;
    private float yOffset;
    private float zOffset;
    private float particleSpeed;
    private int particleCount;
    private boolean longDistance;
    private int[] particleArguments;
    
    public SPacketParticles() {
    }
    
    public SPacketParticles(final EnumParticleTypes particleIn, final boolean longDistanceIn, final float xIn, final float yIn, final float zIn, final float xOffsetIn, final float yOffsetIn, final float zOffsetIn, final float speedIn, final int countIn, final int... argumentsIn) {
        this.particleType = particleIn;
        this.longDistance = longDistanceIn;
        this.xCoord = xIn;
        this.yCoord = yIn;
        this.zCoord = zIn;
        this.xOffset = xOffsetIn;
        this.yOffset = yOffsetIn;
        this.zOffset = zOffsetIn;
        this.particleSpeed = speedIn;
        this.particleCount = countIn;
        this.particleArguments = argumentsIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.particleType = EnumParticleTypes.getParticleFromId(buf.readInt());
        if (this.particleType == null) {
            this.particleType = EnumParticleTypes.BARRIER;
        }
        this.longDistance = buf.readBoolean();
        this.xCoord = buf.readFloat();
        this.yCoord = buf.readFloat();
        this.zCoord = buf.readFloat();
        this.xOffset = buf.readFloat();
        this.yOffset = buf.readFloat();
        this.zOffset = buf.readFloat();
        this.particleSpeed = buf.readFloat();
        this.particleCount = buf.readInt();
        final int i = this.particleType.getArgumentCount();
        this.particleArguments = new int[i];
        for (int j = 0; j < i; ++j) {
            this.particleArguments[j] = buf.readVarInt();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.particleType.getParticleID());
        buf.writeBoolean(this.longDistance);
        buf.writeFloat(this.xCoord);
        buf.writeFloat(this.yCoord);
        buf.writeFloat(this.zCoord);
        buf.writeFloat(this.xOffset);
        buf.writeFloat(this.yOffset);
        buf.writeFloat(this.zOffset);
        buf.writeFloat(this.particleSpeed);
        buf.writeInt(this.particleCount);
        for (int i = this.particleType.getArgumentCount(), j = 0; j < i; ++j) {
            buf.writeVarInt(this.particleArguments[j]);
        }
    }
    
    public EnumParticleTypes getParticleType() {
        return this.particleType;
    }
    
    public boolean isLongDistance() {
        return this.longDistance;
    }
    
    public double getXCoordinate() {
        return this.xCoord;
    }
    
    public double getYCoordinate() {
        return this.yCoord;
    }
    
    public double getZCoordinate() {
        return this.zCoord;
    }
    
    public float getXOffset() {
        return this.xOffset;
    }
    
    public float getYOffset() {
        return this.yOffset;
    }
    
    public float getZOffset() {
        return this.zOffset;
    }
    
    public float getParticleSpeed() {
        return this.particleSpeed;
    }
    
    public int getParticleCount() {
        return this.particleCount;
    }
    
    public int[] getParticleArgs() {
        return this.particleArguments;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleParticles(this);
    }
}
