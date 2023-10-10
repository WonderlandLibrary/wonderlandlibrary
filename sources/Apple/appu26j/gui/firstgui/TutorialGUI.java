package appu26j.gui.firstgui;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class TutorialGUI extends GuiScreen
{
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("panorama.png"));
        this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        float width = this.width / 2;
        float height = this.height / 2;
        this.drawRect(width - 200, 0, width + 200, this.height, new Color(0, 0, 25, 75).getRGB());
        this.drawStringWithShadow("How To Use Apple Client", width - (this.getStringWidth("How To Use Apple Client", 16) / 2), 10, 16, -1);
        ArrayList<String> items = new ArrayList<>();
        items.add("1. Press the right shift key on your keyboard to open the GUI.");
        items.add("2. To go to the mods menu, click on the \"SETTINGS\" button in the GUI.");
        items.add("3. To turn on or off a mod, left click on the mod.");
        items.add("4. To get the settings of a mod, right click on the mod.");
        items.add("5. To change the slider value of a setting, hover on it and scroll up/down.");
        items.add("6. To change the mode value of a setting, left or right click on the setting.");
        items.add("7. To change the color value of a setting, hover on it and scroll up/down.");
        items.add("8. To exit the settings screen and the mod menu, press the ESC key.");
        items.add("9. To search for a mod, left click on the search box and begin typing.");
        items.add("10. To move an enabled mod with a GUI, open the GUI and drag the mod.");
        items.add("11. To open the music player, press the M key on your keyboard.");
        items.add("12. To open the profile manager, press the P key on your keyboard.");
        items.add("13. Have fun!");
        
        for (int i = 0; i < items.size(); i++)
        {
            String item = items.get(i);
            this.drawStringWithShadow(item, width - 190, 40 + (i * 15), new Color(200, 200, 200).getRGB());
        }
        
        this.drawStringWithShadow("Press ESC to display the main menu", width - (this.getStringWidth("Press ESC to display the main menu", 12) / 2), this.height - 30, 12, -1);
    }
}
