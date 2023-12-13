// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import java.awt.Color;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.module.render.ViewmodelChanger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.module.render.EnchantColor;
import com.elementars.eclient.Xulu;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.RenderItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderItem.class })
public class MixinRenderItem
{
    @Shadow
    private void renderModel(final IBakedModel model, final int color, final ItemStack stack) {
    }
    
    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int renderEffect(final int oldValue) {
        return Xulu.MODULE_MANAGER.getModule(EnchantColor.class).isToggled() ? EnchantColor.getColor(1L, 1.0f).getRGB() : oldValue;
    }
    
    @Inject(method = { "renderItemModel" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.BEFORE) })
    private void test(final ItemStack stack, final IBakedModel bakedmodel, final ItemCameraTransforms.TransformType transform, final boolean leftHanded, final CallbackInfo ci) {
        if (Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class) != null && Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).mode.getValue().isOK(leftHanded) && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class).isToggled() && Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).item.getValue() && (!Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).pause.getValue() || !Wrapper.getMinecraft().player.isHandActive() || this.isHandGood(Wrapper.getMinecraft().player.getActiveHand(), leftHanded))) {
            GlStateManager.scale((float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).sizex.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).sizey.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).sizez.getValue());
            GlStateManager.rotate(Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).x.getValue() * 360.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).y.getValue() * 360.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).z.getValue() * 360.0f, 0.0f, 0.0f, 1.0f);
            GlStateManager.translate((float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).posX.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).posY.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).posZ.getValue());
        }
    }
    
    private boolean isHandGood(final EnumHand activeHand, final boolean leftHandedRenderHand) {
        switch (activeHand) {
            case MAIN_HAND: {
                return leftHandedRenderHand;
            }
            case OFF_HAND: {
                return !leftHandedRenderHand;
            }
            default: {
                return false;
            }
        }
    }
    
    @Redirect(method = { "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V"))
    private void color(final float colorRed, final float colorGreen, final float colorBlue, final float colorAlpha) {
        if (Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class) != null && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class).isToggled()) {
            GlStateManager.color(colorRed, colorGreen, colorBlue, (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).alpha.getValue());
        }
        else {
            GlStateManager.color(colorRed, colorGreen, colorBlue, colorAlpha);
        }
    }
    
    @Redirect(method = { "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"))
    private void renderModelColor(final RenderItem renderItem, final IBakedModel model, final ItemStack stack) {
        if (Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class) != null && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class).isToggled()) {
            this.renderModel(model, new Color(1.0f, 1.0f, 1.0f, Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).alpha.getValue()).getRGB(), stack);
        }
        else {
            this.renderModel(model, -1, stack);
        }
    }
}
