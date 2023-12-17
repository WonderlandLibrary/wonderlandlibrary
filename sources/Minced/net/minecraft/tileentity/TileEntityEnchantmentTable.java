// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Random;
import net.minecraft.world.IInteractionObject;
import net.minecraft.util.ITickable;

public class TileEntityEnchantmentTable extends TileEntity implements ITickable, IInteractionObject
{
    public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float flipT;
    public float flipA;
    public float bookSpread;
    public float bookSpreadPrev;
    public float bookRotation;
    public float bookRotationPrev;
    public float tRot;
    private static final Random rand;
    private String customName;
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        return compound;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }
    
    @Override
    public void update() {
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        final EntityPlayer entityplayer = this.world.getClosestPlayer(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, 3.0, false);
        if (entityplayer != null) {
            final double d0 = entityplayer.posX - (this.pos.getX() + 0.5f);
            final double d2 = entityplayer.posZ - (this.pos.getZ() + 0.5f);
            this.tRot = (float)MathHelper.atan2(d2, d0);
            this.bookSpread += 0.1f;
            if (this.bookSpread < 0.5f || TileEntityEnchantmentTable.rand.nextInt(40) == 0) {
                final float f1 = this.flipT;
                do {
                    this.flipT += TileEntityEnchantmentTable.rand.nextInt(4) - TileEntityEnchantmentTable.rand.nextInt(4);
                } while (f1 == this.flipT);
            }
        }
        else {
            this.tRot += 0.02f;
            this.bookSpread -= 0.1f;
        }
        while (this.bookRotation >= 3.1415927f) {
            this.bookRotation -= 6.2831855f;
        }
        while (this.bookRotation < -3.1415927f) {
            this.bookRotation += 6.2831855f;
        }
        while (this.tRot >= 3.1415927f) {
            this.tRot -= 6.2831855f;
        }
        while (this.tRot < -3.1415927f) {
            this.tRot += 6.2831855f;
        }
        float f2;
        for (f2 = this.tRot - this.bookRotation; f2 >= 3.1415927f; f2 -= 6.2831855f) {}
        while (f2 < -3.1415927f) {
            f2 += 6.2831855f;
        }
        this.bookRotation += f2 * 0.4f;
        this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0f, 1.0f);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float f3 = (this.flipT - this.pageFlip) * 0.4f;
        final float f4 = 0.2f;
        f3 = MathHelper.clamp(f3, -0.2f, 0.2f);
        this.flipA += (f3 - this.flipA) * 0.9f;
        this.pageFlip += this.flipA;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.enchant";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }
    
    public void setCustomName(final String customNameIn) {
        this.customName = customNameIn;
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerEnchantment(playerInventory, this.world, this.pos);
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:enchanting_table";
    }
    
    static {
        rand = new Random();
    }
}
