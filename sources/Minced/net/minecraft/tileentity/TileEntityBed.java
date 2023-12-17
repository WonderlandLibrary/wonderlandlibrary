// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.init.Items;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumDyeColor;

public class TileEntityBed extends TileEntity
{
    private EnumDyeColor color;
    
    public TileEntityBed() {
        this.color = EnumDyeColor.RED;
    }
    
    public void setItemValues(final ItemStack p_193051_1_) {
        this.setColor(EnumDyeColor.byMetadata(p_193051_1_.getMetadata()));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("color")) {
            this.color = EnumDyeColor.byMetadata(compound.getInteger("color"));
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("color", this.color.getMetadata());
        return compound;
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 11, this.getUpdateTag());
    }
    
    public EnumDyeColor getColor() {
        return this.color;
    }
    
    public void setColor(final EnumDyeColor color) {
        this.color = color;
        this.markDirty();
    }
    
    public boolean isHeadPiece() {
        return BlockBed.isHeadPiece(this.getBlockMetadata());
    }
    
    public ItemStack getItemStack() {
        return new ItemStack(Items.BED, 1, this.color.getMetadata());
    }
}
