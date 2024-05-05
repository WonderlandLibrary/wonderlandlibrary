package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C03PacketPlayer
  implements Packet<INetHandler>
{
  public static double x;
  public static double y;
  public static double z;
  public static float yaw;
  public static float pitch;
  boolean field_149474_g;
  boolean field_149480_h;
  boolean rotating;
  protected boolean moving;
  private static final String __OBFID = "CL_00001360";
  
  public C03PacketPlayer() {}
  
  public C03PacketPlayer(boolean p_i45256_1_)
  {
    this.field_149474_g = p_i45256_1_;
  }
  
  public void processPacket(INetHandlerPlayServer handler)
  {
    handler.processPlayer(this);
  }
  
  public void readPacketData(PacketBuffer data)
    throws IOException
  {
    this.field_149474_g = (data.readUnsignedByte() != 0);
  }
  
  public double getX(double defaultValue)
  {
    return this.moving ? x : defaultValue;
  }
  
  public double getY(double defaultValue)
  {
    return this.moving ? y : defaultValue;
  }
  
  public double getZ(double defaultValue)
  {
    return this.moving ? z : defaultValue;
  }
  
  public void setField_149474_g(boolean field_149474_g)
  {
    this.field_149474_g = field_149474_g;
  }
  
  public void writePacketData(PacketBuffer data)
    throws IOException
  {
    data.writeByte(this.field_149474_g ? 1 : 0);
  }
  
  public double getPositionX()
  {
    return x;
  }
  
  public double getPositionY()
  {
    return y;
  }
  
  public double getPositionZ()
  {
    return z;
  }
  
  public float getYaw()
  {
    return yaw;
  }
  
  public float getPitch()
  {
    return pitch;
  }
  
  public boolean func_149465_i()
  {
    return this.field_149474_g;
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
  
  public void processPacket(INetHandler handler)
  {
    processPacket((INetHandlerPlayServer)handler);
  }
  
  public static class C04PacketPlayerPosition
    extends C03PacketPlayer
  {
    private static final String __OBFID = "CL_00001361";
    
    public C04PacketPlayerPosition()
    {
      this.field_149480_h = true;
    }
    
    public C04PacketPlayerPosition(double p_i45942_1_, double p_i45942_3_, double p_i45942_5_, boolean p_i45942_7_)
    {
      x = p_i45942_1_;
      y = p_i45942_3_;
      z = p_i45942_5_;
      this.field_149474_g = p_i45942_7_;
      this.field_149480_h = true;
    }
    
    public void readPacketData(PacketBuffer data)
      throws IOException
    {
      x = data.readDouble();
      y = data.readDouble();
      z = data.readDouble();
      super.readPacketData(data);
    }
    
    public void writePacketData(PacketBuffer data)
      throws IOException
    {
      data.writeDouble(x);
      data.writeDouble(y);
      data.writeDouble(z);
      super.writePacketData(data);
    }
    
    public void processPacket(INetHandler handler)
    {
      super.processPacket((INetHandlerPlayServer)handler);
    }
  }
  
  public static class C05PacketPlayerLook
    extends C03PacketPlayer
  {
    private static final String __OBFID = "CL_00001363";
    
    public C05PacketPlayerLook()
    {
      this.rotating = true;
    }
    
    public C05PacketPlayerLook(float p_i45255_1_, float p_i45255_2_, boolean p_i45255_3_)
    {
      yaw = p_i45255_1_;
      pitch = p_i45255_2_;
      this.field_149474_g = p_i45255_3_;
      this.rotating = true;
    }
    
    public void readPacketData(PacketBuffer data)
      throws IOException
    {
      yaw = data.readFloat();
      pitch = data.readFloat();
      super.readPacketData(data);
    }
    
    public void writePacketData(PacketBuffer data)
      throws IOException
    {
      data.writeFloat(yaw);
      data.writeFloat(pitch);
      super.writePacketData(data);
    }
    
    public void processPacket(INetHandler handler)
    {
      super.processPacket((INetHandlerPlayServer)handler);
    }
  }
  
  public static class C06PacketPlayerPosLook
    extends C03PacketPlayer
  {
    private static final String __OBFID = "CL_00001362";
    
    public C06PacketPlayerPosLook()
    {
      this.field_149480_h = true;
      this.rotating = true;
    }
    
    public C06PacketPlayerPosLook(double p_i45941_1_, double p_i45941_3_, double p_i45941_5_, float p_i45941_7_, float p_i45941_8_, boolean p_i45941_9_)
    {
      x = p_i45941_1_;
      y = p_i45941_3_;
      z = p_i45941_5_;
      yaw = p_i45941_7_;
      pitch = p_i45941_8_;
      this.field_149474_g = p_i45941_9_;
      this.rotating = true;
      this.field_149480_h = true;
    }
    
    public void readPacketData(PacketBuffer data)
      throws IOException
    {
      x = data.readDouble();
      y = data.readDouble();
      z = data.readDouble();
      yaw = data.readFloat();
      pitch = data.readFloat();
      super.readPacketData(data);
    }
    
    public void writePacketData(PacketBuffer data)
      throws IOException
    {
      data.writeDouble(x);
      data.writeDouble(y);
      data.writeDouble(z);
      data.writeFloat(yaw);
      data.writeFloat(pitch);
      super.writePacketData(data);
    }
    
    public void processPacket(INetHandler handler)
    {
      super.processPacket((INetHandlerPlayServer)handler);
    }
  }
}
