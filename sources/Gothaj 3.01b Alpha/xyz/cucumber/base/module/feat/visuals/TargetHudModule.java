package xyz.cucumber.base.module.feat.visuals;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventAttack;
import xyz.cucumber.base.events.ext.EventRender3D;
import xyz.cucumber.base.events.ext.EventRenderGui;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.addons.Dragable;
import xyz.cucumber.base.module.settings.BooleanSettings;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.module.settings.NumberSettings;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.Timer;
import xyz.cucumber.base.utils.math.Convertors;
import xyz.cucumber.base.utils.math.PositionHandler;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;
import xyz.cucumber.base.utils.render.StencilUtils;

@ModuleInfo(category = Category.VISUALS, description = "Displays target hud", name = "Target Hud", priority = ArrayPriority.LOW)
public class TargetHudModule extends Mod implements Dragable {
	
	private ModeSettings mode = new ModeSettings("Mode", new String[] {
			"Style 1", "Style 2"
	});
	
	private NumberSettings positionX = new NumberSettings("Position X", 30, 0, 1000, 1);
	private NumberSettings positionY = new NumberSettings("Position Y", 50, 0, 1000, 1);
	
	private BooleanSettings follow = new BooleanSettings("Follow", false);
	
	private BooleanSettings imageRounding = new BooleanSettings("Image rounding", true);
	private BooleanSettings rounded = new BooleanSettings("Rounded", true);
	
	private ColorSettings backgroundColor = new ColorSettings("Background color", "Static", 0xff000000, -1, 50);
	
	private ColorSettings healthColor = new ColorSettings("Health color", "Static", 0xffff0000, -1, 50);
	
	private double animation, openAnimation;
	
	private Timer timer = new Timer();
	
	private EntityPlayer target;
	
	private boolean open;
	
	public TargetHudModule() {
		this.addSettings(
				positionX,
				positionY,
				mode,
				follow,
				imageRounding,
				rounded,
				backgroundColor,
				healthColor
				);
	}
	
	@Override
	public PositionUtils getPosition() {
		if(target == null)return null;
		
		double width = 2+30+8+Fonts.getFont("rb-b").getWidth(target.getName())+8;
		if(width < 110) {
			 width = 110;
		}
		double height = 34;
		if(follow.isEnabled()) return new PositionUtils(0,0,0,0,1);
		
		double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
		return new PositionUtils(pos[0], pos[1], width, height, 1);
	}



	@Override
	public void setXYPosition(double x, double y) {
		this.positionX.setValue(x);
		this.positionY.setValue(y);
	}

	double x;
	double y;
	@EventListener
	public void onRender3D(EventRender3D e) {
		if(follow.isEnabled()) {
			
			
			if(target == null)return;

			if(mc.thePlayer.getDistanceToEntity(target) > 10) {
				target = null;
				return;
			}
			double x1 = (target.prevPosX + (target.posX - target.prevPosX) * e.getPartialTicks()) - mc.getRenderManager().viewerPosX;
			double y1 = (target.prevPosY + (target.posY - target.prevPosY) * e.getPartialTicks()) - mc.getRenderManager().viewerPosY;
			double z1 = (target.prevPosZ + (target.posZ - target.prevPosZ) * e.getPartialTicks()) - mc.getRenderManager().viewerPosZ;
			double width = target.width/2.5;
	        AxisAlignedBB bb = new AxisAlignedBB(x1 - width, y1, z1 - width,x1 + width, y1 + target.height, z1 + width).expand(0.2, 0.1, 0.2);
	        
	        List<double[]> vectors = Arrays.asList(
	    		   new double[] {bb.minX, bb.minY, bb.minZ},
	    		   new double[] {bb.minX, bb.maxY, bb.minZ},
	    		   new double[] {bb.minX, bb.maxY, bb.maxZ},
	    		   new double[] {bb.minX, bb.minY, bb.maxZ},
	    		   new double[] {bb.maxX, bb.minY, bb.minZ},
	    		   new double[] {bb.maxX, bb.maxY, bb.minZ},
	    		   new double[] {bb.maxX, bb.maxY, bb.maxZ},
	    		   new double[] {bb.maxX, bb.minY, bb.maxZ}
	    		   );

	        double[] position = new double[]{Float.MAX_VALUE, Float.MAX_VALUE, -1.0F, -1.0F};

	        for (double[] vec : vectors) {
	        	float[] points = Convertors.convert2D((float) vec[0], (float) vec[1], (float) vec[2], new ScaledResolution(mc).getScaleFactor());
	            if (points != null && points[2] >= 0.0F && points[2] < 1.0F) {
	                final float pX = points[0];
	                final float pY = points[1];
	                position[0] = Math.min(position[0], pX);
	                position[1] = Math.min(position[1], pY);
	                position[2] = Math.max(position[2], pX);
	                position[3] = Math.max(position[3], pY);
	            }
	        }
	        x = position[0];
	        y = position[1]+(position[3]-position[1])/2-17;
		}else  {
			double[] pos = PositionHandler.getScaledPosition(positionX.getValue(), positionY.getValue());
			x = pos[0];
			y = pos[1];
		}
	}
	@EventListener
	public void onRender2D(EventRenderGui e) {
		if(mc.currentScreen instanceof GuiChat && !follow.isEnabled()) {
			target = mc.thePlayer;
			timer.reset();
			open = true;
		}
		if(target == null) {
			open = false;
			return;
		}
		if(open) {
			openAnimation = (openAnimation * 9+1) /10;
			if(timer.hasTimeElapsed(2000, true)) {
				open = false;
			}
		}else {
			openAnimation = (openAnimation * 9) /10;
		}
		
		double width = 2+30+8+Fonts.getFont("rb-b").getWidth(target.getName())+8;
		if(width < 110) {
			 width = 110;
		}
		double height = 34;
		GL11.glPushMatrix();
		GL11.glTranslated(x-x*openAnimation+width/2-width/2*openAnimation, y-y*openAnimation+height/2-height/2*openAnimation, 0);
		GL11.glScaled(openAnimation, openAnimation, 1);
		switch(mode.getMode().toLowerCase()) {
		case "style 1":
			int bg = ColorUtils.getColor(backgroundColor, System.nanoTime()/1000000, 0, 5);
			if(rounded.isEnabled()) {
				if(!backgroundColor.getMode().toLowerCase().equals("mix")) {
					RenderUtils.drawRoundedRect(x, y, x+width, y+height, bg, 3);
				}else {
					RenderUtils.drawMixedRoundedRect(x, y, x+width, y+height, ColorUtils.getAlphaColor(backgroundColor.getMainColor(), backgroundColor.getAlpha()), backgroundColor.getSecondaryColor(),6);
				}
			}
			else {
				if(!backgroundColor.getMode().toLowerCase().equals("mix")) {
					RenderUtils.drawRect(x, y, x+width, y+height, bg);
				}else RenderUtils.drawMixedRect(x, y, x+width, y+height, ColorUtils.getAlphaColor(backgroundColor.getMainColor(), backgroundColor.getAlpha()), backgroundColor.getSecondaryColor());
			}
				
			
			if (mc.getNetHandler().getPlayerInfo(target.getUniqueID()) != null && mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getLocationSkin() != null)
	        {
				if(imageRounding.isEnabled()) {
					StencilUtils.initStencil();
			        GL11.glEnable(GL11.GL_STENCIL_TEST);
			        StencilUtils.bindWriteStencilBuffer();
			        RenderUtils.drawRoundedRect(x+2, y+2,x+32, y+32, 0xff212121, 2.5f);
					
					
					StencilUtils.bindReadStencilBuffer(1);
					RenderUtils.color(ColorUtils.mix(-1, 0xffff0000, target.hurtTime, 10));
			        Minecraft.getMinecraft().getTextureManager().bindTexture(mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getLocationSkin());
			        Gui.drawScaledCustomSizeModalRect(x+2, y+2, 8, 8, 8, 8,30,30, 64F, 64F);
		            
		            StencilUtils.uninitStencilBuffer();
				}else {
					RenderUtils.color(ColorUtils.mix(-1, 0xffff0000, target.hurtTime, 10));
			        Minecraft.getMinecraft().getTextureManager().bindTexture(mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getLocationSkin());
			        Gui.drawScaledCustomSizeModalRect(x+2, y+2, 8, 8, 8, 8,30,30, 64F, 64F);
				}
				
	        }
			
			Fonts.getFont("rb-b").drawString(target.getName(), x+2+30+ (width-32)/2-(Fonts.getFont("rb-b").getWidth(target.getName()))/2,y+ 7, -1);
			
			if(mc.thePlayer.getHealth() == target.getHealth()) {
				Fonts.getFont("rb-b").drawString("Equal", x+2+30+ (width-32)/2-(Fonts.getFont("rb-b").getWidth("Equal"))/2, y+15, 0xffffff63);//W: 63ffb6   E: ffff63   L: ff4a4d
			}else if(mc.thePlayer.getHealth() < target.getHealth()) {
				Fonts.getFont("rb-b").drawString("Losing", x+2+30+ (width-32)/2-(Fonts.getFont("rb-b").getWidth("Losing"))/2, y+15, 0xffff4a4d);//W: 63ffb6   E: ffff63   L: ff4a4d
			}else Fonts.getFont("rb-b").drawString("Winning", x+2+30+ (width-32)/2-(Fonts.getFont("rb-b").getWidth("Winning"))/2, y+15, 0xff63ffb6);//W: 63ffb6   E: ffff63   L: ff4a4d
			
			double size = (width-4)-36;
			if(rounded.isEnabled())
				RenderUtils.drawRoundedRect(x+36, y+24,x+36+size, y+30, 0x90000000, 1);
			else
				RenderUtils.drawRect(x+36, y+24,x+36+size, y+30, 0x90000000);
			
			animation = (animation * 9 +(size/target.getMaxHealth()*target.getHealth()))/10;
			int health = ColorUtils.getColor(healthColor, System.nanoTime()/1000000, 0, 5);
			if(rounded.isEnabled())
				RenderUtils.drawRoundedRect(x+36, y+24, x+36+animation, y+30, health, 1);
			else
				RenderUtils.drawRect(x+36, y+24, x+36+animation, y+30, health);
			break;
		}
		GlStateManager.resetColor();
		GL11.glPopMatrix();
		
		
		/*
		RenderUtils.drawRoundedRect(0, 50, width, 50+height, 0xff212121, 3);
		RenderUtils.drawRoundedRect(2, 52, 32, 82, 0xffffffff, 7);
		
		Fonts.getFont("rb-b").drawString("C0FPacket", 2+30+8, 50+4, -1);
		
		RenderUtils.drawRoundedRect(2+30+4, 50+height/2-3, width-4, 50+height/2-1, 0xff00ff00, 0.5);
		RenderUtils.drawRoundedRect(2+30+4, 50+height/2+1, width-4, 50+height/2+3, 0xff4afff3, 0.5);
		Fonts.getFont("rb-m-13").drawString("100%", 2+30+8+(Fonts.getFont("rb-b").getWidth(target.getName()))/2-Fonts.getFont("rb-m-13").getWidth("100%")/2,  50+height/2+3+4, 0xffaaaaaa);*/
		
	}
	
	@EventListener
	public void onAttack(EventAttack e) {
		if( e.getEntity() instanceof EntityPlayer) {
			target = (EntityPlayer) e.getEntity();
			timer.reset();
			open = true;
		}
	}
	
	public void drawCircle(double x, double y, double radius, int color, double size, double from, double to) {
		RenderUtils.start2D();
		
		RenderUtils.color(color);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		
		for(double i = from; i <= to; i++) {
			GL11.glVertex2d(x+Math.sin(Math.toRadians(i))*radius, y-Math.cos(Math.toRadians(i))*radius);
		}
		GL11.glEnd();
		RenderUtils.stop2D();
	}
	
}
