package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.EventRender;
import ru.smertnix.celestial.event.events.impl.render.EventRender2D;
import ru.smertnix.celestial.event.events.impl.render.EventRender3D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.MultipleBoolSetting;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class Prediction extends Feature {
	public double x,y,z;
	public Entity e;
    public static ColorSetting predict;
    public static MultipleBoolSetting sel = new MultipleBoolSetting("Object selection",
    		new BooleanSetting("Pearls", true),
    		new BooleanSetting("Arrows", true),
    		new BooleanSetting("Snowballs"),
    		new BooleanSetting("Eggs"),
    		new BooleanSetting("Potions"),
    		new BooleanSetting("Rod"));


    public Prediction() {
        super("Prediction", "Рисует предикт от предметов", FeatureCategory.Render);
        predict = new ColorSetting("Color", new Color(0xFFFFFF).getRGB(), () -> true);
        addSettings(sel, predict);
    }
    public boolean esp;
    @Override
    public void onEnable() {
    	if (Celestial.instance.featureManager.getFeature(ShaderESP.class).isEnabled()) {
    		esp = true;
    		Celestial.instance.featureManager.getFeature(ShaderESP.class).toggle();
    	}
        super.onEnable();
    }

    @EventTarget
    public void render(EventRender e) {
    	  double ix = - (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * e.pt);
          double iy = - (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * e.pt);
          double iz = - (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * e.pt);

          GlStateManager.pushMatrix();
          GlStateManager.translate(ix, iy, iz);
          GlStateManager.disableDepth();
          GlStateManager.enableBlend();
          GlStateManager.disableTexture2D();
          GL11.glEnable(GL11.GL_LINE_SMOOTH);
          GL11.glLineWidth(3);
          RenderUtils.color(predict.getColorValue());
          GL11.glBegin(GL11.GL_LINES);

          GL11.glEnd();
          GL11.glLineWidth(1.5f);
          GL11.glBegin(GL11.GL_LINES);


          {
              for (Entity en : mc.world.loadedEntityList) {
                  if (en instanceof EntityEnderPearl && sel.getSetting("Pearls").getBoolValue()) {
                      penisbobra(en, ((EntityEnderPearl) en).getGravityVelocity(), 0.8F, false);
                  }
                  if (en instanceof EntityArrow && sel.getSetting("Arrows").getBoolValue()) {
                      penisbobra(en, 0.05, 0.6F, false);
                  }
                  if (en instanceof EntitySnowball && sel.getSetting("Snowballs").getBoolValue()) {
                      penisbobra(en, 0.05, 0.6F, false);
                  }
                  if (en instanceof EntityEgg && sel.getSetting("Eggs").getBoolValue()) {
                      penisbobra(en, 0.05, 0.6F, false);
                  }
                  if (en instanceof EntityPotion && sel.getSetting("Potions").getBoolValue()) {
                      penisbobra(en, 0.05, 0.6F, false);
                  }
                  if (en instanceof EntitySmallFireball && sel.getSetting("Potions").getBoolValue()) {
                      penisbobra(en, 0.05, 0.6F, false);
                  }
              }
          }


          GL11.glEnd();
          GL11.glDisable(GL11.GL_LINE_SMOOTH);
          GlStateManager.enableTexture2D();
          GlStateManager.enableDepth();
          GlStateManager.disableBlend();
          GlStateManager.resetColor();
          GlStateManager.popMatrix();
          if (esp) {
          	esp = false;
          }
    }





    public HashMap<Entity, Vec3d> lastPoss = new HashMap<>();
    public HashMap<Entity, Integer> i1 = new HashMap<>();
    double ix,iy,iz;
    private void penisbobra(Entity e, double g, double water, boolean r) {
    	double motionX = e.motionX;
        double motionY = e.motionY;
        double motionZ = e.motionZ;
        double x = e.posX;
        double y = e.posY;
        double z = e.posZ;
        ix = e.posX;
        iy = e.posY;
        iz = e.posZ;

        for (int i = 0; i < 300; i++) {
            if (r)
                RenderUtils.color(-1);
            ix = x;
            iy = y;
            iz = z;
            x += motionX;
            y += motionY;
            z += motionZ;
            if (mc.world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock() == Blocks.WATER) {
                motionX *= water;
                motionY *= water;
                motionZ *= water;
            }
            else {
                motionX *= 0.99;
                motionY *= 0.99;
                motionZ *= 0.99;
            }
            motionY -= g;
            Vec3d pos = new Vec3d(x, y, z);
            if (mc.world.rayTraceBlocks(new Vec3d(ix,iy,iz), pos) != null) {

                if (mc.world.rayTraceBlocks(new Vec3d(ix,iy,iz), pos).typeOfHit == RayTraceResult.Type.ENTITY) {
                    break;
                }
                break;
            }

            if (y <= 0 ) break;
            if (e.motionZ == 0 && e.motionX == 0 && e.motionY == 0) continue;
            lastPoss.put(e, new Vec3d(ix,iy,iz));
            GL11.glVertex3d(ix,iy,iz);
            GL11.glVertex3d(x,y,z);
            i1.put(e, i);


        }

    }
}
