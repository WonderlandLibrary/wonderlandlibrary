// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySkull;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemBlock;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.model.ModelRenderer;

public class LayerCustomHead implements LayerRenderer
{
    private final ModelRenderer field_177209_a;
    private static final String __OBFID = "CL_00002422";
    
    public LayerCustomHead(final ModelRenderer p_i46120_1_) {
        this.field_177209_a = p_i46120_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        final ItemStack var9 = p_177141_1_.getCurrentArmor(3);
        if (var9 != null && var9.getItem() != null) {
            final Item var10 = var9.getItem();
            final Minecraft var11 = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            if (p_177141_1_.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            final boolean var12 = p_177141_1_ instanceof EntityVillager || (p_177141_1_ instanceof EntityZombie && ((EntityZombie)p_177141_1_).isVillager());
            if (!var12 && p_177141_1_.isChild()) {
                final float var13 = 2.0f;
                final float var14 = 1.4f;
                GlStateManager.scale(var14 / var13, var14 / var13, var14 / var13);
                GlStateManager.translate(0.0f, 16.0f * p_177141_8_, 0.0f);
            }
            this.field_177209_a.postRender(0.0625f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (var10 instanceof ItemBlock) {
                final float var13 = 0.625f;
                GlStateManager.translate(0.0f, -0.25f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.scale(var13, -var13, -var13);
                if (var12) {
                    GlStateManager.translate(0.0f, 0.1875f, 0.0f);
                }
                var11.getItemRenderer().renderItem(p_177141_1_, var9, ItemCameraTransforms.TransformType.HEAD);
            }
            else if (var10 == Items.skull) {
                final float var13 = 1.1875f;
                GlStateManager.scale(var13, -var13, -var13);
                if (var12) {
                    GlStateManager.translate(0.0f, 0.0625f, 0.0f);
                }
                GameProfile var15 = null;
                if (var9.hasTagCompound()) {
                    final NBTTagCompound var16 = var9.getTagCompound();
                    if (var16.hasKey("SkullOwner", 10)) {
                        var15 = NBTUtil.readGameProfileFromNBT(var16.getCompoundTag("SkullOwner"));
                    }
                    else if (var16.hasKey("SkullOwner", 8)) {
                        var15 = TileEntitySkull.updateGameprofile(new GameProfile((UUID)null, var16.getString("SkullOwner")));
                        var16.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), var15));
                    }
                }
                TileEntitySkullRenderer.instance.renderSkull(-0.5f, 0.0f, -0.5f, EnumFacing.UP, 180.0f, var9.getMetadata(), var15, -1);
            }
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
