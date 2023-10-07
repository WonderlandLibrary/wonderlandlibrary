// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public class TileEntityEnderChest extends TileEntity implements IUpdatePlayerListBox
{
    public float field_145972_a;
    public float prevLidAngle;
    public int field_145973_j;
    private int field_145974_k;
    private static final String __OBFID = "CL_00000355";
    
    @Override
    public void update() {
        if (++this.field_145974_k % 20 * 4 == 0) {
            this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.field_145973_j);
        }
        this.prevLidAngle = this.field_145972_a;
        final int var1 = this.pos.getX();
        final int var2 = this.pos.getY();
        final int var3 = this.pos.getZ();
        final float var4 = 0.1f;
        if (this.field_145973_j > 0 && this.field_145972_a == 0.0f) {
            final double var5 = var1 + 0.5;
            final double var6 = var3 + 0.5;
            this.worldObj.playSoundEffect(var5, var2 + 0.5, var6, "random.chestopen", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }
        if ((this.field_145973_j == 0 && this.field_145972_a > 0.0f) || (this.field_145973_j > 0 && this.field_145972_a < 1.0f)) {
            final float var7 = this.field_145972_a;
            if (this.field_145973_j > 0) {
                this.field_145972_a += var4;
            }
            else {
                this.field_145972_a -= var4;
            }
            if (this.field_145972_a > 1.0f) {
                this.field_145972_a = 1.0f;
            }
            final float var8 = 0.5f;
            if (this.field_145972_a < var8 && var7 >= var8) {
                final double var6 = var1 + 0.5;
                final double var9 = var3 + 0.5;
                this.worldObj.playSoundEffect(var6, var2 + 0.5, var9, "random.chestclosed", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
            }
            if (this.field_145972_a < 0.0f) {
                this.field_145972_a = 0.0f;
            }
        }
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            this.field_145973_j = type;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    @Override
    public void invalidate() {
        this.updateContainingBlockInfo();
        super.invalidate();
    }
    
    public void func_145969_a() {
        ++this.field_145973_j;
        this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.field_145973_j);
    }
    
    public void func_145970_b() {
        --this.field_145973_j;
        this.worldObj.addBlockEvent(this.pos, Blocks.ender_chest, 1, this.field_145973_j);
    }
    
    public boolean func_145971_a(final EntityPlayer p_145971_1_) {
        return this.worldObj.getTileEntity(this.pos) == this && p_145971_1_.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
}
