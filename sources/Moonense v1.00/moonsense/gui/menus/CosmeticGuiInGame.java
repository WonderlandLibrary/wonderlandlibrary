package moonsense.gui.menus;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import moonsense.Client;
import moonsense.alt.GuiAltManager;
import moonsense.cosmetic.CosmeticBoolean;
import moonsense.hud.HUDConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class CosmeticGuiInGame extends GuiScreen {
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		
		this.buttonList.add(new GuiButton(100, this.width / 2 - 100, this.height / 2 + 150, "Back"));
		
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 60, "Default Cape"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 - 30, "Snowy Cape"));
		
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 1) {
			CosmeticBoolean.cape1 = true;
			CosmeticBoolean.snowCape = false;
		}
		if(button.id == 2) {
			CosmeticBoolean.cape1 = false;
			CosmeticBoolean.snowCape = true;
		}
		if(button.id == 100) {
			mc.displayGuiScreen(new HUDConfigScreen());
		}
		super.actionPerformed(button);
	}
	
}
