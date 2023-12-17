// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;

public class TileEntityEnderChest extends TileEntity implements ITickable
{
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
    private int ticksSinceSync;
    
    @Override
    public void update() {
        if (++this.ticksSinceSync % 20 * 4 == 0) {
            this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
        }
        this.prevLidAngle = this.lidAngle;
        final int i = this.pos.getX();
        final int j = this.pos.getY();
        final int k = this.pos.getZ();
        final float f = 0.1f;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0f) {
            final double d0 = i + 0.5;
            final double d2 = k + 0.5;
            this.world.playSound(null, d0, j + 0.5, d2, SoundEvents.BLOCK_ENDERCHEST_OPEN, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
        }
        if ((this.numPlayersUsing == 0 && this.lidAngle > 0.0f) || (this.numPlayersUsing > 0 && this.lidAngle < 1.0f)) {
            final float f2 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1f;
            }
            else {
                this.lidAngle -= 0.1f;
            }
            if (this.lidAngle > 1.0f) {
                this.lidAngle = 1.0f;
            }
            final float f3 = 0.5f;
            if (this.lidAngle < 0.5f && f2 >= 0.5f) {
                final double d3 = i + 0.5;
                final double d4 = k + 0.5;
                this.world.playSound(null, d3, j + 0.5, d4, SoundEvents.BLOCK_ENDERCHEST_CLOSE, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (this.lidAngle < 0.0f) {
                this.lidAngle = 0.0f;
            }
        }
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    @Override
    public void invalidate() {
        this.updateContainingBlockInfo();
        super.invalidate();
    }
    
    public void openChest() {
        ++this.numPlayersUsing;
        this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
    }
    
    public void closeChest() {
        --this.numPlayersUsing;
        this.world.addBlockEvent(this.pos, Blocks.ENDER_CHEST, 1, this.numPlayersUsing);
    }
    
    public boolean canBeUsed(final EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
}
