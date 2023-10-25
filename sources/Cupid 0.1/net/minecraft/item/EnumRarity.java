package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;

public enum EnumRarity {
  COMMON(EnumChatFormatting.WHITE, "Common"),
  UNCOMMON(EnumChatFormatting.YELLOW, "Uncommon"),
  RARE(EnumChatFormatting.AQUA, "Rare"),
  EPIC(EnumChatFormatting.LIGHT_PURPLE, "Epic");
  
  public final EnumChatFormatting rarityColor;
  
  public final String rarityName;
  
  EnumRarity(EnumChatFormatting color, String name) {
    this.rarityColor = color;
    this.rarityName = name;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\EnumRarity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */