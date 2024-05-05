package team.massacre.impl.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import team.massacre.Massacre;
import team.massacre.api.event.api.annotations.Handler;
import team.massacre.api.module.Category;
import team.massacre.api.module.Module;
import team.massacre.api.property.Property;
import team.massacre.api.property.Representation;
import team.massacre.api.property.impl.DoubleProperty;
import team.massacre.impl.event.EventRender3D;
import team.massacre.impl.event.EventUpdate;
import team.massacre.impl.module.movement.Flight;
import team.massacre.impl.module.movement.Speed;
import team.massacre.utils.GuiUtils;

public final class TargetStrafe extends Module {
   public final DoubleProperty radius;
   public final Property<Boolean> holdspace;
   private final Property<Boolean> render;
   private final List<Vector3f> points;
   public byte direction;

   public TargetStrafe() {
      super("Target Strafe", 0, Category.MOVEMENT);
      this.radius = new DoubleProperty("Radius", 2.0D, 0.1D, 4.0D, 0.1D, Representation.DOUBLE);
      this.holdspace = new Property("Hold Space", true);
      this.render = new Property("Render", true);
      this.points = new ArrayList();
      this.addValues(new Property[]{this.radius, this.holdspace, this.render});
   }

   @Handler
   public void onMotionUpdate(EventUpdate event) {
      if (event.isPre()) {
         if (this.mc.thePlayer.isCollidedHorizontally) {
            this.direction = (byte)(-this.direction);
            return;
         }

         if (this.mc.gameSettings.keyBindLeft.isKeyDown()) {
            this.direction = 1;
            return;
         }

         if (this.mc.gameSettings.keyBindRight.isKeyDown()) {
            this.direction = -1;
         }
      }

   }

   public static void drawLinesAroundPlayer(Entity entity, RenderManager renderManager, double radius, float partialTicks, int points, float width, int color) {
      GL11.glPushMatrix();
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glDisable(2929);
      GL11.glLineWidth(width);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(2929);
      GL11.glBegin(3);
      double x = GuiUtils.interpolate(entity.prevPosX, entity.posX, partialTicks) - renderManager.viewerPosX;
      double y = GuiUtils.interpolate(entity.prevPosY, entity.posY, partialTicks) - renderManager.viewerPosY;
      double z = GuiUtils.interpolate(entity.prevPosZ, entity.posZ, partialTicks) - renderManager.viewerPosZ;
      GuiUtils.glColor(color);

      for(int i = 0; i <= points; ++i) {
         GL11.glVertex3d(x + radius * Math.cos((double)i * 3.141592653589793D * 2.0D / (double)points), y, z + radius * Math.sin((double)i * 3.141592653589793D * 2.0D / (double)points));
      }

      GL11.glEnd();
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glEnable(2929);
      GL11.glEnable(3553);
      GL11.glPopMatrix();
   }

   @Handler
   public void onRender3D(EventRender3D event) {
      KillAura2 killAura = (KillAura2)Massacre.INSTANCE.getModuleManager().getModule(KillAura2.class);
      Speed speed = (Speed)Massacre.INSTANCE.getModuleManager().getModule(Speed.class);
      Flight flight = (Flight)Massacre.INSTANCE.getModuleManager().getModule(Flight.class);
      Iterator var5 = this.mc.theWorld.getLoadedEntityList().iterator();

      while(var5.hasNext()) {
         Entity entity = (Entity)var5.next();
         boolean colorchange = speed.getState() || flight.getState();
         int color = false;
         int color;
         if (killAura.target == entity && colorchange && !(Boolean)this.holdspace.getValue()) {
            color = Color.green.getRGB();
         } else if (killAura.target == entity && colorchange && (Boolean)this.holdspace.getValue() && this.mc.gameSettings.keyBindJump.isKeyDown()) {
            color = Color.green.getRGB();
         } else {
            color = Color.white.getRGB();
         }

         if ((Boolean)this.render.getValue() && entity != null && entity instanceof EntityPlayer && entity != this.mc.thePlayer && killAura.target == entity) {
            drawLinesAroundPlayer(entity, this.mc.getRenderManager(), (Double)this.radius.getValue(), event.getPartialTicks(), 12, 3.0F, Color.black.getRGB());
            drawLinesAroundPlayer(entity, this.mc.getRenderManager(), (Double)this.radius.getValue(), event.getPartialTicks(), 12, 2.0F, color);
         }
      }

   }
}
