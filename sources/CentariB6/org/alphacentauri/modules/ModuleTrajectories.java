package org.alphacentauri.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventRender3D;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;
import org.alphacentauri.management.util.RenderUtils;
import org.alphacentauri.management.util.RotationUtils;
import org.lwjgl.opengl.GL11;

public class ModuleTrajectories extends Module implements EventListener {
   private Property lineWidth = new Property(this, "LineThickness", Float.valueOf(1.0F));

   public ModuleTrajectories() {
      super("Trajectories", "Helps you at aiming!", new String[]{"trajectories"}, Module.Category.Render, 6830571);
   }

   public void onEvent(Event event) {
      if(event instanceof EventRender3D) {
         EntityPlayerSP player = AC.getMC().getPlayer();
         ItemStack stack = player.getHeldItem();
         if(stack == null) {
            return;
         }

         Item item = stack.getItem();
         if(item instanceof ItemBow && player.getItemInUse() == null) {
            return;
         }

         if(!(item instanceof ItemBow) && !(item instanceof ItemSnowball) && !(item instanceof ItemEgg) && !(item instanceof ItemEnderPearl) && !(item instanceof ItemPotion)) {
            return;
         }

         boolean usingBow = player.getHeldItem().getItem() instanceof ItemBow;
         float yaw1 = player.rotationYaw;
         float pitch1 = player.rotationPitch;
         if(RotationUtils.isSet()) {
            yaw1 = RotationUtils.getYaw();
            pitch1 = RotationUtils.getPitch();
         }

         double arrowPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)AC.getMC().timer.renderPartialTicks - (double)(MathHelper.cos((float)Math.toRadians((double)yaw1)) * 0.16F);
         double arrowPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)Minecraft.getMinecraft().timer.renderPartialTicks + (double)player.getEyeHeight() - 0.1D;
         double arrowPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)Minecraft.getMinecraft().timer.renderPartialTicks - (double)(MathHelper.sin((float)Math.toRadians((double)yaw1)) * 0.16F);
         float arrowMotionFactor = usingBow?1.0F:0.4F;
         float yaw = (float)Math.toRadians((double)yaw1);
         float pitch = (float)Math.toRadians((double)pitch1);
         float arrowMotionX = -MathHelper.sin(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
         float arrowMotionY = -MathHelper.sin(pitch) * arrowMotionFactor;
         float arrowMotionZ = MathHelper.cos(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
         double arrowMotion = Math.sqrt((double)(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ));
         arrowMotionX = (float)((double)arrowMotionX / arrowMotion);
         arrowMotionY = (float)((double)arrowMotionY / arrowMotion);
         arrowMotionZ = (float)((double)arrowMotionZ / arrowMotion);
         if(usingBow) {
            float bowPower = (float)player.getItemInUseDuration() / 20.0F;
            bowPower = (bowPower * bowPower + bowPower * 2.0F) / 3.0F;
            if(bowPower > 1.0F) {
               bowPower = 1.0F;
            }

            if(bowPower <= 0.1F) {
               bowPower = 1.0F;
            }

            bowPower = bowPower * 3.0F;
            arrowMotionX = arrowMotionX * bowPower;
            arrowMotionY = arrowMotionY * bowPower;
            arrowMotionZ = arrowMotionZ * bowPower;
         } else {
            arrowMotionX = (float)((double)arrowMotionX * 1.5D);
            arrowMotionY = (float)((double)arrowMotionY * 1.5D);
            arrowMotionZ = (float)((double)arrowMotionZ * 1.5D);
         }

         GL11.glPushMatrix();
         GL11.glEnable(2848);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(3042);
         GL11.glDisable(3553);
         GL11.glDisable(2929);
         GL11.glEnable('肝');
         GL11.glDepthMask(false);
         GL11.glLineWidth(((Float)this.lineWidth.value).floatValue());
         RenderManager renderManager = AC.getMC().getRenderManager();
         double gravity = usingBow?0.05D:(item instanceof ItemPotion?0.4D:0.03D);
         Vec3 playerVector = new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
         GL11.glColor3d(0.0D, 0.5D, 1.0D);
         GL11.glBegin(3);
         EnumFacing sideHit = null;

         for(int i = 0; i < 1000; ++i) {
            GL11.glVertex3d(arrowPosX - renderManager.viewerPosX, arrowPosY - renderManager.viewerPosY, arrowPosZ - renderManager.viewerPosZ);
            arrowPosX += (double)arrowMotionX;
            arrowPosY += (double)arrowMotionY;
            arrowPosZ += (double)arrowMotionZ;
            arrowMotionX = (float)((double)arrowMotionX * 0.99D);
            arrowMotionY = (float)((double)arrowMotionY * 0.99D);
            arrowMotionZ = (float)((double)arrowMotionZ * 0.99D);
            arrowMotionY = (float)((double)arrowMotionY - gravity);
            MovingObjectPosition rayTrace = AC.getMC().theWorld.rayTraceBlocks(playerVector, new Vec3(arrowPosX, arrowPosY, arrowPosZ));
            if(rayTrace != null) {
               sideHit = rayTrace.sideHit;
               break;
            }
         }

         GL11.glEnd();
         double renderX = arrowPosX - renderManager.viewerPosX;
         double renderY = arrowPosY - renderManager.viewerPosY;
         double renderZ = arrowPosZ - renderManager.viewerPosZ;
         AxisAlignedBB bb = new AxisAlignedBB(renderX - 0.5D, renderY - 0.5D, renderZ - 0.5D, renderX + 0.5D, renderY + 0.5D, renderZ + 0.5D);
         GL11.glColor4d(0.0D, 0.5D, 1.0D, 0.15000000596046448D);
         RenderUtils.drawColorBox(bb);
         GL11.glColor4d(0.0D, 0.0D, 0.0D, 0.5D);
         RenderGlobal.drawSelectionBoundingBox(bb);
         GL11.glDisable(3042);
         GL11.glEnable(3553);
         GL11.glEnable(2929);
         GL11.glDisable('肝');
         GL11.glDepthMask(true);
         GL11.glDisable(2848);
         GL11.glPopMatrix();
      }

   }
}
