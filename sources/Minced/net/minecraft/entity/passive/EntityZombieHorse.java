// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityZombieHorse extends AbstractHorse
{
    public EntityZombieHorse(final World worldIn) {
        super(worldIn);
    }
    
    public static void registerFixesZombieHorse(final DataFixer fixer) {
        AbstractHorse.registerFixesAbstractHorse(fixer, EntityZombieHorse.class);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224);
        this.getEntityAttribute(EntityZombieHorse.JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.ENTITY_ZOMBIE_HORSE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.ENTITY_ZOMBIE_HORSE_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        super.getHurtSound(damageSourceIn);
        return SoundEvents.ENTITY_ZOMBIE_HORSE_HURT;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE_HORSE;
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        final boolean flag = !itemstack.isEmpty();
        if (flag && itemstack.getItem() == Items.SPAWN_EGG) {
            return super.processInteract(player, hand);
        }
        if (!this.isTame()) {
            return false;
        }
        if (this.isChild()) {
            return super.processInteract(player, hand);
        }
        if (player.isSneaking()) {
            this.openGUI(player);
            return true;
        }
        if (this.isBeingRidden()) {
            return super.processInteract(player, hand);
        }
        if (flag) {
            if (!this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE) {
                this.openGUI(player);
                return true;
            }
            if (itemstack.interactWithEntity(player, this, hand)) {
                return true;
            }
        }
        this.mountTo(player);
        return true;
    }
}
