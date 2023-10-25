/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils;

import baritone.Baritone;
import baritone.api.BaritoneAPI;
import baritone.api.command.IBaritoneChatControl;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.Helper;
import baritone.utils.IRenderer;
import baritone.utils.PathRenderer;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collections;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.GLU;

public class GuiClick
extends GuiScreen {
    private final FloatBuffer MODELVIEW = BufferUtils.createFloatBuffer(16);
    private final FloatBuffer PROJECTION = BufferUtils.createFloatBuffer(16);
    private final IntBuffer VIEWPORT = BufferUtils.createIntBuffer(16);
    private final FloatBuffer TO_WORLD_BUFFER = BufferUtils.createFloatBuffer(3);
    private BlockPos clickStart;
    private BlockPos currentMouseOver;

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Vec3d viewerPos;
        RayTraceResult result;
        int mx = Mouse.getX();
        int my = Mouse.getY();
        Vec3d near = this.toWorld(mx, my, 0.0);
        Vec3d far = this.toWorld(mx, my, 1.0);
        if (near != null && far != null && (result = this.mc.world.rayTraceBlocks(near.add(viewerPos = new Vec3d(this.mc.getRenderManager().viewerPosX, this.mc.getRenderManager().viewerPosY, this.mc.getRenderManager().viewerPosZ)), far.add(viewerPos), false, false, true)) != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            this.currentMouseOver = result.getBlockPos();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (this.currentMouseOver != null) {
            if (mouseButton == 0) {
                if (this.clickStart != null && !this.clickStart.equals(this.currentMouseOver)) {
                    BaritoneAPI.getProvider().getPrimaryBaritone().getSelectionManager().removeAllSelections();
                    BaritoneAPI.getProvider().getPrimaryBaritone().getSelectionManager().addSelection(BetterBlockPos.from(this.clickStart), BetterBlockPos.from(this.currentMouseOver));
                    TextComponentString component = new TextComponentString("Selection made! For usage: " + (String)Baritone.settings().prefix.value + "help sel");
                    component.getStyle().setColor(TextFormatting.WHITE).setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, IBaritoneChatControl.FORCE_COMMAND_PREFIX + "help sel"));
                    Helper.HELPER.logDirect(component);
                    this.clickStart = null;
                } else {
                    BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(this.currentMouseOver));
                }
            } else if (mouseButton == 1) {
                BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(this.currentMouseOver.up()));
            }
        }
        this.clickStart = null;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.clickStart = this.currentMouseOver;
    }

    public void onRender() {
        GlStateManager.getFloat(2982, (FloatBuffer)this.MODELVIEW.clear());
        GlStateManager.getFloat(2983, (FloatBuffer)this.PROJECTION.clear());
        GlStateManager.glGetInteger(2978, (IntBuffer)this.VIEWPORT.clear());
        if (this.currentMouseOver != null) {
            Entity e = this.mc.getRenderViewEntity();
            PathRenderer.drawManySelectionBoxes(e, Collections.singletonList(this.currentMouseOver), Color.CYAN);
            if (this.clickStart != null && !this.clickStart.equals(this.currentMouseOver)) {
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.color(Color.RED.getColorComponents(null)[0], Color.RED.getColorComponents(null)[1], Color.RED.getColorComponents(null)[2], 0.4f);
                GlStateManager.glLineWidth(((Float)Baritone.settings().pathRenderLineWidthPixels.value).floatValue());
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();
                BetterBlockPos a = new BetterBlockPos(this.currentMouseOver);
                BetterBlockPos b = new BetterBlockPos(this.clickStart);
                IRenderer.drawAABB(new AxisAlignedBB(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z), Math.max(a.x, b.x) + 1, Math.max(a.y, b.y) + 1, Math.max(a.z, b.z) + 1));
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
            }
        }
    }

    private Vec3d toWorld(double x, double y, double z) {
        boolean result = GLU.gluUnProject((float)x, (float)y, (float)z, this.MODELVIEW, this.PROJECTION, this.VIEWPORT, (FloatBuffer)this.TO_WORLD_BUFFER.clear());
        if (result) {
            return new Vec3d(this.TO_WORLD_BUFFER.get(0), this.TO_WORLD_BUFFER.get(1), this.TO_WORLD_BUFFER.get(2));
        }
        return null;
    }
}

