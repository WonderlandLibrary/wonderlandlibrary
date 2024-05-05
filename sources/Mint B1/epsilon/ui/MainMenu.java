package epsilon.ui;

import java.net.URI;
import java.util.ArrayList;

import org.lwjgl.util.Color;

import epsilon.Epsilon;
import epsilon.altmanager.GuiAltManager;
import epsilon.botting.MintAttackGUI;
import epsilon.font.CustomFont;
import epsilon.font.fontrenderer.TTFFontRenderer;
import epsilon.util.ColorUtil;
import epsilon.util.ShapeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MainMenu extends GuiScreen {
	
	private final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Skid 55");
	
	private float x, y, screenWidth, screenHeight;
    private ArrayList<String> changelog = new ArrayList();
    private final ShapeUtils draw = new ShapeUtils();
    
    
    
    
	public MainMenu() {
		
	}
	
	public void initGui() {
	changelog.clear();
	//Bypass
	changelog.add("");
	changelog.add("Bypass");
	changelog.add("UpdatedNCP Disabler");
	changelog.add("Removed useless modes in fly, speed, etc");
	changelog.add("");
	
	//Visual
	changelog.add("Visual");
	changelog.add("ClickGUI :o");
	changelog.add("Improved Arraylist");
	changelog.add("Improved TabGUI");
	changelog.add("");
	
	//Misc
	changelog.add("Misc");
	changelog.add("Mint Stresser");
	changelog.add("");
	
	super.initGui();
		
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		
		x = (sr.getScaledWidth() / 2.0F) - (screenWidth / 2.0F);
        y = (sr.getScaledHeight() / 2.0F) - (screenHeight / 2.0F) - 6;
		
		mc.getTextureManager().bindTexture(new ResourceLocation("epsilon/MainMenu/nigglemethis.png"));
		
		this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
		
		this.drawGradientRect(0, height - 1400, width, height, 0x00000000, 0xff000000);
		
		
		FontRenderer fr = mc.fontRendererObj;
		fr.drawString("Changelog", 1, 5, -1);
		
		for (int i = 0; i < changelog.size(); i++) {
            fr.drawString(changelog.get(i), 1, 16 + i * 12, -1);
        }
		
		draw.roundedRectNonFilled(width/2-60, height/2-45, 120,19,5, new Color(255,255,255,255), true, true, false, false);
		draw.roundedRectCustom(width/2-60, height/2-45, 120,19,5, new Color(255,255,255,155), true, true, false, false);
		
		draw.roundedRectNonFilled(width/2-60, height/2-25, 120,19,5, new Color(255,255,255,255), false, false, false, false);
		draw.roundedRect(width/2-60, height/2-25, 120,19,5, new Color(255,255,255,155));
		
		draw.roundedRectNonFilled(width/2-60, height/2-5, 120,19,5, new Color(255,255,255,255), false, false, false, false);
		draw.roundedRect(width/2-60, height/2-5, 120,19,5, new Color(255,255,255,155));
		
		draw.roundedRectNonFilled(width/2-60, height/2+15, 120,19,5, new Color(255,255,255,255), false, false, false, false);
		draw.roundedRect(width/2-60, height/2+15, 120,19,5, new Color(255,255,255,155));
		
		draw.roundedRectNonFilled(width/2-60, height/2+35, 120,19,5, new Color(255,255,255,255), false, false, false, false);
		draw.roundedRect(width/2-60, height/2+35, 120,19,5, new Color(255,255,255,155));
		
		draw.roundedRectNonFilled(width/2-60, height/2+55, 120,19,5, new Color(255,255,255,255), false, false, true, true);
		draw.roundedRectCustom(width/2-60, height/2+55, 120,19,5, new Color(255,255,255,155),  false, false, true, true);
		//draw.rect(width/2-60, height/2-45, 120,19, false, new Color(255,255,255,255));
		
		//ShapeUtils.roundedRect(500, height - 460, width/5, height - 325, 15,new Color(255, 255, 255, 255));
		
		String[] buttons = { "Singleplayer", "Multiplayer", "AltManager", "Settings", "Language", "ServerBotter"};
		
		
		
		int count = -40;
		for(String name : buttons) {
			final float x = width/2 + 8 - mc.fontRendererObj.getStringWidth(name)/2f;
			final float y = height/2+count;
			
			boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(name) && mouseY < y + mc.fontRendererObj.FONT_HEIGHT;
			
			this.drawCenteredString(mc.fontRendererObj, name, width/2, height/2+count, hovered ? ColorUtil.getRainbow(10, 0.6f, 1) : -1);
			count+=20;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(width/2f, height/2f, 0);
		GlStateManager.scale(3, 3, 1);
		GlStateManager.translate(-(width/2f), -(height/2f), 0);
		this.drawCenteredString(fontRenderer, Epsilon.name, width/2f, height/2f - mc.fontRendererObj.FONT_HEIGHT/2f - 55, -1);
		GlStateManager.popMatrix();
	}
	
	

	public void mouseClicked(int mouseX, int mouseY, int button) {
		String[] buttons = { "Singeplayer", "Multiplayer", "AltManager", "Settings", "Language", "ServerBotter"};

		int count = -40;
		for(String name : buttons) {
			final float x = width/2 + 8 - mc.fontRendererObj.getStringWidth(name)/2f;
			final float y = height/2+count;
			
			if(mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(name) && mouseY < y + mc.fontRendererObj.FONT_HEIGHT) {
				mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 0.4F + 0.8F));
				
				switch(name) {
					case "Singeplayer":
						mc.displayGuiScreen(new GuiSelectWorld(this));
						break;
						
					case "Multiplayer":
						mc.displayGuiScreen(new GuiMultiplayer(this));
						break;
						
					case "AltManager":
						this.mc.displayGuiScreen(new GuiAltManager());
						break;
						
					case "Settings":
						mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
						break;	
						
					case "Language":
						mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
						break;	
						
					case "ServerBotter":
						this.mc.displayGuiScreen(new MintAttackGUI());
						break;	
				}
			}

			count+=20;
		}
		
	}
	
	public void onGuiClosed() {
		
	}

}
