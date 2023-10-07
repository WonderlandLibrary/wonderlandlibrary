package net.minecraft.network.play.server;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

import java.io.IOException;

public class S39PacketPlayerAbilities implements Packet<INetHandlerPlayClient> {

    private boolean disabledDamage;
    private boolean flying;
    private boolean allowFlying;
    private boolean creative;
    private float flySpeed;
    private float walkSpeed;

    public S39PacketPlayerAbilities() {
    }

    public S39PacketPlayerAbilities(PlayerAbilities capabilities) {
        this.setDisabledDamage(capabilities.isDisabledDamage());
        this.setFlying(capabilities.isFlying());
        this.setAllowFlying(capabilities.isAllowFlying());
        this.setCreative(capabilities.isCreative());
        this.setFlySpeed(capabilities.getFlySpeed());
        this.setWalkSpeed(capabilities.getWalkSpeed());
    }

    public S39PacketPlayerAbilities(float flySpeed, float walkSpeed, boolean flying, boolean allowFlying, boolean creative, boolean invulnerable) {
        this.setFlying(flying);
        this.setAllowFlying(allowFlying);
        this.setCreative(creative);
        this.setDisabledDamage(invulnerable);
        this.setFlySpeed(flySpeed);
        this.setWalkSpeed(walkSpeed);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException {
        byte b0 = buf.readByte();
        this.setDisabledDamage((b0 & 1) > 0);
        this.setFlying((b0 & 2) > 0);
        this.setAllowFlying((b0 & 4) > 0);
        this.setCreative((b0 & 8) > 0);
        this.setFlySpeed(buf.readFloat());
        this.setWalkSpeed(buf.readFloat());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException {
        byte b0 = 0;

        if (this.isDisabledDamage()) {
            b0 = (byte) (b0 | 1);
        }

        if (this.isFlying()) {
            b0 = (byte) (b0 | 2);
        }

        if (this.isAllowFlying()) {
            b0 = (byte) (b0 | 4);
        }

        if (this.isCreative()) {
            b0 = (byte) (b0 | 8);
        }

        buf.writeByte(b0);
        buf.writeFloat(this.flySpeed);
        buf.writeFloat(this.walkSpeed);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler) {
        handler.handlePlayerAbilities(this);
    }

    public void setDisabledDamage(boolean disabledDamage) {
        this.disabledDamage = disabledDamage;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public void setAllowFlying(boolean allowFlying) {
        this.allowFlying = allowFlying;
    }

    public void setCreative(boolean creative) {
        this.creative = creative;
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public void setWalkSpeed(float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    public boolean isDisabledDamage() {
        return disabledDamage;
    }

    public boolean isFlying() {
        return flying;
    }

    public boolean isAllowFlying() {
        return allowFlying;
    }

    public boolean isCreative() {
        return creative;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }
}
