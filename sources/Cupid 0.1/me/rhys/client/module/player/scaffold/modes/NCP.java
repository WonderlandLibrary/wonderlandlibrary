package me.rhys.client.module.player.scaffold.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.player.scaffold.Scaffold;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;

public class NCP extends ModuleMode<Scaffold> {
  private Scaffold.BlockEntry lastBlockEntry;
  
  private int lastSlot;
  
  private int startSlot;
  
  private final Timer towerTimer;
  
  public NCP(String name, Scaffold parent) {
    super(name, (Module)parent);
    this.towerTimer = new Timer();
  }
  
  public void onEnable() {
    this.startSlot = this.mc.thePlayer.inventory.currentItem;
    if (((Scaffold)this.parent).getSlotWithBlock() > -1)
      this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(((Scaffold)this.parent).getSlotWithBlock())); 
    this.lastSlot = ((Scaffold)this.parent).getSlotWithBlock();
  }
  
  public void onDisable() {
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(this.startSlot));
    this.lastBlockEntry = null;
  }
  
  @EventTarget
  public void onMotion(PlayerMotionEvent event) {
    if (this.lastBlockEntry != null) {
      Vec2f rotation = RotationUtil.getRotations(((Scaffold)this.parent).getPositionByFace(this.lastBlockEntry.getPosition(), this.lastBlockEntry
            .getFacing()));
      if (this.mc.thePlayer.isPlayerMoving())
        this.mc.thePlayer.setSprinting(((Scaffold)this.parent).sprint); 
      event.getPosition().setRotation(rotation);
    } 
    Scaffold.BlockEntry blockEntry = ((Scaffold)this.parent).find(new Vec3(0.0D, 0.0D, 0.0D));
    this.lastBlockEntry = blockEntry;
    if (event.getType() == Event.Type.POST && blockEntry != null) {
      int slot = ((Scaffold)this.parent).getSlotWithBlock();
      if (slot > -1) {
        if (this.lastSlot != slot) {
          this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(slot));
          this.lastSlot = slot;
        } 
        if (((Scaffold)this.parent).placeBlock(blockEntry.getPosition().add(0, 0, 0), blockEntry.getFacing(), slot, ((Scaffold)this.parent).swing)) {
          this.mc.thePlayer.motionY = 0.41999998688697815D;
          this.mc.thePlayer.motionZ = this.mc.thePlayer.motionX = 0.0D;
          if (((Scaffold)this.parent).tower && !this.mc.thePlayer.isPotionActive(Potion.jump) && !this.mc.thePlayer.isPlayerMoving() && this.mc.gameSettings.keyBindJump.isKeyDown() && this.towerTimer.hasReached(1500.0D)) {
            this.towerTimer.reset();
            this.mc.thePlayer.motionY = -0.2800000011920929D;
          } 
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\scaffold\modes\NCP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */