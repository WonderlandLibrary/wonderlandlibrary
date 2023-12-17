// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockFurnace;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public class EntityMinecartFurnace extends EntityMinecart
{
    private static final DataParameter<Boolean> POWERED;
    private int fuel;
    public double pushX;
    public double pushZ;
    
    public EntityMinecartFurnace(final World worldIn) {
        super(worldIn);
    }
    
    public EntityMinecartFurnace(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
    }
    
    public static void registerFixesMinecartFurnace(final DataFixer fixer) {
        EntityMinecart.registerFixesMinecart(fixer, EntityMinecartFurnace.class);
    }
    
    @Override
    public Type getType() {
        return Type.FURNACE;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityMinecartFurnace.POWERED, false);
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
    public void killMinecart(final DamageSource source) {
        super.killMinecart(source);
        if (!source.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(Blocks.FURNACE, 1), 0.0f);
        }
    }
    
    @Override
    protected void moveAlongTrack(final BlockPos pos, final IBlockState state) {
        super.moveAlongTrack(pos, state);
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
        if (d0 > 1.0E-4 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001) {
            d0 = MathHelper.sqrt(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0) {
                this.pushX = 0.0;
                this.pushZ = 0.0;
            }
            else {
                final double d2 = d0 / this.getMaximumSpeed();
                this.pushX *= d2;
                this.pushZ *= d2;
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
            final double d2 = 1.0;
            this.motionX *= 0.800000011920929;
            this.motionY *= 0.0;
            this.motionZ *= 0.800000011920929;
            this.motionX += this.pushX * 1.0;
            this.motionZ += this.pushZ * 1.0;
        }
        else {
            this.motionX *= 0.9800000190734863;
            this.motionY *= 0.0;
            this.motionZ *= 0.9800000190734863;
        }
        super.applyDrag();
    }
    
    @Override
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.COAL && this.fuel + 3600 <= 32000) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            this.fuel += 3600;
        }
        this.pushX = this.posX - player.posX;
        this.pushZ = this.posZ - player.posZ;
        return true;
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("PushX", this.pushX);
        compound.setDouble("PushZ", this.pushZ);
        compound.setShort("Fuel", (short)this.fuel);
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.pushX = compound.getDouble("PushX");
        this.pushZ = compound.getDouble("PushZ");
        this.fuel = compound.getShort("Fuel");
    }
    
    protected boolean isMinecartPowered() {
        return this.dataManager.get(EntityMinecartFurnace.POWERED);
    }
    
    protected void setMinecartPowered(final boolean p_94107_1_) {
        this.dataManager.set(EntityMinecartFurnace.POWERED, p_94107_1_);
    }
    
    @Override
    public IBlockState getDefaultDisplayTile() {
        return (this.isMinecartPowered() ? Blocks.LIT_FURNACE : Blocks.FURNACE).getDefaultState().withProperty((IProperty<Comparable>)BlockFurnace.FACING, EnumFacing.NORTH);
    }
    
    static {
        POWERED = EntityDataManager.createKey(EntityMinecartFurnace.class, DataSerializers.BOOLEAN);
    }
}
