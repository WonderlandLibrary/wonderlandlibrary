package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.option.Option;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.Timer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

@Module.Mod(
   displayName = "Inventory Cleaner"
)
public class InventoryCleaner extends Module {
   private Timer timer = new Timer();
   @Option.Op(
      min = 0.0D,
      max = 1000.0D,
      increment = 50.0D
   )
   private double delay = 50.0D;

   @EventTarget
   private void onUpdate(UpdateEvent var1) {
      if (var1.getState() == Event.State.POST) {
         InventoryPlayer var2 = ClientUtils.player().inventory;

         for(int var3 = 9; var3 < 45; ++var3) {
            ItemStack var4 = ClientUtils.player().inventoryContainer.getSlot(var3).getStack();
            if (var4 != null) {
               var4.getItem();
               if (this.timer.delay((float)this.delay)) {
                  ClientUtils.playerController().windowClick(0, var3, 0, 0, ClientUtils.player());
                  ClientUtils.playerController().windowClick(0, -999, 0, 0, ClientUtils.player());
                  this.timer.reset();
               }
            }
         }
      }

   }
}
