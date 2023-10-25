/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.world.World;

public interface IRenderChunkFactory {
    public RenderChunk create(World var1, RenderGlobal var2, int var3);
}

