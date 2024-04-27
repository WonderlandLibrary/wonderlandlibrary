package me.aquavit.liquidsense.injection.forge.mixins.block;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.movement.NoSlow;
import net.minecraft.block.BlockSoulSand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSoulSand.class)
@SideOnly(Side.CLIENT)
public class MixinBlockSoulSand {

    @Inject(method = "onEntityCollidedWithBlock", at = @At("HEAD"), cancellable = true)
    private void onEntityCollidedWithBlock(CallbackInfo callbackInfo) {
        if(LiquidSense.moduleManager.getModule(NoSlow.class).getState() && NoSlow.soulsandValue.get())
            callbackInfo.cancel();
    }
}