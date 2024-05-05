package artemis.youtube;

import org.lwjgl.input.Keyboard;

import artemis.ui.AltLoginThread;
import artemis.ui.PasswordField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public final class YoutubeManager extends GuiScreen{

	Minecraft mc = Minecraft.getMinecraft();
	 private GuiScreen previousScreen;
	
	 public void Youtube(GuiScreen previousScreen) {
	        this.previousScreen = previousScreen;
	 }
    public void drawScreen(int x2, int y2, float z2) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.mc.fontRendererObj, "Youtube:", width / 2, 20, -1);
        this.drawString(this.mc.fontRendererObj, "G8LOL - Coding and More", width / 2 - 50, 60, -1);
       
        super.drawScreen(x2, y2, z2);
    }
    
    public void initGui() {
        int var3 = height / 4 + 24;
        this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
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

