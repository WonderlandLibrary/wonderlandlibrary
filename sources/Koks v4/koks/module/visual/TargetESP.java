package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.TimeHelper;
import koks.event.Render3DEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author kroko
 * @created on 02.02.2021 : 01:31
 */

@Module.Info(name = "TargetESP", category = Module.Category.VISUAL, description = "Its mark your Target")
public class TargetESP extends Module {

    private final TimeHelper animationTimer = new TimeHelper();
    int animationX;

    @Value(name = "Mode", modes = {"Icarus", "Sigma"})
    String mode = "Icarus";

    @Value(name = "Icarus Fade Speed", displayName = "Fade Speed", minimum = 1, maximum = 10000)
    int fadeSpeed = 1000;

    @Value(name = "Icarus Fade Saturation", displayName = "Fade Saturation", minimum = 0.1, maximum = 1)
    double fadeSaturation = 1;

    @Value(name = "Icarus Speed", displayName = "Speed", minimum = 0, maximum = 100)
    int speed = 20;

    @Value(name = "Icarus Width", displayName = "Width", minimum = 0.1, maximum = 5)
    double width = 1;

    @Value(name = "Sigma Color", displayName = "Color", colorPicker = true)
    int sigmaColor = Color.white.getRGB();

    @Value(name = "Sigma Rainbow", displayName = "Rainbow")
    boolean sigmaRainbow = false;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        switch (name) {
            case "Icarus Fade Speed", "Icarus Fade Saturation", "Icarus Speed", "Icarus Width" -> {
                return mode.equalsIgnoreCase("Icarus");
            }
            case "Sigma Color" -> {
                return mode.equalsIgnoreCase("Sigma") && !sigmaRainbow;
            }
            case "Sigma Rainbow" -> {
                return mode.equalsIgnoreCase("Sigma");
            }
        }
        return super.isVisible(value, name);
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof Render3DEvent) {
            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit != null) {
                final Entity curEntity = mc.objectMouseOver.entityHit;
                if (curEntity instanceof EntityLivingBase) {
                    final double xPos = curEntity.lastTickPosX + (curEntity.posX - curEntity.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX;
                    final double yPos = curEntity.lastTickPosY + (curEntity.posY - curEntity.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY;
                    final double zPos = curEntity.lastTickPosZ + (curEntity.posZ - curEntity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ;
                    switch (mode) {
                        case "Icarus" -> {
                            GL11.glPushMatrix();
                            GL11.glDisable(GL11.GL_TEXTURE_2D);
                            GL11.glDisable(GL11.GL_DEPTH_TEST);
                            GL11.glEnable(GL11.GL_LINE_SMOOTH);
                            int currentOffset = 0;
                            float animationY = 0.0f;
                            float animationY2 = 0.0f;
                            if (animationTimer.hasReached(speed)) {
                                animationX++;
                                animationTimer.reset();
                            }
                            if (!curEntity.isInvisible()) {
                                translateRotate(curEntity);
                                GL11.glLineWidth((float) width);
                                GL11.glBegin(GL11.GL_LINE_STRIP);

                                for (int i = animationX; i < 100 + animationX; i++) {
                                    final double c = (2 * i * Math.PI / 100);
                                    Color color = getRainbow(currentOffset, fadeSpeed, (float) fadeSaturation, 1f);
                                    GL11.glColor3d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                                    GL11.glVertex3d((Math.cos(c) * 0.5), animationY, (Math.sin(c) * 0.5));
                                    animationY += curEntity.height / 100;
                                    currentOffset += 10;
                                }
                                GL11.glEnd();

                                GL11.glBegin(GL11.GL_LINE_STRIP);

                                for (int i = 50 + animationX; i < 150 + animationX; i++) {
                                    final double c = (2 * i * Math.PI / 100);
                                    Color color = getRainbow(currentOffset, fadeSpeed, (float) fadeSaturation, 1f);
                                    GL11.glColor3d(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
                                    GL11.glVertex3d((Math.cos(c) * 0.5), animationY2, (Math.sin(c) * 0.5));
                                    animationY2 += curEntity.height / 100;
                                    currentOffset += 10;
                                }
                                GL11.glEnd();
                            }
                            GL11.glDisable(GL11.GL_LINE_SMOOTH);
                            GL11.glEnable(GL11.GL_DEPTH_TEST);
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            GL11.glPopMatrix();
                        }
                        case "Sigma" -> {
                            GL11.glPushMatrix();
                            GL11.glEnable(GL11.GL_BLEND);
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            GL11.glEnable(GL11.GL_LINE_SMOOTH);
                            GL11.glDisable(GL11.GL_DEPTH_TEST);
                            GL11.glDisable(GL11.GL_TEXTURE_2D);
                            GL11.glEnable(GL11.GL_ALPHA_TEST);
                            GL11.glDisable(GL11.GL_LIGHTING);
                            GL11.glLineWidth(3);
                            GL11.glShadeModel(GL11.GL_SMOOTH);
                            GL11.glDisable(GL11.GL_CULL_FACE);
                            final double size = curEntity.width * 1.2;
                            float factor = (float) Math.sin(System.nanoTime() / 800000000f);
                            GL11.glTranslated(0, factor, 0);
                            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                            Color color = new Color(this.sigmaColor);
                            animationX++;
                            for (int j = 0; j < 361; j++) {
                                if(sigmaRainbow)
                                    color = getRainbow(animationX, 30000, 0.8F, 1F);
                                GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 2F);
                                final double x = xPos + Math.cos(Math.toRadians(j)) * size;
                                final double z = zPos - Math.sin(Math.toRadians(j)) * size;
                                GL11.glVertex3d(x, yPos + 1, z);
                                GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0);
                                GL11.glVertex3d(x, yPos + 1 + factor * 0.4F, z);
                            }
                            GL11.glEnd();
                            GL11.glBegin(GL11.GL_LINE_LOOP);
                            for (int j = 0; j < 361; j++) {
                                final double x = xPos + Math.cos(Math.toRadians(j)) * size;
                                final double z = zPos - Math.sin(Math.toRadians(j)) * size;
                                GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1F);
                                GL11.glVertex3d(x, yPos + 1, z);
                            }
                            GL11.glEnd();
                            GL11.glEnable(GL11.GL_ALPHA_TEST);
                            GL11.glShadeModel(GL11.GL_FLAT);
                            GL11.glDisable(GL11.GL_LINE_SMOOTH);
                            GL11.glEnable(GL11.GL_CULL_FACE);
                            //GL11.glEnable(GL11.GL_LIGHTING);
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            GL11.glEnable(GL11.GL_DEPTH_TEST);
                            GL11.glDisable(GL11.GL_BLEND);
                            GlStateManager.resetColor();
                            GL11.glPopMatrix();
                        }
                    }

                }
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    private void translateRotate(Entity entity) {
        final double x = ((entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosX);
        final double y = ((entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosY);
        final double z = ((entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks) - mc.getRenderManager().renderPosZ);
        GL11.glTranslated(x, y, z);
        GL11.glNormal3d(0.0, 1.0, 0.0);
        GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0, 1.0, 0.0);
    }
}
