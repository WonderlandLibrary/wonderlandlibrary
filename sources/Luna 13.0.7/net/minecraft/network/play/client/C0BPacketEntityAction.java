package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0BPacketEntityAction
  implements Packet<INetHandler>
{
  private int field_149517_a;
  private Action field_149515_b;
  private int field_149516_c;
  private static final String __OBFID = "CL_00001366";
  
  public C0BPacketEntityAction() {}
  
  public C0BPacketEntityAction(Entity p_i45937_1_, Action p_i45937_2_)
  {
    this(p_i45937_1_, p_i45937_2_, 0);
  }
  
  public C0BPacketEntityAction(Entity p_i45938_1_, Action p_i45938_2_, int p_i45938_3_)
  {
    this.field_149517_a = p_i45938_1_.getEntityId();
    this.field_149515_b = p_i45938_2_;
    this.field_149516_c = p_i45938_3_;
  }
  
  public void readPacketData(PacketBuffer data)
    throws IOException
  {
    this.field_149517_a = data.readVarIntFromBuffer();
    this.field_149515_b = ((Action)data.readEnumValue(Action.class));
    this.field_149516_c = data.readVarIntFromBuffer();
  }
  
  public void writePacketData(PacketBuffer data)
    throws IOException
  {
    data.writeVarIntToBuffer(this.field_149517_a);
    data.writeEnumValue(this.field_149515_b);
    data.writeVarIntToBuffer(this.field_149516_c);
  }
  
  public void func_180765_a(INetHandlerPlayServer p_180765_1_)
  {
    p_180765_1_.processEntityAction(this);
  }
  
  public Action func_180764_b()
  {
    return this.field_149515_b;
  }
  
  public int func_149512_e()
  {
    return this.field_149516_c;
  }
  
  public void processPacket(INetHandler handler)
  {
    func_180765_a((INetHandlerPlayServer)handler);
  }
  
  public static enum Action
  {
    private static final Action[] $VALUES = { START_SNEAKING, STOP_SNEAKING, STOP_SLEEPING, START_SPRINTING, STOP_SPRINTING, RIDING_JUMP, OPEN_INVENTORY };
    private static final String __OBFID = "CL_00002283";
  }
}
