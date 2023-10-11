package markgg.ui.gui;

import java.io.IOException;

import markgg.RazeClient;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class GuiCredits extends GuiScreen {

	private GuiScreen parent;
	
	public GuiCredits() {}
	
    public void initGui()
    {
    	buttonList.clear();
        buttonList.add(new GuiButton(1, width / 2 - 100, height - 25, 200, 20, "Back"));
    }
   
    protected void actionPerformed(GuiButton guiButton) throws IOException
    {
        if (guiButton.id == 1)
        {
            mc.displayGuiScreen(parent);
        }
    }
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	this.drawDefaultBackground();

        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0, 2.0, 2.0);
        GlStateManager.popMatrix();

        int fixedHeight = ((5 + this.fontRendererObj.FONT_HEIGHT) * 2) + 2;

        // Devs
        
        drawString(this.fontRendererObj, "Raze Developers:", (this.width - this.fontRendererObj.getStringWidth("Raze Developers:")) / 2, 30, 0xFFE44964);
        drawString(this.fontRendererObj, "MarkGG#8181", (this.width - this.fontRendererObj.getStringWidth("MarkGG#8181")) / 2, 41, -1);
        drawString(this.fontRendererObj, "Kellohylly#2833", (this.width - this.fontRendererObj.getStringWidth("Kellohylly#2833")) / 2, 52, -1);
        drawString(this.fontRendererObj, "Liticane#9797", (this.width - this.fontRendererObj.getStringWidth("Liticane#9797")) / 2, 63, -1);
        drawString(this.fontRendererObj, "Szypko.class#0002", (this.width - this.fontRendererObj.getStringWidth("Szypko.class#0002")) / 2, 74, -1);
        drawString(this.fontRendererObj, "Spinyfish#4884", (this.width - this.fontRendererObj.getStringWidth("Spinyfish#4884")) / 2, 85, -1);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }	
	
}
