// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.text.TextFormatting;

public enum EnumRarity
{
    COMMON(TextFormatting.WHITE, "Common"), 
    UNCOMMON(TextFormatting.YELLOW, "Uncommon"), 
    RARE(TextFormatting.AQUA, "Rare"), 
    EPIC(TextFormatting.LIGHT_PURPLE, "Epic");
    
    public final TextFormatting color;
    public final String rarityName;
    
    private EnumRarity(final TextFormatting color, final String name) {
        this.color = color;
        this.rarityName = name;
    }
}
