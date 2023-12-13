package wtf.diablo.module.impl.Render;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import wtf.diablo.events.impl.Render3DEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.render.ColorUtil;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;


@Getter
@Setter
public class ESP extends Module {
    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    private static final Frustum frustrum = new Frustum();

    public BooleanSetting box = new BooleanSetting("Box", true);
    public BooleanSetting healthBar = new BooleanSetting("Health Bar", true);

    public static NumberSetting red = new NumberSetting("Red", 250, 1, 0, 255);
    public static NumberSetting green = new NumberSetting("Green", 250, 1, 0, 255);
    public static NumberSetting blue = new NumberSetting("Blue", 250, 1, 0, 255);

    public static BooleanSetting rainbow = new BooleanSetting("Sync", true);

    public ESP() {
        super("ESP", "Sexz", Category.RENDER, ServerType.All);
        this.addSettings(box, healthBar, red, green, blue, rainbow);
    }

    @Subscribe
    public void onRender(Render3DEvent e) {
        Color color = new Color(255, 255, 255);

        for (Object object : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            EntityLivingBase ent;
            if (!(entity instanceof EntityLivingBase) || entity.isInvisible() || (ent = (EntityLivingBase) entity) == mc.thePlayer || !isInViewFrustrum(ent))
                continue;
            double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) mc.timer.renderPartialTicks;
            double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) mc.timer.renderPartialTicks;
            double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) mc.timer.renderPartialTicks;
            double finalWidth = (double) entity.width / 1.4;
            double finalHeight = (double) entity.height + (entity.isSneaking() ? -0.3 : 0.2);
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(posX - finalWidth - 0.1, posY - 0.1, posZ - finalWidth - 0.1, posX + finalWidth + 0.1, posY + finalHeight + 0.1, posZ + finalWidth + 0.1);
            List<Vector3d> vectorList = Arrays.asList(new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));
            mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
            Vector4d posVec = null;


            for (Vector3d vector : vectorList) {
                FloatBuffer otherVec = GLAllocation.createDirectFloatBuffer(4);
                GL11.glGetFloat(2982, modelview);
                GL11.glGetFloat(2983, projection);
                GL11.glGetInteger(2978, viewport);
                if (GLU.gluProject((float) (vector.x - mc.getRenderManager().viewerPosX), (float) ((double) ((float) vector.y) - mc.getRenderManager().viewerPosY), (float) ((double) ((float) vector.z) - mc.getRenderManager().viewerPosZ), modelview, projection, viewport, otherVec)) {
                    vector = new Vector3d(otherVec.get(0) / (float) new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor(), ((float) Display.getHeight() - otherVec.get(1)) / (float) new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor(), otherVec.get(2));
                }
                if (!(vector.z >= 0.0) || !(vector.z < 1.0)) continue;
                if (posVec == null) {
                    posVec = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                }
                posVec.x = Math.min(vector.x, posVec.x);
                posVec.y = Math.min(vector.y, posVec.y);
                posVec.z = Math.max(vector.x, posVec.z);
                posVec.w = Math.max(vector.y, posVec.w);
            }
            mc.entityRenderer.setupOverlayRendering();
            if (posVec == null) continue;
            GL11.glPushMatrix();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.enableBlend();
            float x = (float) posVec.x;
            float w = (float) posVec.z - x;
            float y = (float) posVec.y;
            float h = (float) posVec.w - y;
            int healthBarColor = getHealthColor(ent);
            if (ent instanceof EntityPlayer) {
                if (healthBar.getValue()) {
                    drawBar(x - 2.0f, y, 1.5f, h, (float) ((int) ent.getMaxHealth()) / 2.0f, (float) ((int) ent.getMaxHealth()) / 2.0f, 0, new Color(-1459617792, true).getRGB());
                    drawBar(x - 3.0f, y, 1.5f, h, (float) ((int) ent.getMaxHealth()) / 2.0f, (float) ((int) ent.getHealth()) / 2.0f, healthBarColor, new Color(-1459617792, true).getRGB());
                }
                if (box.getValue()) {
                    drawBorderedRect(x, y, w, h, 1, new Color(0, 0, 0).getRGB(), 0);
                    drawBorderedRect(x, y, w, h, 0.5, getColor(), 0);
                }
            }
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.disablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.popMatrix();
        }

    }

    public boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static int getColor() {
        return rainbow.getValue() ? ColorUtil.getColor(0) : new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue()).getRGB();
    }

    public void drawBorderedRect(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
        Gui.drawRect(x, y, x + width, y + lineSize, borderColor);
        Gui.drawRect(x, y, x + lineSize, y + height, borderColor);
        Gui.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        Gui.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }

    public void drawBar(float x, float y, float width, float height, float max, float value, int color, int color2) {
        float f = (float) (color >> 24 & 0xFF) / 255.0f;
        float f1 = (float) (color >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (color & 0xFF) / 255.0f;
        float inc = height / max;
        GL11.glColor4f(f1, f2, f3, f);
        float incY = y + height - inc;
        int i = 0;
        while ((float) i < value) {
            drawBorderedRect(x + 0.25f, incY, width - 0.5f, inc, 0.25, new Color(color2, true).getRGB(), color);
            incY -= inc;
            ++i;
        }
    }

    private int getHealthColor(EntityLivingBase player) {
        return Color.HSBtoRGB(Math.max(0.0f, Math.min(player.getHealth(), player.getMaxHealth()) / player.getMaxHealth()) / 3.0f, 0.56f, 1.0f) | 0xFF000000;
    }
}