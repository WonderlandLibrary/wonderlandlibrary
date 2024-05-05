package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;

public class S45PacketTitle
  implements Packet<INetHandler>
{
  private Type field_179812_a;
  private IChatComponent field_179810_b;
  private int field_179811_c;
  private int field_179808_d;
  private int field_179809_e;
  private static final String __OBFID = "CL_00002287";
  
  public S45PacketTitle() {}
  
  public S45PacketTitle(Type p_i45953_1_, IChatComponent p_i45953_2_)
  {
    this(p_i45953_1_, p_i45953_2_, -1, -1, -1);
  }
  
  public S45PacketTitle(int p_i45954_1_, int p_i45954_2_, int p_i45954_3_)
  {
    this(Type.TIMES, (IChatComponent)null, p_i45954_1_, p_i45954_2_, p_i45954_3_);
  }
  
  public S45PacketTitle(Type p_i45955_1_, IChatComponent p_i45955_2_, int p_i45955_3_, int p_i45955_4_, int p_i45955_5_)
  {
    this.field_179812_a = p_i45955_1_;
    this.field_179810_b = p_i45955_2_;
    this.field_179811_c = p_i45955_3_;
    this.field_179808_d = p_i45955_4_;
    this.field_179809_e = p_i45955_5_;
  }
  
  public void readPacketData(PacketBuffer data)
    throws IOException
  {
    this.field_179812_a = ((Type)data.readEnumValue(Type.class));
    if ((this.field_179812_a == Type.TITLE) || (this.field_179812_a == Type.SUBTITLE)) {
      this.field_179810_b = data.readChatComponent();
    }
    if (this.field_179812_a == Type.TIMES)
    {
      this.field_179811_c = data.readInt();
      this.field_179808_d = data.readInt();
      this.field_179809_e = data.readInt();
    }
  }
  
  public void writePacketData(PacketBuffer data)
    throws IOException
  {
    data.writeEnumValue(this.field_179812_a);
    if ((this.field_179812_a == Type.TITLE) || (this.field_179812_a == Type.SUBTITLE)) {
      data.writeChatComponent(this.field_179810_b);
    }
    if (this.field_179812_a == Type.TIMES)
    {
      data.writeInt(this.field_179811_c);
      data.writeInt(this.field_179808_d);
      data.writeInt(this.field_179809_e);
    }
  }
  
  public void func_179802_a(INetHandlerPlayClient p_179802_1_)
  {
    p_179802_1_.func_175099_a(this);
  }
  
  public Type func_179807_a()
  {
    return this.field_179812_a;
  }
  
  public IChatComponent func_179805_b()
  {
    return this.field_179810_b;
  }
  
  public int func_179806_c()
  {
    return this.field_179811_c;
  }
  
  public int func_179804_d()
  {
    return this.field_179808_d;
  }
  
  public int func_179803_e()
  {
    return this.field_179809_e;
  }
  
  public void processPacket(INetHandler handler)
  {
    func_179802_a((INetHandlerPlayClient)handler);
  }
  
  public static enum Type
  {
    private static final Type[] $VALUES = { TITLE, SUBTITLE, TIMES, CLEAR, RESET };
    private static final String __OBFID = "CL_00002286";
    
    public static Type func_179969_a(String p_179969_0_)
    {
      Type[] var1 = values();
      int var2 = var1.length;
      for (int var3 = 0; var3 < var2; var3++)
      {
        Type var4 = var1[var3];
        if (var4.name().equalsIgnoreCase(p_179969_0_)) {
          return var4;
        }
      }
      return TITLE;
    }
    
    public static String[] func_179971_a()
    {
      String[] var0 = new String[values().length];
      int var1 = 0;
      Type[] var2 = values();
      int var3 = var2.length;
      for (int var4 = 0; var4 < var3; var4++)
      {
        Type var5 = var2[var4];
        var0[(var1++)] = var5.name().toLowerCase();
      }
      return var0;
    }
  }
}
