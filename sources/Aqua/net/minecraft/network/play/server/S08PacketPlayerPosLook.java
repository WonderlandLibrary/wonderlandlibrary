package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Set;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

/*
 * Exception performing whole class analysis ignored.
 */
public class S08PacketPlayerPosLook
implements Packet<INetHandlerPlayClient> {
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    private Set<EnumFlags> field_179835_f;

    public S08PacketPlayerPosLook() {
    }

    public S08PacketPlayerPosLook(double xIn, double yIn, double zIn, float yawIn, float pitchIn, Set<EnumFlags> p_i45993_9_) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
        this.yaw = yawIn;
        this.pitch = pitchIn;
        this.field_179835_f = p_i45993_9_;
    }

    public void readPacketData(PacketBuffer buf) throws IOException {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.field_179835_f = EnumFlags.func_180053_a((int)buf.readUnsignedByte());
    }

    public void writePacketData(PacketBuffer buf) throws IOException {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeByte(EnumFlags.func_180056_a(this.field_179835_f));
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handlePlayerPosLook(this);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public Set<EnumFlags> func_179834_f() {
        return this.field_179835_f;
    }
}
