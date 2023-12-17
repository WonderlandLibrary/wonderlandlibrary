// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.debug;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.pathfinding.PathPoint;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.GlStateManager;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class DebugRendererPathfinding implements DebugRenderer.IDebugRenderer
{
    private final Minecraft minecraft;
    private final Map<Integer, Path> pathMap;
    private final Map<Integer, Float> pathMaxDistance;
    private final Map<Integer, Long> creationMap;
    private EntityPlayer player;
    private double xo;
    private double yo;
    private double zo;
    
    public DebugRendererPathfinding(final Minecraft minecraftIn) {
        this.pathMap = (Map<Integer, Path>)Maps.newHashMap();
        this.pathMaxDistance = (Map<Integer, Float>)Maps.newHashMap();
        this.creationMap = (Map<Integer, Long>)Maps.newHashMap();
        this.minecraft = minecraftIn;
    }
    
    public void addPath(final int eid, final Path pathIn, final float distance) {
        this.pathMap.put(eid, pathIn);
        this.creationMap.put(eid, System.currentTimeMillis());
        this.pathMaxDistance.put(eid, distance);
    }
    
    @Override
    public void render(final float partialTicks, final long finishTimeNano) {
        if (!this.pathMap.isEmpty()) {
            final long i = System.currentTimeMillis();
            final Minecraft minecraft = this.minecraft;
            this.player = Minecraft.player;
            this.xo = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * partialTicks;
            this.yo = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * partialTicks;
            this.zo = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(0.0f, 1.0f, 0.0f, 0.75f);
            GlStateManager.disableTexture2D();
            GlStateManager.glLineWidth(6.0f);
            for (final Integer integer : this.pathMap.keySet()) {
                final Path path = this.pathMap.get(integer);
                final float f = this.pathMaxDistance.get(integer);
                this.renderPathLine(partialTicks, path);
                final PathPoint pathpoint = path.getTarget();
                if (this.addDistanceToPlayer(pathpoint) <= 40.0f) {
                    RenderGlobal.renderFilledBox(new AxisAlignedBB(pathpoint.x + 0.25f, pathpoint.y + 0.25f, pathpoint.z + 0.25, pathpoint.x + 0.75f, pathpoint.y + 0.75f, pathpoint.z + 0.75f).offset(-this.xo, -this.yo, -this.zo), 0.0f, 1.0f, 0.0f, 0.5f);
                    for (int j = 0; j < path.getCurrentPathLength(); ++j) {
                        final PathPoint pathpoint2 = path.getPathPointFromIndex(j);
                        if (this.addDistanceToPlayer(pathpoint2) <= 40.0f) {
                            final float f2 = (j == path.getCurrentPathIndex()) ? 1.0f : 0.0f;
                            final float f3 = (j == path.getCurrentPathIndex()) ? 0.0f : 1.0f;
                            RenderGlobal.renderFilledBox(new AxisAlignedBB(pathpoint2.x + 0.5f - f, pathpoint2.y + 0.01f * j, pathpoint2.z + 0.5f - f, pathpoint2.x + 0.5f + f, pathpoint2.y + 0.25f + 0.01f * j, pathpoint2.z + 0.5f + f).offset(-this.xo, -this.yo, -this.zo), f2, 0.0f, f3, 0.5f);
                        }
                    }
                }
            }
            for (final Integer integer2 : this.pathMap.keySet()) {
                final Path path2 = this.pathMap.get(integer2);
                for (final PathPoint pathpoint3 : path2.getClosedSet()) {
                    if (this.addDistanceToPlayer(pathpoint3) <= 40.0f) {
                        DebugRenderer.renderDebugText(String.format("%s", pathpoint3.nodeType), pathpoint3.x + 0.5, pathpoint3.y + 0.75, pathpoint3.z + 0.5, partialTicks, -65536);
                        DebugRenderer.renderDebugText(String.format("%.2f", pathpoint3.costMalus), pathpoint3.x + 0.5, pathpoint3.y + 0.25, pathpoint3.z + 0.5, partialTicks, -65536);
                    }
                }
                for (final PathPoint pathpoint4 : path2.getOpenSet()) {
                    if (this.addDistanceToPlayer(pathpoint4) <= 40.0f) {
                        DebugRenderer.renderDebugText(String.format("%s", pathpoint4.nodeType), pathpoint4.x + 0.5, pathpoint4.y + 0.75, pathpoint4.z + 0.5, partialTicks, -16776961);
                        DebugRenderer.renderDebugText(String.format("%.2f", pathpoint4.costMalus), pathpoint4.x + 0.5, pathpoint4.y + 0.25, pathpoint4.z + 0.5, partialTicks, -16776961);
                    }
                }
                for (int k = 0; k < path2.getCurrentPathLength(); ++k) {
                    final PathPoint pathpoint5 = path2.getPathPointFromIndex(k);
                    if (this.addDistanceToPlayer(pathpoint5) <= 40.0f) {
                        DebugRenderer.renderDebugText(String.format("%s", pathpoint5.nodeType), pathpoint5.x + 0.5, pathpoint5.y + 0.75, pathpoint5.z + 0.5, partialTicks, -1);
                        DebugRenderer.renderDebugText(String.format("%.2f", pathpoint5.costMalus), pathpoint5.x + 0.5, pathpoint5.y + 0.25, pathpoint5.z + 0.5, partialTicks, -1);
                    }
                }
            }
            for (final Integer integer3 : this.creationMap.keySet().toArray(new Integer[0])) {
                if (i - this.creationMap.get(integer3) > 20000L) {
                    this.pathMap.remove(integer3);
                    this.creationMap.remove(integer3);
                }
            }
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }
    
    public void renderPathLine(final float finishTimeNano, final Path pathIn) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < pathIn.getCurrentPathLength(); ++i) {
            final PathPoint pathpoint = pathIn.getPathPointFromIndex(i);
            if (this.addDistanceToPlayer(pathpoint) <= 40.0f) {
                final float f = i / (float)pathIn.getCurrentPathLength() * 0.33f;
                final int j = (i == 0) ? 0 : MathHelper.hsvToRGB(f, 0.9f, 0.9f);
                final int k = j >> 16 & 0xFF;
                final int l = j >> 8 & 0xFF;
                final int i2 = j & 0xFF;
                bufferbuilder.pos(pathpoint.x - this.xo + 0.5, pathpoint.y - this.yo + 0.5, pathpoint.z - this.zo + 0.5).color(k, l, i2, 255).endVertex();
            }
        }
        tessellator.draw();
    }
    
    private float addDistanceToPlayer(final PathPoint point) {
        return (float)(Math.abs(point.x - this.player.posX) + Math.abs(point.y - this.player.posY) + Math.abs(point.z - this.player.posZ));
    }
}
