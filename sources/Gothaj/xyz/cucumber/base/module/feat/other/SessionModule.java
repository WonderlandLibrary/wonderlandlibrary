package xyz.cucumber.base.module.feat.other;

import java.util.Collection;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.EventType;
import xyz.cucumber.base.events.ext.EventBloom;
import xyz.cucumber.base.events.ext.EventBlur;
import xyz.cucumber.base.events.ext.EventReceivePacket;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.addons.Dragable;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.math.PositionHandler;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

@ModuleInfo(category = Category.OTHER, description = "Displays session info", name = "Session", priority = ArrayPriority.LOW)

public class SessionModule extends Mod implements Dragable{
	private NumberSettings positionX = new NumberSettings("Position X", 30, 0, 1000, 1);
	private NumberSettings positionY = new NumberSettings("Position Y", 50, 0, 1000, 1);
	private BooleanSettings blur = new BooleanSettings("Blur", true);
	private BooleanSettings bloom = new BooleanSettings("Bloom", true);
	public ColorSettings bloomColor = new ColorSettings("Bloom color", "Static", 0xff000000, -1, 40);
	public ColorSettings logoColor = new ColorSettings("Mark color", "Mix", 0xff44bcfc, 0xff3275f0, 100);
	public ColorSettings backgroundColor = new ColorSettings("Background color", "Static", 0xff000000, -1, 40);
	
	private PositionUtils accounts = new PositionUtils(0, 0,100, 15+12*5,1);
	
	public SessionModule() {
		this.addSettings(
				positionX,
				positionY,
				blur,
				bloom,
				bloomColor,
				logoColor,
				backgroundColor
				);
	}
	
	private int wins;
	private int deaths;
	private int kills;
	
	private String[] winStrings = new String[] {
		"Winners: <me>","Winner: <me>"
	};
	private String[] killStrings = new String[] {
			"was killed by <me>"
		};
	private String[] deadStrings = new String[] {
			"<me> was killed by", "<me> died", "<me> fell into","<me> tried"
		};
	
	@EventListener
	public void onBlur(EventBlur e) {
		if(!blur.isEnabled()) return;
		e.setCancelled(true);
		if(e.getType() == EventType.POST)
			RenderUtils.drawRoundedRect(accounts.getX(), accounts.getY(), accounts.getX2(), accounts.getY2(), ColorUtils.getColor(backgroundColor, System.nanoTime()/1000000,0, 5),3D);
	}
	
	@EventListener
	public void onBloom(EventBloom e) {
		if(!bloom.isEnabled()) return;
		e.setCancelled(true);
		
		if(e.getType() == EventType.POST)
			RenderUtils.drawRoundedRect(accounts.getX(), accounts.getY(), accounts.getX2(), accounts.getY2(), ColorUtils.getColor(bloomColor, System.nanoTime()/1000000,0, 5), 3D);
	}
	
	@EventListener
	public void onRender2D(EventRenderGui e) {
		double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
		
		accounts.setX(pos[0]);
		accounts.setY(pos[1]);
		
		RenderUtils.drawRoundedRect(accounts.getX(), accounts.getY(), accounts.getX2(), accounts.getY2(), ColorUtils.getColor(backgroundColor, System.nanoTime()/1000000,0, 5), 3D);
		
		GL11.glPushMatrix();
		
		RenderUtils.start2D();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glLineWidth(2);
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		RenderUtils.color(0x00aaaaaa);
		GL11.glVertex2d(accounts.getX(), accounts.getY()+15);
		RenderUtils.color(0xffaaaaaa);
		GL11.glVertex2d(accounts.getX()+accounts.getWidth()/2, accounts.getY()+15);
		RenderUtils.color(0x00aaaaaa);
		GL11.glVertex2d(accounts.getX2(), accounts.getY()+15);
		
		GL11.glEnd();
		
		RenderUtils.stop2D();
		
		GL11.glPopMatrix();
		
		String[] s = "Session".split("");
        double w = 0;
        
        for(String t : s) {
        	Fonts.getFont("rb-b").drawStringWithShadow(t, accounts.getX()+ accounts.getWidth()/2-Fonts.getFont("rb-b").getWidth("Session")/2+w, accounts.getY()+6,  ColorUtils.getColor(logoColor, System.nanoTime()/1000000,w*2, 5),0x45000000);	
        	w += Fonts.getFont("rb-b").getWidth(t);
        }
        
        int time = (int) (System.currentTimeMillis() - Client.INSTANCE.startTime);
        
        int seconds = time /1000;
        
        int minutes = seconds / 60;
        
        int hours = minutes /60;
        Fonts.getFont("rb-b-13").drawString("Playing: ", accounts.getX()+4, accounts.getY()+20, -1);
        Fonts.getFont("rb-m-13").drawString(hours+"h "+minutes % 60+"m "+seconds% 60+"s", accounts.getX()+4+Fonts.getFont("rb-b-13").getWidth("Playing: "), accounts.getY()+20, 0xffaaaaaa);
        Fonts.getFont("rb-b-13").drawString("Wins: ", accounts.getX()+4, accounts.getY()+32, -1);
        Fonts.getFont("rb-m-13").drawString(""+wins, accounts.getX()+4+Fonts.getFont("rb-b-13").getWidth("Wins: "), accounts.getY()+32, 0xffaaaaaa);
        Fonts.getFont("rb-b-13").drawString("Kills: ", accounts.getX()+4, accounts.getY()+44, -1);
        Fonts.getFont("rb-m-13").drawString(""+kills, accounts.getX()+4+Fonts.getFont("rb-b-13").getWidth("Kills: "), accounts.getY()+44, 0xffaaaaaa);
        Fonts.getFont("rb-b-13").drawString("Deaths: ", accounts.getX()+4, accounts.getY()+56, -1);
        Fonts.getFont("rb-m-13").drawString(""+deaths, accounts.getX()+4+Fonts.getFont("rb-b-13").getWidth("Deaths: "), accounts.getY()+56, 0xffaaaaaa);
        Fonts.getFont("rb-b-13").drawString("K/D: ", accounts.getX()+4, accounts.getY()+68, -1);
        Fonts.getFont("rb-m-13").drawString(""+Math.round(((float)kills/(float)(deaths == 0 ? 1 : deaths))*100F)/100F, accounts.getX()+4+Fonts.getFont("rb-b-13").getWidth("K/D: "), accounts.getY()+68,0xffaaaaaa);
	}
	
	@EventListener
	public void onReceivePacket(EventReceivePacket e) {
		if(e.getPacket() instanceof S02PacketChat) {
			S02PacketChat packet = (S02PacketChat) e.getPacket();
			
			String[] s = packet.getChatComponent().getUnformattedText().split("");
			int i = 0;
			String text = "";
			while(i < s.length) {
				if(s[i].equals("§")) {
					i+=2;
					continue;
				}
				text+=s[i];
				i++;
			}
			
			for(String win : winStrings) {
				String f = win.replace("<me>", mc.thePlayer.getName());
				if(text.contains(f)) {
					wins+=1;
					break;
				}
			}
			for(String death : deadStrings) {
				String f = death.replace("<me>", mc.thePlayer.getName());
				if(text.contains(f)) {
					deaths+=1;
					break;
				}
			}
			for(String kill : killStrings) {
				String f = kill.replace("<me>", mc.thePlayer.getName());
				if(text.contains(f)) {
					kills+=1;
					break;
				}
			}
			
		}
	}
	
	@Override
	public PositionUtils getPosition() {
		return accounts;
	}

	@Override
	public void setXYPosition(double x, double y) {
		this.positionX.setValue(x);
		this.positionY.setValue(y);
	}
	
}
