// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import java.util.Random;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProviderEnd;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.MathHelper;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ITickable;

public class TileEntityEndGateway extends TileEntityEndPortal implements ITickable
{
    private static final Logger LOGGER;
    private long age;
    private int teleportCooldown;
    private BlockPos exitPortal;
    private boolean exactTeleport;
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setLong("Age", this.age);
        if (this.exitPortal != null) {
            compound.setTag("ExitPortal", NBTUtil.createPosTag(this.exitPortal));
        }
        if (this.exactTeleport) {
            compound.setBoolean("ExactTeleport", this.exactTeleport);
        }
        return compound;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.age = compound.getLong("Age");
        if (compound.hasKey("ExitPortal", 10)) {
            this.exitPortal = NBTUtil.getPosFromTag(compound.getCompoundTag("ExitPortal"));
        }
        this.exactTeleport = compound.getBoolean("ExactTeleport");
    }
    
    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536.0;
    }
    
    @Override
    public void update() {
        final boolean flag = this.isSpawning();
        final boolean flag2 = this.isCoolingDown();
        ++this.age;
        if (flag2) {
            --this.teleportCooldown;
        }
        else if (!this.world.isRemote) {
            final List<Entity> list = this.world.getEntitiesWithinAABB((Class<? extends Entity>)Entity.class, new AxisAlignedBB(this.getPos()));
            if (!list.isEmpty()) {
                this.teleportEntity(list.get(0));
            }
            if (this.age % 2400L == 0L) {
                this.triggerCooldown();
            }
        }
        if (flag != this.isSpawning() || flag2 != this.isCoolingDown()) {
            this.markDirty();
        }
    }
    
    public boolean isSpawning() {
        return this.age < 200L;
    }
    
    public boolean isCoolingDown() {
        return this.teleportCooldown > 0;
    }
    
    public float getSpawnPercent(final float p_184302_1_) {
        return MathHelper.clamp((this.age + p_184302_1_) / 200.0f, 0.0f, 1.0f);
    }
    
    public float getCooldownPercent(final float p_184305_1_) {
        return 1.0f - MathHelper.clamp((this.teleportCooldown - p_184305_1_) / 40.0f, 0.0f, 1.0f);
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 8, this.getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    public void triggerCooldown() {
        if (!this.world.isRemote) {
            this.teleportCooldown = 40;
            this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, 0);
            this.markDirty();
        }
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            this.teleportCooldown = 40;
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    public void teleportEntity(final Entity entityIn) {
        if (!this.world.isRemote && !this.isCoolingDown()) {
            this.teleportCooldown = 100;
            if (this.exitPortal == null && this.world.provider instanceof WorldProviderEnd) {
                this.findExitPortal();
            }
            if (this.exitPortal != null) {
                final BlockPos blockpos = this.exactTeleport ? this.exitPortal : this.findExitPosition();
                entityIn.setPositionAndUpdate(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5);
            }
            this.triggerCooldown();
        }
    }
    
    private BlockPos findExitPosition() {
        final BlockPos blockpos = findHighestBlock(this.world, this.exitPortal, 5, false);
        TileEntityEndGateway.LOGGER.debug("Best exit position for portal at {} is {}", (Object)this.exitPortal, (Object)blockpos);
        return blockpos.up();
    }
    
    private void findExitPortal() {
        final Vec3d vec3d = new Vec3d(this.getPos().getX(), 0.0, this.getPos().getZ()).normalize();
        Vec3d vec3d2 = vec3d.scale(1024.0);
        for (int i = 16; getChunk(this.world, vec3d2).getTopFilledSegment() > 0 && i-- > 0; vec3d2 = vec3d2.add(vec3d.scale(-16.0))) {
            TileEntityEndGateway.LOGGER.debug("Skipping backwards past nonempty chunk at {}", (Object)vec3d2);
        }
        for (int j = 16; getChunk(this.world, vec3d2).getTopFilledSegment() == 0 && j-- > 0; vec3d2 = vec3d2.add(vec3d.scale(16.0))) {
            TileEntityEndGateway.LOGGER.debug("Skipping forward past empty chunk at {}", (Object)vec3d2);
        }
        TileEntityEndGateway.LOGGER.debug("Found chunk at {}", (Object)vec3d2);
        final Chunk chunk = getChunk(this.world, vec3d2);
        this.exitPortal = findSpawnpointInChunk(chunk);
        if (this.exitPortal == null) {
            this.exitPortal = new BlockPos(vec3d2.x + 0.5, 75.0, vec3d2.z + 0.5);
            TileEntityEndGateway.LOGGER.debug("Failed to find suitable block, settling on {}", (Object)this.exitPortal);
            new WorldGenEndIsland().generate(this.world, new Random(this.exitPortal.toLong()), this.exitPortal);
        }
        else {
            TileEntityEndGateway.LOGGER.debug("Found block at {}", (Object)this.exitPortal);
        }
        this.exitPortal = findHighestBlock(this.world, this.exitPortal, 16, true);
        TileEntityEndGateway.LOGGER.debug("Creating portal at {}", (Object)this.exitPortal);
        this.createExitPortal(this.exitPortal = this.exitPortal.up(10));
        this.markDirty();
    }
    
    private static BlockPos findHighestBlock(final World p_184308_0_, final BlockPos p_184308_1_, final int p_184308_2_, final boolean p_184308_3_) {
        BlockPos blockpos = null;
        for (int i = -p_184308_2_; i <= p_184308_2_; ++i) {
            for (int j = -p_184308_2_; j <= p_184308_2_; ++j) {
                if (i != 0 || j != 0 || p_184308_3_) {
                    for (int k = 255; k > ((blockpos == null) ? 0 : blockpos.getY()); --k) {
                        final BlockPos blockpos2 = new BlockPos(p_184308_1_.getX() + i, k, p_184308_1_.getZ() + j);
                        final IBlockState iblockstate = p_184308_0_.getBlockState(blockpos2);
                        if (iblockstate.isBlockNormalCube() && (p_184308_3_ || iblockstate.getBlock() != Blocks.BEDROCK)) {
                            blockpos = blockpos2;
                            break;
                        }
                    }
                }
            }
        }
        return (blockpos == null) ? p_184308_1_ : blockpos;
    }
    
    private static Chunk getChunk(final World worldIn, final Vec3d vec3) {
        return worldIn.getChunk(MathHelper.floor(vec3.x / 16.0), MathHelper.floor(vec3.z / 16.0));
    }
    
    @Nullable
    private static BlockPos findSpawnpointInChunk(final Chunk chunkIn) {
        final BlockPos blockpos = new BlockPos(chunkIn.x * 16, 30, chunkIn.z * 16);
        final int i = chunkIn.getTopFilledSegment() + 16 - 1;
        final BlockPos blockpos2 = new BlockPos(chunkIn.x * 16 + 16 - 1, i, chunkIn.z * 16 + 16 - 1);
        BlockPos blockpos3 = null;
        double d0 = 0.0;
        for (final BlockPos blockpos4 : BlockPos.getAllInBox(blockpos, blockpos2)) {
            final IBlockState iblockstate = chunkIn.getBlockState(blockpos4);
            if (iblockstate.getBlock() == Blocks.END_STONE && !chunkIn.getBlockState(blockpos4.up(1)).isBlockNormalCube() && !chunkIn.getBlockState(blockpos4.up(2)).isBlockNormalCube()) {
                final double d2 = blockpos4.distanceSqToCenter(0.0, 0.0, 0.0);
                if (blockpos3 != null && d2 >= d0) {
                    continue;
                }
                blockpos3 = blockpos4;
                d0 = d2;
            }
        }
        return blockpos3;
    }
    
    private void createExitPortal(final BlockPos posIn) {
        new WorldGenEndGateway().generate(this.world, new Random(), posIn);
        final TileEntity tileentity = this.world.getTileEntity(posIn);
        if (tileentity instanceof TileEntityEndGateway) {
            final TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;
            tileentityendgateway.exitPortal = new BlockPos(this.getPos());
            tileentityendgateway.markDirty();
        }
        else {
            TileEntityEndGateway.LOGGER.warn("Couldn't save exit portal at {}", (Object)posIn);
        }
    }
    
    @Override
    public boolean shouldRenderFace(final EnumFacing p_184313_1_) {
        return this.getBlockType().getDefaultState().shouldSideBeRendered(this.world, this.getPos(), p_184313_1_);
    }
    
    public int getParticleAmount() {
        int i = 0;
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            i += (this.shouldRenderFace(enumfacing) ? 1 : 0);
        }
        return i;
    }
    
    public void setExactPosition(final BlockPos p_190603_1_) {
        this.exactTeleport = true;
        this.exitPortal = p_190603_1_;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
