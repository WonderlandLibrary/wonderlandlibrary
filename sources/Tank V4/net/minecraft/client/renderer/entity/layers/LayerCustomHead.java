package net.minecraft.client.renderer.entity.layers;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
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

public class LayerCustomHead implements LayerRenderer {
   private final ModelRenderer field_177209_a;

   public LayerCustomHead(ModelRenderer var1) {
      this.field_177209_a = var1;
   }

   public void doRenderLayer(EntityLivingBase var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      ItemStack var9 = var1.getCurrentArmor(3);
      if (var9 != null && var9.getItem() != null) {
         Item var10 = var9.getItem();
         Minecraft var11 = Minecraft.getMinecraft();
         GlStateManager.pushMatrix();
         if (var1.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
         }

         boolean var12 = var1 instanceof EntityVillager || var1 instanceof EntityZombie && ((EntityZombie)var1).isVillager();
         float var13;
         if (!var12 && var1.isChild()) {
            var13 = 2.0F;
            float var14 = 1.4F;
            GlStateManager.scale(var14 / var13, var14 / var13, var14 / var13);
            GlStateManager.translate(0.0F, 16.0F * var8, 0.0F);
         }

         this.field_177209_a.postRender(0.0625F);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         if (var10 instanceof ItemBlock) {
            var13 = 0.625F;
            GlStateManager.translate(0.0F, -0.25F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(var13, -var13, -var13);
            if (var12) {
               GlStateManager.translate(0.0F, 0.1875F, 0.0F);
            }

            var11.getItemRenderer().renderItem(var1, var9, ItemCameraTransforms.TransformType.HEAD);
         } else if (var10 == Items.skull) {
            var13 = 1.1875F;
            GlStateManager.scale(var13, -var13, -var13);
            if (var12) {
               GlStateManager.translate(0.0F, 0.0625F, 0.0F);
            }

            GameProfile var17 = null;
            if (var9.hasTagCompound()) {
               NBTTagCompound var15 = var9.getTagCompound();
               if (var15.hasKey("SkullOwner", 10)) {
                  var17 = NBTUtil.readGameProfileFromNBT(var15.getCompoundTag("SkullOwner"));
               } else if (var15.hasKey("SkullOwner", 8)) {
                  String var16 = var15.getString("SkullOwner");
                  if (!StringUtils.isNullOrEmpty(var16)) {
                     var17 = TileEntitySkull.updateGameprofile(new GameProfile((UUID)null, var16));
                     var15.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), var17));
                  }
               }
            }

            TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, var9.getMetadata(), var17, -1);
         }

         GlStateManager.popMatrix();
      }

   }

   public boolean shouldCombineTextures() {
      return true;
   }
}
