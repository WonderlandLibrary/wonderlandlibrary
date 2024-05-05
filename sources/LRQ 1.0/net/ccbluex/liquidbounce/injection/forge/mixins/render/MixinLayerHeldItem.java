/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.entity.RenderLivingBase
 *  net.minecraft.client.renderer.entity.layers.LayerHeldItem
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHandSide
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={LayerHeldItem.class})
public abstract class MixinLayerHeldItem {
    @Shadow
    @Final
    protected RenderLivingBase<?> field_177206_a;

    @Shadow
    protected abstract void func_188358_a(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3, EnumHandSide var4);

    @Overwrite
    public void func_177141_a(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        ItemStack itemstack1;
        boolean flag = entitylivingbaseIn.func_184591_cq() == EnumHandSide.RIGHT;
        ItemStack itemstack = flag ? entitylivingbaseIn.func_184592_cb() : entitylivingbaseIn.func_184614_ca();
        ItemStack itemStack = itemstack1 = flag ? entitylivingbaseIn.func_184614_ca() : entitylivingbaseIn.func_184592_cb();
        if (!itemstack.func_190926_b() || !itemstack1.func_190926_b()) {
            GlStateManager.func_179094_E();
            if (this.field_177206_a.func_177087_b().field_78091_s) {
                float f = 0.5f;
                GlStateManager.func_179109_b((float)0.0f, (float)0.75f, (float)0.0f);
                GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
            }
            this.func_188358_a(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            this.func_188358_a(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.func_179121_F();
        }
    }
}

