package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals {
  public static final Predicate<Entity> mobSelector = new Predicate<Entity>() {
      public boolean apply(Entity p_apply_1_) {
        return p_apply_1_ instanceof IMob;
      }
    };
  
  public static final Predicate<Entity> VISIBLE_MOB_SELECTOR = new Predicate<Entity>() {
      public boolean apply(Entity p_apply_1_) {
        return (p_apply_1_ instanceof IMob && !p_apply_1_.isInvisible());
      }
    };
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\monster\IMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */