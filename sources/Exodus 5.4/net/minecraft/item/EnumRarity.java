/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;

public enum EnumRarity {
    COMMON(EnumChatFormatting.WHITE, "Common"),
    UNCOMMON(EnumChatFormatting.YELLOW, "Uncommon"),
    RARE(EnumChatFormatting.AQUA, "Rare"),
    EPIC(EnumChatFormatting.LIGHT_PURPLE, "Epic");

    public final String rarityName;
    public final EnumChatFormatting rarityColor;

    private EnumRarity(EnumChatFormatting enumChatFormatting, String string2) {
        this.rarityColor = enumChatFormatting;
        this.rarityName = string2;
    }
}

