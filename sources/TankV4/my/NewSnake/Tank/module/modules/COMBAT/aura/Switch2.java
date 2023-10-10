package my.NewSnake.Tank.module.modules.COMBAT.aura;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import my.NewSnake.Tank.Ui.font.RotationUtils;
import my.NewSnake.Tank.module.Module;
import my.NewSnake.Tank.module.modules.COMBAT.Aura;
import my.NewSnake.event.Event;
import my.NewSnake.event.EventTarget;
import my.NewSnake.event.events.Render3DEvent;
import my.NewSnake.event.events.UpdateEvent;
import my.NewSnake.utils.ClientUtils;
import my.NewSnake.utils.GLUtil;
import my.NewSnake.utils.StateManager;
import my.NewSnake.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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

public class Switch2 extends AuraMode {
   private static volatile int[] $SWITCH_TABLE$my$NewSnake$event$Event$State;
   private int index;
   public static List targets;
   public static RotationUtils.Rotation serverRotation = new RotationUtils.Rotation(0.0F, 0.0F);
   public static boolean Move = true;
   private boolean switchingTargets;
   private Timer timer;

   private List getTargets() {
      ArrayList var1 = new ArrayList();
      Iterator var3 = ClientUtils.loadedEntityList().iterator();

      while(var3.hasNext()) {
         Entity var2 = (Entity)var3.next();
         if (((Aura)this.getModule()).isEntityValid(var2)) {
            var1.add((EntityLivingBase)var2);
         }
      }

      var1.sort(new Comparator(this) {
         final Switch2 this$0;

         {
            this.this$0 = var1;
         }

         public int compare(Object var1, Object var2) {
            return this.compare((EntityLivingBase)var1, (EntityLivingBase)var2);
         }

         public int compare(EntityLivingBase var1, EntityLivingBase var2) {
            return Math.round(var2.getHealth() - var1.getHealth());
         }
      });
      return var1;
   }

   public static void color(int var0) {
      GL11.glColor4f((float)(var0 >> 16 & 255) / 255.0F, (float)(var0 >> 8 & 255) / 255.0F, (float)(var0 & 255) / 255.0F, (float)(var0 >> 24 & 255) / 255.0F);
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB var0) {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      var2.begin(3, DefaultVertexFormats.POSITION);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(3, DefaultVertexFormats.POSITION);
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(1, DefaultVertexFormats.POSITION);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var1.draw();
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

   private void incrementIndex() {
      ++this.index;
      if (this.index >= targets.size()) {
         this.index = 0;
      }

   }

   public static void disable3D() {
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
   }

   public static Vec3 getVectorForRotation(RotationUtils.Rotation var0) {
      float var1 = (float)Math.cos((double)(-var0.getYaw() * 0.017453292F - 3.1415927F));
      float var2 = (float)Math.sin((double)(-var0.getYaw() * 0.017453292F - 3.1415927F));
      float var3 = (float)(-Math.cos((double)(-var0.getPitch() * 0.017453292F)));
      float var4 = (float)Math.sin((double)(-var0.getPitch() * 0.017453292F));
      return new Vec3((double)(var2 * var3), (double)var4, (double)(var1 * var3));
   }

   private void lambda$0(Render3DEvent var1, EntityLivingBase var2) {
      ClientUtils.mc();
      EntityPlayerSP var10002 = Minecraft.thePlayer;
   }

   @EventTarget
   public final void VAPO(Render3DEvent var1) {
      if (Aura.Circulo) {
         EntityLivingBase var2 = (EntityLivingBase)targets.get(0);
         this.drawCircle(var2, var1.getPartialTicks(), Aura.Radius);
      }

   }

   public static double getRotationDifference(RotationUtils.Rotation var0, RotationUtils.Rotation var1) {
      return Math.hypot((double)getAngleDifference(var0.getYaw(), var1.getYaw()), (double)(var0.getPitch() - var1.getPitch()));
   }

   public static int getRainbow(int var0, int var1, float var2) {
      float var3 = (float)((System.currentTimeMillis() + (long)var1) % (long)var0);
      var3 /= (float)var0;
      return Color.getHSBColor(var3, var2, 1.0F).getRGB();
   }

   public Switch2(String var1, boolean var2, Module var3) {
      super(var1, var2, var3);
      targets = new ArrayList();
      this.timer = new Timer();
   }

   private void drawCircle(Entity var1, float var2, double var3) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glLineWidth(1.0F);
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
      double var14 = 6.283185307179586D;

      for(int var16 = 0; var16 <= 90; ++var16) {
         GL11.glColor3f(var11, var12, var13);
         GL11.glVertex3d(var5 + var3 * Math.cos((double)var16 * 6.283185307179586D / 45.0D), var7, var9 + var3 * Math.sin((double)var16 * 6.283185307179586D / 45.0D));
      }

      GL11.glEnd();
      GL11.glDepthMask(true);
      GL11.glEnable(2929);
      GL11.glEnable(3553);
      GL11.glPopMatrix();
   }

   public static void drawBoundingBox(AxisAlignedBB var0) {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var1.draw();
      var2.begin(7, DefaultVertexFormats.POSITION_TEX);
      var2.pos(var0.minX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.maxZ).endVertex();
      var2.pos(var0.minX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.minX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.minZ).endVertex();
      var2.pos(var0.maxX, var0.maxY, var0.maxZ).endVertex();
      var2.pos(var0.maxX, var0.minY, var0.maxZ).endVertex();
      var1.draw();
   }

   public static Vec3 getLocation(AxisAlignedBB var0) {
      double var1 = 0.5D + Aura.yawdiff / 2.0D;
      double var3 = 0.5D + Aura.pitchdiff / 2.0D;
      RotationUtils.VecRotation var5 = searchCenter(var0, true);
      return var5 != null ? var5.getVec() : new Vec3(var0.minX + (var0.maxX - var0.minX) * var1, var0.minY + (var0.maxY - var0.minY) * var3, var0.minZ + (var0.maxZ - var0.minZ) * var1);
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

   public boolean onUpdate(UpdateEvent var1) {
      if (super.onUpdate(var1)) {
         Aura var2;
         EntityLivingBase var3;
         switch($SWITCH_TABLE$my$NewSnake$event$Event$State()[var1.getState().ordinal()]) {
         case 1:
            StateManager.setOffsetLastPacketAura(false);
            var2 = (Aura)this.getModule();
            targets = this.getTargets();
            if (this.index >= targets.size()) {
               this.index = 0;
            }

            if (targets.size() > 0) {
               var3 = (EntityLivingBase)targets.get(this.index);
               if (var3 != null) {
                  if (var2.AutoBlock && ClientUtils.player().getCurrentEquippedItem() != null && ClientUtils.player().getCurrentEquippedItem().getItem() instanceof ItemSword) {
                     ClientUtils.playerController().sendUseItem(ClientUtils.player(), ClientUtils.world(), ClientUtils.player().getCurrentEquippedItem());
                  }

                  float[] var4;
                  if (Aura.Normal) {
                     var4 = my.NewSnake.utils.RotationUtils.getRotations(var3);
                     var1.setYaw(var4[0]);
                     var1.setPitch(var4[1]);
                  }

                  if (Aura.Rotaçao) {
                     Minecraft.getMinecraft();
                     Minecraft.thePlayer.rotationYawHead = var1.getYaw();
                     Minecraft.getMinecraft();
                     Minecraft.thePlayer.renderYawOffset = var1.getYaw();
                  }

                  if (Aura.Bypass) {
                     var4 = getCustomRotation(getLocation(((EntityLivingBase)targets.get(this.index)).getEntityBoundingBox()));
                     var1.setYaw(var4[0]);
                     var1.setPitch(var4[1]);
                  }
               }

               if (Aura.SetupTick) {
                  targets.size();
                  if (this.timer.delay(300.0F)) {
                     this.incrementIndex();
                     this.switchingTargets = true;
                     this.timer.reset();
                  }
               } else {
                  targets.size();
               }
            }

            Aura.SetupTick = !Aura.SetupTick;
            break;
         case 2:
            var2 = (Aura)this.getModule();
            if (Aura.SetupTick && targets.size() > 0 && targets.get(this.index) != null && targets.size() > 0 && targets.size() > 0) {
               var3 = (EntityLivingBase)targets.get(this.index);
               if (ClientUtils.player().isBlocking() || this != false) {
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

   @EventTarget
   public final void VAPO2(Render3DEvent var1) {
      if (Aura.ESPTarget) {
         targets.forEach(this::lambda$0);
      }

      EntityLivingBase var2 = (EntityLivingBase)targets.get(0);
      if (Aura.ESPTarget && var2 != null) {
         double var3 = interpolate(var2.posX, var2.lastTickPosX, (double)var1.getPartialTicks());
         double var5 = interpolate(var2.posY, var2.lastTickPosY, (double)var1.getPartialTicks());
         double var7 = interpolate(var2.posZ, var2.lastTickPosZ, (double)var1.getPartialTicks());
         ClientUtils.mc().getRenderManager();
         double var10001 = var3 - RenderManager.renderPosX;
         double var10002 = var5 + (double)var2.height + 0.1D - (double)var2.height;
         ClientUtils.mc().getRenderManager();
         var10002 -= RenderManager.renderPosY;
         ClientUtils.mc().getRenderManager();
         this.drawEntityESP(var10001, var10002, var7 - RenderManager.renderPosZ, (double)var2.height, 0.65D, new Color(var2.hurtTime > 0 ? 14890790 : getRainbow(4000, 0, 0.85F)));
      }

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

   private void drawEntityESP(double var1, double var3, double var5, double var7, double var9, Color var11) {
      GL11.glPushMatrix();
      GLUtil.setGLCap(3042, true);
      GLUtil.setGLCap(3553, false);
      GLUtil.setGLCap(2896, false);
      GLUtil.setGLCap(2929, false);
      GL11.glDepthMask(false);
      GL11.glLineWidth(1.8F);
      GL11.glBlendFunc(770, 771);
      GLUtil.setGLCap(2848, true);
      GL11.glDepthMask(true);
      BB(new AxisAlignedBB(var1 - var9 + 0.25D, var3, var5 - var9 + 0.25D, var1 + var9 - 0.25D, var3 + var7, var5 + var9 - 0.25D), (new Color(var11.getRed(), var11.getGreen(), var11.getBlue(), 120)).getRGB());
      OutlinedBB(new AxisAlignedBB(var1 - var9 + 0.25D, var3, var5 - var9 + 0.25D, var1 + var9 - 0.25D, var3 + var7, var5 + var9 - 0.25D), 1.0F, var11.getRGB());
      GLUtil.revertAllCaps();
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static boolean hasMotion() {
      ClientUtils.mc();
      if (Minecraft.thePlayer.motionX != 0.0D) {
         ClientUtils.mc();
         if (Minecraft.thePlayer.motionZ != 0.0D) {
            ClientUtils.mc();
            if (Minecraft.thePlayer.motionY != 0.0D) {
               return true;
            }
         }
      }

      return false;
   }

   public static double interpolate(double var0, double var2, double var4) {
      return var2 + (var0 - var2) * var4;
   }

   public static double getRotationDifference(RotationUtils.Rotation var0) {
      return getRotationDifference(var0, serverRotation);
   }

   public static void BB(AxisAlignedBB var0, int var1) {
      enable3D();
      color(var1);
      drawBoundingBox(var0);
      disable3D();
   }

   public static void enable3D() {
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
   }

   private static float getAngleDifference(float var0, float var1) {
      return ((var0 - var1) % 360.0F + 540.0F) % 360.0F - 180.0F;
   }

   public static void OutlinedBB(AxisAlignedBB var0, float var1, int var2) {
      enable3D();
      GL11.glLineWidth(var1);
      color(var2);
      drawOutlinedBoundingBox(var0);
      disable3D();
   }
}
