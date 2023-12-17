// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelShield;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityShulkerBox;

public class TileEntityItemStackRenderer
{
    private static final TileEntityShulkerBox[] SHULKER_BOXES;
    public static TileEntityItemStackRenderer instance;
    private final TileEntityChest chestBasic;
    private final TileEntityChest chestTrap;
    private final TileEntityEnderChest enderChest;
    private final TileEntityBanner banner;
    private final TileEntityBed bed;
    private final TileEntitySkull skull;
    private final ModelShield modelShield;
    
    public TileEntityItemStackRenderer() {
        this.chestBasic = new TileEntityChest(BlockChest.Type.BASIC);
        this.chestTrap = new TileEntityChest(BlockChest.Type.TRAP);
        this.enderChest = new TileEntityEnderChest();
        this.banner = new TileEntityBanner();
        this.bed = new TileEntityBed();
        this.skull = new TileEntitySkull();
        this.modelShield = new ModelShield();
    }
    
    public void renderByItem(final ItemStack itemStackIn) {
        this.renderByItem(itemStackIn, 1.0f);
    }
    
    public void renderByItem(final ItemStack itemStackIn, final float partialTicks) {
        final Item item = itemStackIn.getItem();
        if (item == Items.BANNER) {
            this.banner.setItemValues(itemStackIn, false);
            TileEntityRendererDispatcher.instance.render(this.banner, 0.0, 0.0, 0.0, 0.0f, partialTicks);
        }
        else if (item == Items.BED) {
            this.bed.setItemValues(itemStackIn);
            TileEntityRendererDispatcher.instance.render(this.bed, 0.0, 0.0, 0.0, 0.0f);
        }
        else if (item == Items.SHIELD) {
            if (itemStackIn.getSubCompound("BlockEntityTag") != null) {
                this.banner.setItemValues(itemStackIn, true);
                Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_DESIGNS.getResourceLocation(this.banner.getPatternResourceLocation(), this.banner.getPatternList(), this.banner.getColorList()));
            }
            else {
                Minecraft.getMinecraft().getTextureManager().bindTexture(BannerTextures.SHIELD_BASE_TEXTURE);
            }
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f, -1.0f, -1.0f);
            this.modelShield.render();
            GlStateManager.popMatrix();
        }
        else if (item == Items.SKULL) {
            GameProfile gameprofile = null;
            if (itemStackIn.hasTagCompound()) {
                final NBTTagCompound nbttagcompound = itemStackIn.getTagCompound();
                if (nbttagcompound.hasKey("SkullOwner", 10)) {
                    gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                }
                else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isBlank((CharSequence)nbttagcompound.getString("SkullOwner"))) {
                    final GameProfile gameprofile2 = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    gameprofile = TileEntitySkull.updateGameProfile(gameprofile2);
                    nbttagcompound.removeTag("SkullOwner");
                    nbttagcompound.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                }
            }
            if (TileEntitySkullRenderer.instance != null) {
                GlStateManager.pushMatrix();
                GlStateManager.disableCull();
                TileEntitySkullRenderer.instance.renderSkull(0.0f, 0.0f, 0.0f, EnumFacing.UP, 180.0f, itemStackIn.getMetadata(), gameprofile, -1, 0.0f);
                GlStateManager.enableCull();
                GlStateManager.popMatrix();
            }
        }
        else if (item == Item.getItemFromBlock(Blocks.ENDER_CHEST)) {
            TileEntityRendererDispatcher.instance.render(this.enderChest, 0.0, 0.0, 0.0, 0.0f, partialTicks);
        }
        else if (item == Item.getItemFromBlock(Blocks.TRAPPED_CHEST)) {
            TileEntityRendererDispatcher.instance.render(this.chestTrap, 0.0, 0.0, 0.0, 0.0f, partialTicks);
        }
        else if (Block.getBlockFromItem(item) instanceof BlockShulkerBox) {
            TileEntityRendererDispatcher.instance.render(TileEntityItemStackRenderer.SHULKER_BOXES[BlockShulkerBox.getColorFromItem(item).getMetadata()], 0.0, 0.0, 0.0, 0.0f, partialTicks);
        }
        else {
            TileEntityRendererDispatcher.instance.render(this.chestBasic, 0.0, 0.0, 0.0, 0.0f, partialTicks);
        }
    }
    
    static {
        SHULKER_BOXES = new TileEntityShulkerBox[16];
        for (final EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            TileEntityItemStackRenderer.SHULKER_BOXES[enumdyecolor.getMetadata()] = new TileEntityShulkerBox(enumdyecolor);
        }
        TileEntityItemStackRenderer.instance = new TileEntityItemStackRenderer();
    }
}
