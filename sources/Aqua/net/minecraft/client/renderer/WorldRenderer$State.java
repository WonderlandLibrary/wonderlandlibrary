package net.minecraft.client.renderer;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class WorldRenderer.State {
    private final int[] stateRawBuffer;
    private final VertexFormat stateVertexFormat;
    private TextureAtlasSprite[] stateQuadSprites;

    public WorldRenderer.State(int[] p_i1_2_, VertexFormat p_i1_3_, TextureAtlasSprite[] p_i1_4_) {
        this.stateRawBuffer = p_i1_2_;
        this.stateVertexFormat = p_i1_3_;
        this.stateQuadSprites = p_i1_4_;
    }

    public WorldRenderer.State(int[] buffer, VertexFormat format) {
        this.stateRawBuffer = buffer;
        this.stateVertexFormat = format;
    }

    public int[] getRawBuffer() {
        return this.stateRawBuffer;
    }

    public int getVertexCount() {
        return this.stateRawBuffer.length / this.stateVertexFormat.getIntegerSize();
    }

    public VertexFormat getVertexFormat() {
        return this.stateVertexFormat;
    }

    static /* synthetic */ TextureAtlasSprite[] access$000(WorldRenderer.State x0) {
        return x0.stateQuadSprites;
    }
}
