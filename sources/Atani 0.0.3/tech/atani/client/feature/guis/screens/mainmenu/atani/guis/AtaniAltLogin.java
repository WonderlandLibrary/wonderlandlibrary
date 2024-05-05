package tech.atani.client.feature.guis.screens.mainmenu.atani.guis;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class AtaniAltLogin extends GuiScreen {
	private GuiPasswordField password;
	private final GuiScreen previousScreen;
	private AltLoginThread thread;
	private GuiTextField username;
	private GuiTextField combined;

	public AtaniAltLogin(final GuiScreen previousScreen) {
		this.previousScreen = previousScreen;
	}

	@Override
	protected void actionPerformed(final GuiButton button) {
		switch (button.id) {
		case 1: {
			mc.displayGuiScreen(this.previousScreen);
			break;
		}
		case 0: {
			if (this.combined.getText().isEmpty()) {
				this.thread = new AltLoginThread(this.username.getText(), this.password.getText(), true);
			} else if (!this.combined.getText().isEmpty() && this.combined.getText().contains(":")) {
				final String u = this.combined.getText().split(":")[0];
				final String p = this.combined.getText().split(":")[1];
				this.thread = new AltLoginThread(u.replaceAll(" ", ""), p.replaceAll(" ", ""), true);
			} else {
				this.thread = new AltLoginThread(this.username.getText(), this.password.getText(), true);
			}
			this.thread.start();
			break;
		}
		}
	}

	@Override
	public void drawScreen(final int x, final int y, final float z) {
		this.drawDefaultBackground();
		this.username.drawTextBox();
		this.password.drawTextBox();
		this.combined.drawTextBox();
		mc.fontRendererObj.drawCenteredString("Alt Login", this.width / 2, 20, -1);
		mc.fontRendererObj.drawCenteredString((this.thread == null) ? "\u00A7eWaiting..." : this.thread.getStatus(),
				this.width / 2, 29, -1);
		if (this.username.getText().isEmpty()) {
			mc.fontRendererObj.drawStringWithShadow("Username / E-Mail", (float) (this.width / 2 - 96), 66.0f,
					-7829368);
		}
		if (this.password.getText().isEmpty()) {
			mc.fontRendererObj.drawStringWithShadow("Password", (float) (this.width / 2 - 96), 106.0f, -7829368);
		}
		if (this.combined.getText().isEmpty()) {
			mc.fontRendererObj.drawStringWithShadow("Email:Password", (float) (this.width / 2 - 96), 146.0f, -7829368);
		}
		super.drawScreen(x, y, z);
	}

	@Override
	public void initGui() {
		final int var3 = this.height / 4 + 24;
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
		this.username = new GuiTextField(1, mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
		this.password = new GuiPasswordField(mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
		this.combined = new GuiTextField(var3, mc.fontRendererObj, this.width / 2 - 100, 140, 200, 20);
		this.username.setFocused(true);
		this.username.setMaxStringLength(200);
		this.password.func_146203_f(200);
		this.combined.setMaxStringLength(200);
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void keyTyped(final char character, final int key) {
		try {
			super.keyTyped(character, key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (character == '\t'
				&& (this.username.isFocused() || this.combined.isFocused() || this.password.isFocused())) {
			this.username.setFocused(!this.username.isFocused());
			this.password.setFocused(!this.password.isFocused());
			this.combined.setFocused(!this.combined.isFocused());
		}
		if (character == '\r') {
			this.actionPerformed(this.buttonList.get(0));
		}
		this.username.textboxKeyTyped(character, key);
		this.password.textboxKeyTyped(character, key);
		this.combined.textboxKeyTyped(character, key);
	}

	@Override
	protected void mouseClicked(final int x, final int y, final int button) {
		try {
			super.mouseClicked(x, y, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username.mouseClicked(x, y, button);
		this.password.mouseClicked(x, y, button);
		this.combined.mouseClicked(x, y, button);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void updateScreen() {
		this.username.updateCursorCounter();
		this.password.updateCursorCounter();
		this.combined.updateCursorCounter();
	}
}
