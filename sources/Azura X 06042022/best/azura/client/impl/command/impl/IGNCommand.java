package best.azura.client.impl.command.impl;

import best.azura.client.api.command.ICommand;
import best.azura.client.util.other.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class IGNCommand implements ICommand {
	@Override
	public String getName() {
		return "ign";
	}
	
	@Override
	public String getDescription() {
		return "Get the username";
	}
	
	@Override
	public String[] getAliases() {
		return new String[]{"username", "name"};
	}
	
	@Override
	public void handleCommand(String[] args) {
		GuiScreen.setClipboardString(Minecraft.getMinecraft().getSession().getUsername());
		ChatUtil.sendChat("Copied username to clipboard");
	}
}
