/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityMooshroom
extends EntityCow {
    @Override
    public EntityMooshroom createChild(EntityAgeable entityAgeable) {
        return new EntityMooshroom(this.worldObj);
    }

    public EntityMooshroom(World world) {
        super(world);
        this.setSize(0.9f, 1.3f);
        this.spawnableBlock = Blocks.mycelium;
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (itemStack != null && itemStack.getItem() == Items.bowl && this.getGrowingAge() >= 0) {
            if (itemStack.stackSize == 1) {
                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, new ItemStack(Items.mushroom_stew));
                return true;
            }
            if (entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.mushroom_stew)) && !entityPlayer.capabilities.isCreativeMode) {
                entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
                return true;
            }
        }
        if (itemStack != null && itemStack.getItem() == Items.shears && this.getGrowingAge() >= 0) {
            this.setDead();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + (double)(this.height / 2.0f), this.posZ, 0.0, 0.0, 0.0, new int[0]);
            if (!this.worldObj.isRemote) {
                EntityCow entityCow = new EntityCow(this.worldObj);
                entityCow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                entityCow.setHealth(this.getHealth());
                entityCow.renderYawOffset = this.renderYawOffset;
                if (this.hasCustomName()) {
                    entityCow.setCustomNameTag(this.getCustomNameTag());
                }
                this.worldObj.spawnEntityInWorld(entityCow);
                int n = 0;
                while (n < 5) {
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY + (double)this.height, this.posZ, new ItemStack(Blocks.red_mushroom)));
                    ++n;
                }
                itemStack.damageItem(1, entityPlayer);
                this.playSound("mob.sheep.shear", 1.0f, 1.0f);
            }
            return true;
        }
        return super.interact(entityPlayer);
    }
}

