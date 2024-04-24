package me.offeex.bloomware.mixin.accessor;

import net.minecraft.entity.passive.AnimalEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(AnimalEntity.class)
public interface IAnimalEntity {
    @Accessor
    UUID getLovingPlayer();
}
