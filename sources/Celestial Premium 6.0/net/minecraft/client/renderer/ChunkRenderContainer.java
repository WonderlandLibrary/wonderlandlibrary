/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import baritone.Baritone;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.render.EventRenderChunkContainer;
import org.lwjgl.opengl.GL14;

public abstract class ChunkRenderContainer {
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity(17424);
    protected boolean initialized;

    public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn) {
        this.initialized = true;
        this.renderChunks.clear();
        this.viewEntityX = viewEntityXIn;
        this.viewEntityY = viewEntityYIn;
        this.viewEntityZ = viewEntityZIn;
    }

    public void preRenderChunk(RenderChunk renderChunkIn) {
        EventManager.call(new EventRenderChunkContainer(renderChunkIn));
        BlockPos blockpos = this.baritoneGetPosition(renderChunkIn);
        GlStateManager.translate((float)((double)blockpos.getX() - this.viewEntityX), (float)((double)blockpos.getY() - this.viewEntityY), (float)((double)blockpos.getZ() - this.viewEntityZ));
    }

    private BlockPos baritoneGetPosition(RenderChunk renderChunkIn) {
        if (((Boolean)Baritone.settings().renderCachedChunks.value).booleanValue() && !Minecraft.getMinecraft().isSingleplayer() && Minecraft.getMinecraft().world.getChunk(renderChunkIn.getPosition()).isEmpty()) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GL14.glBlendColor(0.0f, 0.0f, 0.0f, ((Float)Baritone.settings().cachedChunksOpacity.value).floatValue());
            GlStateManager.tryBlendFuncSeparate(32771, 32772, 1, 0);
        }
        return renderChunkIn.getPosition();
    }

    public void addRenderChunk(RenderChunk renderChunkIn, BlockRenderLayer layer) {
        this.renderChunks.add(renderChunkIn);
    }

    public abstract void renderChunkLayer(BlockRenderLayer var1);
}

