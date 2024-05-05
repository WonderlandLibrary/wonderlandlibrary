package epsilon.ui;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import epsilon.Epsilon;
import epsilon.events.listeners.EventRenderGUI;
import epsilon.modules.Module;
import epsilon.modules.dev.ServerLagCheck;
import epsilon.modules.render.Theme;
import epsilon.util.ColorUtil;
import epsilon.util.EpsilonColorUtils;
import epsilon.util.MathHelperEpsilon;
//import epsilon.util.DiscordRPCUtil;
import epsilon.util.MoveUtil;
import epsilon.util.ShapeUtils;
import epsilon.util.Timer;
import epsilon.util.Font.Fonts;/*
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;*/
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class HUD {
	
	private final ShapeUtils render = new ShapeUtils();
	
	public Minecraft mc = Minecraft.getMinecraft();
	public static transient Timer arrayListToggleMovement = new Timer();
	public static int arrayListRainbow = 0;
	public static int arrayListColor = -1;
	
	public void draw(){
		
		List<Module> moduleList = new ArrayList<Module>();
		moduleList.clear();
		for(Module m : Epsilon.modules) {
			if(!m.toggled)
				continue;
			moduleList.add(m);
		}
		
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		FontRenderer fr = mc.fontRendererObj;
		Epsilon.modules.sort(Comparator.comparingInt(m -> fr.getStringWidth(((Module)m).name)).reversed());
		moduleList.sort(Comparator.comparingInt(m -> fr.getStringWidth( ((Module)m).name + (((Module)m).displayInfo.length()>0 ? " " : "") + ((Module)m).displayInfo)).reversed());


		
		int count = 0;
		int mintoffset = 0;
		double maxBps = 0.0;

		float speed = 0.75f;
		
		switch(Theme.getTheme()) {
		case "Stitch":
		case "Superhero":
			
			speed = 0.5f;
			
			break;
			
		case "YellowSnow":
			speed = 1.25f;
			break;
			
		}
		for(Module m : moduleList){
			if(!m.toggled || m.name.equals("TabGUI"))
				continue;
			
			double offset = count*(fr.FONT_HEIGHT + 4);
			
			
			//if(Hud)
			
	/*		ShapeUtils.roundedRect(230, 31, 162,  2 * 50 + 1.5, 1, new org.lwjgl.util.Color(0, 0, 0, 140));
			String r = ChatFormatting.WHITE + "Session";
			Minecraft.getMinecraft().fontRendererObj.drawString(r, 290, new ScaledResolution(mc, 1, 1).getScaledHeight() + 39, 1);
			String r3 = ChatFormatting.WHITE + "Date: ";
			Minecraft.getMinecraft().fontRendererObj.drawString(r3, 240, new ScaledResolution(mc, 1, 1).getScaledHeight() + 109, 1);
			String err = "___________________________";
			Minecraft.getMinecraft().fontRendererObj.drawString(err, 230, new ScaledResolution(mc, 1, 1).getScaledHeight() + 45, ColorUtil.getRainbow(10, 0.6f, 1));
			String err3 = "___________________________";
			Minecraft.getMinecraft().fontRendererObj.drawString(err3, 231, new ScaledResolution(mc, 1, 1).getScaledHeight() + 45, ColorUtil.getRainbow(10, 0.6f, 1)); */
				
			
			
			fr.drawStringWithShadow( m.name, sr.getScaledWidth() - fr.getStringWidth(m.name +  "  " + m.displayInfo + (m.displayInfo!="" ? " " : "")) - 3, (float) (offset) + 6,  ColorUtil.getColorAsInt(mintoffset, Theme.getTheme(), (float) Theme.speed.getValue()));
			
			
			fr.drawStringWithShadow(" " + m.displayInfo, sr.getScaledWidth() - fr.getStringWidth(m.displayInfo  + " " + (m.displayInfo!="" ? " " : "")) -6, (float) (offset) + 6,  -1);
			//Gui.drawRect(sr.getScaledWidth(), 5+count*10, sr.getScaledWidth()-2, 25+count*10, ColorUtil.getColorAsInt(mintoffset, Theme.getTheme(), 0.75F));
			
		//	int ychat = mc.ingameGUI.getChatGUI().getChatOpen() ? 25 : 10;
			
	        //String Build = ChatFormatting.AQUA + "Build " + Epsilon.version + ChatFormatting.WHITE + " | " + ChatFormatting.AQUA + "DEVELOPER" + ChatFormatting.WHITE + " | " + ChatFormatting.AQUA + "Segation" + " (" + "Uid: 1" + ")";
	        //Fonts.Segation23.drawStringWithShadow(Build, 725, new ScaledResolution(mc, 1, 1).getScaledHeight() + 475, 2);
	        
	        mintoffset++;
			count++;
		
			
				
			
		}
		
		if (MoveUtil.getBlocksPerSecond() > maxBps) {
			maxBps = MoveUtil.getBlocksPerSecond();
		}
		
		long BPS =Math.round(maxBps);
		String BPSString = String.valueOf(BPS);

		MathHelperEpsilon math = new MathHelperEpsilon();
		String bps = ChatFormatting.AQUA + "BPS" + ChatFormatting.WHITE + ": " + math.round(maxBps, 2); 
		String Fps = ChatFormatting.AQUA + " FPS" + ChatFormatting.WHITE + ": " + ChatFormatting.WHITE + Math.round(mc.getDebugFPS()*1.25);
		Fonts.Segation23.drawStringWithShadow(bps, 4, new ScaledResolution(mc, 1, 1).getScaledHeight() + 25, -1);
		Fonts.Segation23.drawStringWithShadow(Fps, 0.5, new ScaledResolution(mc, 1, 1).getScaledHeight() + 35, 2);
        String X = ChatFormatting.AQUA + "X" + ChatFormatting.WHITE + ": " + MathHelper.floor_double(mc.thePlayer.posX); 
        String Y = ChatFormatting.AQUA + "Y" + ChatFormatting.WHITE + ": " + MathHelper.floor_double(mc.thePlayer.posY);
        String Z = ChatFormatting.AQUA + "Z" + ChatFormatting.WHITE + ": " + MathHelper.floor_double(mc.thePlayer.posZ);
        Fonts.Segation23.drawStringWithShadow(X, 4, new ScaledResolution(mc, 1, 1).getScaledHeight() + 45, 2);
        Fonts.Segation23.drawStringWithShadow(Y, 4, new ScaledResolution(mc, 1, 1).getScaledHeight() + 55, 2);
        Fonts.Segation23.drawStringWithShadow(Z, 4, new ScaledResolution(mc, 1, 2).getScaledHeight() + 65, 2);
        Fonts.Segation45.drawStringWithShadow("M", 10, new ScaledResolution(mc, 3, 5).getScaledHeight() - 2, ColorUtil.getColorAsInt(mintoffset, Theme.getTheme(), speed/2));
        Fonts.Segation45.drawStringWithShadow(" int", 22, new ScaledResolution(mc, 3, 5).getScaledHeight() - 2, -1);
        if(ServerLagCheck.CalculateTPS>25) {
        	Fonts.Segation18.drawStringWithShadow("Server hasnt responded for:        ticks", 55, new ScaledResolution(mc, 2, 1).getScaledHeight() + 75, -1);
        	Fonts.Segation18.drawStringWithShadow(ServerLagCheck.CalculateTPS +"", 177, new ScaledResolution(mc, 2, 1).getScaledHeight() + 75, ColorUtil.getColorAsInt(mintoffset, Theme.getTheme(), speed/2));
        }
        String Ver = Epsilon.version;
        Fonts.Segation23.drawStringWithShadow(Ver, 55, new ScaledResolution(mc, 3, 5).getScaledHeight() - 2, -1);
		
		Epsilon.onEvent(new EventRenderGUI());
	}

}