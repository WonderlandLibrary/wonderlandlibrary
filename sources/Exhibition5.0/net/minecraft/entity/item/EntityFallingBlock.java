// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockAnvil;
import net.minecraft.util.DamageSource;
import com.google.common.collect.Lists;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTBase;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.BlockFalling;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;

public class EntityFallingBlock extends Entity
{
    private IBlockState field_175132_d;
    public int fallTime;
    public boolean shouldDropItem;
    private boolean field_145808_f;
    private boolean hurtEntities;
    private int fallHurtMax;
    private float fallHurtAmount;
    public NBTTagCompound tileEntityData;
    private static final String __OBFID = "CL_00001668";
    
    public EntityFallingBlock(final World worldIn) {
        super(worldIn);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0f;
    }
    
    public EntityFallingBlock(final World worldIn, final double p_i45848_2_, final double p_i45848_4_, final double p_i45848_6_, final IBlockState p_i45848_8_) {
        super(worldIn);
        this.shouldDropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0f;
        this.field_175132_d = p_i45848_8_;
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.98f);
        this.setPosition(p_i45848_2_, p_i45848_4_, p_i45848_6_);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = p_i45848_2_;
        this.prevPosY = p_i45848_4_;
        this.prevPosZ = p_i45848_6_;
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public void onUpdate() {
        final Block var1 = this.field_175132_d.getBlock();
        if (var1.getMaterial() == Material.air) {
            this.setDead();
        }
        else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            if (this.fallTime++ == 0) {
                final BlockPos var2 = new BlockPos(this);
                if (this.worldObj.getBlockState(var2).getBlock() == var1) {
                    this.worldObj.setBlockToAir(var2);
                }
                else if (!this.worldObj.isRemote) {
                    this.setDead();
                    return;
                }
            }
            this.motionY -= 0.03999999910593033;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863;
            this.motionY *= 0.9800000190734863;
            this.motionZ *= 0.9800000190734863;
            if (!this.worldObj.isRemote) {
                final BlockPos var2 = new BlockPos(this);
                if (this.onGround) {
                    this.motionX *= 0.699999988079071;
                    this.motionZ *= 0.699999988079071;
                    this.motionY *= -0.5;
                    if (this.worldObj.getBlockState(var2).getBlock() != Blocks.piston_extension) {
                        this.setDead();
                        if (!this.field_145808_f && this.worldObj.canBlockBePlaced(var1, var2, true, EnumFacing.UP, null, null) && !BlockFalling.canFallInto(this.worldObj, var2.offsetDown()) && this.worldObj.setBlockState(var2, this.field_175132_d, 3)) {
                            if (var1 instanceof BlockFalling) {
                                ((BlockFalling)var1).onEndFalling(this.worldObj, var2);
                            }
                            if (this.tileEntityData != null && var1 instanceof ITileEntityProvider) {
                                final TileEntity var3 = this.worldObj.getTileEntity(var2);
                                if (var3 != null) {
                                    final NBTTagCompound var4 = new NBTTagCompound();
                                    var3.writeToNBT(var4);
                                    for (final String var6 : this.tileEntityData.getKeySet()) {
                                        final NBTBase var7 = this.tileEntityData.getTag(var6);
                                        if (!var6.equals("x") && !var6.equals("y") && !var6.equals("z")) {
                                            var4.setTag(var6, var7.copy());
                                        }
                                    }
                                    var3.readFromNBT(var4);
                                    var3.markDirty();
                                }
                            }
                        }
                        else if (this.shouldDropItem && !this.field_145808_f && this.worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
                            this.entityDropItem(new ItemStack(var1, 1, var1.damageDropped(this.field_175132_d)), 0.0f);
                        }
                    }
                }
                else if ((this.fallTime > 100 && !this.worldObj.isRemote && (var2.getY() < 1 || var2.getY() > 256)) || this.fallTime > 600) {
                    if (this.shouldDropItem && this.worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
                        this.entityDropItem(new ItemStack(var1, 1, var1.damageDropped(this.field_175132_d)), 0.0f);
                    }
                    this.setDead();
                }
            }
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        final Block var3 = this.field_175132_d.getBlock();
        if (this.hurtEntities) {
            final int var4 = MathHelper.ceiling_float_int(distance - 1.0f);
            if (var4 > 0) {
                final ArrayList var5 = Lists.newArrayList((Iterable)this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                final boolean var6 = var3 == Blocks.anvil;
                final DamageSource var7 = var6 ? DamageSource.anvil : DamageSource.fallingBlock;
                for (final Entity var9 : var5) {
                    var9.attackEntityFrom(var7, Math.min(MathHelper.floor_float(var4 * this.fallHurtAmount), this.fallHurtMax));
                }
                if (var6 && this.rand.nextFloat() < 0.05000000074505806 + var4 * 0.05) {
                    int var10 = (int)this.field_175132_d.getValue(BlockAnvil.DAMAGE);
                    if (++var10 > 2) {
                        this.field_145808_f = true;
                    }
                    else {
                        this.field_175132_d = this.field_175132_d.withProperty(BlockAnvil.DAMAGE, var10);
                    }
                }
            }
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        final Block var2 = (this.field_175132_d != null) ? this.field_175132_d.getBlock() : Blocks.air;
        final ResourceLocation var3 = (ResourceLocation)Block.blockRegistry.getNameForObject(var2);
        tagCompound.setString("Block", (var3 == null) ? "" : var3.toString());
        tagCompound.setByte("Data", (byte)var2.getMetaFromState(this.field_175132_d));
        tagCompound.setByte("Time", (byte)this.fallTime);
        tagCompound.setBoolean("DropItem", this.shouldDropItem);
        tagCompound.setBoolean("HurtEntities", this.hurtEntities);
        tagCompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        tagCompound.setInteger("FallHurtMax", this.fallHurtMax);
        if (this.tileEntityData != null) {
            tagCompound.setTag("TileEntityData", this.tileEntityData);
        }
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        final int var2 = tagCompund.getByte("Data") & 0xFF;
        if (tagCompund.hasKey("Block", 8)) {
            this.field_175132_d = Block.getBlockFromName(tagCompund.getString("Block")).getStateFromMeta(var2);
        }
        else if (tagCompund.hasKey("TileID", 99)) {
            this.field_175132_d = Block.getBlockById(tagCompund.getInteger("TileID")).getStateFromMeta(var2);
        }
        else {
            this.field_175132_d = Block.getBlockById(tagCompund.getByte("Tile") & 0xFF).getStateFromMeta(var2);
        }
        this.fallTime = (tagCompund.getByte("Time") & 0xFF);
        final Block var3 = this.field_175132_d.getBlock();
        if (tagCompund.hasKey("HurtEntities", 99)) {
            this.hurtEntities = tagCompund.getBoolean("HurtEntities");
            this.fallHurtAmount = tagCompund.getFloat("FallHurtAmount");
            this.fallHurtMax = tagCompund.getInteger("FallHurtMax");
        }
        else if (var3 == Blocks.anvil) {
            this.hurtEntities = true;
        }
        if (tagCompund.hasKey("DropItem", 99)) {
            this.shouldDropItem = tagCompund.getBoolean("DropItem");
        }
        if (tagCompund.hasKey("TileEntityData", 10)) {
            this.tileEntityData = tagCompund.getCompoundTag("TileEntityData");
        }
        if (var3 == null || var3.getMaterial() == Material.air) {
            this.field_175132_d = Blocks.sand.getDefaultState();
        }
    }
    
    public World getWorldObj() {
        return this.worldObj;
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
        if (this.field_175132_d != null) {
            final Block var2 = this.field_175132_d.getBlock();
            category.addCrashSection("Immitating block ID", Block.getIdFromBlock(var2));
            category.addCrashSection("Immitating block data", var2.getMetaFromState(this.field_175132_d));
        }
    }
    
    public IBlockState getBlock() {
        return this.field_175132_d;
    }
}
