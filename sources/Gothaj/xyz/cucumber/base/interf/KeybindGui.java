package xyz.cucumber.base.interf;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.module.Mod;
import xyz.cucumber.base.utils.RenderUtils;
import xyz.cucumber.base.utils.render.Fonts;

public class KeybindGui extends GuiScreen{
	
	private Mod mod;
	
	public KeybindGui(Mod mod) {
		this.mod = mod;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		RenderUtils.drawRect(0, 0, this.width, this.height, 0x90000000);
		Fonts.getFont("rb-m-13").drawString(mod.getName(), this.width/2-Fonts.getFont("rb-m-13").getWidth(mod.getName())/2, this.height/2-10, 0xff4269f5);
		Fonts.getFont("rb-r").drawString("Press any key on keyboard (ESCAPE FOR UNBIND)", this.width/2-Fonts.getFont("rb-r").getWidth("Press any key on keyboard (ESCAPE FOR UNBIND)")/2, this.height/2, -1);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if(keyCode == Keyboard.KEY_ESCAPE) {
			mod.setKey(0);
			Client.INSTANCE.getCommandManager().sendChatMessage("§c" + mod.getName() + "§7 was unbound!");
		}else {
			mod.setKey(keyCode);
			Client.INSTANCE.getCommandManager().sendChatMessage("§a" + mod.getName() + "§7 was successfully bound to " + keyCode);
		}
		
		Minecraft.getMinecraft().thePlayer.closeScreen();
		return;
	}

}
