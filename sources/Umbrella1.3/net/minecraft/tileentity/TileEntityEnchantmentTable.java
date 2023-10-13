/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IInteractionObject;

public class TileEntityEnchantmentTable
extends TileEntity
implements IUpdatePlayerListBox,
IInteractionObject {
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
    private static Random field_145923_r = new Random();
    private String field_145922_s;
    private static final String __OBFID = "CL_00000354";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.field_145922_s);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("CustomName", 8)) {
            this.field_145922_s = compound.getString("CustomName");
        }
    }

    @Override
    public void update() {
        float var7;
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        EntityPlayer var1 = this.worldObj.getClosestPlayer((float)this.pos.getX() + 0.5f, (float)this.pos.getY() + 0.5f, (float)this.pos.getZ() + 0.5f, 3.0);
        if (var1 != null) {
            double var2 = var1.posX - (double)((float)this.pos.getX() + 0.5f);
            double var4 = var1.posZ - (double)((float)this.pos.getZ() + 0.5f);
            this.field_145924_q = (float)Math.atan2(var4, var2);
            this.bookSpread += 0.1f;
            if (this.bookSpread < 0.5f || field_145923_r.nextInt(40) == 0) {
                float var6 = this.field_145932_k;
                do {
                    this.field_145932_k += (float)(field_145923_r.nextInt(4) - field_145923_r.nextInt(4));
                } while (var6 == this.field_145932_k);
            }
        } else {
            this.field_145924_q += 0.02f;
            this.bookSpread -= 0.1f;
        }
        while (this.bookRotation >= (float)Math.PI) {
            this.bookRotation -= (float)Math.PI * 2;
        }
        while (this.bookRotation < (float)(-Math.PI)) {
            this.bookRotation += (float)Math.PI * 2;
        }
        while (this.field_145924_q >= (float)Math.PI) {
            this.field_145924_q -= (float)Math.PI * 2;
        }
        while (this.field_145924_q < (float)(-Math.PI)) {
            this.field_145924_q += (float)Math.PI * 2;
        }
        for (var7 = this.field_145924_q - this.bookRotation; var7 >= (float)Math.PI; var7 -= (float)Math.PI * 2) {
        }
        while (var7 < (float)(-Math.PI)) {
            var7 += (float)Math.PI * 2;
        }
        this.bookRotation += var7 * 0.4f;
        this.bookSpread = MathHelper.clamp_float(this.bookSpread, 0.0f, 1.0f);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float var3 = (this.field_145932_k - this.pageFlip) * 0.4f;
        float var8 = 0.2f;
        var3 = MathHelper.clamp_float(var3, -var8, var8);
        this.field_145929_l += (var3 - this.field_145929_l) * 0.9f;
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

    public void func_145920_a(String p_145920_1_) {
        this.field_145922_s = p_145920_1_;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerEnchantment(playerInventory, this.worldObj, this.pos);
    }

    @Override
    public String getGuiID() {
        return "minecraft:enchanting_table";
    }
}

