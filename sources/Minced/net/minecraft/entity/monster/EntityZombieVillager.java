// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.init.MobEffects;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import java.util.UUID;
import net.minecraft.network.datasync.DataParameter;

public class EntityZombieVillager extends EntityZombie
{
    private static final DataParameter<Boolean> CONVERTING;
    private static final DataParameter<Integer> PROFESSION;
    private int conversionTime;
    private UUID converstionStarter;
    
    public EntityZombieVillager(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityZombieVillager.CONVERTING, false);
        this.dataManager.register(EntityZombieVillager.PROFESSION, 0);
    }
    
    public void setProfession(final int profession) {
        this.dataManager.set(EntityZombieVillager.PROFESSION, profession);
    }
    
    public int getProfession() {
        return Math.max(this.dataManager.get(EntityZombieVillager.PROFESSION) % 6, 0);
    }
    
    public static void registerFixesZombieVillager(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityZombieVillager.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Profession", this.getProfession());
        compound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        if (this.converstionStarter != null) {
            compound.setUniqueId("ConversionPlayer", this.converstionStarter);
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setProfession(compound.getInteger("Profession"));
        if (compound.hasKey("ConversionTime", 99) && compound.getInteger("ConversionTime") > -1) {
            this.startConverting(compound.hasUniqueId("ConversionPlayer") ? compound.getUniqueId("ConversionPlayer") : null, compound.getInteger("ConversionTime"));
        }
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        this.setProfession(this.world.rand.nextInt(6));
        return super.onInitialSpawn(difficulty, livingdata);
    }
    
    @Override
    public void onUpdate() {
        if (!this.world.isRemote && this.isConverting()) {
            final int i = this.getConversionProgress();
            this.conversionTime -= i;
            if (this.conversionTime <= 0) {
                this.finishConversion();
            }
        }
        super.onUpdate();
    }
    
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.GOLDEN_APPLE && itemstack.getMetadata() == 0 && this.isPotionActive(MobEffects.WEAKNESS)) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            if (!this.world.isRemote) {
                this.startConverting(player.getUniqueID(), this.rand.nextInt(2401) + 3600);
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean canDespawn() {
        return !this.isConverting();
    }
    
    public boolean isConverting() {
        return this.getDataManager().get(EntityZombieVillager.CONVERTING);
    }
    
    protected void startConverting(@Nullable final UUID conversionStarterIn, final int conversionTimeIn) {
        this.converstionStarter = conversionStarterIn;
        this.conversionTime = conversionTimeIn;
        this.getDataManager().set(EntityZombieVillager.CONVERTING, true);
        this.removePotionEffect(MobEffects.WEAKNESS);
        this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, conversionTimeIn, Math.min(this.world.getDifficulty().getId() - 1, 0)));
        this.world.setEntityState(this, (byte)16);
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 16) {
            if (!this.isSilent()) {
                this.world.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundCategory(), 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    protected void finishConversion() {
        final EntityVillager entityvillager = new EntityVillager(this.world);
        entityvillager.copyLocationAndAnglesFrom(this);
        entityvillager.setProfession(this.getProfession());
        entityvillager.finalizeMobSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), null, false);
        entityvillager.setLookingForHome();
        if (this.isChild()) {
            entityvillager.setGrowingAge(-24000);
        }
        this.world.removeEntity(this);
        entityvillager.setNoAI(this.isAIDisabled());
        if (this.hasCustomName()) {
            entityvillager.setCustomNameTag(this.getCustomNameTag());
            entityvillager.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
        }
        this.world.spawnEntity(entityvillager);
        if (this.converstionStarter != null) {
            final EntityPlayer entityplayer = this.world.getPlayerEntityByUUID(this.converstionStarter);
            if (entityplayer instanceof EntityPlayerMP) {
                CriteriaTriggers.CURED_ZOMBIE_VILLAGER.trigger((EntityPlayerMP)entityplayer, this, entityvillager);
            }
        }
        entityvillager.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
        this.world.playEvent(null, 1027, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
    }
    
    protected int getConversionProgress() {
        int i = 1;
        if (this.rand.nextFloat() < 0.01f) {
            int j = 0;
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k = (int)this.posX - 4; k < (int)this.posX + 4 && j < 14; ++k) {
                for (int l = (int)this.posY - 4; l < (int)this.posY + 4 && j < 14; ++l) {
                    for (int i2 = (int)this.posZ - 4; i2 < (int)this.posZ + 4 && j < 14; ++i2) {
                        final Block block = this.world.getBlockState(blockpos$mutableblockpos.setPos(k, l, i2)).getBlock();
                        if (block == Blocks.IRON_BARS || block == Blocks.BED) {
                            if (this.rand.nextFloat() < 0.3f) {
                                ++i;
                            }
                            ++j;
                        }
                    }
                }
            }
        }
        return i;
    }
    
    @Override
    protected float getSoundPitch() {
        return this.isChild() ? ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 2.0f) : ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
    }
    
    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT;
    }
    
    public SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT;
    }
    
    public SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH;
    }
    
    public SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE_VILLAGER;
    }
    
    @Override
    protected ItemStack getSkullDrop() {
        return ItemStack.EMPTY;
    }
    
    static {
        CONVERTING = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.BOOLEAN);
        PROFESSION = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.VARINT);
    }
}
