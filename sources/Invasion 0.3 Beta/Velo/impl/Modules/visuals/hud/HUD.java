package Velo.impl.Modules.visuals.hud;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import Velo.api.Main.Main;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.ColorUtil;
import Velo.api.Util.Render.BlurUtil;
import Velo.api.Util.Render.RenderUtil;
import Velo.api.Util.Render.ScaledRes;
import Velo.api.Util.fontRenderer.FontRenderer;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.api.Util.fontRenderer.Utils.DrawFontUtil;
import Velo.impl.Event.EventKey;
import Velo.impl.Event.EventRender;
import Velo.impl.Modules.visuals.Blur;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

public class HUD extends Module {
	
	ScaledResolution sr = new ScaledResolution(mc);
	
	public static ModeSetting colormode = new ModeSetting("Color", "Fade", "Custom", "Fade", "Velo", "Rainbow", "Astolfo");
	public static ModeSetting font = new ModeSetting("Font", "Velo", "Velo", "Minecraft");
	public static ModeSetting tabguimode = new ModeSetting("TabGui", "Off", "Velo", "Moon X", "Off");
	public static ModeSetting namemode = new ModeSetting("NameDisplay", "Velo", "Velo2",  "Velo", "Sense", "Tab2");
	public static NumberSetting red = new NumberSetting("ColorRed1", 101, 0, 255, 1);
	public static NumberSetting green = new NumberSetting("ColorGreen1", 0, 0, 255, 1);
	public static NumberSetting blue = new NumberSetting("ColorBlue1", 255, 0, 255, 1);
	public static NumberSetting red2 = new NumberSetting("ColorRed2", 53, 0, 255, 1);
	public static NumberSetting green2 = new NumberSetting("ColorGreen2", 0, 0, 255, 1);
	public static NumberSetting blue2 = new NumberSetting("ColorBlue2", 123, 0, 255, 1);
	public static NumberSetting rainbowsaturation = new NumberSetting("RainbowSaturation", 0.5, 0.05, 1, 0.05);
	public static NumberSetting backgroundopacity = new NumberSetting("BackgroundOpacity", 70, 0, 255, 1);
	public static NumberSetting translatex = new NumberSetting("TranslateX", 3, -100, 100, 0.5);
	public static NumberSetting translatey = new NumberSetting("TranslateY", 1, -100, 100, 0.5);
	public static NumberSetting fontsize = new NumberSetting("FontSize", 18, 0, 50, 0.5);
	public static NumberSetting fontsize2 = new NumberSetting("Name Size", 19, 0, 50, 0.5);
	public static BooleanSetting sidebar = new BooleanSetting("Sidebar", false);
	public static BooleanSetting randomarraycolor = new BooleanSetting("Random Arraylist Color", false);
	public static BooleanSetting topbar = new BooleanSetting("Topbar", true);
	public static BooleanSetting inventory = new BooleanSetting("Inventory", false);
	public static String clientname = "Velo";
	public static String clientversion = "0.3-Beta";
	public static Color color;
	

	
	//TabGui Required Stuff xd
	public int tab;
	public boolean hasExpanded;
	public float expandstuff;
	public float arrayAnimation = 0;
	public float tabguianimation = 0;
	public Fonts font1 = new Fonts();

	
	public short TestVariable = 0;





	public HUD() {
		super("HUD", "HUD", Keyboard.KEY_NONE, Category.VISUALS);
		this.isToggled = true;
		this.loadSettings(colormode,fontsize2, font, tabguimode, namemode, red, red2, green, green2, blue, blue2, rainbowsaturation, backgroundopacity, translatex, translatey, fontsize, sidebar, randomarraycolor, topbar);
	}
	
	public void onEnable() {
	DrawFontUtil.loadFonts();
		Fonts.loadFonts();
	}
	
	public void onDisable() {
		
	}
	
	public void onRenderUpdate(EventRender event) {
		FontRenderer renderer1 = DrawFontUtil.renderer1;
		if(font.equalsIgnorecase("Velo")) {
			ModuleManager.modules.sort(Comparator.comparingInt(m -> font1.Hud.getStringWidth(((Module)m).displayName)).reversed());
		} else {
			ModuleManager.modules.sort(Comparator.comparingInt(m -> mc.fontRendererObj.getStringWidth(((Module)m).displayName)).reversed());
		}
		
		if(mc.gameSettings.showDebugInfo)
			return;
		
		int startY = 30+font1.Hud.FONT_HEIGHT;
		int color = -1;
		if(colormode.equalsIgnorecase("Fade")) {
			color = ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB();
		}

		
		if(inventory.isEnabled()) {
	        Gui.drawRect(774F,15+font1.Hud.FONT_HEIGHT,620F,99F,0x55222222);
	        Gui.drawRect(620F,startY - 15,621F,99F,color);
	        Gui.drawRect(620F,startY,774F,startY+1,color);
	        Gui.drawRect(620F,26F,775F,27F,color);
	        Gui.drawRect(620F,98F,775F,99F,color);
	        Gui.drawRect(774F,startY - 15,775F,99F,color);
	        
	        
	        font1.Hud.drawString("Inventory",(1434F/2F)-(font1.Hud.getStringWidth("Inventory")),(font1.Hud.FONT_HEIGHT) + 15, color , false);
	        

	        GL11.glPushMatrix();
	        RenderHelper.enableGUIStandardItemLighting();
	        renderInv(9,17,600,36,mc.fontRendererObj);
	        renderInv(18,26,600,54,mc.fontRendererObj);
	        renderInv(27,35,600,72,mc.fontRendererObj);
	        RenderHelper.disableStandardItemLighting();

	        GL11.glPopMatrix();
RenderUtil.drawBorderedRect(0F,startY,0F,66F, 174, 66, color);
	    
		}
		
		double diffX = mc.thePlayer.posX - mc.thePlayer.lastTickPosX;
		double diffZ = mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ;
		double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		
	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		String message = dtf.format(now);
		
		String[] times = message.split(":");
		
		if (Integer.valueOf(times[0]) >= 12 && Integer.valueOf(times[0]) < 24) {
			message = message.replaceAll("13:", "1:").replaceAll("14:", "2:").replaceAll("15:", "3:").replaceAll("16:", "4:").replaceAll("17:", "5:").replaceAll("18:", "6:").replaceAll("19:", "7:").replaceAll("20:", "8:").replaceAll("21:", "9:").replaceAll("22:", "10:").replaceAll("23:", "11:").replaceAll("24:", "12:");
			message += " PM";
		}
		else if (Integer.valueOf(times[0]) <= 0) {
			message = message.replaceAll("00:", "12:");
			message += " AM";
		}
		else if (Integer.valueOf(times[0]) <= 12) {
			message += " AM";
		}

		 NetworkPlayerInfo you = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
         String ping = "Ping: " + (you == null ? "0" : you.responseTime);
  
         String xyz = "X: \247f" + Math.round(mc.thePlayer.posX) + " \247rY: \247f" + Math.round(mc.thePlayer.posY) + "\247r Z: \247f" + Math.round(mc.thePlayer.posZ);

         
    
         xyz = xyz.toLowerCase();
 
 
         
     //    font1.Hud.drawString(bpsText + bps, xPosBottom1 + xValue, bottomPos1 + (heightInfo + 32) + realsize + size3 + size4 - moveMe, colorr);

         
		
         
         
		if(font.equalsIgnorecase("Velo")) {
			
			if(namemode.equalsIgnorecase("Tab2")) {
				if(colormode.equalsIgnorecase("Custom")) {
					Gui.drawRect(8, 4.5F,  font1.Hud.getStringWidth("Velo | Tab2 | " + mc.session.getUsername() + " | " + message ) + 17, font1.Hud.FONT_HEIGHT + 4, 0x75000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 81 + i, 3, ColorUtil.getRainbow(3, 0.5F, 1, i*4));
					}
					font1.Hud2.drawString("Velo | Tab2 | " + mc.session.getUsername() + " | " + message , 10, 3, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB(), false);
				}
				if(colormode.equalsIgnorecase("Rainbow")) {
					Gui.drawRect(8, 4.5F,  font1.Hud.getStringWidth("Velo | Tab2 | " + mc.session.getUsername() + " | " + message ) + 15, font1.Hud.FONT_HEIGHT + 4, 0x75000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 81 + i, 3, ColorUtil.getRainbow(3, 0.5F, 1, i*4));
					}
					font1.Hud2.drawString("Velo | Tab2 | " + mc.session.getUsername() + " | " + message , 10, 3, ColorUtil.getRainbow(12, 0.5F, 1, 0), false);
				}
				if(colormode.equalsIgnorecase("Astolfo")) {
					Gui.drawRect(8, 4.5F,  font1.Hud.getStringWidth("Velo | Tab2 | " + mc.session.getUsername() + " | " + message ) + 15, font1.Hud.FONT_HEIGHT + 4, 0x75000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 81 + i, 3, ColorUtil.getRainbow(3, 0.5F, 1, i*4));
					}
					font1.Hud2.drawString("Velo | Tab2 | " + mc.session.getUsername() + " | " + message , 10, 3, ColorUtil.astolfoColors(0, 1000), false);
				}
				if(colormode.equalsIgnorecase("Fade")) {
					Gui.drawRect(8, 4.5F, font1.Hud.getStringWidth("Velo | Tab2 | " + mc.session.getUsername() + " | " + message ) + 15, font1.Hud.FONT_HEIGHT + 4, 0x75000000);
					for(int i = 0; i < 95; i++) {
						Gui.drawRect(7, 4.5F, font1.Hud.getStringWidth("Velo | Tab2 | " + mc.session.getUsername() + " | " + message ) + 17, 3,  ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB());
					}
					font1.Hud2.drawString("Velo | Tab2 | " + mc.session.getUsername() + " | " + message , 10, 3, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB(), false);
				}
				}
			
			
			
			if(namemode.equalsIgnorecase("Sense")) {
				if(colormode.equalsIgnorecase("Custom")) {
					Gui.drawRect(8, 4.5F, font1.Hud.getStringWidth("Velo") + 53, font1.Hud.FONT_HEIGHT + 4, 0x65000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 7 + i, 3, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB());
					}
					font1.Hud2.drawString("Velo " + Main.version, 8, 4F, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB(), false);
				}
				if(colormode.equalsIgnorecase("Rainbow")) {
					Gui.drawRect(8, 4.5F, font1.Hud.getStringWidth("Velo") + 53, font1.Hud.FONT_HEIGHT + 4, 0x65000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 7 + i, 3, ColorUtil.getRainbow(3, 0.5F, 1, i*4));
					}
					font1.Hud2.drawString("Velo " + Main.version, 8, 4F, ColorUtil.getRainbow(3, 0.5F, 1, 0), false);
				}
				if(colormode.equalsIgnorecase("Astolfo")) {
					Gui.drawRect(8, 4.5F, font1.Hud.getStringWidth("Velo") + 53, font1.Hud.FONT_HEIGHT + 4, 0x65000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 7 + i, 3, ColorUtil.astolfoColors(i, 1000));
					}
					font1.Hud2.drawString("Velo " + Main.version, 8, 4F, ColorUtil.astolfoColors(0, 1000), false);
				}
				if(colormode.equalsIgnorecase("Fade")) {
					Gui.drawRect(8, 4.5F, font1.Hud.getStringWidth("Velo") + 53, font1.Hud.FONT_HEIGHT + 4, 0x65000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 7 + i, 3, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7 - i)) / 120D)).getRGB());
					}
					font1.Hud2.drawString("Velo " + Main.version, 8, 4F, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB(), false);
				}
			} else if(namemode.equalsIgnorecase("Velo")){
				if(colormode.equalsIgnorecase("Custom")) {
					font1.Hud2.drawString(clientname, 2, -1, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB(), false);
				}
				if(colormode.equalsIgnorecase("Rainbow")) {
					font1.Hud2.drawString(clientname, 2, -1, ColorUtil.getRainbow(12, 0.5F, 1, 0), false);
				}
				if(colormode.equalsIgnorecase("Astolfo")) {
					font1.Hud2.drawString(clientname, 2, -1, ColorUtil.astolfoColors(0, 1000), false);
				}
				if(colormode.equalsIgnorecase("Fade")) {
					font1.Hud2.drawString(clientname, 2, -1, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB(), false);
				}
			} else if (namemode.equalsIgnorecase("Velo2")) {
				if(colormode.equalsIgnorecase("Custom")) {
					font1.Hud2.drawString(clientname.substring(0, 1), 2, -1, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB(), true);
					font1.Hud2.drawString(clientname.substring(1), 2 + font1.Hud2.getStringWidth(clientname.substring(0, 1)) + 1, -1, Color.LIGHT_GRAY.getRGB(), true);
				}
				if(colormode.equalsIgnorecase("Rainbow")) {
					font1.Hud2.drawString(clientname.substring(0, 1), 2, -1, ColorUtil.getRainbow(12, 0.5F, 1, 0), true);
					font1.Hud2.drawString(clientname.substring(1), 2 + font1.Hud2.getStringWidth(clientname.substring(0, 1)) + 1, -1, Color.LIGHT_GRAY.getRGB(), true);
				}
				if(colormode.equalsIgnorecase("Astolfo")) {
					font1.Hud2.drawString(clientname.substring(0, 1), 2, -1, ColorUtil.astolfoColors(0, 1000), true);
					font1.Hud2.drawString(clientname.substring(1), 2 + font1.Hud2.getStringWidth(clientname.substring(0, 1)) + 1, -1, Color.LIGHT_GRAY.getRGB(), true);
				}
				if(colormode.equalsIgnorecase("Fade")) {
					font1.Hud2.drawString(clientname.substring(0, 1), 2, -1, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB(), true);
					font1.Hud2.drawString(clientname.substring(1), 2 + font1.Hud2.getStringWidth(clientname.substring(0, 1)) + 1, -1, Color.LIGHT_GRAY.getRGB());
				}
			}
		} else {
			if(namemode.equalsIgnorecase("Sense")) {
				if(colormode.equalsIgnorecase("Custom")) {
					Gui.drawRect(8, 4.5F, mc.fontRendererObj.getStringWidth("Velo") + 53, mc.fontRendererObj.FONT_HEIGHT + 4, 0x65000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 7 + i, 3, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB());
					}
					mc.fontRendererObj.drawString("Velo " + Main.version, 8, 4F, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB(), false);
				}
				if(colormode.equalsIgnorecase("Rainbow")) {
					Gui.drawRect(8, 4.5F, mc.fontRendererObj.getStringWidth("Velo") + 53, mc.fontRendererObj.FONT_HEIGHT + 4, 0x65000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 7 + i, 3, ColorUtil.getRainbow(3, 0.5F, 1, i*4));
					}
					mc.fontRendererObj.drawString("Velo " + Main.version, 8, 4F, ColorUtil.getRainbow(3, 0.5F, 1, 0), false);
				}
				if(colormode.equalsIgnorecase("Astolfo")) {
					Gui.drawRect(8, 4.5F, mc.fontRendererObj.getStringWidth("Velo") + 53, mc.fontRendererObj.FONT_HEIGHT + 4, 0x65000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 7 + i, 3, ColorUtil.astolfoColors(i, 1000));
					}
					mc.fontRendererObj.drawString(clientname, 8, 4F, ColorUtil.astolfoColors(0, 1000), false);
				}
				if(colormode.equalsIgnorecase("Fade")) {
					Gui.drawRect(8, 4.5F, mc.fontRendererObj.getStringWidth("Velo") + 53, mc.fontRendererObj.FONT_HEIGHT + 4, 0x65000000);
					for(int i = 0; i < 85; i++) {
						Gui.drawRect(8 + i, 4.5F, 7 + i, 3, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7 - i)) / 120D)).getRGB());
					}
					mc.fontRendererObj.drawString(clientname, 8, 4F, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB(), false);
				}
			} else if(namemode.equalsIgnorecase("Velo")) {
				if(colormode.equalsIgnorecase("Custom")) {
					mc.fontRendererObj.drawString(clientname, 2, 1, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB(), true);
				}
				if(colormode.equalsIgnorecase("Rainbow")) {
					mc.fontRendererObj.drawString(clientname, 2, 1, ColorUtil.getRainbow(12, 0.5F, 1, 0), true);
				}
				if(colormode.equalsIgnorecase("Astolfo")) {
					mc.fontRendererObj.drawString(clientname, 2, 1, ColorUtil.astolfoColors(0, 1000), true);
				}
				if(colormode.equalsIgnorecase("Fade")) {
					mc.fontRendererObj.drawString(clientname, 2, 1, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB(), true);
				}
			} else if(namemode.equalsIgnorecase("Velo2")) {
				if(colormode.equalsIgnorecase("Custom")) {
					mc.fontRendererObj.drawString(clientname.substring(0, 1) + "§7" + clientname.substring(1), 2, 1, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB(), false);
				}
				if(colormode.equalsIgnorecase("Rainbow")) {
					mc.fontRendererObj.drawString(clientname.substring(0, 1) + "§7" + clientname.substring(1), 2, 1, ColorUtil.getRainbow(12, 0.5F, 1, 0), true);
				}
				if(colormode.equalsIgnorecase("Astolfo")) {
					mc.fontRendererObj.drawString(clientname.substring(0, 1) + "§7" + clientname.substring(1), 2, 1, ColorUtil.astolfoColors(0, 1000), true);
				}
				if(colormode.equalsIgnorecase("Fade")) {
					mc.fontRendererObj.drawString(clientname.substring(0, 1) + "§7" + clientname.substring(1), 2, 1, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB(), true);
				}
			}
		}
		
		if(tabguimode.equalsIgnorecase("Velo")) {
			Gui.drawRect(0, 15F, 70, 26 + Module.Category.values().length * 10.8, 0x80000000);
			if(colormode.equalsIgnorecase("Custom")) {
				Gui.drawRect(0, 15F + tabguianimation*12.5, 70, 15F + tabguianimation*12.5 + 14, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB());
			}
			if(colormode.equalsIgnorecase("Astolfo")) {
				Gui.drawRect(0, 15F + tabguianimation*12.5, 70, 15F + tabguianimation*12.5 + 14, ColorUtil.astolfoColors(0, 1000));
			}
			if(colormode.equalsIgnorecase("Rainbow")) {
				Gui.drawRect(0, 15F + tabguianimation*12.5, 70, 15F + tabguianimation*12.5 + 14, ColorUtil.getRainbow(12, 0.5F, 1, 0));
			}
			if(colormode.equalsIgnorecase("Fade")) {
				Gui.drawRect(0, 15F + tabguianimation*12.5, 70, 15F + tabguianimation*12.5 + 14, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB());
			}
			
			if(tabguianimation < tab) {
				tabguianimation += 0.04F;
			}
			if(tabguianimation > tab) {
				tabguianimation -= 0.04F;
			}
			
			int count = 0;
			
			for(Category c : Module.Category.values()) {
				Category category = Module.Category.values()[tab];
				if(category == c) {
					if(c.expandStuffxd < 5) {
						c.expandStuffxd += 0.2F;
					}
				} else {
					if(c.expandStuffxd > 0) {
						c.expandStuffxd -= 0.2F;
					}
				}
				if(font.equalsIgnorecase("Velo")) {
					font1.targethudNameMoon.drawString(c.name, 2 + c.expandStuffxd, 15f + count*12.5F, (category == c ? -1 : Color.LIGHT_GRAY.getRGB()), true);
				} else {
					mc.fontRendererObj.drawString(c.name, 2 + (category == c ? expandstuff : 0), 17.5f + count*12.5F, (category == c ? -1 : Color.LIGHT_GRAY.getRGB()), true);
				}
				count++;
			}
		} else if(tabguimode.equalsIgnorecase("Moon X")) {
			Gui.drawRect(3, 14.5F, 86, 27 + Module.Category.values().length * 11.3F, 0x65000000);
			Gui.drawRect(3, 27F + Module.Category.values().length * 11.3F, 86, 26 + Module.Category.values().length * 11.3F, 0xff000000);
			Gui.drawRect(2.8F, 13.6F, 1.8F, 27 + Module.Category.values().length * 11.3F, 0xff000000);
			Gui.drawRect(86, 13.6F, 87, 27 + Module.Category.values().length * 11.3F, 0xff000000);
			Gui.drawRect(3, 13.5F, 86, 14.6F, 0xff000000);
			if(colormode.equalsIgnorecase("Custom")) {
				Gui.drawRect(3, 15F + tabguianimation*13, 86, 15F + tabguianimation*13 + 14, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB());
			}
			if(colormode.equalsIgnorecase("Astolfo")) {
				Gui.drawRect(3, 15F + tabguianimation*13, 86, 15F + tabguianimation*13 + 14, ColorUtil.astolfoColors(0, 1000));
			}
			if(colormode.equalsIgnorecase("Rainbow")) {
				Gui.drawRect(3, 15F + tabguianimation*13, 86, 15F + tabguianimation*13 + 14, ColorUtil.getRainbow(12, 0.5F, 1, 0));
			}
			if(colormode.equalsIgnorecase("Fade")) {
				Gui.drawRect(3, 15F + tabguianimation*13, 86, 15F + tabguianimation*13 + 14, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB());
			}
			
			if(tabguianimation < tab) {
				tabguianimation += 0.02F;
			}
			if(tabguianimation > tab) {
				tabguianimation -= 0.02F;
			}
			
			int count = 0;
			
			for(Category c : Module.Category.values()) {
				Category category = Module.Category.values()[tab];
				if(category == c) {
					if(c.expandStuffxd < 5) {
						c.expandStuffxd += 0.1F;
					}
				} else {
					if(c.expandStuffxd > 0) {
						c.expandStuffxd -= 0.1F;
					}
				}
				if(font.equalsIgnorecase("Velo")) {
					font1.targethudNameMoon.drawString(c.name, 5 + c.expandStuffxd, 15f + count*13F, (category == c ? -1 : Color.GRAY.getRGB()), true);
				} else {
					mc.fontRendererObj.drawString(c.name, 5 + c.expandStuffxd, 17.5f + count*13F, (category == c ? -1 : Color.GRAY.getRGB()), true);
				}
				count++;
			}
		}
		
		if(font.equalsIgnorecase("Velo")) {
			Gui.drawRect(ScaledRes.getScaledWidth(), ScaledRes.getScaledWidth() / 18, ScaledRes.getScaledWidth(),  ScaledRes.getScaledWidth() / 10, color);
			font1.Hud.drawString(Math.round(diffXZ * (20 * mc.timer.timerSpeed) * 100.00D) / 100.00D + " Blocks/sec", 1, event.getHeight() - 22, -1, false);
			font1.Hud.drawString("" + (int) mc.thePlayer.posX + " " + (int) mc.thePlayer.posY + " " + (int) mc.thePlayer.posZ, 1, event.getHeight() - 13, -1, false);
		//	font1.Hud.drawString(ping, 40, event.getHeight() - 31, -1, false);
	//		font1.Hud.drawString("FPS: " + Minecraft.getDebugFPS(), 1, event.getHeight() - 31, -1, false);
			font1.Hud.drawString("Build:", event.getWidth() - 25 - font1.Hud.getStringWidth(clientversion), event.getHeight() - 13, Color.LIGHT_GRAY.getRGB(), false);
			font1.Hud.drawString(clientversion, event.getWidth() - 2 - font1.Hud.getStringWidth(clientversion), event.getHeight() - 13, -1, false);
		} else {
			mc.fontRendererObj.drawString(Math.round(diffXZ * (20 * mc.timer.timerSpeed) * 100.00D) / 100.00D + " Blocks/sec", 1, event.getHeight() - 21, -1, true);
			mc.fontRendererObj.drawString("" + (int) mc.thePlayer.posX + " " + (int) mc.thePlayer.posY + " " + (int) mc.thePlayer.posZ, 1, event.getHeight() - 11, -1, true);
	//		mc.fontRendererObj.drawString("FPS: " + mc.getDebugFPS(), 2, event.getHeight() - 24, -1, false);
		}
		
		int count2 = 0;
		
		//for(PotionEffect potioneffect : mc.thePlayer.getActivePotionEffects()) {
			//String potioneffectname = potioneffect.getEffectName().toUpperCase();
			//char potioneffectname2 = potioneffectname.charAt(0);
			//String totalname = potioneffectname2 + potioneffect.getEffectName().substring(8);
			
			//renderer1.drawString(totalname + " §7" + potioneffect.getAmplifier() + " - " + potioneffect.getDuration(), event.getWidth() - 5 - DrawFontUtil.renderer1.getStringWidth(totalname + " §7" + potioneffect.getAmplifier() + " - " + potioneffect.getDuration()) - 5, event.getHeight() - 25 - count2*11, 0xff0090ff, false);
			//count2++;
		//}
		
		float count = 0;
		int count3 = 0;
		//BlurUtil.blurAreaBoarder(event.getWidth()/2, event.getHeight()/2, 50, 50, 5);
		float offset = count*(renderer1.getFontHeight() + 3);
		for(Module m : ModuleManager.modules) {
			if(!m.isOutOfArraylist || m.name.equals("ClickGui") || m.hidden.isEnabled())
				continue;
			
			if(m.isToggled) {
				if(m.arrayAnimation2 > 0) {
					m.arrayAnimation2 -= 0.01F;
				}
				if(m.arrayAnimation > 0) {
					m.arrayAnimation -= 1f;
				}
				if(m.transition < 1) {
					m.transition += 0.025;
				}
			} else {
				if(m.arrayAnimation2 < 1) {
					m.arrayAnimation2 = 1;
				}
				if(m.transition > 0) {
					m.transition -= 0.025;
				}
				if(m.arrayAnimation < font1.Hud.getStringWidth(m.displayName) + 8) {
					m.arrayAnimation += 2f;
				}
				if(m.transition < 0.01 && m.arrayAnimation > font1.Hud.getStringWidth(m.displayName) + 7) {
					m.isOutOfArraylist = false;
				}
			}
			
			boolean setTransition = true;
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(translatex.getValue(), translatey.getValue(), 0);
			if(Blur.isEnabled) {}
			if(font.equalsIgnorecase("Velo")) {
				//BlurUtil.blurAreaBoarder((float) event.getWidth() + 3 - font1.Hud.getStringWidth(m.name + "§7" + m.displayName.substring(m.name.length())) + 4.5F + m.arrayAnimation * m.arrayAnimation2, count*9.45f + 4F, event.getWidth() + m.arrayAnimation * m.arrayAnimation2, font1.Hud.FONT_HEIGHT, 3);
				//Gui.drawGradientRect((float) event.getWidth() + 3 - font1.Hud.getStringWidth(m.name + "§7" + m.displayName.substring(m.name.length())) + 4.5F + m.arrayAnimation, count*9.45f + 2.5F, event.getWidth() + m.arrayAnimation, font1.Hud.FONT_HEIGHT, 1, 0x00ffffff);
				Gui.drawRect((float) event.getWidth() - font1.Hud.getStringWidth(m.name + "§7" + m.displayName.substring(m.name.length())) + 3.2F + m.arrayAnimation * m.arrayAnimation2, count*9.5f + 1.5F, event.getWidth() - 3 + m.arrayAnimation * m.arrayAnimation2, font1.Hud.FONT_HEIGHT + count*9.5f - 0.25, new Color(0,0,0,(int) backgroundopacity.getValue()).getRGB());
			} else {
				Gui.drawRect((float) event.getWidth() - mc.fontRendererObj.getStringWidth(m.name + "§7" + m.displayName.substring(m.name.length())) - 7 + m.arrayAnimation * m.arrayAnimation2, count*11f + 2.25F, event.getWidth() - 3 + m.arrayAnimation * m.arrayAnimation2, mc.fontRendererObj.FONT_HEIGHT + count*11f + 4f, new Color(0,0,0,(int) backgroundopacity.getValue()).getRGB());
			}
			int maxLength = 0;
			
			if(font.equalsIgnorecase("Velo")) {
				if(maxLength < font1.Hud.getStringWidth(m.name + m.displayName.substring(m.name.length()))) {
					maxLength = font1.Hud.getStringWidth(m.name + m.displayName.substring(m.name.length()));
				}
			} else {
				if(maxLength < mc.fontRendererObj.getStringWidth(m.name + m.displayName.substring(m.name.length()))) {
					maxLength = mc.fontRendererObj.getStringWidth(m.name + m.displayName.substring(m.name.length()));
				}
			}
			
			
			if(topbar.isEnabled()) {
				if(colormode.equalsIgnorecase("Custom")) {
					Gui.drawRect(event.getWidth() - 2, 0.8F, event.getWidth() - maxLength - 6, 1.5, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB());
				}
				if(colormode.equalsIgnorecase("Astolfo")) {
					Gui.drawRect(event.getWidth() - 2, 0.8F, event.getWidth() - maxLength - 6, 1.5, ColorUtil.astolfoColors(0, 1000));
				}
				if(colormode.equalsIgnorecase("Rainbow")) {
					Gui.drawRect(event.getWidth() - 2, 0.8F, event.getWidth() - maxLength - 6, 1.5, ColorUtil.moonColors(1000, 0));
				}
				if(colormode.equalsIgnorecase("Fade")) {
					Gui.drawRect(event.getWidth() - 2, 0.8F, event.getWidth() - maxLength - 6, 1.5, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7)) / 120D)).getRGB());
				}
			}
			
			if(font.equalsIgnorecase("Velo")) {
				Gui.drawRect((float) event.getWidth() - 2 + m.getTransition() + 9.8F+ 1, count*9.5f, event.getWidth() - 1, font1.Hud.FONT_HEIGHT + count*9.5f, 0x01000000);
				if(sidebar.isEnabled()) {
					
					if(colormode.equalsIgnorecase("Custom")) {
						Gui.drawRect((float) event.getWidth() - 2 + m.getTransition() + 9.8F+ 1, count*9.5f, event.getWidth() - 1, font1.Hud.FONT_HEIGHT + count*9.5f, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB());
					}
					if(colormode.equalsIgnorecase("Astolfo")) {
						Gui.drawRect((float) event.getWidth() - 2 + m.getTransition() + 9.8F+ 1, count*9.5f, event.getWidth() - 1, font1.Hud.FONT_HEIGHT + count*9.5f, ColorUtil.astolfoColors((int) (count*10), 1000));
					}
					if(colormode.equalsIgnorecase("Rainbow")) {
						Gui.drawRect((float) event.getWidth() - 3, count*9.5f + 1.25, event.getWidth() - 2, font1.Hud.FONT_HEIGHT + count*9.5f, ColorUtil.getRainbow(4F, 0.5F, 1, (long) (count*200)));
					}
					if(colormode.equalsIgnorecase("Fade")) {
						Gui.drawRect((float) event.getWidth() - 2, count*9.5f, event.getWidth() - 2.8, font1.Hud.FONT_HEIGHT + count*9.5f, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7 - count3*25)) / 120D)).getRGB());
					}
				}
			} else {
				if(sidebar.isEnabled()) {
					if(colormode.equalsIgnorecase("Custom")) {
						Gui.drawRect((float) event.getWidth() - 2 + m.getTransition() + 9.8F+ 1, count*11, event.getWidth() - 1, mc.fontRendererObj.FONT_HEIGHT + count*11, new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255).getRGB());
					}
					if(colormode.equalsIgnorecase("Astolfo")) {
						//Gui.drawRect((float) event.getWidth() - 2 + m.getTransition() + 9.8F+ 1, count*11, event.getWidth() - 1, mc.fontRendererObj.FONT_HEIGHT + count*11, ColorUtil.astolfoColors(count*10, 1000));
					}
					if(colormode.equalsIgnorecase("Rainbow")) {
						//Gui.drawRect((float) event.getWidth() - 2 + m.getTransition() + 9.8F+ 1, count*11, event.getWidth() - 1, mc.fontRendererObj.FONT_HEIGHT + count*11, ColorUtil.moonColors(1000, count*40));
					}
					if(colormode.equalsIgnorecase("Fade")) {
						Gui.drawRect(event.getWidth() - 1, count*11, event.getWidth() - 1, mc.fontRendererObj.FONT_HEIGHT + count*11, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7 - count3*25)) / 120D)).getRGB());
					}
				}
			}
			
			if(font.equalsIgnorecase("Velo")) {
				if(!randomarraycolor.isEnabled()) {
					if(colormode.equalsIgnorecase("Custom")) {
						font1.Hud.drawString(m.displayName.substring(m.name.length()), (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName.substring(m.name.length())) - 4.5F + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0 * m.transition2, Color.LIGHT_GRAY.getRGB(), false);
						font1.Hud.drawString(m.name, (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName) - 5 + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0 * m.transition2, new Color((int)red.getValue(), (int)green.getValue(), (int)blue.getValue()).getRGB(), false);
					}
					if(colormode.equalsIgnorecase("Fade")) {
						font1.Hud.drawString(m.displayName.substring(m.name.length()), (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName.substring(m.name.length())) - 4.5F + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0 * m.transition2, Color.LIGHT_GRAY.getRGB(), false);
						font1.Hud.drawString(m.name, (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName) - 5 + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0F * m.transition2, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7 - count3*25)) / 120D)).getRGB(), false);
					}
					if(colormode.equalsIgnorecase("Rainbow")) {
						font1.Hud.drawString(m.displayName.substring(m.name.length()), (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName.substring(m.name.length())) - 4.5F + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0 * m.transition2, Color.LIGHT_GRAY.getRGB(), false);
						font1.Hud.drawString(m.name, (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName) - 5 + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0 * m.transition2, ColorUtil.getRainbow(4F, 0.5F, 1, (long) (count*200)), false);
					}
					if(colormode.equalsIgnorecase("Astolfo")) {
						font1.Hud.drawString(m.displayName.substring(m.name.length()), (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName.substring(m.name.length())) - 4.5F + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0 * m.transition2, Color.LIGHT_GRAY.getRGB(), false);
						font1.Hud.drawString(m.name, (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName) - 5 + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0 * m.transition2, ColorUtil.astolfoColors(count3*10, 1000), false);
					}
				} else {
					font1.Hud.drawString(m.displayName.substring(m.name.length()), (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName.substring(m.name.length())) - 4.5F + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0 * m.transition2, Color.LIGHT_GRAY.getRGB(), false);
					font1.Hud.drawString(m.name, (float) event.getWidth() - font1.Hud.getStringWidth(m.displayName) - 5 + m.arrayAnimation * m.arrayAnimation2, count*9.5F + 0 * m.transition2, new Color(m.red1, m.green1, m.blue1).getRGB(), false);
				}
			} else {
				if(colormode.equalsIgnorecase("Custom")) {
					mc.fontRendererObj.drawString(m.name + "§7" + m.displayName.substring(m.name.length()), (float) event.getWidth() - mc.fontRendererObj.getStringWidth(m.displayName) - 5 + m.arrayAnimation * m.arrayAnimation2, count*11 + 4, new Color((int)red.getValue(), (int)green.getValue(), (int)blue.getValue()).getRGB(), true);
				}
				if(colormode.equalsIgnorecase("Fade")) {
					mc.fontRendererObj.drawString(m.name + "§7" + m.displayName.substring(m.name.length()), (float) event.getWidth() - mc.fontRendererObj.getStringWidth(m.displayName) - 5 + m.arrayAnimation * m.arrayAnimation2, count*11 + 4, ColorUtil.getGradientOffset(new Color((int) red2.getValue(), (int) green2.getValue(), (int) blue2.getValue(), 255), new Color((int) red.getValue(), (int) green.getValue(), (int) blue.getValue(), 255), (Math.abs(((System.currentTimeMillis()) / 7 - count3*25)) / 120D)).getRGB(), true);
				}
				if(colormode.equalsIgnorecase("Rainbow")) {
					mc.fontRendererObj.drawString(m.name + "§7" + m.displayName.substring(m.name.length()), (float) event.getWidth() - mc.fontRendererObj.getStringWidth(m.displayName) - 5 + m.arrayAnimation * m.arrayAnimation2, count*11 + 4, ColorUtil.getRainbow(4F, 0.5F, 1, (long) (count*200)), true);
				}
				if(colormode.equalsIgnorecase("Astolfo")) {
					mc.fontRendererObj.drawString(m.name + "§7" + m.displayName.substring(m.name.length()), (float) event.getWidth() - mc.fontRendererObj.getStringWidth(m.displayName) - 5 + m.arrayAnimation * m.arrayAnimation2, count*11 + 4, ColorUtil.astolfoColors((int) (count*10), 1000), true);
				}
			}
			GlStateManager.popMatrix();
			
			count += m.transition - 0.05;
			count3++;
		}
	}
	
	public void onKeypress(EventKey event) {
		int code = event.code;
		Category category = Module.Category.values()[tab];
		List<Module> modules = ModuleManager.getModulesByCategory(category);
		if(code == Keyboard.KEY_UP) {
			expandstuff = 0;
			if(hasExpanded) {
			
				if(hasExpanded && !modules.isEmpty() && modules.get(category.mIndex).tabGuiExpanded) {
					Module module = modules.get(category.mIndex);
					
					
				}
			
			} else {
				if(tab <= 0) {
					tab = Module.Category.values().length - 1;
				} else {
					tab--;
				}
			}
		}
		if(code == Keyboard.KEY_DOWN) {
			expandstuff = 0;
			if(hasExpanded) {
			
				if(hasExpanded && !modules.isEmpty() && modules.get(category.mIndex).tabGuiExpanded) {
					Module module = modules.get(category.mIndex);
					
					
				}
			
			} else {
				if(tab >= Module.Category.values().length - 1) {
					tab = 0;
				} else {
					tab++;
				}
			}
		}
	}
    private void renderInv(int slot ,int endSlot, int x ,int y, net.minecraft.client.gui.FontRenderer font){
        int xOffset = x;
        for (int i :  IntStream.range(slot, endSlot).toArray()) {
            xOffset+=18;
            
            
            ItemStack stack=mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            mc.renderItem.renderItemAndEffectIntoGUI(stack, xOffset+8, y + 10);
         //   mc.renderItem.renderItemOverlays(font, stack, xOffset+18, y + 10);
}
        
    }
    
    private int limit;

    public void Range(int limit) {
        this.limit = limit;
    }

    
    public static void renderItem(int slot ,int endSlot, int x, int y) {
        for (int i = 0; i < 36; i++) {
        	  ItemStack stack= Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().zLevel = -100.0f;
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
        GlStateManager.enableDepth();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y + 8);
        Minecraft.getMinecraft().getRenderItem().zLevel = 0.0f;
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
        }
    }

    
    
    public void Border(float x, float y, float x2, float y2) {
    	 RenderUtil.drawBorderedRect(x, y, x2, y2, 3F, Integer.MIN_VALUE, 0);
        return;

    }
}
