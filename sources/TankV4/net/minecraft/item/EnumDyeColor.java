package net.minecraft.item;

import net.minecraft.block.material.MapColor;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IStringSerializable;

public enum EnumDyeColor implements IStringSerializable {
   private final String name;
   BLACK(15, 0, "black", "black", MapColor.blackColor, EnumChatFormatting.BLACK),
   BLUE(11, 4, "blue", "blue", MapColor.blueColor, EnumChatFormatting.DARK_BLUE);

   private static final EnumDyeColor[] DYE_DMG_LOOKUP = new EnumDyeColor[values().length];
   LIME(5, 10, "lime", "lime", MapColor.limeColor, EnumChatFormatting.GREEN);

   private final int meta;
   MAGENTA(2, 13, "magenta", "magenta", MapColor.magentaColor, EnumChatFormatting.AQUA),
   WHITE(0, 15, "white", "white", MapColor.snowColor, EnumChatFormatting.WHITE);

   private static final EnumDyeColor[] META_LOOKUP = new EnumDyeColor[values().length];
   private final MapColor mapColor;
   RED(14, 1, "red", "red", MapColor.redColor, EnumChatFormatting.DARK_RED);

   private final int dyeDamage;
   YELLOW(4, 11, "yellow", "yellow", MapColor.yellowColor, EnumChatFormatting.YELLOW),
   ORANGE(1, 14, "orange", "orange", MapColor.adobeColor, EnumChatFormatting.GOLD),
   PURPLE(10, 5, "purple", "purple", MapColor.purpleColor, EnumChatFormatting.DARK_PURPLE);

   private static final EnumDyeColor[] ENUM$VALUES = new EnumDyeColor[]{WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, SILVER, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK};
   GRAY(7, 8, "gray", "gray", MapColor.grayColor, EnumChatFormatting.DARK_GRAY),
   SILVER(8, 7, "silver", "silver", MapColor.silverColor, EnumChatFormatting.GRAY),
   LIGHT_BLUE(3, 12, "light_blue", "lightBlue", MapColor.lightBlueColor, EnumChatFormatting.BLUE),
   BROWN(12, 3, "brown", "brown", MapColor.brownColor, EnumChatFormatting.GOLD),
   PINK(6, 9, "pink", "pink", MapColor.pinkColor, EnumChatFormatting.LIGHT_PURPLE),
   CYAN(9, 6, "cyan", "cyan", MapColor.cyanColor, EnumChatFormatting.DARK_AQUA);

   private final String unlocalizedName;
   private final EnumChatFormatting chatColor;
   GREEN(13, 2, "green", "green", MapColor.greenColor, EnumChatFormatting.DARK_GREEN);

   public String getName() {
      return this.name;
   }

   public MapColor getMapColor() {
      return this.mapColor;
   }

   public String getUnlocalizedName() {
      return this.unlocalizedName;
   }

   public int getMetadata() {
      return this.meta;
   }

   public int getDyeDamage() {
      return this.dyeDamage;
   }

   static {
      EnumDyeColor[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         EnumDyeColor var0 = var3[var1];
         META_LOOKUP[var0.getMetadata()] = var0;
         DYE_DMG_LOOKUP[var0.getDyeDamage()] = var0;
      }

   }

   public String toString() {
      return this.unlocalizedName;
   }

   public static EnumDyeColor byDyeDamage(int var0) {
      if (var0 < 0 || var0 >= DYE_DMG_LOOKUP.length) {
         var0 = 0;
      }

      return DYE_DMG_LOOKUP[var0];
   }

   private EnumDyeColor(int var3, int var4, String var5, String var6, MapColor var7, EnumChatFormatting var8) {
      this.meta = var3;
      this.dyeDamage = var4;
      this.name = var5;
      this.unlocalizedName = var6;
      this.mapColor = var7;
      this.chatColor = var8;
   }

   public static EnumDyeColor byMetadata(int var0) {
      if (var0 < 0 || var0 >= META_LOOKUP.length) {
         var0 = 0;
      }

      return META_LOOKUP[var0];
   }
}
