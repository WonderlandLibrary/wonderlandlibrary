package net.minecraft.client.renderer.entity.layers;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;

public class LayerCustomHead
implements LayerRenderer<EntityLivingBase> {
    private final ModelRenderer field_177209_a;

    public LayerCustomHead(ModelRenderer p_i46120_1_) {
        this.field_177209_a = p_i46120_1_;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        ItemStack itemstack = entitylivingbaseIn.getCurrentArmor(3);
        if (itemstack != null && itemstack.getItem() != null) {
            boolean flag;
            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate((float)0.0f, (float)0.2f, (float)0.0f);
            }
            boolean bl = flag = entitylivingbaseIn instanceof EntityVillager || entitylivingbaseIn instanceof EntityZombie && ((EntityZombie)entitylivingbaseIn).isVillager();
            if (!flag && entitylivingbaseIn.isChild()) {
                float f = 2.0f;
                float f1 = 1.4f;
                GlStateManager.scale((float)(f1 / f), (float)(f1 / f), (float)(f1 / f));
                GlStateManager.translate((float)0.0f, (float)(16.0f * scale), (float)0.0f);
            }
            this.field_177209_a.postRender(0.0625f);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            if (item instanceof ItemBlock) {
                float f2 = 0.625f;
                GlStateManager.translate((float)0.0f, (float)-0.25f, (float)0.0f);
                GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GlStateManager.scale((float)f2, (float)(-f2), (float)(-f2));
                if (flag) {
                    GlStateManager.translate((float)0.0f, (float)0.1875f, (float)0.0f);
                }
                minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.HEAD);
            } else if (item == Items.skull) {
                float f3 = 1.1875f;
                GlStateManager.scale((float)f3, (float)(-f3), (float)(-f3));
                if (flag) {
                    GlStateManager.translate((float)0.0f, (float)0.0625f, (float)0.0f);
                }
                GameProfile gameprofile = null;
                if (itemstack.hasTagCompound()) {
                    String s;
                    NBTTagCompound nbttagcompound = itemstack.getTagCompound();
                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.readGameProfileFromNBT((NBTTagCompound)nbttagcompound.getCompoundTag("SkullOwner"));
                    } else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty((String)(s = nbttagcompound.getString("SkullOwner")))) {
                        gameprofile = TileEntitySkull.updateGameprofile((GameProfile)new GameProfile((UUID)null, s));
                        nbttagcompound.setTag("SkullOwner", (NBTBase)NBTUtil.writeGameProfile((NBTTagCompound)new NBTTagCompound(), (GameProfile)gameprofile));
                    }
                }
                TileEntitySkullRenderer.instance.renderSkull(-0.5f, 0.0f, -0.5f, EnumFacing.UP, 180.0f, itemstack.getMetadata(), gameprofile, -1);
            }
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
