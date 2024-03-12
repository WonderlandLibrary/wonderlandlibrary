package xyz.cucumber.base.module.feat.visuals;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.math.RotationUtils;
import xyz.cucumber.base.utils.render.ColorUtils;

@ModuleInfo(category = Category.VISUALS, description = "Displays china hat on your head", name = "China Hat", priority = ArrayPriority.LOW)
public class ChinaHatModule extends Mod{

	public ModeSettings mode = new ModeSettings("Mode", new String[] {
			"Player", "All"
	});
	
	public NumberSettings radius = new NumberSettings("Radius", 0.7, 0, 3, 0.05);
	
	public ColorSettings color = new ColorSettings("Color", "Static", -1, -1, 70);
	
	public ChinaHatModule() {
		this.addSettings(
				mode,radius,color
				);
	}
	
	@EventListener
	public void onRender3D(EventRender3D e) {
		
		
		
		if(mode.getMode().equalsIgnoreCase("All")) {
			for(Entity entity : mc.theWorld.loadedEntityList) {
				if(entity == mc.thePlayer || !(entity instanceof EntityPlayer)) continue;
				
				render(entity, e.getPartialTicks(), new float[] {
						entity.rotationYaw,
						entity.rotationPitch	
					});
			}
		}
		if(mc.gameSettings.thirdPersonView == 0) return;
		render(mc.thePlayer, e.getPartialTicks(), new float[] {
				RotationUtils.customRots ? RotationUtils.serverYaw : mc.thePlayer.rotationYaw,
						RotationUtils.customRots ? RotationUtils.serverPitch : mc.thePlayer.rotationPitch	
			});
	}
	
	public void render(Entity e, float partialTicks, float[] rots) {
		GL11.glPushMatrix();
		
		RenderUtils.start3D();
		
		double x = (e.prevPosX + (e.posX - e.prevPosX) * partialTicks) - mc.getRenderManager().viewerPosX;
		double y = (e.prevPosY + (e.posY - e.prevPosY) * partialTicks) - mc.getRenderManager().viewerPosY;
		double z = (e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks) - mc.getRenderManager().viewerPosZ;
		
		float yaw =  rots[0];
		float pitch = rots[1];
		
		GL11.glTranslated(x-Math.sin(yaw *Math.PI/180)*pitch/270, y+e.height-(e.isSneaking() ? 0.25 : 0), z+Math.cos(yaw *Math.PI/180)*pitch/270);
		
		GL11.glRotated(pitch, Math.cos(yaw *Math.PI/180), 0F, Math.sin(yaw *Math.PI/180));
		GL11.glRotated(-mc.thePlayer.rotationYaw-270, 0, 1F, 0);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex3d(0,0.3,0);
		
		for(double i = 0; i <= 360; i+=5) {
			RenderUtils.color(ColorUtils.getColor(color, System.nanoTime()/1000000, i, 5));
			GL11.glVertex3d(Math.sin(i * Math.PI/180) *radius.getValue(),0,-Math.cos(i * Math.PI/180) * radius.getValue());
		}
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		RenderUtils.stop3D();
		GL11.glPopMatrix();
	}
	
}
