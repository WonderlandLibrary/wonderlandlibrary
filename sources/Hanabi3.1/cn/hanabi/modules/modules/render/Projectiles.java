package cn.hanabi.modules.modules.render;

import cn.hanabi.Wrapper;
import cn.hanabi.events.EventRender;
import cn.hanabi.injection.interfaces.IRenderManager;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Projectiles extends Mod {
   private MovingObjectPosition blockCollision;

   public Projectiles() {
      super("Projectiles", Category.RENDER);
   }

   public static void drawBox(AxisAlignedBB bb) {
      GL11.glBegin(7);
      GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
      GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
      GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
      GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
      GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
      GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
      GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
      GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
      GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
      GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
      GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
      GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
      GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
      GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
      GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
      GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
      GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
      GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
      GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
      GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
      GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
      GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
      GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
      GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
      GL11.glEnd();
   }

   public static void drawSelectionBoundingBox(AxisAlignedBB p_181561_0_) {
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      worldrenderer.begin(3, DefaultVertexFormats.POSITION);
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
      worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
      worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
      tessellator.draw();
      worldrenderer.begin(3, DefaultVertexFormats.POSITION);
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
      worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
      worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
      tessellator.draw();
      worldrenderer.begin(1, DefaultVertexFormats.POSITION);
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
      worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
      worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
      worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
      worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
      worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
      tessellator.draw();
   }

   @EventTarget
   public void onRender(EventRender event) {
      if (mc.thePlayer.inventory.getCurrentItem() != null) {
         EntityPlayerSP player = mc.thePlayer;
         ItemStack stack = player.inventory.getCurrentItem();
         int item = Item.getIdFromItem(mc.thePlayer.getHeldItem().getItem());
         if (item == 261 || item == 368 || item == 332 || item == 344) {
            double posX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)Wrapper.getTimer().renderPartialTicks - Math.cos(Math.toRadians((double)player.rotationYaw)) * 0.1599999964237213;
            double posY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)Wrapper.getTimer().renderPartialTicks + (double)player.getEyeHeight() - 0.1;
            double posZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)Wrapper.getTimer().renderPartialTicks - Math.sin(Math.toRadians((double)player.rotationYaw)) * 0.1599999964237213;
            double itemBow = stack.getItem() instanceof ItemBow ? 1.0 : 0.4000000059604645;
            double yaw = Math.toRadians((double)player.rotationYaw);
            double pitch = Math.toRadians((double)player.rotationPitch);
            double trajectoryX = -Math.sin(yaw) * Math.cos(pitch) * itemBow;
            double trajectoryY = -Math.sin(pitch) * itemBow;
            double trajectoryZ = Math.cos(yaw) * Math.cos(pitch) * itemBow;
            double trajectory = Math.sqrt(trajectoryX * trajectoryX + trajectoryY * trajectoryY + trajectoryZ * trajectoryZ);
            trajectoryX /= trajectory;
            trajectoryY /= trajectory;
            trajectoryZ /= trajectory;
            if (stack.getItem() instanceof ItemBow) {
               float bowPower = (float)(72000 - player.getItemInUseCount()) / 20.0F;
               bowPower = (bowPower * bowPower + bowPower * 2.0F) / 3.0F;
               if (bowPower > 1.0F) {
                  bowPower = 1.0F;
               }

               bowPower *= 3.0F;
               trajectoryX *= (double)bowPower;
               trajectoryY *= (double)bowPower;
               trajectoryZ *= (double)bowPower;
            } else {
               trajectoryX *= 1.5;
               trajectoryY *= 1.5;
               trajectoryZ *= 1.5;
            }

            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0F);
            double gravity = stack.getItem() instanceof ItemBow ? 0.05 : 0.03;
            GL11.glColor4f(0.0F, 1.0F, 0.2F, 0.5F);
            GL11.glBegin(3);

            for(int i = 0; i < 2000; ++i) {
               GL11.glVertex3d(posX - ((IRenderManager)mc.getRenderManager()).getRenderPosX(), posY - ((IRenderManager)mc.getRenderManager()).getRenderPosY(), posZ - ((IRenderManager)mc.getRenderManager()).getRenderPosZ());
               posX += trajectoryX * 0.1;
               posY += trajectoryY * 0.1;
               posZ += trajectoryZ * 0.1;
               trajectoryX *= 0.999;
               trajectoryY *= 0.999;
               trajectoryZ *= 0.999;
               trajectoryY -= gravity * 0.1;
               Vec3 vec = new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
               this.blockCollision = mc.theWorld.rayTraceBlocks(vec, new Vec3(posX, posY, posZ));
               Iterator var29 = mc.theWorld.getLoadedEntityList().iterator();

               while(var29.hasNext()) {
                  Entity o = (Entity)var29.next();
                  if (o instanceof EntityLivingBase && !(o instanceof EntityPlayerSP)) {
                     EntityLivingBase entity = (EntityLivingBase)o;
                     AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox().expand(0.3, 0.3, 0.3);
                     MovingObjectPosition entityCollision = entityBoundingBox.calculateIntercept(vec, new Vec3(posX, posY, posZ));
                     if (entityCollision != null) {
                        this.blockCollision = entityCollision;
                     }

                     if (entityCollision != null) {
                        GL11.glColor4f(1.0F, 0.0F, 0.2F, 0.5F);
                     }

                     if (entityCollision != null) {
                        this.blockCollision = entityCollision;
                     }
                  }
               }

               if (this.blockCollision != null) {
                  break;
               }
            }

            GL11.glEnd();
            double renderX = posX - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
            double renderY = posY - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
            double renderZ = posZ - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
            GL11.glPushMatrix();
            GL11.glTranslated(renderX - 0.5, renderY - 0.5, renderZ - 0.5);
            AxisAlignedBB aim;
            switch (this.blockCollision.sideHit.getIndex()) {
               case 2:
               case 3:
                  GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                  aim = new AxisAlignedBB(0.0, 0.5, -1.0, 1.0, 0.45, 0.0);
                  break;
               case 4:
               case 5:
                  GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                  aim = new AxisAlignedBB(0.0, -0.5, 0.0, 1.0, -0.45, 1.0);
                  break;
               default:
                  aim = new AxisAlignedBB(0.0, 0.5, 0.0, 1.0, 0.45, 1.0);
            }

            drawBox(aim);
            drawSelectionBoundingBox(aim);
            GL11.glPopMatrix();
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glPopMatrix();
         }
      }

   }
}
