package net.minecraft.network;

import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {
   public static void checkThreadAndEnqueue(Packet var0, INetHandler var1, IThreadListener var2) throws ThreadQuickExitException {
      if (!var2.isCallingFromMinecraftThread()) {
         var2.addScheduledTask(new Runnable(var0, var1) {
            private final INetHandler val$p_180031_1_;
            private final Packet val$p_180031_0_;

            {
               this.val$p_180031_0_ = var1;
               this.val$p_180031_1_ = var2;
            }

            public void run() {
               this.val$p_180031_0_.processPacket(this.val$p_180031_1_);
            }
         });
         throw ThreadQuickExitException.field_179886_a;
      }
   }
}
