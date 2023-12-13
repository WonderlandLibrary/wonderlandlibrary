package cn.hanabi.injection.mixins;

import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({C10PacketCreativeInventoryAction.class})
public class MixinC10PacketCreativeInventoryAction {
   @Shadow
   private int slotId;
   @Shadow
   private ItemStack stack;

   public void setSlotId(int id) {
      this.slotId = id;
   }

   public void setStack(ItemStack Stack) {
      this.stack = Stack;
   }
}
