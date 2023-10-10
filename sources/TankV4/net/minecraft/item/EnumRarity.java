package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;

public enum EnumRarity {
   public final String rarityName;
   COMMON(EnumChatFormatting.WHITE, "Common"),
   RARE(EnumChatFormatting.AQUA, "Rare"),
   UNCOMMON(EnumChatFormatting.YELLOW, "Uncommon");

   private static final EnumRarity[] ENUM$VALUES = new EnumRarity[]{COMMON, UNCOMMON, RARE, EPIC};
   public final EnumChatFormatting rarityColor;
   EPIC(EnumChatFormatting.LIGHT_PURPLE, "Epic");

   private EnumRarity(EnumChatFormatting var3, String var4) {
      this.rarityColor = var3;
      this.rarityName = var4;
   }
}
