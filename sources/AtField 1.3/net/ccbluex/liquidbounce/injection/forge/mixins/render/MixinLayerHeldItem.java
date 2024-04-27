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
    protected RenderLivingBase field_177206_a;

    @Shadow
    protected abstract void func_188358_a(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3, EnumHandSide var4);

    @Overwrite
    public void func_177141_a(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        ItemStack itemStack;
        boolean bl = entityLivingBase.func_184591_cq() == EnumHandSide.RIGHT;
        ItemStack itemStack2 = bl ? entityLivingBase.func_184592_cb() : entityLivingBase.func_184614_ca();
        ItemStack itemStack3 = itemStack = bl ? entityLivingBase.func_184614_ca() : entityLivingBase.func_184592_cb();
        if (!itemStack2.func_190926_b() || !itemStack.func_190926_b()) {
            GlStateManager.func_179094_E();
            if (this.field_177206_a.func_177087_b().field_78091_s) {
                float f8 = 0.5f;
                GlStateManager.func_179109_b((float)0.0f, (float)0.75f, (float)0.0f);
                GlStateManager.func_179152_a((float)0.5f, (float)0.5f, (float)0.5f);
            }
            this.func_188358_a(entityLivingBase, itemStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            this.func_188358_a(entityLivingBase, itemStack2, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.func_179121_F();
        }
    }
}

