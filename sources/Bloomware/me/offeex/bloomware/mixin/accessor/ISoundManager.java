package me.offeex.bloomware.mixin.accessor;

import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundManager.class)
public interface ISoundManager {
    @Accessor("soundSystem")
    SoundSystem getSoundSystem();
}
