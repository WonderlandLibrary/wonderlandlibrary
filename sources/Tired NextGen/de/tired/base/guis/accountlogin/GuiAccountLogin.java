package de.tired.base.guis.accountlogin;

import de.tired.util.render.Translate;


import de.tired.base.interfaces.FHook;
import de.tired.base.interfaces.IHook;
import de.tired.util.render.shaderloader.ShaderRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class GuiAccountLogin extends GuiScreen implements IHook {

	private GuiTextField emailField;
	private GuiTextField passwordField;

	private AccountLoginThread thread;
	private Translate translate;

	public GuiAccountLogin() {
		translate = new Translate(0, 0);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
			case 1:
				mc.displayGuiScreen(new GuiMainMenu());
				break;
			case 0:
				this.thread = new AccountLoginThread(emailField.getText(), passwordField.getText());
				this.thread.start();
				break;
		}
		super.actionPerformed(button);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		final ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());

		translate.interpolate(resolution.getScaledWidth(), resolution.getScaledHeight(), 9);

		GL11.glPushMatrix();

		GL11.glTranslatef(resolution.getScaledWidth() / 2, resolution.getScaledHeight() / 2, 0);
		GL11.glScaled(translate.getX() / resolution.getScaledWidth(), translate.getY() / resolution.getScaledHeight(), 0);
		GL11.glTranslatef(-resolution.getScaledWidth() / 2, -resolution.getScaledHeight() / 2, 0);
		GlStateManager.pushMatrix();
		ShaderRenderer.renderBG();
		Gui.drawRect(0, 0, width, height, new Color(22, 22, 22, 213).getRGB());
		GlStateManager.popMatrix();
		ShaderRenderer.stopBlur();
		int defaultHeight = this.height / 4 + 48;
		double widthA = 140;
		Gui.drawRect(width / 2 - widthA, height / 4 + 35, width / 2 + widthA, height / 4 + 175, Integer.MIN_VALUE);
		Gui.drawRect(width / 2 - widthA, height / 4 + 25, width / 2 + widthA, height / 4 + 175, Integer.MIN_VALUE);
		this.emailField.drawTextBox();
		this.passwordField.drawTextBox();
		FHook.fontRenderer.drawStringWithShadow2(this.thread == null ? "Waiting.." : this.thread.getStatus(), 3, 3, -1);
		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui() {
		int defaultHeight = this.height / 4 + 48;
		this.translate = new Translate(0, 0);
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 24 + 72 + 12, "Login"));
		this.buttonList.add(new GuiButton(12, this.width / 2 - 100, this.height / 4 + 24 + 142 + 12, "TheAlteningLogin"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 24 + 72 + 12 + 24, "Back"));
		this.emailField = new GuiTextField(this.height / 4 + 24, this.mc.fontRendererObj, this.width / 2 - 100, defaultHeight - 5, 200, 20);
		this.passwordField = new GuiTextField(this.height / 4 + 24 + 30, this.mc.fontRendererObj, this.width / 2 - 100, defaultHeight + 27, 200, 20);
		this.emailField.setFocused(true);
		passwordField.setCensored(true);
		Keyboard.enableRepeatEvents(true);
		super.initGui();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.emailField.mouseClicked(mouseX, mouseY, mouseButton);
		this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) return;
		try {
			super.keyTyped(typedChar, keyCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (typedChar == '\t') {
			if (!emailField.isFocused() && !passwordField.isFocused()) {
				emailField.setFocused(true);
			} else {
				emailField.setFocused(passwordField.isFocused());
				passwordField.setFocused(!emailField.isFocused());
			}
		}
		if (typedChar == '\r') {
			this.actionPerformed(this.buttonList.get(0));
		}
		emailField.textboxKeyTyped(typedChar, keyCode);
		passwordField.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}
}
