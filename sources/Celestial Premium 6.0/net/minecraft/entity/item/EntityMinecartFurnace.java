/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityMinecartFurnace
extends EntityMinecart {
    private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(EntityMinecartFurnace.class, DataSerializers.BOOLEAN);
    private int fuel;
    public double pushX;
    public double pushZ;

    public EntityMinecartFurnace(World worldIn) {
        super(worldIn);
    }

    public EntityMinecartFurnace(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public static void registerFixesMinecartFurnace(DataFixer fixer) {
        EntityMinecart.registerFixesMinecart(fixer, EntityMinecartFurnace.class);
    }

    @Override
    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.FURNACE;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(POWERED, false);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.fuel > 0) {
            --this.fuel;
        }
        if (this.fuel <= 0) {
            this.pushX = 0.0;
            this.pushZ = 0.0;
        }
        this.setMinecartPowered(this.fuel > 0);
        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
            this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    @Override
    protected double getMaximumSpeed() {
        return 0.2;
    }

    @Override
    public void killMinecart(DamageSource source) {
        super.killMinecart(source);
        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(Blocks.FURNACE, 1), 0.0f);
        }
    }

    @Override
    protected void moveAlongTrack(BlockPos pos, IBlockState state) {
        super.moveAlongTrack(pos, state);
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (d0 > 1.0E-4 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001) {
            d0 = MathHelper.sqrt(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0) {
                this.pushX = 0.0;
                this.pushZ = 0.0;
            } else {
                double d1 = d0 / this.getMaximumSpeed();
                this.pushX *= d1;
                this.pushZ *= d1;
            }
        }
    }

    @Override
    protected void applyDrag() {
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (d0 > 1.0E-4) {
            d0 = MathHelper.sqrt(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            double d1 = 1.0;
            this.motionX *= (double)0.8f;
            this.motionY *= 0.0;
            this.motionZ *= (double)0.8f;
            this.motionX += this.pushX * 1.0;
            this.motionZ += this.pushZ * 1.0;
        } else {
            this.motionX *= (double)0.98f;
            this.motionY *= 0.0;
            this.motionZ *= (double)0.98f;
        }
        super.applyDrag();
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
        ItemStack itemstack = player.getHeldItem(stack);
        if (itemstack.getItem() == Items.COAL && this.fuel + 3600 <= 32000) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            this.fuel += 3600;
        }
        this.pushX = this.posX - player.posX;
        this.pushZ = this.posZ - player.posZ;
        return true;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("PushX", this.pushX);
        compound.setDouble("PushZ", this.pushZ);
        compound.setShort("Fuel", (short)this.fuel);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.pushX = compound.getDouble("PushX");
        this.pushZ = compound.getDouble("PushZ");
        this.fuel = compound.getShort("Fuel");
    }

    protected boolean isMinecartPowered() {
        return this.dataManager.get(POWERED);
    }

    protected void setMinecartPowered(boolean p_94107_1_) {
        this.dataManager.set(POWERED, p_94107_1_);
    }

    @Override
    public IBlockState getDefaultDisplayTile() {
        return (this.isMinecartPowered() ? Blocks.LIT_FURNACE : Blocks.FURNACE).getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH);
    }
}

