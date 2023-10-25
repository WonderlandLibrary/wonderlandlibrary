/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils;

import baritone.api.BaritoneAPI;
import baritone.api.event.events.RenderEvent;
import baritone.api.pathing.calc.IPath;
import baritone.api.pathing.goals.Goal;
import baritone.api.pathing.goals.GoalComposite;
import baritone.api.pathing.goals.GoalGetToBlock;
import baritone.api.pathing.goals.GoalInverted;
import baritone.api.pathing.goals.GoalTwoBlocks;
import baritone.api.pathing.goals.GoalXZ;
import baritone.api.pathing.goals.GoalYLevel;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.Helper;
import baritone.api.utils.interfaces.IGoalRenderPos;
import baritone.behavior.PathingBehavior;
import baritone.pathing.path.PathExecutor;
import baritone.utils.BlockStateInterface;
import baritone.utils.GuiClick;
import baritone.utils.IRenderer;
import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public final class PathRenderer
implements IRenderer {
    private PathRenderer() {
    }

    public static void render(RenderEvent event, PathingBehavior behavior) {
        int currentRenderViewDimension;
        int thisPlayerDimension;
        float partialTicks = event.getPartialTicks();
        Goal goal = behavior.getGoal();
        if (Helper.mc.currentScreen instanceof GuiClick) {
            ((GuiClick)Helper.mc.currentScreen).onRender();
        }
        if ((thisPlayerDimension = behavior.baritone.getPlayerContext().world().provider.getDimensionType().getId()) != (currentRenderViewDimension = BaritoneAPI.getProvider().getPrimaryBaritone().getPlayerContext().world().provider.getDimensionType().getId())) {
            return;
        }
        Entity renderView = Helper.mc.getRenderViewEntity();
        if (renderView.world != BaritoneAPI.getProvider().getPrimaryBaritone().getPlayerContext().world()) {
            System.out.println("I have no idea what's going on");
            System.out.println("The primary baritone is in a different world than the render view entity");
            System.out.println("Not rendering the path");
            return;
        }
        if (goal != null && ((Boolean)PathRenderer.settings.renderGoal.value).booleanValue()) {
            PathRenderer.drawDankLitGoalBox(renderView, goal, partialTicks, (Color)PathRenderer.settings.colorGoalBox.value);
        }
        if (!((Boolean)PathRenderer.settings.renderPath.value).booleanValue()) {
            return;
        }
        PathExecutor current = behavior.getCurrent();
        PathExecutor next = behavior.getNext();
        if (current != null && ((Boolean)PathRenderer.settings.renderSelectionBoxes.value).booleanValue()) {
            PathRenderer.drawManySelectionBoxes(renderView, current.toBreak(), (Color)PathRenderer.settings.colorBlocksToBreak.value);
            PathRenderer.drawManySelectionBoxes(renderView, current.toPlace(), (Color)PathRenderer.settings.colorBlocksToPlace.value);
            PathRenderer.drawManySelectionBoxes(renderView, current.toWalkInto(), (Color)PathRenderer.settings.colorBlocksToWalkInto.value);
        }
        if (current != null && current.getPath() != null) {
            int renderBegin = Math.max(current.getPosition() - 3, 0);
            PathRenderer.drawPath(current.getPath(), renderBegin, (Color)PathRenderer.settings.colorCurrentPath.value, (Boolean)PathRenderer.settings.fadePath.value, 10, 20);
        }
        if (next != null && next.getPath() != null) {
            PathRenderer.drawPath(next.getPath(), 0, (Color)PathRenderer.settings.colorNextPath.value, (Boolean)PathRenderer.settings.fadePath.value, 10, 20);
        }
        behavior.getInProgress().ifPresent(currentlyRunning -> {
            currentlyRunning.bestPathSoFar().ifPresent(p -> PathRenderer.drawPath(p, 0, (Color)PathRenderer.settings.colorBestPathSoFar.value, (Boolean)PathRenderer.settings.fadePath.value, 10, 20));
            currentlyRunning.pathToMostRecentNodeConsidered().ifPresent(mr -> {
                PathRenderer.drawPath(mr, 0, (Color)PathRenderer.settings.colorMostRecentConsidered.value, (Boolean)PathRenderer.settings.fadePath.value, 10, 20);
                PathRenderer.drawManySelectionBoxes(renderView, Collections.singletonList(mr.getDest()), (Color)PathRenderer.settings.colorMostRecentConsidered.value);
            });
        });
    }

    public static void drawPath(IPath path, int startIndex, Color color, boolean fadeOut, int fadeStart0, int fadeEnd0) {
        IRenderer.startLines(color, ((Float)PathRenderer.settings.pathRenderLineWidthPixels.value).floatValue(), (Boolean)PathRenderer.settings.renderPathIgnoreDepth.value);
        int fadeStart = fadeStart0 + startIndex;
        int fadeEnd = fadeEnd0 + startIndex;
        List<BetterBlockPos> positions = path.positions();
        int i = startIndex;
        while (i < positions.size() - 1) {
            BetterBlockPos start = positions.get(i);
            int next = i + 1;
            BetterBlockPos end = positions.get(next);
            int dirX = end.x - start.x;
            int dirY = end.y - start.y;
            int dirZ = end.z - start.z;
            while (!(next + 1 >= positions.size() || fadeOut && next + 1 >= fadeStart || dirX != positions.get((int)(next + 1)).x - end.x || dirY != positions.get((int)(next + 1)).y - end.y || dirZ != positions.get((int)(next + 1)).z - end.z)) {
                end = positions.get(++next);
            }
            if (fadeOut) {
                float alpha;
                if (i <= fadeStart) {
                    alpha = 0.4f;
                } else {
                    if (i > fadeEnd) break;
                    alpha = 0.4f * (1.0f - (float)(i - fadeStart) / (float)(fadeEnd - fadeStart));
                }
                IRenderer.glColor(color, alpha);
            }
            PathRenderer.drawLine(start.x, start.y, start.z, end.x, end.y, end.z);
            tessellator.draw();
            i = next;
        }
        IRenderer.endLines((Boolean)PathRenderer.settings.renderPathIgnoreDepth.value);
    }

    public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2) {
        double vpX = PathRenderer.renderManager.viewerPosX;
        double vpY = PathRenderer.renderManager.viewerPosY;
        double vpZ = PathRenderer.renderManager.viewerPosZ;
        boolean renderPathAsFrickinThingy = (Boolean)PathRenderer.settings.renderPathAsLine.value == false;
        buffer.begin(renderPathAsFrickinThingy ? 3 : 1, DefaultVertexFormats.POSITION);
        buffer.pos(x1 + 0.5 - vpX, y1 + 0.5 - vpY, z1 + 0.5 - vpZ).endVertex();
        buffer.pos(x2 + 0.5 - vpX, y2 + 0.5 - vpY, z2 + 0.5 - vpZ).endVertex();
        if (renderPathAsFrickinThingy) {
            buffer.pos(x2 + 0.5 - vpX, y2 + 0.53 - vpY, z2 + 0.5 - vpZ).endVertex();
            buffer.pos(x1 + 0.5 - vpX, y1 + 0.53 - vpY, z1 + 0.5 - vpZ).endVertex();
            buffer.pos(x1 + 0.5 - vpX, y1 + 0.5 - vpY, z1 + 0.5 - vpZ).endVertex();
        }
    }

    public static void drawManySelectionBoxes(Entity player, Collection<BlockPos> positions, Color color) {
        IRenderer.startLines(color, ((Float)PathRenderer.settings.pathRenderLineWidthPixels.value).floatValue(), (Boolean)PathRenderer.settings.renderSelectionBoxesIgnoreDepth.value);
        BlockStateInterface bsi = new BlockStateInterface(BaritoneAPI.getProvider().getPrimaryBaritone().getPlayerContext());
        positions.forEach(pos -> {
            IBlockState state = bsi.get0((BlockPos)pos);
            AxisAlignedBB toDraw = state.getBlock().equals(Blocks.AIR) ? Blocks.DIRT.getDefaultState().getSelectedBoundingBox(player.world, (BlockPos)pos) : state.getSelectedBoundingBox(player.world, (BlockPos)pos);
            IRenderer.drawAABB(toDraw, 0.002);
        });
        IRenderer.endLines((Boolean)PathRenderer.settings.renderSelectionBoxesIgnoreDepth.value);
    }

    public static void drawDankLitGoalBox(Entity player, Goal goal, float partialTicks, Color color) {
        double maxY;
        double minY;
        double y2;
        double y1;
        double maxZ;
        double minZ;
        double maxX;
        double minX;
        double renderPosX = PathRenderer.renderManager.viewerPosX;
        double renderPosY = PathRenderer.renderManager.viewerPosY;
        double renderPosZ = PathRenderer.renderManager.viewerPosZ;
        double y = MathHelper.cos((float)((double)((float)(System.nanoTime() / 100000L % 20000L) / 20000.0f) * Math.PI * 2.0));
        if (goal instanceof IGoalRenderPos) {
            BlockPos goalPos = ((IGoalRenderPos)((Object)goal)).getGoalPos();
            minX = (double)goalPos.getX() + 0.002 - renderPosX;
            maxX = (double)(goalPos.getX() + 1) - 0.002 - renderPosX;
            minZ = (double)goalPos.getZ() + 0.002 - renderPosZ;
            maxZ = (double)(goalPos.getZ() + 1) - 0.002 - renderPosZ;
            if (goal instanceof GoalGetToBlock || goal instanceof GoalTwoBlocks) {
                y /= 2.0;
            }
            y1 = 1.0 + y + (double)goalPos.getY() - renderPosY;
            y2 = 1.0 - y + (double)goalPos.getY() - renderPosY;
            minY = (double)goalPos.getY() - renderPosY;
            maxY = minY + 2.0;
            if (goal instanceof GoalGetToBlock || goal instanceof GoalTwoBlocks) {
                y1 -= 0.5;
                y2 -= 0.5;
                maxY -= 1.0;
            }
        } else if (goal instanceof GoalXZ) {
            GoalXZ goalPos = (GoalXZ)goal;
            if (((Boolean)PathRenderer.settings.renderGoalXZBeacon.value).booleanValue()) {
                GL11.glPushAttrib(64);
                Helper.mc.getTextureManager().bindTexture(TileEntityBeaconRenderer.TEXTURE_BEACON_BEAM);
                if (((Boolean)PathRenderer.settings.renderGoalIgnoreDepth.value).booleanValue()) {
                    GlStateManager.disableDepth();
                }
                TileEntityBeaconRenderer.renderBeamSegment((double)goalPos.getX() - renderPosX, -renderPosY, (double)goalPos.getZ() - renderPosZ, partialTicks, 1.0, player.world.getTotalWorldTime(), 0, 256, color.getColorComponents(null));
                if (((Boolean)PathRenderer.settings.renderGoalIgnoreDepth.value).booleanValue()) {
                    GlStateManager.enableDepth();
                }
                GL11.glPopAttrib();
                return;
            }
            minX = (double)goalPos.getX() + 0.002 - renderPosX;
            maxX = (double)(goalPos.getX() + 1) - 0.002 - renderPosX;
            minZ = (double)goalPos.getZ() + 0.002 - renderPosZ;
            maxZ = (double)(goalPos.getZ() + 1) - 0.002 - renderPosZ;
            y1 = 0.0;
            y2 = 0.0;
            minY = 0.0 - renderPosY;
            maxY = 256.0 - renderPosY;
        } else {
            if (goal instanceof GoalComposite) {
                for (Goal g : ((GoalComposite)goal).goals()) {
                    PathRenderer.drawDankLitGoalBox(player, g, partialTicks, color);
                }
                return;
            }
            if (goal instanceof GoalInverted) {
                PathRenderer.drawDankLitGoalBox(player, ((GoalInverted)goal).origin, partialTicks, (Color)PathRenderer.settings.colorInvertedGoalBox.value);
                return;
            }
            if (goal instanceof GoalYLevel) {
                GoalYLevel goalpos = (GoalYLevel)goal;
                minX = player.posX - (Double)PathRenderer.settings.yLevelBoxSize.value - renderPosX;
                minZ = player.posZ - (Double)PathRenderer.settings.yLevelBoxSize.value - renderPosZ;
                maxX = player.posX + (Double)PathRenderer.settings.yLevelBoxSize.value - renderPosX;
                maxZ = player.posZ + (Double)PathRenderer.settings.yLevelBoxSize.value - renderPosZ;
                minY = (double)((GoalYLevel)goal).level - renderPosY;
                maxY = minY + 2.0;
                y1 = 1.0 + y + (double)goalpos.level - renderPosY;
                y2 = 1.0 - y + (double)goalpos.level - renderPosY;
            } else {
                return;
            }
        }
        IRenderer.startLines(color, ((Float)PathRenderer.settings.goalRenderLineWidthPixels.value).floatValue(), (Boolean)PathRenderer.settings.renderGoalIgnoreDepth.value);
        PathRenderer.renderHorizontalQuad(minX, maxX, minZ, maxZ, y1);
        PathRenderer.renderHorizontalQuad(minX, maxX, minZ, maxZ, y2);
        buffer.begin(1, DefaultVertexFormats.POSITION);
        buffer.pos(minX, minY, minZ).endVertex();
        buffer.pos(minX, maxY, minZ).endVertex();
        buffer.pos(maxX, minY, minZ).endVertex();
        buffer.pos(maxX, maxY, minZ).endVertex();
        buffer.pos(maxX, minY, maxZ).endVertex();
        buffer.pos(maxX, maxY, maxZ).endVertex();
        buffer.pos(minX, minY, maxZ).endVertex();
        buffer.pos(minX, maxY, maxZ).endVertex();
        tessellator.draw();
        IRenderer.endLines((Boolean)PathRenderer.settings.renderGoalIgnoreDepth.value);
    }

    private static void renderHorizontalQuad(double minX, double maxX, double minZ, double maxZ, double y) {
        if (y != 0.0) {
            buffer.begin(2, DefaultVertexFormats.POSITION);
            buffer.pos(minX, y, minZ).endVertex();
            buffer.pos(maxX, y, minZ).endVertex();
            buffer.pos(maxX, y, maxZ).endVertex();
            buffer.pos(minX, y, maxZ).endVertex();
            tessellator.draw();
        }
    }
}

