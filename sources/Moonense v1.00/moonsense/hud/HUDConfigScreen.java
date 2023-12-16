package moonsense.hud;

import java.io.IOException;

import moonsense.Client;
import moonsense.clickgui.ClickGUI;
import moonsense.gui.menus.CosmeticGuiInGame;
import moonsense.hud.mod.HudMod;
import moonsense.utils.notification.Notification;
import moonsense.utils.notification.NotificationManager;
import moonsense.utils.notification.NotificationType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class HUDConfigScreen extends GuiScreen {
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(845, this.width / 2 + 10, this.height / 2 - 10, 100, 20, "ClickGUI"));
		this.buttonList.add(new GuiButton(42069, this.width / 2 - 110, this.height / 2 - 10, 100, 20, "Cosmetics"));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		for(HudMod m : Client.INSTANCE.hudManager.hudMods) {
			if(m.isEnabled()) {
				m.renderDummy(mouseX, mouseY);
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		switch(button.id) {
		case 845:
			mc.displayGuiScreen(new ClickGUI());
		}
		switch(button.id) {
		case 42069:
			mc.displayGuiScreen(new CosmeticGuiInGame());
		}
	}
	
}
