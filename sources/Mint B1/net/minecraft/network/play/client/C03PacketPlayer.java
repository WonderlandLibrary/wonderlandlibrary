package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer implements Packet
{
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean moving;
    public boolean onGround;
    public boolean field_149480_h;
    public boolean rotating;
    private static final String __OBFID = "CL_00001360";

    public C03PacketPlayer() {}

    public C03PacketPlayer(boolean isOnGround)
    {
        this.onGround = isOnGround;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayer(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.onGround = data.readUnsignedByte() != 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.onGround ? 1 : 0);
    }

    public double getPositionX()
    {
        return this.x;
    }

    public double getPositionY()
    {
        return this.y;
    }

    public double getPositionZ()
    {
        return this.z;
    }

    public float getYaw()
    {
        return this.yaw;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public boolean isOnGround()
    {
        return this.onGround;
    }

    public boolean func_149466_j()
    {
        return this.field_149480_h;
    }

    public boolean getRotating()
    {
        return this.rotating;
    }

    public void func_149469_a(boolean p_149469_1_)
    {
        this.field_149480_h = p_149469_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }

    public static class C04PacketPlayerPosition extends C03PacketPlayer
    {
        private static final String __OBFID = "CL_00001361";

        public C04PacketPlayerPosition()
        {
            this.field_149480_h = true;
        }

        public C04PacketPlayerPosition(double p_i45942_1_, double p_i45942_3_, double p_i45942_5_, boolean isOnGround)
        {
            this.x = p_i45942_1_;
            this.y = p_i45942_3_;
            this.z = p_i45942_5_;
            this.onGround = isOnGround;
            this.field_149480_h = true;
        }

        public C04PacketPlayerPosition(double p_i45942_1_, double p_i45942_3_, double p_i45942_5_)
        {
            this.x = p_i45942_1_;
            this.y = p_i45942_3_;
            this.z = p_i45942_5_;
            this.field_149480_h = true;
        }

        public void readPacketData(PacketBuffer data) throws IOException
        {
            this.x = data.readDouble();
            this.y = data.readDouble();
            this.z = data.readDouble();
            super.readPacketData(data);
        }

        public void writePacketData(PacketBuffer data) throws IOException
        {
            data.writeDouble(this.x);
            data.writeDouble(this.y);
            data.writeDouble(this.z);
            super.writePacketData(data);
        }

        public void processPacket(INetHandler handler)
        {
            super.processPacket((INetHandlerPlayServer)handler);
        }
    }

    public static class C05PacketPlayerLook extends C03PacketPlayer
    {
        private static final String __OBFID = "CL_00001363";

        public C05PacketPlayerLook()
        {
            this.rotating = true;
        }

        public C05PacketPlayerLook(float p_i45255_1_, float p_i45255_2_, boolean isOnGround)
        {
            this.yaw = p_i45255_1_;
            this.pitch = p_i45255_2_;
            this.onGround = isOnGround;
            this.rotating = true;
        }

        public void readPacketData(PacketBuffer data) throws IOException
        {
            this.yaw = data.readFloat();
            this.pitch = data.readFloat();
            super.readPacketData(data);
        }

        public void writePacketData(PacketBuffer data) throws IOException
        {
            data.writeFloat(this.yaw);
            data.writeFloat(this.pitch);
            super.writePacketData(data);
        }

        public void processPacket(INetHandler handler)
        {
            super.processPacket((INetHandlerPlayServer)handler);
        }
    }

    public static class C06PacketPlayerPosLook extends C03PacketPlayer
    {
        private static final String __OBFID = "CL_00001362";

        public C06PacketPlayerPosLook()
        {
            this.field_149480_h = true;
            this.rotating = true;
        }

        public C06PacketPlayerPosLook(double p_i45941_1_, double p_i45941_3_, double p_i45941_5_, float p_i45941_7_, float p_i45941_8_, boolean isOnGround)
        {
            this.x = p_i45941_1_;
            this.y = p_i45941_3_;
            this.z = p_i45941_5_;
            this.yaw = p_i45941_7_;
            this.pitch = p_i45941_8_;
            this.onGround = isOnGround;
            this.rotating = true;
            this.field_149480_h = true;
        }

        public void readPacketData(PacketBuffer data) throws IOException
        {
            this.x = data.readDouble();
            this.y = data.readDouble();
            this.z = data.readDouble();
            this.yaw = data.readFloat();
            this.pitch = data.readFloat();
            super.readPacketData(data);
        }

        public void writePacketData(PacketBuffer data) throws IOException
        {
            data.writeDouble(this.x);
            data.writeDouble(this.y);
            data.writeDouble(this.z);
            data.writeFloat(this.yaw);
            data.writeFloat(this.pitch);
            super.writePacketData(data);
        }

        public void processPacket(INetHandler handler)
        {
            super.processPacket((INetHandlerPlayServer)handler);
        }
    }
    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isRotating() {
        return rotating;
    }

    public void setRotating(final boolean rotating) {
        this.rotating = rotating;
    }

	public boolean isMoving() {
        return Minecraft.getMinecraft().thePlayer != null && (Minecraft.getMinecraft().thePlayer.movementInput.moveForward != 0F || Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe != 0F);
    }
}