package my.NewSnake.Tank.module.modules.WORLD;

import my.NewSnake.Tank.module.Module;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render2DEvent;
import my.NewSnake.utils.ClientUtils;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

@Module.Mod(
   displayName = "Inventory Move"
)
public class InventoryMove extends Module {
   @EventTarget
   private void onRender(Render2DEvent var1) {
      if (ClientUtils.mc().currentScreen != null && !(ClientUtils.mc().currentScreen instanceof GuiChat)) {
         if (Keyboard.isKeyDown(200)) {
            ClientUtils.pitch(ClientUtils.pitch() - 2.0F);
         }

         if (Keyboard.isKeyDown(208)) {
            ClientUtils.pitch(ClientUtils.pitch() + 2.0F);
         }

         if (Keyboard.isKeyDown(203)) {
            ClientUtils.yaw(ClientUtils.yaw() - 3.0F);
         }

         if (Keyboard.isKeyDown(205)) {
            ClientUtils.yaw(ClientUtils.yaw() + 3.0F);
         }
      }

   }
}
