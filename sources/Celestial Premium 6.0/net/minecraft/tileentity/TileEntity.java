/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> field_190562_f = new RegistryNamespaced();
    protected World world;
    protected BlockPos pos = BlockPos.ORIGIN;
    protected boolean tileEntityInvalid;
    private int blockMetadata = -1;
    protected Block blockType;

    private static void func_190560_a(String p_190560_0_, Class<? extends TileEntity> p_190560_1_) {
        field_190562_f.putObject(new ResourceLocation(p_190560_0_), p_190560_1_);
    }

    @Nullable
    public static ResourceLocation func_190559_a(Class<? extends TileEntity> p_190559_0_) {
        return field_190562_f.getNameForObject(p_190559_0_);
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorldObj(World worldIn) {
        this.world = worldIn;
    }

    public boolean hasWorldObj() {
        return this.world != null;
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return this.writeInternal(compound);
    }

    private NBTTagCompound writeInternal(NBTTagCompound compound) {
        ResourceLocation resourcelocation = field_190562_f.getNameForObject(this.getClass());
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
    public static TileEntity create(World worldIn, NBTTagCompound compound) {
        TileEntity tileentity = null;
        String s = compound.getString("id");
        try {
            Class<? extends TileEntity> oclass = field_190562_f.getObject(new ResourceLocation(s));
            if (oclass != null) {
                tileentity = oclass.newInstance();
            }
        }
        catch (Throwable throwable1) {
            LOGGER.error("Failed to create block entity {}", s, throwable1);
        }
        if (tileentity != null) {
            try {
                tileentity.setWorldCreate(worldIn);
                tileentity.readFromNBT(compound);
            }
            catch (Throwable throwable) {
                LOGGER.error("Failed to load data for block entity {}", s, throwable);
                tileentity = null;
            }
        } else {
            LOGGER.warn("Skipping BlockEntity with id {}", s);
        }
        return tileentity;
    }

    protected void setWorldCreate(World worldIn) {
    }

    public int getBlockMetadata() {
        if (this.blockMetadata == -1) {
            IBlockState iblockstate = this.world.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
        }
        return this.blockMetadata;
    }

    public void markDirty() {
        if (this.world != null) {
            IBlockState iblockstate = this.world.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
            this.world.markChunkDirty(this.pos, this);
            if (this.getBlockType() != Blocks.AIR) {
                this.world.updateComparatorOutputLevel(this.pos, this.getBlockType());
            }
        }
    }

    public double getDistanceSq(double x, double y, double z) {
        double d0 = (double)this.pos.getX() + 0.5 - x;
        double d1 = (double)this.pos.getY() + 0.5 - y;
        double d2 = (double)this.pos.getZ() + 0.5 - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
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

    public boolean receiveClientEvent(int id, int type) {
        return false;
    }

    public void updateContainingBlockInfo() {
        this.blockType = null;
        this.blockMetadata = -1;
    }

    public void addInfoToCrashReport(CrashReportCategory reportCategory) {
        reportCategory.setDetail("Name", new ICrashReportDetail<String>(){

            @Override
            public String call() throws Exception {
                return field_190562_f.getNameForObject(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });
        if (this.world != null) {
            CrashReportCategory.addBlockInfo(reportCategory, this.pos, this.getBlockType(), this.getBlockMetadata());
            reportCategory.setDetail("Actual block type", new ICrashReportDetail<String>(){

                @Override
                public String call() throws Exception {
                    int i = Block.getIdFromBlock(TileEntity.this.world.getBlockState(TileEntity.this.pos).getBlock());
                    try {
                        return String.format("ID #%d (%s // %s)", i, Block.getBlockById(i).getUnlocalizedName(), Block.getBlockById(i).getClass().getCanonicalName());
                    }
                    catch (Throwable var3) {
                        return "ID #" + i;
                    }
                }
            });
            reportCategory.setDetail("Actual block data value", new ICrashReportDetail<String>(){

                @Override
                public String call() throws Exception {
                    IBlockState iblockstate = TileEntity.this.world.getBlockState(TileEntity.this.pos);
                    int i = iblockstate.getBlock().getMetaFromState(iblockstate);
                    if (i < 0) {
                        return "Unknown? (Got " + i + ")";
                    }
                    String s = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
                    return String.format("%1$d / 0x%1$X / 0b%2$s", i, s);
                }
            });
        }
    }

    public void setPos(BlockPos posIn) {
        this.pos = posIn.toImmutable();
    }

    public boolean onlyOpsCanSetNbt() {
        return false;
    }

    @Nullable
    public ITextComponent getDisplayName() {
        return null;
    }

    public void rotate(Rotation p_189667_1_) {
    }

    public void mirror(Mirror p_189668_1_) {
    }

    static {
        TileEntity.func_190560_a("furnace", TileEntityFurnace.class);
        TileEntity.func_190560_a("chest", TileEntityChest.class);
        TileEntity.func_190560_a("ender_chest", TileEntityEnderChest.class);
        TileEntity.func_190560_a("jukebox", BlockJukebox.TileEntityJukebox.class);
        TileEntity.func_190560_a("dispenser", TileEntityDispenser.class);
        TileEntity.func_190560_a("dropper", TileEntityDropper.class);
        TileEntity.func_190560_a("sign", TileEntitySign.class);
        TileEntity.func_190560_a("mob_spawner", TileEntityMobSpawner.class);
        TileEntity.func_190560_a("noteblock", TileEntityNote.class);
        TileEntity.func_190560_a("piston", TileEntityPiston.class);
        TileEntity.func_190560_a("brewing_stand", TileEntityBrewingStand.class);
        TileEntity.func_190560_a("enchanting_table", TileEntityEnchantmentTable.class);
        TileEntity.func_190560_a("end_portal", TileEntityEndPortal.class);
        TileEntity.func_190560_a("beacon", TileEntityBeacon.class);
        TileEntity.func_190560_a("skull", TileEntitySkull.class);
        TileEntity.func_190560_a("daylight_detector", TileEntityDaylightDetector.class);
        TileEntity.func_190560_a("hopper", TileEntityHopper.class);
        TileEntity.func_190560_a("comparator", TileEntityComparator.class);
        TileEntity.func_190560_a("flower_pot", TileEntityFlowerPot.class);
        TileEntity.func_190560_a("banner", TileEntityBanner.class);
        TileEntity.func_190560_a("structure_block", TileEntityStructure.class);
        TileEntity.func_190560_a("end_gateway", TileEntityEndGateway.class);
        TileEntity.func_190560_a("command_block", TileEntityCommandBlock.class);
        TileEntity.func_190560_a("shulker_box", TileEntityShulkerBox.class);
        TileEntity.func_190560_a("bed", TileEntityBed.class);
    }
}

