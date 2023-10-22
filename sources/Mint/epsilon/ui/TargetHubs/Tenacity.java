package epsilon.ui.TargetHubs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import epsilon.modules.render.TargetHUD;
import epsilon.ui.HUD;
import epsilon.util.ColorUtil;
import epsilon.util.ESP.RenderUtils;
import epsilon.util.Font.Fonts;
import epsilon.util.Font.MCFontRenderer;
public class Tenacity {
	public static void draw() {
		Minecraft mc = Minecraft.getMinecraft();
		float show_hp_pos = TargetHUD.show_hp_pos;
		float smoothhealth = TargetHUD.smoothhealth;
		float x = TargetHUD.x;
		float y = TargetHUD.y;
		EntityLivingBase target = TargetHUD.target;
		int rainbow = (ColorUtil.getAstolfoRainbow(12, 0.4f, 1, 1));

		if(Double.isNaN(show_hp_pos)) show_hp_pos = 0; 
		if(show_hp_pos > target.getMaxHealth()) show_hp_pos = target.getMaxHealth();
		if(show_hp_pos < 0) smoothhealth = 0;
		show_hp_pos += (target.getHealth()-show_hp_pos)/10;

		if(Double.isNaN(smoothhealth)) smoothhealth = 0; 
		if(smoothhealth < 0) smoothhealth = 0;
		smoothhealth += (target.getHealth()-smoothhealth)/50;
		if(smoothhealth > target.getMaxHealth()) smoothhealth = Math.round(target.getMaxHealth()*10)/10;
		String name = target.getName();

		RenderUtils.drawRoundedRect(x, y, x+150, y+50, 6, 0x50000000);

		RenderUtils.drawRoundedRect(x+140, y+35, x+50, y+31, 3, 0x70000000);
		RenderUtils.drawRoundedRect(x+50+(90/target.getMaxHealth())*smoothhealth, y+35, x+50, y+31, 3, rainbow);

        if(target instanceof EntityPlayer) {
    		mc.getTextureManager().bindTexture(((AbstractClientPlayer) target).getLocationSkin());
    		GL11.glEnable(GL11.GL_BLEND); 
    		GL11.glColor4f(1, 1, 1, 1);
    		Gui.drawScaledCustomSizeModalRect(x+8, y+8, 8, 8, 8, 8, 34, 34, 64, 64);
    		GL11.glDisable(GL11.GL_BLEND);
    		int hurtcolour = 0x00000000;
    		switch(target.hurtTime) {
    			case 9:
    				hurtcolour = 0x40FF0000;
    				break;

    			case 8:
    				hurtcolour = 0x60FF0000;
    				break;

    			case 7:
    				hurtcolour = 0x90FF0000;
    				break;

    			case 6:
    				hurtcolour = 0x7AFF0000;
    				break;

    			case 5:
    				hurtcolour = 0x60FF0000;
    				break;

    			case 4:
    				hurtcolour = 0x45FF0000;
    				break;

    			case 3:
    				hurtcolour = 0x30FF0000;
    				break;

    			case 2:
    				hurtcolour = 0x20FF0000;
    				break;

    			case 1:
    				hurtcolour = 0x10FF0000;
    				break;

    		}
    		Gui.drawRect(x+8, y+8, x+42, y+42, hurtcolour);
        }

		MCFontRenderer cfr = Fonts.Segation18;
		String string = Math.round(smoothhealth/target.getMaxHealth()*10000)/100+"%";
		float namex = x+50+(90/target.getMaxHealth())*smoothhealth-cfr.getStringWidth(string);
		if(namex-x < 50) namex = x+50;
		cfr.drawString(string, namex, y+23, -1);
		cfr.drawString("Distance: " + Math.round(mc.thePlayer.getDistanceToEntity(target)*10F)/10F, x+50, y+35, -1);
		cfr.drawString("Hurt: " + target.hurtTime, x+110, y+35, -1);
		cfr = Fonts.Segation18;
		cfr.drawString(target.getName(), x+50, y+8, -1);
		TargetHUD.pwidth = 150;
		TargetHUD.pheight = 50;

		TargetHUD.show_hp_pos =  show_hp_pos;
		TargetHUD.smoothhealth = smoothhealth;
		TargetHUD.x = x;
		TargetHUD.y = y;
	}
}