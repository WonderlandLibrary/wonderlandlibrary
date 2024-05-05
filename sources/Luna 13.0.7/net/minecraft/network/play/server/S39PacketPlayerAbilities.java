package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S39PacketPlayerAbilities
  implements Packet<INetHandler>
{
  private boolean invulnerable;
  private boolean flying;
  private boolean allowFlying;
  private boolean creativeMode;
  private float flySpeed;
  private float walkSpeed;
  private static final String __OBFID = "CL_00001317";
  
  public S39PacketPlayerAbilities() {}
  
  public S39PacketPlayerAbilities(PlayerCapabilities capabilities)
  {
    setInvulnerable(capabilities.disableDamage);
    setFlying(capabilities.isFlying);
    setAllowFlying(capabilities.allowFlying);
    setCreativeMode(capabilities.isCreativeMode);
    setFlySpeed(capabilities.getFlySpeed());
    setWalkSpeed(capabilities.getWalkSpeed());
  }
  
  public void readPacketData(PacketBuffer data)
    throws IOException
  {
    byte var2 = data.readByte();
    setInvulnerable((var2 & 0x1) > 0);
    setFlying((var2 & 0x2) > 0);
    setAllowFlying((var2 & 0x4) > 0);
    setCreativeMode((var2 & 0x8) > 0);
    setFlySpeed(data.readFloat());
    setWalkSpeed(data.readFloat());
  }
  
  public void writePacketData(PacketBuffer data)
    throws IOException
  {
    byte var2 = 0;
    if (isInvulnerable()) {
      var2 = (byte)(var2 | 0x1);
    }
    if (isFlying()) {
      var2 = (byte)(var2 | 0x2);
    }
    if (isAllowFlying()) {
      var2 = (byte)(var2 | 0x4);
    }
    if (isCreativeMode()) {
      var2 = (byte)(var2 | 0x8);
    }
    data.writeByte(var2);
    data.writeFloat(this.flySpeed);
    data.writeFloat(this.walkSpeed);
  }
  
  public void func_180742_a(INetHandlerPlayClient p_180742_1_)
  {
    p_180742_1_.handlePlayerAbilities(this);
  }
  
  public boolean isInvulnerable()
  {
    return this.invulnerable;
  }
  
  public void setInvulnerable(boolean isInvulnerable)
  {
    this.invulnerable = isInvulnerable;
  }
  
  public boolean isFlying()
  {
    return this.flying;
  }
  
  public void setFlying(boolean isFlying)
  {
    this.flying = isFlying;
  }
  
  public boolean isAllowFlying()
  {
    return this.allowFlying;
  }
  
  public void setAllowFlying(boolean isAllowFlying)
  {
    this.allowFlying = isAllowFlying;
  }
  
  public boolean isCreativeMode()
  {
    return this.creativeMode;
  }
  
  public void setCreativeMode(boolean isCreativeMode)
  {
    this.creativeMode = isCreativeMode;
  }
  
  public float getFlySpeed()
  {
    return this.flySpeed;
  }
  
  public void setFlySpeed(float flySpeedIn)
  {
    this.flySpeed = flySpeedIn;
  }
  
  public float getWalkSpeed()
  {
    return this.walkSpeed;
  }
  
  public void setWalkSpeed(float walkSpeedIn)
  {
    this.walkSpeed = walkSpeedIn;
  }
  
  public void processPacket(INetHandler handler)
  {
    func_180742_a((INetHandlerPlayClient)handler);
  }
}
