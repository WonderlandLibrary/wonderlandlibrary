package epsilon.ClickGUI.dropdown;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Color;

import com.mojang.realmsclient.gui.ChatFormatting;

import epsilon.Epsilon;
import epsilon.modules.Module;
import epsilon.modules.Module.Category;
import epsilon.modules.render.Theme;
import epsilon.settings.Setting;
import epsilon.settings.setting.*;
import epsilon.util.EpsilonColorUtils;
import epsilon.util.ShapeUtils;
import epsilon.util.Font.Fonts;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class DropdownGUI extends GuiScreen {
	
	private long beginTimer = 0, initTimer, begint = 0;

	private final ShapeUtils render = new ShapeUtils();
	
	private int wheelOffset = 0;
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		
		if(beginTimer<300) {
			beginTimer+=5;
			begint = -beginTimer + 300;
			
		}
		//this.drawGradientRect(0, 0, this.width, this.height, EpsilonColorUtils.getColorAsInt(0, Theme.getTheme(), 0.5F, 50), EpsilonColorUtils.getColorAsInt(50, Theme.getTheme(), 0.5F, 50));
		render.rect(0, 0, this.width, this.height, EpsilonColorUtils.getColor(0, Theme.getTheme(), 0.5F, 50));

        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                wheelOffset -= 16;
            } else if (wheel > 0) {
                wheelOffset += 16;
            }
        }
        if(wheelOffset>this.height-400) wheelOffset = this.height-400;
        
		
		int count = 0, colorInt = 0;
		for(Category c : Module.Category.values()) {
			int moduleCount = 0;
			List<Module> modules = Epsilon.getModulesbyCategory(c);


			int categoryOffset = 0;
			int moduleSettingsOffset = 0;
			int pureOffsetSettings =0;
			
			for(Module m : modules) {
				if(m.settingsExpanded) {
					categoryOffset+= m.settings.size()*15 + 1;
					pureOffsetSettings++;
				}
			}
			
			render.rect(this.width/23+count, 45 + wheelOffset + begint,  95, 25, new Color(66, 66, 66, 255));
			render.rect(this.width/23+count, 45 + wheelOffset+ begint,  95, 25, false, EpsilonColorUtils.getColor(colorInt, Theme.getTheme(), 0.5F));
			Fonts.MontserratReg24.drawCenteredStringWithShadow(c.name, this.width/23+count+44, 50 + wheelOffset+ begint, -1);

			render.roundedRectCustom(this.width/23.5+count, 84 + wheelOffset+ begint, 96, modules.size()*20+2 + categoryOffset, 5, EpsilonColorUtils.getColor(colorInt, Theme.getTheme(), 0.5F), false, false, true, true);
			render.roundedRectCustom(this.width/23+count+0.6, 85 + wheelOffset+ begint, 94, modules.size()*20 + categoryOffset, 5, new Color(66, 66, 66, 255), false, false, true, true);

			for(Module m : modules) {
				int sOffset = 0;

				m.hovered = mouseOver(this.width/23+count+15, 90+moduleCount+ wheelOffset+ begint + moduleSettingsOffset/2.3f, mc.fontRendererObj.getStringWidth(m.name + (m.name.length()<5 ? "homer" : "21")), 10, mouseX, mouseY);
				if(mouseOver(this.width/23+count+15, 90+moduleCount+ wheelOffset+ begint + moduleSettingsOffset/2.3f, mc.fontRendererObj.getStringWidth(m.name + (m.name.length()<5 ? "homer" : "21")), 10, mouseX, mouseY)) {
					render.rect(this.width/23+count+0.3f, 85+moduleCount+ wheelOffset+ begint + moduleSettingsOffset/2.3f, 94, 17,EpsilonColorUtils.getColor(colorInt, Theme.getTheme(), 0.5f, 55));
					
				}
				Fonts.MontserratReg18.drawCenteredStringWithShadow(m.name, this.width/23+count+44, 90+moduleCount+ wheelOffset+ begint + moduleSettingsOffset/2.3f, m.toggled ? 0xFF8f8f8f : -1);

				if(m.settingsExpanded) {
					moduleSettingsOffset+=m.settings.size()*35;
					
					render.roundedRect(this.width/23.5+count-10, 90 + wheelOffset+ begint + moduleCount, 6, modules.size()*1.1f + categoryOffset - 3  +pureOffsetSettings*3, 5, EpsilonColorUtils.getColor(colorInt, Theme.getTheme(), 0.5F, 150));
					
					for(Setting s : m.settings) {

						if(s instanceof BooleanSetting) {
							s.hovered = mouseOver(this.width/23+count+5, 93+moduleCount+ wheelOffset+ begint + sOffset + (moduleSettingsOffset - m.settings.size()*35 + 15),mc.fontRendererObj.getStringWidth(m.name + (m.name.length()<5 ? "homer" : "21")), 10, mouseX, mouseY);
							final BooleanSetting set = (BooleanSetting) s;
							Fonts.MontserratReg18.drawStringWithShadow(s.name + (set.isEnabled() ? (ChatFormatting.GREEN + " (o)") : (ChatFormatting.RED + " (o) ")), this.width/23+count+5, 93+moduleCount+ wheelOffset+ begint + sOffset + (moduleSettingsOffset - m.settings.size()*35 + 15), -1);
							
							
						}
						
						if(s instanceof KeybindSetting) {
							final KeybindSetting k = (KeybindSetting) s;
							String key = Keyboard.getKeyName(k.getKeycode());
							Fonts.MontserratReg18.drawStringWithShadow(s.name + ": "+ ChatFormatting.GRAY + key, this.width/23+count+5, 93+moduleCount+ wheelOffset+ begint + sOffset + (moduleSettingsOffset - m.settings.size()*35 + 15), -1);
							
							
						}
						
						if(s instanceof ModeSetting) {
							s.hovered = mouseOver(this.width/23+count+5, 93+moduleCount+ wheelOffset+ begint + sOffset + (moduleSettingsOffset - m.settings.size()*35 + 15),mc.fontRendererObj.getStringWidth(m.name + (m.name.length()<5 ? "homer" : "21")), 10, mouseX, mouseY);
						
							final ModeSetting mode = (ModeSetting) s;

							Fonts.MontserratReg18.drawStringWithShadow(s.name + ": "+ ChatFormatting.GRAY + " >", this.width/23+count+5, 93+moduleCount+ wheelOffset+ begint + sOffset + (moduleSettingsOffset - m.settings.size()*35 + 15), -1);
							
						}
						
						if(s instanceof NumberSetting) {
							s.hovered = mouseOver(this.width/23+count+5-s.name.length()*2, 93+moduleCount+ wheelOffset+ begint + sOffset + (moduleSettingsOffset - m.settings.size()*35 + 15),mc.fontRendererObj.getStringWidth(m.name + (m.name.length()<5 ? "homer" : "21")+s.name.length()+this.width), 10, mouseX, mouseY);
						
							final NumberSetting n = (NumberSetting) s;
							
							Fonts.MontserratReg18.drawStringWithShadow(s.name + ": "+ ChatFormatting.RED + "< " +ChatFormatting.GRAY + n.getValue() + ChatFormatting.GREEN + " > ", this.width/23+count+5, 93+moduleCount+ wheelOffset+ begint + sOffset + (moduleSettingsOffset - m.settings.size()*35 + 15), -1);
							
						}
						
						if(s instanceof NoteSetting) {
							final NoteSetting n = (NoteSetting) s;

							Fonts.MontserratReg18.drawStringWithShadow(s.name, this.width/23+count+30, 93+moduleCount+ wheelOffset+ begint + sOffset + (moduleSettingsOffset - m.settings.size()*35 + 15), -1);
							
						}
						
						if(s.hovered) {
							render.rect(this.width/23.5+count, 87+moduleCount+ wheelOffset+ begint + sOffset + (moduleSettingsOffset - m.settings.size()*35 + 15), 95, 19,EpsilonColorUtils.getColor(colorInt, Theme.getTheme(), 0.5f, 55));
							
						}
						sOffset+=15;
						
					}
					
				}
				
				moduleCount+=20;
			}
			
			count+=this.width/6;
			colorInt++;
		}
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}


	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceMouseClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceMouseClick);
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		for(Module m : Epsilon.modules) {
			if(m.hovered) {
				if(mouseButton == 0)
				m.toggle();
				mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 0.4F + 0.8F));

				if(mouseButton == 1)
					m.settingsExpanded = !m.settingsExpanded;
				
			}

			for(Setting s : m.settings) {
				if(s.hovered) {
					if(s instanceof BooleanSetting) {
						
						((BooleanSetting) s).toggle();
						
					}
					
					if(s instanceof ModeSetting) {
						mc.displayGuiScreen(new ModeSettingGUI((ModeSetting) s, m.name));
					}
					
					if(s instanceof NumberSetting) {
						if(mouseButton == 0) {
							((NumberSetting)s).increment(true);
						}else ((NumberSetting)s).increment(false);
					}
					
				}
				
				
			}
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	public void initGui() {
		beginTimer = 0;
		begint = - 300;
		initTimer = System.currentTimeMillis();
		super.initGui();
		
	}
	
	public void onGuiClosed() {
		begint = - 300;
		super.onGuiClosed();
	}
	
	public boolean mouseOver(final float posX, final float posY, final float width, final float height, final float mouseX, final float mouseY) {
		return mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height;
	}
	
	private int midwidth() {
		return this.width/2;
	}
	
	private int midheight() {
		return this.height/2;
	}
}
