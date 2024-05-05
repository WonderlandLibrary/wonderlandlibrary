package xyz.cucumber.base.interf.config.ext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;
import xyz.cucumber.base.utils.FileUtils;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.cfgs.ConfigFileUtils;
import xyz.cucumber.base.utils.position.PositionUtils;
import xyz.cucumber.base.utils.render.ColorUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class ConfigPanel {

	private String user;
	
	private String time, desc;
	
	private File file;
	
	private PositionUtils position, informations, save, load;
	
	private int animation;

	public ConfigPanel(File f, String user, String time,String desc) {
		file = f;
		this.position = new PositionUtils(0,0,129,40,1);
		this.save = new PositionUtils(0,0,8,8,1);
		this.load = new PositionUtils(0,0,8,8,1);
		this.informations = new PositionUtils(0,0,Fonts.getFont("rb-r-13").getWidth("Hover to view information"),10,1);
		this.user = user;
		this.time = time;
		this.desc = desc;
	}
	
	public void draw(int mouseX, int mouseY) {
		if(position.isInside(mouseX, mouseY)) {
			animation = (animation * 14 + 80 )/15;
		}else {
			animation = (animation * 14 + 40 )/15;
		}
		
		RenderUtils.drawRoundedRect(this.position.getX(), this.position.getY(), this.position.getX2(), this.position.getY2(), ColorUtils.getAlphaColor(0x350b0e12, animation), 3);
		Fonts.getFont("mitr").drawString(file.getName().substring(0, file.getName().lastIndexOf(".json")).toUpperCase(), this.position.getX()+5, this.position.getY()+4, 0xffcccccc);
		Fonts.getFont("rb-m-13").drawString("@"+user, this.position.getX()+5, this.position.getY()+14, 0xff3b81e3);
		
		Fonts.getFont("rb-r-13").drawString(time, this.position.getX()+5+Fonts.getFont("rb-m-13").getWidth("@"+user)+5, this.position.getY()+14, 0xffaaaaaa);
		
		informations.setX(this.position.getX()+5);
		informations.setY(this.position.getY2()-10);
		Fonts.getFont("rb-r-13").drawString("Hover to view information", this.position.getX()+5, this.position.getY2()-8, 0xff777777);
		
		this.save.setX(this.position.getX()+10+Fonts.getFont("rb-r-13").getWidth("Hover to view information")+2);
		this.save.setY(this.position.getY2()-15);
		this.load.setX(this.position.getX()+10+Fonts.getFont("rb-r-13").getWidth("Hover to view information")+17);
		this.load.setY(this.position.getY2()-15);
		RenderUtils.drawImage(this.load.getX(), this.load.getY(), this.load.getWidth(), this.load.getHeight(), new ResourceLocation("client/images/load.png"), 0xffffffff);
		RenderUtils.drawImage(this.save.getX(), this.save.getY(), this.save.getWidth(), this.save.getHeight(), new ResourceLocation("client/images/save.png"), 0xffffffff);
		if(informations.isInside(mouseX, mouseY) && desc != "") {
			RenderUtils.drawRoundedRect(this.informations.getX()+this.informations.getWidth()/2-50, this.informations.getY()-70, this.informations.getX()+this.informations.getWidth()/2+50, this.informations.getY(), 0xff101010,2);
			RenderUtils.drawOutlinedRoundedRect(this.informations.getX()+this.informations.getWidth()/2-50, this.informations.getY()-70, this.informations.getX()+this.informations.getWidth()/2+50, this.informations.getY(), 0x20777777,2,1);
			String[] descSplitted = desc.split(" ");
			String finalStr = "";
			int y = 0;
			for(int i = 0; i < descSplitted.length; i++) {
				if(Fonts.getFont("rb-r-13").getWidth(finalStr)+Fonts.getFont("rb-r-13").getWidth(descSplitted[i]) < 94) {
					finalStr += (i == 0 ? "" : " ") +descSplitted[i];
				}else if(Fonts.getFont("rb-r-13").getWidth(descSplitted[i]) > 94) {
					String[] sp =descSplitted[0].split("");
					for(String s : sp) {
						if(Fonts.getFont("rb-r-13").getWidth(finalStr)+Fonts.getFont("rb-r-13").getWidth(s) < 94) {
							finalStr+=s;
						}else {
							Fonts.getFont("rb-r-13").drawString(finalStr, this.informations.getX()+this.informations.getWidth()/2-47,  this.informations.getY()-65+10*y, 0xff777777);
							finalStr = s;
							y++;
						}
					}
				}else {
					Fonts.getFont("rb-r-13").drawString(finalStr, this.informations.getX()+this.informations.getWidth()/2-47,  this.informations.getY()-65+10*y, 0xff777777);
					y++;
					finalStr = descSplitted[i];
				}
			}
			if(!finalStr.equals(""))Fonts.getFont("rb-r-13").drawString(finalStr, this.informations.getX()+this.informations.getWidth()/2-47,  this.informations.getY()-65+10*y, 0xff777777);
		}
	}
	
	public void onClick(int mouseX, int mouseY, int button) {
		if(save.isInside(mouseX, mouseY) && button == 0) {
			ConfigFileUtils.save(file, true);
		}
		if(load.isInside(mouseX, mouseY) && button == 0) {
			ConfigFileUtils.load(file, true, false);
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
