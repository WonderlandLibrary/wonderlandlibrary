
package Reality.Realii.mods.modules.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.server.MinecraftServer;
import Reality.Realii.utils.cheats.RenderUtills.Pair;
import Reality.Realii.utils.cheats.RenderUtills.Render2;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.render.RenderUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.entity.EntityPlayerSP;
import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.rendering.EventRender2D;
import Reality.Realii.event.events.rendering.EventRender3D;
import Reality.Realii.event.events.rendering.Shader3DEvent;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.value.Mode;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.managers.FriendManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.ClientSettings;




public class Radar
        extends Module {
	
	
	  public static Mode Color23 = new Mode("ColorMode", "ColorMode", new String[]{"Client","Rainbow"}, "Client");
    public Radar() {
        super("Radar", ModuleType.Render);
        addValues(Color23);
    }
    int rainbowTick = 0;

   
   


    @EventHandler
    public void EventRender(EventRender2D e) {
    	 final ScaledResolution sr = new ScaledResolution(mc);
     	  float x1 = sr.getScaledWidth(), y1 = 0;
           float arrayListY = y1;
    	  
  	 
        double center = 90.0;
        double toAddX = 3.0;
        double toAddZ = 15.0;
        double x = 10.0;
        double y = 90.0;
        if (Color23.getModeAsString().equals("Client")) {
        	int i3 = 0;
     		 Color startColor = new Color(ClientSettings.r.getValue().intValue(), ClientSettings.g.getValue().intValue(), ClientSettings.b.getValue().intValue()); 
     		 Color endColor = new Color(ClientSettings.r2.getValue().intValue(), ClientSettings.g2.getValue().intValue(), ClientSettings.b2.getValue().intValue());
       	  Pair<Color, Color> colors = Pair.of(startColor, endColor);
       	  Color c = Render2.interpolateColorsBackAndForth(7, 3 + (i3 * 20), colors.getFirst(), colors.getSecond(), false);
            Gui.drawRect(x, y, x + 100.0, y + 1.0, c.getRGB());
            Gui.drawRect(x, y + 89.0, x + 100.0, y + 90.0, c.getRGB());
            Gui.drawRect(x, y, x + 1.0, y + 90.0, c.getRGB());
            Gui.drawRect(x + 99.0, y, x + 100.0, y + 90.0, c.getRGB());
            Gui.drawRect(x, y, x + 100.0, y + 89.0, new Color(0, 0, 0, 100).getRGB());
        	 }
        	 
        	 if (Color23.getModeAsString().equals("Rainbow")) {
        	
              	Color c = new Color(Color.HSBtoRGB((float) ((double) Client.instance.mc.thePlayer.ticksExisted / 54.1 + Math.sin((double) (rainbowTick + (arrayListY - 4) / 12) / 50.0 * 1.6)) % 1.0f, 1f, 1f));
                Gui.drawRect(x, y, x + 100.0, y + 1.0, c.getRGB());
                Gui.drawRect(x, y + 89.0, x + 100.0, y + 90.0, c.getRGB());
                Gui.drawRect(x, y, x + 1.0, y + 90.0, c.getRGB());
                Gui.drawRect(x + 99.0, y, x + 100.0, y + 90.0, c.getRGB());
                Gui.drawRect(x, y, x + 100.0, y + 89.0, new Color(0, 0, 0, 100).getRGB());
           
             	 }

        // Gui.prepareScissorBox(3.0f, 15.0f, 103.0f, 115.0f);
        GL11.glEnable((int)3089);
        // RenderUtil.prepareScissorBox(3.0, 15.0, 103.0, 115.0, -1879048192);
        for (Entity ent : mc.theWorld.getLoadedEntityList()) {
            if (ent == mc.thePlayer) continue;
            int color = 0;
            if (ent instanceof EntityPlayer) {
                color = new Color(255, 255, 255, 128).getRGB();
            }
            double drawX = 50.0 + (double)(Math.round(mc.thePlayer.posX) - Math.round(ent.posX));
            double drawZ = 50.0 + (double)(Math.round(mc.thePlayer.posZ) - Math.round(ent.posZ));
            Gui.drawRect(drawX + x, drawZ + y, drawX + 1.0 + x, drawZ + 1.0 + y, color);
        }
        GL11.glDisable((int)3089);
        Gui.drawRect(x + 50.0, y, x + 49.0, y + 89.0, new Color(0x56FFFFFF, true).getRGB());
        Gui.drawRect(x, y + 44.0, x + 100.0, y + 45.0, new Color(0x56FFFFFF, true).getRGB());
    }

}

