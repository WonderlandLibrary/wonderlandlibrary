package slavikcodd3r.rainbow.module.modules.render;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import slavikcodd3r.rainbow.Rainbow;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.utils.ClientUtils;
@Mod(displayName = "ClickGUI")
public class ClickGUI extends Module
{  
	@Override
    public void enable() {
		ClientUtils.sendMessage("No gui lol :/");
	}
}

