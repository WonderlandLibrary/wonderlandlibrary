/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.client.renderer.entity.layers;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;

public class LayerCustomHead
implements LayerRenderer {
    private final ModelRenderer field_177209_a;
    private static final String __OBFID = "CL_00002422";

    public LayerCustomHead(ModelRenderer p_i46120_1_) {
        this.field_177209_a = p_i46120_1_;
    }

    @Override
    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        ItemStack var9 = p_177141_1_.getCurrentArmor(3);
        if (var9 != null && var9.getItem() != null) {
            float var13;
            boolean var12;
            Item var10 = var9.getItem();
            Minecraft var11 = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            if (p_177141_1_.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            boolean bl = var12 = p_177141_1_ instanceof EntityVillager || p_177141_1_ instanceof EntityZombie && ((EntityZombie)p_177141_1_).isVillager();
            if (!var12 && p_177141_1_.isChild()) {
                var13 = 2.0f;
                float var14 = 1.4f;
                GlStateManager.scale(var14 / var13, var14 / var13, var14 / var13);
                GlStateManager.translate(0.0f, 16.0f * p_177141_8_, 0.0f);
            }
            this.field_177209_a.postRender(0.0625f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (var10 instanceof ItemBlock) {
                var13 = 0.625f;
                GlStateManager.translate(0.0f, -0.25f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.scale(var13, -var13, -var13);
                if (var12) {
                    GlStateManager.translate(0.0f, 0.1875f, 0.0f);
                }
                var11.getItemRenderer().renderItem(p_177141_1_, var9, ItemCameraTransforms.TransformType.HEAD);
            } else if (var10 == Items.skull) {
                var13 = 1.1875f;
                GlStateManager.scale(var13, -var13, -var13);
                if (var12) {
                    GlStateManager.translate(0.0f, 0.0625f, 0.0f);
                }
                GameProfile var16 = null;
                if (var9.hasTagCompound()) {
                    NBTTagCompound var15 = var9.getTagCompound();
                    if (var15.hasKey("SkullOwner", 10)) {
                        var16 = NBTUtil.readGameProfileFromNBT(var15.getCompoundTag("SkullOwner"));
                    } else if (var15.hasKey("SkullOwner", 8)) {
                        var16 = TileEntitySkull.updateGameprofile(new GameProfile(null, var15.getString("SkullOwner")));
                        var15.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), var16));
                    }
                }
                TileEntitySkullRenderer.instance.renderSkull(-0.5f, 0.0f, -0.5f, EnumFacing.UP, 180.0f, var9.getMetadata(), var16, -1);
            }
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}

