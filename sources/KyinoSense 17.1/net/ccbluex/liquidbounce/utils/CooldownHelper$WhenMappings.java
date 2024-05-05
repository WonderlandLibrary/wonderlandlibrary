/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item$ToolMaterial
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import net.minecraft.item.Item;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, xi=2)
public final class CooldownHelper$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;

    static {
        $EnumSwitchMapping$0 = new int[Item.ToolMaterial.values().length];
        CooldownHelper$WhenMappings.$EnumSwitchMapping$0[Item.ToolMaterial.IRON.ordinal()] = 1;
        CooldownHelper$WhenMappings.$EnumSwitchMapping$0[Item.ToolMaterial.WOOD.ordinal()] = 2;
        CooldownHelper$WhenMappings.$EnumSwitchMapping$0[Item.ToolMaterial.STONE.ordinal()] = 3;
    }
}

