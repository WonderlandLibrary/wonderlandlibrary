/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityMooshroom
extends EntityCow {
    public EntityMooshroom(World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 1.4f);
        this.spawnableBlock = Blocks.MYCELIUM;
    }

    public static void registerFixesMooshroom(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityMooshroom.class);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.BOWL && this.getGrowingAge() >= 0 && !player.capabilities.isCreativeMode) {
            itemstack.func_190918_g(1);
            if (itemstack.isEmpty()) {
                player.setHeldItem(hand, new ItemStack(Items.MUSHROOM_STEW));
            } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MUSHROOM_STEW))) {
                player.dropItem(new ItemStack(Items.MUSHROOM_STEW), false);
            }
            return true;
        }
        if (itemstack.getItem() == Items.SHEARS && this.getGrowingAge() >= 0) {
            this.setDead();
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + (double)(this.height / 2.0f), this.posZ, 0.0, 0.0, 0.0, new int[0]);
            if (!this.world.isRemote) {
                EntityCow entitycow = new EntityCow(this.world);
                entitycow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                entitycow.setHealth(this.getHealth());
                entitycow.renderYawOffset = this.renderYawOffset;
                if (this.hasCustomName()) {
                    entitycow.setCustomNameTag(this.getCustomNameTag());
                }
                this.world.spawnEntityInWorld(entitycow);
                for (int i = 0; i < 5; ++i) {
                    this.world.spawnEntityInWorld(new EntityItem(this.world, this.posX, this.posY + (double)this.height, this.posZ, new ItemStack(Blocks.RED_MUSHROOM)));
                }
                itemstack.damageItem(1, player);
                this.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0f, 1.0f);
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    @Override
    public EntityMooshroom createChild(EntityAgeable ageable) {
        return new EntityMooshroom(this.world);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_MUSHROOM_COW;
    }
}

