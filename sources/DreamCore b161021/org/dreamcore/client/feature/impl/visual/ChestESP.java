package org.dreamcore.client.feature.impl.visual;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.render.EventRender2D;
import org.dreamcore.client.event.events.impl.render.EventRender3D;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.palette.PaletteHelper;
import org.dreamcore.client.helpers.render.RenderHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ColorSetting;
import org.dreamcore.client.settings.impl.ListSetting;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ChestESP extends Feature {

    public static ListSetting chestEspMode = new ListSetting("ChestEsp Mode", "2D", () -> true, "2D", "3D", "Chams");
    public static ColorSetting chamsColor = new ColorSetting("Chams Color", Color.PINK.getRGB(), () -> chestEspMode.currentMode.equals("Chams"));
    private final int black = Color.BLACK.getRGB();
    private final BooleanSetting fullBox;
    private final ColorSetting colorEsp;
    public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom");

    public ChestESP() {
        super("ChestESP", "Показывает сундуки", Type.Visuals);
        colorEsp = new ColorSetting("ChestEsp Color", new Color(0xFFFFFF).getRGB(), () -> colorMode.currentMode.equals("Custom") && !chestEspMode.currentMode.equals("Chams"));
        fullBox = new BooleanSetting("Full Box", false, () -> chestEspMode.currentMode.equals("3D"));
        addSettings(chestEspMode, colorMode, colorEsp, chamsColor, fullBox);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        for (TileEntity entity : mc.world.loadedTileEntityList) {
            if (entity instanceof TileEntityChest) {
                BlockPos pos = entity.getPos();
                int color = 0;
                switch (colorMode.currentMode) {
                    case "Client":
                        color = ClientHelper.getClientColor().getRGB();
                        break;
                    case "Custom":
                        color = colorEsp.getColorValue();
                        break;
                    case "Astolfo":
                        color = PaletteHelper.astolfo(false, entity.getPos().getY()).getRGB();
                        break;
                    case "Rainbow":
                        color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                        break;
                }
                if (chestEspMode.currentMode.equals("3D")) {
                    GlStateManager.pushMatrix();
                    RenderHelper.blockEsp(pos, new Color(color), fullBox.getBoolValue());
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        String mode = chestEspMode.getOptions();
        setSuffix(mode);
        float partialTicks = mc.timer.renderPartialTicks;
        int scaleFactor = event.getResolution().getScaleFactor();
        double scaling = scaleFactor / Math.pow(scaleFactor, 2);
        GlStateManager.scale(scaling, scaling, scaling);
        Color customColor = new Color(colorEsp.getColorValue());
        Color c = new Color(customColor.getRed(), customColor.getGreen(), customColor.getBlue());
        int color = 0;
        switch (colorMode.currentMode) {
            case "Client":
                color = ClientHelper.getClientColor().getRGB();
                break;
            case "Custom":
                color = c.getRGB();
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(false, 1).getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
        }

        for (TileEntity entity : mc.world.loadedTileEntityList) {
            if (isValid(entity)) {
                BlockPos pos = entity.getPos();
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos);
                Vector3d[] vectors = new Vector3d[]{new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ)};
                mc.entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = project2D(scaleFactor, vector.x - mc.getRenderManager().renderPosX, vector.y - mc.getRenderManager().renderPosY, vector.z - mc.getRenderManager().renderPosZ);
                    if (vector != null && vector.z > 0 && vector.z < 1) {
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0);
                        }

                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position != null) {
                    mc.entityRenderer.setupOverlayRendering();
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;

                    if (mode.equalsIgnoreCase("2D")) {
                        RectHelper.drawRect(posX - 1F, posY, posX + 0.5, endPosY + 0.5, black);
                        RectHelper.drawRect(posX - 1F, posY - 0.5, endPosX + 0.5, posY + 0.5 + 0.5, black);
                        RectHelper.drawRect(endPosX - 0.5 - 0.5, posY, endPosX + 0.5, endPosY + 0.5, black);
                        RectHelper.drawRect(posX - 1, endPosY - 0.5 - 0.5, endPosX + 0.5, endPosY + 0.5, black);
                        RectHelper.drawRect(posX - 0.5, posY, posX + 0.5 - 0.5, endPosY, color);
                        RectHelper.drawRect(posX, endPosY - 0.5, endPosX, endPosY, color);
                        RectHelper.drawRect(posX - 0.5, posY, endPosX, posY + 0.5, color);
                        RectHelper.drawRect(endPosX - 0.5, posY, endPosX, endPosY, color);
                    }
                }
            }
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        mc.entityRenderer.setupOverlayRendering();
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, modelview, projection, viewport, vector)) {
            return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2));
        }
        return null;
    }

    private boolean isValid(TileEntity entity) {
        return entity instanceof TileEntityChest;
    }

}
