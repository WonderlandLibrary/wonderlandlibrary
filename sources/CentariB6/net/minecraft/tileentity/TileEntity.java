package net.minecraft.tileentity;

import com.google.common.collect.Maps;
import java.util.Map;
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
import net.minecraft.tileentity.TileEntity.1;
import net.minecraft.tileentity.TileEntity.2;
import net.minecraft.tileentity.TileEntity.3;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity {
   private static final Logger logger = LogManager.getLogger();
   private static Map nameToClassMap = Maps.newHashMap();
   private static Map classToNameMap = Maps.newHashMap();
   protected World worldObj;
   protected BlockPos pos = BlockPos.ORIGIN;
   protected boolean tileEntityInvalid;
   private int blockMetadata = -1;
   protected Block blockType;

   static {
      addMapping(TileEntityFurnace.class, "Furnace");
      addMapping(TileEntityChest.class, "Chest");
      addMapping(TileEntityEnderChest.class, "EnderChest");
      addMapping(BlockJukebox.TileEntityJukebox.class, "RecordPlayer");
      addMapping(TileEntityDispenser.class, "Trap");
      addMapping(TileEntityDropper.class, "Dropper");
      addMapping(TileEntitySign.class, "Sign");
      addMapping(TileEntityMobSpawner.class, "MobSpawner");
      addMapping(TileEntityNote.class, "Music");
      addMapping(TileEntityPiston.class, "Piston");
      addMapping(TileEntityBrewingStand.class, "Cauldron");
      addMapping(TileEntityEnchantmentTable.class, "EnchantTable");
      addMapping(TileEntityEndPortal.class, "Airportal");
      addMapping(TileEntityCommandBlock.class, "Control");
      addMapping(TileEntityBeacon.class, "Beacon");
      addMapping(TileEntitySkull.class, "Skull");
      addMapping(TileEntityDaylightDetector.class, "DLDetector");
      addMapping(TileEntityHopper.class, "Hopper");
      addMapping(TileEntityComparator.class, "Comparator");
      addMapping(TileEntityFlowerPot.class, "FlowerPot");
      addMapping(TileEntityBanner.class, "Banner");
   }

   // $FF: synthetic method
   static Map access$000() {
      return classToNameMap;
   }

   public boolean isInvalid() {
      return this.tileEntityInvalid;
   }

   private static void addMapping(Class cl, String id) {
      if(nameToClassMap.containsKey(id)) {
         throw new IllegalArgumentException("Duplicate id: " + id);
      } else {
         nameToClassMap.put(id, cl);
         classToNameMap.put(cl, id);
      }
   }

   public void invalidate() {
      this.tileEntityInvalid = true;
   }

   public void validate() {
      this.tileEntityInvalid = false;
   }

   public void markDirty() {
      if(this.worldObj != null) {
         IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
         this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
         this.worldObj.markChunkDirty(this.pos, this);
         if(this.getBlockType() != Blocks.air) {
            this.worldObj.updateComparatorOutputLevel(this.pos, this.getBlockType());
         }
      }

   }

   public boolean hasWorldObj() {
      return this.worldObj != null;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public void setPos(BlockPos posIn) {
      this.pos = posIn;
   }

   public void setWorldObj(World worldIn) {
      this.worldObj = worldIn;
   }

   public int getBlockMetadata() {
      if(this.blockMetadata == -1) {
         IBlockState iblockstate = this.worldObj.getBlockState(this.pos);
         this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
      }

      return this.blockMetadata;
   }

   public boolean func_183000_F() {
      return false;
   }

   public double getDistanceSq(double x, double y, double z) {
      double d0 = (double)this.pos.getX() + 0.5D - x;
      double d1 = (double)this.pos.getY() + 0.5D - y;
      double d2 = (double)this.pos.getZ() + 0.5D - z;
      return d0 * d0 + d1 * d1 + d2 * d2;
   }

   public void writeToNBT(NBTTagCompound compound) {
      String s = (String)classToNameMap.get(this.getClass());
      if(s == null) {
         throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
      } else {
         compound.setString("id", s);
         compound.setInteger("x", this.pos.getX());
         compound.setInteger("y", this.pos.getY());
         compound.setInteger("z", this.pos.getZ());
      }
   }

   public World getWorld() {
      return this.worldObj;
   }

   public void readFromNBT(NBTTagCompound compound) {
      this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
   }

   public void addInfoToCrashReport(CrashReportCategory reportCategory) {
      reportCategory.addCrashSectionCallable("Name", new 1(this));
      if(this.worldObj != null) {
         CrashReportCategory.addBlockInfo(reportCategory, this.pos, this.getBlockType(), this.getBlockMetadata());
         reportCategory.addCrashSectionCallable("Actual block type", new 2(this));
         reportCategory.addCrashSectionCallable("Actual block data value", new 3(this));
      }

   }

   public Block getBlockType() {
      if(this.blockType == null) {
         this.blockType = this.worldObj.getBlockState(this.pos).getBlock();
      }

      return this.blockType;
   }

   public void updateContainingBlockInfo() {
      this.blockType = null;
      this.blockMetadata = -1;
   }

   public Packet getDescriptionPacket() {
      return null;
   }

   public static TileEntity createAndLoadEntity(NBTTagCompound nbt) {
      TileEntity tileentity = null;

      try {
         Class<? extends TileEntity> oclass = (Class)nameToClassMap.get(nbt.getString("id"));
         if(oclass != null) {
            tileentity = (TileEntity)oclass.newInstance();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      if(tileentity != null) {
         tileentity.readFromNBT(nbt);
      } else {
         logger.warn("Skipping BlockEntity with id " + nbt.getString("id"));
      }

      return tileentity;
   }

   public double getMaxRenderDistanceSquared() {
      return 4096.0D;
   }

   public boolean receiveClientEvent(int id, int type) {
      return false;
   }
}
