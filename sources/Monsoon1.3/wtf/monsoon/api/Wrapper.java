package wtf.monsoon.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public interface Wrapper {

	Minecraft mc = Minecraft.getMinecraft();
	FontRenderer fr = mc.fontRendererObj;
}
