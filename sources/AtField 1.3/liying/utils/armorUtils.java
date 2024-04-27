/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 */
package liying.utils;

import java.awt.Color;
import net.minecraft.item.ItemStack;

public class armorUtils {
    public Color color2;
    public int Damage;
    public ItemStack Armor;
    public Color color;

    public armorUtils(ItemStack itemStack, int n, Color color, Color color2) {
        this.Armor = itemStack;
        this.Damage = n;
        this.color = color;
        this.color2 = color2;
    }
}

