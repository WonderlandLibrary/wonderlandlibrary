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
import xyz.cucumber.base.module.feat.other.FriendsModule;
import xyz.cucumber.base.module.feat.other.TeamsModule;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.game.EntityUtils;
import xyz.cucumber.base.utils.math.RotationUtils;
import xyz.cucumber.base.utils.render.ColorUtils;

@ModuleInfo(category = Category.VISUALS, description = "Displays arrows to player", name = "Tracers", priority = ArrayPriority.LOW)
public class TracersModule extends Mod{
	
	private ColorSettings color = new ColorSettings("Color", "Static", -1, -1, 100);
	private ColorSettings colorf = new ColorSettings("Team Color", "Static", 0xff00ff00, -1, 100);
	private ColorSettings colorfr = new ColorSettings("Friend Color", "Static", 0xff0000ff, -1, 100);
	private ColorSettings murderC = new ColorSettings("Murder Color", "Static", 0xff0000ff, -1, 100);
	
	public TracersModule() {
		this.addSettings(
				color,
				colorf,
				colorfr,
				murderC
				);
		
	}
	
	@EventListener
	public void onRender2D(EventRenderGui e) {
		
		for(EntityPlayer entity : mc.theWorld.playerEntities) {
			
			if(entity == mc.thePlayer) continue;
			
			double playerX = entity.prevPosX + (entity.posX - entity.prevPosX) * mc.timer.renderPartialTicks;
			double playerZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * mc.timer.renderPartialTicks;
			
			
			
			
			double diffX = playerX - mc.thePlayer.posX;
			double diffZ = playerZ - mc.thePlayer.posZ;

			double x = e.getScaledResolution().getScaledWidth()/2 - Math.sin(Math.toRadians(RotationUtils.fovFromPosition(new double[] { playerX, playerZ })))*35;;
			double y = e.getScaledResolution().getScaledHeight()/2 - Math.cos(Math.toRadians(RotationUtils.fovFromPosition(new double[] { playerX, playerZ })))*35;;
			
			int color =  ColorUtils.getColor(this.color, System.nanoTime()/1000000, 0, 5);
			if(Client.INSTANCE.getModuleManager().getModule(TeamsModule.class).isEnabled()) {
				if(mc.thePlayer.isOnSameTeam(entity) || EntityUtils.isInSameTeam(entity)){
					color = ColorUtils.getColor(colorf, System.nanoTime()/1000000, 0, 5);
				}
			}
			if(Client.INSTANCE.getModuleManager().getModule(FriendsModule.class).isEnabled()) {
				for(String friend : FriendsCommand.friends) {
	        		if(friend.equalsIgnoreCase(entity.getName())) {
	        			color =  ColorUtils.getColor(colorfr, System.nanoTime()/1000000, 0, 5);
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
			renderArrow(x,y,8, color, 0xff151515, -RotationUtils.fovFromEntity(entity));
		}
	}
	
	public void renderArrow(double x, double y, double size, int color, int outlineColor, double rotation) {
		GL11.glPushMatrix();
		GL11.glLineWidth(1);
		RenderUtils.start2D();
		
		GL11.glTranslated(x, y, 0);
		GL11.glRotated(rotation, 0, 0, 1.);
		GL11.glTranslated(-x, -y, 0);
		
		RenderUtils.color(color);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2d(x, y-size/2);
		GL11.glVertex2d(x+size/3, y+size/3);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x-size/3, y+size/3.5F);

		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x+size/3, y+size/3);
		GL11.glVertex2d(x, y+size/4);
		GL11.glVertex2d(x-size/3, y+size/3);
		
		GL11.glEnd();
		
		
		RenderUtils.color(0x40000000);
		GL11.glBegin(GL11.GL_POLYGON);
		
		GL11.glVertex2d(x, y-size/2);
		GL11.glVertex2d(x, y+size/4);
		GL11.glVertex2d(x-size/3, y+size/3);
		
		
		GL11.glEnd();
		RenderUtils.color(0x50000000);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x+size/3, y+size/3);
		GL11.glVertex2d(x, y+size/4);
		GL11.glVertex2d(x-size/3, y+size/3);
		
		GL11.glEnd();
		
		RenderUtils.color(outlineColor);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		
		GL11.glVertex2d(x, y-size/2);
		GL11.glVertex2d(x+size/3, y+size/3);
		GL11.glVertex2d(x, y+size/4);
		GL11.glVertex2d(x-size/3, y+size/3F);

		GL11.glEnd();
		
		RenderUtils.stop2D();

		GL11.glPopMatrix();
	}
	
}
