package me.offeex.bloomware.mixin.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import me.offeex.bloomware.Bloomware;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(SplashOverlay.class)
public abstract class MixinSplashOverlay {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V", shift = At.Shift.AFTER))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Identifier LOGO = new Identifier("bloomware", "icon.png");

        RenderSystem.setShaderTexture(0, LOGO);
        RenderSystem.enableBlend();
        RenderSystem.blendEquation(GL14.GL_FUNC_ADD);
        RenderSystem.blendFunc(GL14.GL_SRC_ALPHA, 1);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

        int w = Bloomware.INSTANCE.getMc().getWindow().getScaledWidth() / 12;
        int x = Bloomware.INSTANCE.getMc().getWindow().getScaledWidth() / 2 - w / 2;
        int y = Bloomware.INSTANCE.getMc().getWindow().getScaledHeight() / 2 - w - 30;
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, w, w, w, w);

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
