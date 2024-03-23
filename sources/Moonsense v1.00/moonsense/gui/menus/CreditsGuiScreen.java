package moonsense.gui.menus;

import java.io.IOException;

import moonsense.cosmetic.CosmeticBoolean;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class CreditsGuiScreen extends GuiScreen {
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		mc.getTextureManager().bindTexture(new ResourceLocation("moonsense/images/bg.png"));
		this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		
		this.buttonList.add(new GuiButton(100, this.width / 2 - 100, this.height / 2 + 150, "Back"));
		
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button.id == 100) {
			mc.displayGuiScreen(new MoonsenseMainMenu());
		}
		super.actionPerformed(button);
	}
	
}
