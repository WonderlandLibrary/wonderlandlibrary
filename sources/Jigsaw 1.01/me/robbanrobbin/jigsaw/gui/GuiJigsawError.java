package me.robbanrobbin.jigsaw.gui;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw.ErrorState;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiJigsawError extends GuiScreen {

	WaitTimer timer = new WaitTimer();
	Exception e;
	ErrorState errorState;
	Module module;
	int repetitions = 1500;
	boolean render;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuiJigsawError() {

	}

	public boolean onError(Exception e, ErrorState errorState, Module module) {
		this.module = module;
		this.e = e;
		this.errorState = errorState;
		render = true;
		reset();
		return true;
	}

	public void reset() {
		timer.reset();
	}

	public void render() {

		if (!render) {
			return;
		}
		if (timer.hasTimeElapsed(15000, true)) {
			render = false;
			return;
		}
		String toDraw = e.toString();
		StackTraceElement[] STElement = e.getStackTrace();
		int left = mc.displayWidth / 3 / mc.gameSettings.guiScale * 2;
		int top = mc.displayHeight / 5 * 2;
		int right = mc.displayWidth / mc.gameSettings.guiScale + 1;
		int bottom = mc.displayHeight / mc.gameSettings.guiScale + 1;
		byte yoffset = 30;

		drawGradientRect(left - 2, top - 2, right, bottom, 0xffff7070, 0xffff5050);
		// drawGradientRect(left - 2, top - 2, right, bottom, 0x00000000,
		// 0x30000000);
		mc.fontRenderer.drawStringWithShadow("Error while " + errorState.getDisplayText() + "!", left, top + 2,
				0xffffffff);
		GL11.glScaled(0.5, 0.5, 0.5);
		if (module == null) {
			repetitions -= 10;
			mc.fontRenderer.drawString("Module null", left * 2, top * 2, 0xffffffff);
		} else {
			mc.fontRenderer.drawString("In module " + module.getName() + "...", left * 2, top * 2 + yoffset,
					0xffffffff);
		}

		mc.fontRenderer.drawString(toDraw, (left) * 2, (top) * 2 + 14 + yoffset, 0xffffffff);
		if (STElement.length != 0) {
			mc.fontRenderer.drawString("In class: " + STElement[0].getFileName(), left * 2, top * 2 + 28 + yoffset,
					0xffffffff);

			mc.fontRenderer.drawString("In method: " + STElement[0].getMethodName(), left * 2,
					top * 2 + 42 + yoffset, 0xffffffff);

			mc.fontRenderer.drawString("At line: " + STElement[0].getLineNumber(), left * 2, top * 2 + 56 + yoffset,
					0xffffffff);

			mc.fontRenderer.drawString(e.getMessage(), left * 2, top * 2 + 70 + yoffset, 0xffffffff);
		} else {
			mc.fontRenderer.drawString("Stack trace empty!", left * 2, top * 2 + 24 + yoffset, 0xffffffff);
		}
		if (module != null) {
			mc.fontRenderer.drawString(module.getName() + " was disabled!", left * 2, top * 2 + 84 + yoffset,
					0xffffffff);
		}
		GL11.glScaled(2, 2, 2);
	}
}
