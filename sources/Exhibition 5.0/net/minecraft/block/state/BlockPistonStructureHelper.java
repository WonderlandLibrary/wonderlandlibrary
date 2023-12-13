// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.state;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockPistonBase;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockPistonStructureHelper
{
    private final World field_177261_a;
    private final BlockPos field_177259_b;
    private final BlockPos field_177260_c;
    private final EnumFacing field_177257_d;
    private final List field_177258_e;
    private final List field_177256_f;
    private static final String __OBFID = "CL_00002033";
    
    public BlockPistonStructureHelper(final World worldIn, final BlockPos p_i45664_2_, final EnumFacing p_i45664_3_, final boolean p_i45664_4_) {
        this.field_177258_e = Lists.newArrayList();
        this.field_177256_f = Lists.newArrayList();
        this.field_177261_a = worldIn;
        this.field_177259_b = p_i45664_2_;
        if (p_i45664_4_) {
            this.field_177257_d = p_i45664_3_;
            this.field_177260_c = p_i45664_2_.offset(p_i45664_3_);
        }
        else {
            this.field_177257_d = p_i45664_3_.getOpposite();
            this.field_177260_c = p_i45664_2_.offset(p_i45664_3_, 2);
        }
    }
    
    public boolean func_177253_a() {
        this.field_177258_e.clear();
        this.field_177256_f.clear();
        final Block var1 = this.field_177261_a.getBlockState(this.field_177260_c).getBlock();
        if (!BlockPistonBase.func_180696_a(var1, this.field_177261_a, this.field_177260_c, this.field_177257_d, false)) {
            if (var1.getMobilityFlag() != 1) {
                return false;
            }
            this.field_177256_f.add(this.field_177260_c);
            return true;
        }
        else {
            if (!this.func_177251_a(this.field_177260_c)) {
                return false;
            }
            for (int var2 = 0; var2 < this.field_177258_e.size(); ++var2) {
                final BlockPos var3 = this.field_177258_e.get(var2);
                if (this.field_177261_a.getBlockState(var3).getBlock() == Blocks.slime_block && !this.func_177250_b(var3)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    private boolean func_177251_a(final BlockPos p_177251_1_) {
        Block var2 = this.field_177261_a.getBlockState(p_177251_1_).getBlock();
        if (var2.getMaterial() == Material.air) {
            return true;
        }
        if (!BlockPistonBase.func_180696_a(var2, this.field_177261_a, p_177251_1_, this.field_177257_d, false)) {
            return true;
        }
        if (p_177251_1_.equals(this.field_177259_b)) {
            return true;
        }
        if (this.field_177258_e.contains(p_177251_1_)) {
            return true;
        }
        int var3 = 1;
        if (var3 + this.field_177258_e.size() > 12) {
            return false;
        }
        while (var2 == Blocks.slime_block) {
            final BlockPos var4 = p_177251_1_.offset(this.field_177257_d.getOpposite(), var3);
            var2 = this.field_177261_a.getBlockState(var4).getBlock();
            if (var2.getMaterial() == Material.air || !BlockPistonBase.func_180696_a(var2, this.field_177261_a, var4, this.field_177257_d, false)) {
                break;
            }
            if (var4.equals(this.field_177259_b)) {
                break;
            }
            if (++var3 + this.field_177258_e.size() > 12) {
                return false;
            }
        }
        int var5 = 0;
        for (int var6 = var3 - 1; var6 >= 0; --var6) {
            this.field_177258_e.add(p_177251_1_.offset(this.field_177257_d.getOpposite(), var6));
            ++var5;
        }
        int var6 = 1;
        while (true) {
            final BlockPos var7 = p_177251_1_.offset(this.field_177257_d, var6);
            final int var8 = this.field_177258_e.indexOf(var7);
            if (var8 > -1) {
                this.func_177255_a(var5, var8);
                for (int var9 = 0; var9 <= var8 + var5; ++var9) {
                    final BlockPos var10 = this.field_177258_e.get(var9);
                    if (this.field_177261_a.getBlockState(var10).getBlock() == Blocks.slime_block && !this.func_177250_b(var10)) {
                        return false;
                    }
                }
                return true;
            }
            var2 = this.field_177261_a.getBlockState(var7).getBlock();
            if (var2.getMaterial() == Material.air) {
                return true;
            }
            if (!BlockPistonBase.func_180696_a(var2, this.field_177261_a, var7, this.field_177257_d, true) || var7.equals(this.field_177259_b)) {
                return false;
            }
            if (var2.getMobilityFlag() == 1) {
                this.field_177256_f.add(var7);
                return true;
            }
            if (this.field_177258_e.size() >= 12) {
                return false;
            }
            this.field_177258_e.add(var7);
            ++var5;
            ++var6;
        }
    }
    
    private void func_177255_a(final int p_177255_1_, final int p_177255_2_) {
        final ArrayList var3 = Lists.newArrayList();
        final ArrayList var4 = Lists.newArrayList();
        final ArrayList var5 = Lists.newArrayList();
        var3.addAll(this.field_177258_e.subList(0, p_177255_2_));
        var4.addAll(this.field_177258_e.subList(this.field_177258_e.size() - p_177255_1_, this.field_177258_e.size()));
        var5.addAll(this.field_177258_e.subList(p_177255_2_, this.field_177258_e.size() - p_177255_1_));
        this.field_177258_e.clear();
        this.field_177258_e.addAll(var3);
        this.field_177258_e.addAll(var4);
        this.field_177258_e.addAll(var5);
    }
    
    private boolean func_177250_b(final BlockPos p_177250_1_) {
        for (final EnumFacing var5 : EnumFacing.values()) {
            if (var5.getAxis() != this.field_177257_d.getAxis() && !this.func_177251_a(p_177250_1_.offset(var5))) {
                return false;
            }
        }
        return true;
    }
    
    public List func_177254_c() {
        return this.field_177258_e;
    }
    
    public List func_177252_d() {
        return this.field_177256_f;
    }
}
