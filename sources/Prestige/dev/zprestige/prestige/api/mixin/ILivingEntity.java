package dev.zprestige.prestige.api.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={LivingEntity.class})
public interface ILivingEntity {
    @Accessor(value="jumpingCooldown")
    void setJumpingCooldown(int var1);
}
