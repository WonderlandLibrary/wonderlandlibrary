/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
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
    protected boolean tileEntityInvalid;
    protected World worldObj;
    private int blockMetadata = -1;
    private static final Logger logger = LogManager.getLogger();
    private static Map<Class<? extends TileEntity>, String> classToNameMap;
    protected Block blockType;
    protected BlockPos pos = BlockPos.ORIGIN;
    private static Map<String, Class<? extends TileEntity>> nameToClassMap;

    public void addInfoToCrashReport(CrashReportCategory crashReportCategory) {
        crashReportCategory.addCrashSectionCallable("Name", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return String.valueOf((String)classToNameMap.get(TileEntity.this.getClass())) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });
        if (this.worldObj != null) {
            CrashReportCategory.addBlockInfo(crashReportCategory, this.pos, this.getBlockType(), this.getBlockMetadata());
            crashReportCategory.addCrashSectionCallable("Actual block type", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    int n = Block.getIdFromBlock(TileEntity.this.worldObj.getBlockState(TileEntity.this.pos).getBlock());
                    try {
                        return String.format("ID #%d (%s // %s)", n, Block.getBlockById(n).getUnlocalizedName(), Block.getBlockById(n).getClass().getCanonicalName());
                    }
                    catch (Throwable throwable) {
                        return "ID #" + n;
                    }
                }
            });
            crashReportCategory.addCrashSectionCallable("Actual block data value", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    IBlockState iBlockState = TileEntity.this.worldObj.getBlockState(TileEntity.this.pos);
                    int n = iBlockState.getBlock().getMetaFromState(iBlockState);
                    if (n < 0) {
                        return "Unknown? (Got " + n + ")";
                    }
                    String string = String.format("%4s", Integer.toBinaryString(n)).replace(" ", "0");
                    return String.format("%1$d / 0x%1$X / 0b%2$s", n, string);
                }
            });
        }
    }

    public boolean hasWorldObj() {
        return this.worldObj != null;
    }

    public void setPos(BlockPos blockPos) {
        this.pos = blockPos;
    }

    public boolean func_183000_F() {
        return false;
    }

    public World getWorld() {
        return this.worldObj;
    }

    public void invalidate() {
        this.tileEntityInvalid = true;
    }

    static {
        nameToClassMap = Maps.newHashMap();
        classToNameMap = Maps.newHashMap();
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

    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        String string = classToNameMap.get(this.getClass());
        if (string == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        nBTTagCompound.setString("id", string);
        nBTTagCompound.setInteger("x", this.pos.getX());
        nBTTagCompound.setInteger("y", this.pos.getY());
        nBTTagCompound.setInteger("z", this.pos.getZ());
    }

    public boolean isInvalid() {
        return this.tileEntityInvalid;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getBlockMetadata() {
        if (this.blockMetadata == -1) {
            IBlockState iBlockState = this.worldObj.getBlockState(this.pos);
            this.blockMetadata = iBlockState.getBlock().getMetaFromState(iBlockState);
        }
        return this.blockMetadata;
    }

    private static void addMapping(Class<? extends TileEntity> clazz, String string) {
        if (nameToClassMap.containsKey(string)) {
            throw new IllegalArgumentException("Duplicate id: " + string);
        }
        nameToClassMap.put(string, clazz);
        classToNameMap.put(clazz, string);
    }

    public void setWorldObj(World world) {
        this.worldObj = world;
    }

    public double getMaxRenderDistanceSquared() {
        return 4096.0;
    }

    public boolean receiveClientEvent(int n, int n2) {
        return false;
    }

    public void updateContainingBlockInfo() {
        this.blockType = null;
        this.blockMetadata = -1;
    }

    public Packet getDescriptionPacket() {
        return null;
    }

    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        this.pos = new BlockPos(nBTTagCompound.getInteger("x"), nBTTagCompound.getInteger("y"), nBTTagCompound.getInteger("z"));
    }

    public static TileEntity createAndLoadEntity(NBTTagCompound nBTTagCompound) {
        TileEntity tileEntity = null;
        try {
            Class<? extends TileEntity> clazz = nameToClassMap.get(nBTTagCompound.getString("id"));
            if (clazz != null) {
                tileEntity = clazz.newInstance();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (tileEntity != null) {
            tileEntity.readFromNBT(nBTTagCompound);
        } else {
            logger.warn("Skipping BlockEntity with id " + nBTTagCompound.getString("id"));
        }
        return tileEntity;
    }

    public Block getBlockType() {
        if (this.blockType == null) {
            this.blockType = this.worldObj.getBlockState(this.pos).getBlock();
        }
        return this.blockType;
    }

    public void validate() {
        this.tileEntityInvalid = false;
    }

    public double getDistanceSq(double d, double d2, double d3) {
        double d4 = (double)this.pos.getX() + 0.5 - d;
        double d5 = (double)this.pos.getY() + 0.5 - d2;
        double d6 = (double)this.pos.getZ() + 0.5 - d3;
        return d4 * d4 + d5 * d5 + d6 * d6;
    }

    public void markDirty() {
        if (this.worldObj != null) {
            IBlockState iBlockState = this.worldObj.getBlockState(this.pos);
            this.blockMetadata = iBlockState.getBlock().getMetaFromState(iBlockState);
            this.worldObj.markChunkDirty(this.pos, this);
            if (this.getBlockType() != Blocks.air) {
                this.worldObj.updateComparatorOutputLevel(this.pos, this.getBlockType());
            }
        }
    }
}

