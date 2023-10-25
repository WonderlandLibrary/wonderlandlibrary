package me.rhys.client.module.combat.aura.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.RotationUtil;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.module.combat.aura.Aura;
import net.minecraft.entity.Entity;

public class Single extends ModuleMode<Aura> {
  public Single(String name, Aura parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  void onUpdate(PlayerUpdateEvent event) {
    if (((Aura)this.parent).lockView && ((Aura)this.parent).target != null) {
      Vec2f rotation = ((Aura)this.parent).getRotations((Entity)((Aura)this.parent).target);
      if (((Aura)this.parent).currentRotation == null)
        ((Aura)this.parent).currentRotation = new Vec2f(this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch); 
      if (((Aura)this.parent).smoothness > 0.0F) {
        float yaw = RotationUtil.updateYawRotation(((Aura)this.parent).currentRotation.x, rotation.x, 
            Math.max(1.0F, 180.0F * (1.0F - ((Aura)this.parent).smoothness / 100.0F)));
        float pitch = RotationUtil.updatePitchRotation(((Aura)this.parent).currentRotation.y, rotation.y, 
            Math.max(1.0F, 90.0F * (1.0F - ((Aura)this.parent).smoothness / 100.0F)));
        rotation.x = yaw;
        rotation.y = pitch;
        ((Aura)this.parent).currentRotation = rotation;
      } 
      if (((Aura)this.parent).roundingType == Aura.RoundingType.MINECRAFT)
        rotation = RotationUtil.clampRotation(rotation); 
      this.mc.thePlayer.rotationYaw = rotation.getVecX();
      this.mc.thePlayer.rotationPitch = rotation.getVecY();
    } 
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    if (((Aura)this.parent).target != null && event.getType() == ((((Aura)this.parent).attackMethod == Aura.AttackMethod.PRE) ? Event.Type.PRE : Event.Type.POST)) {
      ((Aura)this.parent).swing((Entity)((Aura)this.parent).target, event);
      if (!((Aura)this.parent).lockView)
        ((Aura)this.parent).aimAtTarget(event, (Entity)((Aura)this.parent).target); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\aura\modes\Single.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */