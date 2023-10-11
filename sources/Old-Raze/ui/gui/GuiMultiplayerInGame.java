package markgg.ui.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;

public class GuiMultiplayerInGame extends GuiMultiplayer{

	public GuiMultiplayerInGame() {
		super(null);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException{
		if(button.id == 1 || button.id == 4) {
			disconnect();
		}
		
		super.actionPerformed(button);
	}
	
	@Override
	public void connectToSelected() {
		disconnect();
		super.connectToSelected();
	}

	private void disconnect() {
		if(this.mc.theWorld != null) {
			
			this.mc.theWorld.sendQuittingDisconnectingPacket();
			this.mc.loadWorld(null);
			this.mc.displayGuiScreen(null);
			this.parentScreen = null;
			
		}
	}

}
