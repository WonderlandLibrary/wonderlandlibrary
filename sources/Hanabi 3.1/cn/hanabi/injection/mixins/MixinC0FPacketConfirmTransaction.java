package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IC0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({C0FPacketConfirmTransaction.class})
public class MixinC0FPacketConfirmTransaction implements IC0FPacketConfirmTransaction {
   @Shadow
   private int windowId;
   @Shadow
   private short uid;
   @Shadow
   private boolean accepted;

   public void setwindowId(int b) {
      this.windowId = b;
   }

   public int getwindowID() {
      return this.windowId;
   }

   public short getUid() {
      return this.uid;
   }

   public void setUid(short b) {
      this.uid = b;
   }

   public boolean getAccepted() {
      return this.accepted;
   }

   public void setAccepted(boolean b) {
      this.accepted = b;
   }
}
