package xyz.cucumber.base.module.feat.visuals;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.feat.combat.KillAuraModule;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.render.ColorUtils;

@ModuleInfo(category = Category.VISUALS, description = "", name = "Target ESP")
public class TargetESPModule extends Mod {
	
	public ModeSettings mode = new ModeSettings("Mode", new String[] {
			"Circle", "Dotted", "Sims", "Cock", "Rect", "Hitbox"
	});
	
	public ColorSettings color = new ColorSettings("Color", "Static", -1, -1, 100);
	public ColorSettings hitColor = new ColorSettings("Hurttime Color", "Static", 0xffff0000, -1, 100);
	KillAuraModule ka;
	
	private double animation;
	private boolean direction;
	
	public TargetESPModule() {
		this.addSettings(mode,color,hitColor);
	}
	
	public void onEnable() {
		ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
	}
	
	@EventListener
	public void onRender3D(EventRender3D e) {
		ka = (KillAuraModule) Client.INSTANCE.getModuleManager().getModule(KillAuraModule.class);
		if(!ka.isEnabled()) return;
		
		if(ka.target == null) return;
		
		EntityLivingBase entity = ka.target;
		GL11.glPushMatrix();
		RenderUtils.start3D();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		double x = (entity.prevPosX + (entity.posX - entity.prevPosX) * e.getPartialTicks()) - mc.getRenderManager().viewerPosX;
		double y = (entity.prevPosY + (entity.posY - entity.prevPosY) * e.getPartialTicks()) - mc.getRenderManager().viewerPosY;
		double z = (entity.prevPosZ + (entity.posZ - entity.prevPosZ) * e.getPartialTicks()) - mc.getRenderManager().viewerPosZ;
		double size = entity.width/2;
		AxisAlignedBB bb = new AxisAlignedBB(x-size, y+entity.height, z-size, x+size, y, z+size);
		
		switch(mode.getMode().toLowerCase()) {
		case "circle":
			if (direction) {
		          animation -= 0.02;
		          if (-entity.height / 2 > animation) {
		              direction = !direction;}} else {
		          animation += 0.02;
		          if (entity.height / 2 < animation) {
		              direction = !direction;}}
			GL11.glBegin(GL11.GL_QUAD_STRIP);
			for(double i = 0; i <= 360; i+=10) {
				double rX = x+Math.sin(i * Math.PI/180F)* entity.width;
				double rY = y+animation;
				double rZ = z+Math.cos(i * Math.PI/180F)* entity.width;
				RenderUtils.color(ColorUtils.mix(ColorUtils.getColor(color, System.nanoTime()/1000000, i, 5), ColorUtils.getColor(hitColor, System.nanoTime()/1000000, i, 5), entity.hurtTime ,10));
				GL11.glVertex3d(rX, rY+entity.height/2, rZ);
				RenderUtils.color(ColorUtils.mix(ColorUtils.getColor(color, System.nanoTime()/1000000, i, 5), ColorUtils.getColor(hitColor, System.nanoTime()/1000000, i, 5), entity.hurtTime ,10),0);
				GL11.glVertex3d(rX, rY+entity.height/2- animation / entity.height, rZ);
					
			}
			GL11.glEnd();
			break;
		case "dotted":
			if (direction) {
		          animation -= 0.02;
		          if (-entity.height / 2 > animation) {
		              direction = !direction;}} else {
		          animation += 0.02;
		          if (entity.height / 2 < animation) {
		              direction = !direction;}}
			GL11.glPointSize(10);
			GL11.glTranslated(x, y, z);
			GL11.glRotatef((mc.thePlayer.ticksExisted+e.getPartialTicks()) *8, 0f, 1f, 0f);
			GL11.glTranslated(-x, -y, -z);
			
			GL11.glBegin(GL11.GL_POINTS);
			for(double i = 0; i <= 360; i+=40) {
				double rX = x+Math.sin(i * Math.PI/180F)* entity.width;
				double rY = y+animation;
				double rZ = z+Math.cos(i * Math.PI/180F)* entity.width;
				RenderUtils.color(ColorUtils.mix(ColorUtils.getColor(color, System.nanoTime()/1000000, i, 5), ColorUtils.getColor(hitColor, System.nanoTime()/1000000, i, 5), entity.hurtTime ,10));
				GL11.glVertex3d(rX, rY+entity.height/2, rZ);
					
			}
			
			GL11.glEnd();
			GL11.glPointSize(18);
			GL11.glBegin(GL11.GL_POINTS);
			for(double i = 0; i <= 360; i+=40) {
				double rX = x+Math.sin(i * Math.PI/180F)* entity.width;
				double rY = y+animation;
				double rZ = z+Math.cos(i * Math.PI/180F)* entity.width;
				RenderUtils.color(ColorUtils.getAlphaColor(ColorUtils.mix(ColorUtils.getColor(color, System.nanoTime()/1000000, i, 5), ColorUtils.getColor(hitColor, System.nanoTime()/1000000, i, 5), entity.hurtTime ,10), 20));
				GL11.glVertex3d(rX, rY+entity.height/2, rZ);
					
			}
			
			GL11.glEnd();
			break;
		case "sims":
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glRotated(z, x, y, size);
			
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			
			double rY = y+entity.height+0.5;
			RenderUtils.color(-1, color.getAlpha());
			GL11.glVertex3d(x, rY+0.8, z);
			for(double i = 0; i <= 360; i+=60) {
				double rX = x+Math.sin(i * Math.PI/180F)* entity.width/3;
				double rZ = z+Math.cos(i * Math.PI/180F)* entity.width/3;
				RenderUtils.color(ColorUtils.mix(ColorUtils.getColor(color, System.nanoTime()/1000000, 0, 5), ColorUtils.getColor(hitColor, System.nanoTime()/1000000, 0, 5), entity.hurtTime ,10));
				GL11.glVertex3d(rX, rY+0.8/2, rZ);
			}
			GL11.glEnd();
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			GL11.glVertex3d(x, rY, z);
			for(double i = 0; i <= 360; i+=60) {
				double rX = x+Math.sin(i * Math.PI/180F)* entity.width/3;
				double rZ = z+Math.cos(i * Math.PI/180F)* entity.width/3;
				
				GL11.glVertex3d(rX, rY+0.8/2, rZ);
			}
			GL11.glEnd();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			RenderUtils.color(0x25000000);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			
			for(double i = 0; i <= 360; i+=60) {
				double rX = x+Math.sin(i * Math.PI/180F)* entity.width/3;
				double rZ = z+Math.cos(i * Math.PI/180F)* entity.width/3;
				GL11.glVertex3d(x, rY, z);
				GL11.glVertex3d(rX, rY+0.8/2, rZ);
			}
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINE_LOOP);
			
			for(double i = 0; i <= 360; i+=60) {
				double rX = x+Math.sin(i * Math.PI/180F)* entity.width/3;
				double rZ = z+Math.cos(i * Math.PI/180F)* entity.width/3;
				GL11.glVertex3d(x, rY+0.8, z);
				GL11.glVertex3d(rX, rY+0.8/2, rZ);
			}
			GL11.glEnd();
			GL11.glBegin(GL11.GL_LINE_LOOP);
			
			for(double i = 0; i <= 360; i+=60) {
				double rX = x+Math.sin(i * Math.PI/180F)* entity.width/3;
				double rZ = z+Math.cos(i * Math.PI/180F)* entity.width/3;
				GL11.glVertex3d(rX, rY+0.8/2, rZ);
			}
			GL11.glEnd();
			break;

		case "rect":
			RenderUtils.color(ColorUtils.getAlphaColor(ColorUtils.mix(ColorUtils.getColor(color, System.nanoTime()/1000000, 0, 5), ColorUtils.getColor(hitColor, System.nanoTime()/1000000, 0, 5), entity.hurtTime ,10), color.getAlpha()));
			RenderUtils.renderHitbox(new AxisAlignedBB(x-size, y+entity.height+0.3, z-size, x+size, y+entity.height+0.1, z+size), GL11.GL_QUADS);
			break;
		case "hitbox":
			RenderUtils.color(ColorUtils.getAlphaColor(ColorUtils.mix(ColorUtils.getColor(color, System.nanoTime()/1000000, 0, 5), ColorUtils.getColor(hitColor, System.nanoTime()/1000000, 0, 5), entity.hurtTime ,10), color.getAlpha()));
			RenderUtils.renderHitbox(bb, GL11.GL_QUADS);
			break;
		}
		
		RenderUtils.stop3D();
		GL11.glPopMatrix();
	}
}
