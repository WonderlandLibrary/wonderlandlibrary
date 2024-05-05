package net.minecraft.client.renderer.chunk;

import java.util.BitSet;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;

static final class CompiledChunk.1
extends CompiledChunk {
    CompiledChunk.1() {
    }

    protected void setLayerUsed(EnumWorldBlockLayer layer) {
        throw new UnsupportedOperationException();
    }

    public void setLayerStarted(EnumWorldBlockLayer layer) {
        throw new UnsupportedOperationException();
    }

    public boolean isVisible(EnumFacing facing, EnumFacing facing2) {
        return false;
    }

    public void setAnimatedSprites(EnumWorldBlockLayer p_setAnimatedSprites_1_, BitSet p_setAnimatedSprites_2_) {
        throw new UnsupportedOperationException();
    }
}
