package koks.module.visual;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.utils.Resolution;
import koks.event.Render2DEvent;
import koks.event.Render3DEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "TNTESP", description = "You can see the explosion radius", category = Module.Category.VISUAL)
public class TNTESP extends Module {

    double damage = 0;

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof Render3DEvent) {
            damage = 0;
            for (Entity entity : getWorld().loadedEntityList) {
                if (entity instanceof final EntityTNTPrimed tnt) {
                    final double posX = tnt.posX - mc.getRenderManager().renderPosX;
                    final double posY = tnt.posY - mc.getRenderManager().renderPosY;
                    final double posZ = tnt.posZ - mc.getRenderManager().renderPosZ;

                    GL11.glPushMatrix();
                    GL11.glTranslated(posX, posY, posZ);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glColor4d(1, 0, 0, 0.2);

                    Sphere sphere = new Sphere();
                    sphere.setDrawStyle(GLU.GLU_FILL);
                    sphere.draw(4 * 2, 15, 15);
                    float f3 = 4 * 2.0F;
                    Vec3 vec3 = new Vec3(tnt.posX, tnt.posY, tnt.posZ);
                    if (!getPlayer().isImmuneToExplosions()) {
                        double d12 = getPlayer().getDistance(tnt.posX, tnt.posY, tnt.posZ) / (double) f3;
                        if (d12 <= 1.0D) {
                            double d5 = getPlayer().posX - tnt.posX;
                            double d7 = getPlayer().posY + (double) getPlayer().getEyeHeight() - tnt.posY;
                            double d9 = getPlayer().posZ - tnt.posY;
                            double d13 = MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);
                            if (d13 != 0) {
                                double d14 = getWorld().getBlockDensity(vec3, getPlayer().getEntityBoundingBox());
                                double d10 = (1.0D - d12) * d14;
                                damage += (float) ((int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) f3 + 1.0D));
                            }
                        }
                    }

                    GL11.glColor4d(1, 0, 0, 1);

                    Sphere lines = new Sphere();
                    lines.setDrawStyle(GLU.GLU_LINE);
                    lines.draw(4 * 2 + 0.1F, 15, 15);

                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glPopMatrix();
                }
            }
        }
        if (event instanceof Render2DEvent) {
            final Resolution resolution = Resolution.getResolution();
            if (damage != 0) {
                fr.drawString("§c" + damage, resolution.width / 2 + 5, resolution.height / 2 + 5, -1);
            }
        }
    }

    @Override
    public void onEnable() {
        damage = 0;
    }

    @Override
    public void onDisable() {

    }
}
