package cn.hanabi.modules.modules.render;

import cn.hanabi.Hanabi;
import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventRender2D;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.Colors;
import cn.hanabi.utils.Opacity;
import cn.hanabi.utils.RenderUtil;
import cn.hanabi.utils.rotation.RotationUtil;
import cn.hanabi.utils.waypoints.Waypoint;
import cn.hanabi.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Waypoints extends Mod {
   public static Map waypointMap = new HashMap();
   public static Value radius = new Value("WayPoints", "Ranius", 30.0, 10.0, 500.0, 10.0);
   private final Opacity opacity = new Opacity(0);
   public boolean forward = true;
   public Value arrows = new Value("WayPoints", "Arrows", true);
   private double gradualFOVModifier;

   public Waypoints() {
      super("WayPoints", Category.RENDER);
   }

   @EventTarget
   public void onRender3D(EventRender eventRender3D) {
      this.updatePositions();
   }

   @EventTarget
   public void onRenderGui(EventRender2D eventRenderGui) {
      if (mc.getCurrentServerData() != null) {
         GlStateManager.pushMatrix();
         ScaledResolution scaledRes = new ScaledResolution(mc);
         float radiusHeight = (float)(scaledRes.getScaledHeight() / 4);
         float radiusWidth = (float)(scaledRes.getScaledWidth() / 4);
         float w = (float)(scaledRes.getScaledWidth() / 2);
         float h = (float)(scaledRes.getScaledHeight() / 2);
         Iterator var7 = waypointMap.keySet().iterator();

         while(true) {
            while(var7.hasNext()) {
               Waypoint waypoint = (Waypoint)var7.next();
               double[] renderPositions = (double[])waypointMap.get(waypoint);
               if (Objects.equals(waypoint.getAddress(), mc.getCurrentServerData().serverIP) && this.isInView(renderPositions[0] / (double)scaledRes.getScaleFactor(), renderPositions[1] / (double)scaledRes.getScaleFactor(), scaledRes, waypoint)) {
                  GlStateManager.pushMatrix();
                  String str = "§l" + waypoint.getName() + " §a" + (int)mc.thePlayer.getDistance(waypoint.getVec3().xCoord, waypoint.getVec3().yCoord, waypoint.getVec3().zCoord) + "m";
                  FontRenderer font = mc.fontRendererObj;
                  GlStateManager.translate(renderPositions[0] / (double)scaledRes.getScaleFactor(), renderPositions[1] / (double)scaledRes.getScaleFactor(), 0.0);
                  this.scale();
                  GlStateManager.translate(0.0, -2.5, 0.0);
                  float strWidth = (float)font.getStringWidth(str);
                  RenderUtil.rectangleBordered((double)(-strWidth / 2.0F - 3.0F), -12.0, (double)(strWidth / 2.0F + 3.0F), 1.0, 0.5, Colors.getColor(0, 100), waypoint.getColor());
                  GlStateManager.color(1.0F, 1.0F, 1.0F);
                  font.drawStringWithShadow(str, -strWidth / 2.0F, -9.5F, -1);
                  GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                  RenderUtil.drawCircle(3.0F, 0.0F, 4.0F, 3, waypoint.getColor());
                  RenderUtil.drawCircle(3.0F, 0.0F, 3.0F, 3, waypoint.getColor());
                  RenderUtil.drawCircle(3.0F, 0.0F, 2.0F, 3, waypoint.getColor());
                  RenderUtil.drawCircle(3.0F, 0.0F, 1.0F, 3, waypoint.getColor());
                  GlStateManager.popMatrix();
               } else if ((Boolean)this.arrows.getValue() && Objects.equals(waypoint.getAddress(), mc.getCurrentServerData().serverIP)) {
                  float angle = this.findAngle((double)w, renderPositions[0] / (double)scaledRes.getScaleFactor(), (double)h, renderPositions[1] / (double)scaledRes.getScaleFactor()) + (float)(renderPositions[2] > 1.0 ? 180 : 0);
                  int radiusXD = ((Double)radius.getValue()).intValue();
                  double x = (double)radiusXD * Math.cos(Math.toRadians((double)angle));
                  double y = (double)radiusXD * Math.sin(Math.toRadians((double)angle));
                  GlStateManager.pushMatrix();
                  GlStateManager.translate(x + (double)(scaledRes.getScaledWidth() / 2), y + (double)(scaledRes.getScaledHeight() / 2), 0.0);
                  GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
                  GlStateManager.scale(1.5F, 1.0F, 1.0F);
                  if (this.forward && this.opacity.getOpacity() >= 300.0F) {
                     this.forward = false;
                  } else if (!this.forward && this.opacity.getOpacity() <= 25.0F) {
                     this.forward = true;
                  }

                  this.opacity.interp(this.forward ? 300.0F : 25.0F, 3);
                  int alpha = (int)this.opacity.getOpacity();
                  if (alpha > 255) {
                     alpha = 255;
                  } else if (alpha < 0) {
                     alpha = 0;
                  }

                  int f1 = waypoint.getColor() >> 16 & 255;
                  int f2 = waypoint.getColor() >> 8 & 255;
                  int f3 = waypoint.getColor() & 255;
                  int color = Colors.getColor(f1, f2, f3, alpha);
                  RenderUtil.drawCircle(0.0F, 0.0F, 6.0F, 3, Colors.getColor(0, alpha));
                  RenderUtil.drawCircle(0.0F, 0.0F, 5.0F, 3, color);
                  RenderUtil.drawCircle(0.0F, 0.0F, 4.0F, 3, color);
                  RenderUtil.drawCircle(0.0F, 0.0F, 3.0F, 3, color);
                  RenderUtil.drawCircle(0.0F, 0.0F, 2.0F, 3, color);
                  RenderUtil.drawCircle(0.0F, 0.0F, 1.0F, 3, color);
                  RenderUtil.drawCircle(0.0F, 0.0F, 0.0F, 3, color);
                  GlStateManager.popMatrix();
               }
            }

            GlStateManager.popMatrix();
            return;
         }
      }
   }

   private void updatePositions() {
      waypointMap.clear();
      Iterator var1 = Hanabi.INSTANCE.waypointManager.getWaypoints().iterator();

      while(var1.hasNext()) {
         Waypoint waypoint = (Waypoint)var1.next();
         double x = waypoint.getVec3().xCoord - mc.getRenderManager().viewerPosX;
         double y = waypoint.getVec3().yCoord - mc.getRenderManager().viewerPosY;
         double z = waypoint.getVec3().zCoord - mc.getRenderManager().viewerPosZ;
         y += 0.2;
         waypointMap.put(waypoint, this.convertTo2D(x, y, z));
      }

   }

   private double[] convertTo2D(double x, double y, double z, Waypoint waypoint) {
      return this.convertTo2D(x, y, z);
   }

   private void scale() {
      float scale = 1.0F;
      float target = scale * (mc.gameSettings.fovSetting / (mc.gameSettings.fovSetting * mc.thePlayer.getFovModifier()));
      if (this.gradualFOVModifier == 0.0 || Double.isNaN(this.gradualFOVModifier)) {
         this.gradualFOVModifier = (double)target;
      }

      this.gradualFOVModifier += ((double)target - this.gradualFOVModifier) / ((double)Minecraft.getDebugFPS() * 0.7);
      scale = (float)((double)scale * this.gradualFOVModifier);
      scale *= 2.5F;
      GlStateManager.scale(scale, scale, scale);
   }

   private float findAngle(double x, double x2, double y, double y2) {
      ScaledResolution scaledRes = new ScaledResolution(mc);
      float a = (float)(scaledRes.getScaledHeight() - 25);
      float b = (float)(scaledRes.getScaledWidth() - 25);
      return (float)Math.toDegrees(Math.atan2(y2 - y, x2 - x));
   }

   private double[] convertTo2D(double x, double y, double z) {
      FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
      IntBuffer viewport = BufferUtils.createIntBuffer(16);
      FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
      FloatBuffer projection = BufferUtils.createFloatBuffer(16);
      GL11.glGetFloat(2982, modelView);
      GL11.glGetFloat(2983, projection);
      GL11.glGetInteger(2978, viewport);
      boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
      return result ? new double[]{(double)screenCoords.get(0), (double)((float)Display.getHeight() - screenCoords.get(1)), (double)screenCoords.get(2)} : null;
   }

   private boolean isInView(double x, double y, ScaledResolution resolution, Waypoint waypoint) {
      float angle = RotationUtil.getRotationFromPosition(waypoint.getVec3().xCoord, waypoint.getVec3().zCoord, waypoint.getVec3().yCoord - 1.6)[0];
      float angle2 = RotationUtil.getRotationFromPosition(waypoint.getVec3().xCoord, waypoint.getVec3().zCoord, waypoint.getVec3().yCoord - 1.6)[1];
      float cameraYaw = mc.getRenderViewEntity().rotationYaw + (float)(mc.gameSettings.thirdPersonView == 2 ? 180 : 0);
      return RotationUtil.getDistanceBetweenAngles(angle, RotationUtil.getNewAngle(cameraYaw)) < 90.0F && RotationUtil.getDistanceBetweenAngles(angle2, mc.thePlayer.rotationPitch) < 90.0F;
   }
}
