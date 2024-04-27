package me.AquaVit.liquidSense.modules.render;

import com.google.common.collect.Lists;
import me.AquaVit.liquidSense.API.XrayAPI;
import net.ccbluex.liquidbounce.event.BlockRenderSideEvent;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ModuleInfo(name = "UHCXray", description = "UHCXray", category = ModuleCategory.RENDER)
public class UHCXray extends Module {
    public static final HashSet<Integer> blockIDs = new HashSet();

    private int opacity = 160;

    private List<Integer> KEY_IDS = Lists.newArrayList(10, 11, 8, 9, 14, 15, 16, 21, 41, 42, 46, 48, 52, 56, 57, 61, 62,
            73, 74, 84, 89, 103, 116, 117, 118, 120, 129, 133, 137, 145, 152, 153, 154);

    public CopyOnWriteArrayList<XrayAPI> list = new CopyOnWriteArrayList<XrayAPI>();

    public FloatValue KEY_OPACITY = new FloatValue("Opacity", 160.0f, 0.0f, 255.0f);
    public  FloatValue Dis = new FloatValue("Distance", 50.0f, 0.0f, 50.0f);

    public BoolValue CAVEFINDER = new BoolValue("CaveFinder", true);
    public BoolValue ESP = new BoolValue("ESP", true);
    public BoolValue Tracer = new BoolValue("Tracer", false);

    public BoolValue CoalOre = new BoolValue("Coal", false);
    public BoolValue RedStoneOre = new BoolValue("RedStone", false);
    public BoolValue IronOre = new BoolValue("Iron", false);
    public BoolValue GoldOre = new BoolValue("Gold", true);
    public BoolValue DiamondOre = new BoolValue("Diamond", true);
    public BoolValue EmeraldOre = new BoolValue("Emerald", false);
    public BoolValue LapisOre = new BoolValue("Lapis", false);

    @Override
    public void onEnable() {
        blockIDs.clear();
        this.opacity = KEY_OPACITY.get().intValue();
        for (int i : KEY_IDS) {
            blockIDs.add(i);
        }
        list.clear();
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
        list.clear();
    }

    @EventTarget
    public void onRender(Render3DEvent event) {
        GlStateManager.pushMatrix();
        pre();
        for (XrayAPI x1 : list) {
            double[] arrd = new double[3];
            double posX = x1.x - mc.getRenderManager().renderPosX;
            double posY = x1.y - mc.getRenderManager().renderPosY;
            double posZ = x1.z - mc.getRenderManager().renderPosZ;
            mc.thePlayer.cameraYaw = 0f;
            this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 2);
            if (x1.type.contains("Diamond")) {
                if (DiamondOre.get()) {
                    if (Tracer.get()) {
                        arrd[0] = 0.0;
                        arrd[1] = 128.0;
                        arrd[2] = 255.0;
                        this.drawLine(arrd, posX, posY, posZ);
                    }
                    drawblock(x1.x - mc.getRenderManager().viewerPosX,
                            x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
                            getColor(30, 144, 255), new Color(30, 144, 255).getRGB(), 2f);
                }
            } else if (x1.type.contains("Iron")) {
                if (IronOre.get()) {
                    if (Tracer.get()) {
                        arrd[0] = 210.0;
                        arrd[1] = 170.0;
                        arrd[2] = 140.0;
                        this.drawLine(arrd, posX, posY, posZ);
                    }
                    drawblock(x1.x - mc.getRenderManager().viewerPosX,
                            x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
                            getColor(184, 134, 11), new Color(184, 134, 11).getRGB(), 2f);
                }
            } else if (x1.type.contains("Gold")) {
                if (GoldOre.get()) {

                    if (Tracer.get()) {
                        arrd[0] = 255.0;
                        arrd[1] = 255.0;
                        arrd[2] = 0.0;
                        this.drawLine(arrd, posX, posY, posZ);
                    }
                    drawblock(x1.x - mc.getRenderManager().viewerPosX,
                            x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
                            getColor(255, 255, 0), new Color(255, 255, 0).getRGB(), 2f);
                }
            } else if (x1.type.contains("Red")) {
                if (RedStoneOre.get()) {

                    if (Tracer.get()) {
                        arrd[0] = 255.0;
                        arrd[1] = 0.0;
                        arrd[2] = 0.0;
                        this.drawLine(arrd, posX, posY, posZ);
                    }
                    drawblock(x1.x - mc.getRenderManager().viewerPosX,
                            x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
                            getColor(255, 0, 0), new Color(255, 0, 0).getRGB(), 2f);
                }
            } else if (x1.type.contains("Lapis")) {
                if (LapisOre.get()) {

                    if (Tracer.get()) {
                        arrd[0] = 27.0;
                        arrd[1] = 74.0;
                        arrd[2] = 161.0;
                        this.drawLine(arrd, posX, posY, posZ);
                    }
                    drawblock(x1.x - mc.getRenderManager().viewerPosX,
                            x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
                            getColor(255, 0, 0), Colors.BLUE.c, 2f);
                }
            } else if (x1.type.contains("Emerald")) {
                if (EmeraldOre.get()) {

                    if (Tracer.get()) {
                        arrd[0] = 23.0;
                        arrd[1] = 221.0;
                        arrd[2] = 98.0;
                        this.drawLine(arrd, posX, posY, posZ);
                    }
                    drawblock(x1.x - mc.getRenderManager().viewerPosX,
                            x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
                            getColor(255, 0, 0), Colors.GREEN.c, 2f);
                }
            } else if (x1.type.contains("Coal")) {
                if (CoalOre.get()) {

                    if (Tracer.get()) {
                        arrd[0] = 0.0;
                        arrd[1] = 0.0;
                        arrd[2] = 0.0;
                        this.drawLine(arrd, posX, posY, posZ);
                    }
                    drawblock(x1.x - mc.getRenderManager().viewerPosX,
                            x1.y - mc.getRenderManager().viewerPosY, x1.z - mc.getRenderManager().viewerPosZ,
                            getColor(0, 0, 0), new Color(0, 0, 0).getRGB(), 2f);
                }
            }
        }
        post();
        GlStateManager.popMatrix();
    }
    public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
        float a7 = (float) (a4 >> 24 & 255) / 255.0f;
        float a8 = (float) (a4 >> 16 & 255) / 255.0f;
        float a9 = (float) (a4 >> 8 & 255) / 255.0f;
        float a10 = (float) (a4 & 255) / 255.0f;
        float a11 = (float) (a5 >> 24 & 255) / 255.0f;
        float a12 = (float) (a5 >> 16 & 255) / 255.0f;
        float a13 = (float) (a5 >> 8 & 255) / 255.0f;
        float a14 = (float) (a5 & 255) / 255.0f;
        org.lwjgl.opengl.GL11.glPushMatrix();
        org.lwjgl.opengl.GL11.glEnable((int) 3042);
        org.lwjgl.opengl.GL11.glBlendFunc((int) 770, (int) 771);
        org.lwjgl.opengl.GL11.glDisable((int) 3553);
        org.lwjgl.opengl.GL11.glEnable((int) 2848);
        org.lwjgl.opengl.GL11.glDisable((int) 2929);
        org.lwjgl.opengl.GL11.glDepthMask((boolean) false);
        org.lwjgl.opengl.GL11.glColor4f((float) a8, (float) a9, (float) a10, (float) a7);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        org.lwjgl.opengl.GL11.glLineWidth((float) a6);
        org.lwjgl.opengl.GL11.glColor4f((float) a12, (float) a13, (float) a14, (float) a11);
        drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
        org.lwjgl.opengl.GL11.glDisable((int) 2848);
        org.lwjgl.opengl.GL11.glEnable((int) 3553);
        org.lwjgl.opengl.GL11.glEnable((int) 2929);
        org.lwjgl.opengl.GL11.glDepthMask((boolean) true);
        org.lwjgl.opengl.GL11.glDisable((int) 3042);
        org.lwjgl.opengl.GL11.glPopMatrix();
    }
    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
    public static void pre() {
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public static void post() {
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glColor3d(1.0, 1.0, 1.0);
    }


    @EventTarget
    public void onEvent(BlockRenderSideEvent e) {
        if (!ESP.get())
            return;
        if (!mc.theWorld.getBlockState(e.getPos()).getBlock().isFullBlock() || !(mc.theWorld.getBlockState(e.getPos()).getBlock() instanceof BlockOre)
                && !(mc.theWorld.getBlockState(e.getPos()).getBlock() instanceof BlockRedstoneOre))
            return;
        float xDiff = (float) (mc.thePlayer.posX - e.getPos().getX());
        float yDiff = 0.0f;
        float zDiff = (float) (mc.thePlayer.posZ - e.getPos().getZ());
        float dis = MathHelper.sqrt_float((xDiff * xDiff + 0.0f + zDiff * zDiff));
        if (dis > Dis.get().floatValue()) {
            return;
        }
        XrayAPI x = new XrayAPI(Math.round(e.getPos().getZ()), Math.round(e.getPos().getY()),
                Math.round(e.getPos().getX()), mc.theWorld.getBlockState(e.getPos()).getBlock().getLocalizedName());
        if (!this.list.contains(x)) {
            this.list.add(x);
        }
    }

    public static int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }

    private void drawLine(double[] color, double x, double y, double z) {
        GL11.glEnable(2848);
        GL11.glColor3d(color[0], color[1], color[2]);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(x + 0.5, y + 0.5, z + 0.5);
        GL11.glEnd();
        GL11.glDisable(2848);
    }

    public boolean containsID(int id) {
        return blockIDs.contains(id);
    }

    public int getOpacity() {
        return 20;
    }
}
