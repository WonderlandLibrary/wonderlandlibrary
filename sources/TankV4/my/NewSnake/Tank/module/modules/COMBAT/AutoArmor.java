package my.NewSnake.Tank.module.modules.COMBAT;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.TickEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

@Module.Mod(
   displayName = "AutoArmor"
)
public class AutoArmor extends Module {
   @EventTarget
   private void onTick(TickEvent var1) {
      if (ClientUtils.player() != null && (ClientUtils.mc().currentScreen == null || ClientUtils.mc().currentScreen instanceof GuiInventory || !ClientUtils.mc().currentScreen.getClass().getName().contains("inventory"))) {
         int var2 = -1;
         double var3 = -1.0D;

         for(int var5 = 9; var5 < 45; ++var5) {
            ItemStack var6 = ClientUtils.player().inventoryContainer.getSlot(var5).getStack();
            if (var6 != null && var6 == null) {
               double var7 = this.getProtectionValue(var6);
               if (var7 >= var3) {
                  var2 = var5;
                  var3 = var7;
               }
            }
         }

         if (var2 != -1) {
            ClientUtils.playerController().windowClick(ClientUtils.player().inventoryContainer.windowId, var2, 0, 1, ClientUtils.player());
         }
      }

   }

   private double getProtectionValue(ItemStack var1) {
      return (double)((ItemArmor)var1.getItem()).damageReduceAmount + (double)((100 - ((ItemArmor)var1.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, var1) * 4) * 0.0075D;
   }
}
