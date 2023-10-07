// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import net.minecraft.network.play.INetHandlerPlayServer;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;

public class C13PacketPlayerAbilities implements Packet
{
    private boolean invulnerable;
    private boolean flying;
    private boolean allowFlying;
    private boolean creativeMode;
    private float flySpeed;
    private float walkSpeed;
    private static final String __OBFID = "CL_00001364";
    
    public C13PacketPlayerAbilities() {
    }
    
    public C13PacketPlayerAbilities(final PlayerCapabilities capabilities) {
        this.setInvulnerable(capabilities.disableDamage);
        this.setFlying(capabilities.isFlying);
        this.setAllowFlying(capabilities.allowFlying);
        this.setCreativeMode(capabilities.isCreativeMode);
        this.setFlySpeed(capabilities.getFlySpeed());
        this.setWalkSpeed(capabilities.getWalkSpeed());
    }
    
    @Override
    public void readPacketData(final PacketBuffer data) throws IOException {
        final byte var2 = data.readByte();
        this.setInvulnerable((var2 & 0x1) > 0);
        this.setFlying((var2 & 0x2) > 0);
        this.setAllowFlying((var2 & 0x4) > 0);
        this.setCreativeMode((var2 & 0x8) > 0);
        this.setFlySpeed(data.readFloat());
        this.setWalkSpeed(data.readFloat());
    }
    
    @Override
    public void writePacketData(final PacketBuffer data) throws IOException {
        byte var2 = 0;
        if (this.isInvulnerable()) {
            var2 |= 0x1;
        }
        if (this.isFlying()) {
            var2 |= 0x2;
        }
        if (this.isAllowFlying()) {
            var2 |= 0x4;
        }
        if (this.isCreativeMode()) {
            var2 |= 0x8;
        }
        data.writeByte(var2);
        data.writeFloat(this.flySpeed);
        data.writeFloat(this.walkSpeed);
    }
    
    public void func_180761_a(final INetHandlerPlayServer p_180761_1_) {
        p_180761_1_.processPlayerAbilities(this);
    }
    
    public boolean isInvulnerable() {
        return this.invulnerable;
    }
    
    public void setInvulnerable(final boolean isInvulnerable) {
        this.invulnerable = isInvulnerable;
    }
    
    public boolean isFlying() {
        return this.flying;
    }
    
    public void setFlying(final boolean isFlying) {
        this.flying = isFlying;
    }
    
    public boolean isAllowFlying() {
        return this.allowFlying;
    }
    
    public void setAllowFlying(final boolean isAllowFlying) {
        this.allowFlying = isAllowFlying;
    }
    
    public boolean isCreativeMode() {
        return this.creativeMode;
    }
    
    public void setCreativeMode(final boolean isCreativeMode) {
        this.creativeMode = isCreativeMode;
    }
    
    public void setFlySpeed(final float flySpeedIn) {
        this.flySpeed = flySpeedIn;
    }
    
    public void setWalkSpeed(final float walkSpeedIn) {
        this.walkSpeed = walkSpeedIn;
    }
    
    @Override
    public void processPacket(final INetHandler handler) {
        this.func_180761_a((INetHandlerPlayServer)handler);
    }
}
