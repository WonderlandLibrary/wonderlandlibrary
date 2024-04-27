package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.render.Chams;
import me.aquavit.liquidsense.module.modules.render.ItemPhysic;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderEntityItem.class)
public abstract class MixinRenderEntityItem extends MixinRender{

    @Shadow
    protected abstract int func_177078_a(ItemStack p_177078_1_);

    private float ItemToRotate;

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    private int func_177077_a(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_) {

        ItemStack itemstack = itemIn.getEntityItem();
        Item item = itemstack.getItem();

        if (item == null) {
            return 0;
        } else {
            boolean flag = p_177077_9_.isGui3d();
            int i = this.func_177078_a(itemstack);
            float f = 0.25F;

            boolean flagNoHover = !LiquidSense.moduleManager.getModule(ItemPhysic.class).getState() || !ItemPhysic.nohover.get();
            float f1;

            if (flagNoHover) {
                f1 = MathHelper.sin(((float) itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F;
            } else {
                f1 =  flag ? -0.1f : -0.23f;
            }

            float f2 = p_177077_9_.getItemCameraTransforms()
                    .getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
            GlStateManager.translate((float) p_177077_2_, (float) p_177077_4_ + f1 + 0.25F * f2, (float) p_177077_6_);

            if (flag || this.renderManager.options != null) {

                float f3 = (((float) itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart)
                        * (180F / (float) Math.PI);

                if(!flagNoHover)
                    f3 = itemIn.hoverStart + itemIn.getEntityId();

                GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
            }

            if (LiquidSense.moduleManager.getModule(ItemPhysic.class).getState()) {

                if (!itemIn.onGround)
                    ItemToRotate += 4.0f;

            } else {

                if (ItemToRotate != 0.0F)
                    ItemToRotate = 0.0F;
            }

            if (ItemToRotate > 0.0F) {
                if (!itemIn.onGround) {
                    GlStateManager.rotate(ItemToRotate, ItemPhysic.rotateX.get() ? 360F : 0F, ItemPhysic.rotateY.get() ? 360F : 0F, ItemPhysic.rotateZ.get() ? 360F : 0F);
                } else {
                    GlStateManager.scale(1.2F, 1.2F, 1.2F);
                    GlStateManager.rotate(90F, 90F, 0F, 0F);
                }
            }

            if (!flag) {
                float f6 = -0.0F * (float) (i - 1) * 0.5F;
                float f4 = -0.0F * (float) (i - 1) * 0.5F;
                float f5 = -0.046875F * (float) (i - 1) * 0.5F;
                GlStateManager.translate(f6, f4, f5);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

    @Inject(method = "doRender", at = @At("HEAD"))
    private void injectChamsPre(CallbackInfo callbackInfo) {
        if (LiquidSense.moduleManager.getModule(Chams.class).getState() && Chams.itemsValue.get()) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0F, -1000000F);
        }

    }

    @Inject(method = "doRender", at = @At("RETURN"))
    private void injectChamsPost(CallbackInfo callbackInfo) {
        if (LiquidSense.moduleManager.getModule(Chams.class).getState() && Chams.itemsValue.get()) {
            GL11.glPolygonOffset(1.0F, 1000000F);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }
}
