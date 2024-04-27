/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.Vec3i
 */
package net.dev.important.injection.forge.mixins.performance;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value={BlockPos.class})
public abstract class BlockPosMixin_ReduceAllocations
extends Vec3i {
    public BlockPosMixin_ReduceAllocations(int xIn, int yIn, int zIn) {
        super(xIn, yIn, zIn);
    }

    @Overwrite
    public BlockPos func_177984_a() {
        return new BlockPos(this.func_177958_n(), this.func_177956_o() + 1, this.func_177952_p());
    }

    @Overwrite
    public BlockPos func_177981_b(int offset) {
        return offset == 0 ? (BlockPos)this : new BlockPos(this.func_177958_n(), this.func_177956_o() + offset, this.func_177952_p());
    }

    @Overwrite
    public BlockPos func_177977_b() {
        return new BlockPos(this.func_177958_n(), this.func_177956_o() - 1, this.func_177952_p());
    }

    @Overwrite
    public BlockPos func_177979_c(int offset) {
        return offset == 0 ? (BlockPos)this : new BlockPos(this.func_177958_n(), this.func_177956_o() - offset, this.func_177952_p());
    }

    @Overwrite
    public BlockPos func_177978_c() {
        return new BlockPos(this.func_177958_n(), this.func_177956_o(), this.func_177952_p() - 1);
    }

    @Overwrite
    public BlockPos func_177964_d(int offset) {
        return offset == 0 ? (BlockPos)this : new BlockPos(this.func_177958_n(), this.func_177956_o(), this.func_177952_p() - offset);
    }

    @Overwrite
    public BlockPos func_177968_d() {
        return new BlockPos(this.func_177958_n(), this.func_177956_o(), this.func_177952_p() + 1);
    }

    @Overwrite
    public BlockPos func_177970_e(int offset) {
        return offset == 0 ? (BlockPos)this : new BlockPos(this.func_177958_n(), this.func_177956_o(), this.func_177952_p() + offset);
    }

    @Overwrite
    public BlockPos func_177976_e() {
        return new BlockPos(this.func_177958_n() - 1, this.func_177956_o(), this.func_177952_p());
    }

    @Overwrite
    public BlockPos func_177985_f(int offset) {
        return offset == 0 ? (BlockPos)this : new BlockPos(this.func_177958_n() - offset, this.func_177956_o(), this.func_177952_p());
    }

    @Overwrite
    public BlockPos func_177974_f() {
        return new BlockPos(this.func_177958_n() + 1, this.func_177956_o(), this.func_177952_p());
    }

    @Overwrite
    public BlockPos func_177965_g(int offset) {
        return offset == 0 ? (BlockPos)this : new BlockPos(this.func_177958_n() + offset, this.func_177956_o(), this.func_177952_p());
    }

    @Overwrite
    public BlockPos func_177972_a(EnumFacing direction) {
        return new BlockPos(this.func_177958_n() + direction.func_82601_c(), this.func_177956_o() + direction.func_96559_d(), this.func_177952_p() + direction.func_82599_e());
    }
}

