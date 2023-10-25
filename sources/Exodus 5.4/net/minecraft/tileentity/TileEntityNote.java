/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityNote
extends TileEntity {
    public byte note;
    public boolean previousRedstoneState;

    public void triggerNote(World world, BlockPos blockPos) {
        if (world.getBlockState(blockPos.up()).getBlock().getMaterial() == Material.air) {
            Material material = world.getBlockState(blockPos.down()).getBlock().getMaterial();
            int n = 0;
            if (material == Material.rock) {
                n = 1;
            }
            if (material == Material.sand) {
                n = 2;
            }
            if (material == Material.glass) {
                n = 3;
            }
            if (material == Material.wood) {
                n = 4;
            }
            world.addBlockEvent(blockPos, Blocks.noteblock, n, this.note);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        this.note = nBTTagCompound.getByte("note");
        this.note = (byte)MathHelper.clamp_int(this.note, 0, 24);
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        nBTTagCompound.setByte("note", this.note);
    }

    public void changePitch() {
        this.note = (byte)((this.note + 1) % 25);
        this.markDirty();
    }
}

