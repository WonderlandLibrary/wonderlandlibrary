package me.rhys.client.module.ghost.ghostbot;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.MathUtil;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.Timer;
import me.rhys.base.util.vec.Vec2f;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class GhostBot extends Module {
  public int distance;
  
  @Name("Check Tab")
  public boolean tabCheck;
  
  @Name("CPS")
  @Clamp(min = 1.0D, max = 20.0D)
  public int cpsa;
  
  private Vec2f lockViewRotation;
  
  private int aimTicks;
  
  private boolean allowMovement;
  
  private final Timer attackTimer;
  
  public GhostBot(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.distance = 15;
    this.tabCheck = true;
    this.cpsa = 13;
    this.attackTimer = new Timer();
  }
  
  public void onEnable() {
    this.allowMovement = true;
    this.lockViewRotation = null;
    this.aimTicks = 0;
  }
  
  public void onDisable() {
    this.mc.gameSettings.keyBindForward.pressed = false;
  }
  
  @EventTarget
  public void onUpdate(PlayerUpdateEvent event) {
    this.mc.thePlayer.rotationYaw = this.lockViewRotation.getVecX();
    this.mc.thePlayer.rotationPitch = this.lockViewRotation.getVecY();
    if (this.lockViewRotation != null && ++this.aimTicks > 20)
      this.mc.gameSettings.keyBindForward.pressed = this.allowMovement; 
  }
  
  @EventTarget
  public void onMove(PlayerMoveEvent event) {
    if (this.aimTicks > 22 && this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.onGround)
      event.motionY = this.mc.thePlayer.motionY = 0.41999998688697815D; 
  }
  
  @EventTarget
  public void onMotion(PlayerMotionEvent event) {
    if (event.getType() == Event.Type.PRE) {
      Entity entity = getTarget();
      if (entity != null) {
        this.allowMovement = (this.mc.thePlayer.getDistanceToEntity(entity) > 3.0F);
        this.lockViewRotation = RotationUtil.getRotations(entity);
        if (this.aimTicks > 20)
          attack(entity); 
      } else {
        this.aimTicks = 0;
      } 
    } 
  }
  
  private Entity getTarget() {
    for (Entity entity : this.mc.theWorld.loadedEntityList) {
      if (entity == null || this.mc.thePlayer.getDistanceToEntity(entity) > (this.distance * 4) || this.mc.thePlayer
        .isEntityEqual(entity) || (this.tabCheck && 
        !checkTab(entity)) || !(entity instanceof EntityPlayer))
        continue; 
      return entity;
    } 
    return null;
  }
  
  private void attack(Entity entity) {
    int cps = this.cpsa;
    if (this.mc.thePlayer.getDistanceToEntity(entity) < 2.8D) {
      this.mc.gameSettings.keyBindSprint.pressed = false;
      double aps = (cps + MathUtil.randFloat(MathUtil.randFloat(1.0F, 3.0F), MathUtil.randFloat(3.0F, 5.0F)));
      if (this.attackTimer.hasReached(1000.0D / aps)) {
        this.attackTimer.reset();
        this.mc.thePlayer.swingItem();
        this.mc.playerController.attackEntity((EntityPlayer)player(), entity);
      } 
    } else {
      this.mc.gameSettings.keyBindSprint.pressed = true;
    } 
  }
  
  private boolean checkTab(Entity entity) {
    return (entity instanceof EntityPlayer && GuiPlayerTabOverlay.getPlayerList().contains(entity));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\ghostbot\GhostBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */