// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Random;
import net.minecraft.world.IInteractionObject;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public class TileEntityEnchantmentTable extends TileEntity implements IUpdatePlayerListBox, IInteractionObject
{
    public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float field_145932_k;
    public float field_145929_l;
    public float bookSpread;
    public float bookSpreadPrev;
    public float bookRotation;
    public float bookRotationPrev;
    public float field_145924_q;
    private static Random field_145923_r;
    private String field_145922_s;
    private static final String __OBFID = "CL_00000354";
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.field_145922_s);
        }
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("CustomName", 8)) {
            this.field_145922_s = compound.getString("CustomName");
        }
    }
    
    @Override
    public void update() {
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        final EntityPlayer var1 = this.worldObj.getClosestPlayer(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, 3.0);
        if (var1 != null) {
            final double var2 = var1.posX - (this.pos.getX() + 0.5f);
            final double var3 = var1.posZ - (this.pos.getZ() + 0.5f);
            this.field_145924_q = (float)Math.atan2(var3, var2);
            this.bookSpread += 0.1f;
            if (this.bookSpread < 0.5f || TileEntityEnchantmentTable.field_145923_r.nextInt(40) == 0) {
                final float var4 = this.field_145932_k;
                do {
                    this.field_145932_k += TileEntityEnchantmentTable.field_145923_r.nextInt(4) - TileEntityEnchantmentTable.field_145923_r.nextInt(4);
                } while (var4 == this.field_145932_k);
            }
        }
        else {
            this.field_145924_q += 0.02f;
            this.bookSpread -= 0.1f;
        }
        while (this.bookRotation >= 3.1415927f) {
            this.bookRotation -= 6.2831855f;
        }
        while (this.bookRotation < -3.1415927f) {
            this.bookRotation += 6.2831855f;
        }
        while (this.field_145924_q >= 3.1415927f) {
            this.field_145924_q -= 6.2831855f;
        }
        while (this.field_145924_q < -3.1415927f) {
            this.field_145924_q += 6.2831855f;
        }
        float var5;
        for (var5 = this.field_145924_q - this.bookRotation; var5 >= 3.1415927f; var5 -= 6.2831855f) {}
        while (var5 < -3.1415927f) {
            var5 += 6.2831855f;
        }
        this.bookRotation += var5 * 0.4f;
        this.bookSpread = MathHelper.clamp_float(this.bookSpread, 0.0f, 1.0f);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float var6 = (this.field_145932_k - this.pageFlip) * 0.4f;
        final float var7 = 0.2f;
        var6 = MathHelper.clamp_float(var6, -var7, var7);
        this.field_145929_l += (var6 - this.field_145929_l) * 0.9f;
        this.pageFlip += this.field_145929_l;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_145922_s : "container.enchant";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.field_145922_s != null && this.field_145922_s.length() > 0;
    }
    
    public void func_145920_a(final String p_145920_1_) {
        this.field_145922_s = p_145920_1_;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerEnchantment(playerInventory, this.worldObj, this.pos);
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:enchanting_table";
    }
    
    static {
        TileEntityEnchantmentTable.field_145923_r = new Random();
    }
}
