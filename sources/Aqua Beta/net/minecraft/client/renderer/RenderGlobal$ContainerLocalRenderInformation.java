package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumFacing;

public static class RenderGlobal.ContainerLocalRenderInformation {
    final RenderChunk renderChunk;
    EnumFacing facing;
    int setFacing;

    public RenderGlobal.ContainerLocalRenderInformation(RenderChunk p_i2_1_, EnumFacing p_i2_2_, int p_i2_3_) {
        this.renderChunk = p_i2_1_;
        this.facing = p_i2_2_;
        this.setFacing = p_i2_3_;
    }

    public void setFacingBit(byte p_setFacingBit_1_, EnumFacing p_setFacingBit_2_) {
        this.setFacing = this.setFacing | p_setFacingBit_1_ | 1 << p_setFacingBit_2_.ordinal();
    }

    public boolean isFacingBit(EnumFacing p_isFacingBit_1_) {
        return (this.setFacing & 1 << p_isFacingBit_1_.ordinal()) > 0;
    }

    private void initialize(EnumFacing p_initialize_1_, int p_initialize_2_) {
        this.facing = p_initialize_1_;
        this.setFacing = p_initialize_2_;
    }

    static /* synthetic */ void access$000(RenderGlobal.ContainerLocalRenderInformation x0, EnumFacing x1, int x2) {
        x0.initialize(x1, x2);
    }
}
