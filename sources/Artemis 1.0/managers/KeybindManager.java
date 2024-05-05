package artemis.managers;

import java.awt.List;
import java.util.*; 
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.Category;
import artemis.module;
import artemis.managers.elements.KeyboardButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class KeybindManager extends GuiScreen{
	
	

	Minecraft mc = Minecraft.getMinecraft();
	 private GuiScreen previousScreen;
	 
	 public int placeForKeybindYTop() {
			return 20;
		}
	 public int placeForKeybindYMiddle() {
			return 50;
		}
	 public int placeForKeybindYBottom() {
			return 80;
		}
	
	 public void Youtube(GuiScreen previousScreen) {
	        this.previousScreen = previousScreen;
	 }
    public void drawScreen(int x2, int y2, float z2) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.mc.fontRendererObj, "Keybind Manager", width / 2 - 7, 10, -1);
      
       
        //super.drawScreen(x2, y2, z2);
    }
    
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 48, "Back"));
        for(int i = 1; i < 26; i++){
        this.buttonList.add(new GuiButton(2, width / 2, this.placeForKeybindYTop(), 20, 12, "Q"));
        }
        Keyboard.enableRepeatEvents(true);
    }
protected void actionPerformed(GuiButton button) {
    switch (button.id) {
        case 1: {
            this.mc.displayGuiScreen(this.previousScreen);
            break;
        }
    }
    
  }
}
