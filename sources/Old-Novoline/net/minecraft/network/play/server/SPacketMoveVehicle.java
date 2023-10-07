package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketMoveVehicle implements Packet<INetHandlerPlayClient> {

	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public SPacketMoveVehicle() {
	}

	public SPacketMoveVehicle(Entity entityIn) {
		this.x = entityIn.posX;
		this.y = entityIn.posY;
		this.z = entityIn.posZ;
		this.yaw = entityIn.rotationYaw;
		this.pitch = entityIn.rotationPitch;
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
    public void processPacket(INetHandlerPlayClient handler) {
		handler.handleMoveVehicle(this);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pitch;
	}
}
