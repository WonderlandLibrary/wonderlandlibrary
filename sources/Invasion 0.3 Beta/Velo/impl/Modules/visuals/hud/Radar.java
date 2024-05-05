package Velo.impl.Modules.visuals.hud;

import java.awt.Color;
import java.util.Iterator;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

import Velo.api.Friend.FriendManager;
import Velo.api.Module.Module;
import Velo.api.Util.Other.Colors;
import Velo.api.Util.Render.RenderUtil;
import Velo.impl.Event.EventRender;
import Velo.impl.Settings.NumberSetting;

public class Radar extends Module {



   public Radar() {
		super("Radar", "Radar", 0, Category.VISUALS);
		this.loadSettings(X, Y, SCALE, SIZE);
	}



private boolean dragging;
   float hue;
   public static NumberSetting X = new NumberSetting("Pos X", 400, 1, 1920, 1), Y = new NumberSetting("Pos Y", 400, 1, 1080, 1)
		   , SCALE = new NumberSetting("Scale", 1, 1, 5, 1) , SIZE = new NumberSetting("Scale", 100, 50, 500, 1);
   

   
   @Override
	public void onRenderUpdate(EventRender event) {
	   int size = (int) SIZE.getValueFloat();
       float xOffset = X.getValueFloat();
       float yOffset = Y.getValueFloat();
       float playerOffsetX = (float)mc.thePlayer.posX;
       float playerOffSetZ = (float)mc.thePlayer.posZ;
       int var141 = event.sr.getScaledWidth();
       int var151 = event.sr.getScaledHeight();




       if (this.hue > 255.0F) {
          this.hue = 0.0F;
       }

       float h = this.hue;
       float h2 = this.hue + 85.0F;
       float h3 = this.hue + 170.0F;
       if (h > 255.0F) {
          h = 0.0F;
       }

       if (h2 > 255.0F) {
          h2 -= 255.0F;
       }

       if (h3 > 255.0F) {
          h3 -= 255.0F;
       }

       Color color33 = Color.getHSBColor(h / 255.0F, 0.9F, 1.0F);
       Color color332 = Color.getHSBColor(h2 / 255.0F, 0.9F, 1.0F);
       Color color333 = Color.getHSBColor(h3 / 255.0F, 0.9F, 1.0F);
       int color1 = color33.getRGB();
       int color2 = color332.getRGB();
       int color3 = color333.getRGB();
       this.hue = (float)((double)this.hue + 0.1D);
       RenderUtil.rectangleBordered((double)xOffset, (double)yOffset, (double)(xOffset + (float)size), (double)(yOffset + (float)size), 0.5D, Colors.getColor(90), Colors.getColor(0));
       RenderUtil.rectangleBordered((double)(xOffset + 1.0F), (double)(yOffset + 1.0F), (double)(xOffset + (float)size - 1.0F), (double)(yOffset + (float)size - 1.0F), 1.0D, Colors.getColor(90), Colors.getColor(61));
       RenderUtil.rectangleBordered((double)xOffset + 2.5D, (double)yOffset + 2.5D, (double)(xOffset + (float)size) - 2.5D, (double)(yOffset + (float)size) - 2.5D, 0.5D, Colors.getColor(61), Colors.getColor(0));
       RenderUtil.rectangleBordered((double)(xOffset + 3.0F), (double)(yOffset + 3.0F), (double)(xOffset + (float)size - 3.0F), (double)(yOffset + (float)size - 3.0F), 0.5D, Colors.getColor(27), Colors.getColor(61));
 //      RenderUtil.drawGradientSideways((double)(xOffset + 3.0F), (double)(yOffset + 3.0F), (double)(xOffset + (float)(size / 2)), (double)yOffset + 3.6D, color1, color2);
     
   //    Gui.drawRect((double)xOffset + ((double)(size / 2) - 0.5D) + 52, (double)yOffset + 3.5D, (double)xOffset + (double)(size / 2) + 0.5D - 52, (double)(yOffset + (float)size) - 3.5D, Colors.getColor(0, 0, 0, 155));
   //    RenderUtil.drawGradientSideways((double)(xOffset + (float)(size / 2)), (double)(yOffset + 3.0F), (double)(xOffset + (float)size - 3.0F), (double)yOffset + 3.6D, color2, color3);
      Gui.drawRect((double)xOffset + ((double)(size / 2) - 0.5D), (double)yOffset + 3.5D, (double)xOffset + (double)(size / 2) + 0.5D, (double)(yOffset + (float)size) - 3.5D, Colors.getColor(255, 80));
      Gui.drawRect((double)xOffset + 3.5D, (double)yOffset + ((double)(size / 2) - 0.5D), (double)(xOffset + (float)size) - 3.5D, (double)yOffset + (double)(size / 2) + 0.5D, Colors.getColor(255, 80));


      
      Iterator var21 = mc.theWorld.getLoadedEntityList().iterator();

       while(var21.hasNext()) {
          Object o = var21.next();
          if (o instanceof EntityPlayer) {
             EntityPlayer ent = (EntityPlayer)o;
             if (ent.isEntityAlive() && ent != mc.thePlayer && !ent.isInvisible() && !ent.isInvisibleToPlayer(mc.thePlayer)) {
                float pTicks = mc.timer.renderPartialTicks;
                float posX = (float)((ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * (double)pTicks - (double)playerOffsetX) * SCALE.getValue());
                float posZ = (float)((ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - (double)playerOffSetZ) * SCALE.getValue());
                int color;
                if (FriendManager.isFriend(ent.getName())) {
                   color = mc.thePlayer.canEntityBeSeen(ent) ? Colors.getColor(0, 195, 255) : Colors.getColor(0, 195, 255);
                } else {
                   color = mc.thePlayer.canEntityBeSeen(ent) ? new Color(255, 0, 0, 0).getRGB() : new Color(255, 255, 0, 255).getRGB();
                }

                float cos = (float)Math.cos((double)mc.thePlayer.rotationYaw * 0.017453292519943295D);
                float sin = (float)Math.sin((double)mc.thePlayer.rotationYaw * 0.017453292519943295D);
                float rotY = -(posZ * cos - posX * sin);
                float rotX = -(posX * cos + posZ * sin);
                if (rotY > (float)(size / 2 - 5)) {
                   rotY = (float)(size / 2) - 5.0F;
                } else if (rotY < (float)(-(size / 2 - 5))) {
                   rotY = (float)(-(size / 2 - 5));
                }

                if (rotX > (float)(size / 2) - 5.0F) {
                   rotX = (float)(size / 2 - 5);
                } else if (rotX < (float)(-(size / 2 - 5))) {
                   rotX = -((float)(size / 2) - 5.0F);
                }

                RenderUtil.rectangleBordered((double)(xOffset + (float)(size / 2) + rotX) - 1.5D, (double)(yOffset + (float)(size / 2) + rotY) - 1.0D, (double)(xOffset + (float)(size / 2) + rotX) + 1.5D, (double)(yOffset + (float)(size / 2) + rotY) + 1.0D, 0.5D, color, Colors.getColor(2));
             }
          }
       }
   


 
		super.onRenderUpdate(event);
	}



}
