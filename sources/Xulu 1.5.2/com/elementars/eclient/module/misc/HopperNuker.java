// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import java.util.Iterator;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import com.elementars.eclient.module.Category;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class HopperNuker extends Module
{
    public ArrayList<BlockPos> hoppers;
    int pickaxeSlot;
    
    public HopperNuker() {
        super("HopperNuker", "Nuker for hoppers", 0, Category.MISC, true);
        this.hoppers = new ArrayList<BlockPos>();
    }
    
    @Override
    public void onUpdate() {
        if (this.isToggled()) {
            final Iterable<BlockPos> blocks = (Iterable<BlockPos>)BlockPos.getAllInBox(HopperNuker.mc.player.getPosition().add(-5, -5, -5), HopperNuker.mc.player.getPosition().add(5, 5, 5));
            for (final BlockPos pos : blocks) {
                if (HopperNuker.mc.world.getBlockState(pos).getBlock() == Blocks.HOPPER) {
                    this.pickaxeSlot = -1;
                    for (int i = 0; i < 9 && this.pickaxeSlot == -1; ++i) {
                        final ItemStack stack = HopperNuker.mc.player.inventory.getStackInSlot(i);
                        if (stack != ItemStack.EMPTY) {
                            if (stack.getItem() instanceof ItemPickaxe) {
                                final ItemPickaxe pickaxe = (ItemPickaxe)stack.getItem();
                                this.pickaxeSlot = i;
                            }
                        }
                    }
                    if (this.pickaxeSlot != -1) {
                        HopperNuker.mc.player.inventory.currentItem = this.pickaxeSlot;
                    }
                    HopperNuker.mc.playerController.onPlayerDamageBlock(pos, HopperNuker.mc.player.getHorizontalFacing());
                    HopperNuker.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }
}
