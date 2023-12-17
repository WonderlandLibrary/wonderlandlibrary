// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockAnvil;
import net.minecraft.util.DamageSource;
import com.google.common.collect.Lists;
import net.minecraft.util.math.MathHelper;
import net.minecraft.nbt.NBTBase;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockFalling;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.init.Blocks;
import net.minecraft.entity.MoverType;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;

public class EntityFallingBlock extends Entity
{
    private IBlockState fallTile;
    public int fallTime;
    public boolean shouldDropItem;
    private boolean dontSetBlock;
    private boolean hurtEntities;
    private int fallHurtMax;
    private float fallHurtAmount;
    public NBTTagCompound tileEntityData;
    protected static final DataParameter<BlockPos> ORIGIN;
    
    public EntityFallingBlock(final World worldIn) {
        super(worldIn);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0f;
    }
    
    public EntityFallingBlock(final World worldIn, final double x, final double y, final double z, final IBlockState fallingBlockState) {
        super(worldIn);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0f;
        this.fallTile = fallingBlockState;
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.98f);
        this.setPosition(x, y + (1.0f - this.height) / 2.0f, z);
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
    
    public void setOrigin(final BlockPos p_184530_1_) {
        this.dataManager.set(EntityFallingBlock.ORIGIN, p_184530_1_);
    }
    
    public BlockPos getOrigin() {
        return this.dataManager.get(EntityFallingBlock.ORIGIN);
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void entityInit() {
        this.dataManager.register(EntityFallingBlock.ORIGIN, BlockPos.ORIGIN);
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public void onUpdate() {
        final Block block = this.fallTile.getBlock();
        if (this.fallTile.getMaterial() == Material.AIR) {
            this.setDead();
        }
        else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            if (this.fallTime++ == 0) {
                final BlockPos blockpos = new BlockPos(this);
                if (this.world.getBlockState(blockpos).getBlock() == block) {
                    this.world.setBlockToAir(blockpos);
                }
                else if (!this.world.isRemote) {
                    this.setDead();
                    return;
                }
            }
            if (!this.hasNoGravity()) {
                this.motionY -= 0.03999999910593033;
            }
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            if (!this.world.isRemote) {
                BlockPos blockpos2 = new BlockPos(this);
                final boolean flag = this.fallTile.getBlock() == Blocks.CONCRETE_POWDER;
                boolean flag2 = flag && this.world.getBlockState(blockpos2).getMaterial() == Material.WATER;
                final double d0 = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;
                if (flag && d0 > 1.0) {
                    final RayTraceResult raytraceresult = this.world.rayTraceBlocks(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), true);
                    if (raytraceresult != null && this.world.getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.WATER) {
                        blockpos2 = raytraceresult.getBlockPos();
                        flag2 = true;
                    }
                }
                if (!this.onGround && !flag2) {
                    if ((this.fallTime > 100 && !this.world.isRemote && (blockpos2.getY() < 1 || blockpos2.getY() > 256)) || this.fallTime > 600) {
                        if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                            this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0f);
                        }
                        this.setDead();
                    }
                }
                else {
                    final IBlockState iblockstate = this.world.getBlockState(blockpos2);
                    if (!flag2 && BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582, this.posZ)))) {
                        this.onGround = false;
                        return;
                    }
                    this.motionX *= 0.699999988079071;
                    this.motionZ *= 0.699999988079071;
                    this.motionY *= -0.5;
                    if (iblockstate.getBlock() != Blocks.PISTON_EXTENSION) {
                        this.setDead();
                        if (!this.dontSetBlock) {
                            if (this.world.mayPlace(block, blockpos2, true, EnumFacing.UP, null) && (flag2 || !BlockFalling.canFallThrough(this.world.getBlockState(blockpos2.down()))) && this.world.setBlockState(blockpos2, this.fallTile, 3)) {
                                if (block instanceof BlockFalling) {
                                    ((BlockFalling)block).onEndFalling(this.world, blockpos2, this.fallTile, iblockstate);
                                }
                                if (this.tileEntityData != null && block instanceof ITileEntityProvider) {
                                    final TileEntity tileentity = this.world.getTileEntity(blockpos2);
                                    if (tileentity != null) {
                                        final NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                                        for (final String s : this.tileEntityData.getKeySet()) {
                                            final NBTBase nbtbase = this.tileEntityData.getTag(s);
                                            if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                nbttagcompound.setTag(s, nbtbase.copy());
                                            }
                                        }
                                        tileentity.readFromNBT(nbttagcompound);
                                        tileentity.markDirty();
                                    }
                                }
                            }
                            else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0f);
                            }
                        }
                        else if (block instanceof BlockFalling) {
                            ((BlockFalling)block).onBroken(this.world, blockpos2);
                        }
                    }
                }
            }
            this.motionX *= 0.9800000190734863;
            this.motionY *= 0.9800000190734863;
            this.motionZ *= 0.9800000190734863;
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        final Block block = this.fallTile.getBlock();
        if (this.hurtEntities) {
            final int i = MathHelper.ceil(distance - 1.0f);
            if (i > 0) {
                final List<Entity> list = (List<Entity>)Lists.newArrayList((Iterable)this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                final boolean flag = block == Blocks.ANVIL;
                final DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                for (final Entity entity : list) {
                    entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor(i * this.fallHurtAmount), this.fallHurtMax));
                }
                if (flag && this.rand.nextFloat() < 0.05000000074505806 + i * 0.05) {
                    int j = this.fallTile.getValue((IProperty<Integer>)BlockAnvil.DAMAGE);
                    if (++j > 2) {
                        this.dontSetBlock = true;
                    }
                    else {
                        this.fallTile = this.fallTile.withProperty((IProperty<Comparable>)BlockAnvil.DAMAGE, j);
                    }
                }
            }
        }
    }
    
    public static void registerFixesFallingBlock(final DataFixer fixer) {
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        final Block block = (this.fallTile != null) ? this.fallTile.getBlock() : Blocks.AIR;
        final ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(block);
        compound.setString("Block", (resourcelocation == null) ? "" : resourcelocation.toString());
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
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        final int i = compound.getByte("Data") & 0xFF;
        if (compound.hasKey("Block", 8)) {
            this.fallTile = Block.getBlockFromName(compound.getString("Block")).getStateFromMeta(i);
        }
        else if (compound.hasKey("TileID", 99)) {
            this.fallTile = Block.getBlockById(compound.getInteger("TileID")).getStateFromMeta(i);
        }
        else {
            this.fallTile = Block.getBlockById(compound.getByte("Tile") & 0xFF).getStateFromMeta(i);
        }
        this.fallTime = compound.getInteger("Time");
        final Block block = this.fallTile.getBlock();
        if (compound.hasKey("HurtEntities", 99)) {
            this.hurtEntities = compound.getBoolean("HurtEntities");
            this.fallHurtAmount = compound.getFloat("FallHurtAmount");
            this.fallHurtMax = compound.getInteger("FallHurtMax");
        }
        else if (block == Blocks.ANVIL) {
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
    
    public void setHurtEntities(final boolean p_145806_1_) {
        this.hurtEntities = p_145806_1_;
    }
    
    @Override
    public boolean canRenderOnFire() {
        return false;
    }
    
    @Override
    public void addEntityCrashInfo(final CrashReportCategory category) {
        super.addEntityCrashInfo(category);
        if (this.fallTile != null) {
            final Block block = this.fallTile.getBlock();
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
    
    static {
        ORIGIN = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.BLOCK_POS);
    }
}
