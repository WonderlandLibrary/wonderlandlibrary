package de.violence.module.modules.RENDER;

import de.violence.Violence;
import de.violence.gui.VSetting;
import de.violence.module.Module;
import de.violence.module.ui.Category;
import de.violence.ui.BlickWinkel3D;
import de.violence.ui.Colours;
import de.violence.ui.Location3D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.BlockAir;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;

public class ProjectileESP extends Module {
   public VSetting WidthSlider = new VSetting("Width", this, 1.0D, 10.0D, 2.0D, false);
   private VSetting bMoveAway = new VSetting("Move Away", this, false);
   public static boolean shouldAction;
   public Map lastDistance = new HashMap();

   public ProjectileESP() {
      super("ProjectileESP", Category.RENDER);
   }

   public void onFrameRender() {
      Iterator var2 = this.mc.theWorld.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity e = (Entity)var2.next();
         if(e instanceof EntityArrow) {
            this.entityESPBox2D(e);
         }
      }

      super.onFrameRender();
   }

   private void moveAway(Entity e) {
      if(e instanceof EntityArrow) {
         EntityArrow startLoc = (EntityArrow)e;
         if(startLoc.isEntityInsideOpaqueBlock() || !(this.mc.theWorld.getBlockState(startLoc.getPosition().add(0.0D, -0.1D, 0.0D)).getBlock() instanceof BlockAir)) {
            return;
         }
      }

      Location3D startLoc1 = new Location3D(e.posX, e.posY, e.posZ);
      Location3D endLoc = new Location3D(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
      BlickWinkel3D blickWinkel3D = new BlickWinkel3D(startLoc1, endLoc);
      final float yaw = (float)((double)MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw) - blickWinkel3D.getYaw());
      if(startLoc1.distance(endLoc) <= 50.0D && startLoc1.distance(endLoc) > 3.0D) {
         if(this.lastDistance.containsKey(e)) {
            double lastDist = ((Double)this.lastDistance.get(e)).doubleValue();
            if(startLoc1.distance(endLoc) > lastDist) {
               return;
            }
         } else {
            this.lastDistance.put(e, Double.valueOf(startLoc1.distance(endLoc)));
         }

         shouldAction = false;
         this.move(yaw - 90.0F, 0.2F);
         (new Thread(new Runnable() {
            public void run() {
               try {
                  Thread.sleep(600L);
                  ProjectileESP.this.move(yaw - 180.0F, 0.2F);
                  ProjectileESP.shouldAction = false;
               } catch (Exception var2) {
                  ;
               }

            }
         })).start();
         Violence.getViolence().sendChat("moving away from projectile. yaw: " + yaw);
      }

   }

   public void entityESPBox2D(Entity entity) {
      if(this.bMoveAway.isToggled()) {
         this.moveAway(entity);
      }

      GlStateManager.pushMatrix();
      GlStateManager.translate(entity.posX - RenderManager.renderPosX, entity.posY - RenderManager.renderPosY + (double)((int)(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY)) * 1.35D, entity.posZ - RenderManager.renderPosZ);
      GlStateManager.rotate(-this.mc.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.scale(0.027027028F, 0.027027028F, 0.027027028F);
      int color = Colours.getColor(255, 170, 0, 255);
      int high = -20 + (int)(entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) * 50;
      int width = (int)this.WidthSlider.getCurrent() - 3;
      int i1 = 22 + width;
      byte i2 = 21;
      byte i3 = 18;
      int i4 = 8 - width;
      int i5 = 26 + width;
      int i6 = 9 - width;
      byte i7 = 24;
      GlStateManager.disableDepth();
      GuiScreen.drawRect(i1, -i1 - high, i4, -i3 - high, Colours.getColor(255, 170, 0, 255));
      GuiScreen.drawRect(i3, -i1 - high, i1, -i4 - high, Colours.getColor(255, 170, 0, 255));
      GuiScreen.drawRect(-i1, -i1 - high, -i4, -i3 - high, Colours.getColor(255, 170, 0, 255));
      GuiScreen.drawRect(-i3, -i1 - high, -i1, -i4 - high, Colours.getColor(255, 170, 0, 255));
      GuiScreen.drawRect(i1, i2, i4, i5, Colours.getColor(255, 170, 0, 255));
      GuiScreen.drawRect(i3, i7, i1, i6, Colours.getColor(255, 170, 0, 255));
      GuiScreen.drawRect(-i1, i2, -i4, i5, Colours.getColor(255, 170, 0, 255));
      GuiScreen.drawRect(-i3, i7, -i1, i6, Colours.getColor(255, 170, 0, 255));
      i1 = 22 + width;
      i2 = 21;
      i3 = 19;
      i4 = 8 - width;
      i5 = 24 + width;
      i6 = 9 - width;
      i7 = 21;
      GlStateManager.disableDepth();
      GuiScreen.drawRect(i1, -i1 - high, i4, -i3 - high, color);
      GuiScreen.drawRect(i3, -i1 - high, i1, -i4 - high, color);
      GuiScreen.drawRect(-i1, -i1 - high, -i4, -i3 - high, color);
      GuiScreen.drawRect(-i3, -i1 - high, -i1, -i4 - high, color);
      GuiScreen.drawRect(i1, i2, i4, i5, color);
      GuiScreen.drawRect(i3, i7, i1, i6, color);
      GuiScreen.drawRect(-i1, i2, -i4, i5, color);
      GuiScreen.drawRect(-i3, i7, -i1, i6, color);
      GlStateManager.enableDepth();
      GlStateManager.popMatrix();
   }
}
