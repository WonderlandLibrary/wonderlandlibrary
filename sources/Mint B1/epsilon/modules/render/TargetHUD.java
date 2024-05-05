package epsilon.modules.render;

import java.awt.Color;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import epsilon.events.Event;
import epsilon.modules.Module.Category;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.ui.TargetHubs.Tenacity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class TargetHUD extends Module{
	ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	public static EntityLivingBase target;

	static double width = 10000;
	static double height = 10000;
	public static float initialx, initialy;
	public static boolean initialize = true;
	public float ExtendedLength;	
	public static float smoothhealth;
	public static float smoothhealth2;
    public static float show_hp_pos = 0;
    public static double show_hp_pos_smooth = 0;
	public static double pwidth = 0;
	public static double pheight = 0;
    
	public TargetHUD(){
		super("TargetHUD", Keyboard.KEY_NONE, Category.RENDER, "TargetHUD what else bozo");
		this.addSettings(xsetting,ysetting,mode);
	}

	public static NumberSetting xsetting = new NumberSetting("X", -500, 0, 500, 1);
	public static NumberSetting ysetting = new NumberSetting("Y", -500, 0, 500, 1);
	public static Float x = (float) xsetting.getValue();
	public static Float y = (float) ysetting.getValue();
	public static ModeSetting mode = new ModeSetting("TargetHUD","Astolfo","Novoline","Flux","Astolfo","Monsoon","Smoke","Exhibition","Health","Name","SIGMER","Kiwi","Tenacity");

	public void onEvent(Event e) {
	}
	
	public static void draw(EntityLivingBase target, float yoffset) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

		width = sr.getScaledWidth();
		height = sr.getScaledHeight();
		x = (float) (xsetting.getValue() + width/2);
		y = (float) (ysetting.getValue() + height/2 + yoffset);
		
		TargetHUD.target = target;
		switch(mode.getMode()) {
			
		case "Tenacity":
			Tenacity.draw();
			break;
		}
	}
}



