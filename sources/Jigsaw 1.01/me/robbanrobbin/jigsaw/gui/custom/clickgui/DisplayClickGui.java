package me.robbanrobbin.jigsaw.gui.custom.clickgui;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.font.Fonts;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class DisplayClickGui extends GuiScreen {
	
	private int lastX;
	private int lastY;
	
	private int lastRenderX;
	private int lastRenderY;
	
	private boolean dragged = false;
	
	private boolean mouseDown = false;
	
	private boolean inMenu = false;
	
	public DisplayClickGui(boolean b) {
		this.inMenu = b;
	}

	@Override
	public void initGui() {
		super.initGui();
		if (!inMenu) {
			
		}
		else {
			this.buttonList.add(new GuiButton(3299, width - 100, height - 20, 100, 20, "Back"));
		}
		Jigsaw.getClickGuiManager().resetTimes();
		
//		mc.entityRenderer.useShader = true;
//		if(mc.entityRenderer.getShaderGroup() == null || !mc.entityRenderer.getShaderGroup().getShaderGroupName().equals("minecraft:jigsaw/shader/guiblur.json")) {
//			mc.entityRenderer.loadShader(Jigsaw.blurShader);
//		}
		
		Jigsaw.getClickGuiManager().focusedComponent = Jigsaw.getSearchBar();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (!inMenu) {
//			searchBar.keyTyped(typedChar, keyCode);
		}
		if(Jigsaw.getClickGuiManager().focusedComponent != null && Jigsaw.getClickGuiManager().focusedComponent.isEnabled()) {
			Jigsaw.getClickGuiManager().focusedComponent.keyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button.id == 3299) {
			mc.displayGuiScreen(new GuiMainMenu());
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		this.drawRect(0, 0, this.width, this.height, 0x70000000);
        GuiUtils.updateBlurBuffer(true);
		
		Fonts.font18.drawStringWithShadow("§7Type anything to search for a specific hack", 1, height - 26, 0xffffffff);
		Fonts.font18.drawStringWithShadow("§7Right-click a window to maximize/minimize it", 1, height - 17, 0xffffffff);
		Fonts.font18.drawStringWithShadow("§7Right-click a button to show settings for the hack", 1, height - 8, 0xffffffff);
		
		ScaledResolution resolution = new ScaledResolution(mc);
		GlStateManager.scale(2d / resolution.getScaleFactor(), 2d / resolution.getScaleFactor(), 1);
		
		mouseX *= resolution.getScaleFactor() / 2d;
		mouseY *= resolution.getScaleFactor() / 2d;
		
		lastRenderX = mouseX;
		lastRenderY = mouseY;
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(inMenu) {
			drawDefaultBackground();
		}
		
		GuiUtils.partialTicks = partialTicks;
		ClickGuiManager manager = Jigsaw.getClickGuiManager();
		manager.mouseX = mouseX;
		manager.mouseY = mouseY;
		manager.draw();
		
	}
	
	@Override
	public void updateScreen() {

		boolean foundHover = false;
		for(ComponentWindow wind : Lists.reverse(Jigsaw.getClickGuiManager().windows)) {
			if(!wind.isEnabled()) {
				continue;
			}
			if(!foundHover && lastRenderX > wind.getX() && lastRenderX < wind.getX() + wind.getWidth()
					&& lastRenderY > wind.getY() && lastRenderY < wind.getY() + wind.getHeight()) {
				wind.onHover(lastRenderX - wind.getX(), lastRenderY - wind.getY());
				foundHover = true;
			}
			else {
				if(wind.isHovered()) {
					wind.onLostHover(lastRenderX - wind.getX(), lastRenderY - wind.getY());
				}
			}
		}
		
		ClickGuiManager manager = Jigsaw.getClickGuiManager();
		manager.update();
		super.updateScreen();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		ScaledResolution resolution = new ScaledResolution(mc);
		mouseX *= resolution.getScaleFactor() / 2d;
		mouseY *= resolution.getScaleFactor() / 2d;
		mouseDown = true;
		lastX = mouseX;
		lastY = mouseY;
		for(ComponentWindow wind : Lists.reverse(Jigsaw.getClickGuiManager().windows)) {
			if(!wind.isEnabled()) {
				continue;
			}
			if(mouseX > wind.getX() && mouseX < wind.getX() + wind.getWidth()
					&& mouseY > wind.getY() && mouseY < wind.getY() + wind.getHeight()) {
				wind.onClicked(mouseX - wind.getX(), mouseY - wind.getY(), mouseButton);
				break;
			}
		}
		if(Jigsaw.getClickGuiManager().canRemoveOverlayWindow && Jigsaw.getClickGuiManager().lastOverlayWindow != null) {
			Jigsaw.getClickGuiManager().removeOverlayWindow();
		}
		if(!Jigsaw.getClickGuiManager().canRemoveOverlayWindow) {
			Jigsaw.getClickGuiManager().canRemoveOverlayWindow = true;
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		ScaledResolution resolution = new ScaledResolution(mc);
		mouseX *= resolution.getScaleFactor() / 2d;
		mouseY *= resolution.getScaleFactor() / 2d;
		mouseDown = false;
		dragged = false;
		for(ComponentWindow wind : Jigsaw.getClickGuiManager().windows) {
			if(!wind.isEnabled()) {
				continue;
			}
			wind.onReleased(state);
		}
		for(ComponentWindow wind : Lists.reverse(Jigsaw.getClickGuiManager().windows)) {
			if(!wind.isEnabled()) {
				continue;
			}
			if(mouseX > wind.getX() && mouseX < wind.getX() + wind.getWidth()
					&& mouseY > wind.getY() && mouseY < wind.getY() + wind.getHeight()) {
				wind.onReleased(mouseX - wind.getX(), mouseY - wind.getY(), state);
				break;
			}
		}
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		ScaledResolution resolution = new ScaledResolution(mc);
		mouseX *= resolution.getScaleFactor() / 2d;
		mouseY *= resolution.getScaleFactor() / 2d;
		dragged = true;
		for(ComponentWindow wind : Lists.reverse(Jigsaw.getClickGuiManager().windows)) {
			if(!wind.isEnabled()) {
				continue;
			}
			if(mouseX >= wind.getX() && mouseX < wind.getX() + wind.getWidth()
					&& mouseY >= wind.getY() && mouseY < wind.getY() + wind.getHeight()) {
				wind.onDragged(mouseX - lastX, mouseY - lastY, mouseX - wind.getX(), mouseY - wind.getY(), clickedMouseButton);
				break;
			}
		}
		lastX = mouseX;
		lastY = mouseY;
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	
	
	@Override
	public void onGuiClosed() {
		mc.entityRenderer.useShader = false;
		if(Jigsaw.getSearchBar() != null) {
			Jigsaw.getSearchBar().resetTyped();
		}
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
			
			for(ComponentWindow wind : Lists.reverse(Jigsaw.getClickGuiManager().windows)) {
				if(!wind.isEnabled()) {
					continue;
				}
				if(lastRenderX >= wind.getX() && lastRenderX < wind.getX() + wind.getWidth()
						&& lastRenderY >= wind.getY() && lastRenderY < wind.getY() + wind.getHeight()) {
					wind.onScroll(i);
					break;
				}
			}
			
		}
	}
	
}
