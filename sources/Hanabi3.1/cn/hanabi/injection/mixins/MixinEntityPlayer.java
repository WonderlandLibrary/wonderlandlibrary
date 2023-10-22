package cn.hanabi.injection.mixins;

import cn.hanabi.injection.interfaces.IEntityPlayer;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({EntityPlayer.class})
public abstract class MixinEntityPlayer extends MixinEntity implements IEntityPlayer {
   @Shadow
   private int itemInUseCount;
   @Shadow
   protected float speedInAir;

   @Shadow
   public abstract GameProfile getGameProfile();

   public void setSpeedInAir(float i) {
      this.speedInAir = i;
   }

   public void setItemInUseCount(int i) {
      this.itemInUseCount = i;
   }
}
