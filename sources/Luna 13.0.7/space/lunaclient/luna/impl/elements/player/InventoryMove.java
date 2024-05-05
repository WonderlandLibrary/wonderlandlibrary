package space.lunaclient.luna.impl.elements.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="InventoryMove", category=Category.PLAYER, description="Allows you to move in your inventory.")
public class InventoryMove
  extends Element
{
  public InventoryMove() {}
  
  public void onEnable()
  {
    super.onEnable();
  }
  
  public void onDisable()
  {
    super.onDisable();
  }
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    if ((mc.currentScreen instanceof GuiContainer)) {
      for (KeyBinding keyBinding : new KeyBinding[] { mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump }) {
        keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
      }
    }
  }
}
