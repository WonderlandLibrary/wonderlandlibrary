/*
 * Decompiled with CFR 0.152.
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
    private boolean isAggressive;
    private static final AttributeModifier attackingSpeedBoostModifier;
    private static final Set<Block> carriableBlocks;
    private static final UUID attackingSpeedBoostModifierUUID;

    private boolean shouldAttackPlayer(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.armorInventory[3];
        if (itemStack != null && itemStack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            return false;
        }
        Vec3 vec3 = entityPlayer.getLook(1.0f).normalize();
        Vec3 vec32 = new Vec3(this.posX - entityPlayer.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0f) - (entityPlayer.posY + (double)entityPlayer.getEyeHeight()), this.posZ - entityPlayer.posZ);
        double d = vec32.lengthVector();
        double d2 = vec3.dotProduct(vec32 = vec32.normalize());
        return d2 > 1.0 - 0.025 / d ? entityPlayer.canEntityBeSeen(this) : false;
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

    public IBlockState getHeldBlockState() {
        return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & 0xFFFF);
    }

    public boolean isScreaming() {
        return this.dataWatcher.getWatchableObjectByte(18) > 0;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        IBlockState iBlockState = nBTTagCompound.hasKey("carried", 8) ? Block.getBlockFromName(nBTTagCompound.getString("carried")).getStateFromMeta(nBTTagCompound.getShort("carriedData") & 0xFFFF) : Block.getBlockById(nBTTagCompound.getShort("carried")).getStateFromMeta(nBTTagCompound.getShort("carriedData") & 0xFFFF);
        this.setHeldBlockState(iBlockState);
    }

    @Override
    protected void updateAITasks() {
        float f;
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0f);
        }
        if (this.isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0) {
            this.setScreaming(false);
        }
        if (this.worldObj.isDaytime() && (f = this.getBrightness(1.0f)) > 0.5f && this.worldObj.canSeeSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f) {
            this.setAttackTarget(null);
            this.setScreaming(false);
            this.isAggressive = false;
            this.teleportRandomly();
        }
        super.updateAITasks();
    }

    public EntityEnderman(World world) {
        super(world);
        this.setSize(0.6f, 2.9f);
        this.stepHeight = 1.0f;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0, false));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(10, new AIPlaceBlock(this));
        this.tasks.addTask(11, new AITakeBlock(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, new AIFindPlayer(this));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityEndermite>(this, EntityEndermite.class, 10, true, false, new Predicate<EntityEndermite>(){

            public boolean apply(EntityEndermite entityEndermite) {
                return entityEndermite.isSpawnedByPlayer();
            }
        }));
    }

    protected boolean teleportRandomly() {
        double d = this.posX + (this.rand.nextDouble() - 0.5) * 64.0;
        double d2 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5) * 64.0;
        return this.teleportTo(d, d2, d3);
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        Item item = this.getDropItem();
        if (item != null) {
            int n2 = this.rand.nextInt(2 + n);
            int n3 = 0;
            while (n3 < n2) {
                this.dropItem(item, 1);
                ++n3;
            }
        }
    }

    protected boolean teleportToEntity(Entity entity) {
        Vec3 vec3 = new Vec3(this.posX - entity.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0f) - entity.posY + (double)entity.getEyeHeight(), this.posZ - entity.posZ);
        vec3 = vec3.normalize();
        double d = 16.0;
        double d2 = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - vec3.xCoord * d;
        double d3 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3.yCoord * d;
        double d4 = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - vec3.zCoord * d;
        return this.teleportTo(d2, d3, d4);
    }

    public void setScreaming(boolean bl) {
        this.dataWatcher.updateObject(18, (byte)(bl ? 1 : 0));
    }

    @Override
    public float getEyeHeight() {
        return 2.55f;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (damageSource.getEntity() == null || !(damageSource.getEntity() instanceof EntityEndermite)) {
            if (!this.worldObj.isRemote) {
                this.setScreaming(true);
            }
            if (damageSource instanceof EntityDamageSource && damageSource.getEntity() instanceof EntityPlayer) {
                if (damageSource.getEntity() instanceof EntityPlayerMP && ((EntityPlayerMP)damageSource.getEntity()).theItemInWorldManager.isCreative()) {
                    this.setScreaming(false);
                } else {
                    this.isAggressive = true;
                }
            }
            if (damageSource instanceof EntityDamageSourceIndirect) {
                this.isAggressive = false;
                int n = 0;
                while (n < 64) {
                    if (this.teleportRandomly()) {
                        return true;
                    }
                    ++n;
                }
                return false;
            }
        }
        boolean bl = super.attackEntityFrom(damageSource, f);
        if (damageSource.isUnblockable() && this.rand.nextInt(10) != 0) {
            this.teleportRandomly();
        }
        return bl;
    }

    public void setHeldBlockState(IBlockState iBlockState) {
        this.dataWatcher.updateObject(16, (short)(Block.getStateId(iBlockState) & 0xFFFF));
    }

    @Override
    protected String getHurtSound() {
        return "mob.endermen.hit";
    }

    protected boolean teleportTo(double d, double d2, double d3) {
        int n;
        double d4 = this.posX;
        double d5 = this.posY;
        double d6 = this.posZ;
        this.posX = d;
        this.posY = d2;
        this.posZ = d3;
        boolean bl = false;
        BlockPos blockPos = new BlockPos(this.posX, this.posY, this.posZ);
        if (this.worldObj.isBlockLoaded(blockPos)) {
            n = 0;
            while (n == 0 && blockPos.getY() > 0) {
                BlockPos blockPos2 = blockPos.down();
                Block block = this.worldObj.getBlockState(blockPos2).getBlock();
                if (block.getMaterial().blocksMovement()) {
                    n = 1;
                    continue;
                }
                this.posY -= 1.0;
                blockPos = blockPos2;
            }
            if (n != 0) {
                super.setPositionAndUpdate(this.posX, this.posY, this.posZ);
                if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox())) {
                    bl = true;
                }
            }
        }
        if (!bl) {
            this.setPosition(d4, d5, d6);
            return false;
        }
        n = 128;
        int n2 = 0;
        while (n2 < n) {
            double d7 = (double)n2 / ((double)n - 1.0);
            float f = (this.rand.nextFloat() - 0.5f) * 0.2f;
            float f2 = (this.rand.nextFloat() - 0.5f) * 0.2f;
            float f3 = (this.rand.nextFloat() - 0.5f) * 0.2f;
            double d8 = d4 + (this.posX - d4) * d7 + (this.rand.nextDouble() - 0.5) * (double)this.width * 2.0;
            double d9 = d5 + (this.posY - d5) * d7 + this.rand.nextDouble() * (double)this.height;
            double d10 = d6 + (this.posZ - d6) * d7 + (this.rand.nextDouble() - 0.5) * (double)this.width * 2.0;
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d8, d9, d10, (double)f, (double)f2, (double)f3, new int[0]);
            ++n2;
        }
        this.worldObj.playSoundEffect(d4, d5, d6, "mob.endermen.portal", 1.0f, 1.0f);
        this.playSound("mob.endermen.portal", 1.0f, 1.0f);
        return true;
    }

    @Override
    protected String getLivingSound() {
        return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    @Override
    protected Item getDropItem() {
        return Items.ender_pearl;
    }

    static {
        attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
        attackingSpeedBoostModifier = new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15f, 0).setSaved(false);
        carriableBlocks = Sets.newIdentityHashSet();
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

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        IBlockState iBlockState = this.getHeldBlockState();
        nBTTagCompound.setShort("carried", (short)Block.getIdFromBlock(iBlockState.getBlock()));
        nBTTagCompound.setShort("carriedData", (short)iBlockState.getBlock().getMetaFromState(iBlockState));
    }

    @Override
    protected String getDeathSound() {
        return "mob.endermen.death";
    }

    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            int n = 0;
            while (n < 2) {
                this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width, (this.rand.nextDouble() - 0.5) * 2.0, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5) * 2.0, new int[0]);
                ++n;
            }
        }
        this.isJumping = false;
        super.onLivingUpdate();
    }

    static class AIPlaceBlock
    extends EntityAIBase {
        private EntityEnderman enderman;

        private boolean func_179474_a(World world, BlockPos blockPos, Block block, Block block2, Block block3) {
            return !block.canPlaceBlockAt(world, blockPos) ? false : (block2.getMaterial() != Material.air ? false : (block3.getMaterial() == Material.air ? false : block3.isFullCube()));
        }

        public AIPlaceBlock(EntityEnderman entityEnderman) {
            this.enderman = entityEnderman;
        }

        @Override
        public boolean shouldExecute() {
            return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : (this.enderman.getHeldBlockState().getBlock().getMaterial() == Material.air ? false : this.enderman.getRNG().nextInt(2000) == 0);
        }

        @Override
        public void updateTask() {
            Random random = this.enderman.getRNG();
            World world = this.enderman.worldObj;
            int n = MathHelper.floor_double(this.enderman.posX - 1.0 + random.nextDouble() * 2.0);
            int n2 = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 2.0);
            int n3 = MathHelper.floor_double(this.enderman.posZ - 1.0 + random.nextDouble() * 2.0);
            BlockPos blockPos = new BlockPos(n, n2, n3);
            Block block = world.getBlockState(blockPos).getBlock();
            Block block2 = world.getBlockState(blockPos.down()).getBlock();
            if (this.func_179474_a(world, blockPos, this.enderman.getHeldBlockState().getBlock(), block, block2)) {
                world.setBlockState(blockPos, this.enderman.getHeldBlockState(), 3);
                this.enderman.setHeldBlockState(Blocks.air.getDefaultState());
            }
        }
    }

    static class AITakeBlock
    extends EntityAIBase {
        private EntityEnderman enderman;

        public AITakeBlock(EntityEnderman entityEnderman) {
            this.enderman = entityEnderman;
        }

        @Override
        public void updateTask() {
            Random random = this.enderman.getRNG();
            World world = this.enderman.worldObj;
            int n = MathHelper.floor_double(this.enderman.posX - 2.0 + random.nextDouble() * 4.0);
            int n2 = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 3.0);
            int n3 = MathHelper.floor_double(this.enderman.posZ - 2.0 + random.nextDouble() * 4.0);
            BlockPos blockPos = new BlockPos(n, n2, n3);
            IBlockState iBlockState = world.getBlockState(blockPos);
            Block block = iBlockState.getBlock();
            if (carriableBlocks.contains(block)) {
                this.enderman.setHeldBlockState(iBlockState);
                world.setBlockState(blockPos, Blocks.air.getDefaultState());
            }
        }

        @Override
        public boolean shouldExecute() {
            return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : (this.enderman.getHeldBlockState().getBlock().getMaterial() != Material.air ? false : this.enderman.getRNG().nextInt(20) == 0);
        }
    }

    static class AIFindPlayer
    extends EntityAINearestAttackableTarget {
        private EntityEnderman enderman;
        private int field_179451_i;
        private int field_179450_h;
        private EntityPlayer player;

        public AIFindPlayer(EntityEnderman entityEnderman) {
            super((EntityCreature)entityEnderman, EntityPlayer.class, true);
            this.enderman = entityEnderman;
        }

        @Override
        public void startExecuting() {
            this.field_179450_h = 5;
            this.field_179451_i = 0;
        }

        @Override
        public void resetTask() {
            this.player = null;
            this.enderman.setScreaming(false);
            IAttributeInstance iAttributeInstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            iAttributeInstance.removeModifier(attackingSpeedBoostModifier);
            super.resetTask();
        }

        @Override
        public boolean shouldExecute() {
            double d = this.getTargetDistance();
            List<EntityPlayer> list = this.taskOwner.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(d, 4.0, d), this.targetEntitySelector);
            Collections.sort(list, this.theNearestAttackableTargetSorter);
            if (list.isEmpty()) {
                return false;
            }
            this.player = list.get(0);
            return true;
        }

        @Override
        public void updateTask() {
            if (this.player != null) {
                if (--this.field_179450_h <= 0) {
                    this.targetEntity = this.player;
                    this.player = null;
                    super.startExecuting();
                    this.enderman.playSound("mob.endermen.stare", 1.0f, 1.0f);
                    this.enderman.setScreaming(true);
                    IAttributeInstance iAttributeInstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
                    iAttributeInstance.applyModifier(attackingSpeedBoostModifier);
                }
            } else {
                if (this.targetEntity != null) {
                    if (this.targetEntity instanceof EntityPlayer && this.enderman.shouldAttackPlayer((EntityPlayer)this.targetEntity)) {
                        if (this.targetEntity.getDistanceSqToEntity(this.enderman) < 16.0) {
                            this.enderman.teleportRandomly();
                        }
                        this.field_179451_i = 0;
                    } else if (this.targetEntity.getDistanceSqToEntity(this.enderman) > 256.0 && this.field_179451_i++ >= 30 && this.enderman.teleportToEntity(this.targetEntity)) {
                        this.field_179451_i = 0;
                    }
                }
                super.updateTask();
            }
        }

        @Override
        public boolean continueExecuting() {
            if (this.player != null) {
                if (!this.enderman.shouldAttackPlayer(this.player)) {
                    return false;
                }
                this.enderman.isAggressive = true;
                this.enderman.faceEntity(this.player, 10.0f, 10.0f);
                return true;
            }
            return super.continueExecuting();
        }
    }
}

