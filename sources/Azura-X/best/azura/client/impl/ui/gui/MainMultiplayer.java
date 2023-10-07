package best.azura.client.impl.ui.gui;

import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.render.BlurModule;
import best.azura.client.impl.rpc.DiscordRPCImpl;
import best.azura.client.impl.ui.font.Fonts;
import best.azura.client.impl.ui.gui.impl.Window;
import best.azura.client.impl.ui.gui.impl.buttons.ButtonImpl;
import best.azura.client.impl.ui.gui.impl.buttons.TextButton;
import best.azura.client.util.render.RenderUtil;
import best.azura.client.util.render.StencilUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import viamcp.ViaMCP;
import viamcp.protocols.ProtocolCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import static org.lwjgl.opengl.GL11.*;

public class MainMultiplayer extends GuiScreen {

	private static final ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
	private final OldServerPinger oldServerPinger = new OldServerPinger();

	private static final Logger logger = LogManager.getLogger();

	private final ArrayList<ButtonImpl> buttons = new ArrayList<>();
	private final GuiScreen parent;
	private ButtonImpl selected;
	private double animation = 0;
	private long start = 0;
	private GuiScreen toShow;
	private best.azura.client.impl.ui.gui.impl.Window currentWindow = null;
	private ScrollRegion region;

	public MainMultiplayer(GuiScreen parent) {
		this.parent = parent;
		this.selected = null;
	}

	@Override
	public void initGui() {
		mc.timer.timerSpeed = 1.0f;
		DiscordRPCImpl.updateNewPresence(
				"Multiplayer",
				"Selecting what server to play on"
		);
		this.region = new ScrollRegion(mc.displayWidth / 2 - 500, mc.displayHeight / 2 - 400, 1000, 720);
		start = System.currentTimeMillis();
		animation = 0;

		buttons.clear();
		int count = 0;

		for (ServerData data : loadServerList()) {
			ButtonImpl b;
			buttons.add(b = new ButtonImpl("", mc.displayWidth / 2 - 470, mc.displayHeight / 2 - 380 + count * 78, 940, 70, 5));
			b.serverData = data;
			count++;
		}
		String[] strings = new String[]{"Join", "Add", "Remove", "Change version", "Direct connect", "Edit", "Back"};
		int calcWidth = 0;
		for (String s : strings) {
			int width = Fonts.INSTANCE.arial20.getStringWidth(s);
			buttons.add(new ButtonImpl(s, mc.displayWidth / 2 - 470 + calcWidth, mc.displayHeight / 2 + 350, width + 40, 40, 3));
			calcWidth += width + 40 + 5;
		}
		pingServers();

	}

	public void pingServers() {
		for (ButtonImpl button : buttons) {
			if (button.serverData != null) {
				executor.submit(() -> {
					try {
						oldServerPinger.ping(button.serverData);
					} catch (UnknownHostException var2) {
						button.serverData.pingToServer = -1L;
						button.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Can't resolve hostname";
					} catch (Exception var3) {
						button.serverData.pingToServer = -1L;
						button.serverData.serverMOTD = EnumChatFormatting.DARK_RED + "Can't connect to server.";
					}
				});
			}
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		oldServerPinger.clearPendingNetworks();
	}

	@Override
	public void onTick() {
		region.onTick();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		if (toShow != null) {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			animation = -1 * Math.pow(anim - 1, 6) + 1;
			animation = 1 - animation;
		} else {
			float anim = Math.min(1, (System.currentTimeMillis() - start) / 500f);
			animation = -1 * Math.pow(anim - 1, 6) + 1;
		}

		if (toShow != null && animation == 0) {
			mc.displayGuiScreen(toShow);
			return;
		}

		GlStateManager.pushMatrix();
		glEnable(GL_BLEND);
		drawDefaultBackground();

		RenderUtil.INSTANCE.scaleFix(1.0);
		final ScaledResolution sr = new ScaledResolution(mc);
		final boolean blur = Client.INSTANCE.getModuleManager().getModule(BlurModule.class).isEnabled() && BlurModule.blurMenu.getObject();
		if (blur) {
			GL11.glPushMatrix();
			RenderUtil.INSTANCE.invertScaleFix(1.0);
			StencilUtil.initStencilToWrite();
			GlStateManager.scale(1.0 / sr.getScaleFactor(), 1.0 / sr.getScaleFactor(), 1);
		}
		RenderUtil.INSTANCE.drawRoundedRect(mc.displayWidth / 2 - 500, mc.displayHeight / 2 - 400, 1000, 800, 10, new Color(0, 0, 0, 170));
		if (blur) {
			GlStateManager.scale(sr.getScaleFactor(), sr.getScaleFactor(), 1);
			StencilUtil.readStencilBuffer(1);
			BlurModule.blurShader.blur();
			StencilUtil.uninitStencilBuffer();
			RenderUtil.INSTANCE.scaleFix(1.0);
			GL11.glPopMatrix();
		}

		region.prepare();
		int count = 0;
		int calcHeight = 0;
		region.render(mouseX, mouseY);
		for (ButtonImpl button : buttons) {
			if (button.serverData != null) {
				button.y = mc.displayHeight / 2 - 380 + count * 78 + region.mouse;
				button.width = 940;
				button.x = mc.displayWidth / 2 - 470;
				button.height = 70;
				button.roundness = 5;
				button.animation = animation;
				button.draw(currentWindow != null ? 0 : mouseX, currentWindow != null ? 0 : mouseY);
				button.selected = selected == button;
				count++;
				calcHeight += 78;
			}

		}

		if (calcHeight > region.height) {
			region.offset = region.height - calcHeight - 20;
		}

		region.end();

		for (ButtonImpl button : buttons) {
			if (button.serverData != null) continue;
			button.animation = animation;
			button.selected = selected == button;
			button.draw(mouseX, mouseY);
		}

		final ProtocolVersion version = ProtocolCollection.getProtocolById(ViaMCP.getInstance().getVersion());
		if (version != null)
			Fonts.INSTANCE.arial15.drawStringWithShadow("Selected version: " + version.getName(), 3, 3, -1);

		if (currentWindow != null && currentWindow.hidden && currentWindow.animation == 0) {
			currentWindow = null;
		}

		if (currentWindow != null) {
			RenderUtil.INSTANCE.drawRect(0, 0, mc.displayWidth, mc.displayHeight, new Color(0, 0, 0, (int) (100 * currentWindow.animation)));
			currentWindow.draw(mouseX, mouseY);
		}

		glDisable(GL_BLEND);
		GlStateManager.popMatrix();

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

		if (currentWindow != null) {
			best.azura.client.api.ui.buttons.Button button = currentWindow.clickedButton(mouseX, mouseY);
			if (button == null) return;
			if (button instanceof ButtonImpl) {
				ButtonImpl button1 = (ButtonImpl) button;
				switch (button1.text) {
					case "Add":
						currentWindow.hide();
						ButtonImpl b2;
						buttons.add(b2 = new ButtonImpl("d", 0, 0, 0, 0, 5));
						String name = null, ip = null;
						for (best.azura.client.api.ui.buttons.Button b3 : currentWindow.buttons) {
							if (b3 instanceof TextButton && name == null) {
								name = ((TextButton) b3).fontText;
							} else if (b3 instanceof TextButton) ip = ((TextButton) b3).fontText;
						}
						b2.serverData = new ServerData(name, ip, false);
						pingServers();
						saveServerList();
						break;
					case "Connect":
						currentWindow.hide();
						ServerData serverData = null;

						serverData = new ServerData("Direct", ((TextButton) currentWindow.buttons.stream().filter(button2 -> button2 instanceof TextButton && ((TextButton)button2).text.equalsIgnoreCase("Server address")).findAny().get()).fontText, false);

						if (serverData == null) return;

						mc.displayGuiScreen(new GuiConnecting(this, mc, serverData));
						break;
					case "Remove":
						currentWindow.hide();
						buttons.remove(selected);
						selected = null;
						saveServerList();
						break;
					case "Edit":
						currentWindow.hide();

						if (selected == null) return;

						String serverName = null, serverIP = null;
						for (best.azura.client.api.ui.buttons.Button b3 : currentWindow.buttons) {
							if (b3 instanceof TextButton && serverName == null) {
								serverName = ((TextButton) b3).fontText;
							} else if (b3 instanceof TextButton) serverIP = ((TextButton) b3).fontText;
						}

						selected.serverData = new ServerData(serverName, serverIP, false);
						pingServers();
						saveServerList();
						break;
					case "Cancel":
						currentWindow.hide();
						break;
				}

				if (currentWindow.text.equals("Change version"))
					Arrays.stream(ProtocolCollection.values()).filter(v -> v.getVersion().getName().equals(button1.text)).findFirst().ifPresent(version -> ViaMCP.getInstance().setVersion(version.getVersion().getVersion()));
				return;
			}
		}

		boolean multiplayer = false;
		String clickedButton = "";
		for (ButtonImpl button : buttons) {
			if (button.serverData != null && button.hovered && region.isHovered()) {
				if (button == selected) {
					multiplayer = true;
					break;
				}
				selected = button;
				return;
			} else if (button.hovered && button.serverData == null) {
				clickedButton = button.text;
			}
		}

		if (multiplayer) {
			mc.displayGuiScreen(new GuiConnecting(this, mc, selected.serverData));
			return;
		}

		if (clickedButton.equals("")) {
			selected = null;
			return;
		}

		switch (clickedButton) {
			case "Remove":
				if (selected != null) {
					currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Confirm removing server", 300, 200);
					button(currentWindow, mc.displayWidth / 2, mc.displayHeight / 2);
				}
				break;
			case "Add":
				currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Add server", 300, 300);
				currentWindow.buttons.add(new TextButton("Server name", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 70, 280, 40, false));
				currentWindow.buttons.add(new TextButton("Server address", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 20, 280, 40, false));
				currentWindow.buttons.add(new ButtonImpl("Add", mc.displayWidth / 2 - 140, mc.displayHeight / 2 + 100, 100, 40, 5));
				currentWindow.buttons.add(new ButtonImpl("Cancel", mc.displayWidth / 2 + 40, mc.displayHeight / 2 + 100, 100, 40, 5));
				break;
			case "Back":
				animation = 0;
				start = System.currentTimeMillis();
				toShow = parent;
				break;
			case "Direct connect":
				currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Direct connect", 300, 200);
				currentWindow.buttons.add(new TextButton("Server address", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 30, 280, 40, false));
				currentWindow.buttons.add(new ButtonImpl("Connect", mc.displayWidth / 2 - 140, mc.displayHeight / 2 + 50, 100, 40, 5));
				currentWindow.buttons.add(new ButtonImpl("Cancel", mc.displayWidth / 2 + 40, mc.displayHeight / 2 + 50, 100, 40, 5));
				break;
			case "Join":
				if (selected == null) return;
				mc.displayGuiScreen(new GuiConnecting(this, mc, selected.serverData));
				break;
			case "Edit":
				if (selected == null) return;

				currentWindow = new best.azura.client.impl.ui.gui.impl.Window("Edit server", 300, 300);
				currentWindow.buttons.add(new TextButton("Server name", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 70, 280, 40, false));
				currentWindow.buttons.add(new TextButton("Server address", mc.displayWidth / 2 - 140, mc.displayHeight / 2 - 20, 280, 40, false));
				currentWindow.buttons.add(new ButtonImpl("Edit", mc.displayWidth / 2 - 140, mc.displayHeight / 2 + 100, 100, 40, 5));
				currentWindow.buttons.add(new ButtonImpl("Cancel", mc.displayWidth / 2 + 40, mc.displayHeight / 2 + 100, 100, 40, 5));

				((TextButton) currentWindow.buttons.stream().filter(button -> button instanceof TextButton && ((TextButton)button).text.equalsIgnoreCase("Server name")).findAny().get()).fontText = selected.serverData.serverName;
				((TextButton) currentWindow.buttons.stream().filter(button -> button instanceof TextButton && ((TextButton)button).text.equalsIgnoreCase("Server address")).findAny().get()).fontText = selected.serverData.serverIP;
				break;
			case "Change version":
				currentWindow = new Window("Change version", 400, 400);
				int count = 0, countX = 0;
				for (ProtocolCollection collection : ProtocolCollection.values()) {
					ProtocolVersion version = collection.getVersion();
					if (version == ProtocolVersion.unknown) continue;
					currentWindow.buttons.add(new ButtonImpl(version.getName(), mc.displayWidth / 2 - 120 + countX * 101, mc.displayHeight / 2 - 150 + count * 21, 100, 20));
					count++;
					if (count * 20 > 300) {
						count = 0;
						countX++;
					}
				}
				currentWindow.buttons.add(new ButtonImpl("Cancel", mc.displayWidth / 2 + 90, mc.displayHeight / 2 + 150, 100, 40, 5));
				break;
		}

	}

	public static void button(Window currentWindow, int i, int i2) {
		ButtonImpl button;
		currentWindow.buttons.add(button = new ButtonImpl("Remove", i - 140, i2 + 50, 100, 40, 5));
		button.normalColor = new Color(100, 50, 50, 50);
		button.hoverColor = new Color(100, 50, 50, 100);
		currentWindow.buttons.add(new ButtonImpl("Cancel", i + 40, i2 + 50, 100, 40, 5));
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		region.handleMouseInput();
	}

	@Override
	public void confirmClicked(boolean result, int id) {
		initGui();
		mc.displayGuiScreen(this);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (currentWindow != null) currentWindow.keyTyped(typedChar, keyCode);
		if (selected != null && keyCode == Keyboard.KEY_DELETE && selected.serverData != null) {
			buttons.remove(selected);
			saveServerList();
			selected = null;
		}
	}

	public void saveServerList() {
		try {
			NBTTagList nbttaglist = new NBTTagList();

			for (ButtonImpl button : buttons) {
				if (button.serverData != null) {
					if (button.serverData.serverName.equals("§6FEATURED No Rules")) continue;
					nbttaglist.appendTag(button.serverData.getNBTCompound());
				}
			}

			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setTag("servers", nbttaglist);
			CompressedStreamTools.safeWrite(nbttagcompound, new File(this.mc.mcDataDir, "servers.dat"));
		} catch (Exception exception) {
			logger.error("Couldn't save server list", exception);
		}
	}

	public ArrayList<ServerData> loadServerList() {
		try {
			ArrayList<ServerData> servers = new ArrayList<>();
			NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));

			if (nbttagcompound == null) {
				return new ArrayList<ServerData>();
			}

			NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);
			servers.add(new ServerData("§6FEATURED No Rules", "play.norules.wtf:25565", false));

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
			}
			return servers;
		} catch (Exception exception) {
			logger.error("Couldn't load server list", exception);
		}
		return new ArrayList<ServerData>();
	}


}
