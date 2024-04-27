/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.client.renderer.entity.RendererLivingEntity
 *  net.minecraft.client.renderer.entity.layers.LayerHeldItem
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.dev.important.injection.forge.mixins.render;

import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={LayerHeldItem.class})
public class MixinLayerHeldItem {
    @Shadow
    @Final
    private RendererLivingEntity<?> field_177206_a;

    @Overwrite
    public void func_177141_a(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        ItemStack itemstack = entitylivingbaseIn.func_70694_bm();
        if (itemstack != null) {
            UUID uuid;
            EntityPlayer entityplayer;
            GlStateManager.func_179094_E();
            if (this.field_177206_a.func_177087_b().field_78091_s) {
                float f = 0.5f;
                GlStateManager.func_179109_b((float)0.0f, (float)0.625f, (float)0.0f);
                GlStateManager.func_179114_b((float)-20.0f, (float)-1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.func_179152_a((float)f, (float)f, (float)f);
            }
            if ((entityplayer = Minecraft.func_71410_x().field_71441_e.func_152378_a(uuid = entitylivingbaseIn.func_110124_au())) != null && entityplayer.func_70632_aY()) {
                if (entitylivingbaseIn.func_70093_af()) {
                    ((ModelBiped)this.field_177206_a.func_177087_b()).func_178718_a(0.0325f);
                    GlStateManager.func_179109_b((float)-0.58f, (float)0.3f, (float)-0.2f);
                    GlStateManager.func_179114_b((float)-24390.0f, (float)137290.0f, (float)-2009900.0f, (float)-2054900.0f);
                } else {
                    ((ModelBiped)this.field_177206_a.func_177087_b()).func_178718_a(0.0325f);
                    GlStateManager.func_179109_b((float)-0.48f, (float)0.2f, (float)-0.2f);
                    GlStateManager.func_179114_b((float)-24390.0f, (float)137290.0f, (float)-2009900.0f, (float)-2054900.0f);
                }
            } else {
                ((ModelBiped)this.field_177206_a.func_177087_b()).func_178718_a(0.0625f);
            }
            GlStateManager.func_179109_b((float)-0.0625f, (float)0.4375f, (float)0.0625f);
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer)entitylivingbaseIn).field_71104_cf != null) {
                itemstack = new ItemStack((Item)Items.field_151112_aM, 0);
            }
            Item item = itemstack.func_77973_b();
            Minecraft minecraft = Minecraft.func_71410_x();
            if (item instanceof ItemBlock && Block.func_149634_a((Item)item).func_149645_b() == 2) {
                GlStateManager.func_179109_b((float)0.0f, (float)0.1875f, (float)-0.3125f);
                GlStateManager.func_179114_b((float)20.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                float f1 = 0.375f;
                GlStateManager.func_179152_a((float)(-f1), (float)(-f1), (float)f1);
            }
            if (entitylivingbaseIn.func_70093_af()) {
                GlStateManager.func_179109_b((float)0.0f, (float)0.203125f, (float)0.0f);
            }
            minecraft.func_175597_ag().func_178099_a(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.func_179121_F();
        }
    }
}

