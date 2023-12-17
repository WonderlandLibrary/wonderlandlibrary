// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import java.util.List;
import net.minecraft.potion.PotionType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.base.Predicate;
import org.apache.logging.log4j.Logger;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;

public class EntityPotion extends EntityThrowable
{
    private static final DataParameter<ItemStack> ITEM;
    private static final Logger LOGGER;
    public static final Predicate<EntityLivingBase> WATER_SENSITIVE;
    
    public EntityPotion(final World worldIn) {
        super(worldIn);
    }
    
    public EntityPotion(final World worldIn, final EntityLivingBase throwerIn, final ItemStack potionDamageIn) {
        super(worldIn, throwerIn);
        this.setItem(potionDamageIn);
    }
    
    public EntityPotion(final World worldIn, final double x, final double y, final double z, final ItemStack potionDamageIn) {
        super(worldIn, x, y, z);
        if (!potionDamageIn.isEmpty()) {
            this.setItem(potionDamageIn);
        }
    }
    
    @Override
    protected void entityInit() {
        this.getDataManager().register(EntityPotion.ITEM, ItemStack.EMPTY);
    }
    
    public ItemStack getPotion() {
        final ItemStack itemstack = this.getDataManager().get(EntityPotion.ITEM);
        if (itemstack.getItem() != Items.SPLASH_POTION && itemstack.getItem() != Items.LINGERING_POTION) {
            if (this.world != null) {
                EntityPotion.LOGGER.error("ThrownPotion entity {} has no item?!", (Object)this.getEntityId());
            }
            return new ItemStack(Items.SPLASH_POTION);
        }
        return itemstack;
    }
    
    public void setItem(final ItemStack stack) {
        this.getDataManager().set(EntityPotion.ITEM, stack);
        this.getDataManager().setDirty(EntityPotion.ITEM);
    }
    
    @Override
    public float getGravityVelocity() {
        return 0.05f;
    }
    
    @Override
    protected void onImpact(final RayTraceResult result) {
        if (!this.world.isRemote) {
            final ItemStack itemstack = this.getPotion();
            final PotionType potiontype = PotionUtils.getPotionFromItem(itemstack);
            final List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);
            final boolean flag = potiontype == PotionTypes.WATER && list.isEmpty();
            if (result.typeOfHit == RayTraceResult.Type.BLOCK && flag) {
                final BlockPos blockpos = result.getBlockPos().offset(result.sideHit);
                this.extinguishFires(blockpos, result.sideHit);
                for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                    this.extinguishFires(blockpos.offset(enumfacing), enumfacing);
                }
            }
            if (flag) {
                this.applyWater();
            }
            else if (!list.isEmpty()) {
                if (this.isLingering()) {
                    this.makeAreaOfEffectCloud(itemstack, potiontype);
                }
                else {
                    this.applySplash(result, list);
                }
            }
            final int i = potiontype.hasInstantEffect() ? 2007 : 2002;
            this.world.playEvent(i, new BlockPos(this), PotionUtils.getColor(itemstack));
            this.setDead();
        }
    }
    
    private void applyWater() {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0, 2.0, 4.0);
        final List<EntityLivingBase> list = this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, axisalignedbb, (com.google.common.base.Predicate<? super EntityLivingBase>)EntityPotion.WATER_SENSITIVE);
        if (!list.isEmpty()) {
            for (final EntityLivingBase entitylivingbase : list) {
                final double d0 = this.getDistanceSq(entitylivingbase);
                if (d0 < 16.0 && isWaterSensitiveEntity(entitylivingbase)) {
                    entitylivingbase.attackEntityFrom(DamageSource.DROWN, 1.0f);
                }
            }
        }
    }
    
    private void applySplash(final RayTraceResult p_190543_1_, final List<PotionEffect> p_190543_2_) {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().grow(4.0, 2.0, 4.0);
        final List<EntityLivingBase> list = this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, axisalignedbb);
        if (!list.isEmpty()) {
            for (final EntityLivingBase entitylivingbase : list) {
                if (entitylivingbase.canBeHitWithPotion()) {
                    final double d0 = this.getDistanceSq(entitylivingbase);
                    if (d0 >= 16.0) {
                        continue;
                    }
                    double d2 = 1.0 - Math.sqrt(d0) / 4.0;
                    if (entitylivingbase == p_190543_1_.entityHit) {
                        d2 = 1.0;
                    }
                    for (final PotionEffect potioneffect : p_190543_2_) {
                        final Potion potion = potioneffect.getPotion();
                        if (potion.isInstant()) {
                            potion.affectEntity(this, this.getThrower(), entitylivingbase, potioneffect.getAmplifier(), d2);
                        }
                        else {
                            final int i = (int)(d2 * potioneffect.getDuration() + 0.5);
                            if (i <= 20) {
                                continue;
                            }
                            entitylivingbase.addPotionEffect(new PotionEffect(potion, i, potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
                        }
                    }
                }
            }
        }
    }
    
    private void makeAreaOfEffectCloud(final ItemStack p_190542_1_, final PotionType p_190542_2_) {
        final EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
        entityareaeffectcloud.setOwner(this.getThrower());
        entityareaeffectcloud.setRadius(3.0f);
        entityareaeffectcloud.setRadiusOnUse(-0.5f);
        entityareaeffectcloud.setWaitTime(10);
        entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / entityareaeffectcloud.getDuration());
        entityareaeffectcloud.setPotion(p_190542_2_);
        for (final PotionEffect potioneffect : PotionUtils.getFullEffectsFromItem(p_190542_1_)) {
            entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
        }
        final NBTTagCompound nbttagcompound = p_190542_1_.getTagCompound();
        if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99)) {
            entityareaeffectcloud.setColor(nbttagcompound.getInteger("CustomPotionColor"));
        }
        this.world.spawnEntity(entityareaeffectcloud);
    }
    
    private boolean isLingering() {
        return this.getPotion().getItem() == Items.LINGERING_POTION;
    }
    
    private void extinguishFires(final BlockPos pos, final EnumFacing p_184542_2_) {
        if (this.world.getBlockState(pos).getBlock() == Blocks.FIRE) {
            this.world.extinguishFire(null, pos.offset(p_184542_2_), p_184542_2_.getOpposite());
        }
    }
    
    public static void registerFixesPotion(final DataFixer fixer) {
        EntityThrowable.registerFixesThrowable(fixer, "ThrownPotion");
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityPotion.class, new String[] { "Potion" }));
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        final ItemStack itemstack = new ItemStack(compound.getCompoundTag("Potion"));
        if (itemstack.isEmpty()) {
            this.setDead();
        }
        else {
            this.setItem(itemstack);
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        final ItemStack itemstack = this.getPotion();
        if (!itemstack.isEmpty()) {
            compound.setTag("Potion", itemstack.writeToNBT(new NBTTagCompound()));
        }
    }
    
    private static boolean isWaterSensitiveEntity(final EntityLivingBase p_190544_0_) {
        return p_190544_0_ instanceof EntityEnderman || p_190544_0_ instanceof EntityBlaze;
    }
    
    static {
        ITEM = EntityDataManager.createKey(EntityPotion.class, DataSerializers.ITEM_STACK);
        LOGGER = LogManager.getLogger();
        WATER_SENSITIVE = (Predicate)new Predicate<EntityLivingBase>() {
            public boolean apply(@Nullable final EntityLivingBase p_apply_1_) {
                return isWaterSensitiveEntity(p_apply_1_);
            }
        };
    }
}
