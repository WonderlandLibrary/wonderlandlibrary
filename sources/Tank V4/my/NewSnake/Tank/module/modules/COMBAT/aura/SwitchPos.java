package my.NewSnake.Tank.module.modules.COMBAT.aura;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import my.NewSnake.Tank.Ui.font.RotationUtils;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.modules.COMBAT.AuraPos;
import my.NewSnake.Tank.module.modules.MOVEMENT.NoSlowDown;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render3DEvent;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.GLUtils;
import my.NewSnake.utils.StateManager;
import my.NewSnake.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class SwitchPos extends AuraMode {
   private static volatile int[] $SWITCH_TABLE$my$NewSnake$event$Event$State;
   private int index;
   private float animtest;
   public static List targets;
   public static RotationUtils.Rotation serverRotation = new RotationUtils.Rotation(0.0F, 0.0F);
   private Timer timer;
   private int test;
   private boolean anim;
   private boolean switchingTargets;

   public static float[] ncpRotations(Entity var0, UpdateEvent var1) {
      double var2 = var0.posX + (var0.posX - var0.lastTickPosX) - var1.getX();
      double var10000 = var0.posY + (double)var0.getEyeHeight();
      double var10001 = var1.getY();
      Minecraft.getMinecraft();
      double var4 = var10000 - (var10001 + (double)Minecraft.thePlayer.getEyeHeight()) - 0.1D;
      double var6 = var0.posZ + (var0.posZ - var0.lastTickPosZ) - var1.getZ();
      double var8 = Math.sqrt(Math.pow(var2, 2.0D) + Math.pow(var6, 2.0D));
      float var10 = (float)Math.toDegrees(-Math.atan(var2 / var6));
      float var11 = (float)(-Math.toDegrees(Math.atan(var4 / var8)));
      if (var2 < 0.0D && var6 < 0.0D) {
         var10 = 90.0F + (float)Math.toDegrees(Math.atan(var6 / var2));
      } else if (var2 > 0.0D && var6 < 0.0D) {
         var10 = -90.0F + (float)Math.toDegrees(Math.atan(var6 / var2));
      }

      var10 = (float)((double)var10 + (Math.random() * 4.0D - Math.random()));
      var11 = (float)((double)var11 + (Math.random() * 4.0D - Math.random()));
      if (var11 > 90.0F) {
         var11 = 90.0F;
      }

      if (var11 < -90.0F) {
         var11 = -90.0F;
      }

      if (var10 > 180.0F) {
         var10 = 180.0F;
      }

      if (var10 < -180.0F) {
         var10 = -180.0F;
      }

      return new float[]{var10, var11};
   }

   private boolean hasSword() {
      ClientUtils.mc();
      if (Minecraft.thePlayer.inventory.getCurrentItem() != null) {
         ClientUtils.mc();
         if (Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            return true;
         }
      }

      return false;
   }

   public static RotationUtils.VecRotation searchCenter(AxisAlignedBB var0, boolean var1) {
      RotationUtils.VecRotation var2 = null;

      for(double var3 = 0.15D; var3 < 0.85D; var3 += 0.1D) {
         for(double var5 = 0.15D; var5 < 1.0D; var5 += 0.1D) {
            for(double var7 = 0.15D; var7 < 0.85D; var7 += 0.1D) {
               Vec3 var9 = new Vec3(var0.minX + (var0.maxX - var0.minX) * var3, var0.minY + (var0.maxY - var0.minY) * var5, var0.minZ + (var0.maxZ - var0.minZ) * var7);
               RotationUtils.Rotation var10 = toRotation(var9, var1);
               RotationUtils.VecRotation var11 = new RotationUtils.VecRotation(var9, var10);
               if (var2 == null || getRotationDifference(var11.getRotation()) < getRotationDifference(var2.getRotation())) {
                  var2 = var11;
               }
            }
         }
      }

      return var2;
   }

   private void drawCircle(Entity var1, float var2, double var3) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      GLUtils.startSmooth();
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(0.01F);
      GL11.glBegin(3);
      double var10000 = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
      ClientUtils.mc().getRenderManager();
      double var5 = var10000 - RenderManager.viewerPosX;
      var10000 = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
      ClientUtils.mc().getRenderManager();
      double var7 = var10000 - RenderManager.viewerPosY;
      var10000 = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;
      ClientUtils.mc().getRenderManager();
      double var9 = var10000 - RenderManager.viewerPosZ;
      float var11 = 0.003921569F * (float)Color.WHITE.getRed();
      float var12 = 0.003921569F * (float)Color.WHITE.getGreen();
      float var13 = 0.003921569F * (float)Color.WHITE.getBlue();
      if (this.test <= 10) {
         if (this.anim) {
            this.animtest += 0.01F;
         } else {
            this.animtest -= 0.01F;
         }

         this.test = 10;
      }

      --this.test;
      if ((double)this.animtest <= var7) {
         this.anim = true;
      } else if ((double)this.animtest >= var7 + (double)var1.getEyeHeight() + 0.25D) {
         this.anim = false;
      }

      for(int var14 = 0; var14 <= 90; ++var14) {
         GL11.glColor4f(var11, var12, var13, (float)var14);
         GL11.glVertex3d(var5 + var3 * Math.cos((double)var14 * 6.283185307179586D / 45.0D), (double)this.animtest, var9 + var3 * Math.sin((double)var14 * 6.283185307179586D / 45.0D));
      }

      GL11.glEnd();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GLUtils.endSmooth();
      GL11.glEnable(3553);
      GL11.glPopMatrix();
   }

   public boolean onUpdate(UpdateEvent var1) {
      if (super.onUpdate(var1)) {
         AuraPos var2;
         switch($SWITCH_TABLE$my$NewSnake$event$Event$State()[var1.getState().ordinal()]) {
         case 1:
            StateManager.setOffsetLastPacketAura(false);
            var2 = (AuraPos)this.getModule();
            NoSlowDown var7 = (NoSlowDown)(new NoSlowDown()).getInstance();
            targets = this.getTargets();
            if (this.index >= targets.size()) {
               this.index = 0;
            }

            if (targets.size() > 0) {
               EntityLivingBase var4 = (EntityLivingBase)targets.get(this.index);
               if (var4 != null) {
                  if (var2.AutoBlock && ClientUtils.player().getCurrentEquippedItem() != null && ClientUtils.player().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                     ClientUtils.playerController().sendUseItem(ClientUtils.player(), ClientUtils.world(), ClientUtils.player().getCurrentEquippedItem());
                  }

                  float[] var5;
                  if (AuraPos.POS) {
                     var5 = my.NewSnake.utils.RotationUtils.getRotationsA(var4);
                     var1.setYaw(var5[0]);
                     var1.setPitch(var5[1]);
                  }

                  if (AuraPos.TESTNCP) {
                     var5 = ncpRotations(var4, var1);
                     var1.setYaw(var5[0]);
                     var1.setPitch(var5[1]);
                  }

                  if (AuraPos.Normal) {
                     var5 = my.NewSnake.utils.RotationUtils.getRotations(var4);
                     var1.setYaw(var5[0]);
                     var1.setPitch(var5[1]);
                  }

                  if (AuraPos.Bypass) {
                     var5 = getCustomRotation(getLocation(((EntityLivingBase)targets.get(this.index)).getEntityBoundingBox()));
                     var1.setYaw(var5[0]);
                     var1.setPitch(var5[1]);
                  }
               }

               if (AuraPos.SetupTick) {
                  targets.size();
                  if (this.timer.delay(300.0F)) {
                     this.incrementIndex();
                     this.timer.reset();
                  }
               } else {
                  targets.size();
               }
            }

            AuraPos.SetupTick = !AuraPos.SetupTick;
            break;
         case 2:
            var2 = (AuraPos)this.getModule();
            if (AuraPos.SetupTick && targets.size() > 0 && targets.get(this.index) != null && targets.size() > 0 && targets.get(this.index) != null) {
               EntityLivingBase var3 = (EntityLivingBase)targets.get(this.index);
               if (ClientUtils.player().isBlocking()) {
                  ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
               }

               var2.attack(var3);
               if (ClientUtils.player().isBlocking()) {
                  ClientUtils.packet(new C08PacketPlayerBlockPlacement(ClientUtils.player().inventory.getCurrentItem()));
               }
            }
         }
      }

      return true;
   }

   public static Vec3 getVectorForRotation(RotationUtils.Rotation var0) {
      float var1 = (float)Math.cos((double)(-var0.getYaw() * 0.017453292F - 3.1415927F));
      float var2 = (float)Math.sin((double)(-var0.getYaw() * 0.017453292F - 3.1415927F));
      float var3 = (float)(-Math.cos((double)(-var0.getPitch() * 0.017453292F)));
      float var4 = (float)Math.sin((double)(-var0.getPitch() * 0.017453292F));
      return new Vec3((double)(var2 * var3), (double)var4, (double)(var1 * var3));
   }

   public static RotationUtils.Rotation toRotation(Vec3 var0, boolean var1) {
      ClientUtils.mc();
      double var10002 = Minecraft.thePlayer.posX;
      ClientUtils.mc();
      double var10003 = Minecraft.thePlayer.getEntityBoundingBox().minY;
      ClientUtils.mc();
      var10003 += (double)Minecraft.thePlayer.getEyeHeight();
      ClientUtils.mc();
      Vec3 var2 = new Vec3(var10002, var10003, Minecraft.thePlayer.posZ);
      if (var1) {
         ClientUtils.mc();
         double var10001 = Minecraft.thePlayer.motionX;
         ClientUtils.mc();
         var10002 = Minecraft.thePlayer.motionY;
         ClientUtils.mc();
         var2.addVector(var10001, var10002, Minecraft.thePlayer.motionZ);
      }

      double var3 = var0.xCoord - var2.xCoord;
      double var5 = var0.yCoord - var2.yCoord;
      double var7 = var0.zCoord - var2.zCoord;
      return new RotationUtils.Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(var7, var3)) - 90.0F), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(var5, Math.sqrt(var3 * var3 + var7 * var7))))));
   }

   public static RotationUtils.VecRotation faceBlock(BlockPos var0) {
      if (var0 == null) {
         return null;
      } else {
         RotationUtils.VecRotation var1 = null;

         for(double var2 = 0.1D; var2 < 0.9D; var2 += 0.1D) {
            for(double var4 = 0.1D; var4 < 0.9D; var4 += 0.1D) {
               for(double var6 = 0.1D; var6 < 0.9D; var6 += 0.1D) {
                  ClientUtils.mc();
                  double var10002 = Minecraft.thePlayer.posX;
                  ClientUtils.mc();
                  double var10003 = Minecraft.thePlayer.getEntityBoundingBox().minY;
                  ClientUtils.mc();
                  var10003 += (double)Minecraft.thePlayer.getEyeHeight();
                  ClientUtils.mc();
                  Vec3 var8 = new Vec3(var10002, var10003, Minecraft.thePlayer.posZ);
                  Vec3 var9 = (new Vec3(var0)).addVector(var2, var4, var6);
                  double var10 = var8.distanceTo(var9);
                  double var12 = var9.xCoord - var8.xCoord;
                  double var14 = var9.yCoord - var8.yCoord;
                  double var16 = var9.zCoord - var8.zCoord;
                  double var18 = Math.sqrt(var12 * var12 + var16 * var16);
                  RotationUtils.Rotation var20 = new RotationUtils.Rotation(MathHelper.wrapAngleTo180_float((float)Math.toDegrees(Math.atan2(var16, var12)) - 90.0F), MathHelper.wrapAngleTo180_float((float)(-Math.toDegrees(Math.atan2(var14, var18)))));
                  Vec3 var21 = getVectorForRotation(var20);
                  Vec3 var22 = var8.addVector(var21.xCoord * var10, var21.yCoord * var10, var21.zCoord * var10);
                  ClientUtils.mc();
                  MovingObjectPosition var23 = Minecraft.theWorld.rayTraceBlocks(var8, var22, false, false, true);
                  if (var23 != null && var23.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                     RotationUtils.VecRotation var24 = new RotationUtils.VecRotation(var9, var20);
                     if (var1 == null || getRotationDifference(var24.getRotation()) < getRotationDifference(var1.getRotation())) {
                        var1 = var24;
                     }
                  }
               }
            }
         }

         return var1;
      }
   }

   public static double getRotationDifference(RotationUtils.Rotation var0, RotationUtils.Rotation var1) {
      return Math.hypot((double)getAngleDifference(var0.getYaw(), var1.getYaw()), (double)(var0.getPitch() - var1.getPitch()));
   }

   public static float[] getCustomRotation(Vec3 var0) {
      ClientUtils.mc();
      double var10002 = Minecraft.thePlayer.posX;
      ClientUtils.mc();
      double var10003 = Minecraft.thePlayer.posY;
      ClientUtils.mc();
      var10003 += (double)Minecraft.thePlayer.getEyeHeight();
      ClientUtils.mc();
      Vec3 var1 = new Vec3(var10002, var10003, Minecraft.thePlayer.posZ);
      double var2 = var0.yCoord - var1.yCoord;
      double var4 = var0.xCoord - var1.xCoord;
      double var6 = var0.zCoord - var1.zCoord;
      double var8 = Math.sqrt(var4 * var4 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var6, var4)) - 90.0F;
      float var11 = (float)(-Math.toDegrees(Math.atan2(var2, var8)));
      return new float[]{MathHelper.wrapAngleTo180_float(var10), MathHelper.wrapAngleTo180_float(var11)};
   }

   public static boolean isFacingEntity(EntityLivingBase var0) {
      float var1 = getNeededRotations(var0)[0];
      float var2 = getNeededRotations(var0)[1];
      ClientUtils.mc();
      float var3 = Minecraft.thePlayer.rotationYaw;
      ClientUtils.mc();
      float var4 = Minecraft.thePlayer.rotationPitch;
      float var5 = 21.0F + var0.getCollisionBorderSize();
      if (var3 < 0.0F) {
         var3 += 360.0F;
      }

      if (var4 < 0.0F) {
         var4 += 360.0F;
      }

      if (var1 < 0.0F) {
         var1 += 360.0F;
      }

      if (var2 < 0.0F) {
         var2 += 360.0F;
      }

      if (var3 >= var1 - var5 && var3 <= var1 + var5) {
         return var4 >= var2 - var5 && var4 <= var2 + var5;
      } else {
         return false;
      }
   }

   public SwitchPos(String var1, boolean var2, Module var3) {
      super(var1, var2, var3);
      targets = new ArrayList();
      this.timer = new Timer();
   }

   private List getTargets() {
      ArrayList var1 = new ArrayList();
      Iterator var3 = ClientUtils.loadedEntityList().iterator();

      while(var3.hasNext()) {
         Entity var2 = (Entity)var3.next();
         if (((AuraPos)this.getModule()).isEntityValid(var2)) {
            var1.add((EntityLivingBase)var2);
         }
      }

      var1.sort(new Comparator(this) {
         final SwitchPos this$0;

         public int compare(EntityLivingBase var1, EntityLivingBase var2) {
            return Math.round(var2.getHealth() - var1.getHealth());
         }

         {
            this.this$0 = var1;
         }

         public int compare(Object var1, Object var2) {
            return this.compare((EntityLivingBase)var1, (EntityLivingBase)var2);
         }
      });
      return var1;
   }

   public static float[] getNeededRotations(EntityLivingBase var0) {
      double var10000 = var0.posX;
      ClientUtils.mc();
      double var1 = var10000 - Minecraft.thePlayer.posX;
      var10000 = var0.posZ;
      ClientUtils.mc();
      double var3 = var10000 - Minecraft.thePlayer.posZ;
      var10000 = var0.posY + (double)var0.getEyeHeight();
      ClientUtils.mc();
      double var10001 = Minecraft.thePlayer.getEntityBoundingBox().minY;
      ClientUtils.mc();
      double var5 = var10000 - (var10001 + (double)Minecraft.thePlayer.getEyeHeight());
      double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3);
      float var9 = (float)(MathHelper.func_181159_b(var3, var1) * 180.0D / 3.141592653589793D) - 90.0F;
      float var10 = (float)(-(MathHelper.func_181159_b(var5, var7) * 180.0D / 3.141592653589793D));
      return new float[]{var9, var10};
   }

   static int[] $SWITCH_TABLE$my$NewSnake$event$Event$State() {
      int[] var10000 = $SWITCH_TABLE$my$NewSnake$event$Event$State;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[Event.State.values().length];

         try {
            var0[Event.State.POST.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[Event.State.PRE.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$my$NewSnake$event$Event$State = var0;
         return var0;
      }
   }

   private void incrementIndex() {
      ++this.index;
      if (this.index >= targets.size()) {
         this.index = 0;
      }

   }

   private static float getAngleDifference(float var0, float var1) {
      return ((var0 - var1) % 360.0F + 540.0F) % 360.0F - 180.0F;
   }

   public static double getRotationDifference(RotationUtils.Rotation var0) {
      return getRotationDifference(var0, serverRotation);
   }

   public static Vec3 getLocation(AxisAlignedBB var0) {
      double var1 = 0.5D + AuraPos.yawdiff / 2.0D;
      double var3 = 0.5D + AuraPos.pitchdiff / 2.0D;
      RotationUtils.VecRotation var5 = searchCenter(var0, true);
      return var5 != null ? var5.getVec() : new Vec3(var0.minX + (var0.maxX - var0.minX) * var1, var0.minY + (var0.maxY - var0.minY) * var3, var0.minZ + (var0.maxZ - var0.minZ) * var1);
   }

   @EventTarget
   public void onRender(Render3DEvent var1) {
      if (AuraPos.Circle) {
         this.drawCircle((Entity)targets.get(this.index), var1.getPartialTicks(), AuraPos.Tamanho);
      }

   }
}
