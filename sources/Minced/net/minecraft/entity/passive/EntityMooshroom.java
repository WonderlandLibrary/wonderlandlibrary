// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityMooshroom extends EntityCow
{
    public EntityMooshroom(final World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 1.4f);
        this.spawnableBlock = Blocks.MYCELIUM;
    }
    
    public static void registerFixesMooshroom(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityMooshroom.class);
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.BOWL && this.getGrowingAge() >= 0 && !player.capabilities.isCreativeMode) {
            itemstack.shrink(1);
            if (itemstack.isEmpty()) {
                player.setHeldItem(hand, new ItemStack(Items.MUSHROOM_STEW));
            }
            else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.MUSHROOM_STEW))) {
                player.dropItem(new ItemStack(Items.MUSHROOM_STEW), false);
            }
            return true;
        }
        if (itemstack.getItem() == Items.SHEARS && this.getGrowingAge() >= 0) {
            this.setDead();
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY + this.height / 2.0f, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            if (!this.world.isRemote) {
                final EntityCow entitycow = new EntityCow(this.world);
                entitycow.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                entitycow.setHealth(this.getHealth());
                entitycow.renderYawOffset = this.renderYawOffset;
                if (this.hasCustomName()) {
                    entitycow.setCustomNameTag(this.getCustomNameTag());
                }
                this.world.spawnEntity(entitycow);
                for (int i = 0; i < 5; ++i) {
                    this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY + this.height, this.posZ, new ItemStack(Blocks.RED_MUSHROOM)));
                }
                itemstack.damageItem(1, player);
                this.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0f, 1.0f);
            }
            return true;
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    public EntityMooshroom createChild(final EntityAgeable ageable) {
        return new EntityMooshroom(this.world);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_MUSHROOM_COW;
    }
}
