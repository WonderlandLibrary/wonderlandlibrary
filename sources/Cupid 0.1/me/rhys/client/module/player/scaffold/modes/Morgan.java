package me.rhys.client.module.player.scaffold.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.input.KeyboardInputEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.player.scaffold.Scaffold;
import net.minecraft.util.Vec3;

public class Morgan extends ModuleMode<Scaffold> {
  private Scaffold.BlockEntry blockEntry;
  
  private int startSlot;
  
  private int lastSlot;
  
  private boolean didPlaceBlock;
  
  public boolean scaffoling = false;
  
  public boolean isScaffoling() {
    return this.scaffoling;
  }
  
  public Morgan(String name, Scaffold parent) {
    super(name, (Module)parent);
  }
  
  public void onEnable() {
    this.scaffoling = true;
    this.blockEntry = null;
    this.didPlaceBlock = false;
    this.startSlot = this.mc.thePlayer.inventory.currentItem;
    if (((Scaffold)this.parent).getSlotWithBlock() > -1)
      this.mc.thePlayer.inventory.currentItem = ((Scaffold)this.parent).getSlotWithBlock(); 
    this.lastSlot = ((Scaffold)this.parent).getSlotWithBlock();
  }
  
  public void onDisable() {
    this.scaffoling = false;
    this.mc.timer.timerSpeed = 1.0F;
    if (this.mc.thePlayer.inventory.currentItem != this.startSlot)
      this.mc.thePlayer.inventory.currentItem = this.startSlot; 
  }
  
  @EventTarget
  public void onMotion(PlayerMotionEvent event) {
    Vec2f vec2f = null;
    this.mc.timer.timerSpeed = 0.8F;
    if (this.mc.thePlayer.isPlayerMoving())
      (getMc()).thePlayer.setSprinting(false); 
    if (this.blockEntry != null) {
      vec2f = RotationUtil.getRotations(((Scaffold)this.parent)
          .getPositionByFace(this.blockEntry.getPosition(), this.blockEntry
            .getFacing()));
      vec2f.setVecY(90.0F);
    } 
    Scaffold.BlockEntry blockEntry = ((Scaffold)this.parent).find(new Vec3(0.0D, 0.0D, 0.0D));
    if (blockEntry == null)
      return; 
    this.blockEntry = blockEntry;
    if (vec2f != null)
      event.getPosition().setRotation(vec2f); 
    int slot = ((Scaffold)this.parent).getSlotWithBlock();
    if (((Scaffold)this.parent).getBlockCount() < 1 && this.didPlaceBlock) {
      this.mc.thePlayer.motionY -= 20.0D;
      this.didPlaceBlock = false;
      return;
    } 
    if (this.blockEntry != null && vec2f != null && slot > -1 && event.getType() == Event.Type.PRE) {
      if (this.lastSlot != slot) {
        this.mc.thePlayer.inventory.currentItem = slot;
        this.lastSlot = slot;
      } 
      ((Scaffold)this.parent).placeBlockVerus(this.blockEntry.getPosition().add(0, 0, 0), this.blockEntry.getFacing(), slot, true);
    } 
  }
  
  @EventTarget
  public void onKeyboard(KeyboardInputEvent event) {
    if (!((Scaffold)this.parent).sprint && event.getKeyCode() == 29)
      event.setCancelled(true); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\scaffold\modes\Morgan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */