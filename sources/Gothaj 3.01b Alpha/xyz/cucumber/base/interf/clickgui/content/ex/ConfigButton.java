package xyz.cucumber.base.interf.clickgui.content.ex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.utils.FileUtils;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.cfgs.ConfigFileUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class ConfigButton {

	private File file;
	
	private PositionUtils position, load, save, remove;
	
	public ConfigButton(File file, PositionUtils position) {
		this.file = file;
		this.position = position;
		
		this.load = new PositionUtils(0,0,10,10,1);
		this.save = new PositionUtils(0,0,10,10,1);
		this.remove = new PositionUtils(0,0,10,10,1);
	}

	public void draw(int mouseX, int mouseY){
		RenderUtils.drawRoundedRect(position.getX(), position.getY(), position.getX2(), position.getY2(), 0xff181818, 0.5F);
		
		String s = file.getName().substring(0, file.getName().lastIndexOf("."));
		Fonts.getFont("volte").drawString(s, position.getX()+3, position.getY()+3, -1);
		
		Fonts.getFont("rb-m-13").drawString("Status: ", position.getX()+9, position.getY()+10, -1);
		Fonts.getFont("rb-r-13").drawString("Private", position.getX()+9+Fonts.getFont("rb-r-13").getWidth("Status: "), position.getY()+10, -1);
		
		try {
			BufferedReader load = new BufferedReader(new FileReader(file));
			JsonObject json = (JsonObject) FileUtils.jsonParser.parse(load);
			
			Fonts.getFont("rb-m-13").drawString("Edit:", position.getX()+9, position.getY()+18, -1);
			Fonts.getFont("rb-r-13").drawString(json.get("last-update").getAsString(), position.getX()+9+Fonts.getFont("rb-r-13").getWidth("Edit:"), position.getY()+18, -1);
			
			
		}catch(Exception e) {
		}
		
		this.load.setX(position.getX()+position.getWidth()/2-17.5);
		this.load.setY(position.getY2()-12);
		
		this.save.setX(position.getX()+position.getWidth()/2-5);
		this.save.setY(position.getY2()-12);
		
		this.remove.setX(position.getX()+position.getWidth()/2+7.5);
		this.remove.setY(position.getY2()-12);
		
		RenderUtils.drawRoundedRect(this.load.getX(), this.load.getY(), this.load.getX2(), this.load.getY2(), 0xff45ff93, 0.25f);
		RenderUtils.drawImage(this.load.getX()+2.5, this.load.getY()+2, this.load.getWidth()-4, this.load.getHeight()-4, new ResourceLocation("client/images/load.png"), 0xff000000);
		
		RenderUtils.drawRoundedRect(this.save.getX(), this.save.getY(), this.save.getX2(), this.save.getY2(), 0xff45d1ff, 0.25f);
		
		RenderUtils.drawImage(this.save.getX()+2.5, this.save.getY()+2, this.save.getWidth()-4, this.save.getHeight()-4, new ResourceLocation("client/images/save.png"), 0xff000000);
		
		RenderUtils.drawRoundedRect(this.remove.getX(), this.remove.getY(), this.remove.getX2(), this.remove.getY2(), 0xffff454b, 0.25f);
		RenderUtils.drawImage(this.remove.getX()+2.5, this.remove.getY()+2, this.remove.getWidth()-4, this.remove.getHeight()-4, new ResourceLocation("client/images/remove.png"), 0xff000000);
		
	}
	
	public void onClick(int mouseX, int mouseY, int button){
		if(position.isInside(mouseX, mouseY) && button == 0) {
			if(load.isInside(mouseX, mouseY)) {
				ConfigFileUtils.load(this.file, true, false);
			}
			
			if(save.isInside(mouseX, mouseY)) {
				ConfigFileUtils.save(this.file, true);
			}
			
			if(remove.isInside(mouseX, mouseY)) {
				file.delete();
			}
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public PositionUtils getPosition() {
		return position;
	}

	public void setPosition(PositionUtils position) {
		this.position = position;
	}
	
	
}
