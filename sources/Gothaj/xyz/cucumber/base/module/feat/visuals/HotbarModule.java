package xyz.cucumber.base.module.feat.visuals;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import xyz.cucumber.base.events.EventListener;
import xyz.cucumber.base.events.ext.EventRenderHotbar;
import xyz.cucumber.base.module.ArrayPriority;
import xyz.cucumber.base.module.Category;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.module.ModuleInfo;
import xyz.cucumber.base.module.settings.ColorSettings;
import xyz.cucumber.base.module.settings.ModeSettings;
import xyz.cucumber.base.utils.MovementUtils;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

@ModuleInfo(category = Category.VISUALS, description = "Displays hotbar", name = "Hotbar", priority = ArrayPriority.LOW)
public class HotbarModule extends Mod{

	
	private ModeSettings mode = new ModeSettings("Mode", new String[] {
			"Style 1", "Style 2"
	});
	
	public ColorSettings textColor = new ColorSettings("Text Color", "Static", -1,-1, 100);
	public ColorSettings backgroundColor = new ColorSettings("Background Color", "Static", -1,-1, 100);
	private double animation;
	
	public HotbarModule() {
		this.addSettings(
				mode,
				textColor,
				backgroundColor
				);
		
	}
	
	@EventListener
	public void onRenderHotbar(EventRenderHotbar e) {
		e.setCancelled(true);
		RenderUtils.drawRect(0, e.getScaledResolution().getScaledHeight_double()-23, e.getScaledResolution().getScaledWidth_double(), e.getScaledResolution().getScaledWidth_double(), ColorUtils.getColor(backgroundColor, System.nanoTime()/1000000, 0, 5));
		
		double centerX = e.getScaledResolution().getScaledWidth()/2-(23*9)/2;
		animation = (animation *10+ 23*mc.thePlayer.inventory.currentItem)/11;
		switch(mode.getMode().toLowerCase()) {
		case "style 1":
			renderGradientCircle(centerX+23/2+animation, e.getScaledResolution().getScaledHeight_double()-23/2,13, 0x77000000);
			for (int j = 0; j < 9; ++j) {
				double k = centerX+23*j;
				Fonts.getFont("rb-b").drawString((j+1)+"", k+23/2-Fonts.getFont("rb-b").getWidth((j+1)+"")/2+0.5, e.getScaledResolution().getScaledHeight_double()-23/2-2, 0xffaaaaaa);
			}
			break;
		case "style 2":
			RenderUtils.drawRect(centerX+animation, e.getScaledResolution().getScaledHeight()-23, centerX+animation+23, e.getScaledResolution().getScaledHeight(), 0x77000000);
			break;
		}
		GL11.glPushMatrix();
		GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();

        for (int j = 0; j < 9; ++j) {
            double k = centerX+3+23*j;
            double l = e.getScaledResolution().getScaledHeight() - 23+3;
            if(mc.thePlayer.inventory.currentItem != j) {
            	e.getGuiIngame().renderHotbarItem(j, (int) k, (int) l, e.getPartialTicks(), mc.thePlayer);
            }
            else {GL11.glPushMatrix();
            	GL11.glScaled(1.05, 1.05, 1);
            	e.getGuiIngame().renderHotbarItem(j, (int) (k-k*0.05)+1, (int) (l-l*0.05)+1, e.getPartialTicks(), mc.thePlayer);
            	GL11.glScaled(1, 1, 1);
            	GL11.glPopMatrix();
            }
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
        
        NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
        int ping =0;
        if (networkPlayerInfo != null) {
        	ping = networkPlayerInfo.getResponseTime();
        }
        
        double size = 5+Fonts.getFont("rb-b").getWidth("FPS: ") + Fonts.getFont("rb-m").getWidth(mc.debugFPS+"")+5
        		+Fonts.getFont("rb-b").getWidth("Ping: ") + Fonts.getFont("rb-m").getWidth(ping+"")+5;
        //Coords
        renderCustomText("X:", 5, e.getScaledResolution().getScaledHeight()-19);
        Fonts.getFont("rb-m").drawString(Math.round(mc.thePlayer.posX)+"", 5+Fonts.getFont("rb-b").getWidth("X: "), e.getScaledResolution().getScaledHeight()-19, 0xffaaaaaa);
        
        renderCustomText("Y:", 5+Fonts.getFont("rb-b").getWidth("X: ") + Fonts.getFont("rb-m").getWidth(Math.round(mc.thePlayer.posX)+"")+
        		5, e.getScaledResolution().getScaledHeight()-19);
        Fonts.getFont("rb-m").drawString(Math.round(mc.thePlayer.posY)+"", 5+Fonts.getFont("rb-b").getWidth("X: ") + Fonts.getFont("rb-m").getWidth(Math.round(mc.thePlayer.posX)+"")+
        		5+Fonts.getFont("rb-b").getWidth("Y: "), e.getScaledResolution().getScaledHeight()-19, 0xffaaaaaa);
        
        renderCustomText("Y:", 5+Fonts.getFont("rb-b").getWidth("X: ") + Fonts.getFont("rb-m").getWidth(Math.round(mc.thePlayer.posX)+"")+
        		5, e.getScaledResolution().getScaledHeight()-19);
        renderCustomText("Z:", 5+Fonts.getFont("rb-b").getWidth("X: ") + Fonts.getFont("rb-m").getWidth(Math.round(mc.thePlayer.posX)+"")+
        		5+Fonts.getFont("rb-b").getWidth("Y: ") + Fonts.getFont("rb-m").getWidth(Math.round(mc.thePlayer.posY)+"")
        		+5, e.getScaledResolution().getScaledHeight()-19);
        Fonts.getFont("rb-m").drawString(Math.round(mc.thePlayer.posZ)+"",  5+Fonts.getFont("rb-b").getWidth("X: ") + Fonts.getFont("rb-m").getWidth(Math.round(mc.thePlayer.posX)+"")+
        		5+Fonts.getFont("rb-b").getWidth("Y: ") + Fonts.getFont("rb-m").getWidth(Math.round(mc.thePlayer.posY)+"")
        		+5+Fonts.getFont("rb-b").getWidth("Z: "), e.getScaledResolution().getScaledHeight()-19, 0xffaaaaaa);
        
        //FPS
        renderCustomText("FPS:", 5, e.getScaledResolution().getScaledHeight()-8);
        Fonts.getFont("rb-m").drawString(Math.round(mc.debugFPS)+"", 5+Fonts.getFont("rb-b").getWidth("FPS: ") , e.getScaledResolution().getScaledHeight()-8, 0xffaaaaaa);
        //Ping
        renderCustomText("Ping:", 5+Fonts.getFont("rb-b").getWidth("FPS: ") + Fonts.getFont("rb-m").getWidth(mc.debugFPS+"")+5, e.getScaledResolution().getScaledHeight()-8);
        Fonts.getFont("rb-m").drawString(ping+"", 5+Fonts.getFont("rb-b").getWidth("FPS: ") + Fonts.getFont("rb-m").getWidth(mc.debugFPS+"")+5
        		+Fonts.getFont("rb-b").getWidth("Ping: ") , e.getScaledResolution().getScaledHeight()-8, 0xffaaaaaa);
        
        //BPS
        double deltaX = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
        
        double deltaZ = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ; 
        double bps = Math.round(Math.sqrt(deltaX * deltaX + deltaZ * deltaZ)*100F*20)/100D;
        renderCustomText("BPS:", 5+Fonts.getFont("rb-b").getWidth("FPS: ") + Fonts.getFont("rb-m").getWidth(mc.debugFPS+"")+5
        		+Fonts.getFont("rb-b").getWidth("Ping: ")+Fonts.getFont("rb-m").getWidth(ping+"")+5, e.getScaledResolution().getScaledHeight()-8);
        Fonts.getFont("rb-m").drawString(bps+"", 5+Fonts.getFont("rb-b").getWidth("FPS: ") + Fonts.getFont("rb-m").getWidth(mc.debugFPS+"")+5
        		+Fonts.getFont("rb-b").getWidth("Ping: ")+Fonts.getFont("rb-m").getWidth(ping+"")+5+Fonts.getFont("rb-b").getWidth("BPS: ") , e.getScaledResolution().getScaledHeight()-8, 0xffaaaaaa);
	}
	
	public void renderCustomText(String text, double x, double y) {
		String[] s = text.split("");
		double w = 0;
		for(String t : s) {
			Fonts.getFont("rb-b").drawStringWithShadow(t, x+w, y, ColorUtils.getColor(textColor, System.nanoTime()/1000000, w, 5), 0x40000000);
			w+= Fonts.getFont("rb-b").getWidth(t);
		}
	}
	
	public void renderGradientCircle(double x, double y, double r, int color) {
		GL11.glPushMatrix();
		
		RenderUtils.start2D();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		RenderUtils.color(color);
		GL11.glVertex2d(x, y);
		for(int i = 0; i<=360; i+=10) {
			RenderUtils.color(ColorUtils.getAlphaColor(color, 0));
			GL11.glVertex2d(x+Math.sin(i * Math.PI /180)*r, y-Math.cos(i * Math.PI /180)*r);
		}
		
		GL11.glEnd();
		RenderUtils.stop2D();
		
		GL11.glPopMatrix();
	}
}
