package Reality.Realii.mods.modules.render;


import libraries.javax.vecmath.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.awt.Color;

import javax.swing.text.html.parser.Entity;

import org.lwjgl.opengl.GL11;

import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.math.Vec4f;
import Reality.Realii.utils.render.RenderUtil;


public class Chinahat
extends Module {
	  private Numbers<Number> Possitiony = new Numbers<>("Y", 2.2, 0.5, 3.5, 0.1);
	  private Numbers<Number> size = new Numbers<>("You moms pussy size", 1, 0, 2, 0.1);
	  private Numbers<Number> Colothing = new Numbers<>("Color Delay", 200, 50, 500, 1);
	  public static Mode Color23 = new Mode("ColorMode", "ColorMode", new String[]{"Client","Rainbow"}, "Client");
	  int rainbowTick = 0;
    public Chinahat(){
        super("ChinaHat", ModuleType.Render);
        addValues(size,Possitiony,Colothing,Color23);
    }
    
    
   
    
    @EventHandler
    public void onRender2(EventRender3D eventRender) {
    	  if (mc.gameSettings.thirdPersonView != 0) {
    	  GL11.glPushMatrix();
          GL11.glDisable((int) 3553);
          RenderUtil.startSmooth();
          GL11.glDisable((int) 2929);
          GL11.glDepthMask((boolean) false);
          GL11.glLineWidth((float) 2);
          GL11.glBegin((int) 3);
          float x = (float) ((float)(mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * eventRender.getPartialTicks()) - mc.getRenderManager().renderPosX);
  		  float y = (float) (((float)(mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * eventRender.getPartialTicks()) + 1.8) - mc.getRenderManager().renderPosY);
          float z = (float) ((float)(mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * eventRender.getPartialTicks()) - mc.getRenderManager().renderPosZ);
          double pix2 = Math.PI * 2;
          int tick = 0;
          
          for (int i = 0; i <= 45; ++i) {
        	  int i2 = 0;
        	  
	      		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
	      		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
	        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
	        	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i2 * 20), colors.getFirst(), colors.getSecond(), false);
              GL11.glColor3f(c.getRed() / 255f, (float) c.getGreen() / 255f, (float) c.getBlue() / 255f);
              GL11.glVertex3d((double) (x + size.getValue().doubleValue() * Math.cos((double) i * (Math.PI * 2) / 45.0)), (double) y, (double) (z + size.getValue().doubleValue() * Math.sin((double) i * (Math.PI * 2) / 45.0)));
              tick += 1;
          }
          GL11.glEnd();
          GL11.glDepthMask((boolean) true);
          GL11.glEnable((int) 2929);
          RenderUtil.endSmooth();
          GL11.glEnable((int) 3553);
          GL11.glPopMatrix();
    	  }
    }
    
    @EventHandler
    public void onRender(EventRender3D eventRender) {
    	
    	  if (mc.gameSettings.thirdPersonView != 0 ) {
    		  
    		  float x = (float) ((float)(mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * eventRender.getPartialTicks()) - mc.getRenderManager().renderPosX);
    		  float y = (float) (((float)(mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * eventRender.getPartialTicks()) + 2.1) - mc.getRenderManager().renderPosY);
              float z = (float) ((float)(mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * eventRender.getPartialTicks()) - mc.getRenderManager().renderPosZ);
              
              GL11.glPushMatrix();
              GL11.glEnable(GL11.GL_BLEND);
              GL11.glDisable(GL11.GL_TEXTURE_2D);
              GL11.glEnable(GL11.GL_LINE_SMOOTH);
              GL11.glDepthMask(false);
              GL11.glLineWidth(0.6F);
              GL11.glBegin(1);
              Color color = null;
          	  final ScaledResolution sr = new ScaledResolution(mc);
          	  float x1 = sr.getScaledWidth(), y1 = 0;
                float arrayListY = y1;
            
             
              for (double i = 0; i <= 360; i = i + 0.1) {
            	    if (Color23.getModeAsString().equals("Client")) {
            	    	   int i3 = 0;
            	      		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
            	      		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
            	        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
            	        	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i3 * 20), colors.getFirst(), colors.getSecond(), false);
                        GL11.glColor4f(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, 0.2F);
                   	 }
                   	 
                   	 if (Color23.getModeAsString().equals("Rainbow")) {
                   	
                         	Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                            GL11.glColor4f(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, 0.2F);
                      
                      
                        	 }
                   	if(mc.gameSettings.keyBindSneak.isKeyDown()) {
                   		
                   		GL11.glVertex3d(x + Math.sin(i * Math.PI / 180.0D) * size.getValue().doubleValue(), y - 0.3D , z + Math.cos(i * Math.PI / 180.0D) * size.getValue().doubleValue());
          		  } else {
          			
          			GL11.glVertex3d(x + Math.sin(i * Math.PI / 180.0D) * size.getValue().doubleValue(), y - 0.3D , z + Math.cos(i * Math.PI / 180.0D) * size.getValue().doubleValue());
            	   
          		  }
                   	
                    GL11.glVertex3d(x, y + 0.09D, z);
            	}
              
              GL11.glEnd();
              GL11.glDepthMask(true);
              GL11.glDisable(GL11.GL_LINE_SMOOTH);
              GL11.glEnable(GL11.GL_TEXTURE_2D);
              GL11.glDisable(GL11.GL_BLEND);
              GL11.glPopMatrix();
              GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

    	  }
    }
    
   
   
    
    

   
}