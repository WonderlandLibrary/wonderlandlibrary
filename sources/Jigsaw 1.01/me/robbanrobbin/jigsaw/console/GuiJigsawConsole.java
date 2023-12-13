package me.robbanrobbin.jigsaw.console;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.TickTimer;
import me.robbanrobbin.jigsaw.client.commands.CommandSuggestion;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.client.gui.GuiScreen;

public class GuiJigsawConsole extends GuiScreen {
	
	private boolean requireScroll = false;
	private int scroll = 0;
	
	private boolean repeatEvents = false;
	private int boxHeight = 0;
	
	private TickTimer cursorTimer = new TickTimer();
	private boolean showCursor = false;
	
	@Override
	public void initGui() {
		repeatEvents = Keyboard.areRepeatEventsEnabled();
		Keyboard.enableRepeatEvents(true);
		scroll = 0;
		super.initGui();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if(keyCode == 28) {
			Jigsaw.getConsoleManager().tryRunCommand(Jigsaw.getConsoleManager().typed);
			Jigsaw.getConsoleManager().resetTyped();
			Jigsaw.getConsoleManager().showingResults = true;
			return;
		}
		Jigsaw.getConsoleManager().showingResults = false;
		Jigsaw.getCommandManager().clearResults();
		Jigsaw.getConsoleManager().keyTyped(typedChar, keyCode);
		
		cursorTimer.ticks = -10;
		showCursor = true;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		int color = 0xa1000000;

		int textXMargin = 8;
		int textYMargin = 4;
		
		int left = width / 5;
		int right = width - width / 5;
		int top = 0;
		int bottom = fontRenderer.FONT_HEIGHT + textYMargin * 2 - 1;
		
		int boxWidth = right - left;
		int boxHeight = bottom - top;
		
		drawBox(left, top, right, bottom, color);
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GuiUtils.scissorHelper(left, top, right, bottom);
		
		Fonts.font19.drawStringWithShadow(Jigsaw.getConsoleManager().typed + (showCursor ? "_" : ""), left + textXMargin, textYMargin, GuiUtils.getPreferredFontARGBColor());
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		int y = bottom + textYMargin;
		
		if(Jigsaw.getConsoleManager().showingResults) {
			
			requireScroll = (boxHeight = drawBigBox(left, y + scroll, right, Jigsaw.getCommandManager().getResults(), textXMargin, textYMargin, boxHeight, color)) > height - bottom;
			
		}
		else {
			
			for(CommandSuggestion suggestion : Jigsaw.getCommandManager().getSuggestions(Jigsaw.getConsoleManager().typed)) {
				
				drawBox(left, y, right, y + bottom, color);
				
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				GuiUtils.scissorHelper(left, y, right, y + bottom);
				drawString(Fonts.font19, "§j" + suggestion.command, left + textXMargin / 2, y + textYMargin, GuiUtils.getPreferredFontARGBColor());
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				
				y += boxHeight + 1;
			}
			
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private void drawBox(int left, int top, int right, int bottom, int color) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GuiUtils.scissorHelper(left, top, right, bottom);
		
		GuiUtils.drawBlurBuffer(true);
		GuiUtils.enableDefaults();
		
//		drawRect(left, top, right, bottom, color);
		GuiUtils.setColor(ClientSettings.getBackGroundGuiColor());
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(left, top);
			GL11.glVertex2d(right, top);
			GL11.glVertex2d(right, bottom);
			GL11.glVertex2d(left, bottom);
		}
		GL11.glEnd();
		
		drawHorizontalLine(left, right - 1, top, 0x6fffffff);
		drawHorizontalLine(left, right - 1, bottom - 1, 0x6fffffff);
		
		drawVerticalLine(left, top, bottom - 1, 0x6fffffff);
		drawVerticalLine(right - 1, top, bottom - 1, 0x6fffffff);
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	private int drawBigBox(int left, int top, int right, ArrayList<String> text, int textXMargin, int textYMargin, int height, int color) {
		
		int bottom = top;
		
		int lineHeightCompensation = 3;
		
		for(String s : text) {
//			drawString(fontRenderer, s, left + textXMargin / 2, bottom + textYMargin, 0xffffffff);
			
			bottom += height - lineHeightCompensation;
		}
		
		bottom += lineHeightCompensation;
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GuiUtils.scissorHelper(left, top, right, bottom);
		
		GuiUtils.drawBlurBuffer(true);
		GuiUtils.enableDefaults();
		
//		drawRect(left, top, right, bottom, color);
		GuiUtils.setColor(ClientSettings.getBackGroundGuiColor());
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(left, top);
			GL11.glVertex2d(right, top);
			GL11.glVertex2d(right, bottom);
			GL11.glVertex2d(left, bottom);
		}
		GL11.glEnd();
		
		drawHorizontalLine(left, right - 1, top, 0x6fffffff);
		drawHorizontalLine(left, right - 1, bottom - 1, 0x6fffffff);
		
		drawVerticalLine(left, top, bottom - 1, 0x6fffffff);
		drawVerticalLine(right - 1, top, bottom - 1, 0x6fffffff);
		
		bottom = top;
		
		for(String s : text) {
			drawString(Fonts.font19, s, left + textXMargin / 2, bottom + textYMargin, GuiUtils.getPreferredFontARGBColor());
			
			bottom += height - lineHeightCompensation;
		}
		bottom += lineHeightCompensation;
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		return bottom;
		
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(repeatEvents);
		super.onGuiClosed();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			
			if (i > 1) {
				i = 1;
			}

			if (i < -1) {
				i = -1;
			}
			
			i*= 20;
			
			if(i < 0 && !requireScroll) {
				return;
			}
			
			this.scroll += i;
			
			if(this.scroll > 0) {
				this.scroll = 0;
			}
			
		}
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		cursorTimer.tick();
		
		if(cursorTimer.hasTicksPassed(10, true)) {
			showCursor = !showCursor;
		}
	}
	
}
