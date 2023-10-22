package cn.hanabi.modules.modules.render;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventRender;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.ClientUtil;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.util.Iterator;
import me.yarukon.palette.ColorValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class ESP extends Mod {
   public static Value mode = new Value("ESP", "Mode", 0);
   private final Value invisible = new Value("ESP", "Invisible", false);
   public static ColorValue esp = new ColorValue("Esp Color", 0.5F, 1.0F, 1.0F, 1.0F, true, false, 10.0F);

   public ESP() {
      super("ESP", Category.RENDER);
      mode.LoadValue(new String[]{"Box", "2D", "OutLine"});
   }

   public void renderBox(Entity entity, double r, double g, double b) {
      if (!entity.isInvisible() || (Boolean)this.invisible.getValueState()) {
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)Wrapper.getTimer().renderPartialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
         double width = entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX - 0.1;
         double height = entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY + 0.25;
         RenderUtil.drawEntityESP(x, y, z, width, height, esp.getColor(), 1.0F, 1.0F, 1.0F, 0.0F, 1.0F);
      }
   }

   @EventTarget
   public void onRender(EventRender event) {
      if (mode.isCurrentMode("Box")) {
         this.setDisplayName("Box");
         Iterator var2 = mc.theWorld.loadedEntityList.iterator();

         while(var2.hasNext()) {
            Object o = var2.next();
            if (o instanceof EntityPlayer) {
               EntityPlayer ent = (EntityPlayer)o;
               if (ent != mc.thePlayer && !ent.isDead) {
                  this.renderBox(ent, 1.0, 1.0, 1.0);
               }
            }
         }
      } else if (mode.isCurrentMode("2D")) {
         this.setDisplayName("2D");
         this.doOther2DESP();
      }

   }

   private boolean isValid(EntityLivingBase entity) {
      return entity != mc.thePlayer && !(entity.getHealth() <= 0.0F) && entity instanceof EntityPlayer;
   }

   private void doOther2DESP() {
      Iterator var1 = mc.theWorld.playerEntities.iterator();

      while(var1.hasNext()) {
         EntityPlayer entity = (EntityPlayer)var1.next();
         if (entity.isInvisible() && !(Boolean)this.invisible.getValueState()) {
            return;
         }

         if (this.isValid(entity)) {
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.enableBlend();
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            float partialTicks = Wrapper.getTimer().renderPartialTicks;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
            float SCALE = 0.035F;
            SCALE /= 2.0F;
            float xMid = (float)x;
            float yMid = (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F);
            float zMid = (float)z;
            GlStateManager.translate((float)x, (float)y + entity.height + 0.5F - (entity.isChild() ? entity.height / 2.0F : 0.0F), (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-SCALE, -SCALE, -SCALE);
            Tessellator tesselator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tesselator.getWorldRenderer();
            double xLeft = -30.0;
            double xRight = 30.0;
            double yUp = 15.0;
            double yDown = 140.0;
            RenderUtil.drawRect(-30.0F, 15.0F, 30.0F, 140.0F, ClientUtil.reAlpha((new Color(255, 255, 255)).getRGB(), 0.2F));
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GlStateManager.disableBlend();
            GL11.glDisable(3042);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glNormal3f(1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
         }
      }

   }
}
