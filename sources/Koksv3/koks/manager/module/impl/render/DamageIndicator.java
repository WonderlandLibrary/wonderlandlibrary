package koks.manager.module.impl.render;

import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventRender3D;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Kroko, Phantom, Deleteboys, Dirt
 * @created on 07.12.2020 : 08:42
 */

@ModuleInfo(name = "DamageIndicator", description = "Its shows the damage", category = Module.Category.RENDER)
public class DamageIndicator extends Module {

    public HashMap<EntityLivingBase, Float> health = new HashMap<>();
    public CopyOnWriteArrayList<DamageParticle> particleCopy = new CopyOnWriteArrayList<>();
    public ArrayList<DamageParticle> particles = new ArrayList<>();

    public Setting speed = new Setting("Speed", 0.05F, 0.05F, 0.2F, false, this);
    public Setting existTime = new Setting("ExistTime", 20, 10, 100, true, this);

    @Override
    public void onEvent(Event event) {
        if (!isToggled())
            return;

        if (event instanceof EventUpdate) {
            particleCopy.addAll(particles);
            particleCopy.forEach(damageParticle -> {
                ++damageParticle.tick;

                if(damageParticle.tick <= 10) {
                    damageParticle.pos[1] = damageParticle.pos[1] + damageParticle.tick * (speed.getCurrentValue() / 10);
                }
                if(damageParticle.tick > existTime.getCurrentValue()) {
                    particles.remove(damageParticle);
                }
            });
            particleCopy.clear();

            getWorld().loadedEntityList.forEach(entity -> {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase livingBase = (EntityLivingBase) entity;
                    if (!health.containsKey(livingBase))
                        health.put(livingBase, livingBase.getHealth());
                    float lastHealth = health.get(livingBase);
                    float health = livingBase.getHealth();
                    if (lastHealth != health) {
                        float damage = (Math.round((lastHealth - health) * 10));
                        damage /= 10;
                        String color = damage < 0 ? "§a" : !getPlayer().onGround ? "§e" : "§c";
                        String text = color + Math.abs(damage);
                        particles.add(new DamageParticle(new double[]{livingBase.posX, livingBase.posY, livingBase.posZ}, text));
                        this.health.remove(livingBase);
                    }
                }
            });
        }

        if (event instanceof EventRender3D) {
            for (DamageParticle damageParticle : particles) {
                double[] position = damageParticle.pos;
                String text = damageParticle.text;

                float x = (float) (position[0] - getRenderManager().renderPosX);
                float y = (float) (position[1] - getRenderManager().renderPosY);
                float z = (float) (position[2] - getRenderManager().renderPosZ);

                GL11.glPushMatrix();

                GL11.glTranslatef(x, y, z);

                double scale = 0.03;
                GL11.glScaled(-scale, -scale, scale);
                GL11.glRotated(getRenderManager().playerViewY, 0, 1, 0);
                fr.drawStringWithShadow(text, fr.getStringWidth(text) / 2 * -1, fr.FONT_HEIGHT * -1, -1);

                GL11.glPopMatrix();
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    public static class DamageParticle {
        int tick;
        double[] pos;
        String text;

        public DamageParticle(double[] pos, String text) {
            this.tick = 0;
            this.pos = pos;
            this.text = text;
        }
    }
}
