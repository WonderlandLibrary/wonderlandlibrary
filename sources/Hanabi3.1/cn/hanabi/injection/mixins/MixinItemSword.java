package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IItemSword;
import net.minecraft.item.ItemSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ItemSword.class})
public class MixinItemSword implements IItemSword {
   @Shadow
   private float attackDamage;

   public float getAttackDamage() {
      return this.attackDamage;
   }
}
