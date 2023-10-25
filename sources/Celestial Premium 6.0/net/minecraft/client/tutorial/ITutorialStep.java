/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.tutorial;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public interface ITutorialStep {
    default public void func_193248_b() {
    }

    default public void func_193245_a() {
    }

    default public void func_193247_a(MovementInput p_193247_1_) {
    }

    default public void func_193249_a(MouseHelper p_193249_1_) {
    }

    default public void func_193246_a(WorldClient p_193246_1_, RayTraceResult p_193246_2_) {
    }

    default public void func_193250_a(WorldClient p_193250_1_, BlockPos p_193250_2_, IBlockState p_193250_3_, float p_193250_4_) {
    }

    default public void func_193251_c() {
    }

    default public void func_193252_a(ItemStack p_193252_1_) {
    }
}

