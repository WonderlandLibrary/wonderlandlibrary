package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;

/*
 * Exception performing whole class analysis ignored.
 */
public class TileEntityBanner
extends TileEntity {
    private int baseColor;
    private NBTTagList patterns;
    private boolean field_175119_g;
    private List<EnumBannerPattern> patternList;
    private List<EnumDyeColor> colorList;
    private String patternResourceLocation;

    public void setItemValues(ItemStack stack) {
        this.patterns = null;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10)) {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
            if (nbttagcompound.hasKey("Patterns")) {
                this.patterns = (NBTTagList)nbttagcompound.getTagList("Patterns", 10).copy();
            }
            this.baseColor = nbttagcompound.hasKey("Base", 99) ? nbttagcompound.getInteger("Base") : stack.getMetadata() & 0xF;
        } else {
            this.baseColor = stack.getMetadata() & 0xF;
        }
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.field_175119_g = true;
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        TileEntityBanner.setBaseColorAndPatterns(compound, this.baseColor, this.patterns);
    }

    public static void setBaseColorAndPatterns(NBTTagCompound compound, int baseColorIn, NBTTagList patternsIn) {
        compound.setInteger("Base", baseColorIn);
        if (patternsIn != null) {
            compound.setTag("Patterns", (NBTBase)patternsIn);
        }
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

    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
    }

    public int getBaseColor() {
        return this.baseColor;
    }

    public static int getBaseColor(ItemStack stack) {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        return nbttagcompound != null && nbttagcompound.hasKey("Base") ? nbttagcompound.getInteger("Base") : stack.getMetadata();
    }

    public static int getPatterns(ItemStack stack) {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        return nbttagcompound != null && nbttagcompound.hasKey("Patterns") ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
    }

    public List<EnumBannerPattern> getPatternList() {
        this.initializeBannerData();
        return this.patternList;
    }

    public NBTTagList getPatterns() {
        return this.patterns;
    }

    public List<EnumDyeColor> getColorList() {
        this.initializeBannerData();
        return this.colorList;
    }

    public String getPatternResourceLocation() {
        this.initializeBannerData();
        return this.patternResourceLocation;
    }

    private void initializeBannerData() {
        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null) {
            if (!this.field_175119_g) {
                this.patternResourceLocation = "";
            } else {
                this.patternList = Lists.newArrayList();
                this.colorList = Lists.newArrayList();
                this.patternList.add((Object)EnumBannerPattern.BASE);
                this.colorList.add((Object)EnumDyeColor.byDyeDamage((int)this.baseColor));
                this.patternResourceLocation = "b" + this.baseColor;
                if (this.patterns != null) {
                    for (int i = 0; i < this.patterns.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound = this.patterns.getCompoundTagAt(i);
                        EnumBannerPattern tileentitybanner$enumbannerpattern = EnumBannerPattern.getPatternByID((String)nbttagcompound.getString("Pattern"));
                        if (tileentitybanner$enumbannerpattern == null) continue;
                        this.patternList.add((Object)tileentitybanner$enumbannerpattern);
                        int j = nbttagcompound.getInteger("Color");
                        this.colorList.add((Object)EnumDyeColor.byDyeDamage((int)j));
                        this.patternResourceLocation = this.patternResourceLocation + tileentitybanner$enumbannerpattern.getPatternID() + j;
                    }
                }
            }
        }
    }

    public static void removeBannerData(ItemStack stack) {
        NBTTagList nbttaglist;
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9) && (nbttaglist = nbttagcompound.getTagList("Patterns", 10)).tagCount() > 0) {
            nbttaglist.removeTag(nbttaglist.tagCount() - 1);
            if (nbttaglist.hasNoTags()) {
                stack.getTagCompound().removeTag("BlockEntityTag");
                if (stack.getTagCompound().hasNoTags()) {
                    stack.setTagCompound((NBTTagCompound)null);
                }
            }
        }
    }
}
