/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderItem
 *  net.minecraft.client.resources.model.IBakedModel
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import me.report.liquidware.modules.render.EnchantEffect;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={RenderItem.class})
public abstract class MixinRenderItem {
    @Shadow
    protected abstract void func_175035_a(IBakedModel var1, int var2);

    @Redirect(method={"renderEffect"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/entity/RenderItem;renderModel(Lnet/minecraft/client/resources/model/IBakedModel;I)V"))
    private void renderModel(RenderItem renderItem, IBakedModel model, int color) {
        EnchantEffect enchantEffect = (EnchantEffect)LiquidBounce.moduleManager.getModule(EnchantEffect.class);
        this.func_175035_a(model, enchantEffect.getState() ? enchantEffect.getColor().getRGB() : -8372020);
    }
}

