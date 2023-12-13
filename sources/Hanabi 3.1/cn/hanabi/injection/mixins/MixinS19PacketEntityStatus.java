package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IS19PacketEntityStatus;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({S19PacketEntityStatus.class})
public class MixinS19PacketEntityStatus implements IS19PacketEntityStatus {
   @Shadow
   private int entityId;
   @Shadow
   private byte logicOpcode;

   public void getEntityId(int b) {
      this.entityId = b;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public void getLogicOpcode(byte b) {
      this.logicOpcode = b;
   }

   public byte getLogicOpcode() {
      return this.logicOpcode;
   }
}
