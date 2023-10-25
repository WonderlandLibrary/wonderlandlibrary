/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBanner
extends TileEntity {
    private List<EnumDyeColor> colorList;
    private NBTTagList patterns;
    private List<EnumBannerPattern> patternList;
    private int baseColor;
    private boolean field_175119_g;
    private String patternResourceLocation;

    public int getBaseColor() {
        return this.baseColor;
    }

    public static void removeBannerData(ItemStack itemStack) {
        NBTTagList nBTTagList;
        NBTTagCompound nBTTagCompound = itemStack.getSubCompound("BlockEntityTag", false);
        if (nBTTagCompound != null && nBTTagCompound.hasKey("Patterns", 9) && (nBTTagList = nBTTagCompound.getTagList("Patterns", 10)).tagCount() > 0) {
            nBTTagList.removeTag(nBTTagList.tagCount() - 1);
            if (nBTTagList.hasNoTags()) {
                itemStack.getTagCompound().removeTag("BlockEntityTag");
                if (itemStack.getTagCompound().hasNoTags()) {
                    itemStack.setTagCompound(null);
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        super.writeToNBT(nBTTagCompound);
        TileEntityBanner.func_181020_a(nBTTagCompound, this.baseColor, this.patterns);
    }

    public String func_175116_e() {
        this.initializeBannerData();
        return this.patternResourceLocation;
    }

    @Override
    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        super.readFromNBT(nBTTagCompound);
        this.baseColor = nBTTagCompound.getInteger("Base");
        this.patterns = nBTTagCompound.getTagList("Patterns", 10);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.field_175119_g = true;
    }

    public static int getPatterns(ItemStack itemStack) {
        NBTTagCompound nBTTagCompound = itemStack.getSubCompound("BlockEntityTag", false);
        return nBTTagCompound != null && nBTTagCompound.hasKey("Patterns") ? nBTTagCompound.getTagList("Patterns", 10).tagCount() : 0;
    }

    public NBTTagList func_181021_d() {
        return this.patterns;
    }

    public List<EnumDyeColor> getColorList() {
        this.initializeBannerData();
        return this.colorList;
    }

    public static void func_181020_a(NBTTagCompound nBTTagCompound, int n, NBTTagList nBTTagList) {
        nBTTagCompound.setInteger("Base", n);
        if (nBTTagList != null) {
            nBTTagCompound.setTag("Patterns", nBTTagList);
        }
    }

    public static int getBaseColor(ItemStack itemStack) {
        NBTTagCompound nBTTagCompound = itemStack.getSubCompound("BlockEntityTag", false);
        return nBTTagCompound != null && nBTTagCompound.hasKey("Base") ? nBTTagCompound.getInteger("Base") : itemStack.getMetadata();
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        this.writeToNBT(nBTTagCompound);
        return new S35PacketUpdateTileEntity(this.pos, 6, nBTTagCompound);
    }

    private void initializeBannerData() {
        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null) {
            if (!this.field_175119_g) {
                this.patternResourceLocation = "";
            } else {
                this.patternList = Lists.newArrayList();
                this.colorList = Lists.newArrayList();
                this.patternList.add(EnumBannerPattern.BASE);
                this.colorList.add(EnumDyeColor.byDyeDamage(this.baseColor));
                this.patternResourceLocation = "b" + this.baseColor;
                if (this.patterns != null) {
                    int n = 0;
                    while (n < this.patterns.tagCount()) {
                        NBTTagCompound nBTTagCompound = this.patterns.getCompoundTagAt(n);
                        EnumBannerPattern enumBannerPattern = EnumBannerPattern.getPatternByID(nBTTagCompound.getString("Pattern"));
                        if (enumBannerPattern != null) {
                            this.patternList.add(enumBannerPattern);
                            int n2 = nBTTagCompound.getInteger("Color");
                            this.colorList.add(EnumDyeColor.byDyeDamage(n2));
                            this.patternResourceLocation = String.valueOf(this.patternResourceLocation) + enumBannerPattern.getPatternID() + n2;
                        }
                        ++n;
                    }
                }
            }
        }
    }

    public List<EnumBannerPattern> getPatternList() {
        this.initializeBannerData();
        return this.patternList;
    }

    public void setItemValues(ItemStack itemStack) {
        this.patterns = null;
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("BlockEntityTag", 10)) {
            NBTTagCompound nBTTagCompound = itemStack.getTagCompound().getCompoundTag("BlockEntityTag");
            if (nBTTagCompound.hasKey("Patterns")) {
                this.patterns = (NBTTagList)nBTTagCompound.getTagList("Patterns", 10).copy();
            }
            this.baseColor = nBTTagCompound.hasKey("Base", 99) ? nBTTagCompound.getInteger("Base") : itemStack.getMetadata() & 0xF;
        } else {
            this.baseColor = itemStack.getMetadata() & 0xF;
        }
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.field_175119_g = true;
    }

    public static enum EnumBannerPattern {
        BASE("base", "b"),
        SQUARE_BOTTOM_LEFT("square_bottom_left", "bl", "   ", "   ", "#  "),
        SQUARE_BOTTOM_RIGHT("square_bottom_right", "br", "   ", "   ", "  #"),
        SQUARE_TOP_LEFT("square_top_left", "tl", "#  ", "   ", "   "),
        SQUARE_TOP_RIGHT("square_top_right", "tr", "  #", "   ", "   "),
        STRIPE_BOTTOM("stripe_bottom", "bs", "   ", "   ", "###"),
        STRIPE_TOP("stripe_top", "ts", "###", "   ", "   "),
        STRIPE_LEFT("stripe_left", "ls", "#  ", "#  ", "#  "),
        STRIPE_RIGHT("stripe_right", "rs", "  #", "  #", "  #"),
        STRIPE_CENTER("stripe_center", "cs", " # ", " # ", " # "),
        STRIPE_MIDDLE("stripe_middle", "ms", "   ", "###", "   "),
        STRIPE_DOWNRIGHT("stripe_downright", "drs", "#  ", " # ", "  #"),
        STRIPE_DOWNLEFT("stripe_downleft", "dls", "  #", " # ", "#  "),
        STRIPE_SMALL("small_stripes", "ss", "# #", "# #", "   "),
        CROSS("cross", "cr", "# #", " # ", "# #"),
        STRAIGHT_CROSS("straight_cross", "sc", " # ", "###", " # "),
        TRIANGLE_BOTTOM("triangle_bottom", "bt", "   ", " # ", "# #"),
        TRIANGLE_TOP("triangle_top", "tt", "# #", " # ", "   "),
        TRIANGLES_BOTTOM("triangles_bottom", "bts", "   ", "# #", " # "),
        TRIANGLES_TOP("triangles_top", "tts", " # ", "# #", "   "),
        DIAGONAL_LEFT("diagonal_left", "ld", "## ", "#  ", "   "),
        DIAGONAL_RIGHT("diagonal_up_right", "rd", "   ", "  #", " ##"),
        DIAGONAL_LEFT_MIRROR("diagonal_up_left", "lud", "   ", "#  ", "## "),
        DIAGONAL_RIGHT_MIRROR("diagonal_right", "rud", " ##", "  #", "   "),
        CIRCLE_MIDDLE("circle", "mc", "   ", " # ", "   "),
        RHOMBUS_MIDDLE("rhombus", "mr", " # ", "# #", " # "),
        HALF_VERTICAL("half_vertical", "vh", "## ", "## ", "## "),
        HALF_HORIZONTAL("half_horizontal", "hh", "###", "###", "   "),
        HALF_VERTICAL_MIRROR("half_vertical_right", "vhr", " ##", " ##", " ##"),
        HALF_HORIZONTAL_MIRROR("half_horizontal_bottom", "hhb", "   ", "###", "###"),
        BORDER("border", "bo", "###", "# #", "###"),
        CURLY_BORDER("curly_border", "cbo", new ItemStack(Blocks.vine)),
        CREEPER("creeper", "cre", new ItemStack(Items.skull, 1, 4)),
        GRADIENT("gradient", "gra", "# #", " # ", " # "),
        GRADIENT_UP("gradient_up", "gru", " # ", " # ", "# #"),
        BRICKS("bricks", "bri", new ItemStack(Blocks.brick_block)),
        SKULL("skull", "sku", new ItemStack(Items.skull, 1, 1)),
        FLOWER("flower", "flo", new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())),
        MOJANG("mojang", "moj", new ItemStack(Items.golden_apple, 1, 1));

        private String[] craftingLayers = new String[3];
        private String patternName;
        private ItemStack patternCraftingStack;
        private String patternID;

        private EnumBannerPattern(String string2, String string3) {
            this.patternName = string2;
            this.patternID = string3;
        }

        private EnumBannerPattern(String string2, String string3, ItemStack itemStack) {
            this(string2, string3);
            this.patternCraftingStack = itemStack;
        }

        public boolean hasCraftingStack() {
            return this.patternCraftingStack != null;
        }

        public static EnumBannerPattern getPatternByID(String string) {
            EnumBannerPattern[] enumBannerPatternArray = EnumBannerPattern.values();
            int n = enumBannerPatternArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumBannerPattern enumBannerPattern = enumBannerPatternArray[n2];
                if (enumBannerPattern.patternID.equals(string)) {
                    return enumBannerPattern;
                }
                ++n2;
            }
            return null;
        }

        private EnumBannerPattern(String string2, String string3, String string4, String string5, String string6) {
            this(string2, string3);
            this.craftingLayers[0] = string4;
            this.craftingLayers[1] = string5;
            this.craftingLayers[2] = string6;
        }

        public String getPatternID() {
            return this.patternID;
        }

        public ItemStack getCraftingStack() {
            return this.patternCraftingStack;
        }

        public boolean hasValidCrafting() {
            return this.patternCraftingStack != null || this.craftingLayers[0] != null;
        }

        public String[] getCraftingLayers() {
            return this.craftingLayers;
        }

        public String getPatternName() {
            return this.patternName;
        }
    }
}

