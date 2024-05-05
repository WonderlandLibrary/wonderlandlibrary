package Velo.api.ClickGui.Display;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import org.lwjgl.opengl.GL11;

import Velo.api.ClickGui.Util.Checkbox;
import Velo.api.ClickGui.Util.ClickGuiRenderUtils;
import Velo.api.ClickGui.Util.Component;
import Velo.api.ClickGui.Util.PictureButtons;
import Velo.api.ClickGui.Util.Slider;
import Velo.api.Main.Main;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Module.Module.Category;
import Velo.api.Util.Other.ColorUtil;
import Velo.api.Util.Other.UrlTextureUtil;
import Velo.api.Util.Render.RenderUtil;
import Velo.api.Util.Render.ScaledRes;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.api.Util.fontRenderer.Utils.DrawFontUtil;
import Velo.api.setting.Setting;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import Velo.impl.Modules.combat.Killaura;
import Velo.impl.Modules.visuals.hud.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class DisplayClickGui extends GuiScreen {
	
	ArrayList<Module> modulesArray;
	ArrayList<Setting> modulesSetting;
	private ArrayList<Component> subcomponents;
	private ResourceLocation img;
	private boolean hasTriedToDownload;
	public static String button = null;
	private boolean dragging1 = false;
	public boolean dragging = false;
	public boolean keybindFocused = false;
	int x = 0;
	int y = 0;
	public DisplayClickGui() {
	    GlStateManager.color( 255f, 255f, 255f);
		modulesArray = new ArrayList<>(); 
		modulesSetting = new ArrayList<>(); 
	}
	
	@Override
	public void initGui() {
	    GlStateManager.color( 255f, 255f, 255f);
		float widthxd  = (float) (width / 7.55);
		float HxW = 16;
	      for (int i = 0; i <   CombatClick.length; i++) {
	        	CombatClick[i].setWidth(HxW);
	        	CombatClick[i].setHeight(HxW);
	        	CombatClick[i].setX(widthxd);
	        	CombatClick[i].setY(100);
	        	if(button == "Combat") {
	        		CombatClick[i].setColor(Color.blue);
	        	}else {
	        		CombatClick[i].setColor(Color.white);
	        	}
	        }
	        
	        for (int i = 0; i <   MovementClick.length; i++) {
	        	MovementClick[i].setWidth(HxW);
	        	MovementClick[i].setHeight(HxW);
	        	MovementClick[i].setX(widthxd);
	        	MovementClick[i].setY(135);
	         	if(button == "Movement") {
	         		MovementClick[i].setColor(Color.blue);
	        	}else {
	        		MovementClick[i].setColor(Color.white);
	        	}
	        }
	        
	      
	        for (int i = 0; i <   PlayerClick.length; i++) {
	        	PlayerClick[i].setWidth(HxW);
	        	PlayerClick[i].setHeight(HxW);
	        	PlayerClick[i].setX(widthxd);
	        	PlayerClick[i].setY(170);
	         	if(button == "Player") {
	         		PlayerClick[i].setColor(Color.blue);
	        	}else {
	        		PlayerClick[i].setColor(Color.white);
	        	}
	        }
	        for (int i = 0; i <   ExploitClick.length; i++) {
	        	ExploitClick[i].setWidth(HxW);
	        	ExploitClick[i].setHeight(HxW);
	        	ExploitClick[i].setX(widthxd);
	        	ExploitClick[i].setY(205);
	         	if(button == "Exploit") {
	         		ExploitClick[i].setColor(Color.blue);
	        	}else {
	        		ExploitClick[i].setColor(Color.white);
	        	}
	        }

	        for (int i = 0; i <   OtherClick.length; i++) {
	         	if(button == "Other") {
	         		OtherClick[i].setColor(Color.blue);
	        	}else {
	        		OtherClick[i].setColor(Color.white);
	        	}
	        	OtherClick[i].setWidth(HxW);
	        	OtherClick[i].setHeight(HxW);
	        	OtherClick[i].setX(widthxd);
	        	OtherClick[i].setY(240);
	        }
	        for (int i = 0; i <   VisualsClick.length; i++) {
	        	VisualsClick[i].setWidth(HxW);
	        	VisualsClick[i].setHeight(HxW);
	        	VisualsClick[i].setX(widthxd);
	        	VisualsClick[i].setY(275);
	        	VisualsClick[i].setColor(Color.white);
	         	if(button == "Visual") {
	         		VisualsClick[i].setColor(Color.blue);
	        	}else {
	        		VisualsClick[i].setColor(Color.white);
	        	}
	        }
		super.initGui();
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		if(dragging1) {
			 x = mouseX - 188;
			 y = mouseY - 55;
		}
		 GL11.glColor4f(Color.WHITE.getRed(), Color.WHITE.getBlue(), Color.WHITE.getGreen(), Color.WHITE.getAlpha());
	  
		Gui.drawRect(ScaledRes.getScaledWidth()/2f + 340 + x, ScaledRes.getScaledHeight()/2f -180+ y, ScaledRes.getScaledWidth()/2f - 325+ x, ScaledRes.getScaledHeight()/2f - 200 + y, 0xff111111);
		Gui.drawRect(ScaledRes.getScaledWidth()/2f + 340 + x, ScaledRes.getScaledHeight()/2f + 200 + y, ScaledRes.getScaledWidth()/2f - 365+ x, ScaledRes.getScaledHeight()/2f - 200 + y,  0xff111111);
		 GL11.glColor4f(Color.WHITE.getRed(), Color.WHITE.getBlue(), Color.WHITE.getGreen(), Color.WHITE.getAlpha());   
		Gui.drawRect(ScaledRes.getScaledWidth()/2f - 220 + x, 454 + y, ScaledRes.getScaledWidth()/2f - 325+ x, 419f + y, new Color(66, 66, 66, 255).getRGB());
		Gui.drawRect(ScaledRes.getScaledWidth()/2f + 340 + x, ScaledRes.getScaledHeight()/2f + 165 + y, ScaledRes.getScaledWidth()/2f - 305+ x, ScaledRes.getScaledHeight()/2f - 180 + y, new Color(35, 35, 35, 255).getRGB());
		Gui.drawRect(ScaledRes.getScaledWidth()/2f + 320 + x, ScaledRes.getScaledHeight()/2f + 165 + y, ScaledRes.getScaledWidth()/2f - 325+ x, ScaledRes.getScaledHeight()/2f - 150 + y, new Color(35, 35, 35, 255).getRGB());
		ClickGuiRenderUtils.drawRoundedRect(ScaledRes.getScaledWidth()/2f -325 + x, ScaledRes.getScaledHeight()/2f - 180 + y, ScaledRes.getScaledWidth()/2f - 375+ x, ScaledRes.getScaledHeight()/2f - 180 + y, 25);
		Gui.drawRect(ScaledRes.getScaledWidth()/2f + 340 + x, ScaledRes.getScaledHeight()/2f + 200 + y, ScaledRes.getScaledWidth()/2f - 220+ x, ScaledRes.getScaledHeight()/2f - 180 + y, new Color(45, 45, 45, 255).getRGB());
		   Gui.drawRect(ScaledRes.getScaledWidth()/2f - 220 + x, 454 + y , ScaledRes.getScaledWidth()/2f - 325+ x, 420.5f + y, new Color(35, 35, 35, 255).getRGB());
			Fonts.targethud.drawString(mc.thePlayer.getName(), 188 + x, 423+ y, -1);
			Fonts.noti.drawString("Playing " + GuiConnecting.adress, 188 + x, 436+ y, new Color(150, 150, 150).getRGB());
		//	Main.getDiscordRP()
			RenderUtil.drawFilledCircle((int)(ScaledRes.getScaledWidth()/2f + -345 + x), (int)(ScaledRes.getScaledHeight()/2f - 182 + y), 15d,  new Color(35, 35, 35, 255).getRGB());
			
		
			RenderUtil.drawFilledCircle((int)(ScaledRes.getScaledWidth()/2f  - 345 + x), (int)(ScaledRes.getScaledHeight()/2f - 40 + y), 13d,  new Color(35, 35, 35, 255).getRGB());
			RenderUtil.drawFilledCircle((int)(ScaledRes.getScaledWidth()/2f  - 345 + x), (int)(ScaledRes.getScaledHeight()/2f - 146 + y), 13d,  new Color(35, 35, 35, 255).getRGB());
			RenderUtil.drawFilledCircle((int)(ScaledRes.getScaledWidth()/2f  - 345 + x), (int)(ScaledRes.getScaledHeight()/2f - 76 + y), 13d,  new Color(35, 35, 35, 255).getRGB());
			RenderUtil.drawFilledCircle((int)(ScaledRes.getScaledWidth()/2f  - 345 + x), (int)(ScaledRes.getScaledHeight()/2f - 111 + y), 13d,  new Color(35, 35, 35, 255).getRGB());
			RenderUtil.drawFilledCircle((int)(ScaledRes.getScaledWidth()/2f  - 345 + x), (int)(ScaledRes.getScaledHeight()/2f + 29 + y), 13d,  new Color(35, 35, 35, 255).getRGB());
			RenderUtil.drawFilledCircle((int)(ScaledRes.getScaledWidth()/2f  - 345 + x), (int)(ScaledRes.getScaledHeight()/2f - 6 + y), 13d,  new Color(35, 35, 35, 255).getRGB());







			
			
			
			Fonts.mm2.drawString("I", 133 + x, 62+ y, new Color(125, 125, 125, 255).getRGB());


			
			GlStateManager.pushMatrix();
	           GL11.glColor3d(255, 255, 255);
               GlStateManager.color(255, 255, 255, 255);
			int count = 0;
			for(Category c : Module.Category.values()) {
	
			      GlStateManager.color(255, 255, 255, 255);
				boolean hoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count*49 - 145 && mouseY <= (ScaledRes.getScaledHeight()/2f + count*49 - 145) + DrawFontUtil.renderer2.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 225 && mouseX <= ScaledRes.getScaledWidth()/2f - 225 + DrawFontUtil.renderer2.getStringWidth("" + c.name.charAt(0));
			       for (int i = 0; i < CombatClick.length; i++) {
			
			    	      GlStateManager.color(255, 255, 255, 255);
			                    mc.getTextureManager().bindTexture(new ResourceLocation("Velo/combat.png"));
			                   
			               Gui.drawModalRectWithCustomSizedTexture((float) CombatClick[i].getX(), (float) CombatClick[i].getY(), 0, 0, (float) CombatClick[i].getWidth(),
			                    (float) CombatClick[i].getHeight(), (float) CombatClick[i].getWidth(), (float) CombatClick[i].getHeight());
	
		                   GL11.glColor3d(255, 255, 255);
		                   GlStateManager.color(255, 255, 255, 255);
			        
			        }
			     
				count+= 1;
			}
	    	
	    	for(Category c : Module.Category.values()) {
	   	//	 ClickGuiRenderUtils.color(0xff0000ff);
						       for (int i = 0; i < VisualsClick.length; i++) {
						    		boolean hoveringOver = mouseY >= VisualsClick[i].getHeight() + count*49 - VisualsClick[i].getY() && mouseY <= (VisualsClick[i].getHeight()/2f + count*49 - 145) + DrawFontUtil.renderer2.getFontHeight() && mouseX >= VisualsClick[i].getWidth() - 225 && mouseX <= VisualsClick[i].getWidth() - 225 + DrawFontUtil.renderer2.getStringWidth("" + c.name.charAt(0));
			    	
			 if(button == "Visual") {
				// ClickGuiRenderUtils.color(0xff0000ff);
			
			
			                    mc.getTextureManager().bindTexture(new ResourceLocation("Velo/visuals.png"));
			               //     GL11.glColor3f(0, 255, 0);
			      //              GlStateManager.color(000, 000, 000);
			               Gui.drawModalRectWithCustomSizedTexture((float) VisualsClick[i].getX(), (float) VisualsClick[i].getY(), 0, 0, (float) VisualsClick[i].getWidth(),
			                    (float) VisualsClick[i].getHeight(), (float) VisualsClick[i].getWidth(), (float) VisualsClick[i].getHeight()); 
			          //  ClickGuiRenderUtils.color(Color.blue.getRGB());
			 }else {
			//	 GL11.glColor4f(Color.WHITE.getRed(), Color.WHITE.getBlue(), Color.WHITE.getGreen(), Color.WHITE.getAlpha());
				 GlStateManager.pushMatrix();
			      mc.getTextureManager().bindTexture(new ResourceLocation("Velo/visuals.png"));
			//	   GL11.glColor4f(255.0F, 255.0F, 255.0F, 255.0F);
			 //     ClickGuiRenderUtils.color(0xff0000ff);
		            Gui.drawModalRectWithCustomSizedTexture((float) VisualsClick[i].getX(), (float) VisualsClick[i].getY(), 0, 0, (float) VisualsClick[i].getWidth(),
		                    (float) VisualsClick[i].getHeight(), (float) VisualsClick[i].getWidth(), (float) VisualsClick[i].getHeight()); 
		        //    Gui.drawRect(0, 0, 2, 2, 0xff0000ff);
		         //   ClickGuiRenderUtils.color(0xff0000ff);
		            GlStateManager.popMatrix();
			 }
			        
			        }		
				count+= 1;
			}
	    	for(Category c : Module.Category.values()) {
	    		
				boolean hoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count*49 - 145 && mouseY <= (ScaledRes.getScaledHeight()/2f + count*49 - 145) + DrawFontUtil.renderer2.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 225 && mouseX <= ScaledRes.getScaledWidth()/2f - 225 + DrawFontUtil.renderer2.getStringWidth("" + c.name.charAt(0));
			       for (int i = 0; i < MovementClick.length; i++) {
			 GlStateManager.pushMatrix();
			                    mc.getTextureManager().bindTexture(new ResourceLocation("Velo/movement.png"));
			               Gui.drawModalRectWithCustomSizedTexture((float) MovementClick[i].getX(), (float) MovementClick[i].getY(), 0, 0, (float) MovementClick[i].getWidth(),
			                    (float) MovementClick[i].getHeight(), (float) MovementClick[i].getWidth(), (float) MovementClick[i].getHeight());          
			            GlStateManager.popMatrix();
			        }		
				count+= 1;
			}
	       	for(Category c : Module.Category.values()) {
	    		
	    			boolean hoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count*49 - 145 && mouseY <= (ScaledRes.getScaledHeight()/2f + count*49 - 145) + DrawFontUtil.renderer2.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 225 && mouseX <= ScaledRes.getScaledWidth()/2f - 225 + DrawFontUtil.renderer2.getStringWidth("" + c.name.charAt(0));
	    		       for (int i = 0; i < OtherClick.length; i++) {
	    		 GlStateManager.pushMatrix();
	    		                    mc.getTextureManager().bindTexture(new ResourceLocation("Velo/other.png"));
	    		               Gui.drawModalRectWithCustomSizedTexture((float) OtherClick[i].getX(), (float) OtherClick[i].getY(), 0, 0, (float) OtherClick[i].getWidth(),
	    		                    (float) OtherClick[i].getHeight(), (float) OtherClick[i].getWidth(), (float) OtherClick[i].getHeight());          
	    		            GlStateManager.popMatrix();
	    		        }		
	    			count+= 1;
	    		}
	       	for(Category c : Module.Category.values()) {
	    		
	    			boolean hoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count*49 - 145 && mouseY <= (ScaledRes.getScaledHeight()/2f + count*49 - 145) + DrawFontUtil.renderer2.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 225 && mouseX <= ScaledRes.getScaledWidth()/2f - 225 + DrawFontUtil.renderer2.getStringWidth("" + c.name.charAt(0));
	    		       for (int i = 0; i < ExploitClick.length; i++) {
	    		 GlStateManager.pushMatrix();
	    		                    mc.getTextureManager().bindTexture(new ResourceLocation("Velo/exploit.png"));
	    		               Gui.drawModalRectWithCustomSizedTexture((float) ExploitClick[i].getX(), (float) ExploitClick[i].getY(), 0, 0, (float) ExploitClick[i].getWidth(),
	    		                    (float) ExploitClick[i].getHeight(), (float) ExploitClick[i].getWidth(), (float) ExploitClick[i].getHeight());          
	    		            GlStateManager.popMatrix();
	    		        }		
	    			count+= 1;
	    		}
	       	for(Category c : Module.Category.values()) {
	    		
	    			boolean hoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count*49 - 145 && mouseY <= (ScaledRes.getScaledHeight()/2f + count*49 - 145) + DrawFontUtil.renderer2.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 225 && mouseX <= ScaledRes.getScaledWidth()/2f - 225 + DrawFontUtil.renderer2.getStringWidth("" + c.name.charAt(0));
	    		       for (int i = 0; i < PlayerClick.length; i++) {
	    		 GlStateManager.pushMatrix();
	    		                    mc.getTextureManager().bindTexture(new ResourceLocation("Velo/player.png"));
	    		               Gui.drawModalRectWithCustomSizedTexture((float) PlayerClick[i].getX(), (float) PlayerClick[i].getY(), 0, 0, (float) PlayerClick[i].getWidth(),
	    		                    (float) PlayerClick[i].getHeight(), (float) PlayerClick[i].getWidth(), (float) PlayerClick[i].getHeight());          
	    		            GlStateManager.popMatrix();
	    		        }		
	    			count+= 1;
	    		}
	    	
	        GlStateManager.popMatrix();
			
			
		if(this.img == null && !this.hasTriedToDownload) {
			this.hasTriedToDownload = true;
	    UrlTextureUtil.downloadAndSetTexture("https://mc-heads.net/avatar/" + mc.thePlayer.getName(), new UrlTextureUtil.ResourceLocationCallback() {
	    	
            @Override
            public void onTextureLoaded(final ResourceLocation rl) {
                DisplayClickGui.access$0(DisplayClickGui.this, rl);
            }
        });
		}
		
		
		
        if (this.img != null ) {
            final int b = 23;
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            this.mc.getTextureManager().bindTexture(this.img);
            Gui.drawModalRectWithCustomSizedTexture(158 + x, 423 + y, 0.6f, 0.6f, b, b, (float)b, (float)b);
            GL11.glPopMatrix();
        }
		if(!modulesArray.isEmpty()) {
			for(Category c : Module.Category.values()) {
		//	DrawFontUtil.renderer2.drawString("" + c.name, ScaledRes.getScaledWidth()/2f - 225, ScaledRes.getScaledHeight()/2f + count*49 - 135, hoveringOver ? -1 : Color.GRAY.hashCode(), true);
			float count2 = 0;
			for(int i = 0; i < modulesArray.toArray().length; i++) {
				Fonts.notides.drawString("" + modulesArray.get(i).name + " ", ScaledRes.getScaledWidth()/2f - 316, ScaledRes.getScaledHeight()/2f + count2*13 - 157, modulesArray.get(i).isEnabled() ? -1 : Color.GRAY.hashCode(), true);
				
				count2+= 1.3;
				}
			}
		}
		
		

		
		if(!modulesSetting.isEmpty()) {
			int count3 = 0;
			for(int i = 0; i < modulesSetting.toArray().length; i++) {
				Setting setting = modulesSetting.get(i);
				if(setting instanceof ModeSetting) {
					ModeSetting mode = (ModeSetting) setting;
					Fonts.notides.drawString(modulesSetting.get(i).name + ": " + mode.modes.get(mode.index), ScaledRes.getScaledWidth()/2f - 210, ScaledRes.getScaledHeight()/2f + count3*16 - 157, -1, true);
				}
				if(setting instanceof BooleanSetting) {
					BooleanSetting bool = (BooleanSetting) setting;
				//	this.subcomponents.add(new Checkbox(bool, (int)ScaledRes.getScaledWidth()/2 - 210, (int)ScaledRes.getScaledHeight()/2 + count3*16 - 157));
					Checkbox poo = new Checkbox((BooleanSetting) setting, (int)ScaledRes.getScaledWidth()/2 - 210, (int)ScaledRes.getScaledHeight()/2 + count3*16 - 157);
					poo.renderComponent();
					//Fonts.notides.drawString(modulesSetting.get(i).name, ScaledRes.getScaledWidth()/2f - 210, ScaledRes.getScaledHeight()/2f + count3*16 - 157, bool.isEnabled() ? -1 : Color.lightGray.hashCode(), true);
					
				}
				if(setting instanceof NumberSetting) {				
					NumberSetting number = (NumberSetting) setting;
					double numberbar = (number.getValue() - number.getMinimum())/(number.getMaximum() - number.getMinimum());
				//	boolean isHoveringOver2 = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 157 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 144 && mouseX >= ScaledRes.getScaledWidth()/2f - 210 && mouseX <= ScaledRes.getScaledWidth()/2f + 210;
				//	boolean isHoveringOver2 = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 157 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 144 && mouseX >= ScaledRes.getScaledWidth()/2f - 25 && mouseX <= ScaledRes.getScaledWidth()/2f + 255;
					boolean isHoveringOver2 = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 157 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 144 && mouseX >= ScaledRes.getScaledWidth()/2f - 210 && mouseX <= ScaledRes.getScaledWidth()/2f + 270;
					
					//	boolean isHoveringOver2 = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 157 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 144 && mouseX >= ScaledRes.getScaledWidth()/2f - 210 && mouseX <= ScaledRes.getScaledWidth()/2f + 52;
		//boolean isHoveringOver2 = mouseY >= sr.getScaledHeight()/2f + count3*16 - 157 && mouseY <= sr.getScaledHeight()/2f + count3*16 - 144 && mouseX >= sr.getScaledWidth()/2f - 100 && mouseX <= sr.getScaledWidth()/2f + 0;
		for(int i1 = 0; i1 < 85; i1++) {
			float hue1 = System.currentTimeMillis() % (int)((100.5f - 9) * 1000) / (float)((100.5f - 8) * 1000);
		int color =  ColorUtil.getGradientOffset(new Color((int) HUD.red2.getValue(), (int) HUD.green2.getValue(), (int) HUD.blue2.getValue(), 255), new Color((int) HUD.red.getValue(), (int) HUD.green.getValue(), (int) HUD.blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7 - i1)) / 120D)).getRGB();
		Slider poo = new Slider((NumberSetting) setting, (int)ScaledRes.getScaledWidth()/2 - 210, (int)ScaledRes.getScaledHeight()/2 + count3*16 - 157);
		poo.renderComponent();
		poo.updateComponent(mouseX, mouseY);
			//Gui.drawRect(ScaledRes.getScaledWidth()/2f - 210, ScaledRes.getScaledHeight()/2f + count3*16 - 157, ScaledRes.getScaledWidth()/2f + 52, ScaledRes.getScaledHeight()/2f + count3*16 - 144, 0x901f1f1f);
		//Gui.drawRect(ScaledRes.getScaledWidth()/2f - 210, ScaledRes.getScaledHeight()/2f + count3*16 - 157, (float) (ScaledRes.getScaledWidth()/2f - 210 + (numberbar * 262)), ScaledRes.getScaledHeight()/2f + count3*16 - 144, 0xff0090ff);
	//Fonts.notides.drawString(modulesSetting.get(i).name + ": " + number.getValue(), ScaledRes.getScaledWidth()/2f - 210, ScaledRes.getScaledHeight()/2f + count3*16 - 157, -1, true);
		
	if(isHoveringOver2) {
		if(dragging) {
			double diff = number.getMaximum() - number.getMinimum();
			double val = number.getMinimum() + (MathHelper.clamp_double((mouseX - ScaledRes.getScaledWidth()/2f + 209) / (ScaledRes.getScaledWidth()/2f - 389), 0, 1)) * diff;
 		number.setValue(val);	
		}
	}
}
				}
				count3++;
			}
			
		}
		
	}
	
    static /* synthetic */ void access$0(final DisplayClickGui ka, final ResourceLocation img) {
        ka.img = img;
    }
	
    public static void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(2, 2, 8f, 8f, 8, 8, width, height, 64f, 64f);
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
	     for (PictureButtons circle : CombatClick) {
	            circle.mouseClicked(mouseX, mouseY, mouseButton);
	        }
	     for (PictureButtons circle1 : ExploitClick) {
	            circle1.mouseClicked(mouseX, mouseY, mouseButton);
	        }
	     for (PictureButtons circle2 : MovementClick) {
	            circle2.mouseClicked(mouseX, mouseY, mouseButton);
	        }
	     for (PictureButtons circle3 : PlayerClick) {
	            circle3.mouseClicked(mouseX, mouseY, mouseButton);
	        }
	     for (PictureButtons circle4 : OtherClick) {
	            circle4.mouseClicked(mouseX, mouseY, mouseButton);
	        }
	     for (PictureButtons circle5 : VisualsClick) {
	            circle5.mouseClicked(mouseX, mouseY, mouseButton);
	        }
	     
			if(!modulesArray.isEmpty()) {
				float count2 = 0;
				for(int i = 0; i < modulesArray.toArray().length; i++) {
					boolean isHoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count2*13 - 157 && mouseY <= ScaledRes.getScaledHeight()/2f + count2*13 - 157 + Fonts.notides.FONT_HEIGHT && mouseX >= ScaledRes.getScaledWidth()/2f - 316 && mouseX <= ScaledRes.getScaledWidth()/2f - 286 + 
					Fonts.notides.getStringWidth("" + modulesArray.get(i).name);
					
					if(isHoveringOver) {
						if(mouseButton == 0) {
							modulesArray.get(i).toggle();
						} else if(mouseButton == 1) {
							modulesSetting.clear();
							modulesSetting.addAll(modulesArray.get(i).settings);
						}
					}
					count2+= 1.3;
				}
			}
			
			if(!modulesSetting.isEmpty()) {
				int count3 = 0;
				for(int i = 0; i < modulesSetting.toArray().length; i++) {
					Setting setting = modulesSetting.get(i);
					if(setting instanceof ModeSetting) {
						ModeSetting mode = (ModeSetting) setting;
						boolean isHoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 157 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 157 + Fonts.notides.FONT_HEIGHT && mouseX >= ScaledRes.getScaledWidth()/2f - 210 && mouseX <= ScaledRes.getScaledWidth()/2f - 210 +
						Fonts.notides.getStringWidth("" + modulesSetting.get(i).name + ": " + mode.modes.get(mode.index));
						
						if((isHoveringOver)) {
							mode.cycle();
						}
					}
					if(setting instanceof BooleanSetting) {
						BooleanSetting bool = (BooleanSetting) setting;
						boolean isHoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 157 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 157 + Fonts.notides.FONT_HEIGHT && mouseX >= ScaledRes.getScaledWidth()/2f - 210 && mouseX <= ScaledRes.getScaledWidth()/2f - 210 +
						Fonts.notides.getStringWidth("" + modulesSetting.get(i).name + ": " + (bool.isEnabled() ? "On" : "Off"));
												if(isHoveringOver) {
							bool.toggle();
						}
					}
					if(setting instanceof NumberSetting) {
						
						//Gui.drawRect(ScaledRes.getScaledWidth()/2f - 100, ScaledRes.getScaledHeight()/2f + count3*16 - 157, ScaledRes.getScaledWidth()/2f + 220, ScaledRes.getScaledHeight()/2f + count3*16 - 144, 0x901f1f1f);
					//	Gui.drawRect(ScaledRes.getScaledWidth()/2f - 100, ScaledRes.getScaledHeight()/2f + count3*16 - 157, (float) (ScaledRes.getScaledWidth()/2f - 100 + (numberbar * 262)), ScaledRes.getScaledHeight()/2f + count3*16 - 144, 0xff0090ff);
					//	Fonts.notides.drawString(modulesSetting.get(i).name + ": " + number.getValue(), ScaledRes.getScaledWidth()/2f - 100, ScaledRes.getScaledHeight()/2f + count3*16 - 157, -1, true);
						
						NumberSetting number = (NumberSetting) setting;
					//	boolean isHoveringOver = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 157 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 144 + Fonts.notides.getFontHeight() && mouseX >= ScaledRes.getScaledWidth()/2f - 140 && mouseX <= ScaledRes.getScaledWidth()/2f + 10 +
						Fonts.notides.getStringWidth("" + modulesSetting.get(i).name + ": " + number.getValue());
						
						double numberbar = (number.getValue() - number.getMinimum())/(number.getMaximum() - number.getMinimum());
						//135 180
						boolean isHoveringOver2 = mouseY >= ScaledRes.getScaledHeight()/2f + count3*16 - 157 && mouseY <= ScaledRes.getScaledHeight()/2f + count3*16 - 144 && mouseX >= ScaledRes.getScaledWidth()/2f - 210 && mouseX <= ScaledRes.getScaledWidth()/2f + 270;
						if(isHoveringOver2) {
							if(mouseButton == 0) {
								dragging = true;
							}
						}
						
					}
					count3++;
				}
			}
	     
      
   if(mouseButton == 0 && isInside((int)ScaledRes.getScaledWidth()/2f + 340 + x, (int)ScaledRes.getScaledHeight()/2f -180+ y)) {
    		dragging1 = true;
   }
    	
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
    	dragging1 = false;
    	dragging = false;
    	super.mouseReleased(mouseX, mouseY, state);
    }
    
    public boolean isInside(float f, float g) {
        return Math.sqrt(Math.pow((this.x + (ScaledRes.getScaledWidth() / 2f)) - this.x, 2) + Math.pow((this.y + (ScaledRes.getScaledWidth() / 2f)) - this.y, 2)) <= ScaledRes.getScaledWidth() / 2f;
    }
    
    private final PictureButtons[] CombatClick = {new PictureButtons() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && isInside(mouseX, mouseY)) {
    
            button = "Combat";
    		for(Category c : Module.Category.values()) {
    			
    			
				modulesArray.clear();
				System.out.println("pressed");
				for(Module m : ModuleManager.getModulesByCategory(c.COMBAT)) {
					modulesArray.add(m);
				}
    		}
    	}
        }
    
    }};
 
 private final PictureButtons[] MovementClick = {new PictureButtons() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }
      
        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && isInside(mouseX, mouseY)) {
            	
            
 		for(Category c : Module.Category.values()) {
 			  button = "Movement";
 			
				modulesArray.clear();
				//System.out.println("pressed");
				for(Module m : ModuleManager.getModulesByCategory(c.MOVEMENT)) {
					modulesArray.add(m);
				}
			
 	}
        }
        }
    
    }};
 
 private final PictureButtons[] PlayerClick = {new PictureButtons() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && isInside(mouseX, mouseY)) {
            	
            
 		for(Category c : Module.Category.values()) {
 			
 			  button = "Player";
				modulesArray.clear();
				//System.out.println("pressed");
				for(Module m : ModuleManager.getModulesByCategory(c.PLAYER)) {
					modulesArray.add(m);
				}
 		}
			
 	}
        }
    
    }};
 
 private final PictureButtons[] VisualsClick = {new PictureButtons() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && isInside(mouseX, mouseY)) {
            	  button = "Visual";
            
 		for(Category c : Module.Category.values()) {
 			
 			
				modulesArray.clear();
				//System.out.println("pressed");
				for(Module m : ModuleManager.getModulesByCategory(c.VISUALS)) {
					modulesArray.add(m);
				}
			
 	}
        }
        }
    
    }};
 private final PictureButtons[] ExploitClick = {new PictureButtons() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
if(isInside(mouseX, mouseY)) {
	for(int i = 0; i < 85; i++) {
Gui.drawRect((int)(ScaledRes.getScaledWidth()/2f  - 345 + x), (int)(ScaledRes.getScaledHeight()/2f - 40 + y),(int)(ScaledRes.getScaledWidth()/2f  - 345 + x + i), (int)(ScaledRes.getScaledHeight()/2f - 40 + y), i);
	}
}
            if (mouseButton == 0 && isInside(mouseX, mouseY)) {
            	
            
 		for(Category c : Module.Category.values()) {
 			  button = "Exploit";
 			
				modulesArray.clear();
				//System.out.println("pressed");
				for(Module m : ModuleManager.getModulesByCategory(c.EXPLOITS)) {
					modulesArray.add(m);
				}
			
 	}
            }
        }
    
    }};	
 /**
 * 
 */
private final PictureButtons[] OtherClick = {new PictureButtons() {
        public boolean isInside(int x, int y) {
            return Math.sqrt(Math.pow((this.x + (this.getWidth() / 2f)) - x, 2) + Math.pow((this.y + (this.getWidth() / 2f)) - y, 2)) <= this.getWidth() / 2f;
        }

        public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
            if (mouseButton == 0 && isInside(mouseX, mouseY)) {
            	
     
 		for(Category c : Module.Category.values()) {
 			  button = "Other";
 			
				modulesArray.clear();
				//System.out.println("pressed");
				for(Module m : ModuleManager.getModulesByCategory(c.OTHER)) {
					modulesArray.add(m);
				}
 		}
			
 	}
        }
    
    }};
 
}
