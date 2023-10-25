package space.clowdy.utils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import space.clowdy.Client;
import space.clowdy.modules.ModuleManager;
import space.clowdy.modules.impl.SelfDestruct;

public class KeyboardHandler {
     @SubscribeEvent
     public void onKeyInputEvent(KeyInputEvent keyInputEvent) {
          if (keyInputEvent.getKey() == 344 && keyInputEvent.getAction() == 1 && Minecraft.getInstance().currentScreen == null && !SelfDestruct.cacheFile.exists() && !SelfDestruct.hidden) {
               Minecraft.getInstance().displayGuiScreen(Client.clickGui);
          }

          ModuleManager.handleKeyboard(keyInputEvent.getKey(), keyInputEvent.getAction());
     }
}
