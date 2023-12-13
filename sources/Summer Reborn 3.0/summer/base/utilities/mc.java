package summer.base.utilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public class mc {

	public static EntityPlayerSP thePlayer;
	public static WorldClient theWorld;
	public static PlayerControllerMP playerController;
	public static GameSettings gameSettings;
	public static Session session;
	public static FontRenderer fontRendererObj;
	public static GuiScreen currentScreen;
	public static Timer timer;
	public RenderManager renderManager;

	public static void updateInfo() {
		thePlayer = Minecraft.getMinecraft().thePlayer;
		theWorld = Minecraft.getMinecraft().theWorld;
		playerController = Minecraft.getMinecraft().playerController;
		gameSettings = Minecraft.getMinecraft().gameSettings;
		session = Minecraft.getMinecraft().session;
		fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
		currentScreen = Minecraft.getMinecraft().currentScreen;
		timer = Minecraft.getMinecraft().timer;
	}

	public static ItemRenderer getItemRenderer() {
		return Minecraft.getMinecraft().getItemRenderer();
	}

	public static NetHandlerPlayClient getNetHandler() {
		return Minecraft.getMinecraft().getNetHandler();
	}

	public static void displayGuiScreen(GuiScreen screen) {
		Minecraft.getMinecraft().displayGuiScreen(screen);
	}

	public static ServerData getCurrentServerData() {
		return Minecraft.getMinecraft().getCurrentServerData();
	}

	public RenderManager getRenderManager() {
		return this.renderManager;
	}
}
