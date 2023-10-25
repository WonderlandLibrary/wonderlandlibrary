/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFallingBlock
extends Entity {
    private IBlockState fallTile;
    public int fallTime;
    public boolean shouldDropItem = true;
    private boolean canSetAsBlock;
    private boolean hurtEntities;
    private int fallHurtMax = 40;
    private float fallHurtAmount = 2.0f;
    public NBTTagCompound tileEntityData;
    protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.BLOCK_POS);

    public EntityFallingBlock(World worldIn) {
        super(worldIn);
    }

    public EntityFallingBlock(World worldIn, double x, double y, double z, IBlockState fallingBlockState) {
        super(worldIn);
        this.fallTile = fallingBlockState;
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.98f);
        this.setPosition(x, y + (double)((1.0f - this.height) / 2.0f), z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.setOrigin(new BlockPos(this));
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    public void setOrigin(BlockPos p_184530_1_) {
        this.dataManager.set(ORIGIN, p_184530_1_);
    }

    public BlockPos getOrigin() {
        return this.dataManager.get(ORIGIN);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(ORIGIN, BlockPos.ORIGIN);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public void onUpdate() {
        Block block = this.fallTile.getBlock();
        if (this.fallTile.getMaterial() == Material.AIR) {
            this.setDead();
        } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            if (this.fallTime++ == 0) {
                BlockPos blockpos = new BlockPos(this);
                if (this.world.getBlockState(blockpos).getBlock() == block) {
                    this.world.setBlockToAir(blockpos);
                } else if (!this.world.isRemote) {
                    this.setDead();
                    return;
                }
            }
            if (!this.hasNoGravity()) {
                this.motionY -= (double)0.04f;
            }
            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            if (!this.world.isRemote) {
                RayTraceResult raytraceresult;
                BlockPos blockpos1 = new BlockPos(this);
                boolean flag = this.fallTile.getBlock() == Blocks.field_192444_dS;
                boolean flag1 = flag && this.world.getBlockState(blockpos1).getMaterial() == Material.WATER;
                double d0 = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;
                if (flag && d0 > 1.0 && (raytraceresult = this.world.rayTraceBlocks(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), true)) != null && this.world.getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.WATER) {
                    blockpos1 = raytraceresult.getBlockPos();
                    flag1 = true;
                }
                if (!this.onGround && !flag1) {
                    if (this.fallTime > 100 && !this.world.isRemote && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600) {
                        if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                            this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0f);
                        }
                        this.setDead();
                    }
                } else {
                    IBlockState iblockstate = this.world.getBlockState(blockpos1);
                    if (!flag1 && BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - (double)0.01f, this.posZ)))) {
                        this.onGround = false;
                        return;
                    }
                    this.motionX *= (double)0.7f;
                    this.motionZ *= (double)0.7f;
                    this.motionY *= -0.5;
                    if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION) {
                        this.setDead();
                        if (!this.canSetAsBlock) {
                            if (this.world.mayPlace(block, blockpos1, true, EnumFacing.UP, null) && (flag1 || !BlockFalling.canFallThrough(this.world.getBlockState(blockpos1.down()))) && this.world.setBlockState(blockpos1, this.fallTile, 3)) {
                                TileEntity tileentity;
                                if (block instanceof BlockFalling) {
                                    ((BlockFalling)block).onEndFalling(this.world, blockpos1, this.fallTile, iblockstate);
                                }
                                if (this.tileEntityData != null && block instanceof ITileEntityProvider && (tileentity = this.world.getTileEntity(blockpos1)) != null) {
                                    NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                                    for (String s : this.tileEntityData.getKeySet()) {
                                        NBTBase nbtbase = this.tileEntityData.getTag(s);
                                        if ("x".equals(s) || "y".equals(s) || "z".equals(s)) continue;
                                        nbttagcompound.setTag(s, nbtbase.copy());
                                    }
                                    tileentity.readFromNBT(nbttagcompound);
                                    tileentity.markDirty();
                                }
                            } else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0f);
                            }
                        } else if (block instanceof BlockFalling) {
                            ((BlockFalling)block).func_190974_b(this.world, blockpos1);
                        }
                    }
                }
            }
            this.motionX *= (double)0.98f;
            this.motionY *= (double)0.98f;
            this.motionZ *= (double)0.98f;
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        int i;
        Block block = this.fallTile.getBlock();
        if (this.hurtEntities && (i = MathHelper.ceil(distance - 1.0f)) > 0) {
            ArrayList<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
            boolean flag = block == Blocks.ANVIL;
            DamageSource damagesource = flag ? DamageSource.anvil : DamageSource.fallingBlock;
            for (Entity entity : list) {
                entity.attackEntityFrom(damagesource, Math.min(MathHelper.floor((float)i * this.fallHurtAmount), this.fallHurtMax));
            }
            if (flag && (double)this.rand.nextFloat() < (double)0.05f + (double)i * 0.05) {
                int j = this.fallTile.getValue(BlockAnvil.DAMAGE);
                if (++j > 2) {
                    this.canSetAsBlock = true;
                } else {
                    this.fallTile = this.fallTile.withProperty(BlockAnvil.DAMAGE, j);
                }
            }
        }
    }

    public static void registerFixesFallingBlock(DataFixer fixer) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.AIR;
        ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(block);
        compound.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setByte("Data", (byte)block.getMetaFromState(this.fallTile));
        compound.setInteger("Time", this.fallTime);
        compound.setBoolean("DropItem", this.shouldDropItem);
        compound.setBoolean("HurtEntities", this.hurtEntities);
        compound.setFloat("FallHurtAmount", this.fallHurtAmount);
        compound.setInteger("FallHurtMax", this.fallHurtMax);
        if (this.tileEntityData != null) {
            compound.setTag("TileEntityData", this.tileEntityData);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        int i = compound.getByte("Data") & 0xFF;
        this.fallTile = compound.hasKey("Block", 8) ? Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i) : (compound.hasKey("TileID", 99) ? Block.getBlockById(compound.getInteger("TileID")).getStateFromMeta(i) : Block.getBlockById(compound.getByte("Tile") & 0xFF).getStateFromMeta(i));
        this.fallTime = compound.getInteger("Time");
        Block block = this.fallTile.getBlock();
        if (compound.hasKey("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.fallHurtAmount = compound.getFloat("FallHurtAmount");
            this.fallHurtMax = compound.getInteger("FallHurtMax");
        } else if (block == Blocks.ANVIL) {
            this.hurtEntities = true;
        }
        if (compound.hasKey("DropItem", 99)) {
            this.shouldDropItem = compound.getBoolean("DropItem");
        }
        if (compound.hasKey("TileEntityData", 10)) {
            this.tileEntityData = compound.getCompoundTag("TileEntityData");
        }
        if (block == null || block.getDefaultState().getMaterial() == Material.AIR) {
            this.fallTile = Blocks.SAND.getDefaultState();
        }
    }

    public World getWorldObj() {
        return this.world;
    }

    public void setHurtEntities(boolean p_145806_1_) {
        this.hurtEntities = p_145806_1_;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public void addEntityCrashInfo(CrashReportCategory category) {
        super.addEntityCrashInfo(category);
        if (this.fallTile != null) {
            Block block = this.fallTile.getBlock();
            category.addCrashSection("Immitating block ID", Block.getIdFromBlock(block));
            category.addCrashSection("Immitating block data", block.getMetaFromState(this.fallTile));
        }
    }

    @Nullable
    public IBlockState getBlock() {
        return this.fallTile;
    }

    @Override
    public boolean ignoreItemEntityData() {
        return true;
    }
}

