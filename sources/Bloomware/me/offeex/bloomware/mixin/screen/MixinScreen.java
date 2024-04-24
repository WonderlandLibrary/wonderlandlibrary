package me.offeex.bloomware.mixin.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.visuals.NoRender;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(Screen.class)
public class MixinScreen {
    @Inject(method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At("HEAD"), cancellable = true)
    private void renderBackground(MatrixStack matrixStack, CallbackInfo ci) {
        NoRender module = NoRender.INSTANCE;
        if (Bloomware.INSTANCE.getMc().world != null && module.getEnabled() && NoRender.INSTANCE.getDarkness().getToggled()) ci.cancel();
    }
}
