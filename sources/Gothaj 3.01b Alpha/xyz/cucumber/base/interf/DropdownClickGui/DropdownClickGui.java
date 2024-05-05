package xyz.cucumber.base.interf.DropdownClickGui;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import xyz.cucumber.base.Client;
import xyz.cucumber.base.interf.DropdownClickGui.ext.DropdownButton;
import xyz.cucumber.base.interf.DropdownClickGui.ext.DropdownCategory;
import xyz.cucumber.base.module.Category;

public class DropdownClickGui extends GuiScreen{
	
	private ArrayList<DropdownButton> buttons = new ArrayList();
	
	public DropdownClickGui() {
		
		double centerX = 20;
		double centerY = 20;
		buttons.clear();
		int i = 0;
		for(Category c : Category.values()) {
			DropdownCategory category = new DropdownCategory(c);
			category.getPosition().setX(centerX+122*i);
			category.getPosition().setY(centerY);
			category.getPosition().setHeight(20);
			buttons.add(category);
			i++;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(DropdownButton b : buttons) {
			b.draw(mouseX, mouseY);
		}
		Client.INSTANCE.getConfigManager().draw(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		Client.INSTANCE.getConfigManager().onKey(keyCode, typedChar);
		if(Client.INSTANCE.getConfigManager().open) return;
		for(DropdownButton b : buttons) {
			b.onKey(typedChar, keyCode);
		}
		
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		Client.INSTANCE.getConfigManager().onClick(mouseX, mouseY, mouseButton);
		if(Client.INSTANCE.getConfigManager().open) return;
		for(DropdownButton b : buttons) {
			b.onClick(mouseX, mouseY, mouseButton);
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		Client.INSTANCE.getConfigManager().onRelease(mouseX, mouseY, state);
		for(DropdownButton b : buttons) {
			b.onRelease(mouseX, mouseY, state);
		}
		super.mouseReleased(mouseX, mouseY, state);
	}

	@Override
	public void initGui() {
		Client.INSTANCE.getConfigManager().initGui();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
