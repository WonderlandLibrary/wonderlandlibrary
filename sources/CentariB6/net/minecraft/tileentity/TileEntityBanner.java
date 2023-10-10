package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner.EnumBannerPattern;

public class TileEntityBanner extends TileEntity {
   private int baseColor;
   private NBTTagList patterns;
   private boolean field_175119_g;
   private List patternList;
   private List colorList;
   private String patternResourceLocation;

   public void writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      func_181020_a(compound, this.baseColor, this.patterns);
   }

   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      this.baseColor = compound.getInteger("Base");
      this.patterns = compound.getTagList("Patterns", 10);
      this.patternList = null;
      this.colorList = null;
      this.patternResourceLocation = null;
      this.field_175119_g = true;
   }

   private void initializeBannerData() {
      if(this.patternList == null || this.colorList == null || this.patternResourceLocation == null) {
         if(!this.field_175119_g) {
            this.patternResourceLocation = "";
         } else {
            this.patternList = Lists.newArrayList();
            this.colorList = Lists.newArrayList();
            this.patternList.add(EnumBannerPattern.BASE);
            this.colorList.add(EnumDyeColor.byDyeDamage(this.baseColor));
            this.patternResourceLocation = "b" + this.baseColor;
            if(this.patterns != null) {
               for(int i = 0; i < this.patterns.tagCount(); ++i) {
                  NBTTagCompound nbttagcompound = this.patterns.getCompoundTagAt(i);
                  EnumBannerPattern tileentitybanner$enumbannerpattern = EnumBannerPattern.getPatternByID(nbttagcompound.getString("Pattern"));
                  if(tileentitybanner$enumbannerpattern != null) {
                     this.patternList.add(tileentitybanner$enumbannerpattern);
                     int j = nbttagcompound.getInteger("Color");
                     this.colorList.add(EnumDyeColor.byDyeDamage(j));
                     this.patternResourceLocation = this.patternResourceLocation + tileentitybanner$enumbannerpattern.getPatternID() + j;
                  }
               }
            }
         }
      }

   }

   public Packet getDescriptionPacket() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      this.writeToNBT(nbttagcompound);
      return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
   }

   public static void removeBannerData(ItemStack stack) {
      NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
      if(nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9)) {
         NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
         if(nbttaglist.tagCount() > 0) {
            nbttaglist.removeTag(nbttaglist.tagCount() - 1);
            if(nbttaglist.hasNoTags()) {
               stack.getTagCompound().removeTag("BlockEntityTag");
               if(stack.getTagCompound().hasNoTags()) {
                  stack.setTagCompound((NBTTagCompound)null);
               }
            }
         }
      }

   }

   public static int getPatterns(ItemStack stack) {
      NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
      return nbttagcompound != null && nbttagcompound.hasKey("Patterns")?nbttagcompound.getTagList("Patterns", 10).tagCount():0;
   }

   public List getColorList() {
      this.initializeBannerData();
      return this.colorList;
   }

   public static void func_181020_a(NBTTagCompound p_181020_0_, int p_181020_1_, NBTTagList p_181020_2_) {
      p_181020_0_.setInteger("Base", p_181020_1_);
      if(p_181020_2_ != null) {
         p_181020_0_.setTag("Patterns", p_181020_2_);
      }

   }

   public NBTTagList func_181021_d() {
      return this.patterns;
   }

   public static int getBaseColor(ItemStack stack) {
      NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
      return nbttagcompound != null && nbttagcompound.hasKey("Base")?nbttagcompound.getInteger("Base"):stack.getMetadata();
   }

   public int getBaseColor() {
      return this.baseColor;
   }

   public List getPatternList() {
      this.initializeBannerData();
      return this.patternList;
   }

   public void setItemValues(ItemStack stack) {
      this.patterns = null;
      if(stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10)) {
         NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
         if(nbttagcompound.hasKey("Patterns")) {
            this.patterns = (NBTTagList)nbttagcompound.getTagList("Patterns", 10).copy();
         }

         if(nbttagcompound.hasKey("Base", 99)) {
            this.baseColor = nbttagcompound.getInteger("Base");
         } else {
            this.baseColor = stack.getMetadata() & 15;
         }
      } else {
         this.baseColor = stack.getMetadata() & 15;
      }

      this.patternList = null;
      this.colorList = null;
      this.patternResourceLocation = "";
      this.field_175119_g = true;
   }

   public String func_175116_e() {
      this.initializeBannerData();
      return this.patternResourceLocation;
   }
}
