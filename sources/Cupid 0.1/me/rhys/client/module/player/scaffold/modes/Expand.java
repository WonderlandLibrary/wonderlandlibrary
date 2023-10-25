package me.rhys.client.module.player.scaffold.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.input.KeyboardInputEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.player.scaffold.Scaffold;
import net.minecraft.util.Vec3;

public class Expand extends ModuleMode<Scaffold> {
  private int startSlot;
  
  private int lastSlot;
  
  @Name("Expansion")
  @Clamp(min = 1.0D, max = 6.0D)
  private int expansion = 4;
  
  public Expand(String name, Scaffold parent) {
    super(name, (Module)parent);
  }
  
  public void onEnable() {
    this.startSlot = this.mc.thePlayer.inventory.currentItem;
    if (((Scaffold)this.parent).getSlotWithBlock() > -1)
      this.mc.thePlayer.inventory.currentItem = ((Scaffold)this.parent).getSlotWithBlock(); 
    this.lastSlot = ((Scaffold)this.parent).getSlotWithBlock();
  }
  
  public void onDisable() {
    if (this.mc.thePlayer.inventory.currentItem != this.startSlot)
      this.mc.thePlayer.inventory.currentItem = this.startSlot; 
  }
  
  @EventTarget
  public void onMotion(PlayerMotionEvent event) {
    Vec2f vec2f = null;
    if (this.mc.thePlayer.isPlayerMoving())
      (getMc()).thePlayer.setSprinting(((Scaffold)this.parent).sprint); 
    int slot = ((Scaffold)this.parent).getSlotWithBlock();
    if (slot > -1 && event.getType() == Event.Type.PRE) {
      if (this.lastSlot != slot) {
        this.mc.thePlayer.inventory.currentItem = slot;
        this.lastSlot = slot;
      } 
      int expand = this.expansion * 5;
      for (int i = 0; i < expand; i++) {
        Scaffold.BlockEntry blockEntry = ((Scaffold)this.parent).findExpand(new Vec3(this.mc.thePlayer.motionX * i, 0.0D, this.mc.thePlayer.motionZ * i), i);
        if (blockEntry != null) {
          event.getPosition().setRotation(RotationUtil.getRotations(((Scaffold)this.parent)
                .getPositionByFace(blockEntry.getPosition(), blockEntry
                  .getFacing())));
          ((Scaffold)this.parent).placeBlock(blockEntry.getPosition(), blockEntry.getFacing(), ((Scaffold)this.parent)
              .getSlotWithBlock(), ((Scaffold)this.parent).swing);
        } 
      } 
    } 
  }
  
  @EventTarget
  public void onKeyboard(KeyboardInputEvent event) {
    if (!((Scaffold)this.parent).sprint && event.getKeyCode() == 29)
      event.setCancelled(true); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\scaffold\modes\Expand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */