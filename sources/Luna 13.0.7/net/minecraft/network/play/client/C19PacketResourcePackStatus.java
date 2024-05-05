package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C19PacketResourcePackStatus
  implements Packet<INetHandler>
{
  private String field_179720_a;
  private Action field_179719_b;
  private static final String __OBFID = "CL_00002282";
  
  public C19PacketResourcePackStatus() {}
  
  public C19PacketResourcePackStatus(String p_i45935_1_, Action p_i45935_2_)
  {
    if (p_i45935_1_.length() > 40) {
      p_i45935_1_ = p_i45935_1_.substring(0, 40);
    }
    this.field_179720_a = p_i45935_1_;
    this.field_179719_b = p_i45935_2_;
  }
  
  public void readPacketData(PacketBuffer data)
    throws IOException
  {
    this.field_179720_a = data.readStringFromBuffer(40);
    this.field_179719_b = ((Action)data.readEnumValue(Action.class));
  }
  
  public void writePacketData(PacketBuffer data)
    throws IOException
  {
    data.writeString(this.field_179720_a);
    data.writeEnumValue(this.field_179719_b);
  }
  
  public void func_179718_a(INetHandlerPlayServer p_179718_1_)
  {
    p_179718_1_.func_175086_a(this);
  }
  
  public void processPacket(INetHandler handler)
  {
    func_179718_a((INetHandlerPlayServer)handler);
  }
  
  public static enum Action
  {
    private static final Action[] $VALUES = { SUCCESSFULLY_LOADED, DECLINED, FAILED_DOWNLOAD, ACCEPTED };
    private static final String __OBFID = "CL_00002281";
  }
}
