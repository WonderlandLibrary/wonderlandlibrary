package net.minecraft.network;

import net.minecraft.util.IThreadListener;

public class PacketThreadUtil
{
  private static final String __OBFID = "CL_00002306";
  
  public PacketThreadUtil() {}
  
  public static void func_180031_a(Packet<INetHandler> p_180031_0_, final INetHandler p_180031_1_, IThreadListener p_180031_2_)
  {
    if (!p_180031_2_.isCallingFromMinecraftThread())
    {
      p_180031_2_.addScheduledTask(new Runnable()
      {
        private static final String __OBFID = "CL_00002305";
        
        public void run()
        {
          this.val$p_180031_0_.processPacket(p_180031_1_);
        }
      });
      throw ThreadQuickExitException.field_179886_a;
    }
  }
}
