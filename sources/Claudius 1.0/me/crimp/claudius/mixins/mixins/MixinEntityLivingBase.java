package me.crimp.claudius.mixins.mixins;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.modules.render.SwingSpeed;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    public MixinEntityLivingBase(World world) {
        super(world);
    }



    @Shadow
    protected abstract boolean isPlayer();
    @Inject(method = "getArmSwingAnimationEnd", at = @At(value = "HEAD"), cancellable = true)
    protected void onGetArmSwingAnimationEnd(CallbackInfoReturnable<Integer> callback) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player == null || !this.isPlayer())
            return;

        if (claudius.moduleManager.isModuleEnabled("SwingSpeed")) {
            callback.setReturnValue((int) (SwingSpeed.INSTANCE).sped.getValue().intValue());
        }
    }
}
