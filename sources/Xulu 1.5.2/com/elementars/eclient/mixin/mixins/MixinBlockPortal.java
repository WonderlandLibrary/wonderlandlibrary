// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.module.misc.AntiSound;
import com.elementars.eclient.Xulu;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.block.BlockPortal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ BlockPortal.class })
public class MixinBlockPortal
{
    @Redirect(method = { "randomDisplayTick" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FFZ)V"))
    private void onPortalSound(final World world, final double x, final double y, final double z, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch, final boolean distanceDelay) {
        if (!Xulu.MODULE_MANAGER.getModule(AntiSound.class).isToggled() || !Xulu.MODULE_MANAGER.getModuleT(AntiSound.class).portal.getValue()) {
            world.playSound(x, y, z, soundIn, category, volume, pitch, distanceDelay);
        }
    }
}
