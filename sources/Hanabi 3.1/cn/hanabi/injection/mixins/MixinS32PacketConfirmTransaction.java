package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IS32PacketConfirmTransaction;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({S32PacketConfirmTransaction.class})
public class MixinS32PacketConfirmTransaction implements IS32PacketConfirmTransaction {
   @Shadow
   private int windowId;
   @Shadow
   private short actionNumber;
   @Shadow
   private boolean field_148893_c;

   public void setwindowId(int b) {
      this.windowId = b;
   }

   public int getwindowID() {
      return this.windowId;
   }

   public short getUid() {
      return this.actionNumber;
   }

   public void setUid(short b) {
      this.actionNumber = b;
   }

   public boolean getAccepted() {
      return this.field_148893_c;
   }

   public void setAccepted(boolean b) {
      this.field_148893_c = b;
   }
}
