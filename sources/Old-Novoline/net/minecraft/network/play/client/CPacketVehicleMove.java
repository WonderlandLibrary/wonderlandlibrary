package net.minecraft.network.play.client;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class CPacketVehicleMove implements Packet<INetHandlerPlayServer> {

	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public CPacketVehicleMove() {
	}

	public CPacketVehicleMove(Entity entityIn) {
		this.x = entityIn.posX;
		this.y = entityIn.posY;
		this.z = entityIn.posZ;
		this.yaw = entityIn.rotationYaw;
		this.pitch = entityIn.rotationPitch;
	}

	public CPacketVehicleMove(Entity entityIn, float yaw, float pitch) {
		this.x = entityIn.posX;
		this.y = entityIn.posY;
		this.z = entityIn.posZ;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public CPacketVehicleMove(Entity entityIn, double posX, double posY, double posZ) {
		this.x = posX;
		this.y = posY;
		this.z = posZ;
		this.yaw = entityIn.rotationYaw;
		this.pitch = entityIn.rotationPitch;
	}

	public CPacketVehicleMove(double posX, double posY, double posZ, float yaw, float pitch) {
		this.x = posX;
		this.y = posY;
		this.z = posZ;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	@Override
    public void readPacketData(PacketBuffer buf) throws IOException {
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
		this.yaw = buf.readFloat();
		this.pitch = buf.readFloat();
	}

	@Override
    public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(yaw);
		buf.writeFloat(pitch);
	}

	@Override
    public void processPacket(INetHandlerPlayServer handler) {
		handler.processVehicleMove(this);
	}

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
}
