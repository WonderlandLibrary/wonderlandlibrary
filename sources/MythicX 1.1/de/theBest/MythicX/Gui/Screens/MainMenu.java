package de.theBest.MythicX.Gui.Screens;

import de.theBest.MythicX.MythicX;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class MainMenu extends GuiScreen {

    public MainMenu() {

    }

    public void initGui(){
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks){

        this.drawGradientRect(0, height-100,width,height,0x000000000,0xff000000);

        this.drawString(mc.fontRendererObj, MythicX.name, (int) (width/2f), (int) (height/2f - mc.fontRendererObj.FONT_HEIGHT/2f),-1);
    }

    public void mouseClicked(int mouseX, int mouseY, int button){

    }

    public void onGuiClosed(){

    }
}
