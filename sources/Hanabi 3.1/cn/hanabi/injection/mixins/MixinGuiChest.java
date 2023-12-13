package cn.hanabi.injection.mixins;

import cn.hanabi.modules.modules.player.ChestStealer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiChest.class})
public class MixinGuiChest {
   @Shadow
   private IInventory lowerChestInventory;

   @Inject(
      method = {"drawGuiContainerForegroundLayer"},
      at = {@At("HEAD")}
   )
   private void nmsl(int mouseX, int mouseY, CallbackInfo ci) {
      ChestStealer.isChest = StatCollector.translateToLocal("container.chest").equalsIgnoreCase(this.lowerChestInventory.getDisplayName().getUnformattedText()) || StatCollector.translateToLocal("container.chestDouble").equalsIgnoreCase(this.lowerChestInventory.getDisplayName().getUnformattedText());
   }
}
