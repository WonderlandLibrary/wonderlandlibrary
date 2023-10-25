package me.rhys.client.module.player;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.TickEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {
  @Name("Spoof")
  public boolean spoof = true;
  
  public InventoryMove(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  public void onTick(TickEvent event) {
    if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof net.minecraft.client.gui.GuiChat))
      for (KeyBinding keyBinding : new KeyBinding[] { this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindJump })
        keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());  
  }
  
  @EventTarget
  public void onPacket(PacketEvent event) {
    if (this.spoof && event.getDirection() == Event.Direction.IN && event.getPacket() instanceof C16PacketClientStatus) {
      C16PacketClientStatus c16PacketClientStatus = (C16PacketClientStatus)event.getPacket();
      if (c16PacketClientStatus.getStatus() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT)
        event.setCancelled(true); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\InventoryMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */