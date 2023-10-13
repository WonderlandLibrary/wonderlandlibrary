/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Sets
 */
package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityEnderman
extends EntityMob {
    private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier attackingSpeedBoostModifier = new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15f, 0).setSaved(false);
    private static final Set carriableBlocks = Sets.newIdentityHashSet();
    private boolean isAggressive;
    private static final String __OBFID = "CL_00001685";

    static {
        carriableBlocks.add(Blocks.grass);
        carriableBlocks.add(Blocks.dirt);
        carriableBlocks.add(Blocks.sand);
        carriableBlocks.add(Blocks.gravel);
        carriableBlocks.add(Blocks.yellow_flower);
        carriableBlocks.add(Blocks.red_flower);
        carriableBlocks.add(Blocks.brown_mushroom);
        carriableBlocks.add(Blocks.red_mushroom);
        carriableBlocks.add(Blocks.tnt);
        carriableBlocks.add(Blocks.cactus);
        carriableBlocks.add(Blocks.clay);
        carriableBlocks.add(Blocks.pumpkin);
        carriableBlocks.add(Blocks.melon_block);
        carriableBlocks.add(Blocks.mycelium);
    }

    public EntityEnderman(World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 2.9f);
        this.stepHeight = 1.0f;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0, false));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(10, new AIPlaceBlock());
        this.tasks.addTask(11, new AITakeBlock());
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, new AIFindPlayer());
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate(){
            private static final String __OBFID = "CL_00002223";

            public boolean func_179948_a(EntityEndermite p_179948_1_) {
                return p_179948_1_.isSpawnedByPlayer();
            }

            public boolean apply(Object p_apply_1_) {
                return this.func_179948_a((EntityEndermite)p_apply_1_);
            }
        }));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3f);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Short(0));
        this.dataWatcher.addObject(17, new Byte(0));
        this.dataWatcher.addObject(18, new Byte(0));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        IBlockState var2 = this.func_175489_ck();
        tagCompound.setShort("carried", (short)Block.getIdFromBlock(var2.getBlock()));
        tagCompound.setShort("carriedData", (short)var2.getBlock().getMetaFromState(var2));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        IBlockState var2 = tagCompund.hasKey("carried", 8) ? Block.getBlockFromName(tagCompund.getString("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF) : Block.getBlockById(tagCompund.getShort("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
        this.func_175490_a(var2);
    }

    private boolean shouldAttackPlayer(EntityPlayer p_70821_1_) {
        ItemStack var2 = p_70821_1_.inventory.armorInventory[3];
        if (var2 != null && var2.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            return false;
        }
        Vec3 var3 = p_70821_1_.getLook(1.0f).normalize();
        Vec3 var4 = new Vec3(this.posX - p_70821_1_.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0f) - (p_70821_1_.posY + (double)p_70821_1_.getEyeHeight()), this.posZ - p_70821_1_.posZ);
        double var5 = var4.lengthVector();
        double var7 = var3.dotProduct(var4 = var4.normalize());
        return var7 > 1.0 - 0.025 / var5 ? p_70821_1_.canEntityBeSeen(this) : false;
    }

    @Override
    public float getEyeHeight() {
        return 2.55f;
    }

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            for (int var1 = 0; var1 < 2; ++var1) {
                this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width, (this.rand.nextDouble() - 0.5) * 2.0, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5) * 2.0, new int[0]);
            }
        }
        this.isJumping = false;
        super.onLivingUpdate();
    }

    @Override
    protected void updateAITasks() {
        float var1;
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0f);
        }
        if (this.isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0) {
            this.setScreaming(false);
        }
        if (this.worldObj.isDaytime() && (var1 = this.getBrightness(1.0f)) > 0.5f && this.worldObj.isAgainstSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0f < (var1 - 0.4f) * 2.0f) {
            this.setAttackTarget(null);
            this.setScreaming(false);
            this.isAggressive = false;
            this.teleportRandomly();
        }
        super.updateAITasks();
    }

    protected boolean teleportRandomly() {
        double var1 = this.posX + (this.rand.nextDouble() - 0.5) * 64.0;
        double var3 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double var5 = this.posZ + (this.rand.nextDouble() - 0.5) * 64.0;
        return this.teleportTo(var1, var3, var5);
    }

    protected boolean teleportToEntity(Entity p_70816_1_) {
        Vec3 var2 = new Vec3(this.posX - p_70816_1_.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0f) - p_70816_1_.posY + (double)p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        var2 = var2.normalize();
        double var3 = 16.0;
        double var5 = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - var2.xCoord * var3;
        double var7 = this.posY + (double)(this.rand.nextInt(16) - 8) - var2.yCoord * var3;
        double var9 = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - var2.zCoord * var3;
        return this.teleportTo(var5, var7, var9);
    }

    protected boolean teleportTo(double p_70825_1_, double p_70825_3_, double p_70825_5_) {
        double var7 = this.posX;
        double var9 = this.posY;
        double var11 = this.posZ;
        this.posX = p_70825_1_;
        this.posY = p_70825_3_;
        this.posZ = p_70825_5_;
        boolean var13 = false;
        BlockPos var14 = new BlockPos(this.posX, this.posY, this.posZ);
        if (this.worldObj.isBlockLoaded(var14)) {
            boolean var15 = false;
            while (!var15 && var14.getY() > 0) {
                BlockPos var16 = var14.offsetDown();
                Block var17 = this.worldObj.getBlockState(var16).getBlock();
                if (var17.getMaterial().blocksMovement()) {
                    var15 = true;
                    continue;
                }
                this.posY -= 1.0;
                var14 = var16;
            }
            if (var15) {
                super.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
                    var13 = true;
                }
            }
        }
        if (!var13) {
            this.setPosition(var7, var9, var11);
            return false;
        }
        int var28 = 128;
        for (int var29 = 0; var29 < var28; ++var29) {
            double var30 = (double)var29 / ((double)var28 - 1.0);
            float var19 = (this.rand.nextFloat() - 0.5f) * 0.2f;
            float var20 = (this.rand.nextFloat() - 0.5f) * 0.2f;
            float var21 = (this.rand.nextFloat() - 0.5f) * 0.2f;
            double var22 = var7 + (this.posX - var7) * var30 + (this.rand.nextDouble() - 0.5) * (double)this.width * 2.0;
            double var24 = var9 + (this.posY - var9) * var30 + this.rand.nextDouble() * (double)this.height;
            double var26 = var11 + (this.posZ - var11) * var30 + (this.rand.nextDouble() - 0.5) * (double)this.width * 2.0;
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, var22, var24, var26, (double)var19, (double)var20, (double)var21, new int[0]);
        }
        this.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0f, 1.0f);
        this.playSound("mob.endermen.portal", 1.0f, 1.0f);
        return true;
    }

    @Override
    protected String getLivingSound() {
        return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    @Override
    protected String getHurtSound() {
        return "mob.endermen.hit";
    }

    @Override
    protected String getDeathSound() {
        return "mob.endermen.death";
    }

    @Override
    protected Item getDropItem() {
        return Items.ender_pearl;
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        Item var3 = this.getDropItem();
        if (var3 != null) {
            int var4 = this.rand.nextInt(2 + p_70628_2_);
            for (int var5 = 0; var5 < var4; ++var5) {
                this.dropItem(var3, 1);
            }
        }
    }

    public void func_175490_a(IBlockState p_175490_1_) {
        this.dataWatcher.updateObject(16, (short)(Block.getStateId(p_175490_1_) & 0xFFFF));
    }

    public IBlockState func_175489_ck() {
        return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & 0xFFFF);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (source.getEntity() == null || !(source.getEntity() instanceof EntityEndermite)) {
            if (!this.worldObj.isRemote) {
                this.setScreaming(true);
            }
            if (source instanceof EntityDamageSource && source.getEntity() instanceof EntityPlayer) {
                if (source.getEntity() instanceof EntityPlayerMP && ((EntityPlayerMP)source.getEntity()).theItemInWorldManager.isCreative()) {
                    this.setScreaming(false);
                } else {
                    this.isAggressive = true;
                }
            }
            if (source instanceof EntityDamageSourceIndirect) {
                this.isAggressive = false;
                for (int var4 = 0; var4 < 64; ++var4) {
                    if (!this.teleportRandomly()) continue;
                    return true;
                }
                return false;
            }
        }
        boolean var3 = super.attackEntityFrom(source, amount);
        if (source.isUnblockable() && this.rand.nextInt(10) != 0) {
            this.teleportRandomly();
        }
        return var3;
    }

    public boolean isScreaming() {
        return this.dataWatcher.getWatchableObjectByte(18) > 0;
    }

    public void setScreaming(boolean p_70819_1_) {
        this.dataWatcher.updateObject(18, (byte)(p_70819_1_ ? 1 : 0));
    }

    class AIFindPlayer
    extends EntityAINearestAttackableTarget {
        private EntityPlayer field_179448_g;
        private int field_179450_h;
        private int field_179451_i;
        private EntityEnderman field_179449_j;
        private static final String __OBFID = "CL_00002221";

        public AIFindPlayer() {
            super((EntityCreature)EntityEnderman.this, EntityPlayer.class, true);
            this.field_179449_j = EntityEnderman.this;
        }

        @Override
        public boolean shouldExecute() {
            double var1 = this.getTargetDistance();
            List var3 = this.taskOwner.worldObj.func_175647_a(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(var1, 4.0, var1), this.targetEntitySelector);
            Collections.sort(var3, this.theNearestAttackableTargetSorter);
            if (var3.isEmpty()) {
                return false;
            }
            this.field_179448_g = (EntityPlayer)var3.get(0);
            return true;
        }

        @Override
        public void startExecuting() {
            this.field_179450_h = 5;
            this.field_179451_i = 0;
        }

        @Override
        public void resetTask() {
            this.field_179448_g = null;
            this.field_179449_j.setScreaming(false);
            IAttributeInstance var1 = this.field_179449_j.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            var1.removeModifier(attackingSpeedBoostModifier);
            super.resetTask();
        }

        @Override
        public boolean continueExecuting() {
            if (this.field_179448_g != null) {
                if (!this.field_179449_j.shouldAttackPlayer(this.field_179448_g)) {
                    return false;
                }
                this.field_179449_j.isAggressive = true;
                this.field_179449_j.faceEntity(this.field_179448_g, 10.0f, 10.0f);
                return true;
            }
            return super.continueExecuting();
        }

        @Override
        public void updateTask() {
            if (this.field_179448_g != null) {
                if (--this.field_179450_h <= 0) {
                    this.targetEntity = this.field_179448_g;
                    this.field_179448_g = null;
                    super.startExecuting();
                    this.field_179449_j.playSound("mob.endermen.stare", 1.0f, 1.0f);
                    this.field_179449_j.setScreaming(true);
                    IAttributeInstance var1 = this.field_179449_j.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                    var1.applyModifier(attackingSpeedBoostModifier);
                }
            } else {
                if (this.targetEntity != null) {
                    if (this.targetEntity instanceof EntityPlayer && this.field_179449_j.shouldAttackPlayer((EntityPlayer)this.targetEntity)) {
                        if (this.targetEntity.getDistanceSqToEntity(this.field_179449_j) < 16.0) {
                            this.field_179449_j.teleportRandomly();
                        }
                        this.field_179451_i = 0;
                    } else if (this.targetEntity.getDistanceSqToEntity(this.field_179449_j) > 256.0 && this.field_179451_i++ >= 30 && this.field_179449_j.teleportToEntity(this.targetEntity)) {
                        this.field_179451_i = 0;
                    }
                }
                super.updateTask();
            }
        }
    }

    class AIPlaceBlock
    extends EntityAIBase {
        private EntityEnderman field_179475_a;
        private static final String __OBFID = "CL_00002222";

        AIPlaceBlock() {
            this.field_179475_a = EntityEnderman.this;
        }

        @Override
        public boolean shouldExecute() {
            return !this.field_179475_a.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") ? false : (this.field_179475_a.func_175489_ck().getBlock().getMaterial() == Material.air ? false : this.field_179475_a.getRNG().nextInt(2000) == 0);
        }

        @Override
        public void updateTask() {
            Random var1 = this.field_179475_a.getRNG();
            World var2 = this.field_179475_a.worldObj;
            int var3 = MathHelper.floor_double(this.field_179475_a.posX - 1.0 + var1.nextDouble() * 2.0);
            int var4 = MathHelper.floor_double(this.field_179475_a.posY + var1.nextDouble() * 2.0);
            int var5 = MathHelper.floor_double(this.field_179475_a.posZ - 1.0 + var1.nextDouble() * 2.0);
            BlockPos var6 = new BlockPos(var3, var4, var5);
            Block var7 = var2.getBlockState(var6).getBlock();
            Block var8 = var2.getBlockState(var6.offsetDown()).getBlock();
            if (this.func_179474_a(var2, var6, this.field_179475_a.func_175489_ck().getBlock(), var7, var8)) {
                var2.setBlockState(var6, this.field_179475_a.func_175489_ck(), 3);
                this.field_179475_a.func_175490_a(Blocks.air.getDefaultState());
            }
        }

        private boolean func_179474_a(World worldIn, BlockPos p_179474_2_, Block p_179474_3_, Block p_179474_4_, Block p_179474_5_) {
            return !p_179474_3_.canPlaceBlockAt(worldIn, p_179474_2_) ? false : (p_179474_4_.getMaterial() != Material.air ? false : (p_179474_5_.getMaterial() == Material.air ? false : p_179474_5_.isFullCube()));
        }
    }

    class AITakeBlock
    extends EntityAIBase {
        private EntityEnderman field_179473_a;
        private static final String __OBFID = "CL_00002220";

        AITakeBlock() {
            this.field_179473_a = EntityEnderman.this;
        }

        @Override
        public boolean shouldExecute() {
            return !this.field_179473_a.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") ? false : (this.field_179473_a.func_175489_ck().getBlock().getMaterial() != Material.air ? false : this.field_179473_a.getRNG().nextInt(20) == 0);
        }

        @Override
        public void updateTask() {
            Random var1 = this.field_179473_a.getRNG();
            World var2 = this.field_179473_a.worldObj;
            int var3 = MathHelper.floor_double(this.field_179473_a.posX - 2.0 + var1.nextDouble() * 4.0);
            int var4 = MathHelper.floor_double(this.field_179473_a.posY + var1.nextDouble() * 3.0);
            int var5 = MathHelper.floor_double(this.field_179473_a.posZ - 2.0 + var1.nextDouble() * 4.0);
            BlockPos var6 = new BlockPos(var3, var4, var5);
            IBlockState var7 = var2.getBlockState(var6);
            Block var8 = var7.getBlock();
            if (carriableBlocks.contains(var8)) {
                this.field_179473_a.func_175490_a(var7);
                var2.setBlockState(var6, Blocks.air.getDefaultState());
            }
        }
    }
}

