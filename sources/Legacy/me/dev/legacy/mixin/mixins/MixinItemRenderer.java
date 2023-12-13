package me.dev.legacy.mixin.mixins;

import me.dev.legacy.features.modules.render.ItemViewModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(method = "renderItemSide", at = @At("HEAD"))
    public void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        if (ItemViewModel.INSTANCE.isEnabled()) {
            GlStateManager.scale(ItemViewModel.INSTANCE.scaleX.getValue() / 100F, ItemViewModel.INSTANCE.scaleY.getValue() / 100F, ItemViewModel.INSTANCE.scaleZ.getValue() / 100F);
            if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                GlStateManager.translate(ItemViewModel.INSTANCE.translateX.getValue() / 100F, ItemViewModel.INSTANCE.translateY.getValue() / 100F, ItemViewModel.INSTANCE.translateZ.getValue() / 100F);
                GlStateManager.rotate(ItemViewModel.INSTANCE.rotateX.getValue(), 1, 0, 0);
                GlStateManager.rotate(ItemViewModel.INSTANCE.rotateY.getValue(), 0, 1, 0);
                GlStateManager.rotate(ItemViewModel.INSTANCE.rotateZ.getValue(), 0, 0, 1);
            } else if (transform == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                GlStateManager.translate(-ItemViewModel.INSTANCE.translateX.getValue() / 100F, ItemViewModel.INSTANCE.translateY.getValue() / 100F, ItemViewModel.INSTANCE.translateZ.getValue() / 100F);
                GlStateManager.rotate(-ItemViewModel.INSTANCE.rotateX.getValue(), 1, 0, 0);
                GlStateManager.rotate(ItemViewModel.INSTANCE.rotateY.getValue(), 0, 1, 0);
                GlStateManager.rotate(ItemViewModel.INSTANCE.rotateZ.getValue(), 0, 0, 1);
            }
        }
    }


}
