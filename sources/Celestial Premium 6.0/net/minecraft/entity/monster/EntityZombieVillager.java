/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombieVillager
extends EntityZombie {
    private static final DataParameter<Boolean> CONVERTING = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> field_190739_c = EntityDataManager.createKey(EntityZombieVillager.class, DataSerializers.VARINT);
    private int conversionTime;
    private UUID field_191992_by;

    public EntityZombieVillager(World p_i47277_1_) {
        super(p_i47277_1_);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(CONVERTING, false);
        this.dataManager.register(field_190739_c, 0);
    }

    public void func_190733_a(int p_190733_1_) {
        this.dataManager.set(field_190739_c, p_190733_1_);
    }

    public int func_190736_dl() {
        return Math.max(this.dataManager.get(field_190739_c) % 6, 0);
    }

    public static void func_190737_b(DataFixer p_190737_0_) {
        EntityLiving.registerFixesMob(p_190737_0_, EntityZombieVillager.class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Profession", this.func_190736_dl());
        compound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        if (this.field_191992_by != null) {
            compound.setUniqueId("ConversionPlayer", this.field_191992_by);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.func_190733_a(compound.getInteger("Profession"));
        if (compound.hasKey("ConversionTime", 99) && compound.getInteger("ConversionTime") > -1) {
            this.func_191991_a(compound.hasUniqueId("ConversionPlayer") ? compound.getUniqueId("ConversionPlayer") : null, compound.getInteger("ConversionTime"));
        }
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        this.func_190733_a(this.world.rand.nextInt(6));
        return super.onInitialSpawn(difficulty, livingdata);
    }

    @Override
    public void onUpdate() {
        if (!this.world.isRemote && this.isConverting()) {
            int i = this.func_190735_dq();
            this.conversionTime -= i;
            if (this.conversionTime <= 0) {
                this.func_190738_dp();
            }
        }
        super.onUpdate();
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.GOLDEN_APPLE && itemstack.getMetadata() == 0 && this.isPotionActive(MobEffects.WEAKNESS)) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            if (!this.world.isRemote) {
                this.func_191991_a(player.getUniqueID(), this.rand.nextInt(2401) + 3600);
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
        return this.getDataManager().get(CONVERTING);
    }

    protected void func_191991_a(@Nullable UUID p_191991_1_, int p_191991_2_) {
        this.field_191992_by = p_191991_1_;
        this.conversionTime = p_191991_2_;
        this.getDataManager().set(CONVERTING, true);
        this.removePotionEffect(MobEffects.WEAKNESS);
        this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, p_191991_2_, Math.min(this.world.getDifficulty().getDifficultyId() - 1, 0)));
        this.world.setEntityState(this, (byte)16);
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 16) {
            if (!this.isSilent()) {
                this.world.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundCategory(), 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
        } else {
            super.handleStatusUpdate(id);
        }
    }

    protected void func_190738_dp() {
        EntityPlayer entityplayer;
        EntityVillager entityvillager = new EntityVillager(this.world);
        entityvillager.copyLocationAndAnglesFrom(this);
        entityvillager.setProfession(this.func_190736_dl());
        entityvillager.func_190672_a(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), null, false);
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
        this.world.spawnEntityInWorld(entityvillager);
        if (this.field_191992_by != null && (entityplayer = this.world.getPlayerEntityByUUID(this.field_191992_by)) instanceof EntityPlayerMP) {
            CriteriaTriggers.field_192137_q.func_192183_a((EntityPlayerMP)entityplayer, this, entityvillager);
        }
        entityvillager.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
        this.world.playEvent(null, 1027, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
    }

    protected int func_190735_dq() {
        int i = 1;
        if (this.rand.nextFloat() < 0.01f) {
            int j = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int k = (int)this.posX - 4; k < (int)this.posX + 4 && j < 14; ++k) {
                for (int l = (int)this.posY - 4; l < (int)this.posY + 4 && j < 14; ++l) {
                    for (int i1 = (int)this.posZ - 4; i1 < (int)this.posZ + 4 && j < 14; ++i1) {
                        Block block = this.world.getBlockState(blockpos$mutableblockpos.setPos(k, l, i1)).getBlock();
                        if (block != Blocks.IRON_BARS && block != Blocks.BED) continue;
                        if (this.rand.nextFloat() < 0.3f) {
                            ++i;
                        }
                        ++j;
                    }
                }
            }
        }
        return i;
    }

    @Override
    protected float getSoundPitch() {
        return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 2.0f : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_AMBIENT;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_DEATH;
    }

    @Override
    public SoundEvent func_190731_di() {
        return SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.field_191183_as;
    }

    @Override
    protected ItemStack func_190732_dj() {
        return ItemStack.field_190927_a;
    }
}

