/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityFishHook
extends Entity {
    private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager.createKey(EntityFishHook.class, DataSerializers.VARINT);
    private boolean inGround;
    private int ticksInGround;
    private EntityPlayer angler;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    public Entity caughtEntity;
    private State field_190627_av = State.FLYING;
    private int field_191518_aw;
    private int field_191519_ax;

    public EntityFishHook(World p_i47290_1_, EntityPlayer p_i47290_2_, double p_i47290_3_, double p_i47290_5_, double p_i47290_7_) {
        super(p_i47290_1_);
        this.func_190626_a(p_i47290_2_);
        this.setPosition(p_i47290_3_, p_i47290_5_, p_i47290_7_);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    public EntityFishHook(World worldIn, EntityPlayer fishingPlayer) {
        super(worldIn);
        this.func_190626_a(fishingPlayer);
        this.func_190620_n();
    }

    private void func_190626_a(EntityPlayer p_190626_1_) {
        this.setSize(0.25f, 0.25f);
        this.ignoreFrustumCheck = true;
        this.angler = p_190626_1_;
        this.angler.fishEntity = this;
    }

    public void func_191516_a(int p_191516_1_) {
        this.field_191519_ax = p_191516_1_;
    }

    public void func_191517_b(int p_191517_1_) {
        this.field_191518_aw = p_191517_1_;
    }

    private void func_190620_n() {
        float f = this.angler.prevRotationPitch + (this.angler.rotationPitch - this.angler.prevRotationPitch);
        float f1 = this.angler.prevRotationYaw + (this.angler.rotationYaw - this.angler.prevRotationYaw);
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180));
        double d0 = this.angler.prevPosX + (this.angler.posX - this.angler.prevPosX) - (double)f3 * 0.3;
        double d1 = this.angler.prevPosY + (this.angler.posY - this.angler.prevPosY) + (double)this.angler.getEyeHeight();
        double d2 = this.angler.prevPosZ + (this.angler.posZ - this.angler.prevPosZ) - (double)f2 * 0.3;
        this.setLocationAndAngles(d0, d1, d2, f1, f);
        this.motionX = -f3;
        this.motionY = MathHelper.clamp(-(f5 / f4), -5.0f, 5.0f);
        this.motionZ = -f2;
        float f6 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX *= 0.6 / (double)f6 + 0.5 + this.rand.nextGaussian() * 0.0045;
        this.motionY *= 0.6 / (double)f6 + 0.5 + this.rand.nextGaussian() * 0.0045;
        this.motionZ *= 0.6 / (double)f6 + 0.5 + this.rand.nextGaussian() * 0.0045;
        float f7 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f7) * 57.29577951308232);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(DATA_HOOKED_ENTITY, 0);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (DATA_HOOKED_ENTITY.equals(key)) {
            int i = this.getDataManager().get(DATA_HOOKED_ENTITY);
            this.caughtEntity = i > 0 ? this.world.getEntityByID(i - 1) : null;
        }
        super.notifyDataManagerChange(key);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = 64.0;
        return distance < 4096.0;
    }

    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.angler == null) {
            this.setDead();
        } else if (this.world.isRemote || !this.func_190625_o()) {
            if (this.inGround) {
                ++this.ticksInGround;
                if (this.ticksInGround >= 1200) {
                    this.setDead();
                    return;
                }
            }
            float f = 0.0f;
            BlockPos blockpos = new BlockPos(this);
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            if (iblockstate.getMaterial() == Material.WATER) {
                f = BlockLiquid.func_190973_f(iblockstate, this.world, blockpos);
            }
            if (this.field_190627_av == State.FLYING) {
                if (this.caughtEntity != null) {
                    this.motionX = 0.0;
                    this.motionY = 0.0;
                    this.motionZ = 0.0;
                    this.field_190627_av = State.HOOKED_IN_ENTITY;
                    return;
                }
                if (f > 0.0f) {
                    this.motionX *= 0.3;
                    this.motionY *= 0.2;
                    this.motionZ *= 0.3;
                    this.field_190627_av = State.BOBBING;
                    return;
                }
                if (!this.world.isRemote) {
                    this.func_190624_r();
                }
                if (!(this.inGround || this.onGround || this.isCollidedHorizontally)) {
                    ++this.ticksInAir;
                } else {
                    this.ticksInAir = 0;
                    this.motionX = 0.0;
                    this.motionY = 0.0;
                    this.motionZ = 0.0;
                }
            } else {
                if (this.field_190627_av == State.HOOKED_IN_ENTITY) {
                    if (this.caughtEntity != null) {
                        if (this.caughtEntity.isDead) {
                            this.caughtEntity = null;
                            this.field_190627_av = State.FLYING;
                        } else {
                            this.posX = this.caughtEntity.posX;
                            double d2 = this.caughtEntity.height;
                            this.posY = this.caughtEntity.getEntityBoundingBox().minY + d2 * 0.8;
                            this.posZ = this.caughtEntity.posZ;
                            this.setPosition(this.posX, this.posY, this.posZ);
                        }
                    }
                    return;
                }
                if (this.field_190627_av == State.BOBBING) {
                    this.motionX *= 0.9;
                    this.motionZ *= 0.9;
                    double d0 = this.posY + this.motionY - (double)blockpos.getY() - (double)f;
                    if (Math.abs(d0) < 0.01) {
                        d0 += Math.signum(d0) * 0.1;
                    }
                    this.motionY -= d0 * (double)this.rand.nextFloat() * 0.2;
                    if (!this.world.isRemote && f > 0.0f) {
                        this.func_190621_a(blockpos);
                    }
                }
            }
            if (iblockstate.getMaterial() != Material.WATER) {
                this.motionY -= 0.03;
            }
            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.func_190623_q();
            double d1 = 0.92;
            this.motionX *= 0.92;
            this.motionY *= 0.92;
            this.motionZ *= 0.92;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    private boolean func_190625_o() {
        boolean flag1;
        ItemStack itemstack = this.angler.getHeldItemMainhand();
        ItemStack itemstack1 = this.angler.getHeldItemOffhand();
        boolean flag = itemstack.getItem() == Items.FISHING_ROD;
        boolean bl = flag1 = itemstack1.getItem() == Items.FISHING_ROD;
        if (!this.angler.isDead && this.angler.isEntityAlive() && (flag || flag1) && this.getDistanceSqToEntity(this.angler) <= 1024.0) {
            return false;
        }
        this.setDead();
        return true;
    }

    private void func_190623_q() {
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
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
    }

    private void func_190624_r() {
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1, false, true, false);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (raytraceresult != null) {
            vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        }
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0));
        double d0 = 0.0;
        for (Entity entity1 : list) {
            double d1;
            AxisAlignedBB axisalignedbb;
            RayTraceResult raytraceresult1;
            if (!this.canBeHooked(entity1) || entity1 == this.angler && this.ticksInAir < 5 || (raytraceresult1 = (axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.3f)).calculateIntercept(vec3d, vec3d1)) == null || !((d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec)) < d0) && d0 != 0.0) continue;
            entity = entity1;
            d0 = d1;
        }
        if (entity != null) {
            raytraceresult = new RayTraceResult(entity);
        }
        if (raytraceresult != null && raytraceresult.typeOfHit != RayTraceResult.Type.MISS) {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.ENTITY) {
                this.caughtEntity = raytraceresult.entityHit;
                this.func_190622_s();
            } else {
                this.inGround = true;
            }
        }
    }

    private void func_190622_s() {
        this.getDataManager().set(DATA_HOOKED_ENTITY, this.caughtEntity.getEntityId() + 1);
    }

    private void func_190621_a(BlockPos p_190621_1_) {
        WorldServer worldserver = (WorldServer)this.world;
        int i = 1;
        BlockPos blockpos = p_190621_1_.up();
        if (this.rand.nextFloat() < 0.25f && this.world.isRainingAt(blockpos)) {
            ++i;
        }
        if (this.rand.nextFloat() < 0.5f && !this.world.canSeeSky(blockpos)) {
            --i;
        }
        if (this.ticksCatchable > 0) {
            --this.ticksCatchable;
            if (this.ticksCatchable <= 0) {
                this.ticksCaughtDelay = 0;
                this.ticksCatchableDelay = 0;
            } else {
                this.motionY -= 0.2 * (double)this.rand.nextFloat() * (double)this.rand.nextFloat();
            }
        } else if (this.ticksCatchableDelay > 0) {
            this.ticksCatchableDelay -= i;
            if (this.ticksCatchableDelay > 0) {
                double d2;
                this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0);
                float f = this.fishApproachAngle * ((float)Math.PI / 180);
                float f1 = MathHelper.sin(f);
                float f2 = MathHelper.cos(f);
                double d0 = this.posX + (double)(f1 * (float)this.ticksCatchableDelay * 0.1f);
                double d1 = (float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0f;
                Block block = worldserver.getBlockState(new BlockPos(d0, d1 - 1.0, d2 = this.posZ + (double)(f2 * (float)this.ticksCatchableDelay * 0.1f))).getBlock();
                if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
                    if (this.rand.nextFloat() < 0.15f) {
                        worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0, d1 - (double)0.1f, d2, 1, (double)f1, 0.1, (double)f2, 0.0, new int[0]);
                    }
                    float f3 = f1 * 0.04f;
                    float f4 = f2 * 0.04f;
                    worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double)f4, 0.01, (double)(-f3), 1.0, new int[0]);
                    worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double)(-f4), 0.01, (double)f3, 1.0, new int[0]);
                }
            } else {
                this.motionY = -0.4f * MathHelper.nextFloat(this.rand, 0.6f, 1.0f);
                this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                double d3 = this.getEntityBoundingBox().minY + 0.5;
                worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, d3, this.posZ, (int)(1.0f + this.width * 20.0f), (double)this.width, 0.0, (double)this.width, (double)0.2f, new int[0]);
                worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, d3, this.posZ, (int)(1.0f + this.width * 20.0f), (double)this.width, 0.0, (double)this.width, (double)0.2f, new int[0]);
                this.ticksCatchable = MathHelper.getInt(this.rand, 20, 40);
            }
        } else if (this.ticksCaughtDelay > 0) {
            this.ticksCaughtDelay -= i;
            float f5 = 0.15f;
            if (this.ticksCaughtDelay < 20) {
                f5 = (float)((double)f5 + (double)(20 - this.ticksCaughtDelay) * 0.05);
            } else if (this.ticksCaughtDelay < 40) {
                f5 = (float)((double)f5 + (double)(40 - this.ticksCaughtDelay) * 0.02);
            } else if (this.ticksCaughtDelay < 60) {
                f5 = (float)((double)f5 + (double)(60 - this.ticksCaughtDelay) * 0.01);
            }
            if (this.rand.nextFloat() < f5) {
                double d6;
                double d5;
                float f6 = MathHelper.nextFloat(this.rand, 0.0f, 360.0f) * ((float)Math.PI / 180);
                float f7 = MathHelper.nextFloat(this.rand, 25.0f, 60.0f);
                double d4 = this.posX + (double)(MathHelper.sin(f6) * f7 * 0.1f);
                Block block1 = worldserver.getBlockState(new BlockPos((int)d4, (int)(d5 = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0f)) - 1, (int)(d6 = this.posZ + (double)(MathHelper.cos(f6) * f7 * 0.1f)))).getBlock();
                if (block1 == Blocks.WATER || block1 == Blocks.FLOWING_WATER) {
                    worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d4, d5, d6, 2 + this.rand.nextInt(2), (double)0.1f, 0.0, (double)0.1f, 0.0, new int[0]);
                }
            }
            if (this.ticksCaughtDelay <= 0) {
                this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0f, 360.0f);
                this.ticksCatchableDelay = MathHelper.getInt(this.rand, 20, 80);
            }
        } else {
            this.ticksCaughtDelay = MathHelper.getInt(this.rand, 100, 600);
            this.ticksCaughtDelay -= this.field_191519_ax * 20 * 5;
        }
    }

    protected boolean canBeHooked(Entity p_189739_1_) {
        return p_189739_1_.canBeCollidedWith() || p_189739_1_ instanceof EntityItem;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
    }

    public int handleHookRetraction() {
        if (!this.world.isRemote && this.angler != null) {
            int i = 0;
            if (this.caughtEntity != null) {
                this.bringInHookedEntity();
                this.world.setEntityState(this, (byte)31);
                i = this.caughtEntity instanceof EntityItem ? 3 : 5;
            } else if (this.ticksCatchable > 0) {
                LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);
                lootcontext$builder.withLuck((float)this.field_191518_aw + this.angler.getLuck());
                for (ItemStack itemstack : this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.rand, lootcontext$builder.build())) {
                    EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY, this.posZ, itemstack);
                    double d0 = this.angler.posX - this.posX;
                    double d1 = this.angler.posY - this.posY;
                    double d2 = this.angler.posZ - this.posZ;
                    double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    double d4 = 0.1;
                    entityitem.motionX = d0 * 0.1;
                    entityitem.motionY = d1 * 0.1 + (double)MathHelper.sqrt(d3) * 0.08;
                    entityitem.motionZ = d2 * 0.1;
                    this.world.spawnEntityInWorld(entityitem);
                    this.angler.world.spawnEntityInWorld(new EntityXPOrb(this.angler.world, this.angler.posX, this.angler.posY + 0.5, this.angler.posZ + 0.5, this.rand.nextInt(6) + 1));
                    Item item = itemstack.getItem();
                    if (item != Items.FISH && item != Items.COOKED_FISH) continue;
                    this.angler.addStat(StatList.FISH_CAUGHT, 1);
                }
                i = 1;
            }
            if (this.inGround) {
                i = 2;
            }
            this.setDead();
            return i;
        }
        return 0;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 31 && this.world.isRemote && this.caughtEntity instanceof EntityPlayer && ((EntityPlayer)this.caughtEntity).isUser()) {
            this.bringInHookedEntity();
        }
        super.handleStatusUpdate(id);
    }

    protected void bringInHookedEntity() {
        if (this.angler != null) {
            double d0 = this.angler.posX - this.posX;
            double d1 = this.angler.posY - this.posY;
            double d2 = this.angler.posZ - this.posZ;
            double d3 = 0.1;
            this.caughtEntity.motionX += d0 * 0.1;
            this.caughtEntity.motionY += d1 * 0.1;
            this.caughtEntity.motionZ += d2 * 0.1;
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void setDead() {
        super.setDead();
        if (this.angler != null) {
            this.angler.fishEntity = null;
        }
    }

    public EntityPlayer func_190619_l() {
        return this.angler;
    }

    static enum State {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING;

    }
}

