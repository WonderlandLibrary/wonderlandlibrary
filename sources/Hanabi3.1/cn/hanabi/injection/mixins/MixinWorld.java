package cn.hanabi.injection.mixins;

import cn.hanabi.events.EventSoundPlay;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({World.class})
public class MixinWorld {
   @Inject(
      at = {@At("HEAD")},
      method = {"playSoundAtEntity"},
      cancellable = true
   )
   public void playSoundAtEntity(Entity entityIn, String name, float volume, float pitch, CallbackInfo ci) {
      EventSoundPlay e = new EventSoundPlay(entityIn, name);
      if (e.cancel) {
         ci.cancel();
      }

   }
}
