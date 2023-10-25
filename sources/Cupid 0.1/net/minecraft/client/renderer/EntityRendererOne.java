package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

class EntityRendererOne implements Predicate {
  final EntityRenderer field_90032_a;
  
  EntityRendererOne(EntityRenderer p_i1243_1_) {
    this.field_90032_a = p_i1243_1_;
  }
  
  public boolean apply(Entity p_apply_1_) {
    return p_apply_1_.canBeCollidedWith();
  }
  
  public boolean apply(Object p_apply_1_) {
    return apply((Entity)p_apply_1_);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\EntityRendererOne.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */