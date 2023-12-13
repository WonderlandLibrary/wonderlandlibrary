package cn.hanabi.modules.modules.world;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventChat;
import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventUpdate;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.Colors;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.TimeHelper;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.yarukon.palette.ColorValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class HideAndSeek extends Mod {
   public static List kids = new ArrayList();
   public TimeHelper timer = new TimeHelper();
   public static ColorValue esp = new ColorValue("HideAndSeek Color", 0.5F, 1.0F, 1.0F, 1.0F, true, false, 10.0F);

   public HideAndSeek() {
      super("HideAndSeek", Category.WORLD);
   }

   public void onEnable() {
      kids.clear();
   }

   public void onDisable() {
      kids.clear();
   }

   @EventTarget
   public void onChat(EventChat e) {
      if (e.getMessage().contains("躲猫猫")) {
         this.timer.reset();
      }

   }

   @EventTarget
   public void onRender(EventRender e) {
      Iterator var2 = kids.iterator();

      while(var2.hasNext()) {
         EntityLivingBase entity = (EntityLivingBase)var2.next();
         if (entity == null) {
            return;
         }

         Color color = new Color(Colors.DARKRED.c);
         mc.getRenderManager();
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
         mc.getRenderManager();
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
         mc.getRenderManager();
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
         double d;
         double mid;
         if (entity instanceof EntityPlayer) {
            d = entity.isSneaking() ? 0.25 : 0.0;
            mid = 0.275;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            double rotAdd = -0.25 * (double)(Math.abs(entity.rotationPitch) / 90.0F);
            GL11.glTranslated(0.0, rotAdd, 0.0);
            GL11.glTranslated((x -= 0.275) + 0.275, (y += (double)entity.getEyeHeight() - 0.225 - d) + 0.275, (z -= 0.275) + 0.275);
            GL11.glRotated((double)(-entity.rotationYaw % 360.0F), 0.0, 1.0, 0.0);
            GL11.glTranslated(-(x + 0.275), -(y + 0.275), -(z + 0.275));
            GL11.glTranslated(x + 0.275, y + 0.275, z + 0.275);
            GL11.glRotated((double)entity.rotationPitch, 1.0, 0.0, 0.0);
            GL11.glTranslated(-(x + 0.275), -(y + 0.275), -(z + 0.275));
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 1.0F);
            GL11.glLineWidth(1.0F);
            RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025));
            GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 0.5F);
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025));
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
         } else {
            d = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX;
            mid = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.25;
            float red = 1.0F;
            float green = 0.0F;
            float blue = 0.0F;
            float alpha = 0.5F;
            float lineRed = 0.0F;
            float lineGreen = 0.5F;
            float lineBlue = 1.0F;
            float lineAlpha = 1.0F;
            float lineWdith = 2.0F;
            RenderUtil.drawEntityESP(x, y, z, d, mid, esp.getColor(), 0.0F, 0.5F, 1.0F, 0.0F, 2.0F);
         }
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      Iterator var2 = mc.theWorld.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity entity = (Entity)var2.next();
         if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArmorStand) && !(entity instanceof EntityWither) && !kids.contains(entity) && !entity.getName().contains("§c§l") && this.timer.isDelayComplete(5000L)) {
            double pos = entity.posY - (double)((int)entity.posY);
            if (pos > 0.1 && (pos + "").length() > 8) {
               kids.add((EntityLivingBase)entity);
               PlayerUtil.tellPlayer("§b[Hanabi]§a检测到一个异常动物:" + entity.getName());
            }
         }
      }

      kids.removeIf((entityx) -> {
         return entityx.isDead || entityx.getHealth() < 0.0F;
      });
   }
}
