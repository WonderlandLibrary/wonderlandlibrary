package moonsense.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import moonsense.Client;
import moonsense.clickgui.comp.ModButton;
import moonsense.clickgui.comp.ModButton1;
import moonsense.hud.mod.HudMod;
import moonsense.hud.HUDConfigScreen;
import moonsense.utils.RoundedUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ClickGUI extends GuiScreen {
	public HudMod mod;
	
	ArrayList<ModButton> modButtons = new ArrayList<>();
	ArrayList<ModButton1> modButtons1 = new ArrayList<>();
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		switch(button.id) {
		case 348:
			mc.displayGuiScreen(new HUDConfigScreen());
		}
			
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(348, this.width / 2 - 50, this.height / 2 + 180, 100, 20, "Edit Mod Positions"));
		this.modButtons.add(new ModButton(215, 64, 44, 11, Client.INSTANCE.hudManager.testMod));
		this.modButtons.add(new ModButton(266, 64, 43, 11, Client.INSTANCE.hudManager.fps));
		this.modButtons.add(new ModButton(316, 64, 55, 11, Client.INSTANCE.hudManager.targetHud));
		this.modButtons.add(new ModButton(377, 64, 64, 11, Client.INSTANCE.hudManager.coords));
		this.modButtons.add(new ModButton(447, 64, 65, 11, Client.INSTANCE.hudManager.ping));
		this.modButtons.add(new ModButton(519, 64, 65, 11, Client.INSTANCE.hudManager.armorStatus));
		this.modButtons1.add(new ModButton1(215, 64, 51, 11, Client.INSTANCE.hudManager.fullBright));
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		RoundedUtils.drawSmoothRoundedRect(200, 50, sr.getScaledWidth() - 200, sr.getScaledHeight() - 50, 10, new Color(0, 0, 0, 170).getRGB());
		
		this.drawString(fontRendererObj, "Moonsense Client", 600, 500, -1);
		
		for(ModButton m : modButtons) {
			m.draw();
		}
		for(ModButton1 m1 : modButtons1) {
			m1.draw();
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(ModButton m : modButtons) {
			m.onClick(mouseX, mouseY, mouseButton);
		}
		for(ModButton1 m1 : modButtons1) {
			m1.onClick(mouseX, mouseY, mouseButton);
		}
	}

}
