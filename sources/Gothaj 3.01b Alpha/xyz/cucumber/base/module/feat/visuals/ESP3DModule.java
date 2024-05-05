package xyz.cucumber.base.module.feat.visuals;

import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
@ModuleInfo(category = Category.VISUALS, description = "Allows you see players behind obsticles", name = "ESP3D")
public class ESP3DModule extends Mod{

	public ModeSettings mode = new ModeSettings("Mode", new String[] {
			"Hitbox", "Circle"
	});
	public BooleanSettings filled = new BooleanSettings("Filled", true);
	public ColorSettings color = new ColorSettings("Fill Color", "Static", -1,-1, 50);
	public BooleanSettings outline = new BooleanSettings("Outline", true);
	public ColorSettings outlineColor = new ColorSettings("Outline Color", "Static", -1,-1, 100);
	
	public ColorSettings hurtColor = new ColorSettings("Hit Fill Color", "Static", -1,-1, 100);
	public ColorSettings hurtOutlineColor = new ColorSettings("Hit Outline Color", "Static", -1,-1, 100);
	
	public ColorSettings murdColor = new ColorSettings("Murder Fill Color", "Static", -1,-1, 100);
	public ColorSettings murdOutlineColor = new ColorSettings("Murder Outline Color", "Static", -1,-1, 100);
	
	
	
	public NumberSettings size = new NumberSettings("Size", 1, 0.1, 2, 0.05);
	
	public ESP3DModule() {
		this.addSettings(
				size,
				mode,
				filled,
				color,
				outline,
				outlineColor,
				hurtColor,
				hurtOutlineColor,
				murdColor,
				murdOutlineColor
				);
	}
	
	@EventListener
	public void onRender3D(EventRender3D e) {
		GL11.glPushMatrix();
		
		for(EntityPlayer player : mc.theWorld.playerEntities) {
			if(!RenderUtils.isInViewFrustrum(player) || mc.thePlayer == player) continue;
			GL11.glPushMatrix();
			
			double x = (player.prevPosX + (player.posX - player.prevPosX) * e.getPartialTicks()) - mc.getRenderManager().viewerPosX;
			double y = (player.prevPosY + (player.posY - player.prevPosY) * e.getPartialTicks()) - mc.getRenderManager().viewerPosY;
			double z = (player.prevPosZ + (player.posZ - player.prevPosZ) * e.getPartialTicks()) - mc.getRenderManager().viewerPosZ;
			
			switch(mode.getMode().toLowerCase()) {
			case "hitbox":
				double width = player.width*size.getValue()/2;
				AxisAlignedBB bb = new AxisAlignedBB(x-width,y+player.height+(player.isSneaking() ? 0 : 0.2),z-width,x+width,y,z+width);
				GL11.glPushMatrix();
				RenderUtils.start2D();
				
				if(filled.isEnabled()) {
					int color = ColorUtils.getAlphaColor(ColorUtils.mix(ColorUtils.getColor(this.color, System.nanoTime()/1000000, 0, 5), ColorUtils.getColor(hurtColor, System.nanoTime()/1000000, 0, 5), player.hurtTime, 10), this.color.getAlpha());
					if(Client.INSTANCE.getModuleManager().getModule(MurderFinderModule.class).isEnabled()) {
						MurderFinderModule mod = (MurderFinderModule) Client.INSTANCE.getModuleManager().getModule(MurderFinderModule.class);
						for(Entry<String,Entity> entry : mod.murders.entrySet()) {
							if(entry.getKey().equals(player.getName())) {
								color = ColorUtils.getAlphaColor(ColorUtils.mix(ColorUtils.getColor(murdColor, System.nanoTime()/1000000, 0, 5), ColorUtils.getColor(hurtColor, System.nanoTime()/1000000, 0, 5), player.hurtTime, 10), murdColor.getAlpha());
			        			break;
			        		}
						}
					}
					RenderUtils.color(color);
					RenderUtils.renderHitbox(bb, GL11.GL_QUADS);
				}
				if(outline.isEnabled()) {
					GL11.glLineWidth(0.5F);
					int color = ColorUtils.getAlphaColor(ColorUtils.mix(ColorUtils.getColor(outlineColor, System.nanoTime()/1000000, 0, 5), ColorUtils.getColor(hurtOutlineColor, System.nanoTime()/1000000, 0, 5), player.hurtTime, 10), outlineColor.getAlpha());
					if(Client.INSTANCE.getModuleManager().getModule(MurderFinderModule.class).isEnabled()) {
						MurderFinderModule mod = (MurderFinderModule) Client.INSTANCE.getModuleManager().getModule(MurderFinderModule.class);
						for(Entry<String,Entity> entry : mod.murders.entrySet()) {
							if(entry.getKey().equalsIgnoreCase(player.getName())) {
								color = ColorUtils.getAlphaColor(ColorUtils.mix(ColorUtils.getColor(murdOutlineColor, System.nanoTime()/1000000, 0, 5), ColorUtils.getColor(hurtColor, System.nanoTime()/1000000, 0, 5), player.hurtTime, 10), murdOutlineColor.getAlpha());
			        			break;
			        		}
						}
					}
					RenderUtils.color(color);
					RenderUtils.renderHitbox(bb, GL11.GL_LINE_LOOP);
				}
				RenderUtils.stop2D();
				GL11.glPopMatrix();
				break;
			case "circle":
				width = player.width*size.getValue()/2;
				GL11.glPushMatrix();
				RenderUtils.start2D();
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glBegin(GL11.GL_QUAD_STRIP);
				
				for(double i = 0; i <=360; i+=10) {
					double px = x+Math.sin(i * Math.PI / 180)*width;
					double pz = z-Math.cos(i * Math.PI / 180)*width;
					int color = ColorUtils.getAlphaColor(ColorUtils.mix(ColorUtils.getColor(this.color, System.nanoTime()/1000000, i, 5), ColorUtils.getColor(hurtColor, System.nanoTime()/1000000, i, 5), player.hurtTime, 10), this.color.getAlpha());
					if(Client.INSTANCE.getModuleManager().getModule(MurderFinderModule.class).isEnabled()) {
						MurderFinderModule mod = (MurderFinderModule) Client.INSTANCE.getModuleManager().getModule(MurderFinderModule.class);
						for(Entry<String,Entity> entry : mod.murders.entrySet()) {
							if(entry.getKey().equalsIgnoreCase(player.getName())) {
								color = ColorUtils.getAlphaColor(ColorUtils.mix(ColorUtils.getColor(murdColor, System.nanoTime()/1000000, 0, 5), ColorUtils.getColor(hurtColor, System.nanoTime()/1000000, 0, 5), player.hurtTime, 10), murdColor.getAlpha());
			        			break;
			        		}
						}
					}
					RenderUtils.color(color);
					GL11.glVertex3d(px, y, pz);
					RenderUtils.color(color, 0);
					GL11.glVertex3d(px, y+1, pz);
				}
				GL11.glEnd();
				RenderUtils.stop2D();
				GL11.glPopMatrix();
				break;
			}
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
	}
	
}
