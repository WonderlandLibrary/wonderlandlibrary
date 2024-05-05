
package Reality.Realii.mods.modules.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import static Reality.Realii.utils.cheats.player.Helper.mc;

import java.awt.Color;
import java.sql.Date;

import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.misc.EventChat;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.value.Mode;
import Reality.Realii.guis.font.FontLoaders;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;
import Reality.Realii.mods.modules.combat.Killaura;
import Reality.Realii.mods.modules.combat.Velocity;
import Reality.Realii.mods.modules.world.Scaffold;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.utils.render.RenderUtil;

public class SesionInfo
        extends Module {
	
	  public static Mode Color23 = new Mode("ColorMode", "ColorMode", new String[]{"Client","Rainbow"}, "Client");
	    private int kills;
	    private int wins;
	    double width;
	    double height;
	    private EntityPlayer target;
	    int rainbowTick = 0;
	    

    public SesionInfo() {
        super("Sesion Info", ModuleType.Render);
        addValues(Color23);
    }
   
    @EventHandler
    public void onChat(EventChat c) {
    	 if (c.getMessage().contains("You won!")){
    		 ++this.wins;
    	 }
    		
    	 
    	
           
              String Ez = "was killed by " + mc.thePlayer.getName();
         
              String Ez2 = "was slain by " + mc.thePlayer.getName();
              String Ez3 = "was thrown to the void by " + mc.thePlayer.getName();
              String Ez4 = "was killed with magic while fighting " + mc.thePlayer.getName();
              String Ez5 = "couldn't fly while escaping " + mc.thePlayer.getName();
              String Ez6 = "fell to their death while escaping " + mc.thePlayer.getName();
              String Ez7 = "You killed";
              
              if (c.getMessage().contains(Ez) || c.getMessage().contains(Ez2)  || c.getMessage().contains(Ez3)  || c.getMessage().contains(Ez4)  || c.getMessage().contains(Ez5)  || c.getMessage().contains(Ez6) || c.getMessage().contains(Ez7)) {
                  ++this.kills;
              }
          }
    
    @EventHandler
    public void onMove(EventMove e) {
    	  if (Killaura.target instanceof EntityPlayer) {
              this.target = (EntityPlayer)Killaura.target;
          }
    }
      
    

    @EventHandler
    public void renderHud(EventRender2D event) {

  	  final ScaledResolution sr = new ScaledResolution(mc);
  	  float x1 = sr.getScaledWidth(), y1 = 0;
        float arrayListY = y1;
    	Color c2 = new Color(0,0,0,150);
    	 if (Color23.getModeAsString().equals("Client")) {
    		 int i3 = 0;
      		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
      		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
        	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
        	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i3 * 20), colors.getFirst(), colors.getSecond(), false);
           //	 RenderUtil.drawRect(4, 88, 160, 85, c.getRGB());
			 RenderUtil.drawBorderedRect(4, 28, 160, 85,3, c.getRGB(), c2.getRGB());
           	 
           	 }
           	 
           	 if (Color23.getModeAsString().equals("Rainbow")) {
           	
                 	Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                  
               	 RenderUtil.drawRect(4, 28, 160, 25, c.getRGB());
              
                	 }
           	 
    	 //RenderUtil.drawBorderedRect(4, 28, 160, 85,2, c2.getRGB(), c2.getRGB());
    	 String serverIp;
    	 serverIp = mc.getCurrentServerData() != null ? mc.getCurrentServerData().serverIP : "Singleplayer";
    	FontLoaders.product18.drawStringWithShadow("Username: " + mc.thePlayer.getName()  , 9, 33,  new Color(255, 255, 255).getRGB());
    	FontLoaders.product18.drawStringWithShadow("Play Time: " + "nigga" , 9, 43,  new Color(255, 255, 255).getRGB());
    	FontLoaders.product18.drawStringWithShadow("Kills: " + kills , 9, 53,  new Color(255, 255, 255).getRGB());
    	FontLoaders.product18.drawStringWithShadow("Wins: " + wins , 9, 63,  new Color(255, 255, 255).getRGB());
    	FontLoaders.product18.drawStringWithShadow("Server: " + serverIp , 9, 73,  new Color(255, 255, 255).getRGB());
    	 
        
    
    }
    
    
   
    
}


    
    


