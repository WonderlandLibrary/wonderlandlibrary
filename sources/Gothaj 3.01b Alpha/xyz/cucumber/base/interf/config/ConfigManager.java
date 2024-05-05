package xyz.cucumber.base.interf.config;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.interf.config.ext.ConfigClose;
import xyz.cucumber.base.interf.config.ext.ConfigNavButton;
import xyz.cucumber.base.interf.config.ext.ConfigPanel;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class ConfigManager {
	
	private PositionUtils position, configs;
	private PositionUtils openButton;
	
	public boolean open;
	
	private ArrayList<ConfigNavButton> buttons = new ArrayList();
	
	private ConfigClose closeButton;
	
	public ConfigNavButton active;
	
	public GuiTextField search;
	
	
	
	public ConfigManager() {
		buttons.clear();
		this.position = new PositionUtils(0,0,400,300,1);
		this.configs = new PositionUtils(2,50,396,248,1);
		this.openButton = new PositionUtils(0,00,25,30,1);
		
		this.active = new ConfigNavButton(this,0, "Local", 2.5, 15);
		buttons.add(active);
		buttons.add(new ConfigNavButton(this,1, "Public", 2.5, 15));
		buttons.add(new ConfigNavButton(this,2, "Cloud", 2.5, 15));
		closeButton = new ConfigClose(0, 300-20, 5, 15, 15);
		this.search = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, (int) 0, (int)  0, 140, 15);
	}
	public void initGui() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		this.position.setX(sr.getScaledWidth_double());
		this.position.setY(sr.getScaledHeight()/2-this.position.getHeight()/2);
	}
	
	public void draw(int mouseX, int mouseY) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		if(open) {
			this.position.setX((this.position.getX() * 20 + (sr.getScaledWidth_double()/2-this.position.getWidth()/2)) /21);
		}else {
			this.position.setX((this.position.getX() * 20 + (sr.getScaledWidth_double())) /21);
		}
		
		this.position.setY(sr.getScaledHeight()/2-this.position.getHeight()/2);
		this.configs.setX(this.position.getX()+2);
		this.configs.setY(this.position.getY()+50);
		this.openButton.setY(this.position.getY() + position.getHeight()/2- this.openButton.getHeight()/2);
		this.openButton.setX(this.position.getX() -this.openButton.getWidth());
        
		RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), 0xff202020, 3f);
		RenderUtils.drawRoundedRect(this.configs.getX(), this.configs.getY(), this.configs.getX2(), this.configs.getY2(), 0xff161616, 3f);
		
		RenderUtils.drawRoundedRectWithCorners(this.openButton.getX(), this.openButton.getY(), this.openButton.getX2()+1, this.openButton.getY2(), 0xff202020, 3f,true, false, true, false);
		
		RenderUtils.drawImage(this.position.getX()+2, this.position.getY()+2, 18, 18, new ResourceLocation("client/images/gothaj.png"), 0xffffffff);
		
		int i = 0;
		RenderUtils.drawRoundedRect(this.position.getX()+this.position.getWidth()/2-75, this.position.getY()+35, this.position.getX()+this.position.getWidth()/2+75, this.position.getY()+48, 0xff181818, 3f);
		for(ConfigNavButton button : buttons) {
			button.getPosition().setX(this.position.getX()+this.position.getWidth()/2-75+50*i);
			button.getPosition().setY(this.position.getY()+35);
			button.draw(mouseX, mouseY);
			i++;
		}
		this.search.xPosition = (int) (this.position.getX()+this.position.getWidth()/2-70);
		this.search.yPosition = (int) (this.position.getY()+4);
		
		renderTextField(search);
		
		int x = 0;
		int y = 0;
		for(ConfigPanel panel : active.configs) {
			if(!panel.getFile().getName().substring(0, panel.getFile().getName().lastIndexOf(".json")).toLowerCase().contains(search.getText().toLowerCase())) continue;
			panel.getPosition().setX(this.configs.getX()+2.5+131*x);
			panel.getPosition().setY(this.configs.getY()+2+41*y);
			panel.draw(mouseX, mouseY);
			x++;
			if(x == 3) {
				x = 0;
				y++;
			}
		}
		closeButton.getPosition().setX(this.position.getX()+this.position.getWidth()-20);
		closeButton.getPosition().setY(this.position.getY()+5);
		closeButton.draw(mouseX, mouseY);
	}
	
	public void onClick(int mouseX, int mouseY, int b) {
		if(openButton.isInside(mouseX, mouseY) && b == 0) {
			this.open = !open;
		}
		
		if(!open) return;
		for(ConfigNavButton button : buttons) {
			button.onClick(mouseX, mouseY, b);
		}
		if(this.configs.isInside(mouseX, mouseY)) {
			for(ConfigPanel panel : active.configs) {
				if(panel.getFile().getName().substring(0, panel.getFile().getName().lastIndexOf(".json")).toLowerCase().contains(search.getText().toLowerCase()))
					panel.onClick(mouseX, mouseY, b);
			}
		}
		if(closeButton.getPosition().isInside(mouseX, mouseY) && b == 0) this.open = false;
		this.search.mouseClicked(mouseX, mouseY, b);
	}

	public void onRelease(int mouseX, int mouseY, int b) {
		if(!open) return;
	}
	public void renderTextField(GuiTextField field) {
		field.setMaxStringLength(25);
		RenderUtils.drawRoundedRect(field.xPosition, field.yPosition, field.xPosition+field.width, field.yPosition+field.height, 0x95333333, 3);
		if(field.isFocused()) {
			
			Fonts.getFont("rb-r").drawString("|", field.xPosition+4+spacing(field), field.yPosition+6, -1);
				
		}else {
			
		}
		if(field.getText().equals("")) {
			Fonts.getFont("rb-r").drawString("Search...", field.xPosition+4,  field.yPosition+6,0xffaaaaaa);
		}else {
			int color = 0xffaaaaaa;
			if(field.isFocused()) {
				color = -1;
			}
			
			Fonts.getFont("rb-r").drawString(field.getText(), field.xPosition+4,  field.yPosition+6, color);
		}
	}
	private double spacing(GuiTextField field) {
		String[] text = field.getText().split("");
		
		double d = 0;
		int i = 0;
		for(String t : text) {
			if(field.getCursorPosition() == i) {
				return d;
			}
			i++;
			d+= Fonts.getFont("rb-r").getWidth(t);
		}
		return d;
	}
	public void onKey(int keycode, char chr) {
		if(!open) return;
		this.search.textboxKeyTyped(chr,keycode);
	}
}
