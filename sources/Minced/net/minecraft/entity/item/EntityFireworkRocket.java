// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.MOVEMENT.SunElytra;
import ru.tuskevich.Minced;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.Entity;

public class EntityFireworkRocket extends Entity
{
    private static final DataParameter<ItemStack> FIREWORK_ITEM;
    private static final DataParameter<Integer> BOOSTED_ENTITY_ID;
    private int fireworkAge;
    private int lifetime;
    private EntityLivingBase boostedEntity;
    
    public EntityFireworkRocket(final World worldIn) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
    }
    
    @Override
    protected void entityInit() {
        this.dataManager.register(EntityFireworkRocket.FIREWORK_ITEM, ItemStack.EMPTY);
        this.dataManager.register(EntityFireworkRocket.BOOSTED_ENTITY_ID, 0);
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        return distance < 4096.0 && !this.isAttachedToEntity();
    }
    
    @Override
    public boolean isInRangeToRender3d(final double x, final double y, final double z) {
        return super.isInRangeToRender3d(x, y, z) && !this.isAttachedToEntity();
    }
    
    public EntityFireworkRocket(final World worldIn, final double x, final double y, final double z, final ItemStack givenItem) {
        super(worldIn);
        this.fireworkAge = 0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(x, y, z);
        int i = 1;
        if (!givenItem.isEmpty() && givenItem.hasTagCompound()) {
            this.dataManager.set(EntityFireworkRocket.FIREWORK_ITEM, givenItem.copy());
            final NBTTagCompound nbttagcompound = givenItem.getTagCompound();
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Fireworks");
            i += nbttagcompound2.getByte("Flight");
        }
        this.motionX = this.rand.nextGaussian() * 0.001;
        this.motionZ = this.rand.nextGaussian() * 0.001;
        this.motionY = 0.05;
        this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
    }
    
    public EntityFireworkRocket(final World p_i47367_1_, final ItemStack p_i47367_2_, final EntityLivingBase p_i47367_3_) {
        this(p_i47367_1_, p_i47367_3_.posX, p_i47367_3_.posY, p_i47367_3_.posZ, p_i47367_2_);
        this.dataManager.set(EntityFireworkRocket.BOOSTED_ENTITY_ID, p_i47367_3_.getEntityId());
        this.boostedEntity = p_i47367_3_;
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt(x * x + z * z);
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
            this.rotationPitch = (float)(MathHelper.atan2(y, f) * 57.29577951308232);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
    }
    
    @Override
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        if (this.isAttachedToEntity()) {
            if (this.boostedEntity == null) {
                final Entity entity = this.world.getEntityByID(this.dataManager.get(EntityFireworkRocket.BOOSTED_ENTITY_ID));
                if (entity instanceof EntityLivingBase) {
                    this.boostedEntity = (EntityLivingBase)entity;
                }
            }
            if (this.boostedEntity != null) {
                if (this.boostedEntity.isElytraFlying()) {
                    final Vec3d vec3d = this.boostedEntity.getLookVec();
                    final double d0 = 1.5;
                    final double d2 = 0.1;
                    final EntityLivingBase boostedEntity = this.boostedEntity;
                    boostedEntity.motionX += vec3d.x * 0.1 + (vec3d.x * 1.5 - this.boostedEntity.motionX) * 0.5;
                    final EntityLivingBase boostedEntity2 = this.boostedEntity;
                    boostedEntity2.motionY += vec3d.y * 0.1 + (vec3d.y * 1.5 - this.boostedEntity.motionY) * 0.5;
                    final EntityLivingBase boostedEntity3 = this.boostedEntity;
                    boostedEntity3.motionZ += vec3d.z * 0.1 + (vec3d.z * 1.5 - this.boostedEntity.motionZ) * 0.5;
                }
                this.setPosition(this.boostedEntity.posX, this.boostedEntity.posY, this.boostedEntity.posZ);
                this.motionX = this.boostedEntity.motionX;
                this.motionY = this.boostedEntity.motionY;
                this.motionZ = this.boostedEntity.motionZ;
            }
        }
        else {
            this.motionX *= 1.15;
            this.motionZ *= 1.15;
            this.motionY += 0.04;
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }
        final float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 57.29577951308232);
        while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
            this.prevRotationPitch -= 360.0f;
        }
        while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
            this.prevRotationPitch += 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
            this.prevRotationYaw -= 360.0f;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
            this.prevRotationYaw += 360.0f;
        }
        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2f;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2f;
        if (this.fireworkAge == 0 && !this.isSilent() && !Minced.getInstance().manager.getModule(SunElytra.class).state) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_FIREWORK_LAUNCH, SoundCategory.AMBIENT, 3.0f, 1.0f);
        }
        ++this.fireworkAge;
        if (this.world.isRemote && this.fireworkAge % 2 < 2) {
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3, this.posZ, this.rand.nextGaussian() * 0.05, -this.motionY * 0.5, this.rand.nextGaussian() * 0.05, new int[0]);
        }
        if (!this.world.isRemote && this.fireworkAge > this.lifetime) {
            this.world.setEntityState(this, (byte)17);
            this.dealExplosionDamage();
            this.setDead();
        }
    }
    
    private void dealExplosionDamage() {
        float f = 0.0f;
        final ItemStack itemstack = this.dataManager.get(EntityFireworkRocket.FIREWORK_ITEM);
        final NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
        final NBTTagList nbttaglist = (nbttagcompound != null) ? nbttagcompound.getTagList("Explosions", 10) : null;
        if (nbttaglist != null && !nbttaglist.isEmpty()) {
            f = (float)(5 + nbttaglist.tagCount() * 2);
        }
        if (f > 0.0f) {
            if (this.boostedEntity != null) {
                this.boostedEntity.attackEntityFrom(DamageSource.FIREWORKS, (float)(5 + nbttaglist.tagCount() * 2));
            }
            final double d0 = 5.0;
            final Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
            for (final EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, this.getEntityBoundingBox().grow(5.0))) {
                if (entitylivingbase != this.boostedEntity && this.getDistanceSq(entitylivingbase) <= 25.0) {
                    boolean flag = false;
                    for (int i = 0; i < 2; ++i) {
                        final RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, new Vec3d(entitylivingbase.posX, entitylivingbase.posY + entitylivingbase.height * 0.5 * i, entitylivingbase.posZ), false, true, false);
                        if (raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        continue;
                    }
                    final float f2 = f * (float)Math.sqrt((5.0 - this.getDistance(entitylivingbase)) / 5.0);
                    entitylivingbase.attackEntityFrom(DamageSource.FIREWORKS, f2);
                }
            }
        }
    }
    
    public boolean isAttachedToEntity() {
        return this.dataManager.get(EntityFireworkRocket.BOOSTED_ENTITY_ID) > 0;
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 17 && this.world.isRemote) {
            final ItemStack itemstack = this.dataManager.get(EntityFireworkRocket.FIREWORK_ITEM);
            final NBTTagCompound nbttagcompound = itemstack.isEmpty() ? null : itemstack.getSubCompound("Fireworks");
            this.world.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, nbttagcompound);
        }
        super.handleStatusUpdate(id);
    }
    
    public static void registerFixesFireworkRocket(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(EntityFireworkRocket.class, new String[] { "FireworksItem" }));
    }
    
    public void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setInteger("Life", this.fireworkAge);
        compound.setInteger("LifeTime", this.lifetime);
        final ItemStack itemstack = this.dataManager.get(EntityFireworkRocket.FIREWORK_ITEM);
        if (!itemstack.isEmpty()) {
            compound.setTag("FireworksItem", itemstack.writeToNBT(new NBTTagCompound()));
        }
    }
    
    public void readEntityFromNBT(final NBTTagCompound compound) {
        this.fireworkAge = compound.getInteger("Life");
        this.lifetime = compound.getInteger("LifeTime");
        final NBTTagCompound nbttagcompound = compound.getCompoundTag("FireworksItem");
        if (nbttagcompound != null) {
            final ItemStack itemstack = new ItemStack(nbttagcompound);
            if (!itemstack.isEmpty()) {
                this.dataManager.set(EntityFireworkRocket.FIREWORK_ITEM, itemstack);
            }
        }
    }
    
    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }
    
    static {
        FIREWORK_ITEM = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.ITEM_STACK);
        BOOSTED_ENTITY_ID = EntityDataManager.createKey(EntityFireworkRocket.class, DataSerializers.VARINT);
    }
}
