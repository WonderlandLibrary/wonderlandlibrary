/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class DebugRendererPathfinding
implements DebugRenderer.IDebugRenderer {
    private final Minecraft minecraft;
    private final Map<Integer, Path> pathMap = Maps.newHashMap();
    private final Map<Integer, Float> pathMaxDistance = Maps.newHashMap();
    private final Map<Integer, Long> creationMap = Maps.newHashMap();
    private EntityPlayer player;
    private double xo;
    private double yo;
    private double zo;

    public DebugRendererPathfinding(Minecraft minecraftIn) {
        this.minecraft = minecraftIn;
    }

    public void addPath(int p_188289_1_, Path p_188289_2_, float p_188289_3_) {
        this.pathMap.put(p_188289_1_, p_188289_2_);
        this.creationMap.put(p_188289_1_, System.currentTimeMillis());
        this.pathMaxDistance.put(p_188289_1_, Float.valueOf(p_188289_3_));
    }

    @Override
    public void render(float p_190060_1_, long p_190060_2_) {
        if (!this.pathMap.isEmpty()) {
            long i = System.currentTimeMillis();
            this.player = this.minecraft.player;
            this.xo = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * (double)p_190060_1_;
            this.yo = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * (double)p_190060_1_;
            this.zo = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * (double)p_190060_1_;
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(0.0f, 1.0f, 0.0f, 0.75f);
            GlStateManager.disableTexture2D();
            GlStateManager.glLineWidth(6.0f);
            for (Integer integer : this.pathMap.keySet()) {
                Path path = this.pathMap.get(integer);
                float f = this.pathMaxDistance.get(integer).floatValue();
                this.renderPathLine(p_190060_1_, path);
                PathPoint pathpoint = path.getTarget();
                if (!(this.addDistanceToPlayer(pathpoint) <= 40.0f)) continue;
                RenderGlobal.renderFilledBox(new AxisAlignedBB((float)pathpoint.xCoord + 0.25f, (float)pathpoint.yCoord + 0.25f, (double)pathpoint.zCoord + 0.25, (float)pathpoint.xCoord + 0.75f, (float)pathpoint.yCoord + 0.75f, (float)pathpoint.zCoord + 0.75f).offset(-this.xo, -this.yo, -this.zo), 0.0f, 1.0f, 0.0f, 0.5f);
                for (int j = 0; j < path.getCurrentPathLength(); ++j) {
                    PathPoint pathpoint1 = path.getPathPointFromIndex(j);
                    if (!(this.addDistanceToPlayer(pathpoint1) <= 40.0f)) continue;
                    float f1 = j == path.getCurrentPathIndex() ? 1.0f : 0.0f;
                    float f2 = j == path.getCurrentPathIndex() ? 0.0f : 1.0f;
                    RenderGlobal.renderFilledBox(new AxisAlignedBB((float)pathpoint1.xCoord + 0.5f - f, (float)pathpoint1.yCoord + 0.01f * (float)j, (float)pathpoint1.zCoord + 0.5f - f, (float)pathpoint1.xCoord + 0.5f + f, (float)pathpoint1.yCoord + 0.25f + 0.01f * (float)j, (float)pathpoint1.zCoord + 0.5f + f).offset(-this.xo, -this.yo, -this.zo), f1, 0.0f, f2, 0.5f);
                }
            }
            for (Integer integer1 : this.pathMap.keySet()) {
                Path path1 = this.pathMap.get(integer1);
                for (PathPoint pathpoint3 : path1.getClosedSet()) {
                    if (!(this.addDistanceToPlayer(pathpoint3) <= 40.0f)) continue;
                    DebugRenderer.renderDebugText(String.format("%s", new Object[]{pathpoint3.nodeType}), (double)pathpoint3.xCoord + 0.5, (double)pathpoint3.yCoord + 0.75, (double)pathpoint3.zCoord + 0.5, p_190060_1_, -65536);
                    DebugRenderer.renderDebugText(String.format("%.2f", Float.valueOf(pathpoint3.costMalus)), (double)pathpoint3.xCoord + 0.5, (double)pathpoint3.yCoord + 0.25, (double)pathpoint3.zCoord + 0.5, p_190060_1_, -65536);
                }
                for (PathPoint pathpoint4 : path1.getOpenSet()) {
                    if (!(this.addDistanceToPlayer(pathpoint4) <= 40.0f)) continue;
                    DebugRenderer.renderDebugText(String.format("%s", new Object[]{pathpoint4.nodeType}), (double)pathpoint4.xCoord + 0.5, (double)pathpoint4.yCoord + 0.75, (double)pathpoint4.zCoord + 0.5, p_190060_1_, -16776961);
                    DebugRenderer.renderDebugText(String.format("%.2f", Float.valueOf(pathpoint4.costMalus)), (double)pathpoint4.xCoord + 0.5, (double)pathpoint4.yCoord + 0.25, (double)pathpoint4.zCoord + 0.5, p_190060_1_, -16776961);
                }
                for (int k = 0; k < path1.getCurrentPathLength(); ++k) {
                    PathPoint pathpoint2 = path1.getPathPointFromIndex(k);
                    if (!(this.addDistanceToPlayer(pathpoint2) <= 40.0f)) continue;
                    DebugRenderer.renderDebugText(String.format("%s", new Object[]{pathpoint2.nodeType}), (double)pathpoint2.xCoord + 0.5, (double)pathpoint2.yCoord + 0.75, (double)pathpoint2.zCoord + 0.5, p_190060_1_, -1);
                    DebugRenderer.renderDebugText(String.format("%.2f", Float.valueOf(pathpoint2.costMalus)), (double)pathpoint2.xCoord + 0.5, (double)pathpoint2.yCoord + 0.25, (double)pathpoint2.zCoord + 0.5, p_190060_1_, -1);
                }
            }
            for (Integer integer2 : this.creationMap.keySet().toArray(new Integer[0])) {
                if (i - this.creationMap.get(integer2) <= 20000L) continue;
                this.pathMap.remove(integer2);
                this.creationMap.remove(integer2);
            }
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public void renderPathLine(float p_190067_1_, Path p_190067_2_) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < p_190067_2_.getCurrentPathLength(); ++i) {
            PathPoint pathpoint = p_190067_2_.getPathPointFromIndex(i);
            if (!(this.addDistanceToPlayer(pathpoint) <= 40.0f)) continue;
            float f = (float)i / (float)p_190067_2_.getCurrentPathLength() * 0.33f;
            int j = i == 0 ? 0 : MathHelper.hsvToRGB(f, 0.9f, 0.9f);
            int k = j >> 16 & 0xFF;
            int l = j >> 8 & 0xFF;
            int i1 = j & 0xFF;
            bufferbuilder.pos((double)pathpoint.xCoord - this.xo + 0.5, (double)pathpoint.yCoord - this.yo + 0.5, (double)pathpoint.zCoord - this.zo + 0.5).color(k, l, i1, 255).endVertex();
        }
        tessellator.draw();
    }

    private float addDistanceToPlayer(PathPoint p_190066_1_) {
        return (float)(Math.abs((double)p_190066_1_.xCoord - this.player.posX) + Math.abs((double)p_190066_1_.yCoord - this.player.posY) + Math.abs((double)p_190066_1_.zCoord - this.player.posZ));
    }
}

