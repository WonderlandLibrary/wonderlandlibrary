package xyz.cucumber.base.module.feat.visuals;

import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.commands.cmds.FriendsCommand;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.addons.Dragable;
import xyz.cucumber.base.module.feat.other.FriendsModule;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.math.PositionHandler;
import xyz.cucumber.base.utils.math.RotationUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.StencilUtils;

@ModuleInfo(category = Category.VISUALS, description = "Display players on map", name = "Radar", priority = ArrayPriority.LOW)
public class RadarModule extends Mod implements Dragable{
	
	private NumberSettings positionX = new NumberSettings("Position X", 30, 0, 1000, 1);
	private NumberSettings positionY = new NumberSettings("Position Y", 50, 0, 1000, 1);
	
	private ModeSettings mode = new ModeSettings("Mode", new String[] { 
			"Rect", "Rounded", "Circle"
	});
	
	private PositionUtils position = new PositionUtils(0,0,100,100,1);
	
	private NumberSettings size = new NumberSettings("Size", 100, 40, 200, 1);
	
	private ColorSettings bg = new ColorSettings("Background Color", "Static", 0xff000000, -1, 100);
	private ColorSettings normalC = new ColorSettings("Player Color", "Static", 0xff000000, -1, 100);
	private ColorSettings teamC = new ColorSettings("Team Color", "Static", 0xff000000, -1, 100);
	private ColorSettings friendC = new ColorSettings("Friend Color", "Static", 0xff000000, -1, 100);
	private ColorSettings murderC = new ColorSettings("Murder Color", "Static", 0xff000000, -1, 100);
	
	public RadarModule() {
		this.addSettings(
				positionX,
				positionY,
				mode,
				size,
				bg,
				normalC,
				teamC,
				friendC,
				murderC
				);
		
	}



	@Override
	public PositionUtils getPosition() {
		return this.position;
	}



	@Override
	public void setXYPosition(double x, double y) {
		this.positionX.setValue(x);
		this.positionY.setValue(y);
	}



	@EventListener
	public void onRenderGui(EventRenderGui e) {
		double[] pos = PositionHandler.getScaledPosition(this.positionX.getValue(), this.positionY.getValue());
		this.position.setX(pos[0]);
		this.position.setY(pos[1]);
		this.position.setWidth(size.getValue());
		this.position.setHeight(size.getValue());
		switch(mode.getMode().toLowerCase()) {
		case "rounded":
			RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getColor(bg, System.nanoTime()/1000000, 0, 5), 3F);
			break;
		case "circle":
			RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getColor(bg, System.nanoTime()/1000000, 0, 5),(float) (size.getValue()/4));
			break;
		default:
			RenderUtils.drawRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getColor(bg, System.nanoTime()/1000000, 0, 5));
			break;
		}
		
		StencilUtils.initStencil();
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        StencilUtils.bindWriteStencilBuffer();
		switch(mode.getMode().toLowerCase()) {
		case "rounded":
			RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getColor(bg, System.nanoTime()/1000000, 0, 5), 3F);
			break;
		case "circle":
			RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getColor(bg, System.nanoTime()/1000000, 0, 5),(float) (size.getValue()/4));
			break;
		default:
			RenderUtils.drawRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getColor(bg, System.nanoTime()/1000000, 0, 5));
			break;
		}
		
		StencilUtils.bindReadStencilBuffer(1);
	
        
		for(EntityPlayer entity : mc.theWorld.playerEntities) {
			
			if(entity == mc.thePlayer) continue;
			
			double playerX = entity.prevPosX + (entity.posX - entity.prevPosX) * mc.timer.renderPartialTicks;
			double playerZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * mc.timer.renderPartialTicks;
			
			
			
			
			double diffX = playerX - mc.thePlayer.posX;
			double diffZ = playerZ - mc.thePlayer.posZ;
			
			double dist = Math.sqrt(Math.pow(diffX, 2)+Math.pow(diffZ, 2))*1.2;
			
			
			
			double x = this.position.getX()+this.position.getWidth()/2;
			double y = this.position.getY()+this.position.getHeight()/2;
			x = x - Math.sin(Math.toRadians(RotationUtils.fovFromEntity(entity)))*dist;
			y = y - Math.cos(Math.toRadians(RotationUtils.fovFromEntity(entity)))*dist;
			
			int color =  ColorUtils.getColor(normalC, System.nanoTime()/1000000, 0, 5);
			if(Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled()) {
				if(mc.thePlayer.isOnSameTeam(entity) || EntityUtils.isInSameTeam(entity)){
					color = ColorUtils.getColor(teamC, System.nanoTime()/1000000, 0, 5);
				}
			}
			if(Client.INSTANCE.getModuleManager().getModule(FriendsModule.class).isEnabled()) {
				for(String friend : FriendsCommand.friends) {
	        		if(friend.equalsIgnoreCase(entity.getName())) {
	        			color =  ColorUtils.getColor(friendC, System.nanoTime()/1000000, 0, 5);
	        			break;
	        		}
	        	}
			}
			if(Client.INSTANCE.getModuleManager().getModule(MurderFinderModule.class).isEnabled()) {
				MurderFinderModule mod = (MurderFinderModule) Client.INSTANCE.getModuleManager().getModule(MurderFinderModule.class);
				for(Entry<String,Entity> entry : mod.murders.entrySet()) {
					if(entry.getKey().equalsIgnoreCase(entity.getName())) {
	        			color =  ColorUtils.getColor(murderC, System.nanoTime()/1000000, 0, 5);
	        			break;
	        		}
				}
			}
			

			RenderUtils.drawCircle(x, y, 1.4, color, 1);
		}
		StencilUtils.uninitStencilBuffer();
		RenderUtils.drawLine(this.position.getX()+this.position.getWidth()/2, this.position.getY(), this.position.getX()+this.position.getWidth()/2, this.position.getY2(), 0xff777777, 1);
		RenderUtils.drawLine(this.position.getX(), this.position.getY()+this.position.getHeight()/2, this.position.getX2(), this.position.getY()+this.position.getHeight()/2, 0xff777777, 1);
	}
	
	public void renderView(double x, double y, double yaw) {
		GL11.glPushMatrix();
		
		RenderUtils.start2D();
		RenderUtils.color(-1);

		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBegin(GL11.GL_POLYGON);
		
		GL11.glVertex2d(x, y);
		RenderUtils.color(0x00ffffff);
		GL11.glVertex2d(x-Math.sin((yaw-35) * Math.PI /180)*8, y-Math.cos((yaw-35) * Math.PI /180)*8);
		GL11.glVertex2d(x-Math.sin((yaw+35) * Math.PI /180)*8, y-Math.cos((yaw+35) * Math.PI /180)*8);
		
		GL11.glEnd();
		
		RenderUtils.stop2D();
		GL11.glRotatef((float) (0), 0f, 0f, 0f);
		
		
		GL11.glPopMatrix();
	}
	
}
