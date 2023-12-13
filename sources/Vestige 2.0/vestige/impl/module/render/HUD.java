package vestige.impl.module.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.RenderEvent;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.ModeSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.font.FontUtil;
import vestige.font.MinecraftFontRenderer;
import vestige.util.render.BlurUtil;
import vestige.util.render.ColorUtil;
import vestige.util.render.DrawUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;

@ModuleInfo(name = "HUD", category = Category.RENDER)
public class HUD extends Module {
	
	public final ModeSetting mode = new ModeSetting("Mode", this, "Simple2", "Simple", "Simple2", "Vestige V1", "New", "New2");
	
	public final ModeSetting color = new ModeSetting("Color", this, "Normal", "Normal", "Custom", "Rainbow") {
		@Override
		public boolean isShown() {
			return !mode.is("Simple");
		}
	};
	
	public final NumberSetting red1 = new NumberSetting("Red 1", this, 0, 0, 255, 5, true) { public boolean isShown() { return !mode.is("Simple") && color.is("Custom"); }};
	public final NumberSetting green1 = new NumberSetting("Green 1", this, 200, 0, 255, 5, true) { public boolean isShown() { return !mode.is("Simple") && color.is("Custom"); }};
	public final NumberSetting blue1 = new NumberSetting("Blue 1", this, 235, 0, 255, 5, true) { public boolean isShown() { return !mode.is("Simple") && color.is("Custom"); }};

	public final NumberSetting red2 = new NumberSetting("Red 2", this, 20, 0, 255, 5, true) { public boolean isShown() { return !mode.is("Simple") && color.is("Custom"); }};
	public final NumberSetting green2 = new NumberSetting("Green 2", this, 75, 0, 255, 5, true) { public boolean isShown() { return !mode.is("Simple") && color.is("Custom"); }};
	public final NumberSetting blue2 = new NumberSetting("Blue 2", this, 230, 0, 255, 5, true) { public boolean isShown() { return !mode.is("Simple") && color.is("Custom"); }};
	
	private final BooleanSetting boxes = new BooleanSetting("ArrayList boxes", this, true) {
		@Override
		public boolean isShown() {
			return mode.is("Simple2");
		}
	};
	
	private final BooleanSetting rightBars = new BooleanSetting("ArrayList right bars", this, false) {
		@Override
		public boolean isShown() {
			return mode.is("Simple2");
		}
	};
	
	private final BooleanSetting blur = new BooleanSetting("Blur", this, false) {
		@Override
		public boolean isShown() {
			return mode.is("New");
		}
	};
	
	private final BooleanSetting showBalance = new BooleanSetting("Show balance", this, false);
	
    private final ArrayList<Module> modules = new ArrayList<>();
    private boolean modulesAdded;
    
    public HUD() {
    	this.registerSettings(mode, color, red1, green1, blue1, red2, green2, blue2, boxes, rightBars, blur);
    	this.setEnabledSilently(true);
    }
    
    @Listener
    public void onUpdate(UpdateEvent event) {
        switch (mode.getMode()) {
        	case "Simple":
        	case "Simple2":
        		FontRenderer fr = mc.fontRendererObj;

                Collections.reverse(modules);

                try {
                    modules.sort((m1, m2) -> (int) (Math.round((fr.getStringWidth(m1.getDisplayName()) * 8) - Math.round(fr.getStringWidth(m2.getDisplayName()) * 8))));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }

                Collections.reverse(modules);
        		break;
        	case "Vestige V1":
        	case "New":
        	case "New2":
        		MinecraftFontRenderer fr2 = FontUtil.product_sans;

                Collections.reverse(modules);

                try {
                    modules.sort((m1, m2) -> (int) (Math.round((fr2.getStringWidth(m1.getDisplayName()) * 8) - Math.round(fr2.getStringWidth(m2.getDisplayName()) * 8))));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }

                Collections.reverse(modules);
        		break;
        }
    }

    @Listener
    public void onRender(RenderEvent event) {
        if (!modulesAdded) {
            for (Module m : Vestige.getInstance().getModuleManager().getModules()) {
                modules.add(m);
            }
            modulesAdded = true;
        }

        switch (mode.getMode()) {
        	case "Simple":
        		this.renderSimple();
        		break;
        	case "Simple2":
        		this.renderSimple2();
        		break;
        	case "New":
        		this.renderNew();
        		break;
        	case "New2":
        		this.renderNew2();
        		break;
        	case "Vestige V1":
        		this.renderVestigeV1();
        		break;
        }
    }
    
    private void renderNew() {
    	MinecraftFontRenderer fr = FontUtil.product_sans;
    	
    	if(showBalance.isEnabled()) {
        	fr.drawStringWithShadow("Balance : " + Vestige.getInstance().getBalanceProcessor().getBalance(), 4, mc.displayHeight / 2 - 23, -1);
        }
    	
    	this.renderNewWatermark();
    	
    	int screenWidth = mc.displayWidth / 2;
    	
    	double bpt = Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ);
        double bps = bpt * 20 * mc.timer.timerSpeed;
        double roundedBps = Math.round(bps * 100) / 100.0D;
        
        fr.drawStringWithShadow("BPS : " + roundedBps, 4, mc.displayHeight / 2 - 11, -1);
        
        double offsetY = 6;
		double lastLength = 0;
		boolean firstModulePassed = false;
		double lastLeft = 0;
		
		float toggleSpeed = 0.0075F;

		Module previousModule;
		
		Color color1;
		Color color2;
		
		if(color.is("Normal")) {
			color1 = new Color(5, 138, 255);
			color2 = new Color(0, 36, 217);
		} else {
			color1 = new Color((int) red1.getCurrentValue(), (int) green1.getCurrentValue(), (int) blue1.getCurrentValue());
			color2 = new Color((int) red2.getCurrentValue(), (int) green2.getCurrentValue(), (int) blue2.getCurrentValue());
		}
		
		boolean rainbow = color.is("Rainbow");
		
		/*
		if(blur.isEnabled()) {
			for(Module m : modules) {
				String displayName = m.getDisplayName();
				double nameLength = fr.getStringWidth(displayName);

				float mult;

				if(m.isEnabled()) {
					mult = (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
				} else {
					mult = 1 - (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
				}

				mult = Math.min(mult, 1);
				
				double left = screenWidth - nameLength - 9;
				double top = offsetY;
				double right = screenWidth - 4;
				double bottom = offsetY + 11;

				float width = (float) (right - left);
				float height = (float) (top - bottom) / 2;

				if(mult <= 0) continue;
				
				//Gui.drawRect(left, top, right, offsetY + 11 * mult, 0x30f7f6f2);
				Gui.drawRect(left, top, right, offsetY + 11 * mult, 0x10000000);
				BlurUtil.blur((float) left - 2, (float) top, (float) right + 3, (float) bottom);
				
				offsetY += 11 * mult;
	    	}
		}
		*/
    	
    	offsetY = 6;	
		
		for(Module m : modules) {
			String displayName = m.getDisplayName();
			double nameLength = fr.getStringWidth(displayName);

			float mult;

			if(m.isEnabled()) {
				mult = (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			} else {
				mult = 1 - (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			}

			mult = Math.min(mult, 1);

			if(mult > 0) {
				lastLength = nameLength;
			}
			
			double left = screenWidth - nameLength - 9;
			double top = offsetY;
			double right = screenWidth - 4;
			double bottom = offsetY + 11;

			float width = (float) (right - left);
			float height = (float) (top - bottom) / 2;

			if(mult <= 0) continue;

			if(!firstModulePassed) {
				Gui.drawRect(left - 2, top - 2, right + 2, top, 0x20000000);
			} else {
				Gui.drawRect(lastLeft, top - 1, left, top + 1, 0x20000000);
			}

			if(mult == 1) {
				Gui.drawRect(left - 2, firstModulePassed ? top + 1 : top, left, 2 + offsetY + 9.5 * mult, 0x20000000);
			}

			startScaling(mult, (float) right, (float) top, width, height);
			
			this.drawString(fr, displayName, screenWidth - nameLength - 7, (float) offsetY + 1.5F, offsetY);

			stopScaling(mult, (float) right, (float) top, width, height);

			offsetY += 11 * mult;

			lastLeft = left;
			
			firstModulePassed = true;
			previousModule = m;
		}
		
		double left = screenWidth - lastLength - 9;
		double right = screenWidth - 4;

		Gui.drawRect(screenWidth - 4, 6, screenWidth - 2, offsetY + 2, 0x20000000);
		Gui.drawRect(left - 2, offsetY, right, offsetY + 2, 0x20000000);
    }
    
    private void renderNew2() {
    	MinecraftFontRenderer fr = FontUtil.product_sans;
    	
    	Color color1;
		Color color2;
		
		if(color.is("Normal")) {
			color1 = new Color(5, 138, 255);
			color2 = new Color(0, 36, 217);
		} else {
			color1 = new Color((int) red1.getCurrentValue(), (int) green1.getCurrentValue(), (int) blue1.getCurrentValue());
			color2 = new Color((int) red2.getCurrentValue(), (int) green2.getCurrentValue(), (int) blue2.getCurrentValue());
		}
		
		int screenWidth = mc.displayWidth / 2;
    	
    	double bpt = Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ);
        double bps = bpt * 20 * mc.timer.timerSpeed;
        double roundedBps = Math.round(bps * 100) / 100.0D;
        
        fr.drawStringWithShadow("BPS : " + roundedBps, 4, mc.displayHeight / 2 - 11, -1);
		
		double offsetY = 6;
		float toggleSpeed = 0.0075F;
    	
    	boolean rainbow = color.is("Rainbow");
    	
    	String clientName = "V" + ChatFormatting.WHITE + "estige " + Vestige.getInstance().getVersion() + " | " + Vestige.getInstance().getUsername() + " | " + mc.getDebugFPS() + "FPS";
    	
    	Gui.drawRect(4, 6, 6 + fr.getStringWidth(clientName) - 0.5, 19, 0x15f7f6f2);
    	BlurUtil.blur(3.5F, 5.5F, (float) (8 + fr.getStringWidth(clientName)) + 0.5F, 20.5F);
    	
    	for(double x = 4; x < 8 + fr.getStringWidth(clientName); x++) {
    		Gui.drawRect(x, 4.5, x + 1, 7, ColorUtil.customColors(color1, color2, rainbow, 3F, (long) (25 - (x * (rainbow ? 8 : 10)))).getRGB());
    	}
    	
    	DrawUtil.drawRoundedRect(3, 19, 4 + fr.getStringWidth(clientName) + 5, 21, 2, 0x20000000);

		DrawUtil.drawRoundedRect(2, 5, 4, 20, 2, 0x20000000);
		DrawUtil.drawRoundedRect(8 + fr.getStringWidth(clientName), 5, 10 + fr.getStringWidth(clientName), 20, 2, 0x20000000);
    	
    	fr.drawStringWithShadow(clientName, 6, 9, ColorUtil.customColors(color1, color2, rainbow, 3F, -50).getRGB());
    	
    	/*
    	for(Module m : modules) {
			String displayName = m.getDisplayName();
			double nameLength = fr.getStringWidth(displayName);

			float mult;

			if(m.isEnabled()) {
				mult = (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			} else {
				mult = 1 - (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			}

			mult = Math.min(mult, 1);
			
			double left = screenWidth - nameLength - 9;
			double top = offsetY;
			double right = screenWidth - 4;
			double bottom = offsetY + 10;

			float width = (float) (right - left);
			float height = (float) (top - bottom) / 2;

			if(mult <= 0) continue;
			
			//Gui.drawRect(left, top, right, bottom, 0x30f7f6f2);
			//Gui.drawRect(left, top, right, offsetY + 11 * mult, 0x10f7f6f2);
			//BlurUtil.blur((float) left, (float) top, (float) right, (float) bottom);
			//Gui.drawRect(left - 2, top, left, offsetY + 11 * mult, ColorUtil.customColors(color1, color2, rainbow, rainbow ? 3F : 1.5F, (int) offsetY * -10).getRGB());
			
			offsetY += 10 * mult;
    	}
    	
    	offsetY = 6;
    	*/
    	
    	for(Module m : modules) {
			String displayName = m.getDisplayName();
			double nameLength = fr.getStringWidth(displayName);

			float mult;

			if(m.isEnabled()) {
				mult = (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			} else {
				mult = 1 - (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			}

			mult = Math.min(mult, 1);
			
			double left = screenWidth - nameLength - 9;
			double top = offsetY;
			double right = screenWidth - 4;
			double bottom = offsetY + 10;

			float width = (float) (right - left);
			float height = (float) (top - bottom) / 2;

			if(mult <= 0) continue;
			
			startScaling(mult, (float) right, (float) top, width, height);
			
			this.drawString(fr, displayName, screenWidth - nameLength - 7, (float) offsetY + 1.5F, offsetY);

			stopScaling(mult, (float) right, (float) top, width, height);
			
			offsetY += 10 * mult;
    	}
    }
    
    private void renderVestigeV1() {
    	MinecraftFontRenderer fr = FontUtil.product_sans;
		ScaledResolution sr = new ScaledResolution(mc);

		Color color1;
		Color color2;

		float toggleSpeed = 0.0075F;

		if(color.is("Custom")) {
			color1 = new Color((int) red1.getCurrentValue(), (int) green1.getCurrentValue(), (int) blue1.getCurrentValue());
			color2 = new Color((int) red2.getCurrentValue(), (int) green2.getCurrentValue(), (int) blue2.getCurrentValue());
		} else {
			color1 = new Color(5, 138, 255);
			color2 = new Color(0, 36, 217);
		}
		
		boolean rainbow = color.is("Rainbow");

		this.renderVestigeV1Watermark();
		
		double bpt = Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ);
        double bps = bpt * 20 * mc.timer.timerSpeed;
        double roundedBps = Math.round(bps * 100) / 100.0D;

		fr.drawStringWithShadow("BPS : " + roundedBps, 3, sr.getScaledHeight() - 12, -1);
		
		int offsetY = 6;
		double lastLength = 0;
		boolean firstModulePassed = false;
		double lastLeft = 0;

		double nameLength = 0;

		for(Module m : modules) {
			String displayName = m.getDisplayName();
			nameLength = fr.getStringWidth(displayName);

			float mult;

			if(m.isEnabled()) {
				mult = (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			} else {
				mult = 1 - (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			}

			mult = Math.min(mult, 1);

			if(mult > 0) {
				lastLength = nameLength;
			}
			
			float startX = (float) (sr.getScaledWidth() - nameLength - 10);
			float startY = offsetY + 11;

			float endX = sr.getScaledWidth() - 3;
			float endY = offsetY;

			float width = endX - startX;
			float height = (endY - startY) / 2;

			if(mult <= 0) continue;

			startScaling(mult, endX, endY, width, height);

			Gui.drawRect(sr.getScaledWidth() - nameLength - 8, offsetY, sr.getScaledWidth() - 3, offsetY + 11, 0x70000000);
			Gui.drawRect(sr.getScaledWidth() - nameLength - 10, offsetY, sr.getScaledWidth() - fr.getStringWidth(m.getDisplayName()) - 8, offsetY + 11, ColorUtil.customColors(color1, color2, rainbow, rainbow ? 3F : 1.5F, offsetY * -10).getRGB());
			fr.drawStringWithShadow(displayName, sr.getScaledWidth() - nameLength - 6, offsetY + 1.5F, ColorUtil.customColors(color1, color2, rainbow, rainbow ? 3F : 1.5F, offsetY * -10).getRGB());

			stopScaling(mult, endX, endY, width, height);

			offsetY += 11 * mult;
		}
    }
    
    private void startScaling(float mult, float endX, float endY, float width, float height) {
		float xAmount = endX - (width / 2);
		float yAmount = endY - (height / 2);

		float finalXAnim = xAmount - mult * xAmount;
		float finalYAnim = yAmount - mult * yAmount;

		GL11.glTranslatef(finalXAnim, finalYAnim, 1);
		GL11.glScalef(mult, mult, 1);
	}

	private void stopScaling(float mult, float endX, float endY, float width, float height) {
		float xAmount = endX - (width / 2);
		float yAmount = endY - (height / 2);

		float finalXAnim = xAmount - mult * xAmount;
		float finalYAnim = yAmount - mult * yAmount;

		GL11.glScalef(1 / mult, 1 / mult, 1);
		GL11.glTranslatef(-finalXAnim, -finalYAnim, 1);
	}
    
    private void renderNewWatermark() {
    	MinecraftFontRenderer fr = FontUtil.product_sans;
    	
    	String watermark = "V" + ChatFormatting.WHITE + "estige " + Vestige.getInstance().getVersion() + " | " + mc.getDebugFPS() + "FPS | " + Vestige.getInstance().getUsername();
    	//String watermark = "V" + ChatFormatting.WHITE + "estige " + Vestige.getInstance().getVersion() + " | " + mc.getDebugFPS() + "FPS | " + (Vestige.getInstance().getModuleManager().getModule(NameProtect.class).isEnabled() ? "Free version" : mc.session.getUsername());
		double length = fr.getStringWidth(watermark);
		
		Color color1;
		Color color2;
		
		if(color.is("Normal")) {
			color1 = new Color(5, 138, 255);
			color2 = new Color(0, 36, 217);
		} else {
			color1 = new Color((int) red1.getCurrentValue(), (int) green1.getCurrentValue(), (int) blue1.getCurrentValue());
			color2 = new Color((int) red2.getCurrentValue(), (int) green2.getCurrentValue(), (int) blue2.getCurrentValue());
		}
		
		boolean rainbow = color.is("Rainbow");
		
		if(blur.isEnabled()) {
			BlurUtil.blur(4, 6, (float) (8 + fr.getStringWidth(watermark)), 16);
		}
		
		int aaa = 0;
		for(int i = 4; i < length + 7; i++) {
			if(i == 4) {
				Gui.drawRect(i - 0.5, 3, i + 1, 3.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (i * (rainbow ? 8 : 10))).getRGB());
				Gui.drawRect(i - 1, 3.5, i + 1, 4, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (i * (rainbow ? 8 : 10))).getRGB());
				Gui.drawRect(i - 1, 4, i + 1, 4.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (i * (rainbow ? 8 : 10))).getRGB());
				Gui.drawRect(i - 1, 4.5, i + 1, 5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (i * (rainbow ? 8 : 10))).getRGB());
				Gui.drawRect(i - 1, 5, i + 1, 5.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (i * (rainbow ? 8 : 10))).getRGB());
				Gui.drawRect(i - 0.5, 5.5, i + 1, 6, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (i * (rainbow ? 8 : 10))).getRGB());
			}
			Gui.drawRect(i, 3, i + 1, 6, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (i * (rainbow ? 8 : 10))).getRGB());
			aaa = i;
		}


		Gui.drawRect(aaa, 3, aaa + 2, 3.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * (rainbow ? 8 : 10))).getRGB());
		Gui.drawRect(aaa, 3.5, aaa + 2.5, 4, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * (rainbow ? 8 : 10))).getRGB());
		Gui.drawRect(aaa, 4, aaa + 2.5, 4.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * (rainbow ? 8 : 10))).getRGB());
		Gui.drawRect(aaa, 4.5, aaa + 2.5, 5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * (rainbow ? 8 : 10))).getRGB());
		Gui.drawRect(aaa, 5, aaa + 2.5, 5.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * (rainbow ? 8 : 10))).getRGB());
		Gui.drawRect(aaa, 5.5, aaa + 2, 6, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * (rainbow ? 8 : 10))).getRGB());

		DrawUtil.drawRoundedRect(3, 19, 4 + length + 5, 21, 2, 0x20000000);

		DrawUtil.drawRoundedRect(2, 4, 4, 20, 2, 0x20000000);
		DrawUtil.drawRoundedRect(8 + length, 4, 10 + length, 20, 2, 0x20000000);
		
		fr.drawStringWithShadow(watermark, 6, 9, ColorUtil.customColors(color1, color2, rainbow, 3F, -50).getRGB());
    }
    
    private void renderVestigeV1Watermark() { // lazy to recode xd
    	MinecraftFontRenderer fr = FontUtil.product_sans;
    	
    	String watermark = "V" + ChatFormatting.WHITE + "estige " + Vestige.getInstance().getVersion() + " | " + mc.getDebugFPS() + "FPS | " + Vestige.getInstance().getUsername();
		double length = fr.getStringWidth(watermark);
		
		Color color1;
		Color color2;
		
		if(color.is("Normal")) {
			color1 = new Color(5, 138, 255);
			color2 = new Color(0, 36, 217);
		} else {
			color1 = new Color((int) red1.getCurrentValue(), (int) green1.getCurrentValue(), (int) blue1.getCurrentValue());
			color2 = new Color((int) red2.getCurrentValue(), (int) green2.getCurrentValue(), (int) blue2.getCurrentValue());
		}
		
		boolean rainbow = color.is("Rainbow");
    	
		for(int i = 0; i < 2; i++) {
			DrawUtil.drawRoundedRect(3, 2, fr.getStringWidth(watermark) + 10, 20, 4.5, 0x60000000);
		}
		
		int aaa = 0;
		
		for(int i = 6; i < fr.getStringWidth(watermark) + 6; i++) {
			if(i == 6) {
				Gui.drawRect(i - 0.5, 3, i + 1, 3.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (i * 10)).getRGB());
				Gui.drawRect(i - 1, 3.5, i + 1, 4, ColorUtil.customColors(color1, color2,rainbow, 3F, 25 - (i * 10)).getRGB());
				Gui.drawRect(i - 1, 4, i + 1, 4.5, ColorUtil.customColors(color1, color2,rainbow, 3F, 25 - (i * 10)).getRGB());
				Gui.drawRect(i - 1, 4.5, i + 1, 5, ColorUtil.customColors(color1, color2,rainbow, 3F, 25 - (i * 10)).getRGB());
				Gui.drawRect(i - 1, 5, i + 1, 5.5, ColorUtil.customColors(color1, color2,rainbow, 3F, 25 - (i * 10)).getRGB());
				Gui.drawRect(i - 0.5, 5.5, i + 1, 6, ColorUtil.customColors(color1, color2,rainbow, 3F, 25 - (i * 10)).getRGB());
			}
			Gui.drawRect(i, 3, i + 1, 6, ColorUtil.customColors(color1, color2,rainbow, 3F, 25 - (i * 10)).getRGB());
			aaa = i;
		}


		Gui.drawRect(aaa, 3, aaa + 2, 3.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * 10)).getRGB());
		Gui.drawRect(aaa, 3.5, aaa + 2.5, 4, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * 10)).getRGB());
		Gui.drawRect(aaa, 4, aaa + 2.5, 4.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * 10)).getRGB());
		Gui.drawRect(aaa, 4.5, aaa + 2.5, 5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * 10)).getRGB());
		Gui.drawRect(aaa, 5, aaa + 2.5, 5.5, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * 10)).getRGB());
		Gui.drawRect(aaa, 5.5, aaa + 2, 6, ColorUtil.customColors(color1, color2, rainbow, 3F, 25 - (aaa * 10)).getRGB());
		
		if(color.is("Normal")) {
			DrawUtil.drawRoundedRect(6, 5, fr.getStringWidth(watermark) + 7, 7, 4, 0x300e5de6);
		}

		fr.drawStringWithShadow(watermark, 6, 9, ColorUtil.customColors(color1, color2, rainbow, 3F, -50).getRGB());
	}

    
    private void renderSimple() {
    	FontRenderer fr = mc.fontRendererObj;
        
        fr.drawStringWithShadow("V" + ChatFormatting.WHITE + "estige " + Vestige.getInstance().getVersion(), 4, 4, Vestige.getInstance().getClientColor().getRGB());
        
        if(showBalance.isEnabled()) {
        	fr.drawStringWithShadow("Balance : " + Vestige.getInstance().getBalanceProcessor().getBalance(), 4, 15, -1);
        }
        
        double bpt = Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ);
        double bps = bpt * 20 * mc.timer.timerSpeed;
        double roundedBps = Math.round(bps * 100) / 100.0D;
        
        fr.drawStringWithShadow("BPS : " + roundedBps, 4, mc.displayHeight / 2 - 11, -1);
        
        float y = 8;

        for(Module module : modules) {
            if (module.isEnabled()) {
                String name = module.getDisplayName();
                float moduleLength = fr.getStringWidth(name);

                fr.drawStringWithShadow(name, mc.displayWidth / 2 - moduleLength - 6, y, Vestige.getInstance().getClientColor().getRGB());

                y += 10;
            }
        }
    }
    
    private void renderSimple2() {
    	FontRenderer fr = mc.fontRendererObj;
        
    	Color color1;
		Color color2;
		
		if(color.is("Normal")) {
			color1 = new Color(5, 138, 255);
			color2 = new Color(0, 36, 217);
		} else {
			color1 = new Color((int) red1.getCurrentValue(), (int) green1.getCurrentValue(), (int) blue1.getCurrentValue());
			color2 = new Color((int) red2.getCurrentValue(), (int) green2.getCurrentValue(), (int) blue2.getCurrentValue());
		}
		
		boolean rainbow = color.is("Rainbow");
    	
        fr.drawStringWithShadow("V" + ChatFormatting.WHITE + "estige " + Vestige.getInstance().getVersion(), 4, 4, ColorUtil.customColors(color1, color2, rainbow, rainbow ? 3F : 1F, 50).getRGB());
        fr.drawStringWithShadow("FPS : " + mc.getDebugFPS(), 4, 15, -1);
        
        if(showBalance.isEnabled()) {
        	fr.drawStringWithShadow("Balance : " + Vestige.getInstance().getBalanceProcessor().getBalance(), 4, 15, -1);
        }
        
        double bpt = Math.hypot(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ);
        double bps = bpt * 20 * mc.timer.timerSpeed;
        double roundedBps = Math.round(bps * 100) / 100.0D;
        
        fr.drawStringWithShadow("BPS : " + roundedBps, 4, mc.displayHeight / 2 - 11, -1);
        
        double offsetY = 8;
		double lastLength = 0;
		boolean firstModulePassed = false;
		double lastLeft = 0;
		
		float toggleSpeed = 0.0075F;
		
		int screenWidth = mc.displayWidth / 2;

        for(Module m : modules) {
        	String displayName = m.getDisplayName();
			double nameLength = fr.getStringWidth(displayName);

			float mult;

			if(m.isEnabled()) {
				mult = (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			} else {
				mult = 1 - (float) (m.getToggleTimer().getTimeElapsed() * toggleSpeed);
			}

			mult = Math.min(mult, 1);
			
			double left = screenWidth - nameLength - 10;
			double top = offsetY;
			double right = screenWidth - 6;
			double bottom = offsetY + 10;

			float width = (float) (right - left);
			float height = (float) (top - bottom) / 2;

			if(mult <= 0) continue;
			
			if(boxes.isEnabled()) {
				Gui.drawRect(left, top - 1, right, offsetY + 10 * mult - 1, 0x45000000);
			}
			
			startScaling(mult, (float) right, (float) top, width, height);
			
			this.drawString(displayName, screenWidth - nameLength - 8, (float) offsetY, offsetY);

			stopScaling(mult, (float) right, (float) top, width, height);
			
			if(rightBars.isEnabled()) {
				for(double i = offsetY - 1; i < offsetY + 10 * mult - 1; i++) {
					Gui.drawRect(right, i, right + 2, i + 1, ColorUtil.customColors(color1, color2, rainbow, rainbow ? 3F : 1F, (long) (offsetY + i * -10)).getRGB());
				}
			}
			
			offsetY += 10 * mult;
        }
    }
    
    private void drawString(MinecraftFontRenderer fr, String text, double x, float y, double offsetY) {
    	switch (mode.getMode()) {
    		case "New":
    		case "New2":
    			Color color1;
    			Color color2;
    			
    			if(color.is("Normal")) {
    				color1 = new Color(5, 138, 255);
    				color2 = new Color(0, 36, 217);
    			} else {
    				color1 = new Color((int) red1.getCurrentValue(), (int) green1.getCurrentValue(), (int) blue1.getCurrentValue());
    				color2 = new Color((int) red2.getCurrentValue(), (int) green2.getCurrentValue(), (int) blue2.getCurrentValue());
    			}
    			
    			boolean rainbow = color.is("Rainbow");
    			
    			fr.drawStringWithShadow(text, (float) x, y, ColorUtil.customColors(color1, color2, rainbow, rainbow ? 3F : 1.5F, (int) offsetY * -10).getRGB());
    			break;
    	}
    }
    
    private void drawString(String text, double x, float y, double offsetY) {
    	switch (mode.getMode()) {
    		case "Simple2":
    			Color color1;
    			Color color2;
    			
    			if(color.is("Normal")) {
    				color1 = new Color(5, 138, 255);
    				color2 = new Color(0, 36, 217);
    			} else {
    				color1 = new Color((int) red1.getCurrentValue(), (int) green1.getCurrentValue(), (int) blue1.getCurrentValue());
    				color2 = new Color((int) red2.getCurrentValue(), (int) green2.getCurrentValue(), (int) blue2.getCurrentValue());
    			}
    			
    			boolean rainbow = color.is("Rainbow");
    			
    			mc.fontRendererObj.drawStringWithShadow(text, (float) x, y, ColorUtil.customColors(color1, color2, rainbow, rainbow ? 3F : 1F, (int) offsetY * -10).getRGB());
    			break;
    	}
    }
    
    private void drawRect(double x1, double y1, double x2, double y2) {
    	switch (mode.getMode()) {
    		case "New":
    			break;
    	}
    }
    
    public Color getCurrentColor() {
    	switch (mode.getMode()) {
    		case "Simple":
    			return Vestige.getInstance().getClientColor();
    		case "Simple2":
    		case "New":
    		case "New2":
    		case "Vestige V1":
    			if(color.is("Normal")) {
    				return ColorUtil.getVestigeColors(1.5F, 50);
    			} else if(color.is("Custom")) {
    				return ColorUtil.customColors(new Color((int) red1.getCurrentValue(), (int) green1.getCurrentValue(), (int) blue1.getCurrentValue()), new Color((int) red2.getCurrentValue(), (int) green2.getCurrentValue(), (int) blue2.getCurrentValue()), false, 1.5F, 50);
    			} else if(color.is("Rainbow")) {
    				return ColorUtil.customColors(null, null, true, 3F, 50);
    			}
    	}
    	
    	return Vestige.getInstance().getClientColor();
    }
    
}
