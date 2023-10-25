/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.util.StringUtils;

public class LayerCustomHead
implements LayerRenderer<EntityLivingBase> {
    private final ModelRenderer field_177209_a;

    @Override
    public void doRenderLayer(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        ItemStack itemStack = entityLivingBase.getCurrentArmor(3);
        if (itemStack != null && itemStack.getItem() != null) {
            float f8;
            boolean bl;
            Item item = itemStack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            if (entityLivingBase.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            boolean bl2 = bl = entityLivingBase instanceof EntityVillager || entityLivingBase instanceof EntityZombie && ((EntityZombie)entityLivingBase).isVillager();
            if (!bl && entityLivingBase.isChild()) {
                f8 = 2.0f;
                float f9 = 1.4f;
                GlStateManager.scale(f9 / f8, f9 / f8, f9 / f8);
                GlStateManager.translate(0.0f, 16.0f * f7, 0.0f);
            }
            this.field_177209_a.postRender(0.0625f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (item instanceof ItemBlock) {
                f8 = 0.625f;
                GlStateManager.translate(0.0f, -0.25f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.scale(f8, -f8, -f8);
                if (bl) {
                    GlStateManager.translate(0.0f, 0.1875f, 0.0f);
                }
                minecraft.getItemRenderer().renderItem(entityLivingBase, itemStack, ItemCameraTransforms.TransformType.HEAD);
            } else if (item == Items.skull) {
                f8 = 1.1875f;
                GlStateManager.scale(f8, -f8, -f8);
                if (bl) {
                    GlStateManager.translate(0.0f, 0.0625f, 0.0f);
                }
                GameProfile gameProfile = null;
                if (itemStack.hasTagCompound()) {
                    String string;
                    NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
                    if (nBTTagCompound.hasKey("SkullOwner", 10)) {
                        gameProfile = NBTUtil.readGameProfileFromNBT(nBTTagCompound.getCompoundTag("SkullOwner"));
                    } else if (nBTTagCompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(string = nBTTagCompound.getString("SkullOwner"))) {
                        gameProfile = TileEntitySkull.updateGameprofile(new GameProfile(null, string));
                        nBTTagCompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameProfile));
                    }
                }
                TileEntitySkullRenderer.instance.renderSkull(-0.5f, 0.0f, -0.5f, EnumFacing.UP, 180.0f, itemStack.getMetadata(), gameProfile, -1);
            }
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

    public LayerCustomHead(ModelRenderer modelRenderer) {
        this.field_177209_a = modelRenderer;
    }
}

