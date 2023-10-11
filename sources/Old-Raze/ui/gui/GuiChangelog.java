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

public class GuiChangelog extends GuiScreen {

	private GuiScreen parent;

	public GuiChangelog() {}

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
	    FontRenderer fr = mc.fontRendererObj;

	    int greenColor = 0x00FF00, yellowColor = 0xFFFF00, redColor = 0xFF0000;
        int fixedHeight = ((5 + this.fontRendererObj.FONT_HEIGHT) * 2) + 2;
	    String title = RazeClient.INSTANCE.getName() + " " +RazeClient.INSTANCE.getVersion() + " | What's new?";
	    
	    GlStateManager.pushMatrix();
	    GlStateManager.scale(1, 1, 1);
        
        drawString(this.fontRendererObj, title, (this.width - this.fontRendererObj.getStringWidth(title)) / 2, 30, -1);
        drawString(this.fontRendererObj, "[+] RAZE DEV BUILD", (this.width - this.fontRendererObj.getStringWidth("[+] RAZE DEV BUILD")) / 2, 41, greenColor);
	    
	    GlStateManager.popMatrix();

	    super.drawScreen(mouseX, mouseY, partialTicks);
	}


}
