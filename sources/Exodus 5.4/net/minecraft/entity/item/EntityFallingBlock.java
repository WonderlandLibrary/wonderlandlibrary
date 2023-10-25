/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityFallingBlock
extends Entity {
    public boolean shouldDropItem = true;
    private boolean canSetAsBlock;
    private IBlockState fallTile;
    public NBTTagCompound tileEntityData;
    private boolean hurtEntities;
    public int fallTime;
    private int fallHurtMax = 40;
    private float fallHurtAmount = 2.0f;

    public EntityFallingBlock(World world) {
        super(world);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        Block block = this.fallTile != null ? this.fallTile.getBlock() : Blocks.air;
        ResourceLocation resourceLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(block);
        nBTTagCompound.setString("Block", resourceLocation == null ? "" : resourceLocation.toString());
        nBTTagCompound.setByte("Data", (byte)block.getMetaFromState(this.fallTile));
        nBTTagCompound.setByte("Time", (byte)this.fallTime);
        nBTTagCompound.setBoolean("DropItem", this.shouldDropItem);
        nBTTagCompound.setBoolean("HurtEntities", this.hurtEntities);
        nBTTagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        nBTTagCompound.setInteger("FallHurtMax", this.fallHurtMax);
        if (this.tileEntityData != null) {
            nBTTagCompound.setTag("TileEntityData", this.tileEntityData);
        }
    }

    @Override
    public void fall(float f, float f2) {
        int n;
        Block block = this.fallTile.getBlock();
        if (this.hurtEntities && (n = MathHelper.ceiling_float_int(f - 1.0f)) > 0) {
            ArrayList arrayList = Lists.newArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
            boolean bl = block == Blocks.anvil;
            DamageSource damageSource = bl ? DamageSource.anvil : DamageSource.fallingBlock;
            for (Entity entity : arrayList) {
                entity.attackEntityFrom(damageSource, Math.min(MathHelper.floor_float((float)n * this.fallHurtAmount), this.fallHurtMax));
            }
            if (bl && (double)this.rand.nextFloat() < (double)0.05f + (double)n * 0.05) {
                int n2 = this.fallTile.getValue(BlockAnvil.DAMAGE);
                if (++n2 > 2) {
                    this.canSetAsBlock = true;
                } else {
                    this.fallTile = this.fallTile.withProperty(BlockAnvil.DAMAGE, n2);
                }
            }
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public void setHurtEntities(boolean bl) {
        this.hurtEntities = bl;
    }

    public EntityFallingBlock(World world, double d, double d2, double d3, IBlockState iBlockState) {
        super(world);
        this.fallTile = iBlockState;
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.98f);
        this.setPosition(d, d2, d3);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = d;
        this.prevPosY = d2;
        this.prevPosZ = d3;
    }

    public World getWorldObj() {
        return this.worldObj;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    public IBlockState getBlock() {
        return this.fallTile;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void addEntityCrashInfo(CrashReportCategory crashReportCategory) {
        super.addEntityCrashInfo(crashReportCategory);
        if (this.fallTile != null) {
            Block block = this.fallTile.getBlock();
            crashReportCategory.addCrashSection("Immitating block ID", Block.getIdFromBlock(block));
            crashReportCategory.addCrashSection("Immitating block data", block.getMetaFromState(this.fallTile));
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        int n = nBTTagCompound.getByte("Data") & 0xFF;
        this.fallTile = nBTTagCompound.hasKey("Block", 8) ? Block.getBlockFromName(nBTTagCompound.getString("Block")).getStateFromMeta(n) : (nBTTagCompound.hasKey("TileID", 99) ? Block.getBlockById(nBTTagCompound.getInteger("TileID")).getStateFromMeta(n) : Block.getBlockById(nBTTagCompound.getByte("Tile") & 0xFF).getStateFromMeta(n));
        this.fallTime = nBTTagCompound.getByte("Time") & 0xFF;
        Block block = this.fallTile.getBlock();
        if (nBTTagCompound.hasKey("HurtEntities", 99)) {
            this.hurtEntities = nBTTagCompound.getBoolean("HurtEntities");
            this.fallHurtAmount = nBTTagCompound.getFloat("FallHurtAmount");
            this.fallHurtMax = nBTTagCompound.getInteger("FallHurtMax");
        } else if (block == Blocks.anvil) {
            this.hurtEntities = true;
        }
        if (nBTTagCompound.hasKey("DropItem", 99)) {
            this.shouldDropItem = nBTTagCompound.getBoolean("DropItem");
        }
        if (nBTTagCompound.hasKey("TileEntityData", 10)) {
            this.tileEntityData = nBTTagCompound.getCompoundTag("TileEntityData");
        }
        if (block == null || block.getMaterial() == Material.air) {
            this.fallTile = Blocks.sand.getDefaultState();
        }
    }

    @Override
    public void onUpdate() {
        Block block = this.fallTile.getBlock();
        if (block.getMaterial() == Material.air) {
            this.setDead();
        } else {
            BlockPos blockPos;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            if (this.fallTime++ == 0) {
                blockPos = new BlockPos(this);
                if (this.worldObj.getBlockState(blockPos).getBlock() == block) {
                    this.worldObj.setBlockToAir(blockPos);
                } else if (!this.worldObj.isRemote) {
                    this.setDead();
                    return;
                }
            }
            this.motionY -= (double)0.04f;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)0.98f;
            this.motionY *= (double)0.98f;
            this.motionZ *= (double)0.98f;
            if (!this.worldObj.isRemote) {
                blockPos = new BlockPos(this);
                if (this.onGround) {
                    this.motionX *= (double)0.7f;
                    this.motionZ *= (double)0.7f;
                    this.motionY *= -0.5;
                    if (this.worldObj.getBlockState(blockPos).getBlock() != Blocks.piston_extension) {
                        this.setDead();
                        if (!this.canSetAsBlock) {
                            if (this.worldObj.canBlockBePlaced(block, blockPos, true, EnumFacing.UP, null, null) && !BlockFalling.canFallInto(this.worldObj, blockPos.down()) && this.worldObj.setBlockState(blockPos, this.fallTile, 3)) {
                                TileEntity tileEntity;
                                if (block instanceof BlockFalling) {
                                    ((BlockFalling)block).onEndFalling(this.worldObj, blockPos);
                                }
                                if (this.tileEntityData != null && block instanceof ITileEntityProvider && (tileEntity = this.worldObj.getTileEntity(blockPos)) != null) {
                                    NBTTagCompound nBTTagCompound = new NBTTagCompound();
                                    tileEntity.writeToNBT(nBTTagCompound);
                                    for (String string : this.tileEntityData.getKeySet()) {
                                        NBTBase nBTBase = this.tileEntityData.getTag(string);
                                        if (string.equals("x") || string.equals("y") || string.equals("z")) continue;
                                        nBTTagCompound.setTag(string, nBTBase.copy());
                                    }
                                    tileEntity.readFromNBT(nBTTagCompound);
                                    tileEntity.markDirty();
                                }
                            } else if (this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                                this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0f);
                            }
                        }
                    }
                } else if (this.fallTime > 100 && !this.worldObj.isRemote && (blockPos.getY() < 1 || blockPos.getY() > 256) || this.fallTime > 600) {
                    if (this.shouldDropItem && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                        this.entityDropItem(new ItemStack(block, 1, block.damageDropped(this.fallTile)), 0.0f);
                    }
                    this.setDead();
                }
            }
        }
    }

    @Override
    protected void entityInit() {
    }
}

