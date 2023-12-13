package me.robbanrobbin.jigsaw.client.gui.tab;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import net.minecraft.client.renderer.GlStateManager;

public class TabGuiTitle extends TabGuiItem {
	
	public String title = null;
	
	public TabGuiContainer nextContainer;
	
	public boolean hasnextContainer() {
		return nextContainer != null;
	}

	@Override
	public int getWidth() {
		return TabGuiItem.fontRenderer.getStringWidth(title) + 10;
	}

	@Override
	public int getHeight() {
		return TabGuiItem.fontRenderer.FONT_HEIGHT + 2;
	}
	
	@Override
	public void update() {
		super.update();
		if(nextContainer == null) {
			return;
		}
		nextContainer.x = this.x + this.parent.width + 2;
		nextContainer.y = this.y;
		nextContainer.update();
	}
	
	@Override
	public void setValues() {
		super.setValues();
		if(nextContainer != null) {
			nextContainer.setValues();
			return;
		}
	}
	
	@Override
	public void render() {
		super.render();
		int drawWidth = parent.width;
		if(TabGui.novitex) {
			drawWidth = width - 10;
		}
		GlStateManager.enableAlpha();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableCull();
		
		GuiUtils.setColor(ClientSettings.getBackGroundGuiColor());
		if(TabGui.novitex) {
			GL11.glColor4f(0.7f, 0.2f, 0.2f, 0.5f);
			if(selected) {
				GL11.glColor4f(0.7f, 0.2f, 0.2f, 1f);
			}
		}
		if(selected) {
			GuiUtils.setColor(ClientSettings.getForeGroundGuiColor());
		}
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(x, y);
			GL11.glVertex2d(x, y + height);
			GL11.glVertex2d(x + drawWidth, y + height);
			GL11.glVertex2d(x + drawWidth, y);
		}
		GL11.glEnd();
		
		GlStateManager.enableBlend();
		GlStateManager.enableTexture2D();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		boolean toggled = true;
		if(Jigsaw.isModuleName(title) && !Jigsaw.getModuleByName(title).isToggled()) {
			toggled = false;
		}
		if(TabGui.novitex) {
			TabGuiItem.fontRenderer.drawString(title, x + 5, y, toggled ? 0xffffff : 0xbfbfbf);
		}
		else {
			TabGuiItem.fontRenderer.drawStringWithShadow(title, x + 2, y + 1.5f, toggled ? GuiUtils.getPreferredFontARGBColor() : 0xbfbfbf);
		}
		
		if(maximised && hasnextContainer()) {
			nextContainer.render();
		}
		if(maximised && !hasnextContainer()) {
			Jigsaw.getModuleByName(this.title).toggle();
			maximised = false;
		}
	}
	
}
