package cn.hanabi.modules.modules.render;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventPreMotion;
import cn.hanabi.events.EventRender2D;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.modules.world.AntiBot;
import cn.hanabi.utils.GLUtil;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Maps;
import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.Display;

public class ArrowEsp extends Mod {
   private int alpha;
   private final Value size = new Value("ArrowEsp", "Size", 10.0, 5.0, 25.0, 0.1);
   private final Value radius = new Value("ArrowEsp", "Radius", 45.0, 10.0, 200.0, 1.0);
   private final EntityListener entityListener = new EntityListener();
   private final Value players = new Value("ArrowEsp", "Players", true);
   private final Value animals = new Value("ArrowEsp", "Animals", true);
   private final Value mobs = new Value("ArrowEsp", "Mobs", false);
   private final Value invisibles = new Value("ArrowEsp", "Invisibles", false);
   private final Value passives = new Value("ArrowEsp", "Passives", true);

   public ArrowEsp() {
      super("ArrowEsp", Category.RENDER);
   }

   public void onEnable() {
      this.alpha = 0;
   }

   @EventTarget
   public void onRender3D(EventPreMotion event) {
      this.entityListener.render3d();
   }

   @EventTarget
   public void onRender2D(EventRender2D event) {
      mc.theWorld.loadedEntityList.forEach((o) -> {
         if (o instanceof EntityLivingBase && this.isValid((EntityLivingBase)o)) {
            EntityLivingBase entity = (EntityLivingBase)o;
            Vec3 pos = (Vec3)this.entityListener.getEntityLowerBounds().get(entity);
            if (pos != null) {
               float x = (float)Display.getWidth() / 2.0F / (float)(mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale);
               float y = (float)Display.getHeight() / 2.0F / (float)(mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale);
               float yaw = this.getRotations(entity) - mc.thePlayer.rotationYaw;
               GLUtil.startSmooth();
               GlStateManager.translate(x, y, 0.0F);
               GlStateManager.rotate(yaw, 0.0F, 0.0F, 1.0F);
               GlStateManager.translate(-x, -y, 0.0F);
               RenderUtil.drawTracerPointer(x, y - ((Double)this.radius.getValue()).floatValue(), ((Double)this.size.getValue()).floatValue(), 2.0F, 1.0F, this.getColor(entity, this.alpha).getRGB());
               GlStateManager.translate(x, y, 0.0F);
               GlStateManager.rotate(-yaw, 0.0F, 0.0F, 1.0F);
               GlStateManager.translate(-x, -y, 0.0F);
               GLUtil.endSmooth();
            }
         }

      });
   }

   private boolean isOnScreen(Vec3 pos) {
      if (pos.xCoord > -1.0 && pos.zCoord < 1.0) {
         return pos.xCoord / (double)(mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) >= 0.0 && pos.xCoord / (double)(mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) <= (double)Display.getWidth() && pos.yCoord / (double)(mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) >= 0.0 && pos.yCoord / (double)(mc.gameSettings.guiScale == 0 ? 1 : mc.gameSettings.guiScale) <= (double)Display.getHeight();
      } else {
         return false;
      }
   }

   private boolean isValid(EntityLivingBase entity) {
      return !AntiBot.isBot(entity) && entity != mc.thePlayer && this.isValidType(entity) && entity.getEntityId() != -1488 && entity.isEntityAlive() && (!entity.isInvisible() || (Boolean)this.invisibles.getValueState());
   }

   private boolean isValidType(EntityLivingBase entity) {
      return (Boolean)this.players.getValueState() && entity instanceof EntityPlayer || (Boolean)this.mobs.getValueState() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (Boolean)this.passives.getValueState() && (entity instanceof EntityVillager || entity instanceof EntityGolem) || (Boolean)this.animals.getValueState() && entity instanceof EntityAnimal;
   }

   private float getRotations(EntityLivingBase ent) {
      double x = ent.posX - mc.thePlayer.posX;
      double z = ent.posZ - mc.thePlayer.posZ;
      return (float)(-(Math.atan2(x, z) * 57.29577951308232));
   }

   private Color getColor(EntityLivingBase player, int alpha) {
      float f = mc.thePlayer.getDistanceToEntity(player);
      float f1 = 50.0F;
      float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
      int design = HUD.design.getColor();
      float red = (float)(design >> 16 & 255) / 255.0F;
      float green = (float)(design >> 8 & 255) / 255.0F;
      float blue = (float)(design & 255) / 255.0F;
      Color clr = HUD.hudMode.isCurrentMode("Simple") ? new Color(1.0F, 1.0F, 1.0F, f2) : new Color(red, green, blue, f2);
      return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), (int)((float)(255 - clr.getAlpha()) / 1.3F));
   }

   public static class EntityListener {
      private final Map entityUpperBounds = Maps.newHashMap();
      private final Map entityLowerBounds = Maps.newHashMap();

      private void render3d() {
         if (!this.entityUpperBounds.isEmpty()) {
            this.entityUpperBounds.clear();
         }

         if (!this.entityLowerBounds.isEmpty()) {
            this.entityLowerBounds.clear();
         }

         Iterator var1 = ArrowEsp.mc.theWorld.loadedEntityList.iterator();

         while(var1.hasNext()) {
            Entity e = (Entity)var1.next();
            Vec3 bound = this.getEntityRenderPosition(e);
            bound.add(new Vec3(0.0, (double)e.height + 0.2, 0.0));
            Vec3 upperBounds = RenderUtil.to2D(bound.xCoord, bound.yCoord, bound.zCoord);
            Vec3 lowerBounds = RenderUtil.to2D(bound.xCoord, bound.yCoord - 2.0, bound.zCoord);
            if (upperBounds != null && lowerBounds != null) {
               this.entityUpperBounds.put(e, upperBounds);
               this.entityLowerBounds.put(e, lowerBounds);
            }
         }

      }

      private Vec3 getEntityRenderPosition(Entity entity) {
         double partial = (double)Wrapper.getTimer().renderPartialTicks;
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial - ArrowEsp.mc.getRenderManager().viewerPosX;
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial - ArrowEsp.mc.getRenderManager().viewerPosY;
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial - ArrowEsp.mc.getRenderManager().viewerPosZ;
         return new Vec3(x, y, z);
      }

      public Map getEntityLowerBounds() {
         return this.entityLowerBounds;
      }
   }
}
