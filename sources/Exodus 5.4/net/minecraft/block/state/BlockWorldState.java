/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.block.state;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockWorldState {
    private TileEntity tileEntity;
    private final World world;
    private final boolean field_181628_c;
    private boolean tileEntityInitialized;
    private IBlockState state;
    private final BlockPos pos;

    public BlockPos getPos() {
        return this.pos;
    }

    public IBlockState getBlockState() {
        if (this.state == null && (this.field_181628_c || this.world.isBlockLoaded(this.pos))) {
            this.state = this.world.getBlockState(this.pos);
        }
        return this.state;
    }

    public BlockWorldState(World world, BlockPos blockPos, boolean bl) {
        this.world = world;
        this.pos = blockPos;
        this.field_181628_c = bl;
    }

    public TileEntity getTileEntity() {
        if (this.tileEntity == null && !this.tileEntityInitialized) {
            this.tileEntity = this.world.getTileEntity(this.pos);
            this.tileEntityInitialized = true;
        }
        return this.tileEntity;
    }

    public static Predicate<BlockWorldState> hasState(final Predicate<IBlockState> predicate) {
        return new Predicate<BlockWorldState>(){

            public boolean apply(BlockWorldState blockWorldState) {
                return blockWorldState != null && predicate.apply((Object)blockWorldState.getBlockState());
            }
        };
    }
}

