
package Reality.Realii.mods.modules.render;


import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;

import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.render.RenderUtil;

import java.util.ArrayList;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import java.awt.*;


public class MotionGraph
        extends Module {
	  private Numbers<Number> width = new Numbers<>("Width", 180, 100, 300, 1);
	  private Numbers<Number> height = new Numbers<>("Hehigh",5, 1, 20, 1);
	  private Numbers<Number> y = new Numbers<>("Y pos", -210, -500, 500, 1);
	  public static Mode Color23 = new Mode("ColorMode", "ColorMode", new String[]{"Client","Rainbow"}, "Client");
	  private final ArrayList<Float> speeds = new ArrayList<>();
	  private double Speed;
	  private float BiggerSpeed;
	  int rainbowTick = 0;
	
    public MotionGraph() {
        super("MotionGraph", ModuleType.Render);
        addValues(width,height,y,Color23);
    }
    

    @EventHandler
    public void onUpdateAlwaysInGui() {
        if (BiggerSpeed != 100) {
            synchronized (speeds) {
                speeds.clear();
                Speed = 0;
            }
        }

        BiggerSpeed = 100;
    }
    public double getSpeed() {
        return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }
    
    @EventHandler
    public void OnUpdate(EventPreUpdate e) {
    	   if (speeds.size() > 100 - 2) {
               speeds.remove(0);
           }

           speeds.add((float) getSpeed() * mc.timer.timerSpeed);

           Speed = -1;
           for (final float f : speeds) {
               if (f > Speed) {
                   Speed = f;
               }
           }
    }
    public void color(final double red, final double green, final double blue, final double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }
    public void color(final double red, final double green, final double blue) {
        color(red, green, blue, 1);
    }

    public void color(Color color) {
        if (color == null)
            color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
    }

    public void color(Color color, final int alpha) {
        if (color == null)
            color = Color.white;
        color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 0.5);
    }
    
    @EventHandler
    public void onRender(EventRender2D event) {
    	
    	 
    	
    	  final ScaledResolution sr = new ScaledResolution(mc);
    	  float x1 = sr.getScaledWidth(), y1 = 0;
          float arrayListY = y1;
    	  double bps = (Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * mc.timer.timerSpeed) * 20;
    	//  RenderUtil.drawRect(140, 120, 250, 180, new Color (000,000,000,100).getRGB());
    	  if (Color23.getModeAsString().equals("Client")) {
    		  int i3 = 0;
	      		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
	      		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
	        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
	        	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i3 * 20), colors.getFirst(), colors.getSecond(), false);
         	//RenderUtil.drawRect(140, 120, 250, 117, c.getRGB());
              RenderUtil.drawBorderedRect(7, 120, 170, 180,3, c.getRGB(),new Color (0,0,0,100).getRGB());
          	 }
          	 
          	 if (Color23.getModeAsString().equals("Rainbow")) {
          	
                	Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                 //	RenderUtil.drawRect(140, 120, 250, 117, c.getRGB());
               //  RenderUtil.drawRect(140, 120, 250, 180, new Color (000,000,000,100).getRGB());
                 RenderUtil.drawBorderedRect(140, 120, 250, 180,3, c.getRGB(),new Color (0,0,0,100).getRGB());
             
               	 }
     
     
      	
          GL11.glPushMatrix();
          GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
          GL11.glEnable(GL11.GL_BLEND);
          GL11.glEnable(GL11.GL_LINE_SMOOTH);
          GL11.glLineWidth(2);
          GL11.glDisable(GL11.GL_TEXTURE_2D);
          GL11.glDisable(GL11.GL_DEPTH_TEST);
          GL11.glDepthMask(false);
          GL11.glBegin(GL11.GL_LINES);

          if (speeds.size() > 3) {
              final float width = (float) (sr.getScaledWidth() / 2f - this.width.getValue().intValue() / 0.3f);
              //1
              
              for (int i = 1; i < speeds.size() - 1; i++) {
            	 
            	
                  
                  final float y = (float) (speeds.get(i) * 10 * height.getValue().intValue());
                  final float y2 = (float) (speeds.get(i + 1) * 10 * height.getValue().intValue());
                  final float length = (float) ((this.width.getValue().intValue() / (speeds.size() - 1)) * 1.52f);


                  GL11.glVertex2f(width + (i * length), (float) (sr.getScaledHeight() / 2F - Math.min(y, 50) - this.y.getValue().intValue()));
                  GL11.glVertex2f(width + ((i + 1) * length), (float) (sr.getScaledHeight() / 2F - Math.min(y2, 50) - this.y.getValue().intValue()));
              }
          }
          GL11.glEnd();

          GL11.glEnable(GL11.GL_TEXTURE_2D);
          GL11.glDisable(GL11.GL_LINE_SMOOTH);
          GL11.glEnable(GL11.GL_DEPTH_TEST);
          GL11.glDepthMask(true);
          GL11.glDisable(GL11.GL_BLEND);

          GlStateManager.resetColor();
          GL11.glPopMatrix();
     
    
    }
    
    
    
    
}

