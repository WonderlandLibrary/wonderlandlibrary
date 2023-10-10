package today.flux.gui.other;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by John on 2016/11/24.
 */
public class GuiProxy extends GuiScreen {
	public static InetSocketAddress address;
	private static final Minecraft mc = Minecraft.getMinecraft();

	private GuiScreen prevMenu;

	private GuiTextField textbox1;
	private String errorMsg;

	public GuiProxy(final GuiScreen parent) {
		this.prevMenu = parent;
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		(this.textbox1 = new GuiTextField(0, mc.fontRendererObj, this.width / 2 - 100,
				this.height / 4 + 20, 200, 20)).setMaxStringLength(250);
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 70, "Set"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 95, "Done"));

		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void actionPerformed(final GuiButton button) throws IOException {
		switch (button.id) {
		case 0: {
			if (!StringUtils.isNullOrEmpty(this.textbox1.getText())) {
				try {
					String ip = this.textbox1.getText().split(":")[0];
					String portSring = this.textbox1.getText().split(":")[1];


					address = new InetSocketAddress(ip, Integer.parseInt(portSring));

					errorMsg = null;
				} catch (Exception e) {
					errorMsg = e.getMessage();
				}
			} else {
				address = null;
			}
			break;
		}
		case 2: {
			this.mc.displayGuiScreen(this.prevMenu);
			break;
		}
		}
	}

	@Override
	protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
		this.textbox1.textboxKeyTyped(typedChar, keyCode);
		if (keyCode == 1) {
			this.mc.displayGuiScreen(this.prevMenu);
		}
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		this.textbox1.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		this.textbox1.updateCursorCounter();
		super.updateScreen();
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(mc.fontRendererObj, "Configure SOCKS5 proxy", this.width / 2, 48,
				-1);
		this.textbox1.drawTextBox();
		this.drawString(mc.fontRendererObj, "IP:Port", this.width / 2 - 100, this.height / 4 + 8,
				-1);
		String currentProxy = (address == null) ? "NULL" : (address.getHostString() + ":" + address.getPort());
		this.drawCenteredString(mc.fontRendererObj, "Current proxy: §a" + currentProxy,
				this.width / 2, 12, -1);
		if (!StringUtils.isNullOrEmpty(errorMsg)) {
			this.drawCenteredString(mc.fontRendererObj, errorMsg, this.width / 2, 24, 16711680);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
