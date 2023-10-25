package fun.expensive.client.feature.impl.visual;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.render.EventRender2D;
import fun.rich.client.event.events.impl.render.EventRender3D;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ColorSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.math.RotationHelper;
import fun.rich.client.utils.render.ClientHelper;
import fun.rich.client.utils.render.ColorUtils;
import fun.rich.client.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PearlESP extends Feature {
    public ColorSetting globalColor = new ColorSetting("Global Color", Color.PINK.getRGB(), () -> true);
    public BooleanSetting tracers = new BooleanSetting("Tracers", true, () -> true);
    public BooleanSetting esp = new BooleanSetting("ESP", true, () -> true);
    public BooleanSetting triangleESP = new BooleanSetting("TriangleESP", true, () -> true);
    private final ListSetting triangleMode = new ListSetting("Triangle Mode", "Custom", () -> triangleESP.getBoolValue(), "Astolfo", "Rainbow", "Client", "Custom");
    private final ColorSetting triangleColor;


    public PearlESP() {
        super("PearlESP", "Показывает есп перла", FeatureCategory.Visuals);
        triangleColor = new ColorSetting("Triangle Color", Color.PINK.getRGB(), () -> triangleESP.getBoolValue() && triangleMode.currentMode.equals("Custom"));

        addSettings(globalColor, triangleESP, triangleMode, triangleColor, esp, tracers);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        GlStateManager.pushMatrix();
        java.util.List<EntityEnderPearl> check = mc.world.loadedEntityList.stream().filter(x -> x instanceof EntityEnderPearl)
                .map(x -> (EntityEnderPearl) x).collect(Collectors.toList());
        check.forEach(entity -> {
            boolean viewBobbing = mc.gameSettings.viewBobbing;
            mc.gameSettings.viewBobbing = false;
            mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            mc.gameSettings.viewBobbing = viewBobbing;

            if (tracers.getBoolValue()) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glLineWidth(1);
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().renderPosY - 1;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - mc.getRenderManager().renderPosZ;
                RenderUtils.setColor(globalColor.getColorValue());
                Vec3d vec = new Vec3d(0, 0, 1).rotatePitch((float) -(Math.toRadians(mc.player.rotationPitch))).rotateYaw((float) -Math.toRadians(mc.player.rotationYaw));
                GL11.glBegin(2);
                GL11.glVertex3d(vec.xCoord, mc.player.getEyeHeight() + vec.yCoord, vec.zCoord);
                GL11.glVertex3d(x, y + 1.10, z);
                GL11.glEnd();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glPopMatrix();
            }

            if (esp.getBoolValue()) {
                RenderUtils.drawEntityBox(entity, new Color(globalColor.getColorValue()), true, 0.20F);
            }
        });
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (!this.triangleESP.getBoolValue()) {
            return;
        }
        ScaledResolution sr = new ScaledResolution(mc);
        float size = 50.0f;
        float xOffset = (float) sr.getScaledWidth() / 2.0f - 24.5f;
        float yOffset = (float) sr.getScaledHeight() / 2.0f - 25.2f;
        for (Entity entity : PearlESP.mc.world.loadedEntityList) {
            if (entity == null || !(entity instanceof EntityEnderPearl)) continue;
            int color = 0;
            switch (triangleMode.currentMode) {
                case "Client":
                    color = ClientHelper.getClientColor().getRGB();
                    break;
                case "Custom":
                    color = triangleColor.getColorValue();
                    break;
                case "Astolfo":
                    color = ColorUtils.astolfo(false, 1).getRGB();
                    break;
                case "Rainbow":
                    color = ColorUtils.rainbow(300, 1, 1).getRGB();
                    break;
            }
            GL11.glPushMatrix();
            int x = event.getResolution().getScaledWidth() / 2;
            int y = event.getResolution().getScaledHeight() / 2;
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(RotationHelper.getAngle(entity) % 360.0F + 180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            RenderUtils.drawBlurredShadow((float) x - 3, (float) (y + 48), 5.0F, 10.0F, 15, new Color(color));
            RenderUtils.drawTriangle((float) x - 5, (float) (y + 50), 5.0F, 10.0F, new Color(color).darker().getRGB(), color);
            GL11.glTranslatef((float) x, (float) y, 0.0F);
            GL11.glRotatef(-(RotationHelper.getAngle(entity) % 360.0F + 180.0F), 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef((float) (-x), (float) (-y), 0.0F);
            GL11.glPopMatrix();
        }
    }
}

