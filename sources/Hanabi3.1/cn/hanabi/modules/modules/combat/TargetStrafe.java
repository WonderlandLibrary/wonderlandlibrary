package cn.hanabi.modules.modules.combat;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import cn.hanabi.events.EventMove;
import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventWorldChange;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.PlayerUtil;
import cn.hanabi.utils.TimeHelper;
import cn.hanabi.utils.rotation.RotationUtil;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@ObfuscationClass
public class TargetStrafe extends Mod {
   public static boolean dire = true;
   public static Value range = new Value("TargetStrafe", "Strafe Range", 2.0, 0.5, 4.5, 0.1);
   public static Value targetkey = new Value("TargetStrafe", "Space Toggled", true);
   public static Value voidCheck = new Value("TargetStrafe", "Void Check", true);
   public static Value behindValue = new Value("TargetStrafe", "Behind", false);
   public static Value change = new Value("TargetStrafe", "Direction", true);
   public static Value render = new Value("TargetStrafe", "Render", true);
   public static boolean direction = true;
   public TimeHelper timer = new TimeHelper();
   private int strafe = -1;
   public float yaw;

   public TargetStrafe() {
      super("TargetStrafe", Category.COMBAT);
   }

   public static boolean canStrafe() {
      boolean press = !(Boolean)targetkey.getValue() || mc.gameSettings.keyBindJump.isKeyDown();
      return KillAura.target != null && press && ModManager.getModule("KillAura").isEnabled() && (ModManager.getModule("Speed").isEnabled() || ModManager.getModule("Fly").isEnabled());
   }

   public void onEnable() {
      super.onEnable();
   }

   @EventTarget
   private void onChangeWorld(EventWorldChange event) {
      this.strafe = -1;
   }

   @EventTarget
   private void onRender3D(EventRender event) {
      if (canStrafe() && (Boolean)render.getValue()) {
         EntityLivingBase target = KillAura.target;
         this.esp(target, event.getPartialTicks(), (Double)range.getValue());
      }

   }

   public void esp(Entity entity, float partialTicks, double rad) {
      float points = 90.0F;
      GlStateManager.enableDepth();

      for(double il = 0.0; il < Double.MIN_VALUE; il += Double.MIN_VALUE) {
         GL11.glPushMatrix();
         GL11.glDisable(3553);
         GL11.glEnable(2848);
         GL11.glEnable(2881);
         GL11.glEnable(2832);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glHint(3154, 4354);
         GL11.glHint(3155, 4354);
         GL11.glHint(3153, 4354);
         GL11.glDisable(2929);
         GL11.glLineWidth(3.5F);
         GL11.glBegin(3);
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - mc.getRenderManager().viewerPosX;
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - mc.getRenderManager().viewerPosY;
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - mc.getRenderManager().viewerPosZ;
         double pix2 = 6.283185307179586;
         float speed = 5000.0F;

         float baseHue;
         for(baseHue = (float)(System.currentTimeMillis() % (long)((int)speed)); baseHue > speed; baseHue -= speed) {
         }

         baseHue /= speed;

         for(int i = 0; i <= 90; ++i) {
            float max = ((float)i + (float)(il * 8.0)) / points;

            float hue;
            for(hue = max + baseHue; hue > 1.0F; --hue) {
            }

            float r = 0.003921569F * (float)(new Color(Color.HSBtoRGB(hue, 0.75F, 1.0F))).getRed();
            float g = 0.003921569F * (float)(new Color(Color.HSBtoRGB(hue, 0.75F, 1.0F))).getGreen();
            float b = 0.003921569F * (float)(new Color(Color.HSBtoRGB(hue, 0.75F, 1.0F))).getBlue();
            int color = Color.WHITE.getRGB();
            GL11.glColor3f(r, g, b);
            GL11.glVertex3d(x + rad * Math.cos((double)i * 6.283185307179586 / (double)points), y + il, z + rad * Math.sin((double)i * 6.283185307179586 / (double)points));
         }

         GL11.glEnd();
         GL11.glDepthMask(true);
         GL11.glEnable(2929);
         GL11.glDisable(2848);
         GL11.glDisable(2881);
         GL11.glEnable(2832);
         GL11.glEnable(3553);
         GL11.glPopMatrix();
         GlStateManager.color(255.0F, 255.0F, 255.0F);
      }

   }

   public boolean isStrafing(EventMove event, EntityLivingBase target, double moveSpeed) {
      boolean pressingSpace = !(Boolean)targetkey.getValue() || Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
      if (this.isEnabled() && target != null && moveSpeed != 0.0 && pressingSpace) {
         boolean aroundVoid = false;

         for(int x = -1; x < 1; ++x) {
            for(int z = -1; z < 1; ++z) {
               if (this.isVoid(x, z)) {
                  aroundVoid = true;
               }
            }
         }

         float yaw = RotationUtil.getRotationFromEyeHasPrev(target).getYaw();
         boolean behindTarget = RotationUtil.getRotationDifference(MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(target.rotationYaw)) <= 10.0F;
         if (mc.thePlayer.isCollidedHorizontally || aroundVoid && (Boolean)voidCheck.getValue() || behindTarget && (Boolean)behindValue.getValue()) {
            this.strafe *= -1;
         }

         float targetStrafe = (Boolean)change.getValue() && mc.thePlayer.moveStrafing != 0.0F ? mc.thePlayer.moveStrafing * (float)this.strafe : (float)this.strafe;
         if (PlayerUtil.isBlockUnder()) {
            targetStrafe = 0.0F;
         }

         double rotAssist = 45.0 / this.getEnemyDistance(target);
         double moveAssist = (double)(45.0F / this.getStrafeDistance(target));
         float mathStrafe = 0.0F;
         if (targetStrafe > 0.0F) {
            if ((target.getEntityBoundingBox().minY > mc.thePlayer.getEntityBoundingBox().maxY || target.getEntityBoundingBox().maxY < mc.thePlayer.getEntityBoundingBox().minY) && this.getEnemyDistance(target) < (double)((Double)range.getValue()).floatValue()) {
               yaw = (float)((double)yaw + -rotAssist);
            }

            mathStrafe = (float)((double)mathStrafe + -moveAssist);
         } else if (targetStrafe < 0.0F) {
            if ((target.getEntityBoundingBox().minY > mc.thePlayer.getEntityBoundingBox().maxY || target.getEntityBoundingBox().maxY < mc.thePlayer.getEntityBoundingBox().minY) && this.getEnemyDistance(target) < (double)((Double)range.getValue()).floatValue()) {
               yaw = (float)((double)yaw + rotAssist);
            }

            mathStrafe = (float)((double)mathStrafe + moveAssist);
         }

         double[] doSomeMath = new double[]{Math.cos(Math.toRadians((double)(yaw + 90.0F + mathStrafe))), Math.sin(Math.toRadians((double)(yaw + 90.0F + mathStrafe)))};
         double[] asLast = new double[]{moveSpeed * doSomeMath[0], moveSpeed * doSomeMath[1]};
         if (event != null) {
            event.setX(mc.thePlayer.motionX = asLast[0]);
            event.setZ(mc.thePlayer.motionZ = asLast[1]);
         } else {
            mc.thePlayer.motionX = asLast[0];
            mc.thePlayer.motionZ = asLast[1];
         }

         return false;
      } else {
         return true;
      }
   }

   private double getEnemyDistance(EntityLivingBase target) {
      return mc.thePlayer.getDistance(target.posX, mc.thePlayer.posY, target.posZ);
   }

   private float getStrafeDistance(EntityLivingBase target) {
      return (float)Math.max(this.getEnemyDistance(target) - (double)((Double)range.getValue()).floatValue(), this.getEnemyDistance(target) - (this.getEnemyDistance(target) - (double)(((Double)range.getValue()).floatValue() / (((Double)range.getValue()).floatValue() * 2.0F))));
   }

   private boolean isVoid(int x, int z) {
      if (mc.thePlayer.posY < 0.0) {
         return true;
      } else {
         for(int off = 0; (double)off < mc.thePlayer.posY + 2.0; off += 2) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset((double)x, (double)(-off), (double)z);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
               return false;
            }
         }

         return true;
      }
   }
}
