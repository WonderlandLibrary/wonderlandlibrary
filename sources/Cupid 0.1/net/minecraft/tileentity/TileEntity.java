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
    if (nameToClassMap.containsKey(id))
      throw new IllegalArgumentException("Duplicate id: " + id); 
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
    return (this.worldObj != null);
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
  }
  
  public void writeToNBT(NBTTagCompound compound) {
    String s = classToNameMap.get(getClass());
    if (s == null)
      throw new RuntimeException(getClass() + " is missing a mapping! This is a bug!"); 
    compound.setString("id", s);
    compound.setInteger("x", this.pos.getX());
    compound.setInteger("y", this.pos.getY());
    compound.setInteger("z", this.pos.getZ());
  }
  
  public static TileEntity createAndLoadEntity(NBTTagCompound nbt) {
    TileEntity tileentity = null;
    try {
      Class<? extends TileEntity> oclass = nameToClassMap.get(nbt.getString("id"));
      if (oclass != null)
        tileentity = oclass.newInstance(); 
    } catch (Exception exception) {
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
      if (getBlockType() != Blocks.air)
        this.worldObj.updateComparatorOutputLevel(this.pos, getBlockType()); 
    } 
  }
  
  public double getDistanceSq(double x, double y, double z) {
    double d0 = this.pos.getX() + 0.5D - x;
    double d1 = this.pos.getY() + 0.5D - y;
    double d2 = this.pos.getZ() + 0.5D - z;
    return d0 * d0 + d1 * d1 + d2 * d2;
  }
  
  public double getMaxRenderDistanceSquared() {
    return 4096.0D;
  }
  
  public BlockPos getPos() {
    return this.pos;
  }
  
  public Block getBlockType() {
    if (this.blockType == null)
      this.blockType = this.worldObj.getBlockState(this.pos).getBlock(); 
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
    reportCategory.addCrashSectionCallable("Name", new Callable<String>() {
          public String call() throws Exception {
            return (String)TileEntity.classToNameMap.get(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
          }
        });
    if (this.worldObj != null) {
      CrashReportCategory.addBlockInfo(reportCategory, this.pos, getBlockType(), getBlockMetadata());
      reportCategory.addCrashSectionCallable("Actual block type", new Callable<String>() {
            public String call() throws Exception {
              int i = Block.getIdFromBlock(TileEntity.this.worldObj.getBlockState(TileEntity.this.pos).getBlock());
              try {
                return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i), Block.getBlockById(i).getUnlocalizedName(), Block.getBlockById(i).getClass().getCanonicalName() });
              } catch (Throwable var3) {
                return "ID #" + i;
              } 
            }
          });
      reportCategory.addCrashSectionCallable("Actual block data value", new Callable<String>() {
            public String call() throws Exception {
              IBlockState iblockstate = TileEntity.this.worldObj.getBlockState(TileEntity.this.pos);
              int i = iblockstate.getBlock().getMetaFromState(iblockstate);
              if (i < 0)
                return "Unknown? (Got " + i + ")"; 
              String s = String.format("%4s", new Object[] { Integer.toBinaryString(i) }).replace(" ", "0");
              return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s });
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
    addMapping((Class)TileEntityFurnace.class, "Furnace");
    addMapping((Class)TileEntityChest.class, "Chest");
    addMapping((Class)TileEntityEnderChest.class, "EnderChest");
    addMapping((Class)BlockJukebox.TileEntityJukebox.class, "RecordPlayer");
    addMapping((Class)TileEntityDispenser.class, "Trap");
    addMapping((Class)TileEntityDropper.class, "Dropper");
    addMapping((Class)TileEntitySign.class, "Sign");
    addMapping((Class)TileEntityMobSpawner.class, "MobSpawner");
    addMapping((Class)TileEntityNote.class, "Music");
    addMapping((Class)TileEntityPiston.class, "Piston");
    addMapping((Class)TileEntityBrewingStand.class, "Cauldron");
    addMapping((Class)TileEntityEnchantmentTable.class, "EnchantTable");
    addMapping((Class)TileEntityEndPortal.class, "Airportal");
    addMapping((Class)TileEntityCommandBlock.class, "Control");
    addMapping((Class)TileEntityBeacon.class, "Beacon");
    addMapping((Class)TileEntitySkull.class, "Skull");
    addMapping((Class)TileEntityDaylightDetector.class, "DLDetector");
    addMapping((Class)TileEntityHopper.class, "Hopper");
    addMapping((Class)TileEntityComparator.class, "Comparator");
    addMapping((Class)TileEntityFlowerPot.class, "FlowerPot");
    addMapping((Class)TileEntityBanner.class, "Banner");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\tileentity\TileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */