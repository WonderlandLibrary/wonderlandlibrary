package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract interface IRenderChunkFactory
{
  public abstract RenderChunk func_178602_a(World paramWorld, RenderGlobal paramRenderGlobal, BlockPos paramBlockPos, int paramInt);
}
