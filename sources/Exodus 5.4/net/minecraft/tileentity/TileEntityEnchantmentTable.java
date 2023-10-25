/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IInteractionObject;

public class TileEntityEnchantmentTable
extends TileEntity
implements IInteractionObject,
ITickable {
    public float pageFlip;
    public float bookRotation;
    public float bookSpread;
    private String customName;
    public int tickCount;
    public float pageFlipPrev;
    public float field_145929_l;
    private static Random rand = new Random();
    public float field_145932_k;
    public float bookSpreadPrev;
    public float bookRotationPrev;
    public float field_145924_q;

    @Override
    public void update() {
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        EntityPlayer entityPlayer = this.worldObj.getClosestPlayer((float)this.pos.getX() + 0.5f, (float)this.pos.getY() + 0.5f, (float)this.pos.getZ() + 0.5f, 3.0);
        if (entityPlayer != null) {
            double d = entityPlayer.posX - (double)((float)this.pos.getX() + 0.5f);
            double d2 = entityPlayer.posZ - (double)((float)this.pos.getZ() + 0.5f);
            this.field_145924_q = (float)MathHelper.func_181159_b(d2, d);
            this.bookSpread += 0.1f;
            if (this.bookSpread < 0.5f || rand.nextInt(40) == 0) {
                float f = this.field_145932_k;
                do {
                    this.field_145932_k += (float)(rand.nextInt(4) - rand.nextInt(4));
                } while (f == this.field_145932_k);
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
        float f = this.field_145924_q - this.bookRotation;
        while (f >= (float)Math.PI) {
            f -= (float)Math.PI * 2;
        }
        while (f < (float)(-Math.PI)) {
            f += (float)Math.PI * 2;
        }
        this.bookRotation += f * 0.4f;
        this.bookSpread = MathHelper.clamp_float(this.bookSpread, 0.0f, 1.0f);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float f2 = (this.field_145932_k - this.pageFlip) * 0.4f;
        float f3 = 0.2f;
        f2 = MathHelper.clamp_float(f2, -f3, f3);
        this.field_145929_l += (f2 - this.field_145929_l) * 0.9f;
        this.pageFlip += this.field_145929_l;
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }

    @Override
    public String getGuiID() {
        return "minecraft:enchanting_table";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.enchant";
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerEnchantment(inventoryPlayer, this.worldObj, this.pos);
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("CustomName", 8)) {
            this.customName = nBTTagCompound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        if (this.hasCustomName()) {
            nBTTagCompound.setString("CustomName", this.customName);
        }
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomName(String string) {
        this.customName = string;
    }
}

