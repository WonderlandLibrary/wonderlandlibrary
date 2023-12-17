// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.block.BlockJukebox;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity
{
    private static final Logger LOGGER;
    private static final RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> REGISTRY;
    protected World world;
    protected BlockPos pos;
    protected boolean tileEntityInvalid;
    private int blockMetadata;
    protected Block blockType;
    
    public TileEntity() {
        this.pos = BlockPos.ORIGIN;
        this.blockMetadata = -1;
    }
    
    private static void register(final String id, final Class<? extends TileEntity> clazz) {
        TileEntity.REGISTRY.putObject(new ResourceLocation(id), clazz);
    }
    
    @Nullable
    public static ResourceLocation getKey(final Class<? extends TileEntity> clazz) {
        return TileEntity.REGISTRY.getNameForObject(clazz);
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public void setWorld(final World worldIn) {
        this.world = worldIn;
    }
    
    public boolean hasWorld() {
        return this.world != null;
    }
    
    public void readFromNBT(final NBTTagCompound compound) {
        this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
    }
    
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        return this.writeInternal(compound);
    }
    
    private NBTTagCompound writeInternal(final NBTTagCompound compound) {
        final ResourceLocation resourcelocation = TileEntity.REGISTRY.getNameForObject(this.getClass());
        if (resourcelocation == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        compound.setString("id", resourcelocation.toString());
        compound.setInteger("x", this.pos.getX());
        compound.setInteger("y", this.pos.getY());
        compound.setInteger("z", this.pos.getZ());
        return compound;
    }
    
    @Nullable
    public static TileEntity create(final World worldIn, final NBTTagCompound compound) {
        TileEntity tileentity = null;
        final String s = compound.getString("id");
        try {
            final Class<? extends TileEntity> oclass = TileEntity.REGISTRY.getObject(new ResourceLocation(s));
            if (oclass != null) {
                tileentity = (TileEntity)oclass.newInstance();
            }
        }
        catch (Throwable throwable1) {
            TileEntity.LOGGER.error("Failed to create block entity {}", (Object)s, (Object)throwable1);
        }
        if (tileentity != null) {
            try {
                tileentity.setWorldCreate(worldIn);
                tileentity.readFromNBT(compound);
            }
            catch (Throwable throwable2) {
                TileEntity.LOGGER.error("Failed to load data for block entity {}", (Object)s, (Object)throwable2);
                tileentity = null;
            }
        }
        else {
            TileEntity.LOGGER.warn("Skipping BlockEntity with id {}", (Object)s);
        }
        return tileentity;
    }
    
    protected void setWorldCreate(final World worldIn) {
    }
    
    public int getBlockMetadata() {
        if (this.blockMetadata == -1) {
            final IBlockState iblockstate = this.world.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
        }
        return this.blockMetadata;
    }
    
    public void markDirty() {
        if (this.world != null) {
            final IBlockState iblockstate = this.world.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
            this.world.markChunkDirty(this.pos, this);
            if (this.getBlockType() != Blocks.AIR) {
                this.world.updateComparatorOutputLevel(this.pos, this.getBlockType());
            }
        }
    }
    
    public double getDistanceSq(final double x, final double y, final double z) {
        final double d0 = this.pos.getX() + 0.5 - x;
        final double d2 = this.pos.getY() + 0.5 - y;
        final double d3 = this.pos.getZ() + 0.5 - z;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double getMaxRenderDistanceSquared() {
        return 4096.0;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Block getBlockType() {
        if (this.blockType == null && this.world != null) {
            this.blockType = this.world.getBlockState(this.pos).getBlock();
        }
        return this.blockType;
    }
    
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return null;
    }
    
    public NBTTagCompound getUpdateTag() {
        return this.writeInternal(new NBTTagCompound());
    }
    
    public boolean isInvalid() {
        return this.tileEntityInvalid;
    }
    
    public void invalidate() {
        this.tileEntityInvalid = true;
    }
    
    public void validate() {
        this.tileEntityInvalid = false;
    }
    
    public boolean receiveClientEvent(final int id, final int type) {
        return false;
    }
    
    public void updateContainingBlockInfo() {
        this.blockType = null;
        this.blockMetadata = -1;
    }
    
    public void addInfoToCrashReport(final CrashReportCategory reportCategory) {
        reportCategory.addDetail("Name", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return TileEntity.REGISTRY.getNameForObject(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });
        if (this.world != null) {
            CrashReportCategory.addBlockInfo(reportCategory, this.pos, this.getBlockType(), this.getBlockMetadata());
            reportCategory.addDetail("Actual block type", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    final int i = Block.getIdFromBlock(TileEntity.this.world.getBlockState(TileEntity.this.pos).getBlock());
                    try {
                        return String.format("ID #%d (%s // %s)", i, Block.getBlockById(i).getTranslationKey(), Block.getBlockById(i).getClass().getCanonicalName());
                    }
                    catch (Throwable var3) {
                        return "ID #" + i;
                    }
                }
            });
            reportCategory.addDetail("Actual block data value", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    final IBlockState iblockstate = TileEntity.this.world.getBlockState(TileEntity.this.pos);
                    final int i = iblockstate.getBlock().getMetaFromState(iblockstate);
                    if (i < 0) {
                        return "Unknown? (Got " + i + ")";
                    }
                    final String s = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
                    return String.format("%1$d / 0x%1$X / 0b%2$s", i, s);
                }
            });
        }
    }
    
    public void setPos(final BlockPos posIn) {
        this.pos = posIn.toImmutable();
    }
    
    public boolean onlyOpsCanSetNbt() {
        return false;
    }
    
    @Nullable
    public ITextComponent getDisplayName() {
        return null;
    }
    
    public void rotate(final Rotation rotationIn) {
    }
    
    public void mirror(final Mirror mirrorIn) {
    }
    
    static {
        LOGGER = LogManager.getLogger();
        REGISTRY = new RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>>();
        register("furnace", TileEntityFurnace.class);
        register("chest", TileEntityChest.class);
        register("ender_chest", TileEntityEnderChest.class);
        register("jukebox", BlockJukebox.TileEntityJukebox.class);
        register("dispenser", TileEntityDispenser.class);
        register("dropper", TileEntityDropper.class);
        register("sign", TileEntitySign.class);
        register("mob_spawner", TileEntityMobSpawner.class);
        register("noteblock", TileEntityNote.class);
        register("piston", TileEntityPiston.class);
        register("brewing_stand", TileEntityBrewingStand.class);
        register("enchanting_table", TileEntityEnchantmentTable.class);
        register("end_portal", TileEntityEndPortal.class);
        register("beacon", TileEntityBeacon.class);
        register("skull", TileEntitySkull.class);
        register("daylight_detector", TileEntityDaylightDetector.class);
        register("hopper", TileEntityHopper.class);
        register("comparator", TileEntityComparator.class);
        register("flower_pot", TileEntityFlowerPot.class);
        register("banner", TileEntityBanner.class);
        register("structure_block", TileEntityStructure.class);
        register("end_gateway", TileEntityEndGateway.class);
        register("command_block", TileEntityCommandBlock.class);
        register("shulker_box", TileEntityShulkerBox.class);
        register("bed", TileEntityBed.class);
    }
}
