// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.tutorial;

import net.minecraft.item.ItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInput;

public interface ITutorialStep
{
    default void onStop() {
    }
    
    default void update() {
    }
    
    default void handleMovement(final MovementInput input) {
    }
    
    default void handleMouse(final MouseHelper mouseHelperIn) {
    }
    
    default void onMouseHover(final WorldClient worldIn, final RayTraceResult result) {
    }
    
    default void onHitBlock(final WorldClient worldIn, final BlockPos pos, final IBlockState state, final float diggingStage) {
    }
    
    default void openInventory() {
    }
    
    default void handleSetSlot(final ItemStack stack) {
    }
}
