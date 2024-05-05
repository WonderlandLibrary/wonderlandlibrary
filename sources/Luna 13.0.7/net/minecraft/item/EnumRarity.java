package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;

public enum EnumRarity
{
  public final EnumChatFormatting rarityColor;
  public final String rarityName;
  private static final EnumRarity[] $VALUES = { COMMON, UNCOMMON, RARE, EPIC };
  private static final String __OBFID = "CL_00000056";
  
  private EnumRarity(String p_i45349_1_, int p_i45349_2_, EnumChatFormatting p_i45349_3_, String p_i45349_4_)
  {
    this.rarityColor = p_i45349_3_;
    this.rarityName = p_i45349_4_;
  }
}
