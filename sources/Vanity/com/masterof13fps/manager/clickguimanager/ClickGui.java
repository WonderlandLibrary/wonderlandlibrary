package com.masterof13fps.manager.clickguimanager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import com.masterof13fps.Client;
import com.masterof13fps.features.modules.impl.gui.ClickGUI;
import com.masterof13fps.manager.clickguimanager.components.Frame;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

/**
 * @author sendQueue <Vinii>
 *
 *         Further info at Vinii.de or github@vinii.de, file created at 11.11.2020. 
 *         Use is only authorized if given credit!
 * 
 */
public class ClickGui extends GuiScreen {

	public static int compID = 0;

	private ArrayList<com.masterof13fps.manager.clickguimanager.components.Frame> frames = new ArrayList<com.masterof13fps.manager.clickguimanager.components.Frame>();
	//dont change
	private final UnicodeFontRenderer fr = Client.main().fontMgr().font("Arial", 12, Font.PLAIN);
	/**
	 * 
	 */
	public ClickGui() {
		compID = 0;
		
	}

	protected void addFrame(com.masterof13fps.manager.clickguimanager.components.Frame frame) {
		if (!frames.contains(frame)) {
			frames.add(frame);
		}
	}

	@Override
	public void initGui() {
		if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
			if (mc.entityRenderer.theShaderGroup != null) {
				mc.entityRenderer.theShaderGroup.deleteShaderGroup();
			}
			if (Client.main().setMgr().settingByName("Blur", Client.main().modMgr().getModule(ClickGUI.class)).isToggled()) {
				mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
			}
		}

		for (com.masterof13fps.manager.clickguimanager.components.Frame frame : frames) {
			frame.initialize();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (com.masterof13fps.manager.clickguimanager.components.Frame frame : frames) {
			frame.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);

		for (com.masterof13fps.manager.clickguimanager.components.Frame frame : frames) {
			frame.keyTyped(keyCode, typedChar);
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			mc.displayGuiScreen(null);
			Client.main().modMgr().getModule(ClickGUI.class).setState(false);
		}
	}

	public ArrayList<com.masterof13fps.manager.clickguimanager.components.Frame> getFrames() {
		return frames;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (Frame frame : frames) {
			frame.render(mouseX, mouseY);
		}
	}
}
