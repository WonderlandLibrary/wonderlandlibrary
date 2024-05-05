/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.state;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockWorldState {
    private final World world;
    private final BlockPos pos;
    private final boolean field_181628_c;
    private IBlockState state;
    private TileEntity tileEntity;
    private boolean tileEntityInitialized;

    public BlockWorldState(World p_i46451_1_, BlockPos p_i46451_2_, boolean p_i46451_3_) {
        this.world = p_i46451_1_;
        this.pos = p_i46451_2_;
        this.field_181628_c = p_i46451_3_;
    }

    public IBlockState getBlockState() {
        if (this.state != null) return this.state;
        if (!this.field_181628_c) {
            if (!this.world.isBlockLoaded(this.pos)) return this.state;
        }
        this.state = this.world.getBlockState(this.pos);
        return this.state;
    }

    public TileEntity getTileEntity() {
        if (this.tileEntity != null) return this.tileEntity;
        if (this.tileEntityInitialized) return this.tileEntity;
        this.tileEntity = this.world.getTileEntity(this.pos);
        this.tileEntityInitialized = true;
        return this.tileEntity;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public static Predicate<BlockWorldState> hasState(final Predicate<IBlockState> p_177510_0_) {
        return new Predicate<BlockWorldState>(){

            @Override
            public boolean apply(BlockWorldState p_apply_1_) {
                if (p_apply_1_ == null) return false;
                if (!p_177510_0_.apply(p_apply_1_.getBlockState())) return false;
                return true;
            }
        };
    }
}

