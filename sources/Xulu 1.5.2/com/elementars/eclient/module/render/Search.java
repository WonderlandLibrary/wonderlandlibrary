// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import com.elementars.eclient.util.MathUtil;
import java.awt.Color;
import com.elementars.eclient.event.EventTarget;
import net.minecraft.util.math.Vec3d;
import com.elementars.eclient.event.events.EventRenderBlock;
import com.elementars.eclient.util.RainbowUtils;
import net.minecraft.world.World;
import com.elementars.eclient.util.XuluTessellator;
import com.elementars.eclient.event.events.RenderEvent;
import net.minecraft.client.renderer.culling.Frustum;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import com.elementars.eclient.module.Category;
import net.minecraft.client.renderer.culling.ICamera;
import dev.xulu.settings.Value;
import com.elementars.eclient.util.Triplet;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import net.minecraft.block.Block;
import java.util.List;
import com.elementars.eclient.module.Module;

public class Search extends Module
{
    private static final List<Block> BLOCKS;
    public final Map<BlockPos, Triplet<Integer, Integer, Integer>> posList;
    private final Value<String> mode;
    private final Value<Float> renderDistance;
    private final Value<Boolean> render;
    private final Value<Boolean> tracers;
    private final Value<Boolean> rainbow;
    private final Value<Integer> red;
    private final Value<Integer> green;
    private final Value<Integer> blue;
    private final Value<Integer> alpha;
    private final Value<Integer> alphaF;
    ICamera camera;
    
    public Search() {
        super("Search", "ESP for a certain block id", 0, Category.RENDER, true);
        this.posList = new HashMap<BlockPos, Triplet<Integer, Integer, Integer>>();
        this.mode = this.register(new Value<String>("Mode", this, "Solid", new ArrayList<String>(Arrays.asList("Solid", "Outline", "Full"))));
        this.renderDistance = this.register(new Value<Float>("RenderDistance", this, 50.0f, 1.0f, 100.0f));
        this.render = this.register(new Value<Boolean>("Render", this, true));
        this.tracers = this.register(new Value<Boolean>("Tracers", this, false));
        this.rainbow = this.register(new Value<Boolean>("Rainbow", this, false));
        this.red = this.register(new Value<Integer>("Default Red", this, 255, 0, 255));
        this.green = this.register(new Value<Integer>("Default Green", this, 130, 0, 255));
        this.blue = this.register(new Value<Integer>("Default Blue", this, 170, 0, 255));
        this.alpha = this.register(new Value<Integer>("Alpha", this, 70, 0, 255));
        this.alphaF = this.register(new Value<Integer>("Full Alpha", this, 100, 0, 255));
        this.camera = (ICamera)new Frustum();
    }
    
    @Override
    public void onEnable() {
        Search.mc.renderGlobal.loadRenderers();
    }
    
    @Override
    public void onDisable() {
        this.posList.clear();
        Search.mc.renderGlobal.loadRenderers();
    }
    
    public static List<Block> getBLOCKS() {
        return Search.BLOCKS;
    }
    
    public static boolean addBlock(final String string) {
        if (Block.getBlockFromName(string) != null) {
            Search.BLOCKS.add(Block.getBlockFromName(string));
            return true;
        }
        return false;
    }
    
    public static boolean delBlock(final String string) {
        if (Block.getBlockFromName(string) != null) {
            Search.BLOCKS.remove(Block.getBlockFromName(string));
            return true;
        }
        return false;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final double d3 = Search.mc.player.lastTickPosX + (Search.mc.player.posX - Search.mc.player.lastTickPosX) * event.getPartialTicks();
        final double d4 = Search.mc.player.lastTickPosY + (Search.mc.player.posY - Search.mc.player.lastTickPosY) * event.getPartialTicks();
        final double d5 = Search.mc.player.lastTickPosZ + (Search.mc.player.posZ - Search.mc.player.lastTickPosZ) * event.getPartialTicks();
        this.camera.setPosition(d3, d4, d5);
        if (Search.mc.player == null) {
            return;
        }
        if (this.render.getValue()) {
            if (this.mode.getValue().equalsIgnoreCase("Solid")) {
                XuluTessellator.prepare(7);
                synchronized (this.posList) {
                    this.posList.forEach((blockPos, triplet) -> {
                        if (blockPos.getDistance((int)Search.mc.player.posX, (int)Search.mc.player.posY, (int)Search.mc.player.posZ) <= this.renderDistance.getValue() && this.camera.isBoundingBoxInFrustum(Search.mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)Search.mc.world, blockPos))) {
                            this.drawBlock(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)triplet.getFirst()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)triplet.getSecond()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)triplet.getThird()));
                        }
                        return;
                    });
                }
                XuluTessellator.release();
            }
            else if (this.mode.getValue().equalsIgnoreCase("Outline")) {
                synchronized (this.posList) {
                    this.posList.forEach((blockPos, triplet) -> {
                        if (blockPos.getDistance((int)Search.mc.player.posX, (int)Search.mc.player.posY, (int)Search.mc.player.posZ) <= this.renderDistance.getValue()) {
                            this.drawBlockO(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)triplet.getFirst()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)triplet.getSecond()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)triplet.getThird()));
                        }
                        return;
                    });
                }
            }
            else if (this.mode.getValue().equalsIgnoreCase("Full")) {
                synchronized (this.posList) {
                    this.posList.forEach((blockPos, triplet) -> {
                        XuluTessellator.prepare(7);
                        if (blockPos.getDistance((int)Search.mc.player.posX, (int)Search.mc.player.posY, (int)Search.mc.player.posZ) <= this.renderDistance.getValue()) {
                            this.drawBlock(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)triplet.getFirst()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)triplet.getSecond()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)triplet.getThird()));
                            this.drawBlockO(blockPos, ((boolean)this.rainbow.getValue()) ? RainbowUtils.r : ((int)triplet.getFirst()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.g : ((int)triplet.getSecond()), ((boolean)this.rainbow.getValue()) ? RainbowUtils.b : ((int)triplet.getThird()));
                        }
                        XuluTessellator.release();
                        return;
                    });
                }
            }
        }
        if (this.tracers.getValue()) {
            synchronized (this.posList) {
                this.posList.forEach((blockPos, triplet) -> {
                    if (blockPos.getDistance((int)Search.mc.player.posX, (int)Search.mc.player.posY, (int)Search.mc.player.posZ) <= this.renderDistance.getValue()) {
                        drawLineToBlock(blockPos, ((boolean)this.rainbow.getValue()) ? ((float)RainbowUtils.r) : ((float)triplet.getFirst()), ((boolean)this.rainbow.getValue()) ? ((float)RainbowUtils.g) : ((float)(int)triplet.getSecond()), ((boolean)this.rainbow.getValue()) ? ((float)RainbowUtils.b) : ((float)(int)triplet.getThird()), this.alpha.getValue());
                    }
                });
            }
        }
    }
    
    @EventTarget
    public void onRender(final EventRenderBlock event) {
        if (Search.BLOCKS.contains(event.getBlockState().getBlock())) {
            final Vec3d vec3d = event.getBlockState().getOffset(event.getBlockAccess(), event.getBlockPos());
            final double d0 = event.getBlockPos().getX() + vec3d.x;
            final double d2 = event.getBlockPos().getY() + vec3d.y;
            final double d3 = event.getBlockPos().getZ() + vec3d.z;
            final BlockPos pos = new BlockPos(d0, d2, d3);
            synchronized (this.posList) {
                this.posList.put(pos, this.getColor(event.getBlockState().getBlock()));
            }
        }
    }
    
    private void drawBlock(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.alpha.getValue());
        final IBlockState iBLOCKState3 = Search.mc.world.getBlockState(blockPos);
        final Vec3d interp3 = MathUtil.interpolateEntity((Entity)Search.mc.player, Search.mc.getRenderPartialTicks());
        XuluTessellator.drawBox2(iBLOCKState3.getSelectedBoundingBox((World)Search.mc.world, blockPos).offset(-interp3.x, -interp3.y, -interp3.z), color.getRGB(), 63);
    }
    
    private void drawBlockO(final BlockPos blockPos, final int r, final int g, final int b) {
        final IBlockState iBLOCKState2 = Search.mc.world.getBlockState(blockPos);
        final Vec3d interp2 = MathUtil.interpolateEntity((Entity)Search.mc.player, Search.mc.getRenderPartialTicks());
        XuluTessellator.drawBoundingBox(iBLOCKState2.getSelectedBoundingBox((World)Search.mc.world, blockPos).offset(-interp2.x, -interp2.y, -interp2.z), 1.5f, r, g, b, this.alphaF.getValue());
    }
    
    private Triplet<Integer, Integer, Integer> getColor(final Block block) {
        if (block == Blocks.DIAMOND_ORE) {
            return new Triplet<Integer, Integer, Integer>(0, 255, 255);
        }
        if (block == Blocks.IRON_ORE) {
            return new Triplet<Integer, Integer, Integer>(255, 226, 191);
        }
        if (block == Blocks.GOLD_ORE) {
            return new Triplet<Integer, Integer, Integer>(255, 216, 0);
        }
        if (block == Blocks.COAL_ORE) {
            return new Triplet<Integer, Integer, Integer>(35, 35, 35);
        }
        if (block == Blocks.LAPIS_ORE) {
            return new Triplet<Integer, Integer, Integer>(0, 50, 255);
        }
        if (block == Blocks.PORTAL) {
            return new Triplet<Integer, Integer, Integer>(170, 0, 255);
        }
        if (block == Blocks.EMERALD_ORE) {
            return new Triplet<Integer, Integer, Integer>(0, 255, 0);
        }
        if (block == Blocks.REDSTONE_ORE) {
            return new Triplet<Integer, Integer, Integer>(186, 0, 0);
        }
        if (block == Blocks.END_PORTAL_FRAME) {
            return new Triplet<Integer, Integer, Integer>(255, 255, 150);
        }
        return new Triplet<Integer, Integer, Integer>(this.red.getValue(), this.green.getValue(), this.blue.getValue());
    }
    
    public static void drawLineToBlock(final BlockPos pos, final float red, final float green, final float blue, final float opacity) {
        final Vec3d interp3 = MathUtil.interpolateEntity((Entity)Search.mc.player, Search.mc.getRenderPartialTicks());
        drawLine(pos.x - interp3.x + 0.5, pos.y - interp3.y + 0.5, pos.z - interp3.z + 0.5, 0.0, red, green, blue, opacity);
    }
    
    public static void drawLine(final double posx, final double posy, final double posz, final double up, final float red, final float green, final float blue, final float opacity) {
        final Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-(float)Math.toRadians(Minecraft.getMinecraft().player.rotationPitch)).rotateYaw(-(float)Math.toRadians(Minecraft.getMinecraft().player.rotationYaw));
        drawLineFromPosToPos(eyes.x, eyes.y + Search.mc.player.getEyeHeight(), eyes.z, posx, posy, posz, up, red, green, blue, opacity);
    }
    
    public static void drawLineFromPosToPos(final double posx, final double posy, final double posz, final double posx2, final double posy2, final double posz2, final double up, final float red, final float green, final float blue, final float opacity) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.5f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, opacity);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        final boolean bobbing = Search.mc.gameSettings.viewBobbing;
        Search.mc.gameSettings.viewBobbing = false;
        Search.mc.entityRenderer.orientCamera(Search.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(posx, posy, posz);
        GL11.glVertex3d(posx2, posy2, posz2);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glColor3d(1.0, 1.0, 1.0);
        GlStateManager.enableLighting();
        Search.mc.gameSettings.viewBobbing = bobbing;
    }
    
    static {
        BLOCKS = new ArrayList<Block>(Arrays.asList((Block)Blocks.PORTAL, Blocks.DIAMOND_ORE));
    }
}
