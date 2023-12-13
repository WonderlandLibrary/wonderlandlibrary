/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.tileentity;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity {
    private static final Logger logger = LogManager.getLogger();
    private static Map<String, Class<? extends TileEntity>> nameToClassMap = Maps.newHashMap();
    private static Map<Class<? extends TileEntity>, String> classToNameMap = Maps.newHashMap();
    protected World worldObj;
    protected BlockPos pos = BlockPos.ORIGIN;
    protected boolean tileEntityInvalid;
    private int blockMetadata = -1;
    protected Block blockType;

    private static void addMapping(Class<? extends TileEntity> cl, String id) {
        if (nameToClassMap.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate id: " + id);
        }
        nameToClassMap.put(id, cl);
        classToNameMap.put(cl, id);
    }

    public World getWorld() {
        return this.worldObj;
    }

    public void setWorldObj(World worldIn) {
        this.worldObj = worldIn;
    }

    public boolean hasWorldObj() {
        return this.worldObj != null;
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
    }

    public void writeToNBT(NBTTagCompound compound) {
        String s2 = classToNameMap.get(this.getClass());
        if (s2 == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        compound.setString("id", s2);
        compound.setInteger("x", this.pos.getX());
        compound.setInteger("y", this.pos.getY());
        compound.setInteger("z", this.pos.getZ());
    }

    public static TileEntity createAndLoadEntity(NBTTagCompound nbt) {
        TileEntity tileentity = null;
        try {
            Class<? extends TileEntity> oclass = nameToClassMap.get(nbt.getString("id"));
            if (oclass != null) {
                tileentity = oclass.newInstance();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (tileentity != null) {
            tileentity.readFromNBT(nbt);
        } else {
            logger.warn("Skipping BlockEntity with id " + nbt.getString("id"));
        }
        return tileentity;
    }

    public int getBlockMetadata() {
        if (this.blockMetadata == -1) {
            IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
        }
        return this.blockMetadata;
    }

    public void markDirty() {
        if (this.worldObj != null) {
            IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
            this.worldObj.markChunkDirty(this.pos, this);
            if (this.getBlockType() != Blocks.air) {
                this.worldObj.updateComparatorOutputLevel(this.pos, this.getBlockType());
            }
        }
    }

    public double getDistanceSq(double x2, double y2, double z2) {
        double d0 = (double)this.pos.getX() + 0.5 - x2;
        double d1 = (double)this.pos.getY() + 0.5 - y2;
        double d2 = (double)this.pos.getZ() + 0.5 - z2;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public double getMaxRenderDistanceSquared() {
        return 4096.0;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Block getBlockType() {
        if (this.blockType == null) {
            this.blockType = this.worldObj.getBlockState(this.pos).getBlock();
        }
        return this.blockType;
    }

    public Packet getDescriptionPacket() {
        return null;
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
        reportCategory.addCrashSectionCallable("Name", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return (String)classToNameMap.get(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });
        if (this.worldObj != null) {
            CrashReportCategory.addBlockInfo(reportCategory, this.pos, this.getBlockType(), this.getBlockMetadata());
            reportCategory.addCrashSectionCallable("Actual block type", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    int i2 = Block.getIdFromBlock(TileEntity.this.worldObj.getBlockState(TileEntity.this.pos).getBlock());
                    try {
                        return String.format("ID #%d (%s // %s)", i2, Block.getBlockById(i2).getUnlocalizedName(), Block.getBlockById(i2).getClass().getCanonicalName());
                    }
                    catch (Throwable var3) {
                        return "ID #" + i2;
                    }
                }
            });
            reportCategory.addCrashSectionCallable("Actual block data value", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    IBlockState iblockstate = TileEntity.this.worldObj.getBlockState(TileEntity.this.pos);
                    int i2 = iblockstate.getBlock().getMetaFromState(iblockstate);
                    if (i2 < 0) {
                        return "Unknown? (Got " + i2 + ")";
                    }
                    String s2 = String.format("%4s", Integer.toBinaryString(i2)).replace(" ", "0");
                    return String.format("%1$d / 0x%1$X / 0b%2$s", i2, s2);
                }
            });
        }
    }

    public void setPos(BlockPos posIn) {
        this.pos = posIn;
    }

    public boolean func_183000_F() {
        return false;
    }

    static {
        TileEntity.addMapping(TileEntityFurnace.class, "Furnace");
        TileEntity.addMapping(TileEntityChest.class, "Chest");
        TileEntity.addMapping(TileEntityEnderChest.class, "EnderChest");
        TileEntity.addMapping(BlockJukebox.TileEntityJukebox.class, "RecordPlayer");
        TileEntity.addMapping(TileEntityDispenser.class, "Trap");
        TileEntity.addMapping(TileEntityDropper.class, "Dropper");
        TileEntity.addMapping(TileEntitySign.class, "Sign");
        TileEntity.addMapping(TileEntityMobSpawner.class, "MobSpawner");
        TileEntity.addMapping(TileEntityNote.class, "Music");
        TileEntity.addMapping(TileEntityPiston.class, "Piston");
        TileEntity.addMapping(TileEntityBrewingStand.class, "Cauldron");
        TileEntity.addMapping(TileEntityEnchantmentTable.class, "EnchantTable");
        TileEntity.addMapping(TileEntityEndPortal.class, "Airportal");
        TileEntity.addMapping(TileEntityCommandBlock.class, "Control");
        TileEntity.addMapping(TileEntityBeacon.class, "Beacon");
        TileEntity.addMapping(TileEntitySkull.class, "Skull");
        TileEntity.addMapping(TileEntityDaylightDetector.class, "DLDetector");
        TileEntity.addMapping(TileEntityHopper.class, "Hopper");
        TileEntity.addMapping(TileEntityComparator.class, "Comparator");
        TileEntity.addMapping(TileEntityFlowerPot.class, "FlowerPot");
        TileEntity.addMapping(TileEntityBanner.class, "Banner");
    }
}

